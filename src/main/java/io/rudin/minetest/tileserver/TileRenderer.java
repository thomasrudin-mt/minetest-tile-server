package io.rudin.minetest.tileserver;

import static io.rudin.minetest.tileserver.blockdb.tables.Blocks.BLOCKS;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.rudin.minetest.tileserver.config.TileServerConfig;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.rudin.minetest.tileserver.blockdb.tables.records.BlocksRecord;
import io.rudin.minetest.tileserver.service.TileCache;
import io.rudin.minetest.tileserver.util.CoordinateResolver;
import io.rudin.minetest.tileserver.util.CoordinateResolver.MapBlockCoordinateInfo;
import io.rudin.minetest.tileserver.util.WhiteTile;

@Singleton
public class TileRenderer {

	private static final Logger logger = LoggerFactory.getLogger(TileRenderer.class);
	
	@Inject
	public TileRenderer(DSLContext ctx, TileCache cache, MapBlockRenderer blockRenderer, TileServerConfig cfg) {
		this.ctx = ctx;
		this.cache = cache;
		this.blockRenderer = blockRenderer;
		this.cfg = cfg;

		this.yCondition = BLOCKS.POSY.between(cfg.tilesMinY(), cfg.tilesMaxY());
	}

	private final Condition yCondition;

	private final TileServerConfig cfg;

	private final TileCache cache;

	private final DSLContext ctx;

	private final MapBlockRenderer blockRenderer;

	public BufferedImage createTile() {
		return new BufferedImage(CoordinateResolver.TILE_PIXEL_SIZE, CoordinateResolver.TILE_PIXEL_SIZE, BufferedImage.TYPE_INT_RGB);
	}

	private final int DEFAULT_BLOCK_ZOOM = 13;

	public byte[] render(int tileX, int tileY, int zoom) throws IllegalArgumentException, DataFormatException, IOException, ExecutionException {

		//Check binary cache
		if (cache.has(tileX, tileY, zoom)) {
			return cache.get(tileX, tileY, zoom);
		}


		
		if (zoom < DEFAULT_BLOCK_ZOOM) {
			//TODO: fail-fast for regions without map-blocks -> white

			MapBlockCoordinateInfo mapblockInfo = CoordinateResolver.fromTile(tileX, tileY, zoom);
		
			int x1 = mapblockInfo.x;
			int x2 = mapblockInfo.x + (int)mapblockInfo.width;
			
			int z1 = mapblockInfo.z;
			int z2 = mapblockInfo.z + ((int)mapblockInfo.height * -1);
			
			Integer count = ctx
				.select(DSL.count())
				.from(BLOCKS)
				.where(
						BLOCKS.POSX.ge(Math.min(x1, x2))
						.and(BLOCKS.POSX.le(Math.max(x1, x2)))
						.and(BLOCKS.POSZ.ge(Math.min(z1, z2)))
						.and(BLOCKS.POSZ.le(Math.max(z1, z2)))
						.and(yCondition)
				)
				.fetchOne(DSL.count());
		
			if (count == 0) {
				byte[] data = WhiteTile.getPNG();
				cache.put(tileX, tileY, zoom, data);

				return data;
			}
			
		}


		BufferedImage image = renderImage(tileX, tileY, zoom);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(image, "png", output);

		byte[] data = output.toByteArray();

		return data;

	}


	public BufferedImage renderImage(int tileX, int tileY, int zoom) throws IllegalArgumentException, DataFormatException, IOException, ExecutionException {

		//Check if binary cached, use cached version for rendering
		if (cache.has(tileX, tileY, zoom)) {
			byte[] data = cache.get(tileX, tileY, zoom);
			return ImageIO.read(new ByteArrayInputStream(data));
		}

		final int HALF_TILE_PIXEL_SIZE = CoordinateResolver.TILE_PIXEL_SIZE  / 2;


		if (zoom > DEFAULT_BLOCK_ZOOM) {
			//Zoom in

			int nextZoom = zoom - 1;
			int offsetNextZoomX = Math.abs(tileX % 2);
			int offsetNextZoomY = Math.abs(tileY % 2);

			int nextZoomX = (tileX - offsetNextZoomX) / 2;
			int nextZoomY = (tileY - offsetNextZoomY) / 2;

			BufferedImage tile = createTile();
			Graphics2D graphics = tile.createGraphics();

			//Get all quadrants
			BufferedImage image = renderImage(nextZoomX, nextZoomY, nextZoom);
			
			BufferedImage quadrant = image.getSubimage(
					HALF_TILE_PIXEL_SIZE * offsetNextZoomX,
					HALF_TILE_PIXEL_SIZE * offsetNextZoomY,
					HALF_TILE_PIXEL_SIZE,
					HALF_TILE_PIXEL_SIZE
			);
			
			Image scaledInstance = quadrant.getScaledInstance(
					CoordinateResolver.TILE_PIXEL_SIZE,
					CoordinateResolver.TILE_PIXEL_SIZE,
					Image.SCALE_FAST
			);
			
			graphics.drawImage(scaledInstance, 0, 0, CoordinateResolver.TILE_PIXEL_SIZE, CoordinateResolver.TILE_PIXEL_SIZE, null);

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(tile, "png", output);

			byte[] data = output.toByteArray();

			cache.put(tileX, tileY, zoom, data);


			return tile;


		} else if (zoom < DEFAULT_BLOCK_ZOOM) {
			//Zoom out


			BufferedImage tile = createTile();
			//Pack 4 tiles from higher zoom into 1 tile

			int nextZoom = zoom + 1;
			int nextZoomX = tileX * 2;
			int nextZoomY = tileY * 2;

			Graphics2D graphics = tile.createGraphics();


			//upper left
			BufferedImage image = renderImage(nextZoomX, nextZoomY, nextZoom);
			Image scaledInstance = image.getScaledInstance(HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, Image.SCALE_FAST);
			graphics.drawImage(scaledInstance, 0, 0, HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, null);

			//upper right
			image = renderImage(nextZoomX+1, nextZoomY, nextZoom);
			scaledInstance = image.getScaledInstance(HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, Image.SCALE_FAST);
			graphics.drawImage(scaledInstance, HALF_TILE_PIXEL_SIZE, 0, HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, null);

			//lower left
			image = renderImage(nextZoomX, nextZoomY+1, nextZoom);
			scaledInstance = image.getScaledInstance(HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, Image.SCALE_FAST);
			graphics.drawImage(scaledInstance, 0, HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, null);

			//lower right
			image = renderImage(nextZoomX+1, nextZoomY+1, nextZoom);
			scaledInstance = image.getScaledInstance(HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, Image.SCALE_FAST);
			graphics.drawImage(scaledInstance, HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, HALF_TILE_PIXEL_SIZE, null);

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(tile, "png", output);

			byte[] data = output.toByteArray();

			cache.put(tileX, tileY, zoom, data);


			return tile;

		}

		//Default zoom (13 == 1 mapblock with 16px wide blocks)

		MapBlockCoordinateInfo coordinateInfo = CoordinateResolver.fromTile(tileX, tileY, zoom);

		//16x16 mapblocks on a tile
		BufferedImage image = createTile();
		Graphics2D graphics = image.createGraphics();

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, CoordinateResolver.TILE_PIXEL_SIZE, CoordinateResolver.TILE_PIXEL_SIZE);


		int mapblockX = coordinateInfo.x;
		int mapblockZ = coordinateInfo.z;

		long now = System.currentTimeMillis();

		Result<BlocksRecord> blocks = ctx
				.selectFrom(BLOCKS)
				.where(
						BLOCKS.POSX.eq(mapblockX)
								.and(BLOCKS.POSZ.eq(mapblockZ))
								.and(yCondition)
				)
				.orderBy(BLOCKS.POSY.desc())
				.fetch();

		long fetchTime = System.currentTimeMillis() - now;

		logger.debug("Got {} blocks for mapblockX={} mapblockZ={} tileX={} tileY={}",
				blocks.size(),
				mapblockX,
				mapblockZ,
				tileX,
				tileY
		);

		if (!blocks.isEmpty()) {

			now = System.currentTimeMillis();

			blockRenderer.render(mapblockX, mapblockZ, graphics, 16);

			long renderTime = System.currentTimeMillis() - now;

			String msg = "Timings of tile X={} Y={}: fetch={} ms render={} ms";

			if (renderTime < 500 && fetchTime < 500)
				logger.debug(msg, tileX, tileY, fetchTime, renderTime);
			else
				logger.warn(msg, tileX, tileY, fetchTime, renderTime);
		}


		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(image, "png", output);

		byte[] data = output.toByteArray();

		cache.put(tileX, tileY, zoom, data);


		return image;
	}

}

package io.rudin.minetest.tileserver.job;

import com.google.common.util.concurrent.Striped;
import io.rudin.minetest.tileserver.TileRenderer;
import io.rudin.minetest.tileserver.config.Layer;
import io.rudin.minetest.tileserver.config.LayerConfig;
import io.rudin.minetest.tileserver.config.TileServerConfig;
import io.rudin.minetest.tileserver.query.YQueryBuilder;
import io.rudin.minetest.tileserver.util.CoordinateResolver;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.concurrent.locks.ReadWriteLock;

import static io.rudin.minetest.tileserver.blockdb.tables.Blocks.BLOCKS;

@Singleton
public class InitialTileRendererJob implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(InitialTileRendererJob.class);

    @Inject
    public InitialTileRendererJob(DSLContext ctx, TileServerConfig cfg, TileRenderer renderer, YQueryBuilder yQueryBuilder, LayerConfig layerCfg){
        this.ctx = ctx;
        this.renderer = renderer;
        this.yQueryBuilder = yQueryBuilder;
        this.layerCfg = layerCfg;
    }

    private final YQueryBuilder yQueryBuilder;

    private final LayerConfig layerCfg;

    private final DSLContext ctx;

    private final TileRenderer renderer;

    @Override
    public void run() {
        try {
            logger.info("Starting initial tilerendering");

            for (Layer layer: layerCfg.layers) {

                Condition yCondition = yQueryBuilder.getCondition(layer);

                Record2<Integer, Integer> minMaxRecords = ctx
                        .select(DSL.max(BLOCKS.POSX), DSL.min(BLOCKS.POSX))
                        .from(BLOCKS)
                        .where(yCondition)
                        .fetchOne();

                Integer maxX = minMaxRecords.get(DSL.max(BLOCKS.POSX));
                Integer minX = minMaxRecords.get(DSL.min(BLOCKS.POSX));

                long start = System.currentTimeMillis();
                int xStripes = maxX - minX;
                long byteCount = 0;
                int errorCount = 0;
                long lastProgressUpdate = 0;
                int lastTileCount = 0;

                int tileCount = 0;

                logger.info("X-Stripe count: {} (from {} to {})", xStripes, minX, maxX);

                for (int posx = minX; posx <= maxX; posx++) {

                    double xProgress = (posx - minX) / (double) xStripes;

                    minMaxRecords = ctx
                            .select(DSL.max(BLOCKS.POSZ), DSL.min(BLOCKS.POSZ))
                            .from(BLOCKS)
                            .where(yCondition)
                            .and(BLOCKS.POSX.eq(posx))
                            .fetchOne();

                    Integer maxZ = minMaxRecords.get(DSL.max(BLOCKS.POSZ));
                    Integer minZ = minMaxRecords.get(DSL.min(BLOCKS.POSZ));

                    if (maxZ == null || minZ == null) {
                        continue; //empty column
                    }

                    int zCount = maxZ - minZ;

                    for (int posz = minZ; posz <= maxZ; posz++) {

                        double zProgress = (posz - minZ) / (double) zCount;

                        CoordinateResolver.TileInfo tileInfo = CoordinateResolver.fromCoordinates(posx, posz);

                        try {
                            byte[] data = renderer.render(layer, tileInfo.x, tileInfo.y, CoordinateResolver.ONE_TO_ONE_ZOOM);
                            byteCount += data.length;
                            tileCount++;

                        } catch (Exception e) {
                            errorCount++;
                            logger.error("run", e);

                            if (errorCount > 100) {
                                logger.error("Error-count: {}, bailing out!", errorCount);
                                return;
                            }
                        }

                        long now = System.currentTimeMillis();
                        long diff = now - lastProgressUpdate;

                        if (diff > 5000) {
                            //Report every 5 seconds
                            lastProgressUpdate = now;

                            int currentTileCount = tileCount - lastTileCount;

                            double tilesPerSecond = (double) currentTileCount / ((double) diff / 1000d);

                            //Reset count
                            lastTileCount = tileCount;

                            logger.info("Initial rendering status: x-progress({}%) z-progress({}%) tiles({}) data({} MB) posx({}) posz({}) speed({} tiles/s)",
                                    Math.floor(xProgress * 100), Math.floor(zProgress * 100), tileCount, Math.floor(byteCount / 1000) / 1000,
                                    posx, posz, Math.floor(tilesPerSecond)
                            );

                            //Explicit gc()
                            //Runtime.getRuntime().gc();
                        }
                    }

                }

                long renderTime = System.currentTimeMillis() - start;

                logger.info("Initial rendering completed in {} seconds ({} MB, {} tiles)",
                        renderTime, Math.floor(byteCount / 1000) / 1000, tileCount
                );
            }

        } catch (Exception e){
            logger.error("run", e);
        }

    }
}

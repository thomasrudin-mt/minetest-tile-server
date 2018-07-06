package io.rudin.minetest.tileserver;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.rudin.minetest.tileserver.config.TileServerConfig;
import io.rudin.minetest.tileserver.job.InitialTileRendererJob;
import io.rudin.minetest.tileserver.job.UpdateChangedTilesJob;
import io.rudin.minetest.tileserver.job.UpdatePlayerJob;
import io.rudin.minetest.tileserver.module.ConfigModule;
import io.rudin.minetest.tileserver.module.DBModule;
import io.rudin.minetest.tileserver.module.ServiceModule;
import io.rudin.minetest.tileserver.poi.PoiMapBlockListener;
import io.rudin.minetest.tileserver.route.ConfigRoute;
import io.rudin.minetest.tileserver.route.PlayerRoute;
import io.rudin.minetest.tileserver.route.PoiRoute;
import io.rudin.minetest.tileserver.route.TileRoute;
import io.rudin.minetest.tileserver.transformer.JsonTransformer;
import io.rudin.minetest.tileserver.ws.WebSocketHandler;
import io.rudin.minetest.tileserver.ws.WebSocketUpdater;
import org.aeonbits.owner.ConfigFactory;

import static spark.Spark.*;

public class TileServer {

	
	public static void main(String[] args) throws Exception {

		TileServerConfig cfg = ConfigFactory.create(TileServerConfig.class);


		Injector injector = Guice.createInjector(
				new ConfigModule(cfg),
				new DBModule(cfg),
				new ServiceModule(cfg)
		);
		
		DBMigration dbMigration = injector.getInstance(DBMigration.class);
		dbMigration.migrate();
		
		staticFileLocation("/public");
		port(cfg.httPort());

		JsonTransformer json = injector.getInstance(JsonTransformer.class);

		webSocket("/ws", WebSocketHandler.class);
		get("/tiles/:z/:x/:y", injector.getInstance(TileRoute.class));
		get("/player", injector.getInstance(PlayerRoute.class), json);
		get("/config", injector.getInstance(ConfigRoute.class), json);
		get("/poi", injector.getInstance(PoiRoute.class), json);

		//Initialize web server
		init();

		//Initialize ws updater
		injector.getInstance(WebSocketUpdater.class).init();

		//Register poi mapblock listener
		if (cfg.poiEnable()) {
			injector.getInstance(PoiMapBlockListener.class).setup();
		}

		ScheduledExecutorService executor = injector.getInstance(ScheduledExecutorService.class);

		executor.scheduleAtFixedRate(injector.getInstance(UpdateChangedTilesJob.class), 0, cfg.tilerendererUpdateInterval(), TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(injector.getInstance(UpdatePlayerJob.class), 0, cfg.playerUpdateInterval(), TimeUnit.SECONDS);

		if (cfg.tilerendererEnableInitialRendering()){
			//Start initial rendering
			executor.submit(injector.getInstance(InitialTileRendererJob.class));
		}

		AtomicBoolean running = new AtomicBoolean(true);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			stop();
			running.set(false);
		}));

		while (running.get()){
			Thread.sleep(500);
		}
		
	}
	
}

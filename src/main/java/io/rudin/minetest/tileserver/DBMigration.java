package io.rudin.minetest.tileserver;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.flywaydb.core.Flyway;

import io.rudin.minetest.tileserver.config.TileServerConfig;

@Singleton
public class DBMigration {

	@Inject
	public DBMigration(TileServerConfig cfg) {
		this.flyway = new Flyway();
		flyway.setDataSource(cfg.minetestDatabaseUrl(), cfg.minetestDatabaseUsername(), cfg.minetestDatabasePassword());
		flyway.setBaselineVersionAsString("0");
		flyway.setBaselineOnMigrate(true);

		if (cfg.tileCacheType() == TileServerConfig.CacheType.DATABASE) {
			this.tileFlyway = new Flyway();
			tileFlyway.setDataSource(cfg.tileDatabaseUrl(), cfg.tileDatabaseUsername(), cfg.tileDatabasePassword());
			tileFlyway.setSqlMigrationPrefix("TILEV");
		}
	}

	private final Flyway flyway;
	private Flyway tileFlyway;

	public void migrate() {
		if (tileFlyway != null)
			tileFlyway.migrate();

		flyway.migrate();
	}

}

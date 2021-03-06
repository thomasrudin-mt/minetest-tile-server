package io.rudin.minetest.tileserver.base;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import io.rudin.minetest.tileserver.blockdb.tables.records.BlocksRecord;
import io.rudin.minetest.tileserver.config.TileServerConfig;
import io.rudin.minetest.tileserver.module.ConfigModule;
import io.rudin.minetest.tileserver.module.DBModule;
import io.rudin.minetest.tileserver.module.ServiceModule;
import io.rudin.minetest.tileserver.qualifier.MapDB;
import io.rudin.minetest.tileserver.qualifier.TileDB;
import io.rudin.minetest.tileserver.util.StreamUtil;
import org.aeonbits.owner.ConfigFactory;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static io.rudin.minetest.tileserver.blockdb.tables.Blocks.BLOCKS;

public class TileServerTest {

    private static Logger logger = LoggerFactory.getLogger(TileServerTest.class);

    @Before
    public void init() throws SQLException, IOException {
        Map<String, String> properties = new HashMap<>();

        properties.put("minetest.db.url", "jdbc:h2:mem:blocks;DB_CLOSE_DELAY=-1");
        properties.put("minetest.db.username", "sa");
        properties.put("minetest.db.password", "");
        properties.put("minetest.db.driver", "org.h2.Driver");
        properties.put("minetest.db.dialect", "H2");

        properties.put("tile.db.url", "jdbc:h2:mem:tiles;DB_CLOSE_DELAY=-1");
        properties.put("tile.db.username", "sa");
        properties.put("tile.db.password", "");
        properties.put("tile.db.driver", "org.h2.Driver");
        properties.put("tile.db.dialect", "H2");

        properties.put("tile.cache.impl", "DATABASE");

        TileServerConfig cfg = ConfigFactory.create(TileServerConfig.class, properties);

        injector = Guice.createInjector(
                new ConfigModule(cfg),
                new DBModule(cfg),
                new ServiceModule(cfg)
        );

        DataSource dataSource = injector.getInstance(Key.get(DataSource.class, MapDB.class));
        try (Connection connection = dataSource.getConnection()){
            connection.createStatement().execute("drop all objects");
            connection.createStatement().execute("runscript from 'classpath:/minetest-db.sql'");

        }

        DataSource tileDataSource = injector.getInstance(Key.get(DataSource.class, TileDB.class));
        try (Connection connection = tileDataSource.getConnection()){
            connection.createStatement().execute("drop all objects");
            connection.createStatement().execute("runscript from 'classpath:/minetest-tiledb.sql'");

        }

        //does not work: create function plsql not available in h2
        //injector.getInstance(DBMigration.class).migrate();

        logger.info("Environment set up, injecting members");

        injector.injectMembers(this);
    }

    private Injector injector;

    protected void uploadMapBlocksToDatabase(String mapblocksDir) throws IOException {
        DSLContext ctx = injector.getInstance(Key.get(DSLContext.class, MapDB.class));
        File mapBlockDir = new File(mapblocksDir);
        for (File file: mapBlockDir.listFiles()){

            if (file.getName().startsWith("."))
                continue;

            String[] parts = file.getName().split("[.]");

            int posx = Integer.parseInt(parts[0]);
            int posy = Integer.parseInt(parts[1]);
            int posz = Integer.parseInt(parts[2]);

            ByteArrayOutputStream data = new ByteArrayOutputStream();

            try (InputStream input = new FileInputStream(file)){
                StreamUtil.copyStream(input, data);
            }

            BlocksRecord record = ctx.newRecord(BLOCKS);
            record.setMtime(System.currentTimeMillis());
            record.setPosy(posy);
            record.setPosx(posx);
            record.setPosz(posz);
            record.setData(data.toByteArray());
            record.insert();

        }

    }

    @After
    public void after(){
    }

}

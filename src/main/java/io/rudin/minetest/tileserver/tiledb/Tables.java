/*
 * This file is generated by jOOQ.
*/
package io.rudin.minetest.tileserver.tiledb;


import io.rudin.minetest.tileserver.tiledb.tables.FlywaySchemaHistory;
import io.rudin.minetest.tileserver.tiledb.tables.Tiles;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in 
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = io.rudin.minetest.tileserver.tiledb.tables.FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>tiles</code>.
     */
    public static final Tiles TILES = io.rudin.minetest.tileserver.tiledb.tables.Tiles.TILES;
}

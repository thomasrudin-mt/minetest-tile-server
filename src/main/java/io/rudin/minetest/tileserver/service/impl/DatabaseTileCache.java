package io.rudin.minetest.tileserver.service.impl;

import com.google.common.util.concurrent.Striped;
import io.rudin.minetest.tileserver.qualifier.TileDB;
import io.rudin.minetest.tileserver.service.TileCache;
import io.rudin.minetest.tileserver.tiledb.tables.records.TilesRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static io.rudin.minetest.tileserver.tiledb.tables.Tiles.TILES;

@Singleton
public class DatabaseTileCache implements TileCache {

    @Inject
    public DatabaseTileCache(@TileDB DSLContext ctx){
        this.ctx = ctx;
        this.lock = Striped.lazyWeakReadWriteLock(50);
    }

    private final Striped<ReadWriteLock> lock;

    private final DSLContext ctx;

    private String getKey(int x, int y, int z){
        return x + "/" + y + "/" + z;
    }

    private ReadWriteLock getLock(int x, int y, int z){
        return lock.get(getKey(x,y,z));
    }

    @Override
    public void put(int x, int y, int z, byte[] data) {

        final String key = getKey(x,y,z);

        ReadWriteLock lock = getLock(x, y, z);
        Lock writeLock = lock.writeLock();
        writeLock.lock();

        try {
            //re-check in critical section
            if (has(x,y,z)){
                //already inserted
                return;
            }

            TilesRecord record = ctx.newRecord(TILES);
            record.setTile(data);
            record.setX(x);
            record.setY(y);
            record.setZ(z);
            record.setMtime(System.currentTimeMillis());
            record.insert();

        } finally {
            writeLock.unlock();

        }
    }

    @Override
    public byte[] get(int x, int y, int z) {

        return ctx
                .select(TILES.TILE)
                .from(TILES)
                .where(TILES.X.eq(x))
                .and(TILES.Y.eq(y))
                .and(TILES.Z.eq(z))
                .fetchOne(TILES.TILE);

    }

    @Override
    public boolean has(int x, int y, int z) {
        Integer count = ctx
                .select(DSL.count())
                .from(TILES)
                .where(TILES.X.eq(x))
                .and(TILES.Y.eq(y))
                .and(TILES.Z.eq(z))
                .fetchOne(DSL.count());

        return count > 0;
    }

    @Override
    public void remove(int x, int y, int z) {
        ReadWriteLock lock = getLock(x, y, z);
        Lock writeLock = lock.writeLock();
        writeLock.lock();

        try {
            ctx.delete(TILES)
                    .where(TILES.X.eq(x))
                    .and(TILES.Y.eq(y))
                    .and(TILES.Z.eq(z))
                    .execute();

        } finally {
            writeLock.unlock();

        }
    }
}

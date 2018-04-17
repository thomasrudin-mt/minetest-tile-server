/*
 * This file is generated by jOOQ.
*/
package io.rudin.minetest.tileserver.blockdb.tables.records;


import io.rudin.minetest.tileserver.blockdb.tables.TileserverBlockDepth;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TileserverBlockDepthRecord extends UpdatableRecordImpl<TileserverBlockDepthRecord> implements Record3<Integer, Integer, Integer> {

    private static final long serialVersionUID = 1284678813;

    /**
     * Setter for <code>tileserver_block_depth.posx</code>.
     */
    public void setPosx(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>tileserver_block_depth.posx</code>.
     */
    public Integer getPosx() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>tileserver_block_depth.posz</code>.
     */
    public void setPosz(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>tileserver_block_depth.posz</code>.
     */
    public Integer getPosz() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>tileserver_block_depth.visibley</code>.
     */
    public void setVisibley(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>tileserver_block_depth.visibley</code>.
     */
    public Integer getVisibley() {
        return (Integer) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record2<Integer, Integer> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Integer, Integer, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Integer, Integer, Integer> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return TileserverBlockDepth.TILESERVER_BLOCK_DEPTH.POSX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return TileserverBlockDepth.TILESERVER_BLOCK_DEPTH.POSZ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return TileserverBlockDepth.TILESERVER_BLOCK_DEPTH.VISIBLEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getPosx();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component2() {
        return getPosz();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component3() {
        return getVisibley();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getPosx();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getPosz();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getVisibley();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TileserverBlockDepthRecord value1(Integer value) {
        setPosx(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TileserverBlockDepthRecord value2(Integer value) {
        setPosz(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TileserverBlockDepthRecord value3(Integer value) {
        setVisibley(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TileserverBlockDepthRecord values(Integer value1, Integer value2, Integer value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TileserverBlockDepthRecord
     */
    public TileserverBlockDepthRecord() {
        super(TileserverBlockDepth.TILESERVER_BLOCK_DEPTH);
    }

    /**
     * Create a detached, initialised TileserverBlockDepthRecord
     */
    public TileserverBlockDepthRecord(Integer posx, Integer posz, Integer visibley) {
        super(TileserverBlockDepth.TILESERVER_BLOCK_DEPTH);

        set(0, posx);
        set(1, posz);
        set(2, visibley);
    }
}
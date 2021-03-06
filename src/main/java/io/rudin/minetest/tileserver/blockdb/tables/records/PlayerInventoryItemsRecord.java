/*
 * This file is generated by jOOQ.
*/
package io.rudin.minetest.tileserver.blockdb.tables.records;


import io.rudin.minetest.tileserver.blockdb.tables.PlayerInventoryItems;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Row4;
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
public class PlayerInventoryItemsRecord extends UpdatableRecordImpl<PlayerInventoryItemsRecord> implements Record4<String, Integer, Integer, String> {

    private static final long serialVersionUID = -1669759709;

    /**
     * Setter for <code>player_inventory_items.player</code>.
     */
    public void setPlayer(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>player_inventory_items.player</code>.
     */
    public String getPlayer() {
        return (String) get(0);
    }

    /**
     * Setter for <code>player_inventory_items.inv_id</code>.
     */
    public void setInvId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>player_inventory_items.inv_id</code>.
     */
    public Integer getInvId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>player_inventory_items.slot_id</code>.
     */
    public void setSlotId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>player_inventory_items.slot_id</code>.
     */
    public Integer getSlotId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>player_inventory_items.item</code>.
     */
    public void setItem(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>player_inventory_items.item</code>.
     */
    public String getItem() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record3<String, Integer, Integer> key() {
        return (Record3) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<String, Integer, Integer, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<String, Integer, Integer, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return PlayerInventoryItems.PLAYER_INVENTORY_ITEMS.PLAYER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return PlayerInventoryItems.PLAYER_INVENTORY_ITEMS.INV_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return PlayerInventoryItems.PLAYER_INVENTORY_ITEMS.SLOT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return PlayerInventoryItems.PLAYER_INVENTORY_ITEMS.ITEM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component1() {
        return getPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component2() {
        return getInvId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component3() {
        return getSlotId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getItem();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getInvId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getSlotId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getItem();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerInventoryItemsRecord value1(String value) {
        setPlayer(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerInventoryItemsRecord value2(Integer value) {
        setInvId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerInventoryItemsRecord value3(Integer value) {
        setSlotId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerInventoryItemsRecord value4(String value) {
        setItem(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerInventoryItemsRecord values(String value1, Integer value2, Integer value3, String value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PlayerInventoryItemsRecord
     */
    public PlayerInventoryItemsRecord() {
        super(PlayerInventoryItems.PLAYER_INVENTORY_ITEMS);
    }

    /**
     * Create a detached, initialised PlayerInventoryItemsRecord
     */
    public PlayerInventoryItemsRecord(String player, Integer invId, Integer slotId, String item) {
        super(PlayerInventoryItems.PLAYER_INVENTORY_ITEMS);

        set(0, player);
        set(1, invId);
        set(2, slotId);
        set(3, item);
    }
}

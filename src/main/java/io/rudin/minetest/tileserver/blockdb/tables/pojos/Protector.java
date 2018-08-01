/*
 * This file is generated by jOOQ.
*/
package io.rudin.minetest.tileserver.blockdb.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


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
public class Protector implements Serializable {

    private static final long serialVersionUID = -1006123423;

    private Integer id;
    private String  owner;
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer posx;
    private Integer posy;
    private Integer posz;
    private Long    mtime;

    public Protector() {}

    public Protector(Protector value) {
        this.id = value.id;
        this.owner = value.owner;
        this.x = value.x;
        this.y = value.y;
        this.z = value.z;
        this.posx = value.posx;
        this.posy = value.posy;
        this.posz = value.posz;
        this.mtime = value.mtime;
    }

    public Protector(
        Integer id,
        String  owner,
        Integer x,
        Integer y,
        Integer z,
        Integer posx,
        Integer posy,
        Integer posz,
        Long    mtime
    ) {
        this.id = id;
        this.owner = owner;
        this.x = x;
        this.y = y;
        this.z = z;
        this.posx = posx;
        this.posy = posy;
        this.posz = posz;
        this.mtime = mtime;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getX() {
        return this.x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return this.z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Integer getPosx() {
        return this.posx;
    }

    public void setPosx(Integer posx) {
        this.posx = posx;
    }

    public Integer getPosy() {
        return this.posy;
    }

    public void setPosy(Integer posy) {
        this.posy = posy;
    }

    public Integer getPosz() {
        return this.posz;
    }

    public void setPosz(Integer posz) {
        this.posz = posz;
    }

    public Long getMtime() {
        return this.mtime;
    }

    public void setMtime(Long mtime) {
        this.mtime = mtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Protector (");

        sb.append(id);
        sb.append(", ").append(owner);
        sb.append(", ").append(x);
        sb.append(", ").append(y);
        sb.append(", ").append(z);
        sb.append(", ").append(posx);
        sb.append(", ").append(posy);
        sb.append(", ").append(posz);
        sb.append(", ").append(mtime);

        sb.append(")");
        return sb.toString();
    }
}
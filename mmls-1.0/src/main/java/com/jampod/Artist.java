package com.jampod;

/**
 * Artist Class
 * @author Nick G
 * @version 1.0
 * @created 3.23.22
 */
public class Artist {
    private String GUID;
    private String name;
    private String type;

    public Artist(String GUID, String name, String type) {
        this.GUID = GUID;
        this.name = name;
        this.type = type;
    }

    public String getGUID() {
        return GUID;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }

    public String toString() {
        return String.format("%s - %s - %s",GUID,name,type);
    }

    public String toExportableString() {
        return GUID+","+name+","+type;
    }
}

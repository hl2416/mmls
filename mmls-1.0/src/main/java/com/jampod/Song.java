package com.jampod;

/**
 * Song Class
 * @author Nick G
 * @version 1.0
 * @created 3.27.22
 */
public class Song {
    protected String GUID;
    protected Artist artist;
    protected String title;
    protected int duration;

    public Song(String GUID, Artist artist, String title, int duration) {
        this.GUID = GUID;
        this.artist = artist;
        this.title = title;
        this.duration = duration;
    }

    public String getGUID() {
        return GUID;
    }
    public Artist getArtist() {
        return artist;
    }
    public String getTitle() {
        return title;
    }
    public int getDuration() {
        return duration;
    }
    public String toString() {
        return String.format("%s - %s - %s - %s",GUID,artist.getGUID(),duration,title);
    }
    public String toExportableString() {
        return GUID+","+artist.getGUID()+","+duration+","+title;
    }
}

package com.jampod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Release Class
 * @author Nick G
 * @version 1.0
 * @created 3.27.22
 */
public class Release {
    protected String GUID;
    protected Artist artist;
    protected String name;
    protected Date releaseDate;
    protected String medium;
    protected List<Song> trackList;

    public Release(String GUID, Artist artist, String name, Date releaseDate, String medium, List<Song> trackList) {
        this.GUID = GUID;
        this.artist = artist;
        this.name = name;
        this.releaseDate = releaseDate;
        this.medium = medium;
        this.trackList = trackList;
    }

    public Release(String GUID, Artist artist, String name, Date releaseDate, String medium) {
        this.GUID = GUID;
        this.artist = artist;
        this.name = name;
        this.releaseDate = releaseDate;
        this.medium = medium;
        this.trackList = new ArrayList<Song>();
    }

    public String getGUID() {
        return GUID;
    }
    public Artist getArtist() {
        return artist;
    }
    public String getName() {
        return name;
    }
    public Date getReleaseDate() {
        return releaseDate;
    }
    public String getMedium() {
        return medium;
    }
    public List<Song> getTrackList() {
        return trackList;
    }
    public int getDuration() {
        int duration = 0;
        for(Song song : trackList) {
            duration += song.getDuration();
        }
        return duration;
    }
    public void setTrackList(List<Song> trackList) {
        this.trackList = trackList;
    }
    public String toString() {
        return String.format("%s - %s - %s - %s - %s - %s",GUID,artist.getGUID(),name,medium,releaseDate,trackList);
    }
    public String toExportableString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String retStr = GUID+","+artist.getGUID()+","+name+","+medium+","+dateFormat.format(releaseDate);
        for(Song song : trackList) {
            retStr+=","+song.getGUID();
        }
        return retStr;
    }
}

package com.jampod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * LibrarySong Class
 * @author Nick G
 * @version 1.0
 * @created 3.27.22
 */
public class LibrarySong extends Song {
    private Date acquisitionDate;
    private int rating = 0;
    public LibrarySong(Song song) {
        super(song.getGUID(), song.getArtist(), song.getTitle(), song.getDuration());
        rating = 0;
        acquisitionDate = new Date();
    }
    public LibrarySong(Song song, int rating) {
        super(song.getGUID(), song.getArtist(), song.getTitle(), song.getDuration());
        this.rating = rating;
        acquisitionDate = new Date();
    }
    public LibrarySong(Song song, Date acquisitionDate) {
        super(song.getGUID(), song.getArtist(), song.getTitle(), song.getDuration());
        rating = 0;
        this.acquisitionDate = acquisitionDate;
    }
    public LibrarySong(Song song, int rating, Date acquisitionDate) {
        super(song.getGUID(), song.getArtist(), song.getTitle(), song.getDuration());
        this.rating = rating;
        this.acquisitionDate = acquisitionDate;
    }
    public LibrarySong(String GUID, Artist artist, String title, int duration, int rating, Date acquisitionDate) {
        super(GUID, artist, title, duration);
        this.rating = rating;
        this.acquisitionDate = acquisitionDate;
    }
    public Date getAcquisitionDate() {
        return acquisitionDate;
    }
    public int getRating() {
        return rating;
    }
    @Override
    public String toExportableString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return GUID+","+artist.getGUID()+","+duration+","+title+","+rating+","+dateFormat.format(acquisitionDate);
    }
}

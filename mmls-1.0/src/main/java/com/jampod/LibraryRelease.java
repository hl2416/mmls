package com.jampod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * LibraryRelease Class
 * @author Nick G
 * @version 1.0
 * @created 3.27.22
 */
public class LibraryRelease extends Release {
    private Date acquisitionDate;
    private List<LibrarySong> trackList;
    public LibraryRelease(Release release) {
        super(release.getGUID(), release.getArtist(), release.getName(), release.getReleaseDate(), release.getMedium(), release.getTrackList());
        acquisitionDate = new Date();
    }
    public LibraryRelease(Release release, Date acquisitionDate) {
        super(release.getGUID(), release.getArtist(), release.getName(), release.getReleaseDate(), release.getMedium(), release.getTrackList());
        this.acquisitionDate = acquisitionDate;
    }
    public LibraryRelease(String GUID, Artist artist, String name, Date releaseDate, String medium, List<LibrarySong> trackList, Date acquisitionDate) {
        super(GUID, artist, name, releaseDate, medium);
        List<Song> convertedList = (List<Song>)(List<?>) trackList;
        super.setTrackList(convertedList);
        this.trackList = trackList;
        this.acquisitionDate = acquisitionDate;
    }
    public Date getAcquisitionDate() {
        return acquisitionDate;
    }
    public int getRating() {
        int rating = 0;
        for(LibrarySong song : trackList) {
            rating += song.getRating();
        }
        rating /= trackList.size();
        return rating;
    }
    public String toExportableString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String retStr = GUID+","+artist.getGUID()+","+name+","+medium+","+dateFormat.format(releaseDate)+","+dateFormat.format(acquisitionDate);
        for(Song song : trackList) {
            retStr+=","+song.getGUID();
        }
        return retStr;
    }
}

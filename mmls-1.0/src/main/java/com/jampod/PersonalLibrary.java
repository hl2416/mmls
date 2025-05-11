package com.jampod;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

/**
 * PersonalLibrary Class
 * @author Nick G
 * @version 1.1
 * @created 3.27.22
 */
public class PersonalLibrary extends Database {
    private String user;
    protected List<LibrarySong> songs;
    protected List<LibraryRelease> releases;
    protected List<Artist> artists;
    protected List<Action> actions;

    public PersonalLibrary(String user) {
        this.user = user;
        songs = new ArrayList<LibrarySong>();
        releases = new ArrayList<LibraryRelease>();
        artists = new ArrayList<Artist>();
        actions = new ArrayList<Action>();
    }
    public String getUser() {
        return user;
    }

    public List<LibraryRelease> getLibraryReleases() {
        return releases;
    }

    public List<LibrarySong> getLibrarySongs() {
        return songs;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<Action> getActions() {
        return actions;
    }

    public boolean exportLibrary() {
        try {
            Writer writer = new FileWriter(user+"-songs.csv");
            CSVWriter csvWriter = new CSVWriter(writer);
            for(LibrarySong song : songs) {
                csvWriter.writeNext(song.toExportableString().split(","));
            }
            csvWriter.close();
        } catch (IOException ioe) {
            System.out.println("IOException writing SONGS "+ioe);
            return false;
        }
        try {
            Writer writer = new FileWriter(user+"-releases.csv");
            CSVWriter csvWriter = new CSVWriter(writer);
            for(LibraryRelease release : releases) {
                csvWriter.writeNext(release.toExportableString().split(","));
            }
            csvWriter.close();
        } catch (IOException ioe) {
            System.out.println("IOException writing RELEASES "+ioe);
            return false;
        }
        try {
            Writer writer = new FileWriter(user+"-artists.csv");
            CSVWriter csvWriter = new CSVWriter(writer);
            for(Artist artist : artists) {
                csvWriter.writeNext(artist.toExportableString().split(","));
            }
            csvWriter.close();
        } catch (IOException ioe) {
            System.out.println("IOException writing ARTISTS "+ioe);
            return false;
        }
        return true;
    }
}

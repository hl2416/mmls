package com.jampod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Database Class
 * 
 * @author Nick G
 * @version 1.1
 * @created 3.27.22
 */
public class Database implements CSVManager {
    protected List<Song> songs;
    protected List<Release> releases;
    protected List<Artist> artists;

    public Database() {
        this.songs = new ArrayList<Song>();
        this.releases = new ArrayList<Release>();
        this.artists = new ArrayList<Artist>();
    }

    public Database(List<Song> songs, List<Release> releases, List<Artist> artists) {
        this.songs = songs;
        this.releases = releases;
        this.artists = artists;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    @Override
    public int loadArtists(File file) {
        int count = 0;
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length == 2) {
                    Artist artist = new Artist(nextLine[0], nextLine[1], "");
                    count++;
                    artists.add(artist);
                } else if (nextLine.length == 3) {
                    Artist artist = new Artist(nextLine[0], nextLine[1], nextLine[2]);
                    count++;
                    artists.add(artist);
                } else {
                    throw new CsvValidationException();
                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file in " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("IO Error!");
        } catch (CsvValidationException e) {
            System.err.println("Invalid CSV formatted file!");
        }
        return count;
    }

    @Override
    public int loadSongs(File file) {
        int count = 0;
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String artistGUID = nextLine[1];

                List<Artist> artistResults = (List<Artist>) new SearchGUID().search(this, "artist~"+artistGUID);
                Artist artist;
                if(artistResults.size() == 0) {
                    artist = new Artist("none", "unknown", "");
                } else {
                    artist = artistResults.get(0);
                }

                Song song = new Song(nextLine[0],artist,nextLine[3],Integer.parseInt(nextLine[2]));
                count++;
                songs.add(song);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file in " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("IO Error!");
        } catch (CsvValidationException e) {
            System.err.println("Invalid CSV formatted file!");
        } catch (NumberFormatException e) {
            System.err.println("Invalid duration format!");
        }
        return count;
    }

    @Override
    public int loadReleases(File file) {
        int count = 0;
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String artistGUID = nextLine[1];

                List<Artist> artistResults = (List<Artist>) new SearchGUID().search(this, "artist~"+artistGUID);
                Artist artist;
                if(artistResults.size() == 0) {
                    artist = new Artist("none", "unknown", "");
                } else {
                    artist = artistResults.get(0);
                }

                List<Song> trackList = new ArrayList<Song>();
                for(int i = 5; i < nextLine.length; i++) {
                    List<Song> songResults = (List<Song>) new SearchGUID().search(this, "song~"+nextLine[i]);
                    trackList.addAll(songResults);
                }

                Date releaseDate;
                try {
                    releaseDate = DateUtils.parseDate(nextLine[4]);
                } catch (ParseException e) {
                    // System.err.println("Invalid date in CSV imported!");
                    releaseDate = new Date();
                }
                Release release = new Release(nextLine[0], artist, nextLine[2], releaseDate, nextLine[3], trackList);
                count++;
                releases.add(release);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file in " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("IO Error!");
        } catch (CsvValidationException e) {
            System.err.println("Invalid CSV formatted file!");
        }
        return count;
    }

    @Override
    public Path exportDatabase(Database db) {
        return null;
    }

    public String toString() {
        String ret = "";
        for(Artist artist : artists) {
            ret+="\n"+artist;
        }
        for(Song song : songs) {
            ret+="\n"+song;
        }
        for(Release release : releases) {
            ret+="\n"+release;
        }
        return ret;
    }
}

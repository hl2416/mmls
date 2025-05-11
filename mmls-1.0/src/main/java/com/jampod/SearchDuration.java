package com.jampod;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SearchDuration implements Search {

    /**
     * Search param is split string item~min/max~duration so example release~min~100
     */
    public List<?> search(Database dataset, String search) {
        String[] searchParams = search.split("~");
        int duration = Integer.parseInt(searchParams[2]);
        switch(searchParams[0]) {
            case "release":
                List<Release> releases = dataset.getReleases();
                List<Release> resultsA = new ArrayList<Release>();
                if(searchParams[1].equals("min")) {
                    for(Release release : releases) {
                        if(release.getDuration() > duration) {
                            resultsA.add(release);
                        }
                    }
                } else {
                    for(Release release : releases) {
                        if(release.getDuration() < duration) {
                            resultsA.add(release);
                        }
                    }
                }
                return resultsA;
            case "song":
                List<Song> songs = dataset.getSongs();
                List<Song> results = new ArrayList<Song>();
                if(searchParams[1].equals("min")) {
                    for(Song song : songs) {
                        if(song.getDuration() > duration) {
                            results.add(song);
                        }
                    }
                } else {
                    for(Song song : songs) {
                        if(song.getDuration() < duration) {
                            results.add(song);
                        }
                    }
                }
                return results;
        }
        return null;
    }
    
}

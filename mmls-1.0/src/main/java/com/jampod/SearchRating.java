package com.jampod;

import java.util.ArrayList;
import java.util.List;

public class SearchRating implements SearchLibrary {

    /**
     * Search param is split string target~min/max~rating so example song~min~3
     */
    public List<?> search(PersonalLibrary dataset, String search) {
        String[] searchParams = search.split("~");
        if(searchParams.length != 3) {
            System.out.println("Invalid Search parameters");
            return null;
        }
        int rating = Integer.parseInt(searchParams[2]);
        switch(searchParams[0]) {
            case "song":
                List<LibrarySong> songs = dataset.getLibrarySongs();
                List<LibrarySong> results1 = new ArrayList<LibrarySong>();
                if(searchParams[1].equals("min")) {
                    for(LibrarySong song : songs) {
                        if(song.getRating() > rating) {
                            results1.add(song);
                        }
                    }
                } else {
                    for(LibrarySong song : songs) {
                        if(song.getRating() < rating) {
                            results1.add(song);
                        }
                    }
                }
                return results1;
            case "release":
                List<LibraryRelease> releases = dataset.getLibraryReleases();
                List<LibraryRelease> results2 = new ArrayList<LibraryRelease>();
                if(searchParams[1].equals("min")) {
                    for(LibraryRelease release : releases) {
                        if(release.getRating() > rating) {
                            results2.add(release);
                        }
                    }
                } else {
                    for(LibraryRelease release : releases) {
                        if(release.getRating() < rating) {
                            results2.add(release);
                        }
                    }
                }
                return results2;
            case "artist":
                // todo
                return null;
        }
        return null;
    }
    
}

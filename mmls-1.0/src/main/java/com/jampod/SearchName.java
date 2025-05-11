package com.jampod;

import java.util.ArrayList;
import java.util.List;

public class SearchName implements Search {

    /**
     * @param String searchTerm Search term is split string with type~name so example: song~bohemian rhapsody
     */
    public List<?> search(Database dataset, String search) {
        String[] searchParams = search.split("~");
        switch(searchParams[0]) {
            case "artist":
                List<Artist> artists = dataset.getArtists();
                List<Artist> results1 = new ArrayList<Artist>();
                for(Artist artist : artists) {
                    if(artist.getName().equals(searchParams[1])) {
                        results1.add(artist);
                    }
                }
                return results1;
            case "release":
                List<Release> releases = dataset.getReleases();
                List<Release> results2 = new ArrayList<Release>();
                for(Release release : releases) {
                    if(release.getName().equals(searchParams[1])) {
                        results2.add(release);
                    }
                }
                return results2;
            default:
                System.out.println("Unknown search target");
        }
        return null;
    }
    
}

package com.jampod;

import java.util.ArrayList;
import java.util.List;

public class SearchType implements Search {
    public List<?> search(Database dataset, String searchParam) {
        List<Artist> artists = dataset.getArtists();
        List<Artist> artistResults = new ArrayList<Artist>();

        for(Artist artist : artists) {
            if(artist.getType().equals(searchParam)) {
                artistResults.add(artist);
            }
        }
        return artistResults;
    }
}

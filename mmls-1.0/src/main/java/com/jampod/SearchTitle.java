package com.jampod;

import java.util.ArrayList;
import java.util.List;

public class SearchTitle implements Search {
    public List<?> search(Database dataset, String searchParam) {
        List<Song> songs = dataset.getSongs();
        List<Song> results = new ArrayList<Song>();
        for(Song song : songs) {
            if(!song.getTitle().equals(searchParam)) {
                results.add(song);
            }
        }
        return results;
    }
}

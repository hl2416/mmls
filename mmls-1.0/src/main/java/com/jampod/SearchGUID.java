package com.jampod;

import java.util.List;
import java.util.ArrayList;

public class SearchGUID implements Search {
    /**
     * @param String searchTerm Search term is split string with type~guid so example: artist~093250-2356236-2414214-2154162
     */
    public List<?> search(Database dataset, String searchTerm) {
        String[] searchParams = searchTerm.split("~");
        switch (searchParams[0]) {
            case "artist":
                List<Artist> searchSet1 = dataset.getArtists();
                List<Artist> resultSet1 = new ArrayList<Artist>();
                for (Artist a : searchSet1) {
                    if (a.getGUID().equals(searchParams[1])) {
                        resultSet1.add(a);
                    }
                }
                return resultSet1;
            case "release":
                List<Release> searchSet2 = dataset.getReleases();
                List<Release> resultSet2 = new ArrayList<Release>();
                for (Release release : searchSet2) {
                    if (release.getGUID().equals(searchParams[1])) {
                        resultSet2.add(release);
                    }
                }
                return resultSet2;
            case "song":
                List<Song> searchSet3 = dataset.getSongs();
                List<Song> resultSet3 = new ArrayList<Song>();
                for (Song s : searchSet3) {
                    if (s.getGUID().equals(searchParams[1])) {
                        resultSet3.add(s);
                    }
                }
                return resultSet3;
        }
        return new ArrayList<Object>();
    }
}
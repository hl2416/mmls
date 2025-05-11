package com.jampod;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

public class SearchReleaseDate implements Search {

    /**
     * Search param is split string date~min/max so example 2022-01-01~min
     */
    public List<?> search(Database dataset, String search) {
        List<Release> releases = dataset.getReleases();
        List<Release> results = new ArrayList<Release>();
        String[] searchParams = search.split("~");
        Date searchDate;
        try {
            searchDate = DateUtils.parseDate(searchParams[0]);
        } catch (ParseException e) {
            // System.err.println("Invalid date in CSV imported!");
            return new ArrayList<Release>();
        }
        if(searchParams[1].equals("min")) {
            for(Release release : releases) {
                if(release.getReleaseDate().getTime() >= searchDate.getTime()) {
                    results.add(release);
                }
            }
        } else {
            for(Release release : releases) {
                if(release.getReleaseDate().getTime() <= searchDate.getTime()) {
                    results.add(release);
                }
            }
        }
        return results;
    }
    
}

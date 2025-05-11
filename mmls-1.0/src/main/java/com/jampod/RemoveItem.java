package com.jampod;

import java.util.List;
import com.opencsv.CSVReader;
import java.io.FileReader;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.exceptions.CsvException;

public class RemoveItem
{
    public void removeSong(String accountName, String song) throws IOException, CsvException
    {
        CSVReader cr = new CSVReader(new FileReader("lib[" + accountName + "]-Songs.csv"));
        CSVWriter cw = new CSVWriter(new FileWriter("lib[" + accountName + "]-Songs.csv"));

        List<String[]> s = cr.readAll();
        
        boolean found = false;
        for(int i = 0; i < s.size(); i++)
        {
            if(s.get(i)[0].equals(song) || s.get(i)[3].equals(song))
            {
                found = true;
                String aG = s.get(i)[1];
                s.remove(i);
                cw.writeAll(s);
                checkArtists(accountName, aG);
                break;
            }
        }

        if(found == false)
        {
            System.out.println("Song not found... Please enter another song's GUID or title or quit");
        }
        cr.close();
        cw.close();
    }

    public void removeRelease(String accountName, String release) throws IOException, CsvException
    {
        CSVReader cr = new CSVReader(new FileReader("lib[" + accountName + "]-Releases.csv"));
        CSVWriter cw = new CSVWriter(new FileWriter("lib[" + accountName + "]-Releases.csv"));

        List<String[]> r = cr.readAll();
        
        boolean found = false;
        for(int i = 0; i < r.size(); i++)
        {
            if(r.get(i)[0].equals(release) || r.get(i)[2].equals(release))
            {
                found = true;
                String aG = r.get(i)[1];
                r.remove(i);
                cw.writeAll(r);
                checkArtists(accountName, aG);
                break;
            }
        }

        if(found == false)
        {
            System.out.println("Release not found... Please enter another release's GUID or title or quit");
        }
        cr.close();
        cw.close();
    }

    public void checkArtists(String accountName, String artistGUID) throws IOException, CsvException
    {
        CSVReader crS = new CSVReader(new FileReader("lib[" + accountName + "]-Songs.csv"));
        CSVReader crA = new CSVReader(new FileReader("lib[" + accountName + "]-Artists.csv"));
        CSVReader crR = new CSVReader(new FileReader("lib[" + accountName + "]-Releases.csv"));
        CSVWriter cw = new CSVWriter(new FileWriter("lib[" + accountName + "]-Artists.csv"));

        List<String[]> s = crS.readAll();
        List<String[]> a = crA.readAll();
        List<String[]> r = crR.readAll();

        boolean match = false;
        for(int i = 0; i < s.size(); i++)
        {
            if(s.get(i)[1].equals(artistGUID))
            {
                match = true;
                break;
            }
        }

        for(int i = 0; i < r.size(); i++)
        {
            if(r.get(i)[1].equals(artistGUID))
            {
                match = true;
                break;
            }
        }

        if(match == false)
        {
            for(int i = 0; i < a.size(); i++)
            {
                if(a.get(i)[0].equals(artistGUID))
                {
                    a.remove(i);
                    cw.writeAll(a);
                    break;
                }
            }
        }

        crS.close();
        crA.close();
        crR.close();
        cw.close();
    }
}

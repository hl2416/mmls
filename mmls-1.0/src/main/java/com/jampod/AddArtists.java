package com.jampod;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AddArtists 
{
    public static void main(String args[]) throws IOException
    {
        Scanner in = new Scanner(System.in);
        BufferedReader brSongs = new BufferedReader(new FileReader("c:/Z/SWEN383/mmls/src/main/java/com/jampod/songs.csv"));
        BufferedReader brArtists = new BufferedReader(new FileReader("c:/Z/SWEN383/mmls/src/main/java/com/jampod/artists.csv"));
        FileWriter fw = new FileWriter("lib.csv", true);

        String song = "";
        String sline = "";
        String aline = "";
        
        while(!song.equals("exit"))
        {
            System.out.print("Type a song to be added or type exit to quit: ");
            song = in.nextLine();
            if(!song.equals("exit"))
            {
                System.out.println("Song to be added: " + song);
                sline = brSongs.readLine();
                while((sline) != null)
                {
                    String[] s = sline.split(",(?! )");
                    if(s[3].equals(song))
                    {
                        aline = brArtists.readLine();
                        while((aline) != null)
                        {
                            String[] a = aline.split(",(?! )");
                            if(a[0].equals(s[1]))
                            {
                                fw.append(s[3]);
                                fw.append(",");
                                fw.append(a[1]);
                                System.out.println("Song " + s[3] + " and Artist " + a[1] + " has been appended to lib.csv!");
                                break;
                            }
                            else
                            {
                                aline = brArtists.readLine();
                            }
                        }
                        break;
                    }
                    else
                    {
                        sline = brSongs.readLine();
                    }
                }
            }
            else
            {
                System.out.println("Stopped adding songs...");
            }
        }
        fw.close();
        brSongs.close();
        brArtists.close();
        in.close();
    }
}

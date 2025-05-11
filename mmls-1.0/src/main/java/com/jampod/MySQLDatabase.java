package com.jampod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * MySQLDatabase Connector Class
 * Connects to the online database and retrieves entries
 * @author Nick G
 * @version 1.0
 * @created 4.11.22
 */
public class MySQLDatabase {

    private Connection conn;

    final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

    private String url = "jdbc:mysql://db4free.net/jampods_db_exp";

    private String userName;
    private String password;

    public MySQLDatabase() {
        userName = "jampod";
        password = "MusicMan";
    }

    public void loadDriver() {
        // Step 2) Load a driver
        try {
            // Class.forName method returns the Class object
            // associated with the class or interface
            // with the given string name,
            // using the given class loader.
            Class.forName(DEFAULT_DRIVER);
            // System.out.println("\nDriver Loaded " + DEFAULT_DRIVER + "\n");
        } // end of try block
        catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to load driver class: " + DEFAULT_DRIVER);
        } // end of catch
    } // end of method loadDriver()

    public boolean connect() {
        boolean connected = false;

        try {
            loadDriver();
            conn = DriverManager.getConnection(url, userName, password);
            connected = true;
        } catch (SQLException sqle) {
            System.out.println("\nERROR CAN NOT MAKE CONNECITON");
            System.out.println("ERROR WAS " + sqle + "\n");
            sqle.printStackTrace();
        }

        return connected;
    }

    public boolean close() {
        boolean closed = false;
        try {
            if (conn.isClosed()) {
                return true;
            } else {
                conn.close();
                closed = true;
            }
        } catch (SQLException e) {
            System.out.println("\nERROR CANNOT CLOSE CONNECTION");
        }
        return closed;
    }

    public List<Release> getReleases() {
        List<Release> releases = new ArrayList<Release>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM jampods_db_exp.release;", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {;
                // Retrieve resultset data to print on the screen
                String GUID = rs.getString(1);
                String artistGUID = rs.getString(2);
                String name = rs.getString(3);
                String medium = rs.getString(4);
                Date release_date = rs.getDate(5);
                Artist artist = getArtist(artistGUID);
                List<Song> songList = getSongs(GUID);
                Release release = new Release(GUID, artist, name, release_date, medium, songList);
                releases.add(release);
            }
            return releases;
        } catch (SQLException sqle) {
            System.out.println("ERROR MESSAGE -> " + sqle);
            System.out.println("ERROR SQLException in getReleases()");
        }
        return releases;
    }

    public Release getRelease(String guid) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM jampods_db_exp.release WHERE guid LIKE \""+guid+"\";", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            rs.next();
                // Retrieve resultset data to print on the screen
            String GUID = rs.getString(1);
            String artistGUID = rs.getString(2);
            String name = rs.getString(3);
            String medium = rs.getString(4);
            Date release_date = rs.getDate(5);
            Artist artist = getArtist(artistGUID);
            List<Song> songList = getSongs(guid);
            Release release = new Release(GUID, artist, name, release_date, medium, songList);
            return release;
        } catch (SQLException sqle) {
            System.out.println("ERROR MESSAGE -> " + sqle);
            System.out.println("ERROR SQLException in getRelease()");
        }
        return new Release("ERROR", new Artist("ERROR","ERROR","ERROR"), "ERROR", new Date(), "ERROR", new ArrayList<Song>());
    }

    public Song getSong(String guid) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM song WHERE guid LIKE \""+guid+"\";", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            rs.next();
                // Retrieve resultset data to print on the screen
            String GUID = rs.getString(1);
            String artistGUID = rs.getString(2);
            int duration = rs.getInt(3);
            String title = rs.getString(4);
            Artist artist = getArtist(artistGUID);
            Song song = new Song(GUID, artist, title, duration);
            return song;
        } catch (SQLException sqle) {
            System.out.println("ERROR MESSAGE -> " + sqle);
            System.out.println("ERROR SQLException in getSong()");
        }
        return new Song("ERROR",new Artist("ERROR","ERROR","ERROR"),"ERROR",-1);
    }

    public List<Song> getSongs() {
        List<Song> songList = new ArrayList<Song>();
        try {
            String sql = "SELECT * FROM song;";
            PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Retrieve resultset data to print on the screen
                String GUID = rs.getString(1);
                String artistGUID = rs.getString(2);
                int duration = rs.getInt(3);
                String title = rs.getString(4);
                Artist artist = getArtist(artistGUID);
                Song song = new Song(GUID, artist, title, duration);
                songList.add(song);
            } // end of if
        } catch (SQLException sqle) {
            System.out.println("ERROR MESSAGE -> " + sqle);
            System.out.println("ERROR SQLException in getSong()");
        }
        return songList;
    }

    /**
     * get songs via guid, guid being that of a release
     * @param guid
     * @return
     */
    public List<Song> getSongs(String guid) {
        List<Song> songList = new ArrayList<Song>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM release_songs WHERE release_guid LIKE \""+guid+"\";", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Retrieve resultset data to print on the screen
                String Song_GUID = rs.getString(2);
                Song song = this.getSong(Song_GUID);
                songList.add(song);
            } // end of if
        } catch (SQLException sqle) {
            System.out.println("ERROR MESSAGE -> " + sqle);
            System.out.println("ERROR SQLException in getSongs()");
        }
        return songList;
    }

    public Artist getArtist(String guid) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM artist WHERE guid LIKE \""+guid+"\";", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            rs.next();
                // Retrieve resultset data to print on the screen
            String GUID = rs.getString(1);
            String name = rs.getString(2);
            String type = rs.getString(3);
            Artist artist = new Artist(GUID, name, type);
            return artist;
        } catch (SQLException sqle) {
            System.out.println("ERROR MESSAGE -> " + sqle);
            System.out.println("ERROR SQLException in getArtists()");
        }
        return new Artist("ERROR","ERROR","ERROR");
    }

    public List<Artist> getArtists() {
        List<Artist> artistList = new ArrayList<Artist>();
        try {
            String sql = "SELECT * FROM artist;";
            PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Retrieve resultset data to print on the screen
                String GUID = rs.getString(1);
                String name = rs.getString(2);
                String type = rs.getString(3);
                Artist artist = new Artist(GUID, name, type);
                artistList.add(artist);
            } // end of if
        } catch (SQLException sqle) {
            System.out.println("ERROR MESSAGE -> " + sqle);
            System.out.println("ERROR SQLException in getArtist()");
        }
        return artistList;
    }

} // end of class
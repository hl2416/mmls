package com.jampod;

import java.util.Date;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.text.SimpleDateFormat;

public class AddItem {
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

    public void addSong(String accountName, String song, Database db, int r, String a) throws IOException {
        for (int i = 0; i < db.getSongs().size(); i++) {
            Song s = db.getSongs().get(i);
            if (s.getGUID().equals(song) || s.getTitle().equals(song)) {
                if (r == 0 && a.equals("0")) {
                    LibrarySong ls = new LibrarySong(s);
                    appendCSVS(accountName, ls);
                } else if (r > 0 && a.equals("0")) {
                    LibrarySong ls = new LibrarySong(s, r);
                    appendCSVS(accountName, ls);
                } else if (r == 0 && !a.equals("0")) {
                    String[] da = a.split("-");
                    ZoneId zID = ZoneId.systemDefault();
                    LocalDate ld = LocalDate.of(Integer.parseInt(da[2]), Integer.parseInt(da[0]),
                            Integer.parseInt(da[1]));
                    Date d = Date.from(ld.atStartOfDay(zID).toInstant());
                    LibrarySong ls = new LibrarySong(s, d);
                    appendCSVS(accountName, ls);
                } else if (r > 0 && !a.equals("0")) {
                    String[] da = a.split("-");
                    ZoneId zID = ZoneId.systemDefault();
                    LocalDate ld = LocalDate.of(Integer.parseInt(da[2]), Integer.parseInt(da[0]),
                            Integer.parseInt(da[1]));
                    Date d = Date.from(ld.atStartOfDay(zID).toInstant());
                    LibrarySong ls = new LibrarySong(s, r, d);
                    appendCSVS(accountName, ls);
                }
                break;
            }
        }
    }

    public void addSong(String accountName, Song song, Database db, int r, String a) throws IOException {
        if (r == 0 && a.equals("0")) {
            LibrarySong ls = new LibrarySong(song);
            appendCSVS(accountName, ls);
        } else if (r > 0 && a.equals("0")) {
            LibrarySong ls = new LibrarySong(song, r);
            appendCSVS(accountName, ls);
        } else if (r == 0 && !a.equals("0")) {
            String[] da = a.split("-");
            ZoneId zID = ZoneId.systemDefault();
            LocalDate ld = LocalDate.of(Integer.parseInt(da[2]), Integer.parseInt(da[0]), Integer.parseInt(da[1]));
            Date d = Date.from(ld.atStartOfDay(zID).toInstant());
            LibrarySong ls = new LibrarySong(song, d);
            appendCSVS(accountName, ls);
        } else if (r > 0 && !a.equals("0")) {
            String[] da = a.split("-");
            ZoneId zID = ZoneId.systemDefault();
            LocalDate ld = LocalDate.of(Integer.parseInt(da[2]), Integer.parseInt(da[0]), Integer.parseInt(da[1]));
            Date d = Date.from(ld.atStartOfDay(zID).toInstant());
            LibrarySong ls = new LibrarySong(song, r, d);
            appendCSVS(accountName, ls);
        }
    }

    public void addRelease(String accountName, String release, Database db, String a) throws IOException {
        for (int i = 0; i < db.getReleases().size(); i++) {
            Release r = db.getReleases().get(i);
            if (r.getGUID().equals(release) || r.getName().equals(release)) {
                if (a.equals("0")) {
                    LibraryRelease lr = new LibraryRelease(r);
                    appendCSVR(accountName, lr);
                } else if (!a.equals("0")) {
                    String[] da = a.split("-");
                    ZoneId zID = ZoneId.systemDefault();
                    LocalDate ld = LocalDate.of(Integer.parseInt(da[2]), Integer.parseInt(da[0]),
                            Integer.parseInt(da[1]));
                    Date d = Date.from(ld.atStartOfDay(zID).toInstant());
                    LibraryRelease lr = new LibraryRelease(r, d);
                    appendCSVR(accountName, lr);
                }
                break;
            }
        }
    }

    public void addRelease(String accountName, Release release, Database db, String a) throws IOException {
        if (a.equals("0")) {
            LibraryRelease lr = new LibraryRelease(release);
            appendCSVR(accountName, lr);
        } else if (!a.equals("0")) {
            String[] da = a.split("-");
            ZoneId zID = ZoneId.systemDefault();
            LocalDate ld = LocalDate.of(Integer.parseInt(da[2]), Integer.parseInt(da[0]),
                    Integer.parseInt(da[1]));
            Date d = Date.from(ld.atStartOfDay(zID).toInstant());
            LibraryRelease lr = new LibraryRelease(release, d);
            appendCSVR(accountName, lr);
        }
    }

    public void appendCSVS(String accountName, LibrarySong ls) throws IOException {
        FileWriter fws = new FileWriter("lib[" + accountName + "]-Songs.csv", true);
        FileWriter fwa = new FileWriter("lib[" + accountName + "]-Artists.csv", true);
        BufferedReader brA = new BufferedReader(new FileReader("lib[" + accountName + "]-Artists.csv"));

        fws.append(ls.getGUID());
        fws.append(",");
        fws.append(ls.artist.getGUID());
        fws.append(",");
        fws.append(String.valueOf(ls.getDuration()));
        fws.append(",");
        fws.append(ls.getTitle());
        fws.append(",");
        fws.append(String.valueOf(ls.getRating()));
        fws.append(",");
        fws.append(sdf.format(ls.getAcquisitionDate()));
        fws.append("\n");

        boolean match = false;
        String aline = brA.readLine();
        while ((aline) != null) {
            String[] a = aline.split(",(?! )");
            if (a[0].equals(ls.artist.getGUID())) {
                match = true;
            }
            aline = brA.readLine();
        }

        if (match == false) {
            fwa.append(ls.artist.getGUID());
            fwa.append(",");
            fwa.append(ls.artist.getName());
            fwa.append(",");
            fwa.append(ls.artist.getType());
            fwa.append("\n");
        }

        brA.close();
        fws.close();
        fwa.close();
    }

    public void appendCSVR(String accountName, LibraryRelease lr) throws IOException {
        FileWriter fwr = new FileWriter("lib[" + accountName + "]-Releases.csv", true);

        fwr.append(lr.getGUID());
        fwr.append(",");
        fwr.append(lr.artist.getGUID());
        fwr.append(",");
        fwr.append(lr.getName());
        fwr.append(",");
        fwr.append(lr.getMedium());
        fwr.append(",");
        fwr.append(sdf.format(lr.getReleaseDate()));
        fwr.append(",");
        fwr.append(sdf.format(lr.getAcquisitionDate()));

        for (int i = 0; i < lr.getTrackList().size(); i++) {
            fwr.append(",");
            fwr.append(lr.getTrackList().get(i).getGUID());
        }

        fwr.append("\n");

        fwr.close();
    }
}

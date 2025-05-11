package com.jampod;

import java.io.File;
import java.nio.file.Path;

public interface CSVManager {
    public int loadArtists(File file);
    public int loadSongs(File file);
    public int loadReleases(File file);
    public Path exportDatabase(Database db);
}
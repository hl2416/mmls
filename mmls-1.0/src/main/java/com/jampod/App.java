package com.jampod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.opencsv.exceptions.CsvException;

import javax.lang.model.util.ElementScanner14;

import java.io.FileWriter;
import java.io.IOException;

import com.jampod.Inputs.CLI;

/**
 * Entry Point of the Application
 */
public class App {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] args) throws IOException, CsvException{
        MySQLDatabase db = new MySQLDatabase();
        db.connect();
        List<Artist> onlineArtistList = db.getArtists();
        List<Song> onlineSongList = db.getSongs();
        List<Release> onlineReleaseList = db.getReleases();
        db.close();
        Database localDB = new Database(onlineSongList, onlineReleaseList, onlineArtistList);
        // Database localDB = new Database();
        localDB.loadArtists(new File("artists.csv"));
        localDB.loadSongs(new File("songs.csv"));
        localDB.loadReleases(new File("releases.csv"));
        Scanner userPrompt = new Scanner(System.in);
        System.out.println("Please enter your account name");
        String accountName = userPrompt.nextLine();
        FileWriter fws = new FileWriter("lib[" + accountName + "]-Songs.csv", true);
        FileWriter fwa = new FileWriter("lib[" + accountName + "]-Artists.csv", true);
        FileWriter fwr = new FileWriter("lib[" + accountName + "]-Releases.csv", true);
        fws.close();
        fwa.close();
        fwr.close();
        System.out.println("Hi " + accountName);
        PersonalLibraryProxy personalLibrary = new PersonalLibraryProxy(accountName);
        LibraryAction libA = new LibraryAction();
        AddItem aI = new AddItem();
        RemoveItem rI = new RemoveItem();

        System.out.println("Welcome to MMLS!\nTo get you started, here are a few commands:");
        String helpCommand = "\n\nTo learn more about using each command you can do command -help (ex. search -help)\n"
                + ANSI_BLUE +
                "help" + ANSI_RESET + " - a list of commands and help page will appear\n" + ANSI_BLUE +
                "search" + ANSI_RESET + " - search from the database or your library\n" + ANSI_BLUE +
                "add" + ANSI_RESET + " - add to your library\n" + ANSI_BLUE +
                "remove" + ANSI_RESET + " - remove from your library\n" + ANSI_BLUE +
                "undo" + ANSI_RESET + " - undo library action\n" + ANSI_BLUE +
                "redo" + ANSI_RESET + " - redo library action\n" + ANSI_BLUE;// +
                // "export" + ANSI_RESET + " - export your library to CSV\n" + ANSI_BLUE +
                // "rate" + ANSI_RESET + " - rate a song in your library";
        Boolean end = false;
        while (!end) {
            System.out.println(helpCommand);
            System.out.println("Enter a command or enter " + ANSI_RED + "'quit'" + ANSI_RESET + ": ");
            String command = userPrompt.nextLine();
            String[] commandSpecs = command.split(" ");
            if (commandSpecs.length == 0) {
                System.out.println("INVALID COMMAND");
            }
            switch (commandSpecs[0]) {
                case "quit":
                    userPrompt.close();
                    System.out.println("Goodbye!");
                    end = true;
                    break;
                case "help":
                    System.out.println(helpCommand);
                    break;
                case "search":
                    // CORE SEARCH WORKFLOW:
                    // TARGET -> ITEM -> SUBJECT -> EXTRA(?) -> ARGUMENT
                    // Search TARGET for ITEMs which SUBJECT meet (EXTRA: min/max) ARGUEMENT specification.
                    Boolean inProgressSearch = true;
                    while (inProgressSearch) {
                        Boolean inProgressSearchTarget = true;
                        String target = null;
                        System.out.println("Starting search. Options: library, database, help, quit");
                        while (inProgressSearch && inProgressSearchTarget) {
                            String targetValidate = userPrompt.nextLine();
                            switch (targetValidate) {
                                case "help":
                                    System.out.println(
                                            "The search command has 3 steps. First, select the target to search (database or your library)\nThen, select what you would like to search... song, artist, release\nThen specify the parameters of the search");
                                    break;
                                case "library":
                                case "database":
                                    target = targetValidate;
                                    inProgressSearchTarget = false;
                                    break;
                                case "quit":
                                    inProgressSearchTarget = false;
                                    inProgressSearch = false;
                                    break;
                                default:
                                    System.out.println("Invalid option! Options: library, database, help, quit");
                            }
                        }
                        Boolean inProgressSearchItem = true;
                        String item = "";
                        if(inProgressSearch) {
                            System.out.println("Searching in "+target+"\nWhat are you looking for? artist, song, release");
                        }
                        while (inProgressSearch && inProgressSearchItem) {
                            String itemValidate = userPrompt.nextLine();
                            switch(itemValidate) {
                                case "artist":
                                case "release":
                                case "song":
                                    item = itemValidate;
                                    inProgressSearchItem = false;
                                    break;
                                case "quit":
                                    inProgressSearchItem = false;
                                    inProgressSearch = false;
                                    break;
                                default:
                                    System.out.println("Invalid option! Options: song, release, artist, quit");
                                    break;
                            }
                        }
                        ArrayList<String> options = new ArrayList<String>();
                        options.add("GUID");
                        switch(item) {
                            case "artist":
                                options.add("name");
                                options.add("type");
                                if(target.equals("library")) {
                                    options.add("rating");
                                }
                                break;
                            case "release":
                                options.add("title");
                                options.add("artist_GUID");
                                options.add("artist_name");
                                options.add("song_GUID");
                                options.add("total_duration");
                                if(target.equals("library")) {
                                    options.add("rating");
                                }
                                break;
                            case "song":
                                options.add("title");
                                options.add("artist_GUID");
                                options.add("artist_name");
                                options.add("song_GUID");
                                options.add("duration");
                                if(target.equals("library")) {
                                    options.add("rating");
                                }
                                break;
                            default:
                                break;
                        }
                        options.add("quit");
                        Boolean inProgressSearchSubject = true;
                        String subject = null;
                        if(inProgressSearch) {
                            System.out.println("Searching for "+item+" in "+target);
                        }
                        while(inProgressSearch && inProgressSearchSubject) {
                            System.out.println("Possible options to search for: "+options.toString());
                            String validateSubject = userPrompt.nextLine();
                            if(validateSubject.equals("quit")) {
                                inProgressSearch = false;
                                inProgressSearchSubject = false;
                            } else {
                                Boolean validOption = false;
                                for(String possibleOption : options) {
                                    if(validateSubject.equals(possibleOption)) {
                                        subject = validateSubject;
                                        validOption =  true;
                                        inProgressSearchSubject = false;
                                    }
                                }
                                if(!validOption) {
                                    System.out.println("Invalid Search Option");
                                }
                            }
                        }
                        String extra = null;
                        if(inProgressSearch && (subject.equals("rating") || subject.equals("duration") || subject.equals("total_duration"))) {
                            Boolean inProgressSearchExtra = true;
                            System.out.println("Searching by min or max?");
                            while(inProgressSearchExtra) {
                                String extraValidate = userPrompt.nextLine();
                                if(extraValidate.equals("quit")) {
                                    inProgressSearchExtra = false;
                                    inProgressSearch = false;
                                } else if(extraValidate.equals("min") || extraValidate.equals("max")) {
                                    extra = extraValidate;
                                } else {
                                    System.out.println("Invalid Search Option. min, max, or quit");
                                }
                            }
                        }
                        String argument = null;
                        if(inProgressSearch) {
                            System.out.println("Searching for "+item+" in "+target+" by "+subject+"\nEnter search term");
                            argument = userPrompt.nextLine();
                            Database dataset;
                            if(target.equals("library")) {
                                dataset = personalLibrary.getLibrary();
                            } else {
                                dataset = localDB;
                            }
                            Search searchMethod = null;
                            String searchQuery = null;
                            List results = null;
                            if(subject.equals("rating")) {
                                // special library search
                            } else {
                                switch(subject) {
                                    case "title":
                                        searchMethod = new SearchTitle();
                                        break;
                                    case "duration":
                                    case "total_duration":
                                        searchQuery = item+"~"+argument;
                                        searchMethod = new SearchDuration();
                                        break;
                                    case "guid":
                                        searchQuery = item+"~"+argument;
                                        searchMethod = new SearchGUID();
                                        break;
                                    case "song_GUID":
                                        // !FIX THIS, IT SEARCHES SONGS, NOT RELEASES
                                        searchQuery = "song~"+argument;
                                        searchMethod = new SearchGUID();
                                        break;
                                    case "artist_GUID":
                                        // !FIX THIS, IT SEARCHES ARTISTS, NOT RELEASES
                                        searchQuery = "artist~"+argument;
                                        searchMethod = new SearchGUID();
                                        break;
                                    case "type":
                                        searchQuery = argument;
                                        searchMethod = new SearchType();
                                    case "name":
                                        searchQuery = item+"~"+argument;
                                        searchMethod = new SearchName();
                                        break;
                                    case "artist_name":
                                        // !FIX THIS, IT SEARCHES ARTISTS, NOT RELEASES
                                        searchQuery = "artist~"+argument;
                                        searchMethod = new SearchName();
                                        break;
                                }
                                results = searchMethod.search(dataset, searchQuery);
                            }
                            System.out.println("Results for "+item+" in "+target+" by "+subject+" with term "+argument);
                            if(results.size() > 0) {
                                // ArrayList<String> actions = new ArrayList<String>();
                                // if(target.equals("library")) {
                                //     switch(item) {
                                //         case "release":
                                //         case "artist":
                                //             actions.add("explore");
                                //             actions.add("remove");
                                //             break;
                                //         case "song":
                                //             actions.add("remove");
                                //             actions.add("rate");
                                //             break;
                                //     }
                                // } else {
                                //     switch(item) {
                                //         case "release":
                                //             actions.add("explore");
                                //             actions.add("add");
                                //             break;
                                //         case "artist":
                                //             actions.add("explore");
                                //             break;
                                //         case "song":
                                //             actions.add("add");
                                //             break;
                                //     }
                                // }
                                // actions.add("quit");
                                for(int i = 0; i < results.size(); i++) {
                                    System.out.println(results.get(i));
                                    // System.out.println(i + " | " + results.get(i));
                                }
                                // boolean searchActionInProgress = true;
                                // String actionSelected = null;
                                // while(searchActionInProgress) {
                                //     System.out.println("\nWhat would you like to do with these results?\nAvailable actions: "+actions);
                                //     String validateAction = userPrompt.nextLine();
                                //     if(validateAction.equals("quit")) {
                                //         searchActionInProgress = false;
                                //         inProgressSearch = false;
                                //     } else {
                                //         Boolean validAction = false;
                                //         for(String possibleAction : actions) {
                                //             if(validateAction.equals(possibleAction)) {
                                //                 actionSelected = validateAction;
                                //                 validAction =  true;
                                //             }
                                //         }
                                //         if(!validAction) {
                                //             System.out.println("Invalid Action");
                                //         } else {
                                //             System.out.println("Select # that you would like to "+actionSelected);
                                //             int itemForAction = userPrompt.nextInt();
                                //             userPrompt.nextLine();
                                //             if(itemForAction < 0 || itemForAction > results.size()-1) {
                                //                 System.out.println("Invalid selection!");
                                //             } else {
                                //                 switch(actionSelected) {
                                //                     case "add":
                                //                         if(item.equals("song")) {
                                //                             aI.addSong(personalLibrary.user, (Song)results.get(itemForAction), dataset, 0, "0");
                                //                         } else {
                                //                             aI.addRelease(personalLibrary.user, (Release)results.get(itemForAction), dataset, "0");
                                //                         }
                                //                     case "explore":
                                                        
                                //                     case "remove":
                                //                         RemoveItem remove = new RemoveItem();
                                //                         if(item.equals("song")) {
                                //                             rI.removeSong(personalLibrary.user, (Song)results.get(itemForAction), dataset, 0, "0");
                                //                         } else {
                                //                             rI.removeRelease(personalLibrary.user, (Release)results.get(itemForAction), dataset, "0");
                                //                         }
                                //                     case "rate":
                                //                         System.out.println("Doing: "+actionSelected + " " + itemForAction);
                                //                         System.out.println(results.get(itemForAction));
                                //                         break;
                                //                 }
                                //             }
                                //         }
                                //     }
                                // }
                            } else {
                                System.out.println("No Results!");
                            }
                        }

                    }
                    break;
                case "add":
                    boolean adding = true;
                    while(adding)
                    {
                        System.out.println("Would you like to add a Song or Release? (Enter song or release for adding or quit to exit)");
                        String which = userPrompt.nextLine();
                        switch(which)
                        {
                            case "song":
                                System.out.println("What song would you like to add? (Enter either the GUID or Title)");
                                String song = userPrompt.nextLine();
                                boolean f1 = false;
                                for(int i = 0; i < localDB.getSongs().size(); i++)
                                {
                                    if(localDB.getSongs().get(i).getGUID().equals(song) || localDB.getSongs().get(i).getTitle().equals(song))
                                    {
                                        f1 = true;
                                        System.out.println("Would you like to rate this song? (Enter a rating 1 to 5 or 0 if no)");
                                        String rate = userPrompt.nextLine();
                                        if(Integer.parseInt(rate) > 5)
                                        {
                                            System.out.println("Please enter a number between 1 to 5 or 0 if no");
                                        }
                                        else if(Integer.parseInt(rate) >= -1 && Integer.parseInt(rate) <= 5)
                                        {
                                            System.out.println("Would you like to enter an acquisition date? (Enter with format mm-dd-yyyy and 0 if no)");
                                            String date = userPrompt.nextLine();
                                            String[] d = date.split("-");
                                            if(date.equals("0") || d.length == 3)
                                            {
                                                int r = Integer.parseInt(rate);
                                                aI.addSong(accountName, song, localDB, r, date);
                                                libA.index += 1;
                                                libA.command.add(libA.index,"addS");
                                                libA.aName.add(libA.index, accountName);
                                                libA.type.add(libA.index, song);
                                                libA.db.add(libA.index,localDB);
                                                libA.rate.add(libA.index, rate);
                                                libA.date.add(libA.index, date);
                                                try
                                                {
                                                    libA.command.subList(libA.index + 1, libA.command.size() - 1);
                                                    libA.aName.subList(libA.index + 1, libA.command.size() - 1);
                                                    libA.type.subList(libA.index + 1, libA.command.size() - 1);
                                                    libA.db.subList(libA.index + 1, libA.command.size() - 1);
                                                    libA.rate.subList(libA.index + 1, libA.command.size() - 1);
                                                    libA.date.subList(libA.index + 1, libA.command.size() - 1);
                                                }
                                                catch(Exception e)
                                                {

                                                }
                                                break;
                                            }
                                            else
                                            {
                                                System.out.println("Please enter in the format mm-dd-yyyy");
                                                break;
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("Please enter a number between 1 to 5 or 0 if no");
                                        }
                                    }
                                }
                                if(f1 == false)
                                {
                                    System.out.println("No such song found in Database");
                                }
                                break;
                            case "release":
                                System.out.println("What release would you like to add? (Enter either the GUID or Title)");
                                String release = userPrompt.nextLine();
                                boolean f2 = false;
                                for(int i = 0; i < localDB.getReleases().size(); i++)
                                {
                                    if(localDB.getReleases().get(i).getGUID().equals(release) || localDB.getReleases().get(i).getName().equals(release))
                                    {
                                        f2 = true;
                                        System.out.println("Would you like to enter an acquisition date? (Enter with format mm-dd-yyyy and 0 if no)");
                                        String date = userPrompt.nextLine();
                                        String[] d = date.split("-");
                                        if(date.equals("0") || d.length == 3)
                                        {
                                            aI.addRelease(accountName, release, localDB, date);
                                            libA.index += 1;
                                            libA.command.add(libA.index,"addR");
                                            libA.aName.add(libA.index, accountName);
                                            libA.type.add(libA.index, release);
                                            libA.db.add(libA.index,localDB);
                                            libA.rate.add(libA.index, "0");
                                            libA.date.add(libA.index, date);
                                            try
                                            {
                                                libA.command.subList(libA.index + 1, libA.command.size() - 1);
                                                libA.aName.subList(libA.index + 1, libA.command.size() - 1);
                                                libA.type.subList(libA.index + 1, libA.command.size() - 1);
                                                libA.db.subList(libA.index + 1, libA.command.size() - 1);
                                                libA.rate.subList(libA.index + 1, libA.command.size() - 1);
                                                libA.date.subList(libA.index + 1, libA.command.size() - 1);
                                            }
                                            catch(Exception e)
                                            {

                                            }
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Please enter in the format mm-dd-yyyy");
                                        }
                                    }
                                }
                                if(f2 == false)
                                {
                                    System.out.println("No such song found in Database");
                                }
                                break;
                            case "quit":
                                adding = false;
                                break;
                            default:
                                System.out.println("Invalid addition (Please enter either song or release or quit)");
                        }
                    }
                    break;
                case "remove":
                    boolean removing = true;
                    while(removing)
                    {
                        System.out.println("What would you like to remove? (Enter song or release for removal or quit to exit)");
                        String which = userPrompt.nextLine();
                        switch(which)
                        {
                            case "song":
                                System.out.println("What song would you like to remove? (Enter either the GUID or name)");
                                String song = userPrompt.nextLine();
                                rI.removeSong(accountName, song);
                                libA.index += 1;
                                libA.command.add(libA.index,"removeS");
                                libA.aName.add(libA.index, accountName);
                                libA.type.add(libA.index, song);
                                libA.db.add(libA.index,localDB);
                                libA.rate.add(libA.index, "0");
                                libA.date.add(libA.index, "0");
                                try
                                {
                                    libA.command.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.aName.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.type.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.db.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.rate.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.date.subList(libA.index + 1, libA.command.size() - 1);
                                }
                                catch(Exception e)
                                {

                                }
                                break;
                            case "release":
                                System.out.println("What release would you like to remove? (Enter either the GUID or name)");
                                String release = userPrompt.nextLine();
                                rI.removeRelease(accountName, release);
                                libA.index += 1;
                                libA.command.add(libA.index,"removeR");
                                libA.aName.add(libA.index, accountName);
                                libA.type.add(libA.index, release);
                                libA.db.add(libA.index,localDB);
                                libA.rate.add(libA.index, "0");
                                libA.date.add(libA.index, "0");
                                try
                                {
                                    libA.command.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.aName.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.type.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.db.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.rate.subList(libA.index + 1, libA.command.size() - 1);
                                    libA.date.subList(libA.index + 1, libA.command.size() - 1);
                                }
                                catch(Exception e)
                                {

                                }
                                break;
                            case "quit":
                                removing = false;
                                break;
                            default:
                                System.out.println("Invalid Removal (Please enter either song or release or quit)");
                        }
                    }
                    break;
                case "undo":
                    // HAI JIE WORKS HERE
                    libA.undo();
                    break;
                case "redo":
                    // HAI JIE WORKS HERE
                    libA.redo();
                    break;
                case "export":
                    break;
                case "rate":
                    break;
                default:
                    System.out.println("INVALID COMMAND");
                    break;
            }
        }
    }
}

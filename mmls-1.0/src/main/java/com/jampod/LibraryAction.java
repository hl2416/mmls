package com.jampod;

import java.io.IOException;
import java.util.List;

import javax.xml.crypto.Data;

import java.util.ArrayList;
import com.opencsv.exceptions.CsvException;

public class LibraryAction
{
    int index = -1;
    protected List<String> command;
    protected List<String> aName;
    protected List<String> type;
    protected List<Database> db;
    protected List<String> rate;
    protected List<String> date;

    AddItem a = new AddItem();
    RemoveItem r = new RemoveItem();

    public LibraryAction()
    {
        this.command = new ArrayList<String>();
        this.aName = new ArrayList<String>();
        this.type = new ArrayList<String>();
        this.db = new ArrayList<Database>();
        this.rate = new ArrayList<String>();
        this.date = new ArrayList<String>();
    }

    public void redo() throws IOException, CsvException
    {
        if(this.command == null)
        {
            System.out.println("Nothing to redo!");
        }
        else
        {
            try
            {
                index += 1;
                if(command.get(index).equals("addS"))
                {
                    a.addSong(aName.get(index), type.get(index), db.get(index), Integer.parseInt(rate.get(index)), date.get(index));
                }
                else if(command.get(index).equals("addR"))
                {
                    a.addRelease(aName.get(index), type.get(index), db.get(index), date.get(index));
                }
                else if(command.get(index).equals("removeS"))
                {
                    r.removeSong(aName.get(index), type.get(index));
                }
                else if(command.get(index).equals("removeR"))
                {
                    r.removeRelease(aName.get(index), type.get(index));
                }
            }
            catch(IndexOutOfBoundsException e)
            {
                System.out.println("Nothing to redo!");
            }
        }
    }

    public void undo() throws IOException, CsvException
    {
        if(index == -1)
        {
            System.out.println("Nothing to undo!");
        }
        else
        {
            if(command.get(index).equals("addS"))
            {
                r.removeSong(aName.get(index), type.get(index));
            }
            else if(command.get(index).equals("addR"))
            {
                r.removeRelease(aName.get(index), type.get(index));
            }
            else if(command.get(index).equals("removeS"))
            {
                a.addSong(aName.get(index), type.get(index), db.get(index), Integer.parseInt(rate.get(index)), date.get(index));
            }
            else if(command.get(index).equals("removeR"))
            {
                a.addRelease(aName.get(index), type.get(index), db.get(index), date.get(index));
            }
            index -= 1;
        }
    }
}

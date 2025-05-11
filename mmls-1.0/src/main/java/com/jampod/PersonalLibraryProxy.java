package com.jampod;

public class PersonalLibraryProxy {
    String user;
    PersonalLibrary library;

    public PersonalLibraryProxy(String user) {
        this.user = user;
        this.library = new PersonalLibrary(user);
    }

    // public void execute(LibraryAction action) {
    //     library.execute(action);
    // }

    public PersonalLibrary getLibrary() {
        return library;
    }
}

package com.taqtik.lab.bookshopper.main;

import android.app.Application;

public class BookApplication extends Application {

    public String urlScheme = "com.taqtik.lab.BookShopper";
    public String webBaseURL = "https://lab.taqtik.com/api/";

    @Override
    public void onCreate() {
        super.onCreate();


    }
}

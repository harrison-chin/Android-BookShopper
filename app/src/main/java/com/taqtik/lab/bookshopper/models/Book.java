package com.taqtik.lab.bookshopper.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public String id;
    public String title;
    public Author author;
    public String price;

    private String TAG = "MainActivity";

    public Book() {

    }

    public Book(String id, String title, Author author, String price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.author.id);
        dest.writeString(this.author.family_name);
        dest.writeString(this.author.first_name);
        dest.writeString(this.author.date_of_birth);
        dest.writeString(this.price);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    // "De-parcel object
    public Book(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.author.id = in.readString();
        this.author.family_name = in.readString();
        this.author.first_name = in.readString();
        this.author.date_of_birth = in.readString();
        this.price = in.readString();
    }
}

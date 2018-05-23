package com.taqtik.lab.bookshopper.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable {
    public String id;
    public String family_name;
    public String first_name;
    public String date_of_birth;

    private String TAG = "MainActivity";

    public Author() {

    }

    public Author(String id, String family_name, String first_name, String date_of_birth) {
        this.id = id;
        this.family_name = family_name;
        this.first_name = first_name;
        this.date_of_birth = date_of_birth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.family_name);
        dest.writeString(this.first_name);
        dest.writeString(this.date_of_birth);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    // "De-parcel object
    public Author(Parcel in) {
        this.id = in.readString();
        this.family_name = in.readString();
        this.first_name = in.readString();
        this.date_of_birth = in.readString();
    }
}

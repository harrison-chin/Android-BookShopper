package com.taqtik.lab.bookshopper.main;

import android.app.Application;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class BookApplication extends Application {

    public String urlScheme = "com.taqtik.lab.BookShopper";
    public String webBaseURL = "https://lab.taqtik.com/api/";

    // user preferences
    public String userFirstName = "";
    public String userLastName = "";
    public String userEmail = "";
    public String userPhone = "";

    @Override
    public void onCreate() {
        super.onCreate();

        getUserProfile();
    }

    // Faviorite Packages List
    public void saveUserProfile() {
        String jsonString = "";
        jsonString += "{\n";
        jsonString += ("\"firstName\": " + "\"" + this.userFirstName + "\",");
        jsonString += ("\"lastName\": " + "\"" + this.userLastName + "\",");
        jsonString += ("\"email\": " + "\"" + this.userEmail + "\",");
        jsonString += ("\"phone\": " + "\"" + this.userPhone + "\"");
        jsonString += "}";

        SharedPreferences preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Key_UserProfile", jsonString);
        editor.commit();
    }

    public void getUserProfile() {
        SharedPreferences preferences = this.getSharedPreferences("Preferences", this.MODE_PRIVATE);
        String jsonString = preferences.getString("Key_UserProfile", "");
        if (!jsonString.isEmpty()) {
            try {
                JSONObject reader = new JSONObject(jsonString);
                if (reader.has("firstName")) {
                    this.userFirstName = reader.getString("firstName");
                }
                if (reader.has("lastName")) {
                    this.userLastName = reader.getString("lastName");
                }
                if (reader.has("email")) {
                    this.userEmail = reader.getString("email");
                }
                if (reader.has("phone")) {
                    this.userPhone = reader.getString("phone");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

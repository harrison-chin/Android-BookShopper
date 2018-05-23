package com.taqtik.lab.bookshopper.activities.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.taqtik.lab.bookshopper.R;
import com.taqtik.lab.bookshopper.fragments.books.BooksFragment;
import com.taqtik.lab.bookshopper.main.BookApplication;
import com.taqtik.lab.bookshopper.models.Author;
import com.taqtik.lab.bookshopper.models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private ArrayList<Book> books = new ArrayList<Book>();

    private BooksFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            this.fragment = new BooksFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("books", this.books);
            this.fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.listcontainer, this.fragment).commit();
        }
        getBooks();
    }

    private void getBooks() {
        BookApplication mApp = ((BookApplication)this.getApplicationContext());
        String urlString = mApp.webBaseURL + "books";

        this.books.clear();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "getBooks response: " + response);
                        // Convert JSON String to Map
                        if(response != JSONObject.NULL) {
                            try {
                                JSONArray reader = new JSONArray(response);
                                for (int i = 0; i < reader.length(); i++) {
                                    JSONObject bookJSON = (JSONObject) reader.get(i);
                                    Book book  = new Book();
                                    if (bookJSON.has("_id")) {
                                        book.id = bookJSON.getString("_id");
                                    }
                                    if (bookJSON.has("title")) {
                                        book.title = bookJSON.getString("title");
                                    }
                                    if (bookJSON.has("author")) {
                                        JSONObject authorJSON = bookJSON.getJSONObject("author");
                                        Author author = new Author();
                                        if (authorJSON.has("_id")) {
                                            author.id = authorJSON.getString("_id");
                                        }
                                        if (authorJSON.has("family_name")) {
                                            author.family_name = authorJSON.getString("family_name");
                                        }
                                        if (authorJSON.has("first_name")) {
                                            author.first_name = authorJSON.getString("first_name");
                                        }
                                        if (authorJSON.has("date_of_birth")) {
                                            author.date_of_birth = authorJSON.getString("date_of_birth");
                                        }
                                        book.author = author;
                                    }
                                    if (bookJSON.has("price")) {
                                        book.price = bookJSON.getString("price");
                                    }
                                    books.add(book);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i(TAG, "onError : " + e.getMessage());
                            }
                            fragment.reloadListView();
                        }
                        Log.i(TAG, "Books count: " + books.size());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "getBooks error: " + error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

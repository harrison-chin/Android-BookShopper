package com.taqtik.lab.bookshopper.fragments.books;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.taqtik.lab.bookshopper.R;
import com.taqtik.lab.bookshopper.adapters.books.BooksRecyclerAdapter;
import com.taqtik.lab.bookshopper.models.Book;

import java.util.ArrayList;

public class BooksFragment extends Fragment {
    private String TAG = "MainActivity.Meteor";

    private ListView listView;
    public BooksRecyclerAdapter adapter = null;

    private ArrayList<Book> books = new ArrayList<Book>();

    public BooksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.books = bundle.getParcelableArrayList("books");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);

        this.listView = (ListView)rootView.findViewById(R.id.list_view);

        this.adapter= new BooksRecyclerAdapter(this.books, getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book item = books.get(position);
                // SHow Book details
            }
        });
        return rootView;
    }
}

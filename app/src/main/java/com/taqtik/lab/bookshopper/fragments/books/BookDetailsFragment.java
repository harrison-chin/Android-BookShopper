package com.taqtik.lab.bookshopper.fragments.books;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.taqtik.lab.bookshopper.R;
import com.taqtik.lab.bookshopper.activities.books.BookDetailsActivity;
import com.taqtik.lab.bookshopper.models.Book;

public class BookDetailsFragment extends Fragment {
    private String TAG = "MainActivity.Meteor";

    private TextView title_textView;
    private TextView author_textView;
    private TextView description_textView;
    private Button checkout_button;
    private Book book;
    public Context mContext;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = this.getContext();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.book = bundle.getParcelable("book");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_book_details, container, false);

        this.title_textView = (TextView)rootView.findViewById(R.id.title_textView);
        this.author_textView = (TextView)rootView.findViewById(R.id.author_textView);
        this.description_textView = (TextView)rootView.findViewById(R.id.description_textView);
        this.checkout_button = (Button) rootView.findViewById(R.id.checkout_button);
        this.checkout_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BookDetailsActivity bookDetailsActivity = (BookDetailsActivity)getActivity();
                bookDetailsActivity.showBraintreePayActivity();
            }
        });

        if (this.book != null) {
            this.title_textView.setText(this.book.title);
            String authorText = "Author: " + (this.book.author.first_name + " " + this.book.author.family_name);
            this.author_textView.setText(authorText);
            String descriptionText = "Description:\nBook ID: " + this.book.id;
            this.description_textView.setText(descriptionText);
        }

        return rootView;
    }


}

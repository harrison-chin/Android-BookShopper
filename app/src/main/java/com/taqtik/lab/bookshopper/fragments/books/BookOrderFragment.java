package com.taqtik.lab.bookshopper.fragments.books;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taqtik.lab.bookshopper.R;
import com.taqtik.lab.bookshopper.activities.books.BookOrderActivity;
import com.taqtik.lab.bookshopper.main.BookApplication;
import com.taqtik.lab.bookshopper.models.Book;

public class BookOrderFragment extends Fragment {
    private String TAG = "MainActivity.Meteor";

    private ProgressBar mProgressBar;
    private TextView title_textView;
    private TextView author_textView;
    private TextView price_textView;
    private TextView firstName_textView;
    private EditText firstName_editText;
    private TextView lastName_textView;
    private EditText lastName_editText;
    private TextView email_textView;
    private EditText email_editText;
    private Button place_order_button;
    private Book book;
    private String nonce;
    private String pay_type;
    private String pay_description;

    private String firstName;
    private String lastName;
    private String email;

    public Context mContext;

    public BookOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this.getContext();

        BookApplication mApp = ((BookApplication) this.getActivity().getApplicationContext());
        this.firstName = mApp.userFirstName;
        this.lastName = mApp.userLastName;
        this.email = mApp.userEmail;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.book = bundle.getParcelable("book");
            this.nonce = bundle.getString("nonce");
            this.pay_type = bundle.getString("type");
            this.pay_description = bundle.getString("description");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book_order, container, false);

        this.mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        this.title_textView = (TextView)rootView.findViewById(R.id.title_textView);
        this.author_textView = (TextView)rootView.findViewById(R.id.author_textView);
        this.price_textView = (TextView)rootView.findViewById(R.id.price_textView);
        this.firstName_textView = (TextView)rootView.findViewById(R.id.firstName_textView);
        this.firstName_editText = (EditText)rootView.findViewById(R.id.firstName_editText);
        this.lastName_textView = (TextView)rootView.findViewById(R.id.lastName_textView);
        this.lastName_editText = (EditText)rootView.findViewById(R.id.lastName_editText);
        this.email_textView = (TextView)rootView.findViewById(R.id.email_textView);
        this.email_editText = (EditText)rootView.findViewById(R.id.email_editText);

        this.place_order_button = (Button) rootView.findViewById(R.id.place_order_button);
        this.place_order_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getUserInformation()) {
                    BookOrderActivity bookOrderActivity = (BookOrderActivity) getActivity();
                    bookOrderActivity.sendRequestPaymentToServer(nonce, book.price, firstName, lastName, email);
                }
            }
        });

        if (this.book != null) {
            this.title_textView.setText(this.book.title);
            String authorText = "Author: " + (this.book.author.first_name + " " + this.book.author.family_name);
            this.author_textView.setText(authorText);
            String paymentText = String.format("Pay $%s by %s", this.book.price, this.pay_type);
            if (this.pay_description != null && (this.pay_description.length()>0)) {
                paymentText = String.format("%s (%s)", paymentText, this.pay_description);
            }
            this.price_textView.setText(paymentText);
        }

        this.firstName_editText.setText(this.firstName);
        this.lastName_editText.setText(this.lastName);
        this.email_editText.setText(this.email);

        this.showProgressBar(false);
        return rootView;
    }

    private boolean getUserInformation() {
        this.firstName = this.firstName_editText.getText().toString().trim();
        this.lastName = this.lastName_editText.getText().toString().trim();
        this.email = this.email_editText.getText().toString().trim();
        if (this.firstName.length()==0) {
            this.firstName_editText.requestFocus();
            return false;
        }
        if (this.lastName.length()==0) {
            this.lastName_editText.requestFocus();
            return false;
        }
        if (this.email.length()==0) {
            this.email_editText.requestFocus();
            return false;
        }

        BookApplication mApp = ((BookApplication) this.getActivity().getApplicationContext());
        mApp.userFirstName = this.firstName;
        mApp.userLastName = this.lastName;
        mApp.userEmail = this.email;
        mApp.saveUserProfile();

        return true;
    }

    public void showProgressBar(boolean show) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(show ? View.VISIBLE: View.INVISIBLE);
        }
    }

    public void enablePlaceOrderButton(boolean enable) {
        place_order_button.setEnabled(enable);
    }
}

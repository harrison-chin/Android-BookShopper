package com.taqtik.lab.bookshopper.activities.books;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.taqtik.lab.bookshopper.R;
import com.taqtik.lab.bookshopper.fragments.books.BookDetailsFragment;
import com.taqtik.lab.bookshopper.models.Book;

public class BookDetailsActivity extends AppCompatActivity {
    private String TAG = "MainActivity.Meteor";

    public static final int DROP_IN_REQUEST = 1;
    private String clientTokenOrTokenizationKey = "";

    private Book book;

    private BookDetailsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        this.book = intent.getParcelableExtra("book");
        if (this.book != null) {
           if (savedInstanceState == null) {
               this.fragment = new BookDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("book", book);
               this.fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.viewcontainer, this.fragment).commit();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    public void showBraintreePayActivity() {
        this.fragment.showProgressBar(true);
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(this.clientTokenOrTokenizationKey);

        startActivityForResult(dropInRequest.getIntent(this), DROP_IN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DROP_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                this.fragment.showProgressBar(false);
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String nonce = result.getPaymentMethodNonce().getNonce();
                String description = result.getPaymentMethodNonce().getDescription();
                String type = result.getPaymentMethodType().getCanonicalName();
                this.showOrderActivity(this.book, nonce, type, description);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                this.fragment.showProgressBar(false);
                String message = "Transaction Cancelled";
                showMessage("Checkout Status", message);
            } else {
                this.fragment.showProgressBar(false);
                // an error occurred, checked the returned exception
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                String message = exception.getMessage();
                showMessage("Checkout Error", message);
            }
        }
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                    }
                });
        dlgAlert.create().show();
    }

    private void showOrderActivity(Book book, String nonce, String type, String description) {
        Intent bookOrderIntent = new Intent(this, BookOrderActivity.class);
        bookOrderIntent.putExtra("book", book);
        bookOrderIntent.putExtra("nonce", nonce);
        bookOrderIntent.putExtra("type", type);
        bookOrderIntent.putExtra("description", description);

        this.startActivity(bookOrderIntent);
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finishAfterTransition();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}

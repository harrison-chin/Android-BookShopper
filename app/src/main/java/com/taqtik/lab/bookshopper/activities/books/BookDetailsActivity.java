package com.taqtik.lab.bookshopper.activities.books;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.taqtik.lab.bookshopper.R;
import com.taqtik.lab.bookshopper.fragments.books.BookDetailsFragment;
import com.taqtik.lab.bookshopper.main.BookApplication;
import com.taqtik.lab.bookshopper.models.Author;
import com.taqtik.lab.bookshopper.models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class BookDetailsActivity extends AppCompatActivity {
    private String TAG = "MainActivity.Meteor";

    public static final int DROP_IN_REQUEST = 1;
    private String clientTokenOrTokenizationKey = "{key}";
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "John.Doe@test.com";


    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        this.book = intent.getParcelableExtra("book");
        if (this.book != null) {
           if (savedInstanceState == null) {
                BookDetailsFragment fragment = new BookDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("book", book);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.viewcontainer, fragment).commit();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    public void showBraintreePayActivity() {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(this.clientTokenOrTokenizationKey);

        startActivityForResult(dropInRequest.getIntent(this), DROP_IN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DROP_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String nonce = result.getPaymentMethodNonce().getNonce();
                Log.i(TAG, "paymentMethodNonce: " + nonce);
                this.sendRequestPaymentToServer(nonce, this.book.price, this.firstName, this.lastName, this.email);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // canceled
            } else {
                // an error occurred, checked the returned exception
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.i(TAG, "error: " + exception.getMessage());
            }
        }
    }

    private void sendRequestPaymentToServer(String nonce, String amount, String firstName, String lastName, String email) {
        BookApplication mApp = ((BookApplication)this.getApplicationContext());
        String paymentURL = mApp.webBaseURL + "braintreepay";

        try {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("payment_method_nonce", nonce);
            jsonBody.put("amount", amount);
            jsonBody.put("firstName", firstName);
            jsonBody.put("lastName", lastName);
            jsonBody.put("email", email);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, paymentURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String message = "Successfully charged. Thanks for shopping the books!";
                    showSuccessMessage(message);
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            queue.add(stringRequest);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showSuccessMessage(String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Checkout");
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                    }
                });
        dlgAlert.create().show();
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

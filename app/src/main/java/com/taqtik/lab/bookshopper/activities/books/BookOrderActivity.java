package com.taqtik.lab.bookshopper.activities.books;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
import com.taqtik.lab.bookshopper.R;
import com.taqtik.lab.bookshopper.fragments.books.BookOrderFragment;
import com.taqtik.lab.bookshopper.main.BookApplication;
import com.taqtik.lab.bookshopper.models.Book;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class BookOrderActivity extends AppCompatActivity {
    private String TAG = "MainActivity.Meteor";

    private Book book;
    private String nonce;
    private String pay_type;
    private String pay_description;

    private String firstName = "";
    private String lastName = "";
    private String email = "";

    private BookOrderFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_order);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        this.book = intent.getParcelableExtra("book");
        this.nonce = intent.getStringExtra("nonce");
        this.pay_type = intent.getStringExtra("type");
        this.pay_description = intent.getStringExtra("description");
        if (this.book != null) {
            if (savedInstanceState == null) {
                this.fragment = new BookOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("book", this.book);
                bundle.putString("nonce", this.nonce);
                bundle.putString("type", this.pay_type);
                bundle.putString("description", this.pay_description);
                this.fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.viewcontainer, this.fragment).commit();
            }
        }
    }

    public void sendRequestPaymentToServer(String nonce, String amount, String firstName, String lastName, String email) {
        BookApplication mApp = ((BookApplication)this.getApplicationContext());
        String paymentURL = mApp.webBaseURL + "braintreepay";

        try {
            this.fragment.showProgressBar(true);
            this.fragment.enablePlaceOrderButton(false);
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
                    fragment.showProgressBar(false);
                    fragment.enablePlaceOrderButton(true);
                    String message = "Successfully charged. Thanks for shopping the books!";
                    showMessage("Checkout Status", message);
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    fragment.showProgressBar(false);
                    fragment.enablePlaceOrderButton(true);
                    showMessage("Checkout Error", error.toString());
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

    private void showMessage(String title, String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAfterTransition();
                    }
                });
        dlgAlert.create().show();
    }
}

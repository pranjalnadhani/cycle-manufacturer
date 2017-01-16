package com.pranjalnc.cycleshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.product_title)
    TextView productTitle;
    @BindView(R.id.product_price)
    TextView productPrice;
    @BindView(R.id.product_description)
    TextView productDescription;
    @BindView(R.id.image)
    AppCompatImageView productImage;

    SharedPreferences prefs;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(DetailActivity.this,
                        ProgressDialog.THEME_HOLO_DARK);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Adding to cart...");
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.show();

                int quantity = 1;
                int product_id = product.getId();

                JSONObject details = new JSONObject();
                try {
                    details.put("quantity", quantity);
                    details.put("product_id", product_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(DetailActivity.this);
                String url = "https://cycleman.herokuapp.com/cart";

                // Request a string response from the provided URL.
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, details,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Product added to cart!", Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Could not register account!", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "JWT " + prefs.getString("token", ""));
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        if (in == null)
            finish();
        String json = getIntent().getStringExtra("product");
        product = new Gson().fromJson(json, Product.class);

        Glide.with(this).load(product.getLarge_image()).into(productImage);
        productTitle.setText(product.getName());
        productPrice.setText("Rs. " + String.valueOf(product.getPrice()));
        productDescription.setText(product.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        if (prefs.contains("user_id") && prefs.contains("token")) {
            getMenuInflater().inflate(R.menu.menu_logout, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_logout:
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Toast.makeText(getApplicationContext(),
                        "Logged out!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_view_cart:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

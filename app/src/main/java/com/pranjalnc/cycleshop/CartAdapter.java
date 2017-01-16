package com.pranjalnc.cycleshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pranjal on 16/1/17.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    public static final int ADD = 1;
    public static final int REDUCE = 2;
    private Context mContext;
    private List<Cart> cartItemsList;

    public CartAdapter(Context mContext, List<Cart> cartItemsList) {
        this.mContext = mContext;
        this.cartItemsList = cartItemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Cart item = cartItemsList.get(position);

        Double item_price = item.getProduct().getPrice();
        int item_quantity = item.getQuantity();
        Double total_amount = item_price * item_quantity;

        holder.title.setText(item.getProduct().getName());
        holder.price.setText("Rs. " + String.valueOf(item_price));
        holder.quantity.setText(String.valueOf(" x " + item.getQuantity()));
        holder.total.setText("Rs. " + String.valueOf(total_amount));

        Glide.with(mContext).load(item.getProduct().getThumbnail()).into(holder.thumbnail);

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuantity(ADD, item, holder);
                holder.quantity.setText(String.valueOf(" x " + item.getQuantity()));
                holder.total.setText("Rs. " + String.valueOf(
                        item.getQuantity() * item.getProduct().getPrice()
                ));
            }
        });

        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuantity(REDUCE, item, holder);
                holder.quantity.setText(String.valueOf(" x " + item.getQuantity()));
                holder.total.setText("Rs. " + String.valueOf(
                        item.getQuantity() * item.getProduct().getPrice()
                ));
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(mContext);
                String url = "https://cycleman.herokuapp.com/cart/" + item.getId();
                StringRequest request = new StringRequest(
                        Request.Method.DELETE,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(mContext, "Item removed!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, "Some error occured!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "JWT " + prefs.getString("token", ""));
                        return params;
                    }
                };

                queue.add(request);
            }
        });
    }

    private void changeQuantity(int opCode, final Cart item, final MyViewHolder viewHolder) {
        viewHolder.add.setVisibility(View.GONE);
        viewHolder.reduce.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://cycleman.herokuapp.com/cart/" + item.getId();

        int quantity = item.getQuantity();
        switch (opCode) {
            case ADD:
                ++quantity;
                break;
            case REDUCE:
                --quantity;
                break;
        }

        if (quantity > 0) {
            final JSONObject data = new JSONObject();
            try {
                data.put("quantity", item.getQuantity());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final int finalQuantity = quantity;
            StringRequest request = new StringRequest(
                    Request.Method.PUT,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            viewHolder.add.setVisibility(View.VISIBLE);
                            viewHolder.reduce.setVisibility(View.VISIBLE);
                            item.setQuantity(finalQuantity);
                            Toast.makeText(mContext, "Cart updated!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            viewHolder.add.setVisibility(View.VISIBLE);
                            viewHolder.reduce.setVisibility(View.VISIBLE);
                            Toast.makeText(mContext, "Some error occured!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "JWT " + prefs.getString("token", ""));
                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return data.toString().getBytes();
                }
            };

            queue.add(request);
        }
    }

    @Override
    public int getItemCount() {
        return cartItemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, quantity, total;
        public ImageView thumbnail;
        public ImageButton add, reduce, remove;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.cart_title);
            price = (TextView) itemView.findViewById(R.id.cart_price);
            quantity = (TextView) itemView.findViewById(R.id.cart_quantity);
            total = (TextView) itemView.findViewById(R.id.cart_total);
            add = (ImageButton) itemView.findViewById(R.id.add_quantity);
            reduce = (ImageButton) itemView.findViewById(R.id.reduce_quantity);
            thumbnail = (ImageView) itemView.findViewById(R.id.cart_thumbnail);
            remove = (ImageButton) itemView.findViewById(R.id.remove_item);
        }
    }
}

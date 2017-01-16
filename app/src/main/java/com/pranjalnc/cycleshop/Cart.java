package com.pranjalnc.cycleshop;

import android.support.annotation.Nullable;

/**
 * Created by pranjal on 16/1/17.
 */

public class Cart {
    private int id;
    private Product product;
    private Order order;
    private int quantity;
    private int user_id;

    public Cart(int id, @Nullable Product product, @Nullable Order order, int quantity, int user_id) {
        this.id = id;
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Order getOrder() {
        return order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUser_id() {
        return user_id;
    }
}

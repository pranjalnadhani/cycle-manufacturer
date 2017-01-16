package com.pranjalnc.cycleshop;

import org.json.JSONObject;

/**
 * Created by pranjal on 15/1/17.
 */

public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private String thumbnail;
    private String large_image;
    private JSONObject features;
    private int category_id;

    public Product(int id, String name, double price, String description, String thumbnail, String large_image, JSONObject features, int category_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.thumbnail = thumbnail;
        this.large_image = large_image;
        this.features = features;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getLarge_image() {
        return large_image;
    }

    public JSONObject getFeatures() {
        return features;
    }

    public int getCategory_id() {
        return category_id;
    }
}

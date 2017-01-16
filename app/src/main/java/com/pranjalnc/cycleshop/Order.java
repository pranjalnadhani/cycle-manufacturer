package com.pranjalnc.cycleshop;

/**
 * Created by pranjal on 16/1/17.
 */

public class Order {
    private int id;
    private Product wheels;
    private Product seats;
    private boolean gears;
    private int user_id;

    public Order(int id, Product wheels, Product seats, boolean gears, int user_id) {
        this.id = id;
        this.wheels = wheels;
        this.seats = seats;
        this.gears = gears;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public Product getWheels() {
        return wheels;
    }

    public Product getSeats() {
        return seats;
    }

    public boolean isGears() {
        return gears;
    }

    public int getUser_id() {
        return user_id;
    }
}

package com.example.pillpal_v2.Model;

public class User {
    String medicine_name, price, quantity, Inventory_ID;
    private String documentId;
    private String imageUrl;
    String description;

    public User(String medicine_name, String price, String quantity, String inventory_ID, String documentId, String imageUrl,String description) {
        this.medicine_name = medicine_name;
        this.price = price;
        this.quantity = quantity;
        Inventory_ID = inventory_ID;
        this.documentId = documentId;
        this.imageUrl = imageUrl;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getInventory_ID() {
        return Inventory_ID;
    }

    public void setInventory_ID(String inventory_ID) {
        Inventory_ID = inventory_ID;
    }


}
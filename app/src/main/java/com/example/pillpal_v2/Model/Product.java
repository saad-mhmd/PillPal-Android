package com.example.pillpal_v2.Model;

public class Product {
    private String name;
    private String price;
    private String documentId;
    private String quantity;
    private String description;
    private String imageUrl;

    public Product() {
    }

    public Product(String name, String price, String quantity, String description, String imageUrl,String documentId) {
        this.name = name;
        this.documentId = documentId;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }



    public String getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}


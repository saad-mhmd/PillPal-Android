package com.example.pillpal_v2.Model;

public class Supplement {
    private String name;
    private String price;
    private String quantity;
    private String description;
    private String imageUrl;
    String DocumentId;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Supplement() {
    }

    public Supplement(String name, String price, String quantity, String description, String imageUrl,String DocumentId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.DocumentId=DocumentId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
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

    public String getDocumentId() {return DocumentId;
    }
}

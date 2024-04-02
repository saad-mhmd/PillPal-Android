package com.example.pillpal_v2.Model;

public class ProductPatient {
    private String Name;
    private String price;
    private String PharmaName;
    private String path;
    private String imageUrl;

    public ProductPatient(String name, String price, String pharmaName, String path, String imageUrl) {
        Name = name;
        this.price = price;
        PharmaName = pharmaName;
        this.path = path;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPharmaName() {
        return PharmaName;
    }

    public void setPharmaName(String pharmaName) {
        PharmaName = pharmaName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

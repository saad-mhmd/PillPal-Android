package com.example.pillpal_v2.Model;

public class Medicine {
    Medicine() {
    }

    private String medicineName;
    private String price;
    private String PharmaName;
    private String place;
    String imageUrl;
    String path;
    String documentID;
    String description;
    String lattitude;
    String longitude;

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Medicine(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Medicine(String medicineName, String price, String PharmaName, String place, String imageUrl, String path, String documentID, String description, String lattitude, String longitude) {
        //  this.imageResource = imageResource;
        this.medicineName = medicineName;
        this.path = path;
        this.price = price;
        this.PharmaName = PharmaName;
        this.place = place;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.documentID = documentID;
        this.description = description;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return medicineName;
    }

    public void setName(String medicineName) {
        this.medicineName = medicineName;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPharmacy() {
        return PharmaName;
    }

    public void setPharmacy(String PharmaName) {
        this.PharmaName = PharmaName;
    }

    public String getPharmacyPlace() {
        return place;
    }

    public void setplace(String place) {
        this.place = place;
    }
}

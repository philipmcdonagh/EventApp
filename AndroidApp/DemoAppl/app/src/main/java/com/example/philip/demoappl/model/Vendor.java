package com.example.philip.demoappl.model;

public class Vendor {

    private String vendorName;
    private String vendorCategory;
    private String vendorSubCategory;
    private String vendorDescription;
    private int vendorStand;

    public Vendor(){}

    //Constructor
    public Vendor(String vendorName,String vendorCategory, String vendorSubCategory, String vendorDescription, int vendorStand) {
        this.vendorName = vendorName;
        this.vendorCategory = vendorCategory;
        this.vendorSubCategory = vendorSubCategory;
        this.vendorDescription = vendorDescription;
        this.vendorStand = vendorStand;
    }

    //getters & setters
    public String getVendorName() {
        return vendorName;
    }

    public String getVendorCategory() {
        return vendorCategory;
    }

    public String getVendorSubCategory() {
        return vendorSubCategory;
    }

    public String getVendorDescription() {
        return vendorDescription;
    }

    public int getVendorStand() {
        return vendorStand;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setVendorCategory(String vendorCategory) {
        this.vendorCategory = vendorCategory;
    }

    public void setVendorSubCategory(String vendorSubCategory) { this.vendorSubCategory = vendorSubCategory; }

    public void setVendorDescription(String vendorDescription) { this.vendorDescription = vendorDescription; }

    public void setVendorStand(int vendorStand) { this.vendorStand = vendorStand; }

}

package com.example.ngoadmin.Models;

public class DonationType {

    private String id;
    private String donation_type;

    public DonationType(String id, String donation_type) {
        this.id = id;
        this.donation_type = donation_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDonation_type() {
        return donation_type;
    }

    public void setDonation_type(String donation_type) {
        this.donation_type = donation_type;
    }
}

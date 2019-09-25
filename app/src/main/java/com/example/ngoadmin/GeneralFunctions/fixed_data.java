package com.example.ngoadmin.GeneralFunctions;

import java.util.ArrayList;
import java.util.List;

public class fixed_data {

    public static int add_event_max_images = 10;

    public static final int PICK_IMAGE = 110;
    public static final int READ_EXTERNAL_STORAGE = 111;
    public static final int WRITE_EXTERNAL_STORAGE = 112;



    public static List<String> GetDonationTypeList(){

        ArrayList<String> donationtype = new ArrayList<>();

        donationtype.add("Money");
        donationtype.add("Clothes");
        donationtype.add("Food");
        donationtype.add("Other");

        return donationtype;
    }
}

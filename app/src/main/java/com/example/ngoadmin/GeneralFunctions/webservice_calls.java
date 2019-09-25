package com.example.ngoadmin.GeneralFunctions;

import android.content.Context;
import android.widget.Toast;
import com.example.ngoadmin.Models.Category;
import com.example.ngoadmin.Models.DonationType;
import com.example.ngoadmin.database_call.NetworkCall;
import com.example.ngoadmin.database_call.jsn;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class webservice_calls {

    //Fetch all categories of a particular NGO
    public static List<Category> GetEventCategories(final Context context, String ngo_id){
        final List<Category> categories = new ArrayList<>();

        HashMap<String,String> params = new HashMap<>();
        params.put("type","GetEventCategories");
        params.put("ngo_id",ngo_id);
        NetworkCall.call(params).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public boolean setResponse(String responseStr) {
                try {
                    JSONObject reader = new JSONObject(responseStr);
                    if (reader.getString("action").equals("1")) {
                        JSONArray catreader = reader.getJSONArray("message");
                        for (int i = 0; i < catreader.length(); i++) {
                            JSONObject t = catreader.getJSONObject(i);
                            categories.add(new Category(t.getString("id"), t.getString("category_name")));
                        }
                    }
                    else{
                        Toast.makeText(context, "There was some problem. Please" +
                                "try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(context, "Exception GetEventCategories : "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        return categories;
    }


    //Fetch all categories of a particular NGO
    public static List<DonationType> GetDonationTypes(final Context context, String ngo_id){
        final List<DonationType> donationTypes = new ArrayList<>();
        HashMap<String,String> params = new HashMap<>();
        params.put("type","GetDonationTypes");
        params.put("ngo_id",ngo_id);
        NetworkCall.call(params).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public boolean setResponse(String responseStr) {
                try {
                    JSONObject reader = new JSONObject(responseStr);
                    if (reader.getString("action").equals("1")) {
                        JSONArray catreader = reader.getJSONArray("message");
                        for (int i = 0; i < catreader.length(); i++) {
                            JSONObject t = catreader.getJSONObject(i);
                            donationTypes.add(new DonationType(t.getString("id"), t.getString("donation_type")));
                        }
                    }
                    else{
                        Toast.makeText(context, "There was some problem. Please" +
                                "try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(context, "Exception GetEventCategories : "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        return donationTypes;
    }


    //Insert event of an NGO
    public static String AddEvent(final Context context,String ngo_id, String category_id, String title, String f_date,
                                String t_date, String f_time, String t_time, String address,
                                String description, String image_flag, String datetime_added){

        HashMap<String,String> params = new HashMap<>();
        params.put("type","AddEvent");
        params.put("ngo_id",ngo_id);
        params.put("category_id",category_id);
        params.put("title",title);
        params.put("f_date",f_date);
        params.put("t_date",t_date);
        params.put("f_time",f_time);
        params.put("t_time",t_time);
        params.put("address",address);
        params.put("description",description);
        params.put("image_flag",image_flag);
        params.put("datetime_added",datetime_added);

        final String[] event_id = {""};
        NetworkCall.call(params).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public boolean setResponse(String responseStr) {

                try {
                    JSONObject reader = new JSONObject(responseStr);
                    if (reader.getString("action").equals("1")) {
                        JSONObject id = jsn.getJSONObjectAt0(responseStr);
                        event_id[0] = id.getString("id");
                        Toast.makeText(context, "Event ID : "+event_id[0], Toast.LENGTH_SHORT).show();
                    }
                    else{
                        event_id[0] = "";
                        Toast.makeText(context, "AddEvent: "+responseStr, Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    event_id[0] = "";
                    Toast.makeText(context, "Exception AddEvent : "+e.getMessage(), Toast.LENGTH_LONG).show();
                }


                return false;
            }
        });
        return event_id[0];
    }

}

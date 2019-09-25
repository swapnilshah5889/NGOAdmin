package com.example.ngoadmin.database_call;

import androidx.annotation.NonNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCall {

    private SetDataResponse setDataRes;
    private static HashMap<String, String> parameters;
    private String responseString = "";


    private boolean isTaskKilled = false;
    public static String SERVER_URL_WEBSERVICE_API = utils_string.BASE_URL+"ngo_webservices.php";
    private static final String TAG = "NetworkCall";


    public static NetworkCall call(HashMap<String, String> parameters) {
        NetworkCall call = new NetworkCall();
        NetworkCall.parameters = parameters;
        call.performPostCall();
        return call;
    }


    private void performPostCall() {
        Call<Object> call = RestClient.getClient().getResponse(SERVER_URL_WEBSERVICE_API,
                parameters);

        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()) {
                    responseString = RestClient.getGSONBuilder().toJson(response.body());
                } else {
                    responseString = "";
                }
                if (setDataRes!=null) setDataRes.setResponse(responseString);
            }
            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                responseString = "";
                setDataRes.setResponse(t.getMessage());
            }

        });
    }



    public interface SetDataResponse {
        boolean setResponse(String responseStr);
    }

    public void setDataResponseListener(SetDataResponse setDataRes) {
        this.setDataRes = setDataRes;
    }

}

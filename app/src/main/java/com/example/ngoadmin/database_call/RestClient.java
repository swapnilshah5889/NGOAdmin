package com.example.ngoadmin.database_call;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;


public class RestClient {

    private static ApiInterface apiInterface;
    private static String baseUrl = "http://www.google.com/";

    public static ApiInterface getClient() {

        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = client.create(ApiInterface.class);
        return apiInterface;
    }

    public static Gson getGSONBuilder() {
        return new GsonBuilder().
                registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        if (src == src.longValue())
                            return new JsonPrimitive("" + src.longValue());
                        return new JsonPrimitive("" + src);
                    }
                }).create();

    }

    public interface ApiInterface {

        @FormUrlEncoded
        @POST
        Call<Object> getResponse(@Url String url, @FieldMap Map<String, String> params);

        @GET
        Call<Object> getResponse(@Url String url);


        @Multipart
        @POST
        Call<Object> uploadData(@Url String url, @Part MultipartBody.Part file, @PartMap() Map<String, RequestBody> params);
    }

}


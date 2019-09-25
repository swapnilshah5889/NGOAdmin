package com.example.ngoadmin.database_call;

import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 2/25/2018.
 */

public class Master_Upload {

    private JSONObject jsonObject = new JSONObject();

    public JSONObject Master_Upload(Uri uri, Context context
            , String FileName, String DocType
            , String UploadDirectory) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            InputStream stream = context.getContentResolver().openInputStream(uri);

            URL url = new URL("http://proglan.in/techmicra/customcuisine/master_upload.php?t_id="
                    + FileName + "&directory=" + UploadDirectory);

            String rename = FileName + DocType;

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            String boundary = "*****";
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.connect();

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition:form-data;name=\"fileUpload\";filename=\"" + rename + "\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);

            assert stream != null;
            int bytesAvailable = stream.available();
            int maxBufferSize = 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = stream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = stream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = stream.read(buffer, 0, bufferSize);
            }
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            int c = httpURLConnection.getResponseCode();

            stream.close();
            dataOutputStream.close();
            if (c == 200) {
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String s;

                while ((s = bufferedReader.readLine()) != null) {
                    stringBuilder.append(s);
                }
                jsonObject = new JSONObject(stringBuilder.toString());

            } else {
                jsonObject = new JSONObject();
                jsonObject.put("action", "0");
                jsonObject.put("message", "Maintenance Error");
                Toast.makeText(context, "We Are Under Maintenance", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return jsonObject;
    }

}

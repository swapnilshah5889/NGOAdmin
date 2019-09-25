package com.example.ngoadmin.database_call;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class jsn {
    public static HashMap<String, String> getParamType(String type) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        return map;
    }


    public static String getRequestURL(HashMap<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(NetworkCall.SERVER_URL_WEBSERVICE_API).append("?");

        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            sb.append(pair.getKey() + "=" + pair.getValue()).append("&");
//            it.remove(); // avoids a ConcurrentModificationException
        }
        Log.e("full_request", "full_request: " + sb.toString());
        return sb.toString();
    }

    public static HashMap<String, String> getMapForModel(Object cls, String type) {
        HashMap<String, String> map = getMapForModel(cls);
        map.put("type", type);
        return map;
    }

    public static HashMap<String, String> getMapForModel(Object cls) {
        HashMap<String, String> map = new HashMap<>();
        for (Field field : cls.getClass().getDeclaredFields()) {
            // Skip this if you intend to access to public fields only
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                map.put(field.getName(), String.valueOf(field.get(cls)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static String getStringsForWhere(HashMap<String, String> map) {

        StringBuilder string = new StringBuilder();

        Iterator iterator = map.entrySet().iterator();
        int i = 1;
        for (String key : map.keySet()) {

            string.append(key).append(" = ").append(map.get(key));
            if (i < map.keySet().size()) {
                string.append(" AND ");
                i++;
            }
        }
        return string.toString();
    }


    public static String getAction(String responseString) {
        JSONObject object = getJSONObject(responseString);
        if (object != null) {
            try {
                return object.getString("action");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public static boolean checkResponseStr(String responseString) {
        if (responseString != null && !responseString.equals("")) {
            String action = getAction(responseString);
            return action.equals("1");
        }

        return false;
    }

    public static String getJSONString(JSONObject object, String name) {
        try {
            return object.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getResponseFirstParam(String responseString) {
        return getResponseFirstParam(responseString, null);
    }

    public static boolean isValid(String string) {
        return string != null && !string.equals("");
    }

    public static String getResponseFirstParam(String responseString, String key) {
        if (!isValid(responseString)) return "";
        Log.e("ResponseString", responseString);
        JSONArray jsonArray = getJSONArrayMessage(responseString);
        if (jsonArray != null && jsonArray.length() > 0) {
            JSONObject jsonObject = getJSONObject(jsonArray, 0);

            if (jsonObject != null) {

                if (key != null) {
                    return getJSONString(jsonObject, key);

                } else {
                    JSONArray keys = jsonObject.names();
                    return getJSONString(jsonObject, getJSONString(keys, 0));

                }
            }
        } else {
            JSONObject mainObject = getJSONObject(responseString);
            assert mainObject != null;
            return getJSONString(mainObject, "message");
        }
        return null;
    }

    public static JSONObject getJSONObject(String responseString) {
        try {
            return new JSONObject(responseString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONArray jsonArray, int position) {
        try {
            return jsonArray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringOfRequest(HashMap<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(NetworkCall.SERVER_URL_WEBSERVICE_API).append("?");

        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            sb.append(pair.getKey() + "=" + pair.getValue()).append("&");
//            it.remove(); // avoids a ConcurrentModificationException
        }
        Log.e("full_request", "full_request: " + sb.toString());
        return sb.toString();
    }


    public static Object copyClass(Object source, Object destination) {
        Gson gson = (new GsonBuilder()).create();
        String sourceString = gson.toJson(source);
        Log.e(TAG, "convert: " + sourceString);
        return gson.fromJson(sourceString, destination.getClass());
    }

    public static <T> T copyClass(Object source, Class<T> destination) {
        Gson gson = (new GsonBuilder()).create();
        String sourceString = gson.toJson(source);
        Log.e(TAG, "convert: " + sourceString);
        return gson.fromJson(sourceString, destination);
    }

    public static Object getSingleObjectFromString(String responseString, Object classObject) {
        JSONObject object = getJSONObjectAt0(responseString);
        return getObjectFromJSON(object, classObject);
    }

    public static List<Object> getAllObjectFromString(String responseString, Object classObject) {
        List<Object> listMain = new ArrayList<>();
        JSONArray list = getJSONArrayMessage(responseString);
        if (list != null && list.length() > 0) {
            for (int i = 0; i < list.length(); i++) {
                JSONObject object = getJSONObject(list, i);
                if (object != null) {
                    listMain.add(getObjectFromJSON(object, classObject));
                }
            }
        }
        return listMain;
    }

    private static final String TAG = "jsn";

    public static List<Object> getClassFromResponseString(String responseString, Class className) {
        return getAllObjectFromString(responseString,className);
    }
    public static List<Object> getAllObjectFromString(String responseString, Class className) {
        List<Object> listMain = new ArrayList<>();
        JSONArray list = getJSONArrayMessage(responseString);
        if (list != null && list.length() > 0) {
            for (int i = 0; i < list.length(); i++) {
                JSONObject object = getJSONObject(list, i);
                if (object != null) {
                    listMain.add(getObjectFromJSON(object, className));
                }
            }
        }
//        Log.e(TAG, "getAllObjectFromString: "+listMain.getDetail(0).toString() );
        return listMain;
    }

    public static List getAllClassFromString(String responseString, Class className) {
        List listMain = new ArrayList<>();
        JSONArray list = getJSONArrayMessage(responseString);
        if (list != null && list.length() > 0) {
            for (int i = 0; i < list.length(); i++) {
                JSONObject object = getJSONObject(list, i);
                if (object != null) {
                    listMain.add(getObjectFromJSON(object, className));
                }
            }
        }
//        Log.e(TAG, "getAllObjectFromString: "+listMain.getDetail(0).toString() );
        return listMain;
    }


    public static String where(String key, String value) {
        return key + "=" + quoteSingle(value);
    }

    public static String quoteSingle(String string) {
        return "\'" + string + "\'";
    }

    public static String whereIsNotDeleted(String key, String value) {
        String where1 = where(key, value);
        String where2 = where("IsDeleted", "0");
        return whereJoinAnd(where1, where2);
    }

    public static String whereJoinAnd(String... wheres) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (String where : wheres) {
            sb.append(where);
            if (i < wheres.length) {
                sb.append(" AND ");
                i++;
            }
        }
        return sb.toString();
    }

    public String getWhere(HashMap<String, String> map) {

        StringBuilder string = new StringBuilder();

        int i = 1;
        for (String key : map.keySet()) {

            string.append(key).append(" = ").append(map.get(key));
            if (i < map.keySet().size()) {
                string.append(" AND ");
                i++;
            }
        }
        return string.toString();
    }

    public static Object getObjectFromJSON(JSONObject object, Object classObject) {
        Gson gson = (new GsonBuilder()).create();
        if (classObject instanceof Class) {
            Class cls = (Class) classObject;
            return gson.fromJson(object.toString(), cls);
        } else {
            return gson.fromJson(object.toString(), classObject.getClass());
        }
    }

//    public static Object getObjectFromJSON(JSONObject object, Class className) {
//        Gson gson = (new GsonBuilder()).create();
//        if (className instanceof Class) {
//            Class cls= (Class) className;
//            return gson.fromJson(object.toString(), cls);
//        } else {
//            return gson.fromJson(object.toString(), classObject.getClass());
//        }
//    }

    public static String getJSONString(JSONArray jsonArray, int position) {
        try {
            return (String) jsonArray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONObject getJSONObjectAt0(String s) {
        JSONArray array = getJSONArrayMessage(s);
        assert array != null;
        return getJSONObject(array, 0);
    }

    public static JSONArray getJSONArrayMessage(String responseString) {
        JSONObject object = getJSONObject(responseString);
        if (object != null) {
            try {
                return object.getJSONArray("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONArray getJSONArrayList(JSONObject object) {
        if (object != null) {
            try {
                return object.getJSONArray("List");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

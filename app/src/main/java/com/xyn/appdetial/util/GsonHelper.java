package com.xyn.appdetial.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class GsonHelper {

    private JsonObject jsonObject;

    /**
     * @param jsonElement : must be JsonObject
     */
    public GsonHelper(JsonElement jsonElement) {
        this(jsonElement.getAsJsonObject());
    }

    public GsonHelper(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    private static JsonElement getJsonElement(JsonObject jsonObject, String key) {
        if (jsonObject != null) {
            return jsonObject.get(key);
        } else {
            return null;
        }
    }

    public static boolean getBoolean(JsonObject jsonObject, String key,
                                     boolean defValue) {
        if (jsonObject == null) {
            return defValue;
        }
        JsonElement v = getJsonElement(jsonObject, key);
        if (v == null) {
            return defValue;
        } else {
            return v.getAsBoolean();
        }
    }

    public static int getInt(JsonObject jsonObject, String key, int defValue) {
        if (jsonObject == null) {
            return defValue;
        }
        JsonElement v = getJsonElement(jsonObject, key);
        if (v == null) {
            return defValue;
        } else {
            return v.getAsInt();
        }
    }

    /*
     *
     */
    public static String getString(JsonObject jsonObject, String key,
                                   String defValue) {
        if (jsonObject == null) {
            return defValue;
        }
        JsonElement v = getJsonElement(jsonObject, key);
        if (v == null) {
            return defValue;
        } else {
            return v.getAsString();
        }
    }

    public static String getString(JsonElement jsonElement, String key,
                                   String defValue) {
        return getString(jsonElement.getAsJsonObject(), key, defValue);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(JsonElement element, Type typeOfT) {
        Gson gson = new Gson();
        return gson.fromJson(element, typeOfT);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(JsonElement element, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(element, clazz);
    }

    public static <T> List<T> parseList(JsonElement ele, Type type) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(ele, type);
        return list;
    }

    public static <T> List<T> parseList(String str, Type type) {
        Gson gson = new Gson();
        String trimmed = str.trim();
        List<T> list = gson.fromJson(trimmed, type);
        return list;
    }

    public String getString(String key, String defValue) {
        JsonElement v = getJsonElement(jsonObject, key);
        if (v != null) {
            return v.getAsString();
        } else {
            return defValue;
        }
    }

    public static String readLocalJson(Context context, String fileName) {
        String jsonString = "";
        String resultString = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    context.getResources().getAssets().open(fileName)));
            while ((jsonString = bufferedReader.readLine()) != null) {
                resultString += jsonString;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return resultString;
    }
}

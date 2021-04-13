package com.kkb.utils;

import com.google.gson.Gson;

public class JSONUtil {
    private static final Gson G = new Gson();
    public static String toJSON(Object obj) {
        return G.toJson(obj);
    }
}

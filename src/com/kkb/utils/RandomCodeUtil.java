package com.kkb.utils;

import java.util.Random;

public class RandomCodeUtil {

    private static final Random R = new Random();

    public static String getCode() {
        return Integer.toString(R.nextInt(899999) + 100000);
    }
}

package com.timewasteanalyzer.util;


public class Utility {

    public static boolean equals(String s1, String s2) {
        boolean result = false;
        if (s1 == null) {
            if (s2 == null) {
                result = true;
            }
        } else {
            if (s2 != null) {
                result = s1.equals(s2);
            }
        }
        return result;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

}

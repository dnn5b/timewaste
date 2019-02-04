package com.timewasteanalyzer.util


object Utility {

    fun equals(s1: String?, s2: String?): Boolean {
        var result = false
        if (s1 == null) {
            if (s2 == null) {
                result = true
            }
        } else {
            if (s2 != null) {
                result = s1 == s2
            }
        }
        return result
    }

    fun isEmpty(string: String?): Boolean {
        return string == null || string.isEmpty()
    }

}

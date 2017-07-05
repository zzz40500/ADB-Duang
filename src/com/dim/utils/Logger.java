package com.dim.utils;


/**
 * Created by dim on 16/4/1.
 */
public class Logger {

    private static boolean debug = true;

    public static void println(String message) {
        if (debug) {
            System.out.println(message);
        }
    }
}

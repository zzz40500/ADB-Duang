package com.dim.utils;

import com.dim.ui.NotificationHelper;

/**
 * Created by dim on 16/4/1.
 */
public class Logger {

    private static boolean debug = true;

    public static void println(String message) {
        if (debug) {
//            NotificationHelper.info(message);
            System.out.println(message);
        }
    }
}

package com.dim.comand;

import java.io.InputStream;

import static com.dim.utils.Logger.println;


/**
 * Created by dim on 16/3/31.
 */
public abstract class Command<T> {

    public abstract boolean run();

    public String executeCommand(String command) {
        println(command);
        String result = null;
        try {
            Process exec = Runtime.getRuntime().exec(command);
            InputStream errorStream = exec.getErrorStream();
            byte[] buffer = new byte[2048];
            int readBytes = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while ((readBytes = errorStream.read(buffer)) > 0) {
                stringBuilder.append(new String(buffer, 0, readBytes));
            }
            result = stringBuilder.toString();

        } catch (Exception e) {
            println(e.toString());
            e.printStackTrace();
        }
        return result;
    }

    public T getResult() {
        return null;
    }
}

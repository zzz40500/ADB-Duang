package com.dim.utils;

import com.android.ddmlib.*;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dim on 17/7/5.
 */
public class DeviceHelper {

    public static void checkDuangAppInstalled(final IDevice iDevice) {
        try {
            iDevice.executeShellCommand(" cd /data/data/red.dim.duang", new MultiLineReceiver() {
                @Override
                public void processNewLines(String[] strings) {
                    boolean installed = true;
                    for (String string : strings) {
                        Logger.println(string);
                        if (string.contains("No such file or directory")) {
                            installed = false;
                        }
                    }
                    if (!installed) {
                        File file = new File(System.getProperty("java.io.tmpdir"), "setting.apk");
                        if (!file.exists()) {
                            try {
                                InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("setting.apk");
                                System.out.println(System.getProperty("java.io.tmpdir"));
                                IOUtils.copy(resourceAsStream, new FileOutputStream(file));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        install(iDevice, file.getAbsolutePath());
                    }
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void install(IDevice iDevice, String apkUrl) {
        try {
            iDevice.installPackage(apkUrl, true, new String[0]);
        } catch (InstallException e) {
            e.printStackTrace();
        }
    }

    public static void executeShell(IDevice device, IShellOutputReceiver iShellOutputReceiver, String cmd) {
        try {
            device.executeShellCommand(cmd, iShellOutputReceiver);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

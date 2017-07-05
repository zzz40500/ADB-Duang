package com.dim.utils;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import org.apache.http.util.TextUtils;

import static com.dim.utils.DeviceHelper.executeShell;

/**
 * Created by dim on 17/7/5.
 */
public class PropertyHelper {

    public static boolean isShowOverdrawEnabled(IDevice device) {
        String result = getValue(device.getProperty(Property.getDebugOverdrawPropertyKey(getApiLevel(device))));
        return Property.getDebugOverdrawPropertyEnabledValue(getApiLevel(device)).equals(result);
    }

    public static boolean isLayoutUpdateEnabled(IDevice device) {
        String result = getValue(device.getProperty(Property.getDebugLayoutUpdatePropertyKey()));
        return Property.getDebugLayoutUpdateEnabledValue().equals(result);
    }

    public static boolean isShowProfileGPURendering(IDevice device) {
        String result = getValue(device.getProperty(Property.PROFILE_PROPERTY));
        return "visual_bars".equals(result);
    }

    public static void setDebugLayoutEnabled(IDevice device, boolean enabled, IShellOutputReceiver iShellOutputReceiver) {
        String cmd = "setprop " + Property.DEBUG_LAYOUT_PROPERTY + " " + (enabled ? "true" : "false");
        System.out.println(cmd);
        executeShell(device, iShellOutputReceiver, cmd);
        executeShell(device, null, "am broadcast -a red.dim.updatesystemprop -n red.dim.duang/.UpdateReceiver");

    }

    public static void setShowOverdrawEnabled(IDevice device, boolean enabled, IShellOutputReceiver iShellOutputReceiver) {
        String cmd = "setprop " + Property.getDebugOverdrawPropertyKey(getApiLevel(device)) + " "
                + (enabled ? Property.getDebugOverdrawPropertyEnabledValue(getApiLevel(device)) : "false");
        System.out.println(cmd);
        executeShell(device, iShellOutputReceiver, cmd);
        executeShell(device, null, "am broadcast -a red.dim.updatesystemprop -n red.dim.duang/.UpdateReceiver");
    }

    private static int getApiLevel(IDevice device) {
        try {
            return Integer.valueOf(device.getProperty("ro.build.version.sdk"));
        } catch (Throwable throwable) {
            return 19;
        }
    }

    public static void setProfileGPURenderingEnabled(IDevice device, boolean enabled, IShellOutputReceiver iShellOutputReceiver) {
        String cmd = "setprop " + Property.PROFILE_PROPERTY + " " + (enabled ? "visual_bars" : "false");
        System.out.println(cmd);
        executeShell(device, iShellOutputReceiver, cmd);
        executeShell(device, null, "am broadcast -a red.dim.updatesystemprop -n red.dim.duang/.UpdateReceiver");
    }

    public static void setLayoutUpdateEnable(IDevice device, boolean enabled, IShellOutputReceiver iShellOutputReceiver) {
        String cmd = "setprop " + Property.DEBUG_SHOW_DIRTY_REGIONS + " " + (enabled ? "true" : "false");
        System.out.println(cmd);
        executeShell(device, iShellOutputReceiver, cmd);
        executeShell(device, null, "am broadcast -a red.dim.updatesystemprop -n red.dim.duang/.UpdateReceiver");
    }

    public static boolean isDebugLayoutEnabled(IDevice device) {
        String result = getValue(device.getProperty(Property.DEBUG_LAYOUT_PROPERTY));
        return "true".equals(result);
    }

    private static String getValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return "false";
        }
        return value;
    }

}

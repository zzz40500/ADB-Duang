package com.dim.utils;

public class Property {

    public static final String DEBUG_LAYOUT_PROPERTY = "debug.layout";
    public static final String PROFILE_PROPERTY = "debug.hwui.profile";

    private static final String DEBUG_OVERDRAW_PROPERTY_JB_MR1 = "debug.hwui.show_overdraw";
    private static final String DEBUG_OVERDRAW_PROPERTY_KITKAT = "debug.hwui.overdraw";
    public static final String DEBUG_SHOW_DIRTY_REGIONS = "debug.hwui.show_dirty_regions";

    public static String getDebugOverdrawPropertyKey(int sdkInt) {
        if (sdkInt >= 19) {
            return DEBUG_OVERDRAW_PROPERTY_KITKAT;
        } else if (sdkInt >= 17) {
            return DEBUG_OVERDRAW_PROPERTY_JB_MR1;
        } else {
            return "";
        }
    }

    public static String getDebugLayoutUpdatePropertyKey() {
        return DEBUG_SHOW_DIRTY_REGIONS;
    }

    public static String getDebugLayoutUpdateEnabledValue() {
        return "true";
    }

    public static String getDebugOverdrawPropertyEnabledValue(int sdkInt) {
        if (sdkInt >= 19) {
            return "show";
        } else if (sdkInt >= 17) {
            return "true";
        } else {
            return "";
        }
    }

}

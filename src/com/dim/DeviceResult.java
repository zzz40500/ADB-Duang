package com.dim;

import com.android.ddmlib.IDevice;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.android.facet.AndroidFacet;

/**
 * Created by dim on 16/3/31.
 */
public class DeviceResult {

    public  IDevice device;
    public  AndroidFacet facet;
    public  String packageName;
    public  AnActionEvent anActionEvent;

    public DeviceResult(AnActionEvent anActionEvent, IDevice device, AndroidFacet facet, String packageName) {
        this.device = device;
        this.facet = facet;
        this.packageName = packageName;
//        this.packageName = "com.xingin.xhs";
        this.anActionEvent = anActionEvent;
    }
}

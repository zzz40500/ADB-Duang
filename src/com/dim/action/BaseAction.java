package com.dim.action;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.tools.idea.sdk.IdeSdks;
import com.dim.DeviceResult;
import com.dim.ui.DeviceChooserDialog;
import com.dim.ui.ModuleChooserDialogHelper;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.android.facet.AndroidFacet;
import org.jetbrains.android.sdk.AndroidSdkUtils;
import org.jetbrains.android.util.AndroidUtils;

import java.util.List;

import static com.dim.ui.NotificationHelper.error;


/**
 * Created by dim on 16/3/31.
 */
public abstract class BaseAction extends AnAction {


    @Override
    public void actionPerformed(final AnActionEvent anActionEvent) {
        final DeviceResult deviceResult = getDevice(anActionEvent);

        if (deviceResult == null) {
            return;
        }

        if (deviceResult.facet == null) {
            error(" no module ");

        } else if (deviceResult.device == null) {
            //没设备
            error(" no device ");
        } else if (IdeSdks.getAndroidSdkPath() == null) {

            error("android sdk is null ");

        } else {
            if (runEnable(anActionEvent)) {
                run(deviceResult, anActionEvent);
            }
        }

    }

    protected boolean runEnable(AnActionEvent anActionEvent) {
        return true;
    }

    protected String getAndroidFacetName(AnActionEvent anActionEvent) {
        return null;
    }

    abstract void run(DeviceResult deviceResult, AnActionEvent anActionEvent);

    private DeviceResult getDevice(AnActionEvent anActionEvent) {
        List<AndroidFacet> facets = getApplicationFacets(anActionEvent.getProject());
        if (!facets.isEmpty()) {

            AndroidFacet facet = null;

            String androidFacetName = getAndroidFacetName(anActionEvent);

            if (androidFacetName != null) {
                for (AndroidFacet androidFacet : facets) {
                    if (androidFacet.getModule().getName().equals(androidFacetName)) {
                        facet = androidFacet;
                    }
                }
                if (facet == null) {
                    return null;
                }

            } else {

                if (facets.size() > 1) {
                    facet = ModuleChooserDialogHelper.showDialogForFacets(anActionEvent.getProject(), facets);
                    if (facet == null) {
                        return null;
                    }
                } else {
                    facet = facets.get(0);
                }
            }
            String packageName =facet.getAndroidModuleInfo().getPackage();
            AndroidDebugBridge bridge = AndroidSdkUtils.getDebugBridge(anActionEvent.getProject());
            if (bridge == null) {
                error("No platform configured");
                return null;
            }

            if (bridge.isConnected() && bridge.hasInitialDeviceList()) {

                IDevice[] devices = bridge.getDevices();
                if (devices.length == 1) {
                    return new DeviceResult(anActionEvent, devices[0], facet, packageName);
                } else if (devices.length > 1) {
                    return askUserForDevice(anActionEvent, facet, packageName);
                } else {
                    return new DeviceResult(anActionEvent, null, facet, null);
                }
            }
        }
        return new DeviceResult(anActionEvent, null, null, null);
    }


    private static List<AndroidFacet> getApplicationFacets(Project project) {

        List<AndroidFacet> facets = Lists.newArrayList();
        for (AndroidFacet facet : AndroidUtils.getApplicationFacets(project)) {
            if (!isTestProject(facet)) {
                facets.add(facet);
            }
        }

        return facets;
    }

    private static boolean isTestProject(AndroidFacet facet) {
        return facet.getManifest() != null
                && facet.getManifest().getInstrumentations() != null
                && !facet.getManifest().getInstrumentations().isEmpty();
    }

    private static DeviceResult askUserForDevice(AnActionEvent anActionEvent, AndroidFacet facet, String packageName) {
        final DeviceChooserDialog chooser = new DeviceChooserDialog(facet);
        chooser.show();

        if (chooser.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return null;
        }

        IDevice[] selectedDevices = chooser.getSelectedDevices();
        if (selectedDevices.length == 0) {
            return null;
        }

        return new DeviceResult(anActionEvent, selectedDevices[0], facet, packageName);
    }

}

package com.dim.comand;

import com.android.tools.idea.sdk.IdeSdks;
import com.android.tools.idea.welcome.install.AndroidSdk;
import com.dim.DeviceResult;
import com.intellij.ide.util.PropertiesComponent;

import java.io.File;
import java.util.List;

import static com.dim.utils.Logger.println;


/**
 * Created by dim on 16/3/31.
 */
public class PushCommand extends Command {

    private final DeviceResult deviceResult;
    private final String localFilePath;
    private final String remoteFilePath;

    public PushCommand(DeviceResult deviceResult, String localFilePath, String remoteFilePath) {
        this.deviceResult = deviceResult;
        this.localFilePath = localFilePath;
        this.remoteFilePath = remoteFilePath;
    }

    @Override
    public boolean run() {

        String command = IdeSdks.getAndroidSdkPath().getPath() + "/platform-tools/adb -s " + deviceResult.device.getSerialNumber() + " push  " + localFilePath + " " + remoteFilePath;
        println(command);
        String result = executeCommand(command);
        println(result);
        ChmodCommand chmodCommand = new ChmodCommand(deviceResult, remoteFilePath);
        //修改data 的权限
        chmodCommand.run();
        LsCommand lsCommand = new LsCommand(deviceResult, remoteFilePath);
        lsCommand.run();
        List<String> result1 = lsCommand.getResult();
        if (result1.contains(new File(localFilePath).getName())) {
            return true;
        }

        return false;
    }
}

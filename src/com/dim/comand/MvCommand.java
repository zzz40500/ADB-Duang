package com.dim.comand;

import com.android.ddmlib.MultiLineReceiver;
import com.dim.DeviceResult;

import java.util.concurrent.TimeUnit;

import static com.dim.utils.Logger.println;

/**
 * mv xxx xxx.
 * <p>
 * Created by dim on 16/3/31.
 */
public class MvCommand extends Command {


    private final DeviceResult deviceResult;
    private String sdcardName;
    private final String filePath;

    public MvCommand(DeviceResult deviceResult, String sdcardName, String filePath) {
        this.deviceResult = deviceResult;
        this.sdcardName = sdcardName;
        this.filePath = filePath;
    }

    @Override
    public boolean run() {
        try {
            String command = "su root  mv  /sdcard/" + sdcardName + " " + filePath;
            println(command);
            deviceResult.device.executeShellCommand(command
                    , new ChmodReceiver(), 2L, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Created by dim on 16/3/31.
     */
    public class ChmodReceiver extends MultiLineReceiver {

        @Override
        public void processNewLines(String[] lines) {
            for (String line : lines) {
                println("ChmodReceiver : " + line);
            }

        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }

}

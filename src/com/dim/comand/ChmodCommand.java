package com.dim.comand;

import com.android.ddmlib.MultiLineReceiver;
import com.dim.DeviceResult;

import java.util.concurrent.TimeUnit;

import static com.dim.utils.Logger.println;

/**
 * chmod 777 xxx.
 * <p>
 * Created by dim on 16/3/31.
 */
public class ChmodCommand extends Command {
    private final DeviceResult deviceResult;
    private final String filePath;

    public ChmodCommand(DeviceResult deviceResult, String filePath) {
        this.deviceResult = deviceResult;
        this.filePath = filePath;
    }

    @Override
    public boolean run() {
        try {
            if (!deviceResult.device.isRoot()) {
                deviceResult.device.root();
            }
            String command = "su root chmod 777 " + filePath;
            println(command);
            deviceResult.device.executeShellCommand(command
                    , new ChmodReceiver(), 2L, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Created by dim on 16/3/31.
     */
    public class ChmodReceiver
            extends MultiLineReceiver {

        @Override
        public void processNewLines(String[] lines) {
            for (String line : lines) {
                println("Receiver : " + line);
            }
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }
}

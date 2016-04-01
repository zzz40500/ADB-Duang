package com.dim.comand;

import com.android.ddmlib.MultiLineReceiver;
import com.dim.DeviceResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.dim.utils.Logger.println;

/**
 * ls 命令
 * Created by dim on 16/3/31.
 */
public class LsCommand extends Command<List<String>> {


    private final DeviceResult deviceResult;
    private final String filePath;
    private List<String> fileList = new ArrayList<String>();


    public LsCommand(DeviceResult deviceResult, String filePath) {
        this.deviceResult = deviceResult;
        this.filePath = filePath;
    }

    @Override
    public boolean run() {

        try {
            String command = "cd  " + filePath + "\n ls";
            println(command);
            deviceResult.device.executeShellCommand(command
                    , new LsReceiver(), 15L, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<String> getResult() {
        return fileList;
    }

    class LsReceiver extends MultiLineReceiver {

        @Override
        public void processNewLines(String[] lines) {
            for (String line : lines) {
                if (line != null && line.length() > 0) {
                    if (line.contains("No such file or directory")) {
                        break;
                    }else if(line.contains("failed")){
                        break;

                    }
                    fileList.add(line);

                }
//                println("LsCommand line : " + line);
            }
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }
}

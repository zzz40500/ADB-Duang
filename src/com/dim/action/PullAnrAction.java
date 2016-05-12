package com.dim.action;

import com.dim.DeviceResult;
import com.dim.comand.ChmodCommand;
import com.dim.comand.LsCommand;
import com.dim.comand.PullCommand;
import com.dim.ui.ChooserListFileDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.util.ui.UIUtil;

import java.util.List;

import static com.dim.ui.NotificationHelper.info;


/**
 * Created by dim on 16/3/31.
 */
public class PullAnrAction extends BaseAction {


    @Override
    void run(final DeviceResult deviceResult, AnActionEvent anActionEvent) {
        //异步获取, 因为adb 获取权限是一个同步操作.如果手机长时间设置允许,idea 将一直阻塞.
        new Thread(new Runnable() {
            @Override
            public void run() {
                //数据库路径
                final String dataPath = "/data/anr/";
                PullCommand pullCommand = new PullCommand(deviceResult, dataPath, "traces.txt", "anr");
                boolean run = pullCommand.run();
                if (run) {
                    info("pull /data/anr/traces.txt success ! ");
                } else {
                    info("pull /data/anr/traces.txt failed ! ");
                }

            }
        }).start();


    }
}

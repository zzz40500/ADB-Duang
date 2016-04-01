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
 * adb pull xx xx
 * Created by dim on 16/3/31.
 */
public class PullPreferenceAction extends BaseAction {


    @Override
    void run(final DeviceResult deviceResult, AnActionEvent anActionEvent) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //数据库路径
                final String dataPath = "/data/data/" + deviceResult.packageName + "/" + "shared_prefs";
                ChmodCommand chmodCommand = new ChmodCommand(deviceResult, dataPath);
                //修改data 的权限
                chmodCommand.run();
                // 数据库列表
                LsCommand ls = new LsCommand(deviceResult, dataPath);

                ls.run();
                final List<String> list = ls.getResult();
                //todo 选择要pull 的文件
                if (list.size() > 0) {
                    UIUtil.invokeLaterIfNeeded(new Runnable() {
                        public void run() {

                                ChooserListFileDialog chooserListFileDialog = new ChooserListFileDialog(getEventProject(deviceResult.anActionEvent), list);
                                chooserListFileDialog.show();
                                final String file = chooserListFileDialog.getSelectedFile();

                                if (file != null && file.length() > 0) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            PullCommand pullCommand = new PullCommand(deviceResult, dataPath, file, "shared_prefs");
                                            boolean run = pullCommand.run();
                                            if (run) {
                                                info("pull " + file + " success !");
                                            } else {
                                                info("pull " + file + " failed !");
                                            }
                                        }
                                    }).start();
                                }
                        }
                    });
                } else {
                    //没有文件
                    info(deviceResult.facet.getModule().getName() + " without preference");
                }
            }
        }).start();

    }
}

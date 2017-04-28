package com.dim.action;

import com.dim.DeviceResult;
import com.dim.comand.LsCommand;
import com.dim.comand.PullCommand;
import com.dim.ui.ChooserListFileDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.util.ui.UIUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dim.ui.NotificationHelper.error;
import static com.dim.ui.NotificationHelper.info;

/**
 * adb pull xx xx
 * Created by dim on 16/3/31.
 */
public class PullTraceMethodAction extends BaseAction {

    @Override
    void run(final DeviceResult deviceResult, AnActionEvent anActionEvent) {
        final Task.Backgroundable task = new Task.Backgroundable(deviceResult.anActionEvent.getProject(),
                "PullTraceMethodAction") {

            @Override
            public void run(@NotNull final ProgressIndicator progressIndicator) {
                progressIndicator.setIndeterminate(true);
                //数据库路径
                final String dataPath = "/sdcard/";

                // 数据库列表
                LsCommand ls = new LsCommand(deviceResult, dataPath);

                ls.run();
                final List<String> list = new ArrayList<String>();
                for (String s : ls.getResult()) {

                    if (s.endsWith(".trace")) {
                        list.add(s);
                    }
                }
                //todo 选择要pull 的文件
                if (list.size() > 0) {
                    UIUtil.invokeLaterIfNeeded(new Runnable() {
                        @Override
                        public void run() {
                            ChooserListFileDialog chooserListFileDialog =
                                    new ChooserListFileDialog(getEventProject(deviceResult.anActionEvent),
                                            list);
                            chooserListFileDialog.show();
                            final String file = chooserListFileDialog.getSelectedFile();

                            if (file != null && file.length() > 0) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        PullCommand pullCommand =
                                                new PullCommand(deviceResult, dataPath, file, "trace");
                                        boolean run = pullCommand.run();
                                        if (run) {
                                            info("pull \"" + dataPath + "/" + file + "\" success!");
                                        } else {
                                            error("pull \"" + dataPath + "/" + file + "\" failed.");
                                        }
                                    }
                                }.start();
                            }
                        }
                    });
                } else {
                    //没有文件
                    info(deviceResult.packageName + " has no trace file.");
                }
            }
        };
        ProgressManager.getInstance().run(task);

    }
}

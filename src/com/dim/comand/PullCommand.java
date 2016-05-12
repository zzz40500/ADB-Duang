package com.dim.comand;

import com.android.tools.idea.sdk.IdeSdks;
import com.dim.DeviceResult;
import com.dim.MySelectInContext;
import com.intellij.ide.SelectInTarget;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.UIUtil;

import java.io.File;

import static com.dim.utils.Logger.println;

/**
 * Created by dim on 16/3/31.
 */
public class PullCommand extends Command {

//    /data/data/com.xingin.xhs/shared_prefs/mob_sdk_exception_1.xml

    private final DeviceResult deviceResult;
    private final String originPath;
    private final String localPath;
    private final String fileName;

    private final String duangFile = "duang";

    public PullCommand(DeviceResult deviceResult, String originPath, String fileName, String localPath) {
        this.deviceResult = deviceResult;
        this.originPath = originPath;
        this.fileName = fileName;
        this.localPath = localPath;
    }

    public boolean run() {

        //pull 出来
        ChmodCommand chmodCommand = new ChmodCommand(deviceResult, originPath + "/" + fileName);
        chmodCommand.run();
        String command = IdeSdks.getAndroidSdkPath().getPath() + "/platform-tools/adb   -s " + deviceResult.device.getSerialNumber() + " pull -p   " + originPath + "/" + fileName + " " + getLocalFile().getPath();
        println(command);
        String result = executeCommand(command);
        println(result);
        if (result.contains("(100%)")) {
            scrollToTargetSource();
            return true;
        }

        return false;

    }

    private void scrollToTargetSource() {


        for (int i = 0; i < 3; i++) {

            VirtualFile file = deviceResult.anActionEvent.getProject().getBaseDir();
            if (file != null) {
                VirtualFile child = file.findChild(duangFile);
                if (child == null) {
                    file.refresh(true, true);
                    child = file.findChild(duangFile);
                }


                if (child != null) {
                    VirtualFile appFile = child.findChild(deviceResult.facet.getModule().getName());
                    if (appFile == null) {
                        file.refresh(true, true);
                        appFile = child.findChild(deviceResult.facet.getModule().getName());
                    }
                    if (appFile != null) {
                        VirtualFile fileNameFile = appFile.findChild(localPath);
                        if (fileNameFile == null) {
                            file.refresh(true, true);
                            fileNameFile = appFile.findChild(localPath);
                        }
                        if (fileNameFile != null) {
                            println("file   " + fileNameFile.getName());
                            fileNameFile.refresh(true, true);
                            VirtualFile targetFile = fileNameFile.findChild(fileName);
                            if (targetFile != null) {
                                selectInTargetFile(targetFile);
                                break;
                            } else {
                                if (i == 2) {
                                    selectInTargetFile(child);
                                    break;
                                }

                            }
                        }
                    }
                }

            }
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectInTargetFile(final VirtualFile targetFile) {

        UIUtil.invokeLaterIfNeeded(new Runnable() {
            public void run() {
                Project project = deviceResult.anActionEvent.getProject();
                Editor editor = deviceResult.anActionEvent.getData(PlatformDataKeys.EDITOR);
                MySelectInContext selectInContext = new MySelectInContext(targetFile, editor, project);
                ProjectViewImpl projectView = (ProjectViewImpl) ProjectView.getInstance(project);
                AbstractProjectViewPane currentProjectViewPane = projectView.getCurrentProjectViewPane();
                SelectInTarget target = currentProjectViewPane.createSelectInTarget();
                if (target != null && target.canSelect(selectInContext)) {
                    target.selectIn(selectInContext, false);
                } else {
                    selectInContext = new MySelectInContext(targetFile.getParent(), editor, project);
                    if (target != null && target.canSelect(selectInContext)) {
                        target.selectIn(selectInContext, false);
                    }
                }
            }
        });
    }


    private File getLocalFile() {

        String parentFile = deviceResult.anActionEvent.getProject().getBasePath();
        File file = new File(parentFile + "/" + duangFile + "/" + deviceResult.facet.getModule().getName() + "/" + localPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


}

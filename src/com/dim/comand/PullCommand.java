package com.dim.comand;

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
public class PullCommand
	extends Command {
	private final DeviceResult deviceResult;
	private final String originPath;
	private final String localFolder;
	private final String fileName;

	private final String duangFile = "duang";

	public PullCommand(DeviceResult deviceResult, String originPath, String fileName, String localFolder) {
		this.deviceResult = deviceResult;
		this.originPath = originPath;
		this.fileName = fileName;
		this.localFolder = localFolder;
	}

	public boolean run() {
		//pull 出来
		String dstPath = originPath + "/" + fileName;
		ChmodCommand chmodCommand = new ChmodCommand(deviceResult, dstPath);
		chmodCommand.run();
		File localFile = new File(getLocalFile(), fileName);
		/*String command = IdeSdks.getAndroidSdkPath().getPath() + "/platform-tools/adb -s " +
		                 deviceResult.device.getSerialNumber() + " pull \"" + dstPath +
		                 "\" \"" + localFile.getAbsolutePath() + "\"";
		println(command);
		String result = executeCommand(command);*/

		try {
			deviceResult.device.pullFile(dstPath, localFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (isSuccess(localFile)) {
//			scrollToTargetSource();
			return true;
		}

		return false;
	}

	private boolean isSuccess(String s) {
		return new File(s).exists();
	}

	private boolean isSuccess(File f) {
		return f.exists();
	}

	public void scrollToTargetSource() {
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
						VirtualFile fileNameFile = appFile.findChild(localFolder);
						if (fileNameFile == null) {
							file.refresh(true, true);
							fileNameFile = appFile.findChild(localFolder);
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
		File file =
			new File(parentFile, duangFile + "/" + deviceResult.facet.getModule().getName() + "/" +
			                     localFolder);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
}

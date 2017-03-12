package com.dim.action;

import com.dim.DeviceResult;
import com.dim.comand.MvCommand;
import com.dim.comand.PushCommand;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlFileImpl;

import org.jetbrains.annotations.NotNull;

import static com.dim.ui.NotificationHelper.error;
import static com.dim.ui.NotificationHelper.info;

/**
 * Created by dim on 16/3/31.
 */
public class PushAction
	extends BaseAction {
	private String parentFileName;

	private boolean isDataBase(String parentFileName) {
		return parentFileName.equals("databases");
	}

	private boolean isPreference(String parentFileName) {
		return parentFileName.equals("shared_prefs");
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		e.getPresentation().setVisible(runEnable(e));
	}

	@Override
	protected String getAndroidFacetName(AnActionEvent anActionEvent) {
		Object o = anActionEvent.getDataContext().getData(DataConstants.PSI_FILE);
		if (o instanceof XmlFileImpl) {

			return ((XmlFileImpl) o).getVirtualFile().getParent().getParent().getName();

		} else if (o instanceof PsiFile) {
			return parentFileName = ((PsiFile) o).getVirtualFile().getParent().getParent().getName();
		}
		return super.getAndroidFacetName(anActionEvent);
	}

	@Override
	protected boolean runEnable(AnActionEvent anActionEvent) {
		Object o = anActionEvent.getDataContext().getData(DataConstants.PSI_FILE);
		if (o instanceof XmlFileImpl) {
			parentFileName = ((XmlFileImpl) o).getVirtualFile().getParent().getName();
			if (isPreference(parentFileName)) {
				return true;
			}

		} else if (o instanceof PsiFile) {
			parentFileName = ((PsiFile) o).getVirtualFile().getParent().getName();
			if (isDataBase(parentFileName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	void run(final DeviceResult deviceResult, final AnActionEvent anActionEvent) {
		final Task.Backgroundable task =
			new Task.Backgroundable(deviceResult.anActionEvent.getProject(), "PushAction") {
				@Override
				public void run(@NotNull ProgressIndicator progressIndicator) {
					progressIndicator.setIndeterminate(true);
					Object o = anActionEvent.getDataContext().getData(DataConstants.PSI_FILE);
					final VirtualFile virtualFile;
					if (o != null) {
						virtualFile = ((PsiFile) o).getVirtualFile();
					} else {
						error("push failed.");
						return;
					}
					if (o instanceof XmlFileImpl) {
						if (!isPreference(parentFileName)) {
							return;
						}
						PushCommand pushCommand =
							new PushCommand(deviceResult,
							                virtualFile.getCanonicalPath(),
							                "/data/data/" + deviceResult.packageName +
							                "/shared_prefs/", virtualFile.getName());
						boolean run = pushCommand.run();
						if (run) {
							info("push " + virtualFile.getName() + " success!");
						} else {
							String name = virtualFile.getName();

							PushCommand pushSdcardCommand
								= new PushCommand(deviceResult,
								                  virtualFile.getCanonicalPath(),
								                  "/sdcard/",
								                  virtualFile.getName());
							boolean result = pushSdcardCommand.run();
							if (result) {
								MvCommand mvCommand =
									new MvCommand(deviceResult,
									              name,
									              "/data/data/" + deviceResult.packageName +
									              "/shared_prefs/");
								if (mvCommand.run()) {
									info("push " + virtualFile.getName() + " success!");
								}
							} else {
								error("push " + virtualFile.getName() + " failed.");
							}
						}
					} else {
						String parentFileName = virtualFile.getParent().getName();
						if (!isDataBase(parentFileName)) {
							return;
						}
						PushCommand pushCommand =
							new PushCommand(deviceResult,
							                virtualFile.getPath(),
							                "/data/data/" + deviceResult.packageName + "/databases/",
							                virtualFile.getName());
						boolean run = pushCommand.run();
						if (run) {
							info("push " + virtualFile.getName() + " success!");
						} else {
							String name = virtualFile.getName();

							PushCommand pushSdcardCommand =
								new PushCommand(deviceResult,
								                virtualFile.getPath(),
								                "/sdcard/",
								                virtualFile.getName());
							boolean result = pushSdcardCommand.run();
							if (result) {
								MvCommand mvCommand =
									new MvCommand(deviceResult,
									              name,
									              "/data/data/" + deviceResult.packageName + "/databases/");
								if (mvCommand.run()) {
									info("push " + virtualFile.getName() + " success!");
								}
							} else {
								error("push " + virtualFile.getName() + " failed.");
							}
						}
					}
				}
			};
		ProgressManager.getInstance().run(task);
	}
}

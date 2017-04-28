package com.dim.action;

import com.dim.ui.NotificationHelper;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by dim on 17/4/27.
 */
public class EditDbAction extends AnAction {

    private String targetFilePath;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        File file = new File("/Applications/DB Browser for SQLite.app");
        if (file.exists()) {
            java.awt.Desktop dp = java.awt.Desktop.getDesktop();
            try {
                dp.open(new File(targetFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            NotificationHelper.sendNotification(
                    "down <a href=https://github.com/sqlitebrowser/sqlitebrowser/releases>sqlitebrowser</a>"
                    , NotificationType.INFORMATION, new NotificationListener() {
                        @Override
                        public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent hyperlinkEvent) {
                            java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                            try {
                                dp.browse(hyperlinkEvent.getURL().toURI());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }
    }


    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        e.getPresentation().setVisible(runEnable(e));
    }


    protected boolean runEnable(AnActionEvent anActionEvent) {
        Object o = anActionEvent.getDataContext().getData(DataConstants.PSI_FILE);
        if (o instanceof PsiFile) {

            targetFilePath = ((PsiFile) o).getVirtualFile().getPath();
            if (isDataBase(((PsiFile) o).getVirtualFile().getParent().getName())) {
                if (isMacOs())
                    return true;
            }
        }
        return false;
    }

    private boolean isMacOs() {
        String osName = System.getProperty("os.name");
        return osName != null && osName.toLowerCase().contains("mac");
    }


    private boolean isDataBase(String parentFileName) {
        return parentFileName.equals("databases");
    }

}

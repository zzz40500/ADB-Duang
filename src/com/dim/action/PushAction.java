package com.dim.action;

import com.dim.DeviceResult;
import com.dim.comand.MvCommand;
import com.dim.comand.PushCommand;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlFileImpl;

import static com.dim.ui.NotificationHelper.error;
import static com.dim.ui.NotificationHelper.info;
import static com.dim.utils.Logger.println;

/**
 * Created by dim on 16/3/31.
 */
public class PushAction extends BaseAction {


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


        new Thread(new Runnable() {
            @Override
            public void run() {
                Object o = anActionEvent.getDataContext().getData(DataConstants.PSI_FILE);
                if (o instanceof XmlFileImpl) {
                    if (isPreference(parentFileName)) {
                        PushCommand pushCommand = new PushCommand(deviceResult, ((PsiFile) o).getVirtualFile().getPath(),
                                "data/data/" + deviceResult.packageName + "/shared_prefs/");
                        boolean run = pushCommand.run();
                        if (run) {
                            info("push " + ((PsiFile) o).getVirtualFile().getName() + " success !");
                        } else {
                            String name = ((PsiFile) o).getVirtualFile().getName();

                            PushCommand pushSdcardCommand = new PushCommand(deviceResult, ((PsiFile) o).getVirtualFile().getPath(),
                                    "/sdcard/ ");
                            boolean result = pushSdcardCommand.run();
                            if (result) {

                                MvCommand mvCommand=new MvCommand(deviceResult,name,"data/data/" + deviceResult.packageName + "/shared_prefs/");

                                if(mvCommand.run()){
                                    info("push " + ((PsiFile) o).getVirtualFile().getName() + " success !");
                                }
                            } else {
                                info("push " + ((PsiFile) o).getVirtualFile().getName() + " failed !");
                            }

                        }

                    }


                } else if (o instanceof PsiFile) {

                    String parentFileName = ((PsiFile) o).getVirtualFile().getParent().getName();
                    if (isDataBase(parentFileName)) {
                        PushCommand pushCommand = new PushCommand(deviceResult, ((PsiFile) o).getVirtualFile().getPath(),
                                "data/data/" + deviceResult.packageName + "/databases/");
                        boolean run = pushCommand.run();
                        if (run) {
                            info("push " + ((PsiFile) o).getVirtualFile().getName() + " success !");
                        } else {

                            String name = ((PsiFile) o).getVirtualFile().getName();

                            PushCommand pushSdcardCommand = new PushCommand(deviceResult, ((PsiFile) o).getVirtualFile().getPath(),
                                    "/sdcard/ ");
                            boolean result = pushSdcardCommand.run();
                            if (result) {

                                MvCommand mvCommand=new MvCommand(deviceResult,name,"data/data/" + deviceResult.packageName + "/databases/");

                                if(mvCommand.run()){
                                    info("push " + ((PsiFile) o).getVirtualFile().getName() + " success !");
                                }
                            } else {
                                info("push " + ((PsiFile) o).getVirtualFile().getName() + " failed !");
                            }

                        }
                    }
                }
            }
        }).start();


    }


}

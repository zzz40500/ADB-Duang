package com.dim.ui;

import com.dim.action.PullDatabaseAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.playback.commands.ActionCommand;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dim on 17/4/29.
 */
public class WindowTool implements ToolWindowFactory {
    private JPanel panel1;
    private JButton pullDatabaseFromDeviceButton;
    private JButton pullPreferenceFromDeviceButton;
    private JButton pullANRInfoFromButton;
    private JButton pullMethodTracingInfoButton1;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel1, "", false);
        toolWindow.getContentManager().addContent(content);
        pullDatabaseFromDeviceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform("com.dim.action.PullDatabaseAction");
            }
        });
        pullPreferenceFromDeviceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform("com.dim.action.PullPreferenceAction");
            }
        });
        pullANRInfoFromButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform("com.dim.action.PullAnrAction");
            }
        });
        pullMethodTracingInfoButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform("com.dim.action.PullTraceMethodAction");
            }
        });
    }

    private void actionPerform(String action) {

        ActionManager am = ActionManager.getInstance();
        am.tryToExecute(am.getAction(action),
                ActionCommand.getInputEvent(action), panel1, "", true);

    }
}

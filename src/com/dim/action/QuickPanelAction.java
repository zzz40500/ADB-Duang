package com.dim.action;

import com.dim.utils.Logger;
import com.intellij.ide.actions.QuickSwitchSchemeAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by qingwei on 16/5/12.
 */
public class QuickPanelAction extends QuickSwitchSchemeAction {


    @Override
    protected void fillActions(Project project, @NotNull DefaultActionGroup defaultActionGroup, @NotNull DataContext dataContext) {

        if (project == null) {
            return;
        }
        addAction("com.dim.action.PullDatabaseAction", defaultActionGroup);
        addAction("com.dim.action.PullPreferenceAction", defaultActionGroup);
        addAction("com.dim.action.PullAnrAction", defaultActionGroup);
        addAction("com.dim.action.PullTraceMethodAction", defaultActionGroup);
        addAction("com.dim.action.PushAction", defaultActionGroup);
    }

    protected boolean isEnabled() {
        return true;
    }

    private void addAction(final String actionId, final DefaultActionGroup toGroup) {
        final AnAction action = ActionManager.getInstance().getAction(actionId);
        if (action != null) {
            toGroup.add(action);
        }
    }

    protected String getPopupTitle(AnActionEvent e) {
        return "ADB Duang";
    }
}

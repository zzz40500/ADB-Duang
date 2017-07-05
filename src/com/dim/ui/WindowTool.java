package com.dim.ui;

import com.android.ddmlib.*;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.playback.commands.ActionCommand;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.android.sdk.AndroidSdkUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static com.dim.utils.DeviceHelper.checkDuangAppInstalled;
import static com.dim.utils.PropertyHelper.*;
import static com.dim.utils.ThirdAnaction.*;

/**
 * Created by dim on 17/4/29.
 */
public class WindowTool implements ToolWindowFactory {
    private JPanel panel1;
    private JButton pullDatabaseFromDeviceButton;
    private JButton pullPreferenceFromDeviceButton;
    private JButton pullANRInfoFromButton;
    private JButton pullMethodTracingInfoButton1;
    private JCheckBox layoutBoundaries;
    private JCheckBox GPUUpdates;
    private JComboBox devices;
    private JCheckBox overdraw;
    private JCheckBox rendering;
    private JButton ADBUninstallAppButton;
    private JButton ADBClearAppDataButton;
    private JButton ADBKillApp;
    private JButton ADBStartApp;
    private JButton ADBRestartApp;
    private JButton ADBClearRestartAppData;
    private List<IDevice> deviceList = new ArrayList<IDevice>();
    private IDevice selectDevice;
    private AndroidDebugBridge bridge;

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow toolWindow) {
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

        initThirdAnAction();
        initClickListener();
        update(project);
        AndroidDebugBridge.addDeviceChangeListener(new AndroidDebugBridge.IDeviceChangeListener() {

            @Override
            public void deviceConnected(IDevice iDevice) {
                if (selectDevice == null || !selectDevice.isOnline()) {
                    update(project);
                }
            }

            @Override
            public void deviceDisconnected(IDevice iDevice) {
            }

            @Override
            public void deviceChanged(IDevice iDevice, int i) {
            }
        });

    }

    private void initThirdAnAction() {
        ADBUninstallAppButton.setVisible(isEnable(UninstallAction));
        ADBClearAppDataButton.setVisible(isEnable(ClearDataAction));
        ADBKillApp.setVisible(isEnable(KillAction));
        ADBStartApp.setVisible(isEnable(StartAction));
        ADBRestartApp.setVisible(isEnable(RestartAction));
        ADBClearRestartAppData.setVisible(isEnable(ClearDataAndRestartAction));
        ADBUninstallAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform(UninstallAction);
            }
        });
        ADBClearAppDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform(ClearDataAction);
            }
        });
        ADBKillApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform(KillAction);
            }
        });
        ADBStartApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform(StartAction);
            }
        });
        ADBRestartApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform(RestartAction);
            }
        });
        ADBClearRestartAppData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerform(ClearDataAndRestartAction);
            }
        });
    }

    private boolean isEnable(String action) {
        ActionManager am = ActionManager.getInstance();
        return am.getAction(action) != null;
    }

    private void initClickListener() {
        overdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectDevice != null) {
                    setShowOverdrawEnabled(selectDevice, overdraw.isSelected(), new MultiLineReceiver() {
                        @Override
                        public boolean isCancelled() {
                            return false;
                        }

                        @Override
                        public void processNewLines(String[] strings) {
                            overdraw.setSelected(isShowOverdrawEnabled(selectDevice));
                        }
                    });
                }
            }
        });
        rendering.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (selectDevice != null) {
                    setProfileGPURenderingEnabled(selectDevice, rendering.isSelected(), new MultiLineReceiver() {

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }

                        @Override
                        public void processNewLines(String[] strings) {
                            rendering.setSelected(isShowProfileGPURendering(selectDevice));
                        }
                    });
                }
            }
        });
        layoutBoundaries.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (selectDevice != null) {
                    setDebugLayoutEnabled(selectDevice, layoutBoundaries.isSelected(), new MultiLineReceiver() {

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }

                        @Override
                        public void processNewLines(String[] strings) {
                            layoutBoundaries.setSelected(isDebugLayoutEnabled(selectDevice));
                        }
                    });
                }
            }
        });
        GPUUpdates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (selectDevice != null) {
                    setLayoutUpdateEnable(selectDevice, GPUUpdates.isSelected(), new MultiLineReceiver() {

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }

                        @Override
                        public void processNewLines(String[] strings) {
                            GPUUpdates.setSelected(isLayoutUpdateEnabled(selectDevice));
                        }
                    });
                }
            }
        });
    }

    private void update(@NotNull Project project) {
        boolean update = initDevices(project);
        if (selectDevice != null) {
            checkDuangAppInstalled(selectDevice);
            if (update) {
                devices.removeAllItems();
                int index = 0;
                for (int i = 0; i < deviceList.size(); i++) {
                    IDevice device = deviceList.get(i);
                    devices.addItem(device.getName());
                    if (device.getName().equals(selectDevice.getName()) && device.getSerialNumber().equals(selectDevice.getSerialNumber())) {
                        index = i;
                    }

                }
                devices.setSelectedIndex(index);
            }
            overdraw.setSelected(isShowOverdrawEnabled(selectDevice));
            rendering.setSelected(isShowProfileGPURendering(selectDevice));
            layoutBoundaries.setSelected(isDebugLayoutEnabled(selectDevice));
            GPUUpdates.setSelected(isLayoutUpdateEnabled(selectDevice));
        }
    }


    private boolean initDevices(Project project) {
        boolean update = true;
        if (bridge == null) {
            bridge = AndroidSdkUtils.getDebugBridge(project);
        }
        deviceList.clear();
        if (bridge != null) {
            IDevice[] devices = bridge.getDevices();
            if (devices != null) {
                for (IDevice device : devices) {
                    if (selectDevice != null) {
                        if (device.getName().equals(selectDevice.getName()) && device.getSerialNumber().equals(selectDevice.getSerialNumber())) {
                            selectDevice = device;
                            update = false;
                        }
                    }
                    deviceList.add(device);
                }
            }
            if (deviceList.size() > 0 && selectDevice == null) {
                selectDevice = deviceList.get(0);
                update = true;
            }
        } else {
            update = false;
        }
        return update;
    }

    private void actionPerform(String action) {
        ActionManager am = ActionManager.getInstance();
        am.tryToExecute(am.getAction(action),
                ActionCommand.getInputEvent(action), panel1, "", true);

    }
}

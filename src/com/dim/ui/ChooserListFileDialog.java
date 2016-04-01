package com.dim.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.intellij.refactoring.util.duplicates.Match;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;


public class ChooserListFileDialog extends DialogWrapper {
    private JPanel contentPane;
    private JComboBox fileListComboBox1;
    private String selectFile;


    public ChooserListFileDialog(Project project, final List<String> fileList) {
        super(project, true);
        setTitle("File List");
        setSize(500, 60);
        for (String file : fileList) {
            fileListComboBox1.addItem(file);
        }

        Disposer.register(myDisposable, new Disposable() {
            @Override
            public void dispose() {
            }
        });
        init();
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        fileListComboBox1.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fileListComboBox1.isPopupVisible() ) {
                    fileListComboBox1.showPopup();
                }
                fileListComboBox1.setSelectedIndex(Math.min(fileListComboBox1.getSelectedIndex()+1,fileListComboBox1.getItemCount()-1));

            }
        },KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        fileListComboBox1.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fileListComboBox1.isPopupVisible() ) {
                    fileListComboBox1.showPopup();
                }
                fileListComboBox1.setSelectedIndex(Math.max(fileListComboBox1.getSelectedIndex()-1,0));
            }
        },KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        fileListComboBox1.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doOKAction();

            }
        },KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
//        contentPane.requestFocus(false);
//        getButton(getOKAction()).addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                super.focusGained(e);
//                fileListComboBox1.requestFocus();
//
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                super.focusLost(e);
//                fileListComboBox1.requestFocus();
//
//            }
//        });
        fileListComboBox1.requestFocus();
    }

    @Override
    protected void doOKAction() {
        selectFile = fileListComboBox1.getSelectedItem().toString();
        super.doOKAction();
    }



    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }



    private void onCancel() {
        dispose();
    }


    public String getSelectedFile() {
        return selectFile;
    }
}

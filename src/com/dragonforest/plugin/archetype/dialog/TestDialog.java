package com.dragonforest.plugin.archetype.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

public class TestDialog extends DialogWrapper {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton button1;
    private JCheckBox checkBox1;
    private JPasswordField passwordField1;
    private JTextPane textPane1;

    public TestDialog(Project project) {
        super(project, true);

        setTitle("this is a test dialog!");

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        init();
        setSize(600, 400);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}

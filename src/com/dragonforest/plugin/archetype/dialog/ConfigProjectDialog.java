package com.dragonforest.plugin.archetype.dialog;

import com.dragonforest.plugin.archetype.listener.OnConfigProjectListener;
import com.dragonforest.plugin.archetype.model.AppModel;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.*;

public class ConfigProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonPrevious;
    private JTextField jtf_appName;
    private JTextField jtf_packageName;
    private JTextField jtf_applcationId;

    OnConfigProjectListener onConfigProjectListener;

    public ConfigProjectDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setTitle("config your app info..");
        initShow();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPrevious();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initShow() {
        jtf_appName.setText("MyArchetypeApp");
        jtf_packageName.setText("com.example.package");
        jtf_applcationId.setText("com.example.package");
    }

    private void onOK() {
        // add your code here
        String appName = jtf_appName.getText().toString().trim();
        String applicationId = jtf_applcationId.getText().toString().trim();
        String packageName = jtf_packageName.getText().toString().trim();

        if (appName == null
                || appName.equals("")
                || applicationId == null
                || applicationId.equals("")
                || packageName == null
                || packageName.equals("")) {
            Messages.showMessageDialog("输入项不能为空！", "警告", Messages.getInformationIcon());
            return;
        }
        dispose();

        AppModel appModel = new AppModel();
        appModel.setAppName(appName);
        appModel.setApplicationId(applicationId);
        appModel.setPackageName(packageName);
        if (onConfigProjectListener != null) {
            onConfigProjectListener.onFinish(appModel);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onPrevious() {
        if (onConfigProjectListener != null) {
            setVisible(false);
            onConfigProjectListener.onPrevious();
        }
    }

    public void setOnConfigProjectListener(OnConfigProjectListener onConfigProjectListener) {
        this.onConfigProjectListener = onConfigProjectListener;
    }

    public static void main(String[] args) {
        ConfigProjectDialog dialog = new ConfigProjectDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

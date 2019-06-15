package com.dragonforest.plugin.archetype.dialog;

import com.dragonforest.plugin.archetype.listener.OnConfigAboutInfoListener;
import com.dragonforest.plugin.archetype.model.AboutModel;

import javax.swing.*;
import java.awt.event.*;

public class ConfigAboutInfoDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonPrevious;
    private JButton buttonNext;
    private JTextField jtf_companyName;
    private JTextField jtf_companyPhone;
    private JTextField jtf_companyUrl;
    private JTextField jtf_support;
    private JTextField jtf_suppertPhone;

    private OnConfigAboutInfoListener onConfigAboutInfoListener;

    public ConfigAboutInfoDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonPrevious);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setTitle("config about information..");
        initShow();

        buttonPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPrevious();
            }
        });

        buttonNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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
        jtf_companyName.setText("北京大有中诚科技有限公司");
        jtf_companyPhone.setText("400-705-6818");
        jtf_companyUrl.setText("http://www.coop168.net/");
        jtf_support.setText("北京大有中诚科技有限公司");
        jtf_suppertPhone.setText("400-705-6818");
    }

    private void onPrevious() {
        if (onConfigAboutInfoListener != null) {
            setVisible(false);
            onConfigAboutInfoListener.onPrevious();
        }
    }

    private void onOK() {
        // add your code here

        String companyName = jtf_companyName.getText();
        String companyPhone = jtf_companyPhone.getText();
        String companyUrl = jtf_companyUrl.getText();
        String support = jtf_support.getText();
        String supportPhone = jtf_suppertPhone.getText();
        if (companyName == null
                || companyName.equals("")
                || companyPhone == null
                || companyPhone.equals("")
                || companyUrl == null
                || companyUrl.equals("")
                || support == null
                || support.equals("")
                || supportPhone == null
                || supportPhone.equals("")) {
            return;
        }
        setVisible(false);
        AboutModel aboutModel = new AboutModel(companyName, companyPhone, companyUrl, support, supportPhone);
        if (onConfigAboutInfoListener != null)
            onConfigAboutInfoListener.onFinish(aboutModel);

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public void setOnConfigAboutInfoListener(OnConfigAboutInfoListener onConfigAboutInfoListener) {
        this.onConfigAboutInfoListener = onConfigAboutInfoListener;
    }

}

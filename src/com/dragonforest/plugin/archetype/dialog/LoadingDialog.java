package com.dragonforest.plugin.archetype.dialog;

import javax.swing.*;
import java.awt.event.*;

public class LoadingDialog extends JDialog {
    private JPanel contentPane;
    private JProgressBar progressBar1;
    private JLabel jb_title;

    public static LoadingDialog instance=new LoadingDialog();

    public LoadingDialog() {
        setContentPane(contentPane);
        setModal(false);

        setSize(800,100);
        setTitle("please wait a moment...");
        progressBar1.setIndeterminate(true);
        setLocationRelativeTo(null);

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

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setTitle(String title){
        jb_title.setText(title);
    }

    public static void loading(String title){
        instance.setTitle(title);
        instance.pack();
        instance.setVisible(true);
    }

    public static void cancel(){
        instance.dispose();
    }
}

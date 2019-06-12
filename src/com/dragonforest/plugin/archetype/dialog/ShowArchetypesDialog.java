package com.dragonforest.plugin.archetype.dialog;

import com.dragonforest.plugin.archetype.listener.OnChooseArchetypeListener;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class ShowArchetypesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList archetypeList;

    OnChooseArchetypeListener onChooseArchetypeListener;

    public ShowArchetypesDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        contentPane.setSize(400, 600);
        setLocationRelativeTo(null);

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

    private void onOK() {
        // add your code here
        dispose();
        String selectedValue = (String) archetypeList.getSelectedValue();
        if (selectedValue == null || selectedValue.equals(""))
            return;
        if (onChooseArchetypeListener != null) {
            onChooseArchetypeListener.onChoose(selectedValue);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /**
     * 设置列表数据项
     *
     * @param dataList
     */
    public void setListData(List<String> dataList) {
        // jlist设置数据项
        archetypeList.setListData(dataList.toArray(new String[dataList.size()]));
    }

    public void setOnChooseArchetypeListener(OnChooseArchetypeListener onChooseArchetypeListener) {
        this.onChooseArchetypeListener = onChooseArchetypeListener;
    }

    public static void main(String[] args) {
        ShowArchetypesDialog dialog = new ShowArchetypesDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

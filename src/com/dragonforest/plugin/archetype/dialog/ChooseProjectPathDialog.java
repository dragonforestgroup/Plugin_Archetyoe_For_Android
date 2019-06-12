package com.dragonforest.plugin.archetype.dialog;


import com.dragonforest.plugin.archetype.listener.OnFinishProjectPathListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class ChooseProjectPathDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonPrevious;
    private JTextField jtf_baseDir;
    private JTextField jtf_projectName;
    private JButton jb_chooseProjectDir;

    private final String defaultProjectName = "MyArchetypeProject";
    private int projectIndex = 1;

    Project project = null;
    OnFinishProjectPathListener onFinishProjectPathListener;

    public ChooseProjectPathDialog(Project project) {
        this.project = project;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(600, 400);

        jtf_baseDir.setEditable(false);
        setLocationRelativeTo(null);
        setTitle("set your project path..");
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

        jb_chooseProjectDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onChooseBaseDirClicked();
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
        String basePath = project.getBasePath();
        String name = project.getName();
        String workSpacePath = basePath.substring(0, basePath.indexOf(name)-1);

        String projectName = defaultProjectName;
        while (true) {
            File file = new File(workSpacePath + File.separator + projectName);
            if (file.exists()) {
                projectName = defaultProjectName + projectIndex;
                projectIndex++;
                continue;
            }
            break;
        }

        jtf_baseDir.setText(workSpacePath);
        jtf_projectName.setText(projectName);
    }

    private void onChooseBaseDirClicked() {
        String chooseDir = getChooseDir();
        if (chooseDir == null)
            return;
        jtf_baseDir.setText(chooseDir);
        Messages.showMessageDialog("选择文件路径" + chooseDir, "", Messages.getInformationIcon());
    }

    private String getChooseDir() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String basePath = project.getBasePath();
        String name = project.getName();
        String workSpacePath = basePath.substring(0, basePath.indexOf(name));
        chooser.setSelectedFile(new File(workSpacePath));
        chooser.setDialogTitle("please choose project path:");
        int returnVal = chooser.showOpenDialog(chooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    private void onOK() {
        // add your code here

        String baseDir = jtf_baseDir.getText().toString().trim();
        String projectName = jtf_projectName.getText().toString().trim();

        if (baseDir == null
                || baseDir.equals("")
                || projectName == null
                || projectName.equals("")) {
            Messages.showMessageDialog("名称不能为空！", "警告", Messages.getErrorIcon());
            return;
        }

        dispose();

        String projectDir = baseDir + File.separator + projectName;
        File file = new File(projectDir);
        if (!file.exists()) {
            file.mkdir();
        }
        if (onFinishProjectPathListener != null) {
            onFinishProjectPathListener.onFinish(projectDir);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onPrevious(){
        setVisible(false);
        if(onFinishProjectPathListener!=null){
            onFinishProjectPathListener.onPrevious();
        }
    }

    public void setOnFinishProjectPathListener(OnFinishProjectPathListener onFinishProjectPathListener) {
        this.onFinishProjectPathListener = onFinishProjectPathListener;
    }

}

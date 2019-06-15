package com.dragonforest.plugin.archetype.utils;

import com.intellij.openapi.ui.Messages;

import javax.swing.*;

public class MessageUtil {
    public static boolean isDebug = false;

    public static void showMessage(String title, String message, Icon icon) {
        Messages.showMessageDialog(message, title, icon);
    }

    public static void debugMessage(String title, String message, Icon icon) {
        if (isDebug) {
            showMessage(title, message, icon);
        }
    }

    public static String showMessageDialogInputArchetype(){
        return Messages.showInputDialog("please input github url here:","add archetype",Messages.getInformationIcon());
    }
}

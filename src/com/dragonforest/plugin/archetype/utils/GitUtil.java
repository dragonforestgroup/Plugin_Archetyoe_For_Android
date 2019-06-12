package com.dragonforest.plugin.archetype.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import com.intellij.openapi.ui.Messages;

import java.io.File;

public class GitUtil {
    /**
     * 克隆项目到本地
     *
     * @param gitUri
     * @param localPath
     */
    public static boolean cloneToLocalPath(String gitUri,String localPath){
        try {
            Git git= Git.cloneRepository()
                    .setURI(gitUri)
                    .setDirectory(new File(localPath))
                    .call();
            return true;
        } catch (GitAPIException e) {
            e.printStackTrace();
            Messages.showMessageDialog(e.getMessage(),"clone项目出错！",Messages.getErrorIcon());
        }
        return false;
    }

    public static void asynCloneToLocalPath(String gitUri,String localPath,OnCloneListener onCloneListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Git git= Git.cloneRepository()
                            .setURI(gitUri)
                            .setDirectory(new File(localPath))
                            .call();
                    if(onCloneListener!=null){
                        onCloneListener.onCloneSuccess();
                    }
                } catch (GitAPIException e) {
                    e.printStackTrace();
                    if(onCloneListener!=null){
                        onCloneListener.onCloneError(e.getMessage());
                    }
                }
            }
        }).start();
    }

    public interface OnCloneListener{
        void onCloneSuccess();
        void onCloneError(String msg);
    }
}

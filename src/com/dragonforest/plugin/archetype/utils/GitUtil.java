package com.dragonforest.plugin.archetype.utils;

import com.intellij.dvcs.ui.DvcsBundle;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import com.intellij.openapi.ui.Messages;
import org.eclipse.jgit.lib.AnyObjectId;

import java.io.File;
import java.util.Collection;

public class GitUtil {
    public static volatile boolean isGitSuccess=false;
    /**
     * 克隆项目到本地
     *
     * @param gitUri
     * @param localPath
     */
    public static boolean cloneToLocalPath(String gitUri,String localPath){
        try {
            isGitSuccess=false;
            Git git= Git.cloneRepository()
                    .setURI(gitUri)
                    .setDirectory(new File(localPath))
                    .call();
            isGitSuccess=true;
            return true;
        } catch (GitAPIException e) {
            e.printStackTrace();
            Messages.showMessageDialog(e.getMessage(),"clone项目出错！",Messages.getErrorIcon());
        }
        return false;
    }

    public static void asynCloneToLocalPath(Project project, String gitUri, String localPath, OnCloneListener onCloneListener){

        new Task.Backgroundable(project, DvcsBundle.message("cloning.repository", gitUri)) {
            @Override
            public void run(ProgressIndicator indicator) {
                GitUtil.cloneToLocalPath(gitUri,localPath);
            }

            @Override
            public void onSuccess() {
               if(isGitSuccess){
                   if(onCloneListener!=null){
                       onCloneListener.onCloneSuccess();
                   }
               }else{
                   if(onCloneListener!=null){
                       onCloneListener.onCloneError("clone error");
                   }
               }
            }
        }.queue();
    }

    public interface OnCloneListener{
        void onCloneSuccess();
        void onCloneError(String msg);
    }
}

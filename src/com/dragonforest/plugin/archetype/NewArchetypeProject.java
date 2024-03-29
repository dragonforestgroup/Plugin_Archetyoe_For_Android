package com.dragonforest.plugin.archetype;

import com.dragonforest.plugin.archetype.config.Configuration;
import com.dragonforest.plugin.archetype.dialog.*;
import com.dragonforest.plugin.archetype.listener.OnChooseArchetypeListener;
import com.dragonforest.plugin.archetype.listener.OnConfigAboutInfoListener;
import com.dragonforest.plugin.archetype.listener.OnConfigProjectListener;
import com.dragonforest.plugin.archetype.listener.OnFinishProjectPathListener;
import com.dragonforest.plugin.archetype.model.AboutModel;
import com.dragonforest.plugin.archetype.model.AppModel;
import com.dragonforest.plugin.archetype.model.Result;
import com.dragonforest.plugin.archetype.utils.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class NewArchetypeProject extends AnAction {

    private Project project;

    /**
     * 选择的archeType, 当前是github项目地址
     */
    String archeTypeName = null;
    /**
     * 项目主目录
     */
    String localProjectPath = null;
    /**
     * app信息
     */
    AppModel appModel = null;

    /**
     * about信息
     */
    AboutModel aboutModel=null;

    // 对话框
    private ShowArchetypesDialog showArchetypesDialog;
    private ChooseProjectPathDialog chooseProjectPathDialog;
    private ConfigProjectDialog configProjectDialog;
    private ConfigAboutInfoDialog configAboutInfoDialog;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // TODO: insert action logic here

        project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        MessageUtil.debugMessage("DragonForest", "创建一个archetype project:basePath:" + project.getBasePath() + ",getProjectFilePath:" + project.getProjectFilePath(), Messages.getInformationIcon());
        // 显示选择Archetype对话框
        showChooseArchetypeDialog();

        //测试dialog
//        TestDialog testDialog=new TestDialog(project);
//        testDialog.show();

    }

    /**
     * 选择archetype
     */
    private void showChooseArchetypeDialog() {
        showArchetypesDialog = new ShowArchetypesDialog();
        showArchetypesDialog.setOnChooseArchetypeListener(new OnChooseArchetypeListener() {
            @Override
            public void onChoose(String archetypeName) {
                NewArchetypeProject.this.archeTypeName = archetypeName;

                // 显示选择项目路径对话框
                showChooseProjectPathDialog();

            }

            @Override
            public void onPrevious() {

            }
        });

        //设置数据
        showArchetypesDialog.setListData(Configuration.getInstance().getArchetypes());
        showArchetypesDialog.setVisible(true);

    }

    /**
     * 选择项目路径
     */
    private void showChooseProjectPathDialog() {
        chooseProjectPathDialog = new ChooseProjectPathDialog(project);
        chooseProjectPathDialog.setOnFinishProjectPathListener(new OnFinishProjectPathListener() {
            @Override
            public void onFinish(String chooseDir) {
                NewArchetypeProject.this.localProjectPath = chooseDir;
                MessageUtil.debugMessage("", "选择最终的path：" + chooseDir, Messages.getInformationIcon());

                // 显示配置about的属性dialog
                showConfigAboutInfoDialog();
            }

            @Override
            public void onPrevious() {
                showArchetypesDialog.setVisible(true);
            }
        });
        chooseProjectPathDialog.setVisible(true);
    }

    /**
     * 配置about信息
     */
    private void showConfigAboutInfoDialog(){
        configAboutInfoDialog = new ConfigAboutInfoDialog();
        configAboutInfoDialog.setOnConfigAboutInfoListener(new OnConfigAboutInfoListener() {
            @Override
            public void onFinish(AboutModel aboutModel) {
                NewArchetypeProject.this.aboutModel=aboutModel;
                showConfigProjectDialog();
            }

            @Override
            public void onPrevious() {
                chooseProjectPathDialog.setVisible(true);
            }
        });
        configAboutInfoDialog.setVisible(true);
    }

    /**
     * 配置app信息
     */
    private void showConfigProjectDialog() {
        configProjectDialog = new ConfigProjectDialog();
        configProjectDialog.setOnConfigProjectListener(new OnConfigProjectListener() {
            @Override
            public void onFinish(AppModel appModel) {
                NewArchetypeProject.this.appModel = appModel;

                MessageUtil.debugMessage("", "配置的项为：" + appModel.getAppName() + "," + appModel.getAppName() + "," + appModel.getApplicationId(), Messages.getInformationIcon());

                // 1.从git上克隆
                // TODO: 2019/6/12 这里加一个进度框
                // FIXME: 2019/6/12 异步克隆的问题（线程切换）

                // 异步加载
                LoadingDialog.loading("cloning from " + archeTypeName);
                GitUtil.asynCloneToLocalPath(project, NewArchetypeProject.this.archeTypeName, NewArchetypeProject.this.localProjectPath, new GitUtil.OnCloneListener() {
                    @Override
                    public void onCloneSuccess() {
                        LoadingDialog.loading("modifying...");
                        // 2.修改包名，applicationid,appName 等
                        Result modifyResult = modifyAppInfo();
                        if (!modifyResult.isOk()) {
                            LoadingDialog.cancel();
                            MessageUtil.showMessage("警告", modifyResult.getMsg(), Messages.getErrorIcon());
                            return;
                        }
                        LoadingDialog.cancel();
                        // 3.打开项目
                        MessageUtil.showMessage("创建完成", "即将打开项目：" + new File(localProjectPath).getAbsolutePath(), Messages.getInformationIcon());
                        try {
                            ProjectManager.getInstance().loadAndOpenProject(localProjectPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JDOMException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCloneError(String msg) {
                        LoadingDialog.cancel();
                        MessageUtil.showMessage("错误", "克隆出错！！！" + msg, Messages.getErrorIcon());
                    }
                });

                // 同步加载
                /*
                LoadingDialog.loading("cloning from " + archeTypeName);
                boolean isCloned = GitUtil.cloneToLocalPath(NewArchetypeProject.this.archeTypeName, NewArchetypeProject.this.localProjectPath);
                if(!isCloned){
                    LoadingDialog.cancel();
                    MessageUtil.showMessage("错误", "克隆出错！！！", Messages.getErrorIcon());
                    return;
                }
                // 2.修改包名，applicationid,appName 等
                boolean isModified = modifyAppInfo();
                if (!isModified) {
                    LoadingDialog.cancel();
                    MessageUtil.showMessage("警告", "修改app信息失败！！！", Messages.getErrorIcon());
                    return;
                }
                LoadingDialog.cancel();
                // 3.打开项目
                MessageUtil.showMessage("创建完成", "请打开项目：" + localProjectPath, Messages.getInformationIcon());
                ProjectStructureDetector projectStructureDetector

                */
            }

            @Override
            public void onPrevious() {
                configAboutInfoDialog.setVisible(true);
            }
        });
        configProjectDialog.setVisible(true);
    }

    /**
     * 修改包名，applicationid,appName 等
     */
    private Result modifyAppInfo() {
        Result result=new Result();
        // 从AndroidManifest文件中读取原有包名
        String manifestPath = NewArchetypeProject.this.localProjectPath
                + File.separator
                + "app"
                + File.separator
                + "src"
                + File.separator
                + "main"
                + File.separator
                + "AndroidManifest.xml";
        XmlUtil manifestXmlUtil = new XmlUtil(manifestPath);
        String packageNameOld = manifestXmlUtil.readPackageNameFromManifest();
        if (packageNameOld == null) {
            result.setMsg("获取的包名为空!,请检查项目中Manifest文件是否存在和格式是否正确！！！！");
            result.setOk(false);
            return result;
        }
        MessageUtil.debugMessage("获取包名完成", "获取的原有包名为：" + packageNameOld, Messages.getInformationIcon());

        // 创建新的包名临时目录（必须是临时目录，避免包名路径可能和原有包名路径重合），并将原包名下的文件拷贝过去
        String mainPackageDir = localProjectPath
                + File.separator
                + "app"
                + File.separator
                + "src"
                + File.separator
                + "main"
                + File.separator
                + "java"; //包名基本目录
        String tempPackageDir = localProjectPath
                + File.separator
                + "temp"; // 临时目录，包含所有原有包名下的内容
        String[] splitOldPackageName = packageNameOld.split("\\.");
        String innerPackageDirOld = "";
        for (int i = 0; i < splitOldPackageName.length; i++) {
            innerPackageDirOld += splitOldPackageName[i] + File.separator;
        }
        String packagePathOld = mainPackageDir + File.separator + innerPackageDirOld;
        MessageUtil.debugMessage("获取包名路径完成", "old包名路径为：" + packagePathOld, Messages.getInformationIcon());
        boolean isPackageDirOldCopyed = FileUtil.copyDir(packagePathOld, tempPackageDir);
        if (!isPackageDirOldCopyed) {
            result.setMsg("拷贝包目录失败");
            result.setOk(false);
            return result;
        }
        MessageUtil.debugMessage("拷贝成功：", "拷贝成功", Messages.getInformationIcon());

        // 遍历新包名下的所有文件，查找替换包名字符串
        Collection<File> javaFiles = FileUtil.listFiles(tempPackageDir, new String[]{"java"}, true);
        Iterator<File> iterator = javaFiles.iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();
            FileUtil.readAndReplace(file.getAbsolutePath(), packageNameOld, appModel.getPackageName());
        }

        // 修改AndroidManifest.xml
        boolean isManifestModified = manifestXmlUtil.modifyManifest(appModel.getPackageName());
        if (!isManifestModified) {
            result.setMsg("Manifest.xml修改出错！Manifest.xml是否存在！");
            result.setOk(false);
            return result;
        }

        // 修改strings.xml
        String StringsXmlPath = NewArchetypeProject.this.localProjectPath
                + File.separator
                + "app"
                + File.separator
                + "src"
                + File.separator
                + "main"
                + File.separator
                + "res"
                + File.separator
                + "values"
                + File.separator
                + "strings.xml";
        XmlUtil stringsXmlUtil = new XmlUtil(StringsXmlPath);
        boolean isStringsModified = stringsXmlUtil.modifyStrings(appModel.getAppName(),aboutModel);
        if (!isStringsModified) {
            result.setMsg("Strings.xml文件修改失败！请检查strings.xml是否配置正常！");
            result.setOk(false);
            return result;
        }

        // 修改config.gradle
        String gradleConfigPath = NewArchetypeProject.this.localProjectPath
                + File.separator
                + "config.gradle";
        GradleUtil gradleUtil=new GradleUtil(gradleConfigPath);
        boolean isConfigGradleModified = gradleUtil.modifiedGradleConfig(appModel.getApplicationId(),appModel.getAppName(),"MMMMMMMMM");
        if (!isConfigGradleModified) {
            result.setMsg("gradle配置修改出错！请检查项目根目录下config.gradle是否存在！");
            result.setOk(false);
            return result;
        }

        // 删除main/java原有包名目录，并将临时目录中的文件拷贝过去,最后删除临时目录
        boolean isMainPackageDirCleaned = FileUtil.cleanDir(mainPackageDir);
        if (!isMainPackageDirCleaned) {
            result.setMsg("main/java 目录旧包名结构清理失败！");
            result.setOk(false);
            return result;
        }
        String[] splitNewPackageName = appModel.getPackageName().split("\\.");
        String innerPackageDirNew = "";
        for (int i = 0; i < splitNewPackageName.length; i++) {
            innerPackageDirNew += splitNewPackageName[i] + File.separator;
        }
        String packagePathNew = mainPackageDir + File.separator + innerPackageDirNew;
        boolean isTempPackageDirNewCopyed = FileUtil.copyDir(tempPackageDir + File.separator, packagePathNew);
        if (!isTempPackageDirNewCopyed) {
            result.setMsg("临时目录拷贝失败！");
            result.setOk(false);
            return result;
        }
        FileUtil.deleteDir(tempPackageDir);

        // 删除test/java 下原有目录，并创建新的包名目录
        String testPackageDir = localProjectPath
                + File.separator
                + "app"
                + File.separator
                + "src"
                + File.separator
                + "test"
                + File.separator
                + "java"; //包名基本目录
        boolean isTestPackageDirCleaned = FileUtil.cleanDir(testPackageDir);
        if (!isTestPackageDirCleaned) {
            result.setMsg("test/java 目录旧包名结构清理失败！");
            result.setOk(false);
            return result;
        }
        String testPackagePathNew = testPackageDir + File.separator + innerPackageDirNew;
        boolean isMkTestPackage = FileUtil.mkDir(testPackagePathNew);
        if (!isMkTestPackage) {
            result.setMsg("test/java 目录新包名结构创建失败！");
            result.setOk(false);
            return result;
        }

        // 删除androidTest/java 下原有目录，并创建新的包名目录
        String androidTestPackageDir = localProjectPath
                + File.separator
                + "app"
                + File.separator
                + "src"
                + File.separator
                + "androidTest"
                + File.separator
                + "java"; //包名基本目录
        boolean isAndroidTestPackageDirCleaned = FileUtil.cleanDir(androidTestPackageDir);
        if (!isAndroidTestPackageDirCleaned) {
            result.setMsg("androidTest/java 目录旧包名结构清理失败！");
            result.setOk(false);
            return result;
        }
        String androidTestPackagePathNew = androidTestPackageDir + File.separator + innerPackageDirNew;
        boolean isMkAndroidTestPackagePath = FileUtil.mkDir(androidTestPackagePathNew);
        if (!isMkAndroidTestPackagePath) {
            result.setMsg("androidTest/java 目录新包名结构创建失败！");
            result.setOk(false);
            return result;
        }
        result.setMsg("项目信息修改配置成功！");
        result.setOk(true);
        return result;
    }
}

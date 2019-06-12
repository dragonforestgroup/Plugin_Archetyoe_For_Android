package com.dragonforest.plugin.archetype.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

public class FileUtil {
    public static boolean copyFile(String srcFile, String dstFile) {
        try {
            FileUtils.copyFile(new File(srcFile), new File(dstFile));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean copyDir(String srcDir, String dstDir) {
        try {
            FileUtils.copyDirectory(new File(srcDir), new File(dstDir));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean copyDirToDir(String srcDir, String dstDir) {
        try {
            FileUtils.copyDirectoryToDirectory(new File(srcDir), new File(dstDir));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 剪切文件
     *
     * @param srcDir
     * @param dstDir
     * @return
     */
    public static boolean moveDirToDir(String srcDir, String dstDir) {
        try {
            FileUtils.moveDirectoryToDirectory(new File(srcDir), new File(dstDir),true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



    public static Collection<File> listFiles(String dir, String[] extentions, boolean recusive) {
        return FileUtils.listFiles(new File(dir), extentions, recusive);
    }

    /**
     * 替换文件中的字符串
     *
     * @param filePath
     * @param oldText
     * @param newText
     * @return
     */
    public static boolean readAndReplace(String filePath, String oldText, String newText) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        BufferedReader bufReader = null;
        BufferedWriter bufWriter = null;
        try {
            // 读取内容
            StringBuilder stringBuilder = new StringBuilder();
            bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            bufReader.close();
            String content = stringBuilder.toString();

            // 替换
            String newContent = content.replace(oldText, newText);

            // 写入新内容
            bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath), false)));
            bufWriter.write(newContent);
            bufWriter.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 清除文件夹下的所有文件和文件夹
     *
     * @param dir
     * @return
     */
    public static boolean cleanDir(String dir) {
        try {
            FileUtils.cleanDirectory(new File(dir));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除文件夹
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(String dir) {
        try {
            FileUtils.deleteDirectory(new File(dir));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建目录
     * @param dir
     * @return
     */
    public static boolean mkDir(String dir){
        try {
            FileUtils.forceMkdir(new File(dir));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

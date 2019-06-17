package com.dragonforest.plugin.archetype.utils;

import java.io.*;

public class GradleUtil {

    String configPath;

    public GradleUtil(String configPath) {
        this.configPath = configPath;
    }

    /**
     * 修改config.gradle
     * 修改内容：
     * 1.applicationId
     * 2.appname
     * 3.appId
     *
     * @param applicationId
     * @param appName
     * @param appId
     * @return
     */
    // TODO: 2019/6/12  待实现 需要按照config.gradle的格式来搞... 
    public boolean modifiedGradleConfig(String applicationId, String appName, String appId) {

        String configContent = readConfigGradle(configPath);
        if (configContent == null || configContent.equals("")) {
            return false;
        }
        // 修改applicationId
        String applicationIdOld = findPropertyValue(configContent, "applicationId");
        if (applicationIdOld != null) {
            configContent = configContent.replace(applicationIdOld, applicationId);
        }
        // 修改appname
        String appNameOld = findPropertyValue(configContent, "appName");
        if (applicationIdOld != null) {
            configContent = configContent.replace(appNameOld, appName);
        }
        // 修改appId
        String appIdOld = findPropertyValue(configContent, "appId");
        if (appIdOld != null) {
            configContent = configContent.replace(appIdOld, appId);
        }
        return saveToFile(configContent);
    }

    /**
     * 读取文件内容
     * @param configPath
     * @return
     */
    private String readConfigGradle(String configPath) {
        File file = new File(configPath);
        if (!file.exists()) {
            System.out.println("文件不存在！");
            return "";
        }
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                sb.append(strLine);
                sb.append("\n");
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 保存到文件
     * 覆盖写入
     *
     * @param content
     * @return
     */
    private boolean saveToFile(String content) {
        Boolean isSaved = false;
        File file = new File(configPath);
        if (!file.exists()) {
            System.out.println("文件不存在！");
            return false;
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bw.write(content);
            bw.flush();
            isSaved = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSaved;
    }

    /**
     * 通过属性名查找到属性值
     *
     * @param content
     * @param propertyName
     * @return
     */
    private String findPropertyValue(String content, String propertyName) {
        String value = "";
        int beginIndex = content.indexOf(propertyName);
        if (beginIndex == -1) {
            //没找到
            return null;
        }
        String tempStr = content.substring(beginIndex);
        String tempStr2 = tempStr.substring(tempStr.indexOf("'") + 1);
        value = tempStr2.substring(0, tempStr2.indexOf("'"));
        return value;
    }

}

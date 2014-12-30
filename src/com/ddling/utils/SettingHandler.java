package com.ddling.utils;

import net.sf.json.JSONObject;

import java.io.*;

/**
 * Created by lingdongdong on 14/12/29.
 */
public class SettingHandler {

    private static SettingHandler settingHandlerInstance = null;

    public static SettingHandler getSettingHandlerInstance() {

        if (settingHandlerInstance == null) {
            settingHandlerInstance = new SettingHandler();
        }

        return settingHandlerInstance;
    }

    public String getSettingValue(String key) {

        String settingStr = readJsonSettingFile(Constants.SETTING_FILE_PATH);
        JSONObject jsonObject = JSONObject.fromObject(settingStr);

        String settingValue = null;
        settingValue = jsonObject.getString(key);

        return settingValue;
    }

    private String readJsonSettingFile(String path) {
        File file = new File(path);
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();

        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        SettingHandler settingHandler = SettingHandler.getSettingHandlerInstance();
        String value = settingHandler.getSettingValue("smtp_server_port");
        System.out.println(value);
    }
}

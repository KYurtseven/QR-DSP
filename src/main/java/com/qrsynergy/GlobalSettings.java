package com.qrsynergy;

public class GlobalSettings {

    /**
     * Local: for local development
     * Test: For demo test
     * Production: Not used!
     * @return current production mode: LOCAL, TEST or PRODUCTION
     */
    public static ProductionMode getCurrentProductionMode(){
        return ProductionMode.TEST;
    }


    /**
     *
     * @return uploaded file location
     */
    public static String getUploadLocation(){
        return System.getProperty("user.dir") + "\\FILES\\";
    }

    /**
     *
     * @return location for storing excels in the temp directory
     */
    public static String getTempLocation(){
        return System.getProperty("user.dir") + "\\TEMP\\";
    }

    /**
     *
     * @return full path, including "qrdsp.apk"
     */
    public static String getMobileAppLocation(){
       return System.getProperty("user.dir") + "\\MOBILE_APP\\qrdsp.apk";
    }

    /**
     *
     * @return img file location
     */
    public static String getImgLocation(){
        return System.getProperty("user.dir") + "\\img\\";
    }
}

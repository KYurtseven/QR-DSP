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
}

package com.qrsynergy.domain;

public final class ConstURL {

    private static String root = "LOCAL";
    private static final String localroot = "http://localhost:3000/";
    private static final String login = "api/user/login";

    public static final String loginURL = getLoginURL();

    private static String getLoginURL() {
        if(root.equals("LOCAL")){
            return localroot + login;
        }
        // TODO
        return localroot + login;
    }
}

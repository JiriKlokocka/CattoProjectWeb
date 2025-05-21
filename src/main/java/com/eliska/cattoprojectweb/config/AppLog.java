package com.eliska.cattoprojectweb.config;

import com.eliska.cattoprojectweb.CattoProjectWebApplication;

public class AppLog {
    public static void Log(String message) {
        CattoProjectWebApplication.MyLog(message);
    }
}

package com.eliska.cattoprojectweb.config;

import com.eliska.cattoprojectweb.CattoProjectWebApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

public class AppLog {



    public static void Log(String message) {
        CattoProjectWebApplication.MyLog(message);
    }
}

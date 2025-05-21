package com.eliska.cattoprojectweb.config;

import com.eliska.cattoprojectweb.CattoProjectWebApplication;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class MyErrorHandler extends DefaultResponseErrorHandler {
//    @Override
//    public void handleError(ClientHttpResponse response) throws IOException {
//        CattoProjectWebApplication.MyLog("+++++++++++++++" + response.getStatusCode().toString());
//    }
}
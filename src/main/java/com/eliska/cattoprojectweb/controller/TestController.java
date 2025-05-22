package com.eliska.cattoprojectweb.controller;

import com.eliska.cattoprojectweb.config.Config;
import com.eliska.cattoprojectweb.config.LoginRequestEntity;
import com.eliska.cattoprojectweb.config.LoginResponseEntity;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


@RestController
public class TestController {

    //------------ 1st --------------------
    //@Autowired
    //private final Config config;

    //@Autowired
    //@Qualifier("myRestTemplate")
    //private RestTemplate restTemplate;

    //-------------2nd--------------------
    //private final Config config;

    //@Qualifier("myRestTemplate")
    //private final RestTemplate restTemplate;

    //public TestController(@Qualifier("myRestTemplate") RestTemplate restTemplate, Config config) {
    //this.config = config;
    //this.restTemplate = restTemplate;
    //}

    //-------------3th--------------------
    private final Config config;
    private final RestTemplate restTemplateTokened;


    @Autowired
    private RestTemplate restTemplateNormal = new RestTemplate();
    @Value("${myapp.apiPath}")
    private String apiPath;

    public TestController(Config config, RestTemplateBuilder restTemplateBuilder) {
        this.config = config;
        this.restTemplateTokened = config.someRestTemplate(restTemplateBuilder);
    }

    public void AuthenticateToAPI(String apiAuthUrl, String username, String password) {
        ResponseEntity<LoginResponseEntity> loginResponse = restTemplateNormal.postForEntity(apiAuthUrl, new LoginRequestEntity(username,password), LoginResponseEntity.class);
        var result = loginResponse.getBody();
        //result = null;
        config.setToken(result.getJwtToken());
    }

    @GetMapping("/test")
    public String test() throws JSONException {

        String result = "";
        try{
            AuthenticateToAPI("http://localhost:9090"+"/signin","eliskas", "asdf");
            result = restTemplateTokened.getForObject("/users/getall", String.class);
        } catch (Exception e) {
            result = errHandler(e);
        }

        StringBuilder output = new StringBuilder();

        output.append("<html><head><body>");
        output.append("<h1>Test controller</h1>");
        output.append("<div style='color:green;margin-bottom:10px;'>");
        output.append(config.getToken());
        output.append("</div>");
        output.append("<pre style='color:navy;margin-bottom:10px;'>");
        output.append(result);
        output.append("</pre>");
        output.append("<body></head></html>");
        return output.toString();
    }

    private String errHandler(Exception e) {
        if(e instanceof HttpClientErrorException) {
            var eHttp = (HttpClientErrorException) e;
            //return ((HttpClientErrorException) e).getResponseBodyAsString();
            return String.format("[%s(%s)] %s", eHttp.getClass(), eHttp.getStatusCode(), eHttp.getResponseBodyAsString());
        } else if(e instanceof ResourceAccessException){
            return String.format("[%s] %s",e.getClass(),e.getMessage());
        } else {
            return String.format("[%s] %s",e.getClass(),e.getMessage());
        }
    }


}

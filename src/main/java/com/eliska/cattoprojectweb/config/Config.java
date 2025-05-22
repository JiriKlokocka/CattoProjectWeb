package com.eliska.cattoprojectweb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;


@Configuration
public class Config {

    @Getter
    @Setter
    private String token;



    @Bean(name = "myRestTemplate")
    public RestTemplate someRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("http://localhost:9090/api")
                .additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    request.getHeaders().add("Authorization" , "Bearer " + token);
                    return execution.execute(request, body);
                }).build();
    }
}

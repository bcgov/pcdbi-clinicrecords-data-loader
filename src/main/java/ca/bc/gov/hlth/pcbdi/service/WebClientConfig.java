package ca.bc.gov.hlth.pcbdi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Value("${chefs.url}")
    private String chefsUrl;

    @Bean
    public WebClient chefsWebClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(chefsUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        return webClient;
    }
}

package com.wejuai.console.config;

import com.wejuai.console.support.RestTemplateClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author ZM.Wang
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplateClient().getRestTemplate();
    }
}

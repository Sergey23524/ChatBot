package ru.perepechin.vkbot.configuration;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    public CloseableHttpClient getClient(@Value("${connect.timeout}") Integer connectTimeout) {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                        .setConnectTimeout(connectTimeout)
                        .build()
                )
                .build();
    }
}


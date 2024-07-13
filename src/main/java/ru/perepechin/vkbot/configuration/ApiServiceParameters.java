package ru.perepechin.vkbot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Служебные параметры для запросов
 */
@Component
public class ApiServiceParameters {

    private final String accessToken;
    private final String versionApi;

    public ApiServiceParameters(@Value("${access.token}") String accessToken, @Value("${vk.api.version}") String versionApi) {
        this.accessToken = accessToken;
        this.versionApi = versionApi;
    }

    public Map<String, String> getServiceParameters() {
        return Map.of(
                "access_token", accessToken, "v", versionApi
        );
    }
}

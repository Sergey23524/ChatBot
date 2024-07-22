package ru.perepechin.vkbot.configuration;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Служебные параметры для запросов
 *  * <p> Сообщение для подтверждения авторизации {@link #CONFIRMATION_MESSAGE} </p>
 *  * <p> Сообщение при успешном получении event {@link #OK_MESSAGE} </p>
 *  * <p> Адрес Vk api {@link #SERVER_API} </p>
 *  * <p> Идентификатор группы {@link #CONFIRMATION_GROUP_ID} </p>
 */
@Component
public class ApiServiceParameters {

    private final JdbcTemplate jdbcTemplate;

    public static final String ACCESS_TOKEN = "access.token";
    public static final String VERSION_API = "vk.api.version";
    public static final String SERVER_API = "vk.api.server";
    public static final String CONFIRMATION_GROUP_ID = "confirmation.group.id";
    public static final String CONFIRMATION_MESSAGE = "confirmation.message";
    public static final String OK_MESSAGE = "ok.message";

    private static final String PROPERTY_NOT_EXIST = "Не задана настройка %s";

    private Map<String, Object> paramsMap = new HashMap<>();

    public ApiServiceParameters(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    void init() {
        paramsMap = jdbcTemplate.query("select name, val from properties", (rs, i) ->
                Pair.of(rs.getString("name"), rs.getObject("val"))).stream()
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public Map<String, String> getServiceParameters() {
        return Map.of(
                "access_token", getPropertyValue(ACCESS_TOKEN), "v", getPropertyValue(VERSION_API)
        );
    }

    public String getPropertyValue(String name) {
        Object value = paramsMap.get(name);
        if (value == null) {
            throw new RuntimeException(String.format(PROPERTY_NOT_EXIST, name));
        }
        return String.valueOf(value);
    }

    public Long getLongPropertyValue(String name) {
        return Long.valueOf(getPropertyValue(name));
    }
}

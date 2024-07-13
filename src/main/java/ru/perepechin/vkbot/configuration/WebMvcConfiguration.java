package ru.perepechin.vkbot.configuration;

import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.perepechin.vkbot.api.event.Event;
import ru.perepechin.vkbot.configuration.deserializer.EventDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Map<Class<?>, JsonDeserializer<?>> deserializers = new HashMap<>();
        //Добавляем кастомный десериализатор для события
        deserializers.put(Event.class, new EventDeserializer());

        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.deserializersByType(deserializers);

        converters.add(0, new MappingJackson2HttpMessageConverter(builder.build()));
    }
}

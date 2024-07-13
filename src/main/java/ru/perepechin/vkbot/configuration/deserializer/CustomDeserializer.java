package ru.perepechin.vkbot.configuration.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CustomDeserializer<T> extends JsonDeserializer<T> {

    protected <V> V getValue(JsonParser parser, DeserializationContext deserializationContext, Class<V> tClass) throws IOException {
        parser.nextToken();
        return deserializationContext.readValue(parser, tClass);
    }

    protected void deserialize(JsonNode node, DeserializationContext deserializationContext, T tClass, Set<String> excludeFields) {
        var fields = Arrays.stream(tClass.getClass().getDeclaredFields())
                .filter(df -> !excludeFields.contains(df.getName()))
                .collect(Collectors.toMap(Field::getName, field -> field));
        Iterator<String> nFields = node.fieldNames();
        while (nFields.hasNext()) {
            String nField = nFields.next();
            Field f = fields.get(nField);
            if (f != null) {
                f.setAccessible(true);
                try {
                    f.set(tClass, getValue(node.get(nField).traverse(), deserializationContext, f.getType()));
                } catch (IllegalAccessException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

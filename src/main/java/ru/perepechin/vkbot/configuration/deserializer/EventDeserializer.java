package ru.perepechin.vkbot.configuration.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import ru.perepechin.vkbot.api.event.Event;
import ru.perepechin.vkbot.api.event.MessageNew;

import java.io.IOException;
import java.util.Set;

public class EventDeserializer extends CustomDeserializer<Event<?>> {

    @Override
    public Event<?> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        Event<Object> event = new Event<>();

        JsonNode node = jp.getCodec().readTree(jp);
        //десериализуем все кроме object 'по умолчанию'
        deserialize(node, deserializationContext, event, Set.of("object"));

        switch (event.getType()) {
            //В зависимости от типа события десериализуем к нужному классу
            case MESSAGE_NEW -> {
                JsonParser settingsParser = node.get("object").traverse(jp.getCodec());
                event.setObject(getValue(settingsParser, deserializationContext, MessageNew.class));
            }
            default -> event.setObject(null);
        }
        return event;
    }
}

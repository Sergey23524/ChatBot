package ru.perepechin.vkbot.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.perepechin.vkbot.api.event.Event;
import ru.perepechin.vkbot.api.event.Message;
import ru.perepechin.vkbot.api.event.MessageNew;
import ru.perepechin.vkbot.client.HttpClient;
import ru.perepechin.vkbot.client.QueryBuilder;
import ru.perepechin.vkbot.client.VkMethod;
import ru.perepechin.vkbot.client.model.SendMessage;
import ru.perepechin.vkbot.configuration.ApiServiceParameters;
import ru.perepechin.vkbot.exception.VkBotException;

/**
 * Сервис для работы с VK api
 */
@Service
public class VkBotService {
    private static final Logger logger = LoggerFactory.getLogger(VkBotService.class);

    private final HttpClient client;
    private final ApiServiceParameters apiServiceParameters;

    private static final String MSG = "Новое событие: %s";
    private static final String ANSWER_PREFIX = "Вы сказали: ";

    public VkBotService(HttpClient client, ApiServiceParameters apiServiceParameters) {
        this.client = client;
        this.apiServiceParameters = apiServiceParameters;
    }

    /**
     * Обработка событий, отправляемых Callback API
     * @param event событие
     * @return сообщение об успешном получении и обработки события
     */
    @SuppressWarnings("unchecked")
    public String processEvent(Event<?> event) throws VkBotException {
        if (!event.getGroup_id().equals(apiServiceParameters.getLongPropertyValue(ApiServiceParameters.CONFIRMATION_GROUP_ID))) {
            throw VkBotException.wrongGroupId(event.getGroup_id());
        }
        logger.info(String.format(MSG, event.getType().name()));
        switch (event.getType()) {
            case CONFIRMATION -> {
                return confirm();
            }
            case MESSAGE_NEW -> {
                return newMessage((Event<MessageNew>) event);
            }
            default -> throw VkBotException.unsupportedEvent();
        }
    }

    private String confirm() {
        return apiServiceParameters.getPropertyValue(ApiServiceParameters.CONFIRMATION_MESSAGE);
    }

    /**
     * В ответ на событие 'Новое сообщение', отправляем пользователю обратно {@link #ANSWER_PREFIX} + его текст
     * @param event событие 'Новое сообщение'
     */
    private String newMessage(Event<MessageNew> event) throws VkBotException {
        Message msg = event.getObject().getMessage();
        sendMessage(msg.getFrom_id(), ANSWER_PREFIX + msg.getText());
        return apiServiceParameters.getPropertyValue(ApiServiceParameters.OK_MESSAGE);
    }

    private void sendMessage(Integer from_id, String message) throws VkBotException {
        QueryBuilder<SendMessage> query = new QueryBuilder<>();
        client.post(query
                .url(apiServiceParameters.getPropertyValue(ApiServiceParameters.SERVER_API))
                .method(VkMethod.MESSAGES_SEND)
                .serviceParameters(apiServiceParameters.getServiceParameters())
                .body(new SendMessage(from_id, message, () -> (int) (System.currentTimeMillis() & 0xFFFFFFF))).buildPost()
        );
    }

}

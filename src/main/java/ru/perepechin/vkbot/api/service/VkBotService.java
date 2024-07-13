package ru.perepechin.vkbot.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * <p> Сообщение для подтверждения авторизации {@link #confirmationMessage} </p>
 * <p> Сообщение при успешном получении event {@link #doneMessage} </p>
 * <p> Адрес Vk api {@link #vkServer} </p>
 * <p> Идентификатор группы {@link #groupId} </p>
 */
@Service
public class VkBotService {
    private static final Logger logger = LoggerFactory.getLogger(VkBotService.class);

    private final String confirmationMessage;
    private final String doneMessage;
    private final String vkServer;
    private final Long groupId;

    private final HttpClient client;
    private final ApiServiceParameters apiServiceParameters;

    private static final String MSG = "Новое событие: %s";
    private static final String ANSWER_PREFIX = "Вы сказали: ";

    public VkBotService(@Value("${confirmation.message}") String confirmationMessage, @Value("${ok.message}") String doneMessage,
                        @Value("${vk.api.server}") String vkServer, @Value("${confirmation.group.id}") Long groupId, HttpClient client, ApiServiceParameters apiServiceParameters) {
        this.confirmationMessage = confirmationMessage;
        this.doneMessage = doneMessage;
        this.vkServer = vkServer;
        this.groupId = groupId;
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
        if (!event.getGroup_id().equals(groupId)) {
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
        return confirmationMessage;
    }

    /**
     * В ответ на событие 'Новое сообщение', отправляем пользователю обратно {@link #ANSWER_PREFIX} + его текст
     * @param event событие 'Новое сообщение'
     */
    private String newMessage(Event<MessageNew> event) throws VkBotException {
        Message msg = event.getObject().getMessage();
        sendMessage(msg.getFrom_id(), ANSWER_PREFIX + msg.getText());
        return doneMessage;
    }

    private void sendMessage(Integer from_id, String message) throws VkBotException {
        QueryBuilder<SendMessage> query = new QueryBuilder<>();
        client.post(query
                .url(vkServer)
                .method(VkMethod.MESSAGES_SEND)
                .serviceParameters(apiServiceParameters.getServiceParameters())
                .body(new SendMessage(from_id, message)).buildPost()
        );
    }

}

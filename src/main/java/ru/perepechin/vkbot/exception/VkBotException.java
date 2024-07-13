package ru.perepechin.vkbot.exception;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class VkBotException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(VkBotException.class);

    public VkBotException(String description) {
        super(description);
    }

    private static final String unsupportedEvent = "Неподдерживаемый тип события";
    private static final String wrongGroupId = "Некорректный id группы: %d";
    private static final String requestError = "Произошла ошибка при отправке запроса: %s";

    public static VkBotException unsupportedEvent() {
        logger.error(unsupportedEvent);
        return new VkBotException(unsupportedEvent);
    }

    public static VkBotException wrongGroupId(Long id) {
        String msg = String.format(wrongGroupId, id);
        logger.error(msg);
        return new VkBotException(msg);
    }

    public static VkBotException requestError(String msg) {
        msg = String.format(requestError, msg);
        logger.error(msg);
        return new VkBotException(msg);
    }

    public static VkBotException requestError(Throwable th) {
        String msg = String.format(requestError, th.getMessage());
        logger.error(msg, th);
        return new VkBotException(msg);
    }
}

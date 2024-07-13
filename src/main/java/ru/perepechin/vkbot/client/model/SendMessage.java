package ru.perepechin.vkbot.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Новое сообщание для пользователя
 */
@Data
@AllArgsConstructor
public class SendMessage implements VkRequest {
    private Integer userId;
    private Integer randomId;
    private String message;

    public SendMessage(Integer userId, String message) {
        this.userId = userId;
        this.randomId = 0;
        this.message = message;
    }

    @Override
    public Map<String, String> getFormData() {
        return Map.of(
                "user_id", String.valueOf(userId),
                "random_id", String.valueOf(randomId),
                "message", message
        );
    }
}

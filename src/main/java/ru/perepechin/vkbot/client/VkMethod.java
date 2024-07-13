package ru.perepechin.vkbot.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VkMethod {
    MESSAGES_SEND("messages.send")
    ;

    private final String method;
}

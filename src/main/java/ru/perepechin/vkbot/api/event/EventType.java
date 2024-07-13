package ru.perepechin.vkbot.api.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {
    @JsonProperty("confirmation")
    CONFIRMATION,
    @JsonProperty("message_new")
    MESSAGE_NEW
    ;
}

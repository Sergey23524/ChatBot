package ru.perepechin.vkbot.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность 'Событие'
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event<T> {
    private EventType type;
    private Long group_id;
    private T object;
}

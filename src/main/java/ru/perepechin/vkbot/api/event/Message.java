package ru.perepechin.vkbot.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {
    private Integer id;
    private Integer from_id;
    private String text;
}

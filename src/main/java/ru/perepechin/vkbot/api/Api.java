package ru.perepechin.vkbot.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.perepechin.vkbot.api.event.Event;
import ru.perepechin.vkbot.api.service.VkBotService;
import ru.perepechin.vkbot.exception.VkBotException;

@RestController
public class Api {
    private VkBotService vkBotService;

    public Api(VkBotService vkBotService) {
        this.vkBotService = vkBotService;
    }

    /**
     * Точка обработки событий Callback API
     */
    @PostMapping(value = "api", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<?> event(@RequestBody Event<?> event) throws VkBotException {
        return ResponseEntity.ok(vkBotService.processEvent(event));
    }
}

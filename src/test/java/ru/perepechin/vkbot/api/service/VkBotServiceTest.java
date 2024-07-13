package ru.perepechin.vkbot.api.service;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.perepechin.vkbot.api.event.Event;
import ru.perepechin.vkbot.api.event.EventType;
import ru.perepechin.vkbot.api.event.Message;
import ru.perepechin.vkbot.api.event.MessageNew;
import ru.perepechin.vkbot.client.HttpClient;
import ru.perepechin.vkbot.configuration.ApiServiceParameters;
import ru.perepechin.vkbot.exception.VkBotException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("all")
@TestPropertySource(locations = {
        "classpath:application.properties",
})
class VkBotServiceTest {

    @Autowired
    private ApiServiceParameters apiServiceParameters;

    @Autowired
    private VkBotService service;

    @MockBean
    private HttpClient client;

    @Value("${confirmation.message}")
    private String confirmationMessage;
    @Value("${confirmation.group.id}")
    private Long groupId;
    @Value("${ok.message}")
    private String ok;
    @Value("${vk.api.server}")
    private String vkServer;

    @Before
    public void setup() throws VkBotException {
        when(client.post(any())).thenReturn(HttpStatus.OK);
    }

    @Test
    void processСonfirmEvent_ok() throws VkBotException {
        var event = new Event(EventType.CONFIRMATION, groupId, null);
        assertThat(service.processEvent(event)).isEqualTo(confirmationMessage);
    }

    @Test
    void processСonfirmEvent_error() throws VkBotException {
        var event = new Event(EventType.CONFIRMATION, 12L, null);
        assertThatExceptionOfType(VkBotException.class)
                .isThrownBy(() -> service.processEvent(event))
                .withMessage("Некорректный id группы: 12");
    }

    @Test
    void processNewMessageEvent_ok() throws VkBotException {
        var newMsg = new MessageNew(new Message(12, 12, "test"));
        var event = new Event(EventType.MESSAGE_NEW, groupId, newMsg);
        assertThat(service.processEvent(event)).isEqualTo(ok);
    }
}
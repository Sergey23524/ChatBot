package ru.perepechin.vkbot.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.perepechin.vkbot.exception.VkBotException;

import java.io.IOException;

/**
 * Реализация запросов
 */
@Component
public class HttpClient {

    public HttpStatus post(HttpPost post) throws VkBotException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                String responseString = EntityUtils.toString(response.getEntity());
                if (HttpStatus.OK.value() != response.getStatusLine().getStatusCode()) {
                    throw VkBotException.requestError(responseString);
                }
                return HttpStatus.resolve(response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            throw VkBotException.requestError(e);
        }
    }
}

package ru.perepechin.vkbot.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.perepechin.vkbot.exception.VkBotException;

import java.io.IOException;

/**
 * Реализация запросов
 */
@Component
public class HttpClient {
    private final Integer MAX_RETRIES;
    private final Long RETRY_DELAY_MS;

    private final CloseableHttpClient httpClient;

    public HttpClient(@Value("${max.retries}") Integer maxRetries, @Value("${retry.delay.ms}") Long retryDelayMs, CloseableHttpClient httpClient) {
        MAX_RETRIES = maxRetries;
        RETRY_DELAY_MS = retryDelayMs;
        this.httpClient = httpClient;
    }

    public HttpStatus post(HttpPost post) throws VkBotException {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                String responseString = EntityUtils.toString(response.getEntity());
                if (HttpStatus.OK.value() != response.getStatusLine().getStatusCode()) {
                    throw VkBotException.requestError(responseString);
                }
                return HttpStatus.resolve(response.getStatusLine().getStatusCode());
            } catch (IOException e) {
                if (++retries >= MAX_RETRIES) {
                    throw VkBotException.requestError(e);
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS); //задержка
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new VkBotException("Не удалось выполнить запрос после нескольких попыток");
    }
}

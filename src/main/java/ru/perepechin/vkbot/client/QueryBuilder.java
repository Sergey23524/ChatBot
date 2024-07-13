package ru.perepechin.vkbot.client;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import ru.perepechin.vkbot.client.model.VkRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Билдер запросов к VK api
 * @param <T> Сущность для тела запроса
 */
public class QueryBuilder<T extends VkRequest> {

    private String url;
    private T body;
    private VkMethod method;
    private final Map<String, String> rowData = new HashMap<>();

    private final ContentType textPlainUtf8 = ContentType.create("text/plain", "UTF-8");

    public QueryBuilder<T> url(String url) {
        this.url = url;
        return this;
    }

    public QueryBuilder<T> method(VkMethod method) {
        this.method = method;
        return this;
    }

    public QueryBuilder<T> body(T body) {
        this.body = body;
        return this;
    }

    public QueryBuilder<T> serviceParameters(Map<String, String> serviceParameters) {
        this.rowData.putAll(serviceParameters);
        return this;
    }

    public HttpPost buildPost() {
        HttpPost request = new HttpPost(url + method.getMethod());

        if (body != null) {
            rowData.putAll(body.getFormData());
        }

        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        rowData.forEach((key, value) -> multipartEntity.addTextBody(key, value, textPlainUtf8));
        request.setEntity(multipartEntity.build());

        return request;
    }
}

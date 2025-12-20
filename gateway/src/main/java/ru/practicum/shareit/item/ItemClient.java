package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.NewCommentRequestDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(NewItemRequestDto item, Long userId) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> update(Long itemId, UpdateItemRequestDto item, Long userId) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> getById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getUserItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findItems(String text) {
        Map<String, Object> parameters = Map.of("text", text);

        return get("/search?text={text}", null, parameters);
    }

    public ResponseEntity<Object> addComment(Long itemId, Long userId, NewCommentRequestDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}

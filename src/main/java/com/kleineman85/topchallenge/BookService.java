package com.kleineman85.topchallenge;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";
    private final RestTemplate restTemplate;

    public List<Book> getBookList(String query, String langRestrict) {
        validateParameters(query, langRestrict);
        String url = getUrl(query, langRestrict);
        List<Book> bookList = new ArrayList<>();

        log.info("Fetching books from Google Books API");
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode rootNode = responseEntity.getBody();
        if (rootNode == null) {
            log.info("Received empty response from Google Books API");
        } else {
            JsonNode itemsNode = rootNode.get("items");
            log.info("Received response with {} items from Google Books API", itemsNode.size());
            bookList = BookMapper.mapItemsNodeToBookList(itemsNode);
        }

        return bookList;
    }

    private void validateParameters(String query, String langRestrict) {
        if(query == null || query.isBlank()){
            throw new IllegalArgumentException("Invalid query parameter q, should not be null or empty");
        }
        if (langRestrict != null && !langRestrict.isBlank() && !langRestrict.matches("^\\w{2}$")) {
            throw new IllegalArgumentException("Invalid query parameter langRestrict, should match ISO-639-1");
        }
    }

    private String getUrl(String query, String langRestrict) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", query)
                .queryParam("langRestrict", langRestrict)
                .queryParam("maxResults", 10)
                .queryParam("orderBy", "newest")
                .encode()
                .toUriString();
    }

}

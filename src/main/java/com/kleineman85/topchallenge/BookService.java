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
        List<Book> bookList = new ArrayList<>();
        String url = getUrl(query, langRestrict);

        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(url, JsonNode.class);

        // TODO refactor, check for NPE
        JsonNode rootNode = responseEntity.getBody();
        JsonNode itemsNode = rootNode.get("items");

        itemsNode.forEach( item -> {
            JsonNode volumeInfoNode = item.get("volumeInfo");
            Book book = VolumeInfoToBookMapper.mapVolumeInfoNodeToBook(volumeInfoNode);
            bookList.add(book);
        });

        return bookList;

    }

    private void validateParameters(String query, String langRestrict) {
        if (langRestrict != null && !langRestrict.isBlank() && !langRestrict.matches("^\\w{2}$")) {
            throw new IllegalArgumentException("Invalid langRestrict, should match ISO-639-1");
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

package com.kleineman85.topchallenge;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Utility class, final because it should not be subclassed.
 * Constructor is private because it should not be instantiated.
 */
@Slf4j
public final class BookMapper {

    private BookMapper() {
    }

    public static List<Book> mapItemsNodeToBookList(JsonNode itemsNode) {
        List<Book> bookList = new ArrayList<>();
        log.info("Mapping response to an array of books");
        itemsNode.forEach(item -> {
            JsonNode volumeInfoNode = item.get("volumeInfo");
            if (volumeInfoNode == null) {
                log.info("Received empty volumeInfo");
                return;
            }
            Book book = mapVolumeInfoNodeToBook(volumeInfoNode);
            bookList.add(book);
        });

        return bookList;
    }

    public static Book mapVolumeInfoNodeToBook(JsonNode volumeInfoNode) {
        String titel = getTitel(volumeInfoNode);
        log.debug("Mapping VolumeInfo with title {} to Book", titel);
        return Book.builder()
                .titel(titel)
                .auteurs(getAuteurs(volumeInfoNode))
                .isbn(getIsbn(volumeInfoNode))
                .publicatieDatum(getPublicatieDatum(volumeInfoNode))
                .build();
    }

    private static String getTitel(JsonNode volumeInfoNode) {
        return String.valueOf(volumeInfoNode.get("title")).replace("\"", "");
    }

    private static List<String> getAuteurs(JsonNode volumeInfoNode) {
        JsonNode authorsNode = volumeInfoNode.get("authors");
        if(authorsNode == null) {
            return Collections.emptyList();
        }
        List<String> auteurList = new ArrayList<>();

        authorsNode.forEach(author -> auteurList.add(author.asText().replace("\"", "")));

        return auteurList;
    }

    private static List<String> getIsbn(JsonNode volumeInfoNode) {
        JsonNode industryIdentifiersNode = volumeInfoNode.get("industryIdentifiers");
        if (industryIdentifiersNode == null) {
            return Collections.emptyList();
        }
        List<String> isbnList = new ArrayList<>();

        industryIdentifiersNode.forEach(
                industryIdentifier -> {
                    String type = industryIdentifier.get("type").asText().replace("\"", "");
                    String identifier = industryIdentifier.get("identifier").asText().replace("\"", "");
                    isbnList.add(type + " : " + identifier);
                });

        return isbnList;
    }

    private static String getPublicatieDatum(JsonNode volumeInfoNode) {
        String publishedDateString = String.valueOf(volumeInfoNode.get("publishedDate")).replace("\"", "");
        if(publishedDateString == null) {
            return null;
        }
        String publicatieDatumString;

        try {
            DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate publishedDate = LocalDate.parse(publishedDateString, formatterInput);
            DateTimeFormatter formatterNL = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("nl", "NL"));

            publicatieDatumString = publishedDate.format(formatterNL);
        } catch (DateTimeParseException e) {
            publicatieDatumString = publishedDateString;
        }

        return publicatieDatumString;
    }

}

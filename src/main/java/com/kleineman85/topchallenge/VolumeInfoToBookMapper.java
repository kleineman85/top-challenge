package com.kleineman85.topchallenge;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Utility class, final because it should not be subclassed.
 * Constructor is private because it should not be instantiated.
 */
@Slf4j
public final class VolumeInfoToBookMapper {

    private VolumeInfoToBookMapper() {
    }

    public static Book mapVolumeInfoNodeToBook(JsonNode volumeInfoNode) {
        return Book.builder()
                .titel(getTitel(volumeInfoNode))
                .auteurs(getAuteurs(volumeInfoNode))
                .isbn(getIsbn(volumeInfoNode))
                .publicatieDatum(getPublicatieDatum(volumeInfoNode))
                .build();
    }

    private static String getTitel(JsonNode volumeInfoNode) {
        return volumeInfoNode.get("title").asText().replace("\"", "");
    }

    private static List<String> getAuteurs(JsonNode volumeInfoNode) {
        JsonNode authorsNode = volumeInfoNode.get("authors");
        List<String> auteurList = new ArrayList<>();

        authorsNode.forEach(author -> auteurList.add(author.asText().replace("\"", "")));

        return auteurList;
    }

    private static List<String> getIsbn(JsonNode volumeInfoNode) {
        JsonNode industryIdentifiersNode = volumeInfoNode.get("industryIdentifiers");
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

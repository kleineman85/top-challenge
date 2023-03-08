package com.kleineman85.topchallenge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private RestTemplate mockedRestTemplate;
    private BookService testObject;
    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void initialize() {
        testObject = new BookService(mockedRestTemplate);
    }

    @Test
    void givenValidRequestShouldReturnBookList() throws Exception {
        // given
        URL url = getClass().getClassLoader().getResource("testResponse.json");
        Path path = Paths.get(url.toURI());
        String jsonResponse = Files.readString(path);
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        ResponseEntity<Object> mockedResponseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        when(mockedRestTemplate.getForEntity(anyString(), any())).thenReturn(mockedResponseEntity);

        // when
        List<Book> result = testObject.getBookList("the alchemist", "en");

        // then
        assertFalse(result.isEmpty());
    }

    @Test
    void givenInvalidQueryParameterQShouldThrowIllegalArgumentException(){
        // given

        // when
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> testObject.getBookList(" ", "en"));

        // then
        assertEquals("Invalid query parameter q, should not be null or empty", result.getMessage());
    }

    @Test
    void givenInvalidQueryParameterLangRestrictShouldThrowIllegalArgumentException(){
        // given

        // when
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> testObject.getBookList("the alchemist", "english"));

        // then
        assertEquals("Invalid query parameter langRestrict, should match ISO-639-1", result.getMessage());
    }

}

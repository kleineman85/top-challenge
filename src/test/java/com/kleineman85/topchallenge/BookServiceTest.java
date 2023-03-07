package com.kleineman85.topchallenge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class BookServiceTest {

    private BookService testObject;

    @BeforeEach
    void initialize(){
        testObject = new BookService(new RestTemplate());
    }

    @Test
    void myTest(){
        // given

        // when
        testObject.getBookList("the alchemist", "nl");

        // then

    }

}

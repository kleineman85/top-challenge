package com.kleineman85.topchallenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/books", produces = "application/json")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Object> getBooks(@RequestParam(name = "q", required = false) String query,
                                           @RequestParam(name = "langRestrict", required = false) String langRestrict) {
        log.info("Received request with query params q={} and langRestrict={}", query, langRestrict);

        try {
            List<Book> bookList = bookService.getBookList(query, langRestrict);
            return new ResponseEntity<>(bookList, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            log.info("Bad request: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>("An error occured, for questions contact support@company.com", HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            log.info("Finished request and sending response");

        }

    }

}

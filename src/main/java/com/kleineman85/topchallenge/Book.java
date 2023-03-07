package com.kleineman85.topchallenge;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Book {
    private String titel;
    private List<String> auteurs;
    private List<String> isbn;
    private String publicatieDatum;

}

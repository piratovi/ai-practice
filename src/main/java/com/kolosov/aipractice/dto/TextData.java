package com.kolosov.aipractice.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("textData")
public record TextData(
        @Id String id,
        String text,
        String type
) {

    public TextData(String text, String type) {
        this(null, text, type);
    }
}

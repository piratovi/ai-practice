package com.kolosov.aipractice.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("flattery")
public record Flattery(
        @Id String id,
        String text
) {

    public Flattery(String text) {
        this(null, text);
    }
}

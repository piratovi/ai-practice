package com.kolosov.aipractice.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Flattery {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    public Flattery(String text) {
        this.text = text;
    }

}
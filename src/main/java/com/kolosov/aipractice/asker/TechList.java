package com.kolosov.aipractice.asker;

import java.util.List;

record TechList(List<Tech> tech) {

    record Tech(
            String name,
            String dateOfInvention,
            String description,
            String difficulty
    ) {}
}

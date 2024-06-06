package com.kolosov.aipractice.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TextDataRepository {

    private final MongoTemplate mongoTemplate;

    public TextDataRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public <T> T save(T data) {
        return mongoTemplate.save(data);
    }

    public <T> List<T> getRecentData(Class<T> entityClass, int limit) {
        var query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "_id"))
                .limit(limit);

        return mongoTemplate.find(query, entityClass);
    }
}
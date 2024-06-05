package com.kolosov.aipractice.repository;

import com.kolosov.aipractice.dto.TextData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class TextDataRepository {

    private final MongoTemplate mongoTemplate;

    public TextDataRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public TextData saveTextData(String text, String type) {
        var textData = new TextData(text, type);
        return mongoTemplate.save(textData);
    }

    public List<String> getTextDataByType(String type) {
        var query = new Query().addCriteria(Criteria.where("type").is(type));
        List<TextData> result = mongoTemplate.find(query, TextData.class);
        return result.stream().map(TextData::text).toList();
    }
}

package com.kolosov.aipractice.rag;

import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

@Service
public class RagSearchRequestService {
    public RagSearchRequestService() {
    }

    public SearchRequest prepareSearchRequest(String question, String fileName) {
        var filterExpressionBuilder = new FilterExpressionBuilder();
        Filter.Expression expression = filterExpressionBuilder.eq("file_name", fileName).build();
        return SearchRequest.query(question)
                .withFilterExpression(expression);
    }
}
package com.kolosov.aipractice.rag;

import com.kolosov.aipractice.asker.ChatGPTCostCalculator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter.Expression;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.command.annotation.Command;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Command
public class RagAsker {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;
    private final ChatGPTCostCalculator calculator;

    public RagAsker(
            @Qualifier("openAiChatModel") ChatModel chatModel,
            VectorStore vectorStore,
            ChatGPTCostCalculator calculator
    ) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.calculator = calculator;
    }

    @Command(command = "rag")
    public String run(String question, String fileName) {
        SearchRequest searchRequest = prepareSearchRequest(question, fileName);
        List<Document> similaritySearchResult = vectorStore.similaritySearch(searchRequest);

        String documents = similaritySearchResult.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        var systemPromptTemplate = new SystemPromptTemplate("""
                You are a helpful assistant, conversing with a user about the subjects contained in a set of documents.
                Use the information from the DOCUMENTS section to provide accurate answers. If unsure or if the answer
                isn't found in the DOCUMENTS section, simply state that you don't know the answer.

                DOCUMENTS:
                {documents}
                """);
        var systemMessage = systemPromptTemplate.createMessage(Map.of("documents", documents));

        var promptTemplate = new PromptTemplate("""
                QUESTION:
                {question}
                """);
        var userMessage = promptTemplate.createMessage(Map.of("question", question));

        var messages = List.of(systemMessage, userMessage);
        var prompt = new Prompt(messages);
        var chatResponse = chatModel.call(prompt);

        BigDecimal cost = calculator.calculateCost(chatResponse);
        System.out.println("Cost: " + cost + " cents.");

        return chatResponse.getResult().getOutput().getContent();
    }

    private SearchRequest prepareSearchRequest(String question, String fileName) {
        var filterExpressionBuilder = new FilterExpressionBuilder();
        Expression expression = filterExpressionBuilder.eq("file_name", fileName).build();
        return SearchRequest.query(question)
                .withFilterExpression(expression);
    }


}

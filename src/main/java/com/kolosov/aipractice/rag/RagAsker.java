package com.kolosov.aipractice.rag;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.command.annotation.Command;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Command
public class RagAsker {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagAsker(
            @Qualifier("openAiChatClient") ChatClient chatClient,
            VectorStore vectorStore
    ) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Command(command = "rag")
    public void run(String question) {
        List<Document> similaritySearchResult = vectorStore.similaritySearch(question);
        String documents = similaritySearchResult.stream().map(Document::getContent).collect(Collectors.joining("\n"));
        Map<String, Object> map = Map.of(
                "documents", documents,
                "question", question
        );

        var systemPromptTemplate = new SystemPromptTemplate("""
                You are a helpful assistant, conversing with a user about the subjects contained in a set of documents.
                Use the information from the DOCUMENTS section to provide accurate answers. If unsure or if the answer
                isn't found in the DOCUMENTS section, simply state that you don't know the answer.

                DOCUMENTS:
                {documents}
                """);
        var systemMessage = systemPromptTemplate.createMessage(map);

        var promptTemplate = new PromptTemplate("""
                QUESTION:
                {question}
                """);
        var userMessage = promptTemplate.createMessage(map);

        var messages = List.of(systemMessage, userMessage);
        var prompt = new Prompt(messages);
        var chatResponse = chatClient.call(prompt);
        String content = chatResponse.getResult().getOutput().getContent();
        System.out.println(content);
    }


}

package com.kolosov.aipractice.asker;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleAsker {

    private final ChatModel chatModel;

    public SimpleAsker(
            @Qualifier("ollamaChatModel") ChatModel chatModel
    ) {
        this.chatModel = chatModel;
    }

    public void run() {
        var systemPromptTemplate = new SystemPromptTemplate("""
                You are a wise old Greek from Myths.
                """);
        var systemMessage = systemPromptTemplate.createMessage();

        var promptTemplate = new PromptTemplate("""
                What is the best for me?

                Context: I like eat ice cream!
                """);
        var userMessage = promptTemplate.createMessage();

        var messages = List.of(systemMessage, userMessage);
        var chatResponse = chatModel.call(new Prompt(messages));
        String content = chatResponse.getResult().getOutput().getContent();
        System.out.println(content);
    }


}

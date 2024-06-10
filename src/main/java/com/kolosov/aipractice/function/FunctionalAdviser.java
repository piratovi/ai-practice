package com.kolosov.aipractice.function;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.command.annotation.Command;

import java.util.List;

@Command
public class FunctionalAdviser {

    private final ChatClient chatClient;
    private final OpenMeteoFunction openMeteoFunction;

    public FunctionalAdviser(
            @Qualifier("openAiChatClient")
            ChatClient chatClient, OpenMeteoFunction openMeteoFunction
    ) {
        this.chatClient = chatClient;
        this.openMeteoFunction = openMeteoFunction;
    }

    @Command(command = "advice")
    public void advice() {
        var systemPromptTemplate = new SystemPromptTemplate("""
                You are the best advisor about leisure time activities.
                """);
        var systemMessage = systemPromptTemplate.createMessage();

        var promptTemplate = new PromptTemplate("""
                Based on weather forecast, plan one interesting activity in Phuket per each day.
                """);
        var userMessage = promptTemplate.createMessage();

        var messages = List.of(systemMessage, userMessage);


        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .withFunction("OpenMeteoFunction")
                .build();

        var prompt = new Prompt(messages, chatOptions);
        var chatResponse = chatClient.call(prompt);
        String content = chatResponse.getResult().getOutput().getContent();
        System.out.println(content);
    }
}

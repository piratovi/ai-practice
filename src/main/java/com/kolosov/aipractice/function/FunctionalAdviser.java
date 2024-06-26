package com.kolosov.aipractice.function;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.command.annotation.Command;

import java.util.List;

@Command
public class FunctionalAdviser {

    private final ChatModel chatModel;

    public FunctionalAdviser(
            @Qualifier("openAiChatModel") ChatModel chatModel
    ) {
        this.chatModel = chatModel;
    }

    @Command(command = "advice")
    public String advice() {
        var systemPromptTemplate = new SystemPromptTemplate("""
                You are the best advisor about leisure time activities.
                """);
        var systemMessage = systemPromptTemplate.createMessage();

        var promptTemplate = new PromptTemplate("""
                Based on weather forecast, plan one interesting activity in Phuket per each day.
                """);
        var userMessage = promptTemplate.createMessage();

        var messages = List.of(systemMessage, userMessage);

        OpenAiChatOptions aiChatOptions = OpenAiChatOptions.builder()
                .withFunction("weekForecast")
                .build();

        var prompt = new Prompt(messages, aiChatOptions);
        var chatResponse = chatModel.call(prompt);
        return chatResponse.getResult().getOutput().getContent();
    }
}

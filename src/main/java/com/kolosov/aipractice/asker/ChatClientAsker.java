package com.kolosov.aipractice.asker;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.command.annotation.Command;

import java.util.List;

@Command
public class ChatClientAsker {

    private final ChatModel chatModel;

    public ChatClientAsker(
            @Qualifier("openAiChatModel") ChatModel chatModel
    ) {
        this.chatModel = chatModel;
    }

    @Command(command = "chatClient")
    public List<Duty> chatClient(String message) {

        ChatClient chatClient = ChatClient.create(chatModel);

        ChatOptions chatOptions = ChatOptionsBuilder.builder()
                .withTemperature(1F)
                .build();


        return chatClient.prompt()
                .options(chatOptions)
                .user(message)
                .system("You are a ship captain. Give a response from your scope")
                .call()
                .entity(new ParameterizedTypeReference<List<Duty>>() {
                });
    }

    public record Duty(String name, String description) {
    }

}

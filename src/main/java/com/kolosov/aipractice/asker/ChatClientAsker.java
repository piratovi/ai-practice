package com.kolosov.aipractice.asker;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;

@Command
public class ChatClientAsker {

    private final ChatModel chatModel;
    private final ChatGPTCostCalculator calculator;

    public ChatClientAsker(
            @Qualifier("openAiChatModel") ChatModel chatModel,
            ChatGPTCostCalculator calculator
    ) {
        this.chatModel = chatModel;
        this.calculator = calculator;
    }

    @Command(command = "chatClient")
    public String chatClient(String message, String pathToJpeg) {
        ChatClient chatClient = ChatClient.create(chatModel);

        ChatOptions chatOptions = ChatOptionsBuilder.builder()
                .withTemperature(1F)
                .build();

        Resource imageResource = new FileSystemResource(pathToJpeg);

        ChatResponse chatResponse = chatClient.prompt()
                .options(chatOptions)
                .user(promptUserSpec -> promptUserSpec.text(message).media(MimeTypeUtils.IMAGE_JPEG, imageResource))
                .system("You are a ship captain. Give a response from your scope")
                .call()
                .chatResponse();

        BigDecimal cost = calculator.calculateCost(chatResponse);
        System.out.println("Cost: " + cost + " cents.");

        return chatResponse.getResult().getOutput().getContent();
    }

}

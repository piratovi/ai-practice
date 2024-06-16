package com.kolosov.aipractice.asker;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.command.annotation.Command;
import reactor.core.publisher.Flux;

@Command
public class ChatClientStreamer {

    private final ChatModel chatModel;

    public ChatClientStreamer(
            @Qualifier("openAiChatModel") ChatModel chatModel
    ) {
        this.chatModel = chatModel;
    }

    @Command(command = "chatClientStream")
    public void chatClient(String message) {

        ChatClient chatClient = ChatClient.create(chatModel);

        ChatOptions chatOptions = ChatOptionsBuilder.builder()
                .withTemperature(1F)
                .build();

        Flux<String> content = chatClient.prompt()
                .options(chatOptions)
                .user(message)
                .system("You are a ice cream seller. Give a response from your scope")
                .stream()
                .content();

        content.subscribe(System.out::print);
    }

}

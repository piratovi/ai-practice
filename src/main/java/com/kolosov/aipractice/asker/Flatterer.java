package com.kolosov.aipractice.asker;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;

import java.util.List;

@Command
public class Flatterer {

    @Value("classpath:/prompts/flattery.st")
    private Resource flattery;

    private final ChatClient chatClient;

    public Flatterer(
            @Qualifier("openAiChatClient") ChatClient chatClient
    ) {
        this.chatClient = chatClient;
    }

    @Command(command = "flatter")
    public void flatter() {
        PromptTemplate promptTemplate = new PromptTemplate(flattery);
        var userMessage = promptTemplate.createMessage();

        var systemPromptTemplate = new SystemPromptTemplate("""
                You are very experienced flatterer of women.
                Also in the same time you are talented Thai poet
                Give answer in poetry only in Thai Language!
                """);
        Message systemMessage = systemPromptTemplate.createMessage();

        List<Message> messages = List.of(systemMessage, userMessage);
        ChatResponse chatResponse = chatClient.call(new Prompt(messages));
        System.out.println(chatResponse.getResult().getOutput().getContent());
    }
}

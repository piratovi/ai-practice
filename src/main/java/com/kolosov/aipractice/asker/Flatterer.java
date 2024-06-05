package com.kolosov.aipractice.asker;

import com.kolosov.aipractice.repository.TextDataRepository;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;

import java.util.ArrayList;
import java.util.List;

@Command
public class Flatterer {

    private static final String FLATTERY = "flattery";
    @Value("classpath:/prompts/flattery.st")
    private Resource flattery;

    private final ChatClient chatClient;

    private final TextDataRepository textDataRepository;

    public Flatterer(
            @Qualifier("openAiChatClient") ChatClient chatClient, TextDataRepository textDataRepository
    ) {
        this.chatClient = chatClient;
        this.textDataRepository = textDataRepository;
    }

    @Command(command = "flatter")
    public void flatter() {
        PromptTemplate promptTemplate = new PromptTemplate(flattery);
        var userMessage = promptTemplate.createMessage();

        var systemPromptTemplate = new SystemPromptTemplate("""
                You are very experienced flatterer of women.
                Also in the same time you are talented Thai poet
                Give answer in poetry only in Thai Language!
                Don't repeat yourself. Be creative!
                """);
        Message systemMessage = systemPromptTemplate.createMessage();

        List<AssistantMessage> assistantMessages = textDataRepository.getTextDataByType(FLATTERY).stream()
                .map(AssistantMessage::new)
                .toList();

        List<Message> messages = new ArrayList<>();
        messages.addAll(assistantMessages);
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatResponse chatResponse = chatClient.call(new Prompt(messages));
        String result = chatResponse.getResult().getOutput().getContent();
        textDataRepository.saveTextData(result, FLATTERY);

        System.out.println(result);
    }
}

package com.kolosov.aipractice.asker;

import com.kolosov.aipractice.dto.Flattery;
import com.kolosov.aipractice.repository.FlatteryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Command
public class Flatterer {

    @Value("classpath:/prompts/flattery.st")
    private Resource flattery;

    private final ChatModel chatModel;
    private final FlatteryRepository flatteryRepository;
    private final ChatGPTCostCalculator chatGPTCostCalculator;

    public Flatterer(
            @Qualifier("openAiChatModel") ChatModel chatModel,
            FlatteryRepository flatteryRepository,
            ChatGPTCostCalculator chatGPTCostCalculator
    ) {
        this.chatModel = chatModel;
        this.flatteryRepository = flatteryRepository;
        this.chatGPTCostCalculator = chatGPTCostCalculator;
    }

    @Command(command = "flatter")
    public void flatter(String userInput) {
        PromptTemplate promptTemplate = new PromptTemplate(flattery);
        var userMessage = promptTemplate.createMessage(Map.of("userInput", userInput));

        var systemPromptTemplate = new SystemPromptTemplate("""
                You are very experienced flatterer of women.
                Also in the same time you are talented Thai poet
                Give answer in poetry only in Thai Language!
                Don't repeat yourself. Be creative!
                """);
        Message systemMessage = systemPromptTemplate.createMessage();

        List<AssistantMessage> assistantMessages = flatteryRepository.findLastFiveFlatters().stream()
                .map(Flattery::getText)
                .map(AssistantMessage::new)
                .toList();

        List<Message> messages = new ArrayList<>();
        messages.addAll(assistantMessages);
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatResponse chatResponse = chatModel.call(new Prompt(messages));

        BigDecimal cost = chatGPTCostCalculator.calculateCost(chatResponse);
        System.out.println("Cost: " + cost + " cents.");

        String result = chatResponse.getResult().getOutput().getContent();
        flatteryRepository.save(new Flattery(result));

        System.out.println(result);
    }
}

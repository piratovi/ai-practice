package com.kolosov.aipractice.rag;

import com.kolosov.aipractice.asker.ChatGPTCostCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.shell.command.annotation.Command;

import java.math.BigDecimal;

@Command
@RequiredArgsConstructor
public class RagChatClient {

    private final OpenAiChatModel chatModel;
    private final VectorStore vectorStore;
    private final RagSearchRequestService ragSearchRequestService;
    private final ChatGPTCostCalculator calculator;

    @Command(command = "ragChatClient")
    public String ragChatClient(String question, String fileName) {
        SearchRequest searchRequest = ragSearchRequestService.prepareSearchRequest(question, fileName);

        ChatResponse response = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, searchRequest))
                .user(question)
                .call()
                .chatResponse();

        BigDecimal cost = calculator.calculateCost(response);
        System.out.println("Cost: " + cost + " cents.");

        return response.getResult().getOutput().getContent();
    }
}

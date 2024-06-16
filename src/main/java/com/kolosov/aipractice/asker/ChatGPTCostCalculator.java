package com.kolosov.aipractice.asker;

import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class ChatGPTCostCalculator {

    @Value("${spring.ai.openai.chat.options.token.centcost.input}")
    BigDecimal centPerInputToken;
    @Value("${spring.ai.openai.chat.options.token.centcost.output}")
    BigDecimal centPerOutputToken;

    BigDecimal calculateCost(ChatResponse chatResponse) {
        Usage usage = chatResponse.getMetadata().getUsage();

        BigDecimal inputTokens = BigDecimal.valueOf(usage.getPromptTokens());
        BigDecimal outputTokens = BigDecimal.valueOf(usage.getGenerationTokens());

        BigDecimal inputTokensCost = centPerInputToken.multiply(inputTokens);
        BigDecimal outputTokensCost = centPerOutputToken.multiply(outputTokens);

        return inputTokensCost.add(outputTokensCost);
    }
}
package com.kolosov.aipractice.embedding;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class OllamaEmbeddingClientBridge {

    private final OllamaEmbeddingModel ollamaEmbeddingModel;
    private final OpenAiEmbeddingModel openAiEmbeddingModel;


    @Bean
    @Primary
    public EmbeddingModel embeddingClient() {
        return ollamaEmbeddingModel;
    }
}
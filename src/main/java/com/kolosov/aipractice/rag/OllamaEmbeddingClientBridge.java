package com.kolosov.aipractice.rag;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OllamaEmbeddingClientBridge {

    private final OllamaEmbeddingClient ollamaEmbeddingClient;
    private final OpenAiEmbeddingClient openAiEmbeddingClient;

    public OllamaEmbeddingClientBridge(OllamaEmbeddingClient ollamaEmbeddingClient, OpenAiEmbeddingClient openAiEmbeddingClient) {
        this.ollamaEmbeddingClient = ollamaEmbeddingClient;
        this.openAiEmbeddingClient = openAiEmbeddingClient;
    }

    @Bean
    @Primary
    public EmbeddingClient embeddingClient() {
        return ollamaEmbeddingClient;
    }
}
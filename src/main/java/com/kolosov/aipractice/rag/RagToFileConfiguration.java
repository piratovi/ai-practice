package com.kolosov.aipractice.rag;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@Deprecated
public class RagToFileConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(RagToFileConfiguration.class);

    @Value("classpath:/docs/vumi.txt")
    private Resource vumi;

    private final String vumiVectorStoreName = "vumiVectorStore.json";

    @SneakyThrows
//    @Bean
    VectorStore simpleVectorStore(EmbeddingModel embeddingClient) {
        File vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.length() != 0) {
            return loadVectorStore(embeddingClient, vectorStoreFile);
        } else {
            return createVectorStore(embeddingClient, vectorStoreFile);
        }
    }

    private SimpleVectorStore loadVectorStore(EmbeddingModel embeddingClient, File vectorStoreFile) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);
        LOGGER.warn("Vector store already has data. Loading from it");
        simpleVectorStore.load(vectorStoreFile);
        return simpleVectorStore;
    }

    @SneakyThrows
    private SimpleVectorStore createVectorStore(EmbeddingModel embeddingClient, File vectorStoreFile) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);
        LOGGER.warn("Vector store doesn't have any data. Creating a new one");
        TextReader textReader = new TextReader(vumi);
        textReader.getCustomMetadata().put("filename", "vumi.txt");
        List<Document> documents = textReader.get();
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        List<Document> splitDocuments = tokenTextSplitter.apply(documents);
        simpleVectorStore.add(splitDocuments);
        simpleVectorStore.save(vectorStoreFile);
        return simpleVectorStore;
    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "embedded");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vumiVectorStoreName;
        return new File(absolutePath);
    }
}

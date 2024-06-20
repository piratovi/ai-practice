package com.kolosov.aipractice.embedding;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.shell.command.annotation.Command;

import java.util.List;

@Command
@RequiredArgsConstructor
public class Embedding {

    private final OllamaEmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    @Command(command = "getEmbeddingDimension")
    public int getEmbeddingDimension() {
        return embeddingModel.dimensions();
    }

    @Command(command = "embed")
    public void embed(String absolutePath) {
//        var config = PdfDocumentReaderConfig.builder()
//                .build();

        var paragraphPdfDocumentReader = new ParagraphPdfDocumentReader(absolutePath);
        List<Document> documents = paragraphPdfDocumentReader.read();
        vectorStore.accept(documents);
    }

}

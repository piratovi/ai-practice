package com.kolosov.aipractice.embedding;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.shell.command.annotation.Command;

import java.util.List;

@Command
@RequiredArgsConstructor
public class Embedding {

    private final static List<String> processedFiles = List.of(
            "vumi.pdf",
            "spring-boot-reference.pdf"
    );

    private final OllamaEmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    @Command(command = "getEmbeddingDimension")
    public int getEmbeddingDimension() {
        return embeddingModel.dimensions();
    }

    //This method represents ETL pipeline
    @Command(command = "embed")
    public void embed(String absolutePath) {
        //TODO change to resource
        //Extract
        String fileUrl = "file:///" + absolutePath.replace("\\", "/");
        PdfDocumentReaderConfig pdfDocumentReaderConfig = preparePdfDocumentReaderConfig();
        PagePdfDocumentReader reader = new PagePdfDocumentReader(fileUrl, pdfDocumentReaderConfig);
        List<Document> documents = reader.read();

        //Transform (divides documents to fit the AI model’s context window.)
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splittedDocuments = splitter.split(documents);

        //Load
        vectorStore.write(splittedDocuments);
    }

    //TODO is this config necessary?
    private PdfDocumentReaderConfig preparePdfDocumentReaderConfig() {
        return PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                        .withNumberOfTopTextLinesToDelete(0)
                        .build())
                .withPagesPerDocument(1)
                .build();
    }

}

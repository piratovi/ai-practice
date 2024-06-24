package com.kolosov.aipractice.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

@Deprecated
//@Service
public class DocumentRagLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(RagToFileConfiguration.class);

    private final JdbcClient jdbcClient;

    private final VectorStore vectorStore;

    @Value("classpath:/docs/vumi.pdf")
    private Resource vumiPdfResource;

    public DocumentRagLoader(
            JdbcClient jdbcClient,
            VectorStore vectorStore
    ) {
        this.jdbcClient = jdbcClient;
        this.vectorStore = vectorStore;
    }

//    @PostConstruct
    public void init() {
        var count = jdbcClient.sql("select count(*) from vector_store")
                .query(Integer.class)
                .single();

        LOGGER.info("Current count of the Vector Store: {}", count);
        if (count == 0) {
            LOGGER.info("Loading Vumi PDF into Vector Store");
            var extractedTextFormatter = new ExtractedTextFormatter.Builder()
                    .withNumberOfBottomTextLinesToDelete(0)
                    .withNumberOfTopPagesToSkipBeforeDelete(0)
                    .build();
            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(extractedTextFormatter)
                    .withPagesPerDocument(1)
                    .build();

            var pdfReader = new PagePdfDocumentReader(vumiPdfResource, config);
            var textSplitter = new TokenTextSplitter();
            List<Document> documents = pdfReader.get();
            vectorStore.accept(textSplitter.apply(documents));

            LOGGER.info("Application is ready");
        }
        LOGGER.info("vectorStore = ", vectorStore);
    }
}

package com.kolosov.aipractice.asker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BeanParserAsker {

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    public BeanParserAsker(
            @Qualifier("openAiChatModel") ChatModel chatModel,
            ObjectMapper objectMapper
    ) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
    }

    private void run() {
        var outputParser = new BeanOutputParser<>(TechList.class, objectMapper);
        var format = outputParser.getFormat();

        var prompt = getPrompt(format, "trendy techs");
        var chatResponse = chatModel.call(prompt);
        String content = chatResponse.getResult().getOutput().getContent();
//        content = content.replace("'''json\n", "").replace("\n'''", "");
        System.out.println(content);
        var result = outputParser.parse(content);
        System.out.println(result);
    }


    public Prompt getPrompt(String format, String topic) {
        Map<String, Object> map = Map.of(
                "genre", topic,
                "format", format
        );

        var systemPromptTemplate = new SystemPromptTemplate("""
                You are a very exited web user that follows every trend in the {genre} topic.
                Use this format: {format}
                """);
        var systemMessage = systemPromptTemplate.createMessage(map);

        var promptTemplate = new PromptTemplate("""
                What are top 10 {genre}?
                """);
        var userMessage = promptTemplate.createMessage(map);

        var messages = List.of(systemMessage, userMessage);
        return new Prompt(messages);
    }
}

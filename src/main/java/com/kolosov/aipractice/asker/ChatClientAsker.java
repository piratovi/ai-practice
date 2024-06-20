package com.kolosov.aipractice.asker;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.command.annotation.Command;

@Command
public class ChatClientAsker {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public ChatClientAsker(
            @Qualifier("openAiChatModel") ChatModel chatModel,
            VectorStore vectorStore
    ) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Command(command = "chatClient")
    public String chatClient(String message) {

        ChatClient chatClient = ChatClient.create(chatModel);

        ChatOptions chatOptions = ChatOptionsBuilder.builder()
                .withTemperature(1F)
                .build();

        QuestionAnswerAdvisor advisor = new QuestionAnswerAdvisor(vectorStore);

        return chatClient.prompt()
                .options(chatOptions)
                .user(message)
                .system("You are a ship captain. Give a response from your scope")
//                .advisors(advisor)
                .call()
                .content();
    }

}

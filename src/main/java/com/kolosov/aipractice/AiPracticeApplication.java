package com.kolosov.aipractice;

import com.kolosov.aipractice.rag.RagAsker;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
public class AiPracticeApplication implements CommandLineRunner {

    public AiPracticeApplication() {
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(AiPracticeApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) {
    }


}



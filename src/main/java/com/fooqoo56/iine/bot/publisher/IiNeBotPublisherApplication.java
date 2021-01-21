package com.fooqoo56.iine.bot.publisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class IiNeBotPublisherApplication {

    /**
     * main.
     *
     * @param args 引数
     */
    public static void main(final String[] args) {
        SpringApplication.run(IiNeBotPublisherApplication.class, args);
    }

}

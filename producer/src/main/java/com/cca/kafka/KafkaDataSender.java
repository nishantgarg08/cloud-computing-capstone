package com.cca.kafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cca.kafka.common.payload.Person;


@SpringBootApplication
public class KafkaDataSender implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KafkaDataSender.class, args);
    }

    @Autowired
    public PersonDataSender sender;
    @Value("${app.topic.messageCount}")
    private String messageCount;
    @Value("${app.topic.sleep}")
    private long sleep;
    long messages=0;
    @Override
    public void run(String... strings) throws Exception {

    }
}
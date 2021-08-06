package com.cca.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cca.kafka.common.payload.Person;

@Service
@Component
public class PersonDataSender {

    private static final Logger LOG = LoggerFactory.getLogger(Person.class);

    @Autowired
    private KafkaTemplate<String, Person> kafkaTemplate;

    @Value("${app.topic.name}")
    private String topic;

    public void send(Person data){
        LOG.debug("sending data='{}' to topic='{}'", data, topic);

        Message<Person> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        
        kafkaTemplate.send(message);
    }

}
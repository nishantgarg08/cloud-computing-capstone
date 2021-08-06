package com.cca.kafka;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cca.kafka.common.payload.Person;

@EnableScheduling
@RestController
@Configuration
@RequestMapping("/sender")

public class ProducerService {
    private static final Logger LOG = LoggerFactory.getLogger(ProducerService.class);
    boolean startTest = false;
    @Autowired
    public PersonDataSender sender;
    @Value("${app.topic.messageCount}")
    private Long msgCount;
    long messages = 0;

    @Scheduled(fixedDelayString = "${app.topic.sleep}")
    public void send() {
        if (startTest) {
            for (int i = 0; i < msgCount; i++) {
                Person p = new Person();
                sender.send(p);
                if (messages == 0)
                    LOG.info("Producer is starting sending message");

                if (messages % 1000 == 0)
                    LOG.info("Transferred " + messages + " no of new messages ");
                messages++;
            }
        }
    }

    @RequestMapping(value = "/startTest")
    public @ResponseBody
    void start(@RequestParam Boolean start, @RequestParam(required = false) Long messageCount) throws IOException {
        startTest = start;
        if(startTest == true)
        {
            LOG.info("Starting the test");

        } else
        {
            LOG.info("Stopping the test");
        }
        if(messageCount!= null)
            msgCount = messageCount;
    }
}

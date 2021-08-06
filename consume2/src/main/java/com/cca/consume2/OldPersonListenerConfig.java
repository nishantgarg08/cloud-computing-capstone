package com.cca.consume2;

import java.util.Arrays;
import java.util.Properties;

import org.apache.arrow.vector.ipc.ArrowStreamReader;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.web.bind.annotation.RestController;

import com.cca.kafka.common.payload.Person;

@RestController
@Configuration
@EnableKafka
public class OldPersonListenerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(OldPersonListenerConfig.class);
    @Value("${spring.kafka.bootstrap-servers}")
    String BOOTSTRAP_SERVERS;
    @Value("${app.topic.name}")
    String topic;

    //@Autowired
    public void executeConsumer() {

        // Create normal Receiver
        Properties normalConsumerProperties = getProperties(StringDeserializer.class, "normal-receiver");
        KafkaConsumer normalConsumer = new KafkaConsumer<String, Person>(normalConsumerProperties);
        normalConsumer.subscribe(Arrays.asList(topic));

        // Create arrow Receiver
        Properties arrowConsumerProperties = getProperties(VectorSchemaRootDeserializer.class, "arrow-receiver");
        KafkaConsumer arrowConsumer = new KafkaConsumer<String, ArrowStreamReader>(arrowConsumerProperties);
        arrowConsumer.subscribe(Arrays.asList(topic+"-arrow"));

        startConsumer(normalConsumer, arrowConsumer);

    }

    private void startConsumer(KafkaConsumer normalConsumer, KafkaConsumer arrowConsumer) {
        LOG.info("Listening on topic=" + topic );
        // Register with Hot partition Detector
        //registerConsumer(normalConsumer.partitionsFor(topic), "myconsumer");
        LOG.info(String.valueOf(normalConsumer.partitionsFor(topic)));
        while (true) {
            ConsumerRecords<String, Person> records = normalConsumer.poll(10);
            for (ConsumerRecord<String, Person> record : records) {
                JSONObject p = new JSONObject(String.valueOf(record.value()));

            }
        }
    }

    private Properties getProperties(Class deserializer, String groupId)
    {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        //props.put(ConsumerConfig.GROUP_ID_CONFIG, String.valueOf(System.currentTimeMillis()));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, String.valueOf(System.currentTimeMillis()));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 20);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.setProperty("max.partition.fetch.bytes",String.valueOf(Integer.MAX_VALUE));
        // props.setProperty("max.partition.fetch.bytes",String.valueOf(Integer.MAX_VALUE));
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,String.valueOf(Integer.MAX_VALUE));
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.cca.kafka.consumer");
        return props;
    }


}
package com.cca.arrow.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.Float4Vector;
import org.apache.arrow.vector.UInt8Vector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.dictionary.DictionaryProvider;
import org.apache.arrow.vector.ipc.ArrowStreamWriter;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cca.kafka.common.payload.Person;
import com.cca.kafka.common.payload.RestHttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;

import static com.cca.arrow.converter.ArrowSchemas.personSchema;

@EnableScheduling
@RestController
@Configuration
@RequestMapping("/arrow")
@Service
public class MessageReaderArrowWriter implements ConsumerSeekAware {
    private static final Logger LOG = LoggerFactory.getLogger(MessageReaderArrowWriter.class);
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    @Autowired
    private PersonDb personDb;
    @Autowired
    private KafkaTemplate<String, ByteArrayOutputStream> kafkaVectorTemplate;
    int partionId;
    long offsetNumber;
    int messageCount;

    @Value("${app.service.consumer}")
    private String consumerName;
    @Value("${arrow.batch.milliseconds}")
    private int pollTime;
    @Value("${arrow.batch.chunksize}")
    private Integer chunk_size;
    private Vectorizer<Person> vectorizer;

    @Value("${app.topic.name}")
    private String topicName;
    @KafkaListener(topics = "${app.topic.name}", id = "consumer1",autoStartup = "false")
    public void receive(@Payload String data,
        @Headers MessageHeaders headers) throws IOException, JsonProcessingException {
        if(messageCount == 0)
        {
            RestHttpClient client = new RestHttpClient();
            Map<String, String> offset = new HashMap<>();
            offset.put("offset", String.valueOf(offsetNumber+1));
            client.doGetRequest(consumerName+"/consumer/arrow-statrt-offset", offset);
            headers.keySet().forEach(key -> {
                LOG.info("{}: {}", key, headers.get(key));
            });
        }
        JSONObject jsonObj = new JSONObject(data);
        Person p = new Person(jsonObj.getString("gender").charAt(0), jsonObj.getInt("salary"), jsonObj.getLong("creationTime"));
        personDb.addPerson(p);
        messageCount ++;

    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        // Seek all the assigned partition to a certain offset `10234575L`
        LOG.info("Changing {} offset of {} partiontion for topic {} ", offsetNumber, partionId, topicName);
        assignments.keySet().forEach(x->{
            if(x.partition() == partionId)
            {
                LOG.info("Inside onPartitionsAssigned "+partionId);
                callback.seek(topicName, partionId, offsetNumber+1);
            }
        });
        //assignments.keySet().forEach(partition -> callback.seek("com.madadipouya.message.events", partition.partition(), 10234575L));
    }
    @RequestMapping(value = "/writer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    void updateContainerPerfInfo(@RequestBody String body) throws IOException {
        JSONObject p = new JSONObject(body);
        Boolean isHot = p.getBoolean("hotStatus");

        if(isHot == true)
        {
            partionId = p.getInt("partitionId");
            offsetNumber = p.getLong("offsetNumber");
            MessageListenerContainer container = kafkaListenerEndpointRegistry.getListenerContainer("consumer1");
            if (!container.isRunning())
            {
                container.start();
            }
        }

    }

    @Scheduled(fixedDelayString = "${arrow.batch.milliseconds}")
    public void writeArrowMsgToKafka()
    {
        System.out.println(pollTime+"######"+personDb.size()+"#####"+chunk_size);
        if(personDb.size() < chunk_size)
            return;
        List<Person> persons = personDb.poll();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeToKafkaInArrowFile(persons);
            }
        }).start();
        //timetoWriteArrowMessage += (double)(System.nanoTime() - time)/1000;
        //LOG.info("Wrote {} persons record to Arrow. Time Taken = {} microsec", persons.size(), timetoWriteArrowMessage);
    }

    private void writeToKafkaInArrowFile(List<Person> persons)
    {
        try {
            vectorizer = this::vectorizePerson;
            write(chunk_size, topicName+"-arrow", persons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Integer chunk_size, String s, List<Person> persons) throws IOException {
        if(persons.size() == 0)
            return;
        ArrayList<VectorSchemaRoot> schemas = new ArrayList<>();
        DictionaryProvider.MapDictionaryProvider dictProvider = new DictionaryProvider.MapDictionaryProvider();
        try (RootAllocator allocator = new RootAllocator();
             VectorSchemaRoot schemaRoot = VectorSchemaRoot.create(personSchema(), allocator);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ArrowStreamWriter fileWriter = new ArrowStreamWriter(schemaRoot, dictProvider, byteArrayOutputStream)) {
            fileWriter.start();

            int index = 0;
            while (index < persons.size()) {
                schemaRoot.allocateNew();
                int chunkIndex = 0;
                while (chunkIndex < chunk_size && index + chunkIndex < persons.size()) {
                    vectorizer.vectorize(persons.get(index + chunkIndex), chunkIndex, schemaRoot);
                    chunkIndex++;
                }
                schemaRoot.setRowCount(chunkIndex);
                LOG.debug("Filled chunk with {} items; {} items written", chunkIndex, index + chunkIndex);
                fileWriter.writeBatch();
                LOG.debug("Chunk written");
                index += chunkIndex;
                send(byteArrayOutputStream);
                schemaRoot.clear();
            }
            LOG.debug("Writing done");
            fileWriter.end();
        }
    }

    @FunctionalInterface
    public interface Vectorizer<T> {
        void vectorize(T value, int index, VectorSchemaRoot batch);
    }
    /**
     * Converts a Person into entries into the vector contained in the VectorSchemaRoot. The method assumes that the
     * schema of the vectorSchemaRoot is the Person schema.
     *
     * @param person     Person to write
     * @param index      Where to write in the vectors
     * @param schemaRoot Container of the vectors
     */
    private void vectorizePerson(Person person, int index, VectorSchemaRoot schemaRoot) {
        // Using setSafe: it increases the buffer capacity if needed
        // Age, Gender, Salary
        //((VarCharVector) schemaRoot.getVector("firstName")).setSafe(index, person.getFirstName().getBytes());
        //((VarCharVector) schemaRoot.getVector("lastName")).setSafe(index, person.getLastName().getBytes());
        //((UInt4Vector) schemaRoot.getVector("age")).setSafe(index, person.getAge());
        ((VarCharVector) schemaRoot.getVector("gender")).setSafe(index, new byte[]{(byte)person.getGender()});
        ((UInt8Vector) schemaRoot.getVector("salary")).setSafe(index, person.getSalary());
        ((Float4Vector) schemaRoot.getVector("time")).setSafe(index, person.getCreationTime());
    }

    public void send(ByteArrayOutputStream stream){
        Message<ByteArrayOutputStream> message = MessageBuilder
            .withPayload(stream)
            .setHeader(KafkaHeaders.TOPIC, topicName+"-arrow")
            .build();

        kafkaVectorTemplate.send(message);
    }
}

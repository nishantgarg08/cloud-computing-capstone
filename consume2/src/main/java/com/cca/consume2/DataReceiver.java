package com.cca.consume2;


import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.arrow.vector.Float4Vector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowStreamReader;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.cca.kafka.common.payload.Register;
import com.cca.kafka.common.payload.RestHttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static jdk.incubator.vector.VectorOperators.ADD;

@Service
public class DataReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(DataReceiver.class);
    private static final Long BILLION = 1000000000L;
    /*@Autowired
    private ConsumerState state;*/
    @Autowired
    private ConsumerState state;
    @Value("${app.service.consumer}")
    private String serviceName;
    @Value("${app.service.hotpartion}")
    private String hpServiceName;
    private double startNormalReceiveTime;
    private long startArrowReceiveTime;
    private long arrowMessageCount =0;
    @KafkaListener(topics = "${app.topic.name}")
    public void receive(@Payload String data,
        @Headers MessageHeaders headers) throws IOException, JsonProcessingException{
        if (state.getTotal().getRecords() == 0) {
            startNormalReceiveTime = System.currentTimeMillis();
            state.getNormal().setStartReceiveTime(startNormalReceiveTime);
            state.getTotal().setStartReceiveTime(startNormalReceiveTime);

            KafkaConsumer consumer = (KafkaConsumer) headers.get("kafka_consumer");
            String consumerId = consumer.groupMetadata().memberId();
            registerConsumerId(consumerId, serviceName);
            headers.keySet().forEach(key -> {
                LOG.info("{}: {}", key, headers.get(key));
            });

        }
        int partionId = (Integer) headers.get("kafka_receivedPartitionId");
        long readOfset = (Long) headers.get("kafka_offset");
        if (state.getPartitions().get(partionId).isHot() == false) {
            state.getTotal().incrRecords(1);
            state.getNormal().incrRecords(1);
            state.getPartitions().get(partionId).setHot(false);
            state.getPartitions().get(partionId).setNumber(partionId);
            state.getPartitions().get(partionId).setNormalReceiverOffset(readOfset);

            JSONObject p = new JSONObject(data);

            if (p.has("creationTime")) {
                double timeTaken = 1.0 * ((1.0) * System.nanoTime()/BILLION -  ((1.0) * p.getLong("creationTime")/BILLION));
                state.getNormal().setMessageResidencyTime(timeTaken);
                state.getTotal().setMessageResidencyTime(timeTaken);
            }
            long time = System.nanoTime();
            if (p.getString("gender").equalsIgnoreCase("F")) {
                state.getNormal().incrResult(1);
                state.getTotal().incrResult(1);
            }
            double operationTime = 1.0 * ((double)System.nanoTime()/BILLION - (double)time/BILLION);
            state.getNormal().incrMessageProcessingTime(operationTime);
            state.getTotal().incrMessageProcessingTime(operationTime);
            state.getTotal().setLastReceiveTime(System.currentTimeMillis());
            state.getNormal().setLastReceiveTime(System.currentTimeMillis());
            /*double recvTime = 1.0 * (1.0 * System.nanoTime()/BILLION - startNormalReceiveTime);

            //System.out.println("Receive Time "+recvTime);
            state.getNormal().setReceiveTime(recvTime);
            state.getTotal().setReceiveTime(recvTime);*/

        }
    }

    private void registerConsumerId(String consumerId,String serviceName) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Register register = new Register(consumerId, serviceName);
        String json = mapper.writeValueAsString(register);
        RestHttpClient client = new RestHttpClient();
        client.doPostRequest(hpServiceName+"/partition/register", json);
    }

    @KafkaListener(topics = "${app.topic.hotname}",
        containerFactory = "arrowKafkaListenerContainerFactory")
    public void receiveArrow(@Payload ArrowStreamReader data,
        @Headers MessageHeaders headers) throws IOException {
        if(arrowMessageCount == 0)
        {
            startArrowReceiveTime = System.currentTimeMillis();
            state.getArrow().setStartReceiveTime(startArrowReceiveTime);
        }
        arrowMessageCount++;
        VectorSchemaRoot schemaRoot = data.getVectorSchemaRoot();
        while (data.loadNextBatch()) {
            Float4Vector time = (Float4Vector) schemaRoot.getVector("time");
            VarCharVector gender = (VarCharVector) schemaRoot.getVector("gender");

            int capacity = gender.getValueCapacity();
            int values[] = new int[capacity];
            long readTime = System.nanoTime();
            double totalTimeTaken = state.getTotal().getMessageResidencyTime();
            double arrowTotalTimeTaken = 0.0D;
            if ( arrowMessageCount > 1)
               arrowTotalTimeTaken += state.getArrow().getMessageResidencyTime();
            //LOG.info("{} - {} = {}", (1.0) * System.nanoTime()/1000000000, ((1.0) * p.getLong("creationTime")/1000000000), timeTaken);
                /*state.getNormal().incrMsgDeliveryTime(timeTaken);
                state.getNormal().maxMsgDeliveryTime(timeTaken);
                state.getTotal().incrMsgDeliveryTime(timeTaken);
                state.getTotal().maxMsgDeliveryTime(timeTaken);*/

            for (int i = 0; i < capacity; i++) {
                double timeTaken = (1.0 *(readTime/BILLION) - (1.0 * time.get(i)/BILLION));
                state.getArrow().setMessageResidencyTime(timeTaken);
                state.getTotal().setMessageResidencyTime(timeTaken);
                /*state.getArrow().incrMsgDeliveryTime(timeTaken);
                state.getArrow().maxMsgDeliveryTime(timeTaken);

                state.getTotal().incrMsgDeliveryTime(timeTaken);
                state.getTotal().maxMsgDeliveryTime(timeTaken);*/

                values[i] = new String(gender.get(i), StandardCharsets.UTF_8)
                    .equalsIgnoreCase("F") ? 1 : 0;
            }
            //state.getArrow().setMessageResidencyTime(1.0D * arrowTotalTimeTaken/(capacity + 1));
            //state.getTotal().setMessageResidencyTime(1.0D * totalTimeTaken/(capacity + 1));

            state.getArrow().incrRecords(capacity);
            state.getTotal().incrRecords(capacity);

            state.getArrow().incrArrowMessage(1);
            long startProcessingTime = System.nanoTime();
            int count = getVectorValue(values);
            double operationTime = 1.0 * ((double) System.nanoTime()/BILLION - (1.0) * startProcessingTime/BILLION);
            state.getArrow().incrResult(count);
            state.getTotal().incrResult(count);
            state.getArrow().incrMessageProcessingTime(operationTime);
            state.getTotal().incrMessageProcessingTime(operationTime);
            state.getTotal().setLastReceiveTime(System.currentTimeMillis());
            state.getArrow().setLastReceiveTime(System.currentTimeMillis());
            /*double recvTime = (1.0 * System.nanoTime()/BILLION) - (1.0 * startArrowReceiveTime/BILLION);
            state.getArrow().setReceiveTime(recvTime);
            state.getTotal().setReceiveTime(recvTime);*/
        }
    }

    VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    private int getVectorValue(int[] values) {
        int length = SPECIES.length();
        int sum = 0;
        for (int i = 0; i < values.length; i += length) {
            var m = SPECIES.indexInRange(i, values.length);
            IntVector va = IntVector.fromArray(SPECIES, values, i, m);
            va.compare(VectorOperators.EQ, 1);
            sum += va.reduceLanes(ADD);
        }
        return sum;
    }
}

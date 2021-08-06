package com.cca.hotpartitiondetector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cca.kafka.common.payload.PartitionInfo;
import com.cca.kafka.common.payload.RestHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@Configuration
@RequestMapping("/partition")
public class PartitionController {

    // Storage for Consumers
    @Value("${app.service.arrow}")
    String urlArrowWriter;
    @Autowired
    ConsumerStorage storage;

    // Got the Hot Partion and let the consumer know about this
    @RequestMapping(value = "/notify/{partionId}")
    public @ResponseBody
    Long updatePartitionInfo(@PathVariable("partionId") int partitionId, @RequestParam Boolean hotStatus) throws IOException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("hotStatus", String.valueOf(hotStatus));
        RestHttpClient client = new RestHttpClient();
        String consumerSvcName = storage.getServiceName();
        // Ask client on how much you read so far
        long offsetNumber = Long.parseLong(client.doGetRequest(consumerSvcName+"/consumer/notify/"+partitionId, queryParams));
        storage.update(offsetNumber);
        storage.update(partitionId);
        // Now call the Arrow Writer
        PartitionInfo partitionInfo = new PartitionInfo(partitionId, offsetNumber, hotStatus);
        String json = new ObjectMapper().writeValueAsString(partitionInfo);
        client.doPostRequest(urlArrowWriter+"/arrow/writer", json);
        return offsetNumber;
    }


    // Each consumer registers
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String register(@RequestBody String body) {
        JSONObject p = new JSONObject(body);
        String consumerSvcName = p.getString("serviceUrl");
        String consumerId = p.getString("consumerId");
        storage.insert(consumerId);
        storage.update(consumerSvcName);
        return consumerSvcName;
    }
}

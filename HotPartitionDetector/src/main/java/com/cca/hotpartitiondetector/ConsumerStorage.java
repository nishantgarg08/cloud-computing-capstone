package com.cca.hotpartitiondetector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Scope("singleton")
public class ConsumerStorage {
    ConcurrentHashMap<String, ConsumerInfo> consumerMap;

    public ConsumerStorage() {
        consumerMap = new ConcurrentHashMap<>();
    }
    public void insert (String consumerId)
    {
        ConsumerInfo info = new ConsumerInfo();
        consumerMap.put(consumerId, info);
    }
    public void update (String svcName)
    {
        Map.Entry<String,ConsumerInfo> entry = consumerMap.entrySet().iterator().next();
        entry.getValue().setServiceName(svcName);
    }
    public void update (int partion)
    {
        Map.Entry<String,ConsumerInfo> entry = consumerMap.entrySet().iterator().next();
        entry.getValue().setPartionId(partion);
    }

    public void update (long offset)
    {
        Map.Entry<String,ConsumerInfo> entry = consumerMap.entrySet().iterator().next();
        entry.getValue().setOffSetId(offset);
    }
    public String getServiceName ()
    {
        Map.Entry<String,ConsumerInfo> entry = consumerMap.entrySet().iterator().next();
        return entry.getValue().getServiceName();
    }
    private class ConsumerInfo {
        int partionId;
        long offSetId;
        String serviceName;

        public ConsumerInfo() {
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public int getPartionId() {
            return partionId;
        }

        public void setPartionId(int partionId) {
            this.partionId = partionId;
        }

        public long getOffSetId() {
            return offSetId;
        }

        public void setOffSetId(long offSetId) {
            this.offSetId = offSetId;
        }
    }
}

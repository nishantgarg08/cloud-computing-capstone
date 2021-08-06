package com.cca.kafka.common.payload;

public class Register {
    private String consumerId;
    private String serviceUrl;

    public Register(String consumerId, String serviceUrl) {
        this.consumerId = consumerId;
        this.serviceUrl = serviceUrl;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}

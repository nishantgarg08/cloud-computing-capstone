package com.cca.arrow.converter;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;


public class VectorSchemaRootSerializer implements Serializer<ByteArrayOutputStream> {

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, ByteArrayOutputStream vectorSchemaRoot) {
        byte[] bytes = vectorSchemaRoot.toByteArray();
        return bytes;
    }

    @Override
    public void close() {

    }
}

package com.cca.consume2;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Map;

import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.ipc.ArrowStreamReader;
import org.apache.kafka.common.serialization.Deserializer;


public class VectorSchemaRootDeserializer<T extends Serializable> implements Deserializer<ArrowStreamReader> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @SuppressWarnings("unchecked")
    @Override

    public ArrowStreamReader deserialize(String s, byte[] bytes) {
        RootAllocator allocator = new RootAllocator();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ArrowStreamReader fileReader = new ArrowStreamReader(byteArrayInputStream, allocator);
        return fileReader;
    }
    @Override
    public void close() {
    }

}


/*
public class VectorSchemaRootDeSerializer implements ExtendedDeserializer {
    public VectorSchemaRootDeSerializer() {
        System.out.println("A");
    }

    @Override
    public void configure(Map map, boolean b) {
        System.out.println("A");
    }
    @Override
    public ArrowStreamReader deserialize(String s, byte[] bytes) {
        RootAllocator allocator = new RootAllocator();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ArrowStreamReader fileReader = new ArrowStreamReader(byteArrayInputStream, allocator);
        return fileReader;
    }

    @Override
    public void close() {

    }

    @Override
    public Object deserialize(String s, Headers headers, byte[] bytes) {
        return null;
    }
}
*/

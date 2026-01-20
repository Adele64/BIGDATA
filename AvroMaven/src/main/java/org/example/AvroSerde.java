package org.example;

import com.bigdata.avro.Student;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;


public class AvroSerde implements Serde<Student> {

    private final AvroSerializer serializer;
    private final AvroDeserializer deserializer;

    public AvroSerde(Student targetType) {
        this.serializer = new AvroSerializer();
        this.deserializer = new AvroDeserializer(targetType);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
        serializer.close();
        deserializer.close();
    }

    @Override
    public Serializer serializer() {
        return serializer;
    }

    @Override
    public Deserializer deserializer() {
        return deserializer;
    }
}
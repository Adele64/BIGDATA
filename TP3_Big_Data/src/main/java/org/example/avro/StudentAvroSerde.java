package org.example.avro;

import org.apache.kafka.common.serialization.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

public class StudentAvroSerde implements Serde<Student> {
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}
    
    @Override
    public void close() {}
    
    @Override
    public Serializer<Student> serializer() {
        return new Serializer<Student>() {
            @Override
            public byte[] serialize(String topic, Student student) {
                try {
                    return student == null ? null : student.toByteBuffer().array();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    @Override
    public Deserializer<Student> deserializer() {
        return new Deserializer<Student>() {
            @Override
            public Student deserialize(String topic, byte[] data) {
                try {
                    return data == null ? null : Student.fromByteBuffer(ByteBuffer.wrap(data));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
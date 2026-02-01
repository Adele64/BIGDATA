package org.example.avro;

import org.apache.kafka.clients.producer.*;
import java.util.Properties;

public class StudentAvroProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        
        try (KafkaProducer<String, byte[]> producer = new KafkaProducer<>(properties)) {
            Student student = Student.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setAge(22)
                .setEngineeringDegree("Computer Science")
                .build();
            
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(
                "students-avro",
                student.toByteBuffer().array()
            );
            
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println("Avro message sent successfully");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
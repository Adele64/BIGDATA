package org.example.avro;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class StudentAvroConsumer {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-students-avro");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of("students-avro"));
        
        try {
            while (true) {
                ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, byte[]> record : records) {
                    Student student = Student.fromByteBuffer(ByteBuffer.wrap(record.value()));
                    System.out.println("Avro Student: " + student);
                }
            }
        } finally {
            consumer.close();
        }
    }
}
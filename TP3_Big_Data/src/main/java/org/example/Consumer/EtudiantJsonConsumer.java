package org.example.consumer;

import org.example.Etudiant;
import org.apache.kafka.clients.consumer.*;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class EtudiantJsonConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.example.consumer.EtudiantJsonDeserializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-etudiants-json");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        KafkaConsumer<String, Etudiant> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of("etudiants-json"));
        
        try {
            while (true) {
                ConsumerRecords<String, Etudiant> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, Etudiant> record : records) {
                    System.out.println("JSON Student: " + record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
}
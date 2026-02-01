package org.example.producer;

import org.example.Etudiant;
import org.apache.kafka.clients.producer.*;
import java.util.Properties;
import java.util.stream.IntStream;

public class EtudiantProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.example.producer.EtudiantSerializer");
        
        try (KafkaProducer<String, Etudiant> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<String, Etudiant> record = new ProducerRecord<>(
                "etudiants", 
                new Etudiant("test", "test", 21, "IT")
            );
            
            IntStream.range(0, 10).forEach(i -> producer.send(record));
        }
    }
}
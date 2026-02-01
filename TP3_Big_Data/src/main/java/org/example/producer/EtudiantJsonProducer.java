package org.example.producer;

import org.example.Etudiant;
import org.apache.kafka.clients.producer.*;
import java.util.Properties;

public class EtudiantJsonProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.example.producer.EtudiantJsonSerializer");
        
        try (KafkaProducer<String, Etudiant> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<String, Etudiant> record = new ProducerRecord<>(
                "etudiants-json", 
                new Etudiant("John", "Doe", 22, "Computer Science")
            );
            
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println("JSON message sent successfully");
                }
            });
        }
    }
}
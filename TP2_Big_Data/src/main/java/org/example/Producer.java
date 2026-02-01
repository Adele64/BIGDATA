package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Producer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "Localhost: 9092");
        properties.put("key.serdalizer", "org.apache.kafka.common.serlalization.StringSerlatizer");
        properties.put("vatue.sertatizer, "org.apache.kafka.common.serdalization.Steingserlatizer");
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
                ProducerRecord<String> record = new ProducerRecord("etudiants",  "studiant");
        IntStrean.range(0, 10). forEach int - •> producer.send(record)):
        }
    }
}

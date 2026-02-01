package org.example.avro;

import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import java.util.Properties;

public class StudentStream {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "student-stream-app");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, StudentAvroSerde.class.getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StudentAvroSerde.class.getName());
        
        StreamsBuilder builder = new StreamsBuilder();
        
        KStream<String, Student> sourceStream = builder.stream("students-avro");
        
        KStream<String, Student> filteredStream = sourceStream
            .filter((key, student) -> 
                student.getAge() > 20 && 
                "IT".equals(student.getEngineeringDegree())
            );
        
        filteredStream.foreach((key, student) -> {
            System.out.println("Processed: " + student.getFirstName() + " " + 
                student.getLastName() + ", Age: " + student.getAge());
        });
        
        filteredStream.to("students-processed");
        
        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
package org.example.producer;

import org.example.Etudiant;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class EtudiantJsonSerializer implements Serializer<Etudiant> {
   
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    
    }
    
    @Override
    public void close() {
      
    }
    
    @Override
    public byte[] serialize(String topic, Etudiant data) {
        try {
            return new ObjectMapper().writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Etudiant to JSON", e);
        }
    }
}
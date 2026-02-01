package org.example.Consumer;

import org.example.Etudiant;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class EtudiantJsonDeserializer implements Deserializer<Etudiant> {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public Etudiant deserialize(String topic, byte[] data) {
        try {
            return data == null ? null : objectMapper.readValue(data, Etudiant.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON", e);
        }
    }
}
package org.example.consumer;

import org.example.Etudiant;
import org.apache.kafka.common.serialization.Deserializer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EtudiantDeserializer implements Deserializer<Etudiant> {
    
    @Override
    public Etudiant deserialize(String topic, byte[] data) {
        if (data == null) return null;
        
        ByteBuffer buffer = ByteBuffer.wrap(data);
        
        int firstNameLength = buffer.getInt();
        String firstName = new String(readBytes(buffer, firstNameLength), StandardCharsets.UTF_8);
        
        int lastNameLength = buffer.getInt();
        String lastName = new String(readBytes(buffer, lastNameLength), StandardCharsets.UTF_8);
        
        int age = buffer.getInt();
        
        int degreeLength = buffer.getInt();
        String degree = new String(readBytes(buffer, degreeLength), StandardCharsets.UTF_8);
        
        return new Etudiant(firstName, lastName, age, degree);
    }
    
    private byte[] readBytes(ByteBuffer buffer, int length) {
        byte[] bytes = new byte[length];
        if (length > 0) buffer.get(bytes);
        return bytes;
    }
}
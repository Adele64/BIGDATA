package org.example.producer;

import org.example.Etudiant;
import org.apache.kafka.common.serialization.Serializer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class EtudiantSerializer implements Serializer<Etudiant> {
    
    @Override
    public byte[] serialize(String topic, Etudiant etudiant) {
        if (etudiant == null) {
            return null;
        }
        
        byte[] firstNameBytes = etudiant.getFirstName() != null 
            ? etudiant.getFirstName().getBytes(StandardCharsets.UTF_8) 
            : new byte[0];
        
        byte[] lastNameBytes = etudiant.getLastName() != null 
            ? etudiant.getLastName().getBytes(StandardCharsets.UTF_8) 
            : new byte[0];
        
        byte[] degreeBytes = etudiant.getEngineeringDegree() != null 
            ? etudiant.getEngineeringDegree().getBytes(StandardCharsets.UTF_8) 
            : new byte[0];
        
        int capacity = 4 + firstNameBytes.length + 
                       4 + lastNameBytes.length + 
                       4 + 4 + degreeBytes.length;
        
        ByteBuffer buffer = ByteBuffer.allocate(capacity);
        buffer.putInt(firstNameBytes.length);
        buffer.put(firstNameBytes);
        buffer.putInt(lastNameBytes.length);
        buffer.put(lastNameBytes);
        buffer.putInt(etudiant.getAge());
        buffer.putInt(degreeBytes.length);
        buffer.put(degreeBytes);
        
        return buffer.array();
    }
}
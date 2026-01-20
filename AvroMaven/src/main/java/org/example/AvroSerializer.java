package org.example;

import com.bigdata.avro.Student;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class AvroSerializer implements Serializer<Student> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Pas de configuration nécessaire
    }

    @Override
    public byte[] serialize(String topic, Student data) {
        if (data == null) {
            return null;
        }

        try {
            return data.toByteBuffer().array();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sérialisation Avro", e);
        }
    }

    @Override
    public void close() {
        // Pas de ressources à fermer
    }
}
package org.example;

import com.bigdata.avro.Student;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;


public class AvroDeserializer implements Deserializer<Student> {

    private final Student targetType;

    public AvroDeserializer(Student targetType) {
        this.targetType = targetType;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Pas de configuration nécessaire
    }

    @Override
    public Student deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            return Student.fromByteBuffer(ByteBuffer.wrap(data));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la désérialisation Avro", e);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'instance", e);
        }
    }

    @Override
    public void close() {
        // Pas de ressources à fermer
    }
}
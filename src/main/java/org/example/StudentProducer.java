package org.example;

import com.bigdata.avro.Student;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;

/**
 * Producer qui envoie des objets Student (Avro) dans Kafka
 */
public class StudentProducer {

    private static final String TOPIC = "students-avro";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) throws InterruptedException {
        // Configuration du producer
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // Création du producer
        KafkaProducer<String, Student> producer = new KafkaProducer<>(props);

        // Données de test
        String[] names = {"Alice Dupont", "Bob Martin", "Charlie Bernard", "Diana Petit", "Ethan Durand"};
        String[] degrees = {"IT", "Business", "IT", "Engineering", "IT"};
        Random random = new Random();

        System.out.println("=== Démarrage du Producer ===");
        System.out.println("Envoi de messages vers le topic: " + TOPIC);

        try {
            // Envoi de 10 étudiants
            for (int i = 0; i < 10; i++) {
                int index = i % names.length;
                
                // Création d'un objet Student Avro
                Student student = Student.newBuilder()
                        .setId(i + 1)
                        .setName(names[index])
                        .setAge(18 + random.nextInt(8)) // Age entre 18 et 25
                        .setDegree(degrees[index])
                        .setEmail(names[index].toLowerCase().replace(" ", ".") + "@university.com")
                        .build();

                // Envoi du message
                ProducerRecord<String, Student> record = 
                    new ProducerRecord<>(TOPIC, String.valueOf(student.getId()), student);
                
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.printf("✓ Étudiant envoyé: %s, Age: %d, Diplôme: %s [Partition: %d, Offset: %d]%n",
                                student.getName(), student.getAge(), student.getDegree(),
                                metadata.partition(), metadata.offset());
                    } else {
                        System.err.println("✗ Erreur lors de l'envoi: " + exception.getMessage());
                    }
                });

                Thread.sleep(1000); // Pause d'1 seconde entre chaque envoi
            }

            System.out.println("\n=== Tous les messages ont été envoyés ===");

        } finally {
            producer.flush();
            producer.close();
            System.out.println("Producer fermé.");
        }
    }
}
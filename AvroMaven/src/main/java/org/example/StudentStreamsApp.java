package org.example;

import com.bigdata.avro.Student;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

/**
 * Application Kafka Streams pour traiter les messages Student
 */
public class StudentStreamsApp {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String INPUT_TOPIC = "students-avro";
    private static final String OUTPUT_TOPIC = "students-processed";
    private static final String APPLICATION_ID = "student-streams-app";

    public static void main(String[] args) {
        // Configuration de Kafka Streams
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // Création du Serde pour Student
        AvroSerde studentSerde = new AvroSerde(new Student());

        // Construction du flux de données
        StreamsBuilder builder = new StreamsBuilder();

        // Lecture du topic d'entrée
        KStream<String, Student> studentsStream = builder.stream(
                INPUT_TOPIC,
                Consumed.with(Serdes.String(), studentSerde)
        );

        System.out.println("=== Application Kafka Streams démarrée ===");
        System.out.println("Lecture depuis: " + INPUT_TOPIC);
        System.out.println("Écriture vers: " + OUTPUT_TOPIC);
        System.out.println("==========================================\n");

        // Affichage de tous les étudiants
        studentsStream.foreach((key, student) -> {
            System.out.println("┌─────────────────────────────────────────────");
            System.out.printf("│ Étudiant reçu: %s%n", student.getName());
            System.out.printf("│ ID: %d | Age: %d | Diplôme: %s%n", 
                    student.getId(), student.getAge(), student.getDegree());
            System.out.printf("│ Email: %s%n", student.getEmail());
            System.out.println("└─────────────────────────────────────────────\n");
        });

        // FILTRAGE: Étudiants de plus de 20 ans ET diplôme IT
        KStream<String, Student> filteredStream = studentsStream
                .filter((key, student) -> {
                    boolean ageCondition = student.getAge() > 20;
                    boolean degreeCondition = "IT".equalsIgnoreCase(student.getDegree().toString());
                    boolean passes = ageCondition && degreeCondition;
                    
                    if (passes) {
                        System.out.println("✓ FILTRE PASSÉ: " + student.getName() + 
                                " (Age: " + student.getAge() + ", Diplôme: " + student.getDegree() + ")");
                    } else {
                        System.out.println("✗ FILTRE REJETÉ: " + student.getName() + 
                                " (Age: " + student.getAge() + ", Diplôme: " + student.getDegree() + ")");
                    }
                    
                    return passes;
                });

        // Affichage des étudiants filtrés avec détails
        filteredStream.foreach((key, student) -> {
            System.out.println("\n╔═══════════════════════════════════════════════╗");
            System.out.println("║   ÉTUDIANT SÉLECTIONNÉ APRÈS FILTRAGE         ║");
            System.out.println("╠═══════════════════════════════════════════════╣");
            System.out.printf("║ Nom: %-40s ║%n", student.getName());
            System.out.printf("║ ID: %-41d ║%n", student.getId());
            System.out.printf("║ Age: %-40d ║%n", student.getAge());
            System.out.printf("║ Diplôme: %-36s ║%n", student.getDegree());
            System.out.printf("║ Email: %-38s ║%n", student.getEmail());
            System.out.println("╚═══════════════════════════════════════════════╝\n");
        });

        // Envoi vers le topic de sortie
        filteredStream.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), studentSerde));

        // Création et démarrage de l'application Streams
        KafkaStreams streams = new KafkaStreams(builder.build(), props);

        // Gestion de l'arrêt propre
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n=== Arrêt de l'application Kafka Streams ===");
            streams.close();
        }));

        // Démarrage
        streams.start();

        System.out.println("Application en cours d'exécution... (Ctrl+C pour arrêter)");
    }
}
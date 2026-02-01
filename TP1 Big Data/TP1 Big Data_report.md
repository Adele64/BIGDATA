# TP1 Big Data

[tp1-kafka.pdf](TP1%20Big%20Data/tp1-kafka.pdf)

# Partie 1 - Installation du JAR

### 1.3 Manipulation des Topics

 1. Créer un topic

1. Lister les topics
2. Décrire le topic créé précédemment test-topic

```jsx
tpreseau@pccop0b230-13:~/kafka_2.13-4.1.1$ bin/kafka-topics.sh --create --topic test-topic --partitions 3 --replication-factor 1 --bootstrap-server localhost:9092
Created topic test-topic.
tpreseau@pccop0b230-13:~/kafka_2.13-4.1.1$ bin/kafka-topics.sh --list --bootstrap-server localhost:9092
test-topic
tpreseau@pccop0b230-13:~/kafka_2.13-4.1.1$ bin/kafka-topics.sh --describe --topic test-topic --bootstrap-server localhost:9092
Topic: test-topic	TopicId: i2MC2bl0S6y0cd_PoC_ZgQ	PartitionCount: 3	ReplicationFactor: 1	Configs: min.insync.replicas=1,segment.bytes=1073741824
	Topic: test-topic	Partition: 0	Leader: 1	Replicas: 1	Isr: 1	Elr: 	LastKnownElr: 
	Topic: test-topic	Partition: 1	Leader: 1	Replicas: 1	Isr: 1	Elr: 	LastKnownElr: 
	Topic: test-topic	Partition: 2	Leader: 1	Replicas: 1	Isr: 1	Elr: 	LastKnownElr: 
```

### 1.4 Manipulation des Topics

1.Démarrer un producteur sur le topic test-topic

![image.png](TP1%20Big%20Data/image.png)

2.Démarrer un consommateur sur le topic test-topic
• Pourquoi les messages envoyez précédemment ne sont-ils pas reçu ?

La raison pour laquelle les messages envoyés par le producteur ne sont pas reçus par le consommateur si ce dernier est démarré après l'envoi des messages, réside dans le modèle de consommation de Kafka.

Kafka garde une trace des offsets de chaque consommateur. L'offset est simplement la position dans le topic à partir de laquelle un consommateur commence à lire les messages. Si on lance un consommateur sans `--from-beginning`, il commencera à lire à partir de l'offset où il s'est arrêté lors de la dernière exécution, ce qui signifie qu'il manque les messages précédemment envoyés.

1. Expérimentez :
• Envoyez de nouveaux messages depuis le producteur
• Observez-les apparaître dans le consommateur
• Arrêtez le consommateur (Ctrl+C) et démarrez-le
--from-beginning. Que se passe-t-il ?

![image.png](TP1%20Big%20Data/image%201.png)

4. Rajoutez un consumer group lors du lancement du topic avec --group group

5. Questions à répondre :
• Lancez ce consommateur 2 fois en parallèle (2 terminaux différents avec
même groupe). Envoyez des messages. Comment sont-ils distribués ?

```jsx
bin/kafka-console-consumer.sh --topic test-topic --bootstrap-server localhost:9092 --group group1

bin/kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092

```

![image.png](TP1%20Big%20Data/image%202.png)

 On a observé que le topic a une seule partition, seul un consommateur du groupe peut lire les messages. 

Le groupe de consommateurs permet de répartir les messages d’un topic entre plusieurs consommateurs. Chaque message sera lu une seule fois par un membre du groupe. Cela permet une consommation parallèle sans duplication des messages.

# Partie 2 - Utilisation avec Docker

## 2.1 Configuration Docker Compose

1. Créer un fichier docker-compose-zookeeper.yml en comprenant
l’image confluentinc/cp-kafka:7.4.4 avec un ZooKeeper
confluentinc/cp-zookeeper:7.4.4

```jsx
nano docker-compose-zookeeper.yml

version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.4
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_SYNC_LIMIT: 2
    ports:
      - "2181:2181"
    networks:
      - kafka-net

  kafka-broker:
    image: confluentinc/cp-kafka:7.4.4
    depends_on:
      - zookeeper
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL: PLAINTEXT
      KAFKA_ADVERTISED_LISTENER: INSIDE://kafka-broker:29092
      KAFKA_LISTENER_NAME_INTERNAL: INSIDE
      KAFKA_LISTENER_PORT: 29092
      KAFKA_LISTENER_INTERNAL: INSIDE
      KAFKA_LISTENER_EXTERNAL: OUTSIDE
      KAFKA_LISTENER_PORT_EXTERNAL: 9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_INTERNAL: PLAINTEXT
      KAFKA_LISTENER_SECURITY_PROTOCOL_EXTERNAL: PLAINTEXT
      KAFKA_LISTENER_SASL_MECHANISMS: PLAIN
      KAFKA_LOG_DIRS: /var/lib/kafka/data
    ports:
      - "9093:9093"
      - "29092:29092"
    networks:
      - kafka-net
    restart: unless-stopped

networks:
  kafka-net:
    driver: bridge

```

1. Créer un fichier docker-compose-kraft.yml en comprenant l’image
confluentinc/cp-kafka:8.0.3 mode KRaft

```jsx
nano docker-compose-kraft.yml

version: '3'
services:
  kafka-broker:
    image: confluentinc/cp-kafka:8.0.3
    environment:
      KAFKA_LISTENER_SECURITY_PROTOCOL: PLAINTEXT
      KAFKA_LISTENER_PORT: 9092
      KAFKA_ADVERTISED_LISTENER: INSIDE://kafka-broker:9092
      KAFKA_LISTENER_NAME_INTERNAL: INSIDE
      KAFKA_LISTENER_INTERNAL: INSIDE
      KAFKA_LISTENER_EXTERNAL: OUTSIDE
      KAFKA_LISTENER_PORT_EXTERNAL: 9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_INTERNAL: PLAINTEXT
      KAFKA_LISTENER_SECURITY_PROTOCOL_EXTERNAL: PLAINTEXT
      KAFKA_LISTENER_SASL_MECHANISMS: PLAIN
      KAFKA_LOG_DIRS: /var/lib/kafka/data
      KAFKA_KRAFT_MODE: "true"
    ports:
      - "9093:9093"
      - "9092:9092"
    networks:
      - kafka-net
    restart: unless-stopped

networks:
  kafka-net:
    driver: bridge

```

### 3. Démarrer Kafka :

```jsx
 -f docker-compose-kraft up -d
```

### 4. Vérifier les logs :

```jsx
docker compose logs -f kafka-broker
```

![image.png](TP1%20Big%20Data/image%203.png)

### 2.2 Manipulation des Topics avec Docker

```jsx
**Connexion au docker :**
docker exec -it kafka-broker sh
```

1. **Créer un topic etudiants**

```jsx
sh-5.1$ kafka-topics --create --topic etudiants --partitions 3 --replication-factor 1 --bootstrap-server localhost:9092
Created topic etudiants.
```

1. **Lister les topics et décrire le topic etudiants**

```jsx
sh-5.1$ kafka-topics --describe --topic etudiants --bootstrap-server localhost:9092
Topic: etudiants	TopicId: 5iyXxUscQAagucJtsmrYIw	PartitionCount: 3	ReplicationFactor: 1	Configs: 
	Topic: etudiants	Partition: 0	Leader: 1	Replicas: 1	Isr: 1	Elr: 	LastKnownElr: 
	Topic: etudiants	Partition: 1	Leader: 1	Replicas: 1	Isr: 1	Elr: 	LastKnownElr: 
	Topic: etudiants	Partition: 2	Leader: 1	Replicas: 1	Isr: 1	Elr: 	LastKnownElr: 
```

1. **Créer un producteur Docker sur le topic etudiants**

```jsx
sh-5.1$ kafka-console-producer --topic etudiants --bootstrap-server localhost:9092
>{"firstName": "Alice", "lastName": "Dupont", "age": 22, "engineeringDegree": "Computer Science"}
{"firstName": "Bob", "lastName": "Martin", "age": 25, "engineeringDegree": "Mechanical Engineering"}
>>

```

```jsx
tpreseau@pccop0b230-13:~/kafka_2.13-4.1.1$  docker exec -it kafka-broker sh
sh-5.1$ kafka-console-consumer --topic etudiants --bootstrap-server localhost:9092 --from-beginning
{"firstName": "Alice", "lastName": "Dupont", "age": 22, "engineeringDegree": "Computer Science"}
{"firstName": "Bob", "lastName": "Martin", "age": 25, "engineeringDegree": "Mechanical Engineering"}

```

## Questions de synthèse

- **Différence entre une partition et un topic :**
    
    Topic : Un topic est un canal logique dans Kafka où les messages sont envoyés et stockés. C'est la structure principale dans Kafka qui regroupe les messages.
    
    Partition : Un topic peut être divisé en plusieurs partitions. Une partition est une unité de stockage physique qui permet la distribution des messages sur plusieurs serveurs. Chaque partition est ordonnée et contient une séquence de messages.
    
- **À quoi sert un consumer group ?**
    
    Un consumer group permet à plusieurs consommateurs de lire des messages à partir d'un même topic de manière parallèle, mais en assurant que chaque message est consommé par un seul consommateur dans le groupe. Il aide à distribuer la charge et à garantir une consommation efficace des messages.
    
- **Avantages de Docker pour le développement avec Kafka** :
    
    Facilité de déploiement : Docker permet de déployer rapidement un environnement Kafka sans avoir à configurer manuellement chaque composant.
    
    Portabilité : Les conteneurs Docker peuvent être exécutés sur n'importe quel environnement, ce qui rend l'application Kafka facilement transférable.
    
    Isolation : Chaque service Kafka (comme le broker, ZooKeeper, ou UI) peut être exécuté dans des conteneurs séparés, offrant ainsi une meilleure gestion des dépendances et de la configuration.
    
- **Que se passe-t-il si on a plus de consommateurs que de partitions dans un groupe ?**
    
    Si un groupe de consommateurs a plus de consommateurs que de partitions, certains consommateurs resteront inactifs. En effet, chaque partition peut être consommée par un seul consommateur à la fois. Les consommateurs excédentaires ne traiteront aucun message.
    
- **Comment Kafka garantit-il l’ordre des messages ?**
    
    Kafka garantit l'ordre des messages au sein d'une partition. Les messages dans une partition sont reçus et consommés dans l'ordre dans lequel ils sont produits. Cependant, Kafka ne garantit pas l'ordre entre les partitions.

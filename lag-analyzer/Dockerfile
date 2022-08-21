FROM azul/zulu-openjdk:11

ENV SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/Anomaly_Detection"

COPY target/lag-analyzer-0.0.1-SNAPSHOT.jar /lag-analyzer.jar

COPY ./truststore/kafka_broker.truststore.jks ./truststore/kafka_broker.truststore.jks

CMD ["java", "-jar", "lag-analyzer.jar"]

FROM azul/zulu-openjdk:11

EXPOSE 8081

ENV SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/Anomaly_Detection"

COPY target/interactions-0.0.1-SNAPSHOT.jar /interactions.jar

COPY ./truststore/kafka_broker.truststore.jks ./truststore/kafka_broker.truststore.jks

CMD ["java", "-jar", "interactions.jar"]

FROM azul/zulu-openjdk:11

ENV SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/Anomaly_Detection"

COPY target/alert-processor-0.0.1-SNAPSHOT.jar /alert-processor.jar

COPY ./truststore/kafka_broker.truststore.jks ./truststore/kafka_broker.truststore.jks

CMD ["java", "-jar", "alert-processor.jar"]

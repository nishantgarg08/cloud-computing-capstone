FROM openjdk:16
COPY consume2/target/consume2-0.0.1-SNAPSHOT.jar /consumer.jar
COPY ArrowConverter/target/ArrowConverter-2.5.3.jar /arrow-converer.jar
COPY HotPartitionDetector/target/HotPartitionDetector-0.0.1-SNAPSHOT.jar /hot-partion-detector.jar
COPY producer/target/producer-2.0.0.RELEASE.jar /producer.jar
COPY monitoring/target/monitoring-1.0-SNAPSHOT-jar-with-dependencies.jar /monitoring.jar
CMD ["bash"]
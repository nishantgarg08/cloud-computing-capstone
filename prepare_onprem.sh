KAFKA_SETUP=ec2-34-228-141-63.compute-1.amazonaws.com
PRODUCER_SETUP=2.2.2.2
CONSUMER_SETUP=3.3.3.3
AROOW_HOT_SETUP=ec2-34-207-148-8.compute-1.amazonaws.com

mvn install
mkdir -p ./final
cp producer/target/producer-2.0.0.RELEASE.jar ./final/producer.jar
cp consume2/target/consume2-0.0.1-SNAPSHOT.jar ./final/consumer.jar
cp ArrowConverter/target/ArrowConverter-2.5.3.jar ./final/arrow-converer.jar
cp HotPartitionDetector/target/HotPartitionDetector-0.0.1-SNAPSHOT.jar ./final/hot-partion-detector.jar


echo "========= Producer Command =========="
java --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-modules=jdk.incubator.vector -Dspring.kafka.bootstrap-servers=ec2-34-228-141-63.compute-1.amazonaws.com:9092 -Dapp.topic.messageCount=10000 -Dapp.topic.sleep=5000 -jar producer.jar

echo "============= Hot Partion and Arrow Converer Command ============="

java  --add-opens  java.base/java.lang=ALL-UNNAMED --add-opens  java.base/java.nio=ALL-UNNAMED  --add-modules=jdk.incubator.vector  -Dspring.kafka.bootstrap-servers=ec2-3-90-104-171.compute-1.amazonaws.com:9092 -Darrow.batch.chunksize=5120 -Darrow.batch.milliseconds=100 -jar arrow-converer.jar

java --add-opens  java.base/java.lang=ALL-UNNAMED  --add-opens java.base/java.nio=ALL-UNNAMED --add-modules=jdk.incubator.vector -Dspring.kafka.bootstrap-servers=ec2-3-90-104-171.compute-1.amazonaws.com:9092 -Dserver.port=8081 -Dapp.service.arrow=localhost:8080 -jar hot-partion-detector.jar

echo "============= Consumer Setup  ============="
java  --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-modules=jdk.incubator.vector  -Dspring.kafka.bootstrap-servers=ec2-34-228-141-63.compute-1.amazonaws.com:9092 -Dapp.service.consumer=ec2-54-92-214-171.compute-1.amazonaws.com:8080 -Dapp.service.hotpartion=ec2-34-207-148-8.compute-1.amazonaws.com:8081 -jar consumer.jar
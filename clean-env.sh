kubectl delete -f k8s-deployment.yaml
# Delete the topics
kubectl -n apps run cleanup-person-info -ti --image=quay.io/strimzi/kafka:0.24.0-kafka-2.8.0 --rm=true --restart=Never -- bin/kafka-topics.sh --delete --topic person-info --bootstrap-server my-cluster-kafka-bootstrap.kafka.svc.cluster.local:9092
kubectl -n apps run cleanup-person-info-arrow -ti --image=quay.io/strimzi/kafka:0.24.0-kafka-2.8.0 --rm=true --restart=Never -- bin/kafka-topics.sh --delete --topic person-info-arrow --bootstrap-server my-cluster-kafka-bootstrap.kafka.svc.cluster.local:9092

sleep 10

# Install
kubectl create -f k8s-deployment.yaml

ps -ef | grep kubectl |  awk '{print $2}' | xargs kill -9

sleep 30
kubectl port-forward deployment/sender 9080:8080 -n apps &
kubectl port-forward deployment/hp-detector 9081:8080 -n apps &
kubectl port-forward deployment/arrow-writer 9082:8080 -n apps &
kubectl port-forward deployment/consumer 9083:8080 -n apps &

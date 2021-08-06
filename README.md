# kafka-hot-partition

# Start the Kafka Instance 

https://strimzi.io/quickstarts/

# Deployment

Change k8s-deployment.yaml to replace the kafka instance

```aspectj
kubectl create -f k8s-deployment.yaml
```
# Export the pots

### Sender
```aspectj
kubectl port-forward deployment/sender 9080:8080 -n apps
```
### Hot Partition Detector 
```aspectj
kubectl port-forward deployment/hp-detector 9081:8080 -n apps
```

### Arrow Message generator 
```aspectj
kubectl port-forward deployment/arrow-writer 9082:8080 -n apps
```

### Consumer 
```aspectj
kubectl port-forward deployment/consumer 9083:8080 -n apps
```


# EndPoints 

## Sender 
- To start the load ( GET )
```aspectj
http://localhost:9080/sender/startTest?start=true
```

- To stop the load ( GET )
```aspectj
http://localhost:9080/sender/startTest?start=false
```

- To adjust the message Count ( GET )
```aspectj
http://localhost:9080/sender/startTest?start=true&messageCount=20
```

## Hot Partition Detector
- Consumer Registering with Hot Partition detector ( POST )
```aspectj
http://localhost:9081/partition/register
```
Body
```aspectj
{
    "consumerId":"consumer-1627655537227-1-fc986e43-bad2-4370-8e2e-dcf8af6dce1b",
    "serviceUrl":"localhost:8080"
}
```

- Notify Hot Partition to Arrow Message genrator and Consumer ( GET )

This indicates partition 0 is hot .. Topic name is hardcoded for now 

```aspectj
http://localhost:9081/partition/notify/0?hotStatus=true
```

## Arrow Message Generator 
- Notification for arrow message converter ( POST )
```aspectj
http://localhost:9082/arrow/writer
```
BODY

```aspectj
{
   "offsetNumber":83080,
   "partitionId":0,
   "hotStatus":true
}
```
## Consumer

- State of the Consumer ( GET ) 
```aspectj
http://localhost:9083/consumer/state
```

Sample Output
```aspectj
ConsumerState1{
normal={records=10401113, result=5204500, totalMsgDeliveryDelay(s)=342258841.5433361, maxMsgDeliveryDelay(s)=63.343083378, msgProcessingTime(ms)=856.273463999764, totalReceiveTime(ms)=142557.94081, Throughput(messages/sec)=72961}, 
arrow={records=13135638, arrowRecords =2946, result=6564963, totalMsgDeliveryDelay(s)=1251813495.1611853, maxMsgDeliveryDelay(s)=122.396728841, msgProcessingTime(ms)=406.2276730000006, totalReceiveTime(ms)=106352.597518, Throughput(messages/sec)=123510}, 
total={records=23536751, result=11769463, totalMsgDeliveryDelay(s)=1594072336.7049286, maxMsgDeliveryDelay(s)=122.396728841, msgProcessingTime(ms)=1262.5336199997662, totalReceiveTime(ms)=106353.014114, Throughput(messages/sec)=221309}, 
partition={0={number=0, normalReceiverOffset=10401112, arrowStartOffset=10401113, isHot=true}}}
```


- Notification EndPoint for the Consumer that some Hot Partition is detected ( GET )
  This indicates partition 0 is hot .. Topic name is hardcoded for now

```aspectj
http://localhost:9083/partition/notify/0?hotStatus=true
```

# Demo Scenario
- See the dashboard Status
  http://localhost:9083/consumer/state

- Start the sender and start with 10000 messages
  
  http://localhost:9080/sender/startTest?start=true&messageCount=10000

- Simulate this with Hot 

  http://localhost:9081/partition/notify/0?hotStatus=true

- Stop the load

http://localhost:9080/sender/startTest?start=false


# Metrics

- records : Total number of message processed
- totalMsgDeliveryDelay (s): Total Message Delivery delay in sec. Mainly the network time is calculated here. Its the difference between message receive time and message creation time 
- maxMsgDeliveryDelay (s) : Maximum Message delivery time in sec 
- msgProcessingTime (ms) : Message Operation Processing time in ms ( its cumulative ) 
- receiveTime (ms) : Total time spent since first receive of message ( in ms )
- result : result of the operation
- throughput (number of messages/sec) : records/receiveTime


# Test in CUDA ( GPU based machines on AWS )

- Book a AWS EC2  ( p4.xlarge )
- Install kubectl https://docs.aws.amazon.com/eks/latest/userguide/install-kubectl.html
- Configure my-cluster
  aws eks --region eu-west-2 update-kubeconfig --name my-cluster
- Port forward the services  
  kubectl port-forward svc/my-cluster-kafka-bootstrap 8092:9092 -n kafka &
  kubectl port-forward svc/consumer -n apps 8091:8080 &
- Run the Cuda processor like this way
  python3 cuda-processor.py localhost:8092 http://localhost:8091/consumer/cuda

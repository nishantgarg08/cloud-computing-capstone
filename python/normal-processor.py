import json
import sys

import time

import requests
from kafka import KafkaConsumer

from commonUtility import getPartitionStatus
class PythonNormal:
    def __init__(self, records=0, result=0, totalMsgDeliveryDelay=0, maxMsgDeliveryDelay=0, msgProcessingTime=0,
                 receiveTime=0, recordStartTime =0):
        self.records = records
        self.result = result
        self.totalMsgDeliveryDelay = totalMsgDeliveryDelay
        self.maxMsgDeliveryDelay = maxMsgDeliveryDelay
        self.msgProcessingTime = msgProcessingTime
        self.receiveTime = receiveTime
        self.recordStartTime = recordStartTime

    # getter method
    def get_records(self):
        return self.records

    # setter method
    def increment_records(self, x):
        self.records = self.records + x

    # getter method
    def get_result(self):
        return self.result

    # setter method
    def increment_result(self, x):
        self.result = self.result + x

    # getter method
    def get_totalMsgDeliveryDelay(self):
        return self.totalMsgDeliveryDelay

    # setter method
    def increment_totalMsgDeliveryDelay(self, x):
        self.totalMsgDeliveryDelay = self.totalMsgDeliveryDelay + x

    # getter method
    def get_maxMsgDeliveryDelay(self):
        return self.maxMsgDeliveryDelay

    # setter method
    def set_maxMsgDeliveryDelay(self, x):
        if self.maxMsgDeliveryDelay == 0:
            self.maxMsgDeliveryDelay = x
        elif x > self.maxMsgDeliveryDelay :
            self.maxMsgDeliveryDelay = x

    # getter method
    def get_msgProcessingTime(self):
        return self.msgProcessingTime

    # setter method
    def increment_msgProcessingTime(self, x):
        self.msgProcessingTime = self.msgProcessingTime + x

    # getter method
    def get_receiveTime(self):
        return self.receiveTime

    # setter method
    def set_receiveTime(self, x):
        self.receiveTime = x

    def setRecordStartTime(self, x):
        self.recordStartTime = x



headers = {
    'Content-type':'application/json',
    'Accept':'application/json'
}
def postRecords(records):
    url = sys.argv[2]
    print(records)
    rec = json.dumps(records.__dict__)
    r = requests.post(url, data=rec, headers = headers)
    if r.status_code != 200:
        print("Problem on POST data to Java Consumer "+ r.json())

def getTime():
    #return time.time_ns()
    return round(time.time() * 1000)

if __name__ == '__main__':
    bootstrap_servers1 = sys.argv[1]
    consumer = KafkaConsumer(
        'person-info',
        bootstrap_servers=sys.argv[1],
        auto_offset_reset='earliest',
        enable_auto_commit=True,
        group_id='python-normal-consumer'+str(getTime()))
    startRecvTime = getTime()
    records = PythonNormal()
    totalSalary =0
    for message in consumer:
        status = getPartitionStatus()
        if not status:
            if totalSalary == 0:
                records.setRecordStartTime(getTime())
            val = message.value
            Y = json.loads(val)
            records.increment_records(1)
            #deliveryTime = (getTime() - Y['creationTime']/1000000)
            #records.increment_totalMsgDeliveryDelay(deliveryTime)
            #records.set_maxMsgDeliveryDelay(deliveryTime)
            operationTimeStart = getTime()
            if Y['gender'] == "F":
                records.increment_result(1)
            totalSalary = totalSalary + Y['salary']
            records.increment_msgProcessingTime((getTime() - operationTimeStart)/1000)
            #records.set_receiveTime((getTime() - startRecvTime)/1000000)
            postRecords(records)

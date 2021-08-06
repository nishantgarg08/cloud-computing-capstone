import io
import json
import os
import sys

#import pyarrow as pa
#import pyarrow.compute as pc
import pyarrow.ipc as paipc
import time
import numpy as np
import requests
from kafka import KafkaConsumer
from numba import cuda
class CudaRecords:
    def __init__(self, records=0, result=0, totalMsgDeliveryDelay=0, maxMsgDeliveryDelay=0, msgProcessingTime=0,
                 receiveTime=0):
        self.records = records
        self.result = result
        self.totalMsgDeliveryDelay = totalMsgDeliveryDelay
        self.maxMsgDeliveryDelay = maxMsgDeliveryDelay
        self.msgProcessingTime = msgProcessingTime
        self.receiveTime = receiveTime

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

blocks_per_grid = 10
threads_per_block = 512
def decode(msg_value):
    message_bytes = io.BytesIO(msg_value)
    reader = paipc.RecordBatchStreamReader(message_bytes)
    record_batch = reader.read_next_batch()
    results = record_batch.to_pandas()
    return results

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
        print("ArrowReceiver : Problem on POST data to Java Consumer "+ r.json())

def getTime():
    #return time.time_ns()
    return time.time()
@cuda.jit
def gpu_sum_kernel(x, result, out):
    idx = cuda.grid(1)
    out[idx] = x[idx] + result[idx]


def batch_add(d_a, d_b):
    # create output data on the device
    # we decide to use 32 blocks, each containing 128 threads
    gpu_sum_kernel[blocks_per_grid, threads_per_block](d_a, d_b, d_out)
    # wait for all threads to complete
    cuda.synchronize()
    # copy the output array back to the host system
    # and print it
    return d_out


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    employees = 0
    total_salary = 0
    startRecvTime = 0
    consumer = KafkaConsumer(
            'person-info-arrow',
             bootstrap_servers=sys.argv[1],
             auto_offset_reset='earliest',
             enable_auto_commit=True,
             group_id='cuda-processor')
    if employees ==0 :
        startRecvTime = getTime()
    records = CudaRecords()
    totalSalary = 0
    b = np.zeros(blocks_per_grid * threads_per_block)
    for message in consumer:
       if employees ==0 :
           print("Start Consuming Messages")
       message = decode(message.value)
       deliveryTime = (getTime() - message['time']/1000000000)
       records.increment_totalMsgDeliveryDelay(np.sum(deliveryTime))
       records.set_maxMsgDeliveryDelay(np.max(deliveryTime))
       operationTimeStart = getTime()
       records.increment_result(1)
       a = message['salary']
       a = np.pad(a, (0, 5120 - len(a)), 'constant')
       d_a = cuda.to_device(a)
       d_b = cuda.to_device(b)
       d_out = cuda.device_array_like(d_a)
       d_b = batch_add(d_a, d_b)
       records.increment_msgProcessingTime(np.sum((getTime() - operationTimeStart)) * 1000)
       records.set_receiveTime((getTime() - startRecvTime) / 1000000)
       postRecords(records)
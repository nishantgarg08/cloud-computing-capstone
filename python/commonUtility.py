import requests
URL = 'http://localhost:5000'
def getValue(param):
    r = requests.get(url=URL+param)
    return r.json()

def getPartitionStatus():
    return getValue("/consumer/partition/0")
import csv
import random
import json
import time

from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
from time import sleep
from datetime import date, datetime


CLIENT_NAME = "HW12"
TOPIC = "HW12/1"

# Broker path is under AWS IoT > Settings (at the bottom left)
BROKER_PATH = "a3uogk40zc917y-ats.iot.us-east-1.amazonaws.com"

# RSA 2048 bit key: Amazon Root CA 1 found here:
# https://docs.aws.amazon.com/iot/latest/developerguide/managing-device-certs.html
ROOT_CA_PATH = './AmazonRootCA1.pem'
PRIVATE_KEY_PATH = './b912f70074-private.pem.key'
CERTIFICATE_PATH = './b912f70074-certificate.pem.crt'

IoTclient = AWSIoTMQTTClient(CLIENT_NAME)
IoTclient.configureEndpoint(BROKER_PATH, 8883)
IoTclient.configureCredentials(
    ROOT_CA_PATH, 
    PRIVATE_KEY_PATH, 
    CERTIFICATE_PATH
)

# Allow the device to queue infinite messages
IoTclient.configureOfflinePublishQueueing(-1)

# Number of messages to send after a connection returns
IoTclient.configureDrainingFrequency(2)  # 2 requests/second

# How long to wait for a [dis]connection to complete (in seconds)
IoTclient.configureConnectDisconnectTimeout(10)

# How long to wait for publish/[un]subscribe (in seconds)
IoTclient.configureMQTTOperationTimeout(5) 


IoTclient.connect()
IoTclient.publish(TOPIC, "connected", 0)

with open('multi-sensor-data.csv', newline='') as csvfile:
    csv.reader(csvfile, delimiter=',', quotechar='"')
    next(csvfile)
    for row in csvfile:
        time.sleep(1)
        data = row.split(',')
        i=0
        while i < len(data):
            data[i]=data[i].replace('"','')
            data[i]=data[i].replace('\n','')
            data[i]=data[i].replace('\r','')
            i=i+1
        payload = json.dumps({
            "name": data[0],
            "temp": data[1],
            "humidity": data[2],
            "luminosity": data[3],
            "motion": data[4]
        })
        IoTclient.publish(TOPIC, payload, 0)

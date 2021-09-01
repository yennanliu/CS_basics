
class Consumer:

    def __init__(self, cfg):

        self.cfg = cfg

    # https://www.cloudiqtech.com/how-to-build-real-time-streaming-data-pipelines-and-applications-using-apache-kafka/
    def consumer_from_kafka(self, bootstrap_servers, topic, auto_offset_setting='earliest'):
        from kafka import KafkaConsumer
        consumer = KafkaConsumer(bootstrap_servers=['localhost:9092'], auto_offset_reset=auto_offset_setting)
        consumer.subscribe([topic])
        for msg in consumer:
            # consume
            print (msg)
            # or send to somewhere


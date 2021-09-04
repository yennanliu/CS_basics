
"""
class consum stream data
"""
class Consumer:

    def __init__(self, cfg):

        self.cfg = cfg

    # https://www.cloudiqtech.com/how-to-build-real-time-streaming-data-pipelines-and-applications-using-apache-kafka/
    def consume_from_kafka(self, bootstrap_servers, topic, auto_offset_setting='earliest'):
        from kafka import KafkaConsumer
        consumer = KafkaConsumer(bootstrap_servers=['localhost:9092'], auto_offset_reset=auto_offset_setting)
        consumer.subscribe([topic])
        for msg in consumer:
            # consume
            print (msg)
            # or send to somewhere

    # https://github.com/yennanliu/utility_Python/blob/master/stream/load_multiple_stream.py
    # https://realpython.com/python-zip-function/
    # TODO : validate if correct
    def consume_from_text(self, taxt_file_list):
        cache = [[] for i in range(len(taxt_file_list))]
        for file in taxt_file_list:
            cache.append(open(file, 'r'))
        for line in zip(item.readlines() for item in cache)
            print (line)





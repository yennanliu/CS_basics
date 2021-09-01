
class Producer:

    def __init__(self, cfg):
        self.cfg = cfg

    # https://www.cloudiqtech.com/how-to-build-real-time-streaming-data-pipelines-and-applications-using-apache-kafka/
    def produce_to_txt(self, stream, txt_file):
        with open(txt_file) as f:
            lines = f.readlines()
            for line in lines:
                f.write()

    def produce_to_kafka(self, stream, bootstrap_servers, topic):
        from kafka import KafkaProducer
        producer = KafkaProducer(bootstrap_servers=['localhost:9092'])
        with open(txt_file) as f:
            lines = f.readlines()
            for line in lines:
                producer.send(topic, line)
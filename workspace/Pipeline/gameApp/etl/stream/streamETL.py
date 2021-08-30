"""
class extract stream event data form API endpoint / stream event files

https://2.python-requests.org/en/master/user/advanced/#body-content-workflow

"""

import requests

class StreamETL:

    def __init__(self, cfg):
        self.cfg = cfg
        self.stream_file = "stream.txt"
        self.end_point = "https://myAwesomeApi/v1/events"

    # https://stackoverflow.com/questions/57497833/python-requests-stream-data-from-api
    def get_stream():
        s = requests.Session()
        with s.get(self.end_point, headers=None, stream=True) as response:
            for line in response.lines():
                if line:
                    #print (line)
                    #return line
                    self.save_stream(line)

    def save_stream(data_event):
        with open(self.stream_file) as f:
            try:
                for line in f:
                    f.write(line + "\n")
            except Exception as e:
                print ("save_stream failed")
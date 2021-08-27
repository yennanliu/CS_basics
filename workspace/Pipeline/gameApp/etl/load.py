import json

class Load:

    def __init__(self, cfg):
        self.cfg = cfg

    # TODO : try use yield
    # TODO : consider scalability
    def load_txt_data(self, txt_path):
        r = []
        with open(txt_path, 'r') as f:
            for line in f:
                r.append(line)
        return r

    # TODO : try use yield
    def load_json_data(self, json_path):
        r = []
        with open(json_path, 'r') as f:
            for line in f:
                data = json.loads(line)
                r.append(r)
        return r
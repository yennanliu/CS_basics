"""
class for transform/clean raw data
"""
class Transform:

    def __init__(self, cfg)
        self.cfg = cfg

    def get_schema(self):
       fields = [
        'timestamp',
        'user_id',
        'event_type',
        'transaction_id',
        'platform',
        'os',
        'version'
       ]
       return fields

    def get_year(self):
        pass

    def get_month(self):
        pass

    def get_day(self):
        pass

    def validate(self, json_data):
        if json_data['id'] < 0:
            json_data['validated'] = False
        if json_data['timestamp'] < 0:
            json_data['validated'] = False
        return json_data

    def normalize(self, json_data):
        json_data['year'] = get_year(json_data['timestamp'])
        json_data['month'] = get_month(json_data['timestamp'])
        json_data['day'] = get_day(json_data['timestamp'])
        return json_data

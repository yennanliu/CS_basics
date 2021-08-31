"""
class for load (save) transformed data into DB
"""

import db_client

class Load:

    def __init__(self, cfg):
        self.cfg = cfg
        self.BATCH_SIZE = 100

        self.db_client = db_client(cfg)

    def set_db_conn(self):
        db_conn = {'db': 'some_db', 'password': 'some_pwd', 'db_url': 'db_url'}
        return self.db_client.connect(db_conn)

    def insert_to_db(self, data):
        insert_status = False
        try:
            for row in data:
                self.db_client.insert(row)
        except Exception as e:
            print ("insert failed! " + str(e))
        if insert_status:
            self.db_client.commit()
            print ("isert OK")
        self.db_client.close()
 
     def insert_to_db_batch(self, data):
        insert_status = False
        batch_counts = (len(data) // self.BATCH_SIZE)  + 1
        start = end = 0
        try:
            for batch in range(batch_counts):
                start = batch
                end += (start + batch)
                batch_data = data[start:end]
                self.db_client.commit()
        except Exception as e:
            print ("batch isert failed! " + str(e))
        if insert_status:
            print ("batch isert OK")   
        self.db_client.close()
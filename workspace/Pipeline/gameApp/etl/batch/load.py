"""
class for load (save) transformed data into DB
"""
class Load:

    def __init__(self, cfg):
        self.cfg = cfg
        self.db_conn = set_db_conn()
        self.BATCH_SIZE = 100

    def set_db_conn(self):
        db_conn = {'db': 'some_db', 'password': 'some_pwd', 'db_url': 'db_url'}
        return db_conn

    def insert_to_db(self, data):
        db_client = DB(self.db_conn)
        try:
            for row in data:
                db_client.insert(row)
        except Exception as e:
            print ("isert failed! " + str(e))
        print ("isert OK")
 
     def insert_to_db_batch(self, data):
        db_client = DB(self.db_conn)
        batch_counts = (len(data) // self.BATCH_SIZE)  + 1
        start = end = 0
        try:
            for batch in range(batch_counts):
                start = batch
                end += (start + batch)
                batch_data = data[start:end]
                db_client.insert(row)
        except Exception as e:
            print ("batch isert failed! " + str(e))
        print ("batch isert OK")       
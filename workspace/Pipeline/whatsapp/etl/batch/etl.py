"""
https://github.com/yennanliu/utility_Python/blob/master/postgre/PostgreIO.py
"""

import logging

class ETL:

    def __init__(self):

        self.cursor = cursor
        self.conn = conn

        self.join_base_sql = """SELECT a.*, b.prod_id FROM base_table a INNER JOIN {table_b} b ON a.prod_id = b.prod_id"""
        self.transform_base_sql = """SELECT *, upper(customer_code) AS customer_code_ FROM {stg_table}"""
        self.union_base_sql = """SELECT table_a.* FROM {table_a} UNION ALL SELECT table_b.* FROM {table_b}"""

    def join(self, to_join_table):

        join_base_sql = self.join_base_sql.format(to_join_table)

        try:
            self.cursor.execute(join_base_sql)
        except Exception as e:
            logging.info("join failed " + str(e))

    def transform(self, stg_table):

        transform_base_sql = self.transform_base_sql.format(stg_table)

        try:
            self.cursor.execute(transform_base_sql)
        except Exception as e:
            logging.info("transform failed " + str(e))

    def union(self, table_a, table_b):

        union_base_sql = self.union_base_sql.format(table_a, table_b)

        try:
            self.cursor.execute(union_base_sql)
        except Exception as e:
            logging.info("union failed " + str(e))

    def lookup(self):

        pass

    def insert(self):

        pass

    def run(self):

        pass
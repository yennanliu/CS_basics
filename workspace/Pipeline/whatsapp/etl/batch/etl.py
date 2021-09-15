"""
https://github.com/yennanliu/utility_Python/blob/master/postgre/PostgreIO.py
"""

import logging

class ETL:

    def __init__(self):

        self.cursor = cursor
        self.conn = conn

        self.insert_sql = """INSERT INTO {table_name}"""
        self.join_base_sql = """SELECT a.*, b.prod_id FROM base_table a INNER JOIN {table_b} b ON a.prod_id = b.prod_id"""
        self.transform_base_sql = """SELECT *, upper(customer_code) AS customer_code_ FROM {stg_table}"""
        self.union_base_sql = """SELECT table_a.* FROM {table_a} UNION ALL SELECT table_b.* FROM {table_b}"""
        self.lookup_base_sql = None
        self.insert_base_sql = """INSERT INTO {dest_table} FROM SELECT * FROM {lookup_table} WHERE {where_condition}"""

    def join(self, to_join_table, dest_table):

        join_base_sql = self.insert_sql.format(dest_table) + self.join_base_sql.format(to_join_table)

        try:
            self.cursor.execute(join_base_sql)
        except Exception as e:
            logging.info("join failed " + str(e))

    def transform(self, stg_table, dest_table):

        transform_base_sql = self.insert_sql.format(dest_table) + self.transform_base_sql.format(stg_table)

        try:
            self.cursor.execute(transform_base_sql)
        except Exception as e:
            logging.info("transform failed " + str(e))

    def union(self, table_a, table_b, dest_table):

        union_base_sql = self.insert_sql.format(dest_table) + self.union_base_sql.format(table_a, table_b)

        try:
            self.cursor.execute(union_base_sql)
        except Exception as e:
            logging.info("union failed " + str(e))

    def lookup(self, dest_table, dest_table):

        lookup_base_sql =  self.insert_sql.format(dest_table) + self.lookup_base_sql

        try:
            self.cursor.execute(lookup_base_sql)
        except Exception as e:
            logging.info("lookup failed " + str(e))

    def insert(self, dest_table, look_table, where_condition):

        insert_base_sql =  self.insert_base_sql.format(dest_table, look_table, where_condition)

        try:
            self.cursor.execute(insert_base_sql)
        except Exception as e:
            logging.info("insert failed " + str(e))

    def run(self, to_join_table, dest_table_1, stg_table, dest_table_2, table_a, table_b, dest_table_3, dest_table_4, dest_table_5, look_table, where_condition):

        # join -> transform -> union -> lookup -> insert
        join(to_join_table, dest_table_1)
        transform(stg_table, dest_table_2)
        union(table_a, table_b, dest_table_3)
        lookup(dest_table_4)
        insert(dest_table_5, look_table, where_condition)
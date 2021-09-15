from abc import ABC, abstractmethod

class ETLFactory(ABC):

    @abstractmethod
    def join(self, to_join_table, dest_table):
        pass

    @abstractmethod
    def transform(self, stg_table, dest_table):
        pass

    @abstractmethod
    def union(self, table_a, table_b, dest_table):
        pass

    @abstractmethod
    def lookup(self, dest_table, dest_table):
        pass

    @abstractmethod
    def insert(self, dest_table, look_table, where_condition):
        pass

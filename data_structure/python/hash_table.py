##################################################################
# DATA STRUCTURE DEMO : Hash table 
##################################################################

# V0 
class Node:
    def __init__(self, key, value):
        self.key = key
        self.value = value
        
class HashTable:
    def __init__(self, init_size, hash=hash):
        self.__slot = [[] for _ in range(init_size)]
        self.__size = init_size
        self.hash = hash
 
    def put(self, key, value):
        """
        * variable
        : key :  put/remove key-value's key 
        : value : put/remove key-value's value
        : address : the hashed "key" in hashmap

        * transfromation
        : hash transfromation : hash(value)/self.size
        """
        node = Node(key, value)
        # get hashmap index (address) via hash func
        address = self.hash(node.key) % self.__size
        self.__slot[address].append(node)
 
    def get(self, key):
        # get the address in hashmap from "hashed" key
        address = self.hash(key) % self.__size
        for node in self.__slot[address]:
            if node.key == key:
                return node.value
        return None
 
    def remove(self, key):
        # get hashmap index (address) via hash func
        address = self.hash(key) % self.__size
        for idx, node in enumerate(self.__slot[address].copy()):
            if node.key == key:
                self.__slot[address].pop(idx)    

# map = HashTable(5)
# print (map)
# print ('*** put into hashmap')
# for i in range(5):
#     map.put(i, i)
# print ('*** get from hashmap')
# for i in range(5):
#     print (map.get(i)) 
# print ('*** remove from hashmap')
# for i in range(5):
#     (map.remove(i))
# print ('*** get from hashmap')
# for i in range(5):
#     print (map.get(i)) 

# V1 
# http://zhaochj.github.io/2016/05/16/2016-05-16-%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84-hash/
class Node:
    def __init__(self, key, value):
        self.key = key
        self.value = value

class HashTable:
    def __init__(self, init_size, hash=hash):
        self.__slot = [[] for _ in range(init_size)]
        #self.__slot = []
        #for _ in range(init_size):
        #   self.__slot.append([])
        self.__size = init_size
        self.hash = hash
 
    def put(self, key, value):
        node = Node(key, value)
        address = self.hash(node.key) % self.__size
        self.__slot[address].append(node)
 
    def get(self, key, default=None):
        _key = self.hash(key)
        address = _key % self.__size
        for node in self.__slot[address]:
            if node.key == key:
                return node.value
        return default
 
    def remove(self, key):
        address = self.hash(key) % self.__size
        for idx, node in enumerate(self.__slot[address].copy()):
            if node.key == key:
                self.__slot[address].pop(idx)

# map = HashTable(16)
# for i in range(5):
#     map.put(i, i)
# map.remove(3)
# for i in range(5):
#     print(map.get(i, 'not set'))

# # V2 
# # https://github.com/joeyajames/Python/blob/master/HashMap.py
# class HashMap:
#     def __init__(self):
#         self.size = 6
#         self.map = [None] * self.size
        
#     def _get_hash(self, key):
#         hash = 0
#         for char in str(key):
#             hash += ord(char)
#         return hash % self.size
        
#     def add(self, key, value):
#         key_hash = self._get_hash(key)
#         key_value = [key, value]
        
#         if self.map[key_hash] is None:
#             self.map[key_hash] = list([key_value])
#             return True
#         else:
#             for pair in self.map[key_hash]:
#                 if pair[0] == key:
#                     pair[1] = value
#                     return True
#             self.map[key_hash].append(key_value)
#             return True
            
#     def get(self, key):
#         key_hash = self._get_hash(key)
#         if self.map[key_hash] is not None:
#             for pair in self.map[key_hash]:
#                 if pair[0] == key:
#                     return pair[1]
#         return None
            
#     def delete(self, key):
#         key_hash = self._get_hash(key)
        
#         if self.map[key_hash] is None:
#             return False
#         for i in range (0, len(self.map[key_hash])):
#             if self.map[key_hash][i][0] == key:
#                 self.map[key_hash].pop(i)
#                 return True
#         return False
            
#     def print(self):
#         print ('---PHONEBOOK----')
#         for item in self.map:
#             if item is not None:
#                 print (str(item))
            
# # h = HashMap()
# # h.add('Bob', '567-8888')
# # h.add('Ming', '293-6753')
# # h.add('Ming', '333-8233')
# # h.add('Ankit', '293-8625')
# # h.add('Aditya', '852-6551')
# # h.add('Alicia', '632-4123')
# # h.add('Mike', '567-2188')
# # h.add('Aditya', '777-8888')
# # h.print()       
# # h.delete('Bob')
# # h.print()
# # print('Ming: ' + h.get('Ming'))

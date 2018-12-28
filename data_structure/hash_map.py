

#################################################################
# DATA STRUCTURE DEMO : Hash map 
#################################################################

# http://zhaochj.github.io/2016/05/16/2016-05-16-%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84-hash/

class Node:
    def __init__(self, key, value):
        self.key = key
        self.value = value
 
 
class Map:
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
 
if __name__ == '__main__':
    map = Map(16)
    for i in range(5):
        map.put(i, i)
 
    map.remove(3)
 
    for i in range(5):
        print(map.get(i, 'not set'))
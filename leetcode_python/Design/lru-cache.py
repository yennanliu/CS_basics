# Design and implement a data structure for Least Recently Used (LRU) cache. It should support the following operations: get and put.
#
# get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
# put(key, value) - Set or insert the value if the key is not already present. When the cache reached its capacity, it should invalidate the least recently used item before inserting a new item.
# The cache is initialized with a positive capacity.
#
# Follow up:
# Could you do both operations in O(1) time complexity?
#
# Example:
#
# LRUCache cache = new LRUCache( 2 /* capacity */ );
#
# cache.put(1, 1);
# cache.put(2, 2);
# cache.get(1);       // returns 1
# cache.put(3, 3);    // evicts key 2
# cache.get(2);       // returns -1 (not found)
# cache.put(4, 4);    // evicts key 1
# cache.get(1);       // returns -1 (not found)
# cache.get(3);       // returns 3
# cache.get(4);       // returns 4

# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/100800072
class ListNode:
    def __init__(self, key, value):
        self.key = key
        self.value = value
        self.prev = self
        self.next = self

class LRUCache:

    def __init__(self, capacity: int):
        self.dic = dict()
        self.capacity = capacity
        self.size = 0
        self.root = ListNode(0, 0)

    def get(self, key: int) -> int:
        if key in self.dic:
            node = self.dic[key]
            self.removeFromList(node)
            self.insertIntoHead(node)
            return node.value
        else:
            return -1

    def put(self, key: int, value: int) -> None:
        if key in self.dic:
            node = self.dic[key]
            self.removeFromList(node)
            self.insertIntoHead(node)
            node.value = value
        else:
            if self.size >= self.capacity:
                self.removeFromTail()
                self.size -= 1
            node = ListNode(key, value)
            self.insertIntoHead(node)
            self.dic[key] = node
            self.size += 1

    def removeFromList(self, node):
        if node == self.root: return
        prev_node = node.prev
        next_node = node.next
        prev_node.next = next_node
        next_node.prev = prev_node
        node.prev = node.next = None
    
    def insertIntoHead(self, node):
        head_node = self.root.next
        head_node.prev = node
        node.prev = self.root
        self.root.next = node
        node.next = head_node
    
    def removeFromTail(self):
        if self.size == 0: return
        tail_node = self.root.prev
        del self.dic[tail_node.key]
        self.removeFromList(tail_node)

# V1'
# https://blog.csdn.net/laughing2333/article/details/70231547
class LRUCache(object):

    def __init__(self, capacity):
        self.capacity = capacity
        self._cache = []   
        self._cache_look_up = {}

    def get(self, key):
        if key not in self._cache_look_up:
            return -1

        self._cache.remove(key)
        self._cache.append(key)

        return self._cache_look_up[key]

    def put(self, key, value):
        if key in self._cache_look_up:
            self._cache_look_up[key] = value
            self._cache.remove(key)
            self._cache.append(key)
            return
        else:
            if len(self._cache) == self.capacity:
                del_key = self._cache[0]
                self._cache = self._cache[1:]
                del self._cache_look_up[del_key]

            self._cache.append(key)
            self._cache_look_up[key] = value

# V2 

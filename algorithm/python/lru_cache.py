#---------------------------------------------------------------
# LRU cache
#---------------------------------------------------------------

# V0
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Design/lru-cache.py
# IDEA : ARRAY + LRU (implement LRU via array)
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
        # case 1) key in cache
        if key in self._cache_look_up:
            self._cache_look_up[key] = value

            """
            NOTE !!! below trick
                In [14]: x = [1,2,3]
                In [15]: x.remove(2)
                In [16]: x
                Out[16]: [1, 3]
                In [17]: x.append(2)
                In [18]: x
                Out[18]: [1, 3, 2]
            """
            
            self._cache.remove(key)
            self._cache.append(key)
            return

        # case 2) key NOT in cache
        else:
            # case 2-1) len(cache) == capacity -> need to clear cache with LRU
            if len(self._cache) == self.capacity:
                del_key = self._cache[0]
                self._cache = self._cache[1:]
                del self._cache_look_up[del_key]

            # case 2-2) len(cache) < capacity
            self._cache.append(key)
            self._cache_look_up[key] = value

# V1
# IDEA : Ordered dictionary
# https://leetcode.com/problems/lru-cache/solution/
# IDEA : 
#       -> There is a structure called ordered dictionary, it combines behind both hashmap and linked list. 
#       -> In Python this structure is called OrderedDict 
#       -> and in Java LinkedHashMap.
from collections import OrderedDict
class LRUCache(OrderedDict):

    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.capacity = capacity

    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        if key not in self:
            return - 1
        
        self.move_to_end(key)
        return self[key]

    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: void
        """
        if key in self:
            self.move_to_end(key)
        self[key] = value
        if len(self) > self.capacity:
            self.popitem(last = False)
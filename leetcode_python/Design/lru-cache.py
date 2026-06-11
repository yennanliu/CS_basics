"""

146. LRU Cache
Medium

Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.

Implement the LRUCache class:

LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
int get(int key) Return the value of the key if the key exists, otherwise return -1.
void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.
The functions get and put must each run in O(1) average time complexity.

 

Example 1:

Input
["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
[[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
Output
[null, null, null, 1, null, -1, null, -1, 3, 4]

Explanation
LRUCache lRUCache = new LRUCache(2);
lRUCache.put(1, 1); // cache is {1=1}
lRUCache.put(2, 2); // cache is {1=1, 2=2}
lRUCache.get(1);    // return 1
lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
lRUCache.get(2);    // returns -1 (not found)
lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
lRUCache.get(1);    // return -1 (not found)
lRUCache.get(3);    // return 3
lRUCache.get(4);    // return 4
 

Constraints:

1 <= capacity <= 3000
0 <= key <= 104
0 <= value <= 105
At most 2 * 105 calls will be made to get and put.

"""


# V0
# IDEA: OrderedDict
from collections import OrderedDict

class LRUCache(object):

    def __init__(self, capacity):
        self.capacity = capacity
        self.cache = OrderedDict()

    def get(self, key):
        if key not in self.cache:
            return -1

        value = self.cache[key]

        # remove and reinsert to mark as most recently used
        del self.cache[key]
        self.cache[key] = value

        return value

    def put(self, key, value):
        if key in self.cache:
            del self.cache[key]

        self.cache[key] = value

        if len(self.cache) > self.capacity:
            # NOTE !!!
            # -> the `popitem` API
            # pop least recently used (first inserted)
            self.cache.popitem(last=False)


# V0
# IDEA:  HASHMAP + doubly linked list( most recent used + least recent used )
"""

- HashMap: key → node
- Doubly Linked List:
  - most recently used near tail
  - least recently used near head
"""
class Node:
    def __init__(self, key, val):
        self.key = key
        self.val = val
        self.prev = None
        self.next = None


class LRUCache:

    def __init__(self, capacity):
        self.capacity = capacity
        self.cache = {}

        self.head = Node(0, 0)
        self.tail = Node(0, 0)

        self.head.next = self.tail
        self.tail.prev = self.head

    # NOTE !!!  `_remove` helper func
    def _remove(self, node):
        prev = node.prev
        nxt = node.next

        prev.next = nxt
        nxt.prev = prev

    # NOTE !!!  `_insert` helper func
    def _insert(self, node):
        prev = self.tail.prev

        prev.next = node
        node.prev = prev

        node.next = self.tail
        self.tail.prev = node

    def get(self, key):
        if key not in self.cache:
            return -1

        node = self.cache[key]

        self._remove(node)
        self._insert(node)

        return node.val

    def put(self, key, value):

        if key in self.cache:
            node = self.cache[key]
            node.val = value

            self._remove(node)
            self._insert(node)
            return

        if len(self.cache) == self.capacity:
            lru = self.head.next

            self._remove(lru)
            del self.cache[lru.key]

        node = Node(key, value)

        self.cache[key] = node
        self._insert(node)


# V0-1
# IDEA:  HASHMAP + doubly linked list( most recent used + least recent used )
"""

- HashMap: key → node
- Doubly Linked List:
  - most recently used near tail
  - least recently used near head
"""
class Node:
    def __init__(self, key=0, value=0):
        self.key = key
        self.value = value
        self.prev = None
        self.next = None

class LRUCache(object):

    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.capacity = capacity
        # Map stores { key : Node_Object } for O(1) node access
        self.cache = {}
        
        # Initialize sentinel/dummy head and tail boundaries
        self.head = Node(0, 0)
        self.tail = Node(0, 0)
        self.head.next = self.tail
        self.tail.prev = self.head

    def _remove(self, node):
        """Helper to splice a node completely out of the doubly linked list"""
        prev_node = node.prev
        next_node = node.next
        prev_node.next = next_node
        next_node.prev = prev_node

    def _add_to_tail(self, node):
        """Helper to insert a node right before the tail (Most Recently Used position)"""
        prev_node = self.tail.prev
        
        prev_node.next = node
        self.tail.prev = node
        
        node.prev = prev_node
        node.next = self.tail

    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        if key in self.cache:
            node = self.cache[key]
            # Since it was accessed, move it to the MRU (tail) position
            self._remove(node)
            self._add_to_tail(node)
            return node.value
        return -1

    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: None
        """
        if key in self.cache:
            # Update case: remove old node placement
            self._remove(self.cache[key])
            
        node = Node(key, value)
        self.cache[key] = node
        self._add_to_tail(node)
        
        # Eviction case: capacity exceeded
        if len(self.cache) > self.capacity:
            # LRU node is always sitting directly right of the head sentinel node
            lru_node = self.head.next
            self._remove(lru_node)
            # Delete it from our lookup hash map
            del self.cache[lru_node.key]


# V0
# IDEA : ARRAY + LRU (implement LRU via array)
# time: O(N) for get/put, space: O(capacity)
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

# Your LRUCache object will be instantiated and called as such:
# obj = LRUCache(capacity)
# param_1 = obj.get(key)
# obj.put(key,value)


# V1'
# IDEA : Hashmap + DoubleLinkedList
# https://leetcode.com/problems/lru-cache/solution/
class DLinkedNode(): 
    def __init__(self):
        self.key = 0
        self.value = 0
        self.prev = None
        self.next = None
            
class LRUCache():
    def _add_node(self, node):
        """
        Always add the new node right after head.
        """
        node.prev = self.head
        node.next = self.head.next

        self.head.next.prev = node
        self.head.next = node

    def _remove_node(self, node):
        """
        Remove an existing node from the linked list.
        """
        prev = node.prev
        new = node.next

        prev.next = new
        new.prev = prev

    def _move_to_head(self, node):
        """
        Move certain node in between to the head.
        """
        self._remove_node(node)
        self._add_node(node)

    def _pop_tail(self):
        """
        Pop the current tail.
        """
        res = self.tail.prev
        self._remove_node(res)
        return res

    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.cache = {}
        self.size = 0
        self.capacity = capacity
        self.head, self.tail = DLinkedNode(), DLinkedNode()

        self.head.next = self.tail
        self.tail.prev = self.head
        

    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        node = self.cache.get(key, None)
        if not node:
            return -1

        # move the accessed node to the head;
        self._move_to_head(node)

        return node.value

    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: void
        """
        node = self.cache.get(key)

        if not node: 
            newNode = DLinkedNode()
            newNode.key = key
            newNode.value = value

            self.cache[key] = newNode
            self._add_node(newNode)

            self.size += 1

            if self.size > self.capacity:
                # pop the tail
                tail = self._pop_tail()
                del self.cache[tail.key]
                self.size -= 1
        else:
            # update the value.
            node.value = value
            self._move_to_head(node)

# V1''
# IDEA : OrderedDict
# https://leetcode.com/problems/lru-cache/discuss/595933/Python-OrderedDict-sol.-90%2B-w-Hint
from collections import OrderedDict
class LRUCache:

    def __init__(self, capacity: int):
        
        self.size = capacity
        
        self.lru_cache = OrderedDict()
        

    def get(self, key: int) -> int:
        
        if key not in self.lru_cache:
            return -1
        
        else:
            # refresh the entry with given key
            self.lru_cache.move_to_end( key )
            
            return self.lru_cache[ key ]
        

    def put(self, key: int, value: int) -> None:
        
        if key not in self.lru_cache:
            
            if len( self.lru_cache ) >= self.size :
                # pop the least used entry
                self.lru_cache.popitem( last = False )

        else:
            # refresh the entry with given key
            self.lru_cache.move_to_end( key )
        
        self.lru_cache[ key ] = value

# V1'''
# IDEA :  Hashmap + DoubleLinkedList
# https://blog.csdn.net/fuxuemingzhu/article/details/100800072
class ListNode:
    def __init__(self, key, value):
        self.key = key
        self.value = value
        self.prev = self
        self.next = self

class LRUCache:

    def __init__(self, capacity):
        self.dic = dict()
        self.capacity = capacity
        self.size = 0
        self.root = ListNode(0, 0)

    def get(self, key):
        if key in self.dic:
            node = self.dic[key]
            self.removeFromList(node)
            self.insertIntoHead(node)
            return node.value
        else:
            return -1

    def put(self, key, value):
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
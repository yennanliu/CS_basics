"""

460. LFU Cache
Hard

Design and implement a data structure for a Least Frequently Used (LFU) cache.

Implement the LFUCache class:

LFUCache(int capacity) Initializes the object with the capacity of the data structure.
int get(int key) Gets the value of the key if the key exists in the cache. Otherwise, returns -1.
void put(int key, int value) Update the value of the key if present, or inserts the key if not already present. When the cache reaches its capacity, it should invalidate and remove the least frequently used key before inserting a new item. For this problem, when there is a tie (i.e., two or more keys with the same frequency), the least recently used key would be invalidated.
To determine the least frequently used key, a use counter is maintained for each key in the cache. The key with the smallest use counter is the least frequently used key.

When a key is first inserted into the cache, its use counter is set to 1 (due to the put operation). The use counter for a key in the cache is incremented either a get or put operation is called on it.

The functions get and put must each run in O(1) average time complexity.

 

Example 1:

Input
["LFUCache", "put", "put", "get", "put", "get", "get", "put", "get", "get", "get"]
[[2], [1, 1], [2, 2], [1], [3, 3], [2], [3], [4, 4], [1], [3], [4]]
Output
[null, null, null, 1, null, -1, 3, null, -1, 3, 4]

Explanation
// cnt(x) = the use counter for key x
// cache=[] will show the last used order for tiebreakers (leftmost element is  most recent)
LFUCache lfu = new LFUCache(2);
lfu.put(1, 1);   // cache=[1,_], cnt(1)=1
lfu.put(2, 2);   // cache=[2,1], cnt(2)=1, cnt(1)=1
lfu.get(1);      // return 1
                 // cache=[1,2], cnt(2)=1, cnt(1)=2
lfu.put(3, 3);   // 2 is the LFU key because cnt(2)=1 is the smallest, invalidate 2.
                 // cache=[3,1], cnt(3)=1, cnt(1)=2
lfu.get(2);      // return -1 (not found)
lfu.get(3);      // return 3
                 // cache=[3,1], cnt(3)=2, cnt(1)=2
lfu.put(4, 4);   // Both 1 and 3 have the same cnt, but 1 is LRU, invalidate 1.
                 // cache=[4,3], cnt(4)=1, cnt(3)=2
lfu.get(1);      // return -1 (not found)
lfu.get(3);      // return 3
                 // cache=[3,4], cnt(4)=1, cnt(3)=3
lfu.get(4);      // return 4
                 // cache=[3,4], cnt(4)=2, cnt(3)=3
 

Constraints:

0 <= capacity <= 104
0 <= key <= 105
0 <= value <= 109
At most 2 * 105 calls will be made to get and put.

"""


# V0
# IDEA: CUSTOM CLASS + DOUBLY LINKED LIST + HASHMAP (gpt)
class MyNode(object):

    def __init__(self, k=0, v=0):

        """
        NOTE !!!


        ONLY 4 attr in the custom node class

            -> k, v, prev, next
        """

        # Store key / value directly in Node
        # HashMap will be:
        # {key: MyNode}
        self.key = k
        self.val = v

        # Doubly Linked List
        self.prev = None
        self.next = None


class LRUCache(object):

    def __init__(self, capacity):

        """
        NOTE !!!


        define 

            - values (hash map) attr in this class

            - head, tail attr in this class

                - head, tail are `dummy` nodes
        """

        """
        :type capacity: int
        """

        self.capacity = capacity

        # Number of nodes currently in cache
        self.cnt = 0

        # HashMap:
        # key -> Node
        self.values = {}

        # Dummy head / tail
        self.head = MyNode()
        self.tail = MyNode()

        self.head.next = self.tail
        self.tail.prev = self.head


    def get(self, key):
        """
        :type key: int
        :rtype: int
        """

        """
        NOTE !!!

        in py, can use `self.values` to check if key is in hash map,
        no need to use `self.values.keys()`
        """
        # Key does not exist
        if key not in self.values:
            return -1

        # Get the Node from HashMap
        node = self.values[key]

        # This node was recently used,
        # so move it to the end (MRU position)
        self.remove(node)
        self.add_to_end(node)

        return node.val


    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: None
        """

        # Case 1:
        # Key already exists
        if key in self.values:

            node = self.values[key]

            # Update value
            node.val = value

            # Move to end because it is recently used
            self.remove(node)
            self.add_to_end(node)

            return


        # Case 2:
        # Key does not exist
        node = MyNode(key, value)

        # Add to HashMap
        self.values[key] = node

        # Add to MRU position
        self.add_to_end(node)

        self.cnt += 1

        # Cache is over capacity
        if self.cnt > self.capacity:

            # head.next is the LRU node
            lru_node = self.head.next

            # Remove from Linked List
            self.remove(lru_node)

            # IMPORTANT:
            # Also remove from HashMap
            del self.values[lru_node.key]

            self.cnt -= 1


    # Helper function
    def remove(self, node):

        _prev = node.prev
        _next = node.next

        # Connect previous node to next node
        _prev.next = _next

        # Connect next node to previous node
        _next.prev = _prev


    def add_to_end(self, node):

        # Insert node right before tail
        _tail = self.tail
        _prev = self.tail.prev

        # Previous node -> new node
        _prev.next = node
        node.prev = _prev

        # New node -> tail
        node.next = _tail
        _tail.prev = node



# V0-0-1
# IDEA: CUSTOM CLASS + DOUBLY LINKED LIST + HASHMAP (gpt)
"""

1. we custom our own `ListNode` (DOUBLY LINKED LIST)
   with attr:
    
    ```
    key, val, prev, next
    ```


2. the value of hash map is `ListNode` type.
    self.k_v_map = {} 


3.  need to init head, tail as ListNode


4.  need to setup 2 helper func:

    - remove

    - add_to_tail

"""
class ListNode(object):
    def __init__(self, key=0, value=0):
        self.key = key
        self.val = value
        self.prev = None
        self.next = None


class LRUCache(object):

    def __init__(self, capacity):
        """
        :type capacity: int
        """

        # key -> node
        self.k_v_map = {}

        # Dummy head / tail
        # head <-> ... <-> tail
        self.head = ListNode()
        self.tail = ListNode()

        self.head.next = self.tail
        self.tail.prev = self.head

        self.capacity = capacity

    def get(self, key):
        """
        :type key: int
        :rtype: int
        """

        if key not in self.k_v_map:
            return -1

        node = self.k_v_map[key]

        # 被 get 代表最近使用
        self.remove(node)
        self.add_to_tail(node)

        return node.val

    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: None
        """

        # key 已經存在
        if key in self.k_v_map:
            node = self.k_v_map[key]

            # 更新 value
            node.val = value

            # 更新成 MRU
            self.remove(node)
            self.add_to_tail(node)

            return

        # 新 key
        node = ListNode(key, value)
        self.k_v_map[key] = node

        # 新 node 放到 MRU
        self.add_to_tail(node)

        # 超過 capacity
        if len(self.k_v_map) > self.capacity:

            # head 後面的就是 LRU
            lru = self.head.next

            self.remove(lru)
            del self.k_v_map[lru.key]

    def remove(self, node):
        """
        從 linked list 移除 node
        """

        prev_node = node.prev
        next_node = node.next

        prev_node.next = next_node
        next_node.prev = prev_node

    def add_to_tail(self, node):
        """
        把 node 加到 tail 前面
        => MRU
        """

        prev_node = self.tail.prev

        prev_node.next = node
        node.prev = prev_node

        node.next = self.tail
        self.tail.prev = node


# V0-0-2
from collections import OrderedDict
class Node:
    def __init__(self, key, val, count):
        self.key=key
        self.val=val
        self.count=count
# time = O(1) per get/put operation
# space = O(k), k = capacity of cache
class LFUCache:
    
    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.capacity=capacity
        self.key_node={}
        self.count_node={}
        self.minV=None
    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        if not key in self.key_node:  return -1 
        node = self.key_node[key]
        del self.count_node[node.count][key]
        if not self.count_node[node.count]:
            del self.count_node[node.count] 
        node.count+=1
        if not node.count in self.count_node:
            self.count_node[node.count]=OrderedDict()
        
        self.count_node[node.count][key]=node
        
        if not self.minV in self.count_node:
            self.minV+=1
        return node.val
    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: void
        """
        # if element exists, -> update value and count + 1 
        if self.capacity==0: return None
        if key in self.key_node:
            self.key_node[key].val=value
            self.get(key)
        else:
            if len(self.key_node) == self.capacity:
                item=self.count_node[self.minV].popitem(last=False)
                del self.key_node[item[0]]
            node=Node(key,value,1)
            self.key_node[key]=node
            if not 1 in self.count_node:
                self.count_node[1]=OrderedDict()
            
            self.count_node[1][key]=node
            self.minV=1

# V1
# https://blog.csdn.net/Neekity/article/details/84765476
from collections import OrderedDict
class Node:
    def __init__(self, key, val, count):
        self.key=key
        self.val=val
        self.count=count
# time = O(1) per get/put operation
# space = O(k), k = capacity of cache
class LFUCache:
    
    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.capacity=capacity
        self.key_node={}
        self.count_node={}
        self.minV=None
    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        if not key in self.key_node:  return -1 
        node = self.key_node[key]
        del self.count_node[node.count][key]
        if not self.count_node[node.count]:
            del self.count_node[node.count] 
        node.count+=1
        if not node.count in self.count_node:
            self.count_node[node.count]=OrderedDict()
        
        self.count_node[node.count][key]=node
        
        if not self.minV in self.count_node:
            self.minV+=1
        return node.val
    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: void
        """
        # if element exists, -> update value and count + 1 
        if self.capacity==0: return None
        if key in self.key_node:
            self.key_node[key].val=value
            self.get(key)
        else:
            if len(self.key_node) == self.capacity:
                item=self.count_node[self.minV].popitem(last=False)
                del self.key_node[item[0]]
            node=Node(key,value,1)
            self.key_node[key]=node
            if not 1 in self.count_node:
                self.count_node[1]=OrderedDict()
            
            self.count_node[1][key]=node
            self.minV=1

# V2
# https://github.com/kamyu104/LeetCode-Solutions/blob/master/Python/lfu-cache.py
# time = O(1), per operation
# space = O(k), k is the capacity of cache
import collections
class ListNode(object):
    def __init__(self, key, value, freq):
        self.key = key
        self.val = value
        self.freq = freq
        self.next = None
        self.prev = None

class LinkedList(object):
    def __init__(self):
        self.head = None
        self.tail = None

    def append(self, node):
        node.next, node.prev = None, None  # avoid dirty node
        if self.head is None:
            self.head = node
        else:
            self.tail.next = node
            node.prev = self.tail
        self.tail = node

    def delete(self, node):
        if node.prev:
            node.prev.next = node.next
        else:
            self.head = node.next
        if node.next:
            node.next.prev = node.prev
        else:
            self.tail = node.prev
        node.next, node.prev = None, None  # make node clean

class LFUCache(object):

    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.__capa = capacity
        self.__size = 0
        self.__min_freq = 0
        self.__freq_to_nodes = collections.defaultdict(LinkedList)
        self.__key_to_node = {}


    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        if key not in self.__key_to_node:
            return -1

        old_node = self.__key_to_node[key]
        self.__key_to_node[key] = ListNode(key, old_node.val, old_node.freq)
        self.__freq_to_nodes[old_node.freq].delete(old_node)
        if not self.__freq_to_nodes[self.__key_to_node[key].freq].head:
            del self.__freq_to_nodes[self.__key_to_node[key].freq]
            if self.__min_freq == self.__key_to_node[key].freq:
                self.__min_freq += 1

        self.__key_to_node[key].freq += 1
        self.__freq_to_nodes[self.__key_to_node[key].freq].append(self.__key_to_node[key])

        return self.__key_to_node[key].val


    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: void
        """
        if self.__capa <= 0:
            return

        if self.get(key) != -1:
            self.__key_to_node[key].val = value
            return

        if self.__size == self.__capa:
            del self.__key_to_node[self.__freq_to_nodes[self.__min_freq].head.key]
            self.__freq_to_nodes[self.__min_freq].delete(self.__freq_to_nodes[self.__min_freq].head)
            if not self.__freq_to_nodes[self.__min_freq].head:
                del self.__freq_to_nodes[self.__min_freq]
            self.__size -= 1

        self.__min_freq = 1
        self.__key_to_node[key] = ListNode(key, value, self.__min_freq)
        self.__freq_to_nodes[self.__key_to_node[key].freq].append(self.__key_to_node[key])
        self.__size += 1

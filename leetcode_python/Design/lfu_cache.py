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
from collections import OrderedDict
class Node:
    def __init__(self, key, val, count):
        self.key=key
        self.val=val
        self.count=count
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
# Time:  O(1), per operation
# Space: O(k), k is the capacity of cache
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

# V1
# TODO : fix this
# IDEA : DOUBLY LINKED LIST + HASH TABLE
# http://bookshadow.com/weblog/2016/11/22/leetcode-lfu-cache/
# https://www.cnblogs.com/grandyang/p/6258459.html
# class KeyNode(object):
#     def __init__(self, key, value, freq = 1):
#         self.key = key
#         self.value = value
#         self.freq = freq
#         self.prev = self.next = None

# class FreqNode(object):
#     def __init__(self, freq, prev, next):
#         self.freq = freq
#         self.prev = prev
#         self.next = next
#         self.first = self.last = None

# class LFUCache(object):

#     def __init__(self, capacity):
#         """
        
#         :type capacity: int
#         """
#         self.capacity = capacity
#         self.keyDict = dict()
#         self.freqDict = dict()
#         self.head = None

#     def get(self, key):
#         """
#         :type key: int
#         :rtype: int
#         """
#         if key in self.keyDict:
#             keyNode = self.keyDict[key]
#             value = keyNode.value
#             self.increase(key, value)
#             return value
#         return -1

#     def set(self, key, value):
#         """
#         :type key: int
#         :type value: int
#         :rtype: void
#         """
#         if self.capacity == 0:
#             return
#         if key in self.keyDict:
#             self.increase(key, value)
#             return
#         if len(self.keyDict) == self.capacity:
#             self.removeKeyNode(self.head.last)
#         self.insertKeyNode(key, value)

#     def increase(self, key, value):
#         """
#         Increments the freq of an existing KeyNode<key, value> by 1.
#         :type key: str
#         :rtype: void
#         """
#         keyNode = self.keyDict[key]
#         keyNode.value = value
#         freqNode = self.freqDict[keyNode.freq]
#         nextFreqNode = freqNode.next
#         keyNode.freq += 1
#         if nextFreqNode is None or nextFreqNode.freq > keyNode.freq:
#             nextFreqNode = self.insertFreqNodeAfter(keyNode.freq, freqNode)
#         self.unlinkKey(keyNode, freqNode)
#         self.linkKey(keyNode, nextFreqNode)

#     def insertKeyNode(self, key, value):
#         """
#         Inserts a new KeyNode<key, value> with freq 1.
#         :type key: str
#         :rtype: void
#         """
#         keyNode = self.keyDict[key] = KeyNode(key, value)
#         freqNode = self.freqDict.get(1)
#         if freqNode is None:
#             freqNode = self.freqDict[1] = FreqNode(1, None, self.head)
#             if self.head:
#                 self.head.prev = freqNode
#             self.head = freqNode
#         self.linkKey(keyNode, freqNode)

#     def delFreqNode(self, freqNode):
#         """
#         Delete freqNode.
#         :rtype: void
#         """
#         prev, next = freqNode.prev, freqNode.next
#         if prev: prev.next = next
#         if next: next.prev = prev
#         if self.head == freqNode: self.head = next
#         del self.freqDict[freqNode.freq]

#     def insertFreqNodeAfter(self, freq, node):
#         """
#         Insert a new FreqNode(freq) after node.
#         :rtype: FreqNode
#         """
#         newNode = FreqNode(freq, node, node.next)
#         self.freqDict[freq] = newNode
#         if node.next: node.next.prev = newNode
#         node.next = newNode
#         return newNode

#     def removeKeyNode(self, keyNode):
#         """
#         Remove keyNode
#         :rtype: void
#         """
#         self.unlinkKey(keyNode, self.freqDict[keyNode.freq])
#         del self.keyDict[keyNode.key]

#     def unlinkKey(self, keyNode, freqNode):
#         """
#         Unlink keyNode from freqNode
#         :rtype: void
#         """
#         next, prev = keyNode.next, keyNode.prev
#         if prev: prev.next = next
#         if next: next.prev = prev
#         if freqNode.first == keyNode: freqNode.first = next
#         if freqNode.last == keyNode: freqNode.last = prev
#         if freqNode.first is None: self.delFreqNode(freqNode)

#     def linkKey(self, keyNode, freqNode):
#         """
#         Link keyNode to freqNode
#         :rtype: void
#         """
#         firstKeyNode = freqNode.first
#         keyNode.prev = None
#         keyNode.next = firstKeyNode
#         if firstKeyNode: firstKeyNode.prev = keyNode
#         freqNode.first = keyNode
#         if freqNode.last is None: freqNode.last = keyNode

# V2
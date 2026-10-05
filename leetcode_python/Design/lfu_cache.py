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
class LFUCache(object):

    def __init__(self, capacity):
        """
        :type capacity: int
        """
        

    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        

    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: None
        """


# V0-1
# IDEA: LC 146, DOUBLE LINKED LIST + CUSTOM CLASS (gpt)
from collections import Counter


class MyNode(object):

    def __init__(self, k=0, v=0):

        self.k = k
        self.v = v

        # Current frequency
        self.freq = 1

        # Doubly Linked List
        self.next = None
        self.prev = None


class LFUCache(object):

    def __init__(self, capacity):
        """
        :type capacity: int
        """

        self.capacity = capacity

        # Number of nodes currently in cache
        self.size = 0

        # key -> MyNode
        self.values = {}

        # freq -> Doubly Linked List
        #
        # Example:
        #
        # freq = 1:
        # head <-> A <-> B <-> tail
        #
        # freq = 2:
        # head <-> C <-> D <-> tail
        self.freq_lists = {}

        # Minimum frequency currently in cache
        self.min_freq = 0


    def get(self, key):
        """
        :type key: int
        :rtype: int
        """

        # Key does not exist
        if key not in self.values:
            return -1

        node = self.values[key]

        # Increase frequency
        self.update_freq(node)

        return node.v


    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: None
        """

        # Edge case
        if self.capacity == 0:
            return


        # Case 1:
        # Key already exists
        if key in self.values:

            node = self.values[key]

            # Update value
            node.v = value

            # Updating an existing key also counts as usage
            self.update_freq(node)

            return


        # Case 2:
        # Key does not exist

        # Cache is full -> remove LFU
        if self.size >= self.capacity:

            # Get the list with minimum frequency
            freq_list = self.freq_lists[self.min_freq]

            # Within the same frequency,
            # remove the LRU node
            lru_node = freq_list.head.next

            self.remove(lru_node, self.min_freq)

            # Remove from HashMap
            del self.values[lru_node.k]

            self.size -= 1


        # Create new node
        node = MyNode(key, value)

        # Add to HashMap
        self.values[key] = node

        # New node always starts at frequency 1
        self.min_freq = 1

        # Add to frequency-1 list
        if 1 not in self.freq_lists:
            self.freq_lists[1] = self.create_list()

        self.add_to_end(node, 1)

        self.size += 1


    # --------------------------------------------------
    # Frequency update
    # --------------------------------------------------

    def update_freq(self, node):

        old_freq = node.freq
        new_freq = old_freq + 1

        # Remove from old frequency list
        self.remove(node, old_freq)

        # If old frequency was min_freq
        # and there are no more nodes in that list,
        # move min_freq forward
        if old_freq == self.min_freq:
            if self.is_empty(old_freq):
                self.min_freq = new_freq

        # Update node frequency
        node.freq = new_freq

        # Create new frequency list if necessary
        if new_freq not in self.freq_lists:
            self.freq_lists[new_freq] = self.create_list()

        # Add node to MRU position
        self.add_to_end(node, new_freq)


    # --------------------------------------------------
    # Doubly Linked List helpers
    # --------------------------------------------------

    def create_list(self):

        # Dummy head / tail
        head = MyNode()
        tail = MyNode()

        head.next = tail
        tail.prev = head

        return DoublyLinkedList(head, tail)


    def remove(self, node, freq):

        _prev = node.prev
        _next = node.next

        # Connect previous node to next node
        _prev.next = _next

        # Connect next node to previous node
        _next.prev = _prev


    def add_to_end(self, node, freq):

        freq_list = self.freq_lists[freq]

        _tail = freq_list.tail
        _prev = freq_list.tail.prev

        # Previous node -> new node
        _prev.next = node
        node.prev = _prev

        # New node -> tail
        node.next = _tail
        _tail.prev = node


    def is_empty(self, freq):

        freq_list = self.freq_lists[freq]

        return freq_list.head.next == freq_list.tail


class DoublyLinkedList(object):

    def __init__(self, head, tail):

        self.head = head
        self.tail = tail


# V0-2
# IDEA: LC 146, DOUBLE LINKED LIST + CUSTOM CLASS (GEMINI)
class Node(object):

  def __init__(self, key=0, val=0):
    self.key = key
    self.val = val
    self.freq = 1
    self.prev = None
    self.next = None


class DoublyLinkedList(object):

  def __init__(self):
    self.head = Node()
    self.tail = Node()
    self.head.next = self.tail
    self.tail.prev = self.head
    self.size = 0

  def add_to_end(self, node):
    """將節點插入至末尾 (最新使用 MRU)"""
    _prev = self.tail.prev
    _prev.next = node
    node.prev = _prev
    node.next = self.tail
    self.tail.prev = node
    self.size += 1

  def remove(self, node):
    """移除指定節點"""
    _prev = node.prev
    _next = node.next
    _prev.next = _next
    _next.prev = _prev
    self.size -= 1

  def remove_first(self):
    """移除開頭第一個節點 (最久未使用 LRU)"""
    if self.size == 0:
      return None
    first = self.head.next
    self.remove(first)
    return first

  def is_empty(self):
    return self.size == 0


class LFUCache(object):

  def __init__(self, capacity):
    """:type capacity: int"""
    self.capacity = capacity
    self.min_freq = 0
    self.key_to_node = {}  # key -> Node
    self.freq_to_dll = {}  # freq -> DoublyLinkedList

  def _update_freq(self, node):
    """輔助函式：將節點的頻率 +1 並移動至新的頻率鏈結串列中"""
    freq = node.freq
    dll = self.freq_to_dll[freq]
    dll.remove(node)

    # 若當前最低頻率的串列空了，將 min_freq 遞增
    if dll.is_empty() and freq == self.min_freq:
      self.min_freq += 1

    node.freq += 1
    new_freq = node.freq

    if new_freq not in self.freq_to_dll:
      self.freq_to_dll[new_freq] = DoublyLinkedList()

    self.freq_to_dll[new_freq].add_to_end(node)

  def get(self, key):
    """:type key: int

    :rtype: int
    """
    if key not in self.key_to_node:
      return -1

    node = self.key_to_node[key]
    self._update_freq(node)
    return node.val

  def put(self, key, value):
    """:type key: int

    :type value: int
    :rtype: None
    """
    if self.capacity == 0:
      return

    # 情況 1：Key 已存在，更新 Value 並提升頻率
    if key in self.key_to_node:
      node = self.key_to_node[key]
      node.val = value
      self._update_freq(node)
    # 情況 2：Key 不存在
    else:
      # 若達到容量上限，刪除 min_freq 串列中最久未使用的節點
      if len(self.key_to_node) >= self.capacity:
        min_dll = self.freq_to_dll[self.min_freq]
        evicted_node = min_dll.remove_first()
        if evicted_node:
          del self.key_to_node[evicted_node.key]

      # 新增新節點 (初始頻率為 1)
      new_node = Node(key, value)
      self.key_to_node[key] = new_node
      self.min_freq = 1  # 重置最小頻率為 1

      if 1 not in self.freq_to_dll:
        self.freq_to_dll[1] = DoublyLinkedList()

      self.freq_to_dll[1].add_to_end(new_node)


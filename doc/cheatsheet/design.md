# Design

## 0) Concept

### 0-1) Types
- **Data Structure Design**: Design custom data structures (Stack, Queue, HashMap, etc.)
- **Cache Design**: LRU, LFU, Time-based cache systems
- **System Component Design**: File systems, search systems, rate limiters
- **Social Network Design**: Twitter, Instagram feed, following/follower systems
- **Scheduling/Booking Design**: Calendar, meeting rooms, parking systems
- **Stream/Iterator Design**: Data stream processing, custom iterators
- **Game Design**: Tic-Tac-Toe, Snake, game boards

### 0-2) Pattern

#### Pattern 1: HashMap + LinkedList
- **Use Case**: Order-sensitive operations (LRU, LFU, insertion order)
- **Examples**: LRU Cache, LFU Cache, Insert Delete GetRandom O(1)
- **Key Point**: HashMap provides O(1) lookup, LinkedList provides O(1) ordering operations

#### Pattern 2: HashMap + Heap
- **Use Case**: Priority-based operations, top-k problems
- **Examples**: Design Twitter, Top K Frequent Elements in stream
- **Key Point**: HashMap tracks data, Heap maintains priority order

#### Pattern 3: Trie (Prefix Tree)
- **Use Case**: Autocomplete, prefix search, word validation
- **Examples**: Search Autocomplete System, Add and Search Word, Design Search System
- **Key Point**: Efficient prefix-based operations O(L) where L is word length

#### Pattern 4: OOD (Object-Oriented Design)
- **Use Case**: Complex system with multiple components and interactions
- **Examples**: Parking Lot, Elevator System, Library Management
- **Key Point**: Focus on classes, interfaces, relationships, and SOLID principles

#### Pattern 5: Stream/Queue Based
- **Use Case**: Real-time data processing, moving window operations
- **Examples**: Moving Average, Hit Counter, Rate Limiter
- **Key Point**: Deque or Queue for time-window based operations

## 1) General form

### 1-1) Basic OP

#### Step 1: Clarify Requirements
- What operations need to be supported?
- What are the time/space complexity requirements?
- What are the edge cases? (empty input, duplicates, concurrency)
- What is the expected scale? (single machine vs distributed)

#### Step 2: Choose Data Structures
- Map key requirements to appropriate data structures
- Consider trade-offs (time vs space, simplicity vs performance)
- Multiple data structures often needed (HashMap + List, HashMap + Heap, etc.)

#### Step 3: Define Class Structure
```python
class DesignName:
    def __init__(self, params):
        # Initialize data structures
        self.data_structure1 = {}
        self.data_structure2 = []

    def operation1(self, params):
        # Implement operation
        pass

    def operation2(self, params):
        # Implement operation
        pass
```

#### Step 4: Implement Core Operations
- Focus on the required methods
- Maintain invariants (data consistency between structures)
- Handle edge cases

#### Step 5: Optimize
- Identify bottlenecks
- Use appropriate data structures for O(1) operations when needed
- Consider lazy evaluation or caching

## 1-2) Interview Tips

### Tip 1: Ask Clarifying Questions
- "Should this support concurrent access?" (Usually no for LC problems)
- "What should happen if we try to get a non-existent key?"
- "Are there any constraints on input size or range?"
- "Do we need to support deletion/updates?"

### Tip 2: Start with Simple Solution
- Start with brute force using basic data structures
- Explain time/space complexity
- Then optimize based on requirements

### Tip 3: Data Structure Selection
- **Need fast lookup?** → HashMap/HashSet
- **Need ordering?** → LinkedList, TreeMap, Heap
- **Need both?** → Combine them (HashMap + LinkedList for LRU)
- **Prefix operations?** → Trie
- **Range queries?** → Segment Tree, Binary Indexed Tree
- **Time-based operations?** → Queue/Deque with timestamps

### Tip 4: Common Mistakes to Avoid
- Not maintaining consistency between multiple data structures
- Forgetting to handle edge cases (empty, single element, duplicates)
- Not considering time complexity of helper operations
- Over-engineering (keep it simple if requirements allow)

### Tip 5: OOD Specific Tips
- Define clear interfaces and responsibilities
- Use meaningful class and method names
- Consider SOLID principles (especially Single Responsibility)
- Think about extensibility and maintainability

## 1-3) Things to Notice

### Notice 1: OrderedDict in Python
- Combines HashMap and LinkedList functionality
- `move_to_end(key)`: O(1) operation to reorder
- `popitem(last=False)`: Remove first item (FIFO), `last=True` for LIFO
- Perfect for LRU/LFU cache implementations

### Notice 2: Double Data Structure Pattern
- When using multiple data structures, ensure they stay synchronized
- Example: LRU uses `cache_dict` (lookup) + `cache_list` (order)
- Always update BOTH when adding/removing/modifying

### Notice 3: Dummy Nodes for LinkedList
- Use dummy head/tail nodes to simplify edge cases
- Avoids null checks for head/tail operations
- Common in LRU Cache implementations

### Notice 4: Time-based Expiration
- Use timestamp + cleanup strategy
- Lazy cleanup: Remove expired items when accessed
- Eager cleanup: Use heap/queue to track expiration times
- Trade-off: Space (keeping old data) vs Time (cleanup overhead)

### Notice 5: defaultdict and Counter
```python
from collections import defaultdict, Counter
# Avoid key existence checks
followers = defaultdict(set)  # Auto-creates empty set
tweet_count = defaultdict(int)  # Auto-creates 0
```

## 1-4) Classic LC Problems by Category

### Category 1: Cache Design ⭐⭐⭐
- **LC 146. LRU Cache** (Medium) - HashMap + DoublyLinkedList
- **LC 460. LFU Cache** (Hard) - HashMap + OrderedDict for frequency buckets
- **LC 432. All O(1) Data Structure** (Hard) - HashMap + DoublyLinkedList of buckets
- **LC 1756. Design Most Recently Used Queue** (Medium)

### Category 2: Data Structure Design
- **LC 380. Insert Delete GetRandom O(1)** (Medium) - HashMap + ArrayList
- **LC 381. Insert Delete GetRandom O(1) - Duplicates** (Hard)
- **LC 211. Design Add and Search Words Data Structure** (Medium) - Trie
- **LC 208. Implement Trie (Prefix Tree)** (Medium)
- **LC 641. Design Circular Deque** (Medium)
- **LC 622. Design Circular Queue** (Medium)
- **LC 225. Implement Stack using Queues** (Easy)
- **LC 232. Implement Queue using Stacks** (Easy)

### Category 3: Stream/Time-based Design
- **LC 346. Moving Average from Data Stream** (Easy) - Queue
- **LC 362. Design Hit Counter** (Medium) - Queue with timestamps
- **LC 353. Design Snake Game** (Medium) - Queue + Set
- **LC 1396. Design Underground System** (Medium) - HashMap
- **LC 981. Time Based Key-Value Store** (Medium) - HashMap + Binary Search

### Category 4: File System Design
- **LC 1166. Design File System** (Medium) - HashMap for path storage
- **LC 588. Design In-Memory File System** (Hard) - Trie-like nested dict structure
- **LC 1244. Design A Leaderboard** (Medium) - HashMap + TreeMap

### Category 5: Social Network Design
- **LC 355. Design Twitter** (Medium) - HashMap + Heap for feed merging
- **LC 1603. Design Parking System** (Easy) - Simple counter

### Category 6: Search/Autocomplete Design
- **LC 642. Design Search Autocomplete System** (Hard) - Trie + Heap
- **LC 1268. Search Suggestions System** (Medium) - Trie or Sorting
- **LC 1146. Snapshot Array** (Medium) - HashMap for snapshots

### Category 7: Iterator Design
- **LC 284. Peeking Iterator** (Medium) - Iterator wrapper with lookahead
- **LC 251. Flatten 2D Vector** (Medium) - Two pointers
- **LC 341. Flatten Nested List Iterator** (Medium) - Stack for DFS
- **LC 281. Zigzag Iterator** (Medium) - Queue of iterators

### Category 8: Rate Limiter Design
- **LC 362. Design Hit Counter** (Medium) - Sliding window
- Design Token Bucket Rate Limiter (Common interview question)
- Design Leaky Bucket Rate Limiter (Common interview question)

### Category 9: Game Design
- **LC 348. Design Tic-Tac-Toe** (Medium) - Row/Col/Diagonal counters
- **LC 353. Design Snake Game** (Medium) - Queue + Set
- **LC 1286. Iterator for Combination** (Medium)

## 2) LC Example

#### 2-1) Design File System
```python
# LC 1166. Design File System
# V1
# IDEA : dict
# https://leetcode.com/problems/design-file-system/discuss/365925/Python-dict-solution
class FileSystem:

    def __init__(self):
        self.d = {}

    def createPath(self, path: str, value: int) -> bool:
        if path in self.d: return False
        if len(path) == 1: return False
        idx = len(path) - 1
        while path[idx] != '/': idx -= 1
        if idx == 0 or path[:idx] in self.d: 
            self.d[path] = value
            return True
        return False
        
    def get(self, path: str) -> int:
        return self.d.get(path, -1)
```

#### 2-2) Design In-Memory File System
```python
# LC 588. Design In-Memory File System

# V0
# IDEA : Dict
class FileSystem(object):

    def __init__(self):
        """
        NOTE !!! we init root as below structure
        """
        self.root = {'dirs' : {}, 'files': {}}

    def ls(self, path):
        """
        :type path: str
        :rtype: List[str]
        """
        node, type = self.getExistedNode(path)
        if type == 'dir':
            return sorted(node['dirs'].keys() + node['files'].keys())
        return [path.split('/')[-1]]

    def mkdir(self, path):
        """
        :type path: str
        :rtype: void
        """
        node = self.root
        #for dir in filter(len, path.split('/')):
        for dir in [ x for x in path.split('/') if len(x) > 0 ]:
            if dir not in node['dirs']:
                node['dirs'][dir] = {'dirs' : {}, 'files': {}}
            node = node['dirs'][dir]

    def addContentToFile(self, filePath, content):
        """
        :type filePath: str
        :type content: str
        :rtype: void
        """
        dirs = filePath.split('/')
        path, file = '/'.join(dirs[:-1]), dirs[-1]
        self.mkdir(path)
        node, type = self.getExistedNode(path)
        if file not in node['files']:
            node['files'][file] = ''
        node['files'][file] += content

    def readContentFromFile(self, filePath):
        """
        :type filePath: str
        :rtype: str
        """
        dirs = filePath.split('/')
        path, file = '/'.join(dirs[:-1]), dirs[-1]
        node, type = self.getExistedNode(path)
        return node['files'][file]
        
    def getExistedNode(self, path):
        """
        :type path: str
        :rtype: str
        """
        node = self.root

        # method 1) : filter
        # https://www.runoob.com/python/python-func-filter.html
        #print ("*** path = " + str(path))
        #print ("*** filter(len, path.split('/') = " + str(filter(len, path.split('/'))))
        #for dir in filter(len, path.split('/')): # filter out path.split('/') outcome which with len > 0

        # method 2) list comprehension with condition
        for dir in [ x for x in path.split('/') if len(x) > 0 ]:
            if dir in node['dirs']: 
                node = node['dirs'][dir]
            else:
                return node, 'file'
        return node, 'dir'
```

#### 2-3) LRU Cache
```python
# LC 146 LRU Cache (Least Recently Used (LRU) cache)
# V0
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
```

#### 2-4) LFU Cache
```python
# LC 460. LFU Cache
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

```

#### 2-5) Design Twitter
```python
# LC 355  Design Twitter
# V0
# https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E8%AE%BE%E8%AE%A1Twitter.md
from collections import defaultdict
from heapq import merge
class Twitter(object):
    
    def __init__(self):
        self.follower_followees_map = defaultdict(set)
        self.user_tweets_map = defaultdict(list)
        self.time_stamp = 0

    def postTweet(self, userId, tweetId):
        self.user_tweets_map[userId].append((self.time_stamp, tweetId))
        self.time_stamp -= 1

    def getNewsFeed(self, userId):
        # get the followees list
        followees = self.follower_followees_map[userId]
        # add userId as well, since he/she can also see his/her post in the timeline
        followees.add(userId)
        
        # reversed(.) returns a listreverseiterator, so the complexity is O(1) not O(n)
        candidate_tweets = [reversed(self.user_tweets_map[u]) for u in followees]

        tweets = []
        """
        python starred expression :
        -> will extend Iterable Unpacking
        example 1 : *candidate_tweets
        exmaple 2 : a, *b, c = range(5)
        ref :
        https://www.python.org/dev/peps/pep-3132/
        https://blog.csdn.net/weixin_41521681/article/details/103528136
        http://swaywang.blogspot.com/2012/01/pythonstarred-expression.html
        https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md
        """
        # complexity is 10*log(n), n is twitter's user number in worst case
        for t in merge(*candidate_tweets):
            tweets.append(t[1])
            if len(tweets) == 10:
                break
        return tweets

    def follow(self, followerId, followeeId):
        self.follower_followees_map[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        self.follower_followees_map[followerId].discard(followeeId)
```

#### 2-6) Design Log Storage System
```python
# LC 635 Design Log Storage System
```

#### 2-7) Design Search Autocomplete System
```python
# LC 642 Design Search Autocomplete System
# V1
# IDEA : DICT TRIE
# http://bookshadow.com/weblog/2017/07/16/leetcode-design-search-autocomplete-system/
class TrieNode:
    def __init__(self):
        self.children = dict()
        self.sentences = set()

class AutocompleteSystem(object):

    def __init__(self, sentences, times):
        """
        :type sentences: List[str]
        :type times: List[int]
        """
        self.buffer = ''
        self.stimes = collections.defaultdict(int)
        self.trie = TrieNode()
        for s, t in zip(sentences, times):
            self.stimes[s] = t
            self.addSentence(s)
        self.tnode = self.trie

    def input(self, c):
        """
        :type c: str
        :rtype: List[str]
        """
        ans = []
        if c != '#':
            self.buffer += c
            if self.tnode: self.tnode = self.tnode.children.get(c)
            if self.tnode: ans = sorted(self.tnode.sentences, key=lambda x: (-self.stimes[x], x))[:3]
        else:
            self.stimes[self.buffer] += 1
            self.addSentence(self.buffer)
            self.buffer = ''
            self.tnode = self.trie
        return ans

    def addSentence(self, sentence):
        node = self.trie
        for letter in sentence:
            child = node.children.get(letter)
            if child is None:
                child = TrieNode()
                node.children[letter] = child
            node = child
            child.sentences.add(sentence)
```

#### 2-8) Insert Delete GetRandom O(1)
```python
# LC 380. Insert Delete GetRandom O(1)
# V0
# IDEA: HashMap + ArrayList
# - HashMap: stores value -> index mapping for O(1) lookup
# - ArrayList: stores actual values for O(1) random access
import random
class RandomizedSet:

    def __init__(self):
        self.dict = {}  # value -> index in list
        self.list = []  # stores values

    def insert(self, val: int) -> bool:
        if val in self.dict:
            return False
        self.dict[val] = len(self.list)
        self.list.append(val)
        return True

    def remove(self, val: int) -> bool:
        if val not in self.dict:
            return False
        # Move last element to the position of element to delete
        last_element = self.list[-1]
        idx = self.dict[val]
        self.list[idx] = last_element
        self.dict[last_element] = idx
        # Remove last element
        self.list.pop()
        del self.dict[val]
        return True

    def getRandom(self) -> int:
        return random.choice(self.list)
```

#### 2-9) Design Hit Counter
```python
# LC 362. Design Hit Counter
# V0
# IDEA: Queue with timestamps (sliding window)
from collections import deque
class HitCounter:

    def __init__(self):
        self.hits = deque()  # stores timestamps

    def hit(self, timestamp: int) -> None:
        self.hits.append(timestamp)

    def getHits(self, timestamp: int) -> int:
        # Remove hits older than 300 seconds
        while self.hits and timestamp - self.hits[0] >= 300:
            self.hits.popleft()
        return len(self.hits)

# V1
# IDEA: Array with buckets (optimized for multiple hits at same timestamp)
class HitCounter:

    def __init__(self):
        self.times = [0] * 300
        self.hits = [0] * 300

    def hit(self, timestamp: int) -> None:
        idx = timestamp % 300
        if self.times[idx] != timestamp:
            self.times[idx] = timestamp
            self.hits[idx] = 1
        else:
            self.hits[idx] += 1

    def getHits(self, timestamp: int) -> int:
        total = 0
        for i in range(300):
            if timestamp - self.times[i] < 300:
                total += self.hits[i]
        return total
```

#### 2-10) Design Tic-Tac-Toe
```python
# LC 348. Design Tic-Tac-Toe
# V0
# IDEA: Track row/col/diagonal sums
# - Each player has a unique value (+1 for player1, -1 for player2)
# - Win when any row/col/diagonal sum equals n or -n
class TicTacToe:

    def __init__(self, n: int):
        self.n = n
        self.rows = [0] * n
        self.cols = [0] * n
        self.diagonal = 0
        self.anti_diagonal = 0

    def move(self, row: int, col: int, player: int) -> int:
        # player 1 -> +1, player 2 -> -1
        value = 1 if player == 1 else -1

        self.rows[row] += value
        self.cols[col] += value

        if row == col:
            self.diagonal += value

        if row + col == self.n - 1:
            self.anti_diagonal += value

        # Check win condition
        if (abs(self.rows[row]) == self.n or
            abs(self.cols[col]) == self.n or
            abs(self.diagonal) == self.n or
            abs(self.anti_diagonal) == self.n):
            return player

        return 0
```

#### 2-11) Design Underground System
```python
# LC 1396. Design Underground System
# V0
# IDEA: Two HashMaps
# - checkInMap: id -> (stationName, time)
# - travelMap: (start, end) -> [total_time, count]
from collections import defaultdict
class UndergroundSystem:

    def __init__(self):
        self.check_in = {}  # id -> (station, time)
        self.travel = defaultdict(lambda: [0, 0])  # (start, end) -> [total_time, count]

    def checkIn(self, id: int, stationName: str, t: int) -> None:
        self.check_in[id] = (stationName, t)

    def checkOut(self, id: int, stationName: str, t: int) -> None:
        start_station, start_time = self.check_in[id]
        route = (start_station, stationName)
        self.travel[route][0] += t - start_time
        self.travel[route][1] += 1
        del self.check_in[id]

    def getAverageTime(self, startStation: str, endStation: str) -> float:
        total_time, count = self.travel[(startStation, endStation)]
        return total_time / count
```

#### 2-12) Design Snake Game
```python
# LC 353. Design Snake Game
# V0
# IDEA: Queue for snake body + Set for fast collision check
from collections import deque
class SnakeGame:

    def __init__(self, width: int, height: int, food: List[List[int]]):
        self.width = width
        self.height = height
        self.food = deque(food)
        self.snake = deque([(0, 0)])  # snake body positions
        self.snake_set = {(0, 0)}  # for O(1) collision check
        self.score = 0

    def move(self, direction: str) -> int:
        # Calculate new head position
        head_r, head_c = self.snake[0]

        if direction == "U":
            new_r, new_c = head_r - 1, head_c
        elif direction == "D":
            new_r, new_c = head_r + 1, head_c
        elif direction == "L":
            new_r, new_c = head_r, head_c - 1
        else:  # "R"
            new_r, new_c = head_r, head_c + 1

        # Check boundary
        if new_r < 0 or new_r >= self.height or new_c < 0 or new_c >= self.width:
            return -1

        # Check if eating food
        if self.food and [new_r, new_c] == self.food[0]:
            self.food.popleft()
            self.score += 1
        else:
            # Remove tail if not eating
            tail = self.snake.pop()
            self.snake_set.remove(tail)

        # Check self-collision (after tail removal)
        if (new_r, new_c) in self.snake_set:
            return -1

        # Add new head
        self.snake.appendleft((new_r, new_c))
        self.snake_set.add((new_r, new_c))

        return self.score
```

#### 2-13) Time Based Key-Value Store
```python
# LC 981. Time Based Key-Value Store
# V0
# IDEA: HashMap + Binary Search
# - HashMap: key -> list of (timestamp, value) pairs
# - List is sorted by timestamp, use binary search to find largest timestamp <= target
from collections import defaultdict
import bisect

class TimeMap:

    def __init__(self):
        self.store = defaultdict(list)  # key -> [(timestamp, value), ...]

    def set(self, key: str, value: str, timestamp: int) -> None:
        self.store[key].append((timestamp, value))

    def get(self, key: str, timestamp: int) -> str:
        if key not in self.store:
            return ""

        values = self.store[key]
        # Binary search for largest timestamp <= target
        idx = bisect.bisect_right(values, (timestamp, chr(127)))

        return values[idx - 1][1] if idx > 0 else ""

# V1 - Manual Binary Search
class TimeMap:

    def __init__(self):
        self.store = defaultdict(list)

    def set(self, key: str, value: str, timestamp: int) -> None:
        self.store[key].append((timestamp, value))

    def get(self, key: str, timestamp: int) -> str:
        if key not in self.store:
            return ""

        values = self.store[key]
        left, right = 0, len(values) - 1
        result = ""

        while left <= right:
            mid = (left + right) // 2
            if values[mid][0] <= timestamp:
                result = values[mid][1]
                left = mid + 1
            else:
                right = mid - 1

        return result
```

#### 2-14) Design Add and Search Words Data Structure
```python
# LC 211. Design Add and Search Words Data Structure
# V0
# IDEA: Trie with wildcard support
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_word = False

class WordDictionary:

    def __init__(self):
        self.root = TrieNode()

    def addWord(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_word = True

    def search(self, word: str) -> bool:
        return self.search_helper(word, 0, self.root)

    def search_helper(self, word: str, index: int, node: TrieNode) -> bool:
        if index == len(word):
            return node.is_word

        char = word[index]

        if char == '.':
            # Try all possible children
            for child in node.children.values():
                if self.search_helper(word, index + 1, child):
                    return True
            return False
        else:
            if char not in node.children:
                return False
            return self.search_helper(word, index + 1, node.children[char])
```

#### 2-15) All O(1) Data Structure
```python
# LC 432. All O`one Data Structure
# V0
# IDEA: HashMap + Doubly Linked List of Buckets
# - Each bucket contains all keys with the same count
# - HashMap: key -> bucket node
# - Doubly Linked List: ordered buckets by count
class Node:
    def __init__(self, count):
        self.count = count
        self.keys = set()
        self.prev = None
        self.next = None

class AllOne:

    def __init__(self):
        self.key_counter = {}  # key -> count
        self.count_node = {}   # count -> Node
        self.head = Node(0)    # dummy head
        self.tail = Node(0)    # dummy tail
        self.head.next = self.tail
        self.tail.prev = self.head

    def inc(self, key: str) -> None:
        if key in self.key_counter:
            count = self.key_counter[key]
            self.key_counter[key] = count + 1
            cur_node = self.count_node[count]

            # Remove key from current count bucket
            cur_node.keys.remove(key)

            # Get or create next count bucket
            if count + 1 not in self.count_node:
                new_node = Node(count + 1)
                self.count_node[count + 1] = new_node
                self._insert_after(cur_node, new_node)

            self.count_node[count + 1].keys.add(key)

            # Remove current bucket if empty
            if not cur_node.keys:
                self._remove_node(cur_node)
                del self.count_node[count]
        else:
            self.key_counter[key] = 1
            if 1 not in self.count_node:
                new_node = Node(1)
                self.count_node[1] = new_node
                self._insert_after(self.head, new_node)
            self.count_node[1].keys.add(key)

    def dec(self, key: str) -> None:
        count = self.key_counter[key]
        cur_node = self.count_node[count]
        cur_node.keys.remove(key)

        if count == 1:
            del self.key_counter[key]
        else:
            self.key_counter[key] = count - 1
            if count - 1 not in self.count_node:
                new_node = Node(count - 1)
                self.count_node[count - 1] = new_node
                self._insert_before(cur_node, new_node)
            self.count_node[count - 1].keys.add(key)

        if not cur_node.keys:
            self._remove_node(cur_node)
            del self.count_node[count]

    def getMaxKey(self) -> str:
        if self.tail.prev == self.head:
            return ""
        return next(iter(self.tail.prev.keys))

    def getMinKey(self) -> str:
        if self.head.next == self.tail:
            return ""
        return next(iter(self.head.next.keys))

    def _insert_after(self, node, new_node):
        new_node.prev = node
        new_node.next = node.next
        node.next.prev = new_node
        node.next = new_node

    def _insert_before(self, node, new_node):
        new_node.next = node
        new_node.prev = node.prev
        node.prev.next = new_node
        node.prev = new_node

    def _remove_node(self, node):
        node.prev.next = node.next
        node.next.prev = node.prev
```

---

## 3) System Design Coding Patterns

### 3-1) Consistent Hashing

**Concept:**
- Distribute data/load across nodes in a scalable way
- Minimize rehashing when nodes are added/removed
- Used in: Distributed caching, load balancing, CDNs
- **Key Benefit**: Only k/n keys need remapping when adding/removing nodes (vs all keys)

**Virtual Nodes:**
- Each physical node has multiple virtual nodes on the ring
- Better load distribution and fault tolerance
- Typical: 100-200 virtual nodes per physical node

**Implementation:**

```python
# V0 - Consistent Hashing with Virtual Nodes
import hashlib
import bisect
from typing import List, Optional

class ConsistentHashing:
    """
    Consistent Hashing Implementation with Virtual Nodes

    Time Complexity:
    - add_node: O(V log N) where V = virtual_nodes, N = total nodes
    - remove_node: O(V log N)
    - get_node: O(log N) binary search

    Space: O(V × P) where P = physical nodes
    """

    def __init__(self, virtual_nodes: int = 150):
        """
        virtual_nodes: number of virtual nodes per physical node
        """
        self.virtual_nodes = virtual_nodes
        self.ring = []  # Sorted list of (hash, node_id)
        self.nodes = set()  # Physical nodes

    def _hash(self, key: str) -> int:
        """Generate hash value for key"""
        return int(hashlib.md5(key.encode()).hexdigest(), 16)

    def add_node(self, node_id: str) -> None:
        """
        Add physical node with virtual nodes
        Time: O(V log N)
        """
        if node_id in self.nodes:
            return

        self.nodes.add(node_id)

        # Add virtual nodes to ring
        for i in range(self.virtual_nodes):
            virtual_key = f"{node_id}:vnode{i}"
            hash_value = self._hash(virtual_key)
            bisect.insort(self.ring, (hash_value, node_id))

    def remove_node(self, node_id: str) -> None:
        """
        Remove physical node and its virtual nodes
        Time: O(V × N) for removal
        """
        if node_id not in self.nodes:
            return

        self.nodes.remove(node_id)

        # Remove all virtual nodes
        self.ring = [(h, nid) for h, nid in self.ring if nid != node_id]

    def get_node(self, key: str) -> Optional[str]:
        """
        Find node responsible for key
        Time: O(log N) - binary search on ring
        """
        if not self.ring:
            return None

        hash_value = self._hash(key)

        # Binary search for first node >= hash_value
        idx = bisect.bisect_right(self.ring, (hash_value, ''))

        # Wrap around if at end
        if idx == len(self.ring):
            idx = 0

        return self.ring[idx][1]

    def get_nodes(self, key: str, count: int = 1) -> List[str]:
        """
        Get multiple nodes for replication
        Time: O(log N + count)
        """
        if not self.ring or count <= 0:
            return []

        hash_value = self._hash(key)
        idx = bisect.bisect_right(self.ring, (hash_value, ''))

        result = []
        seen = set()

        # Traverse ring clockwise
        for i in range(len(self.ring)):
            pos = (idx + i) % len(self.ring)
            node_id = self.ring[pos][1]

            if node_id not in seen:
                result.append(node_id)
                seen.add(node_id)

                if len(result) == count:
                    break

        return result

    def get_distribution(self) -> dict:
        """Get key distribution stats (for testing)"""
        # Sample 10000 keys and count distribution
        distribution = {node: 0 for node in self.nodes}

        for i in range(10000):
            key = f"key_{i}"
            node = self.get_node(key)
            if node:
                distribution[node] += 1

        return distribution
```

**Java Implementation:**

```java
// Java - Consistent Hashing with Virtual Nodes
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

class ConsistentHashing {
    private final int virtualNodes;
    private final TreeMap<Long, String> ring; // Hash -> Node
    private final Set<String> nodes;
    private final MessageDigest md;

    /**
     * Constructor
     * time = O(1)
     * space = O(1)
     */
    public ConsistentHashing(int virtualNodes) {
        this.virtualNodes = virtualNodes;
        this.ring = new TreeMap<>();
        this.nodes = new HashSet<>();
        try {
            this.md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private long hash(String key) {
        md.reset();
        byte[] digest = md.digest(key.getBytes());
        long hash = 0;
        for (int i = 0; i < 8; i++) {
            hash = (hash << 8) | (digest[i] & 0xFF);
        }
        return hash;
    }

    /**
     * Add node with virtual nodes
     * time = O(V log N)
     * space = O(V)
     */
    public void addNode(String nodeId) {
        if (nodes.contains(nodeId)) return;

        nodes.add(nodeId);

        // Add virtual nodes
        for (int i = 0; i < virtualNodes; i++) {
            String virtualKey = nodeId + ":vnode" + i;
            long hash = hash(virtualKey);
            ring.put(hash, nodeId);
        }
    }

    /**
     * Remove node and its virtual nodes
     * time = O(V log N)
     * space = O(1)
     */
    public void removeNode(String nodeId) {
        if (!nodes.contains(nodeId)) return;

        nodes.remove(nodeId);

        // Remove all virtual nodes
        ring.entrySet().removeIf(entry -> entry.getValue().equals(nodeId));
    }

    /**
     * Find node for key
     * time = O(log N)
     * space = O(1)
     */
    public String getNode(String key) {
        if (ring.isEmpty()) return null;

        long hash = hash(key);

        // Find first entry >= hash (ceiling)
        Map.Entry<Long, String> entry = ring.ceilingEntry(hash);

        // Wrap around if null
        if (entry == null) {
            entry = ring.firstEntry();
        }

        return entry.getValue();
    }

    /**
     * Get multiple nodes for replication
     * time = O(log N + count)
     * space = O(count)
     */
    public List<String> getNodes(String key, int count) {
        if (ring.isEmpty() || count <= 0) return new ArrayList<>();

        long hash = hash(key);
        List<String> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        // Start from ceiling entry
        Long startKey = ring.ceilingKey(hash);
        if (startKey == null) startKey = ring.firstKey();

        // Traverse ring
        Iterator<Map.Entry<Long, String>> iter = ring.tailMap(startKey).entrySet().iterator();

        while (seen.size() < count) {
            if (!iter.hasNext()) {
                iter = ring.entrySet().iterator();
            }

            String nodeId = iter.next().getValue();
            if (!seen.contains(nodeId)) {
                result.add(nodeId);
                seen.add(nodeId);
            }
        }

        return result;
    }
}
```

**Usage Example:**

```python
# Create consistent hashing with 150 virtual nodes per physical node
ch = ConsistentHashing(virtual_nodes=150)

# Add nodes
ch.add_node("node1")
ch.add_node("node2")
ch.add_node("node3")

# Get node for key
node = ch.get_node("user_12345")  # Returns node responsible for this key

# Get multiple nodes for replication
replicas = ch.get_nodes("user_12345", count=3)  # Returns [node1, node2, node3]

# Remove node - only keys on this node need remapping
ch.remove_node("node2")

# Check distribution
distribution = ch.get_distribution()
# {'node1': 5012, 'node2': 4988} - roughly even distribution
```

**LeetCode Related Problems:**
- LC 535 - Encode and Decode TinyURL (hash-based mapping)
- System design interviews: Design URL shortener, Design distributed cache

---

### 3-2) Rate Limiter - Token Bucket

**Concept:**
- Control request rate using token bucket algorithm
- **Tokens**: Added at fixed rate (refill_rate)
- **Bucket**: Max capacity of tokens
- **Request**: Consumes 1 token, rejected if insufficient
- **Use Cases**: API rate limiting, traffic shaping, QoS

**Token Bucket vs Leaky Bucket:**
- Token Bucket: Allows bursts up to bucket capacity
- Leaky Bucket: Smooth output rate, no bursts

**Implementation:**

```python
# LC 359 - Logger Rate Limiter (simplified variant)
# V0 - Token Bucket Rate Limiter
import time
from threading import Lock

class TokenBucketRateLimiter:
    """
    Token Bucket Rate Limiter

    Parameters:
    - capacity: max tokens in bucket (burst size)
    - refill_rate: tokens added per second

    Time Complexity:
    - allow_request: O(1)

    Space: O(1) per user/key
    """

    def __init__(self, capacity: int, refill_rate: float):
        """
        capacity: max tokens (burst size)
        refill_rate: tokens added per second
        """
        self.capacity = capacity
        self.refill_rate = refill_rate
        self.tokens = capacity  # Start with full bucket
        self.last_refill = time.time()
        self.lock = Lock()

    def _refill(self):
        """Refill tokens based on elapsed time"""
        now = time.time()
        elapsed = now - self.last_refill

        # Add tokens based on elapsed time
        tokens_to_add = elapsed * self.refill_rate
        self.tokens = min(self.capacity, self.tokens + tokens_to_add)
        self.last_refill = now

    def allow_request(self, tokens: int = 1) -> bool:
        """
        Try to consume tokens for request
        Returns: True if allowed, False if rate limited
        Time: O(1)
        """
        with self.lock:
            self._refill()

            if self.tokens >= tokens:
                self.tokens -= tokens
                return True

            return False

    def get_available_tokens(self) -> float:
        """Get current available tokens (for monitoring)"""
        with self.lock:
            self._refill()
            return self.tokens


# Multi-user Rate Limiter
class MultiUserRateLimiter:
    """
    Rate limiter for multiple users
    Each user has independent token bucket
    """

    def __init__(self, capacity: int, refill_rate: float):
        self.capacity = capacity
        self.refill_rate = refill_rate
        self.users = {}  # user_id -> (tokens, last_refill)
        self.lock = Lock()

    def allow_request(self, user_id: str, tokens: int = 1) -> bool:
        """
        Check if user can make request
        Time: O(1)
        Space: O(U) where U = number of users
        """
        with self.lock:
            now = time.time()

            if user_id not in self.users:
                # New user: initialize with full bucket
                self.users[user_id] = [self.capacity, now]

            user_tokens, last_refill = self.users[user_id]

            # Refill tokens
            elapsed = now - last_refill
            tokens_to_add = elapsed * self.refill_rate
            user_tokens = min(self.capacity, user_tokens + tokens_to_add)

            # Try to consume
            if user_tokens >= tokens:
                user_tokens -= tokens
                self.users[user_id] = [user_tokens, now]
                return True

            # Update last_refill even if rejected
            self.users[user_id] = [user_tokens, now]
            return False
```

**Java Implementation:**

```java
// Java - Token Bucket Rate Limiter
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

class TokenBucketRateLimiter {
    private final int capacity;
    private final double refillRate; // tokens per second
    private double tokens;
    private long lastRefillTime;

    /**
     * Constructor
     * capacity: max tokens (burst size)
     * refillRate: tokens added per second
     */
    public TokenBucketRateLimiter(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    private synchronized void refill() {
        long now = System.currentTimeMillis();
        double elapsed = (now - lastRefillTime) / 1000.0; // Convert to seconds

        double tokensToAdd = elapsed * refillRate;
        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTime = now;
    }

    /**
     * Try to consume tokens
     * time = O(1)
     * space = O(1)
     */
    public synchronized boolean allowRequest(int requestTokens) {
        refill();

        if (tokens >= requestTokens) {
            tokens -= requestTokens;
            return true;
        }

        return false;
    }

    public synchronized double getAvailableTokens() {
        refill();
        return tokens;
    }
}

// Multi-user Rate Limiter
class MultiUserRateLimiter {
    private final int capacity;
    private final double refillRate;
    private final Map<String, UserBucket> users;

    private static class UserBucket {
        double tokens;
        long lastRefillTime;

        UserBucket(double tokens, long time) {
            this.tokens = tokens;
            this.lastRefillTime = time;
        }
    }

    public MultiUserRateLimiter(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.users = new ConcurrentHashMap<>();
    }

    /**
     * Check if user can make request
     * time = O(1)
     * space = O(U) where U = users
     */
    public boolean allowRequest(String userId, int requestTokens) {
        long now = System.currentTimeMillis();

        users.putIfAbsent(userId, new UserBucket(capacity, now));

        UserBucket bucket = users.get(userId);

        synchronized (bucket) {
            // Refill
            double elapsed = (now - bucket.lastRefillTime) / 1000.0;
            double tokensToAdd = elapsed * refillRate;
            bucket.tokens = Math.min(capacity, bucket.tokens + tokensToAdd);
            bucket.lastRefillTime = now;

            // Try consume
            if (bucket.tokens >= requestTokens) {
                bucket.tokens -= requestTokens;
                return true;
            }

            return false;
        }
    }
}
```

**LC 359 - Logger Rate Limiter (Simplified)**

```python
# LC 359 - Logger Rate Limiter
class Logger:
    """
    Simplified rate limiter: only 1 message per key every 10 seconds
    This is a special case of token bucket where:
    - capacity = 1
    - refill_rate = 1 token per 10 seconds
    """

    def __init__(self):
        self.msg_dict = {}  # message -> last_timestamp

    def shouldPrintMessage(self, timestamp: int, message: str) -> bool:
        """
        time = O(1)
        space = O(M) where M = unique messages
        """
        if message not in self.msg_dict:
            self.msg_dict[message] = timestamp
            return True

        if timestamp - self.msg_dict[message] >= 10:
            self.msg_dict[message] = timestamp
            return True

        return False
```

**Usage Example:**

```python
# Create rate limiter: 10 requests per second, burst up to 20
limiter = TokenBucketRateLimiter(capacity=20, refill_rate=10)

# Handle requests
for i in range(25):
    if limiter.allow_request():
        print(f"Request {i}: ALLOWED")
    else:
        print(f"Request {i}: RATE LIMITED")
    time.sleep(0.05)  # 50ms between requests

# Output:
# Requests 0-19: ALLOWED (burst uses all tokens)
# Requests 20-24: RATE LIMITED (no tokens left)
# After 1 second: 10 new tokens available
```

---

### 3-3) Rate Limiter - Leaky Bucket

**Concept:**
- Queue-based rate limiting with constant output rate
- **Queue**: Stores incoming requests (max capacity)
- **Leak**: Process requests at constant rate
- **Overflow**: Reject requests when queue is full
- **Difference from Token Bucket**: Smooths bursts, no immediate processing of burst

**Implementation:**

```python
# V0 - Leaky Bucket Rate Limiter
import time
from collections import deque
from threading import Lock

class LeakyBucketRateLimiter:
    """
    Leaky Bucket Rate Limiter

    Parameters:
    - capacity: max queue size
    - leak_rate: requests processed per second

    Time Complexity:
    - allow_request: O(1) amortized

    Space: O(capacity)
    """

    def __init__(self, capacity: int, leak_rate: float):
        """
        capacity: max requests in queue
        leak_rate: requests processed per second
        """
        self.capacity = capacity
        self.leak_rate = leak_rate
        self.queue = deque()
        self.last_leak = time.time()
        self.lock = Lock()

    def _leak(self):
        """Process (leak) requests at constant rate"""
        now = time.time()
        elapsed = now - self.last_leak

        # Calculate how many requests should have been processed
        requests_to_leak = int(elapsed * self.leak_rate)

        if requests_to_leak > 0:
            # Remove processed requests from queue
            for _ in range(min(requests_to_leak, len(self.queue))):
                self.queue.popleft()

            self.last_leak = now

    def allow_request(self) -> bool:
        """
        Try to add request to queue
        Returns: True if accepted, False if rejected
        Time: O(1) amortized
        """
        with self.lock:
            self._leak()

            if len(self.queue) < self.capacity:
                self.queue.append(time.time())
                return True

            return False

    def get_queue_size(self) -> int:
        """Get current queue size"""
        with self.lock:
            self._leak()
            return len(self.queue)


# Multi-user Leaky Bucket
class MultiUserLeakyBucket:
    """
    Leaky bucket rate limiter for multiple users
    """

    def __init__(self, capacity: int, leak_rate: float):
        self.capacity = capacity
        self.leak_rate = leak_rate
        self.users = {}  # user_id -> (queue, last_leak_time)
        self.lock = Lock()

    def allow_request(self, user_id: str) -> bool:
        """
        Check if user request can be added to queue
        Time: O(1) amortized
        Space: O(U × C) where U=users, C=capacity
        """
        with self.lock:
            now = time.time()

            if user_id not in self.users:
                self.users[user_id] = (deque(), now)

            queue, last_leak = self.users[user_id]

            # Leak requests
            elapsed = now - last_leak
            requests_to_leak = int(elapsed * self.leak_rate)

            if requests_to_leak > 0:
                for _ in range(min(requests_to_leak, len(queue))):
                    queue.popleft()
                last_leak = now

            # Try to add request
            if len(queue) < self.capacity:
                queue.append(now)
                self.users[user_id] = (queue, last_leak)
                return True

            self.users[user_id] = (queue, last_leak)
            return False
```

**Java Implementation:**

```java
// Java - Leaky Bucket Rate Limiter
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class LeakyBucketRateLimiter {
    private final int capacity;
    private final double leakRate; // requests per second
    private final Queue<Long> queue;
    private long lastLeakTime;

    public LeakyBucketRateLimiter(int capacity, double leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.queue = new LinkedList<>();
        this.lastLeakTime = System.currentTimeMillis();
    }

    private synchronized void leak() {
        long now = System.currentTimeMillis();
        double elapsed = (now - lastLeakTime) / 1000.0;

        int requestsToLeak = (int) (elapsed * leakRate);

        if (requestsToLeak > 0) {
            for (int i = 0; i < Math.min(requestsToLeak, queue.size()); i++) {
                queue.poll();
            }
            lastLeakTime = now;
        }
    }

    /**
     * Try to add request to queue
     * time = O(1) amortized
     * space = O(capacity)
     */
    public synchronized boolean allowRequest() {
        leak();

        if (queue.size() < capacity) {
            queue.offer(System.currentTimeMillis());
            return true;
        }

        return false;
    }

    public synchronized int getQueueSize() {
        leak();
        return queue.size();
    }
}
```

**Comparison: Token Bucket vs Leaky Bucket**

| Feature | Token Bucket | Leaky Bucket |
|---------|-------------|--------------|
| Burst Handling | Allows bursts up to capacity | Smooths bursts |
| Output Rate | Variable (up to capacity) | Constant (leak rate) |
| Use Case | API rate limiting, bursty traffic | Traffic shaping, smooth output |
| Complexity | O(1) per request | O(1) amortized |
| Memory | O(1) per user | O(capacity) per user |

**LeetCode Related:**
- LC 359 - Logger Rate Limiter
- LC 362 - Design Hit Counter
- LC 635 - Design Log Storage System

---

### 3-4) Load Balancing Algorithms

**Concept:**
- Distribute requests across multiple servers
- **Goals**: Even distribution, high availability, low latency
- **Types**: Round Robin, Weighted Round Robin, Least Connections, Consistent Hashing

**Implementation:**

```python
# V0 - Load Balancer Implementations
from typing import List, Optional
import time
from threading import Lock

class RoundRobinLoadBalancer:
    """
    Round Robin Load Balancer
    Distributes requests sequentially across servers

    Time: O(1) per request
    Space: O(N) where N = number of servers
    """

    def __init__(self, servers: List[str]):
        self.servers = servers
        self.current = 0
        self.lock = Lock()

    def get_server(self) -> Optional[str]:
        """
        Get next server in round-robin fashion
        Time: O(1)
        """
        if not self.servers:
            return None

        with self.lock:
            server = self.servers[self.current]
            self.current = (self.current + 1) % len(self.servers)
            return server

    def add_server(self, server: str):
        """Add new server to pool"""
        with self.lock:
            if server not in self.servers:
                self.servers.append(server)

    def remove_server(self, server: str):
        """Remove server from pool"""
        with self.lock:
            if server in self.servers:
                self.servers.remove(server)
                # Adjust current index if needed
                if self.current >= len(self.servers) and self.servers:
                    self.current = 0


class WeightedRoundRobinLoadBalancer:
    """
    Weighted Round Robin Load Balancer
    Servers with higher weight get more requests

    Example: weights = [5, 1, 1]
    -> Server 0 gets 5/7 of requests
    -> Server 1 and 2 each get 1/7 of requests

    Time: O(1) per request
    Space: O(sum of weights)
    """

    def __init__(self, servers: List[str], weights: List[int]):
        """
        servers: list of server IDs
        weights: corresponding weights (must be positive)
        """
        if len(servers) != len(weights):
            raise ValueError("Servers and weights must have same length")

        self.servers = []
        # Expand servers based on weights
        for server, weight in zip(servers, weights):
            self.servers.extend([server] * weight)

        self.current = 0
        self.lock = Lock()

    def get_server(self) -> Optional[str]:
        """
        Get next server based on weights
        Time: O(1)
        """
        if not self.servers:
            return None

        with self.lock:
            server = self.servers[self.current]
            self.current = (self.current + 1) % len(self.servers)
            return server


class LeastConnectionsLoadBalancer:
    """
    Least Connections Load Balancer
    Routes to server with fewest active connections

    Time: O(N) per request (find min)
    Space: O(N)

    Better for long-lived connections (WebSockets, DB connections)
    """

    def __init__(self, servers: List[str]):
        self.connections = {server: 0 for server in servers}
        self.lock = Lock()

    def get_server(self) -> Optional[str]:
        """
        Get server with least connections
        Time: O(N) where N = servers
        """
        if not self.connections:
            return None

        with self.lock:
            # Find server with minimum connections
            server = min(self.connections, key=self.connections.get)
            self.connections[server] += 1
            return server

    def release_connection(self, server: str):
        """
        Release connection from server
        Call this when request completes
        """
        with self.lock:
            if server in self.connections:
                self.connections[server] = max(0, self.connections[server] - 1)

    def add_server(self, server: str):
        """Add new server"""
        with self.lock:
            if server not in self.connections:
                self.connections[server] = 0

    def remove_server(self, server: str):
        """Remove server"""
        with self.lock:
            if server in self.connections:
                del self.connections[server]


class WeightedLeastConnectionsLoadBalancer:
    """
    Weighted Least Connections
    Considers both connection count and server capacity

    Score = connections / weight (lower is better)

    Time: O(N) per request
    Space: O(N)
    """

    def __init__(self, servers: List[str], weights: List[int]):
        self.servers = servers
        self.weights = {server: weight for server, weight in zip(servers, weights)}
        self.connections = {server: 0 for server in servers}
        self.lock = Lock()

    def get_server(self) -> Optional[str]:
        """
        Get server with lowest connections/weight ratio
        Time: O(N)
        """
        if not self.servers:
            return None

        with self.lock:
            # Calculate score for each server
            best_server = None
            best_score = float('inf')

            for server in self.servers:
                score = self.connections[server] / self.weights[server]
                if score < best_score:
                    best_score = score
                    best_server = server

            self.connections[best_server] += 1
            return best_server

    def release_connection(self, server: str):
        """Release connection"""
        with self.lock:
            if server in self.connections:
                self.connections[server] = max(0, self.connections[server] - 1)
```

**Java Implementation:**

```java
// Java - Round Robin Load Balancer
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class RoundRobinLoadBalancer {
    private final List<String> servers;
    private final AtomicInteger current;

    public RoundRobinLoadBalancer(List<String> servers) {
        this.servers = new ArrayList<>(servers);
        this.current = new AtomicInteger(0);
    }

    /**
     * Get next server
     * time = O(1)
     * space = O(N)
     */
    public String getServer() {
        if (servers.isEmpty()) return null;

        int index = current.getAndIncrement() % servers.size();
        return servers.get(index);
    }

    public synchronized void addServer(String server) {
        if (!servers.contains(server)) {
            servers.add(server);
        }
    }

    public synchronized void removeServer(String server) {
        servers.remove(server);
    }
}

// Java - Least Connections Load Balancer
class LeastConnectionsLoadBalancer {
    private final Map<String, Integer> connections;

    public LeastConnectionsLoadBalancer(List<String> servers) {
        this.connections = new HashMap<>();
        for (String server : servers) {
            connections.put(server, 0);
        }
    }

    /**
     * Get server with least connections
     * time = O(N)
     * space = O(N)
     */
    public synchronized String getServer() {
        if (connections.isEmpty()) return null;

        return connections.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(entry -> {
                String server = entry.getKey();
                connections.put(server, entry.getValue() + 1);
                return server;
            })
            .orElse(null);
    }

    public synchronized void releaseConnection(String server) {
        connections.computeIfPresent(server, (k, v) -> Math.max(0, v - 1));
    }
}
```

**Usage Example:**

```python
# Round Robin
rr_lb = RoundRobinLoadBalancer(["server1", "server2", "server3"])
for _ in range(6):
    print(rr_lb.get_server())
# Output: server1, server2, server3, server1, server2, server3

# Weighted Round Robin
wrr_lb = WeightedRoundRobinLoadBalancer(
    servers=["server1", "server2", "server3"],
    weights=[5, 2, 1]  # server1 gets 5/8 of traffic
)

# Least Connections
lc_lb = LeastConnectionsLoadBalancer(["server1", "server2", "server3"])
server = lc_lb.get_server()  # Returns server with fewest connections
# ... process request ...
lc_lb.release_connection(server)  # Release when done
```

**Load Balancing Algorithm Comparison:**

| Algorithm | Time | Space | Best For | Pros | Cons |
|-----------|------|-------|----------|------|------|
| Round Robin | O(1) | O(N) | Short requests | Simple, fair | Ignores server load |
| Weighted RR | O(1) | O(W) | Varying capacity | Respects capacity | Static weights |
| Least Connections | O(N) | O(N) | Long connections | Dynamic load balancing | Higher overhead |
| Consistent Hashing | O(log N) | O(VN) | Caching, sticky sessions | Minimal remapping | Complex implementation |

**LeetCode Related:**
- System design: Design distributed cache, Design web crawler
- LC 535 - Encode and Decode TinyURL (server selection for URL storage)

---

### 3-5) Interview Tips for System Design Patterns

**Key Talking Points:**

1. **Consistent Hashing:**
   - "Minimizes data movement when nodes are added/removed"
   - "Virtual nodes ensure better load distribution"
   - "Used in Cassandra, DynamoDB, Memcached"

2. **Rate Limiting:**
   - **Token Bucket**: "Allows controlled bursts, good for APIs"
   - **Leaky Bucket**: "Smooth output, good for traffic shaping"
   - "Choose based on whether bursts are acceptable"

3. **Load Balancing:**
   - **Round Robin**: "Simple, works well for homogeneous servers"
   - **Weighted**: "Use when servers have different capacities"
   - **Least Connections**: "Better for long-lived connections"
   - **Consistent Hashing**: "Use for stateful services (caching)"

**Common Interview Questions:**

1. "Design a rate limiter for an API"
   → Token bucket for burst support, Redis for distributed state

2. "How would you distribute load across servers?"
   → Start with Round Robin, mention alternatives based on requirements

3. "Design a distributed cache"
   → Consistent hashing for key distribution, discuss replication

4. "How to handle server failures in load balancer?"
   → Health checks, automatic removal, retry logic

**Implementation Tips:**

- **Thread Safety**: Use locks or atomic operations for concurrent access
- **Distributed Systems**: Use Redis/Memcached for shared state
- **Monitoring**: Track metrics (request rate, server load, error rate)
- **Health Checks**: Periodically verify server availability
- **Graceful Degradation**: Handle failures without complete outage
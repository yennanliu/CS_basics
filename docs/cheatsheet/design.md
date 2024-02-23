# Design

## 0) Concept  

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

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
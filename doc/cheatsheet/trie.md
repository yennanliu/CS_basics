# Trie 
> Whenever we come across questions with multiple strings, it is best to think if Trie can help us.
- https://leetcode.com/problems/search-suggestions-system/solution/

## 0) Concept 
- https://blog.csdn.net/fuxuemingzhu/article/details/79388432
- tree + dict
    - `put Node into dict` (e.g. defaultdict(Node))

<p><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/trie_1.png" ></p>

<p><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/trie_2.png" ></p>

### 0-1) Types

### 0-2) Pattern
```python
# deine node
from collections import defaultdict

### NOTE : need to define our Node
class Node():

    def __init__(self):
        """
        NOTE : we use defaultdict(Node) as our trie data structure

        -> use defaultdict
            - key : every character from word
            - value : Node (Node type)

        and link children with parent Node via defaultdict
        """
        self.children = defaultdict(Node)
        self.isword = False

# implement basic methods
class Trie():

    def __init__(self):
        self.root = Node()

    def insert(self, word):
        ### NOTE : we always start from below
        cur = self.root
        for w in word:
            cur = cur.children[w] # same as self.root.defaultdict[w]
        cur.isword = True

    def search(self, word):
        ### NOTE : we always start from below
        cur = self.root
        for w in word:
            cur = cur.children.get(w)
            if not cur:
                return False
        ### NOTE : need to check if isword
        return cur.isword

    def startsWith(self, prefix):
        ### NOTE : we always start from below
        cur = self.root
        for p in prefix:
            cur = cur.children.get(p)
            if not cur:
                return False
        return True
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Implement Trie (Prefix Tree)
```python
# 208. Implement Trie (Prefix Tree)
# V0
# IDEA : trie concept :  dict + tree
# https://blog.csdn.net/fuxuemingzhu/article/details/79388432
### NOTE : we need implement Node class
from collections import defaultdict
class Node(object):
    def __init__(self):
        ### NOTE : we use defaultdict as dict
        # TODO : make a default py dict version
        self.children = defaultdict(Node)
        self.isword = False
        
class Trie(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        ### NOTE : we use the Node class we implement above
        self.root = Node()

    def insert(self, word):
        current = self.root
        for w in word:
            current = current.children[w]
        ### NOTE : if insert OP completed, mark isword attr as true
        current.isword = True

    def search(self, word):
        current = self.root
        for w in word:
            current = current.children.get(w)
            if current == None:
                return False
        ### NOTE : we need to check if isword atts is true (check if word terminated here as well)
        return current.isword

    def startsWith(self, prefix):
        current = self.root
        for w in prefix:
            current = current.children.get(w)
            if current == None:
                return False
        ### NOTE : we don't need to check isword here, since it is "startsWith"
        return True
```

### 2-2) Add and Search Word - Data structure design
```python
# LC 211 Add and Search Word - Data structure design
# V0
from collections import defaultdict
class Node(object):
    def __init__(self):
        self.children = defaultdict(Node)
        self.isword = False
        
class WordDictionary(object):

    def __init__(self):
        self.root = Node()

    def addWord(self, word):
        current = self.root
        for w in word:
            _next = current.children[w]
            current = _next
        current.isword = True

    def search(self, word):
        return self.match(word, 0, self.root)
    
    def match(self, word, index, root):
        """
        NOTE : match is a helper func (for search)

          - deal with 2 cases
          -   1) word[index] != '.'
          -   2) word[index] == '.'
        """
        # note the edge cases
        if root == None:
            return False
        if index == len(word):
            return root.isword
        # CASE 1: word[index] != '.'
        if word[index] != '.':
            return root != None and self.match(word, index + 1, root.children.get(word[index]))
        # CASE 2: word[index] == '.'
        else:
            for child in root.children.values():
                if self.match(word, index + 1, child):
                    return True
        return False
```

### 2-3) Search Suggestions System
```python
# LC 1268. Search Suggestions System
# V1
# IDEA : TRIE
# https://leetcode.com/problems/search-suggestions-system/discuss/436183/Python-Trie-Solution
class TrieNode:
    def __init__(self):
        self.children = dict()
        self.words = []

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.words.append(word)
            node.words.sort()
            while len(node.words) > 3:
                node.words.pop()
    
    def search(self, word):
        res = []
        node = self.root
        for char in word:
            if char not in node.children:
                break
            node = node.children[char]
            res.append(node.words[:])
        l_remain = len(word) - len(res)
        for _ in range(l_remain):
            res.append([])
        return res

class Solution:
    def suggestedProducts(self, products: List[str], searchWord: str):
        trie = Trie()
        for prod in products:
            trie.insert(prod)
        return trie.search(searchWord)
```

### 2-4) Word Search
```python
# LC 79. Word Search
```

### 2-5) Word Search II
```python
# LC 212. Word Search II
``1
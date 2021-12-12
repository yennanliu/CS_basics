# Trie 

## 0) Concept 
- https://blog.csdn.net/fuxuemingzhu/article/details/79388432
- tree + dict
    - `put Node into dict` (e.g. defaultdict(Node))

### 0-1) Types

### 0-2) Pattern
```python
# deine node
from collections import defaultdict

### NOTE : need to define our Node
class Node():

    def __init__(self):
        """
        NOTE : we use defaultdict(Node) as our the trie data structure
        -> use defaultdict
            - key : every character from word
            - value : Node (Node type)

        and link children with paraent Node via defaultdict
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
            cur = cur.children[w]
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

```

### 2-3) Word Search II
```python
# LC 212. Word Search II
```
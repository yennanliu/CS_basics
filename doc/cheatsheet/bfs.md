# BFS cheatsheet 

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern

## 1) General form
```python
# V1 : via collections.deque
import collections
queue = collections.deque() 
def bfs(root):
    if not root:return 
    queue.append(root)
    while len(queue) > 0:
        for i in range(len(queue)):
            node=queue.popleft() 
            # do sth 
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        return queue

# V2 : via python default
def bfs(root):
    if not root: return 
    q=[root]
    while len(q) > 0:
        for i in range(0,len(q)):
            # do sth
            if q[0].left:
                q.append(q[0].left)
            if q[0].right:
                q.append(q[0].right)
            del q[0]
    return q

```

### 1-1) Basic OP

## 2) LC Example

### 2-1) Word Ladder
```python
# 127 Word Ladder
class Solution(object):
    def ladderLength(self, beginWord, endWord, wordList):

        wordset = set(wordList)
        bfs = collections.deque()
        bfs.append((beginWord, 1))
        while bfs:
            word, length = bfs.popleft()
            if word == endWord:
                return length
            for i in range(len(word)):
                for c in "abcdefghijklmnopqrstuvwxyz":
                    newWord = word[:i] + c + word[i + 1:]
                    if newWord in wordset and newWord != word:
                        wordset.remove(newWord)
                        bfs.append((newWord, length + 1))
        return 0

```
# BFS cheatsheet 

### 1) General form
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

### 2-1)

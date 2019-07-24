# Note for Leetcode Python 

## 1) BFS & DFS (Breadth-first search & Depth-first search)

- Tricks 
    - DFS = Stack + go through
    - BFS = Queue + go through 

- Algorithm 

```python
import collections 

# DFS (Stack)
def dfs(root):    
    queue = collections.deque() # the data structure save input node 
    res = []  # the demand output 
    queue.append(root)
    while queue:
        # do something 
        node =  queue.pop()  ### Stack : first in, last out
        # do something 
        queue.append(node.left)
        queue.append(node.right)
        res.append(node.val)
    return res 

# BFS (Queue)
def bfs(root):    
    queue = collections.deque() # the data structure save input node 
    res = []  # the demand output 
    queue.append(root)
    while queue:
        # do something 
        node =  queue.popleft() ### queue : first in, first out 
        # do something 
        queue.append(node.left)
        queue.append(node.right)
        res.append(node.val)
    return res 

```
- Example 

```python

# DFS (Iteration)

# DFS (Recursion) 
# leetcode # 515 Find Largest Value in Each Tree Row
class Solution(object):

    def largestValues(self, root):
        levels = []
        self.dfs(root, levels, 0)
        return [ max(l) for l in levels ]

    def dfs(self, root, levels, level):
        if not root: 
            return
        if level == len(levels):
            levels.append([])
        levels[level].append(root.val)
        self.dfs(root.left, levels, level + 1)
        self.dfs(root.right, levels, level + 1)

# BFS (Iteration)

# BFS (Recursion)

```

```python
# Tree - DFS

# Tree- BFS  

# Graph- DFS

# Graph -BFS  

```

- Ref
    - https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/walls-and-gates.py
    - https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Depth-First-Search/path-sum.py

## 2) Binary Search Tree (BST)

- Tricks

- Algorithm 

- Example 

```python 
# Convert Sorted Array -> Binary  
# leetcode #108 
class Solution(object):
    def sortedArrayToBST(self, nums):
        """
        :type nums: List[int]
        :rtype: TreeNode
        """
        if not nums: return None
        _len = len(nums)
        mid = _len // 2
        root = TreeNode(nums[mid])
        root.left = self.sortedArrayToBST(nums[:mid])
        root.right = self.sortedArrayToBST(nums[mid+1:])
        return root
```

```python 
# linked list -> Binary Search Tree (BST)
class tree:
    def __init__(self):
        self.data = 0
        self.left = None
        self.right = None

class Solution(object):
    def linkedListToBST(self, root, val):
        newnode = tree()
        newnode.data = val
        newnode.left = None
        newnode.right = None 
        if root == None:
            root = newnode
            return root 
        else:
            current = root 
            while current != None:
                backup = current
                if current.data > val:
                    current = current.left
                else:
                    current = current.right 
                if backup.data > val:
                    backup.left = newnode
                else:
                    backup.right = newnode
        return root 
```

```python
# Binary Search Tree (BST) preorder/inorder/postorder traversal (linked list)
# leetcode #94 Binary Tree Inorder Traversal, 
# leetcode #144 Binary Tree Preorder Traversal

class tree:
    def __init__(self):
        self.data = 0
        self.left = None
        self.right = None

def preorder(ptr):
    if ptr != None:
        print (ptr.data)
        preorder(ptr.left)
        preorder(ptr.right)

def inorder(ptr):
    if ptr != None:
        inorder(ptr.left)
        print (prt.data)
        inorder(ptr.right)

def postorder(ptr):
    if ptr != None:
        postorder(ptr.left)
        postorder(ptr.right)
        print (ptr.data)

def linkedListToBST(root, val):
    newnode = tree()
    newnode.data = val
    newnode.left = None
    newnode.right = None 
    if root == None:
        root = newnode
        return root 
    else:
        current = root 
        while current != None:
            backup = current
            if current.data > val:
                current = current.left
            else:
                current = current.right 
            if backup.data > val:
                backup.left = newnode
            else:
                backup.right = newnode
    return root 

```

```python
# Binary search tree (BST) "search" demo 
# class tree: # same as above 
# ....
def search(prt, val):
    i = 1 
    while True:
        if prt == None: # if can't find in BST, return None 
            return None 
        if ptr == val:
            print ('totally search {} times'.format(i))
            return ptr 
        elif ptr.dara > val:
            ptr = ptr.left 
        else:
            ptr = ptr.right 
        i += 1 
```

```python
# Binary search tree (BST) : add node & delete node 
# 1) Add node 
# class tree: # same as above 
def addNode():
    arr, ptr  = [7,1,3,4,10,9,5,6], None 
    for i in range(arr):
        ptr = linkedListToBST(ptr, arr[i])
    data = int(input('please enter to-insert value'))
    if search(ptr, data) != None:
        print ('the node already existed')
    else:
        ptr = linkedListToBST(ptr, data)
        inorder(ptr)

# 2) Delete node 
# leetcode #450 Delete Node in a BST
def deleteNode(root, key):
    """
    :type root: TreeNode
    :type key: int
    :rtype: TreeNode
    """
    if not root: return None
    if root.val == key:         
        if not root.right: 
            # return left subtree if there is no right subtree
            left = root.left
            return left
        else:
            # return right subtree if there is no left subtree
            right = root.right
            while right.left:
                right = right.left
            root.val, right.val = right.val, root.val
    root.left = self.deleteNode(root.left, key)
    root.right = self.deleteNode(root.right, key)
    return root

```

## 2) B Tree 

- Tricks

- Algorithm 

- Example 


## 3) Queue 

- Tricks
    - First in, first out (FIFO)
    - 2 basic operations : enqueue and dequeue (add and delete)

- Algorithm 

- Example 
```python
# Build a queue via list 
class listToQueue(object):

    def __init__(self, size=10):
        self.queue = []
        self.front = -1
        self.rear = -1
        self.size = size

    def __str__(self):
        return ''.join([ str(i) for i in self.queue ])

    def enqueue(self, item):
        if rear == self.size -1:
            print ('quque is full')
        else:
            rear += 1 
            self.queue[rear] = item

    def dequue(self, item):
        if rear == front:
            print ('queue is blank')
        else:
            front += 1 
            self.queue[front] = 0 
```

```python
# Build a queue via linked-list 
# dev, to double check 
class linkedlistToQueue(object):
    def __init__(self):
        self.name = ' '*20
        self.score = 0 
        self.next = None 
        self.front = None
        self.rear = None

    def enqueue(self, name, score):
        new_data = linkedlistToQueue() ### to check if it works 
        new_data.name = name 
        new_data.score = score 
        if self.rear == None:
            self.front = new_data
        else:
            self.rear.next = new_data
        self.rear = new_data
        new_data.next = None 

    def dequeue(self):
        if self.front == None:
            print ('queue is blank')
        else:
            print ('name : {}, score : {}'.format(self.front.name, front.front.score))
            self.front =  self.front.next 

    def show(self):
        ptr = self.front
        if prt == None:
            print ('quque is blank')
        else:
            while ptr != None:
                print ('name : {}, score : {}'.format(self.front.name, front.front.score))
            ptr = ptr.next 

```

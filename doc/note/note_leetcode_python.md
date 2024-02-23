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
# leetcode #841 Keys and Rooms
# DFS
class Solution:
    def canVisitAllRooms(self, rooms):
        """
        :type rooms: List[List[int]]
        :rtype: bool
        """
        visited = [0] * len(rooms)
        self.dfs(rooms, 0, visited)
        return sum(visited) == len(rooms)
        
    def dfs(self, rooms, index, visited):
        visited[index] = 1
        for key in rooms[index]:
            if not visited[key]:
                self.dfs(rooms, key, visited) 
                
# BFS 
import collections
class Solution:
    """
    @param rooms: a list of keys rooms[i]
    @return: can you enter every room
    """
    def canVisitAllRooms(self, rooms):
        # Write your code here
        q, seen = collections.deque([0]), {0}        
        while q:
            if len(seen) == len(rooms): return True
            k = q.popleft()            
            for v in rooms[k]:
                if v not in seen:
                    seen.add(v)
                    q.append(v)                  
        return len(seen) == len(rooms)

```
```python
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

# leetcode # 490 the maze 
class Solution(object):
    def hasPath(self, maze, start, destination):

        def dfs(x, y):
            if [x, y] == destination:
                return True

            if (x, y) in visited:
                return False    

            visited.add((x, y))

            for dx, dy in [(-1,0),(1,0),(0,-1),(0,1)]:
                new_x, new_y = x, y

                while  ( 0 <= new_x + x < row and 0 <= new_y + y < col and maze[new_x + x ][new_y + y] == 0):
                    new_x += dx
                    new_y += dy 

                    if dfs(new_x, new_y):
                        return True
            return False

        row, col  = len(maze), len(maze[0])
        visited = set()
        return dfs(start[0], start[1])
```

```python
# BFS (Iteration)
```

```python
# BFS (Recursion)
# leetcode # 490 the maze 

```

```python
# Tree - DFS
```

```python
# Tree- BFS 
``` 

```python
# Graph- DFS
```

```python
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
# Convert Sorted Array -> Binary Search Tree (BST)  
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

## 4) Stack 

- Tricks
    - Last in, first out (LIFO)
    - 5 basic operations : create, push(put), pop(delete), isEmpty, full
    - Scenario : 1) inverse traversal 

- Algorithm 

- Example 
```python
# Build a stack via list 
class listToStack(object):

    def __init__(self):
        self.top = -1 
        self.MAXSTACK = 100 # define max stack length 
        self.stack = [None]* self.MAXSTACK

    def isEmpty(self):
        if top == -1:
            return True
        else:
            return False

    def push(self, data):
        top = self.top
        MAXSTACK = self.MAXSTACK
        if top > MAXSTACK - 1:
            print ('stack full, can not add nore')
        else:
            top += 1 
            stack[top] = data 

    def pop(self):
        if self.isEmpty():
            print ('no data to pop, stack is blank')
        else:
             print ('pop element : {}'.format(stack[top]))
             top -= 1 

```

```python
# Build a stack via list 
class linkedistToStack(object):

    def __init__(self):
        self.data = 0 
        self.top = None 
        self.next = None 

    def isEmpty(self):
        if self.top == None:
            return True
        else:
            return False

    def push(self, data):
        top = self.top
        new_add_node = linkedistToStack() ### to check if this works
        new_add_node.data = data          # node's data as input data
        new_add_node.next = top           # point new node to the top 
        top = new_add_node                # new node as the top (stack)

    def pop(self):
        top = self.top
        is self.isEmpty():
            print ('stack is blank')
            return -1 
        else:
            ptr = top        # point to the top of stack  
            top = top.next   # move to the next of top stack 
            tmp = top.data  # get the stack data 
            return tmp       # teturn the data 

```


## 5) Math 

- Tricks

- Algorithm 

- Example 
```python
# leetcode #415 Add Strings
class Solution(object):
    def addString(self, num1, num2):
        result = []
        idx1, idx2, carry = len(num1), len(num2), 0 
        while idx1 or idx2 or carry: # while there is still non-add digit in num1, and num2; or there is non-zero carry 
            digit = carry:
            if idx1:
                idx1 -= 1 # add from rightest digit (inverse from num1 )
                digit += int(num1[idx1])
            if idx2:
                idx2 -= 1 # add from rightest digit (inverse from num1 )
                digit += int(num2[idx2])
            carry =  True if digit > 9 else False  # true if digit (e.g. 10,11...), so carry == True and will do addition to next digit. vice versa.
            result.append(str(digit % 10))
        return ''.join(result[::-1])

```

```python 
# leetcode # 405 Convert a Number to Hexadecimal
def toHex(num):
    ret = ''
    map = ('0', '1','2','3','4','5','6','7','8','9','a','b','c','d','e','f')
    if num == 0:
        return '0'
    if num < 0:
        num += 2**32 # if num < 0, use num = num + 2**32 to deal with it 
    while num > 0 :
        num, val = divmod(num, 16)
        ret += map[val]
    return ret[::-1]
```
```python
# leetcode # 067 Add Binary  
class Solution(object):
    def addBinary(self, a, b):
        i, j, plus = len(a)-1, len(b)-1, 0 
        while i >= 0 or j >= 0 or plus==1:
            plus += int(a[i]) if i >= 0 else 0 
            plus += int(b[i]) if j >= 0 else 0 
            res = str(plus % 2) + res 
            i, j, plus = i-1, j-1, plus/2
        return res 
```

## 5) Recursion  

- Tricks
    - Given a function has logics/sub-function call itself 
    - Using cases : 1) repeated same process 2) has the terminated condition
    - Can refactor the code in an elegant/simple way

- Algorithm 

- Example 
```python 
# tower of Hanoi 
# IDEA
# STEP 1) move 1,2 ... n-1 from 1 -> 2 
# STEP 2) move n from 1 -> 3 
# STEP 3) move n-1, n-2, ....2, 1 from 2 -> 3 
def hanoi(n, p1, p2, p3):
    if n == 1:
        print ('move plate from {} to {}'.format(p1, p3))
    else:
        hanoi(n-1, p1, p3, p2)
        hanoi(n-1, p2, p1, p3)
```

## 6) Hash Table 

- Tricks

- Algorithm 

- Example 
```python
# 325  Maximum Size Subarray Sum Equals k
class Solution(object):
    def maxSubArrayLen(self, nums, k):

        result, acc = 0, 0
        dic = {0: -1}

        for i in range(len(nums)):
            acc += nums[i]
            if acc not in dic:
                dic[acc] = i
            if acc - k in dic:
                result = max(result, i - dic[acc-k])
        return result

```

## 6) String

- Tricks

- Algorithm 

- Example 

```python 

# Hackerrank : Find all palindrome substring
# example : 
# given string = "aba"
# return : ['a', 'b', 'aba']

# https://stackoverflow.com/questions/32488872/find-all-combinations-of-palindromic-substrings
def palindromic_substrings(s):
    if not s:
        return [[]]
    results = []
    for i in range(len(s), 0, -1):
        sub = s[:i]
        if sub == sub[::-1]:
            for rest in palindromic_substrings(s[i:]):
                results.append([sub] + rest)
    return results
```

```python

# Hackerrank : Substring Calculator
# example :
# data= 'abc'
# output : 'a', 'b', 'c', 'ab', 'ac', 'bc', 'abc'

# V0
# https://stackoverflow.com/questions/43413277/calculating-the-substrings-of-a-string-in-python
def substring_calculator1(data):
    from itertools  import combinations
    for num in range(1,len(data)+1):
        for i in combinations(data,num):
            print (''.join(i))

# V1 
# IDEA : combinations
# https://docs.python.org/3/library/itertools.html#itertools.combinations
# soruce code of combinations in python itertools class 
def _combinations(iterable, r):
    # combinations('ABCD', 2) --> AB AC AD BC BD CD
    # combinations(range(4), 3) --> 012 013 023 123
    pool = tuple(iterable)
    n = len(pool)
    if r > n:
        return
    indices = list(range(r))
    yield tuple(pool[i] for i in indices)
    while True:
        for i in reversed(range(r)):
            if indices[i] != i + n - r:
                break
        else:
            return
        indices[i] += 1
        for j in range(i+1, r):
            indices[j] = indices[j-1] + 1
        yield tuple(pool[i] for i in indices)

def substring_calculator1(data):
    for num in range(1,len(data)+1):
        for i in _combinations(data,num):
            print (''.join(i))

# V2 
# IDEA : permutations
# https://docs.python.org/3/library/itertools.html#itertools.permutations
def _permutations(iterable, r=None):
    # permutations('ABCD', 2) --> AB AC AD BA BC BD CA CB CD DA DB DC
    # permutations(range(3)) --> 012 021 102 120 201 210
    pool = tuple(iterable)
    n = len(pool)
    r = n if r is None else r
    if r > n:
        return
    indices = list(range(n))
    cycles = list(range(n, n-r, -1))
    yield tuple(pool[i] for i in indices[:r])
    while n:
        for i in reversed(range(r)):
            cycles[i] -= 1
            if cycles[i] == 0:
                indices[i:] = indices[i+1:] + indices[i:i+1]
                cycles[i] = n - i
            else:
                j = cycles[i]
                indices[i], indices[-j] = indices[-j], indices[i]
                yield tuple(pool[i] for i in indices[:r])
                break
        else:
            return

def _combinations(iterable, r):
    pool = tuple(iterable)
    n = len(pool)
    for indices in _permutations(range(n), r):
        if sorted(indices) == list(indices):
            yield tuple(pool[i] for i in indices)

def substring_calculator1(data):
    for num in range(1,len(data)+1):
        for i in _combinations(data,num):
            print (''.join(i))

```

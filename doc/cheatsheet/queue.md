# Queue 
<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/queue2.png"></p>
- first in first out (FIFO)


<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/stack_vs_queue.png"></p>



## 0) Concept

- An ordering linear data structure
- can be implemented by either `array` or `linkedlist`
- first in first out (FIFO)
- [Java Queue](https://juejin.cn/post/7032838878696308766)
    - Low level : Linked list or Array
- Used when transfer `recursive` code to `iterative`
    - via FIFO properties, we're sure that the `earlier reached` code is executed first, then the `following code`,... and so on

### 0-1) Types

- Queue basic op
    - LC 225 (note: `1 is the head` of queue [1,2,3])
- Priority queue (PQ)
- Monotonic queue
    - LC 239
- double-ended queue (deque)
- design circulr queue
    - LC 622

### 0-2) Pattern

## 1) General form

```python
# python
#------------------
# Queue
#------------------
class Queue(object):
    def __init__(self, limit = 10):
        self.queue = []
        self.front = None
        self.rear = None
        self.limit = limit
        self.size = 0

    def __str__(self):
        return ' '.join([str(i) for i in self.queue])

    # to check if queue is empty
    def isEmpty(self):
        return self.size <= 0

    # to add an element from the rear end of the queue
    def enqueue(self, data):
        if self.size >= self.limit:
            return -1          # queue overflow
        else:
            """
            BEWARE OF IT
            -> the queue is in "inverse" order to the array which is the way we implement here in python 
            i.e. 
                q = [1,2,3]
                but the q is start from 1, end at 3 actually 
            e.g.
                dequeue <---- 1, 2 ,3  <---- enqueue 
            """
            self.queue.append(data)

        # assign the rear as size of the queue and front as 0
        if self.front is None:
            self.front = self.rear = 0
        else:
            self.rear = self.size
            
        self.size += 1

    # to pop an element from the front end of the queue
    def dequeue(self):
        if self.isEmpty():
            return -1          # queue underflow
        else:
            """
            BEWARE OF IT 
            x = [1,2,3]
            x.pop(0)
            -> x = [2,3]
            """
            self.queue.pop(0)
            self.size -= 1
            if self.size == 0:
                self.front = self.rear = 0
            else:
                self.rear = self.size - 1

    def getSize(self):
        return self.size
```

```java
// ----------------
// Monotonic Queue
// ----------------
// java
// algorithm book (labu) p. 278, p.281
// LC 239
class MonotonicQueue{

    // init queue
    LinkedList<Integer> q = new LinkedList<>();
    
    // add element to queue tail
    public void push(int n){
        while (!q.isEmpty() && q.getLast() < n){
            q.pollLast();
        }
        q.addLast(n);
    }

    // return current max value in queue
    public int max(){
        return q.getFirst();
    }

    // if head element is n, delete it
    public void pop(int n){
        if (n == q.getFirst()){
            q.pollLast();
        }
    }
}
```

### 1-1) Basic OP
- insert
- pop
- popleft
- isEmpty


```java
// java
// LC 225
class MyStack_2 {

    private Queue<Integer> queue = new LinkedList<>();

    public void push(int x) {

        /**
         *  NOTE !!!
         *
         *   we still add element to queue first
         *
         */
        queue.add(x);
        /**
         *  NOTE !!!
         *
         *  below op:
         *
         *   Step 2: Reordering to simulate stack behavior
         *
         *     - After adding the new element, we want the new element to
         *       become the first element in the queue
         *       (because it should be the last one to be popped).
         *
         *     -  The for-loop begins by iterating over all elements
         *        except the one just added (i = 1 to i < queue.size()).
         *
         *     - Inside the loop, queue.remove() removes the front
         *       element of the queue (this is the oldest element in the queue),
         *       and queue.add() adds it back to the back of the queue.
         *
         *     - By repeating this process for all the elements in the queue
         *       (except the last added one), we effectively "rotate"
         *       the queue such that the most recently added element is moved
         *       to the front of the queue.
         *
         *
         *
         *   Example:
         *
         *
         *  Suppose you have the queue: [1, 2, 3] and you call push(4).
         *
         *  (NOTE !!! 1 is the head of queue, per [1,2,3] example )
         *
         * Initially, the queue will look like: [1, 2, 3, 4].
         *
         * After the for-loop runs:
         *
         * First, 1 is moved to the back, resulting in: [2, 3, 4, 1].
         *
         * Then, 2 is moved to the back, resulting in: [3, 4, 1, 2].
         *
         * Then, 3 is moved to the back, resulting in: [4, 1, 2, 3].
         *
         * Now, the most recent element (4) is at the front of the queue, simulating the stack behavior (LIFO).
         *
         *
         */
        for (int i=1; i < queue.size(); i++)
            queue.add(queue.remove());
    }

    public void pop() {
        queue.remove();
    }

    public int top() {
        return queue.peek();
    }

    public boolean empty() {
        return queue.isEmpty();
    }
}
```

### 1-2) Queue

### 1-3) Double-ended Queue
> deque

```python
# https://pymotw.com/2/collections/deque.html
"""
A double-ended queue, or deque, 
supports adding and removing elements from either end. 
The more commonly used stacks and queues are degenerate forms of deques, 
where the inputs and outputs are restricted to a single end.
"""

#------------
# basic op
#------------
import collections
d = collections.deque('abcdefg')
print (d)
print (len(d))
print ('left end :', d[0])
print ('right end :', d[-1])
print ('pop :' , d.pop())
print (d)
print ('pop_left:' , d.popleft())
print (d)

#------------
# populating
#------------
# -> A deque can be populated from either end, termed “left” and “right” in the Python implementation.

In [18]: b = collections.deque()
    ...: print (b)
deque([])

In [19]: b.extend('abcdefg') # same as collections.deque('abcdefg')
    ...: print (b)
deque(['a', 'b', 'c', 'd', 'e', 'f', 'g'])

In [20]: b.append('h')
    ...: print (b)
deque(['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'])

In [21]: b.extendleft('0')
    ...: print (b)
deque(['0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'])


#------------
# consuming
#------------

In [23]: import collections
    ...: d = collections.deque('abcdefg')
    ...: print (d)
deque(['a', 'b', 'c', 'd', 'e', 'f', 'g'])

In [24]:
    ...: d.pop() # d.pop(-1)
    ...: print (d)
    ...:
deque(['a', 'b', 'c', 'd', 'e', 'f'])

In [25]: d.popleft()
    ...: print (d)
deque(['b', 'c', 'd', 'e', 'f'])
```

### 1-4) Stack simulate Queue

## 2) LC Example

### 2-1) Sliding Window Maximum

```python
# LC 239 Sliding Window Maximum (hard)
# V1
# http://bookshadow.com/weblog/2015/07/18/leetcode-sliding-window-maximum/
# IDEA : DEQUE
class Solution:
    def maxSlidingWindow(self, nums, k):
        dq = collections.deque()
        ans = []
        for i in range(len(nums)):
            while dq and nums[dq[-1]] <= nums[i]:
                dq.pop()
            dq.append(i)
            if dq[0] == i - k:
                dq.popleft()
            if i >= k - 1:
                ans.append(nums[dq[0]])
        return ans
```

```java
// LC 239 Sliding Window Maximum (hard)
// algorithm book (labu) p. 278, p.281
// java

// monotonic queue
class MonotonicQueue{

    // init queue
    LinkedList<Integer> q = new LinkedList<>();
    
    // add element to queue tail
    public void push(int n){
        while (!q.isEmpty() && q.getLast() < n){
            q.pollLast();
        }
        q.addLast(n);
    }

    // return current max value in queue
    public int max(){
        return q.getFirst();
    }

    // if head element is n, delete it
    public void pop(int n){
        if (n == q.getFirst()){
            q.pollLast();
        }
    }
}

/* main func */
int[] maxSlidingWindow([int] nums, int k){
    MonotonicQueue window = new MonotonicQueue();
    List<Integer> res = new ArrayList<>();

    for (int i = 0; i < nums.length; i++){
        if (i < k - 1){
            // insert k - 1 elements in the window
            window.push(nums[i]);
        }else{
            // window move forward, add new element
            window.push(nums[i]);
            // record current max element in the window
            res.add(window.max());
            // move out element
            window.pop(nums[i - k + 1]);
        }
    }

    // transform res to int[] array as answer form
    int[] arr = new int[res.size()];
    for (int i = 0; i < res.size(); i ++){
        arr[i] = res.get(i);
    }
    return arr;
}
```

### 2-2) Design Circular Queue

```python
# LC 622. Design Circular Queue
# V0 
# IDEA : ARRAY
# https://leetcode.com/problems/design-circular-queue/solution/

# NOTE !!! when `circular`, -> think about `fixed_idx = idx % len`

class MyCircularQueue:

    def __init__(self, k):
        """
        Initialize your data structure here. Set the size of the queue to be k.
        """
        self.queue = [0]*k
        self.headIndex = 0
        self.count = 0
        self.capacity = k

    def enQueue(self, value):
        """
        Insert an element into the circular queue. Return true if the operation is successful.
        """
        if self.count == self.capacity:
            return False
        self.queue[(self.headIndex + self.count) % self.capacity] = value
        self.count += 1
        return True

    def deQueue(self):
        """
        Delete an element from the circular queue. Return true if the operation is successful.
        """
        if self.count == 0:
            return False
        self.headIndex = (self.headIndex + 1) % self.capacity
        self.count -= 1
        return True

    def Front(self):
        """
        Get the front item from the queue.
        """
        if self.count == 0:
            return -1
        return self.queue[self.headIndex]

    def Rear(self):
        """
        Get the last item from the queue.
        """
        # empty queue
        if self.count == 0:
            return -1
        return self.queue[(self.headIndex + self.count - 1) % self.capacity]

    def isEmpty(self):
        """
        Checks whether the circular queue is empty or not.
        """
        return self.count == 0

    def isFull(self):
        """
        Checks whether the circular queue is full or not.
        """
        return self.count == self.capacity

# V0'
# IDEA : LINKED LIST
# https://leetcode.com/problems/design-circular-queue/solution/
class Node:
    def __init__(self, value, nextNode=None):
        self.value = value
        self.next = nextNode

class MyCircularQueue:

    def __init__(self, k):
        """
        Initialize your data structure here. Set the size of the queue to be k.
        """
        self.capacity = k
        self.head = None
        self.tail = None
        self.count = 0

    def enQueue(self, value):
        """
        Insert an element into the circular queue. Return true if the operation is successful.
        """
        if self.count == self.capacity:
            return False
        
        if self.count == 0:
            self.head = Node(value)
            self.tail = self.head
        else:
            newNode = Node(value)
            self.tail.next = newNode
            self.tail = newNode
        self.count += 1
        return True


    def deQueue(self):
        """
        Delete an element from the circular queue. Return true if the operation is successful.
        """
        if self.count == 0:
            return False
        self.head = self.head.next
        self.count -= 1
        return True


    def Front(self):
        """
        Get the front item from the queue.
        """
        if self.count == 0:
            return -1
        return self.head.value

    def Rear(self):
        """
        Get the last item from the queue.
        """
        # empty queue
        if self.count == 0:
            return -1
        return self.tail.value
    
    def isEmpty(self):
        """
        Checks whether the circular queue is empty or not.
        """
        return self.count == 0

    def isFull(self):
        """
        Checks whether the circular queue is full or not.
        """
        return self.count == self.capacity
```
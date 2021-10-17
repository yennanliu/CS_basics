# Queue 

## 0) Concept  
- A ordering linear data structure
- can be implemented by `array` or `linkedlist`
- first in first out

### 0-1) Types
- Queue
- Priority queue

### 0-2) Pattern

## 1) General form
```python
# python
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

### 1-1) Basic OP

### 1-2) Queue

### 1-3) Double-ended Queue
```python
# https://pymotw.com/2/collections/deque.html
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
```
### 1-4) Stack simulate Queue

## 2) LC Example
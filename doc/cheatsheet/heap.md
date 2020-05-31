# Heap cheatsheet 

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

### 1-2) Heap VS Stack VS Queue

### 1-3) Heap sort
```python
# https://docs.python.org/zh-tw/3/library/heapq.html
def heapsort(iterable):

    h = []
    for value in iterable:
        heappush(h, value)
    return [heappop(h) for i in range(len(h))]

# heapsort([1, 3, 5, 7, 9, 2, 4, 6, 8, 0])
# >>> [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

``` 

### 1-4) Priority Queue

## 2) LC Example

### 2-1) Kth Largest Element in a Stream
```python
# 703 Kth Largest Element in a Stream
class KthLargest(object):

    def __init__(self, k, nums):
        self.pool = nums
        self.size = len(self.pool)
        self.k = k
        heapq.heapify(self.pool)
        while self.size > k:
            heapq.heappop(self.pool)
            self.size -= 1

    def add(self, val):
        if self.size < self.k:
            heapq.heappush(self.pool, val)
            self.size += 1
        elif val > self.pool[0]:
            heapq.heapreplace(self.pool, val)
        return self.pool[0]
```
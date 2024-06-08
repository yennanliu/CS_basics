# Priority Queue (PQ) 

## 0) Concept

- Priority queue is one of the implementations of heap
- Not follow "FIFO", but pop elelement with priority
- https://realpython.com/queue-in-python/
- https://docs.python.org/zh-tw/3/library/heapq.html#priority-queue-implementation-notes

### 0-1) Types

### 0-2) Pattern

### 0-3) Use case

- PQ task management
- BFS with PQ
- Dijkstra’s Algorithm with PQ

## 1) General form

### 1-1) Basic OP

#### 1-1-1) Init a small, big PQ in java

```java
// java

// Small PQ (default min-heap)
PriorityQueue<Integer> smallPQ = new PriorityQueue<>();

// Big PQ (max-heap)
PriorityQueue<Integer> bigPQ = new PriorityQueue<>(Comparator.reverseOrder());

// Add elements to PQs
smallPQ.add(5);
smallPQ.add(10);
smallPQ.add(1);

bigPQ.add(5);
bigPQ.add(10);
bigPQ.add(1);

// Print elements from PQs
System.out.println("Small PQ (min-heap):");
while (!smallPQ.isEmpty()) {
    System.out.println(smallPQ.poll());
}

System.out.println("Big PQ (max-heap):");
while (!bigPQ.isEmpty()) {
    System.out.println(bigPQ.poll());
} 
```

## 2) LC Example


### 2-1) Kth Largest Element in an Array
```java
// LC 215
```


### 2-2) Last Stone Weight
```java
// LC 1046
```


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


#### 1-1-2) Custom `sorting` in PQ

```java
// java
// LC 973

// ...

PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
    @Override
    public int compare(int[] a, int[] b) {
        /** 
         *  NOTE !!! below
         * 
         * -> we get custom val form method,
         *    then sort PQ customly with such value
         * 
         */
        double distA = getDist(a[0], a[1]);
        double distB = getDist(b[0], b[1]);
        return Double.compare(distA, distB); // compare properly
    }
});


// custom func
public double getDist(int x, int y) {
return Math.sqrt(x * x + y * y);
}

// ...
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

### 2-3)  Kth Largest Element in a Stream

```java
// java
// LC 703
// V0-1
// IDEA: SMALL PQ (fixed by gpt)
class KthLargest_0_1 {
    int k;
    int[] nums;
    PriorityQueue<Integer> pq;

    public KthLargest_0_1(int k, int[] nums) {
        this.k = k;
        this.nums = nums; // ???
        /**
         * // NOTE !!! we use small PQ
         * e.g. : small -> big (increasing order)
         *
         * -> so, we pop elements when PQ size  > k
         * -> and we're sure that the top element is the `k th biggest element
         * -> e.g. (k-big, k-1 big, k-2 big, ...., 2 big, 1 big)
         */
        this.pq = new PriorityQueue<>();
        // NOTE !!! we also need to add eleemnts to PQ
        for (int x : nums) {
            pq.add(x);
        }
    }

    public int add(int val) {
        this.pq.add(val);
        // NOTE !!! need to remove elements when PQ size > k
        while (this.pq.size() > k) {
            this.pq.poll();
        }
        if (!this.pq.isEmpty()) {
            return this.pq.peek();
        }
        return -1;
    }
}
```

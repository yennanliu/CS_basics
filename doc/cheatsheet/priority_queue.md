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

#### 1-1-2-1) Custom `sorting with external data structure` in PQ

```java
// java

// LC 767

// ...
Map<Character, Integer> charCount = new HashMap<>();
    for (char c : s.toCharArray()) {
        charCount.put(c, charCount.getOrDefault(c, 0) + 1);
    }


// NOTE !!! below 
PriorityQueue<Character> maxHeap = new PriorityQueue<>(new Comparator<Character>() {

    // NOTE !!! below 
    private final Map<Character, Integer> counts = charCount; // Access the charCount map

    @Override
    public int compare(Character char1, Character char2) {
        // Compare based on the frequencies from the charCount map (descending order)
        return counts.get(char2) - counts.get(char1);
    }
});

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

### 2-4) Minimum Path Sum

```java
// java
// LC 64

  /**  NOTE !!!  LC 64 VS LC 1631
   *
   *
   * ✅ Leetcode 64: Minimum Path Sum
   *
   * Key property:
   *    •   You can only move right or down.
   *    •   The cost is accumulative: you sum values along the path.
   *    •   Since you can’t revisit a cell from a different direction, you don’t need visited.
   *    •   DP is perfect here. Every cell is updated once with the best possible value from top or left.
   *
   * ✅ No visited needed:
   *    •   Each cell is filled once.
   *    •   You never have to worry about improving a previous path.
   *    •   No cycles. No need to guard against revisiting.
   *
   * ⸻
   *
   * 🔁 Leetcode 1631: Path With Minimum Effort
   *
   * Key property:
   *    •   You can move in all four directions (up/down/left/right).
   *    •   Cost is not additive, it’s based on the maximum absolute height difference between steps.
   *    •   You might find a better path to a cell after already visiting it.
   *    •   This is Dijkstra-style, but the edge weight is non-linear (max of step costs).
   *
   * ✅ visited is needed here:
   *    •   You must revisit nodes if a better path is found.
   *    •   To avoid processing worse paths, you mark nodes as visited once the minimum effort to reach them is finalized.
   *    •   Without visited, you could end up adding multiple paths for the same cell and wasting computation.
   *
   * ⸻
   *
   * 🔍 Summary:
   *
   * Problem    Move Directions Cost Definition Can revisit cells with better cost? Needs visited?
   * Minimum Path Sum (64)  Right + Down only   Sum of grid values  ❌ No    ❌ No
   * Path With Minimum Effort (1631)    All 4 directions    Max of step differences ✅ Yes   ✅ Yes
   *
   */


// V0-1
// IDEA: MIN PQ + BFS (fixed by gpt)
public int minPathSum_0_1(int[][] grid) {
    if (grid == null || grid.length == 0 || grid[0].length == 0) {
        return 0;
    }

    int m = grid.length;
    int n = grid[0].length;

    // Min-heap by cost
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
    pq.offer(new int[] { 0, 0, grid[0][0] });

    boolean[][] visited = new boolean[m][n];
    int[][] dirs = new int[][] { { 0, 1 }, { 1, 0 } };

    while (!pq.isEmpty()) {
        int[] cur = pq.poll();
        int row = cur[0];
        int col = cur[1];
        int cost = cur[2];

        if (row == m - 1 && col == n - 1) {
            return cost;
        }

        if (visited[row][col]) {
            continue;
        }
        visited[row][col] = true;

        for (int[] dir : dirs) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (newRow < m && newCol < n) {
                pq.offer(new int[] { newRow, newCol, cost + grid[newRow][newCol] });
            }
        }
    }

    return -1; // shouldn't reach here if input is valid
}
```

# Queue 佇列

> **範圍** — FIFO 的基本功：BFS 佇列、雙端佇列、環形緩衝區，以及以佇列為底的設計題。
> **另見**：[monotonic_queue.md](./monotonic_queue.md) — 滑動視窗最大／最小值；[heap.md](./heap.md) — 排序依據是優先級而非到達順序時；[bfs.md](./bfs.md) — 消耗佇列的那個演算法；[stack.md](./stack.md) — LIFO 的對照組。

## LeetCode 題目清單

- [Queue](https://leetcode.com/problem-list/queue/)

## 時間複雜度

| 資料結構 | 搜尋 | 插入 | 刪除 | Min/Max |
| -------------- | -------- | -------- | -------- | -------- |
| 佇列 | O(n) | O(1) | O(1) | O(n) |

> 插入 = enqueue（尾端），刪除 = dequeue（前端），兩者都是 **O(1)**——但前提是底層是鏈結串列／雙端佇列；用陣列從前端 dequeue 的天真作法是 **O(n)**。滑動視窗上的 Min/Max 可以用單調雙端佇列做到攤還 **O(1)**（[monotonic_queue.md](./monotonic_queue.md)）。空間是 **O(n)**。

<p align="center"><img src="../pic/queue2.png"></p>

<p align="center"><img src="../pic/stack_vs_queue.png"></p>

## 總覽
**佇列**是遵守先進先出（FIFO）原則的線性資料結構。元素從尾端加入（enqueue）、從前端移除（dequeue），就像現實生活中排隊一樣。

### 關鍵性質
- **複雜度**：看上面的[時間複雜度](#time-complexity)表
- **核心想法**：最先放進去的元素最先被拿出來（FIFO）
- **什麼時候用**：BFS 走訪、逐層處理、任務排程、緩衝

### 實作選項
- **陣列版**：固定大小，用環形緩衝區提升效率
- **鏈結串列版**：大小可動態成長，操作有效率
- **雙端佇列**：兩端都能存取
- **優先佇列**：依優先級處理元素（另篇討論）

### 參考資料
- [Java Queue Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html)
- [Python collections.deque](https://docs.python.org/3/library/collections.html#collections.deque)
- [Queue vs Stack Comparison](https://www.geeksforgeeks.org/difference-between-stack-and-queue-data-structures/)

## 題型分類

### **模式 1：BFS 與層序走訪** — LC 102
- **描述**：在樹與圖上一層一層處理
- **範例題**：LC 102、103、107、199、513、515、637
- **模式**：先把當前這層的節點全部處理完，再進到下一層

### **模式 2：搭配佇列的滑動視窗** — LC 239
- **描述**：用 FIFO 順序維護視窗狀態
- **範例題**：LC 239、346、362、933、1438
- **模式**：用雙端佇列讓兩端操作都是 O(1)

### **模式 3：設計佇列的各種變形** — LC 232
- **描述**：在特定限制或特殊功能下實作佇列
- **範例題**：LC 225、232、622、641、1670
- **模式**：用堆疊、陣列或鏈結串列，配上特定邏輯

### **模式 4：單調佇列** — LC 239
- **描述**：在佇列中維持遞增／遞減的順序
- **範例題**：LC 239、862、907、1425、1696
- **模式**：把破壞單調性的元素移掉

### **模式 5：串流處理** — LC 346
- **描述**：處理連續進來的資料串流
- **範例題**：LC 346、352、362、703、933
- **模式**：固定大小視窗，或依時間淘汰

### **模式 6：任務排程與模擬** — LC 621
- **描述**：模擬現實世界的排隊系統
- **範例題**：LC 621、1429、1834、2073
- **模式**：在限制條件下依序處理任務

## 模板與演算法

### 模板對照表
| 模板類型 | 使用情境 | 實作方式 | 複雜度 | 什麼時候用 |
|---------------|----------|----------------|------------|-------------|
| **基本佇列** | 單純 FIFO | 陣列／LinkedList | 操作 O(1) | 一般佇列操作 |
| **環形佇列** | 固定大小緩衝區 | 陣列加指標 | 操作 O(1) | 有界緩衝區、ring buffer |
| **雙端佇列** | 兩端存取 | 雙向鏈結串列 | 操作 O(1) | 滑動視窗、迴文 |
| **單調佇列** | 維持順序 | 雙端佇列加邏輯 | 攤還 O(1) | 視窗內的最大／最小值 |
| **用堆疊做佇列** | 以堆疊實作佇列 | 兩個堆疊 | 攤還 O(1) | 面試題 |
| **層序 BFS** | 樹／圖走訪 | 佇列 + 記錄層大小 | O(n) | 逐層處理 |

### 模板 1：基本佇列操作
```python
# Python - Using collections.deque (recommended)
from collections import deque

class Queue:
    def __init__(self):
        self.queue = deque()
    
    def enqueue(self, item):
        self.queue.append(item)  # Add to rear
    
    def dequeue(self):
        if not self.is_empty():
            return self.queue.popleft()  # Remove from front
        return None
    
    def front(self):
        if not self.is_empty():
            return self.queue[0]
        return None
    
    def is_empty(self):
        return len(self.queue) == 0
    
    def size(self):
        return len(self.queue)

# Using list (less efficient for dequeue)
class SimpleQueue:
    def __init__(self):
        self.queue = []
    
    def enqueue(self, item):
        self.queue.append(item)
    
    def dequeue(self):
        if self.queue:
            return self.queue.pop(0)  # O(n) operation
        return None
```

```java
// Java - Using LinkedList
import java.util.*;

class Queue<T> {
    private LinkedList<T> queue;
    
    public Queue() {
        queue = new LinkedList<>();
    }
    
    public void enqueue(T item) {
        queue.addLast(item);  // Add to rear
    }
    
    public T dequeue() {
        if (!isEmpty()) {
            return queue.removeFirst();  // Remove from front
        }
        return null;
    }
    
    public T front() {
        if (!isEmpty()) {
            return queue.getFirst();
        }
        return null;
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public int size() {
        return queue.size();
    }
}

// Using Java Queue interface
Queue<Integer> queue = new LinkedList<>();
queue.offer(1);  // enqueue
queue.poll();    // dequeue
queue.peek();    // front
```

### 模板 2：層序 BFS 模式 — LC 102 ⭐⭐⭐⭐⭐
```python
# Python - Tree level-order traversal
def levelOrder(root):
    if not root:
        return []
    
    result = []
    queue = deque([root])
    
    while queue:
        level_size = len(queue)
        current_level = []
        
        # Process all nodes at current level
        for _ in range(level_size):
            node = queue.popleft()
            current_level.append(node.val)
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        result.append(current_level)
    
    return result

# Graph BFS with distance
def bfs(graph, start):
    visited = set([start])
    queue = deque([(start, 0)])  # (node, distance)
    
    while queue:
        node, dist = queue.popleft()
        
        for neighbor in graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append((neighbor, dist + 1))
    
    return visited
```

```java
// Java - Level-order traversal
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    
    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        List<Integer> currentLevel = new ArrayList<>();
        
        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            currentLevel.add(node.val);
            
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        
        result.add(currentLevel);
    }
    
    return result;
}
```

### 模板 3：環形佇列模式 — LC 622 ⭐⭐⭐
```python
# Python - Fixed size circular queue
class CircularQueue:
    def __init__(self, k):
        self.queue = [0] * k
        self.capacity = k
        self.head = 0
        self.count = 0
    
    def enqueue(self, value):
        if self.is_full():
            return False
        
        # Calculate tail position
        tail = (self.head + self.count) % self.capacity
        self.queue[tail] = value
        self.count += 1
        return True
    
    def dequeue(self):
        if self.is_empty():
            return False
        
        self.head = (self.head + 1) % self.capacity
        self.count -= 1
        return True
    
    def front(self):
        if self.is_empty():
            return -1
        return self.queue[self.head]
    
    def rear(self):
        if self.is_empty():
            return -1
        tail = (self.head + self.count - 1) % self.capacity
        return self.queue[tail]
    
    def is_empty(self):
        return self.count == 0
    
    def is_full(self):
        return self.count == self.capacity
```

```java
// Java - Circular Queue
class CircularQueue {
    private int[] queue;
    private int head;
    private int count;
    private int capacity;
    
    public CircularQueue(int k) {
        queue = new int[k];
        capacity = k;
        head = 0;
        count = 0;
    }
    
    public boolean enqueue(int value) {
        if (isFull()) return false;
        
        int tail = (head + count) % capacity;
        queue[tail] = value;
        count++;
        return true;
    }
    
    public boolean dequeue() {
        if (isEmpty()) return false;
        
        head = (head + 1) % capacity;
        count--;
        return true;
    }
    
    public int front() {
        if (isEmpty()) return -1;
        return queue[head];
    }
    
    public int rear() {
        if (isEmpty()) return -1;
        int tail = (head + count - 1) % capacity;
        return queue[tail];
    }
    
    public boolean isEmpty() {
        return count == 0;
    }
    
    public boolean isFull() {
        return count == capacity;
    }
}
```

### 模板 4：單調佇列模式 — LC 239 ⭐⭐⭐⭐⭐
```python
# Python - Monotonic decreasing queue for sliding window maximum
class MonotonicQueue:
    def __init__(self):
        self.queue = deque()
    
    def push(self, val):
        # Remove smaller elements from rear
        while self.queue and self.queue[-1] < val:
            self.queue.pop()
        self.queue.append(val)
    
    def pop(self, val):
        # Remove if it's the front element
        if self.queue and self.queue[0] == val:
            self.queue.popleft()
    
    def max(self):
        # Front is always the maximum
        return self.queue[0] if self.queue else None

# Sliding window maximum
def maxSlidingWindow(nums, k):
    from collections import deque
    
    dq = deque()  # Store indices
    result = []
    
    for i in range(len(nums)):
        # Remove indices outside window
        while dq and dq[0] <= i - k:
            dq.popleft()
        
        # Remove smaller elements
        while dq and nums[dq[-1]] <= nums[i]:
            dq.pop()
        
        dq.append(i)
        
        # Add to result after first window
        if i >= k - 1:
            result.append(nums[dq[0]])
    
    return result
```

```java
// Java - Monotonic Queue
class MonotonicQueue {
    private LinkedList<Integer> queue;
    
    public MonotonicQueue() {
        queue = new LinkedList<>();
    }
    
    public void push(int val) {
        // Remove smaller elements from rear
        while (!queue.isEmpty() && queue.getLast() < val) {
            queue.pollLast();
        }
        queue.addLast(val);
    }
    
    public void pop(int val) {
        // Remove if it's the front element
        if (!queue.isEmpty() && queue.getFirst() == val) {
            queue.pollFirst();
        }
    }
    
    public int max() {
        return queue.isEmpty() ? -1 : queue.getFirst();
    }
}

// Sliding window maximum
public int[] maxSlidingWindow(int[] nums, int k) {
    Deque<Integer> dq = new LinkedList<>();
    int[] result = new int[nums.length - k + 1];
    int idx = 0;
    
    for (int i = 0; i < nums.length; i++) {
        // Remove indices outside window
        while (!dq.isEmpty() && dq.peekFirst() <= i - k) {
            dq.pollFirst();
        }
        
        // Remove smaller elements
        while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i]) {
            dq.pollLast();
        }
        
        dq.offerLast(i);
        
        // Add to result after first window
        if (i >= k - 1) {
            result[idx++] = nums[dq.peekFirst()];
        }
    }
    
    return result;
}
```

### 模板 5：用堆疊實作佇列的模式 — LC 232 ⭐⭐⭐⭐
```python
# Python - Implement queue using two stacks
class MyQueue:
    def __init__(self):
        self.in_stack = []   # For enqueue
        self.out_stack = []  # For dequeue
    
    def push(self, x):
        self.in_stack.append(x)
    
    def pop(self):
        self.peek()  # Ensure out_stack has elements
        return self.out_stack.pop()
    
    def peek(self):
        if not self.out_stack:
            # Transfer all from in_stack to out_stack
            while self.in_stack:
                self.out_stack.append(self.in_stack.pop())
        return self.out_stack[-1] if self.out_stack else None
    
    def empty(self):
        return len(self.in_stack) == 0 and len(self.out_stack) == 0
```

```java
// Java - Queue using stacks
class MyQueue {
    private Stack<Integer> inStack;
    private Stack<Integer> outStack;
    
    public MyQueue() {
        inStack = new Stack<>();
        outStack = new Stack<>();
    }
    
    public void push(int x) {
        inStack.push(x);
    }
    
    public int pop() {
        peek();  // Ensure outStack has elements
        return outStack.pop();
    }
    
    public int peek() {
        if (outStack.isEmpty()) {
            // Transfer all from inStack to outStack
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
        return outStack.peek();
    }
    
    public boolean empty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }
}
```

### 模板 6：串流處理模式 — LC 346
```python
# Python - Moving average from data stream
class MovingAverage:
    def __init__(self, size):
        self.queue = deque()
        self.window_sum = 0
        self.size = size
    
    def next(self, val):
        self.queue.append(val)
        self.window_sum += val
        
        if len(self.queue) > self.size:
            removed = self.queue.popleft()
            self.window_sum -= removed
        
        return self.window_sum / len(self.queue)

# Hit counter for last 5 minutes
class HitCounter:
    def __init__(self):
        self.queue = deque()
    
    def hit(self, timestamp):
        self.queue.append(timestamp)
    
    def getHits(self, timestamp):
        # Remove hits older than 5 minutes (300 seconds)
        while self.queue and self.queue[0] <= timestamp - 300:
            self.queue.popleft()
        return len(self.queue)
```

```java
// Java - Moving average
class MovingAverage {
    private Queue<Integer> queue;
    private int windowSum;
    private int size;
    
    public MovingAverage(int size) {
        queue = new LinkedList<>();
        this.size = size;
        windowSum = 0;
    }
    
    public double next(int val) {
        queue.offer(val);
        windowSum += val;
        
        if (queue.size() > size) {
            windowSum -= queue.poll();
        }
        
        return (double) windowSum / queue.size();
    }
}
```

### 模板 7：攤平成佇列的迭代器模式 — LC 341
> **核心想法**：只要在建構子裡把*巢狀*結構攤平成一個 **FIFO 佇列**，走訪它的迭代器就變得無聊了。`next()` = `popleft()`，`hasNext()` = `佇列非空`。

```java
// LC 341 - Flatten Nested List Iterator
// IDEA: Eagerly DFS-flatten the nested list into a queue; next()/hasNext() are O(1)
// time = O(N) constructor + O(1) per next/hasNext, space = O(N)
public class NestedIterator implements Iterator<Integer> {
    private final Deque<Integer> queue = new ArrayDeque<>();

    public NestedIterator(List<NestedInteger> nestedList) {
        flatten(nestedList);
    }

    private void flatten(List<NestedInteger> list) {
        for (NestedInteger ni : list) {
            if (ni.isInteger()) {
                queue.addLast(ni.getInteger());
            } else {
                flatten(ni.getList()); // recurse into sub-list
            }
        }
    }

    @Override
    public Integer next() { return queue.pollFirst(); }

    @Override
    public boolean hasNext() { return !queue.isEmpty(); }
}
```

```python
# python
# LC 341 - Flatten Nested List Iterator
# IDEA: Eagerly DFS-flatten the nested list into a deque; next()/hasNext() are O(1)
# time = O(N) constructor + O(1) per next/hasNext, space = O(N)
from collections import deque

class NestedIterator:
    def __init__(self, nestedList):
        self.q = deque()
        self._flatten(nestedList)

    def _flatten(self, lst):
        for ni in lst:
            if ni.isInteger():
                self.q.append(ni.getInteger())
            else:
                self._flatten(ni.getList())   # recurse into sub-list

    def next(self):
        return self.q.popleft()

    def hasNext(self):
        return len(self.q) > 0
```

> **面試追問**：「如果清單超大／無限長呢？」→ 改用**延遲堆疊**版：把 `nestedList` 反轉後推入堆疊，在 `hasNext()` 裡只要頂端是清單就一直彈出並展開。攤還一樣是 O(1)，但額外空間只要 O(深度 + 頂層大小)，而不是 O(N)。

### 模板 8：首個唯一元素佇列（佇列 + 計數表）模式 — LC 387
> **核心想法**：維護一個*候選*元素佇列加一張計數表。回答之前，**從前端淘汰**掉所有計數已經超過 1 的候選。此時前端就是第一個仍然唯一的元素。它能**線上**運作（串流），這是「先計數再重掃」的解法做不到的。

```java
// LC 387 - First Unique Character in a String
// IDEA: Queue of candidate indices + freq map; evict front while it's no longer unique
// time = O(N) (each index enqueued/dequeued once), space = O(N)
public int firstUniqChar(String s) {
    int[] cnt = new int[26];
    Deque<Integer> q = new ArrayDeque<>(); // candidate indices, in arrival order

    for (int i = 0; i < s.length(); i++) {
        cnt[s.charAt(i) - 'a']++;
        q.addLast(i);
        // front is only valid while it is still unique
        while (!q.isEmpty() && cnt[s.charAt(q.peekFirst()) - 'a'] > 1) {
            q.pollFirst();
        }
    }
    return q.isEmpty() ? -1 : q.peekFirst();
}
```

```python
# python
# LC 387 - First Unique Character in a String
# IDEA: Queue of candidate indices + freq map; evict front while it's no longer unique
# time = O(N) (each index enqueued/dequeued once), space = O(N)
from collections import deque, defaultdict

def firstUniqChar(s):
    cnt = defaultdict(int)
    q = deque()  # candidate indices, in arrival order

    for i, c in enumerate(s):
        cnt[c] += 1
        q.append(i)
        # front is only valid while it is still unique
        while q and cnt[s[q[0]]] > 1:
            q.popleft()

    return q[0] if q else -1
```

> **變形——LC 1429（First Unique Number）**：結構相同，但淘汰迴圈搬到 `showFirstUnique()` 裡，`add()` 只負責更新計數並入列。這就把它變成一題每次呼叫攤還 O(1) 的*設計*題。

### 模板 9：佇列輪轉／模擬模式 — LC 1823
> **核心想法**：題目描述一群人／一疊牌**在隊伍裡循環**時，就照字面模擬：`q.addLast(q.pollFirst())` 轉一步；`q.pollFirst()` 淘汰一個。那個雙端佇列*就是*那個圓圈。

```java
// LC 1823 - Find the Winner of the Circular Game (Josephus)
// IDEA: Rotate k-1 survivors to the back, then eliminate the front; repeat until 1 left
// time = O(N * k), space = O(N)
public int findTheWinner(int n, int k) {
    Deque<Integer> q = new ArrayDeque<>();
    for (int i = 1; i <= n; i++) q.addLast(i);

    while (q.size() > 1) {
        for (int i = 0; i < k - 1; i++) {
            q.addLast(q.pollFirst()); // survivors go to the back
        }
        q.pollFirst();                // k-th player is out
    }
    return q.peekFirst();
}

// O(N) time / O(1) space math alternative (Josephus recurrence):
// f(1) = 0 ; f(i) = (f(i-1) + k) % i ; answer = f(n) + 1
public int findTheWinnerMath(int n, int k) {
    int ans = 0;
    for (int i = 2; i <= n; i++) ans = (ans + k) % i;
    return ans + 1;
}
```

```python
# python
# LC 1823 - Find the Winner of the Circular Game (Josephus)
# IDEA: Rotate k-1 survivors to the back, then eliminate the front; repeat until 1 left
# time = O(N * k), space = O(N)
from collections import deque

def findTheWinner(n, k):
    q = deque(range(1, n + 1))
    while len(q) > 1:
        q.rotate(-(k - 1))   # move k-1 survivors to the back
        q.popleft()          # k-th player is out
    return q[0]

# O(N) time / O(1) space math alternative
def findTheWinnerMath(n, k):
    ans = 0
    for i in range(2, n + 1):
        ans = (ans + k) % i
    return ans + 1
```

**同一個模擬想法的各種變形：**

- **LC 950（Reveal Cards In Increasing Order）** — *轉折：把流程**倒過來**模擬*。先升冪排序，再從最大走到最小；每一步把一輪反做回去：把後端的牌移到前端，再把新牌推到前端。
- **LC 649（Dota2 Senate）** — *轉折：用兩個佇列而不是一個*。把兩個陣營的索引各排一列；每一輪各彈出一個，索引小的獲勝並以 `index + n` 重新入列（也就是進到下一輪）。

```java
// LC 950 - Reveal Cards In Increasing Order
// IDEA: Reverse-simulate the reveal: undo "move top to bottom", then undo "reveal"
// time = O(N log N) (sort), space = O(N)
public int[] deckRevealedIncreasing(int[] deck) {
    Arrays.sort(deck);
    Deque<Integer> q = new ArrayDeque<>();
    for (int i = deck.length - 1; i >= 0; i--) {
        if (!q.isEmpty()) q.addFirst(q.pollLast()); // undo: bottom card back to top
        q.addFirst(deck[i]);                        // undo: put the revealed card back
    }
    int[] ans = new int[deck.length];
    int i = 0;
    for (int v : q) ans[i++] = v;
    return ans;
}

// LC 649 - Dota2 Senate
// IDEA: Two index queues; smaller index bans the other and re-enters at index + n
// time = O(N), space = O(N)
public String predictPartyVictory(String senate) {
    int n = senate.length();
    Queue<Integer> radiant = new LinkedList<>(), dire = new LinkedList<>();
    for (int i = 0; i < n; i++) {
        if (senate.charAt(i) == 'R') radiant.offer(i);
        else dire.offer(i);
    }
    while (!radiant.isEmpty() && !dire.isEmpty()) {
        int r = radiant.poll(), d = dire.poll();
        if (r < d) radiant.offer(r + n); // R acts first, survives to next round
        else dire.offer(d + n);
    }
    return radiant.isEmpty() ? "Dire" : "Radiant";
}
```

```python
# python
# LC 950 - Reveal Cards In Increasing Order
# time = O(N log N) (sort), space = O(N)
from collections import deque

def deckRevealedIncreasing(deck):
    deck.sort()
    q = deque()
    for x in reversed(deck):
        if q:
            q.appendleft(q.pop())  # undo: bottom card back to top
        q.appendleft(x)            # undo: put the revealed card back
    return list(q)

# LC 649 - Dota2 Senate
# time = O(N), space = O(N)
def predictPartyVictory(senate):
    n = len(senate)
    radiant = deque(i for i, c in enumerate(senate) if c == 'R')
    dire    = deque(i for i, c in enumerate(senate) if c == 'D')
    while radiant and dire:
        r, d = radiant.popleft(), dire.popleft()
        if r < d:
            radiant.append(r + n)  # R acts first, survives to next round
        else:
            dire.append(d + n)
    return "Radiant" if radiant else "Dire"
```

### 模板 10：視窗內生效效果佇列模式 — LC 995
> **核心想法**：當索引 `i` 上的一次操作會影響接下來 `k` 個索引時，不要重複套用 `k` 次。把它的起始索引推進佇列，一旦 `front + k <= i` 就**從前端讓它過期**，然後用 `queue.size()` 告訴你在 `i` 這裡還有幾個效果生效中。（和差分陣列是同一招，只是用佇列來表達。）

```java
// LC 995 - Minimum Number of K Consecutive Bit Flips
// IDEA: Queue holds start indices of flips still covering i; parity of queue size = current value
// time = O(N), space = O(k)
public int minKBitFlips(int[] nums, int k) {
    int n = nums.length, res = 0;
    Deque<Integer> flips = new ArrayDeque<>(); // start indices of active flips

    for (int i = 0; i < n; i++) {
        // expire flips whose window [start, start + k) no longer covers i
        while (!flips.isEmpty() && flips.peekFirst() + k <= i) {
            flips.pollFirst();
        }
        // effective value = nums[i] flipped flips.size() times
        if ((nums[i] + flips.size()) % 2 == 0) { // still 0 -> must start a flip here
            if (i + k > n) return -1;            // window would run off the end
            flips.addLast(i);
            res++;
        }
    }
    return res;
}
```

```python
# python
# LC 995 - Minimum Number of K Consecutive Bit Flips
# IDEA: Queue holds start indices of flips still covering i; parity of queue size = current value
# time = O(N), space = O(k)
from collections import deque

def minKBitFlips(nums, k):
    n, res = len(nums), 0
    flips = deque()  # start indices of active flips

    for i, x in enumerate(nums):
        # expire flips whose window [start, start + k) no longer covers i
        while flips and flips[0] + k <= i:
            flips.popleft()
        # effective value = x flipped len(flips) times
        if (x + len(flips)) % 2 == 0:   # still 0 -> must start a flip here
            if i + k > n:
                return -1               # window would run off the end
            flips.append(i)
            res += 1
    return res
```

## 依模式分類的題目

### 各模式題目表

#### **BFS 與層序題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Binary Tree Level Order Traversal | 102 | 佇列 + 記錄層 | Medium |
| Binary Tree Zigzag Level Order | 103 | 佇列 + 方向旗標 | Medium |
| Binary Tree Level Order II | 107 | 佇列 + 反轉結果 | Medium |
| Binary Tree Right Side View | 199 | 佇列 + 取每層最後一個 | Medium |
| Find Bottom Left Tree Value | 513 | 佇列 + 追蹤層數 | Medium |
| Find Largest Value in Each Tree Row | 515 | 佇列 + 每層取最大 | Medium |
| Average of Levels in Binary Tree | 637 | 佇列 + 每層求和 | Easy |
| Maximum Width of Binary Tree | 662 | 佇列 + 位置編碼 | Medium |
| Populating Next Right Pointers | 116 | 佇列 + 同層串接 | Medium |
| N-ary Tree Level Order Traversal | 429 | 佇列 + 多個子節點 | Medium |

#### **滑動視窗佇列題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Sliding Window Maximum | 239 | 單調雙端佇列 | Hard |
| Moving Average from Data Stream | 346 | 固定大小佇列 | Easy |
| Design Hit Counter | 362 | 依時間淘汰 | Medium |
| Number of Recent Calls | 933 | 時間視窗佇列 | Easy |
| Longest Subarray Absolute Diff | 1438 | 兩個雙端佇列（min/max） | Medium |
| Jump Game VI | 1696 | DP + 單調佇列 | Medium |
| Constrained Subsequence Sum | 1425 | DP + 單調佇列 | Hard |

#### **佇列設計題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Implement Stack using Queues | 225 | 兩個佇列或輪轉 | Easy |
| Implement Queue using Stacks | 232 | 兩個堆疊 | Easy |
| Design Circular Queue | 622 | 陣列加指標 | Medium |
| Design Circular Deque | 641 | 雙端環形 | Medium |
| Design Front Middle Back Queue | 1670 | 兩個雙端佇列平衡 | Medium |
| Design Most Recently Used Queue | 1756 | 雙端佇列 + set | Medium |

#### **單調佇列題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Sliding Window Maximum | 239 | 遞減單調 | Hard |
| Shortest Subarray with Sum K | 862 | 前綴和 + 單調 | Hard |
| Sum of Subarray Minimums | 907 | 單調堆疊／佇列 | Medium |
| Maximum Score of Good Subarray | 1793 | 單調邊界 | Hard |
| Jump Game VI | 1696 | DP + 單調佇列 | Medium |
| Longest Continuous Subarray | 1438 | 兩個單調佇列 | Medium |
| Maximum Sum Circular Subarray | 918 | 在加倍陣列上做前綴和 + 雙端佇列（視窗 ≤ n）——見 [monotonic_queue.md](./monotonic_queue.md) | Medium |

#### **迭代器與佇列模擬題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Flatten Nested List Iterator | 341 | 建構子裡攤平成佇列（模板 7） | Medium |
| First Unique Character in a String | 387 | 候選佇列 + 計數表（模板 8） | Easy |
| First Unique Number | 1429 | 和 387 相同，但包成設計／串流題 | Medium |
| Find the Winner of the Circular Game | 1823 | 輪轉淘汰／約瑟夫問題（模板 9） | Medium |
| Reveal Cards In Increasing Order | 950 | 用雙端佇列反向模擬（模板 9） | Medium |
| Dota2 Senate | 649 | 兩個索引佇列，以 `i + n` 重新入列（模板 9） | Medium |
| Minimum Number of K Consecutive Bit Flips | 995 | 視窗內生效效果佇列（模板 10） | Hard |

#### **串流處理題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Moving Average from Data Stream | 346 | 固定視窗 | Easy |
| Data Stream as Disjoint Intervals | 352 | 區間合併 | Hard |
| Design Hit Counter | 362 | 時間佇列 | Medium |
| Logger Rate Limiter | 359 | 時間視窗 | Easy |
| Number of Recent Calls | 933 | 時間視窗 | Easy |
| Finding MK Average | 1825 | 多個佇列 | Hard |

#### **任務排程題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Task Scheduler | 621 | 佇列 + 冷卻時間 | Medium |
| Design a Number Container | 2349 | 每個數字一個佇列 | Medium |
| Time Needed to Buy Tickets | 2073 | 佇列模擬 | Easy |
| Single-Threaded CPU | 1834 | 佇列 + 優先佇列 | Medium |
| Number of Visible People in Queue | 1944 | 單調堆疊 | Hard |

## 模式選擇策略

```text
Problem Analysis Flowchart:

1. Is it a tree/graph traversal problem?
   ├── YES → Use BFS with Queue
   │         ├── Level-order → Track level size
   │         └── Shortest path → Track distance
   └── NO → Continue to 2

2. Do you need to maintain window order?
   ├── YES → Use Sliding Window Queue
   │         ├── Max/Min → Monotonic queue
   │         └── Fixed size → Regular queue
   └── NO → Continue to 3

3. Is it a design problem?
   ├── YES → Choose appropriate structure
   │         ├── Fixed size → Circular queue
   │         ├── Both ends → Deque
   │         └── Stack behavior → Queue with rotation
   └── NO → Continue to 4

4. Need monotonic property?
   ├── YES → Use Monotonic Queue
   │         ├── Increasing → Remove larger from rear
   │         └── Decreasing → Remove smaller from rear
   └── NO → Continue to 5

5. Processing data stream?
   ├── YES → Use Stream Processing
   │         ├── Time window → Remove old entries
   │         └── Count window → Fixed size queue
   └── NO → Use basic queue operations
```


## 基本操作速查

### Python collections.deque

```python
from collections import deque

# Create deque
dq = deque()
dq = deque([1, 2, 3])
dq = deque(maxlen=10)  # Fixed size

# Add elements
dq.append(4)       # Add to right: [1,2,3,4]
dq.appendleft(0)   # Add to left: [0,1,2,3,4]
dq.extend([5,6])   # Extend right: [0,1,2,3,4,5,6]
dq.extendleft([-2,-1]) # Extend left: [-1,-2,0,1,2,3,4,5,6]

# Remove elements
val = dq.pop()     # Remove from right
val = dq.popleft() # Remove from left

# Access elements
first = dq[0]      # Access by index
last = dq[-1]      # Last element

# Other operations
dq.rotate(2)       # Rotate right by 2
dq.rotate(-1)      # Rotate left by 1
dq.reverse()       # Reverse in-place
dq.clear()         # Remove all elements

# Check state
size = len(dq)
is_empty = len(dq) == 0
count = dq.count(value)
index = dq.index(value)
```

### Java Queue 與 Deque 操作 ⭐⭐⭐⭐
```java
import java.util.*;

// Queue interface (FIFO)
Queue<Integer> queue = new LinkedList<>();
queue.offer(1);    // Add to rear (returns boolean)
queue.add(2);      // Add to rear (throws exception)
Integer val = queue.poll();  // Remove from front (returns null)
val = queue.remove();        // Remove from front (throws exception)
val = queue.peek();          // View front (returns null)
val = queue.element();       // View front (throws exception)

// Deque interface (double-ended)
Deque<Integer> deque = new LinkedList<>();
// or ArrayDeque for better performance
Deque<Integer> deque = new ArrayDeque<>();

// Add operations
deque.addFirst(1);     // Add to front
deque.addLast(2);      // Add to rear
deque.offerFirst(0);   // Add to front (returns boolean)
deque.offerLast(3);    // Add to rear (returns boolean)

// Remove operations
val = deque.removeFirst(); // Remove from front
val = deque.removeLast();  // Remove from rear
val = deque.pollFirst();   // Remove from front (returns null)
val = deque.pollLast();    // Remove from rear (returns null)

// Peek operations
val = deque.peekFirst();   // View front
val = deque.peekLast();    // View rear
val = deque.getFirst();    // View front (throws exception)
val = deque.getLast();     // View rear (throws exception)

// Stack operations on Deque
deque.push(5);         // Push to front (stack top)
val = deque.pop();     // Pop from front (stack top)
```

## 總結與速查

### 複雜度速查
| 操作 | 陣列佇列 | 鏈結佇列 | 雙端佇列 | 環形佇列 |
|-----------|-------------|--------------|-------|----------------|
| Enqueue | O(1) | O(1) | O(1) | O(1) |
| Dequeue | O(n) | O(1) | O(1) | O(1) |
| 看前端 | O(1) | O(1) | O(1) | O(1) |
| 看尾端 | O(1) | O(1) | O(1) | O(1) |
| 空間 | O(n) | O(n) | O(n) | 固定 O(k) |

### 模板速查
| 模板 | 模式 | 關鍵程式碼 |
|----------|---------|----------|
| **BFS** | 逐層處理 | `for _ in range(level_size)` |
| **環形** | 固定緩衝區 | `(head + count) % capacity` |
| **單調** | 維持順序 | `while q and q[-1] < val: q.pop()` |
| **兩個堆疊** | 模擬佇列 | `if not out: transfer from in` |
| **滑動** | 追蹤視窗 | `if i >= k-1: result.append()` |
| **串流** | 時間／數量視窗 | `while old: queue.popleft()` |

### 常見模式與技巧

#### **逐層處理**
```python
# Process nodes level by level
level_size = len(queue)
for _ in range(level_size):
    node = queue.popleft()
    # Process node
    # Add children
```

#### **環形索引計算**
```python
# Wrap around in circular buffer
tail_index = (head + count) % capacity
next_index = (current + 1) % capacity
```

#### **維持單調性質**
```python
# Decreasing monotonic queue
while queue and queue[-1] < new_val:
    queue.pop()
queue.append(new_val)
```

#### **雙堆疊佇列的最佳化**
```python
# Amortized O(1) operations
if not out_stack:
    while in_stack:
        out_stack.append(in_stack.pop())
```

### 解題步驟

1. **判斷該不該用佇列**
   - 需要 FIFO 處理嗎？
   - 要一層一層走訪嗎？
   - 是有順序的滑動視窗嗎？
   - 是串流處理嗎？

2. **選實作方式**
   - 單純佇列 → deque 或 LinkedList
   - 固定大小 → 環形佇列
   - 兩端都要 → 雙端佇列
   - 有優先級 → 優先佇列（另一種資料結構）

3. **處理邊界情況**
   - 對空佇列做操作
   - 佇列滿了（有界佇列）
   - 只有一個元素
   - 環形佇列的繞回

4. **最佳化操作**
   - 用雙端佇列讓操作維持 O(1)
   - 固定大小就用環形緩衝區
   - 面試題用兩個堆疊
   - 最大／最小查詢用單調結構

### 常見錯誤與提醒

**🚫 常見錯誤：**
- 在 Python 裡用 list.pop(0)（這是 O(n)）
- BFS 時忘了記錄每層的大小
- 環形繞回沒處理好
- 在 Java 裡把 queue.poll() 和 queue.remove() 搞混
- 單調性質沒有正確維持

**✅ 最佳實務：**
- Python 一律用 collections.deque
- Java 用 ArrayDeque 而不是 LinkedList，效能較好
- 層序走訪時明確記下佇列大小
- 滑動視窗要清掉過期元素
- 環形索引用取模運算

### 面試提醒

1. **先釐清需求**
   - 固定大小還是可變大小？
   - 需要存取兩端嗎？
   - 需要執行緒安全嗎？
   - 有空間限制嗎？

2. **BFS vs DFS 的取捨**
   - BFS → 最短路徑、層序
   - DFS → 找路徑、回溯
   - BFS 用佇列，DFS 用堆疊

3. **實作選擇**
   - Python：一律優先用 deque
   - Java：用 ArrayDeque 效能較好
   - 有界問題考慮環形佇列

4. **常見追問**
   - 把它做成執行緒安全
   - 處理多生產者／多消費者
   - 換一組限制條件重做
   - 最佳化空間／時間複雜度

### 進階技巧

#### **無鎖佇列**
- 用在並行程式設計
- 靠 compare-and-swap 操作
- Michael and Scott 演算法

#### **優先雙端佇列**
- 結合優先佇列與雙端佇列
- 雙端優先佇列
- 用 interval heap 實作

#### **持久化佇列**
- 帶版本的不可變佇列
- 函數式程式設計風格
- 內部用兩個堆疊

### 相關主題
- **堆疊**：LIFO vs FIFO 的對照
- **優先佇列**：依順序處理
- **BFS**：佇列的主要應用
- **環形緩衝區**：固定大小的佇列實作
- **生產者－消費者**：佇列的經典應用

## LC 範例

### 2-1) Sliding Window Maximum — LC 239
> 維護一個遞減的索引雙端佇列；前端永遠是當前視窗的最大值。

```java
// LC 239 - Sliding Window Maximum
// IDEA: Monotonic deque — remove out-of-window front, remove smaller rear, front = max
// time = O(N), space = O(k)
public int[] maxSlidingWindow(int[] nums, int k) {
    int n = nums.length;
    int[] ans = new int[n - k + 1];
    Deque<Integer> dq = new ArrayDeque<>(); // indices, decreasing by value
    for (int i = 0; i < n; i++) {
        while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) dq.pollFirst();
        while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) dq.pollLast();
        dq.addLast(i);
        if (i >= k - 1) ans[i - k + 1] = nums[dq.peekFirst()];
    }
    return ans;
}
```

### 2-2) Design Circular Queue — LC 622
> 固定大小陣列；記住 head 索引與元素個數；用模運算處理繞回。

```java
// LC 622 - Design Circular Queue
// IDEA: Fixed array + head pointer + count; tail = (head + count - 1) % capacity
// time = O(1) all ops, space = O(k)
class MyCircularQueue {
    int[] data;
    int head, count, capacity;
    public MyCircularQueue(int k) { data = new int[k]; capacity = k; }
    public boolean enQueue(int value) {
        if (isFull()) return false;
        data[(head + count) % capacity] = value;
        count++;
        return true;
    }
    public boolean deQueue() {
        if (isEmpty()) return false;
        head = (head + 1) % capacity;
        count--;
        return true;
    }
    public int Front() { return isEmpty() ? -1 : data[head]; }
    public int Rear()  { return isEmpty() ? -1 : data[(head + count - 1) % capacity]; }
    public boolean isEmpty() { return count == 0; }
    public boolean isFull()  { return count == capacity; }
}
```

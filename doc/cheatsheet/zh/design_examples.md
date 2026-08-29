# Design — 完整範例

> **範圍** — [design.md](./design.md) 背後的解法檔案庫：二十題 LC「設計一個 X」的完整解，依每題被迫採用的結構組合分組，而不是依題號。
> **另見**：[design.md](./design.md) — 母文件：這些解法在演練的五大模式、五步驟方法與選型表；[design_patterns.md](./design_patterns.md) — 一致性雜湊、限流器與負載平衡，同一輪面試會問但不是 LC 題目；[ood_design.md](./ood_design.md) — LLD 關卡的類別建模與 SOLID；[iterator.md](./iterator.md) — 單獨談 iterator 的契約；[trie.md](./trie.md)、[heap.md](./heap.md)、[hash_map.md](./hash_map.md) — 被組合起來的個別結構。

## LeetCode 題目清單

- [Design](https://leetcode.com/problem-list/design/)
- [Data Stream](https://leetcode.com/problem-list/data-stream/)

## 總覽

這裡是 [design.md](./design.md) 的長尾。母文件留下五大結構模式、五步驟面試方法與選型表；這個檔案留下真正*套用*它們的設計，免得方法本身被 1,300 行的類別淹沒。

### Key Properties
- **複雜度**：每個設計各自標註 — 每題的重點就是哪些操作是 O(1)、哪些是 O(log n)
- **核心想法**：底下每一個設計，都是「一個存資料的結構」加上「第二個結構，唯一任務是讓某個操作變快」
- **什麼時候用**：當你已經從題目讀出需要哪些操作，想看這組搭配從頭到尾怎麼寫出來


## 快取與淘汰策略

### 1) LRU Cache — LC 146 ⭐⭐⭐⭐⭐

```python
# LC 146 LRU Cache (Least Recently Used (LRU) cache)
# V0
# IDEA : ARRAY + LRU (implement LRU via array)
class LRUCache(object):

    def __init__(self, capacity):
        self.capacity = capacity
        self._cache = []   
        self._cache_look_up = {}

    def get(self, key):
        if key not in self._cache_look_up:
            return -1

        self._cache.remove(key)
        self._cache.append(key)

        return self._cache_look_up[key]

    def put(self, key, value):
        # case 1) key in cache
        if key in self._cache_look_up:
            self._cache_look_up[key] = value

            """
            NOTE !!! below trick
                In [14]: x = [1,2,3]
                In [15]: x.remove(2)
                In [16]: x
                Out[16]: [1, 3]
                In [17]: x.append(2)
                In [18]: x
                Out[18]: [1, 3, 2]
            """
            
            self._cache.remove(key)
            self._cache.append(key)
            return

        # case 2) key NOT in cache
        else:
            # case 2-1) len(cache) == capacity -> need to clear cache with LRU
            if len(self._cache) == self.capacity:
                del_key = self._cache[0]
                self._cache = self._cache[1:]
                del self._cache_look_up[del_key]

            # case 2-2) len(cache) < capacity
            self._cache.append(key)
            self._cache_look_up[key] = value

# V1
# IDEA : Ordered dictionary
# https://leetcode.com/problems/lru-cache/solution/
# IDEA : 
#       -> There is a structure called ordered dictionary, it combines behind both hashmap and linked list. 
#       -> In Python this structure is called OrderedDict 
#       -> and in Java LinkedHashMap.
from collections import OrderedDict
class LRUCache(OrderedDict):

    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.capacity = capacity

    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        if key not in self:
            return - 1
        
        self.move_to_end(key)
        return self[key]

    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: void
        """
        if key in self:
            self.move_to_end(key)
        self[key] = value
        if len(self) > self.capacity:
            self.popitem(last = False)
```

### 2) LFU Cache — LC 460 ⭐⭐⭐⭐

```python
# LC 460. LFU Cache
# V0
from collections import OrderedDict
class Node:
    def __init__(self, key, val, count):
        self.key=key
        self.val=val
        self.count=count
class LFUCache:
    
    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.capacity=capacity
        self.key_node={}
        self.count_node={}
        self.minV=None
    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        if not key in self.key_node:  return -1 
        node = self.key_node[key]
        del self.count_node[node.count][key]
        if not self.count_node[node.count]:
            del self.count_node[node.count] 
        node.count+=1
        if not node.count in self.count_node:
            self.count_node[node.count]=OrderedDict()
        
        self.count_node[node.count][key]=node
        
        if not self.minV in self.count_node:
            self.minV+=1
        return node.val
    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: void
        """
        # if element exists, -> update value and count + 1 
        if self.capacity==0: return None
        if key in self.key_node:
            self.key_node[key].val=value
            self.get(key)
        else:
            if len(self.key_node) == self.capacity:
                item=self.count_node[self.minV].popitem(last=False)
                del self.key_node[item[0]]
            node=Node(key,value,1)
            self.key_node[key]=node
            if not 1 in self.count_node:
                self.count_node[1]=OrderedDict()
            
            self.count_node[1][key]=node
            self.minV=1

```

### 3) All O(1) Data Structure — LC 432 ⭐⭐⭐⭐

```python
# LC 432. All O`one Data Structure
# V0
# IDEA: HashMap + Doubly Linked List of Buckets
# - Each bucket contains all keys with the same count
# - HashMap: key -> bucket node
# - Doubly Linked List: ordered buckets by count
class Node:
    def __init__(self, count):
        self.count = count
        self.keys = set()
        self.prev = None
        self.next = None

class AllOne:

    def __init__(self):
        self.key_counter = {}  # key -> count
        self.count_node = {}   # count -> Node
        self.head = Node(0)    # dummy head
        self.tail = Node(0)    # dummy tail
        self.head.next = self.tail
        self.tail.prev = self.head

    def inc(self, key: str) -> None:
        if key in self.key_counter:
            count = self.key_counter[key]
            self.key_counter[key] = count + 1
            cur_node = self.count_node[count]

            # Remove key from current count bucket
            cur_node.keys.remove(key)

            # Get or create next count bucket
            if count + 1 not in self.count_node:
                new_node = Node(count + 1)
                self.count_node[count + 1] = new_node
                self._insert_after(cur_node, new_node)

            self.count_node[count + 1].keys.add(key)

            # Remove current bucket if empty
            if not cur_node.keys:
                self._remove_node(cur_node)
                del self.count_node[count]
        else:
            self.key_counter[key] = 1
            if 1 not in self.count_node:
                new_node = Node(1)
                self.count_node[1] = new_node
                self._insert_after(self.head, new_node)
            self.count_node[1].keys.add(key)

    def dec(self, key: str) -> None:
        count = self.key_counter[key]
        cur_node = self.count_node[count]
        cur_node.keys.remove(key)

        if count == 1:
            del self.key_counter[key]
        else:
            self.key_counter[key] = count - 1
            if count - 1 not in self.count_node:
                new_node = Node(count - 1)
                self.count_node[count - 1] = new_node
                self._insert_before(cur_node, new_node)
            self.count_node[count - 1].keys.add(key)

        if not cur_node.keys:
            self._remove_node(cur_node)
            del self.count_node[count]

    def getMaxKey(self) -> str:
        if self.tail.prev == self.head:
            return ""
        return next(iter(self.tail.prev.keys))

    def getMinKey(self) -> str:
        if self.head.next == self.tail:
            return ""
        return next(iter(self.head.next.keys))

    def _insert_after(self, node, new_node):
        new_node.prev = node
        new_node.next = node.next
        node.next.prev = new_node
        node.next = new_node

    def _insert_before(self, node, new_node):
        new_node.next = node
        new_node.prev = node.prev
        node.prev.next = new_node
        node.prev = new_node

    def _remove_node(self, node):
        node.prev.next = node.next
        node.next.prev = node.prev
```

### 4) Insert Delete GetRandom O(1) — LC 380 ⭐⭐⭐⭐

```python
# LC 380. Insert Delete GetRandom O(1)
# V0
# IDEA: HashMap + ArrayList
# - HashMap: stores value -> index mapping for O(1) lookup
# - ArrayList: stores actual values for O(1) random access
import random
class RandomizedSet:

    def __init__(self):
        self.dict = {}  # value -> index in list
        self.list = []  # stores values

    def insert(self, val: int) -> bool:
        if val in self.dict:
            return False
        self.dict[val] = len(self.list)
        self.list.append(val)
        return True

    def remove(self, val: int) -> bool:
        if val not in self.dict:
            return False
        # Move last element to the position of element to delete
        last_element = self.list[-1]
        idx = self.dict[val]
        self.list[idx] = last_element
        self.dict[last_element] = idx
        # Remove last element
        self.list.pop()
        del self.dict[val]
        return True

    def getRandom(self) -> int:
        return random.choice(self.list)
```

## 帶輔助狀態的堆疊

### 5) 用堆疊做頻率桶 — Max Frequency Stack, LC 895 ⭐⭐⭐⭐


**模式**：`HashMap<freq, Stack>` + `HashMap<value, freq>` 再加一個 `maxFreq` 計數器。跟 LC 432 一樣是「依計數分桶」的想法，但每個桶是**堆疊**，所以同分時由*最晚推入的*勝出。

**關鍵技巧**：`push` 時把值放進它**新**頻率的桶裡，**但不要從較低的桶移除它**。因此每個值會同時出現在桶 `1..f`，`pop` 之後前一份副本本來就躺在桶 `f-1` 裡 — 不需要任何清理。

**不變式**：`group[f]` 依推入順序，存放所有計數曾達到 `f` 的值；`maxFreq` 是最高的非空桶。

```java
// java
// LC 895 - Maximum Frequency Stack
// IDEA: bucket values by frequency, each bucket is a stack -> pop = top of the maxFreq bucket
class FreqStack {
    // time = O(1) push / O(1) pop, space = O(N)
    private final Map<Integer, Integer> freq = new HashMap<>();          // value -> count
    private final Map<Integer, Deque<Integer>> group = new HashMap<>();  // count -> stack of values
    private int maxFreq = 0;

    public void push(int val) {
        int f = freq.merge(val, 1, Integer::sum);
        maxFreq = Math.max(maxFreq, f);
        group.computeIfAbsent(f, x -> new ArrayDeque<>()).push(val);     // keep copies in 1..f
    }

    public int pop() {
        Deque<Integer> st = group.get(maxFreq);
        int val = st.pop();
        freq.merge(val, -1, Integer::sum);
        if (st.isEmpty()) maxFreq--;   // buckets are dense: maxFreq only ever drops by 1
        return val;
    }
}
```

```python
# python
# LC 895 - Maximum Frequency Stack
from collections import defaultdict

class FreqStack:
    # time = O(1) push / O(1) pop, space = O(N)
    def __init__(self):
        self.freq = defaultdict(int)     # value -> count
        self.group = defaultdict(list)   # count -> stack of values
        self.max_freq = 0

    def push(self, val):
        self.freq[val] += 1
        f = self.freq[val]
        self.max_freq = max(self.max_freq, f)
        self.group[f].append(val)

    def pop(self):
        val = self.group[self.max_freq].pop()
        self.freq[val] -= 1
        if not self.group[self.max_freq]:
            self.max_freq -= 1
        return val
```

### 6) 堆疊 + 輔助狀態 — O(1) 取最小值與延遲遞增, LC 155 / LC 1381 ⭐⭐⭐⭐


**模式**：純堆疊沒辦法用 O(1) 回答聚合查詢 — 所以在每個元素旁邊順便推入**它底下那段前綴的答案**。因為堆疊只在頂端增減，當這個元素再度成為頂端時，存起來的答案一定還有效。

**不變式**：`stack[i].min == min(values[0..i])`，所以 `getMin()` 就只是讀堆頂的第二個欄位。

```java
// java
// LC 155 - Min Stack
// IDEA: store (value, minSoFar) pairs -> every query is O(1), no recomputation on pop
class MinStack {
    // time = O(1) all ops, space = O(N)
    private final Deque<int[]> st = new ArrayDeque<>(); // {value, minSoFar}

    public void push(int val) {
        int min = st.isEmpty() ? val : Math.min(val, st.peek()[1]);
        st.push(new int[]{val, min});
    }

    public void pop()      { st.pop(); }
    public int  top()      { return st.peek()[0]; }
    public int  getMin()   { return st.peek()[1]; }
}
```

```python
# python
# LC 155 - Min Stack
class MinStack:
    # time = O(1) all ops, space = O(N)
    def __init__(self):
        self.stack = []   # (value, min_so_far)

    def push(self, val):
        cur_min = val if not self.stack else min(val, self.stack[-1][1])
        self.stack.append((val, cur_min))

    def pop(self):
        self.stack.pop()

    def top(self):
        return self.stack[-1][0]

    def getMin(self):
        return self.stack[-1][1]
```

**變形 - 延遲遞增**（轉折：輔助欄位變成**要套用到底下所有元素的待處理差值**，`pop` 時往下推一層，把 O(k) 的批次更新變成 O(1)）

```java
// java
// LC 1381 - Design a Stack With Increment Operation
// IDEA: inc[i] = amount to add to stack[0..i]; on pop, hand the delta down to i-1
class CustomStack {
    // time = O(1) push / pop / increment, space = O(maxSize)
    private final int[] stack, inc;
    private int size = 0;

    public CustomStack(int maxSize) {
        stack = new int[maxSize];
        inc = new int[maxSize];
    }

    public void push(int x) {
        if (size < stack.length) stack[size++] = x;
    }

    public int pop() {
        if (size == 0) return -1;
        size--;
        int res = stack[size] + inc[size];
        if (size > 0) inc[size - 1] += inc[size];  // propagate pending delta downwards
        inc[size] = 0;
        return res;
    }

    public void increment(int k, int val) {
        int i = Math.min(k, size) - 1;             // mark ONLY the k-th element
        if (i >= 0) inc[i] += val;
    }
}
```

```python
# python
# LC 1381 - Design a Stack With Increment Operation
class CustomStack:
    # time = O(1) push / pop / increment, space = O(maxSize)
    def __init__(self, maxSize):
        self.max_size = maxSize
        self.stack = []
        self.inc = []      # inc[i] applies to stack[0..i]

    def push(self, x):
        if len(self.stack) < self.max_size:
            self.stack.append(x)
            self.inc.append(0)

    def pop(self):
        if not self.stack:
            return -1
        add = self.inc.pop()
        if self.inc:
            self.inc[-1] += add        # propagate pending delta downwards
        return self.stack.pop() + add

    def increment(self, k, val):
        i = min(k, len(self.stack)) - 1
        if i >= 0:
            self.inc[i] += val
```

## 有序 Map、堆積與時間視窗

### 7) 用有序 Map（TreeMap）做訂位／區間設計 — LC 715 / 729 / 731 / 732 / 2034 ⭐⭐⭐⭐⭐


**模式**：把區間存在**以起點為 key 的有序 map** 裡，所有查詢都用 `floorKey`（最大的 `<=` x 的 key）／`ceilingKey`（最小的 `>=` x 的 key）回答。這是 HashMap + LinkedList 在「時間軸上的區間查詢」這一側的對應物。

**Key Idea**：只有 2 個鄰居有影響。一個新區間 `[start, end)` **只可能**跟這兩個衝突
- 起點*在 `start` 或之前*的那個區間（`floorKey`），以及
- 起點*在 `start` 或之後*的那個區間（`ceilingKey`）。

**快速決策表**

| 目標 | 結構 | 例題 |
|------|-----------|---------|
| 完全不允許重疊（重複訂位） | 有序 map `start -> end`，檢查 2 個鄰居 | LC 729 |
| 允許最多 K 層重疊／回報最大重疊數 | 把有序 map 當成**差分／掃描線計數器**（起點 `+1`，終點 `-1`） | LC 731, LC 732 |
| 追蹤一組被覆蓋的範圍（新增／移除／查詢） | 存**已合併且互斥**區間的有序 map | LC 715 |
| 在動態 multiset 中找 `<=` / `>=` x 的最大值 | 有序 map `value -> count` | LC 2034 |

#### 模板 A - 拒絕重疊（`floor` / `ceiling`）

**不變式**：map 裡永遠是**兩兩互斥**的區間，以起點為 key。

```java
// java
// LC 729 - My Calendar I
// IDEA: ordered map start -> end; a booking is legal iff it fits between its 2 neighbours
class MyCalendar {
    // time = O(log N) per book, space = O(N)
    private final TreeMap<Integer, Integer> booked = new TreeMap<>(); // start -> end

    public boolean book(int start, int end) {
        Integer prev = booked.floorKey(start);    // latest booking starting <= start
        if (prev != null && booked.get(prev) > start) return false;  // prev spills into us
        Integer next = booked.ceilingKey(start);  // earliest booking starting >= start
        if (next != null && next < end) return false;                // we spill into next
        booked.put(start, end);
        return true;
    }
}
```

```python
# python
# LC 729 - My Calendar I
# IDEA: python has no TreeMap -> keep 2 parallel sorted lists + bisect (floor = bisect_right - 1)
import bisect

class MyCalendar:
    # time = O(log N) search + O(N) list insert, space = O(N)
    def __init__(self):
        self.starts = []   # sorted starts
        self.ends = []     # ends, aligned with starts

    def book(self, start, end):
        i = bisect.bisect_right(self.starts, start) - 1   # floor index
        if i >= 0 and self.ends[i] > start:
            return False
        j = i + 1                                          # ceiling index
        if j < len(self.starts) and self.starts[j] < end:
            return False
        self.starts.insert(j, start)
        self.ends.insert(j, end)
        return True
```

#### 模板 B - 掃描線差分計數（最大重疊）

**轉折**：不要存區間，改在有序 map 裡存**起點 `+1`／終點 `-1`**。把 key **依排序順序**做前綴和，就是那個時刻的活躍訂位數。

```java
// java
// LC 732 - My Calendar III  (returns max number of concurrent bookings)
// IDEA: ordered map as a delta array on a sparse timeline; prefix-sum in key order
class MyCalendarThree {
    // time = O(N) per book (N = distinct endpoints), space = O(N)
    private final TreeMap<Integer, Integer> delta = new TreeMap<>();

    public int book(int start, int end) {
        delta.merge(start, 1, Integer::sum);
        delta.merge(end, -1, Integer::sum);
        int active = 0, best = 0;
        for (int d : delta.values()) {   // TreeMap iterates keys ascending
            active += d;
            best = Math.max(best, active);
        }
        return best;
    }
}

// LC 731 - My Calendar II  (variation: same delta map, but REJECT + roll back when depth would hit 3)
class MyCalendarTwo {
    // time = O(N) per book, space = O(N)
    private final TreeMap<Integer, Integer> delta = new TreeMap<>();

    public boolean book(int start, int end) {
        delta.merge(start, 1, Integer::sum);
        delta.merge(end, -1, Integer::sum);
        int active = 0;
        for (int d : delta.values()) {
            active += d;
            if (active > 2) {                        // triple booking -> undo
                delta.merge(start, -1, Integer::sum);
                delta.merge(end, 1, Integer::sum);
                return false;
            }
        }
        return true;
    }
}
```

```python
# python
# LC 732 - My Calendar III
# IDEA: dict of deltas, sorted scan per query (no stdlib TreeMap)
from collections import defaultdict

class MyCalendarThree:
    # time = O(N log N) per book, space = O(N)
    def __init__(self):
        self.delta = defaultdict(int)

    def book(self, start, end):
        self.delta[start] += 1
        self.delta[end] -= 1
        active = best = 0
        for t in sorted(self.delta):
            active += self.delta[t]
            best = max(best, active)
        return best


# LC 731 - My Calendar II (variation: roll back the deltas when depth would exceed 2)
class MyCalendarTwo:
    # time = O(N log N) per book, space = O(N)
    def __init__(self):
        self.delta = defaultdict(int)

    def book(self, start, end):
        self.delta[start] += 1
        self.delta[end] -= 1
        active = 0
        for t in sorted(self.delta):
            active += self.delta[t]
            if active > 2:
                self.delta[start] -= 1
                self.delta[end] += 1
                return False
        return True
```

#### 模板 C - 合併後的互斥範圍（新增／移除／查詢）

**轉折**：範圍是**可變的** — 寫入時必須跟鄰居合併，刪除時必須把它們切開。

**不變式**：區間互斥、已排序、**不相鄰**（`ends[i] < starts[i+1]`）、且非空。每個操作在回傳前都要把它恢復。

```java
// java
// LC 715 - Range Module
// IDEA: ordered map of merged disjoint [start, end); add = absorb neighbours then clear inside,
//       remove = re-insert the surviving head/tail pieces then clear inside
class RangeModule {
    // time = O(log N) amortized per op, space = O(N)
    private final TreeMap<Integer, Integer> m = new TreeMap<>(); // start -> end

    public void addRange(int left, int right) {
        Integer s = m.floorKey(left), e = m.floorKey(right);
        if (s != null && m.get(s) >= left) left = s;        // touches/overlaps on the left -> absorb
        if (e != null && m.get(e) > right) right = m.get(e); // extends past right -> absorb
        m.put(left, right);
        m.subMap(left, false, right, true).clear();          // drop everything swallowed
    }

    public boolean queryRange(int left, int right) {
        Integer s = m.floorKey(left);
        return s != null && m.get(s) >= right;               // one merged interval must cover it all
    }

    public void removeRange(int left, int right) {
        Integer s = m.floorKey(left), e = m.floorKey(right);
        if (e != null && m.get(e) > right) m.put(right, m.get(e)); // keep tail piece [right, oldEnd)
        if (s != null && m.get(s) > left) m.put(s, left);          // keep head piece [oldStart, left)
        m.subMap(left, true, right, false).clear();
    }
}
```

```python
# python
# LC 715 - Range Module
# IDEA: same invariant, kept in 2 parallel sorted lists; slice-assignment replaces a whole run at once
import bisect

class RangeModule:
    # time = O(log N) search + O(N) slice per op, space = O(N)
    def __init__(self):
        self.starts = []
        self.ends = []

    def addRange(self, left, right):
        i = bisect.bisect_right(self.starts, left) - 1
        if i >= 0 and self.ends[i] >= left:      # >= -> also merges adjacent ranges
            left = self.starts[i]
        j = bisect.bisect_right(self.starts, right) - 1
        if j >= 0 and self.ends[j] > right:
            right = self.ends[j]
        lo = bisect.bisect_left(self.starts, left)
        hi = bisect.bisect_right(self.starts, right)
        self.starts[lo:hi] = [left]              # replace the whole swallowed run by 1 interval
        self.ends[lo:hi] = [right]

    def queryRange(self, left, right):
        i = bisect.bisect_right(self.starts, left) - 1
        return i >= 0 and self.ends[i] >= right

    def removeRange(self, left, right):
        lo = bisect.bisect_left(self.starts, left)
        hi = bisect.bisect_left(self.starts, right)
        add_s, add_e = [], []
        if hi > lo and self.ends[hi - 1] > right:      # last touched interval survives past right
            add_s, add_e = [right], [self.ends[hi - 1]]
        if lo > 0 and self.ends[lo - 1] > left:        # left neighbour is cut ...
            prev_end = self.ends[lo - 1]
            self.ends[lo - 1] = left
            if prev_end > right:                       # ... or split in two
                add_s, add_e = [right], [prev_end]
        self.starts[lo:hi] = add_s
        self.ends[lo:hi] = add_e
```

**類似題目（同一套有序 map 骨架）**

| LC | 題目 | 轉折 |
|----|---------|-------|
| 729 | My Calendar I | 完全不允許重疊 → 模板 A |
| 731 | My Calendar II | 允許雙重訂位，拒絕三重 → 模板 B + 回滾 |
| 732 | My Calendar III | 回報最大同時訂位數 → 模板 B |
| 715 | Range Module | 可變的覆蓋集合（新增／移除／查詢）→ 模板 C |
| 352 | Data Stream as Disjoint Intervals | `addNum` 就是模板 C 的 `addRange(v, v+1)`；`getIntervals` 回傳合併後的清單 |
| 855 | Exam Room | 有序的**座位集合**；`seat()` 時掃過所有空隙，找離最近鄰居距離最大的位置 |
| 2034 | Stock Price Fluctuation | 有序 map `price -> count`（multiset）做 O(log N) 最大／最小值 + HashMap `timestamp -> price` 處理更正 |

### 8) 兩個堆積 — 動態中位數, LC 295 ⭐⭐⭐⭐⭐


**模式**：把資料流切成**存較小一半的最大堆積**（`lo`）與**存較大一半的最小堆積**（`hi`）。

**不變式**（每次插入後都要恢復）：
1. `max(lo) <= min(hi)` — `lo` 的每個元素都 `<=` `hi` 的每個元素
2. `len(lo) == len(hi)` 或 `len(lo) == len(hi) + 1` — 所以中位數是 `lo.top()`（奇數）或兩個堆頂的平均（偶數）

**關鍵技巧**：插入時一律**先推進 `lo`，把它的最大值彈到 `hi`，再平衡回來**。這樣不用任何比較分支就能維持不變式 1。

```java
// java
// LC 295 - Find Median from Data Stream
// IDEA: max-heap (small half) + min-heap (large half), sizes kept balanced
class MedianFinder {
    // time = O(log N) addNum / O(1) findMedian, space = O(N)
    private final PriorityQueue<Integer> lo = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
    private final PriorityQueue<Integer> hi = new PriorityQueue<>();                           // min-heap

    public void addNum(int num) {
        lo.offer(num);
        hi.offer(lo.poll());                              // push-then-pass keeps lo <= hi
        if (hi.size() > lo.size()) lo.offer(hi.poll());   // rebalance: lo holds the extra element
    }

    public double findMedian() {
        return lo.size() > hi.size() ? lo.peek() : (lo.peek() + hi.peek()) / 2.0;
    }
}
```

```python
# python
# LC 295 - Find Median from Data Stream
# IDEA: heapq is a MIN-heap -> negate values to fake the max-heap half
import heapq

class MedianFinder:
    # time = O(log N) addNum / O(1) findMedian, space = O(N)
    def __init__(self):
        self.lo = []   # max-heap (negated) : smaller half
        self.hi = []   # min-heap           : larger half

    def addNum(self, num):
        heapq.heappush(self.lo, -num)
        heapq.heappush(self.hi, -heapq.heappop(self.lo))
        if len(self.hi) > len(self.lo):
            heapq.heappush(self.lo, -heapq.heappop(self.hi))

    def findMedian(self):
        if len(self.lo) > len(self.hi):
            return float(-self.lo[0])
        return (-self.lo[0] + self.hi[0]) / 2.0
```

**變形 - 單一固定大小的堆積**（轉折：我們只需要*第 k 個*順序統計量，不是中間那個，所以一個**大小為 k 的最小堆積**就夠了；它的根就是答案）

```java
// java
// LC 703 - Kth Largest Element in a Stream
// IDEA: keep ONLY the k largest seen so far in a min-heap -> heap top == kth largest
class KthLargest {
    // time = O(log k) per add, space = O(k)
    private final PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    private final int k;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        for (int n : nums) add(n);
    }

    public int add(int val) {
        minHeap.offer(val);
        if (minHeap.size() > k) minHeap.poll();  // evict the smallest -> heap keeps top-k
        return minHeap.peek();
    }
}
```

```python
# python
# LC 703 - Kth Largest Element in a Stream
import heapq

class KthLargest:
    # time = O(log k) per add, space = O(k)
    def __init__(self, k, nums):
        self.k = k
        self.heap = []
        for n in nums:
            self.add(n)

    def add(self, val):
        heapq.heappush(self.heap, val)
        if len(self.heap) > self.k:
            heapq.heappop(self.heap)
        return self.heap[0]
```

### 9) Time Based Key-Value Store — LC 981

```python
# LC 981. Time Based Key-Value Store
# V0
# IDEA: HashMap + Binary Search
# - HashMap: key -> list of (timestamp, value) pairs
# - List is sorted by timestamp, use binary search to find largest timestamp <= target
from collections import defaultdict
import bisect

class TimeMap:

    def __init__(self):
        self.store = defaultdict(list)  # key -> [(timestamp, value), ...]

    def set(self, key: str, value: str, timestamp: int) -> None:
        self.store[key].append((timestamp, value))

    def get(self, key: str, timestamp: int) -> str:
        if key not in self.store:
            return ""

        values = self.store[key]
        # Binary search for largest timestamp <= target
        idx = bisect.bisect_right(values, (timestamp, chr(127)))

        return values[idx - 1][1] if idx > 0 else ""

# V1 - Manual Binary Search
class TimeMap:

    def __init__(self):
        self.store = defaultdict(list)

    def set(self, key: str, value: str, timestamp: int) -> None:
        self.store[key].append((timestamp, value))

    def get(self, key: str, timestamp: int) -> str:
        if key not in self.store:
            return ""

        values = self.store[key]
        left, right = 0, len(values) - 1
        result = ""

        while left <= right:
            mid = (left + right) // 2
            if values[mid][0] <= timestamp:
                result = values[mid][1]
                left = mid + 1
            else:
                right = mid - 1

        return result
```

### 10) Design Hit Counter — LC 362

```python
# LC 362. Design Hit Counter
# V0
# IDEA: Queue with timestamps (sliding window)
from collections import deque
class HitCounter:

    def __init__(self):
        self.hits = deque()  # stores timestamps

    def hit(self, timestamp: int) -> None:
        self.hits.append(timestamp)

    def getHits(self, timestamp: int) -> int:
        # Remove hits older than 300 seconds
        while self.hits and timestamp - self.hits[0] >= 300:
            self.hits.popleft()
        return len(self.hits)

# V1
# IDEA: Array with buckets (optimized for multiple hits at same timestamp)
class HitCounter:

    def __init__(self):
        self.times = [0] * 300
        self.hits = [0] * 300

    def hit(self, timestamp: int) -> None:
        idx = timestamp % 300
        if self.times[idx] != timestamp:
            self.times[idx] = timestamp
            self.hits[idx] = 1
        else:
            self.hits[idx] += 1

    def getHits(self, timestamp: int) -> int:
        total = 0
        for i in range(300):
            if timestamp - self.times[i] < 300:
                total += self.hits[i]
        return total
```

## 字典樹與前綴搜尋

### 11) Design Search Autocomplete System — LC 642

```python
# LC 642 Design Search Autocomplete System
# V1
# IDEA : DICT TRIE
# http://bookshadow.com/weblog/2017/07/16/leetcode-design-search-autocomplete-system/
class TrieNode:
    def __init__(self):
        self.children = dict()
        self.sentences = set()

class AutocompleteSystem(object):

    def __init__(self, sentences, times):
        """
        :type sentences: List[str]
        :type times: List[int]
        """
        self.buffer = ''
        self.stimes = collections.defaultdict(int)
        self.trie = TrieNode()
        for s, t in zip(sentences, times):
            self.stimes[s] = t
            self.addSentence(s)
        self.tnode = self.trie

    def input(self, c):
        """
        :type c: str
        :rtype: List[str]
        """
        ans = []
        if c != '#':
            self.buffer += c
            if self.tnode: self.tnode = self.tnode.children.get(c)
            if self.tnode: ans = sorted(self.tnode.sentences, key=lambda x: (-self.stimes[x], x))[:3]
        else:
            self.stimes[self.buffer] += 1
            self.addSentence(self.buffer)
            self.buffer = ''
            self.tnode = self.trie
        return ans

    def addSentence(self, sentence):
        node = self.trie
        for letter in sentence:
            child = node.children.get(letter)
            if child is None:
                child = TrieNode()
                node.children[letter] = child
            node = child
            child.sentences.add(sentence)
```

### 12) Design Add and Search Words Data Structure — LC 211

```python
# LC 211. Design Add and Search Words Data Structure
# V0
# IDEA: Trie with wildcard support
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_word = False

class WordDictionary:

    def __init__(self):
        self.root = TrieNode()

    def addWord(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_word = True

    def search(self, word: str) -> bool:
        return self.search_helper(word, 0, self.root)

    def search_helper(self, word: str, index: int, node: TrieNode) -> bool:
        if index == len(word):
            return node.is_word

        char = word[index]

        if char == '.':
            # Try all possible children
            for child in node.children.values():
                if self.search_helper(word, index + 1, child):
                    return True
            return False
        else:
            if char not in node.children:
                return False
            return self.search_helper(word, index + 1, node.children[char])
```

## 檔案系統與路徑

### 13) Design File System — LC 1166

```python
# LC 1166. Design File System
# V1
# IDEA : dict
# https://leetcode.com/problems/design-file-system/discuss/365925/Python-dict-solution
class FileSystem:

    def __init__(self):
        self.d = {}

    def createPath(self, path: str, value: int) -> bool:
        if path in self.d: return False
        if len(path) == 1: return False
        idx = len(path) - 1
        while path[idx] != '/': idx -= 1
        if idx == 0 or path[:idx] in self.d: 
            self.d[path] = value
            return True
        return False
        
    def get(self, path: str) -> int:
        return self.d.get(path, -1)
```

### 14) Design In-Memory File System — LC 588

```python
# LC 588. Design In-Memory File System

# V0
# IDEA : Dict
class FileSystem(object):

    def __init__(self):
        """
        NOTE !!! we init root as below structure
        """
        self.root = {'dirs' : {}, 'files': {}}

    def ls(self, path):
        """
        :type path: str
        :rtype: List[str]
        """
        node, type = self.getExistedNode(path)
        if type == 'dir':
            # NOTE: `dict.keys() + dict.keys()` works only in Python 2; in Python 3 use `sorted(list(node['dirs'].keys()) + list(node['files'].keys()))`
            return sorted(node['dirs'].keys() + node['files'].keys())
        return [path.split('/')[-1]]

    def mkdir(self, path):
        """
        :type path: str
        :rtype: void
        """
        node = self.root
        #for dir in filter(len, path.split('/')):
        for dir in [ x for x in path.split('/') if len(x) > 0 ]:
            if dir not in node['dirs']:
                node['dirs'][dir] = {'dirs' : {}, 'files': {}}
            node = node['dirs'][dir]

    def addContentToFile(self, filePath, content):
        """
        :type filePath: str
        :type content: str
        :rtype: void
        """
        dirs = filePath.split('/')
        path, file = '/'.join(dirs[:-1]), dirs[-1]
        self.mkdir(path)
        node, type = self.getExistedNode(path)
        if file not in node['files']:
            node['files'][file] = ''
        node['files'][file] += content

    def readContentFromFile(self, filePath):
        """
        :type filePath: str
        :rtype: str
        """
        dirs = filePath.split('/')
        path, file = '/'.join(dirs[:-1]), dirs[-1]
        node, type = self.getExistedNode(path)
        return node['files'][file]
        
    def getExistedNode(self, path):
        """
        :type path: str
        :rtype: str
        """
        node = self.root

        # method 1) : filter
        # https://www.runoob.com/python/python-func-filter.html
        #print ("*** path = " + str(path))
        #print ("*** filter(len, path.split('/') = " + str(filter(len, path.split('/'))))
        #for dir in filter(len, path.split('/')): # filter out path.split('/') outcome which with len > 0

        # method 2) list comprehension with condition
        for dir in [ x for x in path.split('/') if len(x) > 0 ]:
            if dir in node['dirs']: 
                node = node['dirs'][dir]
            else:
                return node, 'file'
        return node, 'dir'
```

### 15) Design Log Storage System — LC 635

```python
# LC 635 Design Log Storage System
```

## 動態消息、遊戲與模擬

### 16) Design Twitter — LC 355

```python
# LC 355  Design Twitter
# V0
# https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E8%AE%BE%E8%AE%A1Twitter.md
from collections import defaultdict
from heapq import merge
class Twitter(object):
    
    def __init__(self):
        self.follower_followees_map = defaultdict(set)
        self.user_tweets_map = defaultdict(list)
        self.time_stamp = 0

    def postTweet(self, userId, tweetId):
        self.user_tweets_map[userId].append((self.time_stamp, tweetId))
        self.time_stamp -= 1

    def getNewsFeed(self, userId):
        # get the followees list
        followees = self.follower_followees_map[userId]
        # add userId as well, since he/she can also see his/her post in the timeline
        followees.add(userId)
        
        # reversed(.) returns a listreverseiterator, so the complexity is O(1) not O(n)
        candidate_tweets = [reversed(self.user_tweets_map[u]) for u in followees]

        tweets = []
        """
        python starred expression :
        -> will extend Iterable Unpacking
        example 1 : *candidate_tweets
        exmaple 2 : a, *b, c = range(5)
        ref :
        https://www.python.org/dev/peps/pep-3132/
        https://blog.csdn.net/weixin_41521681/article/details/103528136
        http://swaywang.blogspot.com/2012/01/pythonstarred-expression.html
        https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md
        """
        # complexity is 10*log(n), n is twitter's user number in worst case
        for t in merge(*candidate_tweets):
            tweets.append(t[1])
            if len(tweets) == 10:
                break
        return tweets

    def follow(self, followerId, followeeId):
        self.follower_followees_map[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        self.follower_followees_map[followerId].discard(followeeId)
```

### 17) Design Tic-Tac-Toe — LC 348

```python
# LC 348. Design Tic-Tac-Toe
# V0
# IDEA: Track row/col/diagonal sums
# - Each player has a unique value (+1 for player1, -1 for player2)
# - Win when any row/col/diagonal sum equals n or -n
class TicTacToe:

    def __init__(self, n: int):
        self.n = n
        self.rows = [0] * n
        self.cols = [0] * n
        self.diagonal = 0
        self.anti_diagonal = 0

    def move(self, row: int, col: int, player: int) -> int:
        # player 1 -> +1, player 2 -> -1
        value = 1 if player == 1 else -1

        self.rows[row] += value
        self.cols[col] += value

        if row == col:
            self.diagonal += value

        if row + col == self.n - 1:
            self.anti_diagonal += value

        # Check win condition
        if (abs(self.rows[row]) == self.n or
            abs(self.cols[col]) == self.n or
            abs(self.diagonal) == self.n or
            abs(self.anti_diagonal) == self.n):
            return player

        return 0
```

### 18) Design Snake Game — LC 353

```python
# LC 353. Design Snake Game
# V0
# IDEA: Queue for snake body + Set for fast collision check
from collections import deque
class SnakeGame:

    def __init__(self, width: int, height: int, food: List[List[int]]):
        self.width = width
        self.height = height
        self.food = deque(food)
        self.snake = deque([(0, 0)])  # snake body positions
        self.snake_set = {(0, 0)}  # for O(1) collision check
        self.score = 0

    def move(self, direction: str) -> int:
        # Calculate new head position
        head_r, head_c = self.snake[0]

        if direction == "U":
            new_r, new_c = head_r - 1, head_c
        elif direction == "D":
            new_r, new_c = head_r + 1, head_c
        elif direction == "L":
            new_r, new_c = head_r, head_c - 1
        else:  # "R"
            new_r, new_c = head_r, head_c + 1

        # Check boundary
        if new_r < 0 or new_r >= self.height or new_c < 0 or new_c >= self.width:
            return -1

        # Check if eating food
        if self.food and [new_r, new_c] == self.food[0]:
            self.food.popleft()
            self.score += 1
        else:
            # Remove tail if not eating
            tail = self.snake.pop()
            self.snake_set.remove(tail)

        # Check self-collision (after tail removal)
        if (new_r, new_c) in self.snake_set:
            return -1

        # Add new head
        self.snake.appendleft((new_r, new_c))
        self.snake_set.add((new_r, new_c))

        return self.score
```

### 19) Design Underground System — LC 1396

```python
# LC 1396. Design Underground System
# V0
# IDEA: Two HashMaps
# - checkInMap: id -> (stationName, time)
# - travelMap: (start, end) -> [total_time, count]
from collections import defaultdict
class UndergroundSystem:

    def __init__(self):
        self.check_in = {}  # id -> (station, time)
        self.travel = defaultdict(lambda: [0, 0])  # (start, end) -> [total_time, count]

    def checkIn(self, id: int, stationName: str, t: int) -> None:
        self.check_in[id] = (stationName, t)

    def checkOut(self, id: int, stationName: str, t: int) -> None:
        start_station, start_time = self.check_in[id]
        route = (start_station, stationName)
        self.travel[route][0] += t - start_time
        self.travel[route][1] += 1
        del self.check_in[id]

    def getAverageTime(self, startStation: str, endStation: str) -> float:
        total_time, count = self.travel[(startStation, endStation)]
        return total_time / count
```

## 快速參考

### 20) 其他高頻的 `design` 標籤題目


- **LC 297. Serialize and Deserialize Binary Tree**（Hard）- 前序 DFS，用 `#` 當 null 的哨兵；反序列化時依同樣順序消耗這串 token（見 `tree.md`）
- **LC 449. Serialize and Deserialize BST**（Medium）- 想法相同，但 BST 的順序讓你可以省掉 null 標記，改用 `(lower, upper)` 邊界重建
- **LC 706 / 705. Design HashMap / HashSet**（Easy）- 桶陣列 + 分離鏈結法（見 `hash_map.md`）
- **LC 707. Design Linked List**（Medium）- dummy head + size 欄位（見 `linked_list.md`）
- **LC 745. Prefix and Suffix Search**（Hard）- 把每個 `suffix + '{' + word` 都插進同一棵字典樹，再搜尋 `suf + '{' + pre`
- **LC 676. Implement Magic Dictionary** / **LC 677. Map Sum Pairs**（Medium）- 字典樹變形：剛好一個字元不匹配的 DFS／前綴和聚合（見 `trie.md`）
- **LC 1472. Design Browser History**（Medium）- 陣列 + 目前索引（`visit` 時截斷往前的歷史），或兩個堆疊
- **LC 1352. Product of the Last K Numbers**（Medium）- 前綴乘積清單；`add(0)` 時重置清單，`k > len` 時答 0

---

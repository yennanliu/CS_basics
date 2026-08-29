# 雜湊表 — 題目詳解

> **範圍** — 雜湊表家族的解法檔案庫：每題一個標準解、那些其實是單題深入剖析的專屬模板，以及有序 map（Java `TreeMap` / Python `SortedDict`）的參考資料。
> **另見** — *母文件*：[hash_map.md](./hash_map.md) — 標準模板、「題目→模式」決策表，還有這份檔案庫在背後撐著的面試建議。
> *鄰近文件*：[prefix_sum.md](./prefix_sum.md) — 前綴和自成一家的完整說明；[hashing.md](./hashing.md) — 雜湊怎麼運作，以及計數與 rolling hash 的慣用寫法；[set.md](./set.md) — 只管有沒有，不管值。

## LeetCode 題目清單

- [Hash Table](https://leetcode.com/problem-list/hash-table/)

## 總覽

這個檔案是 [hash_map.md](./hash_map.md) 的長尾。它放了三類東西，這些留在主文件裡只會把模板淹掉：

- **模板與演算法** — 那些實際上是在深入講一兩題的模式（bucket sort、rolling hash、拆字探查、最大頻率的算術），加上有序 map 參考（`TreeMap` / `SortedDict`）— 那是**排序過的** map，所以本來就不屬於主文件。
- **LC 範例** — 寫完整的解法，每題每語言一個標準版本。
- **依模式分類的題目** — 各分類的完整題目表。

### 關鍵性質
- **複雜度**：見主文件的 [Time Complexity](./hash_map.md#time-complexity) 表
- **核心想法**：這裡每一節都是主文件某個[模板](./hash_map.md#templates--algorithms)的應用 — 該背的是模板，這些是排練
- **什麼時候用**：當你已經知道一題要用哪個模板，想看它完整寫出來長什麼樣

## 模板與演算法

### 有序 Map — Java TreeMap / Python SortedDict

> ⚠️ **Python 沒有內建的 `TreeMap`** — 標準函式庫根本沒有有序 map。
> 大家實際上都用第三方套件 **`sortedcontainers`** 的 `SortedDict`（LeetCode 上已預裝）。
> 兩者到底差在哪，見下面的
> [`SortedDict` vs `TreeMap`](#sorteddict-vs-treemap-implementation-differences)
> 比較。

```python
# Python - SortedDict (from sortedcontainers)
from sortedcontainers import SortedDict

# TreeMap Pattern Template
def treemap_pattern(data, target):
    # SortedDict keeps keys in sorted order
    tree_map = SortedDict()

    # Basic operations
    tree_map[key] = value           # O(log n) insert
    value = tree_map.get(key)       # O(1) !! backed by a hash dict, NOT a tree walk
    del tree_map[key]               # O(log n) delete

    # Ordered access — keys() is an INDEXABLE sorted view (O(log n) random access)
    keys = tree_map.keys()
    first_key = keys[0]  if tree_map else None          # firstKey()
    last_key  = keys[-1] if tree_map else None          # lastKey()
    tree_map.peekitem(0)                                # firstEntry() -> (k, v)
    tree_map.peekitem(-1)                               # lastEntry()  -> (k, v)

    # Floor / Ceiling — use the SortedDict's OWN bisect methods.
    # ❌ do NOT do `bisect.bisect_left(list(tree_map.keys()), target)`
    #    -> list(...) copies every key = O(n), killing the O(log n) win
    i = tree_map.bisect_left(target)    # first index with key >= target
    j = tree_map.bisect_right(target)   # first index with key >  target

    ceil_key  = keys[i]     if i < len(tree_map) else None   # ceilingKey(target)
    floor_key = keys[j - 1] if j > 0             else None   # floorKey(target)

    # Range query: all keys in [lo, hi]
    for k in tree_map.irange(lo, hi):                   # subMap(lo, true, hi, true)
        process(k, tree_map[k])

    return tree_map

# Examples: LC 853, LC 729/731/732, LC 846, LC 352, LC 981
```

#### Java `TreeMap` → Python `SortedDict` API 對照 ⭐⭐⭐⭐⭐

| Java `TreeMap` | Python `SortedDict` | 備註 |
|---|---|---|
| `firstKey()` / `lastKey()` | `d.keys()[0]` / `d.keys()[-1]` | |
| `firstEntry()` / `lastEntry()` | `d.peekitem(0)` / `d.peekitem(-1)` | 回傳 `(k, v)` tuple |
| `floorKey(x)`（最大的 ≤ x） | `d.keys()[d.bisect_right(x) - 1]` | 要檢查 `idx >= 0` |
| `ceilingKey(x)`（最小的 ≥ x） | `d.keys()[d.bisect_left(x)]` | 要檢查 `idx < len(d)` |
| `lowerKey(x)`（嚴格 < x） | `d.keys()[d.bisect_left(x) - 1]` | 要檢查 `idx >= 0` |
| `higherKey(x)`（嚴格 > x） | `d.keys()[d.bisect_right(x)]` | 要檢查 `idx < len(d)` |
| `subMap(lo, true, hi, true)` | `d.irange(lo, hi)` | 兩端都包含 |
| `headMap(hi, true)` | `d.irange(maximum=hi)` | |
| `tailMap(lo, true)` | `d.irange(minimum=lo)` | |
| `pollFirstEntry()` / `pollLastEntry()` | `d.popitem(0)` / `d.popitem(-1)` | |
| `descendingMap()` | `reversed(d)` / `d.keys()[::-1]` | |
| `new TreeMap<>(comparator)` | `SortedDict(key_func)` | 是 key 的**轉換函式**，不是比較器 |

⚠️ **頭號陷阱**：Java 的 `floorKey/ceilingKey` 直接給你一個**鍵**（或 `null`）；
Python 的 `bisect_*` 給你的是**索引**，而且可能是 `-1` 或 `len(d)`。
**下標前一定要先檢查索引**：

```python
# python — the safe floor/ceiling idiom
i = d.bisect_left(x)
ceil_key = d.keys()[i] if i < len(d) else None      # ceilingKey(x)

j = d.bisect_right(x) - 1
floor_key = d.keys()[j] if j >= 0 else None         # floorKey(x)
```

```java
// Java - TreeMap Pattern
import java.util.*;

// TreeMap Pattern Template
public void treeMapPattern(int[] data) {
    // TreeMap maintains sorted order by key (Red-Black Tree)
    TreeMap<Integer, Integer> treeMap = new TreeMap<>();

    // Basic operations - O(log n)
    treeMap.put(key, value);        // Insert
    Integer value = treeMap.get(key);  // Search
    treeMap.remove(key);            // Delete

    // Ordered operations - O(log n)
    Integer firstKey = treeMap.firstKey();   // Min key
    Integer lastKey = treeMap.lastKey();     // Max key
    Integer floorKey = treeMap.floorKey(k);  // Largest key <= k
    Integer ceilKey = treeMap.ceilingKey(k); // Smallest key >= k

    // Lower/Higher (exclusive)
    Integer lower = treeMap.lowerKey(k);     // Largest key < k
    Integer higher = treeMap.higherKey(k);   // Smallest key > k

    // Range queries - O(k log n) where k is range size
    Map.Entry<Integer, Integer> firstEntry = treeMap.firstEntry();
    Map.Entry<Integer, Integer> lastEntry = treeMap.lastEntry();

    // Iterate in sorted order - O(n)
    for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
        int key = entry.getKey();
        int val = entry.getValue();
        // Process in sorted order
    }

    // SubMap views (range queries)
    SortedMap<Integer, Integer> subMap = treeMap.subMap(fromKey, toKey);
    SortedMap<Integer, Integer> headMap = treeMap.headMap(toKey);
    SortedMap<Integer, Integer> tailMap = treeMap.tailMap(fromKey);
}
```

#### **`SortedDict` vs `TreeMap`：實作上的差異**

它們能解的問題一樣，但**不是同一種資料結構**：

| | Python `SortedDict` | Java `TreeMap` |
|---|---|---|
| **來源** | `pip install sortedcontainers` — **不是標準函式庫**（LeetCode 上已預裝） | `java.util`，內建 |
| **實作** | `dict` 加上一個由鍵組成的 `SortedList`（list of lists，接近 B-tree） | 紅黑樹（自平衡二元搜尋樹） |
| **`d[k]` / `get(k)`** | **O(1)** — 就是雜湊查找 | **O(log n)** — 要往下走樹 |
| **插入／刪除** | O(log n) 攤提 | O(log n) |
| **floor / ceiling** | O(log n)，靠 `bisect_*`（回傳的是**索引**） | O(log n)，靠 `floorKey/ceilingKey`（回傳**鍵**或 `null`） |
| **第 k 小的鍵** | **O(log n)** — `d.keys()[k]` ✅ | ❌ 不支援（只能 O(n) 走訪） |
| **自訂排序** | `SortedDict(key_func)` — 只能做 key 的**轉換** | `Comparator` — 任意的兩參數邏輯 |
| **重複鍵** | ❌ | ❌ |
| **執行緒安全** | ❌ | ❌（要用 `ConcurrentSkipListMap`） |

**幾個結論：**
1. 單純查值的話，`SortedDict` 比 `TreeMap` **快**（O(1) 雜湊 vs O(log n) 走樹）。
2. `SortedDict` 支援 O(log n) 的**索引存取**（`keys()[k]`）— 對「第 k 小的鍵」這類題目很好用，`TreeMap` 沒有 order-statistic tree 就辦不到。
3. `TreeMap` 的 `Comparator` 表達力嚴格強過 `SortedDict` 的 key function。
4. 如果只能 import 標準函式庫，退而求其次用一般 list 上的 `bisect`
   （查找 O(log n)，但因為要搬動元素，**插入是 O(n)**）— `n` 小的時候夠用。

**TreeMap 與 HashMap 的比較：**

| 特性 | HashMap | TreeMap |
|---------|---------|---------|
| **順序** | 沒有順序 | 依鍵排序 |
| **底層結構** | 雜湊表 + 鏈結串列／紅黑樹（處理碰撞） | 紅黑樹 |
| **插入／刪除／搜尋** | 平均 O(1)，最差 O(n) | O(log n) |
| **走訪** | 沒有特定順序 | 依鍵的排序順序 |
| **Floor/Ceiling** | 不支援 | O(log n) |
| **範圍查詢** | 不支援 | O(k log n) |
| **適用場景** | 只要快速查找，不在乎順序 | 有序走訪、範圍查詢、floor/ceiling |
| **記憶體** | 較少（雜湊表） | 較多（樹節點 + 指標） |

**什麼時候該用 TreeMap：**
- 需要鍵維持排序
- 需要 floor/ceiling（找最接近的鍵）
- 需要範圍查詢（`[a, b]` 之間的所有鍵）
- 需要高效率拿到第一個／最後一個鍵
- 題目牽涉到區間、範圍或順序限制

**什麼時候不該用 TreeMap：**
- 只需要 O(1) 快速查找、不在乎順序
- 記憶體吃緊（TreeMap 比較耗記憶體）
- 用不到有序操作（HashMap 更快）

**常見的 TreeMap 模式：**

1. **模式 1：用有序 Map 做排序**
   ```java
   // LC 853 - Car Fleet
   // Convert HashMap to TreeMap for sorted iteration
   Map<Integer, Integer> map = new HashMap<>();
   // ... populate map ...
   TreeMap<Integer, Integer> sorted = new TreeMap<>(map);
   ```

2. **模式 2：區間管理**
   ```java
   // LC 729/731/732 - My Calendar series
   // Use TreeMap to check overlapping intervals
   TreeMap<Integer, Integer> calendar = new TreeMap<>();

   public boolean book(int start, int end) {
       Integer prev = calendar.floorKey(start);
       Integer next = calendar.ceilingKey(start);

       if ((prev == null || calendar.get(prev) <= start) &&
           (next == null || end <= next)) {
           calendar.put(start, end);
           return true;
       }
       return false;
   }
   ```

3. **模式 3：連續元素**
   ```java
   // LC 846 - Hand of Straights
   // Use TreeMap to process smallest elements first
   TreeMap<Integer, Integer> count = new TreeMap<>();
   // ... count frequency ...

   while (!count.isEmpty()) {
       int first = count.firstKey();
       // Process consecutive sequence starting from first
   }
   ```

4. **模式 4：範圍／資料流問題**
   ```java
   // LC 352 - Data Stream as Disjoint Intervals
   // Maintain disjoint intervals in sorted order
   TreeMap<Integer, int[]> intervals = new TreeMap<>();

   public void addNum(int val) {
       Integer lower = intervals.floorKey(val);
       Integer higher = intervals.ceilingKey(val);
       // Merge intervals if needed
   }
   ```

**經典 LeetCode 題目：**

| 題目 | LC# | 難度 | 關鍵的 TreeMap 操作 |
|---------|-----|------------|----------------------|
| Car Fleet | 853 | Medium | 用位置當鍵來排序 |
| My Calendar I | 729 | Medium | 用 floorKey/ceilingKey 檢查重疊 |
| My Calendar II | 731 | Medium | 計算重疊的預約數 |
| My Calendar III | 732 | Hard | 最大重疊數 |
| Hand of Straights | 846 | Medium | 用 firstKey 拿最小元素 |
| Data Stream as Disjoint Intervals | 352 | Hard | 用 floor/ceiling 合併區間 |
| Time Based Key-Value Store | 981 | Medium | 用 floorKey 查時間戳 |
| Count of Smaller Numbers After Self | 315 | Hard | 有序走訪 |
| Contains Duplicate III | 220 | Medium | 用 floorKey/ceilingKey 做範圍檢查 |
| The Skyline Problem | 218 | Hard | 用 TreeMap 當多重集合 |

**範例：LC 853 - Car Fleet**

```python
# Python - LC 853 Car Fleet
def carFleet(target, position, speed):
    # Use sorted iteration (similar to TreeMap)
    cars = sorted(zip(position, speed), reverse=True)  # Sort by position descending

    stack = []
    for pos, spd in cars:
        time = (target - pos) / spd  # Time to reach target
        if not stack or time > stack[-1]:
            stack.append(time)

    return len(stack)

# Alternative using SortedDict
from sortedcontainers import SortedDict

def carFleet_v2(target, position, speed):
    car_map = SortedDict()
    for p, s in zip(position, speed):
        car_map[-p] = s  # Negative for reverse order

    fleets = 0
    prev_time = 0

    for neg_pos, spd in car_map.items():
        pos = -neg_pos
        time = (target - pos) / spd
        if time > prev_time:
            fleets += 1
            prev_time = time

    return fleets
```

```java
// Java - LC 853 Car Fleet
/**
 * time = O(N log N)
 * space = O(N)
 */
public int carFleet(int target, int[] position, int[] speed) {
    // Build HashMap first
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < position.length; i++) {
        map.put(position[i], speed[i]);
    }

    // Convert to TreeMap for sorted iteration (descending order)
    TreeMap<Integer, Integer> treeMap = new TreeMap<>(Collections.reverseOrder());
    treeMap.putAll(map);

    int fleets = 0;
    double prevTime = 0;

    // Iterate from position closest to target (sorted order)
    for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
        int pos = entry.getKey();
        int spd = entry.getValue();
        double time = (double)(target - pos) / spd;

        // If current car takes longer, it forms a new fleet
        if (time > prevTime) {
            fleets++;
            prevTime = time;
        }
    }

    return fleets;
}
```

**範例：LC 729 - My Calendar I**

```java
// Java - LC 729 My Calendar I
/**
 * time = O(log N) per operation
 * space = O(N)
 */
class MyCalendar {
    TreeMap<Integer, Integer> calendar;

    public MyCalendar() {
        calendar = new TreeMap<>();
    }

    public boolean book(int start, int end) {
        // Find largest start time <= current start
        Integer prev = calendar.floorKey(start);

        // Find smallest start time >= current start
        Integer next = calendar.ceilingKey(start);

        // Check no overlap with previous booking
        if (prev != null && calendar.get(prev) > start) {
            return false;
        }

        // Check no overlap with next booking
        if (next != null && next < end) {
            return false;
        }

        calendar.put(start, end);
        return true;
    }
}
```

```python
# python
# LC 729 - My Calendar I
# V1) Closest 1:1 translation of the Java floorKey / ceilingKey solution
from sortedcontainers import SortedDict

class MyCalendar:
    # time = O(log N) per booking, space = O(N)
    def __init__(self):
        self.calendar = SortedDict()   # start -> end

    def book(self, start: int, end: int) -> bool:
        keys = self.calendar.keys()

        # floorKey(start): largest key <= start
        i = self.calendar.bisect_right(start)
        prev = keys[i - 1] if i > 0 else None

        # ceilingKey(start): smallest key >= start
        j = self.calendar.bisect_left(start)
        nxt = keys[j] if j < len(keys) else None

        if (prev is None or self.calendar[prev] <= start) and \
           (nxt is None or end <= nxt):
            self.calendar[start] = end
            return True
        return False
```

```python
# python
# LC 729 - My Calendar I
# V2) More idiomatic — SortedList of (start, end) tuples.
#     ONE structure, no key/value split; the overlap check reads directly.
#     This is the version to write in an interview.
from sortedcontainers import SortedList

class MyCalendar:
    # time = O(log N) per booking, space = O(N)
    def __init__(self):
        self.books = SortedList()      # sorted list of (start, end)

    def book(self, start: int, end: int) -> bool:
        i = self.books.bisect_left((start, end))
        if i > 0 and self.books[i - 1][1] > start:            # prev event overlaps
            return False
        if i < len(self.books) and end > self.books[i][0]:    # next event overlaps
            return False
        self.books.add((start, end))
        return True
```

```python
# python
# LC 729 - My Calendar I
# V3) Zero-dependency fallback (stdlib only) — search stays O(log N),
#     but list.insert() shifts elements => O(N) per booking.
#     Fine for LC 729's constraints (<= 1000 calls).
import bisect

class MyCalendar:
    # time = O(N) per booking, space = O(N)
    def __init__(self):
        self.books = []                # sorted list of (start, end)

    def book(self, start: int, end: int) -> bool:
        i = bisect.bisect_left(self.books, (start, end))
        if i > 0 and self.books[i - 1][1] > start:
            return False
        if i < len(self.books) and end > self.books[i][0]:
            return False
        self.books.insert(i, (start, end))
        return True
```

**TreeMap 類題目的面試建議：**

1. **辨識訊號：**
   - 「排序順序」、「最小／最大」、「floor/ceiling」→ 想到 TreeMap
   - 「重疊區間」→ TreeMap 配 floorKey/ceilingKey
   - 「連續元素」→ 用 TreeMap.firstKey() 做貪婪處理
   - 「範圍查詢」→ TreeMap.subMap()

2. **常見錯誤：**
   - 忘了 TreeMap 是 O(log n)，HashMap 才是 O(1)
   - 沒處理 floor/ceiling 回傳 null 的情況
   - HashMap 就夠用了卻硬要用 TreeMap
   - 沒考慮樹結構的記憶體開銷
   - **（Python）** 把 `bisect_left/right` 的結果當成**鍵** — 它是**索引**；
     忘了 `idx >= 0` / `idx < len(d)` 的檢查 → `IndexError`，或更慘的是無聲繞回
     （`keys()[-1]` 回傳的是**最大**的鍵，不是「沒有」！）
   - **（Python）** 寫 `bisect.bisect_left(list(d.keys()), x)` — 那個 `list(...)` 複製是 O(n)；
     改呼叫 `d.bisect_left(x)`

3. **最佳化：**
   - 如果只需要有序走訪一次，直接把陣列排序就好（O(n log n)，不用維護 TreeMap）
   - 如果範圍查詢很少發生，考慮延遲排序
   - Python 的話，`sortedcontainers` 提供了高效率的 SortedDict

4. **邊界情況：**
   - 空的 TreeMap（firstKey/lastKey 會丟例外）
   - floor/ceiling 回傳 null
   - 重複鍵（TreeMap 不允許，改把值當計數器用）
   - 反向走訪（Java 用 descendingMap()）
### 用雜湊表做 Bucket Sort（Top-K 頻率，O(n)）

**被問到 top-K 高頻元素時，先問一句：「要 O(n) 嗎？」** — bucket 這招可以完全避開堆積。

**想法**：開一排 bucket，`bucket[freq]` 裝所有出現該次數的元素。從最高頻率往下掃，收集前 K 個。

```python
# LC 347 Top K Frequent Elements — O(n) bucket approach
from collections import Counter

def topKFrequent(nums: list, k: int) -> list:
    count = Counter(nums)
    # bucket[i] = list of numbers that appear exactly i times
    bucket = [[] for _ in range(len(nums) + 1)]
    for num, freq in count.items():
        bucket[freq].append(num)

    result = []
    for freq in range(len(bucket) - 1, 0, -1):
        result.extend(bucket[freq])
        if len(result) >= k:
            return result[:k]
    return result

# LC 692 Top K Frequent Words — bucket + sort within bucket
from collections import Counter

def topKFrequent_words(words: list, k: int) -> list:
    count = Counter(words)
    bucket = [[] for _ in range(len(words) + 1)]
    for word, freq in count.items():
        bucket[freq].append(word)

    result = []
    for freq in range(len(bucket) - 1, 0, -1):
        bucket[freq].sort()          # alphabetical within same frequency
        result.extend(bucket[freq])
        if len(result) >= k:
            return result[:k]
    return result
```

| 做法 | 時間 | 空間 | 什麼時候用 |
|----------|------|-------|------|
| 堆積（nlargest） | O(n log k) | O(n) | 預設 |
| Bucket sort | O(n) | O(n) | 題目明講要 O(n) 時 |

---

### 雜湊表 + 記憶化／DP

**模式**：把 dict 當成由上而下 DP 的快取（記憶化）。鍵就是子問題的狀態（索引、剩餘目標值、走訪過的集合等等）。

```python
# LC 139 Word Break — {index: bool}
def wordBreak(s: str, wordDict: list) -> bool:
    word_set = set(wordDict)
    memo = {}

    def dp(i):
        if i == len(s):
            return True
        if i in memo:
            return memo[i]
        for j in range(i + 1, len(s) + 1):
            if s[i:j] in word_set and dp(j):
                memo[i] = True
                return True
        memo[i] = False
        return False

    return dp(0)

# LC 1048 Longest String Chain — {word: longest_chain_ending_here}
def longestStrChain(words: list) -> int:
    words.sort(key=len)
    dp = {}   # word -> longest chain ending at this word
    best = 1
    for word in words:
        dp[word] = 1
        for i in range(len(word)):
            prev = word[:i] + word[i+1:]   # remove one character
            if prev in dp:
                dp[word] = max(dp[word], dp[prev] + 1)
        best = max(best, dp[word])
    return best

# LC 322 Coin Change — classic DP, memo keyed by amount
def coinChange(coins: list, amount: int) -> int:
    memo = {}
    def dp(rem):
        if rem < 0: return float('inf')
        if rem == 0: return 0
        if rem in memo: return memo[rem]
        memo[rem] = min(dp(rem - c) + 1 for c in coins)
        return memo[rem]
    res = dp(amount)
    return res if res != float('inf') else -1
```

**關鍵原則**：計算**之前**一定先檢查 `if state in memo: return memo[state]`。回傳**之前**一定先把結果存起來。

---

### 單調堆疊 + 雜湊表

**模式**：用堆疊依單調順序處理元素；用雜湊表依索引或值記下每個元素的答案。

```python
# LC 496 Next Greater Element I
# map each element of nums1 to its next-greater in nums2
def nextGreaterElement(nums1: list, nums2: list) -> list:
    next_greater = {}   # val -> next greater val in nums2
    stack = []          # monotonic decreasing stack

    for num in nums2:
        # pop all elements smaller than current — current is their next greater
        while stack and stack[-1] < num:
            next_greater[stack.pop()] = num
        stack.append(num)

    return [next_greater.get(n, -1) for n in nums1]

# LC 503 Next Greater Element II (circular array)
def nextGreaterElements(nums: list) -> list:
    n = len(nums)
    result = [-1] * n
    stack = []  # stores indices

    for i in range(2 * n):   # traverse twice for circular
        while stack and nums[stack[-1]] < nums[i % n]:
            result[stack.pop()] = nums[i % n]
        if i < n:
            stack.append(i)
    return result

# LC 739 Daily Temperatures — index-based answer map
def dailyTemperatures(temps: list) -> list:
    result = [0] * len(temps)
    stack = []  # monotonic decreasing stack of indices

    for i, t in enumerate(temps):
        while stack and temps[stack[-1]] < t:
            j = stack.pop()
            result[j] = i - j
        stack.append(i)
    return result
```

**辨識訊號**：「下一個更大／更小」、「幾天後會變暖」、「股價的跨度」、「最大矩形」。

---

### Rolling Hash（Rabin-Karp）

**什麼時候用**：在期望 O(n) 時間內找出重複／相符的子字串。比 O(n²) 的暴力子字串比較好。

**想法**：用多項式 rolling hash 對每個視窗算雜湊值。視窗右移時，把最左邊的字元移掉、把新的最右字元加進來，都是 O(1)。

```python
# LC 187 Repeated DNA Sequences — find all length-10 substrings appearing ≥ 2 times
def findRepeatedDnaSequences(s: str) -> list:
    if len(s) <= 10:
        return []
    seen, repeated = set(), set()
    for i in range(len(s) - 9):
        sub = s[i:i+10]
        if sub in seen:
            repeated.add(sub)
        seen.add(sub)
    return list(repeated)

# General Rabin-Karp rolling hash template
def rabin_karp(s: str, pattern: str) -> list:
    """Return all start indices where pattern occurs in s."""
    n, m = len(s), len(pattern)
    if m > n:
        return []

    BASE = 26
    MOD = (1 << 61) - 1   # Mersenne prime — minimises collisions

    def char_val(c):
        return ord(c) - ord('a')

    # Precompute BASE^(m-1) mod MOD
    power = pow(BASE, m - 1, MOD)

    # Hash of pattern and first window
    p_hash = 0
    w_hash = 0
    for i in range(m):
        p_hash = (p_hash * BASE + char_val(pattern[i])) % MOD
        w_hash = (w_hash * BASE + char_val(s[i])) % MOD

    result = []
    for i in range(n - m + 1):
        if w_hash == p_hash and s[i:i+m] == pattern:  # verify on hash match
            result.append(i)
        if i < n - m:
            # Roll: remove leftmost, add new rightmost
            w_hash = (w_hash - char_val(s[i]) * power) % MOD
            w_hash = (w_hash * BASE + char_val(s[i + m])) % MOD

    return result

# LC 1044 Longest Duplicate Substring — binary search + rolling hash
def longestDupSubstring(s: str) -> str:
    BASE, MOD = 31, (1 << 61) - 1

    def has_dup(length):
        if length == 0:
            return ""
        power = pow(BASE, length - 1, MOD)
        h = 0
        for c in s[:length]:
            h = (h * BASE + ord(c) - ord('a')) % MOD
        seen = {h: 0}
        for i in range(1, len(s) - length + 1):
            h = (h - (ord(s[i-1]) - ord('a')) * power) % MOD
            h = (h * BASE + ord(s[i+length-1]) - ord('a')) % MOD
            if h in seen:
                # verify (collision guard)
                start = seen[h]
                if s[start:start+length] == s[i:i+length]:
                    return s[i:i+length]
            seen[h] = i
        return ""

    lo, hi, ans = 0, len(s) - 1, ""
    while lo <= hi:
        mid = (lo + hi) // 2
        dup = has_dup(mid)
        if dup:
            ans = dup
            lo = mid + 1
        else:
            hi = mid - 1
    return ans
```

**碰撞防護**：雜湊值相同時，一定要再用 `s[i:i+m] == pattern` 驗證一次 — 碰撞很少見，但不是不可能。

| 題目 | LC# | 難度 | 技巧 |
|---------|-----|------------|-----------|
| Repeated DNA Sequences | 187 | Medium | 子字串集合／rolling hash |
| Longest Duplicate Substring | 1044 | Hard | 二分搜尋 + rolling hash |
| Rabin-Karp string match | - | - | 上面的模板 |

---

### 單字 → 索引的 Map 做配對查找（拆字探查） ⭐⭐⭐⭐

**模式**：想在 `n` 個字串裡找**配對**又不想寫 O(n²) 的雙層迴圈，就把每個字串放進 `word -> index` 的 map，然後對每個單字列出它 O(k) 個切點，拿「能補成答案的那一半」去**探查**這個 map。成本從 `O(n^2 * k)` 掉到 `O(n * k^2)`。

**關鍵想法（LC 336）**：`w = prefix + suffix`。`w + partner` 要是回文，只有兩種形狀：
- `suffix` 是回文 → `partner = reverse(prefix)` 接在**右邊**
- `prefix` 是回文 → `partner = reverse(suffix)` 接在**左邊**

```java
// java
// LC 336 - Palindrome Pairs
// IDEA: word -> index map; for each split point, probe for the reversed other half
// time = O(n * k^2), space = O(n * k)   (n words, k = max word length)
public List<List<Integer>> palindromePairs(String[] words) {
    Map<String, Integer> index = new HashMap<>();   // word -> its index
    for (int i = 0; i < words.length; i++) index.put(words[i], i);

    List<List<Integer>> res = new ArrayList<>();
    for (int i = 0; i < words.length; i++) {
        String w = words[i];
        for (int j = 0; j <= w.length(); j++) {
            String pref = w.substring(0, j), suf = w.substring(j);
            if (isPal(pref)) {                      // partner goes on the LEFT
                String back = new StringBuilder(suf).reverse().toString();
                Integer k = index.get(back);
                if (k != null && !back.equals(w)) res.add(Arrays.asList(k, i));
            }
            if (j != w.length() && isPal(suf)) {    // partner goes on the RIGHT
                String back = new StringBuilder(pref).reverse().toString();
                Integer k = index.get(back);
                if (k != null && !back.equals(w)) res.add(Arrays.asList(i, k));
            }
        }
    }
    return res;
}

private boolean isPal(String s) {
    int i = 0, j = s.length() - 1;
    while (i < j) if (s.charAt(i++) != s.charAt(j--)) return false;
    return true;
}
```

```python
# python
# LC 336 - Palindrome Pairs
# IDEA: {word: index}; for each split point, probe for the reversed other half
# time = O(n * k^2), space = O(n * k)
def palindromePairs(words: list) -> list:
    index = {w: i for i, w in enumerate(words)}
    res = []
    for i, w in enumerate(words):
        n = len(w)
        for j in range(n + 1):
            pref, suf = w[:j], w[j:]
            if pref == pref[::-1]:                 # partner goes on the LEFT
                back = suf[::-1]
                if back in index and back != w:
                    res.append([index[back], i])
            if j != n and suf == suf[::-1]:        # partner goes on the RIGHT
                back = pref[::-1]
                if back in index and back != w:
                    res.append([i, index[back]])
    return res
```

**兩個讓它正確的檢查**（兩個都是去重邏輯，也都是面試的 follow-up）：
- `back != w` — 一個單字不能跟自己配對（題目保證單字互異）。
- 第二個分支裡的 `j != n` — 少了它，`w` / `reverse(w)` 這種配對在「空後綴切法」和「空前綴切法」各會吐出兩組有序對，於是每一組都被**重複回報兩次**。

**空字串自然就處理好了**：`words = ["a", ""]` 會同時得到 `[0,1]` 和 `[1,0]`，因為 `""` 從兩邊看都是回文。

---

### 頻率 Map + 最大頻率的算術（貪婪排程） ⭐⭐⭐⭐

**模式**：一個計數 map，但**個別**的計數根本不重要 — 重要的只有 **`maxFreq`** 和**有幾個鍵並列最大**（`countOfMax`，一個只看一項的「計數的計數」）。答案就是一條封閉形式的公式，不用模擬，也不用堆積。

**關鍵想法（LC 621）**：出現最多次的任務決定了整個排法。它切出 `maxFreq - 1` 個寬度為 `n + 1` 的完整框，再加上最後一個框，裝下所有並列最大的任務。

```text
tasks = AAABBB, n = 2   → maxFreq = 3, countOfMax = 2 (A and B)

  | A B idle | A B idle | A B
  \___ n+1 ___/\___ n+1 __/ \_countOfMax_/

  slots = (3-1)*(2+1) + 2 = 8
```

**遞推式**：`answer = max(len(tasks), (maxFreq - 1) * (n + 1) + countOfMax)`
那個 `max(len(tasks), ...)` 在**任務種類多到根本不需要閒置**時才會派上用場 — 這時公式本身會少算。

```java
// java
// LC 621 - Task Scheduler
// IDEA: only the max frequency and how many tasks tie for it matter
// time = O(N), space = O(1)  (26 keys)
public int leastInterval(char[] tasks, int n) {
    int[] freq = new int[26];
    int maxFreq = 0;
    for (char t : tasks) maxFreq = Math.max(maxFreq, ++freq[t - 'A']);
    int countOfMax = 0;
    for (int f : freq) if (f == maxFreq) countOfMax++;
    int slots = (maxFreq - 1) * (n + 1) + countOfMax;
    return Math.max(tasks.length, slots);           // no idle time needed if tasks are diverse
}

// LC 767 - Reorganize String  (same max-frequency test, then even/odd fill)
// time = O(n), space = O(n)
public String reorganizeString(String s) {
    int[] cnt = new int[26];
    int maxFreq = 0, maxChar = 0;
    for (char c : s.toCharArray()) {
        cnt[c - 'a']++;
        if (cnt[c - 'a'] > maxFreq) { maxFreq = cnt[c - 'a']; maxChar = c - 'a'; }
    }
    int n = s.length();
    if (maxFreq > (n + 1) / 2) return "";           // impossible

    char[] res = new char[n];
    int i = 0;
    while (cnt[maxChar] > 0) {                      // most frequent char at even slots first
        res[i] = (char) ('a' + maxChar); i += 2; cnt[maxChar]--;
    }
    for (int c = 0; c < 26; c++) {
        while (cnt[c] > 0) {
            if (i >= n) i = 1;                      // wrap to odd slots
            res[i] = (char) ('a' + c); i += 2; cnt[c]--;
        }
    }
    return new String(res);
}
```

```python
# python
# LC 621 - Task Scheduler
# IDEA: (maxFreq - 1) frames of width (n + 1), plus every task tied for maxFreq
# time = O(N), space = O(1)  (26 keys)
from collections import Counter

def leastInterval(tasks: list, n: int) -> int:
    freq = Counter(tasks)
    max_freq = max(freq.values())
    count_of_max = sum(1 for f in freq.values() if f == max_freq)
    return max(len(tasks), (max_freq - 1) * (n + 1) + count_of_max)

# python
# LC 767 - Reorganize String
# IDEA: feasible iff max_freq <= (n+1)//2; fill slots 0,2,4,... then 1,3,5,... in freq order
# time = O(n log 26) ~ O(n), space = O(n)
def reorganizeString(s: str) -> str:
    freq = Counter(s)
    if max(freq.values()) > (len(s) + 1) // 2:
        return ""
    res = [''] * len(s)
    i = 0
    for ch, cnt in freq.most_common():          # most frequent first — this is what makes it work
        for _ in range(cnt):
            if i >= len(s):
                i = 1                           # even slots exhausted → switch to odd slots
            res[i] = ch
            i += 2
    return "".join(res)
```

**為什麼奇偶位填法有效**：擺在 `i` 和 `i+2` 的兩份永遠不相鄰，唯一的風險是繞回頭的接點 — 而那裡剛好安全，因為 `max_freq <= (n+1)//2` 保證了出現最多次的字元完全塞得進偶數位。

| 題目 | LC# | `maxFreq` 決定了什麼 |
|---------|-----|------------------------|
| Task Scheduler | 621 | 總時間 = 出現最多次的任務切出的框 |
| Reorganize String | 767 | 可行性：`maxFreq <= (n+1)/2` |

---

## LC 範例

### 2-1) Contiguous Array（LC 525）

**核心模式：轉換 + 前綴和 + HashMap**

#### 關鍵概念
找出是否存在`至少 2 個索引`具有`相同的計數`（running sum）。

這等同於在下面的圖裡找出`任意 2 個 y 值相同的 x`。

#### 模式拆解

**1. 問題轉換：**
```text
Transform the binary array:
- Treat 0 as -1
- Treat 1 as +1

Why? Equal 0s and 1s → sum of transformed array = 0
```

**2. HashMap 的結構：**
```java
Map<Integer, Integer> map = new HashMap<>();
// {count: first_index_where_count_occurred}

map.put(0, -1); // Initialize for subarrays starting at index 0
```

**3. 核心邏輯：**
```text
count: running sum (cumulative)
  - +1 for each 1
  - -1 for each 0

If count(i) == count(j) where i < j:
  → Elements between i and j sum to 0
  → Subarray [i+1, j] is balanced (equal 0s and 1s)
  → Length = j - i
```

**4. 為什麼只存「第一次」出現？**
```text
To maximize length, we want the earliest index with this count.
If count appears at indices [3, 7, 10]:
  - Store index 3
  - When we see count again at index 10, length = 10 - 3 = 7 (maximum)
```

**5. 為什麼要初始化 map.put(0, -1)？**
```text
If from index 0 to i, count = 0:
  → Entire subarray [0, i] is balanced
  → Length = i - (-1) = i + 1 ✓

Without this initialization, we'd miss subarrays starting at index 0.
```

#### 圖解範例
序列：`[0, 0, 0, 0, 1, 1]`
計數變化（0→-1、1→+1）：0 → -1 → -2 → -3 → -4 → -3 → -2

計數在索引 2 和索引 5 都回到 **-2**。長度 = 5 - 2 = **4**，也就是子陣列 `nums[3..5] = [0, 1, 1]` — 等等，講精確一點：這個子陣列是 `nums[index2+1 .. index5] = nums[3..5] = [0,1,1]`……其實 map 裡的索引代表的是這個 running count 上次出現的位置，所以長度 = `i - map[count]` = `5 - 1 = 4`，對應的子陣列是 `nums[2..5] = [0,0,1,1]`（4 個元素，2 個 0 和 2 個 1 ✓）。

<p align="center"><img src="../pic/lc_525_1.png"></p>

#### 數學上的道理

**為什麼計數相同就代表子陣列平衡：**
```text
Let count(i) = cumulative sum at index i

If count(i) == count(j) where i < j:
  count(j) - count(i) = 0

This means:
  sum of elements from index (i+1) to j = 0

In transformed array (0→-1, 1→+1):
  sum = 0 means equal number of -1s and +1s
  → equal number of 0s and 1s in original array
```

#### 實作模板

```java
// Java Template
public int findMaxLength(int[] nums) {
    // Map: {count: first_index_where_count_occurred}
    Map<Integer, Integer> map = new HashMap<>();

    // Initialize: handle subarrays starting at index 0
    map.put(0, -1);

    int maxLen = 0;
    int count = 0;

    for (int i = 0; i < nums.length; i++) {
        // Transform: 0 → -1, 1 → +1
        count += (nums[i] == 1) ? 1 : -1;

        // If count seen before: calculate subarray length
        if (map.containsKey(count)) {
            maxLen = Math.max(maxLen, i - map.get(count));
        } else {
            // Store FIRST occurrence only (for max length)
            map.put(count, i);
        }
    }

    return maxLen;
}
```

```python
# Python Template
def findMaxLength(nums):
    # Map: {count: first_index_where_count_occurred}
    d = {0: -1}  # Initialize for subarrays starting at index 0

    max_len = 0
    count = 0

    for i, num in enumerate(nums):
        # Transform: 0 → -1, 1 → +1
        count += 1 if num == 1 else -1

        # If count seen before: calculate subarray length
        if count in d:
            max_len = max(max_len, i - d[count])
        else:
            # Store FIRST occurrence only (for max length)
            d[count] = i

    return max_len
```

#### 和 LC 560 模式的關鍵差異

| 面向 | LC 560（Subarray Sum K） | LC 525（Contiguous Array） |
|--------|-------------------------|---------------------------|
| **目標** | 數出**所有**子陣列 | 找**最長**的子陣列 |
| **Map 的值** | `count`（出現次數） | `index`（第一次出現的位置） |
| **Map 更新** | 每次都累加次數 | 只在計數第一次出現時寫入 |
| **檢查式** | `presum - k` | 相同的 `count` |
| **初始化** | `{0: 1}` | `{0: -1}` |

#### 同模式的相關題目

- **LC 525**：Contiguous Array（就是這個模式）
- **LC 1124**：Longest Well-Performing Interval（類似的轉換）
- **LC 523**：Continuous Subarray Sum（改成取模的轉換）
- **LC 325**：Maximum Size Subarray Sum Equals k（前綴和 + 索引）

---

### 2-1-1) Subarray Sums Divisible by K（LC 974）

**核心模式：前綴和 + 模運算 + HashMap**

#### 關鍵概念
用餘數追蹤來數出**所有**和可被 K 整除的子陣列。

如果兩個前綴和**對 K 取模的餘數相同**，它們的差就能被 K 整除。

#### 模式拆解

**1. 數學基礎：**
```text
If prefix[i] % k == prefix[j] % k  (where j < i)

Then:
  (prefix[i] - prefix[j]) % k == 0

Which means:
  prefix[i] - prefix[j] = sum of nums[j+1 .. i]

Therefore:
  The subarray [j+1, i] has a sum divisible by k
```

**2. HashMap 的結構：**
```java
Map<Integer, Integer> map = new HashMap<>();
// {remainder: count}  ← Store COUNT, not index (similar to LC 560)

map.put(0, 1); // Initialize for subarrays starting from beginning
```

**3. 為什麼存餘數的「次數」而不是「索引」？**
```text
This is a "count ALL subarrays" problem (like LC 560).

If remainder 3 appears at indices [2, 5, 8]:
  - When we reach index 5: add 1 (subarray from index 2 to 5)
  - When we reach index 8: add 2 (subarrays from 2→8 and 5→8)

Total: 3 valid subarrays
```

**4. 關鍵：處理負餘數**
```java
int remainder = prefixSum % k;

// MUST adjust negative remainders to positive
if (remainder < 0) {
    remainder += k;
}

// Or use this one-liner:
remainder = ((prefixSum % k) + k) % k;
```

**為什麼？** 在 Java／Python 裡 `-7 % 5 = -2`，但我們要的是餘數 3（因為 -2 ≡ 3 mod 5）。

**5. 初始化：為什麼是 map.put(0, 1)？**
```text
If prefixSum % k == 0 at some index i:
  → The entire subarray [0, i] is divisible by k
  → We need to count this case

Without initialization, we'd miss these subarrays.
```

#### 圖解範例

**輸入：** `nums = [4, 5, 0, -2, -3, 1]`、`k = 5`

**前綴和：** `[4, 9, 9, 7, 4, 5]`

**餘數（mod 5）：** `[4, 4, 4, 2, 4, 0]`

| 索引 | Num | PrefixSum | 餘數 | Map 狀態 | 新增次數 | 累計 |
|-------|-----|-----------|-----------|-----------|-------------|-------------|
| - | - | 0 | 0 | {0:1} | - | 0 |
| 0 | 4 | 4 | 4 | {0:1, 4:1} | 0 | 0 |
| 1 | 5 | 9 | 4 | {0:1, 4:2} | +1 | 1 |
| 2 | 0 | 9 | 4 | {0:1, 4:3} | +2 | 3 |
| 3 | -2 | 7 | 2 | {0:1, 4:3, 2:1} | 0 | 3 |
| 4 | -3 | 4 | 4 | {0:1, 4:4, 2:1} | +3 | 6 |
| 5 | 1 | 5 | 0 | {0:2, 4:4, 2:1} | +1 | **7** |

**結果：** 7 個子陣列的和可被 5 整除

**找到的子陣列：**
1. `[4,5,0,-2,-3,1]`（整個陣列，結尾餘數 0）
2. `[5]`（索引 0 與 1 的餘數同為 4）
3. `[5,0]`（索引 0 與 2 的餘數同為 4）
4. `[5,0,-2,-3]`（索引 0 與 4 的餘數同為 4）
5. `[0]`（索引 1 與 2 的餘數同為 4）
6. `[0,-2,-3]`（索引 1 與 4 的餘數同為 4）
7. `[-2,-3]`（索引 2 與 4 的餘數同為 4）

#### 實作模板

```java
// Java Template
public int subarraysDivByK(int[] nums, int k) {
    // Map: {remainder: count}
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1); // Handle subarrays from beginning

    int count = 0;
    int prefixSum = 0;

    for (int num : nums) {
        prefixSum += num;

        // Calculate remainder (handle negatives!)
        int remainder = prefixSum % k;
        if (remainder < 0) {
            remainder += k;
        }
        // Or: int remainder = ((prefixSum % k) + k) % k;

        // Add count of all previous same remainders
        count += map.getOrDefault(remainder, 0);

        // Update remainder count
        map.put(remainder, map.getOrDefault(remainder, 0) + 1);
    }

    return count;
}
```

```python
# Python Template
def subarraysDivByK(nums, k):
    # Map: {remainder: count}
    remainder_count = {0: 1}

    count = 0
    prefix_sum = 0

    for num in nums:
        prefix_sum += num

        # Calculate remainder (Python % handles negatives correctly)
        remainder = prefix_sum % k

        # Add count of all previous same remainders
        count += remainder_count.get(remainder, 0)

        # Update remainder count
        remainder_count[remainder] = remainder_count.get(remainder, 0) + 1

    return count
```

**注意：** Python 的 `%` 一定回傳正餘數，所以不需要調整。

#### 最佳化：用陣列取代 HashMap

既然餘數一定落在 `[0, k-1]`，改用陣列會更快：

```java
public int subarraysDivByK(int[] nums, int k) {
    int[] remainderCount = new int[k];
    remainderCount[0] = 1;

    int count = 0;
    int prefixSum = 0;

    for (int num : nums) {
        prefixSum += num;
        int remainder = ((prefixSum % k) + k) % k;

        count += remainderCount[remainder];
        remainderCount[remainder]++;
    }

    return count;
}
```

**時間複雜度：** O(N)
**空間複雜度：** O(K)，不是 O(N)

#### 和相關題目的關鍵差異

| 面向 | LC 560（Sum = K） | LC 974（Divisible by K） | LC 525（Equal 0/1） |
|--------|------------------|-------------------------|---------------------|
| **目標** | 數子陣列 | 數子陣列 | 找最長 |
| **Map 的鍵** | `prefixSum` | `prefixSum % k` | `count` |
| **Map 的值** | `count` | `count` | `first_index` |
| **檢查式** | `presum - k` | 相同的 `remainder` | 相同的 `count` |
| **特別處理** | 無 | **負餘數！** | 把 0 轉成 -1 |
| **初始化** | `{0: 1}` | `{0: 1}` | `{0: -1}` |

#### 關鍵：為什麼負餘數非處理不可

**範例：** `nums = [-1, -2, -3]`、`k = 5`

不調整的話：
```text
prefixSum = -1: remainder = -1 (wrong!)
prefixSum = -3: remainder = -3 (wrong!)
prefixSum = -6: remainder = -1 (wrong!)
```

調整之後：
```text
prefixSum = -1: remainder = 4 (correct: -1 ≡ 4 mod 5)
prefixSum = -3: remainder = 2 (correct: -3 ≡ 2 mod 5)
prefixSum = -6: remainder = 4 (correct: -6 ≡ 4 mod 5)
```

現在餘數 4 對上了 → 子陣列 `[-1]` 和 `[-2, -3]` 餘數相同 → 子陣列 `[-2, -3]` 的和可被 5 整除 ✓

#### 同模式的相關題目

- **LC 974**：Subarray Sums Divisible by K（就是這個模式）
- **LC 523**：Continuous Subarray Sum（一樣是整除，但多了長度 ≥ 2 的限制）
- **LC 560**：Subarray Sum Equals K（沒有取模，更單純）
- **LC 1248**：Count Nice Subarrays（轉換 + 計數模式）

---

### 2-1-2) Count Number of Nice Subarrays（LC 1248）

**核心模式：把奇數轉換掉 → 前綴和計數（跟 LC 560 一樣）**

#### 關鍵概念
把每個數字看成 0（偶數）或 1（奇數），再套上前綴和 + 雜湊表的模式，就能數出**剛好有 k 個奇數**的子陣列。

#### 核心想法

**轉換：** 把每個元素換成 `num % 2`（奇數是 1，偶數是 0）。

問題就變成：**數出和等於 k 的子陣列** — 這正是 LC 560！

```text
map: {oddCount: frequency}
     → "How many times has this odd-count appeared so far?"

At index i with current oddCount:
  → Find how many previous positions had exactly (oddCount - k) odds
  → Those form subarrays with exactly k odds ending at i
```

**為什麼要 `map.put(0, 1)`？**
```text
If oddCount == k at index i:
  → Entire subarray [0, i] has exactly k odds
  → oddCount - k = 0, must have {0: 1} pre-initialized
```

#### 實作模板

```java
// Java - LC 1248
public int numberOfSubarrays(int[] nums, int k) {
    // map: {oddCount: frequency}
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);  // base case: 0 odds seen 1 time

    int res = 0, oddCount = 0;

    for (int num : nums) {
        if (num % 2 == 1) oddCount++;  // treat odd as +1

        // How many previous positions had (oddCount - k) odds?
        res += map.getOrDefault(oddCount - k, 0);

        // Update count AFTER checking (critical order!)
        map.put(oddCount, map.getOrDefault(oddCount, 0) + 1);
    }

    return res;
}
```

```python
# python - LC 1248
# IDEA: prefix ODD-count + hashmap (same shape as LC 560)
# time: O(n), space: O(n)
# ref: leetcode_python/Array/count-number-of-nice-subarrays.py
class Solution:
    def numberOfSubarrays(self, nums, k):
        total_cnt = 0
        prefix_cnt = 0                 # running count of odd numbers so far

        cnt_map = {0: 1}              # {odd_count : frequency}; base case 0 odds seen once

        for val in nums:
            if val % 2 == 1:          # treat odd as +1 (even contributes 0)
                prefix_cnt += 1

            # NOTE: += get(prefix_cnt - k), NOT += 1
            #   there may be MULTIPLE earlier prefixes with the same odd count,
            #   each one gives a distinct valid subarray ending here
            total_cnt += cnt_map.get(prefix_cnt - k, 0)

            # record current prefix count AFTER checking (avoid self-count)
            cnt_map[prefix_cnt] = cnt_map.get(prefix_cnt, 0) + 1

        return total_cnt
```

> **為什麼是 `+= cnt_map.get(prefix_cnt - k, 0)` 而不是 `+= 1`？**
> `prefix_cnt - k`（那個「互補」的奇數個數）可能在前面好幾個索引都達到過。
> 每一個那樣的起點都能跟現在這個索引配成一個剛好有 `k` 個奇數的子陣列，
> 所以要把整個次數加進來 — 跟 LC 560 的「在前綴值上做 2-sum」是同一招。

#### 另一種做法：滑動視窗（atMost 技巧）

```java
// Exactly k = atMost(k) - atMost(k-1)
public int numberOfSubarrays(int[] nums, int k) {
    return atMost(nums, k) - atMost(nums, k - 1);
}

private int atMost(int[] nums, int k) {
    int l = 0, res = 0, oddCount = 0;
    for (int r = 0; r < nums.length; r++) {
        if (nums[r] % 2 == 1) oddCount++;
        while (oddCount > k) {
            if (nums[l] % 2 == 1) oddCount--;
            l++;
        }
        res += (r - l + 1);
    }
    return res;
}
```

#### 和相關題目的關鍵差異

| 面向 | LC 560（Sum = K） | LC 930（Binary Sum = K） | LC 1248（Nice Subarrays） |
|--------|-----------------|------------------------|--------------------------|
| **轉換** | 不用（直接用值） | 值本來就是 0/1 | `num % 2` → 0 或 1 |
| **Map 的鍵** | `prefixSum` | `prefixSum` | `oddCount` |
| **Map 的值** | `count` | `count` | `count` |
| **初始化** | `{0: 1}` | `{0: 1}` | `{0: 1}` |

#### 同模式的相關題目

- **LC 560**：Subarray Sum Equals K（完全同一個模式，不用轉換）
- **LC 930**：Binary Subarrays with Sum（值本來就是 0/1，想法一樣）
- **LC 974**：Subarray Sums Divisible by K（取模的變形）
- **LC 1248**：Count Nice Subarrays（本題 — 先轉成 0/1 再套 LC 560）

---

### 2-2) Continuous Subarray Sum — LC 523
- 概念跟 Contiguous Array（LC 525）相近

```python
# 523 Continuous Subarray Sum
# IDEA : HASH TABLE
# -> if sum(nums[i:j]) % k == 0 for some i < j, 
#   ->  then sum(nums[:j]) % k == sum(nums[:i]) % k  !!!!
#   -> So we just need to use a dict to keep track of sum(nums[:i]) % k 
#   -> and the corresponding index i. Once some later sum(nums[:i']) % k == sum(nums[:i]) % k and i' - i > 1, so we return True.
class Solution(object):
    def checkSubarraySum(self, nums, k):
        """
        # _dict = {0:-1} : for edge case (need to find a continuous subarray of size AT LEAST two )
        # https://leetcode.com/problems/continuous-subarray-sum/discuss/236976/Python-solution
        # 0: -1 is for edge case that current sum mod k == 0
        # demo :
                In [93]: nums = [0]
                    ...: k = 1
                    ...:
                    ...:
                    ...: s = Solution()
                    ...: r = s.checkSubarraySum(nums, k)
                    ...: print (r)
                0
                i - _dict[tmp] = 1
                False
        """
        ### NOTE : we need to init _dict as {0:-1}
        _dict = {0:-1}
        tmp = 0
        for i in range(len(nums)):
            tmp += nums[i]
            if k != 0:
                ### NOTE : we get remainder of tmp by k
                tmp = tmp % k
            # if tmp in _dict, means there is the other sub part make sub array sum % k == 0
            if tmp in _dict:
                ### only if continuous sub array with length >= 2
                if i - _dict[tmp] > 1:
                    return True
            else:
                _dict[tmp] = i
        return False
```

### 2-3) Group Anagrams — LC 49

**想法**：把每個字串排序，當成標準化的雜湊鍵；鍵相同的就分到同一組。

> 標準解跟分組模板放在一起，見 [hash_map.md → Template 3: Grouping by a Computed Key](./hash_map.md#template-3-grouping-by-a-computed-key)。

### 2-3') Longest Substring Without Repeating Characters — LC 3
```python
# LC 003
# IDEA : TWO POINTER + SLIDING WINDOW + DICT (NOTE this method !!!!)
#       -> use a hash table (d) record visited "element" (e.g. : a,b,c,...)
#          (but NOT sub-string)
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        d = {}
        # left pointer
        l = 0
        res = 0
        """
        NOTE !!!

        we move right pointer first, then left pointer
        """
        # NOTE !!! right pointer
        for r in range(len(s)):
            """
            ### NOTE : deal with "s[r] in d" case ONLY !!! 
            ### NOTE : if already visited, means "repeating"
            #      -> then we need to update left pointer (l)
            """
            if s[r] in d:
                """
                NOTE !!! this
                -> via max(l, d[s[r]] + 1) trick,
                   we can get the "latest" idx of duplicated s[r], and start from that one
                """
                l = max(l, d[s[r]] + 1)
            # if not visited yet, record the alphabet
            # and re-calculate the max length
            d[s[r]] = r
            res = max(res, r -l + 1)
        return res
```

### 2-4) Count Primes — LC 204
```python
# LC 204 Count Primes
# IDEA : dict
# https://leetcode.com/problems/count-primes/discuss/1343795/python%3A-sieve-of-eretosthenes
# prime(x) : check if x is a prime
# prime(0) = 0
# prime(1) = 0
# prime(2) = 0
# prime(3) = 1
# prime(4) = 2
# prime(5) = 3
# python 3
class Solution:
    def countPrimes(self, n):
        # using sieve of eretosthenes algorithm
        if n < 2: return 0
        nonprimes = set()
        for i in range(2, round(n**(1/2))+1):
            if i not in nonprimes:
                for j in range(i*i, n, i):
                    nonprimes.add(j)
        return n - len(nonprimes) - 2  # remove prime(1), prime(2)
```

### 2-5) Valid Sudoku — LC 36
```python
# python
# LC 036 Valid Sudoku
class Solution(object):
    def isValidSudoku(self, board):
        """
        :type board: List[List[str]]
        :rtype: bool
        """
        n = len(board)
        return self.isValidRow(board) and self.isValidCol(board) and self.isValidNineCell(board)
        
    def isValidRow(self, board):
        n = len(board)
        for r in range(n):
            row = [x for x in board[r] if x != '.']
            if len(set(row)) != len(row): # if not repetition 
                return False
        return True

    def isValidCol(self, board):
        n = len(board)
        for c in range(n):
            col = [board[r][c] for r in range(n) if board[r][c] != '.']
            if len(set(col)) != len(col): # if not repetition 
                return False
        return True

    def isValidNineCell(self, board):
        n = len(board)
        for r in range(0, n, 3):
            for c in range(0, n, 3):
                cell = []
                for i in range(3):
                    for j in range(3):
                        num = board[r + i][c + j]
                        if num != '.':
                            cell.append(num)
                if len(set(cell)) != len(cell): # if not repetition 
                    return False
        return True
```
> **注意**：LC 36 只問**目前這個盤面**合不合法 — 它沒有要你把數獨解出來。
> 回溯法的解題器回答的是另一個問題（那是 LC 37），而且它可能把不合法的盤面判成合法，
> 因為它從來不去檢查那些已填好的格子彼此之間有沒有衝突。

```java
// java
// LC 36 Valid Sudoku
// IDEA: one pass, three sets per index -- row, column and 3x3 box.
//       The box index is (r/3)*3 + c/3, which is the whole trick.
// time = O(81) = O(1), space = O(81) = O(1)
public boolean isValidSudoku(char[][] board) {
    Set<Character>[] rows = new HashSet[9];
    Set<Character>[] cols = new HashSet[9];
    Set<Character>[] boxes = new HashSet[9];
    for (int i = 0; i < 9; i++) {
        rows[i] = new HashSet<>();
        cols[i] = new HashSet<>();
        boxes[i] = new HashSet<>();
    }

    for (int r = 0; r < 9; r++) {
        for (int c = 0; c < 9; c++) {
            char v = board[r][c];
            if (v == '.') continue;

            /** NOTE !!! the box a cell belongs to */
            int b = (r / 3) * 3 + c / 3;

            // Set.add returns false when the value was already there -> duplicate
            if (!rows[r].add(v) || !cols[c].add(v) || !boxes[b].add(v)) {
                return false;
            }
        }
    }
    return true;
}
```

### 2-6) Pairs of Songs With Total Durations Divisible by 60 — LC 1010
```python
# LC 1010. Pairs of Songs With Total Durations Divisible by 60
# IDEA : dict
# IDEA : NOTE : we only count "NUMBER OF PAIRS", instead get all pairs indexes
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        rem = {}
        pairs = 0
        for t in time:
            #print ("rem = " + str(rem))
            t %= 60
            if (60 - t) % 60 in rem:
                """
                NOTE : this trick
                -> we append "all 60 duration combinations count" via the existing times of element "(60 - t) % 60" 
                """
                pairs += rem[(60 - t) % 60]
            if t not in rem:
                rem[t] = 1
            else:
                ### NOTE : here : we plus 1 when an element already exist
                rem[t] += 1
        return pairs
```

### 2-7) Subarray Sum Equals K — LC 560
```python
# LC 560 : Subarray Sum Equals K

# IDEA : HASH TABLE + sub array sum
# IDEA : https://blog.csdn.net/fuxuemingzhu/article/details/82767119
class Solution(object):
    def subarraySum(self, nums, k):
        n = len(nums)
        d = collections.defaultdict(int)
        d[0] = 1
        sum = 0
        res = 0
        for i in range(n):
            sum += nums[i]
            # if sum - k in d
            #  -> if sum - (every _ in d) == k
            if sum - k in d:
                res += d[sum - k]
            d[sum] += 1
        return res
```
```java
// LC 560 : Subarray Sum Equals K
// java
// (algorithm book (labu) p.350)
// V1 : brute force + cum sum
int subarraySum(int[] nums, int k){
    int n = nums.length;
    // init pre sum
    int[] sum = new int[n+1];
    sum[0] = 0;
    for (int i = 0; i < n; i++){
        sum[i+1] = sum[i] + nums[i];
    }

    int ans = 0;
    // loop over all sub array
    for (int i=1; i <= n; i++){
        for (int j=0; j < i; j++){
            // sum of nums[j...i-1]
            if (sum[i] - sum[j] == k){
                ans += 1;
            }
        }
    }
    return ans;
}

// (algorithm book (labu) p.350)
// V2 : hash map + cum sum
int subarraySum(int[] nums, int k){
    int n = nums.length;
    // map :  key : prefix, value : prefix exists count
    // init hash map
    HashMap<Integer, Integer> preSum = new HashMap<Integer, Integer>();

    // base case
    preSum.put(0,1);

    int ans = 0;
    int sum0_i = 0;

    for (int i = 0; i < n; i++){
        sum0_i += nums[i];
        // for presum : nums[0..j]
        int sum0_j = sum0_i - k;
        // if there is already presum, update the ans directly
        if (preSum.containsKey(sum0_j)){
            ans += preSum.get(sum0_j);
        }
        // add prefix and nums[0..i] and record exists count
        preSum.put(sum0_i, preSum.getOrDefault(sum0_i,0) + 1);
    }
    return ans;
}
```

### 2-8) K-diff Pairs in an Array — LC 532
```python
# LC 532 K-diff Pairs in an Array
# V0
# IDEA : HASH TABLE
import collections
class Solution(object):
    def findPairs(self, nums, k):
        answer = 0
        cnt = collections.Counter(nums)
        # NOTE THIS : !!! we use set(nums) for reduced time complexity, and deal with k == 0 case separately
        for num in set(nums):
            """
            # [b - a] = k
            #  -> b - a = +k or -k
            #  -> b = k + a or b = -k + a
            #  -> however, 0 <= k <= 10^7, so ONLY b = k + a is possible

            2 cases
                -> case 1) k > 0 and num + k in cnt
                -> case 2) k == 0 and cnt[num] > 1
            """
            # case 1) k > 0 and num + k in cnt
            if k > 0 and num + k in cnt: # | a - b | = k -> a - b = +k or -k, but here don't have to deal with "a - b = -k" case, since this sutuation will be covered when go through whole nums  
                answer += 1
            # case 2) k == 0 and cnt[num] > 1
            if k == 0 and cnt[num] > 1:  # for cases k = 0 ->  pair like (1,1) will work. (i.e. 1 + (-1))
                answer += 1
        return answer

# V0'
# IDEA : SORT + BRUTE FORCE + BREAK
class Solution(object):
    def findPairs(self, nums, k):
        # edge case
        if not nums and k:
            return 0
        nums.sort()
        res = 0
        tmp = []
        for i in range(len(nums)):
            for j in range(i+1, len(nums)):
                if abs(nums[j] - nums[i]) == k:
                    cur = [nums[i], nums[j]]
                    cur.sort()
                    if cur not in tmp:
                        res += 1
                        tmp.append(cur)
                elif abs(nums[j] - nums[i]) > k:
                    break
        return res
```

### 2-9) Sentence Similarity — LC 734
```python
# LC 734. Sentence Similarity
# V0'
# https://zxi.mytechroad.com/blog/hashtable/leetcode-734-sentence-similarity/
import collections
class Solution(object):
    def areSentencesSimilar(self, words1, words2, pairs):
        if len(words1) != len(words2): return False
        similars = collections.defaultdict(set)
        for w1, w2 in pairs:
            similars[w1].add(w2)
            similars[w2].add(w1)
        for w1, w2 in zip(words1, words2):
            if w1 != w2 and w2 not in similars[w1]:
                return False
        return True

# V0
# IDEA : array op
#   -> Apart from edge cases
#   -> there are cases we need to consider
#     -> 1) if sentence1[i] == sentence2[i]
#     -> 2) if sentence1[i] != sentence2[i] and
#           -> [sentence1[i], sentence2[i]] in similarPairs
#           -> [sentence2[i], sentence1[i]] in similarPairs
class Solution(object):
    def areSentencesSimilar(self, sentence1, sentence2, similarPairs):
        # edge case
        if sentence1 == sentence2:
            return True
        if len(sentence1) != len(sentence2):
            return False
        for i in range(len(sentence1)):
            tmp = [sentence1[i], sentence2[i]]
            """
            NOTE : below condition
                1) sentence1[i] != sentence2[i]
                  AND
                2) (tmp not in similarPairs and tmp[::-1] not in similarPairs)

                -> return false
            """
            if sentence1[i] != sentence2[i] and (tmp not in similarPairs and tmp[::-1] not in similarPairs):
                return False
        return True
```

### 2-10) LRU Cache — LC 146
```python
# LC 146 LRU Cache
# note : there is also array/queue approach
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

### 2-11) Find All Anagrams in a String — LC 438
```python
# LC 438. Find All Anagrams in a String
# IDEA : SLIDING WINDOW + collections.Counter()
class Solution(object):
    def findAnagrams(self, s, p):
        ls, lp = len(s), len(p)
        cp = collections.Counter(p)
        cs = collections.Counter()
        ans = []
        for i in range(ls):
            cs[s[i]] += 1
            if i >= lp:
                cs[s[i - lp]] -= 1
                ### BE AWARE OF IT
                if cs[s[i - lp]] == 0:
                    del cs[s[i - lp]]
            if cs == cp:
                ans.append(i - lp + 1)
        return ans
```

### 2-12) Brick Wall — LC 554
```python
# LC 554. Brick Wall
# IDEA : HASH TABLE + COUNTER UPDATE (looping every element in the list and cumsum and 
import collections
class Solution(object):
    def leastBricks(self, wall):
        _counter = collections.Counter()
        count = 0
        # go through every sub-wall in wall
        for w in wall:
            cum_sum = 0
            # go through every element in sub-wall
            for i in range(len(w) - 1):
                cum_sum += w[i]
                ### NOTE we can update collections.Counter() via below
                _counter.update([cum_sum])
                count = max(count, _counter[cum_sum])
        return len(wall) - count
```

### 2-13) Maximum Size Subarray Sum Equals k — LC 325

```java
// LC 325 — prefix sum + hashmap, store FIRST occurrence (max length variant)
// Key: prefixSum[j] - prefixSum[i] = k  →  check if (curSum - k) exists in map
public int maxSubArrayLen(int[] nums, int k) {
    Map<Integer, Integer> preSumMap = new HashMap<>();
    preSumMap.put(0, -1); // handle subarrays starting at index 0

    int curSum = 0, maxSize = 0;
    for (int i = 0; i < nums.length; i++) {
        curSum += nums[i];
        if (preSumMap.containsKey(curSum - k)) {
            maxSize = Math.max(maxSize, i - preSumMap.get(curSum - k));
        }
        preSumMap.putIfAbsent(curSum, i); // store FIRST occurrence only
    }
    return maxSize;
}
```

### 2-14) Smallest Common Region — LC 1257

```java
// java
// LC 1257

// IDEA: HASHMAP (fixed by gpt)
// TODO: validate
public String findSmallestRegion_0_1(List<List<String>> regions, String region1, String region2) {

    // Map each region to its parent
    /**
     *  NOTE !!!
     *
     *   map : {child : parent}
     *
     *   -> so the key is child, and the value is its parent
     *
     */
    Map<String, String> parentMap = new HashMap<>();

    for (List<String> regionList : regions) {
        String parent = regionList.get(0);
        for (int i = 1; i < regionList.size(); i++) {
            parentMap.put(regionList.get(i), parent);
        }
    }

    // Track ancestors of region1
    /**  NOTE !!!
     *
     *  we use `set` to track `parents` (ancestors)
     *  if exists, add it to set,
     *  and set `current region` as its `parent`
     *
     */
    Set<String> ancestors = new HashSet<>();
    while (region1 != null) {
        ancestors.add(region1);
        region1 = parentMap.get(region1);
    }

    // Traverse region2’s ancestors until we find one in region1’s ancestor set
    while (!ancestors.contains(region2)) {
        region2 = parentMap.get(region2);
    }

    return region2;
}
```

---

### 2-15) Tuple with Same Product（LC 1726）

**核心想法：配對乘積的頻率 → 組合計數**

給一個元素互異的正整數陣列，數出滿足 `a * b = c * d` 的四元組 `(a, b, c, d)`。

#### 關鍵洞見

1. 對所有 `i < j` 算出**每一組配對的乘積** `nums[i] * nums[j]`
2. 數出**有幾組配對**共用同一個乘積
3. 若某個乘積出現 `n` 次，任取兩組配對 → `C(n, 2) = n*(n-1)/2` 種組合
4. 每一種配對組合會產生 **8 個四元組**（`(a,b,c,d)` 的排列）

**為什麼是 8？** 給定兩組乘積相同的配對 `(a,b)` 與 `(c,d)`：
- 第一組內部交換：`(a,b)` 或 `(b,a)` → 2 種
- 第二組內部交換：`(c,d)` 或 `(d,c)` → 2 種
- 交換哪一組當 `(a,b)`、哪一組當 `(c,d)` → 2 種
- 合計：`2 × 2 × 2 = 8`

#### 模式

```text
Step 1: Build productCount map
  for i in [0, n):
    for j in (i, n):
      productCount[nums[i]*nums[j]]++

Step 2: For each count n >= 2:
  ans += C(n, 2) * 8
       = n*(n-1)/2 * 8
       = 4 * n * (n-1)
```

#### Java 實作

```java
// LC 1726 - Tuple with Same Product
// Time: O(N^2)  Space: O(N^2)
public int tupleSameProduct(int[] nums) {
    Map<Integer, Integer> productCount = new HashMap<>();

    // Step 1: count frequency of each pair product
    for (int i = 0; i < nums.length; i++) {
        for (int j = i + 1; j < nums.length; j++) {
            int product = nums[i] * nums[j];
            productCount.put(product, productCount.getOrDefault(product, 0) + 1);
        }
    }

    // Step 2: for each product with n pairs, C(n,2) * 8 tuples
    int ans = 0;
    for (int count : productCount.values()) {
        if (count >= 2) {
            ans += count * (count - 1) / 2 * 8;
            // equivalent: ans += 4 * count * (count - 1);
        }
    }
    return ans;
}
```

#### 兩種公式其實等價

```text
C(n,2) * 8
= n*(n-1)/2 * 8
= 4 * n * (n-1)
```

兩種寫法都對。`4 * count * (count - 1)` 這種寫法可以避開整數除法。

#### 同模式的相關題目

| 題目 | LC# | 難度 | 模式 |
|---------|-----|------------|---------|
| Tuple with Same Product | 1726 | Medium | 配對乘積 → C(n,2) × 8 |
| Number of Good Pairs | 1512 | Easy | 配對計數 → C(n,2) |
| Number of Boomerangs | 447 | Medium | 配對距離的頻率 → n*(n-1) |
| Count Number of Texts | 2266 | Medium | 頻率 → 組合計數 |

**和 LC 1512（Good Pairs）的關鍵差異：**
- LC 1512：數出 `nums[i] == nums[j]` 的配對 → 每個值 `C(n,2)`
- LC 1726：從乘積相同的配對數出**四元組** → 每個乘積 `C(n,2) * 8`

---

### 2-16) Minimum Operations to Sort Binary Tree by Level（LC 2471）

**核心模式：逐層 BFS + 用 `{value: index}` 雜湊表求最少交換次數**

> LC 2471 - Minimum Number of Operations to Sort a Binary Tree by Level
> https://leetcode.com/problems/minimum-number-of-operations-to-sort-a-binary-tree-by-level/

#### 關鍵概念

每次操作可以交換**同一層裡任兩個節點的值**。要讓整棵樹逐層排好，
答案就是**每一層各自排序所需的最少交換次數，加總起來**。

所以問題拆成兩塊，彼此獨立：
1. **BFS** 把每一層的值收集成一個陣列。
2. **求每個陣列排序的最少交換次數** — 雜湊表就是在這裡發光。

#### 雜湊表的招數：把陣列排好的最少交換次數

**關鍵想法**：要用**最少**交換次數把陣列排好，就是反覆地
**用一次交換把正確的值放到每個索引上**。而要做到 O(1) 的交換，
必須知道**每個值現在待在哪裡** → 這就是 `{value: index}` 雜湊表的用途。

```text
pos = {value: current_index}   # O(1) lookup of "where is value v right now?"

For each index i (left → right):
  correct_val = sorted_arr[i]            # what SHOULD be at index i
  if arr[i] != correct_val:
     swap_idx = pos[correct_val]         # where correct_val currently is
     # 1) UPDATE the map BEFORE swapping (critical!)
     pos[arr[i]]      = swap_idx         # the value we move away keeps its new home
     pos[correct_val] = i                # correct_val is now at i
     # 2) swap in the array
     arr[i], arr[swap_idx] = arr[swap_idx], arr[i]
     swaps += 1
```

**⚠️ 關鍵：交換「之前」先更新 map。** 交換之後，`arr[i]` 已經不是原本被擠掉的那個值了，
你救不回它的舊鍵。先把兩個新位置寫進 map，再去動陣列。

**為什麼這是最少的**：每一次成功的交換至少讓一個元素落到它最終的排序位置，
所以不會「浪費」任何一次交換。（這就是循環分解的結論：一個陣列需要 `n - (循環數)` 次交換；
這個貪婪的逐索引掃描剛好達到這個次數。）

#### 實作

```python
# python - LC 2471
from collections import deque

class Solution(object):
    def minimumOperations(self, root):
        # time  = O(N log M)  (M = widest level; sorting dominates per level)
        # space = O(M)
        q = deque([root])
        ops = 0

        while q:
            size = len(q)
            level = []
            for _ in range(size):
                node = q.popleft()
                level.append(node.val)
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)

            ops += self.min_swaps(level)   # add this level's cost

        return ops

    def min_swaps(self, arr):
        # min swaps to sort `arr` via {value: index} hashmap
        n = len(arr)
        sorted_arr = sorted(arr)
        pos = {v: i for i, v in enumerate(arr)}   # {value: current index}
        swaps = 0

        for i in range(n):
            correct_val = sorted_arr[i]
            if arr[i] != correct_val:
                swap_idx = pos[correct_val]

                # update map BEFORE swapping (so we don't lose arr[i]'s key)
                pos[arr[i]] = swap_idx
                pos[correct_val] = i

                # swap
                arr[i], arr[swap_idx] = arr[swap_idx], arr[i]
                swaps += 1

        return swaps
```

```java
// java - LC 2471
/**
 * time  = O(N log M)   // M = widest level; sorting dominates
 * space = O(M)
 */
public int minimumOperations(TreeNode root) {
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    int ops = 0;

    while (!q.isEmpty()) {
        int size = q.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            level.add(node.val);
            if (node.left != null)  q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
        ops += minSwaps(level);
    }
    return ops;
}

// min swaps to sort via {value: index} map
private int minSwaps(List<Integer> arr) {
    int n = arr.size();
    Integer[] sorted = arr.toArray(new Integer[0]);
    Arrays.sort(sorted);

    Map<Integer, Integer> pos = new HashMap<>();   // {value: current index}
    for (int i = 0; i < n; i++) pos.put(arr.get(i), i);

    int swaps = 0;
    for (int i = 0; i < n; i++) {
        int correctVal = sorted[i];
        if (!arr.get(i).equals(correctVal)) {
            int swapIdx = pos.get(correctVal);

            // update map BEFORE swapping
            pos.put(arr.get(i), swapIdx);
            pos.put(correctVal, i);

            // swap
            int tmp = arr.get(i);
            arr.set(i, arr.get(swapIdx));
            arr.set(swapIdx, tmp);
            swaps++;
        }
    }
    return swaps;
}
```

#### 圖解追蹤 — `min_swaps([3, 1, 2])`

```text
sorted = [1, 2, 3]
pos    = {3:0, 1:1, 2:2}

i=0: correct=1, arr[0]=3 (mismatch)
     swap_idx = pos[1] = 1
     update map: pos[3]=1, pos[1]=0  → pos = {3:1, 1:0, 2:2}
     swap arr[0],arr[1] → arr = [1, 3, 2]   swaps=1

i=1: correct=2, arr[1]=3 (mismatch)
     swap_idx = pos[2] = 2
     update map: pos[3]=2, pos[2]=1  → pos = {3:2, 1:0, 2:1}
     swap arr[1],arr[2] → arr = [1, 2, 3]   swaps=2

i=2: correct=3, arr[2]=3 (match) → skip

Result: 2 swaps
```

#### 為什麼要用雜湊表（而不是線性掃描）？

沒有 map 的話，要找 `swap_idx`（`correct_val` 待的位置）就得 O(n) 掃一遍，
`min_swaps` 會變成 O(n²)。`{value: index}` 這個 map 把查找變成 O(1)，
於是每一層的成本是 O(n log n)（排序），而不是 O(n²)。

| 做法 | 找交換目標 | min_swaps 總成本 |
|----------|------------------|-----------------|
| 每步線性掃描 | O(n) | O(n²) |
| **`{value: index}` 雜湊表** | **O(1)** | **O(n log n)** |

#### 同樣是「最少交換次數排序」的相關題目

| 題目 | LC# | 備註 |
|---------|-----|-------|
| Min Operations to Sort Tree by Level | 2471 | BFS 分層 + 每層求最少交換 |
| Minimum Swaps to Group All 1's Together | 1151 / 2134 | 滑動視窗變形 |
| Couples Holding Hands | 765 | 循環／併查集求最少交換 |
| First Missing Positive | 41 | 把元素換到對應索引的想法 |

---

### 2-17) Maximum Swap（LC 670）

**核心模式：`{digit: last index}` 雜湊表 + 由左往右的貪婪掃描**

> LC 670 - Maximum Swap
> https://leetcode.com/problems/maximum-swap/
> 給一個整數，**最多交換一次**兩個位數，讓數值最大。

#### 核心想法

只能換一次的話，就要讓**最大的位數盡量往左搬**。由左往右掃，
在**第一個**可以被後面更大位數壓過去的位置，把它跟那個更大位數的
**最後（最右邊）一次出現**交換 — 然後就停。

雜湊表是關鍵：預先算好 `0-9` 這十個位數的 `{digit: last index}`，
於是「我右邊有沒有更大的位數？它最右邊那份在哪？」就從 O(n) 掃描變成 O(1) 查表。

```text
Why LAST occurrence of the larger digit?
  - Moving a big digit further LEFT raises the most significant place → biggest gain.
  - Among equal large digits, taking the RIGHTMOST one leaves larger digits to the
    left untouched, keeping the tail as large as possible.

Why the FIRST improvable position (and stop)?
  - The leftmost place we can increase dominates all lower places → one swap there
    beats any swap further right. Only one swap is allowed, so return immediately.
```

因為位數只有 10 種，map 最多 10 個鍵 → 實際上是 O(1) 空間。

#### 圖解追蹤 — `num = 2736`

```text
digits = [2, 7, 3, 6]

Step 1 — build {digit: last index}:
  {2:0, 7:1, 3:2, 6:3}

Step 2 — scan left→right, for each digit look for a larger digit later:
  i=0, cur=2: check d=9..3 → d=7 exists at last[7]=1 > 0  ✓
              swap digits[0] and digits[1] → [7, 2, 3, 6]
              return 7236   (stop — only one swap allowed)

Result: 7236
```

`num = 9973` → 每個位數右邊都沒有更大的位數 → 不交換 → `9973`。

#### 模式（Python）

```python
# python
# LC 670 - Maximum Swap
# IDEA: {digit: last index} hashmap + greedy left scan
# time = O(n)  (n = number of digits), space = O(1)  (<= 10 keys)
class Solution(object):
    def maximumSwap(self, num):
        digits = list(str(num))

        # last occurrence index of each digit
        last = {int(d): i for i, d in enumerate(digits)}

        for i in range(len(digits)):
            cur = int(digits[i])
            # try the biggest digit (9..cur+1) that appears LATER
            for d in range(9, cur, -1):
                if last.get(d, -1) > i:
                    j = last[d]
                    digits[i], digits[j] = digits[j], digits[i]
                    return int("".join(digits))   # only ONE swap → stop

        return num   # already maximal
```

#### 模式（Java）

```java
// java
// LC 670 - Maximum Swap
// time = O(n), space = O(1)  (<= 10 keys)
public int maximumSwap(int num) {
    char[] digits = String.valueOf(num).toCharArray();

    // last occurrence index of each digit 0-9
    int[] last = new int[10];
    for (int i = 0; i < digits.length; i++) {
        last[digits[i] - '0'] = i;
    }

    for (int i = 0; i < digits.length; i++) {
        int cur = digits[i] - '0';
        // try the biggest digit (9..cur+1) that appears LATER
        for (int d = 9; d > cur; d--) {
            if (last[d] > i) {
                // swap and return (only one swap allowed)
                char tmp = digits[i];
                digits[i] = digits[last[d]];
                digits[last[d]] = tmp;
                return Integer.parseInt(new String(digits));
            }
        }
    }
    return num; // already maximal
}
```

#### 另一種做法 — 三個指標（不用雜湊表）

**由右往左**掃描，同時追蹤 `max_idx`（目前看過最大位數的最右索引），
並記住最好的那組 `(left, right)` 交換。一樣是 O(n) 時間、O(1) 空間，
但雜湊表版本讀起來更直接。

```python
# python — 3-pointer variant
def maximumSwap(num):
    digits = list(str(num))
    left = right = 0
    max_idx = len(digits) - 1
    for i in range(len(digits) - 1, -1, -1):
        if digits[i] > digits[max_idx]:
            max_idx = i                 # new largest digit to the right
        elif digits[i] < digits[max_idx]:
            left, right = i, max_idx    # candidate swap (keep the leftmost such i)
    digits[left], digits[right] = digits[right], digits[left]
    return int("".join(digits))
```

#### 各做法比較

| 做法 | 時間 | 空間 | 備註 |
|----------|------|-------|------|
| 暴力（試遍每一組配對） | O(n²) | O(n) | 簡單，留下最大的候選 |
| `{digit: last index}` 雜湊表 | O(n) | O(1) | 貪婪：第一個可改善的位置 → 最後出現的更大位數 |
| 三個指標（`left/right/max_idx`） | O(n) | O(1) | 由右往左，不用 map |

#### 相似題目

| 題目 | LC# | 關聯 |
|---------|-----|----------|
| Maximum Swap | 670 | `{digit: last index}` + 由左貪婪掃描 |
| Next Greater Element III | 556 | 重排位數，找下一個更大的數 |
| Next Permutation | 31 | 找樞紐 + 後繼 + 反轉後綴（相鄰的想法） |
| Remove K Digits | 402 | 在位數上做貪婪的單調堆疊 |
| Largest Number | 179 | 對數字字串自訂排序 |
| Create Maximum Number | 321 | 跨陣列的貪婪選位數 |

---

### 2-18) Longest Repeating Character Replacement（LC 424）

**核心模式：滑動視窗 + HashMap（頻率計數）+ 追蹤 `max_freq`**

#### 關鍵概念
給字串 `s` 和整數 `k`，你可以替換**最多 `k` 個**字元。回傳能做出的、由同一個字母重複組成的最長子字串長度。

**關鍵洞見**：對任何一個視窗來說，必須替換掉的字元數是：
```text
replacements_needed = window_size - (count of the most frequent char)
                    = (r - l + 1) - max_freq
```
當 `replacements_needed <= k` 時這個視窗就**合法**。取最大的合法視窗。

#### 模式拆解

1. **右邊界 `r` 往外擴**，更新 `cnt_map[s[r]] += 1`。
2. **追蹤 `max_freq`** = 視窗內單一字元的最高計數。
3. **左邊界 `l` 往內縮**，只要 `(r - l + 1) - max_freq > k`（要替換的太多了）。
4. **記錄** `max_len = max(max_len, r - l + 1)`。

> **順序很重要**：**先**更新雜湊表，**再**用 `while` 迴圈檢查合法性。
> 這跟前綴和類的雜湊表題（LC 523、525）相反 — 那些是更新*之前*先檢查。

#### 合法性檢查的兩種寫法

| 寫法 | 檢查式 | 成本 | 備註 |
|---------|-------|------|------|
| **追蹤 `max_freq`** | `(r-l+1) - max_freq > k` | 每步 O(1) | 建議用這個 — 不用掃 map 的值 |
| `max(cnt_map.values())` | `(r-l+1) - max(cnt_map.values()) > k` | 每步 O(26) | 比較好想；因為值最多 26 個，整體還是 O(n) |

> **為什麼縮視窗時不需要把 `max_freq` *調小***：`max_freq` 只用來反映目前找到過的最佳視窗。就算它「過期了」（比當下真正的最大值還大），答案仍然正確 — `max_len` 只有在真的出現更長的合法視窗時才會變大，而那需要一個新的、更高的 `max_freq`。

#### 實作模板

```python
# Python — Sliding Window + max_freq  (from leetcode_python/Hash_table/longest-repeating-character-replacement.py)
# time = O(n), space = O(1)  (only 26 uppercase letters)
class Solution:
    def characterReplacement(self, s, k):
        cnt_map = {}       # {char: count in current window}
        l = 0
        max_freq = 0       # highest single-char freq seen in the window
        max_len = 0

        for r in range(len(s)):
            # 1. update hash map FIRST
            cnt_map[s[r]] = cnt_map.get(s[r], 0) + 1

            # 2. track max frequency
            max_freq = max(max_freq, cnt_map[s[r]])

            # 3. shrink while replacements needed exceed k
            #    (no need to update max_freq here — removing s[l] can't raise it)
            while (r - l + 1) - max_freq > k:
                cnt_map[s[l]] -= 1
                l += 1

            # 4. record best valid window
            max_len = max(max_len, r - l + 1)

        return max_len
```

```java
// Java — Sliding Window + maxFreq
// time = O(n), space = O(1)  (26 letters)
public int characterReplacement(String s, int k) {
    int[] cnt = new int[26];
    int l = 0, maxFreq = 0, maxLen = 0;

    for (int r = 0; r < s.length(); r++) {
        cnt[s.charAt(r) - 'A']++;
        maxFreq = Math.max(maxFreq, cnt[s.charAt(r) - 'A']);

        // shrink window when too many replacements needed
        while ((r - l + 1) - maxFreq > k) {
            cnt[s.charAt(l) - 'A']--;
            l++;
        }
        maxLen = Math.max(maxLen, r - l + 1);
    }
    return maxLen;
}
```

#### 複雜度
```text
Time  = O(n)   -> r moves n times; l only moves forward (at most n times total)
Space = O(1)   -> hash map holds at most 26 uppercase letters
```

#### 為什麼是 O(n) — 雙指標的論證
```text
r advances 0 -> n-1 exactly once.
l NEVER moves backward; across the whole run it advances at most n times.
Total work = O(n + n) = O(n).
```

#### 和其他滑動視窗雜湊表題的對照

| 題目 | LC# | 視窗合法的條件 | Map 的角色 |
|---------|-----|-------------------|----------|
| Longest Repeating Char Replacement | 424 | `size - max_freq <= k` | 視窗內字元的頻率 |
| Longest Substring w/o Repeating | 3 | 沒有重複字元 | `{char: last index}` |
| Max Consecutive Ones III | 1004 | 視窗內 0 的個數 `<= k` | 0 的計數（同樣想法，二元版） |
| Min Window Substring | 76 | 視窗涵蓋目標 | 需要多少 vs 已有多少 |

#### 同模式的相關題目
- **LC 424**：Longest Repeating Character Replacement（就是這個模式）
- **LC 1004**：Max Consecutive Ones III（二元的特例：`size - ones <= k`）
- **LC 1493**：Longest Subarray of 1's After Deleting One Element
- **LC 340**：Longest Substring with At Most K Distinct Characters

---

### 2-19) Partition Labels — LC 763

**想法**：一個 `{char: last index}` 的 map，把「這個字母最後出現在哪？」變成 O(1) 查表；接著由左往右貪婪掃描，把目前這段一路延伸到看過的最遠 last index，等掃描索引走到它的那一刻就切一刀。

```python
# LC 763 Partition Labels
# IDEA : GREEDY
class Solution(object):
    def partitionLabels(self, S):
        # note : this trick for get max index for each element in S
        lindex = { c: i for i, c in enumerate(S) }
        j = anchor = 0
        ans = []
        for i, c in enumerate(S):
            ### NOTE : trick here
            #          -> via below line of code, we can get the max idx of current substring which "has element only exist in itself"
            #          -> e.g. the index we need to do partition 
            j = max(j, lindex[c])
            print ("i = " + str(i) + "," + " c = " + str(c) + "," +   " j = " + str(j) + "," +  " ans = " + str(ans))
            if i == j:
                ans.append(j - anchor + 1)
                anchor = j + 1
        return ans
```

---

## 依模式分類的題目

### 分類 1：計數與頻率（25 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| Valid Anagram | 242 | Easy | 計數 | 比較字元頻率 |
| Group Anagrams | 49 | Medium | 計數 | 把排序後的字串當鍵 |
| Sort Characters by Frequency | 451 | Medium | 計數 | 依頻率排序 |
| Top K Frequent Elements | 347 | Medium | 計數 + 堆積 | 計數 + 優先佇列 |
| Top K Frequent Words | 692 | Medium | 計數 + 堆積 | 計數 + 自訂比較器 |
| Most Common Word | 819 | Easy | 計數 | 清理輸入，再數單字 |
| Subdomain Visit Count | 811 | Easy | 計數 | 拆網域，累加造訪次數 |
| Find All Anagrams in String | 438 | Medium | 滑動視窗 | 視窗頻率比對 |
| Word Pattern | 290 | Easy | 計數 | pattern 與單字之間的雙射 |
| Isomorphic Strings | 205 | Easy | 計數 | 字元對應 |
| First Unique Character | 387 | Easy | 計數 | 找第一個 freq=1 的 |
| Unique Number of Occurrences | 1207 | Easy | 計數 | 頻率的頻率 |
| Find Anagram Mappings | 760 | Easy | 計數 | 索引對應 |
| Vowels of All Substrings | 2063 | Medium | 計數 | 每個母音的貢獻度 |
| Maximum Number of Balloons | 1189 | Easy | 計數 | 數出卡住的那個字元 |
| Number of Good Pairs | 1512 | Easy | 計數 | n*(n-1)/2 組配對 |
| Decode the Message | 2325 | Easy | 計數 | 字元替換 |
| Sort Array by Frequency | 1636 | Easy | 計數 | 先依頻率、再依值排序 |
| Check if Two Strings are Equivalent | 1662 | Easy | 計數 | 組出字串再比較 |
| Baseball Game | 682 | Easy | 計數 | 模擬遊戲規則 |
| Number of Arithmetic Triplets | 2367 | Easy | 計數 | 檢查差值 |
| Count Elements | 1426 | Easy | 計數 | 數出 x+1 存在的那些 x |
| Distribute Candies | 575 | Easy | 計數 | 取種類數與 n/2 的較小值 |
| Intersection of Two Arrays | 349 | Easy | 計數 | 集合交集 |
| Intersection of Two Arrays II | 350 | Easy | 計數 | 頻率交集 |

### 分類 2：Two Sum 變形（15 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| Two Sum | 1 | Easy | Two Sum | 存互補值的索引 |
| Two Sum II | 167 | Easy | 雙指標 | 利用陣列已排序 |
| 3Sum | 15 | Medium | Two Sum | 固定一個，再找配對 |
| 3Sum Closest | 16 | Medium | Two Sum | 追蹤最接近的和 |
| 4Sum | 18 | Medium | Two Sum | 固定兩個，再找配對 |
| Two Sum IV - BST | 653 | Easy | Two Sum | 中序走訪 + 雜湊集合 |
| K-diff Pairs in Array | 532 | Medium | Two Sum | 處理 k=0 的情況 |
| Pairs of Songs with Total Duration Divisible by 60 | 1010 | Medium | Two Sum | 模運算 |
| Count Number of Pairs with Absolute Difference K | 2006 | Easy | Two Sum | 檢查 num+k、num-k |
| Find All K-Distant Indices | 2200 | Easy | Two Sum | 距離限制 |
| Max Number of K-Sum Pairs | 1679 | Medium | Two Sum | 貪婪地把配對移掉 |
| Two Sum Less Than K | 1099 | Easy | Two Sum | 追蹤最大的合法和 |
| Two Sum - Data Structure | 170 | Easy | 設計題 | Add/Find 操作 |
| Count Good Meals | 1711 | Medium | Two Sum | 以 2 的次方當目標值 |
| Count Pairs With XOR in Range | 1803 | Hard | 字典樹（Trie） + Two Sum | XOR 的性質 |

### 分類 3：前綴和與子陣列（17 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| **Subarray Sum Equals K** | **560** | **Medium** | **前綴和** | **{sum: count} 模式，更新前先檢查** |
| Maximum Size Subarray Sum Equals k | 325 | Medium | 前綴和 | 存第一次出現的索引 |
| Continuous Subarray Sum | 523 | Medium | 前綴和 | 模運算，存索引 |
| **Contiguous Array** | **525** | **Medium** | **前綴和 + 轉換** | **把 0→-1、1→+1；存 {count: first_index}** |
| Binary Subarrays with Sum | 930 | Medium | 前綴和 | 跟 LC 560 一樣的計數模式 |
| **Subarray Sums Divisible by K** | **974** | **Medium** | **前綴和 + 取模** | **{remainder: count}；一定要處理負餘數！** |
| Count Number of Nice Subarrays | 1248 | Medium | 前綴和 | 奇數轉 1、偶數轉 0 |
| Subarray Sum Equals K II | 1074 | Hard | 前綴和 | 二維矩陣版本 |
| Minimum Size Subarray Sum | 209 | Medium | 滑動視窗 | 和 ≥ target 時收縮 |
| Number of Subarrays with Bounded Maximum | 795 | Medium | 前綴和 | 排容原理 |
| Shortest Subarray with Sum at Least K | 862 | Hard | 雙端佇列 | 單調雙端佇列最佳化 |
| Count of Range Sum | 327 | Hard | 合併排序 | 逆序對計數的變形 |
| Range Sum Query - Immutable | 303 | Easy | 前綴和 | 預先算好前綴和 |
| Range Sum Query 2D | 304 | Medium | 前綴和 | 二維前綴和陣列 |
| Subarray Product Less Than K | 713 | Medium | 滑動視窗 | 乘積 ≥ k 時收縮 |
| Maximum Average Subarray I | 643 | Easy | 滑動視窗 | 固定視窗大小 |
| Find Pivot Index | 724 | Easy | 前綴和 | 左邊的和 = 右邊的和 |

### 分類 4：滑動視窗配雜湊表（12 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| Longest Substring Without Repeating Characters | 3 | Medium | 滑動視窗 | 追蹤最後出現的位置 |
| Minimum Window Substring | 76 | Hard | 滑動視窗 | 合法時就收縮 |
| Permutation in String | 567 | Medium | 滑動視窗 | 固定視窗大小 |
| Find All Anagrams in String | 438 | Medium | 滑動視窗 | 比對頻率表 |
| Longest Substring with At Most Two Distinct Characters | 159 | Medium | 滑動視窗 | 追蹤字元計數 |
| Longest Substring with At Most K Distinct Characters | 340 | Medium | 滑動視窗 | 把相異字元上限一般化 |
| Fruit Into Baskets | 904 | Medium | 滑動視窗 | 最多 2 種 |
| Longest Repeating Character Replacement | 424 | Medium | 滑動視窗 | 追蹤最大頻率 — [詳細模式](#2-18-longest-repeating-character-replacement-lc-424) |
| Get Equal Substrings Within Budget | 1208 | Medium | 滑動視窗 | 成本限制 |
| Max Consecutive Ones III | 1004 | Medium | 滑動視窗 | 最多翻轉 K 個 0 |
| Substring with Concatenation of All Words | 30 | Hard | 滑動視窗 | 多個單字同時比對 |
| Replace the Substring for Balanced String | 1234 | Medium | 滑動視窗 | 讓所有頻率 ≤ n/4 |

### 分類 5：設計與快取（10 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| LRU Cache | 146 | Medium | OrderedDict | 雜湊表 + 雙向鏈結串列 |
| LFU Cache | 460 | Hard | 雜湊表 + 堆積 | 同時追蹤頻率與最近使用 |
| Design HashMap | 706 | Easy | 陣列 + 鏈結法 | 處理碰撞 |
| Design HashSet | 705 | Easy | 陣列 + 鏈結法 | 跟 HashMap 差不多 |
| All O(1) Data Structure | 432 | Hard | 雜湊表 + 雙向鏈結串列 | 多層次的複雜結構 |
| Insert Delete GetRandom O(1) | 380 | Medium | 雜湊表 + 陣列 | 維護索引對應 |
| Insert Delete GetRandom O(1) - Duplicates | 381 | Hard | 雜湊表 + 陣列 | 處理重複元素 |
| Design Twitter | 355 | Medium | 雜湊表 + 堆積 | 動態消息與追蹤關係 |
| Time Based Key-Value Store | 981 | Medium | 雜湊表 + 二分搜尋 | 依時間戳存取 |
| Design A Leaderboard | 1244 | Medium | 雜湊表 + 排序 | 追蹤分數 |

### 分類 6：圖與樹配雜湊表（8 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| Clone Graph | 133 | Medium | 雜湊表 + DFS | 走訪過程中做節點對應 |
| Copy List with Random Pointer | 138 | Medium | 雜湊表 + DFS | 為 random 指標做節點對應 |
| Find Duplicate Subtrees | 652 | Medium | 雜湊表 + DFS | 把子樹序列化成鍵 |
| Sentence Similarity | 734 | Easy | 雜湊表 + 集合 | 雙向的相似性對應 |
| Accounts Merge | 721 | Medium | 雜湊表 + 併查集 | email 到帳號的對應 |
| Evaluate Division | 399 | Medium | 雜湊表 + DFS | 建出等式圖 |
| Most Stones Removed | 947 | Medium | 雜湊表 + 併查集 | 把同列／同行的石頭連起來 |
| Smallest Common Region | 1257 | Medium | 雜湊表 + 集合 | 父節點對應 + LCA |
### 其他高頻雜湊表題（沒有新模板）

| 題目 | LC# | 難度 | 一句話重點 |
|---------|-----|------|-------------------|
| Find Duplicate File in System | 609 | Medium | 用標準化的鍵分組（模板 1），鍵是**檔案內容**，值是路徑清單 |
| Degree of an Array | 697 | Easy | 一趟建出 `value -> (count, first_index, last_index)`；答案 = 計數最大的那些值裡跨度最短的 |
| First Unique Character in a String | 387 | Easy | 先計數一趟，再按原順序掃第二趟 — 保住「第一個」靠的就是第二趟 |
| Ransom Note | 383 | Easy | 計數相減；Python 直接寫 `Counter(ransom) <= Counter(mag)` |
| Bulls and Cows | 299 | Medium | 第一趟數 bulls；cows = 非 bull 的位數上 `sum(min(count_secret[d], count_guess[d]))` |
| Roman to Integer / Integer to Roman | 13 / 12 | Easy / Medium | 靜態查表 + 貪婪；那些相減的組合（`IV`、`IX`……）要直接放**進**表裡 |
| Jewels and Stones | 771 | Easy | 「用查表打敗巢狀迴圈」的經典暖身題 |

---

## 總結與速查

| 想找什麼 | 去哪裡 |
|---|---|
| 一題該用哪個模板 | [hash_map.md → Problem → Pattern Decision Table](./hash_map.md#problem--pattern-decision-table) |
| 要背起來的標準模板 | [hash_map.md → Templates & Algorithms](./hash_map.md#templates--algorithms) |
| 完整寫出來的解法 | 上面的 [LC 範例](#lc-examples) |
| 某個分類的所有題目 | 上面的 [依模式分類的題目](#problems-by-pattern) |
| 有序 map 的操作（floor/ceiling/範圍） | 上面的 [有序 Map — Java TreeMap / Python SortedDict](#ordered-map--java-treemap--python-sorteddict) |
| 面試建議與常見錯誤 | [hash_map.md → Summary & Quick Reference](./hash_map.md#summary--quick-reference) |

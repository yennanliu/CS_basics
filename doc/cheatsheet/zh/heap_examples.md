# 堆積(heap) — LC 題解範例

> **範圍** — 堆積 / 優先佇列(priority queue)家族的題解存放處：每題每種語言一份標準解，把推理過程、追蹤與陷阱完整寫出來。
> **另見** — *主文件*：[heap.md](./heap.md) — 這些範例所實作的標準模板與模式選擇指南。*從同一份檔案拆出去的鄰近文件*：[heap_advanced.md](./heap_advanced.md) — 延遲刪除(lazy deletion)、反悔貪婪(regret greedy)、掃描線，以及較少見的堆積模板；[heap_language_apis.md](./heap_language_apis.md) — 完整的 `heapq` / `PriorityQueue` API 參考。

## LeetCode 題目清單

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## 總覽

下面每一題每種語言都只解**一次**。同一個想法若有通用形式，模板會放在
[heap.md](./heap.md)（標準）或 [heap_advanced.md](./heap_advanced.md)
（罕見 / 困難），範例只做連結，不重複贅述。

### 關鍵性質
- **複雜度**：以註解寫在每份解法的最上方
- **核心想法**：每題每種語言一份標準解；只有在*複雜度不同*或有*獨特技巧*時才會出現
  第二種寫法，而且會明確說明理由
- **使用時機**：等你能默寫出對應的模板之後 — 這些是你有能力之後才會遇到的變化題

## LC 範例

### 1) Kth Largest Element in a Stream — LC 703
```python
# 703 Kth Largest Element in a Stream
# IDEA : HEAP
# NOTE !!! : we ONLY need to return k biggest element
#           -> we ONLY need to keep at most k element
#               -> if element more than k, then pop element out
#                   -> then return 0 element directly
import heapq
class KthLargest:

    def __init__(self, k, nums):
        self.k = k
        heapq.heapify(nums)
        self.heap = nums
        while len(self.heap) > k:
            heapq.heappop(self.heap)

    def add(self, val):
        if len(self.heap) < self.k:
            heapq.heappush(self.heap, val)
        else:
            heapq.heappushpop(self.heap, val)
            
        return self.heap[0]
```

### 2) Ugly Number II — LC 264
```python
# LC 264 Ugly Number II
# V0 : heap + `visited` set
# IDEA : HEAP
# using brute force is too slow -> time out error
# -> so here we generate "ugly number" by ourself, and order them via heap (heappush)
# -> and return the i-th element as request
import heapq
class Solution(object):
    def nthUglyNumber(self, n):
        # NOTE : we init heap as [1], visited = set([1])
        heap = [1]
        visited = set([1])      
        for i in range(n):
            # NOTE !!! trick here, we use last element via heappop
            val = heapq.heappop(heap)
            # and we genrate ugly by ourself
            for factor in [2,3,5]:
                if val*factor not in visited:
                    heapq.heappush(heap, val*factor)
                    visited.add(val*factor)    
        return val

# V1 : generate by divisibility branch instead of a `visited` set
#      -> trades the O(n) set for duplicate pushes; same O(n log n), smaller space constant
import heapq
class Solution(object):
    def nthUglyNumber(self, n):
        ugly_number = 0

        heap = []
        heapq.heappush(heap, 1)
        for _ in range(n):
            ugly_number = heapq.heappop(heap)
            if ugly_number % 2 == 0:
                heapq.heappush(heap, ugly_number * 2)
            elif ugly_number % 3 == 0:
                heapq.heappush(heap, ugly_number * 2)
                heapq.heappush(heap, ugly_number * 3)
            else:
                heapq.heappush(heap, ugly_number * 2)
                heapq.heappush(heap, ugly_number * 3)
                heapq.heappush(heap, ugly_number * 5)

        return ugly_number
```

### 3) Find Median from Data Stream — LC 295
```python
# 295 Find Median from Data Stream
# https://docs.python.org/zh-tw/3/library/heapq.html
# https://github.com/python/cpython/blob/3.10/Lib/heapq.py
# Note !!! 
#  -> that the heapq in python is a min heap, thus we need to invert the values in the smaller half to mimic a "max heap".
# IDEA : python heapq (heap queue AKA priority queue)
#  -> Step 1) init 2 heap : small, large
#              -> small : stack storage "smaller than half value" elements
#              -> large : stack storage "bigger than half value" elements
#  -> Step 2) check if len(self.small) == len(self.large)
#  -> Step 3-1) add num:  (if len(self.small) == len(self.large))
#              -> since heapq in python is "min" heap, so we need to add minus to smaller stack for "max" heap simulation
#              -> e.g. : 
#                        "-num" in -heappushpop(self.small, -num)
#                        "-heappushpop" is for balacing the "-" back (e.g. -(-value) == value)
#             and pop "biggest" elment in small stack to big stack
#  -> Step 3-2) add num:  (if len(self.small) != len(self.large))
#             -> pop smallest element from large heap to small heap
#             -> e.g. heappush(self.small, -heappushpop(self.large, num))
#  -> Step 4) return median
#             -> if even length (len(self.small) == len(self.large))
#                 -> return float(self.large[0] - self.small[0]) / 2.0
#             -> if odd length ((len(self.small) != len(self.large)))
#                 -> return float(self.large[0])
from heapq import *
class MedianFinder:
    def __init__(self):
        self.small = []  # the smaller half of the list, max heap (invert min-heap)
        self.large = []  # the larger half of the list, min heap

    def addNum(self, num):
        """

        doc : https://docs.python.org/3/library/heapq.html
        src code : https://github.com/python/cpython/blob/3.10/Lib/heapq.py
        
        * heappush(heap, item)
            -> Push the value item onto the heap, maintaining the heap invariant.

        * heappop(heap)
            -> Pop and return the smallest item from the heap, maintaining the heap invariant. If the heap is empty, IndexError is raised. To access the smallest item without popping it, use heap[0].

        * heappushpop(heap, item)
            -> Push item on the heap, then pop and return the smallest item from the heap. The combined action runs more efficiently than heappush() followed by a separate call to heappop().
        """
        if len(self.small) == len(self.large):
            heappush(self.large, -heappushpop(self.small, -num))
        else:
            heappush(self.small, -heappushpop(self.large, num))

    def findMedian(self):
        # even length
        if len(self.small) == len(self.large):
            return float(self.large[0] - self.small[0]) / 2.0
        # odd length
        else:
            return float(self.large[0])
```

### 4) Minimum Cost to Connect Sticks — LC 1167
```python
# LC 1167 Minimum Cost to Connect Sticks
# IDEA : heapq
class Solution(object):
    def connectSticks(self, sticks):
        from heapq import * 
        heapify(sticks)
        res = 0
        while len(sticks) > 1:
            s1 = heappop(sticks)
            s2 = heappop(sticks)
            res += s1 + s2 # merge 2 shortest sticks
            heappush(sticks, s1 + s2)
        return res 
```

### 5) The kth Factor of n — LC 1492
```python
# LC 1492  The kth Factor of n
# note : there is also brute force, math approaches
# IDEA : HEAP
# Initialize max heap. Use PriorityQueue in Java and heap in Python. heap is a min-heap. Hence, to implement max heap, change the sign of divisor before pushing it into the heap.
# https://leetcode.com/problems/the-kth-factor-of-n/solution/
class Solution:
    def kthFactor(self, n, k):
        # push into heap
        # by limiting size of heap to k
        def heappush_k(num):
            heappush(heap, - num)
            if len(heap) > k:
                heappop(heap)
            
        # Python heap is min heap 
        # -> to keep max element always on top,
        # one has to push negative values
        heap = []
        for x in range(1, int(n**0.5) + 1):
            if n % x == 0:
                heappush_k(x)
                if x != n // x:
                    heappush_k(n // x)
                
        return -heappop(heap) if k == len(heap) else -1
```

### 6) Least Number of Unique Integers after K Removals — LC 1481
```python
# LC 1481. Least Number of Unique Integers after K Removals
# NOTE : there's also Counter approaches
# V0 : Counter + sort  (no heap)
from collections import Counter
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        # edge case
        if not arr:
            return 0
        cnt = dict(Counter(arr))
        cnt_sorted = sorted(cnt.items(), key = lambda x : x[1])
        #print ("cnt_sorted = " + str(cnt_sorted))
        removed = 0
        for key, freq in cnt_sorted:
            """
            NOTE !!!
                -> we need to remove exactly k elements and make remain unique integers as less as possible
                -> since we ALREADY sort num_counter,
                -> so the elements NOW are ordering with their count
                    -> so we need to remove ALL element while k still > 0
                    -> so k -= freq, since for element key, there are freq count for it in arr
            """
            if freq <= k:
                k -= freq
                removed += 1

        return len(cnt.keys()) - removed

# V1 : Counter + heapq (priority queue)
# https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/discuss/704179/python-solution%3A-Counter-and-Priority-Queue
# IDEA
# -> Count the occurence of each number.
# -> We want to delete the number with lowest occurence thus we can use minimum steps to reduce the total unique numbers in the list. For example,[4,3,1,1,3,3,2]. The Counter of this array will be: {3:3, 1:2, 4:1, 2:1}. Given k = 3, the greedy approach is to delete 2 and 4 first because both of them are appearing once. We need an ordering data structure to give us the lowest occurence of number each time. As you may know, Priority Queue comes to play
# -> Use heap to build PQ for the counter. We store each member as a tuple: (count, number) Python heap module will sort it based on the first member of the tuple.
# -> loop through k times to pop member out of heap and check if we need to push it back
class Solution(object):
    def findLeastNumOfUniqueInts(self, arr, k):
            # use counter, and heap (priority queue)
            from collections import Counter
            import heapq
            h = []
            for key, val in Counter(arr).items():
                heapq.heappush(h,(val,key))

            while k > 0:
                item = heapq.heappop(h)    
                if item[0] != 1:
                    heapq.heappush(h, (item[0]-1, item[1]))      
                k -=1

            return len(h)
```

### 7) Maximum Number of Events That Can Be Attended — LC 1353

```python
# python
# LC 1353. Maximum Number of Events That Can Be Attended
# Reference: leetcode_python/Heap/maximum-number-of-events-that-can-be-attended.py

"""
Problem: events[i] = [start_i, end_i]. You may attend event i on ANY single day d
         with start_i <= d <= end_i, and only ONE event per day.
         Return the max number of events you can attend.

Example:
  events = [[1,2],[2,3],[3,4]]      -> 3   (day1: e0, day2: e1, day3: e2)
  events = [[1,2],[2,3],[3,4],[1,2]] -> 4  (day1: e0, day2: e3, day3: e1, day4: e2)
"""

# ── Core Idea ────────────────────────────────────────────────────────────────
# GREEDY + MIN HEAP ON END DAY (earliest deadline first).
#
# Walk forward in time. On the current `day`, among all events already OPEN
# (start <= day) and NOT yet expired (end >= day), attend the one that ENDS
# SOONEST. It is the most "urgent" / least flexible, and events ending later
# still have spare days to be attended -> exchange argument, greedy is optimal.
#
#   pq : MIN heap of `end days` of currently-available events
#        e.g. [end_d_1, end_d_2, ...]  -> pq[0] = the most urgent deadline
#
# Each step:
#   1. PUSH   : add every event with start <= day into pq (sorted input makes
#               this a single forward pointer `i` -> each event pushed once)
#   2. PURGE  : lazy-delete expired events -> while pq and pq[0] < day: pop
#   3. ATTEND : if pq non-empty -> pop (attend earliest deadline), ans += 1, day += 1
#
# NOTE !!!  the order PUSH -> PURGE -> ATTEND matters:
#   - purging before pushing can leave stale ends on top
#   - attending before purging can "attend" an already-expired event
#
# Two ways to advance time:
#   (a) day-jumping  : if pq is empty, fast-forward day = events[i][0]
#                      -> time = O(n log n),   no dependency on day range
#   (b) scan all days: for day in range(1, MAX_DAY+1)
#                      -> time = O(D + n log n), D = day range (1e5)
# ─────────────────────────────────────────────────────────────────────────────

# V0 : GREEDY + MIN HEAP, day-jumping   (preferred: independent of day range)
# time  = O(n log n)   (sort + each event pushed/popped once)
# space = O(n)
import heapq

class Solution(object):
    def maxEvents(self, events):
        # 1) sort by start day, so events become available in scan order
        events.sort()

        pq = []          # NOTE !!! min-heap of END days
        i = 0            # forward pointer into events
        day = 0
        ans = 0
        n = len(events)

        while i < n or pq:
            # nothing available -> jump time to the next event's start day
            # (this is what removes the O(D) day-range cost)
            if not pq:
                day = events[i][0]

            # PUSH: all events opened by `day`
            #  -> `<=` is the safe form; `== day` also works here only because
            #     `day` either jumps to the next start or advances by exactly 1
            while i < n and events[i][0] <= day:
                heapq.heappush(pq, events[i][1])
                i += 1

            # PURGE: lazy-delete events whose deadline already passed
            while pq and pq[0] < day:
                heapq.heappop(pq)

            # ATTEND: take the earliest deadline, consume this day
            if pq:
                heapq.heappop(pq)
                ans += 1
                day += 1

        return ans


# V0-1 : GREEDY + MIN HEAP, scan every day  (simpler to write, slower)
# time  = O(D + n log n), D = day range (1e5)
# space = O(n)
class Solution(object):
    def maxEvents(self, events):
        events.sort(key=lambda x: -x[0])   # DESC, so events.pop() gives smallest start
        end_days = []
        ans = 0
        for day in range(1, 100001):
            # PUSH
            while events and events[-1][0] <= day:
                heapq.heappush(end_days, events.pop()[1])
            # PURGE expired
            while end_days and end_days[0] < day:
                heapq.heappop(end_days)
            # ATTEND earliest deadline
            if end_days:
                heapq.heappop(end_days)
                ans += 1
        return ans
```

**為什麼是對 `end day` 做貪婪（而不是 `start day`、也不是持續天數）？**

如果今天有兩個活動都可以參加，選**結束日較早**的那一個永遠不會比較差：結束較晚的那個至少還剩下一樣多的天數可以安排。依開始日排序只控制*活動什麼時候進入堆積*；堆積依結束日排序，則控制*今天這一天要花在哪個活動上*。

```text
events = [[1,4],[1,1]]      day 1: pq = [1, 4]
                            greedy pops 1  -> day 2: pq = [4] -> attend  => 2 ✅
                            wrong (pop 4)  -> day 2: pq = [1] expired    => 1 ❌
```

**模式：時間掃描 + 截止日最小堆積（最早截止優先，earliest-deadline-first）**

| 步驟 | 資料結構 | 用途 |
|------|---------------|---------|
| 依開始日排序 | 陣列 + 指標 `i` | 活動照時間順序變成可參加；每個只被推入一次 |
| 追蹤可參加的活動 | `pq` = **結束日**的最小堆積 | `pq[0]` = 目前仍有效、最急迫的截止日 |
| 丟掉過期的 | `while pq[0] < day: pop` | **延遲刪除** — 堆積無法移除任意位置的元素 |
| 用掉一個時段 | pop `pq` + `day += 1` | 每天一個活動，貪婪地挑最急迫的 |
| 跳過空檔 | `if not pq: day = events[i][0]` | 消掉 O(天數範圍) 這個因子 |

**辨識這個模式的特徵：***「每一單位時間只能服務一個項目」* + *「每個項目有一段有效期 / 截止日」* → 依區間**開始**排序，堆積依區間**結束**排序。

**相似題目：**

| LC # | 題目 | 共通模式 | 關鍵差異 |
|------|---------|---------------|----------------|
| 1751 | Max Number of Events That Can Be Attended II | 輸入的活動資料相同 | 活動佔用**整段**區間並帶有價值 → DP + 二分搜尋，**不是**堆積 |
| 621 | Task Scheduler | 時間掃描 + 堆積，每個時刻一個時段 | 依頻率的最大堆積 + 冷卻佇列（見 [§ 17](#17-task-scheduler--lc-621)） |
| 253 | Meeting Rooms II | 依開始排序，結束時間的最小堆積 | 數的是*同時進行*的區間數，不是挑出一個子集合 |
| 2406 | Divide Intervals Into Min Number of Groups | 依開始排序，結束時間的最小堆積 | 與 253 相同，只是換成區間分組的說法（見 [§ 15](#15-divide-intervals-into-minimum-number-of-groups--lc-2406)） |
| 630 | Course Schedule III | 依截止日貪婪 + 堆積 | 最大堆積做**替換**：超時就丟掉耗時最長的課 |
| 502 | IPO | 依一個鍵排序，依另一個鍵建堆積 | 雙堆積貪婪（資本 → 利潤的最大堆積） |
| 871 | Min Number of Refueling Stops | 把可達的選項推進去，卡住時再貪婪地取最好的 | 油量的最大堆積，只有卡住時才 pop（見 [§ 13](#13-minimum-number-of-refueling-stops--lc-871)） |
| 1834 | Single-Threaded CPU | 推進時間、推入已抵達的任務、取出最好的 | 以 (處理時間, 索引) 建最小堆積；時間直接跳到下一個抵達時刻 |
| 767 | Reorganize String | 每個位置一個時段，用堆積貪婪挑選 | 依剩餘數量的最大堆積 + 上次使用過的字元擋著 |

### 8) Maximum Frequency Stack — LC 895
```python
# LC 895. Maximum Frequency Stack
# IDEA : STACK
# https://leetcode.com/problems/maximum-frequency-stack/solution/
class FreqStack(object):

    def __init__(self):
        self.freq = collections.Counter()
        self.group = collections.defaultdict(list)
        self.maxfreq = 0

    def push(self, x):
        f = self.freq[x] + 1
        self.freq[x] = f
        if f > self.maxfreq:
            self.maxfreq = f
        self.group[f].append(x)

    def pop(self):
        x = self.group[self.maxfreq].pop()
        self.freq[x] -= 1
        if not self.group[self.maxfreq]:
            self.maxfreq -= 1

        return x
```

### 9) Find K Pairs with Smallest Sums — LC 373

```java
// java
// LC 373
// IDEA: PQ (min-heap over the virtual grid)
/**
 *  IDEA:
 *
 *  ✅ Use a min-heap (priority queue) to:
 *
 *  - Always retrieve the next smallest sum pair
 *
 *  - Efficiently keep track of candidates
 *
 */
public List<List<Integer>> kSmallestPairs_0_1(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> res = new ArrayList<>();

    if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0 || k <= 0) {
        return res;
    }

    // Min-heap to store [sum, index in nums1, index in nums2]
    /**
     *  NOTE !!!
     *
     *  min PQ structure:
     *
     *   [ sum, nums_1_idx, nums_2_idx ]
     *
     *
     *   - Heap stores: int[] {sum, index in nums1, index in nums2}
     *
     *   - It's sorted by sum = nums1[i] + nums2[j]
     *
     */
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

    // Add the first k pairs (nums1[0] + nums2[0...k])
    /**  NOTE !!!
     *
     *  we init PQ as below:
     *
     *  - We insert first k pairs: (nums1[i], nums2[0])
     *
     *   - Why nums2[0]?
     *     -> Because nums2 is sorted,
     *       so (nums1[i], nums2[0]) is the smallest possible for that row.
     *
     *
     *   -> so, we insert `nums_1[i] + nums_2[0]`  to PQ for now
     *
     *
     */
    for (int i = 0; i < nums1.length && i < k; i++) {
        minHeap.offer(new int[] { nums1[i] + nums2[0], i, 0 });
    }

    /** NOTE !!!   Pop from Heap and Expand
     *
     * - Poll the `smallest` sum pair (i, j) and add it to result.
     *
     * - You now consider the next element in that row, which is (i, j + 1).
     *
     */
    while (k > 0 && !minHeap.isEmpty()) {

        // current smallest val from PQ
        int[] current = minHeap.poll();
        int i = current[1]; // index in nums1
        int j = current[2]; // index in nums2

        res.add(Arrays.asList(nums1[i], nums2[j]));

        /**
         *  NOTE !!! Push the Next Pair in the Same Row
         *
         *  - This ensures you're exploring pairs in increasing sum order:
         *
         *      - From (i, 0) → (i, 1) → (i, 2) ...
         *
         * - Since the arrays are sorted, this gives increasing sums
         *
         *
         */
        if (j + 1 < nums2.length) {
            minHeap.offer(new int[] { nums1[i] + nums2[j + 1], i, j + 1 });
        }

        k--;
    }

    return res;
}
```

### 10) Kth Smallest Element in a Sorted Matrix — LC 378

```java
// java
// LC 378
// Reference: leetcode_java/src/main/java/LeetCodeJava/Heap/KthSmallestElementInASortedMatrix.java

// IDEA: MAX PQ (Priority Queue)
/**
 *  KEY INSIGHT !!!
 *
 *  `kth smallest element` ~= biggest element from a Max PQ
 *
 *  - Use MAX heap of size k to find kth smallest element
 *  - Keep only the k smallest elements in the heap
 *  - The root (peek) of max heap = kth smallest element overall
 *
 *  Why?
 *  - We maintain a max heap of size k
 *  - This heap contains the k smallest elements seen so far
 *  - The largest among these k elements is at the root
 *  - This root element is exactly the kth smallest element
 */
public int kthSmallest_0_1(int[][] matrix, int k) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
        return 0;
    }

    int n = matrix.length;
    int m = matrix[0].length;

    /** NOTE !!!
     *
     *  Use MAX PQ (max heap)
     *
     *  Since the problem asks for `kth smallest element`
     *  = biggest element from a Max PQ of size k
     */
    // Max-heap: largest value at top
    PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Integer.compare(b, a));

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            pq.offer(matrix[i][j]);
            /** NOTE !!!
             *
             *  Use `pq.size()` to check if we've reached k elements
             *
             *  NO NEED for separate counter variables like size or cnt
             */
            if (pq.size() > k) {
                pq.poll(); // remove largest, keep only k smallest
            }
        }
    }

    // Top of max-heap = kth smallest element
    return pq.peek();
}
```

### 11) Minimum Deletions to Make Character Frequencies Unique — LC 1647

```java
// java
// LC 1647
// Reference: leetcode_java/src/main/java/LeetCodeJava/Heap/MinimumDeletionsToMakeCharacterFrequenciesUnique.java

/**
 * Problem: Return minimum number of character deletions to make all frequencies unique
 *
 * Example 1:
 * Input: s = "aab"
 * Output: 0 (already unique: 'a':2, 'b':1)
 *
 * Example 2:
 * Input: s = "aaabbbcc"
 * Output: 2 (can delete 2 'b's to get 'a':3, 'b':1, 'c':2)
 */

// APPROACH 1: GREEDY + MAX HEAP
// IDEA: Process frequencies from high to low, decrement duplicates
public int minDeletions_heap(String s) {
    // Step 1: Count character frequencies
    int[] freq = new int[26];
    for (char c : s.toCharArray()) {
        freq[c - 'a']++;
    }

    // Step 2: Build max heap with all frequencies
    PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
    for (int f : freq) {
        if (f > 0) {
            pq.add(f);
        }
    }

    // Step 3: Process frequencies, decrement duplicates
    int deletions = 0;
    while (pq.size() > 1) {
        int top = pq.poll();
        int next = pq.peek();

        // If duplicate frequency found
        if (top == next) {
            top--;      // Decrement to make unique
            deletions++;
            if (top > 0) {
                pq.add(top);  // Re-add if still positive
            }
        }
    }

    return deletions;
}

// APPROACH 2: GREEDY + SORTING
// IDEA: Sort frequencies, ensure strictly decreasing sequence
public int minDeletions_sort(String s) {
    // Step 1: Count frequencies
    int[] freq = new int[26];
    for (char c : s.toCharArray()) {
        freq[c - 'a']++;
    }

    // Step 2: Sort frequencies in ascending order
    Arrays.sort(freq);

    int deletions = 0;

    // Step 3: Process from high to low (right to left)
    for (int i = 24; i >= 0; i--) {
        if (freq[i] == 0) {
            break;  // No more characters
        }

        // If current freq >= next freq, adjust it
        if (freq[i] >= freq[i + 1]) {
            int prev = freq[i];
            freq[i] = Math.max(0, freq[i + 1] - 1);  // Make it strictly less
            deletions += prev - freq[i];
        }
    }

    return deletions;
}

// APPROACH 3: GREEDY + HASHSET
// IDEA: Track used frequencies, decrement until unique
public int minDeletions_hashset(String s) {
    // Step 1: Count frequencies
    HashMap<Character, Integer> cnt = new HashMap<>();
    for (char c : s.toCharArray()) {
        cnt.put(c, cnt.getOrDefault(c, 0) + 1);
    }

    // Step 2: Track used frequencies and process
    HashSet<Integer> usedFreq = new HashSet<>();
    int deletions = 0;

    for (int freq : cnt.values()) {
        // Decrement until we find an unused frequency
        while (freq > 0 && usedFreq.contains(freq)) {
            freq--;
            deletions++;
        }
        usedFreq.add(freq);  // Mark this frequency as used
    }

    return deletions;
}

/**
 * KEY INSIGHTS:
 *
 * 1. Max Heap Approach:
 *    - Process frequencies from largest to smallest
 *    - When duplicate found, decrement and re-insert
 *    - Time: O(N + K log K) where K = unique chars
 *    - Space: O(K)
 *
 * 2. Sorting Approach:
 *    - Sort frequencies, then ensure strictly decreasing
 *    - Adjust each freq to be max(0, next_freq - 1)
 *    - Time: O(N + 26 log 26) = O(N)
 *    - Space: O(1) - only 26 letters
 *
 * 3. HashSet Approach:
 *    - Track all used frequencies
 *    - Decrement duplicates until finding unused frequency
 *    - Time: O(N + K * max_freq) worst case
 *    - Space: O(K)
 *
 * Best Choice: Sorting approach for best time complexity O(N)
 *
 * Pattern: Frequency Uniqueness with Greedy + Heap/Sort
 */
```

### 12) Maximum Performance of a Team — LC 1383

```java
// java
// LC 1383
// Reference: leetcode_java/src/main/java/LeetCodeJava/Heap/MaximumPerformanceOfAeam.java

/**
 * Problem: Choose at most k engineers to maximize team performance
 * Performance = (sum of speeds) * (minimum efficiency among chosen engineers)
 *
 * Example 1:
 * Input: n = 6, speed = [2,10,3,1,5,8], efficiency = [5,4,3,9,7,2], k = 2
 * Output: 60
 * Explanation: Select engineer 2 (speed=10, eff=4) and engineer 5 (speed=5, eff=7)
 *              Performance = (10 + 5) * min(4, 7) = 60
 *
 * Example 2:
 * Input: n = 6, speed = [2,10,3,1,5,8], efficiency = [5,4,3,9,7,2], k = 3
 * Output: 68
 * Explanation: Select engineers 1,2,5 → (2+10+5) * min(5,4,7) = 68
 *
 * Constraints:
 * - 1 <= k <= n <= 10^5
 * - speed.length == efficiency.length == n
 * - 1 <= speed[i] <= 10^5
 * - 1 <= efficiency[i] <= 10^8
 */

// APPROACH: GREEDY + SORTING + MIN HEAP
/**
 * KEY INSIGHT:
 *
 * 1. Sort engineers by efficiency in DESCENDING order
 *    - This way, when we process engineer i, all previously considered engineers
 *      have efficiency >= current engineer's efficiency
 *    - So current engineer's efficiency becomes the bottleneck (minimum)
 *
 * 2. Use MIN HEAP to track the k largest speeds
 *    - As we iterate, maintain at most k engineers
 *    - Always remove the engineer with lowest speed when exceeding k
 *    - This maximizes the speed sum while respecting the constraint
 *
 * 3. Calculate performance at each step
 *    - performance = (sum of speeds in heap) * (current engineer's efficiency)
 *    - Current efficiency is guaranteed to be the minimum (due to sorting)
 *
 * Time Complexity: O(N log N) for sorting + O(N log k) for heap operations = O(N log N)
 * Space Complexity: O(N) for storing engineers + O(k) for heap = O(N)
 */
public int maxPerformance(int n, int[] speed, int[] efficiency, int k) {
    final int MOD = 1_000_000_007;

    // Step 1: Pair engineers with [efficiency, speed]
    int[][] engineers = new int[n][2];
    for (int i = 0; i < n; i++) {
        engineers[i] = new int[] { efficiency[i], speed[i] };
    }

    // Step 2: Sort by efficiency in DESCENDING order
    // This ensures current engineer has minimum efficiency among all considered
    Arrays.sort(engineers, (a, b) -> Integer.compare(b[0], a[0]));

    // Step 3: Min heap to maintain k largest speeds
    // We use min heap so we can easily remove the smallest speed when size > k
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    long speedSum = 0;      // Sum of speeds in current team
    long maxPerf = 0;       // Maximum performance found so far

    // Step 4: Process each engineer in order of decreasing efficiency
    for (int[] eng : engineers) {
        int eff = eng[0];   // Current engineer's efficiency (minimum so far)
        int spd = eng[1];   // Current engineer's speed

        // Add current engineer to the team
        minHeap.offer(spd);
        speedSum += spd;

        // If team exceeds k engineers, remove the one with lowest speed
        if (minHeap.size() > k) {
            speedSum -= minHeap.poll();
        }

        // Calculate performance with current engineer as efficiency bottleneck
        // Since engineers are sorted by efficiency DESC, current eff is the minimum
        long performance = speedSum * eff;
        maxPerf = Math.max(maxPerf, performance);
    }

    // Return result modulo 10^9 + 7
    return (int) (maxPerf % MOD);
}

/**
 * STEP-BY-STEP EXAMPLE:
 *
 * Input: speed = [2,10,3,1,5,8], efficiency = [5,4,3,9,7,2], k = 2
 *
 * After sorting by efficiency DESC:
 * [(9,1), (7,5), (5,2), (4,10), (3,3), (2,8)]
 *
 * Iteration 1: eng = (9,1)
 *   - Add speed=1, speedSum=1, heap=[1]
 *   - performance = 1 * 9 = 9, maxPerf = 9
 *
 * Iteration 2: eng = (7,5)
 *   - Add speed=5, speedSum=6, heap=[1,5]
 *   - performance = 6 * 7 = 42, maxPerf = 42
 *
 * Iteration 3: eng = (5,2)
 *   - Add speed=2, speedSum=8, heap=[1,2,5]
 *   - Size > k, remove min=1, speedSum=7, heap=[2,5]
 *   - performance = 7 * 5 = 35, maxPerf = 42
 *
 * Iteration 4: eng = (4,10)
 *   - Add speed=10, speedSum=17, heap=[2,5,10]
 *   - Size > k, remove min=2, speedSum=15, heap=[5,10]
 *   - performance = 15 * 4 = 60, maxPerf = 60 ✓
 *
 * Continue for remaining engineers...
 * Final answer: 60
 */

/**
 * WHY THIS WORKS:
 *
 * 1. Greedy Choice: By sorting by efficiency descending, we ensure that
 *    when considering engineer i, all previous engineers have >= efficiency.
 *    So engineer i's efficiency is the bottleneck (minimum).
 *
 * 2. Optimal Substructure: To maximize performance with current efficiency,
 *    we want to maximize the speed sum. The min heap ensures we keep only
 *    the k engineers with highest speeds among those considered so far.
 *
 * 3. Why Min Heap for "k largest"?
 *    - We want to maintain k largest speeds (maximize sum)
 *    - Min heap lets us easily identify and remove the smallest speed
 *      when we need to make room for a potentially larger speed
 *    - The root of min heap = smallest speed in our selection
 *      → if new speed > root, we should replace it
 *
 * Pattern: Greedy + Sorting + Top K with Heap
 * Similar to: LC 857 (Minimum Cost to Hire K Workers)
 */
```

### 13) Minimum Number of Refueling Stops — LC 871

```java
// java
// LC 871
// Reference: leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/MinimumNumberOfRefuelingStops.java

/**
 * Problem: Find minimum refueling stops to reach target
 *
 * A car starts with startFuel and drives toward a target.
 * Gas stations along the way have [position, fuel].
 * Return minimum stops to reach target, or -1 if impossible.
 *
 * Example:
 * Input: target = 100, startFuel = 10, stations = [[10,60],[20,30],[30,30],[60,40]]
 * Output: 2
 *
 * Constraints:
 * - 1 <= target, startFuel <= 10^9
 * - 0 <= stations.length <= 500
 */

// APPROACH 1: GREEDY + MAX HEAP
/**
 * KEY INSIGHT:
 *
 * "Drive as far as possible. When stuck, pick the best gas station you've already passed."
 *
 * 1. Traverse stations in order
 * 2. Keep a MAX HEAP of fuels from stations you've passed
 * 3. When you CAN'T move forward, refuel using the largest fuel seen so far
 *
 * Why this works:
 * - You DELAY refueling until necessary
 * - Always pick the LARGEST fuel among reachable stations
 * - This is a classic greedy + max heap pattern
 *
 * Time: O(N log N) - each station enters/exits heap once
 * Space: O(N) - for the heap
 */
public int minRefuelStops_heap(int target, int startFuel, int[][] stations) {
    // Max heap (store fuels from passed stations)
    PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Integer.compare(b, a));

    int fuel = startFuel;
    int i = 0;
    int stops = 0;

    while (fuel < target) {
        // Add all reachable stations' fuel to heap
        while (i < stations.length && stations[i][0] <= fuel) {
            pq.add(stations[i][1]);
            i++;
        }

        // No fuel available -> cannot proceed
        if (pq.isEmpty())
            return -1;

        // Greedy: refuel with the largest fuel available
        fuel += pq.poll();
        stops++;
    }

    return stops;
}

// APPROACH 2: GREEDY + MAX HEAP (Iteration style)
/**
 * Alternative iteration: loop through stations, refuel when tank < 0
 *
 * Time: O(N log N)
 * Space: O(N)
 */
public int minRefuelStops_heap_v2(int target, int tank, int[][] stations) {
    PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
    int ans = 0, prev = 0;

    for (int[] station : stations) {
        int location = station[0];
        int capacity = station[1];
        tank -= location - prev;

        // Must refuel from past stations when tank < 0
        while (!pq.isEmpty() && tank < 0) {
            tank += pq.poll();
            ans++;
        }

        if (tank < 0) return -1;
        pq.offer(capacity);
        prev = location;
    }

    // Handle final stretch to target
    tank -= target - prev;
    while (!pq.isEmpty() && tank < 0) {
        tank += pq.poll();
        ans++;
    }

    return tank < 0 ? -1 : ans;
}

// APPROACH 3: DP
/**
 * dp[t] = max distance reachable with exactly t refueling stops
 *
 * For each station i, update dp[t+1] if dp[t] >= station position
 *
 * Time: O(N²)
 * Space: O(N)
 */
public int minRefuelStops_dp(int target, int startFuel, int[][] stations) {
    int N = stations.length;
    long[] dp = new long[N + 1];
    dp[0] = startFuel;

    for (int i = 0; i < N; ++i)
        for (int t = i; t >= 0; --t)
            if (dp[t] >= stations[i][0])
                dp[t + 1] = Math.max(dp[t + 1], dp[t] + (long) stations[i][1]);

    for (int i = 0; i <= N; ++i)
        if (dp[i] >= target)
            return i;

    return -1;
}

/**
 * WHY GREEDY + MAX HEAP IS OPTIMAL:
 *
 * - DP approach is O(N²) which is slower
 * - Greedy + heap is O(N log N) — each station pushed/popped at most once
 * - The greedy choice (largest fuel first) is provably optimal:
 *   choosing any smaller fuel would only require more stops
 *
 * COMMON MISTAKE:
 * - Sorting by position (already sorted in input!)
 * - Trying to pick the "nearest" station instead of "most fuel"
 * - Modifying target or confusing position with remaining fuel
 *
 * Pattern: Greedy + Max Heap (delayed decision-making)
 * Similar to: LC 1353 (Max Events), LC 630 (Course Schedule III)
 */
```

### 14) Minimum Number of Visited Cells in a Grid — LC 2617

```java
// java
// LC 2617
// Reference: leetcode_java/src/main/java/LeetCodeJava/Graph/MinimumNumberOfVisitedCellsInAGrid.java

/**
 * Problem: Find minimum cells to visit from (0,0) to (m-1, n-1)
 *
 * Movement Rules:
 * From cell (i,j) with value grid[i][j], you can move to:
 *   - Right: (i, k) where j < k <= j + grid[i][j]
 *   - Down:  (k, j) where i < k <= i + grid[i][j]
 *
 * Example 1:
 * Input: grid = [[3,4,2,1],[4,2,3,1],[2,1,0,0],[2,4,0,0]]
 * Output: 4
 *
 * Example 2:
 * Input: grid = [[2,1,0],[1,0,0]]
 * Output: -1 (no valid path exists)
 *
 * Constraints:
 * - 1 <= m, n <= 10^5
 * - 1 <= m * n <= 10^5
 * - 0 <= grid[i][j] < m * n
 */

// APPROACH: DP + Per-Row/Column Priority Queues with Lazy Deletion
/**
 * KEY INSIGHTS:
 *
 * 1. Why not BFS directly?
 *    - From each cell, you can potentially jump to O(N) cells
 *    - Total complexity would be O(N²) which is too slow
 *
 * 2. Why Priority Queues?
 *    - We need to find the minimum distance cell that can reach (i,j)
 *    - PQ gives us O(log N) access to minimum
 *
 * 3. Lazy Deletion Pattern:
 *    - A cell at (i, prevCol) can reach columns up to prevCol + grid[i][prevCol]
 *    - If current column j > prevCol + grid[i][prevCol], cell is "expired"
 *    - Remove expired cells when encountered (lazy deletion)
 *
 * 4. Per-Row/Column PQs:
 *    - rowPQs[i] = all cells in row i that might reach future columns
 *    - colPQs[j] = all cells in column j that might reach future rows
 *
 * Time: O(M*N*log(M+N)) - each cell enters/exits heaps once
 * Space: O(M*N) - for dist array and heap entries
 */
public int minimumVisitedCells(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dist = new int[m][n];
    for (int[] row : dist) Arrays.fill(row, -1);

    // One PQ per row, one PQ per column
    // Each stores {distance, index} sorted by distance (min-heap)
    PriorityQueue<int[]>[] rowPQs = new PriorityQueue[m];
    PriorityQueue<int[]>[] colPQs = new PriorityQueue[n];

    for (int i = 0; i < m; i++)
        rowPQs[i] = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    for (int j = 0; j < n; j++)
        colPQs[j] = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

    dist[0][0] = 1;  // Starting cell counts as 1 visited

    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {

            // Step 1: Check row PQ - find best cell in same row that can reach j
            while (!rowPQs[i].isEmpty()) {
                int[] top = rowPQs[i].peek();
                int prevCol = top[1];

                // Check if previous cell can jump to current column
                if (prevCol + grid[i][prevCol] >= j) {
                    int d = top[0] + 1;
                    if (dist[i][j] == -1 || d < dist[i][j])
                        dist[i][j] = d;
                    break;  // First valid cell has minimum distance (PQ property)
                }
                // Lazy deletion: cell can't reach j or any future column
                rowPQs[i].poll();
            }

            // Step 2: Check column PQ - find best cell in same column that can reach i
            while (!colPQs[j].isEmpty()) {
                int[] top = colPQs[j].peek();
                int prevRow = top[1];

                if (prevRow + grid[prevRow][j] >= i) {
                    int d = top[0] + 1;
                    if (dist[i][j] == -1 || d < dist[i][j])
                        dist[i][j] = d;
                    break;
                }
                colPQs[j].poll();
            }

            // Step 3: Add current cell to PQs for future cells to use
            if (dist[i][j] != -1 && grid[i][j] > 0) {
                rowPQs[i].offer(new int[]{dist[i][j], j});
                colPQs[j].offer(new int[]{dist[i][j], i});
            }
        }
    }

    return dist[m - 1][n - 1];
}

/**
 * STEP-BY-STEP EXAMPLE:
 *
 * Grid = [[3,4,2,1],
 *         [4,2,3,1],
 *         [2,1,0,0],
 *         [2,4,0,0]]
 *
 * Process (0,0): dist=1, can reach right to col 3, down to row 3
 *   - Add to rowPQs[0]: {1, 0}
 *   - Add to colPQs[0]: {1, 0}
 *
 * Process (0,1): Check rowPQs[0], cell (0,0) can reach col 3 >= 1 ✓
 *   - dist[0][1] = 1 + 1 = 2
 *
 * Process (0,2): Check rowPQs[0], cell (0,0) can reach col 3 >= 2 ✓
 *   - dist[0][2] = 1 + 1 = 2
 *
 * ... continue for all cells ...
 *
 * Final path: (0,0) → (0,2) → (1,2) → (3,2) or similar
 * Answer: 4 cells visited
 *
 * WHY LAZY DELETION WORKS:
 *
 * Consider row i, processing columns left to right (j = 0,1,2,...):
 * - If cell at (i, prevCol) cannot reach column j
 * - Then prevCol + grid[i][prevCol] < j
 * - For any future column j' > j: prevCol + grid[i][prevCol] < j < j'
 * - So cell can NEVER reach any future column → safe to remove
 *
 * RELATED PROBLEMS:
 * - LC 778: Swim in Rising Water (Dijkstra on grid)
 * - LC 1631: Path With Minimum Effort (Dijkstra on grid)
 * - LC 1293: Shortest Path with Obstacles (BFS + state)
 */
```

### 15) Divide Intervals Into Minimum Number of Groups — LC 2406

```java
// java
// LC 2406
// Reference: leetcode_java/src/main/java/LeetCodeJava/Heap/DivideIntervalsIntoMinimumNumberOfGroups.java

/**
 * Problem: Given intervals, divide them into minimum groups so no two intervals
 *          in the same group overlap (two intervals overlap if they share any point).
 *
 * Example:
 * Input:  [[5,10],[6,8],[1,5],[2,3],[1,10]]
 * Output: 3
 *
 * Key Insight:
 * The minimum number of groups = maximum number of intervals overlapping at any point.
 * This is equivalent to the "Meeting Rooms II" insight.
 *
 * Pattern: Sort by start + Min Heap tracking group end times
 */

// APPROACH 1: SORT + MIN HEAP
/**
 * Core Idea:
 * 1. Sort intervals by start time.
 * 2. Min-heap stores the END time of each active group.
 * 3. For each interval:
 *    - If the earliest-ending group ends BEFORE current start → reuse it (poll + offer).
 *    - Otherwise → open a new group (just offer).
 * 4. Heap size at the end = number of groups needed.
 *
 * NOTE: [1,5] and [5,10] OVERLAP (inclusive endpoints), so reuse only when end < start.
 *
 * Time:  O(N log N) — sort + heap operations
 * Space: O(N)       — heap stores at most N end times
 */
public int minGroups(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));  // sort by start

    PriorityQueue<Integer> pq = new PriorityQueue<>();  // min-heap of end times

    for (int[] interval : intervals) {
        if (!pq.isEmpty() && pq.peek() < interval[0]) {
            pq.poll();  // reuse earliest-ending group
        }
        pq.offer(interval[1]);  // assign current interval to a group
    }

    return pq.size();  // number of concurrent groups = answer
}

// APPROACH 2: SEPARATE SORT (Two-Pointer / Line Sweep)
/**
 * Core Idea:
 * 1. Separate starts[] and ends[] arrays and sort them independently.
 * 2. Sweep through starts; for each start, check if the smallest end has passed.
 *    - start > ends[endPointer] → a group freed up, advance endPointer (don't add group).
 *    - Otherwise → overlap, need a new group.
 * 3. groupCount = number of new groups opened.
 *
 * Time:  O(N log N)
 * Space: O(N)
 */
public int minGroups_v2(int[][] intervals) {
    int n = intervals.length;
    int[] starts = new int[n], ends = new int[n];
    for (int i = 0; i < n; i++) {
        starts[i] = intervals[i][0];
        ends[i]   = intervals[i][1];
    }
    Arrays.sort(starts);
    Arrays.sort(ends);

    int groupCount = 0, endPointer = 0;
    for (int start : starts) {
        if (start > ends[endPointer]) {
            endPointer++;   // reuse a group
        } else {
            groupCount++;   // need a new group
        }
    }
    return groupCount;
}

/**
 * SIMILAR PROBLEMS:
 *
 * - LC 253  Meeting Rooms II          — same algorithm, identical logic
 * - LC 1353 Maximum Events Attended   — greedy + min heap by end time
 * - LC 56   Merge Intervals           — sort + merge overlapping
 * - LC 57   Insert Interval           — merge after inserting
 * - LC 435  Non-Overlapping Intervals — greedy, minimize removed to avoid overlap
 * - LC 452  Minimum Arrows to Burst Balloons — greedy interval scheduling
 * - LC 1094 Car Pooling               — difference array / heap scheduling
 *
 * KEY PATTERN RULE:
 *   min groups = max concurrent overlaps
 *   → always equals heap size when using Sort + Min Heap approach
 */
```

### 16) Minimize Deviation in Array — LC 1675

```java
// java
// LC 1675
// Reference: leetcode_java/src/main/java/LeetCodeJava/Heap/MinimizeDeviationInArray.java

/**
 * Problem: Given nums[], you can:
 *   - Divide any EVEN element by 2  (any number of times)
 *   - Multiply any ODD element by 2 (any number of times)
 * Return the minimum possible deviation = max(nums) - min(nums).
 *
 * Example 1: nums = [1,2,3,4] → [2,2,3,2] → deviation = 3 - 2 = 1
 * Example 2: nums = [4,1,5,20,3] → [4,2,5,5,3] → deviation = 5 - 2 = 3
 *
 * Key Observations:
 * 1. Odd numbers can only be multiplied by 2 ONCE to become even,
 *    then only divided. So first multiply all odds to get their maximum.
 * 2. After making everything even, only DIVISION is possible.
 * 3. To minimize deviation, always shrink the current maximum (divide by 2),
 *    tracking the running minimum along the way.
 * 4. Stop when max is odd (can no longer be divided).
 *
 * Pattern: Greedy + Max Heap
 */

// APPROACH: GREEDY + MAX HEAP
/**
 * Steps:
 * 1. Normalize: multiply all odd numbers by 2 → everything is now even.
 *    Track the global minimum during this step.
 * 2. Push all values into a MAX heap.
 * 3. Loop:
 *    a. Poll the max from the heap.
 *    b. Record deviation = max - min (update answer).
 *    c. If max is ODD → can't divide further → break (best we can do).
 *    d. If max is EVEN → divide by 2, update min, push back to heap.
 *
 * Why max heap?
 *   We always want to reduce the LARGEST value to shrink the range.
 *   The minimum only ever decreases (division makes values smaller).
 *
 * Time:  O(N log N * log(maxVal)) — each element divided at most log(maxVal) times
 * Space: O(N)
 */
public int minimumDeviation(int[] nums) {
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    int min = Integer.MAX_VALUE;

    // Step 1: normalize — make all values even (multiply odds by 2)
    for (int x : nums) {
        if (x % 2 == 1) x *= 2;
        maxHeap.offer(x);
        min = Math.min(min, x);
    }

    int deviation = Integer.MAX_VALUE;

    // Step 2: repeatedly reduce the max until it is odd
    while (true) {
        int max = maxHeap.poll();
        deviation = Math.min(deviation, max - min);

        if (max % 2 == 1) break;  // odd → can't divide further → done

        max /= 2;
        min = Math.min(min, max);  // new value may be the new minimum
        maxHeap.offer(max);
    }

    return deviation;
}

/**
 * WHY THIS WORKS:
 *
 * - After normalization, all numbers are even; we can only divide (move values down).
 * - Each division brings the max closer to the min, potentially reducing deviation.
 * - We stop the moment the max is odd because:
 *     * Multiplying it by 2 would only increase the max (worse deviation).
 *     * The smallest achievable deviation at this state is already recorded.
 * - The minimum is tracked explicitly because after a division the new value
 *   might be smaller than all current heap elements.
 *
 * SIMILAR PROBLEMS:
 * - LC 2616 Minimize the Maximum Difference of Pairs — binary search + greedy
 * - LC 910  Smallest Range II                        — sort + greedy math
 * - LC 1671 Minimum Number of Removals to Sort       — greedy + heap
 * - LC 621  Task Scheduler                           — max heap + greedy
 * - LC 502  IPO                                      — two heaps + greedy
 *
 * COMMON MISTAKE:
 * - Trying to increase small values by multiplying: after normalization,
 *   multiplying any even number makes it larger → increases max → worse.
 *   Only dividing the max is ever beneficial.
 */
```

### 17) Task Scheduler — LC 621

```python
# python
# LC 621
# Reference: leetcode_python/Greedy/task-scheduler.py

"""
Problem: Given tasks (char array) and a cooling interval n, return the minimum
         number of CPU intervals needed to finish all tasks.
         Between two identical tasks there must be at least n intervals
         (filled by other tasks or idle).

Example:
  tasks = ["A","A","A","B","B","B"], n = 2
  Output: 8   →  A → B → idle → A → B → idle → A → B
"""

# ── Core Idea ────────────────────────────────────────────────────────────────
# Use a MAX HEAP to always schedule the highest-frequency task next.
# After execution, a task enters a COOLING QUEUE and becomes available again
# only after n time units have passed.
#
#   max_heap  : stores negative counts (simulated max heap via Python's min heap)
#               → always grabs the most-frequent remaining task
#   cooling_queue : deque of (remaining_neg_count, available_at_time)
#               → holds tasks waiting out their cooldown
#
# Each tick (time += 1):
#   1. Pop highest-freq task from heap, execute it (count += 1 because negated).
#   2. If count still < 0 (copies remain), push (count, time + n) into queue.
#   3. If the front of the queue is now available (available_at == time), push it back to heap.
# ─────────────────────────────────────────────────────────────────────────────

# V0 : BIG PQ + COOLING QUEUE (simulate time tick-by-tick)
import heapq
from collections import Counter, deque

class Solution(object):
    def leastInterval(self, tasks, n):
        if not tasks:
            return 0
        if n == 0:
            return len(tasks)

        counts = Counter(tasks)

        # Max-heap via negation
        max_heap = [-cnt for cnt in counts.values()]
        heapq.heapify(max_heap)

        # cooling_queue stores (remaining_neg_count, available_time)
        cooling_queue = deque()
        time = 0

        while max_heap or cooling_queue:
            time += 1

            if max_heap:
                neg_cnt = heapq.heappop(max_heap)
                remaining_cnt = neg_cnt + 1   # one execution consumed

                # remaining_cnt < 0  →  copies still exist; put in cooldown
                # remaining_cnt == 0 →  task fully done; discard
                if remaining_cnt < 0:
                    cooling_queue.append((remaining_cnt, time + n))

            # Re-queue any task whose cooldown just expired
            if cooling_queue and cooling_queue[0][1] == time:
                ready_cnt, _ = cooling_queue.popleft()
                heapq.heappush(max_heap, ready_cnt)

        return time

# V1 : Math formula  O(N) time, O(1) space
# task_time = (max_count - 1) * (n + 1) + num_tasks_with_max_count
# Answer    = max(task_time, len(tasks))   ← can't be less than total task count
import collections
class Solution(object):
    def leastInterval_math(self, tasks, n):
        count = collections.Counter(tasks)
        most      = count.most_common(1)[0][1]
        num_most  = sum(1 for v in count.values() if v == most)
        return max((most - 1) * (n + 1) + num_most, len(tasks))
```

**為什麼要檢查 `remaining_cnt < 0`？**

任務的數量以**負數**儲存，用來假裝成最大堆積。
- `-3 + 1 = -2` → 還剩 2 份 → 放進冷卻佇列。
- `-1 + 1 =  0` → 任務用完了 → 直接丟掉，不再排回佇列。

**模式：最大堆積 + 冷卻佇列（貪婪排程）**

| 步驟 | 資料結構 | 用途 |
|------|---------------|---------|
| 挑下一個任務 | `max_heap`（數量取負） | 永遠優先安排出現次數最多的任務 |
| 強制冷卻 | `cooling_queue` deque | 把任務押住，直到經過 `time + n` |
| 重新啟用 | 從佇列取出 → 推回堆積 | 任務重新變成可安排 |

**相似題目：**

| LC # | 題目 | 共通模式 |
|------|---------|---------------|
| 767 | Reorganize String | 最大堆積，依頻率交錯排列 |
| 1353 | Maximum Number of Events That Can Be Attended | 貪婪 + 依截止日的堆積 |
| 502 | IPO | 雙堆積貪婪（利潤 + 資本） |
| 1675 | Minimize Deviation in Array | 最大堆積 + 貪婪縮小 |
| 295 | Find Median from Data Stream | 雙堆積系統 |

### 18) Most Frequent IDs — LC 3092

> 模式：**延遲刪除(Lazy Deletion)** — 通用形式見 [heap_advanced.md § Lazy Deletion](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-)。
> 參考：`leetcode_python/Heap/most-frequent-ids.py`

**題目**：`nums[i]` 是一個 ID，`freq[i]` 表示加入該 ID 這麼多份（若為負數則是移除）。
每一步之後，回報**出現次數最多**的那個 ID 的數量（集合為空則回報 0）。

**為什麼單純用堆積會失敗**：一個 ID 的數量會隨時間*改變*。當 `2` 的數量從 3 掉到 0 時，
`(-3, 2)` 這筆資料埋在堆積裡的某處，我們沒辦法在 O(log n) 內找到它。

**核心想法**

```text
c_map = {}   # id -> TRUE frequency          <- source of truth
pq    = []   # max-heap of (-freq, id)       <- candidates, possibly stale
```

每一步：更新 `c_map`、推入新的 `(-freq, id)`，然後**只從堆頂做延遲刪除，
直到堆頂與真實值一致為止**。

```text
nums = [2,3,2,1],  freq = [3,2,-3,1]

step 0: c_map={2:3}          push(-3,2)   top=(-3,2) valid           -> ans 3
step 1: c_map={2:3,3:2}      push(-2,3)   top=(-3,2) valid           -> ans 3
step 2: c_map={2:0,3:2}      push( 0,2)   top=(-3,2) STALE (2 is 0)  -> pop
                                          top=(-2,3) valid -> STOP   -> ans 2
                                          ( (0,2) still sits in pq — untouched )
step 3: c_map={2:0,3:2,1:1}  push(-1,1)   top=(-2,3) valid           -> ans 2

output = [3,3,2,2]
```

注意步驟 2：我們只 pop 掉**剛好一筆** — 也就是擋住答案的那一筆 — 其他過期的資料
全都原地留著。這就是整個技巧的全部。

```python
# python
# LC 3092. Most Frequent IDs
# IDEA: PQ + `Lazy Deletion` + hashmap
# time = O(n log n), space = O(n)
import heapq

class Solution(object):
    def mostFrequentIDs(self, nums, freq):
        c_map = {}   # id -> TRUE frequency
        pq = []      # max-heap of (-frequency, id)

        n = len(nums)
        ans = [0] * n

        for i in range(n):
            val, cnt = nums[i], freq[i]

            # 1. update the true frequency
            c_map[val] = c_map.get(val, 0) + cnt

            # 2. push the updated frequency
            #    (we do NOT delete the old entry from the heap)
            heapq.heappush(pq, (-c_map[val], val))

            """
            NOTE !!!!  how we do `lazy delete`

            -> we ONLY delete `till we reach a correct cnt one`
            -> we leave ALL other cnt (pq elements) unchanged
               -> we ONLY do `lazy delete` till the needed idx
            """
            # 3. pop stale tops: heap freq != true freq  => outdated
            while pq and -pq[0][0] != c_map[pq[0][1]]:
                heapq.heappop(pq)

            # 4. top is now guaranteed accurate (or heap is empty -> 0)
            ans[i] = -pq[0][0] if pq else 0

        return ans
```

```java
// java
// LC 3092. Most Frequent IDs
// time = O(n log n), space = O(n)
class Solution {
    public long[] mostFrequentIDs(int[] nums, int[] freq) {
        Map<Integer, Long> cMap = new HashMap<>();          // id -> TRUE frequency
        // max-heap of {frequency, id}
        PriorityQueue<long[]> pq =
            new PriorityQueue<>((a, b) -> Long.compare(b[0], a[0]));

        int n = nums.length;
        long[] ans = new long[n];

        for (int i = 0; i < n; i++) {
            int val = nums[i];

            // 1. update truth
            long cur = cMap.getOrDefault(val, 0L) + freq[i];
            cMap.put(val, cur);

            // 2. push new candidate, old entry stays behind
            pq.offer(new long[]{cur, val});

            /** NOTE !!! lazy delete ONLY until the top is valid */
            // 3. drop stale tops
            while (!pq.isEmpty() && pq.peek()[0] != cMap.get((int) pq.peek()[1])) {
                pq.poll();
            }

            // 4. read answer
            ans[i] = pq.isEmpty() ? 0 : pq.peek()[0];
        }

        return ans;
    }
}
```

**邊界情況**
- 集合變成空的（`nums=[5,5,3], freq=[2,-2,1]` → `[2,0,1]`）：當 `5` 的數量歸零時
  我們推入 `(0, 5)`。因為 `-0 == 0 == c_map[5]`，這筆是**有效的**、會留下 — 答案
  正確地是 `0`。（推入數量為零的項目無害，而且讓檢查邏輯保持一致。）
- 數量可能達到 `n * max(freq) = 1e10` → **Java 要用 `long`**，`int` 會溢位。

**相似題目（延遲刪除）**

| 題目 | LC # | 什麼會過期 | 過期判斷 | 難度 |
|---------|------|-----------------|-----------|------------|
| Most Frequent IDs | 3092 | 某個 ID 的頻率改變了 | `heapVal != map[id]` | Medium |
| Design a Number Container System | 2349 | 某個索引被指派了新的數字 | `heapIdx` 目前的數字 != 這個數字 | Medium |
| Single-Threaded CPU | 1834 | —（純粹的可用性掃描） | 指標 + 時間閘門 | Medium |
| Sliding Window Median | 480 | 元素滑出視窗了 | `val in removed` 計數器 | Hard |
| Finding MK Average | 1825 | 元素離開了最後 m 筆的串流 | delete-set / multiset | Hard |
| Sliding Window Maximum | 239 | 索引掉出視窗了 | `pq[0].idx <= i - k` | Hard |
| Maximum Number of Events | 1353 | 活動的截止日已過 | `pq[0] < day` | Medium |
| The Number of Beautiful Subsets / Seat Manager | 1845 | 座位被預訂 / 取消預訂 | 重用已釋出 id 的最小堆積 | Medium |
| Process Tasks Using Servers | 2073 | 伺服器忙碌到時刻 t | 雙堆積 + 時間閘門 | Medium |
| Minimum Number of Visited Cells in Grid | 2617 | 格子已經定案 | 每列 / 每行一個 PQ + 延遲 pop | Hard |
| Task Scheduler II / Dijkstra (743, 1631, 778) | — | 後來找到了更短的路徑 | `d > dist[node]: continue` | Medium |

> 💡 **Dijkstra 是最有名的延遲刪除演算法。** 那行經典的
> `if d > dist[u]: continue` *就是*一次延遲刪除 — 它直接丟掉一筆過期的距離資料，
> 而不是對堆積做 decrease-key。同樣的模式，換件衣服而已。


> 下面五個範例來自舊的 `priority_queue.md`，是**以 Java 為主**的 — 對應的
> Python 版本放在 [heap.md](./heap.md) 的模板裡。

### 19) K Closest Points to Origin — LC 973
```java
// Java
// LC 973 - Find K closest points to origin (0,0)
// IDEA: Max heap of size K (to keep K smallest distances)
// Time: O(N log K), Space: O(K)

public int[][] kClosest(int[][] points, int k) {
    // Max heap based on distance (squared, no need for sqrt)
    PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
        (a, b) -> Integer.compare(b[0]*b[0] + b[1]*b[1], a[0]*a[0] + a[1]*a[1])
    );

    for (int[] point : points) {
        maxHeap.offer(point);
        if (maxHeap.size() > k) {
            maxHeap.poll();  // Remove farthest point
        }
    }

    // Convert heap to result array
    int[][] result = new int[k][2];
    for (int i = 0; i < k; i++) {
        result[i] = maxHeap.poll();
    }

    return result;
}

// Alternative: Min heap (push all, pop k)
public int[][] kClosest_MinHeap(int[][] points, int k) {
    PriorityQueue<int[]> minHeap = new PriorityQueue<>(
        (a, b) -> Integer.compare(a[0]*a[0] + a[1]*a[1], b[0]*b[0] + b[1]*b[1])
    );

    for (int[] point : points) {
        minHeap.offer(point);
    }

    int[][] result = new int[k][2];
    for (int i = 0; i < k; i++) {
        result[i] = minHeap.poll();
    }

    return result;
}
```


### 20) Reorganize String — LC 767
```java
// Java
// LC 767 - Rearrange string so no adjacent characters are same
// IDEA: Max heap - always pick most frequent, alternate placement
// Time: O(N log 26) = O(N), Space: O(26) = O(1)

public String reorganizeString(String s) {
    // Count frequency
    int[] freq = new int[26];
    for (char c : s.toCharArray()) {
        freq[c - 'a']++;
    }

    // Check if possible: no char should appear more than (n+1)/2 times
    int n = s.length();
    for (int f : freq) {
        if (f > (n + 1) / 2) {
            return "";
        }
    }

    // Max heap: [frequency, char]
    PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
        (a, b) -> Integer.compare(b[0], a[0])
    );

    for (int i = 0; i < 26; i++) {
        if (freq[i] > 0) {
            maxHeap.offer(new int[]{freq[i], i});
        }
    }

    StringBuilder sb = new StringBuilder();

    while (maxHeap.size() >= 2) {
        // Take two most frequent characters
        int[] first = maxHeap.poll();
        int[] second = maxHeap.poll();

        sb.append((char) (first[1] + 'a'));
        sb.append((char) (second[1] + 'a'));

        // Put back if remaining
        if (--first[0] > 0) maxHeap.offer(first);
        if (--second[0] > 0) maxHeap.offer(second);
    }

    // Handle last character if any
    if (!maxHeap.isEmpty()) {
        sb.append((char) (maxHeap.poll()[1] + 'a'));
    }

    return sb.toString();
}
```


### 21) Sliding Window Median — LC 480
```java
// Java
// LC 480 - Return median of each sliding window of size k
// IDEA: Two heaps (TreeMap/Multiset for lazy removal)
// Time: O(N log K), Space: O(K)

public double[] medianSlidingWindow(int[] nums, int k) {
    // Use TreeMap to support removal
    TreeMap<Integer, Integer> small = new TreeMap<>(Collections.reverseOrder()); // max heap
    TreeMap<Integer, Integer> large = new TreeMap<>(); // min heap

    int smallSize = 0, largeSize = 0;
    double[] result = new double[nums.length - k + 1];

    for (int i = 0; i < nums.length; i++) {
        // Add to appropriate heap
        if (small.isEmpty() || nums[i] <= small.firstKey()) {
            add(small, nums[i]);
            smallSize++;
        } else {
            add(large, nums[i]);
            largeSize++;
        }

        // Rebalance
        while (smallSize > largeSize + 1) {
            int val = small.firstKey();
            remove(small, val);
            smallSize--;
            add(large, val);
            largeSize++;
        }
        while (largeSize > smallSize) {
            int val = large.firstKey();
            remove(large, val);
            largeSize--;
            add(small, val);
            smallSize++;
        }

        // Window is full
        if (i >= k - 1) {
            // Calculate median
            if (k % 2 == 1) {
                result[i - k + 1] = small.firstKey();
            } else {
                result[i - k + 1] = ((double) small.firstKey() + large.firstKey()) / 2.0;
            }

            // Remove element leaving window
            int toRemove = nums[i - k + 1];
            if (toRemove <= small.firstKey()) {
                remove(small, toRemove);
                smallSize--;
            } else {
                remove(large, toRemove);
                largeSize--;
            }
        }
    }

    return result;
}

private void add(TreeMap<Integer, Integer> map, int val) {
    map.put(val, map.getOrDefault(val, 0) + 1);
}

private void remove(TreeMap<Integer, Integer> map, int val) {
    int count = map.get(val);
    if (count == 1) {
        map.remove(val);
    } else {
        map.put(val, count - 1);
    }
}
```


### 22) Sort Characters By Frequency — LC 451
```java
// Java
// LC 451 - Sort characters in string by frequency (descending)
// IDEA: Count frequency, use max heap to build result
// Time: O(N log K) where K = unique chars, Space: O(N)

public String frequencySort(String s) {
    // Count frequency
    Map<Character, Integer> freq = new HashMap<>();
    for (char c : s.toCharArray()) {
        freq.put(c, freq.getOrDefault(c, 0) + 1);
    }

    // Max heap based on frequency
    PriorityQueue<Character> maxHeap = new PriorityQueue<>(
        (a, b) -> Integer.compare(freq.get(b), freq.get(a))
    );

    maxHeap.addAll(freq.keySet());

    // Build result
    StringBuilder sb = new StringBuilder();
    while (!maxHeap.isEmpty()) {
        char c = maxHeap.poll();
        int count = freq.get(c);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
    }

    return sb.toString();
}

// Bucket Sort Alternative (O(N) time)
public String frequencySort_Bucket(String s) {
    Map<Character, Integer> freq = new HashMap<>();
    for (char c : s.toCharArray()) {
        freq.put(c, freq.getOrDefault(c, 0) + 1);
    }

    // Bucket: index = frequency
    List<Character>[] buckets = new List[s.length() + 1];
    for (int i = 0; i < buckets.length; i++) {
        buckets[i] = new ArrayList<>();
    }

    for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
        buckets[entry.getValue()].add(entry.getKey());
    }

    StringBuilder sb = new StringBuilder();
    for (int i = buckets.length - 1; i >= 0; i--) {
        for (char c : buckets[i]) {
            for (int j = 0; j < i; j++) {
                sb.append(c);
            }
        }
    }

    return sb.toString();
}
```


### 23) Last Stone Weight — LC 1046
```java
// Java
// LC 1046 - Smash two heaviest stones, return remaining weight
// IDEA: Max heap - always get two largest
// Time: O(N log N), Space: O(N)

public int lastStoneWeight(int[] stones) {
    // Max heap
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
        Collections.reverseOrder()
    );

    for (int stone : stones) {
        maxHeap.offer(stone);
    }

    while (maxHeap.size() > 1) {
        int stone1 = maxHeap.poll();  // Heaviest
        int stone2 = maxHeap.poll();  // Second heaviest

        if (stone1 != stone2) {
            maxHeap.offer(stone1 - stone2);  // Remaining weight
        }
        // If equal, both destroyed
    }

    return maxHeap.isEmpty() ? 0 : maxHeap.peek();
}
```


## 總結與速查

| # | 題目 | LC | 呈現語言 | 模式（模板所在位置） |
|---|---------|----|----------------|------------------------------|
| 1 | Kth Largest Element in a Stream | 703 | Python | 第 K 個元素 — [heap.md](./heap.md#specific-pattern-templates) |
| 2 | Ugly Number II | 264 | Python | 堆積生成 + 去重集合 |
| 3 | Find Median from Data Stream | 295 | Python | 雙堆積 — [heap.md](./heap.md#specific-pattern-templates) |
| 4 | Minimum Cost to Connect Sticks | 1167 | Python | 重複「取兩個最小的」 |
| 5 | The kth Factor of n | 1492 | Python | 大小上限為 k 的最大堆積 |
| 6 | Least Number of Unique Integers after K Removals | 1481 | Python | Counter + 次數的最小堆積 |
| 7 | Maximum Number of Events That Can Be Attended | 1353 | Python | 時間掃描 + 截止日堆積 |
| 8 | Maximum Frequency Stack | 895 | Python | 依頻率分桶的堆疊（不需要堆積） |
| 9 | Find K Pairs with Smallest Sums | 373 | Java | 對虛擬網格做 K 路合併 |
| 10 | Kth Smallest Element in a Sorted Matrix | 378 | Java | 大小為 k 的最大堆積 |
| 11 | Minimum Deletions to Make Character Frequencies Unique | 1647 | Java | 頻率唯一性 — [heap_advanced.md](./heap_advanced.md) |
| 12 | Maximum Performance of a Team | 1383 | Java | 排序 + 固定大小堆積 — [heap_advanced.md](./heap_advanced.md) |
| 13 | Minimum Number of Refueling Stops | 871 | Java | 反悔貪婪 — [heap_advanced.md](./heap_advanced.md) |
| 14 | Minimum Number of Visited Cells in a Grid | 2617 | Java | 每列 / 每行 PQ + 延遲刪除 — [heap_advanced.md](./heap_advanced.md) |
| 15 | Divide Intervals Into Minimum Number of Groups | 2406 | Java | 區間排程 — [heap.md](./heap.md#template-4-interval-scheduling-pattern--lc-253) |
| 16 | Minimize Deviation in Array | 1675 | Java | 最大堆積 + 貪婪縮小 |
| 17 | Task Scheduler | 621 | Python | 最大堆積 + 冷卻佇列 |
| 18 | Most Frequent IDs | 3092 | Python + Java | 延遲刪除 — [heap_advanced.md](./heap_advanced.md) |
| 19 | K Closest Points to Origin | 973 | Java | 大小為 k 的最大堆積 |
| 20 | Reorganize String | 767 | Java | 貪婪組字串 |
| 21 | Sliding Window Median | 480 | Java | 兩個有序 multiset（`TreeMap`） |
| 22 | Sort Characters By Frequency | 451 | Java | Counter + 最大堆積（也可用桶排序） |
| 23 | Last Stone Weight | 1046 | Java | 最大堆積模擬 |

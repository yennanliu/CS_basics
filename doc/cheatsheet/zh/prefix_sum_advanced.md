# 前綴和 — 進階模板

> **範圍** — 五個需要借用其他結構或其他恆等式的前綴和模板：補集技巧、應付含負數陣列的單調雙端佇列、二維的列對壓縮、前綴 XOR，以及用雜湊表實作的稀疏差分陣列。
> **另見**：[prefix_sum.md](./prefix_sum.md) — 母篇：模板 1–8、概念與模板選擇策略；[prefix_sum_examples.md](./prefix_sum_examples.md) — 實作過的例題；[monotonic_queue.md](./monotonic_queue.md) — 模板 10 背後的雙端佇列；[difference_array.md](./difference_array.md) — 模板 13 的稠密版本；[bit_manipulation.md](./bit_manipulation.md) — 為什麼 XOR 跟加法一樣支援同一個相減恆等式；[matrix.md](./matrix.md) — 模板 11 壓掉的那層二維幾何。

## LeetCode 題目清單

- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)
- [Matrix](https://leetcode.com/problem-list/matrix/)

## 總覽

[prefix_sum.md](./prefix_sum.md) 裡的模板 1–8 講的都是同一招：把陣列建出來，然後相減兩項。
下面這五個，就是那一招不夠用的時候。

### 關鍵性質
- **複雜度**：各模板分別標示；每一個的重點都是把 O(n²) 或 O(n·m²) 的掃描壓成 O(n) 或 O(n·m)
- **核心想法**：前綴和恆等式 `sum(l, r) = P[r+1] - P[l]` 對任何**可逆**的合併運算都成立 — 這就是 XOR 可行、而 min/max 不行的原因
- **什麼時候用**：當你一眼就想到前綴和，但題目有東西把它弄壞了 — 負數、二維、環狀繞回，或座標大到開不出陣列


### 模板 9：補集技巧 —「總和 − 中間視窗」⭐⭐⭐⭐⭐ — LC 1423

**核心想法：** 當元素是**從兩端**拿走時，**剩下沒拿的**永遠是一段連續的中間子陣列。所以不要去枚舉 `(leftTake, rightTake)` 的組合，把問題反過來看：

```text
take k from the two ends, MAXIMIZE taken
        ⇕  (complement)
leave a contiguous window of length n-k, MINIMIZE it

answer = total - min(window of fixed length n-k)

[ 1, 2, 3, 4, 5, 6, 1 ]   n=7, k=3  → window length w = 4
  └──── take ────┘           windows: [1,2,3,4]=10  [2,3,4,5]=14
        ↑ leave the cheapest length-4 window          [3,4,5,6]=18  [4,5,6,1]=16
  total = 22, min window = 10  →  answer = 22 - 10 = 12 ✓
```

這樣一來，「從兩端拿」的題目就變成單純的**前綴和上的固定長度視窗**問題 — 這也是它歸在這裡、而不是歸在雙指標那份文件的原因。

```java
// java
// LC 1423 - Maximum Points You Can Obtain from Cards
// IDEA: complement — maximize both ends == minimize the middle window of length n-k
// time = O(n), space = O(1)
public int maxScore(int[] cardPoints, int k) {
    int n = cardPoints.length, w = n - k;
    int total = 0;
    for (int x : cardPoints) total += x;
    if (w == 0) return total;               // take everything

    int win = 0;
    for (int i = 0; i < w; i++) win += cardPoints[i];
    int minWin = win;

    // slide the fixed-length window (rolling prefix-sum difference)
    for (int i = w; i < n; i++) {
        win += cardPoints[i] - cardPoints[i - w];
        minWin = Math.min(minWin, win);
    }
    return total - minWin;
}
```

```python
# python
# LC 1423 - Maximum Points You Can Obtain from Cards
# IDEA: complement — maximize both ends == minimize the middle window of length n-k
# time = O(n), space = O(1)
def maxScore(cardPoints, k):
    n = len(cardPoints)
    w = n - k
    total = sum(cardPoints)
    if w == 0:
        return total

    win = sum(cardPoints[:w])
    min_win = win
    for i in range(w, n):
        win += cardPoints[i] - cardPoints[i - w]   # slide right, drop left
        min_win = min(min_win, win)

    return total - min_win
```

#### 變形 — **視窗長度不固定**（LC 1658）

一樣是補集，但這裡是從兩端一直拿到總和等於 `x`，所以中間視窗的**和是固定的**（`total - x`），而**長度可變**、且我們要**最大化**它。因為 `nums[i] >= 1`，前綴和嚴格遞增 → 用收縮視窗就可以。

```java
// java
// LC 1658 - Minimum Operations to Reduce X to Zero
// IDEA: remove-from-ends summing to x  ==  keep the LONGEST middle subarray summing to (total - x)
// time = O(n), space = O(1)
public int minOperations(int[] nums, int x) {
    int n = nums.length, total = 0;
    for (int v : nums) total += v;
    int target = total - x;
    if (target < 0) return -1;

    int left = 0, sum = 0, best = -1;
    for (int right = 0; right < n; right++) {
        sum += nums[right];
        while (sum > target) sum -= nums[left++];     // values are positive → safe to shrink
        if (sum == target) best = Math.max(best, right - left + 1);
    }
    return best == -1 ? -1 : n - best;                // ops = n - kept
}
```

```python
# python
# LC 1658 - Minimum Operations to Reduce X to Zero
# time = O(n), space = O(1)
def minOperations(nums, x):
    n = len(nums)
    target = sum(nums) - x
    if target < 0:
        return -1

    left = s = 0
    best = -1
    for right, v in enumerate(nums):
        s += v
        while s > target:
            s -= nums[left]
            left += 1
        if s == target:
            best = max(best, right - left + 1)

    return -1 if best == -1 else n - best
```

> 如果值可能是**負的**，收縮迴圈就會壞掉 — 退回模板 2（`{prefix_sum: first_index}`）去找和為 `target` 的最長子陣列。

### 模板 10：前綴和 + 單調雙端佇列（最短子陣列，允許**負數**）⭐⭐⭐⭐⭐ — LC 862

**核心想法：**「和 ≥ K 的最短子陣列」只有在**所有值都非負**時才能直接用滑動視窗（LC 209）。一旦有負數，前綴和不再單調，視窗也就不能安全地收縮。解法：維護一個**遞增的前綴和索引單調雙端佇列**。

```text
Two rules, both discard indices that can never be the best LEFT end:

1) POP FRONT while  p[i] - p[dq.front] >= K
   → dq.front already satisfies the condition at this i.
     Any later i' > i gives a LONGER subarray, so this front is used once and dropped.

2) POP BACK while  p[dq.back] >= p[i]
   → index i has a prefix sum <= dq.back but is FURTHER RIGHT.
     It is better as a left end in every way (smaller sum AND shorter span). dq.back is dead.

Deque therefore holds indices with STRICTLY INCREASING prefix sums. Each index is
pushed once and popped once → O(n) total.
```

**在 `nums = [2, -1, 2], K = 3`（`p = [0, 2, 1, 3]`）上的追蹤：**

```text
i=0 p=0 : deque empty          → push 0            dq = [0]
i=1 p=2 : 2-0=2 < 3            → push 1            dq = [0,1]
i=2 p=1 : 1-0=1 < 3 ; p[1]=2 >= 1 → pop back 1, push 2   dq = [0,2]
i=3 p=3 : 3-p[0]=3 >= 3 → ans = 3-0 = 3, pop front
          3-p[2]=2 < 3 → stop ; push 3             dq = [2,3]
answer = 3
```

```java
// java
// LC 862 - Shortest Subarray with Sum at Least K
// IDEA: prefix sum + monotonic increasing deque (handles negative numbers)
// time = O(n), space = O(n)
public int shortestSubarray(int[] nums, int k) {
    int n = nums.length;
    long[] p = new long[n + 1];                       // long: sums can overflow int
    for (int i = 0; i < n; i++) p[i + 1] = p[i] + nums[i];

    Deque<Integer> dq = new ArrayDeque<>();           // indices, p[] increasing
    int ans = n + 1;

    for (int i = 0; i <= n; i++) {
        // rule 1: front satisfies the condition -> record and retire it
        while (!dq.isEmpty() && p[i] - p[dq.peekFirst()] >= k) {
            ans = Math.min(ans, i - dq.pollFirst());
        }
        // rule 2: keep prefix sums increasing
        while (!dq.isEmpty() && p[dq.peekLast()] >= p[i]) {
            dq.pollLast();
        }
        dq.offerLast(i);
    }
    return ans <= n ? ans : -1;
}
```

```python
# python
# LC 862 - Shortest Subarray with Sum at Least K
# IDEA: prefix sum + monotonic increasing deque (handles negative numbers)
# time = O(n), space = O(n)
from collections import deque
from itertools import accumulate

def shortestSubarray(nums, k):
    n = len(nums)
    p = list(accumulate(nums, initial=0))
    dq = deque()          # indices with increasing p[]
    ans = n + 1

    for i in range(n + 1):
        while dq and p[i] - p[dq[0]] >= k:      # rule 1: found a valid left end
            ans = min(ans, i - dq.popleft())
        while dq and p[dq[-1]] >= p[i]:         # rule 2: keep p[] increasing
            dq.pop()
        dq.append(i)

    return ans if ans <= n else -1
```

| 題目 | LC # | 值域 | 該用的工具 |
|---------|------|--------|------------|
| Minimum Size Subarray Sum | 209 | 全正 | 單純滑動視窗（見 `sliding_window.md`） |
| Shortest Subarray with Sum at Least K | 862 | 可能有負 | **前綴和 + 單調雙端佇列** |
| Subarray Sum Equals K | 560 | 任意 | 模板 2（雜湊表，精確和） |

### 模板 11：列對壓縮 — 把二維壓成一維前綴和 ⭐⭐⭐⭐ — LC 363

**核心想法：** 每個子矩陣都由一組**列對** `(top, bottom)` 加上一段行區間決定。固定列對，把這兩列之間每一行的和壓成一維陣列 `colSum`，二維問題就變成對應的**一維子陣列問題** — 那個你早就會解了。

```text
fix top/bottom rows                  1-D array of column sums
┌───────────────┐
│ . . . . . . . │
│ a b c d e f g │ ← top          colSum = [a+h, b+i, c+j, ...]
│ h i j k l m n │ ← bottom
│ . . . . . . . │                then answer the 1-D version of the problem
└───────────────┘

O(m²) row pairs × O(n · cost of the 1-D solver)
```

以 **LC 363（最大矩形和 ≤ k）** 來說，一維子問題是「和 ≤ k 的最大子陣列」：
`run - prefix_j <= k`  ⟹  `prefix_j >= run - k`  ⟹  在有序集合中查**大於等於 run − k 的最小前綴和**（`ceiling` / `bisect_left`）。

```java
// java
// LC 363 - Max Sum of Rectangle No Larger Than K
// IDEA: fix row pair -> column sums -> 1D "max subarray sum <= k" via TreeSet ceiling
// time = O(m^2 * n * log n), space = O(n)
public int maxSumSubmatrix(int[][] matrix, int k) {
    int m = matrix.length, n = matrix[0].length;
    int best = Integer.MIN_VALUE;

    for (int top = 0; top < m; top++) {
        int[] colSum = new int[n];                     // reset per top row
        for (int bot = top; bot < m; bot++) {
            for (int c = 0; c < n; c++) colSum[c] += matrix[bot][c];   // extend downward

            // ---- 1D: max subarray sum <= k ----
            TreeSet<Integer> seen = new TreeSet<>();
            seen.add(0);                               // empty prefix sentinel
            int run = 0;
            for (int c = 0; c < n; c++) {
                run += colSum[c];
                // want smallest prefix_j with prefix_j >= run - k
                Integer lo = seen.ceiling(run - k);
                if (lo != null) best = Math.max(best, run - lo);
                seen.add(run);
            }
        }
    }
    return best;
}
```

```python
# python
# LC 363 - Max Sum of Rectangle No Larger Than K
# IDEA: fix row pair -> column sums -> 1D "max subarray sum <= k" via bisect on sorted prefixes
# time = O(m^2 * n * log n), space = O(n)
from bisect import bisect_left, insort

def maxSumSubmatrix(matrix, k):
    m, n = len(matrix), len(matrix[0])
    best = float('-inf')

    for top in range(m):
        col = [0] * n
        for bot in range(top, m):
            for c in range(n):
                col[c] += matrix[bot][c]

            # ---- 1D: max subarray sum <= k ----
            seen = [0]          # sorted prefix sums, 0 = empty prefix
            run = 0
            for c in range(n):
                run += col[c]
                idx = bisect_left(seen, run - k)   # smallest prefix >= run - k
                if idx < len(seen):
                    best = max(best, run - seen[idx])
                insort(seen, run)

    return best
```

> **換掉一維解法，就變成另一題。** 外層雙迴圈完全相同，只換內層那段：
> - **LC 1074**（統計和為 target 的子矩陣個數）→ 內層解法 = 模板 2 的雜湊表。
> - **LC 363**（最大和 ≤ k）→ 內層解法 = 有序集合 + `ceiling`，如上。
> - 如果 `n < m`，先轉置，讓平方那一項落在比較小的維度上。

### 模板 12：前綴 XOR ⭐⭐⭐⭐ — LC 1310

**核心想法：** XOR 是自己的反運算（`a ^ a = 0`），這正是減法對加法所做的事。所以整套前綴和工具只要把 `+`／`-` 換成 `^` 就能直接搬過來：

| | 和 | XOR |
|---|-----|-----|
| 建表 | `p[i+1] = p[i] + a[i]` | `p[i+1] = p[i] ^ a[i]` |
| 區間 `[l, r]` | `p[r+1] - p[l]` | `p[r+1] ^ p[l]` |
| 哨兵 | `p[0] = 0` | `p[0] = 0` |

```java
// java
// LC 1310 - XOR Queries of a Subarray
// IDEA: prefix XOR; xor(l..r) = p[r+1] ^ p[l]  (XOR is its own inverse)
// time = O(n + q), space = O(n)
public int[] xorQueries(int[] arr, int[][] queries) {
    int n = arr.length;
    int[] p = new int[n + 1];
    for (int i = 0; i < n; i++) p[i + 1] = p[i] ^ arr[i];

    int[] res = new int[queries.length];
    for (int i = 0; i < queries.length; i++) {
        res[i] = p[queries[i][1] + 1] ^ p[queries[i][0]];
    }
    return res;
}
```

```python
# python
# LC 1310 - XOR Queries of a Subarray
# time = O(n + q), space = O(n)
from itertools import accumulate

def xorQueries(arr, queries):
    p = list(accumulate(arr, lambda a, b: a ^ b, initial=0))
    return [p[r + 1] ^ p[l] for l, r in queries]
```

#### 變形 — **把 XOR 位元遮罩當成奇偶指紋**（LC 1915）

轉折在於：不是對**數值**做 XOR，而是對一個**每個字母佔一個 bit 的遮罩**做 XOR，讓遮罩的第 `c` 個 bit 代表「到目前為止字母 `c` 出現了奇數次」。於是子字串 `(j, i]` 中每個字母都出現偶數次，等價於 `mask[i] == mask[j]` — 就是模板 2 的雜湊表查詢，只是查的是遮罩而不是和。

```java
// java
// LC 1915 - Number of Wonderful Substrings
// IDEA: prefix XOR bitmask (parity fingerprint) + counting map, like Template 2
// time = O(10n), space = O(2^10)
public long wonderfulSubstrings(String word) {
    long[] cnt = new long[1 << 10];       // mask -> how many prefixes had it
    cnt[0] = 1;                           // empty prefix sentinel (== the {0:1} trick)
    int mask = 0;
    long res = 0;

    for (char ch : word.toCharArray()) {
        mask ^= 1 << (ch - 'a');          // flip this letter's parity bit

        res += cnt[mask];                 // all letters even  (mask ^ mask == 0)
        for (int b = 0; b < 10; b++) {
            res += cnt[mask ^ (1 << b)];  // exactly ONE letter odd
        }
        cnt[mask]++;
    }
    return res;
}
```

```python
# python
# LC 1915 - Number of Wonderful Substrings
# time = O(10n), space = O(2^10)
def wonderfulSubstrings(word):
    cnt = [0] * 1024
    cnt[0] = 1                       # empty prefix
    mask = res = 0

    for ch in word:
        mask ^= 1 << (ord(ch) - ord('a'))
        res += cnt[mask]             # 0 odd letters
        for b in range(10):
            res += cnt[mask ^ (1 << b)]   # exactly 1 odd letter
        cnt[mask] += 1

    return res
```

> **同一副骨架的其他口味：** LC 1738（Find Kth Largest XOR Coordinate Value）是模板 5 的排容原理，把 `+`／`-` 換成 `^`；LC 1829（Maximum XOR for Each Query）則是一路把元素剝掉的後綴 XOR。

### 模板 13：用雜湊表做稀疏差分陣列（掃描線）⭐⭐⭐⭐⭐ — LC 2021

**核心想法**：就是模板 4 的差分陣列，只是座標空間**大到（或負到）開不成陣列**。把陣列換成雜湊表，然後走訪 `sorted(keys)` 而不是 `range(n)`。

**什麼時候該用**：
- 座標很大（`-10^8 <= pos <= 10^8`）→ 開一個 `2 * 10^8` 大小的陣列會爆記憶體
- 座標可能是**負的** → 陣列索引得先做偏移
- 真正有意義的位置只有 `O(n)` 個 — 兩個相鄰事件之間的值不會變，所以**只有事件點可能是答案**

| | 陣列差分（模板 4） | 雜湊表差分（模板 13） |
|---|---|---|
| 儲存 | `[0] * (maxCoord + 2)` | `defaultdict(int)`，只有 2n 個 key |
| 空間 | O(座標範圍) | **O(n)** |
| 走訪 | `for i in range(n)` | `for k in sorted(d)` |
| 時間 | O(range + n) | **O(n log n)**（排序） |
| 負座標 | 需要偏移 | **直接可用** |

**`+1` 這個小技巧**：區間 `[p-r, p+r]` 是**閉區間**，所以「結束」標記要放在 `p + r + 1`，不是 `p + r`。這裡的差一錯誤是這個模式的頭號 bug。

```python
# python
# LC 2021 - Brightest Position on Street
# IDEA: hashmap difference array + line sweep over sorted event keys
# time = O(n log n), space = O(n)
from collections import defaultdict

class Solution:
    def brightestPosition(self, lights):
        events = defaultdict(int)

        # 1) mark events: +1 where coverage starts, -1 right AFTER it ends
        for p, r in lights:
            events[p - r] += 1          # starts illuminating at (p - r)
            events[p + r + 1] -= 1      # stops illuminating AFTER (p + r)  <-- note the +1

        max_brightness = curr = ans_pos = 0

        # 2) sweep positions in ascending order (sorted keys == the prefix sum walk)
        for pos in sorted(events.keys()):
            curr += events[pos]
            # STRICT `>` + ascending order => ties keep the SMALLEST position
            if curr > max_brightness:
                max_brightness = curr
                ans_pos = pos

        return ans_pos
```

```java
// java
// LC 2021 - Brightest Position on Street
// IDEA: TreeMap keeps keys sorted, so the sweep is just an in-order walk
// time = O(n log n), space = O(n)
public int brightestPosition(int[][] lights) {
    TreeMap<Integer, Integer> events = new TreeMap<>();
    for (int[] l : lights) {
        int p = l[0], r = l[1];
        events.merge(p - r, 1, Integer::sum);
        events.merge(p + r + 1, -1, Integer::sum);
    }

    int max = 0, curr = 0, ans = 0;
    for (Map.Entry<Integer, Integer> e : events.entrySet()) {  // already ascending
        curr += e.getValue();
        if (curr > max) {          // strict > => smallest position wins ties
            max = curr;
            ans = e.getKey();
        }
    }
    return ans;
}
```

#### 兩個值得背起來的細節

1. **為什麼 `sorted()` 會給出最小的答案** — 前綴和只有在事件由左往右套用時才正確。再配上**嚴格**的 `>`（不是 `>=`），第一個達到新最大值的位置會被記下來，之後同分的也不會覆蓋它。LC 2021 明確要求「最小的那一個」。
2. **為什麼雜湊表比排序過的事件清單好用** — 用 key 分桶，代表**同一個**座標上的所有 `+1`／`-1` 在掃描看到它們之前就已經合併好了。改用 `List<int[]>` 的話，你還得煩惱同一座標內部的排序先後；用 map 這問題根本不存在。

#### 同一個模式的其他題目

| 題目 | LC # | 事件是什麼 | 備註 |
|---------|------|---------------------|------|
| Brightest Position on Street | 2021 | 路燈 `[p-r, p+r]` | 閉區間 → `+r+1` |
| Meeting Rooms II | 253 | 開始 `+1`、結束 `-1` | 結束是**開區間** → 不用 `+1` |
| Car Pooling | 1094 | 上車 `+num`、下車 `-num` | 範圍固定且小 → 用陣列就好 |
| Corporate Flight Bookings | 1109 | 在 `[first, last]` 上 `+seats` | 閉區間 → `last+1` |
| Maximum Population Year | 1854 | 出生 `+1`、死亡 `-1` | 死亡年份是開區間 |
| My Calendar III | 732 | 預約區間 | 需要即時的 TreeMap（線上處理） |
| Describe the Painting | 1943 | 有顏色的線段 | map 的值 = 顏色總和 |

> **經驗法則**：座標範圍 ≤ 約 10^6 且非負 → 用一般陣列（模板 4）。否則，或座標為負 → 用 HashMap／TreeMap（模板 13）。

### 模板 9-13 — 題目索引

| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Maximum Points You Can Obtain from Cards | 1423 | 總和 − 最小固定視窗 | Medium | 模板 9 |
| Minimum Operations to Reduce X to Zero | 1658 | 總和 − 和為 total−x 的最長視窗 | Medium | 模板 9 變形 |
| Shortest Subarray with Sum at Least K | 862 | 前綴和 + 單調雙端佇列 | Hard | 模板 10 |
| Minimum Size Subarray Sum | 209 | 全正 → 單純滑動視窗 | Medium | 模板 10（對照組） |
| Max Sum of Rectangle No Larger Than K | 363 | 列對壓縮 + 有序集合 | Hard | 模板 11 |
| Number of Submatrices That Sum to Target | 1074 | 列對壓縮 + 雜湊表 | Hard | 模板 11 + 2 |
| XOR Queries of a Subarray | 1310 | 前綴 XOR 區間查詢 | Medium | 模板 12 |
| Number of Wonderful Substrings | 1915 | 前綴 XOR 位元遮罩奇偶 + 計數 | Medium | 模板 12 變形 |
| Find Kth Largest XOR Coordinate Value | 1738 | 二維前綴 XOR（排容原理） | Medium | 模板 12 + 5 |
| Number of Sub-arrays With Odd Sum | 1524 | 前綴奇偶計數（模板 3 取 k = 2） | Medium | 模板 3 變形 |
| Max Consecutive Ones III | 1004 | 0/1 轉換，最多 k 個 0 的最長視窗 | Medium | 模板 6／滑動視窗 |
| Number of Good Ways to Split a String | 1525 | 前綴相異數 vs 後綴相異數 | Medium | 模板 1（前綴 + 後綴） |
| Minimum Number of Operations to Move All Balls to Each Box | 1769 | 左→右與右→左的累計（數量、成本）掃描 | Medium | 模板 7 變形 |
| Plates Between Candles | 2055 | 前綴盤子數 + 最近蠟燭索引陣列 | Medium | 模板 1（離線查詢） |
| Find Good Days to Rob the Bank | 2100 | 前綴非遞增／後綴非遞減的連續長度 | Medium | 模板 1 變形 |
| Product of the Last K Numbers | 1352 | 前綴**乘積**（遇到 0 就重設清單） | Medium | 模板 1 變形 |
| Brightest Position on Street | 2021 | 雜湊表差分陣列 + 依 key 排序掃描 | Medium | 模板 13 |
| Describe the Painting | 1943 | 雜湊表差分陣列，值 = 顏色總和 | Medium | 模板 13 |
| My Calendar III | 732 | TreeMap 差分陣列，線上求最大重疊 | Hard | 模板 13 |

> **交叉參考：** 精確和的雜湊表補集寫法（`prefix_sum - k`）在 [`n_sum.md`](./n_sum.md) 裡也被寫成「在前綴和上做 2-sum」的模板 — 本文一律使用上面模板 2 的版本。

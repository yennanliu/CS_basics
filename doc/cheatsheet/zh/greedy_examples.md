# 貪婪 — 題目實作

> **範圍** — [greedy.md](./greedy.md) 背後的題解倉庫：十四題，依貪婪選擇的形狀分組 — 往前延伸可達範圍、累積後歸零、依頻率交錯、排序後取走，或邊掃邊定案。
> **另見**：[greedy.md](./greedy.md) — 母表：六個模板、交換論證、決策框架，以及貪婪失效的情況；[intervals.md](./intervals.md) — 自成一家的區間貪婪家族；[heap.md](./heap.md) — 頻率那組背後的資料結構；[monotonic_stack.md](./monotonic_stack.md) — LC 402 背後的理論；[dp.md](./dp.md) — 交換論證垮掉時該退回去用的東西。

## LeetCode 題目清單

- [Greedy](https://leetcode.com/problem-list/greedy/)

## 總覽

這裡是 [greedy.md](./greedy.md) 的長尾。母表放的是模板、證明技巧和決策框架；
這個檔案放的是*應用*它們的題目。

### 關鍵性質
- **複雜度**：排序之後是 O(n)，而排序本身是 O(n log n) — 有出入的地方會逐題標註
- **核心想法**：一個貪婪解就是一句話，加上一份證明說這句話是安全的。這裡是依那句話分組的，因為認出它才是全部的難處
- **什麼時候用**：當母表的決策框架已經告訴你交換論證成立之後 — 不成立的情況見 [When Greedy Fails](./greedy.md#when-greedy-fails--know-the-escape-hatch)


## 可達範圍與跳躍

### 1) Jump Game — LC 55 ⭐⭐⭐⭐⭐

> 追蹤能到達的最遠索引；如果目前索引超過它，回傳 false。

```java
// LC 55 - Jump Game
// IDEA: Greedy — track max reachable index; fail if current index exceeds it
// time = O(N), space = O(1)
public boolean canJump(int[] nums) {
    int maxReach = 0;
    for (int i = 0; i < nums.length; i++) {
        if (i > maxReach) return false;
        maxReach = Math.max(maxReach, i + nums[i]);
    }
    return true;
}
```

```python
# 055 Jump Game
# V0
class Solution(object):
    def canJump(self, nums):
        # edge case
        if not nums:
            return True
        cur = 0
        for i in range(len(nums)):
            if cur < i:
                return False
            cur = max(cur, i + nums[i])
        return True
```

### 2) Jump Game II — LC 45 — 跳躍視窗

> 一直擴張目前的跳躍視窗；碰到邊界時就跳一次，並把視窗往前推。

```java
// LC 45 - Jump Game II
// IDEA: Greedy — track current window end and farthest; jump when window end reached
// time = O(N), space = O(1)
public int jump(int[] nums) {
    int jumps = 0, curEnd = 0, farthest = 0;
    for (int i = 0; i < nums.length - 1; i++) {
        farthest = Math.max(farthest, i + nums[i]);
        if (i == curEnd) { jumps++; curEnd = farthest; }
    }
    return jumps;
}
```

```python
# 045 Jump Game II
# V0
# IDEA : GREEDY
"""
Steps:
    step 1) Initialize three integer variables: jumps to count the number of jumps, currentJumpEnd to mark the end of the range that we can jump to, and farthest to mark the farthest place that we can reach. Set each variable to zero
    step 2) terate over nums. Note that we exclude the last element from our iteration because as soon as we reach the last element, we do not need to jump anymore.
            - Update farthest to i + nums[i] if the latter is larger.
            - If we reach currentJumpEnd, it means we finished the current jump, and can begin checking the next jump by setting currentJumpEnd = farthest.
    step 3) return jumps
"""
class Solution:
    def jump(self, nums: List[int]) -> int:
            jumps = 0
            current_jump_end = 0
            farthest = 0
            for i in range(len(nums) - 1):
                # we continuously find the how far we can reach in the current jump
                farthest = max(farthest, i + nums[i])
                # if we have come to the end of the current jump,
                # we need to make another jump
                if i == current_jump_end:
                    jumps += 1
                    current_jump_end = farthest
            return jumps

# V1
# IDEA : GREEDY
# https://leetcode.com/problems/jump-game-ii/discuss/1672485/Python-Greedy
class Solution:
    def jump(self, nums: List[int]) -> int:
        l = r = res = farthest = 0
        while r < len(nums) - 1:
            for idx in range(l, r+1):
                farthest = max(farthest, idx + nums[idx])
            l = r+1
            r = farthest 
            res += 1
        return res
```

### 3) Minimum Number of Taps to Water a Garden — LC 1326 — 換皮的 LC 45


> **Template 4 / LC 45 的變形。** 轉折在於：區間給的是 `(center, radius)`，而不是「從索引 `i` 能走多遠」。**把每個區間依左端點分桶，只保留最大的右端點**，題目就塌回成一模一樣的 `curEnd` / `farthest` 跳躍視窗迴圈。

**關鍵想法**：`maxReach[l] = 所有以 l 為起點的區間中最大的右端點`。接著「最少水龍頭數」== 「從 0 跳到 n 的最少跳躍次數」— 每一輪都延伸到最遠可達點的貪婪是最佳解，因為貪婪在第 `t` 輪的邊界一定 `>=` 任何其他策略在第 `t` 輪的邊界（**貪婪永遠領先**）。

```text
n = 5, ranges = [3,4,1,1,0,0]
tap i covers [i-r, i+r] clipped to [0, n]:
  i=0 r=3 -> [0,3]
  i=1 r=4 -> [0,5]   <- maxReach[0] = 5
  i=2 r=1 -> [1,3]
  i=3 r=1 -> [2,4]
  i=4 r=0 -> [4,4]
maxReach = [5, 3, 4, 4, 4, 0]
i=0: farthest=5, i==curEnd(0) -> taps=1, curEnd=5  => covers the whole garden. Answer 1
```

```java
// java
// LC 1326 - Minimum Number of Taps to Open to Water a Garden
// IDEA: convert (center,radius) -> intervals, bucket by left end, then run Jump Game II window greedy
// time = O(N), space = O(N)
public int minTaps(int n, int[] ranges) {
    int[] maxReach = new int[n + 1];
    for (int i = 0; i <= n; i++) {
        int l = Math.max(0, i - ranges[i]);
        maxReach[l] = Math.max(maxReach[l], Math.min(n, i + ranges[i]));
    }
    int taps = 0, curEnd = 0, farthest = 0;
    for (int i = 0; i < n; i++) {
        farthest = Math.max(farthest, maxReach[i]);
        if (i == farthest) return -1;              // stuck: a gap the taps can't cross
        if (i == curEnd) { taps++; curEnd = farthest; }
    }
    return taps;
}
```

```python
# python
# LC 1326 - Minimum Number of Taps to Open to Water a Garden
# IDEA: (center,radius) -> intervals bucketed by left end, then Jump Game II window greedy
# time = O(N), space = O(N)
class Solution(object):
    def minTaps(self, n, ranges):
        reach = [0] * (n + 1)
        for i, r in enumerate(ranges):
            l = max(0, i - r)
            reach[l] = max(reach[l], min(n, i + r))

        taps, cur_end, farthest = 0, 0, 0
        for i in range(n):
            farthest = max(farthest, reach[i])
            if i == farthest:        # can't move past i -> impossible
                return -1
            if i == cur_end:         # window exhausted -> open one more tap
                taps += 1
                cur_end = farthest
        return taps
```

**相似題目：**
- LC 45 Jump Game II（基礎模板，第 2-2 節）
- LC 1024 Video Stitching（形狀**完全一樣**：clips `[start,end]` → 依 start 分桶，跳到 `time T`）
- LC 55 Jump Game（只問可不可行，第 2-1 節）

---

## 累積與歸零

### 4) Best Time to Buy and Sell Stock II — LC 122 — 每一段漲幅都吃下 ⭐⭐⭐⭐

> 把所有正的價差加起來 — 每個上漲日都買進賣出。

```java
// LC 122 - Best Time to Buy and Sell Stock II
// IDEA: Greedy — sum all positive consecutive differences
// time = O(N), space = O(1)
public int maxProfit(int[] prices) {
    int profit = 0;
    for (int i = 1; i < prices.length; i++)
        if (prices[i] > prices[i-1]) profit += prices[i] - prices[i-1];
    return profit;
}
```

```python
# 122 Best Time to Buy and Sell Stock II
class Solution:
    def maxProfit(self, prices):
        profit = 0
        for i in range(0,len(prices)-1):
            if prices[i+1] > prices[i]:
                # have to sale last stock, then buy a new one
                # and sum up the price difference into profit
                profit += prices[i+1] - prices[i]
        return profit

```

### 5) Gas Station — LC 134 — 出現赤字就重設起點 ⭐⭐⭐⭐

> 油箱變負的就把起點重設到下一站；有解的充要條件是總盈餘 ≥ 0。

```java
// LC 134 - Gas Station
// IDEA: Greedy — reset start when cumulative surplus goes negative; check total >= 0
// time = O(N), space = O(1)
public int canCompleteCircuit(int[] gas, int[] cost) {
    int total = 0, remain = 0, start = 0;
    for (int i = 0; i < gas.length; i++) {
        int diff = gas[i] - cost[i];
        total += diff; remain += diff;
        if (remain < 0) { start = i + 1; remain = 0; }
    }
    return total >= 0 ? start : -1;
}
```

```python
# 134 Gas Station
# V0
# IDEA : GREEDY
# IDEA : if sum(gas) - sum(cost) > 0, => THERE MUST BE A SOLUTION
# IDEA : since it's circular (symmetry), we can maintain "total" (e.g. total += gas[i] - cost[i]) of (gas[i], cost[i]) for each index as their "current sum"
class Solution(object):
    def canCompleteCircuit(self, gas, cost):
        start = remain = total = 0
        for i in range(len(gas)):
            remain += gas[i] - cost[i]
            total += gas[i] - cost[i]
            if remain < 0:
                remain = 0
                start = i + 1
        return -1 if total < 0 else start
```

### 6) Max Non-Overlapping Subarrays With Sum = Target — LC 1546

> 一找到合法的子陣列，就**立刻定案並清掉所有歷史** — 選最早結束的那個，能替後面的子陣列留下最多空間。

#### 「清空」這個貪婪想法

```python
# Check if there is a matching complement prefix sum
if (running_prefix - target) in seen_prefixes:
    cnt += 1

    # GREEDY RESET: Clear everything to prevent overlaps
    seen_prefixes.clear()
    running_prefix = 0
```

**為什麼清空／重設是對的（貪婪論證）：**

- 我們用的是**前綴和**：子陣列 `nums[i+1..j]` 的和是 `target`，等價於 `prefix[j] - prefix[i] == target`，也就是 `prefix[j] - target` 這個前綴和我們之前看過。
- 一偵測到這種匹配，就代表我們找到了一個在目前索引 `j` **盡可能早結束**的合法子陣列。
- **貪婪選擇性質：** 拿*最早結束*的合法子陣列，永遠不會比等一個更晚的差。早點結束就替後面的子陣列釋出最多剩餘元素 — 對總數只有好處，不會有壞處。（這就是區間排程「依結束時間排序」的同一種直覺，只是這裡的結束時間是邊跑邊發現的。）
- 為了保證**下一個**子陣列不會和剛拿的那個重疊，索引 `j` 以前（含 `j`）的一切都必須變成看不見。清掉 `seen_prefixes` 並把 `running_prefix = 0` 歸零，就是讓索引 `j` 變成新的「虛擬起點」— 之後的匹配只能用 `j` **之後**形成的補數。

> ⚠️ **順序很重要：** 繼續往下跑之前，你必須把基底的 `0` 加回去。V1-1 的寫法是重設時 `seen = set([0])`；V1-2 的寫法則是 `seen_prefixes.clear()` 之後（在 `if` 之外）緊接著 `seen_prefixes.add(running_prefix)`，而此時 `running_prefix` 已經是 `0` — 所以 `0` 重新進了集合。如果新集合裡沒有 `0`，那麼緊接在 `j` 後面開始的下一個子陣列就永遠偵測不到。

#### 視覺化

```text
nums = [-1, 3, 5, 1, 4, 2, -9],  target = 6

Legend: prefix = running prefix sum
        complement = prefix - target  (what we look for in `seen`)
        seen = prefix sums since the LAST reset

┌─────┬────────┬────────────┬────────┬─────┬──────────────────┐
│ num │ prefix │ complement │ found? │ cnt │ seen (this seg)  │
├─────┼────────┼────────────┼────────┼─────┼──────────────────┤
│ init│   0    │     -      │   -    │  0  │ {0}              │
│ -1  │  -1    │    -7      │  No    │  0  │ {0,-1}           │
│  3  │   2    │    -4      │  No    │  0  │ {0,-1,2}         │
│  5  │   7    │     1      │  No    │  0  │ {0,-1,2,7}       │
│  1  │   8    │     2      │  YES   │  1  │ RESET → {0}      │  ← subarray [5,1] sums to 6
│  4  │   4    │    -2      │  No    │  1  │ {0,4}            │
│  2  │   6    │     0      │  YES   │  2  │ RESET → {0}      │  ← subarray [4,2] sums to 6
│ -9  │  -9    │   -15      │  No    │  2  │ {0,-9}           │
└─────┴────────┴────────────┴────────┴─────┴──────────────────┘

Answer: cnt = 2   (subarrays [5,1] and [4,2] are non-overlapping)
```

```text
Why NOT wait for a bigger subarray?

Full array [3,5,1,4,2,-9] also sums to 6, but choosing it would
"swallow" indices that could otherwise host BOTH [5,1] and [4,2]:

  [-1,  3,  5,  1,  4,  2, -9]
            └──6──┘                ← take EARLY  →  1 subarray, rest still free
                     └──6──┘       ← then take another →  total = 2  ✅

  [-1,  3,  5,  1,  4,  2, -9]
        └──────── 6 ────────┘      ← greedy-late swallows everything → total = 1  ❌
```

```java
// java
// LC 1546 - Max Non-Overlapping Subarrays With Sum = Target
// IDEA: Greedy + prefix sum + hashset; on match, count and RESET history
// time = O(N), space = O(N)
public int maxNonOverlapping(int[] nums, int target) {
    Set<Integer> seen = new HashSet<>();
    seen.add(0);                 // base prefix
    int running = 0, cnt = 0;
    for (int num : nums) {
        running += num;
        if (seen.contains(running - target)) {
            cnt++;
            seen.clear();        // GREEDY RESET: prevent overlap
            running = 0;
        }
        seen.add(running);       // re-adds 0 right after a reset
    }
    return cnt;
}
```

```python
# python
# LC 1546 - Max Non-Overlapping Subarrays With Sum = Target
# IDEA: Greedy + prefix sum + hashset; on match, count and RESET history
# time = O(N), space = O(N)
class Solution(object):
    def maxNonOverlapping(self, nums, target):
        seen_prefixes = set([0])   # base prefix
        running_prefix = 0
        cnt = 0
        for num in nums:
            running_prefix += num
            if (running_prefix - target) in seen_prefixes:
                cnt += 1
                # GREEDY RESET: clear everything to prevent overlaps
                seen_prefixes.clear()
                running_prefix = 0
            seen_prefixes.add(running_prefix)  # re-adds 0 right after a reset
        return cnt
```

**相似題目：**
- LC 560 Subarray Sum Equals K（數出**所有**子陣列 — 不重設，保留完整歷史）
- LC 974 Subarray Sums Divisible by K（前綴和 + 對餘數做雜湊表）
- LC 134 Gas Station（累積總和變負時重設起點的貪婪）
- LC 435 Non-overlapping Intervals（同樣是最早結束的貪婪，只是區間一開始就全給你了）

### 7) Minimum Add to Make Parentheses Valid — LC 921 — 一個平衡計數器


> **模式**：由左往右掃一趟，用一個計數器記*還沒配對的左括號*。一個配不到左括號的 `)` **必須**當下就修掉 — 後面再多資訊也救不了它。

**為什麼貪婪是安全的**：把 `)` 和**最近**那個未配對的 `(` 配起來永遠不會比較差（括號配對永遠可以用交換論證「解交叉」）。而在索引 `i` 偵測到的缺口之後也補不回來，所以當下就計數是被逼的，不是一種選擇。

```java
// java
// LC 921 - Minimum Add to Make Parentheses Valid
// IDEA: GREEDY balance counter — unmatched ')' must be fixed now; leftover '(' fixed at the end
// time = O(N), space = O(1)
public int minAddToMakeValid(String s) {
    int open = 0, add = 0;
    for (char c : s.toCharArray()) {
        if (c == '(') open++;
        else if (open > 0) open--;   // match with the most recent '('
        else add++;                  // orphan ')' -> must insert a '(' right here
    }
    return add + open;               // + leftover unmatched '('
}
```

```python
# python
# LC 921 - Minimum Add to Make Parentheses Valid
# IDEA: GREEDY balance counter
# time = O(N), space = O(1)
class Solution(object):
    def minAddToMakeValid(self, s):
        open_, add = 0, 0
        for c in s:
            if c == '(':
                open_ += 1
            elif open_ > 0:
                open_ -= 1
            else:
                add += 1        # orphan ')'
        return add + open_
```

**變形 — LC 678 Valid Parenthesis String** *（轉折：`*` 是萬用字元，所以要追蹤的是可能的左括號數量**區間** `[lo, hi]`，而不是單一數字 — 這是帶區間的貪婪，它取代了一個 O(N²) 的 DP）*

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: GREEDY range — lo = min possible open count, hi = max possible; valid iff hi never < 0 and lo can hit 0
// time = O(N), space = O(1)
public boolean checkValidString(String s) {
    int lo = 0, hi = 0;
    for (char c : s.toCharArray()) {
        if (c == '(')      { lo++; hi++; }
        else if (c == ')') { lo--; hi--; }
        else               { lo--; hi++; }   // '*' -> ')' , '' , or '('
        if (hi < 0) return false;            // too many ')' even if every '*' is '('
        lo = Math.max(lo, 0);                // can't owe a negative number of opens
    }
    return lo == 0;                          // 0 is reachable in [lo, hi]
}
```

```python
# python
# LC 678 - Valid Parenthesis String
# IDEA: GREEDY range [lo, hi] of possible unmatched '(' counts
# time = O(N), space = O(1)
class Solution(object):
    def checkValidString(self, s):
        lo = hi = 0
        for c in s:
            if c == '(':
                lo, hi = lo + 1, hi + 1
            elif c == ')':
                lo, hi = lo - 1, hi - 1
            else:                       # '*'
                lo, hi = lo - 1, hi + 1
            if hi < 0:
                return False            # unrecoverable
            lo = max(lo, 0)             # clamp: open count can't go below 0
        return lo == 0
```

> **把 `lo` 夾在 0 為什麼就是全部的關鍵：** `lo` 是「每個 `*` 都當成 `)`」時的*最小*左括號數。既然 `*` 也可以當成空字串，我們隨時可以不再消耗 — 夾住這件事模擬的正是這個。忘了夾，`"(*)"` 這類輸入就會錯。

**相似題目：**
- LC 921 Minimum Add to Make Parentheses Valid（數要修幾次）
- LC 678 Valid Parenthesis String（萬用字元 → 區間計數器 `[lo, hi]`）
- LC 420 Strong Password Checker（同樣是「被逼的就修、能拖的就拖」的計數貪婪，只是分類討論重得多）

---

## 頻率與堆積交錯

### 8) Reorganize String — LC 767 — 用最大堆積交錯 ⭐⭐⭐

> 每次都挑出現次數最多、而且和上一個放下的字元不同的那個。

```java
// LC 767 - Reorganize String
// IDEA: Max-heap by frequency; always pick top that differs from last placed char
// time = O(N log K), space = O(K)  K = distinct chars
public String reorganizeString(String s) {
    int[] freq = new int[26];
    for (char c : s.toCharArray()) freq[c - 'a']++;
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
    for (int i = 0; i < 26; i++) if (freq[i] > 0) pq.offer(new int[]{i, freq[i]});
    StringBuilder sb = new StringBuilder();
    int[] prev = null;
    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        sb.append((char)('a' + curr[0]));
        if (prev != null) pq.offer(prev);
        curr[1]--;
        prev = curr[1] > 0 ? curr : null;
    }
    return sb.length() == s.length() ? sb.toString() : "";
}
```

```python
# LC 767. Reorganize String

# V0 
# IDEA : GREEDY + COUNTER
# IDEA : 
#  step 1) order exists count (big -> small)
#  step 2) select the element which is "most remaining" and DIFFERENT from last ans element and append such element to the end of ans
#  step 3) if can't find such element, return ""
class Solution(object):
    def reorganizeString(self, S):
        cnt = collections.Counter(S)
        # Be aware of it : ans = "#" -> not to have error in ans[-1] when first loop
        ans = '#'
        while cnt:
            stop = True
            for v, c in cnt.most_common():
                """
                NOTE !!! trick here

                1) we only compare last element in ans and current key (v), if they are different, then append
                2) we break at the end of each for loop -> MAKE SURE two adjacent characters are not the same.
                3) we use a flag "stop", to decide whether should stop while loop or not
                """
                if v != ans[-1]:
                    stop = False
                    ans += v
                    cnt[v] -= 1
                    if not cnt[v]:
                        del cnt[v]
                    """
                    NOTE !!!
                     -> we BREAK right after each op, since we want to get next NEW most common element from "updated" cnt.most_common()
                    """
                    break
            # Be aware of it : if there is no valid "v", then the while loop will break automatically at this condition (stop = True)
            if stop:
                break
        return ans[1:] if len(ans[1:]) == len(S) else ''
```

### 9) String Without AAA or BBB — LC 984 — 一個連續計數器

> 連續出現 2 個相同字元就強制換一個；否則永遠寫剩餘數量較多的那個字元。

```java
// LC 984 - String Without AAA or BBB
// IDEA: Greedy — write higher-count char unless 2 consecutive, then must switch
// time = O(A+B), space = O(1)
public String strWithout3a3b(int a, int b) {
    StringBuilder res = new StringBuilder();
    int continueA = 0;
    int continueB = 0;

    while (a > 0 || b > 0) {
        boolean writeA = false;

        // Priority 1: Must switch if 2 consecutive
        if (continueB == 2) {
            writeA = true;
        } else if (continueA == 2) {
            writeA = false;
        } else {
            // Priority 2: Greedy - write the one with more remaining
            writeA = a >= b;
        }

        if (writeA) {
            res.append("a");
            a--;
            continueA++;
            continueB = 0; // Reset other counter
        } else {
            res.append("b");
            b--;
            continueB++;
            continueA = 0; // Reset other counter
        }
    }
    return res.toString();
}

// V1: Using last 2 characters check (Editorial)
public String strWithout3a3b_v1(int A, int B) {
    StringBuilder ans = new StringBuilder();
    while (A > 0 || B > 0) {
        boolean writeA = false;
        int L = ans.length();
        // Check last 2 chars
        if (L >= 2 && ans.charAt(L-1) == ans.charAt(L-2)) {
            if (ans.charAt(L-1) == 'b') writeA = true;
        } else {
            if (A >= B) writeA = true;
        }

        if (writeA) { A--; ans.append('a'); }
        else { B--; ans.append('b'); }
    }
    return ans.toString();
}

// V2: PQ approach (similar to Reorganize String)
// Max heap: always pick char with highest remaining count
// If blocked by consecutive constraint, pick second highest
```

**相似題目：**
- LC 767: Reorganize String（不能有 2 個相鄰相同）
- LC 1405: Longest Happy String（在不出現 3 連的前提下最大化 a、b、c）
- LC 358: Rearrange String K Distance Apart（k 距離限制）

### 10) Task Scheduler — LC 621 — 閒置時間公式 ⭐⭐⭐⭐

> 最短時間 = max((maxFreq−1)*(n+1) + countOfMaxFreq, totalTasks)。

```java
// LC 621 - Task Scheduler
// IDEA: Greedy formula — (maxFreq-1)*(n+1) + #tasks_with_maxFreq; also can't be less than total
// time = O(N), space = O(1)
public int leastInterval(char[] tasks, int n) {
    int[] freq = new int[26];
    for (char t : tasks) freq[t - 'A']++;
    int maxFreq = 0;
    for (int f : freq) maxFreq = Math.max(maxFreq, f);
    int countMax = 0;
    for (int f : freq) if (f == maxFreq) countMax++;
    return Math.max(tasks.length, (maxFreq - 1) * (n + 1) + countMax);
}
```

```python
# LC 621. Task Scheduler
# V0
# pattern :
#    =============================================================================
#    -> task_time = (max_mission_count - 1) * (n + 1) + (number_of_max_mission)
#    =============================================================================
#   
#    -> Example 1) :
#    ->  AAAABBBBCCD, n=3
#    => THE EXPECTED tuned missions is like : ABXXABXXABXXAB
#    -> (4 - 1) * (3 + 1) + 2 = 14
#    -> 4 is the "how many missions the max mission has" (AAAA or BBBB)
#    -> 3 is n
#    -> 2 is "how many mission have max mission count" -> A and B. so it's 2
#    -> in sum,
#    -> (4 - 1) * (3 + 1) is for ABXXABXXABXX
#    -> and 2 is for AB
#
#   -> Example 2) :
#   -> AAABBB, n = 2
#   -> THE EXPECTED tuned missions is like : ABXABXAB
#   -> (3 - 1) * (2 + 1) + (2) = 8
class Solution(object):
    def leastInterval(self, tasks, n):
        count = collections.Counter(tasks)
        most = count.most_common()[0][1]
        num_most = len([i for i, v in count.items() if v == most])
        """
        example 1 : tasks = ["A","A","A","B","B","B"], n = 2
            -> we can split tasks as : A -> B -> idle -> A -> B -> idle -> A -> B
               -> 1) so there are 3-1 group. e.g. (A -> B -> idle), (A -> B -> idle)
                     and each group has (n+1) elements. e.g. (A -> B -> idle)
               -> 2) and the remain element is num_most. e.g. (A, B)
               -> 3) so total cnt = (3-1) * (2+1) + 2 = 8
    
        example 2 : tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
            -> we can split tasks as A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> idle -> idle -> A -> idle -> idle -> A
                -> 1) so there are 6-1 group. e.g. (A -> B -> C), (A -> D -> E), (A -> F -> G), (A -> idle -> idle), (A -> idle -> idle)
                      and each group has (n+1) elements. e.g. (A,B,C) .... (as above)
                -> 2) and the remain element is num_most. e.g. (A) 
                -> 3) so total cnt = (6-1)*(2+1) + 1 =  16
        """
        time = (most - 1) * (n + 1) + num_most
        return max(time, len(tasks)) # when idle slots go negative, just run all tasks sequentially
```

## 先排序，再取走

### 11) Maximum Units on a Truck — LC 1710 — 依單位價值排序

> 把箱型依「每箱單位數」由大到小排序；貪婪地先把高價值的箱子塞滿卡車。

```java
// LC 1710 - Maximum Units on a Truck
// IDEA: Sort by units descending; greedily load boxes until truck is full
// time = O(N log N), space = O(1)
public int maximumUnits(int[][] boxTypes, int truckSize) {
    Arrays.sort(boxTypes, (a, b) -> b[1] - a[1]);
    int total = 0;
    for (int[] box : boxTypes) {
        int take = Math.min(box[0], truckSize);
        total += take * box[1];
        truckSize -= take;
        if (truckSize == 0) break;
    }
    return total;
}
```

```python
# LC 1710. Maximum Units on a Truck
# V0
# IDEA : GREEDY + sorting
class Solution(object):
    def maximumUnits(self, boxTypes, truckSize):
        # boxTypes[i] = [numberOfBoxesi, numberOfUnitsPerBoxi]:
        # edge case
        if not boxTypes or not truckSize:
            return 0
        """
        NOTE : we sort on sort(key=lambda x : -x[1])

            -> if unit is bigger, we can get bigger aggregated result (n * unit)
        """
        boxTypes.sort(key=lambda x : -x[1])
        res = 0
        for n, unit in boxTypes:
            # case 1 : truckSize == 0, break for loop and return ans
            if truckSize == 0:
                break
            # case 2 : truckSize < n, we CAN'T add all n to truck, but CAN only add (truckSize * unit) amount to truck
            elif truckSize < n:
                res += (truckSize * unit)
                truckSize = 0
                break
            # case 3 : normal case, it's OK to put all (n * unit) to truck once
            else:      
                res += (n * unit)
                truckSize -= n
        return res

# V1
# IDEA : GREEDY
# https://leetcode.com/problems/maximum-units-on-a-truck/discuss/1045318/Python-solution
class Solution(object):
    def maximumUnits(self, boxTypes, truckSize):
        boxTypes.sort(key = lambda x: -x[1])
        n = len(boxTypes)
        result = 0
        i = 0
        while truckSize >= boxTypes[i][0]:
            result += boxTypes[i][1] * boxTypes[i][0]
            truckSize -= boxTypes[i][0]
            i += 1
            if i == n:
                return result
        result += truckSize * boxTypes[i][1]
        return result
```

### 12) Minimum Adjacent Swaps to Partition Array — LC 3994 — 群組 key + 索引加總


> 不要真的去模擬交換。**把每個元素重新標記成群組 key `0/1/2`，然後數逆序對** — 只有 3 種 key，用兩個累積計數器就能在 O(N) 一趟掃完。

#### 核心想法

每個元素都恰好屬於一個桶，而且這件事和其他元素**無關**：

```text
Group 0:  num < a          → must end up in the LEFT part
Group 1:  a <= num <= b    → must end up in the MIDDLE part
Group 2:  num > b          → must end up in the RIGHT part
```

有兩個觀察把它變成純計數：

1. **目標唯一。** 既然每個元素的群組是固定的，「好的」陣列就只是把原陣列的 key 排成非遞減的 `0...0 1...1 2...2`。沒有切點要挑 — 不用搜尋，也不用 DP。
2. **排序所需的最少相鄰交換次數 == 逆序對數量。** 一次相鄰交換最多修掉一個逆序對，而只要存在逆序對，就一定有某個*相鄰*對是逆的 — 所以這個下界剛好緊。

再加上貪婪的那一塊：**同一群組的元素彼此永遠不需要交換。** 它們的相對順序和合法性無關，所以最佳目標就是那個*穩定*的排法 — 這也是為什麼答案是單純的逆序對數量，而不是「所有排列取最小」。

**計數技巧：** 只有 3 種相異 key 時，你不需要合併排序也不需要樹狀陣列。由左往右掃，對每個元素加上**已經看過、key 嚴格更大**的數量：

```text
see Group 0  →  swaps += count_1 + count_2   (must cross every 1 and 2 to its left)
see Group 1  →  swaps += count_2             (must cross every 2 to its left)
see Group 2  →  swaps += 0                   (already right-most group)
```

注意 `count_0` 根本用不到 — 沒有東西需要跨過 Group 0。

#### 視覺化軌跡

```text
nums = [9, 7, 5, 3],  a = 4, b = 8
keys =  2  1  1  0        (9>8 → 2;  7,5 ∈ [4,8] → 1;  3<4 → 0)

┌─────┬─────┬───────────────────────┬───────┬─────────┬─────────┐
│ num │ key │ swaps +=              │ swaps │ count_1 │ count_2 │
├─────┼─────┼───────────────────────┼───────┼─────────┼─────────┤
│  9  │  2  │ 0                     │   0   │    0    │    1    │
│  7  │  1  │ count_2 = 1           │   1   │    1    │    1    │
│  5  │  1  │ count_2 = 1           │   2   │    2    │    1    │
│  3  │  0  │ count_1+count_2 = 2+1 │   5   │    2    │    1    │
└─────┴─────┴───────────────────────┴───────┴─────────┴─────────┘

Answer = 5   (matches the 5-swap sequence in the problem statement)

Why 5? The `3` alone must cross 9, 7, 5 → 3 swaps.
Each of 7 and 5 must cross the 9 → 2 swaps.  Total 3 + 2 = 5.
Note 7 and 5 never swap with each other — same group, order doesn't matter.
```

#### 模式

```python
# python
# LC 3994 - Minimum Adjacent Swaps to Partition Array
# IDEA: GREEDY — map to group key 0/1/2, count inversions with running counters
# time = O(N), space = O(1)
class Solution(object):
    def minAdjacentSwaps(self, nums, a, b):
        MOD = 10**9 + 7
        count_1 = 0      # of Group 1 seen so far
        count_2 = 0      # of Group 2 seen so far
        swaps = 0

        for num in nums:
            if num < a:
                # Group 0: must jump over every Group 1 and Group 2 to its left
                swaps = (swaps + count_1 + count_2) % MOD
            elif num <= b:
                # Group 1: must jump over every Group 2 to its left
                swaps = (swaps + count_2) % MOD
                count_1 += 1
            else:
                # Group 2: already in the right-most group -> 0 swaps
                count_2 += 1

        return swaps
```

```java
// java
// LC 3994 - Minimum Adjacent Swaps to Partition Array
// IDEA: GREEDY — map to group key 0/1/2, count inversions with running counters
// time = O(N), space = O(1)
public int minAdjacentSwaps(int[] nums, int a, int b) {
    final int MOD = 1_000_000_007;
    long count1 = 0, count2 = 0, swaps = 0;   // NOTE: long — raw answer can reach ~N^2/2 (5e9)

    for (int num : nums) {
        if (num < a) {
            swaps = (swaps + count1 + count2) % MOD;
        } else if (num <= b) {
            swaps = (swaps + count2) % MOD;
            count1++;
        } else {
            count2++;
        }
    }
    return (int) swaps;
}
```

> ⚠️ **溢位陷阱：** `N = 1e5` 時，真正的交換次數可能到 ~`N²/2 = 5e9` — 會讓 32 位元的 `int` 溢位。Python 沒問題，但 Java/C++ 要用 `long` 累加。在迴圈裡取 `% MOD` 在這裡是安全的，因為我們只做**加法**（不會對取模後的值做比較或減法）。

**推廣到 K 個群組：** 同一套掃描適用於任意數量的有序桶 — 每個 key 維護一個累計計數，加上目前為止看過的所有**嚴格更大** key 的計數總和：

```python
# K ordered groups -> O(N*K) time, O(K) space
cnt = [0] * K
swaps = 0
for key in keys:                       # key in 0..K-1
    swaps += sum(cnt[key + 1:])        # everything larger sitting to the left
    cnt[key] += 1
```

當 `K` 很大時（例如 key 就是值本身），就放棄計數器，改用**樹狀陣列／合併排序**做 O(N log N) 的逆序對計數 — 見 LC 315 / LC 493。

**相似題目：**
| 題目 | LC # | 關係 |
|---------|------|--------------|
| Minimum Adjacent Swaps to Partition Array | 3994 | 3 種群組 key、2 個累計計數器、O(N) |
| Separate Black and White Balls | 2938 | **兩群組的情況** — 對每個 `0`，`swaps += 目前為止看過的 1 的數量` |
| Sort Colors | 75 | 同樣是 0/1/2 分類，但交換是**任意的** → 荷蘭國旗三路分割，不是逆序對 |
| Count of Smaller Numbers After Self | 315 | key 不是 O(1) 種時的通用逆序對計數 → 樹狀陣列 |
| Reverse Pairs | 493 | 用合併排序做逆序對計數（配對條件改過） |
| Minimum Adjacent Swaps to Make a Valid Array | 2340 | 相鄰交換 = 索引距離；小心交叉的修正項 |
| Minimum Swaps to Arrange a Binary Grid | 1536 | 貪婪地做相鄰**列**交換，數要動幾次才滿足「後綴為零」的要求 |
| Couples Holding Hands | 765 | 一樣是最少交換，但位置是**任意的** → 貪婪／併查集；和只能相鄰交換做對照 |

**辨識訊號：** *「最少**相鄰**交換」* + *「元素落在少數幾個有序類別裡」* → 重新標記成 key，然後數逆序對。如果交換**不限**於相鄰，那就是另一個問題（環分解／荷蘭國旗），而且逆序對數量會高估。

## 邊掃邊把答案蓋出來

### 13) Partition Labels — LC 763 — 在最後一次出現的位置切開 ⭐⭐⭐⭐⭐


> **模式**：先算出每個元素的**最後出現位置**，然後掃一趟，維護一個滾動邊界 `end = max(end, last[x])`。每當 `i == end`，就切一刀。不用排序 — 「結束時間」是邊跑邊發現的。

**關鍵想法**：一個字元不能被切到兩段去，所以任何合法的切點都必須 `>= last[c]`，對目前看過的**每個** `c` 都要成立。`end` 正是這個下界。

**為什麼貪婪是安全的（交換論證）**：假設某個最佳解 `OPT` 把第一刀切在索引 `j > end`（它不可能切在 `end` 之前）。改成切在 `end`，等於把 `OPT` 的第一段拆成 `[start..end]` 加上 `[end+1..j]`，而兩半都仍然合法（沒有字元跨過 `end`）。所以貪婪切法得到的段數**至少一樣多** — 不會更少。對剩下的後綴做歸納 ⇒ 貪婪是最佳解。

```text
s = "ababcbacadefegdehijhklij"
       a...........a      -> last['a'] = 8
     b.....b              -> last['b'] = 5
         c...c            -> last['c'] = 7

i:  0 1 2 3 4 5 6 7 8 ...
    a b a b c b a c a
end 8 8 8 8 8 8 8 8 8   <- i == end at i=8  => cut, size 9
```

```java
// java
// LC 763 - Partition Labels
// IDEA: GREEDY — record last index of each char; extend a running boundary, cut when i == end
// time = O(N), space = O(1)   (26-slot table)
public List<Integer> partitionLabels(String s) {
    int[] last = new int[26];
    for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;

    List<Integer> res = new ArrayList<>();
    int start = 0, end = 0;
    for (int i = 0; i < s.length(); i++) {
        end = Math.max(end, last[s.charAt(i) - 'a']);   // boundary must cover this char
        if (i == end) {                                 // nothing pending -> safe to cut
            res.add(end - start + 1);
            start = i + 1;
        }
    }
    return res;
}
```

```python
# python
# LC 763 - Partition Labels
# IDEA: GREEDY — record last index of each char; extend a running boundary, cut when i == end
# time = O(N), space = O(1)
class Solution(object):
    def partitionLabels(self, s):
        last = {c: i for i, c in enumerate(s)}   # last occurrence of every char
        res, start, end = [], 0, 0
        for i, c in enumerate(s):
            end = max(end, last[c])              # boundary must cover this char
            if i == end:                         # nothing pending -> safe to cut
                res.append(end - start + 1)
                start = i + 1
        return res
```

**變形 — LC 769 Max Chunks To Make Sorted** *（轉折：值是 `0..n-1` 的一個排列，所以邊界改成滾動的**前綴最大值**，不需要「最後出現位置」表）*

```java
// java
// LC 769 - Max Chunks To Make Sorted   (arr is a permutation of 0..n-1)
// IDEA: chunk can close at i iff max(arr[0..i]) == i  (everything <= i already placed)
// time = O(N), space = O(1)
public int maxChunksToSorted(int[] arr) {
    int chunks = 0, mx = 0;
    for (int i = 0; i < arr.length; i++) {
        mx = Math.max(mx, arr[i]);
        if (mx == i) chunks++;
    }
    return chunks;
}
```

```python
# python
# LC 769 - Max Chunks To Make Sorted
# time = O(N), space = O(1)
class Solution(object):
    def maxChunksToSorted(self, arr):
        chunks, mx = 0, 0
        for i, v in enumerate(arr):
            mx = max(mx, v)
            if mx == i:
                chunks += 1
        return chunks
```

> **LC 768 Max Chunks To Make Sorted II**（有重複值／值任意）會讓 `mx == i` 這個捷徑失效 — 改成比較滾動的 `prefixMax[i] <= suffixMin[i+1]`。

**相似題目：**
| 題目 | LC # | 關係 |
|---------|------|--------------|
| Partition Labels | 763 | 邊界 = 最後出現位置的最大值 |
| Max Chunks To Make Sorted | 769 | 邊界 = 前綴最大值，值是 `0..n-1` |
| Max Chunks To Make Sorted II | 768 | 有重複值 → prefixMax vs suffixMin |
| Merge Intervals | 56 | 同樣是「延伸滾動終點、出現空隙就收尾」的掃描 |
| Max Non-Overlapping Subarrays Sum=Target | 1546 | 同樣的最早結束貪婪（見 2-9） |

---

### 14) Remove K Digits — LC 402 — 用單調堆疊貪婪地蓋答案 ⭐⭐⭐


> **模式**：在一個**堆疊**上由左往右蓋答案；推入 `c` 之前，只要堆疊頂端比 `c` 「更差」**而且**你還有刪除額度，就一直彈出。幾乎每一題*「刪掉／挑出 k 個字元，讓字串最小／最大」*背後都是這台貪婪引擎。

**關鍵想法**：結果是按**字典序**比較的，所以**最左邊**的那個數字支配它後面的所有數字。因此修好一個早期的位置，永遠比後面拿到的任何好處值錢。

**為什麼貪婪是安全的（交換論證）**：如果 `d[i] > d[i+1]`，那麼刪掉 `d[i]` 會嚴格讓數字變小（縮短後的前綴現在以更小的數字開頭），而刪掉後面任何東西都只會讓那個較大的數字待在更高位。所以任何保留 `d[i]` 的最佳解，都能換成刪掉它的解而不會變差。每個數字最多被推入／彈出一次 ⇒ O(N)。

```text
num = "1432219", k = 3

c=1  stack=[1]
c=4  stack=[1,4]
c=3  4>3, pop 4 (k=2)      stack=[1,3]
c=2  3>2, pop 3 (k=1)      stack=[1,2]
c=2  2>2? no               stack=[1,2,2]
c=1  2>1, pop 2 (k=0)      stack=[1,2,1]
c=9  budget spent          stack=[1,2,1,9]  -> "1219"
```

```java
// java
// LC 402 - Remove K Digits
// IDEA: GREEDY + MONOTONIC (non-decreasing) STACK — pop bigger digits while budget remains
// time = O(N), space = O(N)
public String removeKdigits(String num, int k) {
    StringBuilder st = new StringBuilder();          // acts as the stack
    for (char c : num.toCharArray()) {
        while (k > 0 && st.length() > 0 && st.charAt(st.length() - 1) > c) {
            st.deleteCharAt(st.length() - 1);        // a bigger digit sits in a MORE significant slot
            k--;
        }
        st.append(c);
    }
    st.setLength(Math.max(0, st.length() - k));      // leftover budget -> chop from the tail (already increasing)

    int i = 0;
    while (i < st.length() && st.charAt(i) == '0') i++;   // strip leading zeros
    String res = st.substring(i);
    return res.isEmpty() ? "0" : res;
}
```

```python
# python
# LC 402 - Remove K Digits
# IDEA: GREEDY + MONOTONIC (non-decreasing) STACK — pop bigger digits while budget remains
# time = O(N), space = O(N)
class Solution(object):
    def removeKdigits(self, num, k):
        st = []
        for c in num:
            while k and st and st[-1] > c:
                st.pop()          # bigger digit in a more significant slot -> drop it
                k -= 1
            st.append(c)
        if k:
            st = st[:-k]          # leftover budget -> chop the tail (stack is non-decreasing)
        return ''.join(st).lstrip('0') or '0'
```

> ⚠️ **兩個經典陷阱：** (1) 迴圈結束後 `k` 還有剩 — 此時堆疊已經是非遞減的，所以該砍的是*尾巴*，那裡影響最大；(2) 前導零，以及結果為空的情況 → 回傳 `"0"`。

**變形 — LC 316 Remove Duplicate Letters** *（轉折：沒有固定額度 — 只有當某個字元**後面還會再出現**時才能彈出它，而且每個字元必須恰好出現一次）*

```java
// java
// LC 316 - Remove Duplicate Letters
// IDEA: monotonic stack, but pop only if the top char occurs again later; skip chars already in stack
// time = O(N), space = O(1)  (26 slots)
public String removeDuplicateLetters(String s) {
    int[] last = new int[26];
    for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;
    boolean[] inStack = new boolean[26];
    StringBuilder st = new StringBuilder();

    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (inStack[c - 'a']) continue;                       // already placed, and placing it earlier is better
        while (st.length() > 0 && st.charAt(st.length() - 1) > c
                && last[st.charAt(st.length() - 1) - 'a'] > i) {   // safe: it comes back later
            inStack[st.charAt(st.length() - 1) - 'a'] = false;
            st.deleteCharAt(st.length() - 1);
        }
        st.append(c);
        inStack[c - 'a'] = true;
    }
    return st.toString();
}
```

```python
# python
# LC 316 - Remove Duplicate Letters
# IDEA: monotonic stack; pop top only if it appears again later; skip chars already in stack
# time = O(N), space = O(1)
class Solution(object):
    def removeDuplicateLetters(self, s):
        last = {c: i for i, c in enumerate(s)}
        st, in_st = [], set()
        for i, c in enumerate(s):
            if c in in_st:
                continue
            while st and st[-1] > c and last[st[-1]] > i:   # top reappears later -> safe to drop
                in_st.remove(st.pop())
            st.append(c)
            in_st.add(c)
        return ''.join(st)
```

**變形 — LC 738 Monotone Increasing Digits** *（轉折：你不能刪除 — 只能**借位**：遇到下降時把左邊那位減一，再把它後面全部灌成 `9`；由右往左掃，這樣 `100 → 99` 這種連鎖才處理得到）*

```java
// java
// LC 738 - Monotone Increasing Digits
// IDEA: GREEDY right->left — on d[i-1] > d[i], borrow 1 from d[i-1] and mark everything from i as '9'
// time = O(log N), space = O(log N)
public int monotoneIncreasingDigits(int n) {
    char[] d = String.valueOf(n).toCharArray();
    int mark = d.length;                     // first index that becomes '9'
    for (int i = d.length - 1; i > 0; i--) {
        if (d[i - 1] > d[i]) { d[i - 1]--; mark = i; }   // right->left so 3-3-2 / 1-0-0 cascade correctly
    }
    for (int i = mark; i < d.length; i++) d[i] = '9';    // maximize the suffix
    return Integer.parseInt(new String(d));
}
```

```python
# python
# LC 738 - Monotone Increasing Digits
# IDEA: GREEDY right->left — borrow on a descent, then flood the suffix with '9'
# time = O(log N), space = O(log N)
class Solution(object):
    def monotoneIncreasingDigits(self, n):
        d = list(str(n))
        mark = len(d)
        for i in range(len(d) - 1, 0, -1):
            if d[i - 1] > d[i]:
                d[i - 1] = str(int(d[i - 1]) - 1)
                mark = i
        for i in range(mark, len(d)):
            d[i] = '9'
        return int(''.join(d))
```

**相似題目：**
| 題目 | LC # | 和 LC 402 的差別 |
|---------|------|-----------------|
| Remove K Digits | 402 | 固定額度 `k`，要最小化 |
| Remove Duplicate Letters | 316 | 額度 =「該字元後面還會再出現」；每個字元必須出現一次 |
| Monotone Increasing Digits | 738 | 借位取代刪除；由右往左掃 |
| Maximum Swap | 670 | 只能交換**一次** → 對每個數字，和它右邊最大數字的*最後一次*出現位置交換 |
| Create Maximum Number | 321 | 用這個堆疊從 2 個陣列各挑 `k` 個，再貪婪合併 |
| Largest Number | 179 | 不是堆疊 — 是貪婪的**自訂比較器** `a+b vs b+a` |

---

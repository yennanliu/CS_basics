# 貪婪演算法

> **範圍** — 每一步選當下最好的，以及證明這樣做安全的交換論證——區間排程、跳躍遊戲、任務分配——外加怎麼看出貪婪會失敗、必須改用 DP 的時機。
> **另見**：[greedy_examples.md](./greedy_examples.md) — 撐起這些模板的十四道題目詳解；[intervals.md](./intervals.md) — 區間類的貪婪家族；[heap.md](./heap.md) — 需要反覆取出當前最佳值的貪婪；[dp.md](./dp.md) — 交換論證垮掉時的退路；[sort.md](./sort.md) — 幾乎每個貪婪都從排序開始。

## LeetCode 題目清單

- [Greedy](https://leetcode.com/problem-list/greedy/)

## 概觀
**貪婪演算法**在每一步選擇局部最佳解，期望最後能得到全域最佳解。它在每個決策點挑當下可得的最好選項，而且不回頭檢討先前的決定。

### 關鍵性質
- **時間複雜度**：通常是 O(n)，需要排序時是 O(nlogn)
- **空間複雜度**：視題目而定，O(1) 到 O(n)
- **核心想法**：每一步都做局部最佳的選擇
- **什麼時候用**：具備貪婪選擇性質與最佳子結構的問題
- **限制**：不保證得到全域最佳解

### 核心特徵
- **貪婪選擇性質**：局部最佳能導向全域最佳
- **最佳子結構**：最佳解包含子問題的最佳解
- **不回溯**：一旦做了選擇，就不再重新考慮
- **必須證明**：得證明貪婪確實會得到最佳解

### 貪婪 vs 其他解法
- **貪婪 vs DP**：貪婪選擇成立時，貪婪就是被最佳化過的 DP
- **貪婪 vs 暴力**：快很多，但可能錯過最佳解
- **演進路線**：暴力 → DP → 貪婪（在適用的情況下）

## 模板與演算法

### 模板比較表
| 模板類型 | 適用情境 | 排序依據 | 什麼時候用 |
|---------------|----------|-------------|-------------|
| **區間** | 挑出互不重疊的區間 | 結束時間 | 會議室、活動安排 |
| **優先佇列** | 動態挑選 | 值／頻率 | 任務排程 |
| **雙指標** | 配對／匹配 | 視情況 | 陣列操作 |
| **累積** | 累加和／累乘積 | 不需排序 | 股票、加油站 |
| **跳躍／可達** | 追蹤位置 | 不需排序 | 跳躍遊戲 |

### 通用貪婪模板
```python
def greedy_solution(items):
    # Step 1: Sort or prepare data structure
    items.sort(key=lambda x: x[criterion])
    
    # Step 2: Initialize greedy choice tracking
    result = initial_value
    current_state = initial_state
    
    # Step 3: Make greedy choices
    for item in items:
        if can_select(item, current_state):
            result = update_result(result, item)
            current_state = update_state(current_state, item)
    
    return result
```

### 模板 1：區間排程 — LC 435 ⭐⭐⭐⭐⭐
```python
def interval_scheduling(intervals):
    """Select maximum non-overlapping intervals"""
    if not intervals:
        return 0
    
    # Sort by end time
    intervals.sort(key=lambda x: x[1])
    
    count = 1
    end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        if intervals[i][0] >= end:
            count += 1
            end = intervals[i][1]
    
    return count
```

### 模板 2：搭配堆積的活動選擇 — LC 621 ⭐⭐⭐⭐
```python
import heapq
import collections

def activity_selection_heap(tasks):
    """Select activities using priority queue"""
    # Count frequency or priority
    freq = collections.Counter(tasks)
    
    # Max heap (negate for min heap)
    heap = [(-count, task) for task, count in freq.items()]
    heapq.heapify(heap)
    
    result = []
    while heap:
        count1, task1 = heapq.heappop(heap)
        result.append(task1)
        
        if heap:
            count2, task2 = heapq.heappop(heap)
            result.append(task2)
            
            # Add back if still available
            if count1 < -1:
                heapq.heappush(heap, (count1 + 1, task1))
            if count2 < -1:
                heapq.heappush(heap, (count2 + 1, task2))
    
    return result
```

### 模板 3：貪婪累積 — LC 122 ⭐⭐⭐⭐
```python
def greedy_accumulation(prices):
    """Accumulate positive differences (stock trading)"""
    profit = 0
    
    for i in range(1, len(prices)):
        # Greedy: take profit whenever possible
        if prices[i] > prices[i-1]:
            profit += prices[i] - prices[i-1]
    
    return profit
```

### 模板 4：跳躍遊戲模式 — LC 55 ⭐⭐⭐⭐⭐
```python
def jump_game(nums):
    """Check if can reach end"""
    max_reach = 0
    
    for i in range(len(nums)):
        if i > max_reach:
            return False
        max_reach = max(max_reach, i + nums[i])
        if max_reach >= len(nums) - 1:
            return True
    
    return True

def jump_game_min_jumps(nums):
    """Minimum jumps to reach end"""
    jumps = 0
    current_end = 0
    farthest = 0
    
    for i in range(len(nums) - 1):
        farthest = max(farthest, i + nums[i])
        
        if i == current_end:
            jumps += 1
            current_end = farthest
    
    return jumps
```

### 模板 5：字串重組 — LC 767 ⭐⭐⭐
```python
def reorganize_string(s):
    """Reorganize string so no adjacent chars are same"""
    from collections import Counter
    import heapq
    
    # Count frequencies
    count = Counter(s)
    
    # Check if possible
    max_count = max(count.values())
    if max_count > (len(s) + 1) // 2:
        return ""
    
    # Max heap of frequencies
    heap = [(-cnt, char) for char, cnt in count.items()]
    heapq.heapify(heap)
    
    result = []
    prev_count, prev_char = 0, ''
    
    while heap:
        count, char = heapq.heappop(heap)
        result.append(char)
        
        # Add previous back to heap
        if prev_count < 0:
            heapq.heappush(heap, (prev_count, prev_char))
        
        # Update previous
        prev_count = count + 1
        prev_char = char
    
    return ''.join(result)
```

### 模板 6：分數背包
```python
def fractional_knapsack(items, capacity):
    """Greedy knapsack allowing fractions"""
    # items = [(value, weight), ...]
    # Sort by value/weight ratio
    items.sort(key=lambda x: x[0]/x[1], reverse=True)
    
    total_value = 0
    remaining = capacity
    
    for value, weight in items:
        if weight <= remaining:
            total_value += value
            remaining -= weight
        else:
            # Take fraction
            total_value += value * (remaining / weight)
            break
    
    return total_value
```

## 依模式分類的題目

### **區間問題**
| 題目 | LC # | 關鍵技巧 | Difficulty |
|---------|------|---------------|------------|
| Non-overlapping Intervals | 435 | 依結束時間排序 | Medium |
| Minimum Arrows to Burst Balloons | 452 | 依結束時間排序 | Medium |
| Maximum Length of Pair Chain | 646 | 依結束時間排序 | Medium |
| Merge Intervals | 56 | 依起始時間排序 | Medium |
| Meeting Rooms II | 253 | 排序＋堆積 | Medium |
| Interval List Intersections | 986 | 雙指標 | Medium |

### **活動選擇問題**
| 題目 | LC # | 關鍵技巧 | Difficulty |
|---------|------|---------------|------------|
| Task Scheduler | 621 | 頻率統計 | Medium |
| Maximum Events Attended | 1353 | 排序＋堆積 | Medium |
| Course Schedule III | 630 | 排序＋堆積 | Hard |
| IPO | 502 | 兩個堆積 | Hard |

### **股票交易問題**
| 題目 | LC # | 關鍵技巧 | Difficulty |
|---------|------|---------------|------------|
| Buy Sell Stock II | 122 | 累加每一段漲幅 | Easy |
| Gas Station | 134 | 環狀陣列 | Medium |
| Best Time with Fee | 714 | 狀態追蹤 | Medium |
| Container With Most Water | 11 | 雙指標 | Medium |

### **跳躍遊戲問題**
| 題目 | LC # | 關鍵技巧 | Difficulty |
|---------|------|---------------|------------|
| Jump Game | 55 | 追蹤最遠可達位置 | Medium |
| Jump Game II | 45 | 最少跳躍次數 | Medium |
| Jump Game III | 1306 | BFS/DFS | Medium |
| Reach a Number | 754 | 數學＋貪婪 | Medium |

### **字串重組問題**
| 題目 | LC # | 關鍵技巧 | Difficulty |
|---------|------|---------------|------------|
| Reorganize String | 767 | 最大堆積 | Medium |
| String Without AAA or BBB | 984 | 貪婪＋計數追蹤 | Medium |
| Rearrange K Distance Apart | 358 | 堆積＋佇列 | Hard |
| Task Scheduler | 621 | 頻率 | Medium |
| Longest Happy String | 1405 | 堆積貪婪 | Medium |

### **其他貪婪問題**
| 題目 | LC # | 關鍵技巧 | Difficulty |
|---------|------|---------------|------------|
| Candy | 135 | 兩趟掃描 | Hard |
| Assign Cookies | 455 | 雙指標 | Easy |
| Maximum Units on Truck | 1710 | 依價值排序 | Easy |
| Boats to Save People | 881 | 雙指標 | Medium |
| Minimum Cost to Connect Sticks | 1167 | 最小堆積 | Medium |
| Max Non-Overlapping Subarrays Sum=Target | 1546 | 前綴和＋貪婪重置 | Medium |

## 決策框架

### 貪婪失敗的時候 — 要知道逃生門在哪

> 面試官很愛出那種*看起來*像貪婪的題目。能講出反例並馬上換路走，價值不輸把貪婪寫出來。

| 題目 | LC # | 誘人的貪婪想法 | 為什麼會爛掉 | 真正可行的做法 |
|---------|------|---------------------|---------------|---------------------|
| Split Array Largest Sum | 410 | 「累積和一超過 `total/k` 就切一刀」 | 那個門檻事先根本不知道；局部塞滿的一段會逼出一個超大的尾段 | **對答案二分搜尋**＋一個貪婪的*可行性檢查*（`最大和 <= X 時，能不能切成 <= k 段？`）。貪婪在這裡是 O(N) 的驗證器，不是最佳化器。O(N log(sum)) |
| Wildcard Matching | 44 | 「從左到右逐字比對，`*` 需要多少就吃多少」 | `*` 吃太快，後面的字面字元就沒東西可配 | 要嘛用 **DP** `O(S*P)`，要嘛用**帶回溯錨點**的雙指標貪婪（記住上一個 `*` 的位置，不匹配就倒回去） |
| Best Time to Buy/Sell with Fee | 714 | 「把每個正的差值加起來」（LC 122 那一套） | 手續費是按每筆交易收的，所以小漲幅可能反而是負的 | **DP 狀態機** `hold / cash` — 見上面的股票交易表 |
| 0/1 Knapsack | — | 「依價值／重量比排序」 | 物品不能切開（反例見下面的分數 vs 0/1 背包表） | **DP** `O(nW)` |

**辨識的經驗法則：**
- 目標如果是*「最小化最大值」*／*「最大化最小值」* → 貪婪通常會變成**對答案二分搜尋裡的那個單調判斷式**，而不是一個獨立的演算法（LC 410 是最典型的例子）。
- 如果一個選擇**之後可以划算地反悔**（手續費、上限、能撤回的截止期限）→ 解法往往是**基於堆積的「反悔」貪婪**，而不是 DP。見 [`priority_queue.md`](priority_queue.md) 裡的 *greedy with regret* 模板 — LC 871 Minimum Number of Refueling Stops、LC 630 Course Schedule III、LC 1642 Furthest Building You Can Reach。

---

### 其他高頻的貪婪一句話心法

| 題目 | LC # | 一句話講完的貪婪 | Difficulty |
|---------|------|------------------------|------------|
| Valid Palindrome II | 680 | 雙指標；第一次不匹配時，試著跳過**任一邊**再檢查剩下的部分 | Easy |
| Minimum Domino Rotations For Equal Row | 1007 | 目標值只可能是 `tops[0]` 或 `bottoms[0]`——只要驗這兩個候選 | Medium |
| Increasing Triplet Subsequence | 334 | 記住目前看過的最小值與次小值；出現第三個把兩者都比下去就是 true | Medium |
| Largest Number | 179 | 用自訂比較器 `a+b` vs `b+a`（字串串接）排序 | Medium |
| Hand of Straights / Divide Array in Sets of K | 846 / 1296 | 每一組永遠從**剩下最小的**那張牌開始 | Medium |
| Minimum Increment to Make Array Unique | 945 | 排序後，把每個元素推到 `max(x, prev+1)` | Medium |
| Can Place Flowers | 605 | 由左往右掃，看到第一個合法的位置就種下去 | Easy |


### 模式選擇策略

```text
Greedy Algorithm Selection Flowchart:

1. Can the problem be solved greedily?
   ├── Does local optimal lead to global optimal? → YES → Use Greedy
   ├── Can you prove greedy correctness? → YES → Use Greedy
   └── NO to both → Use DP or other approach

2. What type of greedy pattern?
   ├── Selection from sorted items → Interval/Activity Selection
   ├── Maximize/minimize at each step → Accumulation Pattern
   ├── Dynamic selection → Priority Queue/Heap
   ├── Position/reach tracking → Jump Game Pattern
   └── Pairing/matching → Two Pointers

3. How to make greedy choice?
   ├── Sort by what criterion?
   │   ├── End time → Interval scheduling
   │   ├── Start time → Merge intervals
   │   ├── Value/weight ratio → Knapsack
   │   └── Custom criterion → Problem specific
   └── No sorting needed → Direct iteration

4. Common greedy strategies:
   ├── Always take the best available
   ├── Never make a choice that blocks future options
   ├── Minimize waste/maximize efficiency
   └── Balance resources evenly
```

### 貪婪 vs 動態規劃

| 判準 | 用貪婪 | 用 DP | 例子 |
|-----------|------------|--------|---------|
| 具備貪婪選擇性質 | ✅ | ❌ | 活動選擇 |
| 需要所有子問題的解 | ❌ | ✅ | 0/1 Knapsack |
| 能證明最佳性 | ✅ | - | Huffman coding |
| 子問題會重疊 | ❌ | ✅ | Fibonacci |
| 選擇規則很單純 | ✅ | ❌ | 分數背包 |

---

## 貪婪的證明與周邊演算法 — 交換論證、MST

### 證明模板：交換論證
要驗證一個貪婪選擇，就證明把貪婪挑的那個換成任何其他選擇，結果都不會變好。

```text
1. Assume optimal solution OPT differs from greedy solution G at some step.
2. Show you can swap OPT's choice at that step with G's choice without making things worse.
3. Repeat until OPT == G → greedy is optimal.
```

常見的交換論證題目：LC 435（Non-overlapping Intervals）、LC 452（Burst Balloons）、工作排程。

### 最小生成樹（MST）

**Kruskal**（把邊排序，配併查集）：
```python
def kruskal(n, edges):
    edges.sort(key=lambda x: x[2])  # sort by weight
    parent = list(range(n))

    def find(x):
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]

    def union(a, b):
        a, b = find(a), find(b)
        if a == b: return False
        parent[a] = b
        return True

    mst_cost = 0
    for u, v, w in edges:
        if union(u, v):
            mst_cost += w
    return mst_cost
```

**Prim**（優先佇列，適合稠密圖）：
```python
import heapq
from collections import defaultdict

def prim(n, edges):
    graph = defaultdict(list)
    for u, v, w in edges:
        graph[u].append((w, v))
        graph[v].append((w, u))

    visited = set()
    heap = [(0, 0)]  # (cost, node)
    total = 0
    while heap and len(visited) < n:
        cost, node = heapq.heappop(heap)
        if node in visited: continue
        visited.add(node)
        total += cost
        for w, nei in graph[node]:
            if nei not in visited:
                heapq.heappush(heap, (w, nei))
    return total
```

| 演算法 | Time | 適合 |
|-----------|------|---------|
| Kruskal | O(E log E) | 稀疏圖 |
| Prim (heap) | O(E log V) | 稠密圖 |

### 帶權區間排程 — LC 1235
區間帶有權重／利潤時，光靠貪婪會失敗——要用 DP＋二分搜尋。
```python
# LC 1235 Maximum Profit in Job Scheduling
import bisect

def jobScheduling(startTime, endTime, profit):
    jobs = sorted(zip(startTime, endTime, profit), key=lambda x: x[1])
    dp = [(0, 0)]  # (end_time, max_profit)
    for s, e, p in jobs:
        # Find last job that ends <= s
        i = bisect.bisect_right(dp, (s, float('inf'))) - 1
        new_profit = dp[i][1] + p
        if new_profit > dp[-1][1]:
            dp.append((e, new_profit))
    return dp[-1][1]
```

### 分數背包 vs 0/1 背包
| 性質 | 分數背包 | 0/1 背包 |
|----------|-----------|-----|
| 物品能否切開 | 可以 | 不行 |
| 演算法 | 貪婪（依價值／重量排序） | DP |
| Time | O(n log n) | O(nW) |
| 貪婪可行嗎？ | 可行 | 不可行 |

**為什麼 0/1 背包不能用貪婪**：反例：物品 [(value=6, w=4), (value=5, w=3), (value=5, w=3)]，容量 = 6。貪婪挑比值最高的（item1，ratio=1.5）→ 只拿到 6。DP 挑 item2+item3 → 拿到 10。

### 面試技巧 — 貪婪
| 訊號 | 對應模式 |
|--------|---------|
| 「連接起來的最小成本」 | MST（Kruskal/Prim） |
| 「最大化互不重疊的區間數」 | 依結束時間排序 |
| 「帶冷卻時間的任務排程」 | 數學公式或最大堆積 |
| 「物品可以切成小份」 | 依價值／重量比排序 |
| 「證明這個貪婪是對的」 | 交換論證 |
| 「這裡貪婪會給錯答案」 | 改用 DP |

## 總結與速查

### 複雜度速查
| 模式 | 時間複雜度 | 空間複雜度 | 瓶頸 |
|---------|-----------------|------------------|------------|
| 區間排程 | O(nlogn) | O(1) | 排序 |
| 用堆積挑選 | O(nlogn) | O(n) | 堆積操作 |
| 雙指標 | O(n) 或 O(nlogn) | O(1) | 需要時的排序 |
| 直接累積 | O(n) | O(1) | 單趟掃描 |
| 跳躍遊戲 | O(n) | O(1) | 單趟掃描 |

### 排序依據指南
```python
# Interval problems
intervals.sort(key=lambda x: x[1])  # By end time
intervals.sort(key=lambda x: x[0])  # By start time

# Value optimization
items.sort(key=lambda x: x.value/x.weight, reverse=True)  # By ratio

# Custom priority
tasks.sort(key=lambda x: (x.deadline, -x.profit))  # Multi-criteria
```

### 常見的貪婪套路

#### **交換論證**
```python
# Prove: Swapping any two elements won't improve result
def exchange_argument_proof(arr):
    # If swapping arr[i] and arr[j] doesn't improve,
    # then current order is optimal
    pass
```

#### **貪婪永遠領先**
```python
# Prove: Greedy solution is at least as good at each step
def stays_ahead_proof(greedy, other):
    # Show: greedy[i] >= other[i] for all i
    pass
```

#### **擬陣理論**
```python
# System has matroid structure if:
# 1. Hereditary: Subset of feasible is feasible
# 2. Exchange: Can always extend smaller feasible set
```

### 解題步驟
1. **判斷有沒有貪婪的可能**：找找看有沒有最佳子結構
2. **定義貪婪選擇**：每一步要挑什麼
3. **證明正確性**：交換論證或貪婪永遠領先
4. **有效率地實作**：通常需要先排序
5. **處理邊界情況**：空輸入、只有一個元素
6. **用例子驗證**：把貪婪選擇實際跑一遍

### 常見錯誤與訣竅

**🚫 常見錯誤：**
- 沒證明就假設貪婪成立
- 排序依據挑錯
- 沒把所有邊界情況考慮進去
- 忘記處理平手的情況
- 漏掉全域限制的檢查

**✅ 最佳實務：**
- 一定先確認貪婪性質成立
- 從小例子開始
- 主動想反例
- 需要動態挑選時就用堆積
- 拿邊界情況測一遍

### 證明技巧

#### **交換論證範例**
```python
# Prove interval scheduling is optimal
# If we swap any interval in greedy solution with another,
# we either get same or fewer intervals
```

#### **貪婪永遠領先範例**
```python
# Prove jump game solution is minimal
# At each position, greedy reaches at least as far
```

### 面試技巧
1. **辨識模式**：留意題目裡關於排序或挑選的暗示
2. **從例子下手**：先把小案例走一遍
3. **講清楚假設**：說明貪婪在什麼前提下適用
4. **被問到就證明**：用交換論證或貪婪永遠領先
5. **把程式寫乾淨**：貪婪的程式碼通常很短
6. **再最佳化**：想想能不能用堆積把複雜度壓下來

### 經典貪婪問題
- **活動選擇**：挑出最多互不重疊的活動
- **Huffman 編碼**：建出最佳的前綴碼
- **Kruskal MST**：挑權重最小的邊
- **Dijkstra（戴克斯特拉）**：挑距離最小的頂點
- **分數背包**：優先拿比值最高的

### 相關主題
- **動態規劃**：貪婪行不通時
- **二分搜尋**：處理最佳化問題
- **堆積／優先佇列**：需要動態挑選時
- **排序**：貪婪的常見前置步驟
- **圖論演算法**：很多都建立在貪婪上（MST、最短路徑）

## 題目詳解

十四道題目放在 **[greedy_examples.md](./greedy_examples.md)**，是按貪婪選擇的*形狀*分組，
而不是按主題——因為你在面試現場真正要認出來的正是形狀：

| 分組 | 你做的那個選擇 | 題目 |
|---|---|---|
| [Reach & jump](./greedy_examples.md#reach--jump) | 一路延伸最遠可達點，非跳不可時才跳 | LC 55, 45, 1326 |
| [Accumulate & reset](./greedy_examples.md#accumulate--reset) | 有賺就拿；累計值一變負就歸零重來 | LC 122, 134, 1546, 921 |
| [Frequency & heap interleaving](./greedy_examples.md#frequency--heap-interleaving) | 永遠放當下合法且出現次數最多的那個 | LC 767, 984, 621 |
| [Sort, then take](./greedy_examples.md#sort-then-take) | 依關鍵比值排序，然後照順序拿 | LC 1710, 3994 |
| [Build while scanning](./greedy_examples.md#build-the-answer-while-scanning) | 一個字元或一條邊界確定不會再變，就馬上定案 | LC 763, 402 |

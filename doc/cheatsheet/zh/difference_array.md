# 差分陣列

> **範圍** — O(1) 區間更新、O(n) 重建 — 前綴和的反操作。
> **另見**：[prefix_sum.md](./prefix_sum.md) — 正向的那一半；[scanning_line.md](./scanning_line.md) — 座標稀疏時改用事件排序的版本；[intervals.md](./intervals.md) — 合併而不是累加。

## LeetCode 題目清單

- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)
- [Array](https://leetcode.com/problem-list/array/)

## 總覽
**差分陣列**是一種在陣列上高效做區間更新的技巧。與其一個一個元素更新（每次 O(n)），我們可以用 O(1) 完成一次區間更新，最後再用 O(n) 把整個陣列還原出來。

### 關鍵性質
- **時間複雜度**：
  - 區間更新：O(1)
  - 建差分陣列：O(n)
  - 還原原陣列：O(n)
  - 多次更新：m 次更新是 O(m)
- **空間複雜度**：O(n)，存差分陣列
- **核心想法**：存相鄰元素的差值，讓區間更新變得很便宜
- **什麼時候用**：多次區間更新、區間修改、訂位系統、資源分配

### 參考資料
- [Difference Array Visualization](https://www.geeksforgeeks.org/difference-array-range-update-query-o1/)
- [fucking algorithm - Difference Array](https://labuladong.online/algo/data-structure/diff-array/)
- [Prefix Sum vs Difference Array](https://leetcode.com/discuss/general-discussion/1093346/)

## 題型分類

### **模式 1：基本區間更新**
- **說明**：對某個區間內的所有元素加上／減去一個值
- **辨識訊號**：「更新區間 [i, j]」、「增加 val」、「修改區間」
- **範例**：LC 370、LC 1109、LC 1893
- **模板**：用基本差分陣列模板

### **模式 2：資源分配**
- **說明**：追蹤各區間上的資源用量
- **辨識訊號**：「訂位」、「容量」、「重疊區間」
- **範例**：LC 1094、LC 731、LC 732
- **模板**：用資源追蹤模板

### **模式 3：事件時間軸**
- **說明**：處理發生在不同時間點的事件
- **辨識訊號**：「開始／結束時間」、「排程」、「時間軸」
- **範例**：LC 253、LC 1851、LC 2021
- **模板**：用事件處理模板

### **模式 4：二維差分陣列**
- **說明**：在二維矩陣上做區間更新
- **辨識訊號**：「矩形更新」、「二維區間修改」
- **範例**：LC 2132、LC 2536
- **模板**：用二維差分陣列模板

### **模式 5：覆蓋圖 + 貪婪填補（差分陣列 → 前綴和 → 貪婪）**
- **說明**：先用差分陣列算出哪些位置已經被*既有*的區間蓋到，再貪婪地把剩下沒蓋到的洞補起來
- **辨識訊號**：「最少要幾個 X 才能蓋滿」、「已覆蓋的區間給你了，加最少的新區間」、「照亮／塗滿／補齊整條線」
- **範例**：LC 3964（Minimum Lights to Illuminate a Road）
- **模板**：用覆蓋 + 貪婪模板（模板 6）

### **模式 6：區間更新 + 區間 MAX 查詢（差分陣列**不夠用**的時候）** ⭐⭐⭐⭐
- **說明**：形狀跟差分陣列的「蓋一段區間」一樣，但每個新區間得先**讀出**該區間目前的最大值，再用推導出來的值**覆寫**整段區間
- **辨識訊號**：「每次落下／插入之後，回報目前為止的最大值」、「疊在已經在那裡的東西上面」，更新的值**取決於同一段區間上的查詢結果**
- **差分陣列為什麼會垮**（這正是面試時要講的重點）：
  - 差分陣列是**離線**的 — 先把所有區間都蓋完，最後掃一次前綴和。這裡的更新是**線上**的：第 *i* 步的答案在第 *i+1* 步套用之前就要拿到
  - 差分陣列只累加**總和**。`max` **沒有反元素**，所以不存在能「抵銷一次 max」的 `diff[end + 1] -= val`
  - 升級路線：`差分陣列 → 前綴和` 處理*區間加值 + 最後整個陣列讀一次*；`線段樹 + 懶標記` 處理*區間指派／加值 + 任意時刻做 max/min/sum 查詢*
- **範例**：LC 699（Falling Squares）
- **模板**：用線段樹 + 懶標記指派（模板 7）— 這就是下面模板比較表裡承諾的那一列 **「懶標記傳遞」**

## 模板與演算法

### 模板比較表
| 模板類型 | 適用情境 | 更新時間 | 查詢時間 | 空間 | 什麼時候用 |
|---------------|----------|-------------|------------|-------|-------------|
| **基本差分** | 區間更新 | O(1) | O(n) 重建 | O(n) | 多次區間更新 |
| **搭配前綴和** | 區間更新 + 查詢 | O(1) | O(1) | O(n) | 又要更新又要查詢 |
| **二維差分** | 矩陣區間更新 | O(1) | O(mn) 重建 | O(mn) | 二維區間更新 |
| **懶標記傳遞** | 動態查詢 | O(log n) | O(log n) | O(n) | 更新之間夾雜大量查詢 |

### 通用差分陣列模板
```python
def difference_array_template(nums, updates):
    """
    Universal template for difference array problems
    nums: original array
    updates: list of [start, end, value] operations
    """
    n = len(nums)
    # Build difference array
    diff = [0] * n
    diff[0] = nums[0]
    for i in range(1, n):
        diff[i] = nums[i] - nums[i-1]
    
    # Apply range updates in O(1) each
    for start, end, val in updates:
        diff[start] += val
        if end + 1 < n:
            diff[end + 1] -= val
    
    # Reconstruct final array
    result = [0] * n
    result[0] = diff[0]
    for i in range(1, n):
        result[i] = result[i-1] + diff[i]
    
    return result
```

### 模板 1：基本差分陣列
```python
class DifferenceArray:
    def __init__(self, nums):
        """Initialize difference array from original array"""
        self.n = len(nums)
        self.diff = [0] * self.n
        
        # Build difference array
        self.diff[0] = nums[0]
        for i in range(1, self.n):
            self.diff[i] = nums[i] - nums[i-1]
    
    def update(self, start, end, val):
        """Add val to all elements in range [start, end] in O(1)"""
        self.diff[start] += val
        if end + 1 < self.n:
            self.diff[end + 1] -= val
    
    def get_result(self):
        """Reconstruct the final array in O(n)"""
        result = [0] * self.n
        result[0] = self.diff[0]
        for i in range(1, self.n):
            result[i] = result[i-1] + self.diff[i]
        return result
```

### 模板 2：資源分配
```python
def check_resource_allocation(intervals, capacity, resource_field=2):
    """
    Check if resource allocation is valid
    intervals: [[start, end, resource_needed], ...]
    capacity: maximum available resource
    """
    # Find the range of positions
    max_pos = max(interval[1] for interval in intervals) + 1
    diff = [0] * max_pos
    
    # Apply all resource allocations
    for interval in intervals:
        start, end, resource = interval[0], interval[1], interval[resource_field]
        diff[start] += resource
        if end + 1 < max_pos:
            diff[end + 1] -= resource
    
    # Check if any position exceeds capacity
    current = 0
    for i in range(max_pos):
        current += diff[i]
        if current > capacity:
            return False
    
    return True
```

### 模板 3：事件時間軸
```python
def process_events(events):
    """
    Process events on a timeline
    events: [[start_time, end_time, value], ...]
    Returns: timeline with accumulated values
    """
    if not events:
        return []
    
    # Create timeline
    max_time = max(e[1] for e in events) + 1
    timeline = [0] * max_time
    
    # Process each event
    for start, end, value in events:
        timeline[start] += value
        if end + 1 < max_time:
            timeline[end + 1] -= value
    
    # Calculate prefix sum to get actual values
    for i in range(1, max_time):
        timeline[i] += timeline[i-1]
    
    return timeline
```

### 模板 4：二維差分陣列
```python
class DifferenceArray2D:
    def __init__(self, matrix):
        """Initialize 2D difference array"""
        self.m, self.n = len(matrix), len(matrix[0])
        self.diff = [[0] * self.n for _ in range(self.m)]
        
        # Build 2D difference array
        for i in range(self.m):
            for j in range(self.n):
                self.diff[i][j] = matrix[i][j]
                if i > 0:
                    self.diff[i][j] -= matrix[i-1][j]
                if j > 0:
                    self.diff[i][j] -= matrix[i][j-1]
                if i > 0 and j > 0:
                    self.diff[i][j] += matrix[i-1][j-1]
    
    def update(self, r1, c1, r2, c2, val):
        """Add val to all elements in rectangle [r1,c1] to [r2,c2]"""
        self.diff[r1][c1] += val
        if r2 + 1 < self.m:
            self.diff[r2 + 1][c1] -= val
        if c2 + 1 < self.n:
            self.diff[r1][c2 + 1] -= val
        if r2 + 1 < self.m and c2 + 1 < self.n:
            self.diff[r2 + 1][c2 + 1] += val
    
    def get_result(self):
        """Reconstruct the final 2D array"""
        result = [[0] * self.n for _ in range(self.m)]
        
        for i in range(self.m):
            for j in range(self.n):
                result[i][j] = self.diff[i][j]
                if i > 0:
                    result[i][j] += result[i-1][j]
                if j > 0:
                    result[i][j] += result[i][j-1]
                if i > 0 and j > 0:
                    result[i][j] -= result[i-1][j-1]
        
        return result
```

### 模板 5：搭配座標壓縮的最佳化版
```python
def difference_array_compressed(updates):
    """
    Handle large coordinate space with compression
    updates: [[start, end, value], ...]
    """
    # Collect all unique points
    points = set()
    for start, end, _ in updates:
        points.add(start)
        points.add(end + 1)
    
    # Sort and create mapping
    sorted_points = sorted(points)
    point_to_idx = {p: i for i, p in enumerate(sorted_points)}
    
    # Apply updates on compressed coordinates
    n = len(sorted_points)
    diff = [0] * n
    
    for start, end, val in updates:
        start_idx = point_to_idx[start]
        end_idx = point_to_idx.get(end + 1, n)
        diff[start_idx] += val
        if end_idx < n:
            diff[end_idx] -= val
    
    # Calculate values at each point
    for i in range(1, n):
        diff[i] += diff[i-1]
    
    # Return results with original coordinates
    result = {}
    for i, point in enumerate(sorted_points[:-1]):  # Exclude the last dummy point
        if diff[i] != 0:
            result[point] = diff[i]
    
    return result
```

### 模板 6：覆蓋圖 + 貪婪填補（LC 3964）
```python
# python — LC 3964 Minimum Lights to Illuminate a Road
#
# core idea (3 phases):
#   1. DIFF ARRAY  -> mark every range an existing bulb covers in O(1) each
#   2. PREFIX SUM  -> turn diff into a `covered[i]` map (0 == dark spot)
#   3. GREEDY      -> walk left->right; at each dark spot drop 1 new bulb and
#                     jump ahead 3 (a new bulb at i+1 lights i, i+1, i+2)
#
# time  = O(n)   diff build + prefix sum + single greedy pass
# space = O(n)   difference array + coverage map
def minLights(lights):
    n = len(lights)

    # 1) difference array (size n+1 so `right+1` never overflows)
    diff = [0] * (n + 1)
    for i, v in enumerate(lights):
        if v > 0:
            left  = max(0, i - v)
            right = min(n - 1, i + v)
            diff[left]      += 1        # +1 where coverage starts
            diff[right + 1] -= 1        # -1 right AFTER it ends

    # 2) prefix sum -> covered[i] > 0 means position i is already lit
    covered = [0] * n
    running = 0
    for i in range(n):
        running += diff[i]
        covered[i] = running

    # 3) greedy fill of the dark gaps
    ans = 0
    i = 0
    while i < n:
        if covered[i] == 0:             # dark spot found
            ans += 1
            # best move: put bulb at i+1 -> covers i, i+1, i+2 -> jump 3
            i += 3
        else:
            i += 1                      # already lit -> next position
    return ans
```

> **另一種貪婪寫法（數暗區長度）：** 不用「一次跳 3」的迴圈，改成累加每一段極大暗區的長度，每段加 `(run + 2) // 3` 顆燈泡（用燈泡寬度 3 做上取整除法）。同樣是 O(n)，而且不用玩索引 — 見解答檔裡的 V1-2 / V2。

### 模板 7：區間指派 + 區間 Max（線段樹搭配懶標記傳遞） — LC 699

**什麼時候該用它而不是差分陣列：** 只要一次區間更新需要同一段區間*當下*的聚合值，或者更新**之間**就要給出答案，就是它。差分陣列 = 離線 + 可加總；這個 = 線上 + 任何滿足結合律的聚合。

**先做座標壓縮。** 端點可以大到 `10^8`，但總共只有 `2n` 個，所以把排序去重後的端點映射成索引，讓葉節點 `i` 代表**左閉右開的基本區段** `[xs[i], xs[i+1])`。落在 `[l, l+size)` 的方塊會變成葉節點範圍 `[idx[l], idx[l+size] - 1]` — 輸入左閉右開、葉節點範圍閉區間，正好就是差分陣列用 `end + 1` 這招在防的那個差一錯誤。

```java
// java
// LC 699 - Falling Squares
// IDEA: coordinate-compress the 2n endpoints, then a segment tree with a LAZY ASSIGN tag
//       supports "max over [l,r]" and "set [l,r] = h" in O(log n) each.
//       Per square: h = query(l, r) + size; assign(l, r, h); answer = running max.
// time = O(n log n), space = O(n)
class Solution {
    private int[] mx, lz;   // mx = max height in node's range, lz = pending "assign" tag (0 = none)

    public List<Integer> fallingSquares(int[][] positions) {
        // 1) coordinate compression of all endpoints
        TreeSet<Integer> set = new TreeSet<>();
        for (int[] p : positions) { set.add(p[0]); set.add(p[0] + p[1]); }
        List<Integer> xs = new ArrayList<>(set);
        Map<Integer, Integer> idx = new HashMap<>();
        for (int i = 0; i < xs.size(); i++) idx.put(xs.get(i), i);
        int m = xs.size() - 1;                 // # of elementary segments

        mx = new int[4 * Math.max(m, 1)];
        lz = new int[4 * Math.max(m, 1)];

        List<Integer> res = new ArrayList<>();
        int best = 0;
        for (int[] p : positions) {
            // NOTE !!! square covers [l, l+size) -> closed leaf range [a, b]
            int a = idx.get(p[0]);
            int b = idx.get(p[0] + p[1]) - 1;
            int cur = query(1, 0, m - 1, a, b);        // tallest thing already under it
            update(1, 0, m - 1, a, b, cur + p[1]);     // it lands ON TOP -> assign, not add
            best = Math.max(best, cur + p[1]);
            res.add(best);
        }
        return res;
    }

    // push the pending assign tag down to both children
    private void push(int node) {
        if (lz[node] != 0) {
            for (int c = 2 * node; c <= 2 * node + 1; c++) {
                mx[c] = lz[node];
                lz[c] = lz[node];
            }
            lz[node] = 0;
        }
    }

    private void update(int node, int lo, int hi, int l, int r, int val) {
        if (r < lo || hi < l) return;                       // disjoint
        if (l <= lo && hi <= r) {                           // fully covered -> tag & stop
            mx[node] = val; lz[node] = val; return;
        }
        push(node);
        int mid = (lo + hi) >>> 1;
        update(2 * node, lo, mid, l, r, val);
        update(2 * node + 1, mid + 1, hi, l, r, val);
        mx[node] = Math.max(mx[2 * node], mx[2 * node + 1]);
    }

    private int query(int node, int lo, int hi, int l, int r) {
        if (r < lo || hi < l) return 0;
        if (l <= lo && hi <= r) return mx[node];
        push(node);
        int mid = (lo + hi) >>> 1;
        return Math.max(query(2 * node, lo, mid, l, r),
                        query(2 * node + 1, mid + 1, hi, l, r));
    }
}
```

```python
# python
# LC 699 - Falling Squares
# IDEA: same as java — compress endpoints, segment tree with a lazy ASSIGN tag.
#       max is not invertible, so the diff-array "+val at l, -val at r+1" trick does not apply.
# time = O(n log n), space = O(n)
class Solution(object):
    def fallingSquares(self, positions):
        # 1) coordinate compression
        xs = sorted({x for l, s in positions for x in (l, l + s)})
        idx = {x: i for i, x in enumerate(xs)}
        m = len(xs) - 1                       # # of elementary segments

        size = 4 * max(m, 1)
        mx = [0] * size                       # max height in node's range
        lz = [0] * size                       # pending assign tag (0 = none)

        def push(node):
            if lz[node]:
                for c in (2 * node, 2 * node + 1):
                    mx[c] = lz[node]
                    lz[c] = lz[node]
                lz[node] = 0

        def update(node, lo, hi, l, r, val):
            if r < lo or hi < l:              # disjoint
                return
            if l <= lo and hi <= r:           # fully covered -> tag & stop
                mx[node] = val
                lz[node] = val
                return
            push(node)
            mid = (lo + hi) // 2
            update(2 * node, lo, mid, l, r, val)
            update(2 * node + 1, mid + 1, hi, l, r, val)
            mx[node] = max(mx[2 * node], mx[2 * node + 1])

        def query(node, lo, hi, l, r):
            if r < lo or hi < l:
                return 0
            if l <= lo and hi <= r:
                return mx[node]
            push(node)
            mid = (lo + hi) // 2
            return max(query(2 * node, lo, mid, l, r),
                       query(2 * node + 1, mid + 1, hi, l, r))

        res, best = [], 0
        for l, s in positions:
            # square covers [l, l+s) -> closed leaf range [a, b]
            a, b = idx[l], idx[l + s] - 1
            cur = query(1, 0, m - 1, a, b)    # tallest thing already under it
            update(1, 0, m - 1, a, b, cur + s)  # lands ON TOP -> assign
            best = max(best, cur + s)
            res.append(best)
        return res
```

> **變形 — 「排一排就好」的 O(n²) 保底解（面試時先講這個，再談最佳化）。**
> 訣竅：完全不用樹，每個方塊只留一個 `height[i]`。處理方塊 `i` 時掃過所有更早的方塊，取其中跟它**重疊**者的最大高度（`l < r2 and l2 < r` — 嚴格不等，因為區間是左閉右開，只是碰到邊緣*不算*疊上去）。LC 699 的 `n <= 1000`，這樣寫會過。
> ```python
> # python — LC 699 brute force
> # time = O(n^2), space = O(n)
> def fallingSquares(positions):
>     n = len(positions)
>     h, res, best = [0] * n, [], 0
>     for i, (l, s) in enumerate(positions):
>         r = l + s
>         base = 0
>         for j in range(i):
>             l2, s2 = positions[j]
>             if l < l2 + s2 and l2 < r:      # half-open overlap: touching != stacking
>                 base = max(base, h[j])
>         h[i] = base + s
>         best = max(best, h[i])
>         res.append(best)
>     return res
> ```

## 1) 一般形式

```java

// java
// https://labuladong.online/algo/data-structure/diff-array/

// 差分数组工具类
// V1
class Difference {
    // 差分数组
    private int[] diff;
    
    // 输入一个初始数组，区间操作将在这个数组上进行
    public Difference(int[] nums) {
        assert nums.length > 0;
        diff = new int[nums.length];
        // 根据初始数组构造差分数组
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
    }

    // 给闭区间 [i, j] 增加 val（可以是负数）
    public void increment(int i, int j, int val) {
        diff[i] += val;
        if (j + 1 < diff.length) {
            diff[j + 1] -= val;
        }
    }

    // 返回结果数组
    public int[] result() {
        int[] res = new int[diff.length];
        // 根据差分数组构造结果数组
        res[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            res[i] = res[i - 1] + diff[i];
        }
        return res;
    }
}
```

```java
// V2
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/DifferenceArray.java


// method
public int[] getDifferenceArray(int[][] input, int n) {

/** LC 1109. Corporate Flight Bookings input : [start, end, seats]
 *
 *  NOTE !!!
 *
 *   in java, index start from 0;
 *   but in LC 1109, index start from 1
 *
 */
int[] tmp = new int[n + 1];
for (int[] x : input) {
  int start = x[0];
  int end = x[1];
  int seats = x[2];

  // add
  tmp[start] += seats;

  // subtract
  if (end + 1 <= n) {
    tmp[end + 1] -= seats;
  }
}

for (int i = 1; i < tmp.length; i++) {
  //tmp[i] = tmp[i - 1] + tmp[i];
    tmp[i] += tmp[i - 1];
}

return Arrays.copyOfRange(tmp, 1, n+1);
}
```

## 依模式分類的題目

### 依模式的題目分類

#### **模式 1：基本區間更新題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Range Addition | 370 | Medium | 基本差分陣列 | 模板 1 |
| Corporate Flight Bookings | 1109 | Medium | 區間更新 | 模板 1 |
| Range Addition II | 598 | Easy | 最小重疊區 | 模板 1 |
| Apply Operations to Make All Array Elements Equal to Zero | 2772 | Medium | 區間更新 | 模板 1 |

#### **模式 2：資源分配題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Car Pooling | 1094 | Medium | 容量檢查 | 模板 2 |
| Meeting Rooms II | 253 | Medium | 時間軸事件 | 模板 2 |
| My Calendar I | 729 | Medium | 區間預訂 | 模板 2 |
| My Calendar II | 731 | Medium | 允許重複預訂一次 | 模板 2 |
| My Calendar III | 732 | Hard | K 重預訂 | 模板 2 |

#### **模式 3：事件時間軸題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Number of Flowers in Full Bloom | 2251 | Hard | 時間軸查詢 | 模板 3 |
| Describe the Painting | 2158 | Medium | 顏色混合 | 模板 3 |
| Maximum Population Year | 1854 | Easy | 時間軸計數 | 模板 3 |
| Count Positions on Street With Required Brightness | 2021 | Medium | 光照覆蓋 | 模板 3 |

#### **模式 4：二維差分陣列題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Stamping the Grid | 2132 | Hard | 二維區間更新 | 模板 4 |
| Increment Submatrices by One | 2536 | Medium | 矩形更新 | 模板 4 |

#### **模式 5：覆蓋圖 + 貪婪填補題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Minimum Lights to Illuminate a Road | 3964 | Medium | 差分覆蓋 + 貪婪補洞 | 模板 6 |
| Count Positions on Street With Required Brightness | 2021 | Medium | 差分覆蓋圖（只查詢，不補洞） | 模板 3 |
| Video Stitching | 1024 | Medium | 區間覆蓋 + 貪婪跳躍 | 貪婪 |
| Minimum Number of Taps to Open to Water a Garden | 1326 | Hard | 覆蓋範圍 + 貪婪求最少水龍頭 | 貪婪 |

#### **模式 6：區間更新 + 區間 MAX 查詢題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Falling Squares | 699 | Hard | 座標壓縮 + 線段樹懶標記指派 | 模板 7 |
| My Calendar III | 732 | Hard | 同樣的升級路線，但因為是**可加總**的（+1/-1），用排序 map 版差分陣列還是行得通 | 模板 2 / 5 |

### 依難度排的完整題目清單

#### Easy（打底）
- LC 598: Range Addition II - 找出最小受影響區域
- LC 1854: Maximum Population Year - 簡單時間軸
- LC 1893: Check if All Integers in Range Are Covered - 區間覆蓋

#### Medium（核心）
- LC 370: Range Addition - 經典差分陣列
- LC 1109: Corporate Flight Bookings - 航班座位分配
- LC 1094: Car Pooling - 資源容量驗證
- LC 253: Meeting Rooms II - 最少需要幾間會議室
- LC 729: My Calendar I - 不能重複預訂
- LC 731: My Calendar II - 允許重複預訂一次
- LC 2021: Street Light Brightness - 區間照明
- LC 2158: Amount of New Area Painted - 顏色區段
- LC 2536: Increment Submatrices by One - 二維更新
- LC 2772: Apply Operations to Array - 全部歸零

#### Hard（進階）
- LC 732: My Calendar III - 最大 K 重預訂
- LC 2132: Stamping the Grid - 二維蓋章驗證
- LC 2251: Number of Flowers in Full Bloom - 時間軸上的單點查詢
- LC 699: Falling Squares - 區間指派 + 區間 max（差分陣列不夠 → 線段樹懶標記）

## 2) LC 範例

### 2-1) Range Addition — LC 370

```java
// java
// LC 370


// V0
// IDEA : DIFFERENCE ARRAY
public static int[] getModifiedArray(int length, int[][] updates) {

int[] tmp = new int[length + 1]; // or new int[length]; both works
for (int[] x : updates) {
  int start = x[0];
  int end = x[1];
  int amount = x[2];

  // add
  tmp[start] += amount;

  // subtract (remove the "adding affect" on "NEXT" element)
  /**
   * NOTE !!!
   *
   * <p>we remove the "adding affect" on NEXT element (e.g. end + 1)
   */
  if (end + 1 < length) { // NOTE !!! use `end + 1`
    tmp[end + 1] -= amount;
  }
}

// prepare final result
for (int i = 1; i < tmp.length; i++) {
  tmp[i] += tmp[i - 1];
}

return Arrays.copyOfRange(tmp, 0, length); // return the sub array between 0, lengh index
}

// V1
class Solution {
    public int[] getModifiedArray(int length, int[][] updates) {
        // nums 初始化为全 0
        int[] nums = new int[length];
        // 构造差分解法
        Difference df = new Difference(nums);
        
        for (int[] update : updates) {
            int i = update[0];
            int j = update[1];
            int val = update[2];
            df.increment(i, j, val);
        }
        
        return df.result();
    }
}
```

### 2-2) Corporate Flight Bookings — LC 1109

```java
// java
// LC 1109

// V1
class Solution {
    public int[] corpFlightBookings(int[][] bookings, int n) {
        // nums 初始化为全 0
        int[] nums = new int[n];
        // 构造差分解法
        Difference df = new Difference(nums);

        for (int[] booking : bookings) {
            // 注意转成数组索引要减一哦
            int i = booking[0] - 1;
            int j = booking[1] - 1;
            int val = booking[2];
            // 对区间 nums[i..j] 增加 val
            df.increment(i, j, val);
        }
        // 返回最终的结果数组
        return df.result();
    }
}
```

### 2-3) Car Pooling — LC 1094

```java
// java
// LC 1094
// https://leetcode.com/problems/car-pooling/description/

class Solution {
    public boolean carPooling(int[][] trips, int capacity) {
        // 最多有 1001 个车站
        int[] nums = new int[1001];

        // 构造差分解法
        Difference df = new Difference(nums);

        for (int[] trip : trips) {
            // 乘客数量
            int val = trip[0];

            // 第 trip[1] 站乘客上车
            int i = trip[1];

            // 第 trip[2] 站乘客已经下车，
            // 即乘客在车上的区间是 [trip[1], trip[2] - 1]
            int j = trip[2] - 1;

            // 进行区间操作
            df.increment(i, j, val);
        }

        int[] res = df.result();

        // 客车自始至终都不应该超载
        for (int i = 0; i < res.length; i++) {
            if (capacity < res[i]) {
                return false;
            }
        }
        return true;
    }
}
```

### 2-4) Minimum Lights to Illuminate a Road — LC 3964

**核心想法：** 分三個階段 —（1）用**差分陣列**把*既有*燈泡覆蓋到的每個區間各用 O(1) 記下來，（2）用**前綴和**把它變成一張 `covered[]`，`0` 就是暗點，（3）從左往右做一趟**貪婪**，每碰到一個暗點就放一顆新燈泡。因為放在 `i+1` 的新燈泡會照亮 `i, i+1, i+2`，放完之後可以安心**往前跳 3 格**。

**貪婪為什麼是最佳解：** 第一個暗掉的位置一定得由*某顆*新燈泡照到，而把那顆燈泡放到「還照得到它」的最右邊（`i+1`）能把往前的覆蓋範圍拉到最大 — 不可能比其他放法差。

```python
# python
# LC 3964
class Solution(object):
    def minLights(self, lights):
        n = len(lights)

        # 1) difference array (size n+1 so right+1 is always safe)
        diff = [0] * (n + 1)
        for i, v in enumerate(lights):
            if v > 0:
                left  = max(0, i - v)
                right = min(n - 1, i + v)
                diff[left]      += 1
                diff[right + 1] -= 1

        # 2) prefix sum -> coverage map
        covered = [0] * n
        run = 0
        for i in range(n):
            run += diff[i]
            covered[i] = run

        # 3) greedy fill
        ans = 0
        i = 0
        while i < n:
            if covered[i] == 0:
                ans += 1
                i += 3          # new bulb at i+1 covers i, i+1, i+2
            else:
                i += 1
        return ans
```

```python
# python — compact variant: count dark runs, ceil-divide by bulb width 3
class Solution(object):
    def minLights(self, lights):
        n = len(lights)
        diff = [0] * (n + 1)
        for i, v in enumerate(lights):
            if v > 0:
                diff[max(0, i - v)]          += 1
                diff[min(n - 1, i + v) + 1]  -= 1

        cover = run = ans = 0
        for i in range(n):
            cover += diff[i]
            if cover == 0:
                run += 1                     # extend current dark run
            else:
                ans += (run + 2) // 3        # ceil(run / 3) bulbs for the run
                run = 0
        ans += (run + 2) // 3                # flush trailing dark run
        return ans
```

## 模式選擇策略

```text
Difference Array Problem Analysis Flowchart:

1. Does the problem involve range updates?
   ├── YES → Check update pattern
   │   ├── Multiple ranges need same update? → Use Difference Array
   │   ├── Single element updates? → Use direct array
   │   └── Need immediate query results? → Consider Segment Tree
   └── NO → Not a difference array problem

2. What type of range operation?
   ├── Add/Subtract constant to range → Basic Difference Array (Template 1)
   ├── Track resource usage → Resource Allocation (Template 2)
   ├── Timeline/Event processing → Event Timeline (Template 3)
   └── 2D matrix updates → 2D Difference Array (Template 4)

3. Space/Time Trade-offs:
   ├── Large coordinate space? → Use Coordinate Compression (Template 5)
   ├── Many queries between updates? → Consider Lazy Propagation
   └── Only final result needed? → Basic Difference Array

4. Special Considerations:
   ├── Online vs Offline → Difference array is offline
   ├── Need range queries? → Combine with Prefix Sum
   └── Overlapping intervals? → Check maximum overlap
```

### 決策框架
1. **認出區間更新**：找「更新區間 [l, r]」這類操作
2. **數一數操作次數**：更新很多次 = 適合差分陣列
3. **看查詢的型態**：只要最後結果，還是中途也要查
4. **考慮替代方案**：需要動態查詢就用線段樹

## 總結與速查

### 複雜度速查
| 操作 | 時間複雜度 | 空間複雜度 | 備註 |
|-----------|-----------------|------------------|-------|
| 建差分陣列 | O(n) | O(n) | 從原陣列建 |
| 區間更新 | O(1) | O(1) | 對區間加值 |
| 還原陣列 | O(n) | O(1) | 拿到最終結果 |
| M 次更新 + 還原 | O(m + n) | O(n) | 總複雜度 |
| 二維區間更新 | O(1) | O(1) | 矩形更新 |
| 二維還原 | O(mn) | O(1) | 拿到最終矩陣 |

### 模板速查
| 模板 | 最適合 | 什麼時候別用 | 關鍵模式 |
|----------|----------|------------|-------------|
| 基本差分 | 多次區間更新 | 只更新一次 | diff[i] = arr[i] - arr[i-1] |
| 資源分配 | 有容量限制 | 資源無上限 | 檢查最大用量 |
| 事件時間軸 | 以時間為軸的事件 | 空間類問題 | 時間軸陣列 |
| 二維差分 | 矩陣區間更新 | 一維問題 | 四點更新 |
| 座標壓縮 | 稀疏的大空間 | 稠密陣列 | 映射座標 |

### 常見模式與小技巧

#### **模式：區間更新公式**
```python
# To add val to range [start, end]:
diff[start] += val
if end + 1 < n:
    diff[end + 1] -= val
```

#### **模式：區間的差一問題**
```python
# If passengers get off at station x, they're on board [start, x-1]
# If event ends at time t, it's active [start, t]
# Be careful with inclusive/exclusive boundaries!

# Car pooling example:
for passengers, pickup, dropoff in trips:
    diff[pickup] += passengers
    diff[dropoff] -= passengers  # Not dropoff-1!
```

#### **模式：最大同時事件數**
```python
def max_concurrent(intervals):
    events = []
    for start, end in intervals:
        events.append((start, 1))   # Start event
        events.append((end + 1, -1)) # End event
    
    events.sort()
    max_concurrent = current = 0
    
    for time, delta in events:
        current += delta
        max_concurrent = max(max_concurrent, current)
    
    return max_concurrent
```

### 解題步驟
1. **認出區間操作**：找 [start, end] 型的更新
2. **初始化差分陣列**：通常全部填 0
3. **套用更新**：用公式，每次 O(1)
4. **需要就還原**：做前綴和拿到最終陣列
5. **驗證限制**：檢查容量、重疊等等

### 常見錯誤與提示

**🚫 常見錯誤：**
- **差一錯誤**：區間是閉還是開，要小心
- **陣列越界**：更新前先檢查 end+1
- **初始值**：別忘了原陣列本來的值
- **二維公式寫錯**：四個點的正負號要對
- **溢位**：值很大 × 更新很多次

**✅ 最佳實務：**
- **變數名取清楚**：用 `start/end`，別用 `i/j`
- **在邊界邏輯加註解**：說明是閉區間還開區間
- **測邊界情況**：空區間、整個陣列都更新
- **考慮壓縮**：座標空間很大時
- **及早驗證**：先把不可能的情況擋掉

### 面試提示
1. **認出模式**：「更新很多個區間」→ 差分陣列
2. **解釋這個技巧**：「把區間更新轉成單點更新」
3. **講清楚取捨**：離線處理、還原要 O(n)
4. **知道替代方案**：要線上查詢就用線段樹
5. **處理邊界情況**：空輸入、只有一個元素、區間重疊

### 相關主題
- **前綴和**：反向操作，做區間查詢
- **線段樹**：又要更新又要查詢時
- **樹狀陣列（BIT）**：區間操作的另一個選擇
- **掃描線**：處理區間問題的相關技巧
- **座標壓縮**：處理稀疏的大範圍

#### 長得像差分陣列，其實不是 — 認出破綻
| 題目 | LC # | 差分陣列為什麼套不上 | 去讀 |
|---------|------|-----------------------------------|---------|
| The Skyline Problem | 218 | 端點事件看起來跟 `+h` / `-h` 蓋章一模一樣，但答案是**目前存活高度的最大值**，不是總和 — 你得從一個活的 multiset／堆積裡移掉某個特定高度，這是在 `end+1` 放 `-h` 辦不到的事 | [`scanning_line.md`](./scanning_line.md) |
| Merge Intervals | 56 | 輸出是區間本身，不是每個索引上的值；`+1/-1` 掃描只能告訴你*哪裡*覆蓋數不為零 | [`intervals.md`](./intervals.md) |
| Subarray Sum Equals K | 560 | 這是**用雜湊表查詢**前綴和 — 方向相反（是區間*查詢*，沒有區間*更新*） | [`prefix_sum.md`](./prefix_sum.md) |

> **一句話認破綻：** 差分陣列需要 (a) 更新是**可加總**的、(b) 所有更新**事先**都知道（離線）、(c) 最後讀的是**整個陣列**。破了 (a) → 線段樹（模板 7）；破了 (b) → 線段樹／BIT；破了 (c) 而且座標範圍巨大 → 座標壓縮（模板 5）。

### Java 實作筆記
```java
// Java Difference Array Class
class Difference {
    private int[] diff;
    
    public Difference(int[] nums) {
        diff = new int[nums.length];
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i-1];
        }
    }
    
    public void increment(int i, int j, int val) {
        diff[i] += val;
        if (j + 1 < diff.length) {
            diff[j + 1] -= val;
        }
    }
    
    public int[] result() {
        int[] res = new int[diff.length];
        res[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            res[i] = res[i-1] + diff[i];
        }
        return res;
    }
}
```

### Python 實作筆記
```python
# Python class implementation
class DifferenceArray:
    def __init__(self, nums):
        self.n = len(nums)
        self.diff = [0] * self.n
        if nums:
            self.diff[0] = nums[0]
            for i in range(1, self.n):
                self.diff[i] = nums[i] - nums[i-1]
    
    def update(self, start, end, val):
        self.diff[start] += val
        if end + 1 < self.n:
            self.diff[end + 1] -= val
    
    def get_result(self):
        result = [self.diff[0]]
        for i in range(1, self.n):
            result.append(result[-1] + self.diff[i])
        return result
```

---
**面試必會題**：LC 370、1109、1094、253、732
**進階題**：LC 732、2132、2251
**關鍵字**：差分陣列、區間更新、區間修改、掃描線、前綴和

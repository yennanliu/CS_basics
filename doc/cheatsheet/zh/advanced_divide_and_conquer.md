# 進階分治法

> **範圍** — 合併排序以外的分治法 —— 那些「遞迴 + 合併」的形狀（邊排序邊計數、最近點對、矩陣分治、運算式分治）以及它們的遞迴式。
> **另見**：[2_pointers_quickselect.md](./2_pointers_quickselect.md) — quickselect 的完整內容，本檔一律轉交過去；[sort.md](./sort.md) — 把合併排序與 quickselect 當排序演算法看；[binary_search.md](./binary_search.md) — 只折半、沒有合併步驟；[recursion.md](./recursion.md) — 遞迴的運作機制；[segment_tree.md](./segment_tree.md) — 把分治法固化成資料結構。

## LeetCode 題目清單

- [Divide and Conquer](https://leetcode.com/problem-list/divide-and-conquer/)
- [Merge Sort](https://leetcode.com/problem-list/merge-sort/)
- [Quickselect](https://leetcode.com/problem-list/quickselect/)

## 總覽
**分治法**是一種很強的演算法範式：把複雜問題拆成較小的子問題，遞迴解掉，再把結果合併起來。這套做法在逆序對計數、區間查詢與合併類操作上特別有效。

### 關鍵性質
- **時間複雜度**：大多數題目是 O(n log n)
- **空間複雜度**：輔助陣列 O(n)，遞迴堆疊 O(log n)
- **核心想法**：把問題切成兩半，遞迴求解，再合併結果
- **什麼時候用**：逆序對計數、區間類問題、合併類操作
- **關鍵技巧**：改寫合併排序，在合併步驟塞進自訂邏輯

### 核心特徵
- **切分（Divide）**：把問題拆成更小的子問題
- **求解（Conquer）**：遞迴解子問題
- **合併（Combine）**：把子解合成最終答案
- **最佳子結構**：問題可以被最佳地拆解
- **合併邏輯**：針對需求客製化的合併步驟

## 題型分類

### **類型 1：逆序對計數**
- **說明**：計算「左邊元素 > 右邊元素」的配對數
- **例題**：LC 315（Count Smaller After Self）、LC 493（Reverse Pairs）、LC 327（Count Range Sum）
- **模式**：改寫合併排序，在合併時順便計數

### **類型 2：區間和問題**
- **說明**：計算落在特定範圍內的元素／子陣列數量
- **例題**：LC 327（Count of Range Sum）、LC 493（帶條件的 Reverse Pairs）
- **模式**：前綴和 + 分治法

### **類型 3：陣列重建**
- **說明**：建出具有特定排序性質的陣列
- **例題**：LC 1649（Create Sorted Array）、LC 2426（Pairs Satisfying Inequality）
- **模式**：合併排序 + 重建邏輯

## 模板與演算法

### 模板比較表
| 模板類型 | 適用情境 | 時間複雜度 | 什麼時候用 |
|---------------|----------|-----------------|-------------|
| **基本逆序對計數** | 計算逆序對 | O(n log n) | 單純的逆序對題 |
| **帶條件的逆序對計數** | 帶條件計數 | O(n log n) | Reverse pairs、範圍條件 |
| **區間查詢分治** | 區間和計數 | O(n log n) | 子陣列和問題 |
| **重建型分治** | 建出有序陣列 | O(n log n) | 陣列建構問題 |

### 模板 1：基本逆序對計數 —— LC 315
```python
def count_inversions(arr):
    """Count total number of inversions using merge sort"""
    def merge_and_count(arr, temp, left, mid, right):
        i, j, k = left, mid + 1, left
        inv_count = 0

        # Merge with inversion counting
        while i <= mid and j <= right:
            if arr[i] <= arr[j]:
                temp[k] = arr[i]
                i += 1
            else:
                temp[k] = arr[j]
                # Count inversions: all elements from i to mid are > arr[j]
                inv_count += (mid - i + 1)
                j += 1
            k += 1

        # Copy remaining elements
        while i <= mid:
            temp[k] = arr[i]
            i += 1
            k += 1
        while j <= right:
            temp[k] = arr[j]
            j += 1
            k += 1

        # Copy back to original array
        for i in range(left, right + 1):
            arr[i] = temp[i]

        return inv_count

    def merge_sort_and_count(arr, temp, left, right):
        inv_count = 0
        if left < right:
            mid = (left + right) // 2
            inv_count += merge_sort_and_count(arr, temp, left, mid)
            inv_count += merge_sort_and_count(arr, temp, mid + 1, right)
            inv_count += merge_and_count(arr, temp, left, mid, right)
        return inv_count

    temp = [0] * len(arr)
    return merge_sort_and_count(arr[:], temp, 0, len(arr) - 1)
```

### 模板 2：帶條件的逆序對計數 —— LC 493
```python
def count_reverse_pairs(nums):
    """Count pairs where nums[i] > 2 * nums[j] for i < j"""
    def merge_and_count(nums, temp, left, mid, right):
        # Count reverse pairs first (before sorting)
        count = 0
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)

        # Now perform regular merge
        i, j, k = left, mid + 1, left
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp[k] = nums[i]
                i += 1
            else:
                temp[k] = nums[j]
                j += 1
            k += 1

        while i <= mid:
            temp[k] = nums[i]
            i += 1
            k += 1
        while j <= right:
            temp[k] = nums[j]
            j += 1
            k += 1

        for i in range(left, right + 1):
            nums[i] = temp[i]

        return count

    def merge_sort_and_count(nums, temp, left, right):
        count = 0
        if left < right:
            mid = (left + right) // 2
            count += merge_sort_and_count(nums, temp, left, mid)
            count += merge_sort_and_count(nums, temp, mid + 1, right)
            count += merge_and_count(nums, temp, left, mid, right)
        return count

    temp = [0] * len(nums)
    return merge_sort_and_count(nums[:], temp, 0, len(nums) - 1)
```

### 模板 3：區間和分治 —— LC 327
```python
def count_range_sum(nums, lower, upper):
    """Count subarrays with sum in [lower, upper]"""
    def merge_and_count(prefix_sums, temp, left, mid, right):
        count = 0
        j = k = mid + 1

        # For each prefix sum in left half
        for i in range(left, mid + 1):
            # Find range [j, k) where prefix_sums[j] - prefix_sums[i] is in [lower, upper]
            while j <= right and prefix_sums[j] - prefix_sums[i] < lower:
                j += 1
            while k <= right and prefix_sums[k] - prefix_sums[i] <= upper:
                k += 1
            count += k - j

        # Regular merge
        i, j, p = left, mid + 1, left
        while i <= mid and j <= right:
            if prefix_sums[i] <= prefix_sums[j]:
                temp[p] = prefix_sums[i]
                i += 1
            else:
                temp[p] = prefix_sums[j]
                j += 1
            p += 1

        while i <= mid:
            temp[p] = prefix_sums[i]
            i += 1
            p += 1
        while j <= right:
            temp[p] = prefix_sums[j]
            j += 1
            p += 1

        for i in range(left, right + 1):
            prefix_sums[i] = temp[i]

        return count

    def divide_and_conquer(prefix_sums, temp, left, right):
        if left >= right:
            return 0

        mid = (left + right) // 2
        count = divide_and_conquer(prefix_sums, temp, left, mid)
        count += divide_and_conquer(prefix_sums, temp, mid + 1, right)
        count += merge_and_count(prefix_sums, temp, left, mid, right)
        return count

    # Build prefix sums
    prefix_sums = [0]
    for num in nums:
        prefix_sums.append(prefix_sums[-1] + num)

    temp = [0] * len(prefix_sums)
    return divide_and_conquer(prefix_sums, temp, 0, len(prefix_sums) - 1)
```

### 模板 4：陣列重建 —— LC 1649
```python
def create_sorted_array(instructions):
    """Create sorted array with minimum cost"""
    def merge_and_count(arr, temp, left, mid, right):
        smaller_count = [0] * len(arr)
        larger_count = [0] * len(arr)

        # Count smaller and larger elements during merge
        i, j = left, mid + 1
        for k in range(left, right + 1):
            if i > mid:
                temp[k] = arr[j]
                j += 1
            elif j > right:
                temp[k] = arr[i]
                # Count how many elements from right half are smaller
                smaller_count[arr[i][1]] += (right - mid)
                i += 1
            elif arr[i][0] <= arr[j][0]:
                temp[k] = arr[i]
                smaller_count[arr[i][1]] += (j - mid - 1)
                i += 1
            else:
                temp[k] = arr[j]
                j += 1

        # Copy back
        for k in range(left, right + 1):
            arr[k] = temp[k]

        return smaller_count, larger_count

    def merge_sort_with_count(arr, temp, left, right):
        if left >= right:
            return

        mid = (left + right) // 2
        merge_sort_with_count(arr, temp, left, mid)
        merge_sort_with_count(arr, temp, mid + 1, right)
        merge_and_count(arr, temp, left, mid, right)

    # Implementation depends on specific problem
    # This is a general framework for array reconstruction
    pass
```

---

## 合併排序之外：其他分治形狀

模板 1-4 其實都是**同一個形狀**：從中點切開、兩半各自遞迴、在合併時計數。但面試裡的分治法還有另外四種形狀，是合併排序模板*涵蓋不到*的。要練到能認出自己在哪一種 —— 遞迴式會在你寫下第一行程式碼之前，就先告訴你答案的複雜度。

### 快速判斷表 —— 這是哪一種分治？
| 題目在問什麼 | 形狀 | 遞迴式 | 複雜度 | 例題 |
|--------------------|-------|------------|------------|----------|
| 第 k 小／大、「top k」（不要求排序） | **只遞迴進一邊**（模板 5） | `T(n) = T(n/2) + O(n)` | 期望 **O(n)** | LC 215、973、347、1985 |
| 最佳子陣列／區段，答案可能橫跨中點 | **跨中點合併**（模板 6） | `T(n) = 2T(n/2) + O(n)` | O(n log n) | LC 53、918、218 |
| 合併 k 個已排序的東西 | **兩兩合併**（模板 7） | `T(k) = 2T(k/2) + O(N)` | O(N log k) | LC 23、148、912 |
| 在二維格子或由資料決定的切點上搜尋／建構 | **在隱含空間上分治**（模板 8） | `T(n) = 3T(n/2) + O(1)` | O(n^log₂3) ≈ O(n^1.58) | LC 240、427、395、1763 |

> **主定理快速讀法**：對 `T(n) = a·T(n/b) + f(n)`，把 `f(n)` 拿去跟 `n^(log_b a)` 比：
> - `f(n)` **比較小** → 成本集中在葉子 → `O(n^(log_b a))` —— *LC 240：a=3, b=2, f=O(1) → O(n^1.58)*
> - `f(n)` **相等** → 每層成本一樣 → `O(n^(log_b a) · log n)` —— *LC 53：a=2, b=2, f=O(n) → O(n log n)*
> - `f(n)` **比較大** → 成本集中在根 → `O(f(n))` —— *LC 215：a=1, b=2, f=O(n) → O(n)*
>
> quickselect 之所以贏過排序，關鍵就在 **a 從 2 掉到 1**，讓等比級數 `n + n/2 + n/4 + …` 收斂成 `2n`。

---

### 模板 5：Quickselect —— 分割後只遞迴一邊 —— LC 215 ⭐⭐⭐⭐⭐

**核心想法**：做完一次分割後，pivot 就坐在它**最終排序後的索引** `p` 上。拿 `p` 跟目標索引比一比 —— 答案只會在其中一邊，所以另外一半直接**丟掉**，不用遞迴進去。

**遞迴式**：`T(n) = T(n/2) + O(n)` → `n + n/2 + n/4 + … = O(n)`，這是**期望值**。最差是 `O(n²)`，發生在 pivot 每次都取到極端值時 —— 這就是為什麼 pivot 必須是**隨機**的。

**要選第 k 大**：一開始就轉成遞增的索引：`target = n - k`。不要在分割裡去翻轉比較子。

```java
// java
// LC 215 - Kth Largest Element in an Array
// IDEA: Lomuto partition with a RANDOM pivot; the pivot lands at its final index,
//       so recurse into only the side that contains the target index
// time = O(N) expected / O(N^2) worst, space = O(1) (iterative — no recursion stack)
Random rnd = new Random();

public int findKthLargest(int[] nums, int k) {
    int target = nums.length - k;              // k-th largest == index (n-k) in ascending order
    int lo = 0, hi = nums.length - 1;
    while (true) {
        if (lo == hi) return nums[lo];
        int p = partition(nums, lo, hi, lo + rnd.nextInt(hi - lo + 1));   // random pivot!
        if (p == target) return nums[p];
        else if (p < target) lo = p + 1;       // keep the RIGHT side only
        else hi = p - 1;                       // keep the LEFT side only
    }
}

// Lomuto: park the pivot at the end, sweep everything smaller to the front, put the pivot back
private int partition(int[] a, int lo, int hi, int pivotIdx) {
    int pivot = a[pivotIdx];
    swap(a, pivotIdx, hi);
    int store = lo;
    for (int i = lo; i < hi; i++) if (a[i] < pivot) swap(a, store++, i);
    swap(a, store, hi);
    return store;                              // final resting index of the pivot
}
private void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }
```

> Python 寫法、Hoare 分割與三路分割、LC 973 變形，以及 Median-of-Medians 的大綱，
> 全部都在 [2_pointers_quickselect.md](./2_pointers_quickselect.md)，那份文件才是這個演算法的主場。
> *這裡*要記的只有它的形狀：它是本檔唯一一個只遞迴進**單邊**的分治模板，
> 所以遞迴式是 `T(n) = T(n/2) + O(n)`，總複雜度是 O(n)，
> 而不是本檔其他每個模板都要付的 O(n log n)。

---

### 模板 6：跨中點合併 —— LC 53

**核心想法**：當答案是一段*連續區段*時，它要嘛整段在左半、要嘛整段在右半、要嘛**橫跨中點**。前兩種靠遞迴就免費拿到；第三種才是真正要做的工 —— 而它只要從中間往兩側擴張，O(n) 就算得出來。

**遞迴式**：`T(n) = 2T(n/2) + O(n)` → **O(n log n)**（主定理的相等情況）。Kadane 的 O(n) DP 嚴格更好，所以答題時要講清楚：*「分治是 O(n log n)，Kadane 是 O(n)」* —— 面試官說「現在用分治法解一次」時，要的就是這個分治版本。

```java
// java
// LC 53 - Maximum Subarray (divide & conquer view)
// IDEA: best = max(best in left, best in right, best crossing the midpoint);
//       the crossing one = max suffix of left + max prefix of right
// time = O(N log N), space = O(log N) recursion stack
public int maxSubArray(int[] nums) { return solve(nums, 0, nums.length - 1); }

private int solve(int[] a, int l, int r) {
    if (l == r) return a[l];                       // single element is its own best
    int mid = l + (r - l) / 2;
    int left  = solve(a, l, mid);
    int right = solve(a, mid + 1, r);

    int sum = 0, leftBest = Integer.MIN_VALUE;     // best SUFFIX of the left half (must touch mid)
    for (int i = mid; i >= l; i--) { sum += a[i]; leftBest = Math.max(leftBest, sum); }

    sum = 0;
    int rightBest = Integer.MIN_VALUE;             // best PREFIX of the right half (must touch mid+1)
    for (int i = mid + 1; i <= r; i++) { sum += a[i]; rightBest = Math.max(rightBest, sum); }

    return Math.max(Math.max(left, right), leftBest + rightBest);
}
```

```python
# python
# LC 53 - Maximum Subarray (divide & conquer view)
# time = O(N log N), space = O(log N)
def maxSubArray(nums):
    def solve(l, r):
        if l == r:
            return nums[l]
        mid = (l + r) // 2
        left = solve(l, mid)
        right = solve(mid + 1, r)

        s, left_best = 0, float('-inf')            # best suffix of left half
        for i in range(mid, l - 1, -1):
            s += nums[i]
            left_best = max(left_best, s)

        s, right_best = 0, float('-inf')           # best prefix of right half
        for i in range(mid + 1, r + 1):
            s += nums[i]
            right_best = max(right_best, s)

        return max(left, right, left_best + right_best)

    return solve(0, len(nums) - 1)
```

**🚫 經典 bug**：把 `leftBest` 初始化成 `0`。這等於偷偷允許了*空*的後綴，於是全負數陣列會回傳 `0`，而不是最大的那個單一元素。要從 `-infinity` 開始，並且強制兩半都非空。

#### **變形：LC 918 —— Maximum Sum Circular Subarray**
> 轉折點：繞回頭的區段，剛好就是某個**不**繞回頭區段的補集，所以 `answer = max(maxKadane, total - minKadane)`。要特別擋掉全負數的情況 —— 那時 `total - minKadane == 0` 描述的是被禁止的空陣列。

```java
// java
// LC 918 - Maximum Sum Circular Subarray
// IDEA: best is either a normal subarray, or everything EXCEPT the minimum subarray
// time = O(N), space = O(1)
public int maxSubarraySumCircular(int[] nums) {
    int total = 0, curMax = 0, best = Integer.MIN_VALUE, curMin = 0, worst = Integer.MAX_VALUE;
    for (int x : nums) {
        total  += x;
        curMax = Math.max(curMax + x, x); best  = Math.max(best, curMax);
        curMin = Math.min(curMin + x, x); worst = Math.min(worst, curMin);
    }
    return best > 0 ? Math.max(best, total - worst) : best;   // all-negative -> plain Kadane
}
```

```python
# python
# LC 918 - Maximum Sum Circular Subarray
# time = O(N), space = O(1)
def maxSubarraySumCircular(nums):
    total, cur_max, best, cur_min, worst = 0, 0, float('-inf'), 0, float('inf')
    for x in nums:
        total += x
        cur_max = max(cur_max + x, x)
        best = max(best, cur_max)
        cur_min = min(cur_min + x, x)
        worst = min(worst, cur_min)
    return max(best, total - worst) if best > 0 else best
```

---

### 模板 7：兩兩分治合併（k 路）—— LC 23 ⭐⭐⭐⭐

**核心想法**：要合併 `k` 個有序結構時，**不要**一個一個往上疊（那是 `O(N·k)` —— 前面的元素會被重複複製 `k` 次）。要兩兩配對、成半合併，這樣總共 `N` 個元素每層只碰一次，而層數只有 `log k`。

**遞迴式**：`T(k) = 2T(k/2) + O(N)` → 時間 **O(N log k)**，跟堆積解法一樣，但額外空間是 `O(1)` 而不是 `O(k)`。

```java
// java
// LC 23 - Merge k Sorted Lists
// IDEA: divide the ARRAY OF LISTS in half, merge each half, then merge the two results
// time = O(N log k) where N = total nodes, space = O(log k) recursion stack
public ListNode mergeKLists(ListNode[] lists) {
    if (lists == null || lists.length == 0) return null;
    return mergeRange(lists, 0, lists.length - 1);
}
private ListNode mergeRange(ListNode[] ls, int l, int r) {
    if (l == r) return ls[l];
    int mid = l + (r - l) / 2;
    return merge2(mergeRange(ls, l, mid), mergeRange(ls, mid + 1, r));
}
private ListNode merge2(ListNode a, ListNode b) {
    ListNode dummy = new ListNode(0), cur = dummy;
    while (a != null && b != null) {
        if (a.val <= b.val) { cur.next = a; a = a.next; }
        else                { cur.next = b; b = b.next; }
        cur = cur.next;
    }
    cur.next = (a != null) ? a : b;          // attach the non-empty remainder wholesale
    return dummy.next;
}
```

```python
# python
# LC 23 - Merge k Sorted Lists
# time = O(N log k), space = O(log k)
def mergeKLists(lists):
    if not lists:
        return None

    def merge2(a, b):
        dummy = cur = ListNode()
        while a and b:
            if a.val <= b.val:
                cur.next, a = a, a.next
            else:
                cur.next, b = b, b.next
            cur = cur.next
        cur.next = a or b
        return dummy.next

    def merge_range(l, r):
        if l == r:
            return lists[l]
        mid = (l + r) // 2
        return merge2(merge_range(l, mid), merge_range(mid + 1, r))

    return merge_range(0, len(lists) - 1)
```

#### **變形：LC 148 —— Sort List**
> 轉折點：鏈結串列沒有中點索引，所以要用**快慢指標**找切點。`fast` 要從 `head.next` 開始（不是 `head`），這樣偶數長度時 `slow` 才會停在*左半*的尾巴 —— 否則 2 個節點的串列會切成 `2 + 0`，然後永遠遞迴下去。遞迴前記得用 `slow.next = null` 切斷。

```java
// java
// LC 148 - Sort List
// IDEA: merge sort on a linked list — split via slow/fast, sort halves, reuse merge2()
// time = O(N log N), space = O(log N) recursion stack (O(1) if written bottom-up)
public ListNode sortList(ListNode head) {
    if (head == null || head.next == null) return head;
    ListNode slow = head, fast = head.next;      // fast starts AHEAD -> slow lands on left tail
    while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
    ListNode right = slow.next;
    slow.next = null;                            // cut the list in two
    return merge2(sortList(head), sortList(right));
}
```

```python
# python
# LC 148 - Sort List
# time = O(N log N), space = O(log N)
def sortList(head):
    if not head or not head.next:
        return head
    slow, fast = head, head.next                 # fast starts ahead -> slow lands on left tail
    while fast and fast.next:
        slow, fast = slow.next, fast.next.next
    right, slow.next = slow.next, None           # cut into two halves
    return merge2(sortList(head), sortList(right))
```

---

### 模板 8：在二維／隱含空間上分治 —— LC 240

**核心想法**：「要折半的陣列」不一定得是陣列。把**矩陣切成四個象限**，在中心點做一次比較，就能證明某整個象限不可能含有目標 —— 在列與行都有序的矩陣裡，一個區塊的最小值在左上角、最大值在右下角。

**遞迴式**：`T(n) = 3T(n/2) + O(1)` → 對 `n × n` 矩陣是 **O(n^log₂3) ≈ O(n^1.58)**（主定理，葉子主導）。注意這其實*比* O(m + n) 的階梯走法**還差** —— 兩種都要會，並說得出為什麼實務上會選階梯走法。

```java
// java
// LC 240 - Search a 2D Matrix II
// IDEA: split the rectangle into 4 quadrants around its centre; comparing the centre to the
//       target always eliminates one whole quadrant, leaving 3 subproblems of half the side
// time = O(N^log2(3)) ~ O(N^1.58) on an N x N matrix, space = O(log N)
public boolean searchMatrix(int[][] m, int target) {
    if (m.length == 0 || m[0].length == 0) return false;
    return dq(m, target, 0, 0, m.length - 1, m[0].length - 1);
}
private boolean dq(int[][] m, int t, int r1, int c1, int r2, int c2) {
    if (r1 > r2 || c1 > c2) return false;
    if (t < m[r1][c1] || t > m[r2][c2]) return false;   // block min / max prune
    if (r1 == r2 && c1 == c2) return m[r1][c1] == t;    // 1x1 base case (stops infinite recursion)
    int rm = (r1 + r2) / 2, cm = (c1 + c2) / 2;
    if (m[rm][cm] == t) return true;
    if (m[rm][cm] < t)                                  // centre too small -> drop TOP-LEFT block
        return dq(m, t, r1, cm + 1, rm, c2)
            || dq(m, t, rm + 1, c1, r2, cm)
            || dq(m, t, rm + 1, cm + 1, r2, c2);
    return dq(m, t, r1, c1, rm, cm)                     // centre too big -> drop BOTTOM-RIGHT block
        || dq(m, t, r1, cm + 1, rm, c2)
        || dq(m, t, rm + 1, c1, r2, cm);
}

// The answer you should actually give: walk in from the top-right corner.
// time = O(M + N), space = O(1)
public boolean searchMatrixStaircase(int[][] m, int target) {
    int r = 0, c = m[0].length - 1;
    while (r < m.length && c >= 0) {
        if (m[r][c] == target) return true;
        if (m[r][c] > target) c--; else r++;   // this column is too big / this row is exhausted
    }
    return false;
}
```

```python
# python
# LC 240 - Search a 2D Matrix II
# time = O(N^log2(3)) ~ O(N^1.58), space = O(log N)
def searchMatrix(matrix, target):
    if not matrix or not matrix[0]:
        return False

    def dq(r1, c1, r2, c2):
        if r1 > r2 or c1 > c2:
            return False
        if target < matrix[r1][c1] or target > matrix[r2][c2]:   # block min / max prune
            return False
        if r1 == r2 and c1 == c2:
            return matrix[r1][c1] == target
        rm, cm = (r1 + r2) // 2, (c1 + c2) // 2
        if matrix[rm][cm] == target:
            return True
        if matrix[rm][cm] < target:                              # drop TOP-LEFT block
            return dq(r1, cm + 1, rm, c2) or dq(rm + 1, c1, r2, cm) or dq(rm + 1, cm + 1, r2, c2)
        return dq(r1, c1, rm, cm) or dq(r1, cm + 1, rm, c2) or dq(rm + 1, c1, r2, cm)  # drop BOTTOM-RIGHT

    return dq(0, 0, len(matrix) - 1, len(matrix[0]) - 1)
```

**視覺追蹤** —— 列／行皆有序的區塊，繞著中心 `(rm, cm)` 分出的四個象限：

```text
        c1 ....... cm ....... c2
   r1  +-----------+-----------+
       |    TL     |    TR     |     TL max is m[rm][cm]  -> if m[rm][cm] < target, TL is all too small
   rm  |        (rm,cm)        |     BR min is m[rm+1][cm+1] -> if m[rm][cm] > target, BR is all too big
       +-----------+-----------+
       |    BL     |    BR     |     TR and BL are never eliminable: they hold both
   r2  +-----------+-----------+     smaller and larger values than the centre
```

#### **變形：LC 427 —— Construct Quad Tree**
> 轉折點：一樣切四象限，但這是*建構*而不是搜尋 —— 四個都遞迴下去，之後如果四個子節點都回傳「值相同的葉節點」，就把這個節點**收合**掉。

```python
# python
# LC 427 - Construct Quad Tree
# IDEA: split the grid into 4 equal quadrants; merge back into one leaf when all 4 agree
# time = O(N^2 log N) worst (each of log N levels scans up to N^2 cells), space = O(log N)
def construct(grid):
    def build(r, c, size):
        if size == 1:
            return Node(grid[r][c] == 1, True)
        h = size // 2
        tl = build(r, c, h)
        tr = build(r, c + h, h)
        bl = build(r + h, c, h)
        br = build(r + h, c + h, h)
        if (tl.isLeaf and tr.isLeaf and bl.isLeaf and br.isLeaf
                and tl.val == tr.val == bl.val == br.val):
            return Node(tl.val, True)            # 4 uniform leaves -> collapse into one leaf
        return Node(True, False, tl, tr, bl, br)  # val is ignored for internal nodes

    return build(0, 0, len(grid))
```

> **相關**：LC 558（Logical OR of Two Quad-Trees）把同一套遞迴同時套在*兩棵*樹上 —— 任一節點是 `val == 1` 的葉節點就回傳該葉節點，任一節點是 `val == 0` 的葉節點就回傳另一棵子樹，否則對四組子節點做 OR 再收合。

#### **變形：LC 395 —— 切在*資料決定*的點，而不是中點**
> 轉折點：切點是由輸入決定的。在這個視窗裡**總**出現次數 `< k` 的字元，永遠不可能出現在合法答案內，所以它就是一道牆：在它每次出現的位置把字串切開，再對各段遞迴。

**遞迴式**：每一層至少移除一個相異字元，所以深度 ≤ 字母表大小 → **O(26·N)**。

```java
// java
// LC 395 - Longest Substring with At Least K Repeating Characters
// IDEA: a char with total count < k can never be inside the answer -> split on it and recurse
// time = O(26 * N), space = O(26 * N) for the substrings / recursion
public int longestSubstring(String s, int k) {
    if (s.length() < k) return 0;
    int[] freq = new int[26];
    for (char ch : s.toCharArray()) freq[ch - 'a']++;
    for (int i = 0; i < 26; i++) {
        if (freq[i] > 0 && freq[i] < k) {            // found a wall character
            int best = 0;
            for (String part : s.split(String.valueOf((char) ('a' + i))))
                best = Math.max(best, longestSubstring(part, k));
            return best;
        }
    }
    return s.length();                               // no wall -> the whole string qualifies
}
```

```python
# python
# LC 395 - Longest Substring with At Least K Repeating Characters
# time = O(26 * N), space = O(26 * N)
def longestSubstring(s, k):
    if len(s) < k:
        return 0
    for c in set(s):
        if s.count(c) < k:                           # c can never sit inside a valid substring
            return max(longestSubstring(part, k) for part in s.split(c))
    return len(s)                                    # every char already appears >= k times
```

#### **變形：LC 1763 —— Longest Nice Substring**
> 轉折點：形狀完全一樣，只是判牆的條件不同 —— 某個字元的另一種大小寫沒出現在視窗裡，它就不可能落在 nice substring 內，所以在那裡切開、取較長的一邊。優先選**最左邊**那道牆，才能維持「最早答案優先」的平手規則。

```python
# python
# LC 1763 - Longest Nice Substring
# IDEA: a char missing its case-partner is a wall -> split there, best of the two sides
# time = O(26 * N), space = O(26 * N)
def longestNiceSubstring(s):
    if len(s) < 2:
        return ""
    chars = set(s)
    for i, c in enumerate(s):
        if c.upper() in chars and c.lower() in chars:
            continue                                  # c is fine, keep scanning
        left = longestNiceSubstring(s[:i])            # wall at i -> answer avoids index i
        right = longestNiceSubstring(s[i + 1:])
        return left if len(left) >= len(right) else right
    return s                                          # no wall -> whole string is nice
```

---

## 依模式分類的題目

### **逆序對計數類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Count of Smaller Numbers After Self | 315 | 改寫的合併排序 | Hard |
| Reverse Pairs | 493 | 帶條件的逆序對計數 | Hard |
| Count of Range Sum | 327 | 前綴和 + 分治 | Hard |
| Create Sorted Array through Instructions | 1649 | 動態逆序對計數 | Hard |

### **區間查詢類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Count of Range Sum | 327 | 前綴和合併 | Hard |
| Number of Pairs Satisfying Inequality | 2426 | 範圍條件分治 | Hard |

### **其他掛著分治標籤的題目（速查）**
> 這些題目雖然被標成分治，但實務上通常是用姊妹文件裡的技巧解掉，或只是把上面某個模板輕度套用一下。

| 題目 | LC # | 為什麼算分治／實際上該用什麼 |
|---------|------|--------------------------------------|
| Median of Two Sorted Arrays | 4 | 在較短的陣列上二分搜尋**切割點** —— 見 `binary_search.md`。O(log(min(m,n)))。 |
| The Skyline Problem | 218 | 把建築物切一半、遞迴，然後像合併排序那樣**合併兩條天際線**（掃過兩份關鍵點清單，高度變化時輸出）。O(n log n)。 |
| Sort an Array | 912 | 純合併排序／三路快排 —— 模板 1 與模板 5 的地基。 |
| Convert Sorted Array to BST | 108 | 中間元素當根，兩半各自遞迴。`T(n) = 2T(n/2) + O(1)` → O(n)。 |
| Convert Sorted List to BST | 109 | 一樣，但用快慢指標找中點（LC 148 的技巧），或用**中序建構**做到 O(n)。 |
| Construct BT from Preorder + Inorder | 105 | 根來自前序，中序在根的索引處切開 —— 把中序索引丟進雜湊表，就能從 O(n²) 降到 O(n)。 |
| Construct BT from Inorder + Postorder | 106 | 同樣的切法，但後序要**從右往左**讀，並先建右子樹。 |
| Construct BT from Preorder + Postorder | 889 | 同樣想法；左子樹大小靠在後序中定位 `preorder[i+1]` 得到（答案不唯一）。 |
| Maximum Binary Tree | 654 | 根 = 區間最大值，兩側各自遞迴。最差 O(n²)；用單調堆疊可以做到 O(n)。 |
| Balance a BST | 1382 | 中序攤平成有序陣列，再套 LC 108。 |
| Majority Element | 169 | 分治：一段區間的多數元素必是某一半的多數元素，合併時再計數確認。Boyer–Moore 投票法是 O(n)/O(1)，才是面試想聽的答案。 |
| Range Sum Query - Mutable | 307 | 對索引區間遞迴折半**就是**線段樹 —— 見 `segment_tree.md` / `binary_indexed_tree.md`。 |
| Number of Ways to Reorder Array to Get Same BST | 1569 | 把值分成左右子樹兩串，遞迴，再用 `C(n-1, leftSize)` 交錯合併。 |
| Beautiful Array | 932 | 從兩半組出來：`2*A[i]-1`（奇數）接上 `2*A[i]`（偶數）可以保住性質。 |
| Super Pow | 372 | 快速冪：`a^b = (a^(b/2))^2 · a^(b%2)`，沿著指數陣列逐位套用。 |
| Number of 1 Bits / Reverse Bits | 191 / 190 | 位元層級的分治：用遮罩把成對位元折成 nibble、再折成 byte（`0x55555555`、`0x33333333`、…），O(log w) 而非 O(w)。 |

## LC 範例

### 2-1) Count of Smaller Numbers After Self (LC 315) —— 合併排序 + 追蹤索引
> 合併過程中，數一數有多少右半元素越過了每個左半元素。

```java
// LC 315 - Count of Smaller Numbers After Self
// IDEA: Merge sort on indices; each right-side element that passes left side increments its count
// time = O(N log N), space = O(N)
public List<Integer> countSmaller(int[] nums) {
    int n = nums.length;
    int[] counts = new int[n], indices = new int[n];
    for (int i = 0; i < n; i++) indices[i] = i;
    mergeSort(nums, indices, counts, 0, n - 1);
    List<Integer> res = new ArrayList<>();
    for (int c : counts) res.add(c);
    return res;
}
private void mergeSort(int[] nums, int[] idx, int[] counts, int l, int r) {
    if (l >= r) return;
    int mid = (l + r) / 2;
    mergeSort(nums, idx, counts, l, mid);
    mergeSort(nums, idx, counts, mid + 1, r);
    int[] tmp = new int[r - l + 1];
    int i = l, j = mid + 1, k = 0, rightMoved = 0;
    while (i <= mid && j <= r) {
        if (nums[idx[j]] < nums[idx[i]]) { tmp[k++] = idx[j++]; rightMoved++; }
        else { counts[idx[i]] += rightMoved; tmp[k++] = idx[i++]; }
    }
    while (i <= mid) { counts[idx[i]] += rightMoved; tmp[k++] = idx[i++]; }
    while (j <= r) tmp[k++] = idx[j++];
    System.arraycopy(tmp, 0, idx, l, tmp.length);
}
```

```python
def countSmaller(nums):
    """Count how many numbers after each element are smaller"""
    def merge_and_count(indices, temp, left, mid, right):
        # Count smaller elements to the right
        i, j, k = left, mid + 1, left

        while i <= mid and j <= right:
            if nums[indices[i]] <= nums[indices[j]]:
                temp[k] = indices[i]
                # All elements from mid+1 to j-1 are smaller than nums[indices[i]]
                counts[indices[i]] += (j - mid - 1)
                i += 1
            else:
                temp[k] = indices[j]
                j += 1
            k += 1

        # Process remaining elements
        while i <= mid:
            temp[k] = indices[i]
            counts[indices[i]] += (j - mid - 1)
            i += 1
            k += 1
        while j <= right:
            temp[k] = indices[j]
            j += 1
            k += 1

        # Copy back
        for i in range(left, right + 1):
            indices[i] = temp[i]

    def merge_sort(indices, temp, left, right):
        if left >= right:
            return

        mid = (left + right) // 2
        merge_sort(indices, temp, left, mid)
        merge_sort(indices, temp, mid + 1, right)
        merge_and_count(indices, temp, left, mid, right)

    n = len(nums)
    counts = [0] * n
    indices = list(range(n))
    temp = [0] * n

    merge_sort(indices, temp, 0, n - 1)
    return counts
```

### 2-2) Reverse Pairs (LC 493) —— 合併前先計數
> 在真正合併之前，先用雙指標數出跨兩半的配對（nums[i] > 2*nums[j]）。

```java
// LC 493 - Reverse Pairs
// IDEA: Merge sort — count i>2j pairs across halves first, then merge normally
// time = O(N log N), space = O(N)
public int reversePairs(int[] nums) {
    return mergeSort(nums, 0, nums.length - 1);
}
private int mergeSort(int[] nums, int l, int r) {
    if (l >= r) return 0;
    int mid = (l + r) / 2;
    int count = mergeSort(nums, l, mid) + mergeSort(nums, mid + 1, r);
    int j = mid + 1;
    for (int i = l; i <= mid; i++) {
        while (j <= r && nums[i] > 2L * nums[j]) j++;
        count += j - (mid + 1);
    }
    int[] tmp = new int[r - l + 1];
    int i = l, k = 0; j = mid + 1;
    while (i <= mid && j <= r) tmp[k++] = nums[i] <= nums[j] ? nums[i++] : nums[j++];
    while (i <= mid) tmp[k++] = nums[i++];
    while (j <= r)   tmp[k++] = nums[j++];
    System.arraycopy(tmp, 0, nums, l, tmp.length);
    return count;
}
```

```python
def reversePairs(nums):
    """Count pairs where nums[i] > 2 * nums[j] for i < j"""
    def merge_and_count(nums, temp, left, mid, right):
        # Count reverse pairs first
        count = 0
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)

        # Regular merge
        i, j, k = left, mid + 1, left
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp[k] = nums[i]
                i += 1
            else:
                temp[k] = nums[j]
                j += 1
            k += 1

        while i <= mid:
            temp[k] = nums[i]
            i += 1
            k += 1
        while j <= right:
            temp[k] = nums[j]
            j += 1
            k += 1

        for i in range(left, right + 1):
            nums[i] = temp[i]

        return count

    def merge_sort_and_count(nums, temp, left, right):
        count = 0
        if left < right:
            mid = (left + right) // 2
            count += merge_sort_and_count(nums, temp, left, mid)
            count += merge_sort_and_count(nums, temp, mid + 1, right)
            count += merge_and_count(nums, temp, left, mid, right)
        return count

    temp = [0] * len(nums)
    return merge_sort_and_count(nums[:], temp, 0, len(nums) - 1)
```

### 2-3) Count of Range Sum (LC 327) —— 對前綴和做合併排序
> 先建前綴和陣列；再數出跨兩半、且 (prefixSum[j] - prefixSum[i]) 落在 [lower, upper] 的配對。

```java
// LC 327 - Count of Range Sum
// IDEA: Merge sort on prefix sums; count cross pairs in [lower, upper] before merging
// time = O(N log N), space = O(N)
public int countRangeSum(int[] nums, int lower, int upper) {
    int n = nums.length;
    long[] prefix = new long[n + 1];
    for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + nums[i];
    return mergeSort(prefix, 0, n, lower, upper);
}
private int mergeSort(long[] p, int l, int r, int lo, int hi) {
    if (l >= r) return 0;
    int mid = (l + r) / 2;
    int count = mergeSort(p, l, mid, lo, hi) + mergeSort(p, mid + 1, r, lo, hi);
    int j = mid + 1, k = mid + 1;
    for (int i = l; i <= mid; i++) {
        while (j <= r && p[j] - p[i] < lo) j++;
        while (k <= r && p[k] - p[i] <= hi) k++;
        count += k - j;
    }
    long[] tmp = new long[r - l + 1];
    int i = l, a = mid + 1, idx = 0;
    while (i <= mid && a <= r) tmp[idx++] = p[i] <= p[a] ? p[i++] : p[a++];
    while (i <= mid) tmp[idx++] = p[i++];
    while (a <= r)   tmp[idx++] = p[a++];
    System.arraycopy(tmp, 0, p, l, tmp.length);
    return count;
}
```

```python
def countRangeSum(nums, lower, upper):
    """Count subarrays with sum in [lower, upper]"""
    def merge_and_count(prefix_sums, temp, left, mid, right):
        count = 0
        j = k = mid + 1

        for i in range(left, mid + 1):
            # Find range where prefix_sums[x] - prefix_sums[i] is in [lower, upper]
            while j <= right and prefix_sums[j] - prefix_sums[i] < lower:
                j += 1
            while k <= right and prefix_sums[k] - prefix_sums[i] <= upper:
                k += 1
            count += k - j

        # Merge sorted arrays
        i, j, p = left, mid + 1, left
        while i <= mid and j <= right:
            if prefix_sums[i] <= prefix_sums[j]:
                temp[p] = prefix_sums[i]
                i += 1
            else:
                temp[p] = prefix_sums[j]
                j += 1
            p += 1

        while i <= mid:
            temp[p] = prefix_sums[i]
            i += 1
            p += 1
        while j <= right:
            temp[p] = prefix_sums[j]
            j += 1
            p += 1

        for i in range(left, right + 1):
            prefix_sums[i] = temp[i]

        return count

    def divide_and_conquer(prefix_sums, temp, left, right):
        if left >= right:
            return 0

        mid = (left + right) // 2
        count = divide_and_conquer(prefix_sums, temp, left, mid)
        count += divide_and_conquer(prefix_sums, temp, mid + 1, right)
        count += merge_and_count(prefix_sums, temp, left, mid, right)
        return count

    # Build prefix sum array
    prefix_sums = [0]
    for num in nums:
        prefix_sums.append(prefix_sums[-1] + num)

    temp = [0] * len(prefix_sums)
    return divide_and_conquer(prefix_sums, temp, 0, len(prefix_sums) - 1)
```

## 進階技巧

### 最佳化策略
```python
def divide_and_conquer_optimizations():
    """Various optimization techniques for D&C"""

    # 1. In-place operations to reduce space
    def in_place_merge(arr, left, mid, right):
        # Reduce auxiliary space usage
        pass

    # 2. Iterative bottom-up approach
    def iterative_merge_sort(arr):
        n = len(arr)
        size = 1
        while size < n:
            left = 0
            while left < n - 1:
                mid = min(left + size - 1, n - 1)
                right = min(left + 2 * size - 1, n - 1)
                # merge(arr, left, mid, right)
                left += 2 * size
            size *= 2

    # 3. Parallel divide and conquer
    def parallel_merge_sort(arr):
        # Use threading for large datasets
        pass

    # 4. Hybrid approach with insertion sort for small subarrays
    def hybrid_merge_sort(arr, threshold=10):
        if len(arr) <= threshold:
            return insertion_sort(arr)
        # Regular merge sort for larger arrays
```

### 自訂合併邏輯的模式
```python
class AdvancedMergePatterns:
    """Advanced merge logic for specific problems"""

    def merge_with_multiple_conditions(self, arr1, arr2):
        """Merge with multiple counting conditions"""
        result = []
        i = j = 0
        counts = {"condition1": 0, "condition2": 0}

        while i < len(arr1) and j < len(arr2):
            if self.condition1(arr1[i], arr2[j]):
                counts["condition1"] += len(arr2) - j
            if self.condition2(arr1[i], arr2[j]):
                counts["condition2"] += len(arr2) - j

            if arr1[i] <= arr2[j]:
                result.append(arr1[i])
                i += 1
            else:
                result.append(arr2[j])
                j += 1

        return result + arr1[i:] + arr2[j:], counts

    def merge_with_reconstruction(self, left_part, right_part):
        """Merge while reconstructing array with specific properties"""
        # Custom merge logic for array reconstruction problems
        pass
```

## 效能最佳化提示

### 時間複雜度分析
```python
def complexity_analysis():
    """Analyze time complexity of different D&C approaches"""

    # Standard divide and conquer: T(n) = 2T(n/2) + O(n) = O(n log n)
    # With k-way division: T(n) = kT(n/k) + O(n) = O(n log n) if k is constant
    # With additional work per level: T(n) = 2T(n/2) + O(n^c)
    #   - If c < 1: O(n log n)
    #   - If c = 1: O(n log n)
    #   - If c > 1: O(n^c)

    pass
```

### 空間最佳化
```python
def space_optimizations():
    """Techniques to reduce space complexity"""

    # 1. Reuse auxiliary arrays
    def reuse_temp_array(arr):
        temp = [0] * len(arr)  # Create once, reuse everywhere
        # Pass temp to all recursive calls

    # 2. In-place merge (complex but saves space)
    def in_place_merge_technique(arr, left, mid, right):
        # Advanced in-place merging algorithms
        pass

    # 3. Iterative approach to eliminate recursion stack
    def iterative_divide_conquer(arr):
        # Bottom-up approach to save stack space
        pass
```

## 總結與速查

### 常見的分治模式

| 模式 | 模板 | 適用情境 | 範例 |
|---------|----------|----------|---------|
| **基本逆序對** | 改寫合併排序 | 計算逆序對 | 單純逆序對計數 |
| **帶條件計數** | 合併時加條件 | 特定配對條件 | Reverse pairs |
| **區間查詢** | 前綴和 + 分治 | 區間和問題 | Count range sum |
| **重建** | 邊合併邊建構 | 陣列建構 | 建出有序陣列 |

### 時間複雜度對照
| 題型 | 時間複雜度 | 空間複雜度 | 備註 |
|--------------|-----------------|------------------|-------|
| 基本逆序對 | O(n log n) | O(n) | 標準合併排序 |
| 帶條件逆序對 | O(n log n) | O(n) | 多了條件檢查 |
| 區間和 | O(n log n) | O(n) | 需先做前綴和預處理 |
| 重建 | O(n log n) | O(n) | 可能還要額外結構 |

### 常見錯誤與提示

**🚫 常見錯誤：**
- 合併步驟忘了處理邊界情況
- 合併時索引管理寫錯
- 需要保留原陣列時卻改動了它
- 合併裡的條件檢查寫得沒效率

**✅ 最佳實務：**
- 合併操作一律用輔助陣列
- 小心處理左右邊界
- 把合併步驟的條件檢查最佳化
- 輸入極大時考慮改成迭代寫法
- 相對順序有意義時，要用穩定排序

### 面試提示
1. **認出分治的機會**：看到逆序對計數、區間查詢就要想到
2. **練熟合併邏輯**：關鍵全在客製化的合併步驟
3. **索引要小心**：差一錯誤非常常見
4. **考慮時空權衡**：輔助空間 vs 原地操作
5. **多練合併的變形**：不同的計數／重建邏輯
6. **測邊界情況**：空陣列、單一元素、重複值

這份分治法速查涵蓋了高效解決複雜計數與區間查詢問題最重要的模式與技巧。

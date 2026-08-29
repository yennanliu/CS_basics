# 排序演算法與技巧

> **範圍** — 排序(sorting)演算法與周邊技巧 — 比較式排序與其穩定性、計數／桶／基數排序、quickselect、自訂比較器，以及循環排序(cyclic sort)。
> **另見**：[heap.md](./heap.md) — 堆積排序與 top-k；[binary_search.md](./binary_search.md) — 排序之後能做什麼；[advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — 用合併排序來「計數」而不是排序；[greedy.md](./greedy.md) — 先排序再掃描。

<p align="center"><img src="../pic/sort_cheatsheet.png"></p>

## LeetCode 題目清單

- [Sorting](https://leetcode.com/problem-list/sorting/)
- [Merge Sort](https://leetcode.com/problem-list/merge-sort/)
- [Counting Sort](https://leetcode.com/problem-list/counting-sort/)
- [Bucket Sort](https://leetcode.com/problem-list/bucket-sort/)
- [Radix Sort](https://leetcode.com/problem-list/radix-sort/)
- [Quickselect](https://leetcode.com/problem-list/quickselect/)

## 總覽
**排序(sorting)**就是把元素依特定順序（遞增或遞減）排好的過程。它是許多演算法與資料結構的基礎，讓搜尋、資料分析與解題都能更有效率。

### 關鍵性質
- **穩定性**：保持相等元素之間的相對順序
- **原地**：只用 O(1) 額外空間
- **適應性**：對部分已排序的資料表現更好
- **何時使用**：資料排序、為二分搜尋做前處理、找中位數／百分位數

### 演算法選擇指南
- **小型資料集（n < 50）**：插入排序
- **通用場合**：快速排序、合併排序
- **保證 O(n log n)**：堆積排序、合併排序
- **接近已排序**：插入排序、氣泡排序
- **值域有限**：計數排序、基數排序

### 參考資料
- [Neetcode Sort cheatsheet](https://neetcode.io/courses/lessons/sorting-algorithms)
- [Sorting Visualizations](https://visualgo.net/en/sorting)
- [Princeton Algorithms](https://algs4.cs.princeton.edu/20sorting/)


| **排序演算法** | **時間複雜度（最佳情況）** | **時間複雜度（平均情況）** | **時間複雜度（最壞情況）** | **空間複雜度** |
|-----------------------|-------------------------------|-----------------------------------|---------------------------------|----------------------|
| **氣泡排序 Bubble Sort**        | O(n)                          | O(n²)                             | O(n²)                           | O(1)                 |
| **插入排序 Insertion Sort**     | O(n)                          | O(n²)                             | O(n²)                           | O(1)                 |
| **選擇排序 Selection Sort**     | O(n²)                         | O(n²)                             | O(n²)                           | O(1)                 |
| **合併排序 Merge Sort**         | O(n log n)                    | O(n log n)                        | O(n log n)                      | O(n)                 |
| **快速排序 Quick Sort**         | O(n log n)                    | O(n log n)                        | O(n²)                           | O(log n)             |
| **堆積排序 Heap Sort**          | O(n log n)                    | O(n log n)                        | O(n log n)                      | O(1)                 |
| **計數排序 Counting Sort**      | O(n + k)                      | O(n + k)                          | O(n + k)                        | O(k)                 |
| **基數排序 Radix Sort**         | O(nk)                         | O(nk)                             | O(nk)                           | O(n + k)             |
| **桶排序 Bucket Sort**        | O(n + k)                      | O(n + k)                          | O(n²)                           | O(n)                 |


## 題型分類

### **模式 1：自訂比較器排序** — LC 179
- **描述**：用自訂規則或多重條件來排序
- **範例**：LC 179, 791, 937, 1029, 1366
- **模式**：為複雜的排序規則定義比較函式

### **模式 2：拓撲排序** — LC 207
- **描述**：依相依關係決定元素順序
- **範例**：LC 207, 210, 269, 310, 1136
- **模式**：DFS/BFS 搭配入度追蹤

### **模式 3：區間排序** — LC 56
- **描述**：把區間排序以便合併／處理
- **範例**：LC 56, 57, 252, 253, 435
- **模式**：依起點排序，然後逐一處理

### **模式 4：第 K 個元素** — LC 215
- **描述**：有效率地找出第 k 小／第 k 大的元素
- **範例**：LC 215, 347, 378, 658, 973
- **模式**：Quick Select 或堆積(heap)

### **模式 5：桶／計數排序** — LC 164
- **描述**：值域有限時的排序
- **範例**：LC 164, 274, 451, 1122, 1636
- **模式**：把值當成索引

### **模式 6：合併排序的應用** — LC 148
- **描述**：搭配排序的分治法
- **範例**：LC 23, 148, 315, 327, 493
- **模式**：合併已排序的序列

### **模式 7：貪婪配對（排序 + 雙指標）** — LC 1877
- **描述**：先排序，再把最小的和最大的配成一對，讓每對的和平衡並最小化其中的最大值
- **核心想法**：把大數字配在一起會造出不必要的大和；把兩端（最小 + 最大）配起來能把重量平均分散
- **範例**：LC 1877, 561, 881, 2491
- **模式**：排序 → 從兩端出發的雙指標 → 追蹤各配對結果的最大／最小值

## 模板與演算法

### 演算法比較表
| 演算法 | 最佳 | 平均 | 最壞 | 空間 | 穩定 | 何時使用 |
|-----------|------|---------|-------|-------|--------|-------------|
| **快速排序 Quick Sort** | O(n log n) | O(n log n) | O(n²) | O(log n) | 否 | 通用場合 |
| **合併排序 Merge Sort** | O(n log n) | O(n log n) | O(n log n) | O(n) | 是 | 需要穩定、保證 O(n log n) |
| **堆積排序 Heap Sort** | O(n log n) | O(n log n) | O(n log n) | O(1) | 否 | 原地、保證 O(n log n) |
| **插入排序 Insertion Sort** | O(n) | O(n²) | O(n²) | O(1) | 是 | 資料量小或接近已排序 |
| **計數排序 Counting Sort** | O(n+k) | O(n+k) | O(n+k) | O(k) | 是 | 值域有限的整數 |
| **基數排序 Radix Sort** | O(nk) | O(nk) | O(nk) | O(n+k) | 是 | 固定位寬的整數 |

### 模板 1：快速排序
```python
# Python - Classic Quick Sort
def quickSort(arr, low=0, high=None):
    if high is None:
        high = len(arr) - 1
    
    if low < high:
        # Partition and get pivot index
        pi = partition(arr, low, high)
        
        # Recursively sort left and right
        quickSort(arr, low, pi - 1)
        quickSort(arr, pi + 1, high)
    
    return arr

def partition(arr, low, high):
    # Choose rightmost as pivot
    pivot = arr[high]
    i = low - 1  # Smaller element index
    
    for j in range(low, high):
        if arr[j] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]
    
    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    return i + 1

# 3-way Quick Sort for duplicates
def quickSort3Way(arr, low=0, high=None):
    if high is None:
        high = len(arr) - 1
    
    if low < high:
        lt, gt = partition3Way(arr, low, high)
        quickSort3Way(arr, low, lt - 1)
        quickSort3Way(arr, gt + 1, high)
    
    return arr

def partition3Way(arr, low, high):
    pivot = arr[low]
    i = low
    lt = low
    gt = high
    
    while i <= gt:
        if arr[i] < pivot:
            arr[lt], arr[i] = arr[i], arr[lt]
            lt += 1
            i += 1
        elif arr[i] > pivot:
            arr[i], arr[gt] = arr[gt], arr[i]
            gt -= 1
        else:
            i += 1
    
    return lt, gt
```

```java
// Java - Quick Sort
public void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}

private int partition(int[] arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;
    
    for (int j = low; j < high; j++) {
        if (arr[j] <= pivot) {
            i++;
            swap(arr, i, j);
        }
    }
    
    swap(arr, i + 1, high);
    return i + 1;
}

private void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}
```

#### **變形 — 只跑 `partition3Way` 一次（荷蘭國旗問題）— LC 75**

**變化點**：當字母集是固定的極小集合（`{0,1,2}`）時，你根本不用遞迴 — **單獨一次** 3-way partition 就能在 O(n) / O(1) 內把整個陣列排好。

```java
// java
// LC 75 - Sort Colors
// time = O(n), space = O(1)
// IDEA: Dutch National Flag — 3 pointers lo / i / hi.
//   Invariant:  [0, lo)  == 0   |   [lo, i) == 1   |   (hi, n-1] == 2
//   KEY TRAP: after swapping with `hi`, do NOT advance i — the value pulled
//             in from the back has not been examined yet.
public void sortColors(int[] nums) {
    int lo = 0, i = 0, hi = nums.length - 1;
    while (i <= hi) {
        if (nums[i] == 0) {
            int t = nums[lo]; nums[lo] = nums[i]; nums[i] = t;
            lo++; i++;                 // safe: nums[lo] was a 1 (already seen)
        } else if (nums[i] == 2) {
            int t = nums[hi]; nums[hi] = nums[i]; nums[i] = t;
            hi--;                      // NOTE: i stays put
        } else {
            i++;
        }
    }
}
```

```python
# python
# LC 75 - Sort Colors
# time = O(n), space = O(1)
# IDEA: Dutch National Flag one-pass 3-way partition
class Solution:
    def sortColors(self, nums):
        lo, i, hi = 0, 0, len(nums) - 1
        while i <= hi:
            if nums[i] == 0:
                nums[lo], nums[i] = nums[i], nums[lo]
                lo += 1
                i += 1
            elif nums[i] == 2:
                nums[hi], nums[i] = nums[i], nums[hi]
                hi -= 1          # NOTE: do NOT advance i here
            else:
                i += 1
```

> 常見的追問是：「不用計數排序（兩次掃描）做得到嗎？」→ 上面這個一次掃描的 DNF 就是預期答案。

### 模板 2：合併排序
```python
# Python - Merge Sort
def mergeSort(arr):
    if len(arr) <= 1:
        return arr
    
    mid = len(arr) // 2
    left = mergeSort(arr[:mid])
    right = mergeSort(arr[mid:])
    
    return merge(left, right)

def merge(left, right):
    result = []
    i = j = 0
    
    while i < len(left) and j < len(right):
        if left[i] <= right[j]:
            result.append(left[i])
            i += 1
        else:
            result.append(right[j])
            j += 1
    
    result.extend(left[i:])
    result.extend(right[j:])
    return result

# In-place merge sort
def mergeSortInPlace(arr, left=0, right=None):
    if right is None:
        right = len(arr) - 1
    
    if left < right:
        mid = (left + right) // 2
        mergeSortInPlace(arr, left, mid)
        mergeSortInPlace(arr, mid + 1, right)
        mergeInPlace(arr, left, mid, right)
    
    return arr

def mergeInPlace(arr, left, mid, right):
    left_arr = arr[left:mid + 1]
    right_arr = arr[mid + 1:right + 1]
    
    i = j = 0
    k = left
    
    while i < len(left_arr) and j < len(right_arr):
        if left_arr[i] <= right_arr[j]:
            arr[k] = left_arr[i]
            i += 1
        else:
            arr[k] = right_arr[j]
            j += 1
        k += 1
    
    while i < len(left_arr):
        arr[k] = left_arr[i]
        i += 1
        k += 1
    
    while j < len(right_arr):
        arr[k] = right_arr[j]
        j += 1
        k += 1
```

```java
// Java - Merge Sort
public void mergeSort(int[] arr, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        
        merge(arr, left, mid, right);
    }
}

private void merge(int[] arr, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;
    
    int[] leftArr = new int[n1];
    int[] rightArr = new int[n2];
    
    for (int i = 0; i < n1; i++) {
        leftArr[i] = arr[left + i];
    }
    for (int j = 0; j < n2; j++) {
        rightArr[j] = arr[mid + 1 + j];
    }
    
    int i = 0, j = 0, k = left;
    
    while (i < n1 && j < n2) {
        if (leftArr[i] <= rightArr[j]) {
            arr[k++] = leftArr[i++];
        } else {
            arr[k++] = rightArr[j++];
        }
    }
    
    while (i < n1) {
        arr[k++] = leftArr[i++];
    }
    
    while (j < n2) {
        arr[k++] = rightArr[j++];
    }
}
```

#### **變形 — 由後往前原地合併 — LC 88**

**變化點**：`nums1` 尾端本來就有空位，所以沒有空間放標準 `merge` 用的 O(n) 緩衝區。改成從**尾端**開始填（先放最大的），你寫入的每個位置不是空的、就是已經被取用過 → 額外空間 O(1)。

```java
// java
// LC 88 - Merge Sorted Array
// time = O(m + n), space = O(1)
// IDEA: write from the back. k = last free slot, i/j = last real elems.
//   Loop on `j` only: if nums2 is exhausted, the nums1 prefix is already in place.
public void merge(int[] nums1, int m, int[] nums2, int n) {
    int i = m - 1, j = n - 1, k = m + n - 1;
    while (j >= 0) {
        if (i >= 0 && nums1[i] > nums2[j]) {
            nums1[k--] = nums1[i--];
        } else {
            nums1[k--] = nums2[j--];
        }
    }
}
```

```python
# python
# LC 88 - Merge Sorted Array
# time = O(m + n), space = O(1)
# IDEA: fill nums1 from the tail so we never overwrite an unread element
class Solution:
    def merge(self, nums1, m, nums2, n):
        i, j, k = m - 1, n - 1, m + n - 1
        while j >= 0:
            if i >= 0 and nums1[i] > nums2[j]:
                nums1[k] = nums1[i]
                i -= 1
            else:
                nums1[k] = nums2[j]
                j -= 1
            k -= 1
```

> **為什麼要倒著走？** 正向合併會在讀取 `nums1[0..m-1]` 之前就把它覆蓋掉，逼你多做一次 O(m) 的複製。這種「從最大的那端開始填」的技巧會在所有原地合併中重現（例如 LC 148 Sort List 的合併步驟，就是基於同樣理由改用指標重接）。

### 模板 3：自訂比較器排序 — LC 179
```python
# Python - Custom sorting
class Solution:
    def customSort(self, items):
        # Single key
        items.sort(key=lambda x: x[0])
        
        # Multiple keys
        items.sort(key=lambda x: (x[0], -x[1], x[2]))
        
        # Complex comparison
        def compare(item):
            # Return tuple of sort keys
            if condition:
                return (0, item.value, item.name)
            else:
                return (1, -item.priority, item.id)
        
        items.sort(key=compare)
        
        # Using functools for traditional comparison
        from functools import cmp_to_key
        
        def compare_func(a, b):
            if a < b:
                return -1
            elif a > b:
                return 1
            else:
                return 0
        
        items.sort(key=cmp_to_key(compare_func))
        
        return items

# Custom class for sorting
class CustomComparable:
    def __init__(self, value, priority):
        self.value = value
        self.priority = priority
    
    def __lt__(self, other):
        # Define less than for sorting
        if self.priority != other.priority:
            return self.priority > other.priority
        return self.value < other.value
```

```java
// Java - Custom comparator
public void customSort(List<Item> items) {
    // Lambda comparator
    items.sort((a, b) -> a.value - b.value);
    
    // Multiple criteria
    items.sort((a, b) -> {
        if (a.priority != b.priority) {
            return b.priority - a.priority;  // Descending
        }
        return a.name.compareTo(b.name);    // Ascending
    });
    
    // Using Comparator methods
    items.sort(Comparator
        .comparingInt(Item::getPriority).reversed()
        .thenComparing(Item::getName));
    
    // Custom Comparator class
    items.sort(new Comparator<Item>() {
        @Override
        public int compare(Item a, Item b) {
            // Custom logic
            return customCompare(a, b);
        }
    });
}

// Comparable interface
class Item implements Comparable<Item> {
    int value;
    String name;
    
    @Override
    public int compareTo(Item other) {
        if (this.value != other.value) {
            return this.value - other.value;
        }
        return this.name.compareTo(other.name);
    }
}
```

### 模板 4：Quick Select（第 K 個元素）— LC 215
```python
# Python - Quick Select for k-th smallest
def quickSelect(arr, k):
    # Find k-th smallest (0-indexed)
    return quickSelectHelper(arr, 0, len(arr) - 1, k - 1)

def quickSelectHelper(arr, left, right, k):
    if left == right:
        return arr[left]
    
    # Random pivot for better average case
    import random
    pivot_idx = random.randint(left, right)
    pivot_idx = partition(arr, left, right, pivot_idx)
    
    if k == pivot_idx:
        return arr[k]
    elif k < pivot_idx:
        return quickSelectHelper(arr, left, pivot_idx - 1, k)
    else:
        return quickSelectHelper(arr, pivot_idx + 1, right, k)

def partition(arr, left, right, pivot_idx):
    pivot = arr[pivot_idx]
    # Move pivot to end
    arr[pivot_idx], arr[right] = arr[right], arr[pivot_idx]
    
    store_idx = left
    for i in range(left, right):
        if arr[i] < pivot:
            arr[store_idx], arr[i] = arr[i], arr[store_idx]
            store_idx += 1
    
    # Move pivot to final position
    arr[store_idx], arr[right] = arr[right], arr[store_idx]
    return store_idx
```

### 模板 5：計數排序
```python
# Python - Counting Sort
def countingSort(arr, max_val=None):
    if not arr:
        return arr
    
    if max_val is None:
        max_val = max(arr)
    min_val = min(arr)
    
    # Create counting array
    range_size = max_val - min_val + 1
    count = [0] * range_size
    
    # Count occurrences
    for num in arr:
        count[num - min_val] += 1
    
    # Reconstruct sorted array
    idx = 0
    for i in range(range_size):
        while count[i] > 0:
            arr[idx] = i + min_val
            idx += 1
            count[i] -= 1
    
    return arr

# Stable counting sort
def stableCountingSort(arr):
    if not arr:
        return arr
    
    max_val = max(arr)
    min_val = min(arr)
    range_size = max_val - min_val + 1
    
    count = [0] * range_size
    output = [0] * len(arr)
    
    # Count occurrences
    for num in arr:
        count[num - min_val] += 1
    
    # Cumulative count
    for i in range(1, range_size):
        count[i] += count[i - 1]
    
    # Build output array (stable)
    for i in range(len(arr) - 1, -1, -1):
        output[count[arr[i] - min_val] - 1] = arr[i]
        count[arr[i] - min_val] -= 1
    
    return output
```

### 模板 6：拓撲排序 — LC 207
```python
# Python - Topological Sort (Kahn's Algorithm)
def topologicalSort(numNodes, edges):
    # Build graph and in-degree
    graph = defaultdict(list)
    in_degree = [0] * numNodes
    
    for u, v in edges:
        graph[u].append(v)
        in_degree[v] += 1
    
    # Queue for nodes with no dependencies
    queue = deque([i for i in range(numNodes) if in_degree[i] == 0])
    result = []
    
    while queue:
        node = queue.popleft()
        result.append(node)
        
        for neighbor in graph[node]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)
    
    # Check for cycle
    if len(result) != numNodes:
        return []  # Cycle detected
    
    return result

# DFS-based Topological Sort
def topologicalSortDFS(numNodes, edges):
    graph = defaultdict(list)
    for u, v in edges:
        graph[u].append(v)
    
    visited = set()
    stack = []
    
    def dfs(node):
        visited.add(node)
        for neighbor in graph[node]:
            if neighbor not in visited:
                dfs(neighbor)
        stack.append(node)
    
    for i in range(numNodes):
        if i not in visited:
            dfs(i)
    
    return stack[::-1]
```

### 模板 7：把合併排序當成**計數器**（逆序對／右側較小元素）— LC 315 ⭐⭐⭐⭐⭐

**關鍵想法**：合併步驟是唯一一個「已知整整一批右半邊元素都**小於**某個左半邊元素」的時刻。在那個時刻順手掛上一個計數器，你就能用 O(n log n) 而不是 O(n²) 數出跨越整個陣列的配對數。

**遞迴式**：`answer(lo..hi) = answer(left) + answer(right) + cross-pairs counted during merge`

**關鍵細節**：你必須排序一個**索引**陣列，而不是值 — 答案要依原始位置回報，而值會被排序打亂。

```java
// java
// LC 315 - Count of Smaller Numbers After Self
// time = O(n log n), space = O(n)
// IDEA: merge sort over an INDEX array. While merging, `moved` = how many
//       right-half elements have already been emitted. Every time we emit a
//       LEFT element, exactly `moved` smaller elements sat after it → add.
public List<Integer> countSmaller(int[] nums) {
    int n = nums.length;
    int[] count = new int[n];
    int[] idx = new int[n];
    for (int i = 0; i < n; i++) idx[i] = i;

    sortCount(nums, idx, new int[n], 0, n - 1, count);

    List<Integer> res = new ArrayList<>();
    for (int c : count) res.add(c);
    return res;
}

private void sortCount(int[] nums, int[] idx, int[] tmp, int lo, int hi, int[] count) {
    if (lo >= hi) return;
    int mid = lo + (hi - lo) / 2;
    sortCount(nums, idx, tmp, lo, mid, count);
    sortCount(nums, idx, tmp, mid + 1, hi, count);

    int i = lo, j = mid + 1, k = lo;
    int moved = 0;                          // # of right-half elems already merged
    while (i <= mid && j <= hi) {
        if (nums[idx[j]] < nums[idx[i]]) {  // strict `<` keeps the sort stable
            moved++;
            tmp[k++] = idx[j++];
        } else {
            count[idx[i]] += moved;         // <-- the whole trick
            tmp[k++] = idx[i++];
        }
    }
    while (i <= mid) { count[idx[i]] += moved; tmp[k++] = idx[i++]; }
    while (j <= hi)  { tmp[k++] = idx[j++]; }
    for (int t = lo; t <= hi; t++) idx[t] = tmp[t];
}
```

```python
# python
# LC 315 - Count of Smaller Numbers After Self
# time = O(n log n), space = O(n)
# IDEA: merge sort the INDEX list; `moved` counts right-half elements already
#       emitted, which are exactly the smaller-and-to-the-right ones.
class Solution:
    def countSmaller(self, nums):
        n = len(nums)
        count = [0] * n
        idx = list(range(n))

        def sort_count(lo, hi):
            if lo >= hi:
                return
            mid = (lo + hi) // 2
            sort_count(lo, mid)
            sort_count(mid + 1, hi)

            i, j, moved, tmp = lo, mid + 1, 0, []
            while i <= mid and j <= hi:
                if nums[idx[j]] < nums[idx[i]]:   # right elem is smaller
                    moved += 1
                    tmp.append(idx[j]); j += 1
                else:
                    count[idx[i]] += moved        # <-- the whole trick
                    tmp.append(idx[i]); i += 1
            while i <= mid:
                count[idx[i]] += moved
                tmp.append(idx[i]); i += 1
            while j <= hi:
                tmp.append(idx[j]); j += 1
            idx[lo:hi + 1] = tmp

        sort_count(0, n - 1)
        return count
```

**圖解追蹤** — `nums = [5,2,6,1]`，最後一次把 `[2,5]`（索引 1,0）和 `[1,6]`（索引 3,2）合併：

```text
left = [2(i1), 5(i0)]      right = [1(i3), 6(i2)]      moved = 0
 step 1: 1 < 2   -> emit right, moved = 1
 step 2: 2 <= 6  -> emit left  , count[1] += 1   -> count[1] = 1
 step 3: 5 <= 6  -> emit left  , count[0] += 1   -> count[0] = 2 (1 came from the earlier level)
 step 4: drain right
answer = [2,1,1,0]
```

**同一套骨架，不同的計數判定：**

| 題目 | LC # | 合併時你在數什麼 |
|---------|------|-----------------------------|
| Count of Smaller Numbers After Self | 315 | 右邊 `<` 左邊元素的個數 |
| Reverse Pairs | 493 | 滿足 `left > 2 * right` 的配對（合併前要多掃一次） |
| Count of Range Sum | 327 | 差值落在 `[lower, upper]` 內的前綴和配對 |

---

### 模板 8：依值域做桶排序（`bucket = value / width`）— LC 220 ⭐⭐⭐⭐

**關鍵想法**：當題目問的是「有沒有兩個值相差**至多** `t`？」時，把桶寬設成 `t + 1`。於是：
- 落在**同一個**桶的兩個值必定相差 ≤ `t` → 直接命中
- 相差超過 `t` 的值只可能落在**相鄰**的桶 → 你永遠只需要檢查 `id-1`、`id`、`id+1`

這就把 O(n log k) 的平衡 BST／滑動視窗排序解法變成 O(n)。

```java
// java
// LC 220 - Contains Duplicate III
// time = O(n), space = O(min(n, indexDiff))
// IDEA: bucket width = valueDiff + 1, so "same bucket" == "within valueDiff".
//       Keep only the last `indexDiff` elements as a sliding window of buckets.
public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
    if (indexDiff <= 0 || valueDiff < 0) return false;
    long w = (long) valueDiff + 1;                 // NOTE: long, valueDiff can be MAX_VALUE
    Map<Long, Long> bucket = new HashMap<>();

    for (int i = 0; i < nums.length; i++) {
        long id = bucketId(nums[i], w);
        if (bucket.containsKey(id)) return true;                                        // same bucket
        if (bucket.containsKey(id - 1) && nums[i] - bucket.get(id - 1) <= valueDiff) return true;
        if (bucket.containsKey(id + 1) && bucket.get(id + 1) - nums[i] <= valueDiff) return true;

        bucket.put(id, (long) nums[i]);            // at most 1 value per bucket in the window
        if (i >= indexDiff) bucket.remove(bucketId(nums[i - indexDiff], w));  // slide
    }
    return false;
}

// KEY TRAP: Java integer division truncates toward zero, so -1/3 == 0 == 2/3.
//           Negative values need an explicit floor.
private long bucketId(long x, long w) {
    return x < 0 ? (x + 1) / w - 1 : x / w;
}
```

```python
# python
# LC 220 - Contains Duplicate III
# time = O(n), space = O(min(n, indexDiff))
# IDEA: bucket width = valueDiff + 1; check own bucket + 2 neighbours only.
#       Python's // already floors, so negatives need no special case.
class Solution:
    def containsNearbyAlmostDuplicate(self, nums, indexDiff, valueDiff):
        if indexDiff <= 0 or valueDiff < 0:
            return False
        w = valueDiff + 1
        bucket = {}
        for i, x in enumerate(nums):
            bid = x // w
            if bid in bucket:
                return True
            if bid - 1 in bucket and x - bucket[bid - 1] <= valueDiff:
                return True
            if bid + 1 in bucket and bucket[bid + 1] - x <= valueDiff:
                return True
            bucket[bid] = x
            if i >= indexDiff:                     # slide the window
                bucket.pop(nums[i - indexDiff] // w, None)
        return False
```

**桶寬設計速查表**（這才是可重複使用的部分）：

| 目標 | 桶寬 | 為什麼 |
|------|--------------|-----|
| 「兩個值相差在 `t` 以內」（LC 220） | `t + 1` | 同桶 ⇒ 差 ≤ t；也只有鄰桶可能符合 |
| 「排序後相鄰值的最大間距」（LC 164） | `(max-min)/(n-1)` | 鴿籠原理 ⇒ 最大間距必定*跨*在桶與桶之間，所以桶內順序無關緊要 |
| 「依頻率取前 K 名」（LC 347） | 以頻率當索引，`1..n` | 頻率範圍被 n 限制住 ⇒ 直接當索引用 |

---

### 模板 9：用**衍生鍵**排序來解鎖貪婪／DP — LC 354 ⭐⭐⭐⭐⭐

**關鍵想法**：許多「二維」問題*只要挑對排序順序*，就會塌縮成一個已經解決的一維問題。排序本身就是演算法；而面試的成敗就落在平手時的處理規則上。

**模式**：第一維**遞增**排序，平手時第二維**遞減**排序 — 遞減的平手規則會讓第一維相同的項目彼此**無法**串接，因此對第二維直接做嚴格遞增掃描自動就是正確的。

```java
// java
// LC 354 - Russian Doll Envelopes
// time = O(n log n), space = O(n)
// IDEA: sort width ASC, height DESC on ties -> answer = LIS over heights.
//   WHY height DESC? envelopes [3,5] and [3,7] must never both be chosen.
//   With height DESC they appear as 7 then 5 (decreasing), so no increasing
//   subsequence can pick both. With height ASC you'd wrongly nest them.
public int maxEnvelopes(int[][] envelopes) {
    Arrays.sort(envelopes, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);

    int[] tails = new int[envelopes.length];   // tails[l] = min tail of an LIS of length l+1
    int len = 0;
    for (int[] e : envelopes) {
        int i = Arrays.binarySearch(tails, 0, len, e[1]);
        if (i < 0) i = -(i + 1);               // insertion point = lower_bound
        tails[i] = e[1];
        if (i == len) len++;
    }
    return len;
}
```

```python
# python
# LC 354 - Russian Doll Envelopes
# time = O(n log n), space = O(n)
# IDEA: sort (w ASC, h DESC) -> strictly-increasing LIS over heights
import bisect
class Solution:
    def maxEnvelopes(self, envelopes):
        # NOTE: -e[1] is the whole trick (blocks same-width nesting)
        envelopes.sort(key=lambda e: (e[0], -e[1]))
        tails = []
        for _, h in envelopes:
            i = bisect.bisect_left(tails, h)   # bisect_left => STRICTLY increasing
            if i == len(tails):
                tails.append(h)
            else:
                tails[i] = h
        return len(tails)
```

#### **變形 — 依衍生鍵排序，然後貪婪地「插入」— LC 406**

**變化點**：先依身高由高到低排序，這樣已經放好的人都 ≥ 當前這個人；於是 `k` 就正好是要插入的索引，因為之後插入的較矮的人永遠不會擾動先前那些人的計數。

```java
// java
// LC 406 - Queue Reconstruction by Height
// time = O(n^2) (list insert), space = O(n)
// IDEA: height DESC, k ASC. Insert person at index k — everyone already in
//       the list is taller/equal, so exactly k of them end up in front.
public int[][] reconstructQueue(int[][] people) {
    Arrays.sort(people, (a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);
    List<int[]> res = new ArrayList<>();
    for (int[] p : people) res.add(p[1], p);
    return res.toArray(new int[0][]);
}
```

```python
# python
# LC 406 - Queue Reconstruction by Height
# time = O(n^2), space = O(n)
# IDEA: sort (-h, k) then insert each person at index k
class Solution:
    def reconstructQueue(self, people):
        people.sort(key=lambda p: (-p[0], p[1]))
        res = []
        for p in people:
            res.insert(p[1], p)
        return res
```

#### **變形 — 依衍生鍵排序，然後對前驅做 DP — LC 1048**

**變化點**：依**長度**排序，這樣一個字的所有可能前驅都保證會先被處理 — 於是 DP 完全不需要遞迴，也不需要任何記憶化的順序邏輯。

```java
// java
// LC 1048 - Longest String Chain
// time = O(n * L^2), space = O(n * L)   (L = max word length)
// IDEA: sort by length -> predecessors always come first. For each word try
//       deleting each char and look the shorter word up in the dp map.
public int longestStrChain(String[] words) {
    Arrays.sort(words, (a, b) -> a.length() - b.length());
    Map<String, Integer> dp = new HashMap<>();
    int best = 0;
    for (String w : words) {
        int cur = 1;
        for (int i = 0; i < w.length(); i++) {
            String pre = w.substring(0, i) + w.substring(i + 1);
            cur = Math.max(cur, dp.getOrDefault(pre, 0) + 1);
        }
        dp.put(w, cur);
        best = Math.max(best, cur);
    }
    return best;
}
```

```python
# python
# LC 1048 - Longest String Chain
# time = O(n * L^2), space = O(n * L)
# IDEA: sort by length so predecessors are already in dp when we reach a word
class Solution:
    def longestStrChain(self, words):
        words.sort(key=len)
        dp = {}
        best = 0
        for w in words:
            cur = 1
            for i in range(len(w)):
                cur = max(cur, dp.get(w[:i] + w[i + 1:], 0) + 1)
            dp[w] = cur
            best = max(best, cur)
        return best
```

**衍生鍵排序順序 — 快速決策表：**

| 情境 | 排序順序 | 解鎖了什麼 |
|-----------|-----------|-------------|
| 二維嚴格巢狀（LC 354） | dim1 遞增，平手時 dim2 **遞減** | 對 dim2 做 LIS |
| 放置物品且「比它大的個數」很重要（LC 406） | 身高**遞減**、k 遞增 | 直接插入在索引 k |
| 前驅是「較小者」的 DP（LC 1048） | 依大小／長度遞增 | 正向 DP，不需記憶化 |
| 以串接結果比較（LC 179） | 自訂 `a+b` vs `b+a` | 直接串接起來 |

> **⚠️ 比較器遞移性陷阱**：自訂比較器必須構成*全序* — `compare(a,b) > 0 && compare(b,c) > 0` 必須推得 `compare(a,c) > 0`。不成立時 Java 的 TimSort 會丟出 `IllegalArgumentException: Comparison method violates its general contract!`。LC 179 的 `a+b` vs `b+a` 規則*是*可證明具遞移性的；而像「依哪個欄位不為零就用哪個排」這種臨時規則通常不是。另外請優先用 `Integer.compare(a, b)` 而不是 `a - b`（大值／負值會溢位）。

---

### 模板 10：循環排序（值是 `1..n` 的一個排列）— LC 645 ⭐⭐⭐⭐

**關鍵想法**：當值本身就是索引（`1..n` 或 `0..n-1`）時，你根本不需要比較式排序 — 反覆把每個值交換**回家**到 `index = value - 1`。每次交換都會永久定位一個值，所以總工作量是 O(n)、空間 O(1)。之後，任何值不對的索引就精準指出缺失／重複／放錯位置的元素。

```java
// java
// LC 645 - Set Mismatch
// time = O(n), space = O(1)
// IDEA: cyclic sort — send nums[i] to index nums[i]-1.
//   LOOP CONDITION: use `nums[i] != nums[nums[i]-1]` (compare VALUES), not
//   `nums[i] != i+1`. With duplicates the "home" slot is already taken, and
//   comparing values is what stops the swap loop from spinning forever.
public int[] findErrorNums(int[] nums) {
    int n = nums.length;
    for (int i = 0; i < n; i++) {
        while (nums[i] != nums[nums[i] - 1]) {
            int t = nums[i];
            nums[i] = nums[t - 1];
            nums[t - 1] = t;
        }
    }
    // now nums[i] should be i+1; the one that isn't gives both answers
    for (int i = 0; i < n; i++) {
        if (nums[i] != i + 1) return new int[]{nums[i], i + 1};  // {duplicated, missing}
    }
    return new int[]{-1, -1};
}
```

```python
# python
# LC 645 - Set Mismatch
# time = O(n), space = O(1)
# IDEA: cyclic sort, then scan for the index whose value is not i+1
class Solution:
    def findErrorNums(self, nums):
        n = len(nums)
        for i in range(n):
            # NOTE: compare VALUES (not nums[i] != i+1) so duplicates terminate
            while nums[i] != nums[nums[i] - 1]:
                t = nums[i]
                nums[i], nums[t - 1] = nums[t - 1], nums[i]
        for i in range(n):
            if nums[i] != i + 1:
                return [nums[i], i + 1]   # [duplicated, missing]
        return [-1, -1]
```

**什麼時候該拿出循環排序**：陣列長度是 `n`，**而且**值被限制在 `1..n`（或 `0..n-1`），而追問要求 O(n) 時間 / O(1) 空間（所以不能用 HashSet、不能用計數陣列）。變動的部分是排序後的掃描 — 「第一個不對的索引」就能回答缺失數／重複數／第一個缺失正整數這類問題。

## 依模式分類的題目

### 依模式整理的題目表

#### **自訂比較器題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Largest Number | 179 | 字串比較 | Medium |
| Custom Sort String | 791 | 字元順序 | Medium |
| Reorder Data in Log Files | 937 | 多重鍵排序 | Easy |
| Two City Scheduling | 1029 | 成本差 | Medium |
| Rank Teams by Votes | 1366 | 票數統計 | Medium |
| Sort Array by Parity | 905 | 奇偶分離 | Easy |
| Relative Sort Array | 1122 | 自訂順序 | Easy |

#### **拓撲排序題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Course Schedule | 207 | 環偵測 | Medium |
| Course Schedule II | 210 | 帶相依關係的排序 | Medium |
| Alien Dictionary | 269 | 字元順序 | Hard |
| Minimum Height Trees | 310 | 樹的重心 | Medium |
| Parallel Courses | 1136 | 依層級處理 | Medium |
| Sequence Reconstruction | 444 | 唯一排序 | Medium |

#### **區間排序題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Merge Intervals | 56 | 排序後合併 | Medium |
| Insert Interval | 57 | 二分搜尋插入 | Medium |
| Meeting Rooms | 252 | 重疊檢查 | Easy |
| Meeting Rooms II | 253 | 掃描線 | Medium |
| Non-overlapping Intervals | 435 | 貪婪移除 | Medium |
| Minimum Number of Arrows | 452 | 區間交集 | Medium |

#### **第 K 個元素題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Kth Largest Element | 215 | Quick select | Medium |
| Top K Frequent Elements | 347 | 桶排序 | Medium |
| Kth Smallest in Matrix | 378 | 二分搜尋 | Medium |
| Find K Closest Elements | 658 | 雙指標 | Medium |
| K Closest Points to Origin | 973 | Quick select | Medium |
| Kth Largest in Stream | 703 | 最小堆積 | Easy |

#### **計數／桶排序題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Maximum Gap | 164 | 桶排序 | Hard |
| H-Index | 274 | 計數排序 | Medium |
| Sort Characters By Frequency | 451 | 頻率桶 | Medium |
| Relative Sort Array | 1122 | 計數排序 | Easy |
| Sort Array by Frequency | 1636 | 自訂比較器 | Easy |

#### **合併排序應用題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Merge k Sorted Lists | 23 | K 路合併 | Hard |
| Sort List | 148 | 鏈結串列合併排序 | Medium |
| Count of Smaller Numbers | 315 | 合併排序併計數 | Hard |
| Count of Range Sum | 327 | 合併排序 | Hard |
| Reverse Pairs | 493 | 改造過的合併排序 | Hard |

#### **貪婪配對題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Minimize Maximum Pair Sum | 1877 | 排序 + 雙指標（最小＋最大配對） | Medium |
| Array Partition | 561 | 排序 + 相鄰兩兩配對 | Easy |
| Boats to Save People | 881 | 排序 + 貪婪雙指標 | Medium |
| Divide Players Into Teams | 2491 | 排序 + 最小配最大 | Medium |

#### **把排序當成一行前處理步驟**（不需要新模板）
| 題目 | LC # | 排序／計數技巧 | 難度 |
|---------|------|------------------|------------|
| Group Anagrams | 49 | 用 sorted(word)（或長度 26 的計數 tuple）當雜湊鍵 | Medium |
| Valid Anagram | 242 | 兩個字串都排序，或比較頻率表 | Easy |
| Contains Duplicate | 217 | 排序後檢查相鄰配對（用 HashSet 更好） | Easy |
| Minimum Increment to Make Array Unique | 945 | 排序後把每個值推到 `max(v, prev+1)` | Medium |
| Least Number of Unique Integers after K Removals | 1481 | 統計頻率、頻率遞增排序、先移除最稀有的 | Medium |

## 模式選擇策略

```text
Problem Analysis Flowchart:

1. Need custom ordering rules?
   ├── YES → Custom Comparator
   │         ├── Multiple criteria → Tuple comparison
   │         └── Complex logic → Comparison function
   └── NO → Continue to 2

2. Dealing with dependencies?
   ├── YES → Topological Sort
   │         ├── Detect cycle → Kahn's algorithm
   │         └── Find ordering → DFS approach
   └── NO → Continue to 3

3. Working with intervals?
   ├── YES → Sort by start/end
   │         ├── Merge overlapping → Greedy merge
   │         └── Find conflicts → Sweep line
   └── NO → Continue to 4

4. Finding k-th element?
   ├── YES → Quick Select or Heap
   │         ├── One-time query → Quick select O(n)
   │         └── Multiple queries → Heap O(n log k)
   └── NO → Continue to 5

5. Limited value range?
   ├── YES → Counting/Bucket Sort
   │         ├── Integers → Counting sort
   │         └── With precision → Bucket sort
   └── NO → Use standard sorting
```

## 總結與速查

### 複雜度速查
| 演算法 | 最佳情況 | 平均 | 最壞情況 | 空間 | 穩定 |
|-----------|-----------|---------|------------|-------|--------|
| Quick Sort | O(n log n) | O(n log n) | O(n²) | O(log n) | 否 |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) | 是 |
| Heap Sort | O(n log n) | O(n log n) | O(n log n) | O(1) | 否 |
| Tim Sort | O(n) | O(n log n) | O(n log n) | O(n) | 是 |
| Counting Sort | O(n+k) | O(n+k) | O(n+k) | O(k) | 是 |
| Radix Sort | O(nk) | O(nk) | O(nk) | O(n+k) | 是 |

### 模板速查
| 模板 | 模式 | 關鍵程式碼 |
|----------|---------|----------|
| **Quick Sort** | 分割 | `pivot; partition; recurse` |
| **Merge Sort** | 分割再合併 | `mid; merge(left, right)` |
| **Custom Sort** | 比較器 | `key=lambda x: criteria` |
| **Quick Select** | 第 K 個元素 | `partition until k` |
| **Counting Sort** | 值當索引 | `count[val]++` |
| **Topological** | 相依關係 | `in_degree; queue` |

### 常見模式與技巧

#### **Python 排序技巧**
```python
# Sort with multiple keys
items.sort(key=lambda x: (x[0], -x[1], x[2]))

# Sort by custom class
items.sort(key=lambda x: x.priority, reverse=True)

# Stable sort in multiple passes
items.sort(key=lambda x: x.secondary)  # First
items.sort(key=lambda x: x.primary)    # Then primary

# In-place vs new list
arr.sort()           # In-place
sorted_arr = sorted(arr)  # New list
```

#### **Java 排序技巧**
```java
// Lambda comparator
Arrays.sort(arr, (a, b) -> a - b);

// Method reference
Arrays.sort(arr, Integer::compare);

// Comparator chaining
Arrays.sort(items, Comparator
    .comparing(Item::getPriority)
    .thenComparing(Item::getName));

// Reverse order
Arrays.sort(arr, Collections.reverseOrder());
```

### 解題步驟

1. **判斷是否需要排序**
   - 真的需要排序嗎？
   - 可以只做部分排序嗎？
   - 需要穩定性嗎？

2. **選擇演算法**
   - 資料量大小
   - 值域範圍
   - 記憶體限制
   - 穩定性需求

3. **定義比較規則**
   - 單一鍵還是多重鍵？
   - 遞增還是遞減？
   - 特殊情況的處理

4. **必要時再最佳化**
   - 第 k 個元素用 quick select
   - 值域有限用計數排序
   - 分布均勻用桶排序

### 常見錯誤與提示

**🚫 常見錯誤：**
- 在自訂比較過程中修改陣列
- 比較器中的整數溢位（a - b）
- 比較器沒有處理相等的元素
- 需要穩定性時卻用了不穩定的排序
- 對大型資料集使用 O(n²) 的演算法

**✅ 最佳實務：**
- 大多數情況直接用內建排序
- 優先用 Integer.compare() 而不是相減
- 用重複元素與邊界情況測試
- 只需要 k 個元素時考慮部分排序
- 需要保持相等元素順序時用穩定排序

### 面試提示

1. **演算法選擇**
   - 先從內建排序開始
   - 有需要才最佳化
   - 說明時間／空間的取捨

2. **自訂比較器**
   - 處理所有比較情況
   - 避免整數溢位
   - 維持遞移性

3. **常見問題**
   - 「為什麼選 Quick Sort 而不是 Merge Sort？」
   - 「怎麼讓 Quick Sort 變穩定？」
   - 「什麼時候該用 Counting Sort？」

4. **後續最佳化**
   - 只排序 k 個元素
   - 大資料用外部排序
   - 平行排序

### 進階技巧

#### **混合式排序**
- Tim Sort：合併 + 插入
- Intro Sort：快速 + 堆積 + 插入
- Python 與 Java 標準函式庫都用它們

#### **外部排序**
- 針對磁碟資料的 K 路合併
- 用於資料庫與大數據

#### **平行排序**
- 把資料分給多個處理器
- 平行合併或 sample sort

### 相關主題
- **堆積(heap)**：優先佇列、第 k 個元素
- **二分搜尋**：在已排序陣列上
- **分治法**：合併排序模式
- **貪婪**：區間排程
- **圖**：拓撲排序

## LC 範例

### 2-1) Pancake Sorting — LC 969
```python
# python
# LC 969 Pancake Sorting
# V0
# IDEA : pankcake sort + while loop
# IDEA : 3 STEPS
#   -> step 1) Find the maximum number in arr
#   -> step 2) Reverse from 0 to max_idx
#   -> step 3) Reverse whole list
# https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/pancake_sort.py
class Solution(object):
    def pancakeSort(self, arr):
        """Sort Array with Pancake Sort.
        :param arr: Collection containing comparable items
        :return: Collection ordered in ascending order of items
        Examples:
        >>> pancake_sort([0, 5, 3, 2, 2])
        [0, 2, 2, 3, 5]
        >>> pancake_sort([])
        []
        >>> pancake_sort([-2, -5, -45])
        [-45, -5, -2]
        """
        cur = len(arr)
        res = []
        while cur > 1:
            # step 1) Find the maximum number in arr
            max_idx = arr.index(max(arr[0:cur]))
            res = res + [max_idx+1, cur] # idx is 1 based
            # step 2) Reverse from 0 to max_idx
            # NOTE: `arr[:max_idx][::-1]` EXCLUDES arr[max_idx]; the commented
            #       `arr[max_idx::-1]` INCLUDES it — they are NOT equivalent.
            #       Prefer the commented form to keep the pivot element.
            #arr = arr[max_idx::-1] + arr[max_idx + 1 : len(arr)] # includes pivot
            arr = arr[:max_idx][::-1] + arr[max_idx + 1 : len(arr)]
            # step 3) Reverse whole list
            #arr = arr[cur - 1 :: -1] + arr[cur : len(arr)] # this is OK as well
            #arr = arr[:cur - 1][::-1] + arr[cur : len(arr)] # this is OK as well
            tmp = arr[::-1]
            arr = tmp
            cur -= 1
        print ("arr = " + str(arr))
        return res

# V1
# https://leetcode.com/problems/pancake-sorting/discuss/817978/Python-O(n2)-by-simulation-w-Comment
# https://leetcode.com/problems/pancake-sorting/discuss/330990/Python
class Solution:
    def pancakeSort(self, A):

        res = []

        for x in range(len(A), 1, -1):
            # Carry out pancake-sort from largest number n to smallest number 1

            # find the index of x
            i = A.index(x)

            # flip first i+1 elements to put x on A[0]
            # flip first x elements to put x on A[x-1]
            # now, x is on its corresponding position A[x-1] on ascending order
            # 
            """
            # array extend
            In [10]: x = [1,2,3]

            In [11]: x.extend([4])

            In [12]: x
            Out[12]: [1, 2, 3, 4]

            In [13]: x = [1,2,3]

            In [14]: x = x + [4]

            In [15]: x
            Out[15]: [1, 2, 3, 4]

            """
            #res.extend([i + 1, x])
            res = res + [i + 1, x]

            # update A
            """
            https://stackoverflow.com/questions/509211/understanding-slice-notation

            a[::-1]    # all items in the array, reversed
            a[1::-1]   # the first two items, reversed
            a[:-3:-1]  # the last two items, reversed
            a[-3::-1]  # everything except the last two items, reversed

            -> A[:i:-1] : last i items, reversed

            """
            A = A[:i:-1] + A[:i]
        #print ("res = " + str(res))
        return res

# V1
# IDEA : RECURSIVE
# https://leetcode.com/problems/pancake-sorting/discuss/553116/My-python-solution
# https://leetcode.com/problems/pancake-sorting/discuss/274921/PythonDetailed-Explanation-for-This-Problem
class Solution:
    def pancakeSort(self, A):
        pointer = len(A)
        result = []

        while pointer > 1:
            idx = A.index(pointer)
            result.append(idx + 1)
            A = A[idx::-1] + A[idx + 1:]
            result.append(pointer)
            A = A[pointer - 1::-1] + A[pointer:]
            pointer -= 1
            
        return result
```
```java
// java
// aAlgorithm book (labu) p. 347

// record reverse op array
LinkedList<Integer> res = new LinkedList<>();

List<Integer> pancakeSort(int[] cakes){
	sort(cakes, cakes.length);
	return res;
}

// order first N pancakes
void sort(int[] cakes, int n){
	// base case
	if (n == 1) return;

	// find max index
	int maxCake = 0;
	int maxCakeIndex = 0;
	for (int i = 0; i < n; i ++){
		if (cakes[i] > maxCake){
			maxCakeIndex = i;
			maxCake = cakes[i];
		}
	}
	// after 1st flip, put max pancake to the 1st layer
	reverse(cakes, 0, maxCakeIndex);
	// record this flip
	res.add(maxCakeIndex+1);
	// 2nd flip, make max pancake to the bottom (last layer)
	reverse(cakes, 0, n-1);
	// record this flop
	res.add(n);
	// recursive call : flip the remaining pancakes
	sort(cakes, n-1);
}

/** flip arr[i..j] elements */
void reverse(int[] arr, int i, int j){
	while (i < j){
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
		i++;
		j--;
	}
}
```

### 2-2) Reorder Data in Log Files — LC 937
```python
# LC 937. Reorder Data in Log Files
# V0
# IDEA : SORT BY KEY
class Solution:
    def reorderLogFiles(self, logs):
        def f(log):
            id_, rest = log.split(" ", 1)
            """
            NOTE !!!
              2 cases:
               1) case 1: rest[0].isalpha() => sort by rest, id_
               2) case 2: rest[0] is digit =>  DO NOTHING (keep original order)

               syntax:
                 if condition:
                    return key1, key2, key3 ....
            """
            if rest[0].isalpha():
                return 0, rest, id_
            else:
                return 1, None, None
                #return 100, None, None  # since we need to put Digit-logs behind of Letter-logs, so first key should be ANY DIGIT BIGGER THAN 0 

        logs.sort(key = lambda x : f(x))
        return logs

# V1
# IDEA : SORT BY keys
# https://leetcode.com/problems/reorder-data-in-log-files/solution/
class Solution:
    def reorderLogFiles(self, logs):

        def get_key(log):
            _id, rest = log.split(" ", maxsplit=1)
            """
            NOTE !!!
              2 cases:
               1) case 1: rest[0].isalpha() => sort by rest, id_
               2) case 2: rest[0] is digit =>  DO NOTHING (keep original order)
            """
            return (0, rest, _id) if rest[0].isalpha() else (1, )

        return sorted(logs, key=get_key)
```

### 2-3) Meeting Rooms — LC 252
```python
# LC 252. Meeting Rooms
# V0
class Solution:
    def canAttendMeetings(self, intervals):
        """
        NOTE this
        """
        intervals.sort(key=lambda x: x[0])
        for i in range(1, len(intervals)):
            """
            NOTE this : 
                -> we compare ntervals[i][0] and ntervals[i-1][1]
            """
            if intervals[i][0] < intervals[i-1][1]:
                return False
        return True
```

### 2-4) Custom Sort String — LC 791
```python
# LC 791. Custom Sort String
# V0
# IDEA : COUNTER
from collections import Counter
class Solution(object):
    def customSortString(self, order, s):
        s_map = Counter(s)
        res = ""
        for o in order:
            if o in s_map:
                res += (o * s_map[o])
                del s_map[o]
        for s in s_map:
            res += s * s_map[s]
        return res
```

### 2-5) Find K Closest Elements — LC 658
```python
# LC 658. Find K Closest Elements
# NOTE : there is also stack, binary search.. approaches
# V0'
# IDEA : SORTING
class Solution:
    def findClosestElements(self, arr, k, x):
        # Sort using custom comparator
        sorted_arr = sorted(arr, key = lambda num: abs(x - num))

        # Only take k elements
        result = []
        for i in range(k):
            result.append(sorted_arr[i])
        
        # Sort again to have output in ascending order
        return sorted(result)
```

### 2-6) Largest Number — LC 179
```python
# LC 179. Largest Number
# V0
# IDEA : Sorting via Custom Comparator
class compare(str):
    # __lt__ defines ">" operator in python
    def __lt__(x, y):
        return x+y > y+x

class Solution:
    def largestNumber(self, nums):
        largest = sorted([str(v) for v in nums], key=compare) 
        largest = ''.join(largest) 
        return '0' if largest[0] == '0' else largest 
```


### 2-7) Permutation in String — LC 567
```python
# LC 567 
# V0
# IDEA : collections + sliding window
from collections import Counter
class Solution(object):
    def checkInclusion(self, s1, s2):
        if len(s1) > len(s2):
            return False   
        l = 0
        tmp = ""
        _s1 = Counter(s1)
        _s2 = Counter()     
        for i, item in enumerate(s2):
            ### NOTE : we need to append new element first, then compare
            _s2[item] += 1
            tmp = s2[l:i+1]
            if _s2 == _s1 and len(tmp) > 0:
                return True
            if len(tmp) >= len(s1):
                _s2[tmp[0]] -= 1
                if _s2[tmp[0]] == 0:
                    del _s2[tmp[0]]
                l += 1
        return False
```

```java
// java
// LC 567
// V2
// IDEA : SORTING
// https://leetcode.com/problems/permutation-in-string/editorial/
public boolean checkInclusion_3(String s1, String s2) {
    s1 = sort(s1);
    for (int i = 0; i <= s2.length() - s1.length(); i++) {
        if (s1.equals(sort(s2.substring(i, i + s1.length()))))
            return true;
    }
    return false;
}

public String sort(String s) {
    char[] t = s.toCharArray();
    Arrays.sort(t);
    return new String(t);
} 
```

### 2-8) Car Fleet — LC 853

```java
// java

// LC 853. Car Fleet

    // V0
    // IDEA: pair position and speed, sorting (gpt)
    /**
     * IDEA :
     *
     * The approach involves sorting the cars by their starting positions
     * (from farthest to nearest to the target)
     * and computing their time to reach the target.
     * We then iterate through these times to count the number of distinct fleets.
     *
     *
     *
     * Steps in the Code:
     *  1.  Pair Cars with Their Speeds:
     *      •   Combine position and speed into a 2D array cars for easier sorting and access.
     *  2.  Sort Cars by Position Descending:
     *      •   Use Arrays.sort with a custom comparator to sort cars from farthest to nearest relative to the target.
     *  3.  Calculate Arrival Times:
     *      •   Compute the time each car takes to reach the target using the formula:
     *
     *  time = (target - position) / speed
     *
     *  4.  Count Fleets:
     *      •   Iterate through the times array:
     *      •   If the current car’s arrival time is greater than the lastTime (time of the last fleet), it forms a new fleet.
     *      •   Update lastTime to the current car’s time.
     *  5.  Return Fleet Count:
     *      •   The number of distinct times that exceed lastTime corresponds to the number of fleets.
     *
     */
    public int carFleet(int target, int[] position, int[] speed) {
        int n = position.length;
        // Pair positions with speeds and `sort by position in descending order`
        // cars : [position][speed]
        int[][] cars = new int[n][2];
        for (int i = 0; i < n; i++) {
            cars[i][0] = position[i];
            cars[i][1] = speed[i];
        }

        /**
         * NOTE !!!
         *
         *  Sort by position descending (simulate the "car arriving" process
         */
        Arrays.sort(cars, (a, b) -> b[0] - a[0]); // Sort by position descending

        // Calculate arrival times
        double[] times = new double[n];
        for (int i = 0; i < n; i++) {
            times[i] = (double) (target - cars[i][0]) / cars[i][1];
        }

        // Count fleets
        int fleets = 0;
        double lastTime = 0;
        for (double time : times) {
            /**
             *  4.  Count Fleets:
             *  •   Iterate through the times array:
             *  •   If the current car’s arrival time is greater than the lastTime (time of the last fleet), it forms a new fleet.
             *  •   Update lastTime to the current car’s time.
             */
            // If current car's time is greater than the last fleet's time, it forms a new fleet
            if (time > lastTime) {
                fleets++;
                lastTime = time;
            }
        }

        return fleets;
    }
```


### 2-9) Minimize Maximum Pair Sum in Array — LC 1877

```java
// java
// LC 1877. Minimize Maximum Pair Sum in Array
// Pattern: Greedy Pairing — Sort + Two Pointers

// Core idea:
//   1. Sort the array
//   2. Pair smallest with largest (two pointers from both ends)
//   3. Track the maximum pair sum across all pairs
//
// Why it works: pairing large+large inflates the max unnecessarily;
// pairing min+max balances every pair sum and minimizes the maximum.

public int minPairSum(int[] nums) {
    Arrays.sort(nums);
    int left = 0, right = nums.length - 1;
    int ans = 0;
    while (left < right) {
        ans = Math.max(ans, nums[left] + nums[right]);
        left++;
        right--;
    }
    return ans;
}
```

**使用同一套貪婪配對模式的相似題目：**
| 題目 | LC # | 變化點 |
|---------|------|-------|
| Array Partition | 561 | 最大化各對最小值的總和 → 排序後相鄰兩兩配對 |
| Boats to Save People | 881 | 最少船數 → 帶重量上限的貪婪雙指標 |
| Divide Players Into Teams | 2491 | 每隊技能總和相等 → 第一個配最後一個 |

### 2-10) TopK Frequent Words — LC 692

```java
// java
// LC 692

// V0-1
// IDEA: Sort on map key set
public List<String> topKFrequent_0_1(String[] words, int k) {

    // IDEA: map sorting
    HashMap<String, Integer> freq = new HashMap<>();
    for (int i = 0; i < words.length; i++) {
        freq.put(words[i], freq.getOrDefault(words[i], 0) + 1);
    }
    List<String> res = new ArrayList(freq.keySet());

    /**
     * NOTE !!!
     *
     *  we directly sort over map's keySet
     *  (with the data val, key that read from map)
     *
     *
     *  example:
     *
     *          Collections.sort(res,
     *                 (w1, w2) -> freq.get(w1).equals(freq.get(w2)) ? w1.compareTo(w2) : freq.get(w2) - freq.get(w1));
     */
    Collections.sort(res, (x, y) -> {
        int valDiff = freq.get(y) - freq.get(x); // sort on `value` bigger number first (decreasing order)
        if (valDiff == 0){
            // Sort on `key ` with `lexicographically` order (increasing order)
            //return y.length() - x.length(); // ?
            return x.compareTo(y);
        }
        return valDiff;
    });

    // get top K result
    return res.subList(0, k);
}
```

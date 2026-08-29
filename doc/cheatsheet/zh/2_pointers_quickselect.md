# QuickSelect（以分割求第 K 個元素）

> **範圍** — 以分割（partition）為基礎的選擇演算法：只往 QuickSort 分割後的其中一側遞迴，在平均 O(n) 時間內找出第 K 大、第 K 小或最接近的 K 個元素，包含 pivot 的挑選策略，以及最壞情況 O(n) 的 Median of Medians 大綱。
> **另見**：[2_pointers.md](./2_pointers.md) — 這份文件原本就是從雙指標那頁拆出來的，因為分割掃描長得像雙指標，本質卻是選擇演算法；[sort.md](./sort.md) — QuickSort 本身，以及什麼時候整個排序反而比選擇划算；[heap.md](./heap.md) — 大小為 K 的堆積替代方案，O(n log k) 但適合串流；[advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — 把 quickselect 當成分治法遞迴式來看。

## LeetCode 題目清單

- [Quickselect](https://leetcode.com/problem-list/quickselect/)
- [Divide and Conquer](https://leetcode.com/problem-list/divide-and-conquer/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

## 總覽

**模式總覽：**
QuickSelect 是用來在無序資料中找出第 K 小／第 K 大元素的選擇演算法。它跟 QuickSort 同源，差別在於只往分割後的其中一側遞迴，因此是 **平均 O(n) 時間**，而不是 O(n log n)。

**核心概念：**
```text
Given array: [3, 2, 1, 5, 6, 4], find 2nd largest (k=2)

QuickSort: Sorts entire array → O(n log n)
QuickSelect: Only finds the Kth element position → O(n) average
```

**關鍵洞見：**
- 以 pivot 分割完之後，pivot 就已經站在它排序後的最終位置
- pivot 索引 = k，答案就是它
- pivot 索引 < k，往右半邊找
- pivot 索引 > k，往左半邊找

**演算法步驟：**
1. 選一個 pivot（通常取最後一個元素，或隨機挑以求較好的表現）
2. 分割陣列：比 pivot 小的放左邊，比 pivot 大的放右邊
3. pivot 位置 == k，回傳 pivot 的值
4. pivot 位置 < k，往右半邊遞迴
5. pivot 位置 > k，往左半邊遞迴

### 關鍵性質
- **複雜度**：平均 O(n)；pivot 選得差時最壞 O(n^2)（用 Median of Medians 可保證最壞 O(n)）；迭代寫法額外空間 O(1)，遞迴寫法堆疊 O(log n)
- **核心想法**：分割後 pivot 就落在它最終的排序索引上，把那個索引跟 `k` 一比，就知道答案只可能在哪一側
- **什麼時候用**：一次性的「第 k 大／第 k 小／最接近的 k 個」，而且不需要完整順序
- **什麼時候別用**：串流輸入、輸入唯讀，或需要把 k 個元素照順序輸出 — 這些改用大小為 K 的堆積

## 模板與演算法

### 模板 1：第 K 大元素 — LC 215

```python
# Python - QuickSelect for Kth Largest
def findKthLargest(nums, k):
    """
    Find Kth largest element using QuickSelect.

    Time: O(n) average, O(n^2) worst (if bad pivots)
    Space: O(1) iterative, O(log n) recursive

    Key: Kth largest means (n - k)th smallest in 0-indexed array
    """
    def partition(left, right):
        """
        Partition using last element as pivot.
        Returns pivot's final position.
        """
        pivot = nums[right]
        i = left  # Position where elements < pivot should go

        # Move all elements < pivot to the left
        for j in range(left, right):
            if nums[j] < pivot:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1

        # Place pivot in correct position
        nums[i], nums[right] = nums[right], nums[i]
        return i

    def quickselect(left, right, k_smallest):
        """
        QuickSelect to find k_smallest element (0-indexed).
        """
        if left == right:  # Only one element
            return nums[left]

        # Partition and get pivot position
        pivot_idx = partition(left, right)

        # Check if we found the answer
        if pivot_idx == k_smallest:
            return nums[pivot_idx]
        elif pivot_idx < k_smallest:
            # Search right partition
            return quickselect(pivot_idx + 1, right, k_smallest)
        else:
            # Search left partition
            return quickselect(left, pivot_idx - 1, k_smallest)

    # Kth largest = (n - k)th smallest (0-indexed)
    n = len(nums)
    return quickselect(0, n - 1, n - k)

# Example usage:
# nums = [3, 2, 1, 5, 6, 4], k = 2
# Result: 5 (2nd largest)
```

```java
// Java - QuickSelect for Kth Largest
/**
 * LC 215 - Kth Largest Element in an Array
 *
 * time = O(N) average, O(N^2) worst
 * space = O(1) iterative, O(log N) recursive
 */
class Solution {
    public int findKthLargest(int[] nums, int k) {
        int n = nums.length;
        // Kth largest = (n - k)th smallest (0-indexed)
        return quickSelect(nums, 0, n - 1, n - k);
    }

    private int quickSelect(int[] nums, int left, int right, int kSmallest) {
        if (left == right) {
            return nums[left];
        }

        // Partition and get pivot position
        int pivotIdx = partition(nums, left, right);

        // Check if we found the answer
        if (pivotIdx == kSmallest) {
            return nums[pivotIdx];
        } else if (pivotIdx < kSmallest) {
            // Search right partition
            return quickSelect(nums, pivotIdx + 1, right, kSmallest);
        } else {
            // Search left partition
            return quickSelect(nums, left, pivotIdx - 1, kSmallest);
        }
    }

    private int partition(int[] nums, int left, int right) {
        // Use last element as pivot
        int pivot = nums[right];
        int i = left;  // Position for elements < pivot

        // Move all elements < pivot to the left
        for (int j = left; j < right; j++) {
            if (nums[j] < pivot) {
                swap(nums, i, j);
                i++;
            }
        }

        // Place pivot in correct position
        swap(nums, i, right);
        return i;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
```

---

### 視覺化範例：在 [3, 2, 1, 5, 6, 4] 中找第 2 大

```text
Target: k = 2 (2nd largest)
Array: [3, 2, 1, 5, 6, 4]
n = 6, so we need (n - k) = 4th smallest element (0-indexed)

Step 1: Partition with pivot = 4 (last element)
[3, 2, 1, 4, 6, 5]
         ↑
    pivot_idx = 3

  Elements < 4: [3, 2, 1]
  Pivot: 4 (at index 3)
  Elements > 4: [6, 5]

Check: pivot_idx (3) < k_smallest (4)
Action: Search right partition [6, 5]

Step 2: Partition right side [6, 5] with pivot = 5
[3, 2, 1, 4, 5, 6]
            ↑
    pivot_idx = 4

Check: pivot_idx (4) == k_smallest (4) ✓
Answer: nums[4] = 5 (2nd largest element)
```

---

### 模板 2：距離原點最近的 K 個點 — LC 973

```python
# Python - K Closest Points using QuickSelect
def kClosest(points, k):
    """
    Find K closest points to origin using QuickSelect.

    Time: O(n) average
    Space: O(1)
    """
    def distance(point):
        return point[0] ** 2 + point[1] ** 2

    def partition(left, right):
        pivot_dist = distance(points[right])
        i = left

        for j in range(left, right):
            if distance(points[j]) < pivot_dist:
                points[i], points[j] = points[j], points[i]
                i += 1

        points[i], points[right] = points[right], points[i]
        return i

    def quickselect(left, right, k):
        if left == right:
            return

        pivot_idx = partition(left, right)

        if pivot_idx == k:
            return
        elif pivot_idx < k:
            quickselect(pivot_idx + 1, right, k)
        else:
            quickselect(left, pivot_idx - 1, k)

    # Find K smallest distances
    quickselect(0, len(points) - 1, k - 1)
    return points[:k]
```

```java
// Java - K Closest Points
/**
 * LC 973 - K Closest Points to Origin
 *
 * time = O(N) average
 * space = O(1)
 */
class Solution {
    public int[][] kClosest(int[][] points, int k) {
        quickSelect(points, 0, points.length - 1, k - 1);
        return Arrays.copyOfRange(points, 0, k);
    }

    private void quickSelect(int[][] points, int left, int right, int k) {
        if (left >= right) return;

        int pivotIdx = partition(points, left, right);

        if (pivotIdx == k) {
            return;
        } else if (pivotIdx < k) {
            quickSelect(points, pivotIdx + 1, right, k);
        } else {
            quickSelect(points, left, pivotIdx - 1, k);
        }
    }

    private int partition(int[][] points, int left, int right) {
        int[] pivot = points[right];
        int pivotDist = distance(pivot);
        int i = left;

        for (int j = left; j < right; j++) {
            if (distance(points[j]) < pivotDist) {
                swap(points, i, j);
                i++;
            }
        }

        swap(points, i, right);
        return i;
    }

    private int distance(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }

    private void swap(int[][] points, int i, int j) {
        int[] temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }
}
```

#### **Java 版**

```java
// java
// LC 973 - K Closest Points to Origin
// IDEA: quickselect on squared distance; once the pivot sits at index k-1, points[0..k-1] IS the answer
// time = O(N) expected, space = O(1)
public int[][] kClosest(int[][] points, int k) {
    int lo = 0, hi = points.length - 1;
    while (lo < hi) {
        int p = partitionPts(points, lo, hi, lo + rnd.nextInt(hi - lo + 1));
        if (p == k - 1) break;
        else if (p < k - 1) lo = p + 1;
        else hi = p - 1;
    }
    return Arrays.copyOfRange(points, 0, k);
}
private long d(int[] p) { return (long) p[0] * p[0] + (long) p[1] * p[1]; }   // no sqrt needed
private int partitionPts(int[][] a, int lo, int hi, int pivotIdx) {
    long pivot = d(a[pivotIdx]);
    int[] t = a[pivotIdx]; a[pivotIdx] = a[hi]; a[hi] = t;
    int store = lo;
    for (int i = lo; i < hi; i++)
        if (d(a[i]) < pivot) { int[] x = a[store]; a[store] = a[i]; a[i] = x; store++; }
    int[] x = a[store]; a[store] = a[hi]; a[hi] = x;
    return store;
}
```

---

### 優化：隨機 pivot

```python
# Randomized QuickSelect for better average performance
import random

def findKthLargest_randomized(nums, k):
    """
    Randomized pivot selection reduces worst-case probability.

    Time: O(n) average with high probability
    """
    def partition(left, right):
        # RANDOM pivot selection
        random_idx = random.randint(left, right)
        nums[random_idx], nums[right] = nums[right], nums[random_idx]

        pivot = nums[right]
        i = left

        for j in range(left, right):
            if nums[j] < pivot:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1

        nums[i], nums[right] = nums[right], nums[i]
        return i

    def quickselect(left, right, k_smallest):
        if left == right:
            return nums[left]

        pivot_idx = partition(left, right)

        if pivot_idx == k_smallest:
            return nums[pivot_idx]
        elif pivot_idx < k_smallest:
            return quickselect(pivot_idx + 1, right, k_smallest)
        else:
            return quickselect(left, pivot_idx - 1, k_smallest)

    n = len(nums)
    return quickselect(0, n - 1, n - k)
```

---

### 分割演算法的各種版本

**1. Hoare 分割（從兩端往中間的雙指標）：**

> ⚠️ 它回傳的是**分界點**，不是 pivot 的最終索引 — `nums[j]` 不一定是
> pivot。所以 Lomuto 那套 `p == target` 的判斷在這裡是錯的；要改成縮小區間
> （`target <= j` → `hi = j`，否則 `lo = j + 1`），一路縮到 `lo == hi`。見版本 3。

```python
def partition_hoare(nums, left, right):
    """
    Hoare's partition: pointers move from both ends.
    More efficient with fewer swaps.
    """
    pivot = nums[(left + right) // 2]  # Middle element as pivot
    i, j = left - 1, right + 1

    while True:
        # Move i right until element >= pivot
        i += 1
        while nums[i] < pivot:
            i += 1

        # Move j left until element <= pivot
        j -= 1
        while nums[j] > pivot:
            j -= 1

        if i >= j:
            return j

        nums[i], nums[j] = nums[j], nums[i]
```

**2. Lomuto 分割（單趟掃描）：**
```python
def partition_lomuto(nums, left, right):
    """
    Lomuto's partition: single pointer from left.
    Simpler but may do more swaps.
    """
    pivot = nums[right]
    i = left

    for j in range(left, right):
        if nums[j] <= pivot:
            nums[i], nums[j] = nums[j], nums[i]
            i += 1

    nums[i], nums[right] = nums[right], nums[i]
    return i
```

**3. Java 版 Hoare — 以及它設下的陷阱：**
> 陷阱在於：`hoare()` 只保證 `a[lo..j] <= pivot <= a[j+1..hi]`，所以你**不能**測 `p == target`；要用 `target <= j` 縮小區間，縮到 `lo == hi` 為止。

```java
// java
// LC 215 - Kth Largest Element in an Array (Hoare partition variant)
// time = O(N) expected, space = O(1)
public int findKthLargestHoare(int[] nums, int k) {
    int target = nums.length - k;
    int lo = 0, hi = nums.length - 1;
    while (lo < hi) {
        int j = hoare(nums, lo, hi);
        if (target <= j) hi = j; else lo = j + 1;
    }
    return nums[lo];
}
private int hoare(int[] a, int lo, int hi) {
    int pivot = a[lo + rnd.nextInt(hi - lo + 1)];   // pivot by VALUE, chosen at random
    int i = lo - 1, j = hi + 1;
    while (true) {
        do { i++; } while (a[i] < pivot);
        do { j--; } while (a[j] > pivot);
        if (i >= j) return j;
        swap(a, i, j);
    }
}
```

**4. 三向分割（荷蘭國旗）— 大量重複值的解法：**
> 陷阱在於：面對 `[2,2,2,…,2]`，隨機 pivot 救不了 Lomuto — 每次分割還是只剝掉一個元素，最後是 O(n²)。改成分成 `< / == / >` 三塊，一趟就把所有相等的鍵折疊掉。

```python
# python
# LC 215 - Kth Largest Element in an Array (3-way partition; safe on many duplicates)
# time = O(N) expected even when almost all values are equal, space = O(1)
import random

def findKthLargest(nums, k):
    target = len(nums) - k
    lo, hi = 0, len(nums) - 1
    while True:
        pivot = nums[random.randint(lo, hi)]
        lt, i, gt = lo, lo, hi
        while i <= gt:                          # invariant: [lo,lt) < pivot, [lt,i) == pivot, (gt,hi] > pivot
            if nums[i] < pivot:
                nums[lt], nums[i] = nums[i], nums[lt]
                lt += 1
                i += 1
            elif nums[i] > pivot:
                nums[i], nums[gt] = nums[gt], nums[i]
                gt -= 1
            else:
                i += 1
        if target < lt:
            hi = lt - 1
        elif target > gt:
            lo = gt + 1
        else:
            return pivot                        # target fell inside the equal band
```

### 其他 quickselect 變形（同一副骨架，換個比較鍵）

| 題目 | LC # | 變化點 |
|---------|------|-----------|
| Top K Frequent Elements | 347 | 對次數表的*項目*做 quickselect，比較鍵是出現次數。依頻率做桶排序才是真正的 O(N) 解 — 兩種都要講得出來。 |
| Find the Kth Largest Integer in the Array | 1985 | 值是數字**字串**；只有比較器要改：短的字串較小，長度相同再比字典序。 |
| Wiggle Sort II | 324 | 先 quickselect 找**中位數**，再做三向分割，最後寫進*虛擬索引* `(1 + 2*i) % (n | 1)`，讓相等的中位數被拆得遠遠的。 |
| Kth Largest XOR Coordinate Value | 1738 | 先建二維前綴 XOR 網格（`O(mn)`），再對這 `m*n` 個值 quickselect 找第 k 大。 |


---

### 進階：Median of Medians（最壞 O(n)）— 僅列大綱

> ⚠️ **這是草稿，不是能跑的解法。** 只把挑 pivot 的那一半寫清楚；
> `partition`、驅動迴圈和 `median_of_medians_list` 的遞迴都留成 `pass`。
> 不要以為複製貼上就會動 — 它永遠回傳 `None`。放在這裡是要說明 O(n) 保證*從哪來*，
> 而面試官會問的也只有這一部分。

```python
def findKthLargest_median_of_medians(nums, k):
    """
    OUTLINE of the guaranteed O(n) worst-case pivot strategy — incomplete.

    The idea: pick the pivot as the median of the group-of-5 medians. That pivot
    is guaranteed to discard at least 30% of the array each round, which turns
    the O(n^2) worst case into O(n).

    Time: O(n) worst-case
    Space: O(log n) recursion
    """
    def median_of_medians(left, right):
        """Find approximate median for good pivot."""
        if right - left < 5:
            return sorted(nums[left:right + 1])[len(nums[left:right + 1]) // 2]

        # Divide into groups of 5, find median of each
        medians = []
        for i in range(left, right + 1, 5):
            sub_right = min(i + 4, right)
            median = sorted(nums[i:sub_right + 1])[(sub_right - i) // 2]
            medians.append(median)

        # Recursively find median of medians
        # NOTE: median_of_medians_list is NOT defined here — it would recurse
        # on `medians` the same way this function recurses on `nums`.
        return median_of_medians_list(medians)

    def partition(left, right, pivot_value):
        # Partition around pivot_value — same three-way scan as Template 1 above
        pass                      # left as an exercise

    # Main quickselect loop, using median_of_medians() for the pivot instead of
    # a random index — otherwise identical to Template 1.
    pass                          # left as an exercise
```

**注意：** Median of Medians 太複雜，面試中幾乎沒人真的寫出來。實務上偏好隨機化的 QuickSelect。

---


## 總結與快速查表

### 經典 LeetCode 題目

| 題目 | LC# | 難度 | 變形 | 關鍵洞見 |
|---------|-----|------------|---------|-------------|
| Kth Largest Element in Array | 215 | Medium | 基本 QuickSelect | 轉成找第 (n-k) 小 |
| K Closest Points to Origin | 973 | Medium | 自訂比較器 | 依距離分割 |
| Top K Frequent Elements | 347 | Medium | 搭配次數表 | 對頻率做 QuickSelect |
| Top K Frequent Words | 692 | Medium | 次數表 + trie | QuickSelect + 字典序 |
| Kth Largest Element in Stream | 703 | Easy | 改用 min heap | QuickSelect 用來做初始化 |
| Find Kth Smallest Pair Distance | 719 | Hard | 對答案二分搜尋 | 不是直接的 QuickSelect |
| Wiggle Sort II | 324 | Medium | 三向分割 | 荷蘭國旗的變形 |
| Sort Colors | 75 | Medium | 三向分割 | 荷蘭國旗 |
| Kth Smallest Element in BST | 230 | Medium | 中序走訪 | 不是 QuickSelect（樹結構） |
| Find Median from Data Stream | 295 | Hard | 兩個堆積 | QuickSelect 的替代方案 |

---

### 效能比較

| 演算法 | 平均時間 | 最壞時間 | 空間 | 適用情境 |
|-----------|--------------|------------|-------|----------|
| **QuickSelect** | **O(n)** | O(n²) | O(1) | 在未排序資料中找第 K 個元素 |
| QuickSelect（隨機化） | O(n) | O(n²)，機率極低 | O(1) | 平均表現更穩 |
| Heap（Min/Max） | O(n log k) | O(n log k) | O(k) | 線上／串流資料 |
| Full Sort | O(n log n) | O(n log n) | O(1) 或 O(n) | 反正本來就需要排序好的陣列 |
| Counting Sort | O(n + k) | O(n + k) | O(k) | 整數範圍很小 |

**什麼時候用 QuickSelect：**
- ✅ 只要第 K 個元素，不需要完整排序
- ✅ 可以就地改動輸入陣列（原地）
- ✅ 離線演算法（資料一次到齊）
- ✅ 資料量大到 O(n) 跟 O(n log n) 有感差別

**什麼時候別用 QuickSelect：**
- ❌ 需要 K 個元素照順序輸出 → 用堆積或完整排序
- ❌ 線上／串流資料 → 用堆積
- ❌ 不能改動輸入陣列 → 用堆積
- ❌ 需要最壞情況保證 → 用 Median of Medians（最壞 O(n)）

---

### 面試提點

**1. 常見錯誤：**
- 忘了把「第 K 大」換算成「第 (n - k) 小」
- k 從 0 算還是從 1 算，差一錯誤
- 沒處理 left == right 的終止條件
- 分割沒有真的移動 pivot，導致無限遞迴

**2. 優化技巧：**
- **隨機 pivot**：壓低撞上最壞情況的機率
- **三數取中**：取頭、中、尾三個元素的中位數當 pivot
- **迭代版本**：陣列很大時避免堆疊爆掉
- **尾遞迴**：只對較小的那一半遞迴

**3. 複雜度分析：**
```text
Best/Average Case: O(n + n/2 + n/4 + ... + 1) = O(2n) = O(n)

Worst Case (bad pivots every time):
  O(n + (n-1) + (n-2) + ... + 1) = O(n²)

With randomized pivot:
  Worst case O(n²) probability → near zero for large n
```

**4. 面試時可以講的重點：**
- 「QuickSelect 就是 QuickSort，只是只往一側遞迴」
- 「只要找單一個第 K 個元素，平均 O(n) 比堆積的 O(n log k) 好」
- 「取捨在於：這個做法會改動陣列，堆積則保留原陣列」
- 「隨機 pivot 讓 O(n) 以高機率成立」

**5. 追問：**
- Q：「如果需要 K 個元素排好序呢？」
  - A：用堆積（O(n log k)）或部分 QuickSort
- Q：「如果陣列是唯讀的呢？」
  - A：複製一份，或改用堆積
- Q：「能保證最壞 O(n) 嗎？」
  - A：可以，用 Median of Medians（複雜，很少被問）

---

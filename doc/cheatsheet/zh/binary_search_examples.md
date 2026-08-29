# 二分搜尋 — 範例題解

> **範圍** — [binary_search.md](./binary_search.md) 的題解存放處 — 索引空間類模板的每題一份標準解，附上主文放不下的追蹤過程與陷阱。
> **另見** — *主文件*：[binary_search.md](./binary_search.md) — 迴圈不變式、邊界模板，以及「該用哪個模板」的決策表；[binary_search_on_answer.md](./binary_search_on_answer.md) — 所有對*答案*而非索引做二分搜尋的題目。
> *鄰近文件*：[2_pointers.md](./2_pointers.md) — LC 167 / LC 658 的收斂指標替代解法；[dp.md](./dp.md) — LC 300 取代掉的那個 `O(n²)` DP；[matrix.md](./matrix.md) — 二維網格題型家族。

## LeetCode 題目清單

- [Binary Search](https://leetcode.com/problem-list/binary-search/)

## 總覽

下面每一題都只解**一次**，用最能講清楚它的語言（兩種語言都有教學價值時才都寫）。每題用到的模板放在
[binary_search.md](./binary_search.md) — 這份檔案是練習集，不是第二份模板目錄。對*答案空間*
做二分搜尋的題目請看 [binary_search_on_answer.md](./binary_search_on_answer.md)。

## LC 範例

### 1) Two Sum II — Input Array Is Sorted (LC 167)
**做法**：對每個元素二分搜尋它的補數
```python
# 167 Two Sum II - Input array is sorted
class Solution(object):
    def twoSum(self, numbers, target):
        for i in range(len(numbers)):
            l, r = i+1, len(numbers)-1
            tmp = target - numbers[i]
            while l <= r:
                mid = l + (r-l)//2
                if numbers[mid] == tmp:
                    return [i+1, mid+1]
                elif numbers[mid] < tmp:
                    l = mid+1
                else:
                    r = mid-1
```

### 2) Find Peak Element (LC 162, LC 852)

#### 核心想法：往上爬（保證有峰值）

**關鍵規則：**
```text
If nums[mid] < nums[mid + 1]  →  peak is on the RIGHT  (move l = mid + 1)
If nums[mid] > nums[mid + 1]  →  peak is on the LEFT   (move r = mid)
```

**為什麼一定成立（「-∞ 邊界」這個技巧）：**

題目說 `nums[-1] = nums[n] = -∞`。也就是說陣列兩端永遠被兩個無底洞夾著。

```text
         peak
        /    \
       /      \
-∞ ___/        \___ -∞
```

不管你站在陣列的哪個位置，只要往*比較高*的鄰居走，一定會撞到某個峰值 — 要嘛地勢先升後降
（中間有個峰），要嘛一路升到底（最後一個元素就是峰，因為它右邊是 -∞）。

---

#### 情況 1：`nums[mid] < nums[mid+1]` → 正在**上坡** → 往**右**走

```text
         ?
        /
       /
 .... mid  mid+1 ....
      low  HIGH

You are on an upward slope. Two sub-cases:
  a) The slope eventually drops → peak is somewhere to the right
  b) The slope never drops → last element is a peak (because -∞ is on its right)

Either way, a peak MUST exist to the right → l = mid + 1
```

```text
Example: nums = [1, 2, 3, 1]
                    ^mid ^mid+1
nums[mid]=2 < nums[mid+1]=3  → uphill → move RIGHT
                        ^--- peak is here (index 2, value 3)
```

---

#### 情況 2：`nums[mid] > nums[mid+1]` → 正在**下坡** → 留在**左邊**（含 mid）

```text
 ....  mid  mid+1 ....
       HIGH  low
          \
           \
            ?

You are on a downward slope. Two sub-cases:
  a) nums[mid] > nums[mid-1]: mid itself IS a peak
  b) nums[mid] < nums[mid-1]: the slope was already rising from the left,
     so a peak exists somewhere to the left of mid

Either way, the peak is at mid or to the left → r = mid
```

```text
Example: nums = [1, 2, 1, 3, 5, 6, 4]
                               ^mid ^mid+1
nums[mid]=5 > nums[mid+1]=6?  No — pick a better example:
                                  ^mid  ^mid+1
nums[mid]=6 > nums[mid+1]=4  → downhill → move LEFT (r = mid, keep mid)
              ^--- peak is here (index 5, value 6)
```

---

#### 圖解：搜尋空間如何收斂

```text
nums = [1, 2, 3, 1]
        0  1  2  3

l=0, r=3:  mid=1, nums[1]=2 < nums[2]=3  → uphill → l=2
           [_, _, 2, 3]
                  l  r

l=2, r=3:  mid=2, nums[2]=3 > nums[3]=1  → downhill → r=2
           [_, _, 3, _]
                  l
                  r

l==r → return 2  ✓  (nums[2]=3 is the peak)
```

```text
nums = [1, 2, 1, 3, 5, 6, 4]
        0  1  2  3  4  5  6

l=0, r=6:  mid=3, nums[3]=3 < nums[4]=5  → uphill → l=4
l=4, r=6:  mid=5, nums[5]=6 > nums[6]=4  → downhill → r=5
l=4, r=5:  mid=4, nums[4]=5 < nums[5]=6  → uphill → l=5
l=5, r=5:  l==r → return 5  ✓  (nums[5]=6 is the peak)
```

---

#### 為什麼是 `while (l < r)` 而不是 `while (l <= r)`？

因為用的是 `r = mid`（不是 `r = mid - 1`），當 `l == r` 時迴圈就必須停 —
否則 `mid == l == r` 會造成無窮迴圈（`r = mid` 根本不會縮小）。

```java
// ✅ Correct: while (l < r)
while (l < r) {
    int mid = (l + r) / 2;
    if (nums[mid] > nums[mid + 1])
        r = mid;       // Keep mid, since it may be the peak
    else
        l = mid + 1;   // mid is not the peak, skip it
}
// When l == r, that IS the peak index
return l;
```

---

**做法**：把 mid 和相鄰元素比較，決定往哪邊搜
```python
# LC 162 Find Peak Element, LC 852 Peak Index in a Mountain Array
# V0'
# IDEA : RECURSIVE BINARY SEARCH
class Solution(object):
    def findPeakElement(self, nums):

        def help(nums, l, r):
            if l == r:
                return l
            mid = l + (r - l) // 2
            if (nums[mid] > nums[mid+1]):
                return help(nums, l, mid) # r = mid
            return help(nums, mid+1, r) # l = mid + 1
            
        return help(nums, 0, len(nums)-1)
```

```java
// java
// LC 162
// V2
// IDEA: RECURSIVE BINARY SEARCH
// https://leetcode.com/problems/find-peak-element/editorial/
    // NOTE : ONLY have to compare index i with index i + 1 (its right element)
    //        ; otherwise, i-1 already returned as answer
    public int findPeakElement_2(int[] nums) {
        return search(nums, 0, nums.length - 1);
    }
    public int search(int[] nums, int l, int r) {
        if (l == r)
            return l;
        int mid = (l + r) / 2;
        if (nums[mid] > nums[mid + 1])
            return search(nums, l, mid);
        return search(nums, mid + 1, r);
    }
```

### 3) Valid Perfect Square (LC 367)
**做法**：在 [1, num] 這個範圍上二分搜尋平方根
```python
# 367 Valid Perfect Square, LC 69 Sqrt(x)
# V0'
# IDEA : BINARY SEARCH
class Solution(object):
    def isPerfectSquare(self, num):
        left, right = 0, num
        while left <= right:
            ### NOTE : there is NO mid * mid == num condition
            mid = (left + right) / 2
            if mid * mid >= num:
                right = mid - 1
            else:
                left = mid + 1
        ### NOTE this
        return left * left == num
```

```java
// java
// LC 367
public boolean isPerfectSquare(int num) {

    if (num < 2) {
        return true;
    }

    long left = 2;
    long right = num / 2; // NOTE !!!, "long right = num;" is OK as well
    long x;
    long guessSquared;

    while (left <= right) {
        x = (left + right) / 2;
        guessSquared = x * x;
        if (guessSquared == num) {
            return true;
        }
        if (guessSquared > num) {
            right = x - 1;
        } else {
            left = x + 1;
        }
    }
    return false;
}
```

### 4) Sqrt(x) (LC 69)
**做法**：二分搜尋，邊界要小心處理
```python
# LC 069 Sqrt(x)
# V0
# IDEA : binary search
class Solution(object):
    def mySqrt(self, num):
        if num <= 1:
            return num
        l = 0
        r = num - 1
        while r >= l:
            mid = l + (r - l) // 2
            if mid * mid == num:
                return mid
            elif mid * mid > num:
                r = mid - 1
            else:
                l = mid + 1
        return l if l * l < num else l - 1
```

### 5) Minimum Size Subarray Sum (LC 209)
**做法**：對可能的子陣列長度二分搜尋 + 用滑動視窗驗證
```python
# LC 209 Minimum Size Subarray Sum
### NOTE : there is also sliding window approach
# V1' 
# http://bookshadow.com/weblog/2015/05/12/leetcode-minimum-size-subarray-sum/
# IDEA : BINARY SEARCH 
class Solution:
    def minSubArrayLen(self, s, nums):
        size = len(nums)
        left, right = 0, size
        bestAns = 0
        while left <= right:
            mid = (left + right) / 2
            if self.solve(mid, s, nums):
                bestAns = mid
                right = mid - 1
            else:
                left = mid + 1
        return bestAns

    def solve(self, l, s, nums):
        sums = 0
        for x in range(len(nums)):
            sums += nums[x]
            if x >= l:
                sums -= nums[x - l]
            if sums >= s:
                return True
        return False
```

### 6) First Bad Version (LC 278)
> 找出最左邊的壞版本，而且不要多呼叫 API。

```java
// LC 278 - First Bad Version
// IDEA: Binary search for left boundary — first bad version
// time = O(log N), space = O(1)
public int firstBadVersion(int n) {
    int l = 1, r = n;
    while (l < r) {
        int mid = l + (r - l) / 2;
        if (isBadVersion(mid)) r = mid;
        else l = mid + 1;
    }
    return l;
}
```

### 7) Find K Closest Elements (LC 658)
**做法**：用雙指標把陣列縮到剩 k 個元素
```python
# LC 658. Find K Closest Elements
# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82968136
# IDEA : TWO POINTERS 
class Solution(object):
    def findClosestElements(self, arr, k, x):
        # since the array already sorted, arr[-1] must be the biggest one,
        # while arr[0] is the smallest one
        # so if the distance within arr[-1],  x >  arr[0],  x
        # then remove the arr[-1] since we want to keep k elements with smaller distance,
        # and vice versa (remove arr[0]) 
        while len(arr) > k:
            if x - arr[0] <= arr[-1] - x:
                arr.pop()
            else:
                arr.pop(0)
        return arr
```

### 8) Find Smallest Letter Greater Than Target (LC 744)
**模式**：`while (l < r)` — 找插入位置
```python
# LC 744 Find Smallest Letter Greater Than Target
class Solution(object):
    def nextGreatestLetter(self, letters, target):
        l, r = 0, len(letters)
        
        # Use half-open boundary [l, r)
        while l < r:
            mid = l + (r - l) // 2
            if letters[mid] <= target:  # Need strictly greater
                l = mid + 1
            else:
                r = mid
        
        # Handle circular array - if no letter greater than target, return first
        return letters[l % len(letters)]
```

### 9) Arranging Coins (LC 441)
**模式**：`while (l <= r)` — 用數學性質找精確值
```java
// LC 441 Arranging Coins
public int arrangeCoins(int n) {
    long l = 0, r = n;
    
    while (l <= r) {
        long mid = l + (r - l) / 2;
        long coins = mid * (mid + 1) / 2;  // Sum of 1+2+...+mid
        
        if (coins == n) {
            return (int) mid;
        } else if (coins < n) {
            l = mid + 1;
        } else {
            r = mid - 1;
        }
    }
    
    return (int) r;  // Return the complete rows we can form
}
```

### 10) Find Minimum in Rotated Sorted Array II (LC 154)
**模式**：`while (l < r)` — 處理旋轉陣列中的重複值
```java
// LC 154 Find Minimum in Rotated Sorted Array II (with duplicates)
public int findMin(int[] nums) {
    int l = 0, r = nums.length - 1;
    
    while (l < r) {
        int mid = l + (r - l) / 2;
        
        if (nums[mid] < nums[r]) {
            // Right half is sorted, minimum is in left half (including mid)
            r = mid;
        } else if (nums[mid] > nums[r]) {
            // Left half is sorted, minimum is in right half
            l = mid + 1;
        } else {
            // nums[mid] == nums[r], can't determine which half to search
            // Reduce search space by 1
            r--;
        }
    }
    
    return nums[l];
}
```

### 11) Missing Element in Sorted Array (LC 1060)
**模式**：`while (l < r - 1)` — 用差距計算找出缺失元素
```python
# LC 1060 Missing Element in Sorted Array
class Solution(object):
    def missingElement(self, nums, k):
        def missing_count(idx):
            # How many numbers are missing up to nums[idx]
            return nums[idx] - nums[0] - idx
        
        n = len(nums)
        
        # If k-th missing number is beyond the array
        if k > missing_count(n - 1):
            return nums[-1] + k - missing_count(n - 1)
        
        l, r = 0, n - 1
        
        # Find the largest index where missing_count < k
        while l < r - 1:
            mid = l + (r - l) // 2
            if missing_count(mid) < k:
                l = mid
            else:
                r = mid
        
        # The k-th missing number is between nums[l] and nums[r]
        return nums[l] + k - missing_count(l)
```

### 12) Median of Two Sorted Arrays (LC 4)
> 對較短陣列的切分點做二分搜尋，用 O(log(min(M,N))) 找中位數。

```java
// LC 4 - Median of Two Sorted Arrays
// IDEA: Binary search partition on smaller array
// time = O(log(min(M,N))), space = O(1)
public double findMedianSortedArrays(int[] nums1, int[] nums2) {
    if (nums1.length > nums2.length) return findMedianSortedArrays(nums2, nums1);
    int m = nums1.length, n = nums2.length;
    int l = 0, r = m;
    while (l <= r) {
        int partX = (l + r) / 2;
        int partY = (m + n + 1) / 2 - partX;
        int maxLeftX  = partX == 0 ? Integer.MIN_VALUE : nums1[partX-1];
        int minRightX = partX == m ? Integer.MAX_VALUE : nums1[partX];
        int maxLeftY  = partY == 0 ? Integer.MIN_VALUE : nums2[partY-1];
        int minRightY = partY == n ? Integer.MAX_VALUE : nums2[partY];
        if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
            if ((m + n) % 2 == 0)
                return (Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2.0;
            else
                return Math.max(maxLeftX, maxLeftY);
        } else if (maxLeftX > minRightY) r = partX - 1;
        else l = partX + 1;
    }
    return 0;
}
```

### 13) Time Based Key-Value Store (LC 981)
> 對每個 key，在排序好的時間戳上二分搜尋，找出最大且 <= 給定時間的那個。

```java
// LC 981 - Time Based Key-Value Store
// IDEA: HashMap of key -> sorted list of (timestamp, value); binary search on query
// time = O(log N) per get, O(1) per set, space = O(N)
class TimeMap {
    Map<String, List<int[]>> map = new HashMap<>(); // val stored as [timestamp, valueIndex]
    Map<String, List<String>> vals = new HashMap<>();
    public void set(String key, String value, int timestamp) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(new int[]{timestamp});
        vals.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
    public String get(String key, int timestamp) {
        if (!map.containsKey(key)) return "";
        List<int[]> times = map.get(key);
        int l = 0, r = times.size() - 1, idx = -1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (times.get(mid)[0] <= timestamp) { idx = mid; l = mid + 1; }
            else r = mid - 1;
        }
        return idx == -1 ? "" : vals.get(key).get(idx);
    }
}
```

### 14) Single Element in a Sorted Array (LC 540)
> 成對的規律在單獨元素之後就被打破了；對偶數索引做二分搜尋。

```java
// LC 540 - Single Element in a Sorted Array
// IDEA: Binary search — check if pair pattern holds at mid (even index)
// time = O(log N), space = O(1)
public int singleNonDuplicate(int[] nums) {
    int l = 0, r = nums.length - 1;
    while (l < r) {
        int mid = (l + r) / 2;
        if (mid % 2 == 1) mid--; // ensure mid is even
        if (nums[mid] == nums[mid + 1]) l = mid + 2; // pair intact, single is to the right
        else r = mid;                                  // pair broken, single is here or left
    }
    return nums[l];
}
```

### 15) Check If a Number Is Majority Element in a Sorted Array (LC 1150)

#### 核心想法

給一個排序好的陣列，**多數元素**指的是出現超過 `N/2` 次的元素。

**關鍵洞見**：在排序好的陣列中，若 target 出現超過 `N/2` 次，那麼索引 `firstIndex + N/2` 上的元素也一定是 target。

**為什麼成立：**
- 先找到 target 第一次出現的索引 `firstIndex`
- 若 target 出現 `> N/2` 次，它至少要佔掉 `N/2 + 1` 個連續位置
- 所以 `nums[firstIndex + N/2]` **必定**還是 target

這樣就不用數所有出現次數 — O(log N) 而不是 O(N)。

---

#### 模式：用二分搜尋找第一個索引

```java
// Find first occurrence of target in sorted array
private int findFirstIndex(int[] nums, int target) {
    int low = 0, high = nums.length - 1;
    int firstIdx = -1;

    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (nums[mid] == target) {
            firstIdx = mid;       // record potential answer
            high = mid - 1;       // keep searching LEFT for earlier occurrence
        } else if (nums[mid] < target) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return firstIdx;
}
```

**模板規則：**
- 當 `nums[mid] == target`：先把 `mid` 存成候選，再**往左縮**（`high = mid - 1`）繼續找
- 迴圈結束時，`firstIdx` 就是 target 最左邊的索引（找不到則為 -1）

---

#### 解法

```java
// LC 1150 - Check If a Number Is Majority Element in a Sorted Array
// time: O(log N), space: O(1)
public boolean isMajorityElement(int[] nums, int target) {
    int n = nums.length;

    // Step 1: Find first occurrence of target
    int firstIndex = findFirstIndex(nums, target);

    // Step 2: If not found, can't be majority
    if (firstIndex == -1) return false;

    // Step 3: Check if element at (firstIndex + n/2) is still target
    // If yes → target appears at least (n/2 + 1) times → majority
    int majorityIndex = firstIndex + n / 2;
    return majorityIndex < n && nums[majorityIndex] == target;
}

private int findFirstIndex(int[] nums, int target) {
    int low = 0, high = nums.length - 1, firstIdx = -1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (nums[mid] == target) {
            firstIdx = mid;
            high = mid - 1;    // search left for earlier occurrence
        } else if (nums[mid] < target) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return firstIdx;
}
```

**改用 lower_bound 風格的寫法（V1）：**
```java
// Uses two binary searches: first index of target, first index of (target+1)
public boolean isMajorityElement_v1(int[] nums, int target) {
    int left  = lowerBound(nums, target);      // first index >= target
    int right = lowerBound(nums, target + 1);  // first index >= target+1
    return right - left > nums.length / 2;
}

private int lowerBound(int[] nums, int x) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = (left + right) >>> 1;
        if (nums[mid] >= x) right = mid;
        else left = mid + 1;
    }
    return left;
}
```

---

#### 圖解範例

```text
nums = [2,4,5,5,5,5,5,6,6], target = 5, N = 9

findFirstIndex(5) → index 2

majorityIndex = 2 + 9/2 = 2 + 4 = 6
nums[6] = 5 == target ✓  → return true

Intuition:
index:  0  1  2  3  4  5  6  7  8
value:  2  4 [5] 5  5  5 [5] 6  6
           first↑           ↑ must still be 5 if majority
```

---

#### 找第一個索引 — 與相似模式的比較

| 模式 | `nums[mid] == target` 時 | 迴圈結束後 | 回傳 |
|---------|---------------------------|-----------|---------|
| **標準二分搜尋** | `return mid` | 不適用 | 精確索引或 -1 |
| **找第一個（左邊界）** | `firstIdx = mid; high = mid-1` | `firstIdx` | 最左索引或 -1 |
| **找最後一個（右邊界）** | `lastIdx = mid; low = mid+1` | `lastIdx` | 最右索引或 -1 |
| **Lower Bound** | `right = mid`（半開區間） | `left` | 第一個 >= target 的索引 |

---

#### 相似的 LC 題目

| 題目 | 核心想法 | 難度 |
|---------|-----------|------------|
| **LC 1150** | 找第一個索引 + 跳 N/2 驗證多數性 | Easy（Prime） |
| LC 34 | 兩次邊界搜尋，找第一個和最後一個出現位置 | Medium |
| LC 35 | 找插入位置（迴圈後回傳 `left`） | Easy |
| LC 278 | First Bad Version — 找條件第一次為真的索引 | Easy |
| LC 153 | 在旋轉排序陣列中找最小值 | Medium |
| LC 374 | Guess Number Higher or Lower — 經典的找第一個 | Easy |
| LC 540 | Single Element in a Sorted Array — 用奇偶性做邊界搜尋 | Medium |
| LC 852 | Peak Index in a Mountain Array — 找第一個開始下降的點 | Medium |

---

#### 「找第一個」模式的面試技巧

1. **看到排序陣列 + 計數查詢** → 想「找第一個索引 + O(1) 檢查」
2. **關鍵那一行**：`return majorityIndex < n && nums[majorityIndex] == target` — 邊界檢查很重要
3. **為什麼不用數的？** 數是 O(N)，二分搜尋是 O(log N) — 面試官要的是最佳解
4. **邊界情況**：target 不在陣列裡、只有一個元素、全部元素都等於 target

---

### 16) Find Right Interval (LC 436) ⭐⭐⭐⭐

**做法**：把起點排序 + 做 **lower-bound** 二分搜尋，再把排序後的位置對回**原始索引**。

#### 1) 核心想法

> 對每個區間 `[start_i, end_i]`，找出 `start_j` 是**所有 `>= end_i` 的起點中最小的**那個區間 `j` —
> 這就是一個 **lower-bound**（第一個 `>=` target）搜尋。

麻煩的地方：答案要的是那個區間的**原始索引**，但二分搜尋需要起點是**排序好的**。所以我們把
`(start, original_index)` 成對一起排序 — 排序會讓每個起點跟它的原始索引黏在一起，
定位到排序陣列中的位置後，直接從這個 pair 讀出原始索引就好。

```text
intervals = [[3,4],[2,3],[1,2]]   (original indices 0,1,2)

sort starts with their index →  starts = [(1,2), (2,1), (3,0)]
                                            ^val,idx

For interval [2,3]:  end = 3  → first start >= 3  → (3,0) → original index 0  ✅
For interval [1,2]:  end = 2  → first start >= 2  → (2,1) → original index 1  ✅
For interval [3,4]:  end = 4  → no start >= 4                → -1
```

**為什麼能二分搜尋？** 起點互不相同，排序後就是**單調的** — 剛好符合 lower-bound 的謂詞
`start >= end_i`（False…False, True…True）。總複雜度：`O(n log n)`。

#### 2) 模式 — 帶索引排序 + Lower Bound

```python
# LC 436 Find Right Interval
# V1: manual lower-bound binary search
# time = O(n log n), space = O(n)
class Solution(object):
    def findRightInterval(self, intervals):
        n = len(intervals)

        # NOTE !!! collect BOTH `start val` AND `original idx`, then sort
        # -> sorting keeps start glued to its original index
        starts = [(intervals[i][0], i) for i in range(n)]
        starts.sort()                          # sort by start (unique)

        res = [-1] * n
        for i in range(n):
            target = intervals[i][1]           # we need first start >= end_i

            # ---- lower bound: first start >= target ----
            left, right, ans = 0, n - 1, -1
            while left <= right:
                mid = (left + right) // 2
                if starts[mid][0] >= target:
                    ans = starts[mid][1]       # record original index (candidate)
                    right = mid - 1            # keep searching LEFT for a smaller start
                else:
                    left = mid + 1
            res[i] = ans
        return res
```

**用 `bisect` 更乾淨**（把排序後的起點抽出來，`bisect_left` 就是第一個 `>=`）：

```python
import bisect
# time = O(n log n), space = O(n)
class Solution(object):
    def findRightInterval(self, intervals):
        n = len(intervals)
        # (start, original_idx) sorted by start
        starts = sorted([[iv[0], i] for i, iv in enumerate(intervals)])
        just_starts = [s[0] for s in starts]   # bisect needs a plain sorted list

        res = [-1] * n
        for i, iv in enumerate(intervals):
            idx = bisect.bisect_left(just_starts, iv[1])   # first start >= end_i
            if idx < n:
                res[i] = starts[idx][1]        # map sorted pos -> original index
        return res
```

> **常見陷阱（TLE）**：暴力做法「排序後用雙層迴圈重建索引對照」是 `O(n^2)`。
> 整件事的重點就是把內層那趟掃描換成 `O(log n)` 的 lower-bound 搜尋 —
> 認出**第一個 `>=` target** 這個形狀才是關鍵。

**帶索引排序的通用做法（可重複套用）**：題目要排序資料、但答案得是*原始*位置時，
在排序**之前**先把每個值跟索引配成對（`(val, idx)`），排序這些 pair，對值做二分搜尋，
再讀 `pair[1]` 拿回原始索引。LC 315 / LC 493 用的是同一招。

#### 3) 相似的 LC 題目

| 題目 | LC# | 二分搜尋扮演的角色 | 變化點 |
|---------|-----|--------------------|-------|
| **Find Right Interval** | **436** | lower bound：第一個 `start >= end_i` | 排序後位置 → 原始索引 |
| Search Insert Position | 35 | lower bound：第一個 `>= target` | 直接回傳插入索引 |
| Time Based Key-Value Store | 981 | **upper bound − 1**：時間戳的 floor | 每個 key 一份排序好的時間戳清單 |
| Two Sum II (sorted) | 167 | 在排序好的另一半中搜補數 | 也可以用雙指標 |
| Find First and Last Position | 34 | 左邊界 + 右邊界搜尋 | 兩次 lower/upper-bound 呼叫 |
| Count of Smaller After Self | 315 | `SortedList` + `bisect`，由右往左掃 | 保留索引的計數 |
| My Calendar I | 729 | 用 `SortedDict` 做 floor/ceiling | 在有序 map 上檢查重疊 |
| Data Stream as Disjoint Intervals | 352 | 用 floor/ceiling 合併區間 | 有序的區間 map |

> **辨識訊號**：對一個可以**一次排序完**的集合，「對每個元素，找出*最小的 `>=` X*（或*最大的
> `<=` X*）」→ 排序 + lower/upper-bound 二分搜尋。
> 如果這個集合**會隨時間變動**，改用 `SortedList`／`SortedDict`（見
> [python_trick.md §1-27-3](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md)）。

---

### 17) Find in Mountain Array (LC 1095) ⭐⭐⭐⭐⭐

> **山形陣列**先嚴格遞增到峰值，再嚴格遞減。找出值等於 `target` 的**最小索引**（沒有就回 `-1`），只能用 `arr.get(i)` / `arr.length()` — 而且 `get()` 呼叫次數要越少越好。

#### 核心想法 — 三次二分搜尋，不是一次

陣列本身沒有排序，但它是**兩段排序好的區間接起來**的。所以：

```text
1) find the PEAK             -> hill-climbing binary search (same as LC 162 / LC 852, §2 above)
2) binary search [0, peak]   -> ASCENDING  order
3) binary search [peak+1, n) -> DESCENDING order   <-- the twist most people miss
```

先搜左半段，因為題目要的是最小索引。

讓步驟 3 成立的**遞減方向翻轉**技巧，在
[binary_search.md](./binary_search.md) §2.4 講過一次。
```java
// java
// LC 1095 - Find in Mountain Array
// IDEA: 1) hill-climb binary search for the peak, 2) ascending BS on left half,
//       3) descending BS on right half (flip the comparison)
// time = O(log n)  (~3 * log n get() calls), space = O(1)
class Solution {
    public int findInMountainArray(int target, MountainArray arr) {
        int n = arr.length();
        int l = 0, r = n - 1;
        while (l < r) {                                     // 1) peak = first i with a[i] > a[i+1]
            int mid = l + (r - l) / 2;
            if (arr.get(mid) < arr.get(mid + 1)) l = mid + 1;   // uphill  -> peak on the right
            else r = mid;                                       // downhill-> peak at mid or left
        }
        int peak = l;

        int idx = bs(arr, target, 0, peak, true);           // 2) ascending half (smallest index first)
        if (idx != -1) return idx;
        return bs(arr, target, peak + 1, n - 1, false);     // 3) descending half
    }

    // one binary search that handles BOTH orders
    private int bs(MountainArray arr, int target, int l, int r, boolean asc) {
        while (l <= r) {
            int mid = l + (r - l) / 2;
            int val = arr.get(mid);
            if (val == target) return mid;
            if ((val < target) == asc) l = mid + 1;   // asc: too small -> right; desc: too small -> left
            else r = mid - 1;
        }
        return -1;
    }
}
```

```python
# python
# LC 1095 - Find in Mountain Array
# IDEA: peak via hill-climbing BS, then ascending BS on the left half and descending BS on the right
# time = O(log n), space = O(1)
class Solution:
    def findInMountainArray(self, target, mountain_arr):
        n = mountain_arr.length()

        # 1) find peak
        l, r = 0, n - 1
        while l < r:
            mid = (l + r) // 2
            if mountain_arr.get(mid) < mountain_arr.get(mid + 1):
                l = mid + 1          # uphill  -> peak on the right
            else:
                r = mid              # downhill-> peak at mid or to the left
        peak = l

        # 2) ascending half first (we want the SMALLEST index)
        idx = self.bs(mountain_arr, target, 0, peak, True)
        if idx != -1:
            return idx
        # 3) descending half
        return self.bs(mountain_arr, target, peak + 1, n - 1, False)

    def bs(self, arr, target, l, r, asc):
        while l <= r:
            mid = (l + r) // 2
            val = arr.get(mid)
            if val == target:
                return mid
            if (val < target) == asc:   # asc -> move right; desc -> move left
                l = mid + 1
            else:
                r = mid - 1
        return -1
```

**面試筆記**
- `arr.get()` 是**有次數限制的 API**（LC 1095 上限 100 次） — 什麼都不用快取，把呼叫次數控制在 `3 log n` 就好。絕對不要線性掃描去找峰值。
- 找峰值用的是 `while (l < r)` 搭配 `r = mid` — 為什麼 `l <= r` 會衝過 `mid + 1`，見 §2（Find Peak Element）。
- 找峰值會比較 `a[mid]` 和 `a[mid+1]`，所以 `mid + 1` 必須在範圍內 — 因為 `r = n - 1` 且 `mid < r`，這點有保證。

**相似題目**

| LC | 題目 | 關聯 |
|----|---------|----------|
| **1095** | Find in Mountain Array | 本題 — 找峰值 + 兩次有序搜尋 |
| 852 | Peak Index in a Mountain Array | 只有步驟 1（找峰值） |
| 162 | Find Peak Element | 不保證是山形時的找峰值 |
| 33 / 153 | Search in Rotated Sorted Array | 同樣是「兩段排序區間」，但切分規則不同（[binary_search.md](./binary_search.md) §1.2） |

---

### 18) Longest Increasing Subsequence — `tails` 陣列 (LC 300) ⭐⭐⭐⭐⭐

> 用 `O(n log n)` 求最長遞增子序列。`O(n²)` 的 DP 是預期中的第一個答案；二分搜尋版才是 FAANG 面試官接著要的追問。

#### 核心想法

維護 `tails[k]` = 長度為 `k + 1` 的遞增子序列，**可能的最小結尾值**。

- `tails` **永遠是遞增排序的** → 可以二分搜尋。
- 對每個 `x`，找 `lower_bound(tails, x)`（第一個 `>= x` 的 tail）：
  - 索引 `== len(tails)` → `x` 把最長的那串再延長 → **append**
  - 否則 → 用比較小的 `x` **覆蓋**那個 tail（保留未來的可能性）
- 答案 = `len(tails)`。

> `tails` **不是**真正的子序列 — 只有它的**長度**有意義。

```text
nums = [10, 9, 2, 5, 3, 7, 101, 18]
10  -> [10]
 9  -> [9]              (replace: length-1 run can end smaller)
 2  -> [2]
 5  -> [2,5]
 3  -> [2,3]            (replace 5)
 7  -> [2,3,7]
101 -> [2,3,7,101]
 18 -> [2,3,7,18]       -> answer = 4
```

```java
// java
// LC 300 - Longest Increasing Subsequence
// IDEA: patience sorting - tails[k] = smallest tail of an increasing run of length k+1,
//       binary search (lower_bound) for the slot to extend or overwrite
// time = O(n log n), space = O(n)
public int lengthOfLIS(int[] nums) {
    int[] tails = new int[nums.length];
    int size = 0;
    for (int x : nums) {
        int l = 0, r = size;
        while (l < r) {                 // lower_bound: first tails[i] >= x
            int mid = l + (r - l) / 2;
            if (tails[mid] < x) l = mid + 1;
            else r = mid;
        }
        tails[l] = x;                   // overwrite ...
        if (l == size) size++;          // ... or append (l == size)
    }
    return size;
}
```

```python
# python
# LC 300 - Longest Increasing Subsequence
# IDEA: keep sorted `tails` array, bisect_left = lower_bound -> replace, else append
# time = O(n log n), space = O(n)
import bisect

class Solution:
    def lengthOfLIS(self, nums):
        tails = []
        for x in nums:
            i = bisect.bisect_left(tails, x)   # first tail >= x
            if i == len(tails):
                tails.append(x)                # x extends the longest run
            else:
                tails[i] = x                   # smaller tail for the same length
        return len(tails)
```

**變形 — 嚴格遞增 vs 非遞減**（經典的差一位陷阱）：

```text
strictly increasing (LC 300)      -> bisect_left  / lower_bound  (first tail >= x)
non-decreasing (duplicates OK)    -> bisect_right / upper_bound  (first tail >  x)
```

**變形 — LC 354 Russian Doll Envelopes** = 二維的 LIS。寬度**遞增**排序，寬度相同時高度**遞減**排序（這樣同寬的兩個信封絕不會同時被選中），然後對高度跑一模一樣的 LIS：

```python
# python
# LC 354 - Russian Doll Envelopes
# IDEA: sort by (w asc, h desc) -> reduces to LIS on heights
# time = O(n log n), space = O(n)
import bisect

def maxEnvelopes(envelopes):
    envelopes.sort(key=lambda e: (e[0], -e[1]))   # ties: h DESC blocks same-width chains
    tails = []
    for _, h in envelopes:
        i = bisect.bisect_left(tails, h)
        if i == len(tails):
            tails.append(h)
        else:
            tails[i] = h
    return len(tails)
```

```java
// java
// LC 354 - Russian Doll Envelopes
// IDEA: sort (w asc, h desc), then LIS on the heights array (reuse lengthOfLIS above)
// time = O(n log n), space = O(n)
public int maxEnvelopes(int[][] envelopes) {
    Arrays.sort(envelopes, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);
    int[] heights = new int[envelopes.length];
    for (int i = 0; i < envelopes.length; i++) heights[i] = envelopes[i][1];
    return lengthOfLIS(heights);
}
```

**相關 — 把二分搜尋當成 DP 轉移的查表**

| LC | 題目 | 二分搜尋怎麼用 |
|----|---------|---------------------------|
| **300** | Longest Increasing Subsequence | `tails` + lower bound |
| **354** | Russian Doll Envelopes | 按 (w 遞增, h 遞減) 排序 → 對高度做 LIS |
| 1235 | Maximum Profit in Job Scheduling | 工作按結束時間排序；二分搜尋**最後一個結束時間 `<= start_i` 的工作**，然後 `dp[i] = max(dp[i-1], profit + dp[j])` |
| 1751 | Maximum Number of Events That Can Be Attended II | 同樣是「按結束時間排序 + 二分搜尋前一個相容項」的 DP，多一個 `k` 場次的維度 |
| 1027 | Longest Arithmetic Subsequence | 對 `(index, diff)` 做 DP — 用雜湊表，**不是**二分搜尋（要分得出差別） |

---

### 19) Random Pick with Weight (LC 528) ⭐⭐⭐⭐

> `pickIndex()` 回傳索引 `i` 的機率必須是 `w[i] / sum(w)`。

#### 核心想法 — 把權重變成連續區段，再二分搜尋

先算前綴和，在 `[1, total]` 之間抽一個均勻整數 `target`，回傳**第一個 `>= target` 的前綴和**（也就是 `lower_bound`）。每個索引 `i` 剛好佔掉 `total` 個位置中的 `w[i]` 個 → 機率正好是 `w[i] / total`。

```text
w      = [1,  3,  2]
prefix = [1,  4,  6]
target:   1 | 2 3 4 | 5 6
index :   0 |   1   |  2
```

```java
// java
// LC 528 - Random Pick with Weight
// IDEA: prefix sums split [1, total] into per-index ranges; lower_bound maps a uniform draw to an index
// time = ctor O(n), pickIndex O(log n); space = O(n)
class Solution {
    private int[] prefix;
    private Random rand = new Random();

    public Solution(int[] w) {
        prefix = new int[w.length];
        int s = 0;
        for (int i = 0; i < w.length; i++) { s += w[i]; prefix[i] = s; }
    }

    public int pickIndex() {
        int target = rand.nextInt(prefix[prefix.length - 1]) + 1;   // uniform in [1, total]
        int l = 0, r = prefix.length - 1;
        while (l < r) {                       // lower_bound: first prefix >= target
            int mid = l + (r - l) / 2;
            if (prefix[mid] < target) l = mid + 1;
            else r = mid;
        }
        return l;
    }
}
```

```python
# python
# LC 528 - Random Pick with Weight
# IDEA: prefix sums + bisect_left (lower bound) on a uniform draw in [1, total]
# time = __init__ O(n), pickIndex O(log n); space = O(n)
import bisect, random

class Solution:
    def __init__(self, w):
        self.prefix = []
        s = 0
        for x in w:
            s += x
            self.prefix.append(s)
        self.total = s

    def pickIndex(self):
        target = random.randint(1, self.total)          # inclusive on both ends
        return bisect.bisect_left(self.prefix, target)  # first prefix >= target
```

**差一位的防呆** — 挑**一種**慣例，然後從一而終：
- 在 `[1, total]` 抽 → `bisect_left`（第一個前綴和 `>= target`） ✅（上面用的就是這個）
- 在 `[0, total)` 抽 → `bisect_right`（第一個前綴和 `> target`）
- 兩者混用會安靜地讓索引 `0` 的機率變成 `0`，或產生超出範圍的索引。

**相似題目 — 「對排序好的歷史／累積陣列做二分搜尋」**

| LC | 題目 | 排序陣列裡放什麼 | 查詢方式 |
|----|---------|------------------------------|-------|
| **528** | Random Pick with Weight | 權重的前綴和 | 對隨機抽樣做 lower bound |
| 497 | Random Point in Non-overlapping Rectangles | 每個矩形點數的前綴和 | 一樣的 lower bound，再在矩形內挑點 |
| 911 | Online Election | 時間陣列 + 該時刻的領先者陣列 | upper bound − 1（時間的 floor） |
| 1146 | Snapshot Array | 每個索引一份 `(snap_id, val)` 清單 | upper bound − 1（最新且 `<= snap_id` 的值） |
| 1348 | Tweet Counts Per Frequency | 每個名稱一份排序好的推文時間 | 兩個邊界 → 數出 `[start, end]` 內的數量 |
| 981 | Time Based Key-Value Store | 每個 key 一份排序好的時間戳 | upper bound − 1（上面 §13） |

> 這六題是同一個模板：**維護一個排序好的陣列，每次查詢用 `lower_bound` / `upper_bound − 1` 回答。** 變的只是陣列裡裝什麼。

---

## 依模式分類的題目

| 模板（在 [binary_search.md](./binary_search.md)） | 這裡的題目 |
|---|---|
| 標準精確搜尋，`while l <= r` — §2.1 | LC 167, LC 367, LC 69, LC 441 |
| Lower bound／第一個 `>=` — §1.3 | LC 278, LC 744, LC 436, LC 1150, LC 528, LC 300 |
| Upper bound − 1／floor 查詢 — §1.3 | LC 981 |
| 半開區間 `while l < r`、`r = mid` — §2.0 | LC 162, LC 852, LC 540, LC 154 |
| 用差距的 `while l < r - 1` — §2.0 | LC 1060 |
| 旋轉／兩段排序區間 — §1.2, §2.4 | LC 154, LC 1095 |
| 二分搜尋切分點，而不是索引 — §2.0 | LC 4 |
| 二分搜尋長度 + 視窗驗證 — §1.4 | LC 209 |

## 總結

- **這份存放處不是模板目錄。** 這裡有題目讓你看不懂，先回主文件讀它的模板 — 下面每一份解法都是某個模板的實例。
- **`lower_bound` 是主力。** LC 278、744、436、1150、528、300 全都是「單調謂詞第一次為真的索引」，只是寫成了六種樣子。
- **`while l < r` 搭配 `r = mid` 是為了收斂，不是為了比對** — LC 162、540、154 和 1095 的找峰值都不測試相等；它們是把 `l` 和 `r` 擠到同一個索引上。
- **看資料，不是看搜尋。** LC 436、981 和 528 全都是「維護一個排序陣列，每次查詢用邊界回答」 — 變的只有陣列裡存什麼。

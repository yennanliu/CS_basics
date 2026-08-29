# 滑動視窗

> **範圍** — 依條件伸縮的視窗 — 固定大小、可變大小、至多 k 個，以及用相減湊出的恰好 k 個；擴張／收縮迴圈與六個標準視窗模板都歸這裡管。
> **另見** — *從本檔拆出*：[sliding_window_examples.md](./sliding_window_examples.md) — LC 實作解法庫，每題每種語言一份標準解；[sliding_window_advanced.md](./sliding_window_advanced.md) — 雙端佇列求極值、至多 K 的通用化、超出單一題目的恰好 K、補集／單字層級／分桶視窗。
> *相鄰文件*：[2_pointers.md](./2_pointers.md) — 相向逼近而非一前一後的指標；[hash_map.md](./hash_map.md) — 多數視窗都會帶著的計數表；[monotonic_queue.md](./monotonic_queue.md) — 用 O(n) 求視窗極值；[prefix_sum.md](./prefix_sum.md) — 視窗內可能有負值時該去的地方。

## LeetCode 題目清單

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)

## 總覽

**滑動視窗**用兩個指標在陣列或字串上維護一個「視窗」，靠擴張與收縮有效率地找出最佳解。

### 關鍵性質
- **時間複雜度**：O(n) — 每個元素最多被走訪兩次
- **空間複雜度**：指標本身 O(1)，視窗狀態 O(k)
- **核心想法**：維護一個在資料結構上滑動的視窗 [left, right]
- **兩個階段**： 
  - **擴張**：右指標往前，把視窗撐大
  - **收縮**：視窗不合法時，左指標往前縮小視窗

### 什麼時候用滑動視窗
- **子陣列／子字串題**：找出具有特定性質的最佳子陣列
- **視窗型限制**：牽涉到固定或可變視窗大小的題目
- **最佳化**：在限制條件下求最小／最大長度、數量或總和
- **字元／元素追蹤**：需要統計出現次數的題目

### 參考資料
- [labuladong Sliding Window Guide](https://labuladong.online/algo/essential-technique/sliding-window-framework/)
- [Sliding Window Template Collection](https://leetcode.com/discuss/general-discussion/657507/sliding-window-for-beginners-problems-template-sample-solutions/)

## 題型分類

### 核心組成
1. **雙指標**：用 `left` 和 `right` 界定視窗邊界
2. **迴圈結構**：
    - `while-while`：外層擴張，內層收縮
    - `for-while`：for 負責擴張，while 負責收縮
    - **關鍵洞見**：第一層迴圈找出可行解，第二層迴圈把它優化成最佳解
3. **視窗狀態**：追蹤當前視窗內的元素、次數或總和
4. **合法性條件**：定義視窗什麼時候合法／不合法


<p align="center"><img src="../pic/slide_window.png"></p>

<p align="center"><img src="../pic/slide_window_2.png"></p>

<p align="center"><img src="../pic/slide_window_3.png"></p>


### 五種視窗型態

#### **固定大小視窗**
- **說明**：視窗大小事先給定且固定不變
- **範例**：LC 438（Find All Anagrams）、LC 567（Permutation in String）
- **模式**：維持固定的視窗大小，一次滑動一格

#### **可變大小視窗 — 求最大**
- **說明**：找出滿足限制的最大視窗
- **範例**：LC 3（Longest Substring）、LC 424（Character Replacement）
- **模式**：擴張到不合法為止，記錄最大值，然後收縮

#### **可變大小視窗 — 求最小**  
- **說明**：找出滿足限制的最小視窗
- **範例**：LC 209（Minimum Subarray Sum）、LC 76（Minimum Window Substring）
- **模式**：收縮到不合法為止，記錄最小值，然後擴張

#### **子陣列計數**
- **說明**：計算符合條件的子陣列／子字串個數
- **範例**：LC 713（Subarray Product）、LC 992（Subarrays with K Different）
- **模式**：對每個右端點，數出合法的左端點有幾個

#### **字串比對（雜湊式）**
- **說明**：追蹤視窗內的字元出現次數
- **範例**：LC 567（Permutation）、LC 438（Anagrams）、LC 76（Window Substring）
- **模式**：用 HashMap/Counter 追蹤字元次數

### 視窗狀態與輔助工具
- **技巧**：雙指標、滑動視窗、次數統計
- **資料結構**：HashMap、Counter、Set、陣列
- **輔助工具**：Collections.Counter（Python）、HashMap.getOrDefault（Java）

### 固定大小視窗 vs 可變大小視窗
| 類型 | 什麼時候用 | 收縮條件 | 範例 |
|------|------------|-----------------|---------|
| 固定大小 k | 題目直接給定視窗大小 | `right - left + 1 > k` | LC 567 (Permutation in String) |
| 可變（求最小） | 找最小的合法視窗 | 視窗合法時就收縮 | LC 76 (Min Window Substring) |
| 可變（求最大） | 找最大的合法視窗 | 視窗不合法時才收縮 | LC 3 (Longest No-Repeat) |
| 恰好 K → 至多 K | 統計符合精確限制的視窗數 | 不適用 — 用相減技巧 | LC 992, LC 1248 |

## 模板與演算法

六個模板涵蓋所有必會的滑動視窗型態。模板 2 是第一個要背到能默寫的 —
這個家族裡所有可變大小視窗，都只是換掉它的合法性判斷和結果更新而已。

### 模板比較表

| # | 模板 | 型態 | 結果更新 | 時間／空間 | 代表題 |
|---|----------|-------|---------------|--------------|-----------------|
| 1 | 固定大小視窗 | `for i` + 逐出 `i - k` | `i >= k - 1` 時判斷 | O(n) / O(k) | LC 643, 438, 567 |
| 2 | 先擴後縮（`while` 不變量） | `for right` + `while invalid: shrink` | 任何合法視窗 | O(n) / O(k) | 3–6 的基底 |
| 3 | 滿足 P 的最長視窗 | **不合法時**收縮 | `max(res, r - l + 1)` | O(n) / O(k) | LC 3, 424, 1004 |
| 4 | 滿足 P 的最短視窗 | **合法時**收縮 | `min(res, r - l + 1)` | O(n) / O(k) | LC 209, 76 |
| 5 | 字元計數視窗（`have`/`need`） | 次數表 + 匹配計數器 | 在 `have == need` 時更新 | O(n) / O(charset) | LC 76, 438, 567 |
| 6 | 用「至多」相減湊出「恰好 K」 | 跑兩次至多 | `count += r - l + 1` | O(n) / O(k) | LC 992, 1248, 930 |

> 第 3 列和第 4 列只差**一個字**：最長是在視窗*不合法*時收縮，最短是在*合法*時收縮。
> 這個字寫錯，答案就會默默地錯掉。

### 模板 1：固定大小視窗 ⭐⭐⭐⭐⭐

**實作範例**：LC 643、LC 438、LC 567、LC 1456、LC 219 — 見 [sliding_window_examples.md](./sliding_window_examples.md)。

> *這是大綱，不能直接跑* — `meets_condition` / `meetsCondition` 是你要自己填的題目專屬判斷。

**適用情境**：字母重排、排列、長度為 k 的子字串
**模式**：維持固定的視窗大小，一次滑動一格

```python
# Fixed Size Window Template
def fixed_window(s, k):
    window = {}
    result = []
    
    for i in range(len(s)):
        # Add current element to window
        window[s[i]] = window.get(s[i], 0) + 1
        
        # Remove element that's outside window
        if i >= k:
            left_char = s[i - k]
            window[left_char] -= 1
            if window[left_char] == 0:
                del window[left_char]
        
        # Process window when it reaches target size
        if i >= k - 1:
            # Check condition and update result
            if meets_condition(window):
                result.append(i - k + 1)
    
    return result
```

```java
// Fixed Size Window Template - Java
public List<Integer> fixedWindow(String s, int k) {
    Map<Character, Integer> window = new HashMap<>();
    List<Integer> result = new ArrayList<>();
    
    for (int i = 0; i < s.length(); i++) {
        // Add current element
        char cur = s.charAt(i);
        window.put(cur, window.getOrDefault(cur, 0) + 1);
        
        // Remove element outside window
        if (i >= k) {
            char leftChar = s.charAt(i - k);
            window.put(leftChar, window.get(leftChar) - 1);
            if (window.get(leftChar) == 0) {
                window.remove(leftChar);
            }
        }
        
        // Process when window is full
        if (i >= k - 1 && meetsCondition(window)) {
            result.add(i - k + 1);
        }
    }
    return result;
}
```

### 模板 2：先擴後縮 — `while` 不變量 ⭐⭐⭐⭐⭐

**整份文件最重要的一個慣用寫法。** 一個 `for` 推進 `right` 並加入元素；一個 `while`
推進 `left` 直到視窗重新合法。因為 `left` 永遠不會往回走，每個元素最多被加入一次、
移除一次 → O(n)，不管合法性判斷怎麼寫都一樣。

```text
for right in range(n):         # 1. expand: the window may now be invalid
    add(a[right])
    while not valid(window):   # 2. restore the invariant (may run 0 times)
        remove(a[left]); left += 1
    update_result(left, right)  # 3. the window is valid HERE, and only here
```

那三個空位就是整個設計空間：`add`/`remove` 維護什麼、`valid` 判斷什麼、
`update_result` 記錄什麼。模板 3–6 就是把這個迴圈的空位填滿而已。

> *這是大綱，不能直接跑* — `is_valid`、`update_window_state` 和 `update_result`
> 是題目專屬的空位。

```python
# Python Universal Template
def sliding_window(s, condition):
    # Initialize window state
    left = 0
    window_state = {}  # or Counter, set, etc.
    result = initialize_result()
    
    # Expand window with right pointer
    for right in range(len(s)):
        # Add current element to window
        update_window_state(s[right])
        
        # Contract window while invalid
        while not is_valid(window_state):
            # Remove leftmost element
            remove_from_window(s[left])
            left += 1
        
        # Update result with current valid window
        result = update_result(result, left, right)
    
    return result
```

```java
// Java Universal Template  
public ResultType slidingWindow(String s) {
    // Initialize window state
    int left = 0;
    Map<Character, Integer> window = new HashMap<>();
    ResultType result = initializeResult();
    
    // Expand window with right pointer
    for (int right = 0; right < s.length(); right++) {
        char rightChar = s.charAt(right);
        window.put(rightChar, window.getOrDefault(rightChar, 0) + 1);
        
        // Contract window while invalid
        while (!isValid(window)) {
            char leftChar = s.charAt(left);
            window.put(leftChar, window.get(leftChar) - 1);
            if (window.get(leftChar) == 0) {
                window.remove(leftChar);
            }
            left++;
        }
        
        // Update result with current valid window
        result = updateResult(result, left, right);
    }
    
    return result;
}
```

### 模板 3：滿足 P 的最長視窗 — LC 3 ⭐⭐⭐⭐⭐

**適用情境**：最長子字串類題目、最大合法視窗
**模式**：擴張到不合法為止，記錄最大值，然後收縮

**不變量**：**視窗不合法時**才收縮，所以每輪迴圈結束時，視窗就是以 `right` 結尾的最長合法視窗。
在 `while` *之後*記錄 `r - l + 1`，絕對不要寫在裡面。

```java
// LC 3 - Longest Substring Without Repeating Characters
// IDEA: Sliding window with HashSet to track characters in window
// time = O(N), space = O(min(N, charset))
public int lengthOfLongestSubstring(String s) {
    Set<Character> set = new HashSet<>();
    int l = 0, ans = 0;
    for (int r = 0; r < s.length(); r++) {
        while (set.contains(s.charAt(r))) {
            set.remove(s.charAt(l++));
        }
        set.add(s.charAt(r));
        ans = Math.max(ans, r - l + 1);
    }
    return ans;
}
```

```python
# LC 003 Longest Substring Without Repeating Characters
# IDEA : SLIDING WINDOW + DICT
#       -> use a hash table (d) record visited "element" (e.g. : a,b,c,...)
#          (but NOT sub-string)
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        d = {}
        # left pointer
        l = 0
        res = 0
        # right pointer
        for r in range(len(s)):
            """
            ### NOTE : we deal with "s[r] in d" case first 
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

> Java 版一次縮一個字元；Python 版用「上次出現的索引」把 `left` 直接**跳**到 `d[c] + 1`。
> 同一個不變量，兩種寫法 — 正是因為會跳，所以需要 `max(l, ...)`，才不會讓過期的索引把
> `left` 拉回去。

### 模板 4：滿足 P 的最短視窗 — LC 209 ⭐⭐⭐⭐⭐

**適用情境**：最小覆蓋子字串、最小合法視窗
**模式**：擴張到合法為止，記錄最小值，然後試著收縮

**不變量**：**視窗合法時**就收縮，每次收縮*之前*先記錄長度。
這就是模板 3 把 `while` 條件反過來 — 其他一個字都沒變。

```java
// LC 209 - Minimum Size Subarray Sum
// IDEA: Sliding window — shrink left when sum >= target, record min length
// time = O(N), space = O(1)
public int minSubArrayLen(int target, int[] nums) {
    int l = 0, sum = 0, minLen = Integer.MAX_VALUE;
    for (int r = 0; r < nums.length; r++) {
        sum += nums[r];
        while (sum >= target) {
            minLen = Math.min(minLen, r - l + 1);
            sum -= nums[l++];
        }
    }
    return minLen == Integer.MAX_VALUE ? 0 : minLen;
}
```

```python
# LC 209 Minimum Size Subarray Sum
# IDEA : SLIDING WINDOW : start, end
class Solution:
    def minSubArrayLen(self, s, nums):
        if nums is None or len(nums) == 0:
            return 0

        n = len(nums)
        minLength = n + 1
        sum = 0
        j = 0
        for i in range(n):
            ### NOTE the while loop condition (j < n and sum < s)
            while j < n and sum < s:
                sum += nums[j]
                j += 1
            # NOTE : we need to check if sum >= s here
            if sum >= s:
                minLength = min(minLength, j - i)

            ### NOTE : we need to get min length of sub array
            #          so once it meats the condition (sum >= s)
            #          we should update the minLength (minLength = min(minLength, j - i))
            #          and move to next i and roll back _sum (_sum -= nums[i])
            sum -= nums[i]
            
        ### NOTE : if minLength == n + 1, means there is no such subarray, so return 0 instead
        if minLength == n + 1:
            return 0         
        return minLength
```

### 模板 5：帶 have/need 計數器的字元計數視窗 — LC 76 ⭐⭐⭐⭐⭐

**模式**：一張記錄視窗*還需要*什麼的次數表，加上一個整數，記錄視窗*已經湊到*多少。
有了這個計數器，合法性判斷才會是 O(1)，而不是每一步都做 O(charset) 的次數表比對 —
這正是面試官會追問的細節。

經典的「合法就收縮」可變大小視窗：

```python
from collections import Counter

def minWindow(s, t):
    need = Counter(t)
    missing = len(t)
    best = ""
    left = 0

    for right, c in enumerate(s):
        if need[c] > 0:
            missing -= 1
        need[c] -= 1

        if missing == 0:              # valid window found
            # Shrink from left
            while need[s[left]] < 0:
                need[s[left]] += 1
                left += 1
            if not best or right - left + 1 < len(best):
                best = s[left:right+1]
            # Break window to search for next
            need[s[left]] += 1
            missing += 1
            left += 1

    return best
```

```java
// LC 76 - Minimum Window Substring
// IDEA: Sliding window with frequency maps; shrink when window is valid
// time = O(N + M), space = O(N + M)
public String minWindow(String s, String t) {
    Map<Character, Integer> need = new HashMap<>(), window = new HashMap<>();
    for (char c : t.toCharArray()) need.merge(c, 1, Integer::sum);
    int l = 0, valid = 0, start = 0, minLen = Integer.MAX_VALUE;
    for (int r = 0; r < s.length(); r++) {
        char c = s.charAt(r);
        window.merge(c, 1, Integer::sum);
        if (need.containsKey(c) && window.get(c).equals(need.get(c))) valid++;
        while (valid == need.size()) {
            if (r - l + 1 < minLen) { minLen = r - l + 1; start = l; }
            char d = s.charAt(l++);
            if (need.containsKey(d)) {
                if (window.get(d).equals(need.get(d))) valid--;
                window.merge(d, -1, Integer::sum);
            }
        }
    }
    return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
}
```

> 同一個計數器的兩種寫法：Python 追蹤 `missing`（還欠幾個字元，往 0 遞減），
> Java 追蹤 `valid`（已經湊滿的字元種類數，往 `need.size()` 遞增）。
> 直接比較兩張次數表的偷懶做法，只有**固定**大小視窗才划算；
> 見 [sliding_window_examples.md](./sliding_window_examples.md) 的 LC 438 / LC 567。

### 模板 6：用「至多」相減湊出「恰好 K」 — LC 992 ⭐⭐⭐⭐⭐

**核心洞見：**
「恰好 K」的題目通常很難直接做，但可以用這個威力強大的公式轉換：

```text
Exactly K = At Most K - At Most (K-1)
```

**為什麼成立：**
```text
At Most K: All subarrays with ≤ K distinct/count
At Most (K-1): All subarrays with ≤ K-1 distinct/count

Difference: Only subarrays with EXACTLY K distinct/count
```

**用例子證明：**
```text
Array: [1, 2, 1, 2, 3]
K = 2 (exactly 2 distinct integers)

At Most 2 distinct:
[1], [1,2], [1,2,1], [1,2,1,2], [2], [2,1], [2,1,2], [1], [1,2], [2], [2,3], [3]
Count = 12

At Most 1 distinct:
[1], [2], [1], [2], [3]
Count = 5

Exactly 2 distinct = 12 - 5 = 7 ✓
[1,2], [1,2,1], [1,2,1,2], [2,1], [2,1,2], [1,2], [2,3]
```

#### 計數的那一行：`count += right - left + 1`

一旦 `[left, right]` 是以 `right` 結尾的*最長*合法視窗，它的每個後綴也都合法 —
所以以 `right` 結尾的子陣列恰好有 `right - left + 1` 個。就是這一行，把模板 3 變成了計數器。

**適用情境**：統計符合條件的子陣列個數
**模式**：對每個右端點，數出合法的左端點有幾個

> *這是大綱，不能直接跑* — `initialize_state`、`update_window_state`、`is_valid` 和
> `remove_from_window` 是題目專屬的空位；下面的實作範例會把它們填起來。

```python
# Subarray Counting Template
def count_subarrays(nums, condition):
    left = 0
    count = 0
    window_state = initialize_state()
    
    for right in range(len(nums)):
        # Add current element
        update_window_state(nums[right])
        
        # Shrink window while invalid
        while not is_valid(window_state):
            remove_from_window(nums[left])
            left += 1
        
        # Count valid subarrays ending at 'right'
        count += right - left + 1

    return count
```

#### 實作範例 — Subarrays with K Different Integers

**題目：** 統計恰好含 K 個相異整數的子陣列個數。

```python
# Python - LC 992 Subarrays with K Different Integers
def subarraysWithKDistinct(nums, k):
    """
    Count subarrays with exactly K distinct integers.

    Time: O(n)
    Space: O(k)

    Key: Use Exactly K = At Most K - At Most (K-1) transformation
    """
    def at_most_k_distinct(k):
        """Count subarrays with at most K distinct integers."""
        left = 0
        count = 0
        freq = {}

        for right in range(len(nums)):
            # Add right element
            freq[nums[right]] = freq.get(nums[right], 0) + 1

            # Shrink while > k distinct
            while len(freq) > k:
                freq[nums[left]] -= 1
                if freq[nums[left]] == 0:
                    del freq[nums[left]]
                left += 1

            # Count subarrays ending at right
            count += right - left + 1

        return count

    # Edge case
    if k == 0:
        return 0

    # Exactly K = At Most K - At Most (K-1)
    return at_most_k_distinct(k) - at_most_k_distinct(k - 1)

# Example:
# nums = [1,2,1,2,3], k = 2
# at_most_k(2) = 12
# at_most_k(1) = 5
# exactly_k(2) = 12 - 5 = 7 ✓
```

```java
// Java - LC 992 Subarrays with K Different Integers
/**
 * time = O(N)
 * space = O(K)
 */
public int subarraysWithKDistinct(int[] nums, int k) {
    // Exactly K = At Most K - At Most (K-1)
    return atMostK(nums, k) - atMostK(nums, k - 1);
}

private int atMostK(int[] nums, int k) {
    if (k == 0) return 0;

    int left = 0;
    int count = 0;
    Map<Integer, Integer> freq = new HashMap<>();

    for (int right = 0; right < nums.length; right++) {
        // Add right element
        freq.put(nums[right], freq.getOrDefault(nums[right], 0) + 1);

        // Shrink while > k distinct
        while (freq.size() > k) {
            freq.put(nums[left], freq.get(nums[left]) - 1);
            if (freq.get(nums[left]) == 0) {
                freq.remove(nums[left]);
            }
            left++;
        }

        // Count subarrays ending at right
        count += right - left + 1;
    }

    return count;
}
```

#### 用到這個轉換的題目

| 題目 | LC# | 難度 | 轉換方式 | 關鍵洞見 |
|---------|-----|------------|----------------|-------------|
| **Subarrays with K Different Integers** | **992** | **Hard** | 恰好 K 個相異 = atMost(K) - atMost(K-1) | 核心範例 |
| Count Vowel Substrings of a String | 2062 | Medium | 恰好 5 個母音 = atMost(5) - atMost(4) | 遇到子音就重置視窗（只能全母音） |
| Count Nice Subarrays | 1248 | Medium | 恰好 K 個奇數 = atMost(K) - atMost(K-1) | 把奇數轉成 1、偶數轉成 0 |
| Binary Subarrays With Sum | 930 | Medium | 總和恰好 K = atMost(K) - atMost(K-1) | 子陣列和 |
| Longest Substring with At Most K Distinct | 340 | Medium | 直接用 atMost(K) 求最大長度 | 不用相減 |
| Fruits Into Baskets | 904 | Medium | atMost(2) 個相異，求最大長度 | K=2 的簡化版 |
| Max Consecutive Ones III | 1004 | Medium | atMost(K) 個 0，求最大長度 | 統計 0 的個數 ≤ K |

> 受限字母集的重置變化（LC 2062）、單趟前綴和的替代解（LC 1248）、
> 圖解證明，以及「為什麼直接做恰好 K 很難」的論證，全都在
> [sliding_window_advanced.md](./sliding_window_advanced.md)。

## 總結與快速查表

### 該用哪個模板？— 決策表

| 題型 | 模板 | 關鍵模式 | 範例 |
|--------------|----------|-------------|----------|
| 找**固定**大小的視窗 | 1 — 固定大小 | `for i` 搭配大小追蹤 | LC 438, 567, 643 |
| 找**最大**的合法視窗 | 3 — 最長視窗 | `for-while`，**不合法**時收縮 | LC 3, 424, 1004 |
| 找**最小**的合法視窗 | 4 — 最短視窗 | `for-while`，**合法**時收縮 | LC 76, 209 |
| 比對字元多重集合 | 5 — 字元計數（`have`/`need`） | 次數表 + 匹配計數器 | LC 76, 438, 567 |
| **計算**合法子陣列個數 | 6 — 計數那一行 | `count += right-left+1` | LC 713, 992 |
| **恰好 K** 個相異／唯一 | 6 — 至多相減 | `atMostK(k) - atMostK(k-1)` | LC 992, 1248, 930 |
| 用 O(1) 求視窗最大／最小 | *不在本文件* | 單調雙端佇列 | LC 239 → [monotonic_queue.md](./monotonic_queue.md) |
| 值可能是**負數** | *根本不是視窗題* | 前綴和 + HashMap | LC 560, 974 → [prefix_sum.md](./prefix_sum.md) |

**怎麼用**：先確認題目的目標（最大／最小／計數／恰好），再挑對應的模板。模板 2 是第 2–6 列的底層 — 它是那個迴圈，不是另一個答案。

### 各模板複雜度速查

| 模板 | 時間 | 空間 | 空間花在哪 |
|----------|------|-------|----------------------|
| 1 — 固定大小 | O(n) | O(k) | 視窗本身的內容 |
| 2 — 先擴後縮 | O(n) | O(k) | 視窗狀態裝了什麼就是什麼 |
| 3 — 最長視窗 | O(n) | O(k) | 次數表／計數器 |
| 4 — 最短視窗 | O(n) | O(k) | 次數表／計數器 |
| 5 — 字元計數 | O(n + m) | O(charset) | 兩張大小取決於字母集的表 |
| 6 — 至多相減湊恰好 K | O(n) | O(k) | 一張表，陣列掃兩趟 |

> 全部都是 O(n)，因為 `left` 永遠不往回走：每個元素最多被加入一次、移除一次。
> **優化**：字母集有限時，改用固定的 `int[26]` / `int[128]` 陣列取代 HashMap —
> 漸近複雜度一樣，但實際快很多，比較起來也更單純。

### 依模式分類的題目

#### **固定大小視窗**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Find All Anagrams in a String | 438 | 字元次數比對 | Medium |
| Permutation in String | 567 | 字元次數比對 | Medium |
| Maximum Average Subarray I | 643 | 固定視窗求和 | Easy |
| Contains Duplicate II | 219 | 固定視窗 + HashSet | Easy |
| Maximum Number of Vowels | 1456 | 固定視窗計數 | Medium |

#### **可變大小 — 最大長度**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Longest Substring Without Repeating Characters | 3 | 追蹤字元唯一性 | Medium |
| Longest Repeating Character Replacement | 424 | 次數表 + 最大字元次數 | Medium |
| Max Consecutive Ones III | 1004 | K 次翻轉限制 | Medium |
| Longest Substring with At Most K Distinct Characters | 340 | 統計相異字元數 | Medium |
| Longest Substring with At Most Two Distinct Characters | 159 | 兩種相異字元的限制 | Medium |

#### **可變大小 — 最小長度** 
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Minimum Window Substring | 76 | 追蹤字元覆蓋程度 | Hard |
| Minimum Size Subarray Sum | 209 | 比較累加和 | Medium |
| Smallest Subarray with Sum ≥ K | 862 | 前綴和 + 雙端佇列 | Hard |
| Minimum Window with Characters | 1176 | 飲食計畫限制 | Hard |

#### **子陣列計數**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Subarray Product Less Than K | 713 | 乘積限制 | Medium |
| Subarrays with K Different Integers | 992 | 恰好 K = 至多 K - 至多 (K-1) | Hard |
| Count Vowel Substrings of a String | 2062 | 恰好 5 個母音 = atMost(5) - atMost(4)（遇子音重置） | Medium |
| Number of Subarrays with Bounded Maximum | 795 | 值域上下界限制 | Medium |
| Count Number of Nice Subarrays | 1248 | 統計奇數個數 | Medium |

#### **進階滑動視窗**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Sliding Window Maximum | 239 | 單調雙端佇列 | Hard |
| Sliding Window Median | 480 | 兩個堆積 | Hard |
| Minimum Swaps to Group All 1's Together | 1151 | 固定視窗上的最佳化 | Medium |
| Grumpy Bookstore Owner | 1052 | 狀態變更的最佳化 | Medium |

### 常見模式與技巧

#### **字元次數追蹤**
```python
# Track character counts in window
window = {}
window[char] = window.get(char, 0) + 1

# Remove character from window
window[char] -= 1
if window[char] == 0:
    del window[char]
```

#### **合法性條件**
```python
# Common validity checks
def is_valid_permutation(window, target):
    return window == target

def is_valid_distinct_k(window, k):
    return len(window) <= k

def is_valid_sum(current_sum, target):
    return current_sum >= target
```

#### **結果更新**
```python
# Maximum length problems
max_len = max(max_len, right - left + 1)

# Minimum length problems  
if is_valid:
    min_len = min(min_len, right - left + 1)

# Counting problems
count += right - left + 1  # All subarrays ending at 'right'
```

### 解題步驟

1. **辨認模式**：固定大小、可變求最大／最小，還是計數？
2. **選模板**：依模式挑對應的模板
3. **定義視窗狀態**：HashMap、集合、總和，還是計數器？
4. **定義合法性**：什麼情況下視窗算合法／不合法？
5. **更新邏輯**：什麼時候更新結果、怎麼更新？

### 常見錯誤與提醒

**🚫 常見錯誤：**
- 迴圈結構寫錯（選錯模板）
- 忘了正確維護視窗狀態
- 合法性條件的邏輯寫反
- 漏掉邊界情況（空輸入、只有一個元素）

**✅ 最佳實務：**
- 字元次數類題目直接用 `collections.Counter`
- 從 HashMap 移除元素時一定要處理歸零的情況
- 用邊界情況測：空字串、單一字元、所有字元都相同
- 想清楚題目要的是「恰好 k」還是「至多 k」
- 「恰好 k」的題目：用「至多 k - 至多 (k-1)」

### 面試訊號

| 訊號 | 模式 |
|--------|---------|
| 「帶限制的最長子字串／子陣列」 | 可變視窗，右邊擴張、左邊收縮 |
| 「包含所有字元的最小視窗」 | 合法就收縮（LC 76） |
| 「固定大小 k 的視窗」 | 固定視窗，兩端一起滑 |
| 「恰好 k 個相異／奇數／…」 | AtMost(k) - AtMost(k-1) |
| 「用 O(n) 求視窗最大／最小」 | 單調雙端佇列 |
| 「字串中的排列／字母重排」 | 固定視窗 + Counter 比對 |

### 其他內容在哪

| 想找什麼 | 去哪份文件 |
|---|---|
| LC 567、438、1004、424、1838、713、413、1151、763 的實作解法 | [sliding_window_examples.md](./sliding_window_examples.md) |
| 雙端佇列求極值、至多 K 個相異的家族、恰好 K 的深入探討、補集／單字層級／分桶視窗 | [sliding_window_advanced.md](./sliding_window_advanced.md) |
| 完整的單調雙端佇列家族（LC 239、862、1438、1499） | [monotonic_queue.md](./monotonic_queue.md) |
| 可能含負值的視窗 → 前綴和 + HashMap | [prefix_sum.md](./prefix_sum.md) |
| 相向逼近（而非一前一後）的指標 | [2_pointers.md](./2_pointers.md) |

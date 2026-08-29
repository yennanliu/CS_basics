# Sliding Window — 進階視窗形態

> **範圍** — 第一輪學習可以先跳過的滑動視窗技巧：用雙端佇列維護視窗極值、at-most-K-distinct 這一家、超出單一範例的 exactly-K 計數，以及那些「鍵不是字元」的視窗 —— 補集、以單字為單位的區塊、以索引界定的值桶，還有排序後的區間；六份必背模板留在主頁。
> **另見**：[sliding_window.md](./sliding_window.md) — 這裡每個技巧所依附的六份標準模板，也是 `Template 1-6` 這些引用指向的地方；[sliding_window_examples.md](./sliding_window_examples.md) — 模板本身的題解庫；[monotonic_queue.md](./monotonic_queue.md) — 完整的雙端佇列極值家族，本頁只指過去、不重推一次；[prefix_sum.md](./prefix_sum.md) — 當視窗和可能變負時的前綴和替代方案。

## LeetCode 題目清單

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)
- [Monotonic Queue](https://leetcode.com/problem-list/monotonic-queue/)
- [Rolling Hash](https://leetcode.com/problem-list/rolling-hash/)

## 總覽

底下每個技巧，都是 [sliding_window.md](./sliding_window.md) 那六份模板之一，只是某一格塞了不尋常的東西：視窗狀態變成雙端佇列而不是計數器、視窗的鍵變成一個單字或一個值桶而不是字元、視窗其實是被選中那堆東西的*補集*，或者答案需要跑兩趟視窗而不是一趟。

### 技巧索引

| 技巧 | LC# | 不尋常在哪 | 權重 |
|---|---|---|:---:|
| 用單調雙端佇列求視窗極值 | 239 | 視窗狀態是有序的 deque，不是計數 | 3 |
| 最多 K 種相異元素 | 340, 904, 159 | 收縮的判斷式是 `map.size() > K` | 4 |
| 字母表受限的 exactly-K | 2062 | 遇到禁用字元會*重置*視窗 | 4 |
| 用前綴和 + HashMap 取代視窗 | 560, 974, 525 | 合法性不單調，或值會變負 | 4 |
| 視窗內的前綴小技巧 | 1248 | 一趟就數完所有合法的左邊界 | 3 |
| 最少操作數 → 最大中段子陣列 | 1658 | 答案是視窗的*補集長度* | 4 |
| 補集視窗（「從兩端取」） | 1423 | 答案是 `total - 最小視窗` | 4 |
| 多個互不重疊的固定視窗 | 689, 1031 | 對視窗和做前綴／後綴 argmax | 3 |
| 索引固定的視窗 + 值分桶 | 220 | 視窗以索引界定，但判斷式看的是值 | 4 |
| 以單字為單位的視窗（固定長度區塊） | 30, 187 | 每個對齊偏移各跑一個視窗 | 4 |
| 排序區間上的雙指標 | 1229 | 兩個視窗，一個輸入陣列各一個 | 3 |

> **權重** = 面試出現頻率，1-5 分制，對應底下標題上的星星數。

## 視窗極值與 At-Most-K 視窗

### 用單調雙端佇列求視窗極值 — LC 239 ⭐⭐⭐

> **本頁不重新推導 deque。**[monotonic_queue.md](./monotonic_queue.md) 才是這一家的主人 ——
> 不變式、攤還 O(n) 的論證、求最小值的變形，以及更難的成員（LC 862、LC 1438、LC 1499）。
> 底下只給你辨識所需的最低限度：怎麼看出一道視窗題要的是 deque 而不是 map —— **答案是視窗的某個*極值***，
> 所以被壓過的元素可以永久丟棄。如果答案改成*中位數*，那就什麼都不能丟，你需要兩個堆積或一個有序 multiset。

需要 O(1) 拿到視窗最大／最小值時，用一個維持單調順序的 deque。

```python
from collections import deque

def maxSlidingWindow(nums, k):
    dq = deque()   # stores indices; nums[dq[0]] is always window max
    result = []

    for i, num in enumerate(nums):
        # Remove elements outside window
        if dq and dq[0] == i - k:
            dq.popleft()
        # Maintain decreasing order — remove smaller elements from back
        while dq and nums[dq[-1]] < num:
            dq.pop()
        dq.append(i)

        if i >= k - 1:
            result.append(nums[dq[0]])
    return result
```

**時間**：O(n) —— 每個元素最多進出 deque 一次。

> 維護一個遞減的索引 deque；front 永遠是當前視窗的最大值。

```java
// LC 239 - Sliding Window Maximum
// IDEA: Monotonic decreasing deque — front = max of current window
// time = O(N), space = O(k)
public int[] maxSlidingWindow(int[] nums, int k) {
    int n = nums.length;
    int[] ans = new int[n - k + 1];
    Deque<Integer> deque = new ArrayDeque<>(); // stores indices
    for (int i = 0; i < n; i++) {
        // remove out-of-window indices
        while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) deque.pollFirst();
        // maintain decreasing order
        while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) deque.pollLast();
        deque.offerLast(i);
        if (i >= k - 1) ans[i - k + 1] = nums[deque.peekFirst()];
    }
    return ans;
}
```

### 最多 K 種相異元素 ⭐⭐⭐⭐

**形狀**：模板 3（最長視窗）加上 `while len(window_map) > K: shrink`。整個家族就是同一份模板換不同的 `K`，而它同時也是 [Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) 裡 exactly-K 相減的 `atMost` 那一半。

| 題目 | LC# | `K` | 備註 |
|---------|-----|-----|------|
| Longest Substring with At Most K Distinct Characters | 340 | K | 一般化的敘述 |
| Fruit Into Baskets | 904 | 2 | `K = 2` 的 LC 340，底下有解 |
| Longest Substring with At Most Two Distinct Characters | 159 | 2 | 把 LC 904 搬到字元上 |
| Longest Substring Without Repeating Characters | 3 | — | 退化情況：每個計數都必須是 1 |

#### Fruit Into Baskets — LC 904

**把題目重講一次**：找出最長的連續子陣列，裡面**最多 2 種**不同的值（2 個籃子，每個裝一種水果）。這就是標準的*「最多 K 種相異元素的最長視窗」*，`K = 2`。

**關鍵想法**：維護視窗的 `{fruit_type: count}` 表。每一步把 `right` 往外擴；只要表裡超過 2 個鍵，就從 `left` 收縮到剩 ≤ 2 為止。答案是過程中看過的最大視窗寬度 `right - left + 1`。視窗永遠不會縮到*小於*目前最佳的合法寬度，所以一趟掃描就是 O(n)。

```python
# python
# LC 904 - Fruit Into Baskets
# IDEA: SLIDING WINDOW + HASHMAP — longest window with at most 2 distinct types
# time = O(n), space = O(1)  (map holds at most 3 keys)
class Solution(object):
    def totalFruit(self, fruits):
        if not fruits:
            return 0

        basket = {}          # fruit_type -> count in current window
        left = 0
        max_fruit = 0

        for right in range(len(fruits)):
            # 1) expand: add the fruit at right
            f = fruits[right]
            basket[f] = basket.get(f, 0) + 1

            # 2) shrink: while > 2 distinct types, drop from the left
            while len(basket) > 2:
                lf = fruits[left]
                basket[lf] -= 1
                if basket[lf] == 0:   # type fully gone -> remove key
                    del basket[lf]
                left += 1

            # 3) record best valid window width
            max_fruit = max(max_fruit, right - left + 1)

        return max_fruit
```

**為什麼 `del` 很重要**：`len(basket)` 代表視窗裡不同水果的種類數。如果你只把計數減 1，卻不刪掉歸零的鍵，`len(basket)` 就會虛胖，`while` 迴圈會把視窗縮得太兇（或根本不會正確結束）。計數一歸零就要把鍵移掉。

**一般化**：把 `> 2` 換成 `> K`，這份模板就一字不改地解掉 **LC 340（Longest Substring with At Most K Distinct Characters）** —— LC 904 只是 `K = 2` 的特例。要把它變成*剛好 K 個*的計數器，見主頁的 [Template 6: Exactly K via At-Most Subtraction](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-)。

| 元件 | 作用 |
|-------|------|
| `basket` 表 | 追蹤視窗裡的種類與各自的計數 |
| `while len(basket) > 2` | 維持不變式 —— 讓視窗保持合法 |
| 計數歸零時 `del` | 讓 `len(basket)` 等於真正的相異數 |
| `right - left + 1` | 當前視窗寬度，取最大值存進 `max_fruit` |

#### Longest Substring with At Most Two Distinct Characters — LC 159

> 視窗裡的相異字元超過 2 個就收縮左邊；用頻率表追蹤。

```java
// LC 159 - Longest Substring with At Most Two Distinct Characters
// IDEA: Sliding window with HashMap — shrink when distinct > 2
// time = O(N), space = O(1)
public int lengthOfLongestSubstringTwoDistinct(String s) {
    Map<Character, Integer> freq = new HashMap<>();
    int l = 0, ans = 0;
    for (int r = 0; r < s.length(); r++) {
        freq.merge(s.charAt(r), 1, Integer::sum);
        while (freq.size() > 2) {
            char lc = s.charAt(l);
            freq.merge(lc, -1, Integer::sum);
            if (freq.get(lc) == 0) freq.remove(lc);
            l++;
        }
        ans = Math.max(ans, r - l + 1);
    }
    return ans;
}
```

## 超出單一範例的 Exactly-K 計數

[Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) 已經給了這個轉換，也解過 LC 992。這一節補的是第一輪可以跳過的部分：字母表受限時會怎樣、什麼時候該改用前綴表，以及為什麼直接做「剛好 K 個」的視窗找不到乾淨的收縮條件。

### Count Vowel Substrings —— 重置的變化 — LC 2062 ⭐⭐⭐⭐

**題目：**數出**只由母音組成**（`a, e, i, o, u`）**且**包含**全部 5 個**相異母音的子字串個數。

**關鍵想法：**`剛好 5 個相異母音 = atMost(5) - atMost(4)`

這跟 LC 992 是同一個 `atMost(k) - atMost(k-1)` 轉換，只是**字串版多了一個變化**：

> **「只能有母音」這條限制。** 子字串裡*不能有子音*。所以 `atMost` 一碰到子音，視窗就**當場報廢** —— 清空頻率表，把 `left` 跳到子音後面（`left = right + 1`）。這樣才能保證我們數到的每個視窗都只含母音。

```text
"EXACTLY 5 distinct vowels"  →  atMost(5) - atMost(4)
       └── only counts vowel-only windows (consonant resets window)
```

```python
# Python - LC 2062 Count Vowel Substrings of a String
# IDEA: atMost(5) - atMost(4), with consonant resetting the window
class Solution(object):
    def countVowelSubstrings(self, word):
        # time = O(n) (atMost called twice), space = O(1) (≤ 5 vowels tracked)
        def countAtMost(max_unique):
            vowels = set("aeiou")
            cnt_map = {}
            l = 0
            ans = 0

            for r in range(len(word)):
                # CRITICAL: a consonant ruins the vowel-only window
                # → clear map and jump left past the consonant
                if word[r] not in vowels:
                    cnt_map.clear()
                    l = r + 1
                    continue

                cnt_map[word[r]] = cnt_map.get(word[r], 0) + 1

                # shrink from left while too many distinct vowels
                while len(cnt_map) > max_unique:
                    cnt_map[word[l]] -= 1
                    if cnt_map[word[l]] == 0:
                        del cnt_map[word[l]]
                    l += 1

                # # of valid vowel-only substrings ending at r = window length
                ans += (r - l + 1)

            return ans

        # EXACTLY 5 distinct vowels = atMost(5) - atMost(4)
        return countAtMost(5) - countAtMost(4)
```

```java
// Java - LC 2062 Count Vowel Substrings of a String
// IDEA: atMost(5) - atMost(4), with consonant resetting the window
class Solution {
    /**
     * time = O(n) (atMost called twice), space = O(1) (≤ 5 vowels tracked)
     */
    public int countVowelSubstrings(String word) {
        // EXACTLY 5 distinct vowels = atMost(5) - atMost(4)
        return countAtMost(word, 5) - countAtMost(word, 4);
    }

    private int countAtMost(String word, int maxUnique) {
        Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));
        Map<Character, Integer> cntMap = new HashMap<>();
        int l = 0, ans = 0;

        for (int r = 0; r < word.length(); r++) {
            char c = word.charAt(r);

            // CRITICAL: a consonant ruins the vowel-only window
            // → clear map and jump left past the consonant
            if (!vowels.contains(c)) {
                cntMap.clear();
                l = r + 1;
                continue;
            }

            cntMap.put(c, cntMap.getOrDefault(c, 0) + 1);

            // shrink from left while too many distinct vowels
            while (cntMap.size() > maxUnique) {
                char leftChar = word.charAt(l);
                cntMap.put(leftChar, cntMap.get(leftChar) - 1);
                if (cntMap.get(leftChar) == 0) {
                    cntMap.remove(leftChar);
                }
                l++;
            }

            // # of valid vowel-only substrings ending at r = window length
            ans += (r - l + 1);
        }

        return ans;
    }
}
```

**為什麼子音重置是它跟 LC 992 唯一的差別：**

| | LC 992（K 種相異整數） | LC 2062（5 個相異母音） |
|---|---|---|
| 允許的元素 | 任何整數 | **只有母音** |
| 不合法的元素 | （沒有 —— 全部都允許） | **子音 → 重置視窗** |
| 轉換 | `atMost(k) - atMost(k-1)` | `atMost(5) - atMost(4)` |
| 視窗計數 | `ans += r - l + 1` | `ans += r - l + 1` |

> **重點：**當一道「數出剛好 K 種相異的子字串」的題目，同時還限制了*哪些字元可以出現*，就保留 `atMost` 相減，只要在遇到禁用字元時加上重置（`map.clear(); l = r + 1`）就好。

### 為什麼 At-Most 相減行得通 ⭐⭐⭐

#### 圖解：一格一格看


```text
Array: [1, 2, 1, 3], K = 2 (exactly 2 distinct)

At Most 2 Distinct:
Index 0 (1): [1] ✓                                 → count = 1
Index 1 (2): [2] ✓, [1,2] ✓                        → count = 2
Index 2 (1): [1] ✓, [2,1] ✓, [1,2,1] ✓             → count = 3
Index 3 (3): [3] ✓, [1,3] ✓, but NOT [2,1,3] ❌    → count = 2
                   (window shrinks to [1,3])

Total At Most 2: 1 + 2 + 3 + 2 = 8

At Most 1 Distinct:
Index 0 (1): [1] ✓                                 → count = 1
Index 1 (2): [2] ✓, but NOT [1,2] ❌               → count = 1
                   (window shrinks to [2])
Index 2 (1): [1] ✓, but NOT [2,1] ❌               → count = 1
                   (window shrinks to [1])
Index 3 (3): [3] ✓, but NOT [1,3] ❌               → count = 1
                   (window shrinks to [3])

Total At Most 1: 1 + 1 + 1 + 1 = 4

Exactly 2 Distinct = 8 - 4 = 4 ✓

The 4 subarrays with exactly 2 distinct:
[1,2], [1,2,1], [2,1], [1,3]
```

#### 模式辨識：什麼時候用這招

**看到這些就用「Exactly K」轉換：**
```text
✅ "exactly K distinct/different"
✅ "exactly K times"
✅ "exactly K occurrences"
✅ "subarrays with exactly K ..."
✅ COUNTING problems (not max/min length)
```

**直接用滑動視窗就行的情況：**
```text
✅ "at most K"
✅ "maximum length with ≤ K"
✅ "minimum length with ≥ K"
✅ "longest substring with at most K"
```

#### 常見錯誤

**1. 忘了 k=0 這個邊界情況：**
```python
# Wrong: Doesn't handle k=0
def exactly_k(nums, k):
    return at_most_k(nums, k) - at_most_k(nums, k - 1)
    # at_most_k(nums, -1) may fail!

# Right: Handle k=0 explicitly
def exactly_k(nums, k):
    if k == 0:
        return 0
    return at_most_k(nums, k) - at_most_k(nums, k - 1)
```
**2. 求最大／最小長度時用錯方法：**
```python
# Wrong: Using "exactly K" transformation for max length
def longest_k_distinct(s, k):
    # This gives COUNT, not LENGTH!
    return at_most_k(s, k) - at_most_k(s, k - 1)  # ❌

# Right: Direct at_most_k for max length
def longest_k_distinct(s, k):
    # Track max window size during at_most_k
    return at_most_k_max_length(s, k)  # ✓
```

**3. 把「個數」跟「長度」搞混：**
```python
# For COUNTING subarrays: use right - left + 1
count += right - left + 1

# For MAX LENGTH: track max window size
max_length = max(max_length, right - left + 1)
```

#### 面試時可以講的重點

**1. 怎麼認出來：**
```text
Interviewer: "Count subarrays with exactly K ..."
→ Think: "Exactly K = At Most K - At Most (K-1)"

Interviewer: "Find longest substring with at most K ..."
→ Think: "Direct sliding window, no subtraction needed"
```

**2. 複雜度分析：**
```text
Time: O(n) - each element added once, removed at most once in each pass
      Total: 2 passes × O(n) = O(n)

Space: O(k) - HashMap stores at most K distinct elements
```

**4. 講法：**
- 「直接做『剛好 K 個』很難，因為視窗的合法性不是單調變化的」
- 「『最多 K 個』是單調的 —— 一旦合法，在收縮之前都會一直合法」
- 「減掉『最多 K-1 個』就把所有多算的部分扣掉了」
- 「這個轉換把一題 hard 變成兩題 medium」

> at-most 模板本身這裡不重複 —— 直接從
> [Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) 背起來。

#### 為什麼直接做「剛好 K 個」很難

**直接做的問題：**
```python
# Naive attempt (WRONG!)
def exactly_k_direct(nums, k):
    left = 0
    count = 0
    window = {}

    for right in range(len(nums)):
        window[nums[right]] = window.get(nums[right], 0) + 1

        # When to shrink? This is tricky!
        # If len(window) > k: shrink (too many distinct)
        # If len(window) < k: can't count yet (too few distinct)
        # If len(window) == k: count, but should we shrink?

        # If we shrink when == k, we might miss valid subarrays
        # If we don't shrink, we might count invalid subarrays

        # There's no clean condition! ❌

    return count
```

**為什麼「最多 K 個」可行：**
```python
# Window validity is monotonic:
# - If window is valid (≤ K), all sub-windows are valid
# - If window becomes invalid (> K), shrink until valid
# - Clear shrinking condition: while len(window) > k

# This monotonic property makes sliding window perfect!
```

**這個轉換的數學證明：**
```text
Let S(k) = set of all subarrays with at most k distinct elements

S(2) = {[1], [1,2], [1,2,1], [2], [2,1], [1], [1,3], [3], ...}
S(1) = {[1], [2], [1], [3], ...}  (only single-element subarrays)

S(2) \ S(1) = subarrays in S(2) but not in S(1)
            = subarrays with MORE than 1 but AT MOST 2 distinct
            = subarrays with EXACTLY 2 distinct ✓

Generalized: S(k) \ S(k-1) = subarrays with exactly k distinct
```

### 前綴和 + HashMap vs 滑動視窗 —— 該用哪個？ ⭐⭐⭐⭐

#### 核心想法

**滑動視窗**
- 維護一個視窗 `[left, right]`，依一個**單調**的條件收縮或擴張。
- 只有在合法性單調時才可行：視窗一旦不合法，從左邊收縮一定能把它救回來。
- 天生適合處理**「最多 K 個」**與**「最長／最短」**這類限制。

**前綴和 + HashMap**
- 追蹤一個累加的計數（例如目前為止看過幾個奇數）。
- 用 HashMap 記錄每個前綴計數出現過幾次。
- 在每個索引查 `prefixCount - k`，就能知道有幾個以此結尾的子陣列**剛好有 k 個**目標元素。
- 適合需要**精確**命中目標的計數問題，尤其是「剛好 k 個」破壞了滑動視窗單調性的時候。

#### 為什麼「剛好 K 個」會弄壞純滑動視窗

```text
nums = [2,2,1,2,1], k = 2

At r = 4 (last element), valid subarrays ending here:
  [1,2,1]        → starts at index 2
  [2,1,2,1]      → starts at index 1
  [2,2,1,2,1]    → starts at index 0

→ 3 valid left boundaries — but pure sliding window finds only 1!
```

滑動視窗只能追蹤**一個**左邊界。但「剛好 k 個」在每個右端點都可能有**多個**合法左邊界 —— 前綴和 + HashMap 每一步用 O(1) 就把它們全數進去。

#### 對照表

| 面向 | 滑動視窗 | 前綴和 + HashMap |
|---|---|---|
| **最適合** | 最多 K 個／最長／最短 | 剛好 K 個／數出子陣列個數 |
| **條件型態** | 單調（≤ k、≥ k） | 非單調（== k） |
| **多個左邊界** | ❌ 只能處理一個 | ✅ 全部都算得到 |
| **空間** | O(1) | HashMap 需要 O(n) |
| **時間** | O(n) | O(n) |
| **程式複雜度** | 單純的雙指標 | 需要追蹤前綴 + 基底情況 `map.put(0, 1)` |
| **關鍵招式** | `while (invalid) { shrink left }` | `res += map.get(prefixCount - k)` |

#### 決策指引

```text
Is the condition monotonic? (e.g., sum ≤ k, distinct ≤ k)
  ├── YES → Pure Sliding Window
  └── NO (exactly k, == k) →
        ├── atMost(k) - atMost(k-1)  [two sliding window passes]
        ├── Prefix Sum + HashMap      [one pass, O(n) space]
        └── Prefix Trick in Sliding Window [one pass, O(1) space — see "Prefix Trick Inside the Window" below]
```

#### 兩種寫法並排比較

**滑動視窗 —— 「最多 K 個奇數」：**
```java
private int atMost(int[] nums, int k) {
    int l = 0, res = 0, oddCount = 0;
    for (int r = 0; r < nums.length; r++) {
        if (nums[r] % 2 == 1) oddCount++;
        while (oddCount > k) {
            if (nums[l] % 2 == 1) oddCount--;
            l++;
        }
        res += (r - l + 1);   // all subarrays ending at r with ≤ k odds
    }
    return res;
}
```

**前綴和 + HashMap —— 「剛好 K 個奇數」：**
```java
public int numberOfSubarrays(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);  // base case: empty prefix has 0 odd numbers
    int oddCount = 0, res = 0;
    for (int val : nums) {
        if (val % 2 == 1) oddCount++;
        // how many previous prefixes had (oddCount - k) odds?
        // → those prefixes + current position = subarray with exactly k odds
        res += map.getOrDefault(oddCount - k, 0);
        map.put(oddCount, map.getOrDefault(oddCount, 0) + 1);
    }
    return res;
}
```

#### 相似的 LeetCode 題目

| 題目 | LC# | 難度 | 做法 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| Count Number of Nice Subarrays | 1248 | Medium | 兩種都行 | 把奇數當 1、偶數當 0；用前綴和或 atMost 技巧 |
| Binary Subarrays With Sum | 930 | Medium | 兩種都行 | 0/1 陣列；前綴和最直接 |
| Subarray Sum Equals K | 560 | Medium | **只能用前綴和** | 有負數 → 滑動視窗會壞掉 |
| Subarrays with K Different Integers | 992 | Hard | 滑動視窗（atMost） | 相異數；atMost(k)-atMost(k-1) |
| Number of Subarrays with Sum = k | 974 | Medium | **只能用前綴和** | 整除性的變形；需要精確命中 |
| Contiguous Array | 525 | Medium | **只能用前綴和** | 0 和 1 一樣多；需要精確平衡 |

> **經驗法則**：如果陣列可能有**負數**，或條件是個沒辦法改寫成「最多」的硬等式，就用**前綴和 + HashMap**。如果值都非負、條件是個範圍（≤ k），就用**滑動視窗**。

### 什麼時候純滑動視窗就夠、什麼時候要額外招式 ⭐⭐⭐

#### 核心問題：合法性條件單調嗎？

**純滑動視窗可行**的前提是合法性條件**單調**：
- 視窗一旦不合法，繼續往右擴就會一直不合法
- 一個 `while (invalid) { shrink left }` 就能乾淨地把合法性救回來

**需要額外招式**的情況是條件**不單調**（尤其是「剛好 k 個」）：
- 對固定的 `r`，可能有**多個合法的左邊界**
- 單純縮到合法為止只會給你其中一個答案，其他的就漏掉了

#### 決策表

| 條件型態 | 例子 | 純滑動視窗可行？ | 補救方式 |
|----------------|---------|---------------------|-----|
| `sum ≤ k` | 乘積 < k | ✅ 可以 | — |
| `distinct ≤ k` | 最多 K 種相異 | ✅ 可以 | — |
| `sum ≥ k`（最短長度） | 最小子陣列和 | ✅ 可以 | — |
| `exactly k` 個奇數／相異值 | LC 1248, LC 992 | ❌ 不行 | `atMost(k) - atMost(k-1)` 或前綴小技巧 |
| `exactly k`（含偶數間隔） | LC 1248 | ❌ 不行 | 前綴小技巧（數左邊的偶數間隔） |

**兩種補救方式**，本頁或主頁都有講：

1. **`atMost(k) - atMost(k-1)`** —— 兩趟乾淨的掃描；見 [Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-)。
2. **視窗內的前綴小技巧** —— 一趟、O(1) 空間；見下一節。


### 視窗內的前綴小技巧 — LC 1248 ⭐⭐⭐


**什麼時候用：**要數出**剛好 k 個**某種元素的子陣列，而且你想要一趟 O(n) 解掉，不想呼叫 `atMost` 兩次。

**核心想法：**
```text
When oddCount reaches k (window has exactly k odds):
  - Count how many even numbers are at the LEFT edge of the window
    before hitting the (k-th-from-left) odd number
  - Each of these even numbers gives one more valid left boundary
  - Store this count as `prefix`

After the window shrinks past the leftmost odd:
  - oddCount drops below k, so the while loop exits
  - But `prefix` (the "even gap") is PRESERVED
  - For every future r that keeps oddCount == k,
    those same left boundaries are still valid → add `prefix` again
```

**為什麼碰到新的奇數時 `prefix` 要歸零：**
- `r` 出現新的奇數，會改變「從左邊數來第 k 個奇數」是哪一個
- 新的最左奇數前面有幾個偶數，必須重新算
- 所以把 `prefix = 0` 歸零，讓 while 迴圈重新把它累出來

#### 模板

```java
// Prefix Trick + Sliding Window
// time = O(N), space = O(1)
public int exactlyK(int[] nums, int k) {
    int l = 0, res = 0, oddCount = 0, prefix = 0;

    for (int r = 0; r < nums.length; r++) {
        if (nums[r] % 2 == 1) {
            oddCount++;
            prefix = 0;  // reset: new odd changes left boundary gap
        }

        // Shrink left while window has exactly k odds,
        // counting even elements we skip at the left edge
        while (oddCount == k) {
            prefix++;                        // one more valid left boundary
            if (nums[l] % 2 == 1) oddCount--;
            l++;
        }

        // prefix = # of valid left boundaries for subarrays ending at r
        res += prefix;
    }

    return res;
}
```

#### 走查：`nums = [2,2,1,2,1], k = 2`

```text
r=0 (2): oddCount=0, prefix=0  → res=0
r=1 (2): oddCount=0, prefix=0  → res=0
r=2 (1): oddCount=1, prefix=0  → res=0   (new odd, prefix reset)
r=3 (2): oddCount=1, prefix=0  → res=0
r=4 (1): oddCount=2, prefix=0  → new odd, prefix reset to 0
  while oddCount==2:
    prefix=1, nums[0]=2 (even), l=1       → oddCount still 2
    prefix=2, nums[1]=2 (even), l=2       → oddCount still 2
    prefix=3, nums[2]=1 (odd),  l=3, oddCount=1 → exit while
  res += 3 → res=3
```

答案：3 ✅ —— 三個子陣列 `[1,2,1]`、`[2,1,2,1]`、`[2,2,1,2,1]`

#### 比較：前綴小技巧 vs atMost 相減

| | 前綴小技巧 | atMost(k) - atMost(k-1) |
|---|---|---|
| **掃描趟數** | 1 | 2 |
| **空間** | O(1) | O(1) |
| **複雜度** | O(n) | O(n) |
| **可讀性** | 有點繞（歸零那段） | 比較乾淨、比較直覺 |
| **什麼時候用** | 想要一趟解決 | 想要寫得清楚 |

#### 相關題目

| 題目 | LC# | 難度 | 備註 |
|---------|-----|------------|------|
| Count Number of Nice Subarrays | 1248 | Medium | 剛好 k 個奇數 |
| Binary Subarrays With Sum | 930 | Medium | 和剛好是 k（0/1 陣列） |
| Subarrays with K Different Integers | 992 | Hard | 剛好 k 種相異值 |
| Number of Substrings Containing All Three Characters | 1358 | Medium | 類似的間隔計數 |

## 視窗變換

### 最少操作數 → 最大子陣列長度 — LC 1658 ⭐⭐⭐⭐

#### 核心想法

當題目問的是**從陣列兩端移除元素、直到達成某個目標的最少操作次數**，就把視角翻過來：

```text
Instead of minimizing elements removed from edges,
MAXIMIZE the elements kept in the middle.

Min Edge Removals = Total Length − Max Middle Subarray Length
```

**為什麼這樣行得通：**

```text
removed_sum + remaining_sum = total_sum

If removed_sum must equal x:
  remaining_sum = total_sum - x   ← this becomes the sliding window target

Total Elements − Max Middle Subarray (sum = target) = Min Operations
```

```text
Visual layout:

MIN EDGE PIECES (Ops)              MAX MIDDLE SUBARRAY
 | nums[0] | nums[1] |      | ... | ... | ... |
 \_______________________/  \_______________________/
     Removed from Edges           Left in the Center
          (Sum = x)               (Sum = total_sum - x)
```

#### 模式

```text
Step 1: Compute total = sum(nums)
Step 2: Compute target = total - x
        • If target == 0 → must remove ALL elements → return nums.length
        • If target < 0  → impossible             → return -1
Step 3: Sliding window to find LONGEST subarray with sum == target
Step 4: return nums.length - maxLen   (or -1 if not found)
```

#### 模板（Java）

```java
public int minOperations(int[] nums, int x) {
    int total = 0;
    for (int num : nums) total += num;

    int target = total - x;
    if (target == 0) return nums.length;
    if (target < 0)  return -1;

    int n = nums.length, l = 0, sum = 0, maxLen = -1;

    for (int r = 0; r < n; r++) {
        sum += nums[r];

        // shrink from left while sum exceeds target
        while (l <= r && sum > target) {
            sum -= nums[l++];
        }

        // valid window found — track longest
        if (sum == target) {
            maxLen = Math.max(maxLen, r - l + 1);
        }
    }

    return maxLen == -1 ? -1 : n - maxLen;
}
```

> **為什麼這裡純滑動視窗就夠：**`# Note: works for any nums[i] >= 0 (non-negative)` —— 往右擴時視窗和是**單調不減**的。從左邊收縮一定會讓和變小 —— 合法性條件是單調的 → 乾淨的雙指標解。

#### 手動跑一遍 —— `nums = [1,1,4,2,3], x = 5`

```text
total = 11,  target = 11 - 5 = 6

r  nums[r]  window     sum   action          maxLen
0    1      [1]          1   sum < target      -1
1    1      [1,1]        2   sum < target      -1
2    4      [1,1,4]      6   sum == target      3   ← window [0..2]
3    2      [1,1,4,2]    8   shrink left
           [1,4,2]       7   shrink left
           [4,2]         6   sum == target      3   ← window [2..3]
4    3      [4,2,3]      9   shrink left
           [2,3]         5   sum < target       3

maxLen = 3  →  answer = 5 - 3 = 2 ✓
```

#### 什麼時候套用這個轉換

| 題目裡的訊號 | 轉換 |
|-----------------------|----------------|
| 「從左邊或右邊移除」 | 最少移除數 = n − 最大中段子陣列 |
| 「從兩端操作的最少次數」 | 找和等於 total − x 的最大子陣列 |
| 「只能從邊緣取元素」 | 補集就是一段連續的中間子陣列 |

#### 相似的 LeetCode 題目

| 題目 | LC# | 難度 | 關鍵洞見 |
|---------|-----|------------|-------------|
| **Minimum Operations to Reduce X to Zero** | **1658** | **Medium** | 核心範例 —— 和等於 total − x 的最大子陣列 |
| Minimum Size Subarray Sum | 209 | Medium | 和 ≥ target 的最短子陣列（直接做，不用翻轉） |
| Maximum Erasure Value | 1695 | Medium | 元素全相異的最大子陣列 |
| Subarray Sum Equals K | 560 | Medium | 精確的子陣列和 —— 用前綴和 + HashMap（有負數） |
| Longest Subarray of 1's After Deleting One Element | 1493 | Medium | 最大中段子陣列，移除額度固定 |
| Count Subarrays Where Max Element Appears at Least K Times | 2962 | Medium | 數出合法的中間視窗，最少操作數的框架 |

### 補集視窗（「從兩端取」） — LC 1423 ⭐⭐⭐⭐

**什麼時候用：**你必須從陣列的**兩端**取 `k` 個元素（左右怎麼分都可以）。被選中的元素不連續 —— 但你*沒選的*那些一定連續：剛好就是一個大小 `n - k` 的連續視窗。把取到的最大化 ⇔ **把補集視窗最小化**。

#### 核心想法

```text
[ take l from front ][ ....... leftover ....... ][ take r from back ],  l + r = k

leftover is ALWAYS a contiguous block of size n - k.
  answer = total - min(sum of any window of size n - k)

This flips a "choose from both ends" problem into a plain fixed-size window scan.
Edge case: k >= n → take everything → return total (window size would be 0).
```

#### 模板（Java）

```java
// LC 1423 - Maximum Points You Can Obtain from Cards
// IDEA: complement trick — maximize ends == total - min fixed window of size n-k
// time = O(n), space = O(1)
public int maxScore(int[] cardPoints, int k) {
    int n = cardPoints.length, total = 0;
    for (int c : cardPoints) total += c;
    if (k >= n) return total;                     // take every card

    int win = n - k, cur = 0;
    for (int i = 0; i < win; i++) cur += cardPoints[i];
    int minWindow = cur;

    for (int i = win; i < n; i++) {               // slide the leftover window
        cur += cardPoints[i] - cardPoints[i - win];
        minWindow = Math.min(minWindow, cur);
    }
    return total - minWindow;
}
```

```python
# python
# LC 1423 - Maximum Points You Can Obtain from Cards
# IDEA: complement trick — maximize ends == total - min fixed window of size n-k
# time = O(n), space = O(1)
def maxScore(cardPoints, k):
    n = len(cardPoints)
    total = sum(cardPoints)
    if k >= n:
        return total                              # take every card

    win = n - k
    cur = sum(cardPoints[:win])
    min_window = cur

    for i in range(win, n):                       # slide the leftover window
        cur += cardPoints[i] - cardPoints[i - win]
        min_window = min(min_window, cur)
    return total - min_window
```

#### 手動跑一遍 —— `cardPoints = [1,2,3,4,5,6,1], k = 3`

```text
n = 7, total = 22, leftover window size = 7 - 3 = 4

window            sum
[1,2,3,4]         10   ← min so far
  [2,3,4,5]       14
    [3,4,5,6]     18
      [4,5,6,1]   16
min = 10 at [1,2,3,4] (indices 0..3)  →  the cards taken are indices 4,5,6 = 5+6+1 = 12
answer = 22 - 10 = 12   (0 from the front, 3 from the back) ✓
```

#### 什麼時候套用補集技巧

```text
✅ "Pick k items from the front and/or back"        → min/max window of size n-k
✅ "Remove a contiguous block to optimize the rest" → same idea, inverted
✅ "Choose a prefix + a suffix under a constraint"  → the gap between them is one window
❌ Picks may come from the middle → complement is no longer contiguous, trick fails
```

### 多個互不重疊的固定視窗 — LC 689 ⭐⭐⭐

**什麼時候用：**要選**好幾個互不重疊的固定大小視窗**，把總和最大化。先固定*中間*那個視窗，這時最佳的左視窗與最佳的右視窗彼此獨立 —— 用前綴／後綴的 argmax 掃描預先算好。這是把兩視窗的情況（LC 1031）推廣到三個。

#### 核心想法

```text
Step 1: w[i] = sum of the window starting at i  (rolling sum, i in [0, n-k])
Step 2: left[i]  = index of the BEST window start in [0, i]        (prefix argmax, scan →)
        right[i] = index of the BEST window start in [i, n-k]      (suffix argmax, scan ←)
Step 3: for every middle start `mid` in [k, m-1-k]:
            total = w[left[mid-k]] + w[mid] + w[right[mid+k]]
        keep the max.

Lexicographically smallest indices (LC 689 requires it):
  - prefix scan uses STRICT `>`  → keeps the earliest tie
  - suffix scan uses `>=`        → also keeps the earliest tie (scanning right-to-left)
  - middle loop uses strict `>`  → keeps the earliest mid
```

#### 模板（Java）

```java
// LC 689 - Maximum Sum of 3 Non-Overlapping Subarrays
// IDEA: rolling window sums + prefix/suffix argmax, then fix the middle window
// time = O(n), space = O(n)
public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
    int n = nums.length;
    int[] w = new int[n - k + 1];                     // w[i] = sum of window starting at i
    int cur = 0;
    for (int i = 0; i < n; i++) {
        cur += nums[i];
        if (i >= k) cur -= nums[i - k];
        if (i >= k - 1) w[i - k + 1] = cur;
    }

    int m = w.length;
    int[] left = new int[m], right = new int[m];
    int best = 0;
    for (int i = 0; i < m; i++) {                     // strict > → earliest tie wins
        if (w[i] > w[best]) best = i;
        left[i] = best;
    }
    best = m - 1;
    for (int i = m - 1; i >= 0; i--) {                // >= while scanning left → earliest tie
        if (w[i] >= w[best]) best = i;
        right[i] = best;
    }

    int[] ans = null;
    int bestSum = -1;
    for (int mid = k; mid + k < m; mid++) {           // fix the middle window
        int l = left[mid - k], r = right[mid + k];
        int sum = w[l] + w[mid] + w[r];
        if (sum > bestSum) {
            bestSum = sum;
            ans = new int[]{l, mid, r};
        }
    }
    return ans;
}
```

```python
# python
# LC 689 - Maximum Sum of 3 Non-Overlapping Subarrays
# IDEA: rolling window sums + prefix/suffix argmax, then fix the middle window
# time = O(n), space = O(n)
def maxSumOfThreeSubarrays(nums, k):
    n = len(nums)
    w = [0] * (n - k + 1)                      # w[i] = sum of window starting at i
    cur = sum(nums[:k])
    w[0] = cur
    for i in range(k, n):
        cur += nums[i] - nums[i - k]
        w[i - k + 1] = cur

    m = len(w)
    left, right = [0] * m, [0] * m
    best = 0
    for i in range(m):                         # strict > → earliest tie wins
        if w[i] > w[best]:
            best = i
        left[i] = best
    best = m - 1
    for i in range(m - 1, -1, -1):             # >= while scanning left → earliest tie
        if w[i] >= w[best]:
            best = i
        right[i] = best

    ans, best_sum = None, -1
    for mid in range(k, m - k):                # fix the middle window
        l, r = left[mid - k], right[mid + k]
        s = w[l] + w[mid] + w[r]
        if s > best_sum:
            best_sum, ans = s, [l, mid, r]
    return ans
```

#### 手動跑一遍 —— `nums = [1,2,1,2,6,7,5,1], k = 2`

```text
w  = [3, 3, 3, 8, 13, 12, 6]         (sums of every length-2 window)
left  = [0, 0, 0, 3, 4, 4, 4]        (prefix argmax, earliest tie)
right = [4, 4, 4, 4, 4, 5, 6]        (suffix argmax, earliest tie)

mid = 2 → l=left[0]=0, r=right[4]=4 → 3 + 3 + 13 = 19
mid = 3 → l=left[1]=0, r=right[5]=5 → 3 + 8 + 12 = 23  ← best
mid = 4 → l=left[2]=0, r=right[6]=6 → 3 + 13 + 6 = 22
answer = [0, 3, 5] ✓
```

#### 相似題

| 題目 | LC# | 難度 | 主要差異 |
|---------|-----|------------|----------------|
| **Maximum Sum of 3 Non-Overlapping Subarrays** | **689** | **Hard** | 核心範例 —— 3 個視窗，且索引要字典序最小 |
| Maximum Sum of Two Non-Overlapping Subarrays | 1031 | Medium | 只有 2 個視窗（而且兩個大小不同）—— 緊接在下面解；不需要中間那層迴圈 |

#### 一般化

```text
For j windows (j > 3), drop the fixed-middle trick and go DP:
  dp[j][i] = best total using j windows within the prefix ending at i
           = max(dp[j][i-1],  dp[j-1][i-k] + w[i-k+1])
  → O(n * j) time. The 3-window case just hardcodes j = 3 with prefix/suffix argmax.
```


#### LC 1031: Maximum Sum of Two Non-Overlapping Subarrays —— 前綴和 + 滑動視窗

> 兩種先後順序都試一遍（L 在 M 前、M 在 L 前）。每一種順序都用 `i` 當 M 視窗的**開區間右端**往前掃；同時維護 `maxL` = 目前為止在 M 左側看過的最佳 L 視窗。

**索引配置（i = M 視窗的開區間右端）：**
```text
Indices:  0 . . . [i-M-L] . . . [i-M] . . . [i] . . . n
                   |--- L window ---| |--- M window ---|

L window sum: prefix[i-M]   - prefix[i-M-L]
M window sum: prefix[i]     - prefix[i-M]
```

**為什麼從 `i = L + M` 開始？** 那是兩個視窗頭尾相接所需的最短前綴長度。`i` 一路跑到 `<= n`（含），因為 `prefix` 的大小是 `n+1`。

```java
// LC 1031 - Maximum Sum of Two Non-Overlapping Subarrays
// IDEA: Prefix Sum + Sliding Window — try both L-before-M and M-before-L
// time = O(N), space = O(N)
public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {
    return Math.max(
        helper(nums, firstLen, secondLen),   // firstLen before secondLen
        helper(nums, secondLen, firstLen));  // secondLen before firstLen
}

// L comes before M; i is the exclusive end of the M window
private int helper(int[] nums, int L, int M) {
    int n = nums.length;
    int[] prefix = new int[n + 1];
    for (int i = 0; i < n; i++) {
        prefix[i + 1] = prefix[i] + nums[i];
    }

    int maxL = 0; // best L-window sum seen so far (left of current M)
    int ans   = 0;

    /**
     * i = ending position (exclusive) of M window
     *
     * 1. i starts from L + M  (minimum length to fit both windows)
     * 2. i ends at <= n       (prefix has size n+1)
     *
     * Index layout:
     *   0 . . . [i-M-L] . . . [i-M] . . . [i] . . . n
     *            |--- L window ---| |--- M window ---|
     *
     *   L window: prefix[i-M]   - prefix[i-M-L]   (range [i-M-L, i-M))
     *   M window: prefix[i]     - prefix[i-M]      (range [i-M,   i))
     */
    for (int i = L + M; i <= n; i++) {
        // L window: [i-M-L, i-M)
        int lSum = prefix[i - M] - prefix[i - M - L];
        maxL = Math.max(maxL, lSum);          // keep best L seen so far

        // M window: [i-M, i)
        int mSum = prefix[i] - prefix[i - M];

        ans = Math.max(ans, maxL + mSum);     // best non-overlapping pair
    }

    return ans;
}
```

**模式總結：**
- 前綴和只建一次：O(N)
- 每種順序掃一趟：一邊推進右視窗，一邊維護 `maxL`（最佳左視窗）
- 呼叫兩次（把 L/M 對調）涵蓋兩種順序 → 最終答案取兩者的 `Math.max`

## 鍵不是字元的視窗

### 索引固定的視窗 + 分桶（值鄰近查詢） — LC 220 ⭐⭐⭐⭐

**什麼時候用：**視窗是用**索引距離**界定的（`|i - j| <= indexDiff`），但合法性判斷看的是**值**（`|nums[i] - nums[j]| <= valueDiff`）。普通的頻率表回答不了「視窗裡有沒有*相近的值*？」—— 你需要一個有序結構，或是 O(1) 的分桶技巧。

#### 核心想法

```text
Window = the last `indexDiff` elements (a fixed-capacity set, evicted by index).
Question per new element x: does the window hold a value within valueDiff of x?

Bucket trick:
  bucket(x) = floor(x / (valueDiff + 1))     ← width = valueDiff + 1
  - Two values in the SAME bucket always differ by <= valueDiff  → answer immediately.
  - Values that differ by <= valueDiff but sit in different buckets
    must be in ADJACENT buckets → check bucket-1 and bucket+1 only.
  - Any bucket holds at most one live value (a second one would have returned true).

Why width = valueDiff + 1, not valueDiff?
  With width w, same-bucket values differ by <= w-1. Setting w = valueDiff + 1
  makes "same bucket ⇒ valid" exactly true.
```

#### 模板（Java）

```java
// LC 220 - Contains Duplicate III
// IDEA: fixed-index sliding window + bucketing by value (width = valueDiff + 1)
// time = O(n), space = O(min(n, indexDiff))
public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
    if (indexDiff <= 0 || valueDiff < 0) return false;
    long w = (long) valueDiff + 1;                 // bucket width
    Map<Long, Long> buckets = new HashMap<>();     // bucketId -> the single value living there

    for (int i = 0; i < nums.length; i++) {
        long x = nums[i];
        long b = Math.floorDiv(x, w);              // floorDiv (NOT /) keeps negatives correct

        if (buckets.containsKey(b)) return true;                                  // same bucket
        if (buckets.containsKey(b - 1) && x - buckets.get(b - 1) <= valueDiff) return true;
        if (buckets.containsKey(b + 1) && buckets.get(b + 1) - x <= valueDiff) return true;

        buckets.put(b, x);
        // evict the element that just fell out of the index window
        if (i >= indexDiff) buckets.remove(Math.floorDiv((long) nums[i - indexDiff], w));
    }
    return false;
}
```

```python
# python
# LC 220 - Contains Duplicate III
# IDEA: fixed-index sliding window + bucketing by value (width = valueDiff + 1)
# time = O(n), space = O(min(n, indexDiff))
def containsNearbyAlmostDuplicate(nums, indexDiff, valueDiff):
    if indexDiff <= 0 or valueDiff < 0:
        return False
    w = valueDiff + 1                      # bucket width
    buckets = {}                           # bucketId -> the single value living there

    for i, x in enumerate(nums):
        b = x // w                         # python floor division already handles negatives
        if b in buckets:
            return True
        if b - 1 in buckets and abs(x - buckets[b - 1]) <= valueDiff:
            return True
        if b + 1 in buckets and abs(x - buckets[b + 1]) <= valueDiff:
            return True

        buckets[b] = x
        if i >= indexDiff:                 # evict element leaving the index window
            del buckets[nums[i - indexDiff] // w]
    return False
```

#### 另一種做法：有序集合視窗 —— `O(n log k)`

```text
Keep a TreeSet (Java) / SortedList (Python) of the last indexDiff values.
For each x: floor/ceiling query → is there a neighbour within valueDiff?
  TreeSet<Long> set; Long lo = set.floor(x); Long hi = set.ceiling(x);
Slower (log k) but far easier to get right under interview pressure —
state the bucket version as the O(n) follow-up.
```

#### 陷阱

```text
❌ b = x / w in Java → truncates toward zero, so -3/5 == 0 == 3/5 (wrong bucket for negatives).
   ✅ Math.floorDiv(x, w)
❌ Using width = valueDiff → same-bucket pairs may differ by valueDiff+... ; off-by-one bugs.
❌ Forgetting the eviction step → window becomes "whole prefix", indexDiff ignored.
❌ int overflow on `x - neighbour` when values span ±2^31 → widen to long.
```

#### 相似題

| 題目 | LC# | 難度 | 主要差異 |
|---------|-----|------------|----------------|
| **Contains Duplicate III** | **220** | **Hard** | 核心範例 —— 索引視窗 + 值的鄰近性 |
| Contains Duplicate II | 219 | Easy | 一樣的索引視窗，但要求完全相等 → 普通 HashSet 就夠 |


### 以單字為單位的滑動視窗（固定長度區塊） — LC 30 ⭐⭐⭐⭐

**什麼時候用：**視窗滑動的單位是**固定長度的區塊**，而不是單一字元 —— 等長單字的串接、k-mer、區塊比對。訣竅是跑 `wordLen` 個各自獨立的滑動視窗，每個起始偏移一個，這樣所有可能的對齊都涵蓋到了，而每個字元在每個偏移下仍然只被看 O(1) 次。

#### 核心想法

```text
words all have length L, there are m of them → answer substrings have length L*m.
Any valid start index s satisfies s % L == r for some r in [0, L).
Two starts with the same remainder share chunk boundaries → they belong to
ONE sliding window pass. So run L passes, offset = 0..L-1, each stepping by L.

Inside a pass, this is just the classic "window with a frequency map + match counter":
  - chunk not in need           → hard reset (clear map, jump left past it)
  - chunk over-counted          → shrink from the left until it fits
  - count == m                  → record start, then shrink one chunk to keep scanning

Total work: L passes * (n / L) chunks = O(n) chunk steps, each O(L) to hash a substring.
```

#### 模板（Java）

```java
// LC 30 - Substring with Concatenation of All Words
// IDEA: wordLen independent sliding windows (one per offset) + freq map & match counter
// time = O(wordLen * n), space = O(m * wordLen)
public List<Integer> findSubstring(String s, String[] words) {
    List<Integer> res = new ArrayList<>();
    if (s == null || s.isEmpty() || words.length == 0) return res;
    int wl = words[0].length(), m = words.length;
    if (s.length() < wl * m) return res;

    Map<String, Integer> need = new HashMap<>();
    for (String w : words) need.merge(w, 1, Integer::sum);

    for (int offset = 0; offset < wl; offset++) {           // one window per alignment
        int left = offset, count = 0;
        Map<String, Integer> window = new HashMap<>();

        for (int right = offset; right + wl <= s.length(); right += wl) {
            String word = s.substring(right, right + wl);

            if (!need.containsKey(word)) {                  // unusable chunk → hard reset
                window.clear();
                count = 0;
                left = right + wl;
                continue;
            }

            window.merge(word, 1, Integer::sum);
            count++;
            while (window.get(word) > need.get(word)) {     // too many copies → shrink
                window.merge(s.substring(left, left + wl), -1, Integer::sum);
                left += wl;
                count--;
            }
            if (count == m) {                               // full match at `left`
                res.add(left);
                window.merge(s.substring(left, left + wl), -1, Integer::sum);
                left += wl;
                count--;
            }
        }
    }
    return res;
}
```

```python
# python
# LC 30 - Substring with Concatenation of All Words
# IDEA: wordLen independent sliding windows (one per offset) + freq map & match counter
# time = O(wordLen * n), space = O(m * wordLen)
from collections import Counter, defaultdict

def findSubstring(s, words):
    if not s or not words:
        return []
    wl, m = len(words[0]), len(words)
    need = Counter(words)
    res = []

    for offset in range(wl):                       # one window per alignment
        left, count = offset, 0
        window = defaultdict(int)

        for right in range(offset, len(s) - wl + 1, wl):
            word = s[right:right + wl]

            if word not in need:                   # unusable chunk → hard reset
                window.clear()
                count, left = 0, right + wl
                continue

            window[word] += 1
            count += 1
            while window[word] > need[word]:       # too many copies → shrink
                window[s[left:left + wl]] -= 1
                left += wl
                count -= 1
            if count == m:                         # full match at `left`
                res.append(left)
                window[s[left:left + wl]] -= 1
                left += wl
                count -= 1
    return res
```

#### 手動跑一遍 —— `s = "barfoothefoobarman", words = ["foo","bar"]`（wl=3, m=2）

```text
offset = 0 → chunks: bar foo the foo bar man
  right=0  "bar" ✓  count=1
  right=3  "foo" ✓  count=2 == m → record left=0, drop "bar", left=3, count=1
  right=6  "the" ✗  reset, left=9
  right=9  "foo" ✓  count=1
  right=12 "bar" ✓  count=2 == m → record left=9 ✓
offset = 1 → chunks: arf oot hef oob arm  → all ✗, nothing
offset = 2 → chunks: rfo oth efo oba rma  → all ✗, nothing
result = [0, 9]
```

#### 變形 —— LC 187 Repeated DNA Sequences（固定長度視窗 + rolling hash）

*變化點：視窗長度固定是 10，而且你只需要知道「之前出現過嗎？」，所以把頻率表換成 set —— 再把每個鹼基編成 2 個位元，視窗的識別值就能 O(1) 更新，不必每次重新雜湊一段 10 個字元的子字串。*

```java
// LC 187 - Repeated DNA Sequences
// IDEA: fixed-size window of 10 + 2-bit rolling encode (A=0,C=1,G=2,T=3) + HashSet
// time = O(n), space = O(n)
public List<String> findRepeatedDnaSequences(String s) {
    int L = 10;
    List<String> res = new ArrayList<>();
    if (s.length() < L) return res;

    int[] code = new int[26];
    code['C' - 'A'] = 1; code['G' - 'A'] = 2; code['T' - 'A'] = 3;   // 'A' stays 0
    int mask = (1 << (2 * L)) - 1, h = 0;
    Set<Integer> seen = new HashSet<>(), added = new HashSet<>();

    for (int i = 0; i < s.length(); i++) {
        h = ((h << 2) | code[s.charAt(i) - 'A']) & mask;   // push 2 bits, drop the oldest
        if (i >= L - 1) {
            if (!seen.add(h) && added.add(h)) res.add(s.substring(i - L + 1, i + 1));
        }
    }
    return res;
}
```

```python
# python
# LC 187 - Repeated DNA Sequences
# IDEA: fixed-size window of 10 + 2-bit rolling encode (A=0,C=1,G=2,T=3) + set
# time = O(n), space = O(n)
def findRepeatedDnaSequences(s):
    L = 10
    if len(s) < L:
        return []
    code = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
    mask = (1 << (2 * L)) - 1
    h = 0
    seen, out = set(), set()

    for i, ch in enumerate(s):
        h = ((h << 2) | code[ch]) & mask       # push 2 bits, drop the oldest
        if i >= L - 1:
            if h in seen:
                out.add(s[i - L + 1:i + 1])
            else:
                seen.add(h)
    return list(out)
```


### 排序區間上的雙指標 — LC 1229 ⭐⭐⭐

> 放在這裡是因為它*不是*滑動視窗：這裡沒有一個帶合法性判斷的單一視窗，而是兩個在兩個排序清單上各自前進的指標。
> 它會勾起同一種直覺，所以值得練到分得清楚。真正的區間家族住在
> [intervals.md](./intervals.md) 和 [scanning_line.md](./scanning_line.md)。

**什麼時候用：**兩個排序好的區間陣列；找出第一個（或全部）滿足某個時長／長度要求的重疊區間。

#### 核心想法

```text
Sort both interval arrays by start time.
Use one pointer per array (i, j).
At each step:
  overlap = [max(start_i, start_j), min(end_i, end_j)]
  If overlap length >= required → answer found.
  Otherwise, advance the pointer whose interval ends EARLIER.

Why advance the earlier-ending interval?
  The interval that ends first can NEVER produce a larger overlap
  with any future interval — it's already exhausted.
  Keeping the later-ending interval gives the best chance of
  overlapping with something further right.
```

#### 模式

```text
Step 1: Sort both arrays by start time — O(n log n + m log m)
Step 2: i = 0, j = 0 (one pointer per array)
Step 3: while i < len(A) and j < len(B):
          overlapStart = max(A[i][0], B[j][0])
          overlapEnd   = min(A[i][1], B[j][1])
          if overlapEnd - overlapStart >= duration:
              return [overlapStart, overlapStart + duration]
          if A[i][1] < B[j][1]:   # A[i] ends earlier → advance i
              i++
          else:                    # B[j] ends earlier (or tie) → advance j
              j++
Step 4: return [] (no valid overlap found)
```

#### 模板（Java）—— Meeting Scheduler

```java
// LC 1229 - Meeting Scheduler
// IDEA: Sort + Two Pointers on interval arrays
// time = O(n log n + m log m), space = O(1)
public List<Integer> minAvailableDuration(int[][] slots1, int[][] slots2, int duration) {
    Arrays.sort(slots1, (a, b) -> a[0] - b[0]);
    Arrays.sort(slots2, (a, b) -> a[0] - b[0]);

    int i = 0, j = 0;
    while (i < slots1.length && j < slots2.length) {
        int overlapStart = Math.max(slots1[i][0], slots2[j][0]);
        int overlapEnd   = Math.min(slots1[i][1], slots2[j][1]);

        if (overlapEnd - overlapStart >= duration) {
            return Arrays.asList(overlapStart, overlapStart + duration);
        }

        // advance the pointer whose interval ends earlier
        if (slots1[i][1] < slots2[j][1]) {
            i++;
        } else {
            j++;
        }
    }
    return Collections.emptyList();
}
```

#### 手動跑一遍 —— `slots1=[[10,50],[60,120],[140,210]], slots2=[[0,15],[60,70]], duration=8`

```text
i  j  overlapStart  overlapEnd  length  action
0  0  max(10,0)=10  min(50,15)=15   5   < 8 → slots1[0][1]=50 > slots2[0][1]=15 → j++
0  1  max(10,60)=60 min(50,70)=50  -10  < 8 → slots1[0][1]=50 < slots2[1][1]=70 → i++
1  1  max(60,60)=60 min(120,70)=70  10  ≥ 8 → return [60, 68] ✓
```

#### 相似的 LeetCode 題目

| 題目 | LC# | 難度 | 關鍵洞見 |
|---------|-----|------------|-------------|
| **Meeting Scheduler** | **1229** | **Medium** | 核心範例 —— 在兩個時段陣列中找第一個長度 d 的重疊 |
| Interval List Intersections | 986 | Medium | 收集兩個排序區間清單之間的**所有**重疊；同一條「推進較早結束者」規則 |
| Employee Free Time | 759 | Hard | 合併所有員工的區間、找空檔 —— 同樣是多個排序清單上的指標想法 |
| Merge Intervals | 56 | Medium | 單一排序區間清單；貪婪地合併重疊區間 |
| Insert Interval | 57 | Medium | 一趟完成插入 + 合併進排序好的區間清單 |
| Meeting Rooms | 252 | Easy | 檢查有沒有任兩個區間重疊（依起點排序，比較相鄰的終點） |
| Meeting Rooms II | 253 | Medium | 數出最少需要幾間會議室；起點與終點分開排序，用雙指標 |
| Non-overlapping Intervals | 435 | Medium | 貪婪 —— 移除最少的區間，讓剩下的互不重疊 |

#### 模式辨識

```text
✅ Use Sort + Two Pointers on Intervals when:
   - Two sorted interval arrays, find first/all overlaps
   - "Earliest common availability" type problems
   - Merging or intersecting two independently sorted lists

✅ Related patterns:
   - Single interval list → sort + greedy scan (LC 56, 435)
   - Min rooms / conflicts → sort starts & ends separately (LC 253)
   - All intersections → same two-pointer loop, collect instead of return early (LC 986)
```


## 總結與速查

### 該用哪種進階形態？ —— 決策表

| 題目講的是 | 就用 | 章節 |
|---|---|---|
| 「每個大小 k 的視窗的最大／最小值」 | 單調雙端佇列 | 用單調雙端佇列求視窗極值 — LC 239 |
| 「最多 K 種相異」（求最長） | 模板 3 加上 `map.size() > K` | 最多 K 種相異元素 |
| 「剛好 K 個 …」（計數），字母表不受限 | `atMost(K) - atMost(K-1)` | [Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) |
| 「剛好 K 個 …」但只允許某些字元 | atMost + 遇到禁用字元就硬重置 | Count Vowel Substrings — LC 2062 |
| 「剛好 K 個 …」而且值可能是**負的** | 前綴和 + HashMap —— 視窗一律行不通 | 前綴和 + HashMap vs 滑動視窗 |
| 「剛好 K 個 …」而且你想一趟解決 | 前綴小技巧（數左邊的間隔） | 視窗內的前綴小技巧 — LC 1248 |
| 「從兩端移除的最少操作次數」 | `n - 和等於 total - x 的最大中段子陣列` | 最少操作數 → 最大子陣列長度 — LC 1658 |
| 「從任一端取 k 張牌」 | `total - 大小 n-k 的最小視窗` | 補集視窗 — LC 1423 |
| 「兩／三個互不重疊的子陣列，總和最大」 | 視窗和 + 前綴／後綴 argmax | 多個互不重疊的固定視窗 — LC 689 |
| 「`|i-j| <= k` **且** `|nums[i]-nums[j]| <= t`」 | 索引視窗 + 值分桶 | 索引固定的視窗 + 分桶 — LC 220 |
| 「所有單字的串接」／固定長度的 k-mer | 每個對齊偏移各跑一個視窗 | 以單字為單位的滑動視窗 — LC 30 |
| 「兩份行程表中最早的共同空檔」 | 雙指標，推進較早結束的那個 | 排序區間上的雙指標 — LC 1229 |

### 一個問題就能選出工具

> **合法性是單調的嗎？** 如果一個不合法的視窗在 `right` 繼續前進時仍然不合法，那麼一個
> `while (invalid) shrink` 就夠了，一切都是普通視窗。如果不是 —— 「剛好 k 個」、有負值，
> 或某個沒辦法改寫成「最多」的等式 —— 你就需要 at-most 相減、前綴表，或前綴小技巧。
> 這一個問題就把本頁所有技巧分乾淨了。

### 其他高頻的滑動視窗參考

*一些直接沿用 [sliding_window.md](./sliding_window.md) 模板的知名題目 —— 列出來只是為了辨識，沒有新技巧。*

| 題目 | LC# | 難度 | 用哪份模板／一句話洞見 |
|---------|-----|------------|-----------------------------------|
| Maximum Length of Repeated Subarray | 718 | Medium | 面試預設解是 DP（`dp[i][j]`）；滑動視窗的講法是把一個陣列在另一個上面滑，量每個對齊位置最長的連續相符 —— O(n·m) 時間但只要 O(1) 額外空間 |
| Maximum Number of Occurrences of a Substring | 1297 | Medium | 只需要看 `minSize` 的固定視窗 —— 任何較長的合法子字串裡一定包含一個合法的 `minSize`，所以 `maxSize` 是煙霧彈 |
| New 21 Game | 837 | Medium | 在 DP 陣列上開視窗：`dp[i] = (前 maxPts 個機率的視窗和) / maxPts`，每步 O(1) 維護 |
| Max Value of Equation | 1499 | Hard | 視窗受 `xj - xi <= k` 限制，再對 `yi - xi` 開單調雙端佇列 —— 見 [monotonic_queue.md](monotonic_queue.md) |

### 本頁不涵蓋的

- **無界串流上的視窗** —— 右端不是陣列索引、而是即時資料流的視窗。本頁除了 LC 220 那個按索引淘汰的做法之外，
  沒有任何地方假設隨機存取，所以模板可以直接搬過去；串流用的蓄水池／衰減計數技巧住在
  [streaming_algorithms.md](./streaming_algorithms.md)。
- **深入的 deque 家族** —— [monotonic_queue.md](./monotonic_queue.md)。
- **每個視窗的中位數（LC 480）** —— 那是兩個堆積加惰性刪除，不是 deque；見
  [heap.md](./heap.md)。

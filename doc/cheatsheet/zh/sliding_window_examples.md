# 滑動視窗 — 實戰範例

> **範圍** — 滑動視窗的 LeetCode 實作目錄，每題每語言只留一份標準解，並歸到它所對應的模板底下；模板本身、概念與選擇表都留在主檔滑動視窗那份。
> **另見**：[sliding_window.md](./sliding_window.md) — 每個範例所對應的六個標準模板，以及在它們之間做選擇的決策表；[sliding_window_advanced.md](./sliding_window_advanced.md) — 比較少見的視窗形狀（雙端佇列取極值、補集、以字為單位、分桶）；[hash_map.md](./hash_map.md) — 這些視窗大多要帶著的頻率表；[2_pointers_examples.md](./2_pointers_examples.md) — 對撞指標的實作目錄。

## LeetCode 題目清單

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [String](https://leetcode.com/problem-list/string/)

## 總覽

這裡每一題的解法，都是 [sliding_window.md](./sliding_window.md) 六個模板的其中一個，把三個空格填好而已。標題已經標明用的是哪一個模板，所以先讀模板，這份文件就會變成一組模板的實例，而不是九道各自為政的題目。

### 題目索引

| # | 題目 | LC# | 模板 | 語言 |
|---|---------|-----|----------|-------------|
| 1 | Permutation in String | 567 | 1 — 固定長度 | Java, Python |
| 2 | Find All Anagrams in a String | 438 | 1 — 固定長度 | Java, Python |
| 3 | Minimum Swaps to Group All 1's Together | 1151 | 1 — 固定長度（`k = #ones`） | Python |
| 4 | Max Consecutive Ones III | 1004 | 3 — 最長視窗 | Java |
| 5 | Longest Repeating Character Replacement | 424 | 3 — 最長視窗 | Java, Python |
| 6 | Frequency of the Most Frequent Element | 1838 | 3 — 最長視窗（先排序） | Java |
| 7 | Subarray Product Less Than K | 713 | 6 — 計數槽 | Java, Python |
| 8 | Arithmetic Slices | 413 | 自訂 — 回看連續段計數 | Python |
| 9 | Partition Labels | 763 | 自訂 — 貪婪的最後出現位置視窗 | Java |

> LC 3、LC 76、LC 209 **不在**這裡：它們是 [sliding_window.md](./sliding_window.md) 中模板 3、5、4 的實作本體，不重複陳述。

## 固定長度視窗

### 1) Permutation in String — LC 567

*模板 1。視窗固定為 `len(s1)`；因為長度固定，直接比對兩張頻率表就夠划算了 — 不需要 `have`／`need` 計數器。*

> 維護長度為 len(s1) 的視窗字元頻率；檢查是否與 s1 的頻率相符。

```java
// LC 567 - Permutation in String
// IDEA: Fixed sliding window — track char frequencies, check match
// time = O(N), space = O(1)
public boolean checkInclusion(String s1, String s2) {
    if (s1.length() > s2.length()) return false;
    int[] need = new int[26], window = new int[26];
    for (char c : s1.toCharArray()) need[c-'a']++;
    int k = s1.length();
    for (int i = 0; i < s2.length(); i++) {
        window[s2.charAt(i)-'a']++;
        if (i >= k) window[s2.charAt(i-k)-'a']--;
        if (Arrays.equals(need, window)) return true;
    }
    return false;
}
```

```python
# LC 567 Permutation in String
import collections
class Solution(object):
    def checkInclusion(self, s1, s2):
        l1, l2 = len(s1), len(s2)
        c1 = collections.Counter(s1)
        c2 = collections.Counter()
        p = q = 0
        while q < l2:
            c2[s2[q]] += 1
            if c1 == c2:
                return True
            q += 1
            if q - p + 1 > l1:
                c2[s2[p]] -= 1
                if c2[s2[p]] == 0:
                    del c2[s2[p]]
                p += 1
        return False
```

### 2) Find All Anagrams in a String — LC 438

*模板 1。視窗跟 LC 567 一模一樣，只是改成蒐集每個起始索引，而不是在第一次配對成功時就回傳。*

> 與 LC 567 相同，但要蒐集所有 anagram 視窗配對成功的起始索引。

```java
// LC 438 - Find All Anagrams in a String
// IDEA: Fixed sliding window — collect all positions where window = anagram
// time = O(N), space = O(1)
public List<Integer> findAnagrams(String s, String p) {
    List<Integer> result = new ArrayList<>();
    if (s.length() < p.length()) return result;
    int[] need = new int[26], window = new int[26];
    for (char c : p.toCharArray()) need[c-'a']++;
    int k = p.length();
    for (int i = 0; i < s.length(); i++) {
        window[s.charAt(i)-'a']++;
        if (i >= k) window[s.charAt(i-k)-'a']--;
        if (Arrays.equals(need, window)) result.add(i - k + 1);
    }
    return result;
}
```

```python
# LC 438 Find All Anagrams in a String
# IDEA : SLIDING WINDOW + collections.Counter()
class Solution(object):
    def findAnagrams(self, s, p):
        """
        :type s: str
        :type p: str
        :rtype: List[int]
        """
        ls, lp = len(s), len(p)
        cp = collections.Counter(p)
        cs = collections.Counter()
        ans = []
        for i in range(ls):
            cs[s[i]] += 1
            if i >= lp:
                cs[s[i - lp]] -= 1
                ### BE AWARE OF IT
                if cs[s[i - lp]] == 0:
                    del cs[s[i - lp]]
            if cs == cp:
                ans.append(i - lp + 1)
        return ans
```

### 3) Minimum Swaps to Group All 1's Together — LC 1151

*模板 1，但視窗長度是推導出來的：視窗寬 `k = sum(data)`，答案是 `ones - 任一這種視窗內的最大 1 數量` — 留在視窗裡的 0 剛好就是需要交換的次數。*

```python
# LC 1151 Minimum Swaps to Group All 1's Together
# IDEA : Sliding Window with Two Pointers
# IDEA : core : Find which sub-array HAS MOST "1", since it means it needs MINIMUM SWAP for getting all "1" toogether
# https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/solution/
class Solution:
    def minSwaps(self, data):
        ones = sum(data)
        cnt_one = max_one = 0
        left = right = 0
        while right < len(data):
            # updating the number of 1's by adding the new element
            cnt_one += data[right]
            right += 1
            # maintain the length of the window to ones
            if right - left > ones:
                # updating the number of 1's by removing the oldest element
                cnt_one -= data[left]
                left += 1
            # record the maximum number of 1's in the window
            max_one = max(max_one, cnt_one)
        return ones - max_one
```

## 最長視窗（可變、求最大）

### 4) Max Consecutive Ones III — LC 1004

*模板 3 最純粹的樣子：視窗狀態就是一個計數器（`zeroCnt`），合法性判斷是 `zeroCnt <= k`。*

> 右邊擴張，當 0 的數量超過 k 時從左邊收縮。

```java
// LC 1004 - Max Consecutive Ones III
// IDEA: Sliding window — track zero count, shrink when zeroCnt > k
// time = O(N), space = O(1)
public int longestOnes(int[] nums, int k) {
    int l = 0, zeroCnt = 0, ans = 0;
    for (int r = 0; r < nums.length; r++) {
        if (nums[r] == 0) zeroCnt++;
        while (zeroCnt > k) {
            if (nums[l] == 0) zeroCnt--;
            l++;
        }
        ans = Math.max(ans, r - l + 1);
    }
    return ans;
}
```

### 5) Longest Repeating Character Replacement — LC 424

*模板 3，合法性判斷改成 `windowLen - maxFreq <= k`：不管視窗裡出現最多次的是哪個字元，其他全部都得被替換掉。*

> 視窗合法的條件是（視窗長度 - 最大頻率）<= k；一邊擴張一邊追蹤最大頻率。

```java
// LC 424 - Longest Repeating Character Replacement
// IDEA: Sliding window — valid if windowSize - maxFreq <= k
// time = O(N), space = O(1)
public int characterReplacement(String s, int k) {
    int[] freq = new int[26];
    int l = 0, maxFreq = 0, ans = 0;
    for (int r = 0; r < s.length(); r++) {
        freq[s.charAt(r)-'A']++;
        maxFreq = Math.max(maxFreq, freq[s.charAt(r)-'A']);
        while ((r - l + 1) - maxFreq > k) freq[s.charAt(l++)-'A']--;
        ans = Math.max(ans, r - l + 1);
    }
    return ans;
}
```

```python
# LC 424 Longest Repeating Character Replacement
# IDEA : SLIDING WINDOW + DICT + 2 POINTERS
# (the `while p2 < len(s)` spelling of this same loop was dropped as a duplicate —
#  identical freq map and identical `windowLen - maxFreq > k` shrink, only the outer
#  loop differed)
from collections import defaultdict
class Solution:
    def characterReplacement(self, s, k):
        cnt = defaultdict(int)
        maxLen = 0
        l = 0
        # below can be either while or for loop
        for r in range(len(s)):
            cnt[s[r]] += 1
            ### NOTE : this condition
            while r - l + 1 - max(cnt.values()) > k:
                cnt[s[l]] -= 1
                l += 1
            maxLen = max(maxLen, r - l + 1)     

        return maxLen
```

**第二份 Java 版本，留下來是有理由的** — 這是*另一個*演算法，不是同一份程式碼換個寫法：對每個相異字母各跑一個獨立視窗，然後問「這個視窗能不能全部變成 `letter`？」。複雜度是 O(26·n) 而不是 O(n)，但完全不用維護 `maxFreq`，所以在面試壓力下要論證它是對的容易得多。

```java
// java
// LC 424
// IDEA : Sliding Window (Slow)
// https://leetcode.com/problems/longest-repeating-character-replacement/editorial/
public int characterReplacement_4(String s, int k) {
    HashSet<Character> allLetters = new HashSet();

    // collect all unique letters
    for (int i = 0; i < s.length(); i++) {
        allLetters.add(s.charAt(i));
    }

    int maxLength = 0;
    for (Character letter : allLetters) {
        int start = 0;
        int count = 0;
        // initialize a sliding window for each unique letter
        for (int end = 0; end < s.length(); end += 1) {
            if (s.charAt(end) == letter) {
                // if the letter matches, increase the count
                count += 1;
            }
            // bring start forward until the window is valid again
            while (!isWindowValid(start, end, count, k)) {
                if (s.charAt(start) == letter) {
                    // if the letter matches, decrease the count
                    count -= 1;
                }
                start += 1;
            }
            // at this point the window is valid, update maxLength
            maxLength = Math.max(maxLength, end + 1 - start);
        }
    }
    return maxLength;
}

private Boolean isWindowValid(int start, int end, int count, int k) {
    // end + 1 - start - count is different element count
    return end + 1 - start - count <= k;
}
```

### 6) Frequency of the Most Frequent Element — LC 1838

*模板 3，前面先做一次 O(n log n) 排序。排序才是讓視窗有意義的關鍵：一個視窗最便宜的目標值一定是它最右邊那個數，所以把整個視窗拉平的成本就是 `nums[r] * windowSize - windowSum`。*

> 先排序；右邊擴張，當拉平視窗的成本超過 k 時從左邊收縮。

```java
// LC 1838 - Frequency of the Most Frequent Element
// IDEA: Sort + sliding window — equalize all elements in window to nums[r]
// time = O(N log N), space = O(1)
public int maxFrequency(int[] nums, int k) {
    Arrays.sort(nums);
    int l = 0, ans = 1;
    long windowSum = 0;
    for (int r = 1; r < nums.length; r++) {
        windowSum += nums[r];
        // cost to raise all window elements to nums[r]
        while ((long) nums[r] * (r - l + 1) - windowSum > k) {
            windowSum -= nums[l++];
        }
        ans = Math.max(ans, r - l + 1);
    }
    return ans;
}
```

## 計數型視窗

### 7) Subarray Product Less Than K — LC 713

*模板 6 的計數槽，但少了扣減那一步：條件（`product < k`）本身就已經是「至多」了，所以每一步做 `count += r - l + 1` 就是全部的答案。*

> 當乘積 >= k 時從左邊收縮；每個合法的右端位置貢獻 (r-l+1) 個子陣列。

```java
// LC 713 - Subarray Product Less Than K
// IDEA: Sliding window — count subarrays ending at r with product < k
// time = O(N), space = O(1)
public int numSubarrayProductLessThanK(int[] nums, int k) {
    if (k <= 1) return 0;
    int l = 0, product = 1, count = 0;
    for (int r = 0; r < nums.length; r++) {
        product *= nums[r];
        while (product >= k) product /= nums[l++];
        count += r - l + 1; // all subarrays ending at r with left in [l, r]
    }
    return count;
}
```

```python
# LC 713 Subarray Product Less Than K
# IDEA : SLIDING WINDOW 
# MAINTAIN 2 INDEX : left, i, SO THE SLIDING WINDOW IS : [left, i]
# CHECK IF THE PRODUCT OF ALL DIGITS IN THE WINDOW [left, i] < k
# IF NOT, REMOVE CURRENT LEFT, AND DO LEFT ++
# REPEAT ABOVE PROCESS AND GO THOROUGH ALL ARRAY  
class Solution:
    def numSubarrayProductLessThanK(self, nums, k):
        # init values
        product = 1
        i = 0
        result = 0
        
        for j, num in enumerate(nums):
            ### NOTE : we get product first
            product *= num
            ### NOTE : the while loop condition : product >= k
            #         -> if product >= k, we do the corresponding op
            while i <= j and product >= k:
                ### NOTE this trick
                #    -> divided the number back, since this number already make the product > k 
                product = product // nums[i]
                ### NOTE : move i to 1 right index
                i += 1
            ### NOTE : , the number of intervals with subarray product less than k and with right-most coordinate right, is right - left + 1
            #    -> https://leetcode.com/problems/subarray-product-less-than-k/solution/           
            result += (j - i + 1)           
        return result
```

## 自訂視窗形狀

### 8) Arithmetic Slices — LC 413

*這不是雙指標視窗，而是一個回看的連續段計數器。每個延續等差連續段的索引，會為它後面每一段仍然成立的延伸各貢獻一個切片。之所以放在這裡，是因為 LeetCode 把它歸類在滑動視窗，而且「一路往回看到條件斷掉為止」的迴圈用的是同一種直覺。*

```python
# LC 413 Arithmetic Slices
# IDEA : SLIDING DINDOW + 2 pointers
# STEPS:
#   -> step 1) loop over nums from idx=2 (for i in range(2, len(A)))
#   -> step 2) use the other pointer j, "look back to idx = 0" via while loop
#       -> if there is any case fit condition, add to result
#   -> step 3) return ans
class Solution(object):
    def numberOfArithmeticSlices(self, A):
        # edge case
        if not A or len(A) < 3:
            return 0
        res = 0
        j = 2
        for i in range(2, len(A)):
            # use the other pointer j, "look back to idx = 0" via while loop
            j = i
            while j-2 >= 0:
                # if there is any case fit condition, add to result
                if A[j] - A[j-1] == A[j-1] - A[j-2]:
                    res += 1
                    j -= 1
                else:
                    break
        return res 
```

### 9) Partition Labels — LC 763

*貪婪視窗，沒有收縮階段：先預先算出每個字元最後出現的索引，掃描時一路把 `end` 往外拉，`i == end` 時就切一刀。這個視窗只會變大，然後重新開始。*

```java
// java
// LC 763 Partition Labels

// IDEA: GREEDY + hashMap record last idx + sliding window (fixed by gpt)
public List<Integer> partitionLabels_0_2(String s) {
    List<Integer> res = new ArrayList<>();

    if (s == null || s.length() == 0) {
        return res;
    }

    // Map each character to its last index
    Map<Character, Integer> lastIndexMap = new HashMap<>();
    for (int i = 0; i < s.length(); i++) {
        lastIndexMap.put(s.charAt(i), i);
    }

    int l = 0;
    while (l < s.length()) {
        int end = lastIndexMap.get(s.charAt(l));
        int r = l;

        // Expand the window to include all characters in the current segment
        while (r < end) {
            end = Math.max(end, lastIndexMap.get(s.charAt(r)));
            r++;
        }

        res.add(end - l + 1);
        l = end + 1;
    }

    return res;
}
```

## 總結與速查

| 填進去的槽 | LC 567 / 438 | LC 1004 | LC 424 | LC 1838 | LC 713 |
|---|---|---|---|---|---|
| 視窗狀態 | 字元頻率表 | `zeroCnt` | 字元頻率 + `maxFreq` | `windowSum` | 累計 `product` |
| 合法性判斷 | 長度 `== len(p)` | `zeroCnt <= k` | `len - maxFreq <= k` | `nums[r]*len - sum <= k` | `product < k` |
| 結果更新 | 表相符時 `res.add(l)` | `max(len)` | `max(len)` | `max(len)` | `count += r - l + 1` |
| 模板 | 1 | 3 | 3 | 3（排序後） | 6（計數槽） |

- **記得刪掉次數歸零的 key。** 這裡每個以表為基礎的視窗，都倚賴 `map.size()`／`len(dict)` 是真正的相異元素個數。只遞減不刪除會讓它虛胖，收縮迴圈就會出錯。
- **字母集有界就改用陣列。** LC 567／438 用 `int[26]` 搭配 `Arrays.equals`，比拿 `HashMap` 來比對又快又短。
- **在正確的位置記錄答案。** 最長視窗類的題目在收縮迴圈*之後*更新；最短視窗類的題目在迴圈*裡面*更新。

這些範例所實例化的模板見 [sliding_window.md](./sliding_window.md)；不在六個模板裡的視窗形狀見 [sliding_window_advanced.md](./sliding_window_advanced.md)。

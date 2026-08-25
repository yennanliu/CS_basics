# Sliding Window — Worked Examples

> **Scope** — The worked sliding-window LeetCode catalogue, one canonical solution per problem per language, each filed under the template it instantiates; the templates, the concepts and the decision tables stay in the main sliding-window sheet.
> **See also**: [sliding_window.md](./sliding_window.md) — the six canonical templates each example instantiates, and the decision table that picks between them; [sliding_window_advanced.md](./sliding_window_advanced.md) — the rarer window shapes (deque extrema, complement, word-level, bucketed); [hash_map.md](./hash_map.md) — the frequency map most of these windows carry; [2_pointers_examples.md](./2_pointers_examples.md) — the converging-pointer catalogue.

## LeetCode Problem Lists

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [String](https://leetcode.com/problem-list/string/)

## Overview

Every solution here is one of the six templates in [sliding_window.md](./sliding_window.md) with
its three slots filled in. The heading says which template, so read the template first and this
sheet becomes a set of instantiations rather than nine separate problems.

### Problem Index

| # | Problem | LC# | Template | Language(s) |
|---|---------|-----|----------|-------------|
| 1 | Permutation in String | 567 | 1 — Fixed-Size | Java, Python |
| 2 | Find All Anagrams in a String | 438 | 1 — Fixed-Size | Java, Python |
| 3 | Minimum Swaps to Group All 1's Together | 1151 | 1 — Fixed-Size (`k = #ones`) | Python |
| 4 | Max Consecutive Ones III | 1004 | 3 — Longest Window | Java |
| 5 | Longest Repeating Character Replacement | 424 | 3 — Longest Window | Java, Python |
| 6 | Frequency of the Most Frequent Element | 1838 | 3 — Longest Window (sort first) | Java |
| 7 | Subarray Product Less Than K | 713 | 6 — Counting slot | Java, Python |
| 8 | Arithmetic Slices | 413 | custom — look-back run counting | Python |
| 9 | Partition Labels | 763 | custom — greedy last-index window | Java |

> LC 3, LC 76 and LC 209 are **not** here: they are the worked bodies of Templates 3, 5 and 4
> in [sliding_window.md](./sliding_window.md) and are not restated.

## Fixed-Size Window

### 1) Permutation in String — LC 567

*Template 1. Fixed window of `len(s1)`; because the size is fixed, comparing the two frequency
maps directly is affordable — no `have`/`need` counter needed.*

> Maintain character frequency of window size len(s1); check if it matches s1's freq.

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

*Template 1. Same window as LC 567, but collect every start index instead of returning on the
first match.*

> Same as LC 567 but collect all starting indices where anagram window matches.

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

*Template 1 with a derived window size: the window is `k = sum(data)` wide, and the answer is
`ones - max ones inside any such window` — the 0s left inside are exactly the swaps needed.*

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

## Longest Window (Variable Max)

### 4) Max Consecutive Ones III — LC 1004

*Template 3 in its purest form: the window state is a single counter (`zeroCnt`), the validity
test is `zeroCnt <= k`.*

> Expand right, shrink left when zero count exceeds k.

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

*Template 3 with the validity test `windowLen - maxFreq <= k`: whatever the most frequent
character in the window is, everything else has to be replaced.*

> Window is valid if (window size - max frequency) <= k; expand and track max freq.

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

**Second Java variant, kept for a reason** — a *different* algorithm, not a different spelling:
run one independent window per distinct letter and ask "can this window become all-`letter`?".
O(26·n) instead of O(n), but it needs no `maxFreq` bookkeeping, so it is much easier to argue
correct under pressure.

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

*Template 3 after an O(n log n) sort. Sorting is what makes the window meaningful: the cheapest
target for a window is always its rightmost value, so the cost to level the window is
`nums[r] * windowSize - windowSum`.*

> Sort array; expand right, shrink left when cost to equalize window exceeds k.

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

## Counting Windows

### 7) Subarray Product Less Than K — LC 713

*Template 6's counting slot without the subtraction: the condition (`product < k`) is already
"at most", so `count += r - l + 1` on every step is the whole answer.*

> Shrink left when product >= k; each valid right position contributes (r-l+1) subarrays.

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

## Custom Window Shapes

### 8) Arithmetic Slices — LC 413

*Not a two-pointer window: a look-back run counter. Every index that continues an arithmetic run
adds one slice per extension still valid behind it. Included here because it is filed under
sliding window on LeetCode and the "look back until the condition breaks" loop is the same
instinct.*

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

*Greedy window with no shrink phase: precompute each character's last index, then stretch `end`
while scanning and cut when `i == end`. The window only ever grows and then restarts.*

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

## Summary & Quick Reference

| Filled-in slot | LC 567 / 438 | LC 1004 | LC 424 | LC 1838 | LC 713 |
|---|---|---|---|---|---|
| Window state | char freq map | `zeroCnt` | char freq + `maxFreq` | `windowSum` | running `product` |
| Validity test | size `== len(p)` | `zeroCnt <= k` | `len - maxFreq <= k` | `nums[r]*len - sum <= k` | `product < k` |
| Result update | `res.add(l)` on map match | `max(len)` | `max(len)` | `max(len)` | `count += r - l + 1` |
| Template | 1 | 3 | 3 | 3 (after sort) | 6 (counting slot) |

- **Delete zero-count keys.** Every map-based window here depends on `map.size()` /
  `len(dict)` being the true distinct count. Decrementing without deleting inflates it and the
  shrink loop misbehaves.
- **Bounded alphabet → use an array.** `int[26]` with `Arrays.equals` is both faster and shorter
  than a `HashMap` comparison for LC 567 / 438.
- **Record the result in the right place.** Longest-window problems update *after* the shrink
  loop; shortest-window problems update *inside* it.

For the templates these instantiate see [sliding_window.md](./sliding_window.md); for the window
shapes that are not in the six templates see [sliding_window_advanced.md](./sliding_window_advanced.md).

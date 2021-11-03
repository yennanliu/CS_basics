# Sliding window 
- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E6%BB%91%E5%8A%A8%E7%AA%97%E5%8F%A3%E6%8A%80%E5%B7%A7.md

## 0) Concept  
1. two pointers
2. while loop
3. working with conditions on `start` and `end` index

### 0-1) Types

### 0-2) Pattern
```python
# python pseudo code
# init
l = r = 0
window = []

while r < len(s):
    window.append(s[r])
    r += 1
    # do wth
    while valid:
        window.remove(s[l])
        l += 1
        # do wth
```

```java
// java
int left = 0, right = 0;

while (right < s.size()) {
    window.add(s[right]);
    right++;
    // NOTE : most of the sliding windonw trick 
    // is dealing with "valid" conditions
    // and how to cache some conditions for verfication
    while (valid) {
        window.remove(s[left]);
        left++;
    }
}
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Permutation in String

```python
# LC 567 Permutation in String
# V0 
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

### 2-2) Find All Anagrams in a String

```python
# LC 438 Find All Anagrams in a String
# V0
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

### 2-3) Longest Substring Without Repeating Characters

```python
# LC 003 Longest Substring Without Repeating Characters
# V0
# IDEA : SLIDING WINDOW + DICT
#       -> use a hash table (d) record visit "alphabet" (e.g. : a,b,c,...)
#          (but NOT sub-string)
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        d = {}
        l = 0
        res = 0
        for r in range(len(s)):
            ### NOTE : if already visited, means "repeating"
            #         -> then we need to update left pointer (l)
            if s[r] in d:
                # note here
                l = max(l, d[s[r]] + 1)
            # if not visited yet, record the alphabet
            # and re-calculate the max length
            d[s[r]] = r
            res = max(res, r -l + 1)
        return res
```
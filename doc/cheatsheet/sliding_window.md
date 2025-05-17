# Sliding window 
- [fucking-algorithm : sliding window](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E6%BB%91%E5%8A%A8%E7%AA%97%E5%8F%A3%E6%8A%80%E5%B7%A7.md)
- 2 pointers + while loop + boundary condition
- Find min/max values of sub-set with conditions with given input

## 0) Concept  
1. two pointers
2. while loop
    - while - while
    - for - while
    - NOTE !!! : `1st while` find an acceptable solution, `2nd while` optimize solution, find the best one
3. boundary conditions op
    - `start` and `end` index

### 0-1) Types

- Types
    - Permutation(排列)
        - LC 567: check if a permutation existed in the other string
        ```java
        // java
        int l = 0;
        for (int r = 0; r < s2.length(); r++){
            
            /** NOTE !!! below */
            if (map2.equals(map1)){
                return true;
            }
            // ...
            if (conditon){
                // do sth
            }
        }
        ```
    - Anagrams
        - LC 438: Find All Anagrams (字謎詞) in a String
    - Substring
        - LC 003 : Longest Substring Without Repeating Characters
        - LC 424 : Longest Repeating Character Replacement
        ```java
        // java
        int l = 0;
        for (int r = 0; r < s.length(); r ++){
            // ...

            /** NOTE !!! below */
            while (condition){
                // some op
                // do sth
            }
        }
        ```
    - SubArray
        - LC 713 : Subarray Product Less Than K
        - LC 209 : Minimum Size Subarray Sum
    - SubString without repeating elements
        - LC 003 : Longest Substring Without Repeating Characters

- Algorithm
    - sliding window
    - Counter
    - defaultdict
    - 2 pointers

- Data structure
    - dict
    - set
    - array
    - string

### 0-2) Pattern

#### 0-2-1) Basic

```python
# python
#-------------------------
# V1 : while - while
#-------------------------

# NOTE !!! : 
# `1st while` find an acceptable solution, 
# `2nd while` optimize solution, find the best one

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

#-------------------------
# V2 : for - while
#-------------------------
# init
l = r = 0
window = []

for r in range(len(r)):
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
//-------------------------
// V1 : while - while
//-------------------------
// sliding window pattern
// https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9
int left = 0, right = 0;

while (right < nums.size()) {
    // enlarge window
    window.addLast(nums[right]);
    right++;
    
    while (window needs shrink) {
        // lower window
        window.removeFirst(nums[left]);
        left++;
    }
}

//-------------------------
// V2 : for - while
//-------------------------
int left = 0, right = 0;
for (int right; right < s.length() ; right++) {
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

#### 0-2-1) gat max different characters

```java
// LC 424 : Longest Repeating Character Replacement

int slow = 0;
Map<String, List<Integer>> map = new HashMap();
// ...
for (int fast = 0; fast < s.length(); fast++){

    // String cur = String.valueOf(s.charAt(fast));
    // map.put(cur, map.getOrDefault(cur, 0)+1);

    // NOTE !!! below
    while ((fast - slow + 1) - getMaxVal(map) > k){
        // ...
    }
}

// ...
private int getMaxVal(Map<String, List<Integer>> map){
    int res = 0;
    for (int x : map.values()){
        res = Math.max(res, x);
    }
    return res;
}
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Permutation in String


```java
// java
// LC 567
    // V0
    // IDEA: HASHMAP + SLIDING WINDOW (fixed by gpt)
    public boolean checkInclusion(String s1, String s2) {
        if (!s1.isEmpty() && s2.isEmpty()) {
            return false;
        }
        if (s1.equals(s2)) {
            return true;
        }
        /** NOTE !!!
         *
         *  we init 2 map, one for s1 counter, the other one as track `s2 sub str counter`
         */
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        for (String x : s1.split("")) {
            String k = String.valueOf(x);
            map1.put(x, map1.getOrDefault(k, 0) + 1);
        }

        // 2 pointers (for s2)
        /** NOTE !!!
         *
         *  we have 2 pointers (for s2) that can track character cnt in s2 within l, r pointers
         */
        int l = 0;
        for (int r = 0; r < s2.length(); r++) {
            String val = String.valueOf(s2.charAt(r));
            map2.put(val, map2.getOrDefault(val, 0) + 1);

            /** NOTE !!!
             *
             *  we use below trick to
             *
             *  -> 1) check if `new reached s2 val` is in s1 map
             *  -> 2) check if 2 map are equal
             *
             *  -> so we have more simple code, and clean logic
             */
            if (map2.equals(map1)) {
                return true;
            }

            /**
             *  NOTE !!!
             *
             *  If the window size exceeds the size of s1, move the left pointer
             *  -> means the `permutation str in s2 of s1` IS NOT FOUND YET,
             *  -> in this case, we need to move s2 left pointer, and update tracking map
             */
            if ((r - l + 1) >= s1.length()) {
                // update map
                String leftVal = String.valueOf(s2.charAt(l));
                map2.put(leftVal, map2.get(leftVal) - 1);
                /**
                 * NOTE !!!
                 *
                 *  if can't find permutation at current window ([l,r]),
                 *  then we move left pointer 1 idx (e.g. l += 1)
                 *  for moving and checking next window
                 */
                l += 1;
                if (map2.get(leftVal) == 0) {
                    map2.remove(leftVal);
                }
            }
        }

        return false;
    }
```

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
# V0'
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

# V0'
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

```java
// java
// V0
// IDEA : SLIDING WINDOW + HASH SET
public int lengthOfLongestSubstring(String s) {

    if (s.equals("")){
        return 0;
    }

    if (s.equals(" ")){
        return 1;
    }

    if (s.length() == 1){
        return 1;
    }

    int ans = 0;
    char[] s_array = s.toCharArray();
    for (int i = 0; i < s_array.length-1; i++){
        int j = i;
        Set<String> set = new HashSet<String>();
        while (j < s_array.length){
            String cur = String.valueOf(s_array[j]);
            if (set.contains(cur)){
                ans = Math.max(ans, set.size());
                break;
            }else{
                set.add(cur);
                ans = Math.max(ans, set.size());
                j += 1;
            }
        }
    }

    return ans;
}
```

### 2-4) Subarray Product Less Than K
```python
# LC 713 Subarray Product Less Than K
# V0 
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

### 2-5) Minimum Size Subarray Sum
```python
# LC 209 Minimum Size Subarray Sum
# V0
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

### 2-6) Longest Repeating Character Replacement
```python
# lc 424. Longest Repeating Character Replacement
# V0
# IDEA : SLIDING WINDOW + DICT + 2 POINTERS
from collections import Counter
class Solution(object):
    def characterReplacement(self, s, k):
        table = Counter()
        res = 0
        p1 = p2 = 0
        # below can be either while or for loop
        while p2 < len(s):
            table[s[p2]] += 1
            p2 += 1
            """
            ### NOTE : if remain elements > k, means there is no possibility to make this substring as "longest substring containing the same letter"
               ->  remain elements = p1 - p2 - max(table.values())
               ->  e.g. if we consider "max(table.values()" as the "repeating character", then "p2 - p1 - max(table.values()" is the count of elements we need to replace
               ->  so we need to clear "current candidate" for next iteration
            """
            while p2 - p1 - max(table.values()) > k:
                table[s[p1]] -= 1
                p1 += 1
            res = max(res, p2 - p1)
        return res
    
# V0'
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

```java
// java
// LC 424
// V2
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

### 2-6) Arithmetic Slices
```python
# LC 413 Arithmetic Slices
# V0
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

### 2-7) Minimum Swaps to Group All 1's Together
```python
# LC 1151 Minimum Swaps to Group All 1's Together
# V0
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

### 2-8) Partition Labels

```java
// java
// LC 763 Partition Labels

// V0-2
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
# Two pointers

- Ref
    - [fucking-algorithm : 2 pointers](https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9)

## 0) Concept  

### 0-1) Types

- Pointer types

    - `Fast - Slow pointers`
        - fast, slow pointers from `same start point`

    - `Left- Right pointers`
        - left, right pointers from `idx = 0, idx = len(n) - 1` respectively
        - Usually set
            - left pointer = 0
            - right pointer = len(nums)
        - [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
        - array reverse
        - [2 sum](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
        - [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)

- `Expand` from center (and Deal with `odd, even` cases)
    - LC 680
    - LC 647
    - LC 005

- Merge Sorted Array
    - LC 88

- Minimum Swaps to Group All 1's Together
    - LC 1151 (check [sliding_window.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md))

- Boats to Save People
    - LC 881

- move `right pointer first`, then move `left point` per condition
    - LC 567
    - LC 209 (see `sliding window cheatsheet`)

- Algorithm
    - binary search
    - sliding window
    - for loop + "expand `left`, `right` from center"

- Data structure
    - Array
    - Linked list

### 0-2) Pattern

### 0-2-0) Remove Duplicates from Sorted Array
```java
// java
// LC 26 (LC 83)
// https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9
/**
 *  //--------------------------------
 *  Example 1
 *  //--------------------------------
 *
 *  nums = [1,1,2]
 *
 *  [1,1,2]
 *   s f
 *
 *  [1,2, 1]     if nums[f] != nums[s], move s, then swap f, s
 *   s s  f
 *
 *
 *   //--------------------------------
 *   Example 2
 *   //--------------------------------
 *
 *   nums = [0,0,1,1,1,2,2,3,3,4]
 *
 *   [0,0,1,1,1,2,2,3,3,4]
 *    s f
 *
 *   [0,1,0,1,1,2,2,3,3,4]   if nums[f] != nums[s], move s, then swap f, s
 *    s s f
 *
 *   [0,1,0,1,1,2,2,3,3,4]
 *      s   f
 *
 *   [0,1,0,1,1,2,2,3,3,4]
 *      s     f
 *
 *   [0,1,2,1,1,0,2,3,3,4]   if nums[f] != nums[s], move s, then swap f, s
 *      s s     f
 *
 *   [0,1,2,1,1,0,2,3,3,4]
 *        s       f
 *
 *   [0,1,2,3,1,0,2,1,3,4]  if nums[f] != nums[s], move s, then swap f, s
 *        s s       f
 *
 *   [0,1,2,3,1,0,2,1,3,4]
 *          s         f
 *
 *   [0,1,2,3,4,0,2,1,3,1]   if nums[f] != nums[s], move s, then swap f, s
 *          s s         f
 *
 */
class Solution {
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int slow = 0, fast = 0;
        while (fast < nums.length) {
            // NOTE !!! if slow pointer != fast pointer
            if (nums[fast] != nums[slow]) {
                // NOTE !!! move slow pointer first
                slow++;
                // 维护 nums[0..slow] 无重复
                // NOTE !!! swap slow, fast pointer val
                nums[slow] = nums[fast];
            }
            fast++;
        }
        // 数组长度为索引 + 1
        return slow + 1;
    }
}
```
### 0-2-1) Remove Element
```java
// java
// LC 27
// https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9
/**
 *  //--------------------
 *  Example 1
 *  //--------------------
 *
 *  nums = [3,2,2,3], val = 3
 *
 *  [3,2,2,3]
 *   s
 *   f
 *
 *  [2,3,2,3]    if nums[f] != val, swap, move s
 *   s s
 *     f
 *
 *  [2,2,3,3]   if nums[f] != val, swap, move s
 *     s s
 *       f
 *
 * [2,2,3,3]
 *      s
 *        f
 *
 *
 *  //--------------------
 *  Example 2
 *  //--------------------
 *
 *  nums = [0,1,2,2,3,0,4,2], val = 2
 *
 *
 *  [0,1,2,2,3,0,4,2]   if nums[f] != val, swap, move s
 *   s s
 *   f
 *
 *  [0,1,2,2,3,0,4,2]     if nums[f] != val, swap, move s
 *     s s
 *     f
 *
 *  [0,1,2,2,3,0,4,2]
 *       s
 *       f
 *
 * [0,1,2,2,3,0,4,2]
 *      s
 *        f
 *
 * [0,1,3,2,2,0,4,2]   if nums[f] != val, swap, move s
 *      s s
 *          f
 *
 * [0,1,3,0,2,2,4,2]   if nums[f] != val, swap, move s
 *        s s
 *            f
 *
 * [0,1,3,0,4,2,2,2]    if nums[f] != val, swap, move s
 *          s s
 *              f
 *
 *  [0,1,3,0,4,2,2,2]
 *             s
 *                 f
 */
class Solution {
    public int removeElement(int[] nums, int val) {
        int fast = 0, slow = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }
}
```

### 0-2-2) Move Zeros to End
```java
// java
// LC 283 Move Zeroes
// https://leetcode.com/problems/move-zeroes/
/**
 * Pattern: Move all zeros (or specific elements) to the end while maintaining
 * the relative order of non-zero elements. Must be done in-place.
 *
 * Key Idea:
 *   - Both pointers (l and r) start from index 0
 *   - l tracks the position where the next non-zero should be placed
 *   - r scans through the array
 *   - When r finds a non-zero, swap with l and move l forward
 *   - This moves all zeros to the end naturally
 *
 * Difference from "Remove Element" pattern:
 *   - Remove Element: overwrites without caring about moved elements
 *   - Move Zeros: uses SWAP to preserve all elements in array
 *
 *  //--------------------
 *  Example 1
 *  //--------------------
 *
 *  nums = [0,1,0,3,12]
 *
 *  [0,1,0,3,12]
 *   l
 *   r
 *
 *  [0,1,0,3,12]    nums[r]=0, no swap, move r
 *   l
 *     r
 *
 *  [1,0,0,3,12]    nums[r]!=0, swap(l,r), move l and r
 *   l l
 *     r
 *
 *  [1,0,0,3,12]    nums[r]=0, no swap, move r
 *     l
 *       r
 *
 *  [1,3,0,0,12]    nums[r]!=0, swap(l,r), move l and r
 *     l l
 *         r
 *
 *  [1,3,12,0,0]    nums[r]!=0, swap(l,r), move l and r
 *        l  l
 *            r
 *
 *  //--------------------
 *  Example 2
 *  //--------------------
 *
 *  nums = [0]
 *  [0]
 *   l
 *   r
 *  -> only one element, no change
 *
 *  //--------------------
 *  Example 3
 *  //--------------------
 *
 *  nums = [1,0,2,0,3]
 *
 *  [1,0,2,0,3]    nums[r]!=0, swap(l,r), move l and r
 *   l l
 *   r
 *
 *  [1,0,2,0,3]    nums[r]=0, no swap, move r
 *     l
 *     r
 *
 *  [1,2,0,0,3]    nums[r]!=0, swap(l,r), move l and r
 *     l l
 *       r
 *
 *  [1,2,0,0,3]    nums[r]=0, no swap, move r
 *       l
 *         r
 *
 *  [1,2,3,0,0]    nums[r]!=0, swap(l,r), move l and r
 *       l l
 *           r
 *
 * Time: O(N), Space: O(1)
 */
class Solution {
    public void moveZeroes(int[] nums) {
        if (nums == null || nums.length <= 1)
            return;

        // 'l' is the position where the next non-zero number should be placed
        int l = 0;

        /** NOTE !!!
         *
         *  BOTH l, r start from idx = 0
         */
        // Iterate through the array with 'r'
        for (int r = 0; r < nums.length; r++) {
            // If we find a non-zero element
            if (nums[r] != 0) {
                // Swap it with the element at position 'l'
                int tmp = nums[r];
                nums[r] = nums[l];
                nums[l] = tmp;

                /** NOTE !!!
                 *
                 *  Move 'l' forward if `we swap`
                 */
                // Move 'l' forward
                l++;
            }
        }
    }
}
```

**Similar Problems:**
- LC 283 Move Zeroes (this pattern)
- LC 27 Remove Element (overwrite version)
- LC 905 Sort Array By Parity (move even numbers to front)
- LC 922 Sort Array By Parity II (even/odd positioning)
- LC 2460 Apply Operations to an Array
- LC 1089 Duplicate Zeros (expanding array version)


#### 0-2-3) for loop + "expand `left`, `right` from center"

```python
# LC 005  Longest Palindromic Substring
# LC 647 Palindromic Substrings
# python
# pseudo code
# ...
for i in range(lens(s)):

    # NOTE !!!
    # NO NEED to have logic like `if i % 2 == 1`..
    # we can just consider `odd, even len` cases directly

    #--------------------------------------
    # if odd
    # NOTE !!! if `odd`, left = right = i
    #--------------------------------------
    left = right = i
    while left >= 0 and right < len(s) and s[left] == s[right]:
        if right+1-left > len(res):
            res = s[left:right+1]
        left -= 1
        right += 1
    
    #--------------------------------------
    # if even
    # NOTE !!! if `odd`, left = i - 1, right = i
    #--------------------------------------
    left = i - 1
    right = i
    while left >= 0 and right < len(s) and s[left] == s[right]:
        if right+1-left > len(res):
            res = s[left:right+1]
        left -= 1
        right += 1
# ...
```

#### 0-2-4) Move `right pointer`, then move `left point`

```java
// java
// LC 567

int l = 0;
for (int r = 0; r < s2.length(); r++){
    // ...

    if(some_condition){

        // ...
        // update map and move left pointer
        l += 1;
    }
}


// ...
```

#### 0-2-5) Subsequence Matching (One Always Moves, One Conditionally Moves)

```java
// java
// LC 392 Is Subsequence
// https://leetcode.com/problems/is-subsequence/

/**
 * Pattern: Check if string s is a subsequence of string t
 *
 * Key Idea:
 *   - Use two pointers: i for s (target subsequence), j for t (main string)
 *   - ALWAYS move j (scan through entire t)
 *   - ONLY move i when characters match
 *   - If i reaches end of s, we found all characters in order
 *
 * Example:
 *   s = "abc", t = "ahbgdc"
 *
 *   [a h b g d c]    i=0, j=0, s[i]=a, t[j]=a, match! i++, j++
 *    i j
 *
 *   [a h b g d c]    i=1, j=1, s[i]=b, t[j]=h, no match, j++
 *      i j
 *
 *   [a h b g d c]    i=1, j=2, s[i]=b, t[j]=b, match! i++, j++
 *        i j
 *
 *   [a h b g d c]    i=2, j=3, s[i]=c, t[j]=g, no match, j++
 *          i j
 *
 *   [a h b g d c]    i=2, j=4, s[i]=c, t[j]=d, no match, j++
 *            i j
 *
 *   [a h b g d c]    i=2, j=5, s[i]=c, t[j]=c, match! i++, j++
 *              i j
 *
 *   i == s.length() -> return true
 */
public boolean isSubsequence(String s, String t) {
    if (s.isEmpty())
        return true;
    if (t.isEmpty())
        return false;

    int i = 0; // Pointer for s (target subsequence)
    int j = 0; // Pointer for t (main string)

    /** NOTE !!!
     *
     *  the while loop condition:
     *
     *     i < s.length()
     *     &&
     *     j < t.length()
     */
    while (i < s.length() && j < t.length()) {
        // If characters match, move the pointer for s
        if (s.charAt(i) == t.charAt(j)) {
            i++;
        }
        // Always move the pointer for t
        j++;
    }

    // If i reached the end of s, all characters were found in order
    return i == s.length();
}
```

**Classic Problems:**
- LC 392 Is Subsequence
- LC 524 Longest Word in Dictionary through Deleting
- LC 792 Number of Matching Subsequences

## 1) General form

### 1-1) Basic OP

#### 1-1-1 : Reverse Array
```java
// java
void reverse(int[] nums){

    int left = 0;
    int right = nums.length - 1

    while (left < right){

        int tmp = nums(left);
        nums(left) = nums(right)
        nums(right) = tmp;
        
        left += 1;
        right -= 1;
    }
}
```

#### 1-1-6: [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)

#### 1-1-7: [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)


## 2) LC Example

### 2-1) Remove Element
```python
# python
# basic
class Solution(object):
    def removeElement(self, nums, val):
        length = 0
        for i in range(len(nums)):
            if nums[i] != val:
                nums[length] = nums[i]
                length += 1
        return length
```

```python
# LC 026 : Remove Duplicates from Sorted Array
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/remove-duplicates-from-sorted-array.py
# V0
# IDEA : 2 POINTERS: i, j
class Solution(object):
    def removeDuplicates(self, nums):
        # edge case
        if not nums:
            return
        i = 0
        for j in range(1, len(nums)):
            """
            NOTE !!!
             -> note this condition
             -> we HAVE to swap i+1, j once nums[i], nums[j] are different
             -> so we MAKE SURE there is no duplicate
            """
            if nums[j] != nums[i]:
                nums[i+1], nums[j] = nums[j], nums[i+1]
                i += 1

        #print ("nums = " + str(nums))
        return i+1

# V0'
# IDEA : 2 POINTERS
# HAVE A POINTER j STARTS FROM 0 AND THE OTHER POINTER i GO THROUGH nums
#  -> IF A[i] != A[j]
#     -> THEN SWITCH A[i] AND A[j+1]
#     -> and j += 1
# *** NOTE : it's swith A[j+1] (j+1) with A[i]
# DEMO 1 
# A = [1,1,1,2,3]
# s = Solution()
# s.removeDuplicates(A)
# [1, 1, 1, 2, 3]
# [1, 1, 1, 2, 3]
# [1, 1, 1, 2, 3]
# [1, 2, 1, 1, 3]
# [1, 2, 3, 1, 1]
#
# DEMO 2
# A = [1,2,2,3,4]
# s = Solution()
# s.removeDuplicates(A)
# A = [1, 2, 2, 3, 4]
# A = [1, 2, 2, 3, 4]
# A = [1, 2, 2, 3, 4]
# A = [1, 2, 2, 3, 4]
# A = [1, 2, 3, 2, 4]
# -> A = [1, 2, 3, 4, 2]
class Solution:
    def removeDuplicates(self, A):
        if len(A) == 0:
            return 0
        j = 0
        for i in range(0, len(A)):
            ###  NOTE : below condition
            if A[i] != A[j]:
                A[i], A[j+1] = A[j+1], A[i]
                j = j + 1
        return j+1
```

```python
# LC 283 move-zeroes
# V0
class Solution(object):
    def moveZeroes(self, nums):
        y = 0
        for x in range(len(nums)):
            if nums[x] != 0:
                nums[x], nums[y] = nums[y], nums[x]
                y += 1
        return nums

# V0'
class Solution(object):
    def moveZeroes(self, nums):
        # edge case
        if not nums:
            return
        j = 0
        for i in range(1, len(nums)):
            # if nums[j] = 0, swap with nums[i]
            if nums[j] == 0:
                if nums[i] != 0:
                    nums[j], nums[i] = nums[i], nums[j]
                    j += 1
            # if nums[j] != 0, then move j (j+=1) for searching next 0
            else:
                j += 1
        return nums
```

```python
# LC 080 : Remove Duplicates from Sorted Array II
# V0
# IDEA : 2 POINTERS
#### NOTE : THE nums already ordering
# DEMO
# example 1
# nums = [1,1,1,2,2,3]
#           i j
#           i   j
#        [1,1,2,1,2,3]
#             i   j
#        [1,1,2,2,1,3]
#               i   j
#
# example 2
# nums = [0,0,1,1,1,1,2,3,3] 
#           i j
#        [0,0,1,1,1,1,2,3,3]
#             i j
#        [0,0,1,1,1,1,2,3,3]
#               i j
#        [0,0,1,1,1,1,2,3,3]
#               i   j
#               i     j
#        [0,0,1,1,2,1,1,3,3]
#                 i     j  
#        [0,0,1,1,2,3,1,1,3]
#                   i     j
#        [0,0,1,1,2,3,3,1,1]
class Solution:
    def removeDuplicates(self, nums):
        if len(nums) < 3:
            return len(nums)

        ### NOTE : slow starts from 1
        slow = 1
        ### NOTE : fast starts from 2
        for fast in range(2, len(nums)):
            """
            NOTE : BELOW CONDITION

            1) nums[slow] != nums[fast]: for adding "1st" element
            2) nums[slow] != nums[slow-1] : for adding "2nd" element
            """
            if nums[slow] != nums[fast] or nums[slow] != nums[slow-1]:
                # both of below op are OK
                #nums[slow+1] = nums[fast]
                nums[slow+1], nums[fast] = nums[fast], nums[slow+1] 
                slow += 1
        return slow+1
```

### 2-2) Longest Palindromic Substring
```python
# LC 005 Longest Palindromic Substring
# V0
# IDEA : TWO POINTERS
# -> DEAL WITH odd, even len cases
#  -> step 1) for loop on idx 
#  -> step 2) and start from "center" 
#  -> step 3) and do a while loop
#  -> step 4) check if len of sub str > 1
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1025355/Easy-to-understand-solution-with-O(n2)-time-complexity
# Time complexity = best case O(n) to worse case O(n^2)
# Space complexity = O(1) if not considering the space complexity for result, as all the comparison happens in place.
class Solution:
    # The logic I have used is very simple, iterate over each character in the array and assming that its the center of a palindrome step in either direction to see how far you can go by keeping the property of palindrome true. The trick is that the palindrome can be of odd or even length and in each case the center will be different.
    # For odd length palindrome i am considering the index being iterating on is the center, thereby also catching the scenario of a palindrome with a length of 1.
    # For even length palindrome I am considering the index being iterating over and the next element on the left is the center.
    def longestPalindrome(self, s):

        if len(s) <= 1:
            return s

        res = []

        for idx in range(len(s)):
        
            """
            # CASE 1) : odd len
            # Check for odd length palindrome with idx at its center

            -> NOTE : the only difference (between odd, even len)
            
            -> NOTE !!!  : 2 idx : left = right = idx
            """
            left = right = idx
            # note the condition !!!
            while left >= 0 and right < len(s) and s[left] == s[right]:
                if right - left + 1 > len(res):
                    res = s[left:right + 1]
                left -= 1
                right += 1
              
            """"
            # CASE 2) : even len  
            # Check for even length palindrome with idx and idx-1 as its center

            -> NOTE : the only difference (between odd, even len)

            -> NOTE !!!  : 2 idx : left = idx - 1,  right = idx
            """
            left = idx - 1
            right = idx
            # note the condition !!!
            while left >= 0 and right < len(s) and s[left] == s[right]:
                if right - left + 1 > len(res):
                    res = s[left:right + 1]
                left -= 1
                right += 1

        return res

# V0'
# IDEA : TWO POINTER + RECURSION
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1057629/Python.-Super-simple-and-easy-understanding-solution.-O(n2).
class Solution:
    def longestPalindrome(self, s):
        res = ""
        length = len(s)
        def helper(left, right):
            while left >= 0 and right < length and s[left] == s[right]:
                left -= 1
                right += 1      
            return s[left + 1 : right]
        
        for index in range(len(s)):
            res = max(helper(index, index), helper(index, index + 1), res, key = len)           
        return res
```

### 2-3) Container With Most Water
```python
# LC 11 Container With Most Water
# V0 
# IDEA : TWO POINTERS 
class Solution(object):
    def maxArea(self, height):
        ans = 0
        l = 0
        r = len(height) - 1
        while l < r:
            ans = max(ans, min(height[l], height[r]) * (r - l))
            if height[l] < height[r]:
                l += 1
            else:
                r -= 1
        return ans
```

### 2-4) Longest Consecutive Sequence
```python
# LC 128 Longest Consecutive Sequence

# V0
# IDEA : sliding window
class Solution(object):
    def longestConsecutive(self, nums):
        # edge case
        if not nums:
            return 0
        nums = list(set(nums))
        # if len(nums) == 1: # not necessary
        #     return 1
        # sort first
        nums.sort()
        res = 0
        l = 0
        r = 1
        """
        NOTE !!!

        Sliding window here :
            condition :  l, r are still in list (r < len(nums) and l < len(nums))

            2 cases

                case 1) nums[r] != nums[r-1] + 1
                    -> means not continous, 
                        -> so we need to move r to right (1 idx)
                        -> and MOVE l to r - 1, since it's NOT possible to have any continous subarray within [l, r] anymore
                case 2) nums[r] == nums[r-1] + 1
                        -> means there is continous subarray currently, so we keep moving r to right (r+=1) and get current max sub array length (res = max(res, r-l+1))
        """
        while r < len(nums) and l < len(nums):
            # case 1)
            if nums[r] != nums[r-1] + 1:
                r += 1
                l = (r-1)
            # case 2)
            else:
                res = max(res, r-l+1)
                r += 1
        # edge case : if res == 0, means no continous array (with len > 1), so we return 1 (a single alphabet can be recognized as a "continous assay", and its len = 1)
        return res if res > 1 else 1

# V0'
# IDEA : SORTING + 2 POINTERS
class Solution(object):
    def longestConsecutive(self, nums):
        # edge case
        if not nums:
            return 0

        nums.sort()
        cur_len = 1
        max_len = 1
        #print ("nums = " + str(nums))

        # NOTE : start from idx = 1
        for i in range(1, len(nums)):
            ### NOTE : start from nums[i] != nums[i-1] case
            if nums[i] != nums[i-1]:
                ### NOTE : if nums[i] == nums[i-1]+1 : cur_len += 1
                if nums[i] == nums[i-1]+1:
                    cur_len += 1
                ### NOTE : if nums[i] != nums[i-1]+1 : get max len, and reset cur_lent as 1
                else:
                    max_len = max(max_len, cur_len)
                    cur_len = 1
        # check max len again
        return max(max_len, cur_len)
```

### 2-6) Palindromic Substrings
```python
# LC 647. Palindromic Substrings
# V0'
# IDEA : TWO POINTERS
# https://leetcode.com/problems/palindromic-substrings/discuss/1041760/Python-Easy-Solution-Beats-85
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/longest-palindromic-substring.py
class Solution:
    def countSubstrings(self, s):
        ans = 0    
        for i in range(len(s)):
            # odd
            ans += self.helper(s, i, i)
            # even
            ans += self.helper(s, i, i + 1)  
        return ans
        
    def helper(self, s, l, r):     
        ans = 0    
        while l >= 0 and r < len(s) and s[l] == s[r]:
            l -= 1
            r += 1
            ans += 1          
        return ans

# V0
# IDEA : BRUTE FORCE
class Solution(object):
    def countSubstrings(self, s):
        count = 0
        # NOTE: since i from 0 to len(s) - 1, so for j we need to "+1" then can get go throgh all elements in str
        for i in range(len(s)):
            # Note : for j we need to "+1"
            for j in range(i+1, len(s)+1):
                if s[i:j] == s[i:j][::-1]:
                    count += 1
        return count

# V0''
# IDEA : TWO POINTERS (similar as LC 005)
class Solution(object):
    def countSubstrings(self, s):
        count = 0
        for i in range(len(s)):
            # for every single character
            count += 1
            
            # case 1) palindromic substrings length is odd 
            left = i - 1
            right = i + 1
            while left >= 0 and right < len(s) and s[left] == s[right]:
                count += 1
                left -= 1
                right += 1

            # case 2) palindromic substrings length is even 
            left = i - 1
            right = i
            while left >= 0 and right < len(s) and s[left] == s[right]:
                count += 1
                left -= 1
                right += 1

        return count
```

### 2-7) Sum of Subarray Ranges
```python
# LC 2104. Sum of Subarray Ranges
# V0
# IDEA : BRUTE FORCE
class Solution:
    def subArrayRanges(self, nums):
        res = 0
        for i in range(len(nums)):
            curMin = float("inf")
            curMax = -float("inf")
            for j in range(i, len(nums)):
                curMin = min(curMin, nums[j])
                curMax = max(curMax, nums[j])
                res += curMax - curMin
        return res

# V0'
# IDEA : INCREASING STACK
class Solution:
    def subArrayRanges(self, A0):
        res = 0
        inf = float('inf')
        A = [-inf] + A0 + [-inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] > x:
                j = s.pop()
                k = s[-1]
                res -= A[j] * (i - j) * (j - k)
            s.append(i)
            
        A = [inf] + A0 + [inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] < x:
                j = s.pop()
                k = s[-1]
                res += A[j] * (i - j) * (j - k)
            s.append(i)
        return res
```

### 2-8) Trapping Rain Water
```python
# LC 42. Trapping Rain Water
# NOTE : there is also 2 scan, dp approaches
# V0'
# IDEA : TWO POINTERS 
# IDEA : CORE
#     -> step 1) use left_max, right_mex : record "highest" "wall" in left, right handside at current idx
#     -> step 2) 
#                case 2-1) if height[left] < height[right] : 
#                   -> all left passed idx's height is LOWER than height[right]
#                   -> so the "short" wall MUST on left
#                   -> and since we record left_max, so we can get trap amount based on left_max, height[left]
#                
#                case 2-2) if height[left] > height[right]
#                   -> .... (similar as above)
class Solution:
    def trap(self, height):
 
        if not height:
            return 0

        left_max = right_max = res = 0
        left, right = 0, len(height) - 1
 
        while left < right:
            if height[left] < height[right]:  # left pointer op
                if height[left] < left_max:
                    res += left_max - height[left]
                else:
                    left_max = height[left]
                left += 1  # move left pointer 
            else:
                if height[right] < right_max:  # right pointer op
                    res += right_max - height[right]
                else:
                    right_max = height[right]
                right -= 1  # move right pointer 
        return res
```

### 2-9) Next Permutation
```python
# LC 31. Next Permutation
# V0
class Solution:
    def nextPermutation(self, nums):
        i = j = len(nums)-1
        while i > 0 and nums[i-1] >= nums[i]:
            i -= 1
        if i == 0:   # nums are in descending order
            nums.reverse()
            return 
        k = i - 1    # find the last "ascending" position
        while nums[j] <= nums[k]:
            j -= 1
        nums[k], nums[j] = nums[j], nums[k]  
        l, r = k+1, len(nums)-1  # reverse the second part
        while l < r:
            nums[l], nums[r] = nums[r], nums[l]
            l +=1
            r -= 1

# V0'
class Solution(object):
    def nextPermutation(self, num):
        k, l = -1, 0
        for i in range(len(num) - 1):
            if num[i] < num[i + 1]:
                k = i

        if k == -1:
            num.reverse()
            return

        for i in range(k + 1, len(num)):
            if num[i] > num[k]:
                l = i
        num[k], num[l] = num[l], num[k]
        num[k + 1:] = num[:k:-1] ### dounle check here ###
```

### 2-10) Valid Palindrome II (Palindrome with One Deletion)

**Pattern: Two Pointers with Mismatch Handling**
- Check palindrome from both ends
- On first mismatch, try TWO possibilities:
  1. Skip left character (check `s[l+1...r]`)
  2. Skip right character (check `s[l...r-1]`)
- If EITHER works, return true
- Use helper function to check palindrome in range

**Key Insight:**
- Don't remove character and create new string (O(N) space)
- Instead, use pointers to check substring in-place (O(1) space)

```java
// java
// LC 680. Valid Palindrome II
/**
 * Pattern: Palindrome with at most 1 deletion allowed
 *
 * Example:
 *   s = "abca"
 *
 *   [a b c a]    l=0, r=3, s[l]=a, s[r]=a, match! l++, r--
 *    l     r
 *
 *   [a b c a]    l=1, r=2, s[l]=b, s[r]=c, MISMATCH!
 *      l r       Try: skip b (check "ca") OR skip c (check "ba")
 *                     "ca" is NOT palindrome
 *                     "ba" is NOT palindrome
 *                BUT we need to check full substring!
 *
 *   Actually for "abca":
 *   - Try skip l: check "cba" -> isPali("abca", 2, 3) = true (just "a")
 *   - OR skip r: check "aba" -> isPali("abca", 1, 2) = true (just "b")
 *
 *   Either works -> return true
 *
 * Time: O(N), Space: O(1)
 */
public boolean validPalindrome(String s) {
    int l = 0;
    int r = s.length() - 1;

    while (l < r) {
        if (s.charAt(l) != s.charAt(r)) {
            /** NOTE !!!
             *
             *  On mismatch, try BOTH possibilities:
             *    1. Skip left char  -> check s[l+1...r]
             *    2. Skip right char -> check s[l...r-1]
             *
             *  If EITHER is palindrome, we can make it work with 1 deletion
             */
            return isPalindrome(s, l + 1, r) || isPalindrome(s, l, r - 1);
        }
        l++;
        r--;
    }

    return true; // Already a perfect palindrome
}

/** NOTE !!!
 *
 *  Helper function with left, right pointers as parameters
 *  Checks if substring s[l...r] is palindrome
 *  NO new string created - check in place!
 */
private boolean isPalindrome(String s, int l, int r) {
    while (l < r) {
        if (s.charAt(l) != s.charAt(r)) {
            return false;
        }
        l++;
        r--;
    }
    return true;
}
```

```python
# python
# LC 680. Valid Palindrome II
class Solution:
    def validPalindrome(self, s):

        l, r = 0, len(s) - 1

        while l < r:
            if s[l] != s[r]:
                """
                # NOTE this !!!!
                -> On mismatch, try skipping left OR right character
                -> Check if either resulting substring is palindrome
                """
                skip_left = s[l+1:r+1]   # skip s[l]
                skip_right = s[l:r]      # skip s[r]
                # NOTE this !!!!
                return skip_left == skip_left[::-1] or skip_right == skip_right[::-1]
            else:
                l += 1
                r -= 1

        return True
```

**Common Mistakes:**
- ❌ Creating new strings (O(N) space and time)
- ❌ Only trying to skip one side
- ✅ Use helper with pointers (O(1) space)
- ✅ Try BOTH skip possibilities

**Similar Problems:**
- LC 680 Valid Palindrome II (this pattern)
- LC 125 Valid Palindrome
- LC 1216 Valid Palindrome III (k deletions allowed - DP)
- LC 234 Palindrome Linked List

### 2-11) Merge Sorted Array
```python
# LC 88. Merge Sorted Array
# V0
# IDEA : 2 pointers
### NOTE : we need to merge the sorted arrat to nums1 with IN PLACE (CAN'T USE EXTRA CACHE)
# -> SO WE START FROM RIGHT HAND SIDE (biggeest element) to LEFT HAND SIDE (smallest element)
# -> Then paste the remain elements
class Solution(object):
    def merge(self, nums1, m, nums2, n):
        ### NOTE : we define 2 pointers (p, q) here
        p, q = m-1, n-1
        ### NOTE : the while loop conditions
        while p >= 0 and q >= 0:
            if nums1[p] > nums2[q]:
                #***** NOTE : WE START FROM p+q+1 index, since that's the count of non-zero elements in nums1, and nums2
                nums1[p+q+1] = nums1[p]
                p = p-1
            else:
                ### NOTE WE START FROM p+q+1 index, reason same as above
                nums1[p+q+1] = nums2[q]
                q = q-1
        # if there're still elements in nums2, we just replace the ones in nums1[:q+1] with them (nums2[:q+1])
        nums1[:q+1] = nums2[:q+1]
```

### 2-12) Interval List Intersections

```java
// java
// LC 986
    public int[][] intervalIntersection_1(int[][] firstList, int[][] secondList) {
        if (firstList.length == 0 || secondList.length == 0)
            return new int[0][0];
    /**
     *  NOTE !!!!
     *   - i and j are pointers used to iterate through
     *      `firstList` and `secondList` respectively.
     *
     *   - `startMax` and `endMin` are used to compute
     *     the `intersection` of the current intervals
     *     from firstList and secondList.
     *
     *   - ans is a list to store the resulting intersection intervals.
     */
        int i = 0;
        int j = 0;
        int startMax = 0, endMin = 0;
        List<int[]> ans = new ArrayList<>();

    /**
     *
     *   - The loop continues as long as
     *      there are intervals remaining in `BOTH lists`.
     *
     *   - `startMax` is the maximum of the `START points` of the two
     *     intervals (firstList[i] and secondList[j]).
     *       -> This ensures the intersection starts no earlier than both intervals.
     *
     *   - `endMin` is the minimum of the `END points` of the two intervals.
     *
     *   - This ensures the intersection ends no later than the earlier of
     *     the two intervals.
     *
     */
    while (i < firstList.length && j < secondList.length) {
      startMax = Math.max(firstList[i][0], secondList[j][0]);
      endMin = Math.min(firstList[i][1], secondList[j][1]);

      // you have end greater than start and you already know that this interval is
      // surrounded with startMin and endMax so this must be the intersection
      /**
       *
       *  - If endMin >= startMax, it means there is an intersection between the two intervals.
       *    ->  Add the intersection [startMax, endMin] to the result list.
       */
      if (endMin >= startMax) {
        ans.add(new int[] {startMax, endMin});
      }

      // the interval with min end has been covered completely and have no chance to
      // intersect with any other interval so move that list's pointer
      /**
       * - Since the intervals are sorted and disjoint:
       *    - If the interval from firstList ends first (or at the same time), increment i.
       *    - If the interval from secondList ends first (or at the same time), increment j.
       *    -> This ensures that the interval which has been fully processed is skipped, moving to the next potential candidate for intersection.
       *
       */
      if (endMin == firstList[i][1]) i++;
      if (endMin == secondList[j][1]) j++;
        }

        return ans.toArray(new int[ans.size()][2]);
    }
```

### 2-13) Sort Colors (Dutch National Flag)

**Pattern: Three-Way Partitioning with Two Pointers**
- Use three pointers: left (0s), mid (current), right (2s)
- Partition array into three sections
- Single pass solution

```java
// java
// LC 75. Sort Colors
/**
 * Pattern: Dutch National Flag - Three-way partitioning
 *
 * Goal: Sort array with only 0, 1, 2 in one pass
 *
 * Pointers:
 *   - left: boundary for 0s (everything before left is 0)
 *   - mid: current element being examined
 *   - right: boundary for 2s (everything after right is 2)
 *
 * Example:
 *   nums = [2,0,2,1,1,0]
 *
 *   [2,0,2,1,1,0]    mid=0, nums[mid]=2, swap with right, right--
 *    l           r   [0,0,2,1,1,2]
 *    m
 *
 *   [0,0,2,1,1,2]    mid=0, nums[mid]=0, swap with left, left++, mid++
 *    l         r
 *    m
 *
 *   [0,0,2,1,1,2]    mid=1, nums[mid]=0, swap with left, left++, mid++
 *      l       r
 *      m
 *
 *   [0,0,2,1,1,2]    mid=2, nums[mid]=2, swap with right, right--
 *        l     r
 *        m
 *
 *   [0,0,1,1,2,2]    mid=2, nums[mid]=1, mid++
 *        l   r
 *        m
 *
 *   [0,0,1,1,2,2]    mid=3, nums[mid]=1, mid++
 *        l r
 *          m
 *
 *   mid > right, done!
 *
 * Time: O(N), Space: O(1)
 */
public void sortColors(int[] nums) {
    int left = 0;           // Next position for 0
    int mid = 0;            // Current examining position
    int right = nums.length - 1;  // Next position for 2

    while (mid <= right) {
        if (nums[mid] == 0) {
            // Found 0, swap to left
            swap(nums, left, mid);
            left++;
            mid++;
        } else if (nums[mid] == 2) {
            // Found 2, swap to right
            // NOTE: Don't increment mid yet, need to check swapped element
            swap(nums, mid, right);
            right--;
        } else {
            // Found 1, just move mid
            mid++;
        }
    }
}

private void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

**Similar Problems:**
- LC 75 Sort Colors (this pattern)
- LC 26 Remove Duplicates from Sorted Array
- LC 80 Remove Duplicates from Sorted Array II
- LC 283 Move Zeroes

### 2-14) 3Sum

**Pattern: Two Pointers with Fixed First Element**
- Fix first element, use two pointers for remaining two
- Avoid duplicates by skipping same values
- Sort array first

```java
// java
// LC 15. 3Sum
/**
 * Pattern: Fixed element + Two pointers
 *
 * Steps:
 *   1. Sort array
 *   2. Fix first element (i)
 *   3. Use two pointers (l, r) to find remaining two elements
 *   4. Skip duplicates
 *
 * Example:
 *   nums = [-1,0,1,2,-1,-4]
 *   After sort: [-4,-1,-1,0,1,2]
 *
 *   i=0, nums[i]=-4, l=1, r=5
 *   [-4,-1,-1,0,1,2]
 *     i  l       r    sum=-4+-1+2=-3 < 0, l++
 *
 *   i=1, nums[i]=-1, l=2, r=5
 *   [-4,-1,-1,0,1,2]
 *        i  l     r   sum=-1+-1+2=0, found! [-1,-1,2]
 *                     l++, r--, skip duplicates
 *
 *   [-4,-1,-1,0,1,2]
 *        i    l r     sum=-1+0+1=0, found! [-1,0,1]
 *
 * Time: O(N^2), Space: O(1) excluding result
 */
public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(nums);

    for (int i = 0; i < nums.length - 2; i++) {
        // Skip duplicates for first element
        if (i > 0 && nums[i] == nums[i - 1]) {
            continue;
        }

        int left = i + 1;
        int right = nums.length - 1;
        int target = -nums[i];

        while (left < right) {
            int sum = nums[left] + nums[right];

            if (sum == target) {
                result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                // Skip duplicates for second element
                while (left < right && nums[left] == nums[left + 1]) {
                    left++;
                }
                // Skip duplicates for third element
                while (left < right && nums[right] == nums[right - 1]) {
                    right--;
                }

                left++;
                right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
    }

    return result;
}
```

**Similar Problems:**
- LC 15 3Sum (this pattern)
- LC 16 3Sum Closest
- LC 18 4Sum
- LC 259 3Sum Smaller
- LC 1 Two Sum

### 2-15) Reverse String / Reverse Words

**Pattern: In-place Reversal with Two Pointers**

```java
// java
// LC 344. Reverse String
/**
 * Pattern: Swap from both ends moving toward center
 *
 * Example:
 *   s = ['h','e','l','l','o']
 *
 *   ['h','e','l','l','o']
 *     l           r       swap, l++, r--
 *
 *   ['o','e','l','l','h']
 *       l       r         swap, l++, r--
 *
 *   ['o','l','l','e','h']
 *           l r           l >= r, done!
 *
 * Time: O(N), Space: O(1)
 */
public void reverseString(char[] s) {
    int left = 0;
    int right = s.length - 1;

    while (left < right) {
        char temp = s[left];
        s[left] = s[right];
        s[right] = temp;
        left++;
        right--;
    }
}
```

**Similar Problems:**
- LC 344 Reverse String
- LC 345 Reverse Vowels of a String
- LC 541 Reverse String II
- LC 186 Reverse Words in a String II
- LC 151 Reverse Words in a String

## 3) Classic LC Problems Summary

### Easy:
- LC 26 Remove Duplicates from Sorted Array
- LC 27 Remove Element
- LC 125 Valid Palindrome
- LC 283 Move Zeroes
- LC 344 Reverse String
- LC 345 Reverse Vowels of a String
- LC 349 Intersection of Two Arrays
- LC 350 Intersection of Two Arrays II
- LC 392 Is Subsequence
- LC 680 Valid Palindrome II
- LC 844 Backspace String Compare
- LC 977 Squares of a Sorted Array

### Medium:
- LC 3 Longest Substring Without Repeating Characters (Sliding Window)
- LC 5 Longest Palindromic Substring
- LC 11 Container With Most Water
- LC 15 3Sum
- LC 16 3Sum Closest
- LC 18 4Sum
- LC 75 Sort Colors (Dutch National Flag)
- LC 80 Remove Duplicates from Sorted Array II
- LC 86 Partition List
- LC 88 Merge Sorted Array
- LC 142 Linked List Cycle II
- LC 167 Two Sum II - Input Array Is Sorted
- LC 209 Minimum Size Subarray Sum (Sliding Window)
- LC 287 Find the Duplicate Number
- LC 567 Permutation in String (Sliding Window)
- LC 647 Palindromic Substrings
- LC 713 Subarray Product Less Than K
- LC 881 Boats to Save People
- LC 986 Interval List Intersections

### Hard:
- LC 42 Trapping Rain Water
- LC 76 Minimum Window Substring (Sliding Window)
- LC 828 Count Unique Characters of All Substrings

## 4) Two Pointers Cheat Sheet

| Pattern | When to Use | Example Problems |
|---------|-------------|------------------|
| **Opposite Direction** | Sorted array, palindrome check | LC 167, LC 344, LC 125 |
| **Same Direction (Fast-Slow)** | Remove duplicates, cycle detection | LC 26, LC 27, LC 142 |
| **Sliding Window** | Subarray/substring problems | LC 3, LC 76, LC 209 |
| **Merge Two Lists** | Merge sorted arrays/lists | LC 88, LC 21 |
| **Partition** | Rearrange elements | LC 75, LC 86 |
| **Palindrome with Deletion** | Allow k changes | LC 680, LC 1216 |
| **Fixed + Two Pointers** | Sum problems (3Sum, 4Sum) | LC 15, LC 16, LC 18 |
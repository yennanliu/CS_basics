# Two pointers

- Ref
    - [fucking-algorithm : 2 pointers](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E4%BA%8C%E5%88%86%E6%9F%A5%E6%89%BE%E8%AF%A6%E8%A7%A3.md)

## 0) Concept  

### 0-1) Types

- Pointer types
    - `Fast - Slow pointers`
        - fast, slow pointers from `same start point`
        - Usualy set
            - slow pointer moves 1 idx
            - fast pointer moves 2 idx
        - linked list
            - find mid point of linked list
            - check if linked list is circular
                - LC 141
                - LC 142
            - if a circular linked list, return beginning point of circular
            - find last k elements of a single linked list
    - `Left- Right pointers`
        - left, right pointers from `idx = 0, idx = len(n) - 1` respectively
        - Usually set
            - left pointer = 0
            - right pointer = len(nums)
        - [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
        - array reverse
        - [2 sum](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
        - [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)

- Algorithm
    - binary search
    - sliding window
    - for loop + "expand `left`, `right` from center"

- Data structure
    - Array
    - Linked list

### 0-2) Pattern

#### 0-2-1) for loop + "expand `left`, `right` from center"
```python
# LC 005  Longest Palindromic Substring
# python
# pseudo code
# ...
for i in range(lens(s)):
    
    # if odd
    left = right = i
    while left >= 0 and right < len(s) and s[left] == s[right]:
        if right+1-left > len(res):
            res = s[left:right+1]
    left -= 1
    right += 1
    
    # if even
    left = i - 1
    right = i
    while left >= 0 and right < len(s) and s[left] == s[right]:
        if right+1-left > len(res):
            res = s[left:right+1]
    left -= 1
    right += 1
# ...
```

## 1) General form

### 1-1) Basic OP

#### 1-1-1 : Check if there is a circular linked list 
```java
// java
boolean hasCycle(ListNode head){
    fast = slow = head;
    // NOTE : while loop condition
    while (fast != null and fast.next != null){
        /** NOTE : need to do move slow, fast pointer then compare them */
        slow = slow.next;
        fast = fast.next.next;
        if (fast == slow){
            return True
        }
    }
    return False;
}
```

#### 1-1-2 : return the "ring start point" of circular linked list 
```java
// java
ListNode detectCycle(ListNode head){
    ListNode fast, slow;
    fast = slow = head;
    while (fast != null and fast.next != null){
        fast = fast.next.next;
        slow = slow.next;
        if (fast == slow){
            break;
        }
    }
    slow = head;
    // may need below logic to check whether is cycle linked list or not
    // if (! fast or ! fast.next){
    //     return null;
    // }
    while (slow != fast){
        slow = slow.next;
        fast = fast.next;
    }
    return slow;
}
```

```python
# LC 142. Linked List Cycle II
# python
class Solution:
    def detectCycle(self, head):
        if not head or not head.next:
            return
        slow = fast = head
        while fast and fast.next:
            fast = fast.next.next
            slow = slow.next
            if fast == slow:
                break
        #print ("slow = " + str(slow) + " fast = " + str(fast))
        ### NOTE : via below condition check if is a cycle linked list
        if not fast or not fast.next:
            return
        """
        ### NOTE : re-init slow or fast as head (from starting point)
        -> can init slow or head
        """
        slow = head
        #fast = head 
        """
        ### NOTE : check while slow != fast
        ### NOTE : use the same speed
        """
        while slow != fast:
            # NOTE this !!! : fast, slow move SAME speed (in this step)
            fast = fast.next
            slow = slow.next
        return slow

# V0'
# IDEA : SET
class Solution(object):
    def detectCycle(self, head):
        if not head or not head.next:
            return
        s = set()
        while head:
            s.add(head)
            head = head.next
            if head in s:
                return head
        return
```
#### 1-1-3 : find mid point of a single linked list
```java
// java
while (fast != null and fast.next != null){
    fast = fast.next.next;
    slow = slow.next;
}
return slow;
```

#### 1-1-4 : find last k elements in a single linked list
```java
// java
ListNode fast, slow;
slow = fast = head;
while (k > 0){
    fast = fast.next;
    k -= 1;
}
while (fast != null){
    fast = fast.next;
    slow = slow.next;
}
return slow;
```

#### 1-1-5 : Reverse Array
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
#             i     j
#        [1,1,2,2,1,3]
#               i   j       
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
# LC 5. Longest Palindromic Substring
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
        
            # CASE 1) : odd len
            # Check for odd length palindrome with idx at its center
            ### NOTE : the only difference (between odd, even len)
            left = right = idx
            while left >= 0 and right < len(s) and s[left] == s[right]:
                if right - left + 1 > len(res):
                    res = s[left:right + 1]
                left -= 1
                right += 1
              
            # CASE 2) : even len  
            # Check for even length palindrome with idx and idx-1 as its center
            ### NOTE : the only difference (between odd, even len)
            left = idx - 1
            right = idx
            while left >= 0 and right < len(s) and s[left] == s[right]:
                if right - left + 1 > len(res):
                    res = s[left:right + 1]
                left -= 1
                right += 1

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


### 2-5) Missing Ranges
```python
# LC 163. Missing Ranges
# V0
# IDEA : 2 POINTERS
class Solution(object):
    def findMissingRanges(self, nums, lower, upper):
        l, r = lower, lower
        res = []
        for i in range(len(nums)):
            # if NO missing interval
            if nums[i] == r:  
                l, r = nums[i] + 1, nums[i] + 1
                
            # if missing interval 
            elif nums[i] > r:
                r = max(r, nums[i] - 1)
                
                if r != l:
                    res.append(str(l) + "->" + str(r))
                else:
                    res.append(str(l))
                    
                l, r = nums[i] + 1, nums[i] + 1
        
        # deal with remaining part 
        if l < upper:
            res.append(str(l) + "->" + str(upper))
        elif l == upper:
            res.append(str(l))
            
        return res
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
```
# Two pointer
- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E4%BA%8C%E5%88%86%E6%9F%A5%E6%89%BE%E8%AF%A6%E8%A7%A3.md

## 0) Concept  

### 0-1) Types

- Pointer types
    - `Fast - Slow pointers`
        - Usualy set
            - slow pointer moves 1 idx
            - fast pointer moves 2 inx
        - find mid point of linked list
        - linked list check if circular 
        - if a circular linked list, return beginning point of circular
        - find last k element of a single linked list
    - `Left- Right pointers`
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
    ListNode left, right;
    fast = slow = head;
    while (fast != null and fast.next != null){
        /** NOTE : need to do move slow, fast pointer then compare them*/
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
# LC 26 : Remove Duplicates from Sorted Array
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/remove-duplicates-from-sorted-array.py
# IDEA : 2 POINTERS
# HAVE A POINTER j STARTS FROM 0 AND THE OTHER POINTER i GO THROUGH nums
#  -> IF A[i] != A[j]
#     -> THEN SWITCH A[i] AND A[j+1]
#     -> and j += 1
# *** NOTE : it's swith A[j+1] (j+1) with A[i]
class Solution:
    def removeDuplicates(self, A):
        if len(A) == 0:
            return 0
        j = 0
        for i in range(0, len(A)):
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
```

```python
# LC 80 : Remove Duplicates from Sorted Array II
# V0
# IDEA : 2 POINTERS
#### NOTE : THE nums already ordering
class Solution:
    def removeDuplicates(self, nums):
        if len(nums) < 3:
            return len(nums)

        ### NOTE : slow starts from 1
        slow = 1
        ### NOTE : fast starts from 2
        for fast in range(2, len(nums)):
            ### NOTE : BELOW CONDITION
            if nums[slow] != nums[fast] or nums[slow] != nums[slow-1]:
                nums[slow+1] = nums[fast]
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
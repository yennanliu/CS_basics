# Two pointer
- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E4%BA%8C%E5%88%86%E6%9F%A5%E6%89%BE%E8%AF%A6%E8%A7%A3.md

## 0) Concept  

### 0-1) Types
- Fast - Slow pointers
    - Usualy set
        - slow pointer moves 1 idx
        - fast pointer moves 2 inx
    - find mid point of linked list
    - linked list check if circular 
    - if a circular linked list, return beginning point of circular
    - find last k element of a single linked list
- Left- Right pointers
    - Usually set
        - left pointer = 0
        - right pointer = len(nums)
    - [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
    - array reverse
    - [2 sum](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
    - [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)

### 0-2) Pattern
```c++
int binarySearch(int[] nums, int target) {
    int left = 0, right = ...;

    while(...) {
        int mid = left + (right - left) / 2;
        if (nums[mid] == target) {
            ...
        } else if (nums[mid] < target) {
            left = ...
        } else if (nums[mid] > target) {
            right = ...
        }
    }
    return ...;
}
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
    while (slow != head){
        slow = slow.next;
        fast = fast.next;
    }
    return slow;
}

```

#### 1-1-3 : find mid point of single linked list
```java
// java
while (fast != null and fast.next != null){
    fast = fast.next.next;
    slow = slow.next;
}
return slow;
```

#### 1-1-4 : find last k element in single linked list
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
    def removeDuplicates(self, nums: List[int]) -> int:
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
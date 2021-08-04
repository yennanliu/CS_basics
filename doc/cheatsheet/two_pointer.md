# Two pointer
- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E4%BA%8C%E5%88%86%E6%9F%A5%E6%89%BE%E8%AF%A6%E8%A7%A3.md

## 0) Concept  

### 0-1) Framework

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

## 2) LC Example

### 2-1) Remove Element
```python
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
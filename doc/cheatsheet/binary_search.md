# Binary Search

## 0) Concept  

### 0-1) Types

- Types
    - find `left` boundary
        - LC 367 Valid Perfect Square 
    - find `right` boundary
    - `recursive` binary search

- Algorithm
    - binary search
    - sliding window
    - recursive

- Data structure
    - Array
    - Linked list
    - dict

### 0-2) Pattern

#### 0-2-0) Binary search
```python
# python
# basic
def binary_search(nums, target):
    l, r = 0, len(nums) - 1
    ### NOTE : WE ALWALYS USE CLOSED boundary (for logic unifying)
    #         -> e.g. while l <= r
    #         -> [l, r]  
    while l <= r:
        mid = l + (r-l)//2
        if nums[mid] == target:
            return mid 
        elif nums[mid] < target:
            l = mid+1
        else:
            r = mid-1 
    return -1
```

```c++
// c++
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

#### 0-2-1) Binary search on `LEFT` boundary
```java
// java
int left_bound(int[] nums, int target){
    int left = 0;
    int right = nums.length - 1;
    // NOTE : WE ALWALYS USE CLOSED boundary (for logic unifying)
    //       -> e.g. while l <= r
    //        -> [l, r]  
    while (left <= right){
        int mid = left + (right - mid) / 2;
        if (num[mid] < target){
            left = mid + 1;
        }else if (nums[mid] > target){
            right = mid - 1;
        }else if (nums[mid] == target){
            // DO NOT RETURN !!!, BUT REDUCE RIGHT BOUNDARY FOR FOCUSING ON LEFT BOUNDARY
            right = mid - 1;
        }
    }
    // finally check if it will be OUT OF LEFT boundary
    if (left >= nums.length || nums[left] != target){
        return -1;
    return left;
    }
}
```

#### 0-2-2) Binary search on `RIGHT` boundary
```java
// java
int right_bound(int[] nums, int target){
    int left = 0;
    int right = nums.length - 1;
    // NOTE : WE ALWALYS USE CLOSED boundary (for logic unifying)
    //       -> e.g. while l <= r
    //        -> [l, r]  
    while (left <= right){
        int mid = left + (right - mid) / 2;
        if (num[mid] < target){
            left = mid + 1;
        }else if (nums[mid] > target){
            right = mid - 1;
        }else if (nums[mid] == target){
            // DO NOT RETURN !!!, BUT REDUCE LEFT BOUNDARY FOR FOCUSING ON RIGHT BOUNDARY
            left = mid + 1;
        }
    }
    // finally check if it will be OUT OF RIGHT boundary
    if (right < 0 || nums[right] != target){
        return -1;
    return right;
    }
}
```

## 1) General form

## 2) LC Example

### 2-1) For loop + binary search
```python
# 167 Two Sum II - Input array is sorted
class Solution(object):
    def twoSum(self, numbers, target):
        for i in range(len(numbers)):
            l, r = i+1, len(numbers)-1
            tmp = target - numbers[i]
            while l <= r:
                mid = l + (r-l)//2
                if numbers[mid] == tmp:
                    return [i+1, mid+1]
                elif numbers[mid] < tmp:
                    l = mid+1
                else:
                    r = mid-1
```

### 2-2) Find Peak Element
```python
# LC 162 Find Peak Element
# V0'
# IDEA : RECURSIVE BINARY SEARCH
class Solution(object):
    def findPeakElement(self, nums):

        def help(nums, l, r):
            if l == r:
                return l
            mid = l + (r - l) // 2
            if (nums[mid] > nums[mid+1]):
                return help(nums, l, mid)
            return help(nums, mid+1, r)
            
        return help(nums, 0, len(nums)-1)
```

### 2-3) Valid Perfect Square
```python
# 367 Valid Perfect Square, LC 69 Sqrt(x)
# V0'
# IDEA : BINARY SEARCH
class Solution(object):
    def isPerfectSquare(self, num):
        left, right = 0, num
        while left <= right:
            ### NOTE : there is NO mid * mid == num condition
            mid = (left + right) / 2
            if mid * mid >= num:
                right = mid - 1
            else:
                left = mid + 1
        ### NOTE this
        return left * left == num
```

### 2-4) Minimum Size Subarray Sum
```python
# LC 209 Minimum Size Subarray Sum
### NOTE : there is also sliding window approach
# V1' 
# http://bookshadow.com/weblog/2015/05/12/leetcode-minimum-size-subarray-sum/
# IDEA : BINARY SEARCH 
class Solution:
    def minSubArrayLen(self, s, nums):
        size = len(nums)
        left, right = 0, size
        bestAns = 0
        while left <= right:
            mid = (left + right) / 2
            if self.solve(mid, s, nums):
                bestAns = mid
                right = mid - 1
            else:
                left = mid + 1
        return bestAns

    def solve(self, l, s, nums):
        sums = 0
        for x in range(len(nums)):
            sums += nums[x]
            if x >= l:
                sums -= nums[x - l]
            if sums >= s:
                return True
        return False
```
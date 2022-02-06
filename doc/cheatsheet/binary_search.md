# Binary Search
- Find a value (k) from a `sorted search space` with two pointers
- Steps
    - step 1) Define boundary variables (e.g. left, right) that can `include all possible cases`
    - step 2) Define returned values
    - step 3) Define exit condition
- Scenario
    - find a value from sorted array
    -  if there is kind of monotonicity, for example, if condition(k) is True then condition(k + 1) is True, then we can consider binary search


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
 - https://leetcode.com/problems/binary-search/discuss/423162/Binary-Search-101-The-Ultimate-Binary-Search-Handbook
- https://leetcode.com/discuss/general-discussion/786126/python-powerful-ultimate-binary-search-template-solved-many-problems : TODO : add above to cheatsheet
   
#### 0-2-0) Binary search
```python
# python
# basic
def binary_search(nums, target):
    l = 0
    r = len(nums) - 1
    """
    NOTE : WE ALWALYS USE CLOSED boundary (for logic unifying)
        -> e.g. while l <= r
        -> [l, r] 
    """ 
    while l <= r:
        mid = l + (r-l)//2
        if nums[mid] == target:
            return mid 
        elif nums[mid] < target:
            ### NOTE this
            l = mid+1
        else:
            ### NOTE this
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
```python
# python
def binary_search_left_boundary(nums, target):
    l = 0
    r = len(nums) - 1
    """
    NOTE : WE ALWALYS USE CLOSED boundary (for logic unifying)
        -> e.g. while l <= r
        -> [l, r] 
    """ 
    while l <= r:
        mid = l + (r-l)//2
        # DO NOT RETURN !!!, BUT REDUCE RIGHT BOUNDARY FOR FOCUSING ON LEFT BOUNDARY
        if nums[mid] == target:
            r = mid - 1 
        elif nums[mid] < target:
            ### NOTE this
            l = mid + 1
        else:
            ### NOTE this
            r = mid - 1
    # finally check if it will be OUT OF LEFT boundary
    if l >= len(nums) or nums[l] != target:
        return - 1
    return l
```

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
```python
# python
def binary_search_right_boundary(nums, target):
    l = 0
    r = len(nums) - 1
    """
    NOTE : WE ALWALYS USE CLOSED boundary (for logic unifying)
        -> e.g. while l <= r
        -> [l, r] 
    """ 
    while l <= r:
        mid = l + (r-l)//2
        # DO NOT RETURN !!!, BUT REDUCE LEFT BOUNDARY FOR FOCUSING ON RIGHT BOUNDARY
        if nums[mid] == target:
            l = mid + 1 
        elif nums[mid] < target:
            ### NOTE this
            l = mid + 1
        else:
            ### NOTE this
            r = mid - 1
    # finally check if it will be OUT OF RIGHT boundary
    if r < 0 or nums[r] != target:
        return - 1
    return r
```

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

### 2-1) Two Sum II - Input array is sorted
- (For loop binary search)
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
-  (recursive binary search)
```python
# LC 162 Find Peak Element, LC 852 Peak Index in a Mountain Array
# V0'
# IDEA : RECURSIVE BINARY SEARCH
class Solution(object):
    def findPeakElement(self, nums):

        def help(nums, l, r):
            if l == r:
                return l
            mid = l + (r - l) // 2
            if (nums[mid] > nums[mid+1]):
                return help(nums, l, mid) # r = mid
            return help(nums, mid+1, r) # l = mid + 1
            
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

### 2-5) First Bad Version
```python
# LC 278
# V0
# IDEA : binary search
class Solution(object):
    def firstBadVersion(self, n):
        left = 1 
        right = n
        while right > left + 1:
            mid = (left + right)//2
            if SVNRepo.isBadVersion(mid):
                end = mid 
            else:
                left = mid 
        if SVNRepo.isBadVersion(left):
            return left
        return right 
```

### 2-6) Search Insert Position
```python
# LC 035 Search Insert Position
# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/70738108
class Solution(object):
    def searchInsert(self, nums, target):
        N = len(nums)
        left, right = 0, N #[left, right)
        while left < right:
            mid = left + (right - left) / 2
            if nums[mid] == target:
                return mid
            elif nums[mid] > target:
                right = mid
            else:
                left = mid + 1
        return left
```

### 2-7) Capacity To Ship Packages Within D Days
```python
# LC 1011
# V1
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/discuss/390359/Simple-Python-Binary-Search
class Solution(object):
     def shipWithinDays(self, weights, D):
            def cannot_split(weights, D, max_wgt):
                s = 0
                days = 1
                for w in weights:
                    s += w
                    if s > max_wgt:
                        s = w
                        days += 1
                return days > D

            low, high = max(weights), sum(weights)
            while low < high:
                mid = low + (high - low) // 2
                if cannot_split(weights, D, mid):
                    low = mid + 1
                else:
                    high = mid
            return low
```

### 2-8) Split Array Largest Sum (Hard)
```python
# LC 410 Split Array Largest Sum [Hard]
```

### 2-9) Koko Eating Bananas
```python
# LC 875. Koko Eating Bananas
# V1
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/koko-eating-bananas/solution/
class Solution:
    def minEatingSpeed(self, piles, h):  
        # Initalize the left and right boundaries     
        left = 1
        right = max(piles)
        
        while left < right:
            # Get the middle index between left and right boundary indexes.
            # hour_spent stands for the total hour Koko spends.
            middle = (left + right) // 2            
            hour_spent = 0
            
            # Iterate over the piles and calculate hour_spent.
            # We increase the hour_spent by ceil(pile / middle)
            for pile in piles:
                # python ceil : https://www.runoob.com/python/func-number-ceil.html
                hour_spent += math.ceil(pile / middle)
            
            # Check if middle is a workable speed, and cut the search space by half.
            if hour_spent <= h:
                right = middle
            else:
                left = middle + 1
        
        # Once the left and right boundaries coincide, we find the target value,
        # that is, the minimum workable eating speed.
        return right

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82716042
# IDEA : BINARY SEARCH 
class Solution:
    def minEatingSpeed(self, piles, H):
        minSpeed, maxSpeed = 1, max(piles)
        while minSpeed <= maxSpeed:
            speed = minSpeed + (maxSpeed - minSpeed) // 2
            hour = 0
            for pile in piles:
                hour += math.ceil(pile / speed)
            if hour <= H:
                maxSpeed = speed - 1
            else:
                minSpeed = speed + 1
        return minSpeed
```

### 2-10) Find K Closest Elements
```python
# LC 658. Find K Closest Elements
# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82968136
# IDEA : TWO POINTERS 
class Solution(object):
    def findClosestElements(self, arr, k, x):
        # since the array already sorted, arr[-1] must be the biggest one,
        # while arr[0] is the smallest one
        # so if the distance within arr[-1],  x >  arr[0],  x
        # then remove the arr[-1] since we want to keep k elements with smaller distance,
        # and vice versa (remove arr[0]) 
        while len(arr) > k:
            if x - arr[0] <= arr[-1] - x:
                arr.pop()
            else:
                arr.pop(0)
        return arr
```

### 2-11) Sqrt(x)
```python
# LC 069 Sqrt(x)
# V0
# IDEA : binary search
class Solution(object):
    def mySqrt(self, x):
        # edge case
        if not x or x <= 0:
            return 0
        if x == 1:
            return 1
        l = 0
        r = x-1
        while r >= l:
            mid = l + (r-l)//2
            #print ("l = " + str(l) + " r = " + str(r) + " mid = " + str(mid))
            sq = mid** 2
            if sq == x:
                return mid
            elif sq < x:
                if (mid+1)**2 > x:
                    return mid
                l = mid + 1
            else:
                r = mid - 1

# V0
# IDEA : binary search
class Solution(object):
    def mySqrt(self, num):
        if num <= 1:
            return num
        l = 0
        r = num - 1
        while r >= l:
            mid = l + (r - l) // 2
            if mid * mid == num:
                return mid
            elif mid * mid > num:
                r = mid - 1
            else:
                l = mid + 1
        return l if l * l < num else l - 1
```
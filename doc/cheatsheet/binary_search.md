# Binary Search

- Find a value (k) from a `sorted search space` with two pointers
    - Not really NEED to "all sorted", `partial sorted`, `rotated sorted` is possible as well
- Steps
    - step 1) Define boundary variables (e.g. left, right) that can `include all possible cases`
    - step 2) Define returned values
    - step 3) Define exit condition
- Scenario
    - find a value from sorted array
    -  if there is kind of monotonicity, for example, if condition(k) is True then condition(k + 1) is True, then we can consider binary search

- Ref
    - basic:
        - https://labuladong.online/algo/essential-technique/binary-search-framework/
    - problems:
        - https://labuladong.online/algo/frequency-interview/binary-search-in-action/
        - https://labuladong.online/algo/problem-set/binary-search/
    - bisect : check [python_trick.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md) : can get idx, and keep array sorted, when there is a element inserted


## 0) Concept  

### 0-1) Types

- Types

    - Basic binary search

    - Search in `rotate` array
        - Find `Minimum` in Rotated Sorted Array
            - LC 153
            ```java
            // java

            // ...
            while(r >= l){
            int mid = (l + r) / 2;
            // case 1) if `left subarray + mid` is ascending
            if(nums[mid] >= nums[l]){
                l = mid + 1;
            }
            // case 2) if `right subarray + mid` is ascending
            else{
                r = mid - 1;
            }
            // ...
            ```
        - Search in Rotated Sorted Array
            - LC 33, LC 81
            ```java
            // java
            // ...
            while (r >= l){
                int mid = (l + r) / 2;
                int cur = nums[mid];
                if (cur == target){
                    return mid;
                }
                // Case 1: `left + mid` is in ascending order
                /** NOTE !!! we compare mid with left, instead of 0 idx element */
                else if (nums[mid] >= nums[l]) {
                    /** NOTE !!!
                     *
                     *   1) ">="
                     *   2)  since `if nums[mid] == target`, we should already FOUND a solution
                     *       within code above, so here, we use target < nums[mid],
                     *       but for left boundary, we use ">=". e.g. target >= nums[l]
                     */
                    // case 1-1)  target < mid && target > l
                    if (target >= nums[l] && target < nums[mid]) {
                        r = mid - 1;
                    }
                    // case 2-2)
                    else {
                        l = mid + 1;
                    }
                }

                // Case 2:  `right + mid` is in ascending order
                else {
                    /** NOTE !!!
                     *
                     *   1) "<="
                     *   2)  since `if nums[mid] == target`, we should already FOUND a solution
                     *       within code above, so here, we use target > nums[mid],
                     *       but for right boundary, we use "<=". e.g. target <= nums[r]
                     */
                    // case 2-1)  target > min && target <= r
                    if (target <= nums[r] && target > nums[mid]) {
                        l = mid + 1;
                    }
                    // case 2-2)
                    else {
                        r = mid - 1;
                    }
                }

             }

        // ...
        ```
        - Difference:
            - LC 153 is finding `min`, so ONLY `1` if-else is enough
            - LC 33, 81 is finding `target`, so need `2` if-else logic

    - `Recursive` binary search
        - Garena/test1.py

    - Search in 2D array
        - LC 74
        - `flatten` array with `x % y = z` handling, or visit row by row via binary search

    - Find min in Rotation array
        - LC 153
            - check if `mid` point is at `left` or `right` sub array
                - then search on left or right sub array 
            - Compare mid with left, right

    - Find start, end idx with target
        - LC 34
        ```java
        // java
        // ...
        int mid = (l + r) / 2;
        while (r >= l){
            if (nums[mid] == target){
                /** 
                 *  NOTE !!! below
                 * 
                 *  instead of quit while loop directly,
                 *  we 
                 *    1) keep finding `left idx` if want to find first idx
                 *    2) keep finding `right idx` if want to find last idx
                 * 
                 */
                if(findFirst){
                    r = mid - 1;
                }else{
                    l = mid + 1;
                }
            }else if (nums[mid] < target){
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }

        // ...
        ```

    - Find `LEFT` boundary
        - LC 367, 875
        - the condition are nearly the same, ONLY difference :  `mid == target`, final check

        ```python
        # python
        while r >= l:
            mid = (l + r) // 2
            if mid < target:
                l = mid + 1
            else if mid > target:
                r = mid - 1
            else if mid == target:
                r = mid - 1 # reduce right boundary,
        if l >= nums.length or nums[l] != target: # check if l is out of boundary
            return -1
        return l
        ```

        ```java
        // java
        // ...
        while (r >= l){
            mid = (l + r) / 2;
            if (mid <= some_value){
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }
        // ...
        ```

    - Find `RIGHT` boundary
        - the condition are nearly the same, ONLY difference :  `mid == target`, final check
        ```python
        # python
       while r >= l:
            mid = (l + r) // 2
            if mid < target:
                l = mid + 1
            else if mid > target:
                r = mid - 1
            else if mid == target:
                l = mid + 1   # reduce left boundary
        if r < 0 or nums[r] != target: # check if r is out of boundary
            return -1
        return r
        ```

- Algorithm
    - binary search
    - sliding window
    - recursive

- Data structure
    - Array
    - Linked list
    - dict

### 0-2) Pattern
 - [Binary-Search-101-The-Ultimate-Binary-Search-Handbook](https://leetcode.com/problems/binary-search/discuss/423162/Binary-Search-101-The-Ultimate-Binary-Search-Handbook)
- [py powerful-ultimate-binary-search-template-solved-many-problems](https://leetcode.com/discuss/general-discussion/786126/python-powerful-ultimate-binary-search-template-solved-many-problems) : TODO : add above to cheatsheet
   
#### 0-2-0) Binary search

- Conclusion!!!
    - `left = 0, right = nums.len - 1`
    - `while left <= right`
    - `left = mid + 1`
    - `right = mid - 1`

- Key: 
```
分析二分查找的一個技巧是：不要出現 else，而是把所有情況用 else if 寫清楚，這樣可以清楚地展現所有細節。本文都會使用 else if，旨在講清楚，讀者理解後可自行簡化。
```

- https://labuladong.online/algo/essential-technique/binary-search-framework/#%E9%9B%B6%E3%80%81%E4%BA%8C%E5%88%86%E6%9F%A5%E6%89%BE%E6%A1%86%E6%9E%B6

```java
// java
int binarySearch(int[] nums, int target) {
    int left = 0; 
    // 注意
    int right = nums.length - 1;

    // NOTE !!! <=, need to search when "left == right" idx as well
    while(left <= right) {
        int mid = left + (right - left) / 2;
        if(nums[mid] == target)
            return mid; 
        else if (nums[mid] < target)
            // 注意
            left = mid + 1;
        else if (nums[mid] > target)
            // 注意
            right = mid - 1;
    }
    return -1;
}
```

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

#### 0-2-1) Binary search on `LEFT` boundary

- https://labuladong.online/algo/essential-technique/binary-search-framework/#%E8%83%BD%E5%90%A6%E7%BB%9F%E4%B8%80%E6%88%90%E4%B8%A4%E7%AB%AF%E9%83%BD%E9%97%AD%E7%9A%84%E6%90%9C%E7%B4%A2%E5%8C%BA%E9%97%B4

```java

/** 
 *  2 difference between regular binary search
 * 
 *   1. else if (nums[mid] == target) {
 *          // 收缩右侧边界
 *           right = mid - 1;
 *       }
 *
 *     
 *
 *   2. validate result step
 *    
 *        if (left < 0 || left >= nums.length) {
 *           return -1;
 *       }
 *       // 判断一下 nums[left] 是不是 target
 *       return nums[left] == target ? left : -1;
 *
 * 
 */

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
            // 搜索区间变为 [mid+1, right]
            left = mid + 1;
        }else if (nums[mid] > target){
            // 搜索区间变为 [left, mid-1]
            right = mid - 1;
        }else if (nums[mid] == target){
            // DO NOT RETURN !!!, BUT REDUCE RIGHT BOUNDARY FOR FOCUSING ON LEFT BOUNDARY
            // 收缩右侧边界
            right = mid - 1;
        }
    }
    // // finally check if it will be OUT OF LEFT boundary
    // if (left >= nums.length || nums[left] != target){
    //     return -1;
    // return left;


    // 判断 target 是否存在于 nums 中
    // 如果越界，target 肯定不存在，返回 -1
    if (left < 0 || left >= nums.length) {
        return -1;
    }
    // 判断一下 nums[left] 是不是 target
    return nums[left] == target ? left : -1;
}
```

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

#### 0-2-2) Binary search on `RIGHT` boundary


- https://labuladong.online/algo/essential-technique/binary-search-framework/#%E5%A6%82%E6%9E%9C-target-%E4%B8%8D%E5%AD%98%E5%9C%A8%E6%97%B6%E8%BF%94%E5%9B%9E%E4%BB%80%E4%B9%88


```java
// java

/** 
 *  2 difference between regular binary search
 * 
 *   1. else if (nums[mid] == target) {
 *          // 收缩左侧边界
 *           left = mid + 1;
 *       }
 *
 *     
 *
 *   2. validate result step
 *    
 *   // 最后改成返回 left - 1
 *   if (left - 1 < 0 || left - 1 >= nums.length) {
 *       return -1;
 *   }
 *  return nums[left - 1] == target ? (left - 1) : -1;
 *
 * 
 */

int right_bound(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < target) {
            left = mid + 1;
        } else if (nums[mid] > target) {
            right = mid - 1;
        } else if (nums[mid] == target) {
            // 这里改成收缩左侧边界即可
            left = mid + 1;
        }
    }


    // 最后改成返回 left - 1
    if (left - 1 < 0 || left - 1 >= nums.length) {
        return -1;
    }
    return nums[left - 1] == target ? (left - 1) : -1;
}
```


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

## 1) General form

## 2) LC Example

### 2-0) Search in Rotated Sorted Array
```python
# LC 033. Search in Rotated Sorted Array
# LC 081. Search in Rotated Sorted Array II
# V0
# IDEA : BINARY SEARCH
#        -> CHECK WHICH PART IS ORDERING
#        -> CHECK IF TARGET IS IN WHICH PART
# CASES :
#  1) if mid is on the right of pivot -> array[mid:] is ordering
#     -> check if mid in on the left or right on mid
#     -> binary search on left or right sub array
#  2) if mid in on the left of pivot  -> array[:mid] is ordering
#     -> check if mid in on the left or right on mid
#     -> binary search on left or right sub array
### NOTE : THE NESTED IF ELSE CONDITION 
class Solution(object):
    def search(self, nums, target):
        if not nums: return -1
        left, right = 0, len(nums) - 1
        while left <= right:
            mid = (left + right) // 2
            if nums[mid] == target:
                return mid
            #---------------------------------------------
            # Case 1 :  nums[mid:right] is ordering
            #---------------------------------------------
            # all we need to do is : 1) check if target is within mid - right, and move the left or right pointer
            if nums[mid] < nums[right]:
                # mind NOT use (" nums[mid] < target <= nums[right]")
                # mind the "<="
                if target > nums[mid] and target <= nums[right]: # check the relationship with target, which is different from the default binary search
                    left = mid + 1
                else:
                    right = mid - 1
            #---------------------------------------------
            # Case 2 :  nums[left:mid] is ordering
            #---------------------------------------------
            # all we need to do is : 1) check if target is within left - mid, and move the left or right pointer
            else:
                # # mind NOT use (" nums[left] <= target < nums[mid]")
                # mind the "<="
                if target < nums[mid] and target >= nums[left]:  # check the relationship with target, which is different from the default binary search
                    right = mid - 1
                else:
                    left = mid + 1
        return -1     
```

```java
// java
// LC 33
// V3
// IDEA : One Binary Search
// https://leetcode.com/problems/search-in-rotated-sorted-array/editorial/
public int search_4(int[] nums, int target) {
    int n = nums.length;
    int left = 0, right = n - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2;

        // Case 1: find target
        if (nums[mid] == target) {
            return mid;
        }

        // Case 2: subarray on mid's left is sorted
        else if (nums[mid] >= nums[left]) {
            if (target >= nums[left] && target < nums[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        // Case 3: subarray on mid's right is sorted
        else {
            if (target <= nums[right] && target > nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }

    return -1;
}
```

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
-  (recursive/iterative binary search)
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

```java
// java
// LC 162
// V2
// IDEA: RECURSIVE BINARY SEARCH
// https://leetcode.com/problems/find-peak-element/editorial/
    // NOTE : ONLY have to compare index i with index i + 1 (its right element)
    //        ; otherwise, i-1 already returned as answer
    public int findPeakElement_2(int[] nums) {
        return search(nums, 0, nums.length - 1);
    }
    public int search(int[] nums, int l, int r) {
        if (l == r)
            return l;
        int mid = (l + r) / 2;
        if (nums[mid] > nums[mid + 1])
            return search(nums, l, mid);
        return search(nums, mid + 1, r);
    }

// V3
// IDEA: ITERATIVE BINARY SEARCH
// https://leetcode.com/problems/find-peak-element/editorial/
    public int findPeakElement_3(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] > nums[mid + 1])
                r = mid;
            else
                l = mid + 1;
        }
        return l;
    }
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

```java
// java
// LC 367
public boolean isPerfectSquare(int num) {

    if (num < 2) {
        return true;
    }

    long left = 2;
    long right = num / 2; // NOTE !!!, "long right = num;" is OK as well
    long x;
    long guessSquared;

    while (left <= right) {
        x = (left + right) / 2;
        guessSquared = x * x;
        if (guessSquared == num) {
            return true;
        }
        if (guessSquared > num) {
            right = x - 1;
        } else {
            left = x + 1;
        }
    }
    return false;
}
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
# V0
# IDEA : BINARY SEARCH
class Solution(object):
     def shipWithinDays(self, weights, D):
            """
            NOTE !!!
                -> for this help func,
                -> we ONLY need to check weights can split by offered max_wgt
                -> so the return val is boolean (True or False)
            """
            # help func
            def cannot_split(weights, D, max_wgt):
                s = 0
                days = 1
                for w in weights:
                    s += w
                    if s > max_wgt:
                        s = w
                        days += 1
                return days > D

            """
            NOTE this !!!
                -> for l, we use max(weights)
                -> for r, we use sum(weights)
            """
            l = max(weights)
            r = sum(weights)
            while l <= r:
                mid = l + (r - l) // 2
                if cannot_split(weights, D, mid):
                    l = mid + 1
                else:
                    r = mid - 1
            return l
```

### 2-8) Split Array Largest Sum (Hard)
```python
# LC 410 Split Array Largest Sum [Hard]
```

### 2-9) Koko Eating Bananas

```java
// java
// LC 875

// V0
// IDEA : BINARY SEARCH (close boundary)
/**
 *  KEY !!!!
 *
 *   -> When r < l, it means the `smallest` valid eating speed is l
 *
 */
public int minEatingSpeed(int[] piles, int h) {

    if (piles.length == 0 || piles.equals(null)){
        return 0;
    }

    int l = 1; //Arrays.stream(piles).min().getAsInt();
    int r = Arrays.stream(piles).max().getAsInt();

    while (r >= l){
        int mid = (l + r) / 2;
        int _hour = getCompleteTime(piles, mid);
        if (_hour <= h){
            r = mid - 1;
        }else{
            l = mid + 1;
        }
    }

    return l;
}
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

### 2-12) Find First and Last Position of Element in Sorted Array
```python
# 34. Find First and Last Position of Element in Sorted Array
# V0
# IDEA : BINARY SEARCH
class Solution:
    def searchRange(self, nums: List[int], target: int) -> List[int]:
        
        def search(x):
            lo, hi = 0, len(nums)           
            while lo < hi:
                mid = (lo + hi) // 2
                if nums[mid] < x:
                    lo = mid+1
                else:
                    hi = mid                    
            return lo
        
        lo = search(target)
        hi = search(target+1)-1
        
        if lo <= hi:
            return [lo, hi]
                
        return [-1, -1]
```

```java
// java
// LC 34

// V0
// IDEA: BINARY SEARCH (fixed by gpt)
public int[] searchRange(int[] nums, int target) {
    int[] res = new int[]{-1, -1}; // Default result

    if (nums == null || nums.length == 0) {
        return res;
    }

    // Find the first occurrence of target
    int left = findBound(nums, target, true);
    if (left == -1) {
        return res; // Target not found
    }

    // Find the last occurrence of target
    int right = findBound(nums, target, false);

    return new int[]{left, right};
}

private int findBound(int[] nums, int target, boolean isFirst) {
    int l = 0, r = nums.length - 1;
    int bound = -1;

    while (l <= r) {
        int mid = l + (r - l) / 2;

        if (nums[mid] == target) {
            bound = mid;
            if (isFirst) {
                r = mid - 1; // Keep searching left
            } else {
                l = mid + 1; // Keep searching right
            }
        } else if (nums[mid] < target) {
            l = mid + 1;
        } else {
            r = mid - 1;
        }
    }

    return bound;
}
```

### 2-13) Search a 2D Matrix
```java
// java
// LC 74
// V1
// IDEA : BINARY SEARCH + FLATTEN MATRIX
// https://leetcode.com/problems/search-a-2d-matrix/editorial/
public boolean searchMatrix_2(int[][] matrix, int target) {
    int m = matrix.length;
    if (m == 0)
        return false;
    int n = matrix[0].length;

    // binary search
    /** NOTE !!! FLATTEN MATRIX */
    int left = 0, right = m * n - 1;
    int pivotIdx, pivotElement;
    while (left <= right) {
        pivotIdx = (left + right) / 2;
        /** NOTE !!! TRICK HERE :
         *
         *   pivotIdx / n : y index
         *   pivotIdx % n : x index
         */
        pivotElement = matrix[pivotIdx / n][pivotIdx % n];
        if (target == pivotElement)
            return true;
        else {
            if (target < pivotElement)
                right = pivotIdx - 1;
            else
                left = pivotIdx + 1;
        }
    }
    return false;
}
```

### 2-14) Find Minimum in Rotated Sorted Array
```java
// java
// LC 153

    // V0
    // IDEA : BINARY SEARCH (CLOSED BOUNDARY)
    // https://youtu.be/nIVW4P8b1VA?si=AMhTJOUhDziBz-CV
    /**
     *  NOTE !!!
     *
     *  key : check current `mid point` is at  `left part` or `right part`
     *  if `at left part`
     *   -> nums[l] ~ nums[mid] is in INCREASING order
     *   -> need to search `RIGHT part`, since right part is ALWAYS SMALLER then left part
     *
     *  else, `at right part`
     *   -> need to search `LEFT part`
     */
    public int findMin(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        int res = nums[0];

        /** NOTE !!! closed boundary */
        while (l <= r) {

            // edge case : is array already in increasing order (e.g. [1,2,3,4,5])
            if (nums[l] < nums[r]) {
                res = Math.min(res, nums[l]);
                break;
            }

            int m = l + (r - l) / 2;
            res = Math.min(res, nums[m]);

            // case 1) mid point is at `LEFT part`
            // e.g. [3,4,5,1,2]
            if (nums[m] >= nums[l]) {
                l = m + 1;
            }
            // case 2) mid point is at `RIGHT part`
            // e.g. [5,1,2,3,4]
            else {
                r = m - 1;
            }
        }
        return res;
    }
```

### 2-15) Find First and Last Position of Element in Sorted Array

```java
// java
// LC 34
    public int[] searchRange_1(int[] nums, int target) {
        int[] result = new int[2];
        result[0] = findFirst(nums, target);
        result[1] = findLast(nums, target);
        return result;
    }

    private int findFirst(int[] nums, int target) {
        int idx = -1;
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;

            /** NOTE !!!
             *
             * 1) nums[mid] >= target (find right boundary)
             * 2) we put equals condition below  (nums[mid] == target)
             */
            if (nums[mid] >= target) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
            if (nums[mid] == target)
                idx = mid;
        }
        return idx;
    }

    private int findLast(int[] nums, int target) {
        int idx = -1;
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            /** NOTE !!!
             *
             * 1) nums[mid] <= target (find left boundary)
             * 2) we put equals condition below  (nums[mid] == target)
             */
            if (nums[mid] <= target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
            if (nums[mid] == target)
                idx = mid;
        }
        return idx;
    }
```
# Prefix Sum (前缀和)

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/prefix_sum.png"></p>

## Overview

**Prefix Sum** is a preprocessing technique that allows us to compute the sum of any subarray in O(1) time after O(n) preprocessing. The core idea is to precompute cumulative sums from the beginning of the array to each position.

### Key Properties
- **Time Complexity**: 
  - Preprocessing: O(n)
  - Query subarray sum: O(1)
  - Overall: O(n) preprocessing + O(1) per query
- **Space Complexity**: O(n) for storing prefix sums
- **Core Idea**: `prefixSum[i] = nums[0] + nums[1] + ... + nums[i-1]`
- **Subarray Sum**: `sum(i, j) = prefixSum[j+1] - prefixSum[i]`
- **When to Use**: 
  - Multiple range sum queries
  - Subarray problems with conditions
  - Converting O(n²) to O(n) with HashMap
  - 2D range sum queries

### References
- [Fucking Algorithm - Prefix Sum](https://labuladong.github.io/algo/2/19/22/)
- [LeetCode Prefix Sum Problems](https://leetcode.com/tag/prefix-sum/)
- [LeetCode Problem Set Discussion](https://leetcode.com/discuss/general-discussion/563022/prefix-sum-problems)
- [Hash Map Cheatsheet](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/hash_map.md)

## Problem Categories

### **Pattern 1: Basic Range Sum**
- **Description**: Calculate sum of elements in any given range [i, j]
- **Examples**: LC 303 - Range Sum Query, LC 304 - Range Sum Query 2D
- **Pattern**: Direct application of prefix sum formula
- **Key Insight**: `sum[i:j] = prefixSum[j+1] - prefixSum[i]`

### **Pattern 2: Subarray Sum Equals Target**
- **Description**: Find/count subarrays with sum equal to target value
- **Examples**: LC 560 - Subarray Sum Equals K, LC 325 - Maximum Size Subarray Sum Equals k
- **Pattern**: Use HashMap to store prefix sums and check if `(current_sum - target)` exists
- **Key Insight**: If `prefixSum[j] - prefixSum[i] = k`, then `prefixSum[i] = prefixSum[j] - k`

### **Pattern 3: Subarray with Divisibility/Modulo**
- **Description**: Problems involving divisibility, remainders, or modulo operations
- **Examples**: LC 523 - Continuous Subarray Sum, LC 974 - Subarray Sums Divisible by K
- **Pattern**: Store remainders instead of actual sums in HashMap
- **Key Insight**: If `(prefixSum[j] - prefixSum[i]) % k = 0`, then `prefixSum[j] % k = prefixSum[i] % k`

### **Pattern 4: Range Addition/Difference Array**
- **Description**: Efficiently apply range updates to arrays
- **Examples**: LC 370 - Range Addition, LC 1094 - Car Pooling
- **Pattern**: Use difference array technique with prefix sum
- **Key Insight**: Add at start, subtract at end+1, then compute prefix sum

### **Pattern 5: 2D Prefix Sum**
- **Description**: Calculate sum of any rectangular region in 2D matrix
- **Examples**: LC 304 - Range Sum Query 2D, LC 1314 - Matrix Block Sum
- **Pattern**: Build 2D prefix sum matrix, use inclusion-exclusion principle
- **Key Insight**: `sum = total - left - top + topleft`

### **Pattern 6: Transform and Count**
- **Description**: Transform array elements and use prefix sum for counting
- **Examples**: LC 1248 - Count Nice Subarrays, LC 926 - Flip String to Monotone
- **Pattern**: Convert elements to 0/1, then apply prefix sum with conditions
- **Key Insight**: Transform problem to simpler prefix sum problem

## 0) Concept

## Templates & Algorithms

### Template Comparison Table

| Template Type | Use Case | Key Data Structure | When to Use |
|---------------|----------|-------------------|-------------|
| **Basic Prefix Sum** | Range sum queries | Array | Need multiple range sum calculations |
| **HashMap + Prefix Sum** | Subarray with target sum | HashMap | Find/count subarrays with specific sum |
| **Modulo Prefix Sum** | Divisibility problems | HashMap with remainders | Subarray sum divisible by k |
| **Difference Array** | Range updates | Array with start/end markers | Multiple range additions |
| **2D Prefix Sum** | Rectangle sum queries | 2D matrix | 2D range sum calculations |

### Universal Template

```python
def prefix_sum_solve(nums, target):
    """
    Universal prefix sum template for most problems
    """
    # Step 1: Initialize prefix sum and result
    prefix_sum = 0
    result = 0
    
    # Step 2: HashMap for storing prefix sums (if needed)
    prefix_map = {0: 1}  # Handle subarrays starting from index 0
    
    # Step 3: Iterate through array
    for num in nums:
        # Update prefix sum
        prefix_sum += num
        
        # Check condition based on problem type
        if prefix_sum - target in prefix_map:
            result += prefix_map[prefix_sum - target]
        
        # Update map
        prefix_map[prefix_sum] = prefix_map.get(prefix_sum, 0) + 1
    
    return result
```

### Template 1: Basic Prefix Sum (Range Queries)

```python
class PrefixSum:
    def __init__(self, nums):
        """Build prefix sum array for range queries"""
        self.prefix = [0] * (len(nums) + 1)
        for i in range(len(nums)):
            self.prefix[i + 1] = self.prefix[i] + nums[i]
    
    def range_sum(self, left, right):
        """Get sum of elements from index left to right (inclusive)"""
        return self.prefix[right + 1] - self.prefix[left]
```

```java
// Java implementation
class PrefixSum {
    private int[] prefix;
    
    public PrefixSum(int[] nums) {
        prefix = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
    }
    
    public int rangeSum(int left, int right) {
        return prefix[right + 1] - prefix[left];
    }
}
```

### Template 2: HashMap + Prefix Sum (Subarray Target Sum)

```python
def subarray_sum_equals_k(nums, k):
    """Count subarrays with sum equal to k"""
    count = 0
    prefix_sum = 0
    prefix_map = {0: 1}  # Important: initialize with {0: 1}
    
    for num in nums:
        prefix_sum += num
        
        # Check if (prefix_sum - k) exists
        if prefix_sum - k in prefix_map:
            count += prefix_map[prefix_sum - k]
        
        # Update map
        prefix_map[prefix_sum] = prefix_map.get(prefix_sum, 0) + 1
    
    return count
```

```java
// Java implementation
public int subarraySum(int[] nums, int k) {
    int count = 0, prefixSum = 0;
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);  // Handle subarrays starting from index 0
    
    for (int num : nums) {
        prefixSum += num;
        
        if (map.containsKey(prefixSum - k)) {
            count += map.get(prefixSum - k);
        }
        
        map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
    }
    
    return count;
}
```

### Template 3: Modulo Prefix Sum (Divisibility Problems)

```python
def subarray_divisible_by_k(nums, k):
    """Count subarrays with sum divisible by k"""
    count = 0
    prefix_sum = 0
    remainder_map = {0: 1}  # remainder -> count
    
    for num in nums:
        prefix_sum += num
        remainder = prefix_sum % k
        
        # Handle negative remainders
        if remainder < 0:
            remainder += k
        
        if remainder in remainder_map:
            count += remainder_map[remainder]
        
        remainder_map[remainder] = remainder_map.get(remainder, 0) + 1
    
    return count
```

### Template 4: Difference Array (Range Updates)

```python
def range_addition(length, updates):
    """Apply multiple range additions efficiently"""
    # Step 1: Create difference array
    diff = [0] * (length + 1)
    
    # Step 2: Apply range updates to difference array
    for start, end, val in updates:
        diff[start] += val
        diff[end + 1] -= val
    
    # Step 3: Compute prefix sum to get final result
    result = []
    current_sum = 0
    for i in range(length):
        current_sum += diff[i]
        result.append(current_sum)
    
    return result
```

### Template 5: 2D Prefix Sum

```python
class NumMatrix:
    def __init__(self, matrix):
        """Build 2D prefix sum matrix"""
        if not matrix or not matrix[0]:
            return
        
        m, n = len(matrix), len(matrix[0])
        self.prefix = [[0] * (n + 1) for _ in range(m + 1)]
        
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                self.prefix[i][j] = (matrix[i-1][j-1] + 
                                   self.prefix[i-1][j] + 
                                   self.prefix[i][j-1] - 
                                   self.prefix[i-1][j-1])
    
    def sumRegion(self, row1, col1, row2, col2):
        """Calculate sum of rectangle from (row1,col1) to (row2,col2)"""
        return (self.prefix[row2+1][col2+1] - 
                self.prefix[row1][col2+1] - 
                self.prefix[row2+1][col1] + 
                self.prefix[row1][col1])
```

### Template 6: Transform and Count

```python
def count_nice_subarrays(nums, k):
    """Count subarrays with exactly k odd numbers"""
    # Transform: odd -> 1, even -> 0
    transformed = [1 if x % 2 == 1 else 0 for x in nums]
    
    # Now it's subarray sum equals k problem
    count = 0
    prefix_sum = 0
    prefix_map = {0: 1}
    
    for val in transformed:
        prefix_sum += val
        
        if prefix_sum - k in prefix_map:
            count += prefix_map[prefix_sum - k]
        
        prefix_map[prefix_sum] = prefix_map.get(prefix_sum, 0) + 1
    
    return count
```

## Problems by Pattern

### Pattern-Based Problem Classification

#### **Pattern 1: Basic Range Sum Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Range Sum Query - Immutable | 303 | Basic prefix sum array | Easy | Template 1 |
| Range Sum Query 2D - Immutable | 304 | 2D prefix sum | Medium | Template 5 |
| Product of Array Except Self | 238 | Left/right prefix products | Medium | Modified Template 1 |
| Running Sum of 1d Array | 1480 | Direct prefix sum | Easy | Template 1 |
| Find Pivot Index | 724 | Left sum vs right sum | Easy | Template 1 |

#### **Pattern 2: Subarray Sum Equals Target Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Subarray Sum Equals K | 560 | HashMap + prefix sum | Medium | Template 2 |
| Maximum Size Subarray Sum Equals k | 325 | HashMap with indices | Medium | Template 2 |
| Subarray Sum Equals K II | 713 | Product version | Medium | Modified Template 2 |
| Binary Subarrays With Sum | 930 | Transform to sum equals | Medium | Template 6 |
| Number of Subarrays with Bounded Maximum | 795 | Range sum technique | Medium | Template 2 |

#### **Pattern 3: Subarray with Divisibility/Modulo Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Subarray Sums Divisible by K | 974 | Modulo prefix sum | Medium | Template 3 |
| Continuous Subarray Sum | 523 | Modulo with length check | Medium | Template 3 |
| Make Sum Divisible by P | 1590 | Advanced modulo technique | Medium | Template 3 |
| Check If Array Pairs Are Divisible by k | 1497 | Frequency of remainders | Medium | Modified Template 3 |

#### **Pattern 4: Range Addition/Updates Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Range Addition | 370 | Difference array | Medium | Template 4 |
| Car Pooling | 1094 | Timeline simulation | Medium | Template 4 |
| Corporate Flight Bookings | 1109 | Range updates | Medium | Template 4 |
| Maximum Population Year | 1854 | Event processing | Easy | Template 4 |
| Meeting Rooms II | 253 | Overlap counting | Medium | Template 4 |

#### **Pattern 5: 2D Matrix Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Range Sum Query 2D | 304 | 2D prefix sum | Medium | Template 5 |
| Matrix Block Sum | 1314 | 2D range queries | Medium | Template 5 |
| Number of Submatrices That Sum to Target | 1074 | 2D + HashMap | Hard | Template 5 + 2 |
| Maximum Side Length Square | 1292 | Binary search + 2D prefix | Medium | Template 5 |

#### **Pattern 6: Transform and Count Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Count Number of Nice Subarrays | 1248 | Transform odd/even | Medium | Template 6 |
| Flip String to Monotone Increasing | 926 | Transform 0/1 counting | Medium | Template 6 |
| Max Chunks To Make Sorted | 769 | Sum comparison | Medium | Template 6 |
| Longest Arithmetic Subsequence | 1027 | Transform differences | Medium | Template 6 |

#### **Advanced/Mixed Pattern Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Maximum Sum of Two Non-Overlapping Subarrays | 1031 | Multiple prefix arrays | Medium | Template 1 + DP |
| Subarrays with K Different Integers | 992 | At most K technique | Hard | Template 2 |
| Minimum Window Subsequence | 727 | Sliding window + prefix | Hard | Template 2 + SW |
| Split Array With Same Average | 805 | Subset sum problem | Hard | Template 2 |
| Largest Rectangle in Histogram | 84 | Stack + prefix sum | Hard | Template 1 + Stack |

### Additional Practice Problems

#### **Easy Problems (Foundation Building)**
| Problem | LC # | Focus Area | Template |
|---------|------|------------|----------|
| Two Sum | 1 | HashMap fundamentals | Modified Template 2 |
| Contains Duplicate II | 219 | Sliding window + map | Template 2 |
| Maximum Average Subarray I | 643 | Fixed size subarray | Template 1 |
| Degree of an Array | 697 | Element frequency | Template 2 |

#### **Medium Problems (Core Patterns)**
| Problem | LC # | Focus Area | Template |
|---------|------|------------|----------|
| Contiguous Array | 525 | Balance 0s and 1s | Template 6 |
| Shortest Unsorted Continuous Subarray | 581 | Array analysis | Template 1 |
| Random Pick with Weight | 528 | Weighted random | Template 1 |
| Path Sum III | 437 | Tree + prefix sum | Template 2 |

#### **Hard Problems (Advanced Techniques)**
| Problem | LC # | Focus Area | Template |
|---------|------|------------|----------|
| Count of Range Sum | 327 | Merge sort + prefix | Advanced |
| Reverse Pairs | 493 | Merge sort technique | Advanced |
| Create Maximum Number | 321 | Greedy + prefix | Advanced |
| Count Different Palindromic Subsequences | 730 | DP + prefix | Advanced |

## Pattern Selection Strategy

### Decision Framework for Prefix Sum Problems

```
Problem Analysis Flowchart:

1. Need multiple range sum queries?
   ├── YES → Use Template 1 (Basic Prefix Sum)
   └── NO → Continue to 2

2. Looking for subarrays with specific sum/count?
   ├── YES → Continue to 2a
   └── NO → Continue to 3
   
   2a. Exact sum target?
       ├── YES → Use Template 2 (HashMap + Prefix Sum)
       └── NO → Continue to 2b
   
   2b. Divisibility or modulo involved?
       ├── YES → Use Template 3 (Modulo Prefix Sum)
       └── NO → Continue to 2c
   
   2c. Count odd/even or binary transformation?
       ├── YES → Use Template 6 (Transform and Count)
       └── NO → Use Template 2

3. Multiple range updates needed?
   ├── YES → Use Template 4 (Difference Array)
   └── NO → Continue to 4

4. 2D matrix operations?
   ├── YES → Use Template 5 (2D Prefix Sum)
   └── NO → Continue to 5

5. Special cases:
   ├── Product instead of sum → Modified Template 1
   ├── Tree path sums → Template 2 + Tree traversal
   ├── Sliding window + prefix → Combine templates
   └── Advanced merge/sort → Custom approach
```

### Template Selection Guide

| Problem Keywords | Recommended Template | Example Problems |
|------------------|---------------------|------------------|
| "range sum", "query" | Template 1 | LC 303, 304 |
| "subarray sum equals", "count subarrays" | Template 2 | LC 560, 325 |
| "divisible by", "remainder", "modulo" | Template 3 | LC 974, 523 |
| "range addition", "updates", "intervals" | Template 4 | LC 370, 1094 |
| "2D", "matrix", "rectangle" | Template 5 | LC 304, 1314 |
| "odd numbers", "binary", "transform" | Template 6 | LC 1248, 926 |

### Problem Identification Patterns

#### **Identify Template 1 Usage:**
- Problem mentions: "range sum query", "immutable array", "multiple queries"
- Input: Array + multiple (left, right) queries
- Output: Sum of elements in range [left, right]

#### **Identify Template 2 Usage:**
- Problem mentions: "subarray sum equals K", "count subarrays", "target sum"
- Key insight: Need to find pairs (i, j) where `prefixSum[j] - prefixSum[i] = target`
- HashMap stores: `{prefixSum: count}` or `{prefixSum: index}`

#### **Identify Template 3 Usage:**
- Problem mentions: "divisible by K", "remainder", "modulo", "continuous sum"
- Key insight: `(prefixSum[j] - prefixSum[i]) % k = 0` means same remainders
- HashMap stores: `{remainder: count}` or `{remainder: index}`

#### **Identify Template 4 Usage:**
- Problem mentions: "range updates", "add value to range", "difference array"
- Multiple operations of type: "add val to indices [start, end]"
- Key insight: Mark start/end points, then compute prefix sum

#### **Identify Template 5 Usage:**
- Problem mentions: "2D matrix", "rectangle sum", "submatrix"
- Need sum of rectangle from (r1,c1) to (r2,c2)
- Formula: `total - left - top + topleft`

#### **Identify Template 6 Usage:**
- Problem mentions: "count odd/even", "binary conditions", "transform array"
- First transform array (e.g., odd→1, even→0), then apply prefix sum
- Reduces to simpler prefix sum problem

## Legacy Examples

#### 0-2-1) Get count of `continuous sub array equals to K`

```java
// java
// LC 560

// ..
Map<Integer, Integer> map = new HashMap<>();

// ..

for (int num : nums) {
    presum += num;

    // Check if there's a prefix sum such that presum - k exists
    // NOTE !!! below
    if (map.containsKey(presum - k)) {
        count += map.get(presum - k);
    }

    // Update the map with the current prefix sum
    map.put(presum, map.getOrDefault(presum, 0) + 1);
}

// ..

```

#### 0-2-2) get prefix with `range addition` 

```java
// java

// LC 1094

// ...

int[] prefixSum = new int[1001]; // the biggest array size given by problem

// `init pre prefix sum`
for (int[] t : trips) {
    
    int amount = t[0];
    int start = t[1];
    int end = t[2];

    /**
     *  NOTE !!!!
     *
     *   via trick below, we can `efficiently` setup prefix sum
     *   per start, end index
     *
     *   -> we ADD amount at start point (customer pickup up)
     *   -> we MINUS amount at `end point` (customer drop off)
     *
     *   -> via above, we get the `adjusted` `init prefix sum`
     *   -> so all we need to do next is :
     *      -> loop over the `init prefix sum`
     *      -> and keep adding `previous to current val`
     *      -> e.g. prefixSum[i] = prefixSum[i-1] + prefixSum[i]
     *
     */
    prefixSum[start] += amount;
    prefixSum[end] -= amount;
}

// update `prefix sum` array
for (int i = 1; i < prefixSum.length; i++) {
    prefixSum[i] += prefixSum[i - 1];
}


// ...
```


## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Flip String to Monotone Increasing

```python
# LC 926. Flip String to Monotone Increasing
# NOTE : there is also dp approaches
# V0 
# IDEA : PREFIX SUM
class Solution(object):
    def minFlipsMonoIncr(self, S):
        # get pre-fix sum
        P = [0]
        for x in S:
            P.append(P[-1] + int(x))
        # find min
        res = float('inf')
        for j in range(len(P)):
            res = min(res, P[j] + len(S)-j-(P[-1]-P[j]))
        return res

# V1
# IDEA : PREFIX SUM
# https://leetcode.com/problems/flip-string-to-monotone-increasing/solution/
class Solution(object):
    def minFlipsMonoIncr(self, S):
        # get pre-fix sum
        P = [0]
        for x in S:
            P.append(P[-1] + int(x))
        # return min
        return min(P[j] + len(S)-j-(P[-1]-P[j])
                   for j in range(len(P)))
```

### 2-2) Range Addition
```python
# LC 370. Range Addition
# V0
# IDEA : double loop -> 2 single loops,  prefix sum
class Solution(object):
    def getModifiedArray(self, length, updates):
        # edge case
        if not length:
            return
        if length and not updates:
            return [0 for i in range(length)]
        # init res
        res = [0 for i in range(length)]
        # get cumsum on start and end idx
        # then go through res, adjust the sum
        for _start, _end, val in updates:
            res[_start] += val
            if _end+1 < length:
                res[_end+1] -= val

        #print ("res = " + str(res))
        cur = 0
        for i in range(len(res)):
            cur += res[i]
            res[i] = cur
        #print ("--> res = " + str(res))
        return res

# V0'
# IDEA : double loop -> 2 single loops,  prefix sum
class Solution(object):
    def getModifiedArray(self, length, updates):
        # NOTE : we init res with (len+1)
        res = [0] * (length + 1)
        """
        NOTE !!!

        -> We split double loop into 2 single loops
        -> Steps)
            step 1) go through updates,  add val to start and end idx in res
            step 2) go through res, maintain an aggregated sum (sum) and add it to res[i]
                e.g. res[i], sum = res[i] + sum, res[i] + sum
        """
        for start, end, val in updates:
            res[start] += val
            res[end+1] -= val
        
        sum = 0
        for i in range(0, length):
            res[i], sum = res[i] + sum, res[i] + sum
        
        # NOTE : we return res[0:-1]
        return res[0:-1]

# V0'
# IDEA : double loop -> 2 single loops,  prefix sum
class Solution(object):
    def getModifiedArray(self, length, updates):
        ret = [0] * (length + 1)
        for start, end, val in updates:
            ret[start] += val
            ret[end+1] -= val
        
        sum = 0
        for i in range(0, length):
            ret[i], sum = ret[i] + sum, ret[i] + sum
        
        return ret[0:-1]
```

### 2-3) Count Number of Nice Subarrays
```python
# 1248. Count Number of Nice Subarrays
# NOTE : there are also array, window, deque.. approaches
class Solution:
    def numberOfSubarrays(self, nums, k):
        d = collections.defaultdict(int)
        """
        definition of hash map (in this problem)

            -> d[cum_sum] = number_of_cum_sum_till_now

            -> so at beginning, cum_sum = 0, and its count = 1
            -> so we init hash map via "d[0] = 1"
        """
        d[0] = 1
        # init cum_sum
        cum_sum = 0
        res = 0
        # go to each element
        for i in nums:
            ### NOTE : we need to check "if i % 2 == 1" first, so in the next logic, we can append val to result directly
            if i % 2 == 1:
                cum_sum += 1
            """
            NOTE !!! : trick here !!!
             -> same as 2 sum ...
             -> if cum_sum - x == k
                -> x = cum_sum - k
                -> so we need to check if "cum_sum - k" already in our hash map
            """
            if cum_sum - k in d:
                ### NOTE !!! here : we need to use d[cum_sum - k], since this is # of sub string combination that with # of odd numbers  == k
                res += d[cum_sum - k]
            ### NOTE !!! : we need to add 1 to count of "cum_sum", since in current loop, we got a new cur_sum (as above)
            d[cum_sum] += 1
        return res
```

### 2-4) Maximum Size Subarray Sum Equals k
```python
# LC 325. Maximum Size Subarray Sum Equals k
# V0 
# time complexity : O(N) | space complexity : O(N)
# IDEA : HASH TBALE
# -> have a var acc keep sum of all item in nums,
# -> and use dic collect acc and its index
# -> since we want to find nums[i:j] = k  -> so it's a 2 sum problem now
# -> i.e. if acc - k in dic => there must be a solution (i,j) of  nums[i:j] = k  
# -> return the max result 
# -> ### acc DEMO : given array a = [1,2,3,4,5] ###
# -> acc_list = [1,3,6,10,15]
# -> so sum(a[1:3]) = 9 = acc_list[3] - acc_list[1-1] = 10 - 1 = 9 
class Solution(object):
    def maxSubArrayLen(self, nums, k):

        result, acc = 0, 0
        # NOTE !!! we init dic as {0:-1} ({sum:idx})
        dic = {0: -1}

        for i in range(len(nums)):
            acc += nums[i]
            if acc not in dic:
                ### NOTE : we save idx as dict value
                dic[acc] = i
            ### acc - x = k -> so x = acc - k, that's why we check if acc - x in the dic or not
            if acc - k in dic:
                result = max(result, i - dic[acc-k])
        return result
```

### 2-5) Subarray Sum Equals K

```java
// java
// LC 560
public int subarraySum(int[] nums, int k) {
    /**
     * NOTE !!!
     *
     *   use Map to store prefix sum and its count
     *
     *   map : {prefixSum: count}
     *
     *
     *   -> since "same preSum may have multiple combination" within hashMap,
     *      so it's needed to track preSum COUNT, instead of its index
     */
    Map<Integer, Integer> map = new HashMap<>();
    int presum = 0;
    int count = 0;

    /** 
     *  NOTE !!!
     *
     *  Initialize the map with prefix sum 0 (to handle subarrays starting at index 0)
     */
    map.put(0, 1);

    for (int num : nums) {
        presum += num;

        // Check if there's a prefix sum such that presum - k exists
        if (map.containsKey(presum - k)) {
            count += map.get(presum - k);
        }

        // Update the map with the current prefix sum
        map.put(presum, map.getOrDefault(presum, 0) + 1);
    }

    return count;
}
```

### 2-6) Continuous Subarray Sum

```java
// java
// LC 523
// V1
// IDEA : HASHMAP
// https://leetcode.com/problems/continuous-subarray-sum/editorial/
// https://github.com/yennanliu/CS_basics/blob/master/doc/pic/presum_mod.png
public boolean checkSubarraySum_1(int[] nums, int k) {
    int prefixMod = 0;
    HashMap<Integer, Integer> modSeen = new HashMap<>();
    modSeen.put(0, -1);

    for (int i = 0; i < nums.length; i++) {
        /**
         * NOTE !!! we get `mod of prefixSum`, instead of get prefixSum
         */
        prefixMod = (prefixMod + nums[i]) % k;

        if (modSeen.containsKey(prefixMod)) {
            // ensures that the size of subarray is at least 2
            if (i - modSeen.get(prefixMod) > 1) {
                return true;
            }
        } else {
            // mark the value of prefixMod with the current index.
            modSeen.put(prefixMod, i);
        }
    }

    return false;
}
```

### 2-7) Max Chunks To Make Sorted

```java
// java
// LC 769
// V1-1
// https://leetcode.com/problems/max-chunks-to-make-sorted/editorial/
// IDEA: Prefix Sums
/**
 *  IDEA:
 *
 *  An important observation is that a segment of the
 *  array can form a valid chunk if, when sorted,
 *  it matches the corresponding segment
 *  in the fully sorted version of the array.
 *
 * Since the numbers in arr belong to the range [0, n - 1], we can simplify the problem by using the property of sums. Specifically, for any index, it suffices to check whether the sum of the elements in arr up to that index is equal to the sum of the elements in the corresponding prefix of the sorted array.
 *
 * If these sums are equal, it guarantees that the elements in the current segment of arr match the elements in the corresponding segment of the sorted array (possibly in a different order). When this condition is satisfied, we can form a new chunk — either starting from the beginning of the array or the end of the previous chunk.
 *
 * For example, consider arr = [1, 2, 0, 3, 4] and the sorted version sortedArr = [0, 1, 2, 3, 4]. We find the valid segments as follows:
 *
 * Segment [0, 0] is not valid, since sum = 1 and sortedSum = 0.
 * Segment [0, 1] is not valid, since sum = 1 + 2 = 3 and sortedSum = 0 + 1 = 1.
 * Segment [0, 2] is valid, since sum = 1 + 2 + 0 = 3 and sortedSum = 0 + 1 + 2 = 3.
 * Segment [3, 3] is valid, because sum = 1 + 2 + 0 + 3 = 6 and sortedSum = 0 + 1 + 2 + 3 = 6.
 * Segment [4, 4] is valid, because sum = 1 + 2 + 0 + 3 + 4 = 10 and sortedSum = 0 + 1 + 2 + 3 + 4 = 10.
 * Therefore, the answer here is 3.
 *
 * Algorithm
 * - Initialize n to the size of the arr array.
 * - Initialize chunks, prefixSum, and sortedPrefixSum to 0.
 * - Iterate over arr with i from 0 to n - 1:
 *    - Increment prefixSum by arr[i].
 *    - Increment sortedPrefixSum by i.
 *    - Check if prefixSum == sortedPrefixSum:
 *        - If so, increment chunks by 1.
 * - Return chunks.
 *
 */
public int maxChunksToSorted_1_1(int[] arr) {
    int n = arr.length;
    int chunks = 0, prefixSum = 0, sortedPrefixSum = 0;

    // Iterate over the array
    for (int i = 0; i < n; i++) {
        // Update prefix sum of `arr`
        prefixSum += arr[i];
        // Update prefix sum of the sorted array
        sortedPrefixSum += i;

        // If the two sums are equal, the two prefixes contain the same elements; a chunk can be formed
        if (prefixSum == sortedPrefixSum) {
            chunks++;
        }
    }
    return chunks;
}
```

### 2-8) Maximum Sum of Two Non-Overlapping Subarrays

```java
// java
// LC 1031

// V1
// https://leetcode.ca/2018-09-26-1031-Maximum-Sum-of-Two-Non-Overlapping-Subarrays/
// IDEA: PREFIX SUM
public int maxSumTwoNoOverlap_1(int[] nums, int firstLen, int secondLen) {
    int n = nums.length;
    int[] s = new int[n + 1];
    for (int i = 0; i < n; ++i) {
        s[i + 1] = s[i] + nums[i];
    }
    int ans = 0;
    // case 1)  check `firstLen`, then `secondLen`
    for (int i = firstLen, t = 0; i + secondLen - 1 < n; ++i) {
        t = Math.max(t, s[i] - s[i - firstLen]);
        ans = Math.max(ans, t + s[i + secondLen] - s[i]);
    }
    // case 2)  check  `secondLen`, then `firstLen`
    for (int i = secondLen, t = 0; i + firstLen - 1 < n; ++i) {
        t = Math.max(t, s[i] - s[i - secondLen]);
        ans = Math.max(ans, t + s[i + firstLen] - s[i]);
    }
    return ans;
}
```

## Summary & Quick Reference

### Complexity Quick Reference

| Operation | Time | Space | Notes |
|-----------|------|-------|--------|
| Build prefix sum array | O(n) | O(n) | One-time preprocessing |
| Range sum query | O(1) | O(1) | After preprocessing |
| Subarray sum with HashMap | O(n) | O(n) | Average case, O(n²) worst case |
| 2D prefix sum build | O(mn) | O(mn) | For m×n matrix |
| 2D range query | O(1) | O(1) | After preprocessing |
| Difference array updates | O(k) | O(n) | k updates, n array size |

### Template Quick Reference

| Template | Pattern | Key Code Snippet |
|----------|---------|------------------|
| **Template 1** | Basic Range Sum | `prefix[i+1] = prefix[i] + nums[i]` |
| **Template 2** | HashMap + Target | `if prefix_sum - k in map: count += map[prefix_sum - k]` |
| **Template 3** | Modulo/Divisibility | `remainder = prefix_sum % k; if remainder in map...` |
| **Template 4** | Range Updates | `diff[start] += val; diff[end+1] -= val` |
| **Template 5** | 2D Matrix | `prefix[i][j] = val + left + top - topleft` |
| **Template 6** | Transform Count | `transform array first, then apply prefix sum` |

### Core Mathematical Insights

#### **Prefix Sum Formula**
```python
# For 1D array: sum of subarray [i, j] (inclusive)
subarray_sum = prefix[j + 1] - prefix[i]

# For 2D matrix: sum of rectangle from (r1,c1) to (r2,c2)
rectangle_sum = prefix[r2+1][c2+1] - prefix[r1][c2+1] - prefix[r2+1][c1] + prefix[r1][c1]
```

#### **HashMap Key Insights**
```python
# If prefix_sum[j] - prefix_sum[i] = k
# Then prefix_sum[i] = prefix_sum[j] - k
# So check if (current_prefix_sum - k) exists in map

# For divisibility: if (sum[j] - sum[i]) % k = 0
# Then sum[j] % k = sum[i] % k
# So check if (current_sum % k) exists in remainder map
```

### Common Patterns & Tricks

#### **Pattern 1: Two Sum Extended**
```python
# Convert "find subarray with sum = k" to "find two prefix sums with diff = k"
def subarray_sum_equals_k(nums, k):
    prefix_sum = 0
    count = 0
    prefix_map = {0: 1}  # Critical: handle subarrays from index 0
    
    for num in nums:
        prefix_sum += num
        count += prefix_map.get(prefix_sum - k, 0)
        prefix_map[prefix_sum] = prefix_map.get(prefix_sum, 0) + 1
    
    return count
```

#### **Pattern 2: Difference Array Magic**
```python
# Apply multiple range updates [start, end, val] efficiently
def range_addition(length, updates):
    diff = [0] * (length + 1)  # Extra space for end+1 indexing
    
    for start, end, val in updates:
        diff[start] += val      # Mark start
        diff[end + 1] -= val    # Mark end+1 (undo effect)
    
    # Convert difference array to result using prefix sum
    result = []
    current = 0
    for i in range(length):
        current += diff[i]      # This is prefix sum computation!
        result.append(current)
    
    return result
```

#### **Pattern 3: Transform Before Sum**
```python
# Many problems can be reduced to simpler prefix sum problems
def count_nice_subarrays(nums, k):
    # Transform: odd numbers → 1, even numbers → 0
    # Problem becomes: count subarrays with sum = k
    binary_array = [1 if x % 2 == 1 else 0 for x in nums]
    return subarray_sum_equals_k(binary_array, k)
```

### Problem-Solving Steps

1. **Identify the Pattern**
   - Read problem carefully for keywords (range, subarray, sum, count, etc.)
   - Check if multiple queries or single pass needed
   - Look for mathematical relationships (divisibility, modulo, etc.)

2. **Choose the Right Template**
   - Use decision flowchart to select appropriate template
   - Consider time/space complexity requirements
   - Check if transformation needed before applying prefix sum

3. **Handle Edge Cases**
   - Empty arrays or single elements
   - Negative numbers (especially for modulo operations)
   - Integer overflow for large sums
   - Zero values and their impact on divisibility

4. **Optimize Implementation**
   - Initialize HashMap with base case (usually `{0: 1}`)
   - Handle negative remainders in modulo operations
   - Use one-pass algorithm when possible
   - Consider space optimization if only counts needed

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- **Forgetting base case**: Not initializing HashMap with `{0: 1}` for subarray problems
- **Off-by-one errors**: Incorrect indexing in prefix sum arrays
- **Negative remainders**: Not handling `remainder < 0` in modulo operations  
- **HashMap timing**: Adding to map before vs after checking condition
- **2D indexing**: Confusing row/column indices in 2D prefix sum
- **Range updates**: Forgetting to subtract at `end+1` in difference array

**✅ Best Practices:**
- **Always** initialize prefix sum arrays with size `n+1` for 1-based indexing
- **Always** add `{0: 1}` to HashMap for subarray problems to handle edge cases
- **Double-check** the order: check condition first, then update HashMap
- **Handle negatives**: Use `remainder = (remainder % k + k) % k` for modulo
- **Validate bounds**: Check array bounds when using `end+1` indexing
- **Test edge cases**: Empty arrays, single elements, all negatives

### Interview Tips

1. **Pattern Recognition**
   - If you see "subarray sum equals K" → immediate HashMap + prefix sum
   - If you see "range queries" → basic prefix sum array
   - If you see "divisible by K" → modulo technique with HashMap
   - If you see "multiple range updates" → difference array

2. **Communication Strategy**
   - Explain the mathematical insight: "We're looking for pairs of prefix sums"
   - Draw examples showing how prefix sums work
   - Mention time complexity improvement: "This reduces O(n²) to O(n)"
   - Discuss space-time tradeoffs

3. **Implementation Tips**
   - Start with brute force to verify understanding
   - Then optimize using appropriate prefix sum template
   - Explain why HashMap initialization matters
   - Walk through a small example step by step

4. **Follow-up Discussions**
   - Discuss variations: "What if we need maximum length instead of count?"
   - Explain extension to 2D: "How would this work for matrices?"
   - Consider constraints: "What if numbers are very large?" (overflow)

### Related Topics

- **HashMap/Hash Table**: Essential for most advanced prefix sum problems
- **Sliding Window**: Can be combined with prefix sum for optimization
- **Two Sum**: Many prefix sum problems are extensions of two sum
- **Dynamic Programming**: Prefix sums often used as DP optimization
- **Binary Search**: Can be combined with prefix sum for range queries
- **Segment Trees**: Alternative for range sum with updates
- **Monotonic Stack**: Sometimes combined with prefix sum for optimization

### Advanced Extensions

- **Sparse Arrays**: Use coordinate compression with prefix sum
- **Online Queries**: Segment trees or Fenwick trees for updates + queries
- **2D Range Updates**: 2D difference arrays with 2D prefix sum
- **Weighted Prefix Sum**: Handle different weights for elements
- **Circular Arrays**: Modify templates to handle circular conditions

This comprehensive cheatsheet covers all major prefix sum patterns and provides systematic approaches for solving 40+ LeetCode problems efficiently.

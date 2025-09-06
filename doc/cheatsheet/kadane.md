# KADANE ALGORITHM

- **Core idea**: Find maximum sum of contiguous subarray in O(n) time using dynamic programming approach
- **When to use it**: Maximum subarray sum, optimization problems on arrays, sliding window maximum sum
- **Key LeetCode problems**: LC 53, LC 152, LC 918, LC 1186  
- **Data structures commonly used**: Array, variables to track current/global max
- **Typical states**: Current maximum ending here vs global maximum so far

## 0) Concept  

### 0-1) Types
- **Maximum Subarray Sum**: Classic Kadane's algorithm (LC 53)
- **Maximum Product Subarray**: Modified for multiplication (LC 152) 
- **Circular Array Maximum**: Handle wraparound cases (LC 918)
- **Maximum with At Most One Deletion**: Allow removing one element (LC 1186)
- **2D Kadane's**: Extended to matrices for maximum rectangle sum

### 0-2) Algorithm Pattern / Template

**Basic Kadane's Algorithm:**
```python
def kadane(nums):
    if not nums:
        return 0
    
    current_max = global_max = nums[0]
    
    for i in range(1, len(nums)):
        # At each position: extend current subarray OR start new subarray  
        current_max = max(nums[i], current_max + nums[i])
        global_max = max(global_max, current_max)
    
    return global_max
```

**Key Decision at Each Step:**
- `current_max = max(nums[i], current_max + nums[i])`
  - `nums[i]`: Start new subarray from current element
  - `current_max + nums[i]`: Extend existing subarray

**Important Edge Cases:**
- All negative numbers → return the maximum single element
- Empty array → handle based on problem requirements
- Single element → return that element

## 1) Example Problems  

- **LC 53** – Maximum Subarray (Classic Kadane's - find maximum sum of contiguous subarray)
- **LC 152** – Maximum Product Subarray (Track both max and min products due to negative numbers)
- **LC 918** – Maximum Sum Circular Subarray (Handle circular array with two cases: normal max vs circular max)
- **LC 1186** – Maximum Subarray Sum with One Deletion (Allow deleting at most one element)
- **LC 121** – Best Time to Buy and Sell Stock (Maximum difference/profit subarray problem)
- **LC 1191** – K-Concatenation Maximum Sum (Kadane's on concatenated arrays)

## 2) Implementation Variants

### 2-1) Classic Kadane's (LC 53)
```python
def maxSubArray(nums):
    current_sum = max_sum = nums[0]
    
    for i in range(1, len(nums)):
        current_sum = max(nums[i], current_sum + nums[i])
        max_sum = max(max_sum, current_sum)
    
    return max_sum
```

### 2-2) With Index Tracking
```python
def maxSubArrayWithIndices(nums):
    current_sum = max_sum = nums[0]
    start = end = temp_start = 0
    
    for i in range(1, len(nums)):
        if current_sum < 0:
            current_sum = nums[i]
            temp_start = i
        else:
            current_sum += nums[i]
            
        if current_sum > max_sum:
            max_sum = current_sum
            start = temp_start
            end = i
    
    return max_sum, start, end
```

### 2-3) Maximum Product Subarray (LC 152)
```python
def maxProduct(nums):
    if not nums:
        return 0
    
    max_prod = min_prod = result = nums[0]
    
    for i in range(1, len(nums)):
        if nums[i] < 0:
            # Swap max and min when multiplying by negative
            max_prod, min_prod = min_prod, max_prod
            
        max_prod = max(nums[i], max_prod * nums[i])
        min_prod = min(nums[i], min_prod * nums[i])
        
        result = max(result, max_prod)
    
    return result
```

### 2-4) Circular Maximum Subarray (LC 918)
```python
def maxSubarraySumCircular(nums):
    def kadane(arr):
        current = maximum = arr[0]
        for i in range(1, len(arr)):
            current = max(arr[i], current + arr[i])
            maximum = max(maximum, current)
        return maximum
    
    # Case 1: Maximum subarray is normal (non-circular)
    max_normal = kadane(nums)
    
    # Case 2: Maximum subarray is circular
    # Circular max = Total sum - minimum subarray
    total_sum = sum(nums)
    
    # Find minimum subarray (negate array and find max)
    negated = [-x for x in nums]
    max_negated = kadane(negated)
    min_subarray = -max_negated
    
    max_circular = total_sum - min_subarray
    
    # Edge case: if all numbers are negative
    if max_circular == 0:
        return max_normal
    
    return max(max_normal, max_circular)
```

## 3) Tips & Pitfalls

### ✅ **Tips & Optimizations**
1. **Reset Strategy**: When `current_sum` becomes negative, reset to current element
2. **Space Optimization**: Only need O(1) extra space, no need for DP array
3. **Negative Numbers**: Handle all-negative arrays by returning maximum single element
4. **Product Variants**: Track both maximum and minimum for multiplication problems
5. **Circular Arrays**: Consider both normal and circular cases separately

### ❌ **Common Mistakes**
1. **Forgetting Edge Cases**: Not handling empty arrays or single elements
2. **All Negative Arrays**: Incorrectly returning 0 instead of maximum element
3. **Product Problems**: Not tracking minimum values (important when multiplying negatives)
4. **Index Tracking**: Off-by-one errors when tracking start/end positions
5. **Circular Arrays**: Missing the edge case where minimum subarray equals total array

### **Space vs Time Tradeoffs**
- **Time**: O(n) - single pass through array
- **Space**: O(1) - only need few variables
- **When to use DP array**: If you need to reconstruct the actual subarray or track intermediate states

### **Key Insights**
- At each position, decide: "Should I extend the current subarray or start fresh?"
- Current sum < 0 means starting fresh is better
- Global maximum tracks the best subarray seen so far
- For products: negative numbers can turn small values into large ones (track min/max)

### **Related Patterns**
- **Sliding Window**: When dealing with fixed-size subarrays
- **Prefix Sum**: When dealing with range sum queries
- **Dynamic Programming**: Kadane's is essentially 1D DP optimization
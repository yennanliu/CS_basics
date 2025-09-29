# Advanced Divide and Conquer

## Overview
**Divide and Conquer** is a powerful algorithmic paradigm that breaks complex problems into smaller subproblems, solves them recursively, and combines the results. This approach is particularly effective for inversion counting, range queries, and merge-based operations.

### Key Properties
- **Time Complexity**: Typically O(n log n) for most problems
- **Space Complexity**: O(n) for auxiliary arrays, O(log n) for recursion stack
- **Core Idea**: Divide problem into halves, solve recursively, merge results
- **When to Use**: Counting inversions, range problems, merge operations
- **Key Technique**: Modified merge sort with custom merge logic

### Core Characteristics
- **Divide**: Split problem into smaller subproblems
- **Conquer**: Solve subproblems recursively
- **Combine**: Merge solutions to get final answer
- **Optimal Substructure**: Problem can be broken down optimally
- **Merge Logic**: Custom combine step for specific requirements

## Problem Categories

### **Category 1: Inversion Counting**
- **Description**: Count pairs where left element > right element
- **Examples**: LC 315 (Count Smaller After Self), LC 493 (Reverse Pairs), LC 327 (Count Range Sum)
- **Pattern**: Modified merge sort with counting during merge

### **Category 2: Range Sum Problems**
- **Description**: Count elements/subarrays in specific ranges
- **Examples**: LC 327 (Count of Range Sum), LC 493 (Reverse Pairs with condition)
- **Pattern**: Prefix sums + divide and conquer

### **Category 3: Array Reconstruction**
- **Description**: Build arrays with specific ordering properties
- **Examples**: LC 1649 (Create Sorted Array), LC 2426 (Pairs Satisfying Inequality)
- **Pattern**: Merge sort with reconstruction logic

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Time Complexity | When to Use |
|---------------|----------|-----------------|-------------|
| **Basic Inversion Count** | Count inversions | O(n log n) | Simple inversion problems |
| **Conditional Inversion** | Count with conditions | O(n log n) | Reverse pairs, range conditions |
| **Range Query D&C** | Range sum counting | O(n log n) | Subarray sum problems |
| **Reconstruction D&C** | Build sorted arrays | O(n log n) | Array construction problems |

### Template 1: Basic Inversion Counting
```python
def count_inversions(arr):
    """Count total number of inversions using merge sort"""
    def merge_and_count(arr, temp, left, mid, right):
        i, j, k = left, mid + 1, left
        inv_count = 0

        # Merge with inversion counting
        while i <= mid and j <= right:
            if arr[i] <= arr[j]:
                temp[k] = arr[i]
                i += 1
            else:
                temp[k] = arr[j]
                # Count inversions: all elements from i to mid are > arr[j]
                inv_count += (mid - i + 1)
                j += 1
            k += 1

        # Copy remaining elements
        while i <= mid:
            temp[k] = arr[i]
            i += 1
            k += 1
        while j <= right:
            temp[k] = arr[j]
            j += 1
            k += 1

        # Copy back to original array
        for i in range(left, right + 1):
            arr[i] = temp[i]

        return inv_count

    def merge_sort_and_count(arr, temp, left, right):
        inv_count = 0
        if left < right:
            mid = (left + right) // 2
            inv_count += merge_sort_and_count(arr, temp, left, mid)
            inv_count += merge_sort_and_count(arr, temp, mid + 1, right)
            inv_count += merge_and_count(arr, temp, left, mid, right)
        return inv_count

    temp = [0] * len(arr)
    return merge_sort_and_count(arr[:], temp, 0, len(arr) - 1)
```

### Template 2: Conditional Inversion Counting
```python
def count_reverse_pairs(nums):
    """Count pairs where nums[i] > 2 * nums[j] for i < j"""
    def merge_and_count(nums, temp, left, mid, right):
        # Count reverse pairs first (before sorting)
        count = 0
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)

        # Now perform regular merge
        i, j, k = left, mid + 1, left
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp[k] = nums[i]
                i += 1
            else:
                temp[k] = nums[j]
                j += 1
            k += 1

        while i <= mid:
            temp[k] = nums[i]
            i += 1
            k += 1
        while j <= right:
            temp[k] = nums[j]
            j += 1
            k += 1

        for i in range(left, right + 1):
            nums[i] = temp[i]

        return count

    def merge_sort_and_count(nums, temp, left, right):
        count = 0
        if left < right:
            mid = (left + right) // 2
            count += merge_sort_and_count(nums, temp, left, mid)
            count += merge_sort_and_count(nums, temp, mid + 1, right)
            count += merge_and_count(nums, temp, left, mid, right)
        return count

    temp = [0] * len(nums)
    return merge_sort_and_count(nums[:], temp, 0, len(nums) - 1)
```

### Template 3: Range Sum Divide and Conquer
```python
def count_range_sum(nums, lower, upper):
    """Count subarrays with sum in [lower, upper]"""
    def merge_and_count(prefix_sums, temp, left, mid, right):
        count = 0
        j = k = mid + 1

        # For each prefix sum in left half
        for i in range(left, mid + 1):
            # Find range [j, k) where prefix_sums[j] - prefix_sums[i] is in [lower, upper]
            while j <= right and prefix_sums[j] - prefix_sums[i] < lower:
                j += 1
            while k <= right and prefix_sums[k] - prefix_sums[i] <= upper:
                k += 1
            count += k - j

        # Regular merge
        i, j, p = left, mid + 1, left
        while i <= mid and j <= right:
            if prefix_sums[i] <= prefix_sums[j]:
                temp[p] = prefix_sums[i]
                i += 1
            else:
                temp[p] = prefix_sums[j]
                j += 1
            p += 1

        while i <= mid:
            temp[p] = prefix_sums[i]
            i += 1
            p += 1
        while j <= right:
            temp[p] = prefix_sums[j]
            j += 1
            p += 1

        for i in range(left, right + 1):
            prefix_sums[i] = temp[i]

        return count

    def divide_and_conquer(prefix_sums, temp, left, right):
        if left >= right:
            return 0

        mid = (left + right) // 2
        count = divide_and_conquer(prefix_sums, temp, left, mid)
        count += divide_and_conquer(prefix_sums, temp, mid + 1, right)
        count += merge_and_count(prefix_sums, temp, left, mid, right)
        return count

    # Build prefix sums
    prefix_sums = [0]
    for num in nums:
        prefix_sums.append(prefix_sums[-1] + num)

    temp = [0] * len(prefix_sums)
    return divide_and_conquer(prefix_sums, temp, 0, len(prefix_sums) - 1)
```

### Template 4: Array Reconstruction
```python
def create_sorted_array(instructions):
    """Create sorted array with minimum cost"""
    def merge_and_count(arr, temp, left, mid, right):
        smaller_count = [0] * len(arr)
        larger_count = [0] * len(arr)

        # Count smaller and larger elements during merge
        i, j = left, mid + 1
        for k in range(left, right + 1):
            if i > mid:
                temp[k] = arr[j]
                j += 1
            elif j > right:
                temp[k] = arr[i]
                # Count how many elements from right half are smaller
                smaller_count[arr[i][1]] += (right - mid)
                i += 1
            elif arr[i][0] <= arr[j][0]:
                temp[k] = arr[i]
                smaller_count[arr[i][1]] += (j - mid - 1)
                i += 1
            else:
                temp[k] = arr[j]
                j += 1

        # Copy back
        for k in range(left, right + 1):
            arr[k] = temp[k]

        return smaller_count, larger_count

    def merge_sort_with_count(arr, temp, left, right):
        if left >= right:
            return

        mid = (left + right) // 2
        merge_sort_with_count(arr, temp, left, mid)
        merge_sort_with_count(arr, temp, mid + 1, right)
        merge_and_count(arr, temp, left, mid, right)

    # Implementation depends on specific problem
    # This is a general framework for array reconstruction
    pass
```

## Problems by Pattern

### **Inversion Counting Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Count of Smaller Numbers After Self | 315 | Modified merge sort | Hard |
| Reverse Pairs | 493 | Conditional inversion count | Hard |
| Count of Range Sum | 327 | Prefix sum + D&C | Hard |
| Create Sorted Array through Instructions | 1649 | Dynamic inversion counting | Hard |

### **Range Query Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Count of Range Sum | 327 | Prefix sum merging | Hard |
| Number of Pairs Satisfying Inequality | 2426 | Range condition D&C | Hard |

## LC Examples

### 1. Count of Smaller Numbers After Self (LC 315)
```python
def countSmaller(nums):
    """Count how many numbers after each element are smaller"""
    def merge_and_count(indices, temp, left, mid, right):
        # Count smaller elements to the right
        i, j, k = left, mid + 1, left

        while i <= mid and j <= right:
            if nums[indices[i]] <= nums[indices[j]]:
                temp[k] = indices[i]
                # All elements from mid+1 to j-1 are smaller than nums[indices[i]]
                counts[indices[i]] += (j - mid - 1)
                i += 1
            else:
                temp[k] = indices[j]
                j += 1
            k += 1

        # Process remaining elements
        while i <= mid:
            temp[k] = indices[i]
            counts[indices[i]] += (j - mid - 1)
            i += 1
            k += 1
        while j <= right:
            temp[k] = indices[j]
            j += 1
            k += 1

        # Copy back
        for i in range(left, right + 1):
            indices[i] = temp[i]

    def merge_sort(indices, temp, left, right):
        if left >= right:
            return

        mid = (left + right) // 2
        merge_sort(indices, temp, left, mid)
        merge_sort(indices, temp, mid + 1, right)
        merge_and_count(indices, temp, left, mid, right)

    n = len(nums)
    counts = [0] * n
    indices = list(range(n))
    temp = [0] * n

    merge_sort(indices, temp, 0, n - 1)
    return counts
```

### 2. Reverse Pairs (LC 493)
```python
def reversePairs(nums):
    """Count pairs where nums[i] > 2 * nums[j] for i < j"""
    def merge_and_count(nums, temp, left, mid, right):
        # Count reverse pairs first
        count = 0
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)

        # Regular merge
        i, j, k = left, mid + 1, left
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp[k] = nums[i]
                i += 1
            else:
                temp[k] = nums[j]
                j += 1
            k += 1

        while i <= mid:
            temp[k] = nums[i]
            i += 1
            k += 1
        while j <= right:
            temp[k] = nums[j]
            j += 1
            k += 1

        for i in range(left, right + 1):
            nums[i] = temp[i]

        return count

    def merge_sort_and_count(nums, temp, left, right):
        count = 0
        if left < right:
            mid = (left + right) // 2
            count += merge_sort_and_count(nums, temp, left, mid)
            count += merge_sort_and_count(nums, temp, mid + 1, right)
            count += merge_and_count(nums, temp, left, mid, right)
        return count

    temp = [0] * len(nums)
    return merge_sort_and_count(nums[:], temp, 0, len(nums) - 1)
```

### 3. Count of Range Sum (LC 327)
```python
def countRangeSum(nums, lower, upper):
    """Count subarrays with sum in [lower, upper]"""
    def merge_and_count(prefix_sums, temp, left, mid, right):
        count = 0
        j = k = mid + 1

        for i in range(left, mid + 1):
            # Find range where prefix_sums[x] - prefix_sums[i] is in [lower, upper]
            while j <= right and prefix_sums[j] - prefix_sums[i] < lower:
                j += 1
            while k <= right and prefix_sums[k] - prefix_sums[i] <= upper:
                k += 1
            count += k - j

        # Merge sorted arrays
        i, j, p = left, mid + 1, left
        while i <= mid and j <= right:
            if prefix_sums[i] <= prefix_sums[j]:
                temp[p] = prefix_sums[i]
                i += 1
            else:
                temp[p] = prefix_sums[j]
                j += 1
            p += 1

        while i <= mid:
            temp[p] = prefix_sums[i]
            i += 1
            p += 1
        while j <= right:
            temp[p] = prefix_sums[j]
            j += 1
            p += 1

        for i in range(left, right + 1):
            prefix_sums[i] = temp[i]

        return count

    def divide_and_conquer(prefix_sums, temp, left, right):
        if left >= right:
            return 0

        mid = (left + right) // 2
        count = divide_and_conquer(prefix_sums, temp, left, mid)
        count += divide_and_conquer(prefix_sums, temp, mid + 1, right)
        count += merge_and_count(prefix_sums, temp, left, mid, right)
        return count

    # Build prefix sum array
    prefix_sums = [0]
    for num in nums:
        prefix_sums.append(prefix_sums[-1] + num)

    temp = [0] * len(prefix_sums)
    return divide_and_conquer(prefix_sums, temp, 0, len(prefix_sums) - 1)
```

## Advanced Techniques

### Optimization Strategies
```python
def divide_and_conquer_optimizations():
    """Various optimization techniques for D&C"""

    # 1. In-place operations to reduce space
    def in_place_merge(arr, left, mid, right):
        # Reduce auxiliary space usage
        pass

    # 2. Iterative bottom-up approach
    def iterative_merge_sort(arr):
        n = len(arr)
        size = 1
        while size < n:
            left = 0
            while left < n - 1:
                mid = min(left + size - 1, n - 1)
                right = min(left + 2 * size - 1, n - 1)
                # merge(arr, left, mid, right)
                left += 2 * size
            size *= 2

    # 3. Parallel divide and conquer
    def parallel_merge_sort(arr):
        # Use threading for large datasets
        pass

    # 4. Hybrid approach with insertion sort for small subarrays
    def hybrid_merge_sort(arr, threshold=10):
        if len(arr) <= threshold:
            return insertion_sort(arr)
        # Regular merge sort for larger arrays
```

### Custom Merge Logic Patterns
```python
class AdvancedMergePatterns:
    """Advanced merge logic for specific problems"""

    def merge_with_multiple_conditions(self, arr1, arr2):
        """Merge with multiple counting conditions"""
        result = []
        i = j = 0
        counts = {"condition1": 0, "condition2": 0}

        while i < len(arr1) and j < len(arr2):
            if self.condition1(arr1[i], arr2[j]):
                counts["condition1"] += len(arr2) - j
            if self.condition2(arr1[i], arr2[j]):
                counts["condition2"] += len(arr2) - j

            if arr1[i] <= arr2[j]:
                result.append(arr1[i])
                i += 1
            else:
                result.append(arr2[j])
                j += 1

        return result + arr1[i:] + arr2[j:], counts

    def merge_with_reconstruction(self, left_part, right_part):
        """Merge while reconstructing array with specific properties"""
        # Custom merge logic for array reconstruction problems
        pass
```

## Performance Optimization Tips

### Time Complexity Analysis
```python
def complexity_analysis():
    """Analyze time complexity of different D&C approaches"""

    # Standard divide and conquer: T(n) = 2T(n/2) + O(n) = O(n log n)
    # With k-way division: T(n) = kT(n/k) + O(n) = O(n log n) if k is constant
    # With additional work per level: T(n) = 2T(n/2) + O(n^c)
    #   - If c < 1: O(n log n)
    #   - If c = 1: O(n log n)
    #   - If c > 1: O(n^c)

    pass
```

### Space Optimization
```python
def space_optimizations():
    """Techniques to reduce space complexity"""

    # 1. Reuse auxiliary arrays
    def reuse_temp_array(arr):
        temp = [0] * len(arr)  # Create once, reuse everywhere
        # Pass temp to all recursive calls

    # 2. In-place merge (complex but saves space)
    def in_place_merge_technique(arr, left, mid, right):
        # Advanced in-place merging algorithms
        pass

    # 3. Iterative approach to eliminate recursion stack
    def iterative_divide_conquer(arr):
        # Bottom-up approach to save stack space
        pass
```

## Summary & Quick Reference

### Common D&C Patterns

| Pattern | Template | Use Case | Example |
|---------|----------|----------|---------|
| **Basic Inversion** | Modified merge sort | Count inversions | Simple inversion count |
| **Conditional Count** | Merge with conditions | Specific pair conditions | Reverse pairs |
| **Range Queries** | Prefix sum + D&C | Range sum problems | Count range sum |
| **Reconstruction** | Merge with building | Array construction | Sorted array creation |

### Time Complexity Guide
| Problem Type | Time Complexity | Space Complexity | Notes |
|--------------|-----------------|------------------|-------|
| Basic Inversion | O(n log n) | O(n) | Standard merge sort |
| Conditional Inversion | O(n log n) | O(n) | Additional condition checks |
| Range Sum | O(n log n) | O(n) | Prefix sum preprocessing |
| Reconstruction | O(n log n) | O(n) | May need additional structures |

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Forgetting to handle edge cases in merge step
- Incorrect index management during merge
- Not preserving original array when needed
- Inefficient condition checking in merge

**✅ Best Practices:**
- Always use auxiliary array for merge operations
- Handle left and right boundaries carefully
- Optimize condition checking in merge step
- Consider iterative approach for very large inputs
- Use stable sorting when relative order matters

### Interview Tips
1. **Identify D&C opportunities**: Look for inversion counting, range queries
2. **Master merge logic**: The key is in the custom merge step
3. **Handle indices carefully**: Off-by-one errors are common
4. **Consider space-time tradeoffs**: Auxiliary space vs. in-place operations
5. **Practice merge variations**: Different counting/reconstruction logic
6. **Test edge cases**: Empty arrays, single elements, duplicate values

This comprehensive divide and conquer cheatsheet covers the most important patterns and techniques for solving complex counting and range query problems efficiently.
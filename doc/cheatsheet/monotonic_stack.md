---
layout: cheatsheet
title: "Monotonic Stack"
description: "Specialized stack maintaining monotonic order for optimization problems"
category: "Data Structure"
difficulty: "Hard"
tags: ["monotonic-stack", "stack", "optimization", "histogram"]
patterns:
  - Next greater/smaller element
  - Histogram problems
  - Sequence optimization
---

# Monotonic Stack Data Structure

## Overview
**Monotonic Stack** is a specialized stack data structure that maintains a monotonic (either strictly increasing or strictly decreasing) order of elements. It efficiently solves problems related to finding next greater/smaller elements, histogram areas, and sequence optimization problems.

### Key Properties
- **Time Complexity**: O(n) for most operations (each element pushed/popped once)
- **Space Complexity**: O(n) for the stack storage
- **Core Idea**: Maintain monotonic order while processing elements sequentially
- **When to Use**: Finding next/previous greater/smaller elements, histogram problems, sequence optimization

### References
- [LeetCode Monotonic Stack Pattern](https://leetcode.com/tag/monotonic-stack/)
- [Stack Data Structure Fundamentals](https://en.wikipedia.org/wiki/Stack_(abstract_data_type))

## Problem Categories

### **Pattern 1: Next/Previous Greater Element**
- **Description**: Find the next or previous element that is greater than current element
- **Examples**: LC 496 (Next Greater Element I), LC 503 (Next Greater Element II), LC 739 (Daily Temperatures)
- **Pattern**: Use decreasing monotonic stack, pop when finding greater element

### **Pattern 2: Next/Previous Smaller Element** 
- **Description**: Find the next or previous element that is smaller than current element
- **Examples**: LC 84 (Largest Rectangle), LC 42 (Trapping Rain Water), LC 907 (Sum of Subarray Minimums)
- **Pattern**: Use increasing monotonic stack, pop when finding smaller element

### **Pattern 3: Histogram and Area Problems**
- **Description**: Calculate areas, rectangles, or volumes using height information
- **Examples**: LC 84 (Largest Rectangle in Histogram), LC 42 (Trapping Rain Water), LC 85 (Maximal Rectangle)
- **Pattern**: Find boundaries using monotonic stack, calculate areas between boundaries

### **Pattern 4: Sequence Order and Validation**
- **Description**: Validate sequences, find patterns, or maintain order constraints
- **Examples**: LC 456 (132 Pattern), LC 901 (Online Stock Span), LC 1856 (Maximum Subarray Min-Product)
- **Pattern**: Use stack to maintain sequence properties and validate patterns

### **Pattern 5: Optimization and Maximum/Minimum**
- **Description**: Find optimal solutions involving maximum or minimum constraints
- **Examples**: LC 1944 (Number of Visible People), LC 2104 (Sum of Subarray Ranges), LC 1793 (Maximum Score)
- **Pattern**: Use monotonic properties to maintain optimal candidates

### **Pattern 6: Circular Arrays**
- **Description**: Handle circular or cyclic array problems
- **Examples**: LC 503 (Next Greater Element II), LC 853 (Car Fleet II)
- **Pattern**: Process array twice or use modular arithmetic with monotonic stack

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Stack Order | When to Use |
|---------------|----------|-------------|-------------|
| **Decreasing Stack** | Next/Previous Greater | Decreasing | Find elements greater than current |
| **Increasing Stack** | Next/Previous Smaller | Increasing | Find elements smaller than current |
| **Histogram Area** | Rectangle/Area Problems | Increasing | Calculate areas using heights |
| **Circular Array** | Cyclic Problems | Varies | Process circular sequences |
| **Pattern Validation** | Sequence Validation | Varies | Validate specific patterns |
| **Optimization Stack** | Max/Min Problems | Varies | Maintain optimal candidates |

### Universal Template

```python
def monotonic_stack_template(arr):
    """
    Universal template for monotonic stack problems
    Modify the condition and processing logic based on problem requirements
    """
    stack = []  # Store indices or values
    result = []
    
    for i, val in enumerate(arr):
        # Pop elements that violate monotonic property
        while stack and should_pop(stack, val, i):
            # Process the popped element
            popped = stack.pop()
            process_popped_element(popped, i, result)
        
        # Add current element to stack
        stack.append(i)  # or val depending on problem
    
    # Process remaining elements in stack
    while stack:
        popped = stack.pop()
        process_remaining_element(popped, result)
    
    return result

def should_pop(stack, current_val, current_idx):
    """Define when to pop based on problem requirements"""
    # For next greater: return arr[stack[-1]] <= current_val
    # For next smaller: return arr[stack[-1]] >= current_val
    pass

def process_popped_element(popped_idx, current_idx, result):
    """Process element when it's popped (found its next greater/smaller)"""
    pass

def process_remaining_element(popped_idx, result):
    """Process elements remaining in stack at the end"""
    pass
```

```java
// Java Universal Template
public int[] monotonicStackTemplate(int[] arr) {
    Stack<Integer> stack = new Stack<>();
    int[] result = new int[arr.length];
    
    for (int i = 0; i < arr.length; i++) {
        // Pop elements that violate monotonic property
        while (!stack.isEmpty() && shouldPop(stack, arr, i)) {
            int poppedIdx = stack.pop();
            processElement(poppedIdx, i, result, arr);
        }
        
        // Add current element to stack
        stack.push(i);
    }
    
    // Process remaining elements
    while (!stack.isEmpty()) {
        int poppedIdx = stack.pop();
        processRemainingElement(poppedIdx, result);
    }
    
    return result;
}

private boolean shouldPop(Stack<Integer> stack, int[] arr, int currentIdx) {
    // Define condition based on problem requirements
    return arr[stack.peek()] <= arr[currentIdx]; // For next greater
}
```

### Template 1: Next Greater Element (Decreasing Stack)

```python
def next_greater_element(nums):
    """
    Find next greater element for each element
    LC 496, LC 503, LC 739
    """
    n = len(nums)
    result = [-1] * n
    stack = []  # Store indices
    
    for i in range(n):
        # Pop smaller or equal elements
        while stack and nums[stack[-1]] < nums[i]:
            idx = stack.pop()
            result[idx] = nums[i]  # Found next greater
        
        stack.append(i)
    
    return result
```

```java
// Java Template 1
public int[] nextGreaterElement(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    Arrays.fill(result, -1);
    Stack<Integer> stack = new Stack<>();
    
    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
            result[stack.pop()] = nums[i];
        }
        stack.push(i);
    }
    
    return result;
}
```

### Template 2: Next Smaller Element (Increasing Stack)

```python
def next_smaller_element(nums):
    """
    Find next smaller element for each element
    Used in LC 84, LC 42
    """
    n = len(nums)
    result = [-1] * n
    stack = []  # Store indices
    
    for i in range(n):
        # Pop greater or equal elements
        while stack and nums[stack[-1]] > nums[i]:
            idx = stack.pop()
            result[idx] = nums[i]  # Found next smaller
        
        stack.append(i)
    
    return result
```

### Template 3: Largest Rectangle in Histogram

```python
def largest_rectangle_area(heights):
    """
    Find largest rectangle area in histogram
    LC 84, LC 85
    """
    stack = []  # Store indices
    max_area = 0
    heights.append(0)  # Add sentinel
    
    for i, h in enumerate(heights):
        # Pop taller bars and calculate area
        while stack and heights[stack[-1]] > h:
            height = heights[stack.pop()]
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        
        stack.append(i)
    
    return max_area
```

```java
// Java Template 3
public int largestRectangleArea(int[] heights) {
    Stack<Integer> stack = new Stack<>();
    int maxArea = 0;
    int n = heights.length;
    
    for (int i = 0; i <= n; i++) {
        int h = (i == n) ? 0 : heights[i];
        
        while (!stack.isEmpty() && heights[stack.peek()] > h) {
            int height = heights[stack.pop()];
            int width = stack.isEmpty() ? i : i - stack.peek() - 1;
            maxArea = Math.max(maxArea, height * width);
        }
        
        stack.push(i);
    }
    
    return maxArea;
}
```

### Template 4: Circular Array Processing

```python
def next_greater_circular(nums):
    """
    Find next greater element in circular array
    LC 503
    """
    n = len(nums)
    result = [-1] * n
    stack = []
    
    # Process array twice to handle circular nature
    for i in range(2 * n):
        # Pop smaller elements
        while stack and nums[stack[-1]] < nums[i % n]:
            idx = stack.pop()
            result[idx] = nums[i % n]
        
        # Only add indices from first pass
        if i < n:
            stack.append(i)
    
    return result
```

### Template 5: Stack with Additional Information

```python
def monotonic_stack_with_info(nums):
    """
    Store additional information with stack elements
    Used for complex calculations
    """
    stack = []  # Store (index, value, additional_info)
    result = []
    
    for i, val in enumerate(nums):
        while stack and stack[-1][1] <= val:
            idx, old_val, info = stack.pop()
            # Process with additional information
            result.append(calculate_result(idx, i, old_val, val, info))
        
        # Calculate additional information for current element
        additional_info = calculate_info(val, stack)
        stack.append((i, val, additional_info))
    
    return result
```

### Template 6: Pattern Validation (132 Pattern)

```python
def find_132_pattern(nums):
    """
    Find 132 pattern in array
    LC 456
    """
    n = len(nums)
    if n < 3:
        return False
    
    stack = []  # Store potential k values (decreasing)
    second = float('-inf')  # The "2" in 132 pattern
    
    # Traverse from right to left
    for i in range(n - 1, -1, -1):
        if nums[i] < second:  # Found "1" < "2"
            return True
        
        # Pop smaller values and update second
        while stack and stack[-1] < nums[i]:
            second = stack.pop()
        
        stack.append(nums[i])
    
    return False
```

## Problems by Pattern

### Pattern-Based Problem Classification

#### **Pattern 1: Next/Previous Greater Element Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Next Greater Element I | 496 | Decreasing stack | Easy | Template 1 |
| Next Greater Element II | 503 | Circular array | Medium | Template 4 |
| Daily Temperatures | 739 | Distance calculation | Medium | Template 1 |
| Remove K Digits | 402 | Greedy + stack | Medium | Template 1 |
| Remove Duplicate Letters | 316 | Lexicographical + stack | Medium | Template 1 |
| Sliding Window Maximum | 239 | Monotonic deque | Hard | Template 1 |
| Shortest Unsorted Array | 581 | Two-pass stack | Medium | Template 1 |
| Sum of Subarray Ranges | 2104 | Next greater + smaller | Medium | Template 1+2 |

#### **Pattern 2: Next/Previous Smaller Element Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Largest Rectangle in Histogram | 84 | Area calculation | Hard | Template 3 |
| Maximal Rectangle | 85 | 2D histogram | Hard | Template 3 |
| Sum of Subarray Minimums | 907 | Contribution method | Medium | Template 2 |
| Number of Valid Subarrays | 1063 | Smaller element count | Medium | Template 2 |
| Minimum Cost Tree From Leaf Values | 1130 | Optimal merging | Medium | Template 2 |
| Find the Most Competitive Subsequence | 1673 | Subsequence selection | Medium | Template 2 |
| Maximum Subarray Min-Product | 1856 | Min value as pivot | Medium | Template 2 |

#### **Pattern 3: Histogram and Area Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Trapping Rain Water | 42 | Water level calculation | Hard | Template 2 |
| Container With Most Water | 11 | Two pointers alternative | Medium | Template 2 |
| Maximal Rectangle | 85 | Row-wise histogram | Hard | Template 3 |
| Maximum Rectangle | 221 | DP + histogram | Medium | Template 3 |
| Minimum Number of Taps | 1326 | Interval coverage | Hard | Template 2 |
| Constrained Subsequence Sum | 1425 | DP + monotonic deque | Hard | Template 2 |

#### **Pattern 4: Sequence Order and Validation Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| 132 Pattern | 456 | Pattern detection | Medium | Template 6 |
| Online Stock Span | 901 | Monotonic stack | Medium | Template 1 |
| Score of Parentheses | 856 | Nested structure | Medium | Template 5 |
| Valid Parenthesis String | 678 | Balance validation | Medium | Template 5 |
| Minimum Add to Make Parentheses Valid | 921 | Balance counting | Medium | Template 5 |
| Validate Stack Sequences | 946 | Sequence simulation | Medium | Template 5 |
| Maximum Nesting Depth of Parentheses | 1614 | Depth tracking | Easy | Template 5 |
| Minimum Remove to Make Valid Parentheses | 1249 | Balance + removal | Medium | Template 5 |

#### **Pattern 5: Optimization and Maximum/Minimum Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Maximum Score of Good Subarray | 1793 | Two pointers + stack | Hard | Template 2 |
| Number of Visible People in Queue | 1944 | Line of sight | Medium | Template 1 |
| Car Fleet | 853 | Time calculation | Medium | Template 1 |
| Car Fleet II | 1776 | Collision time | Hard | Template 1 |
| Buildings With Ocean View | 1762 | Right-to-left scan | Medium | Template 1 |
| Find the Winner of Circular Game | 1823 | Josephus problem | Medium | Template 4 |
| Maximum Width Ramp | 962 | Index difference | Medium | Template 1 |
| Pancake Sorting | 969 | Reverse operations | Medium | Template 1 |

#### **Pattern 6: Circular Array Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Next Greater Element II | 503 | Double array traversal | Medium | Template 4 |
| Car Fleet II | 1776 | Circular collision | Hard | Template 4 |
| Circular Array Loop | 457 | Cycle detection | Medium | Template 4 |
| Design Circular Queue | 622 | Circular buffer | Medium | Template 4 |
| Design Circular Deque | 641 | Double-ended circular | Medium | Template 4 |

#### **Advanced/Mixed Pattern Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Sum of Total Strength of Wizards | 2281 | Multiple stacks | Hard | Multiple |
| Number of Ways to Rearrange Sticks | 1866 | Combinatorics + stack | Hard | Template 5 |
| Basic Calculator | 224 | Expression evaluation | Hard | Template 5 |
| Basic Calculator II | 227 | Operator precedence | Medium | Template 5 |
| Basic Calculator III | 772 | Full expression parsing | Hard | Template 5 |
| Evaluate Reverse Polish Notation | 150 | Postfix evaluation | Medium | Template 5 |
| Decode String | 394 | Nested decoding | Medium | Template 5 |
| Find Duplicate Subtrees | 652 | Tree serialization | Medium | Template 5 |
| Exclusive Time of Functions | 636 | Call stack simulation | Medium | Template 5 |
| Minimum Window Subsequence | 727 | Two pointers + stack | Hard | Template 5 |

### Problem Difficulty Distribution
- **Easy (8 problems)**: Basic next greater/smaller, simple validations
- **Medium (28 problems)**: Most common difficulty, various patterns
- **Hard (16 problems)**: Complex area calculations, advanced optimizations

### Template Usage Frequency
- **Template 1 (Next Greater)**: 15 problems
- **Template 2 (Next Smaller)**: 12 problems  
- **Template 3 (Histogram)**: 8 problems
- **Template 4 (Circular)**: 6 problems
- **Template 5 (Validation/Complex)**: 11 problems
- **Multiple Templates**: 8 problems

## Pattern Selection Strategy

### Decision Framework Flowchart

```
Problem Analysis for Monotonic Stack:

1. Does the problem involve finding next/previous elements?
   ├── YES: Next/Previous GREATER elements?
   │   ├── YES: Use Template 1 (Decreasing Stack)
   │   │   ├── Array is circular? → Use Template 4 (Circular)
   │   │   └── Standard case → Template 1
   │   └── NO: Next/Previous SMALLER elements?
   │       ├── YES: Use Template 2 (Increasing Stack)
   │       └── NO: Continue to step 2
   └── NO: Continue to step 2

2. Does the problem involve heights/areas/rectangles?
   ├── YES: Rectangle area calculation?
   │   ├── YES: Use Template 3 (Histogram)
   │   └── NO: Water trapping/volume?
   │       └── YES: Use Template 2 (Next Smaller)
   └── NO: Continue to step 3

3. Does the problem involve sequence validation/patterns?
   ├── YES: Parentheses/brackets?
   │   ├── YES: Use Template 5 (Validation)
   │   └── NO: Specific pattern (like 132)?
   │       └── YES: Use Template 6 (Pattern Detection)
   └── NO: Continue to step 4

4. Does the problem involve optimization/max-min constraints?
   ├── YES: Multiple criteria optimization?
   │   ├── YES: Use Template 5 (Complex Info)
   │   └── NO: Simple max/min tracking?
   │       └── YES: Use Template 1 or 2
   └── NO: Continue to step 5

5. Does the problem involve circular arrays or cyclic behavior?
   ├── YES: Use Template 4 (Circular Processing)
   └── NO: Consider if monotonic stack is the right approach
       └── May need different data structure/algorithm
```

### Step-by-Step Problem Analysis

1. **Identify the Core Requirement**
   - Next/Previous element queries → Templates 1, 2, 4
   - Area/Rectangle calculations → Template 3
   - Pattern validation → Templates 5, 6
   - Optimization problems → Templates 1, 2, 5

2. **Determine Stack Order**
   - Need greater elements → Decreasing stack (pop smaller)
   - Need smaller elements → Increasing stack (pop greater)
   - Area calculations → Usually increasing stack
   - Pattern detection → Varies by pattern

3. **Choose Processing Direction**
   - Left to right: Most common, natural order
   - Right to left: For "next" elements, sometimes easier
   - Circular: Process array multiple times

4. **Decide What to Store**
   - Indices: When need position information
   - Values: When only need element comparison
   - Tuples: When need additional information

### Template Selection Quick Guide

| Problem Type | Template | Stack Content | Processing Order |
|--------------|----------|---------------|------------------|
| **Next Greater** | Template 1 | Indices | Left to Right |
| **Next Smaller** | Template 2 | Indices | Left to Right |
| **Previous Greater** | Template 1 | Indices | Left to Right |
| **Previous Smaller** | Template 2 | Indices | Left to Right |
| **Histogram Areas** | Template 3 | Indices | Left to Right |
| **Circular Arrays** | Template 4 | Indices | 2x traversal |
| **Pattern Detection** | Template 6 | Values | Right to Left |
| **Complex Validation** | Template 5 | Tuples | Varies |

## Summary & Quick Reference

### Complexity Quick Reference
| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| **Push to Stack** | O(1) | - | Each element pushed once |
| **Pop from Stack** | O(1) | - | Each element popped once |
| **Overall Algorithm** | O(n) | O(n) | Amortized linear time |
| **Next Greater/Smaller** | O(n) | O(n) | Single pass through array |
| **Histogram Area** | O(n) | O(n) | Linear scan with stack |
| **Circular Array** | O(n) | O(n) | Two passes, same complexity |

### Template Quick Reference
| Template | Pattern | Key Code Pattern |
|----------|---------|------------------|
| **Template 1** | Next Greater | `while stack and nums[stack[-1]] < nums[i]` |
| **Template 2** | Next Smaller | `while stack and nums[stack[-1]] > nums[i]` |
| **Template 3** | Histogram | `while stack and heights[stack[-1]] > h` |
| **Template 4** | Circular | `for i in range(2 * n)` |
| **Template 5** | Validation | Store additional info in stack |
| **Template 6** | Pattern Detection | Right-to-left with condition tracking |

### Common Patterns & Tricks

#### **Next Greater Element Pattern**
```python
# Standard next greater element
def next_greater_elements(nums):
    stack, result = [], [-1] * len(nums)
    for i, num in enumerate(nums):
        while stack and nums[stack[-1]] < num:
            result[stack.pop()] = num
        stack.append(i)
    return result
```

#### **Contribution Method for Subarrays**
```python
# Count contribution of each element
def sum_subarray_mins(arr):
    n = len(arr)
    left = [-1] * n    # Previous smaller element
    right = [n] * n    # Next smaller element
    
    # Calculate left boundaries
    stack = []
    for i in range(n):
        while stack and arr[stack[-1]] >= arr[i]:
            stack.pop()
        left[i] = stack[-1] if stack else -1
        stack.append(i)
    
    # Calculate contribution
    result = 0
    for i in range(n):
        result += arr[i] * (i - left[i]) * (right[i] - i)
    return result % (10**9 + 7)
```

#### **Histogram Area Calculation**
```python
# Largest rectangle with height as key
def largest_rectangle_area(heights):
    stack = []
    max_area = 0
    for i, h in enumerate(heights + [0]):
        while stack and heights[stack[-1]] > h:
            height = heights[stack.pop()]
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        stack.append(i)
    return max_area
```

### Problem-Solving Steps

1. **Step 1: Identify Pattern Type**
   - Look for keywords: next/previous, greater/smaller, area, rectangle
   - Check for circular/cyclic requirements
   - Identify if validation or pattern detection is needed

2. **Step 2: Choose Appropriate Template**
   - Use decision framework flowchart
   - Consider stack order (increasing vs decreasing)
   - Determine what information to store in stack

3. **Step 3: Implement Core Logic**
   - Set up stack and result containers
   - Implement while loop with correct popping condition
   - Process popped elements appropriately
   - Handle remaining elements in stack

4. **Step 4: Handle Edge Cases**
   - Empty array
   - Single element array
   - All elements same
   - Strictly increasing/decreasing sequences

5. **Step 5: Optimize and Verify**
   - Ensure O(n) time complexity
   - Check space complexity
   - Verify with sample inputs
   - Handle integer overflow if needed

### Common Mistakes & Tips

#### Common Mistakes
- **Wrong Stack Order**: Using increasing stack for next greater problems (should be decreasing)
- **Index vs Value Confusion**: Storing values when indices are needed for distance calculation
- **Incomplete Processing**: Forgetting to process remaining elements in stack
- **Boundary Issues**: Not handling empty stack cases properly
- **Circular Logic**: Not processing circular arrays correctly (missing second pass)
- **Condition Errors**: Using wrong comparison operators (< vs <=, > vs >=)

#### Best Practices
- **Always store indices** when you need position information
- **Use sentinel values** (like 0) to simplify boundary handling
- **Process from left to right** unless specifically need right-to-left
- **Clear variable names**: `stack`, `result`, `current_idx` instead of `s`, `res`, `i`
- **Comment the while condition** to clarify monotonic property
- **Handle edge cases first** before main algorithm

### Interview Tips

1. **Pattern Recognition**
   - Listen for "next greater/smaller" keywords
   - Area/rectangle problems often use monotonic stacks
   - Sequence validation problems may need stack-based approaches

2. **Problem-Solving Approach**
   - Start with brute force to understand the problem
   - Identify if monotonic property can optimize the solution
   - Draw examples to visualize stack behavior

3. **Communication During Interview**
   - Explain why monotonic stack is appropriate
   - Walk through the stack state with examples
   - Discuss time/space complexity trade-offs

4. **Implementation Tips**
   - Start with the template structure
   - Focus on getting the while condition right
   - Test with simple examples (like [2,1,2,4,3,1])

5. **Follow-up Questions to Expect**
   - How to handle duplicates?
   - What if we need previous instead of next?
   - Can you optimize space complexity?
   - How to extend to 2D problems?

### Related Topics

- **Stack**: Monotonic stack is a specialized application of stack data structure
- **Deque**: Monotonic deque for sliding window maximum problems  
- **Two Pointers**: Alternative approach for some area calculation problems
- **Dynamic Programming**: Some optimization problems combine DP with monotonic stacks
- **Binary Search**: Finding boundaries in sorted structures
- **Segment Tree**: Advanced queries on range maximum/minimum

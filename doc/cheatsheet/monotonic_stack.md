# Monotonic Stack Data Structure

> **Scope** — Next greater / previous smaller / span / histogram problems — the stack stays sorted so each element is pushed and popped once. **Templates and the theory behind them**; the worked solutions live in the sheets that own each problem.
> **See also**: [stack_examples.md](./stack_examples.md) — the worked-solution archive for the problems below, one canonical solution per language; [stack.md](./stack.md) — plain LIFO problems, including the index and depth stacks that are *not* monotonic by value; [monotonic_queue.md](./monotonic_queue.md) — the sliding-window counterpart; [dp_monotonic_stack.md](./dp_monotonic_stack.md) — when the stack carries a DP value instead of a neighbour; [heap.md](./heap.md) — when you need a global extreme instead of a neighbouring one.

## LeetCode Problem Lists

- [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)
- [Stack](https://leetcode.com/problem-list/stack/)

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

### **Pattern 1: Next/Previous Greater Element** — LC 739
- **Description**: Find the next or previous element that is greater than current element
- **Examples**: LC 496 (Next Greater Element I), LC 503 (Next Greater Element II), LC 739 (Daily Temperatures)
- **Pattern**: Use decreasing monotonic stack, pop when finding greater element

### **Pattern 2: Next/Previous Smaller Element** — LC 84
- **Description**: Find the next or previous element that is smaller than current element
- **Examples**: LC 84 (Largest Rectangle), LC 42 (Trapping Rain Water), LC 907 (Sum of Subarray Minimums)
- **Pattern**: Use increasing monotonic stack, pop when finding smaller element

### **Pattern 3: Histogram and Area Problems** — LC 84
- **Description**: Calculate areas, rectangles, or volumes using height information
- **Examples**: LC 84 (Largest Rectangle in Histogram), LC 42 (Trapping Rain Water), LC 85 (Maximal Rectangle)
- **Pattern**: Find boundaries using monotonic stack, calculate areas between boundaries

### **Pattern 4: Sequence Order and Validation** — LC 456
- **Description**: Validate sequences, find patterns, or maintain order constraints
- **Examples**: LC 456 (132 Pattern), LC 901 (Online Stock Span), LC 1856 (Maximum Subarray Min-Product)
- **Pattern**: Use stack to maintain sequence properties and validate patterns

### **Pattern 5: Optimization and Maximum/Minimum** — LC 1793
- **Description**: Find optimal solutions involving maximum or minimum constraints
- **Examples**: LC 1944 (Number of Visible People), LC 2104 (Sum of Subarray Ranges), LC 1793 (Maximum Score)
- **Pattern**: Use monotonic properties to maintain optimal candidates

### **Pattern 6: Circular Arrays** — LC 503
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
| **Contribution** | Sum over all subarrays | Increasing | Count the subarrays each element is the min/max of |
| **Dual Stack** | `max − min` aggregates | Both | Run the contribution pass twice and subtract |
| **Tree Build** | Cartesian tree | Decreasing | Popped nodes become the left subtree |

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

### Template 1: Next Greater Element (Decreasing Stack) — LC 496

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

### Template 2: Next Smaller Element (Increasing Stack) — LC 84

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

### Template 3: Largest Rectangle in Histogram — LC 84

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

### Template 4: Circular Array Processing — LC 503

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

### Template 6: Pattern Validation (132 Pattern) — LC 456

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

### Template 7: Contribution Method — Count the Subarrays an Element Dominates — LC 907 ⭐⭐⭐⭐⭐

> Instead of enumerating subarrays, ask *"for how many subarrays is `arr[i]` the minimum?"*.
> Two monotonic passes give the left and right dominance spans; their product is the count.
> The Java form of the same two passes is in
> [stack_examples.md](./stack_examples.md#4-sum-of-subarray-minimums--lc-907).

#### **Contribution Method — Visualizing `left[i]` / `right[i]` (Python)** ⭐⭐⭐⭐⭐

> `leetcode_python/Math/sum-of-subarray-minimums.py`

**Core idea:** every subarray has exactly one minimum, so instead of enumerating subarrays we ask *"for how many subarrays is `arr[i]` the minimum?"* — then sum `arr[i] * count`.

For each index `i`, that count splits into two independent choices:

```text
        left choices              right choices
           <---->                    <----->
   ┌───────────────────────────────────────────────┐
   │  ...  PSE   .   .   .   [i]   .   .   .   NSE   │      arr
   └───────────────────────────────────────────────┘
              ^                          ^
        previous smaller           next smaller-or-equal
        element (strict >=)        element (strict >)

   left[i]  = i - PSE     ← # of left endpoints that keep arr[i] as min
   right[i] = NSE - i     ← # of right endpoints that keep arr[i] as min

   count(i) = left[i] * right[i]
   contribution = arr[i] * left[i] * right[i]
```

- A subarray keeps `arr[i]` as its minimum only if it **starts** somewhere in `(PSE, i]` and **ends** somewhere in `[i, NSE)`.
- The two ranges are independent → multiply them.

**Handling duplicates (avoid double counting):** use **`>=`** on the left pass and **`>`** on the right pass (asymmetric). Equal values are then counted on exactly one side.

```python
# python
# LC 907 - Sum of Subarray Minimums (contribution method)
# time = O(n), space = O(n)
MOD = 10**9 + 7
n = len(arr)
left  = [0] * n   # left[i]  = distance to previous smaller element
right = [0] * n   # right[i] = distance to next smaller-or-equal element

# --- LEFT pass: distance to Previous Smaller Element (pop on >=) ---
mono_st = []
for i in range(n):
    val = arr[i]
    # Pop elements that are greater than OR EQUAL to current val
    while mono_st and arr[mono_st[-1]] >= val:
        mono_st.pop()   # these can't be the left boundary of arr[i]

    # If stack empty -> val is the smallest so far, boundary is index -1
    #   left choices = i - (-1) = i + 1
    # Else -> boundary is the surviving stack top (the PSE)
    #   left choices = i - mono_st[-1]
    left[i] = i + 1 if not mono_st else i - mono_st[-1]
    mono_st.append(i)

# --- RIGHT pass: distance to Next Smaller Element (pop on >) ---
mono_st = []
for i in range(n - 1, -1, -1):
    val = arr[i]
    while mono_st and arr[mono_st[-1]] > val:   # strict > here
        mono_st.pop()
    right[i] = n - i if not mono_st else mono_st[-1] - i
    mono_st.append(i)

ans = 0
for i in range(n):
    ans = (ans + arr[i] * left[i] * right[i]) % MOD
```

**Why `left[i] = i + 1` when the stack is empty:** an empty stack means nothing to the left is smaller than `arr[i]` — `arr[i]` dominates the whole prefix. The imaginary left boundary sits at index `-1`, so the left choices span indices `0..i`, i.e. `i - (-1) = i + 1`.

**Visual trace on `arr = [3, 1, 2, 4]`:**

```text
i=0 val=3 : stack empty              -> left[0] = 0-(-1) = 1   stack=[0]
i=1 val=1 : arr[0]=3 >= 1 -> pop 0
            stack empty              -> left[1] = 1-(-1) = 2   stack=[1]
i=2 val=2 : arr[1]=1 >= 2? no        -> left[2] = 2-1     = 1   stack=[1,2]
i=3 val=4 : arr[2]=2 >= 4? no        -> left[3] = 3-2     = 1   stack=[1,2,3]

left  = [1, 2, 1, 1]
right = [1, 3, 2, 1]   (symmetric backward pass with strict >)

contribution = 3*1*1 + 1*2*3 + 2*1*2 + 4*1*1 = 3 + 6 + 4 + 4 = 17  ✓
```

### Template 8: Dual Stack — `max − min` Over All Subarrays — LC 2104 ⭐⭐⭐⭐


> `sum(ranges) = sum(subarray maxs) − sum(subarray mins)`. Use one monotonic stack pass per role; for each popped element compute how many subarrays it owns as the max/min.

#### Core Idea

```text
range(subarray) = max − min
sum(all ranges) = sum(all subarray maxs) − sum(all subarray mins)
```

For each element `nums[mid]`, find its **left** and **right** dominance boundaries:
- **Left boundary** `L` — index of the previous element that would displace `nums[mid]` from the max/min role (or `-1` if none)
- **Right boundary** `R` — index of the next element that displaces it (or `n` if none)

Number of subarrays where `nums[mid]` is the max/min:
```text
count = (mid − L) × (R − mid)
contribution = nums[mid] × count
```

The **sentinel loop** runs `i` from `0` to `n` inclusive. When `i == n`, it flushes every remaining index from the stack using `n` as the right boundary.

**Duplicate-safe boundary rule** (avoids double-counting equal elements):
- For **max**: pop when `nums[mid] < nums[i]` (strict); left boundary is the last *greater-or-equal* element.
- For **min**: pop when `nums[mid] > nums[i]` (strict); left boundary is the last *smaller-or-equal* element.

---

#### Visual Trace — max pass on `[1, 3, 2]`

```text
Decreasing stack (max contribution)

i=0: push 0         stack=[0]
i=1: nums[0]=1 < nums[1]=3 → pop mid=0
       left=-1, right=1
       contrib = 1 * (0-(-1)) * (1-0) = 1*1*1 = 1
     push 1          stack=[1]
i=2: nums[1]=3 > nums[2]=2, no pop
     push 2          stack=[1,2]
i=3 (sentinel): flush
     pop mid=2: left=1, right=3  → 2*(2-1)*(3-2) = 2
     pop mid=1: left=-1, right=3 → 3*(1-(-1))*(3-1) = 12

max_sum = 1 + 2 + 12 = 15

min pass (increasing stack) → min_sum = 10

answer = 15 − 10 = 5  ✓
verify: [1]=0,[3]=0,[2]=0,[1,3]=2,[3,2]=1,[1,3,2]=2 → sum = 5
```

---

#### Pattern (Python)

```python
# python
# LC 2104 - Sum of Subarray Ranges
# IDEA: sum(ranges) = sum(subarray maxs) - sum(subarray mins)
#       Contribution method via monotonic stack — one pass per role
# time = O(N), space = O(N)
def subArrayRanges(nums):
    n = len(nums)

    def contribution(is_max):
        stack = []
        total = 0
        for i in range(n + 1):          # sentinel: i == n flushes remaining
            while stack and (
                i == n or
                (nums[stack[-1]] < nums[i] if is_max else nums[stack[-1]] > nums[i])
            ):
                mid = stack.pop()
                left  = stack[-1] if stack else -1   # previous boundary index
                right = i                            # current index = right boundary
                total += nums[mid] * (mid - left) * (right - mid)
            stack.append(i)
        return total

    return contribution(True) - contribution(False)
```

#### Pattern (Java)

```java
// java
// LC 2104 - Sum of Subarray Ranges
// IDEA: sum(ranges) = sum(subarray maxs) - sum(subarray mins)
//       Contribution method: for each element count subarrays where it's max/min
// time = O(N), space = O(N)
public long subArrayRanges(int[] nums) {
    return contribution(nums, true) - contribution(nums, false);
}

private long contribution(int[] nums, boolean isMax) {
    int n = nums.length;
    Deque<Integer> stack = new ArrayDeque<>();
    long total = 0;

    for (int i = 0; i <= n; i++) {          // i == n is the sentinel flush
        while (!stack.isEmpty()) {
            int mid = stack.peek();
            boolean shouldPop = (i == n) ||
                (isMax ? nums[mid] < nums[i] : nums[mid] > nums[i]);
            if (!shouldPop) break;
            stack.pop();
            int left  = stack.isEmpty() ? -1 : stack.peek(); // prev boundary
            int right = i;                                    // next boundary
            total += (long) nums[mid] * (mid - left) * (right - mid);
        }
        stack.push(i);
    }
    return total;
}
```

#### Two-Stack Logic Summary

| Pass | Stack type | Pop condition | Computes |
|------|-----------|---------------|----------|
| Max pass | Monotonic **decreasing** | `nums[mid] < nums[i]` | Sum of subarray maximums |
| Min pass | Monotonic **increasing** | `nums[mid] > nums[i]` | Sum of subarray minimums |
| Both | Sentinel at `i = n` | Always flush | Handles right-edge elements |

#### Similar Problems

| Problem | LC# | Key Difference |
|---------|-----|----------------|
| Sum of Subarray Ranges | 2104 | `max_sum − min_sum`; two monotonic stack passes |
| Sum of Subarray Minimums | 907 | Min contribution only; single increasing stack pass |
| Maximum Subarray Min-Product | 1856 | Min contribution × subarray sum; prefix sums + stack |
| Sum of Total Strength of Wizards | 2281 | Min × sum of sums; prefix of prefix sums + stack |
| Largest Rectangle in Histogram | 84 | Area = height × width; pop on shorter bar |
| Number of Visible People in Queue | 1944 | Count pops per element as the answer |

### Template 9: The Stack Builds a Cartesian Tree — LC 654 ⭐⭐⭐⭐


> **Template 9: monotonic stack that builds a tree.** The naive "find max, recurse left/right" is O(n²). A **decreasing** stack builds the same tree in one pass: everything popped by `num` is smaller than `num` and sits to its left → it becomes `num`'s **left** subtree; the surviving stack top is greater than `num` → `num` becomes its **right** child. Root = bottom of the stack.

```text
nums = [3,2,1,6,0,5]

3 → stack[3]
2 → 3>2, 3.right = 2            stack[3,2]
1 → 2>1, 2.right = 1            stack[3,2,1]
6 → pop 1,2,3 (each becomes 6.left in turn, last popped wins) → stack empty
                                 stack[6]      root = 6
0 → 6.right = 0                 stack[6,0]
5 → pop 0 → 5.left = 0; top 6 → 6.right = 5   stack[6,5]
```

```java
// java
// LC 654 - Maximum Binary Tree
// IDEA: Monotonic DECREASING stack of nodes. Nodes popped by num become num's left
//       subtree (last popped = direct left child); surviving top adopts num as right child
// time = O(N), space = O(N)   // beats the O(N^2) divide & conquer build
public TreeNode constructMaximumBinaryTree(int[] nums) {
    Deque<TreeNode> stack = new ArrayDeque<>(); // values decreasing: bottom -> top
    for (int num : nums) {
        TreeNode cur = new TreeNode(num);
        while (!stack.isEmpty() && stack.peek().val < num) {
            cur.left = stack.pop();          // last popped ends up as the left child
        }
        if (!stack.isEmpty()) stack.peek().right = cur;
        stack.push(cur);
    }
    return stack.isEmpty() ? null : stack.peekLast(); // bottom of stack = global max = root
}
```

```python
# python
# LC 654 - Maximum Binary Tree
# IDEA: monotonic decreasing stack of nodes; popped nodes chain into cur.left,
#       remaining top takes cur as its right child; stack[0] is the root
# time = O(N), space = O(N)
def constructMaximumBinaryTree(nums):
    stack = []                      # node values decreasing
    for num in nums:
        cur = TreeNode(num)
        while stack and stack[-1].val < num:
            cur.left = stack.pop()  # overwritten each pop -> keeps the LAST popped
        if stack:
            stack[-1].right = cur
        stack.append(cur)
    return stack[0] if stack else None
```

**Why `cur.left` may be overwritten:** each pop re-assigns `cur.left`, and the popped nodes are already linked to each other (an earlier pop is the previous node's right child), so after the loop `cur.left` correctly points at the root of the whole popped block.

**Related:** LC 1008 (Construct BST from Preorder Traversal) uses the mirror idea — a decreasing stack where a larger value becomes the right child of the last popped node.

> [tree_construction.md](./tree_construction.md#1-maximum-binary-tree--lc-654-build-tree-from-an-array-by-index-range-) builds the same tree by recursive index range — O(n²) worst case, but the shape most people reach for first. The stack build above is the O(n) one.

### Template Variations — Same Stack, a Different Payload


| LC # | Problem | Base template | The twist |
|------|---------|---------------|-----------|
| 1475 | Final Prices With a Special Discount in a Shop | Template 2 (next smaller) | Next smaller **or equal** — pop on `prices[stack[-1]] >= prices[i]`, and the discount is `price - prices[i]` rather than the index distance |
| 1019 | Next Greater Node In Linked List | Template 1 (next greater) | Same decreasing stack, but the input is a linked list — walk it once into an array (or push `(index, val)` while walking) since the answer array needs random access |
| 768 | Max Chunks To Make Sorted II | Template 1 (decreasing pops) | Stack holds **chunk maxima**, not raw elements; answer = final stack size |
| 769 | Max Chunks To Make Sorted | Template 1 (degenerate) | Values are a permutation of `0..n-1`, so a running max replaces the stack: cut a chunk whenever `runningMax == i` |
| 1047 / 1209 | Remove All Adjacent Duplicates In String (I / II) | Template 5 (stack with info) | Stack stores `(char, count)` pairs; pop when `count` reaches `k` — LC 1047 is the `k = 2` special case |

**Max Chunks To Make Sorted II (LC 768) — chunk-maxima stack**

```java
// java
// LC 768 - Max Chunks To Make Sorted II
// IDEA: monotonic increasing stack of chunk MAXIMA. A value smaller than the top must
//       merge every chunk it is smaller than; the merged chunk keeps the largest max
// time = O(N), space = O(N)
public int maxChunksToSorted(int[] arr) {
    Deque<Integer> stack = new ArrayDeque<>(); // chunk maxima, increasing bottom -> top
    for (int num : arr) {
        if (!stack.isEmpty() && num < stack.peek()) {
            int maxOfMerged = stack.pop();
            while (!stack.isEmpty() && num < stack.peek()) stack.pop();
            stack.push(maxOfMerged);           // merged chunk keeps the old max
        } else {
            stack.push(num);                   // starts a new chunk
        }
    }
    return stack.size();
}
```

```python
# python
# LC 768 - Max Chunks To Make Sorted II
# IDEA: increasing stack of chunk maxima; merging keeps the largest max
# time = O(N), space = O(N)
def maxChunksToSorted(arr):
    stack = []                       # chunk maxima, increasing
    for num in arr:
        if stack and num < stack[-1]:
            merged_max = stack.pop()
            while stack and num < stack[-1]:
                stack.pop()
            stack.append(merged_max)
        else:
            stack.append(num)
    return len(stack)

# LC 769 - Max Chunks To Make Sorted (values are a permutation of 0..n-1)
# time = O(N), space = O(1)
def maxChunksToSortedI(arr):
    chunks, running_max = 0, -1
    for i, num in enumerate(arr):
        running_max = max(running_max, num)
        if running_max == i:         # prefix holds exactly the values 0..i
            chunks += 1
    return chunks
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
- **Template 7 (Contribution)**: 5 problems — LC 907, 2104, 1856, 2281, 1944
- **Template 8 (Dual stack)**: 2 problems — LC 2104, 2281
- **Template 9 (Cartesian tree)**: 2 problems — LC 654, 1008
- **Multiple Templates**: 8 problems

## Pattern Selection Strategy

### Decision Framework Flowchart

```text
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
| **Sum over all subarrays** | Template 7 | Indices | Two passes, opposite directions |
| **`max − min` aggregate** | Template 8 | Indices | Template 7 twice, then subtract |
| **Build a tree in one pass** | Template 9 | Nodes | Left to Right |

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
| **Template 7** | Contribution | `total += arr[i] * left[i] * right[i]` |
| **Template 8** | Dual stack | `contribution(max) - contribution(min)` |
| **Template 9** | Cartesian tree | `while stack and stack[-1].val < num: cur.left = stack.pop()` |

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

## LC Examples

One worked problem lives here — the one whose monotonic-stack solution has no other home.
Every other problem this sheet teaches is solved in full in the sheet that owns it; the table
below says which. The templates above are the thing to memorise, and re-solving a problem
that another sheet already works through only makes the two copies drift.

### 2-1) Trapping Rain Water — LC 42 — Horizontal Layers

> Pop a bar when a taller bar arrives; the water trapped is `(min of the two walls − bottom) × width`.
> This fills the puddle in **horizontal layers**, one per pop.
> [2_pointers_examples.md](./2_pointers_examples.md#trapping-rain-water--lc-42) solves the same problem in
> **vertical columns** with two pointers and O(1) space — worth knowing both, because the interviewer
> who asks for O(1) space is asking for that one.


```java
// LC 42 - Trapping Rain Water
// IDEA: Monotonic stack — pop when taller bar found, water fills between boundaries
// time = O(N), space = O(N)
public int trap(int[] height) {
    Deque<Integer> stack = new ArrayDeque<>();
    int water = 0;
    for (int i = 0; i < height.length; i++) {
        while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
            int bottom = stack.pop();
            if (stack.isEmpty()) break;
            int left = stack.peek();
            int width = i - left - 1;
            int boundedHeight = Math.min(height[left], height[i]) - height[bottom];
            water += width * boundedHeight;
        }
        stack.push(i);
    }
    return water;
}
```

### Worked Solutions — Where Each Problem Lives

| LC # | Problem | Template | Worked in |
|------|---------|----------|-----------|
| 496 | Next Greater Element I | 1 | [stack_examples.md](./stack_examples.md#1-next-greater-element-i--lc-496) |
| 503 | Next Greater Element II | 4 | [stack_examples.md](./stack_examples.md#2-next-greater-element-ii--lc-503) |
| 739 | Daily Temperatures | 1 | [stack_examples.md](./stack_examples.md#3-daily-temperatures--lc-739-) |
| 907 | Sum of Subarray Minimums | 7 | [stack_examples.md](./stack_examples.md#4-sum-of-subarray-minimums--lc-907) |
| 2104 | Sum of Subarray Ranges | 8 | [stack_examples.md](./stack_examples.md#5-sum-of-subarray-ranges--lc-2104) |
| 84 | Largest Rectangle in Histogram | 3 | [stack_examples.md](./stack_examples.md#6-largest-rectangle-in-histogram--lc-84-) |
| 901 | Online Stock Span | 1 | [stack_examples.md](./stack_examples.md#7-online-stock-span--lc-901) |
| 402 | Remove K Digits | 2 | [stack_examples.md](./stack_examples.md#8-remove-k-digits--lc-402-) |
| 735 | Asteroid Collision | 5 | [stack_examples.md](./stack_examples.md#10-asteroid-collision--lc-735) |
| 32 | Longest Valid Parentheses | index stack | [stack_examples.md](./stack_examples.md#15-longest-valid-parentheses--lc-32-) |
| 155 | Min Stack | auxiliary stack | [stack.md](./stack.md#template-4-min-stack--o1-getmin--lc-155-) |
| 388 | Longest Absolute File Path | depth stack | [stack.md](./stack.md#template-6-scope--context-ledger--lc-388-lc-636-) |
| 85 | Maximal Rectangle | 3, per row | [matrix_examples.md](./matrix_examples.md#15-maximal-rectangle--lc-85--row-by-row-histogram-reduction) |
| 654 | Maximum Binary Tree | 9 | [tree_construction.md](./tree_construction.md#1-maximum-binary-tree--lc-654-build-tree-from-an-array-by-index-range-) |
| 853 | Car Fleet | sort + running max | [sort.md](./sort.md#2-8-car-fleet--lc-853) |

LC 155, 388, 32 and 853 are on this list because the sheet used to solve them: none keeps a
monotonic invariant by *value*, so the technique above is not what makes them work.

---
layout: cheatsheet
title: "Greedy Algorithms"
description: "Algorithms that make locally optimal choices for global optimization"
category: "Algorithm"
difficulty: "Medium"
tags: ["greedy", "optimization", "local-optimal", "scheduling"]
patterns:
  - Local optimization
  - Activity selection
  - Interval scheduling
---

# Greedy Algorithms

## Overview
**Greedy algorithms** make locally optimal choices at each step with the hope of finding a global optimum. They work by selecting the best available option at each decision point without reconsidering previous choices.

### Key Properties
- **Time Complexity**: Usually O(n) or O(nlogn) with sorting
- **Space Complexity**: O(1) to O(n) depending on problem
- **Core Idea**: Make the locally optimal choice at each step
- **When to Use**: Problems with greedy choice property and optimal substructure
- **Limitation**: Doesn't always yield globally optimal solution

### Core Characteristics
- **Greedy Choice Property**: Local optimal leads to global optimal
- **Optimal Substructure**: Optimal solution contains optimal sub-solutions
- **No Backtracking**: Once a choice is made, it's never reconsidered
- **Proof Required**: Must prove greedy approach gives optimal result

### Greedy vs Other Approaches
- **Greedy vs DP**: Greedy is optimized DP when greedy choice works
- **Greedy vs Brute Force**: Much faster but may miss optimal
- **Path**: Brute Force → DP → Greedy (when applicable)

## Problem Categories

### **Category 1: Interval Scheduling**
- **Description**: Select maximum non-overlapping intervals
- **Examples**: LC 435 (Non-overlapping), LC 452 (Burst Balloons), LC 646 (Pair Chain)
- **Pattern**: Sort by end time, select earliest ending

### **Category 2: Activity Selection**
- **Description**: Maximize number of activities or minimize resources
- **Examples**: LC 621 (Task Scheduler), LC 1353 (Maximum Events)
- **Pattern**: Priority queue or sorting with selection

### **Category 3: Huffman Coding**
- **Description**: Build optimal prefix codes
- **Examples**: LC 1167 (Min Cost to Connect Sticks)
- **Pattern**: Repeatedly merge smallest elements

### **Category 4: Stock Trading**
- **Description**: Maximize profit from transactions
- **Examples**: LC 122 (Buy Sell Stock II), LC 134 (Gas Station)
- **Pattern**: Accumulate positive differences

### **Category 5: Jump Game**
- **Description**: Reach target with minimum steps
- **Examples**: LC 55 (Jump Game), LC 45 (Jump Game II)
- **Pattern**: Track maximum reachable position

### **Category 6: String Reorganization**
- **Description**: Rearrange strings with constraints
- **Examples**: LC 767 (Reorganize String), LC 358 (Rearrange K Distance)
- **Pattern**: Use heap to select most frequent available

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Sorting Key | When to Use |
|---------------|----------|-------------|-------------|
| **Interval** | Non-overlapping selection | End time | Meeting rooms, activities |
| **Priority Queue** | Dynamic selection | Value/frequency | Task scheduling |
| **Two Pointers** | Pairing/matching | Various | Array manipulation |
| **Accumulation** | Running sum/product | None | Stock, gas station |
| **Jump/Reach** | Position tracking | None | Jump games |

### Universal Greedy Template
```python
def greedy_solution(items):
    # Step 1: Sort or prepare data structure
    items.sort(key=lambda x: x[criterion])
    
    # Step 2: Initialize greedy choice tracking
    result = initial_value
    current_state = initial_state
    
    # Step 3: Make greedy choices
    for item in items:
        if can_select(item, current_state):
            result = update_result(result, item)
            current_state = update_state(current_state, item)
    
    return result
```

### Template 1: Interval Scheduling
```python
def interval_scheduling(intervals):
    """Select maximum non-overlapping intervals"""
    if not intervals:
        return 0
    
    # Sort by end time
    intervals.sort(key=lambda x: x[1])
    
    count = 1
    end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        if intervals[i][0] >= end:
            count += 1
            end = intervals[i][1]
    
    return count
```

### Template 2: Activity Selection with Heap
```python
import heapq

def activity_selection_heap(tasks):
    """Select activities using priority queue"""
    # Count frequency or priority
    freq = collections.Counter(tasks)
    
    # Max heap (negate for min heap)
    heap = [(-count, task) for task, count in freq.items()]
    heapq.heapify(heap)
    
    result = []
    while heap:
        count1, task1 = heapq.heappop(heap)
        result.append(task1)
        
        if heap:
            count2, task2 = heapq.heappop(heap)
            result.append(task2)
            
            # Add back if still available
            if count1 < -1:
                heapq.heappush(heap, (count1 + 1, task1))
            if count2 < -1:
                heapq.heappush(heap, (count2 + 1, task2))
    
    return result
```

### Template 3: Greedy Accumulation
```python
def greedy_accumulation(prices):
    """Accumulate positive differences (stock trading)"""
    profit = 0
    
    for i in range(1, len(prices)):
        # Greedy: take profit whenever possible
        if prices[i] > prices[i-1]:
            profit += prices[i] - prices[i-1]
    
    return profit
```

### Template 4: Jump Game Pattern
```python
def jump_game(nums):
    """Check if can reach end"""
    max_reach = 0
    
    for i in range(len(nums)):
        if i > max_reach:
            return False
        max_reach = max(max_reach, i + nums[i])
        if max_reach >= len(nums) - 1:
            return True
    
    return True

def jump_game_min_jumps(nums):
    """Minimum jumps to reach end"""
    jumps = 0
    current_end = 0
    farthest = 0
    
    for i in range(len(nums) - 1):
        farthest = max(farthest, i + nums[i])
        
        if i == current_end:
            jumps += 1
            current_end = farthest
    
    return jumps
```

### Template 5: String Reorganization
```python
def reorganize_string(s):
    """Reorganize string so no adjacent chars are same"""
    from collections import Counter
    import heapq
    
    # Count frequencies
    count = Counter(s)
    
    # Check if possible
    max_count = max(count.values())
    if max_count > (len(s) + 1) // 2:
        return ""
    
    # Max heap of frequencies
    heap = [(-cnt, char) for char, cnt in count.items()]
    heapq.heapify(heap)
    
    result = []
    prev_count, prev_char = 0, ''
    
    while heap:
        count, char = heapq.heappop(heap)
        result.append(char)
        
        # Add previous back to heap
        if prev_count < 0:
            heapq.heappush(heap, (prev_count, prev_char))
        
        # Update previous
        prev_count = count + 1
        prev_char = char
    
    return ''.join(result)
```

### Template 6: Fractional Knapsack
```python
def fractional_knapsack(items, capacity):
    """Greedy knapsack allowing fractions"""
    # items = [(value, weight), ...]
    # Sort by value/weight ratio
    items.sort(key=lambda x: x[0]/x[1], reverse=True)
    
    total_value = 0
    remaining = capacity
    
    for value, weight in items:
        if weight <= remaining:
            total_value += value
            remaining -= weight
        else:
            # Take fraction
            total_value += value * (remaining / weight)
            break
    
    return total_value
```

## Problems by Pattern

### **Interval Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Non-overlapping Intervals | 435 | Sort by end | Medium |
| Minimum Arrows to Burst Balloons | 452 | Sort by end | Medium |
| Maximum Length of Pair Chain | 646 | Sort by end | Medium |
| Merge Intervals | 56 | Sort by start | Medium |
| Meeting Rooms II | 253 | Sort + heap | Medium |
| Interval List Intersections | 986 | Two pointers | Medium |

### **Activity Selection Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Task Scheduler | 621 | Frequency count | Medium |
| Maximum Events Attended | 1353 | Sort + heap | Medium |
| Course Schedule III | 630 | Sort + heap | Hard |
| IPO | 502 | Two heaps | Hard |

### **Stock Trading Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Buy Sell Stock II | 122 | Accumulate gains | Easy |
| Gas Station | 134 | Circular array | Medium |
| Best Time with Fee | 714 | State tracking | Medium |
| Container With Most Water | 11 | Two pointers | Medium |

### **Jump Game Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Jump Game | 55 | Track max reach | Medium |
| Jump Game II | 45 | Min jumps | Medium |
| Jump Game III | 1306 | BFS/DFS | Medium |
| Reach a Number | 754 | Math + greedy | Medium |

### **String Reorganization Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Reorganize String | 767 | Max heap | Medium |
| Rearrange K Distance Apart | 358 | Heap + queue | Hard |
| Task Scheduler | 621 | Frequency | Medium |
| Longest Happy String | 1405 | Heap greedy | Medium |

### **Other Greedy Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Candy | 135 | Two pass | Hard |
| Assign Cookies | 455 | Two pointers | Easy |
| Maximum Units on Truck | 1710 | Sort by value | Easy |
| Boats to Save People | 881 | Two pointers | Medium |
| Minimum Cost to Connect Sticks | 1167 | Min heap | Medium |

## LC Examples

### 2-2) Jump Game
```python
# 055 Jump Game
# V0
class Solution(object):
    def canJump(self, nums):
        # edge case
        if not nums:
            return True
        cur = 0
        for i in range(len(nums)):
            if cur < i:
                return False
            cur = max(cur, i + nums[i])
        return True
```

### 2-2') Jump Game II
```python
# 045 Jump Game II
# V0
# IDEA : GREEDY
"""
Steps:
    step 1) Initialize three integer variables: jumps to count the number of jumps, currentJumpEnd to mark the end of the range that we can jump to, and farthest to mark the farthest place that we can reach. Set each variable to zero
    step 2) terate over nums. Note that we exclude the last element from our iteration because as soon as we reach the last element, we do not need to jump anymore.
            - Update farthest to i + nums[i] if the latter is larger.
            - If we reach currentJumpEnd, it means we finished the current jump, and can begin checking the next jump by setting currentJumpEnd = farthest.
    step 3) return jumps
"""
class Solution:
    def jump(self, nums: List[int]) -> int:
            jumps = 0
            current_jump_end = 0
            farthest = 0
            for i in range(len(nums) - 1):
                # we continuously find the how far we can reach in the current jump
                farthest = max(farthest, i + nums[i])
                # if we have come to the end of the current jump,
                # we need to make another jump
                if i == current_jump_end:
                    jumps += 1
                    current_jump_end = farthest
            return jumps

# V1
# IDEA : GREEDY
# https://leetcode.com/problems/jump-game-ii/discuss/1672485/Python-Greedy
class Solution:
    def jump(self, nums: List[int]) -> int:
        l = r = res = farthest = 0
        while r < len(nums) - 1:
            for idx in range(l, r+1):
                farthest = max(farthest, idx + nums[idx])
            l = r+1
            r = farthest 
            res += 1
        return res
```

### 2-3) Best Time to Buy and Sell Stock II
```python
# 122 Best Time to Buy and Sell Stock II
class Solution:
    def maxProfit(self, prices):
        profit = 0
        for i in range(0,len(prices)-1):
            if prices[i+1] > prices[i]:
                # have to sale last stock, then buy a new one
                # and sum up the price difference into profit
                profit += prices[i+1] - prices[i]
        return profit

```

### 2-4) Gas Station
```python
# 134 Gas Station
# V0
# IDEA : GREEDY
# IDEA : if sum(gas) - sum(cost) > 0, => THERE MUST BE A SOLUTION
# IDEA : since it's circular (symmetry), we can maintain "total" (e.g. total += gas[i] - cost[i]) of (gas[i], cost[i]) for each index as their "current sum"
class Solution(object):
    def canCompleteCircuit(self, gas, cost):
        start = remain = total = 0
        for i in range(len(gas)):
            remain += gas[i] - cost[i]
            total += gas[i] - cost[i]
            if remain < 0:
                remain = 0
                start = i + 1
        return -1 if total < 0 else start
```

### 2-6) Reorganize String
```python
# LC 767. Reorganize String

# V0 
# IDEA : GREEDY + COUNTER
# IDEA : 
#  step 1) order exists count (big -> small)
#  step 2) select the element which is "most remaining" and DIFFERENT from last ans element and append such element to the end of ans
#  step 3) if can't find such element, return ""
class Solution(object):
    def reorganizeString(self, S):
        cnt = collections.Counter(S)
        # Be aware of it : ans = "#" -> not to have error in ans[-1] when first loop
        ans = '#'
        while cnt:
            stop = True
            for v, c in cnt.most_common():
                """
                NOTE !!! trick here

                1) we only compare last element in ans and current key (v), if they are different, then append
                2) we break at the end of each for loop -> MAKE SURE two adjacent characters are not the same.
                3) we use a flag "stop", to decide whether should stop while loop or not
                """
                if v != ans[-1]:
                    stop = False
                    ans += v
                    cnt[v] -= 1
                    if not cnt[v]:
                        del cnt[v]
                    """
                    NOTE !!!
                     -> we BREAK right after each op, since we want to get next NEW most common element from "updated" cnt.most_common()
                    """
                    break
            # Be aware of it : if there is no valid "v", then the while loop will break automatically at this condition (stop = True)
            if stop:
                break
        return ans[1:] if len(ans[1:]) == len(S) else ''
```

### 2-7) Task Scheduler
```python
# LC 621. Task Scheduler
# V0
# pattern :
#    =============================================================================
#    -> task_time = (max_mission_count - 1) * (n + 1) + (number_of_max_mission)
#    =============================================================================
#   
#    -> Example 1) :
#    ->  AAAABBBBCCD, n=3
#    => THE EXPECTED tuned missions is like : ABXXABXXABXXAB
#    -> (4 - 1) * (3 + 1) + 2 = 14
#    -> 4 is the "how many missions the max mission has" (AAAA or BBBB)
#    -> 3 is n
#    -> 2 is "how many mission have max mission count" -> A and B. so it's 2
#    -> in sum,
#    -> (4 - 1) * (3 + 1) is for ABXXABXXABXX
#    -> and 2 is for AB
#
#   -> Example 2) :
#   -> AAABBB, n = 2
#   -> THE EXPECTED tuned missions is like : ABXABXAB
#   -> (3 - 1) * (2 + 1) + (2) = 8
class Solution(object):
    def leastInterval(self, tasks, n):
        count = collections.Counter(tasks)
        most = count.most_common()[0][1]
        num_most = len([i for i, v in count.items() if v == most])
        """
        example 1 : tasks = ["A","A","A","B","B","B"], n = 2
            -> we can split tasks as : A -> B -> idle -> A -> B -> idle -> A -> B
               -> 1) so there are 3-1 group. e.g. (A -> B -> idle), (A -> B -> idle)
                     and each group has (n+1) elements. e.g. (A -> B -> idle)
               -> 2) and the remain element is num_most. e.g. (A, B)
               -> 3) so total cnt = (3-1) * (2+1) + 2 = 8
    
        example 2 : tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
            -> we can split tasks as A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> idle -> idle -> A -> idle -> idle -> A
                -> 1) so there are 6-1 group. e.g. (A -> B -> C), (A -> D -> E), (A -> F -> G), (A -> idle -> idle), (A -> idle -> idle)
                      and each group has (n+1) elements. e.g. (A,B,C) .... (as above)
                -> 2) and the remain element is num_most. e.g. (A) 
                -> 3) so total cnt = (6-1)*(2+1) + 1 =  16
        """
        time = (most - 1) * (n + 1) + num_most
        return max(time, len(tasks)) # be aware of it 
```

### 2-8) Maximum Units on a Truck
```python
# LC 1710. Maximum Units on a Truck
# V0
# IDEA : GREEDY + sorting
class Solution(object):
    def maximumUnits(self, boxTypes, truckSize):
        # boxTypes[i] = [numberOfBoxesi, numberOfUnitsPerBoxi]:
        # edge case
        if not boxTypes or not truckSize:
            return 0
        """
        NOTE : we sort on sort(key=lambda x : -x[1])

            -> if unit is bigger, we can get bigger aggregated result (n * unit)
        """
        boxTypes.sort(key=lambda x : -x[1])
        res = 0
        for n, unit in boxTypes:
            # case 1 : truckSize == 0, break for loop and return ans
            if truckSize == 0:
                break
            # case 2 : truckSize < n, we CAN'T add all n to truck, but CAN only add (truckSize * unit) amount to truck
            elif truckSize < n:
                res += (truckSize * unit)
                truckSize = 0
                break
            # case 3 : normal case, it's OK to put all (n * unit) to truck once
            else:      
                res += (n * unit)
                truckSize -= n
        return res

# V1
# IDEA : GREEDY
# https://leetcode.com/problems/maximum-units-on-a-truck/discuss/1045318/Python-solution
class Solution(object):
    def maximumUnits(self, boxTypes, truckSize):
        boxTypes.sort(key = lambda x: -x[1])
        n = len(boxTypes)
        result = 0
        i = 0
        while truckSize >= boxTypes[i][0]:
            result += boxTypes[i][1] * boxTypes[i][0]
            truckSize -= boxTypes[i][0]
            i += 1
            if i == n:
                return result
        result += truckSize * boxTypes[i][1]
        return result
```

## Decision Framework

### Pattern Selection Strategy

```
Greedy Algorithm Selection Flowchart:

1. Can the problem be solved greedily?
   ├── Does local optimal lead to global optimal? → YES → Use Greedy
   ├── Can you prove greedy correctness? → YES → Use Greedy
   └── NO to both → Use DP or other approach

2. What type of greedy pattern?
   ├── Selection from sorted items → Interval/Activity Selection
   ├── Maximize/minimize at each step → Accumulation Pattern
   ├── Dynamic selection → Priority Queue/Heap
   ├── Position/reach tracking → Jump Game Pattern
   └── Pairing/matching → Two Pointers

3. How to make greedy choice?
   ├── Sort by what criterion?
   │   ├── End time → Interval scheduling
   │   ├── Start time → Merge intervals
   │   ├── Value/weight ratio → Knapsack
   │   └── Custom criterion → Problem specific
   └── No sorting needed → Direct iteration

4. Common greedy strategies:
   ├── Always take the best available
   ├── Never make a choice that blocks future options
   ├── Minimize waste/maximize efficiency
   └── Balance resources evenly
```

### Greedy vs Dynamic Programming

| Criterion | Use Greedy | Use DP | Example |
|-----------|------------|--------|---------|
| Greedy choice property | ✅ | ❌ | Activity selection |
| Need all sub-solutions | ❌ | ✅ | 0/1 Knapsack |
| Can prove optimality | ✅ | - | Huffman coding |
| Overlapping subproblems | ❌ | ✅ | Fibonacci |
| Simple selection rule | ✅ | ❌ | Fractional knapsack |

## Summary & Quick Reference

### Complexity Quick Reference
| Pattern | Time Complexity | Space Complexity | Bottleneck |
|---------|-----------------|------------------|------------|
| Interval scheduling | O(nlogn) | O(1) | Sorting |
| Heap-based selection | O(nlogn) | O(n) | Heap operations |
| Two pointers | O(n) or O(nlogn) | O(1) | Sorting if needed |
| Direct accumulation | O(n) | O(1) | Single pass |
| Jump game | O(n) | O(1) | Single pass |

### Sorting Criteria Guide
```python
# Interval problems
intervals.sort(key=lambda x: x[1])  # By end time
intervals.sort(key=lambda x: x[0])  # By start time

# Value optimization
items.sort(key=lambda x: x.value/x.weight, reverse=True)  # By ratio

# Custom priority
tasks.sort(key=lambda x: (x.deadline, -x.profit))  # Multi-criteria
```

### Common Greedy Patterns

#### **Exchange Argument**
```python
# Prove: Swapping any two elements won't improve result
def exchange_argument_proof(arr):
    # If swapping arr[i] and arr[j] doesn't improve,
    # then current order is optimal
    pass
```

#### **Greedy Stays Ahead**
```python
# Prove: Greedy solution is at least as good at each step
def stays_ahead_proof(greedy, other):
    # Show: greedy[i] >= other[i] for all i
    pass
```

#### **Matroid Theory**
```python
# System has matroid structure if:
# 1. Hereditary: Subset of feasible is feasible
# 2. Exchange: Can always extend smaller feasible set
```

### Problem-Solving Steps
1. **Identify greedy potential**: Look for optimal substructure
2. **Define greedy choice**: What to select at each step
3. **Prove correctness**: Exchange argument or stays ahead
4. **Implement efficiently**: Often requires sorting
5. **Handle edge cases**: Empty input, single element
6. **Verify with examples**: Test greedy choices

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Assuming greedy works without proof
- Wrong sorting criterion
- Not considering all edge cases
- Forgetting to handle ties
- Missing global constraint checks

**✅ Best Practices:**
- Always verify greedy property first
- Start with small examples
- Consider counter-examples
- Use heap for dynamic selection
- Test with edge cases

### Proof Techniques

#### **Exchange Argument Example**
```python
# Prove interval scheduling is optimal
# If we swap any interval in greedy solution with another,
# we either get same or fewer intervals
```

#### **Greedy Stays Ahead Example**
```python
# Prove jump game solution is minimal
# At each position, greedy reaches at least as far
```

### Interview Tips
1. **Recognize patterns**: Look for sorting or selection hints
2. **Start with examples**: Work through small cases
3. **State assumptions**: Clarify if greedy is applicable
4. **Prove if asked**: Use exchange or stays ahead
5. **Code cleanly**: Greedy code is usually simple
6. **Optimize**: Consider using heap for better complexity

### Classic Greedy Problems
- **Activity Selection**: Choose maximum non-overlapping
- **Huffman Coding**: Build optimal prefix codes
- **Kruskal's MST**: Select minimum weight edges
- **Dijkstra's**: Select minimum distance vertex
- **Fractional Knapsack**: Take most valuable ratio

### Related Topics
- **Dynamic Programming**: When greedy doesn't work
- **Binary Search**: For optimization problems
- **Heap/Priority Queue**: For dynamic selection
- **Sorting**: Often prerequisite for greedy
- **Graph Algorithms**: Many use greedy (MST, shortest path)
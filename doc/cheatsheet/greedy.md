# Greedy Algorithms

> **Scope** — Taking the locally best choice and the exchange argument that proves it safe — interval scheduling, jump games, task assignment — plus how to spot when greedy fails and DP is required.
> **See also**: [greedy_examples.md](./greedy_examples.md) — the fourteen worked problems behind these templates; [intervals.md](./intervals.md) — the interval-specific greedy family; [heap.md](./heap.md) — greedy that needs the current best repeatedly; [dp.md](./dp.md) — what to fall back to when the exchange argument breaks; [sort.md](./sort.md) — nearly every greedy starts with a sort.

## LeetCode Problem Lists

- [Greedy](https://leetcode.com/problem-list/greedy/)

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

### Template 1: Interval Scheduling — LC 435 ⭐⭐⭐⭐⭐
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

### Template 2: Activity Selection with Heap — LC 621 ⭐⭐⭐⭐
```python
import heapq
import collections

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

### Template 3: Greedy Accumulation — LC 122 ⭐⭐⭐⭐
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

### Template 4: Jump Game Pattern — LC 55 ⭐⭐⭐⭐⭐
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

### Template 5: String Reorganization — LC 767 ⭐⭐⭐
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
| String Without AAA or BBB | 984 | Greedy + counter tracking | Medium |
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
| Max Non-Overlapping Subarrays Sum=Target | 1546 | Prefix sum + greedy reset | Medium |

## Decision Framework

### When Greedy Fails — Know the Escape Hatch

> Interviewers love problems that *look* greedy. Being able to name the counter-example and pivot is worth as much as the greedy itself.

| Problem | LC # | The tempting greedy | Why it breaks | What actually works |
|---------|------|---------------------|---------------|---------------------|
| Split Array Largest Sum | 410 | "Cut whenever the running sum exceeds `total/k`" | The threshold isn't known in advance; a locally-full chunk can force a huge last chunk | **Binary search on the answer** + a greedy *feasibility check* (`can we split with max sum <= X using <= k parts?`). Greedy is the O(N) validator, not the optimizer. O(N log(sum)) |
| Wildcard Matching | 44 | "Match chars left-to-right, expand `*` as needed" | A `*` expanded too eagerly can strand a later literal | Either **DP** `O(S*P)`, or two-pointer greedy **with a backtrack anchor** (remember the last `*` position and rewind on mismatch) |
| Best Time to Buy/Sell with Fee | 714 | "Sum every positive delta" (LC 122 style) | The fee is charged per transaction, so tiny rises can be net-negative | **DP state machine** `hold / cash` — see the Stock Trading table above |
| 0/1 Knapsack | — | "Sort by value/weight ratio" | Items can't be split (counter-example in the Fractional vs 0/1 table) | **DP** `O(nW)` |

**Recognition rule of thumb:**
- If the objective is *"minimize the maximum"* / *"maximize the minimum"* → the greedy usually becomes a **monotone predicate inside binary search on the answer**, not a standalone algorithm (LC 410 is the canonical case).
- If a choice can be **undone profitably later** (a fee, a cap, a deadline you can retract) → the fix is often a **heap-based "regret" greedy** rather than DP. See the *greedy with regret* template in [`priority_queue.md`](priority_queue.md) — LC 871 Minimum Number of Refueling Stops, LC 630 Course Schedule III, LC 1642 Furthest Building You Can Reach.

---

### Additional High-Frequency Greedy One-Liners

| Problem | LC # | Greedy in one sentence | Difficulty |
|---------|------|------------------------|------------|
| Valid Palindrome II | 680 | Two pointers; on the first mismatch, try skipping **either** side and check the remainder | Easy |
| Minimum Domino Rotations For Equal Row | 1007 | Only `tops[0]` or `bottoms[0]` can be the target value — test just those two candidates | Medium |
| Increasing Triplet Subsequence | 334 | Keep the smallest and second-smallest seen so far; a third beats both ⇒ true | Medium |
| Largest Number | 179 | Sort with custom comparator `a+b` vs `b+a` (string concat) | Medium |
| Hand of Straights / Divide Array in Sets of K | 846 / 1296 | Always start a group from the **smallest remaining** card | Medium |
| Minimum Increment to Make Array Unique | 945 | Sort, then push each element to `max(x, prev+1)` | Medium |
| Can Place Flowers | 605 | Plant at the first legal slot scanning left→right | Easy |


### Pattern Selection Strategy

```text
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

---

## Greedy Proofs & Adjacent Algorithms — Exchange Argument, MST

### Proof Template: Exchange Argument
To verify a greedy choice, show that swapping the greedy pick with any other choice does not improve the result.

```text
1. Assume optimal solution OPT differs from greedy solution G at some step.
2. Show you can swap OPT's choice at that step with G's choice without making things worse.
3. Repeat until OPT == G → greedy is optimal.
```

Common exchange argument problems: LC 435 (Non-overlapping Intervals), LC 452 (Burst Balloons), Job Scheduling.

### Minimum Spanning Tree (MST)

**Kruskal's** (sort edges, union-find):
```python
def kruskal(n, edges):
    edges.sort(key=lambda x: x[2])  # sort by weight
    parent = list(range(n))

    def find(x):
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]

    def union(a, b):
        a, b = find(a), find(b)
        if a == b: return False
        parent[a] = b
        return True

    mst_cost = 0
    for u, v, w in edges:
        if union(u, v):
            mst_cost += w
    return mst_cost
```

**Prim's** (priority queue, dense graphs):
```python
import heapq
from collections import defaultdict

def prim(n, edges):
    graph = defaultdict(list)
    for u, v, w in edges:
        graph[u].append((w, v))
        graph[v].append((w, u))

    visited = set()
    heap = [(0, 0)]  # (cost, node)
    total = 0
    while heap and len(visited) < n:
        cost, node = heapq.heappop(heap)
        if node in visited: continue
        visited.add(node)
        total += cost
        for w, nei in graph[node]:
            if nei not in visited:
                heapq.heappush(heap, (w, nei))
    return total
```

| Algorithm | Time | Best For |
|-----------|------|---------|
| Kruskal | O(E log E) | Sparse graphs |
| Prim (heap) | O(E log V) | Dense graphs |

### Weighted Interval Scheduling — LC 1235
When intervals have weights/profits, greedy alone fails — use DP + binary search.
```python
# LC 1235 Maximum Profit in Job Scheduling
import bisect

def jobScheduling(startTime, endTime, profit):
    jobs = sorted(zip(startTime, endTime, profit), key=lambda x: x[1])
    dp = [(0, 0)]  # (end_time, max_profit)
    for s, e, p in jobs:
        # Find last job that ends <= s
        i = bisect.bisect_right(dp, (s, float('inf'))) - 1
        new_profit = dp[i][1] + p
        if new_profit > dp[-1][1]:
            dp.append((e, new_profit))
    return dp[-1][1]
```

### Fractional vs 0/1 Knapsack
| Property | Fractional | 0/1 |
|----------|-----------|-----|
| Can split items | Yes | No |
| Algorithm | Greedy (sort by value/weight) | DP |
| Time | O(n log n) | O(nW) |
| Greedy works? | Yes | No |

**Why greedy fails for 0/1**: Counter-example: items [(value=6, w=4), (value=5, w=3), (value=5, w=3)], capacity=6. Greedy picks highest ratio (item1, ratio=1.5) → only gets 6. DP picks item2+item3 → gets 10.

### Interview tips — greedy
| Signal | Pattern |
|--------|---------|
| "minimum cost to connect" | MST (Kruskal/Prim) |
| "maximize non-overlapping intervals" | Sort by end time |
| "schedule tasks with cooldown" | Math formula or max-heap |
| "fractional items" | Sort by value/weight ratio |
| "prove this greedy works" | Exchange argument |
| "greedy gives wrong answer here" | Switch to DP |

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

## Worked Examples

Fourteen problems live in **[greedy_examples.md](./greedy_examples.md)**, grouped by the *shape*
of the greedy choice rather than by topic — which is what you actually have to recognise:

| Group | The choice you make | Problems |
|---|---|---|
| [Reach & jump](./greedy_examples.md#reach--jump) | extend the furthest reachable point, and only jump when you must | LC 55, 45, 1326 |
| [Accumulate & reset](./greedy_examples.md#accumulate--reset) | take every gain; reset a running total the moment it goes negative | LC 122, 134, 1546, 921 |
| [Frequency & heap interleaving](./greedy_examples.md#frequency--heap-interleaving) | always place the most frequent item that is currently legal | LC 767, 984, 621 |
| [Sort, then take](./greedy_examples.md#sort-then-take) | sort by the ratio that matters, then consume in order | LC 1710, 3994 |
| [Build while scanning](./greedy_examples.md#build-the-answer-while-scanning) | commit a character or a boundary as soon as it can no longer change | LC 763, 402 |

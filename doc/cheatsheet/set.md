# Set

> **Scope** — Membership, dedup and set algebra — problems where you only need *presence*, not an associated value.
> **See also**: [set_examples.md](./set_examples.md) — the fourteen worked problems; [hash_map.md](./hash_map.md) — when you need a value per key; [hashing.md](./hashing.md) — hashing internals; [Collection.md](./Collection.md) — picking a container.

## LeetCode Problem Lists

- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Ordered Set](https://leetcode.com/problem-list/ordered-set/)

## Time Complexity

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| Hash Set (avg) | O(1)     | O(1)     | O(1)     | O(n)     |

> Average case shown. **Worst case (all elements collide): O(n).** Min/Max requires a full scan (hashing imposes no ordering). Union / intersection of two sets is **O(min(len(s1), len(s2)))**. Space is **O(n)**.

<img src="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/set_operations.png">

## Overview
**Set** is a collection data structure that stores unique elements with no duplicates. It provides efficient membership testing, insertion, and deletion operations.

### Key Properties
- **Complexity**: see the [Time Complexity](#time-complexity) table above
- **Core Features**: No duplicates, unordered (HashSet), O(1) lookups
- **When to Use**: Remove duplicates, membership testing, set operations (union, intersection, difference)

## 0) Concept

### 0-1) Types

#### HashSet
- **Python**: `set()` - unordered, fastest operations
- **Java**: `HashSet<T>` - backed by HashMap
- **Time**: O(1) average for add/remove/contains
- **Use case**: When order doesn't matter, need fast lookups

#### LinkedHashSet
- **Python**: No native support (use OrderedDict keys)
- **Java**: `LinkedHashSet<T>` - maintains insertion order
- **Time**: O(1) for operations, preserves order
- **Use case**: Need set operations + insertion order

#### TreeSet
- **Python**: No native support (use sorted containers)
- **Java**: `TreeSet<T>` - sorted, uses Red-Black tree
- **Time**: O(log n) for add/remove/contains
- **Use case**: Need sorted elements, range queries

### Implementation Comparison
| Type | Ordering | Time | Space | Use Case |
|------|----------|------|-------|----------|
| **HashSet** | None | O(1) | O(n) | Fast lookups, no order needed |
| **LinkedHashSet** | Insertion | O(1) | O(n) | Preserve insertion order |
| **TreeSet** | Sorted | O(log n) | O(n) | Sorted data, range queries |

### 0-2) Pattern

#### Pattern 1: Set Operations
```python
# Union, Intersection, Difference
s1 = {1, 2, 3}
s2 = {2, 3, 4}

union = s1 | s2          # {1, 2, 3, 4}
intersection = s1 & s2   # {2, 3}
difference = s1 - s2     # {1}
symmetric_diff = s1 ^ s2 # {1, 4}
```

#### Pattern 2: Duplicate Detection
```python
# Check for duplicates in array
def has_duplicate(nums):
    return len(nums) != len(set(nums))

# Find duplicates
def find_duplicates(nums):
    seen = set()
    duplicates = set()
    for num in nums:
        if num in seen:
            duplicates.add(num)
        seen.add(num)
    return duplicates
```

#### Pattern 3: Two-Set Tracking
```python
# Track visited and current path (for cycle detection)
def has_cycle(graph, start):
    visited = set()
    current_path = set()

    def dfs(node):
        if node in current_path:
            return True  # Cycle detected
        if node in visited:
            return False

        visited.add(node)
        current_path.add(node)

        for neighbor in graph[node]:
            if dfs(neighbor):
                return True

        current_path.remove(node)
        return False

    return dfs(start)
```

#### Pattern 4: Set for Path/Ancestry Tracking
```python
# LC 1650 - Find LCA using set to track ancestors
def lowestCommonAncestor(p, q):
    # Track all ancestors of p
    ancestors = set()
    while p:
        ancestors.add(p)
        p = p.parent

    # Find first common ancestor
    while q:
        if q in ancestors:
            return q
        q = q.parent
    return None
```

## 1) General form

### 1-1) Basic OP

#### 1-1-1) Set Creation and Basic Operations
```python
# Python
# Create empty set
s = set()
s = {}  # Wrong! This creates a dict

# Create with elements
s = {1, 2, 3}
s = set([1, 2, 3])
s = set("abc")  # {'a', 'b', 'c'}

# Add element
s.add(4)

# Remove element
s.remove(3)     # Raises KeyError if not exists
s.discard(3)    # No error if not exists
s.pop()         # Remove and return arbitrary element

# Check membership
if 2 in s:
    print("Found")

# Size
len(s)

# Clear all
s.clear()
```

```java
// Java
// Create HashSet
Set<Integer> set = new HashSet<>();

// Add element
set.add(1);
set.add(2);
set.add(3);

// Remove element
set.remove(2);

// Check membership
if (set.contains(1)) {
    System.out.println("Found");
}

// Size
int size = set.size();

// Clear
set.clear();

// Iterate
for (int num : set) {
    System.out.println(num);
}
```

#### 1-1-2) Set Operations
```python
# Python set operations
s1 = {1, 2, 3, 4}
s2 = {3, 4, 5, 6}

# Union (elements in either set)
union1 = s1 | s2
union2 = s1.union(s2)           # {1, 2, 3, 4, 5, 6}

# Intersection (elements in both sets)
inter1 = s1 & s2
inter2 = s1.intersection(s2)    # {3, 4}

# Difference (elements in s1 but not s2)
diff1 = s1 - s2
diff2 = s1.difference(s2)       # {1, 2}

# Symmetric difference (elements in either but not both)
sym1 = s1 ^ s2
sym2 = s1.symmetric_difference(s2)  # {1, 2, 5, 6}

# Subset check
is_subset = s1.issubset(s2)     # False
is_superset = s1.issuperset(s2) # False

# Disjoint check (no common elements)
is_disjoint = s1.isdisjoint(s2) # False
```

```java
// Java set operations
Set<Integer> s1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> s2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

// Union
Set<Integer> union = new HashSet<>(s1);
union.addAll(s2);  // {1, 2, 3, 4, 5, 6}

// Intersection
Set<Integer> intersection = new HashSet<>(s1);
intersection.retainAll(s2);  // {3, 4}

// Difference
Set<Integer> difference = new HashSet<>(s1);
difference.removeAll(s2);  // {1, 2}

// Subset check
boolean isSubset = s2.containsAll(s1);  // false
```

#### 1-1-3) Converting Between Collections
```python
# Python conversions
arr = [1, 2, 2, 3, 3, 4]

# Array to set (remove duplicates)
s = set(arr)  # {1, 2, 3, 4}

# Set to array
arr_unique = list(s)

# Set to sorted array
arr_sorted = sorted(s)

# String to set
char_set = set("hello")  # {'h', 'e', 'l', 'o'}

# Set to string
s = {'a', 'b', 'c'}
string = ''.join(sorted(s))  # 'abc'
```

```java
// Java conversions
Integer[] arr = {1, 2, 2, 3, 3, 4};

// Array to set
Set<Integer> set = new HashSet<>(Arrays.asList(arr));

// Set to array
Integer[] arrUnique = set.toArray(new Integer[0]);

// Set to list
List<Integer> list = new ArrayList<>(set);

// List to set
Set<Integer> set2 = new HashSet<>(list);
```

## Problem Categories

### Category 1: Duplicate Detection (10 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Contains Duplicate | 217 | Easy | Set size | len(nums) != len(set(nums)) |
| Contains Duplicate II | 219 | Easy | Sliding window set | Keep window of k elements |
| Contains Duplicate III | 220 | Medium | TreeSet/SortedList | Maintain sorted window |
| Find Duplicate | 287 | Medium | Cycle detection | Floyd's algorithm or set |
| Find All Duplicates | 442 | Medium | Index marking | Use array as hashmap |
| Single Number | 136 | Easy | XOR/Set | XOR cancels duplicates |
| Single Number II | 137 | Medium | Bit manipulation | Count bits mod 3 |
| Single Number III | 260 | Medium | XOR + grouping | Group by differing bit |
| Missing Number | 268 | Easy | Set/XOR | Expected vs actual |
| First Missing Positive | 41 | Hard | In-place set | Use array indices |

### Category 2: Set Operations (8 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Intersection of Two Arrays | 349 | Easy | Set intersection | set1 & set2 |
| Intersection of Two Arrays II | 350 | Easy | Counter | Track frequencies |
| Union of Two Arrays | - | Easy | Set union | set1 | set2 |
| Distribute Candies | 575 | Easy | Set size | min(len(set), n/2) |
| Uncommon Words | 884 | Easy | Set difference | Count once in either |
| Set Mismatch | 645 | Easy | Set difference | Find duplicate & missing |
| Fair Candy Swap | 888 | Easy | Set membership | Target difference |
| Buddy Strings | 859 | Easy | Set of pairs | Check swap possible |

### Category 3: Path/Ancestry Tracking (6 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Lowest Common Ancestor III | 1650 | Medium | Ancestor set | Track parent path |
| Linked List Cycle | 141 | Easy | Visited set | Two pointers better |
| Linked List Cycle II | 142 | Medium | Visited set | Floyd's algorithm |
| Course Schedule | 207 | Medium | DFS + set | Detect cycle |
| Course Schedule II | 210 | Medium | Topological sort | Track visited/path |
| Find Eventual Safe Nodes | 802 | Medium | DFS + states | Terminal vs unsafe |

### Category 4: Sequence Problems (7 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Longest Consecutive Sequence | 128 | Medium | Set lookups | Start from sequence begin |
| Longest Substring Without Repeat | 3 | Medium | Sliding window set | Track seen chars |
| Longest Palindrome | 409 | Easy | Char frequency | Pairs + one odd |
| Maximum Length of Repeated Subarray | 718 | Medium | Set of tuples | Rolling hash |
| Arithmetic Slices | 413 | Medium | Set of differences | Track valid sequences |
| Happy Number | 202 | Easy | Cycle detection | Track seen sums |
| Valid Sudoku | 36 | Medium | Multiple sets | Row/col/box tracking |

### Category 5: Graph/Island Problems (5 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Number of Islands | 200 | Medium | DFS/BFS visited | Track processed cells |
| Number of Distinct Islands | 694 | Medium | Shape hashing | Normalize positions |
| Max Area of Island | 695 | Medium | DFS + visited | Track seen cells |
| Island Perimeter | 463 | Easy | Border counting | Count land-water edges |
| Surrounded Regions | 130 | Medium | Border DFS | Mark connected to border |

### Category 6: String/Pattern Matching (6 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Isomorphic Strings | 205 | Easy | Bijection | Two maps or sets |
| Word Pattern | 290 | Easy | Bijection | Char ↔ word mapping |
| Group Anagrams | 49 | Medium | Sorted key | Use sorted string |
| Find Anagrams | 438 | Medium | Window + counter | Sliding character counts |
| Jewels and Stones | 771 | Easy | Set membership | Set of jewels |
| Unique Email Addresses | 929 | Easy | Normalize + set | Clean emails |

### Category 7: Ordered Set (TreeSet) & Set-as-Index (8 problems)

Problems where a **plain HashSet is not enough** — you need order (floor/ceiling), positional indexing, or a composite key.

| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Insert Delete GetRandom O(1) | 380 | Medium | Set + dense array | Swap-with-last delete → §2-11 |
| Word Ladder | 127 | Hard | Two frontier sets | Bidirectional BFS, `remove` = visited → §2-12 |
| Odd Even Jump | 975 | Hard | TreeMap floor/ceiling | Closest value ≥ / ≤ x → §2-13 |
| Minimum Area Rectangle | 939 | Medium | Set of encoded points | Diagonal determines the other 2 corners → §2-14 |
| The Skyline Problem | 218 | Hard | Ordered multiset | Need max **with arbitrary delete** → `TreeMap<height,count>` |
| Falling Squares | 699 | Hard | Ordered interval set | Query max height over a range, then overwrite it |
| Set Matrix Zeroes | 73 | Medium | Row set + col set | Two sets mark what to zero (O(1)-space follow-up: use row 0 / col 0) |
| Intersection of Two Linked Lists | 160 | Easy | Visited node set | Set of nodes works; two-pointer switch is the O(1)-space answer |

## Decision Framework

### When to Use Set vs Other Data Structures

```text
Problem Analysis:

1. Need to track unique elements?
   ├── YES → Consider Set
   │   ├── Need ordering?
   │   │   ├── YES → TreeSet (Java) / sorted list (Python)
   │   │   └── NO → HashSet
   │   ├── Need count?
   │   │   └── NO → Use Counter/HashMap instead
   │   └── Need fast lookups?
   │       └── YES → HashSet (O(1) average)
   └── NO → Consider other structures

2. Performing set operations (union, intersection)?
   ├── YES → Use Set
   │   └── Multiple operations → Build set once
   └── NO → Continue analysis

3. Detecting duplicates/cycles?
   ├── YES → Use Set for visited tracking
   │   ├── Space constrained?
   │   │   └── YES → Consider Floyd's algorithm
   │   └── NO → Set is ideal
   └── NO → Continue analysis

4. Checking membership repeatedly?
   ├── YES → Convert to Set first
   │   └── O(n) conversion + O(1) lookups
   └── NO → Linear search may be fine
```

### Set vs HashMap Choice

| Use Set When | Use HashMap When |
|--------------|------------------|
| Only need existence check | Need key-value mapping |
| Removing duplicates | Counting frequencies |
| Set operations (∪, ∩, -) | Need associated data |
| Memory efficient (no values) | Need to track counts/indices |

### Python Set vs Java Set

| Feature | Python `set` | Java `HashSet` |
|---------|-------------|----------------|
| **Creation** | `s = {1,2,3}` or `set()` | `Set<T> s = new HashSet<>()` |
| **Add** | `s.add(x)` | `s.add(x)` |
| **Remove** | `s.remove(x)` / `s.discard(x)` | `s.remove(x)` |
| **Contains** | `x in s` | `s.contains(x)` |
| **Union** | `s1 | s2` or `s1.union(s2)` | `s1.addAll(s2)` |
| **Intersection** | `s1 & s2` or `s1.intersection(s2)` | `s1.retainAll(s2)` |
| **Difference** | `s1 - s2` or `s1.difference(s2)` | `s1.removeAll(s2)` |
| **Size** | `len(s)` | `s.size()` |
| **Empty check** | `not s` or `len(s) == 0` | `s.isEmpty()` |

## Summary & Best Practices

### Key Takeaways

1. **When to Use Set**:
   - Remove duplicates from collection
   - Fast membership testing (O(1) average)
   - Performing set operations (union, intersection, difference)
   - Tracking visited nodes in graphs/trees
   - Detecting cycles

2. **Performance Characteristics**:
   - HashSet: O(1) average, O(n) worst (hash collisions)
   - TreeSet: O(log n) for all operations
   - LinkedHashSet: O(1) operations + insertion order

3. **Common Patterns**:
   - Convert array to set to remove duplicates
   - Use set for O(1) lookups instead of O(n) linear search
   - Track visited nodes with set
   - Detect cycles by checking if element already in set

4. **Space-Time Tradeoffs**:
   - Set uses O(n) extra space for O(1) operations
   - Consider two-pointer techniques if space is constrained
   - For small inputs, linear search may be faster

### Interview Tips

**Common Mistakes to Avoid:**
- Using `{}` to create empty set in Python (creates dict instead)
- Forgetting that sets are unordered (don't assume order)
- Not considering TreeSet when you need sorted elements
- Using set when you need to count occurrences (use Counter/HashMap)

**Optimization Tips:**
- Convert lists to sets before repeated membership checks
- Use set operations instead of manual loops
- Consider frozenset for immutable/hashable sets
- Use set comprehensions for cleaner code

**Follow-up Questions:**
- "Can you solve it with O(1) space?" → Consider Floyd's algorithm
- "What if we need to preserve order?" → LinkedHashSet or OrderedDict
- "What if we need sorted elements?" → TreeSet or sorted list
- "What about duplicates with different data?" → Use HashMap instead

## Worked Examples

Fourteen problems live in **[set_examples.md](./set_examples.md)**, grouped by what the set is
actually being used *for* — which is rarely "storing things":

| Group | What the set does | Problems |
|---|---|---|
| ["Have I seen this?"](./set_examples.md#have-i-seen-this-before) | membership as a memory of the past | LC 217, 136, 202, 141 |
| [Set algebra](./set_examples.md#set-algebra) | intersection, difference, bijection | LC 349, 290 |
| [The set as an index](./set_examples.md#the-set-as-an-index) | O(1) lookup that replaces a scan | LC 128, 36, 939, 694 |
| [Inside other algorithms](./set_examples.md#sets-inside-other-algorithms) | a frontier, an ordered structure, or the visible half of a design | LC 380, 127, 975, 1650 |

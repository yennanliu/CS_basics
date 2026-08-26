# Array

> **Scope** — Array fundamentals — in-place rewriting, rotation, partitioning, and the index-as-hash trick. Owns the operations; the pattern families (windows, pointers, prefix sums) each have their own file.
> **See also**: [array_examples.md](./array_examples.md) — the worked problems behind these operations; [python_trick.md](./python_trick.md) and [java_trick_strings_sorting.md](./java_trick_strings_sorting.md) — sort keys and comparators, which own the multi-key rules; [2_pointers.md](./2_pointers.md), [sliding_window.md](./sliding_window.md), [prefix_sum.md](./prefix_sum.md) and [difference_array.md](./difference_array.md) — the four array pattern families; [matrix.md](./matrix.md) — 2D; [sort.md](./sort.md) — ordering.

> Basic linear data structure

## LeetCode Problem Lists

- [Array](https://leetcode.com/problem-list/array/)

## Time Complexity

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| Array          | O(n)     | O(n)     | O(n)     | O(n)     |

> Unsorted dynamic array. Index access is **O(1)**; append is **O(1)** amortized; arbitrary Insert/Delete is **O(n)** (shifting). Search drops to **O(log n)** if the array is sorted (binary search).

## 0) Concept

- [Java Array](https://cloud.tencent.com/developer/article/1672332)
    - Low level : continuous blocks in memory space

### 0-1) Types

- Types
    - [greedy.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/greedy.md)
    - [matrix.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/matrix.md)

- Algorithm
    - index op
    - array op
    - sorting
    - [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
    - [2 pointers](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/2_pointers.md)
        - fast-slow pointers
        - left-right pointers
    - [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)
    - [prefix sum](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/prefix_sum.md)
    - [difference array](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/difference_array.md)
    - [Kadane algo](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/kadane_algo.md)

- Data structure
    - dict
    - set
    - array

## 1) General form

### 1-1) Basic OP

#### 1-1-0) Split Array
```python
# https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md

#-----------------------------------------------------------------------------------------------------
# example 7 : itertools.islice : slice on iterator
#-----------------------------------------------------------------------------------------------------
# https://docs.python.org/3/library/itertools.html#itertools.islice
# syntax : itertools.islice(seq, [start,] stop [, step])

In [6]:  x = itertools.islice(range(10), 0, 9, 2)

In [7]: print (list(x))
[0, 2, 4, 6, 8]


In [18]: y = itertools.islice(range(10), 0, 10, 3)
    ...: print (list(y))
[0, 3, 6, 9]
```

#### 1-1-1) Insert into Array
```python
p=[[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
Out[27]: [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
In [28]: p.insert(1, [6,1])
In [29]: p
Out[29]: [[7, 0], [6, 1], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
```

#### 1-1-2) Delete from Array
```python
p=[[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]

In [4]: p
Out[4]: [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]

In [5]: p.remove([7, 1])

In [6]: p
Out[6]: [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4]]
```
#### 1-1-3) check if element in Array
```python
In [7]: p
Out[7]: [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4]]

In [8]: [7,0] in p
Out[8]: True
```
#### 1-1-4) append to array (head, tail)
```python
# tail
In [9]: p
Out[9]: [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4]]

In [10]: p.append([0,0])

In [11]: p
Out[11]: [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4], [0, 0]]
```

#### 1-1-5) Sort Array*****

**`list.sort()` (V1) sorts IN PLACE. `sorted()` (V2) returns a NEW list.**

| | `_array.sort(...)` (V1) | `sorted(_array, ...)` (V2) |
|---|---|---|
| **In-place?** | ✅ yes — `_array` is mutated | ❌ no — original untouched |
| **Return value** | `None` | new sorted `list` |
| **Works on** | `list` only | **any iterable** (str, tuple, set, dict, generator) |
| **Space** | O(1) extra* | O(n) |
| **Stable?** | ✅ yes (Timsort) | ✅ yes (Timsort) |

> \* CPython's Timsort may need an O(n) temp buffer in the worst case, but **no new list object is allocated**.

```python
# Pattern :
# V1 : IN PLACE, returns None
_array.sort(key = lambda x : <your_sorting_func>)

# V2 : returns a NEW list, `_array` unchanged
new_array = sorted(_array, key = lambda x : <your_sorting_func>)

# 049  Group Anagrams
strs = ["eat","tea","tan","ate","nat","bat"]
strs.sort(key = lambda x : ''.join(sorted(x)) )
print (strs)
# ['bat', 'eat', 'tea', 'ate', 'tan', 'nat']

### NOTE can use this as well
sorted(strs, key = lambda x : ''.join(sorted(x)))
```

**🚫 Common Mistakes:**

```python
# 1) Assigning the result of an in-place sort
arr = arr.sort()          # ❌ arr becomes None !!!
arr.sort(); use(arr)      # ✅

# 2) Calling .sort() on a non-list
s = "cba"
s.sort()                  # ❌ AttributeError : 'str' has no attribute 'sort'
sorted(s)                 # ✅ ['a','b','c']

# 3) Sorting a dict / set (only `sorted` works)
sorted({'b':2, 'a':1})    # ✅ ['a','b']  <-- iterates over KEYS
sorted({3,1,2})           # ✅ [1,2,3]

# 4) Mutating the input when the caller still needs it
def f(nums):
    nums.sort()           # ❌ caller's list is modified (side effect)
    return nums
def f(nums):
    return sorted(nums)   # ✅ no side effect
```

**💡 When to Use Which:**

- **`.sort()`** → you own the list and want O(1) space (e.g. LC 406, LC 56 Merge Intervals, LC 253)
- **`sorted()`** → input is a string / dict / set / tuple, or the original order must be preserved

**Note on Java equivalent:**

```java
// java
// in place (like py .sort())
Arrays.sort(arr);                                  // primitive array, in place
Collections.sort(list);                            // List, in place
list.sort((a, b) -> a[0] - b[0]);                  // List, in place

// returns NEW collection (like py sorted())
List<Integer> sortedList = list.stream()
        .sorted()
        .collect(Collectors.toList());             // new list, original untouched
```

#### 1-1-6) Flatten Array
```python
# LC 341
# V0
class NestedIterator(object):

    def __init__(self, nestedList):

        self.queue = []
        
        def getAll(nests):
            for nest in nests:
                if nest.isInteger():
                    self.queue.append(nest.getInteger())
                else:
                    getAll(nest.getList())
        getAll(nestedList)

    def next(self):

        return self.queue.pop(0)

    def hasNext(self):

        return len(self.queue)

# default py
# V1
def flatten_array(_array):
    r = []
    def helper(_array):
        for i in _array:
            if type(i) == int:
                print (i)
                r.append(i)
            else:
                helper(i)

    helper(_array)
    return r
    
_input = [1,0, [1,2,[4,[5,[6,[7]]]]]]#[1,[4,[6]]] #[[1,1],2,[1,1]]

res = flatten_array(_input)
print ("res = " + str(res))

# V2
# https://stackoverflow.com/questions/2158395/flatten-an-irregular-list-of-lists
def flatten(L):
    for item in L:
        try:
            yield from flatten(item)
        except TypeError:
            yield item

r2 = flatten(_input)
r2_ = [x for x in r2]
print (r2_)

# V3
def flatten2(L):
    for item in L:
        try:
            yield from flatten2(item)
        except:
            yield item

r3 = flatten2(_input)
r3_ = [x for x in r3]
print (r3_)
```

```java
// java
// algorithm book (labu) p.355
//------------------------------------------
// implement NestedInteger data structure
//------------------------------------------
public class NestedInteger {
    private Integer val;
    private List<NestedInteger> list;

    public NestedInteger(Integer val){
        this.val = val;
        this.list = null;
    }

    public NestedInteger(List<NestedInteger> list){
        this.list = list;
        this.val = null;
    }

    // if saved value is integer, return true, else false
    public boolean isIntger(){
        return val != null;
    }

    // if saved value is integer, return it, else return null
    public Integer getInteger(){
        return this.val;
    }

    // if saved value is array, return it, else return null
    public List<NestedInteger> getList(){
        return this.list;
    }

}
```

```java
// java
// LC 341
// algorithm book (labu) p.357
//-----------------------------------------------------------
// NestedInteger solution V1 :  via tree algorithm
//-----------------------------------------------------------
class NestedIterator implements Iterator<Integer>{

    private Iterator<Integer> it;

    public NestedIterator(List<NestedInteger> nestedList){
        // save flatten result
        List<Integer> result = new LinkedList<>();

        for (NestedInteger node: nestedList){
            // start from each node and proceed
            traverse(node, result);
        }

        // get result's iterator
        this.it = result.iterator();
    }

    public Integer next(){
        return it.next();
    }

    public boolean hasNext(){
        return it.hasNext();
    }

    // traverse tree with root as root, and add nodes to result array
    private void traverse(NestedInteger root, List<Integer> result){
        if (root.isIntger()){
            // arrive root node
            result.add(root.getInteger());
            return;
        }

        // traverse framework
        for (NestedInteger child: root.getList()){
            traverse(child, result);
        }
    }
}
```

```java
// java
// LC 341
// algorithm book (labu) p.358
//-----------------------------------------------------------
// NestedInteger solution V2 :  via lazy calling
//-----------------------------------------------------------
public class NestedIterator implements Iterator<Integer>{

    private LinkedList<NestedInteger> list;

    public NestedIterator(List<NestedInteger> nestedList){
        // use LinkedList, for good performance in below op
        list = new LinkedList<>(nestedList);
    }

    public Integer next(){
        // hasNext method make sure 1st element must be Integer type
        return list.remove(0).getInteger();
    }

    public boolean hasNext(){
        // for loop split elements in array until 1st element is Integer type
        while (!list.isEmpty() && list.get(0).isIntger()){
            // when 1st element is array type, go into the loop
            List<NestedInteger> first = list.remove(0).getList();
            // flatten 1st array, and add to "start" in ordering
            for (int i = first.size() - 1; i >= 0; i--){
                list.addFirst(first.get(i));
            }  
        }
        return !list.isEmpty();
    }
}
```

#### 1-1-7) go through 2 arrays (length could be different)
```python
#--------------------
# example 1
#--------------------

# 2 array : s,t
# len(s) = 10, len(t) = 7
# or
# len(s) = 10, len(t) = 11
if len(s) > len(t):
    s,t  = t,s

for i in range(len(s)):
    print (s[i], t[i])


#--------------------
# example 2
#--------------------
# LC 165
class Solution:
    def compareVersion(self, version1: str, version2: str) -> int:
        nums1 = version1.split('.')
        nums2 = version2.split('.')
        n1, n2 = len(nums1), len(nums2)
        
        # NOTE here !!!
        # compare versions
        for i in range(max(n1, n2)):
            i1 = int(nums1[i]) if i < n1 else 0
            i2 = int(nums2[i]) if i < n2 else 0
            if i1 != i2:
                return 1 if i1 > i2 else -1
        
        # the versions are equal
        return 0
```


---

### 1-2) Special Array Algorithms ⭐⭐⭐⭐

#### 1-2-1) Boyer-Moore Majority Vote Algorithm

**Concept:**
- Find element(s) appearing more than ⌊n/k⌋ times in an array
- **Key Idea**: Pair different elements and cancel them out
- Majority element survives cancellation
- **Two-phase**: (1) Find candidates, (2) Verify counts
- **Space**: O(k) for k-1 candidates

**When to Use:**
- "Find majority element" → element appearing > n/2 times
- "Find all elements appearing more than n/3 times"
- "Heavy hitters" or "frequent elements" problems
- Need O(1) space (better than HashMap O(n))

**Related:** See [streaming_algorithms.md](./streaming_algorithms.md) for detailed explanation.

---

##### **Pattern 1: Standard Majority Element (> n/2) - LC 169**

**Algorithm:**
- Maintain one candidate and count
- When count=0, select new candidate
- Increment count for same element, decrement for different
- Majority element survives

```python
# Python - LC 169
def majorityElement(nums):
    """
    Find element appearing > n/2 times
    Time: O(n)
    Space: O(1)
    """
    candidate = None
    count = 0

    # Phase 1: Find candidate
    for num in nums:
        if count == 0:
            candidate = num
            count = 1
        elif num == candidate:
            count += 1
        else:
            count -= 1  # Cancel out

    # Phase 2: Verify (can skip if majority guaranteed)
    # return candidate

    # If not guaranteed, verify:
    if nums.count(candidate) > len(nums) // 2:
        return candidate
    return None
```

```java
// Java - LC 169
// V0
// IDEA: Boyer-Moore Majority Vote
/**
 * Key Insight:
 * - Pair different elements and cancel them out
 * - Majority element will survive cancellation
 * - Works because majority element appears > n/2 times
 *
 * Time: O(n)
 * Space: O(1)
 */
public int majorityElement(int[] nums) {
    /**
     * time = O(N)
     * space = O(1)
     */
    int candidate = nums[0];
    int count = 1;

    // Phase 1: Find candidate by cancellation
    for (int i = 1; i < nums.length; i++) {
        if (count == 0) {
            // Start new candidate when count reaches 0
            candidate = nums[i];
            count = 1;
        } else if (nums[i] == candidate) {
            // Same element: increment count
            count++;
        } else {
            // Different element: cancel out
            count--;
        }
    }

    // Phase 2: Verify (optional if majority guaranteed)
    // If problem guarantees majority exists, return candidate directly
    return candidate;

    // Otherwise, verify:
    // int actualCount = 0;
    // for (int num : nums) {
    //     if (num == candidate) actualCount++;
    // }
    // return actualCount > nums.length / 2 ? candidate : -1;
}
```

**Example Trace:** `nums = [2,2,1,1,1,2,2]`

```text
Index | num | candidate | count | Action
--------------------------------------------
  0   |  2  |     2     |   1   | Initialize
  1   |  2  |     2     |   2   | Same, increment
  2   |  1  |     2     |   1   | Different, decrement
  3   |  1  |     2     |   0   | Different, decrement
  4   |  1  |     1     |   1   | Count=0, new candidate
  5   |  2  |     1     |   0   | Different, decrement
  6   |  2  |     2     |   1   | Count=0, new candidate

Result: 2 (appears 4 times > 7/2 = 3.5)
```

---

##### **Pattern 2: Elements Appearing > n/3 Times - LC 229**

**Key Insight:** At most 2 elements can appear more than n/3 times.

**Algorithm:**
- Maintain two candidates and two counts
- Cancellation requires decrementing both counts
- Must verify both candidates in phase 2

```python
# Python - LC 229
def majorityElement(nums):
    """
    Find all elements appearing > n/3 times
    Time: O(n)
    Space: O(1)
    """
    # Phase 1: Find up to 2 candidates
    candidate1, candidate2 = None, None
    count1, count2 = 0, 0

    for num in nums:
        if num == candidate1:
            count1 += 1
        elif num == candidate2:
            count2 += 1
        elif count1 == 0:
            candidate1, count1 = num, 1
        elif count2 == 0:
            candidate2, count2 = num, 1
        else:
            # Different from both: cancel out
            count1 -= 1
            count2 -= 1

    # Phase 2: Verify candidates
    result = []
    for candidate in [candidate1, candidate2]:
        if candidate is not None and nums.count(candidate) > len(nums) // 3:
            result.append(candidate)

    return result
```

```java
// Java - LC 229
// V0
// IDEA: Boyer-Moore Majority Vote (Generalized)
/**
 * Key Insight:
 * - At most 2 elements can appear > n/3 times
 * - Use 2 candidates and 2 counts
 * - Cancellation decrements both counts
 * - MUST verify both candidates
 *
 * Time: O(n)
 * Space: O(1)
 */
public List<Integer> majorityElement(int[] nums) {
    /**
     * time = O(N)
     * space = O(1)
     */
    int candidate1 = 0, candidate2 = 0;
    int count1 = 0, count2 = 0;

    // Phase 1: Find up to 2 candidates
    for (int num : nums) {
        if (num == candidate1) {
            count1++;
        } else if (num == candidate2) {
            count2++;
        } else if (count1 == 0) {
            candidate1 = num;
            count1 = 1;
        } else if (count2 == 0) {
            candidate2 = num;
            count2 = 1;
        } else {
            // Different from both: cancel out
            count1--;
            count2--;
        }
    }

    // Phase 2: Verify candidates (REQUIRED!)
    count1 = 0;
    count2 = 0;
    for (int num : nums) {
        if (num == candidate1) count1++;
        else if (num == candidate2) count2++;
    }

    List<Integer> result = new ArrayList<>();
    if (count1 > nums.length / 3) result.add(candidate1);
    if (count2 > nums.length / 3) result.add(candidate2);

    return result;
}
```

**Example Trace:** `nums = [3,2,3]`

```text
Index | num | c1 | cnt1 | c2 | cnt2 | Action
-------------------------------------------------
  0   |  3  | 3  |  1   | 0  |  0   | Set candidate1
  1   |  2  | 3  |  1   | 2  |  1   | Set candidate2
  2   |  3  | 3  |  2   | 2  |  1   | Match candidate1

Verification:
- candidate1=3: appears 2 times > 3/3 = 1 ✓
- candidate2=2: appears 1 time ≤ 3/3 = 1 ✗

Result: [3]
```

---

##### **Pattern 3: Generalized k-Majority (> n/k times)**

**Concept:** For elements appearing more than n/k times, at most k-1 candidates exist.

```java
// Generalized Boyer-Moore for n/k threshold
import java.util.*;

class BoyerMooreGeneralized {
    /**
     * Find all elements appearing > n/k times
     * time = O(N × k)
     * space = O(k)
     */
    public List<Integer> majorityElement(int[] nums, int k) {
        // At most k-1 candidates for n/k threshold
        Map<Integer, Integer> candidates = new HashMap<>();

        // Phase 1: Find up to k-1 candidates
        for (int num : nums) {
            if (candidates.containsKey(num)) {
                candidates.put(num, candidates.get(num) + 1);
            } else if (candidates.size() < k - 1) {
                candidates.put(num, 1);
            } else {
                // Decrement all counts (cancellation)
                List<Integer> toRemove = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : candidates.entrySet()) {
                    int count = entry.getValue() - 1;
                    if (count == 0) {
                        toRemove.add(entry.getKey());
                    } else {
                        candidates.put(entry.getKey(), count);
                    }
                }
                for (int key : toRemove) {
                    candidates.remove(key);
                }
            }
        }

        // Phase 2: Verify all candidates
        Map<Integer, Integer> counts = new HashMap<>();
        for (int num : nums) {
            if (candidates.containsKey(num)) {
                counts.put(num, counts.getOrDefault(num, 0) + 1);
            }
        }

        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > nums.length / k) {
                result.add(entry.getKey());
            }
        }

        return result;
    }
}
```

---

#### **Boyer-Moore — Common Mistakes & Tips**

**🚫 Common Mistakes:**

1. **Missing Verification Phase**
   ```java
   // ❌ WRONG: Assuming candidate is always majority
   return candidate;

   // ✅ CORRECT: Verify count (if not guaranteed)
   int actualCount = 0;
   for (int num : nums) {
       if (num == candidate) actualCount++;
   }
   return actualCount > nums.length / 2 ? candidate : -1;
   ```

2. **Wrong Cancellation Logic for LC 229**
   ```java
   // ❌ WRONG: Only checking candidate1
   if (num != candidate1) count1--;

   // ✅ CORRECT: Check both, reset on count=0
   if (num == candidate1) {
       count1++;
   } else if (num == candidate2) {
       count2++;
   } else if (count1 == 0) {
       candidate1 = num; count1 = 1;
   } else if (count2 == 0) {
       candidate2 = num; count2 = 1;
   } else {
       count1--; count2--;
   }
   ```

3. **Not Handling Duplicate Candidates (LC 229)**
   ```java
   // ❌ WRONG: candidate1 and candidate2 can be same initially

   // ✅ CORRECT: Check candidate1 first, then candidate2
   if (num == candidate1) count1++;
   else if (num == candidate2) count2++;
   // ... rest of logic
   ```

**💡 Interview Tips:**

1. **When to Use:**
   - "Find majority element" keywords
   - Need O(1) space (vs HashMap O(n))
   - Guaranteed majority exists

2. **Talking Points:**
   - "Pairing and cancellation eliminates non-majority elements"
   - "At most k-1 elements can appear more than n/k times"
   - "Two-phase: find candidates, then verify"

3. **Complexity:**
   - Time: O(n) single pass (+ O(n) for verification = O(n) total)
   - Space: O(1) for standard, O(k) for generalized

---

#### **Boyer-Moore — Related LeetCode Problems**

| Problem | Difficulty | Threshold | Candidates | Verify? |
|---------|------------|-----------|------------|---------|
| LC 169 | Easy | > n/2 | 1 | Optional* |
| LC 229 | Medium | > n/3 | 2 | Required |
| General | - | > n/k | k-1 | Required |

*Optional if problem guarantees majority element exists.

---

**Summary:**
- ✅ Boyer-Moore: O(n) time, O(1) space majority finding
- ✅ Key insight: Cancellation eliminates non-majority elements
- ✅ Two-phase: (1) Find candidates, (2) Verify counts
- ✅ For > n/2: 1 candidate, for > n/3: 2 candidates, for > n/k: k-1 candidates
- ✅ Alternative: HashMap O(n) space for exact counts

---

#### 1-2-2) Frequency Array + Running Count

**Concept:**
- Track element occurrences using a frequency array (when values are bounded, e.g., 1 to n)
- Use array index as hash key for O(1) lookups
- Maintain a running count that updates as conditions are met
- **Key Insight**: When frequency reaches a threshold, trigger an action

**When to Use:**
- Values are bounded/constrained (e.g., permutations with values 1 to n)
- Need to track "seen in both" or "appeared k times" conditions
- Prefix-based counting problems
- Real-time/streaming count updates

**Related:** Useful for permutation problems, prefix arrays, and intersection counting.

---

##### **Pattern: Prefix Common Array (LC 2657)**

**Problem:** Given two permutations A and B of length n, find C where C[i] = count of numbers present at or before index i in both A and B.

**Algorithm:**
- Use frequency array of size n+1 (since values are 1 to n)
- For each index, process A[i] and B[i]
- When any element's frequency reaches 2, it's common (seen in both arrays)
- Increment running count and store in result

```python
# Python - LC 2657
def findThePrefixCommonArray(A, B):
    """
    Find prefix common array using frequency counting.

    Key Insight:
    - Each number appears at most twice total (once in A, once in B)
    - When count[x] == 2, x has been seen in both arrays → common element

    Time: O(n)
    Space: O(n)
    """
    n = len(A)
    res = [0] * n
    count = [0] * (n + 1)  # values are 1..n
    common = 0

    for i in range(n):
        # Process element from A
        count[A[i]] += 1
        if count[A[i]] == 2:
            common += 1

        # Process element from B
        count[B[i]] += 1
        if count[B[i]] == 2:
            common += 1

        res[i] = common

    return res
```

```java
// Java - LC 2657
// V0
// IDEA: Frequency Array + Running Count
/**
 * Core Insight:
 * - Each number appears at most twice total (once in A, once in B)
 * - When frequency[x] == 2, x is present in both arrays → common element
 * - Running count tracks cumulative common elements
 *
 * Why This Works:
 * - Permutation guarantee: each value 1..n appears exactly once in each array
 * - Processing both arrays simultaneously at each index
 * - frequency[x] can only be 0, 1, or 2
 * - frequency[x] == 2 means: seen once in A AND once in B
 *
 * Time: O(n) - single pass through both arrays
 * Space: O(n) - frequency array of size n+1
 */
public int[] findThePrefixCommonArray(int[] A, int[] B) {
    int n = A.length;
    int[] res = new int[n];

    // Since values are 1 to n, use array as hash map
    int[] frequency = new int[n + 1];
    int commonCount = 0;

    for (int i = 0; i < n; i++) {
        // Process element from A
        frequency[A[i]]++;
        if (frequency[A[i]] == 2) {
            commonCount++;  // Now seen in both arrays
        }

        // Process element from B
        frequency[B[i]]++;
        if (frequency[B[i]] == 2) {
            commonCount++;  // Now seen in both arrays
        }

        // Store current prefix common count
        res[i] = commonCount;
    }

    return res;
}
```

**Example Trace:** `A = [1,3,2,4], B = [3,1,2,4]`

```text
Index | A[i] | B[i] | frequency (after)     | commonCount | Action
----------------------------------------------------------------------
  0   |  1   |  3   | [0,1,0,1,0]           |     0       | freq[1]=1, freq[3]=1
  1   |  3   |  1   | [0,2,0,2,0]           |     2       | freq[3]=2 ✓, freq[1]=2 ✓
  2   |  2   |  2   | [0,2,2,2,0]           |     3       | freq[2]=1, then freq[2]=2 ✓
  3   |  4   |  4   | [0,2,2,2,2]           |     4       | freq[4]=1, then freq[4]=2 ✓

Result: [0, 2, 3, 4]
```

---

##### **Generalized Pattern: Frequency Threshold Detection**

Use this pattern when you need to detect when elements reach a specific count threshold:

```java
// Generalized frequency threshold pattern
/**
 * Detect when elements reach threshold k
 * Useful for: intersection counting, duplicate detection, k-frequency problems
 */
public void frequencyThresholdPattern(int[] arr1, int[] arr2, int maxVal, int threshold) {
    int[] frequency = new int[maxVal + 1];
    int count = 0;

    for (int i = 0; i < arr1.length; i++) {
        // Process from first source
        frequency[arr1[i]]++;
        if (frequency[arr1[i]] == threshold) {
            count++;  // Element reached threshold
        }

        // Process from second source (if applicable)
        frequency[arr2[i]]++;
        if (frequency[arr2[i]] == threshold) {
            count++;
        }

        // Use count as needed...
    }
}
```

---

#### **Frequency Array + Running Count — Common Mistakes & Tips**

**🚫 Common Mistakes:**

1. **Wrong Array Size**
   ```java
   // ❌ WRONG: Off-by-one for 1-indexed values
   int[] frequency = new int[n];  // Can't access frequency[n]

   // ✅ CORRECT: Size n+1 for values 1..n
   int[] frequency = new int[n + 1];
   ```

2. **Checking Threshold Before Increment**
   ```java
   // ❌ WRONG: Check before increment misses the transition
   if (frequency[x] == 2) count++;
   frequency[x]++;

   // ✅ CORRECT: Increment first, then check
   frequency[x]++;
   if (frequency[x] == 2) count++;
   ```

3. **Not Handling Same Element in Both Arrays at Same Index**
   ```java
   // For A[i] == B[i] case, frequency goes 0→1→2 in same iteration
   // This is handled correctly by processing A[i] then B[i] separately
   ```

**💡 Interview Tips:**

1. **When to Use:**
   - "Permutation" or "values 1 to n" keywords
   - "Prefix" or "running" count requirements
   - "Common elements" or "intersection" problems
   - Need O(1) lookup with bounded values

2. **Talking Points:**
   - "Array index as hash key gives O(1) lookup"
   - "Frequency threshold detection for condition triggers"
   - "Running count avoids recomputation"

3. **Complexity:**
   - Time: O(n) single pass
   - Space: O(n) or O(max_value) for frequency array

---

#### **Frequency Array + Running Count — Related LeetCode Problems**

| Problem | Difficulty | Pattern Variant |
|---------|------------|-----------------|
| LC 2657 | Medium | Prefix common array (frequency == 2) |
| LC 349 | Easy | Intersection of two arrays (frequency >= 1 in both) |
| LC 350 | Easy | Intersection with duplicates |
| LC 442 | Medium | Find duplicates (frequency == 2, in-place) |
| LC 448 | Easy | Find missing numbers (frequency == 0) |
| LC 645 | Easy | Set mismatch (frequency == 2 and == 0) |
| LC 1 | Easy | Two Sum (complement frequency check) |
| LC 217 | Easy | Contains duplicate (frequency >= 2) |

---

**Summary:**
- ✅ Frequency Array: Use array index as hash for bounded values (O(1) lookup)
- ✅ Running Count: Maintain cumulative count, update on threshold
- ✅ Key Insight: frequency[x] == k means x appeared k times across sources
- ✅ Best for: Permutations, prefix problems, intersection counting
- ✅ Alternative: HashMap for unbounded/sparse values

---

#### 1-2-3) Index Contribution Counting (Count Each Element's Appearances Across All Subarrays)

**Concept:**
- Instead of enumerating every subarray (O(n²) or worse), ask:
  > "For a single element at index `i`, **how many subarrays contain it?**"
- Then sum that contribution over all relevant elements in **one pass — O(n)**.
- **Key Formula** (for an array/string of length `n`, element at index `i`):

```text
# subarrays containing index i
   = (choices for LEFT boundary) × (choices for RIGHT boundary)
   = (i + 1) × (n - i)

   where:
     left  boundary ∈ {0, 1, ..., i}     → (i + 1) choices
     right boundary ∈ {i, i+1, ..., n-1} → (n - i) choices
```

**Why This Works:**
- A subarray is fully determined by its `(left, right)` pair with `left <= i <= right`.
- The left end can start at any index from `0` to `i` → `i + 1` options.
- The right end can finish at any index from `i` to `n - 1` → `n - i` options.
- Every combination is a distinct subarray that contains index `i`, so the product counts them all.

**When to Use:**
- "Sum / count of X **over all substrings/subarrays**" where each element contributes independently
- The per-element contribution does **not** depend on which subarray it sits in (e.g. counting vowels, summing values, counting matches)
- You want to avoid generating all O(n²) subarrays explicitly

---

##### **Pattern: LC 2063 - Vowels of All Substrings**

**Problem:** Sum the number of vowels in **every** substring of `word`.

**Insight:** Each vowel at index `i` is counted once per substring that contains it. That count is exactly `(i + 1) * (n - i)`. So just sum that product over all vowel positions.

```python
# Python - LC 2063 Vowels of All Substrings
# IDEA: each vowel at index i appears in (i+1)*(n-i) substrings
class Solution(object):
    def countVowels(self, word):
        # time = O(n), space = O(1)
        total_vowel_count = 0
        vowels = set("aeiou")
        n = len(word)

        # single pass: accumulate each vowel's contribution
        for i in range(n):
            if word[i] in vowels:
                starting_choices = i + 1      # left boundary: 0..i
                ending_choices = n - i        # right boundary: i..n-1
                total_vowel_count += starting_choices * ending_choices

        return total_vowel_count
```

```java
// Java - LC 2063 Vowels of All Substrings
// IDEA: each vowel at index i appears in (i+1)*(n-i) substrings
class Solution {
    /**
     * time = O(n), space = O(1)
     *
     * Each vowel at index i is contained in (i+1)*(n-i) substrings:
     *   - left  boundary can be any of 0..i      → (i+1) choices
     *   - right boundary can be any of i..n-1     → (n-i) choices
     * Use `long` for the running total — it can exceed int range.
     */
    public long countVowels(String word) {
        long total = 0;
        int n = word.length();
        String vowels = "aeiou";

        for (int i = 0; i < n; i++) {
            if (vowels.indexOf(word.charAt(i)) >= 0) {
                long startingChoices = i + 1;   // left boundary: 0..i
                long endingChoices = n - i;     // right boundary: i..n-1
                total += startingChoices * endingChoices;
            }
        }

        return total;
    }
}
```

**Example Trace:** `word = "aba"` (n = 3)

```text
Index | char | vowel? | (i+1) | (n-i) | contribution
---------------------------------------------------------
  0   |  a   |  yes   |   1   |   3   |   1 * 3 = 3
  1   |  b   |  no    |   -   |   -   |   0
  2   |  a   |  yes   |   3   |   1   |   3 * 1 = 3

Total = 3 + 0 + 3 = 6

Verify by listing all substrings & their vowel counts:
  "a"   → 1     "ab"  → 1     "aba" → 2
  "b"   → 0     "ba"  → 1
  "a"   → 1
  sum = 1+1+2+0+1+1 = 6 ✓
```

---

##### **Generalized Pattern: Per-Element Contribution**

```java
// Generic "sum a per-element value over all subarrays" template
// time = O(n), space = O(1)
public long sumOverAllSubarrays(int[] arr) {
    long total = 0;
    int n = arr.length;
    for (int i = 0; i < n; i++) {
        long subarraysContainingI = (long) (i + 1) * (n - i);
        total += arr[i] * subarraysContainingI;   // arr[i]'s total contribution
    }
    return total;
}
```

> **Note:** This works whenever an element's contribution is **independent of the subarray boundaries** (counting, summing). When the contribution depends on the element being a min/max within the subarray, use the **Monotonic Stack "Sum of Subarray Minimums" (LC 907)** variant, which computes per-element span via `(left span) * (right span)` instead.

---

#### **Index Contribution Counting — Common Mistakes & Tips**

**🚫 Common Mistakes:**

1. **Off-by-one in the boundary counts**
   ```text
   ❌ left choices = i        (forgets index 0..i is i+1 values)
   ✅ left choices = i + 1
   ❌ right choices = n - i - 1
   ✅ right choices = n - i
   ```

2. **Integer overflow**
   ```java
   // ❌ WRONG: (i+1)*(n-i) can overflow int for large n
   int total = 0; total += (i + 1) * (n - i);

   // ✅ CORRECT: use long
   long total = 0; total += (long) (i + 1) * (n - i);
   ```

3. **Confusing "contains index i" with "starts/ends at i"**
   - Subarrays *containing* i: `(i+1) * (n-i)`
   - Subarrays *starting* at i: `n - i`
   - Subarrays *ending* at i: `i + 1`

**💡 Interview Tips:**
- Recognize the phrase **"over all substrings/subarrays"** + an independent per-element value → contribution counting.
- State it as: "Rather than O(n²) subarrays, I count how many subarrays each element joins — `(i+1)*(n-i)` — and sum."
- Mention `long` for overflow safety up front.

---

#### **Index Contribution Counting — Related LeetCode Problems**

| Problem | LC# | Difficulty | Contribution Idea |
|---------|-----|------------|-------------------|
| **Vowels of All Substrings** | **2063** | **Medium** | Each vowel adds `(i+1)*(n-i)` |
| Sum of All Subarray Minimums | 907 | Medium | Monotonic-stack span: `left * right` |
| Sum of Subarray Ranges | 2104 | Medium | Sum of (max contrib − min contrib) per element |
| Sum of Total Strength of Wizards | 2281 | Hard | Contribution + prefix-of-prefix sums |
| Number of Substrings Containing All Three Characters | 1358 | Medium | Count valid left boundaries per right |

---

**Summary:**
- ✅ Each index `i` is contained in `(i + 1) * (n - i)` subarrays
- ✅ Turns an O(n²) "over all subarrays" sum into a single O(n) pass
- ✅ Works when per-element contribution is independent of subarray bounds
- ✅ Watch for `long` overflow on the product
- ✅ For min/max-dependent contributions → monotonic stack span counting (LC 907)

---

#### 1-2-4) Backward Write Pointer (In-Place Merge / In-Place Overwrite) ⭐⭐⭐⭐⭐

**Key Idea:** when you must write results **into the same array you are still reading**, a forward write pointer can clobber unread data. If the destination has **free space at the tail**, walk **backwards** — the write pointer always sits at or ahead of both read pointers, so nothing is ever overwritten before it is consumed.

**Pattern:**

```text
read  ->  p1 (end of real data in dst), p2 (end of src)
write ->  the LAST slot of dst

while src not exhausted:
    write the LARGER of dst[p1] / src[p2]  into dst[write]
    move that read pointer back, move write back
```

**Why backwards is safe:** `write >= p1` always holds, because `write - p1 = (#unmerged items in src) >= 0`. So the slot being written is either already-consumed space or the padding zone.

**When to Use:**
- Merge a sorted array into another sorted array that has trailing space (LC 88)
- Any "produce output in place, output is never longer than input" rewrite (the forward version of the same idea is `slow`/`fast` in-place filtering, see [2_pointers.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/2_pointers.md))

---

##### **Pattern: LC 88 - Merge Sorted Array**

**Problem:** `nums1` has length `m + n` (first `m` are real, last `n` are padding zeros). Merge `nums2` into it, sorted, **in place**.

**Insight:** merging forwards would overwrite `nums1`'s unread values. Merge from the **largest** element down.

```java
// java
// LC 88 - Merge Sorted Array
// IDEA: fill nums1 from the BACK, always writing the larger of the two tails
public void merge(int[] nums1, int m, int[] nums2, int n) {
    // time = O(m + n), space = O(1)
    int p1 = m - 1;         // last real value in nums1
    int p2 = n - 1;         // last value in nums2
    int write = m + n - 1;  // write pointer: very end of nums1

    /**
     *  NOTE !!!
     *
     *   loop only while p2 >= 0.
     *   If nums2 runs out, whatever is left in nums1[0..p1] is
     *   ALREADY in the right place -> nothing more to do.
     *   (If nums1 runs out first, the `p1 >= 0` guard sends us
     *    down the nums2 branch and copies the rest over.)
     */
    while (p2 >= 0) {
        if (p1 >= 0 && nums1[p1] > nums2[p2]) {
            nums1[write--] = nums1[p1--];
        } else {
            nums1[write--] = nums2[p2--];
        }
    }
}
```

```python
# python
# LC 88 - Merge Sorted Array
# IDEA: fill nums1 from the BACK, always writing the larger of the two tails
class Solution(object):
    def merge(self, nums1, m, nums2, n):
        # time = O(m + n), space = O(1)
        p1, p2, write = m - 1, n - 1, m + n - 1

        # NOTE : loop on p2 only -> leftovers in nums1 are already in place
        while p2 >= 0:
            if p1 >= 0 and nums1[p1] > nums2[p2]:
                nums1[write] = nums1[p1]
                p1 -= 1
            else:
                nums1[write] = nums2[p2]
                p2 -= 1
            write -= 1
```

**Visual Trace** — `nums1 = [1,2,3,0,0,0], m = 3`, `nums2 = [2,5,6], n = 3`

```text
              p1        write
[1, 2, 3, _, _, _]   p2=2 (6)   6 > 3  -> write 6
[1, 2, 3, _, _, 6]   p2=1 (5)   5 > 3  -> write 5
[1, 2, 3, _, 5, 6]   p2=0 (2)   3 > 2  -> write 3 (from nums1)
[1, 2, _, 3, 5, 6]   p2=0 (2)   2 > 2? no -> write 2 (from nums2)
[1, 2, 2, 3, 5, 6]   p2 = -1  -> DONE, [1,2] already in place
```

**Common Mistakes:**
- ❌ Merging forwards (`write = 0`) — destroys `nums1` values you have not read yet
- ❌ Looping `while (p1 >= 0 || p2 >= 0)` and forgetting the `p1 >= 0` null-guard inside
- ❌ Copying the remaining `nums1` tail at the end — unnecessary, it is already correct
- ❌ Using `>=` vs `>` inconsistently — either is fine here (stability is irrelevant for ints)

---

#### 1-2-5) Array + Hash Index Map (O(1) Delete via Swap-With-Last) ⭐⭐⭐⭐⭐

**Key Idea:** deleting from the middle of an array is O(n) *only because of the shifting*. If **order does not matter**, you can delete in O(1):

```text
1. look up the victim's index i   (HashMap: value -> index)
2. move the LAST element into slot i   ("plug the hole")
3. update the moved element's index in the map
4. pop the last slot  (O(1))
```

The array gives **O(1) random access** (needed for `getRandom`), the hash map gives **O(1) lookup**. Together they beat both a plain `HashSet` (no indexable random pick) and a plain array (O(n) lookup/delete).

**When to Use:**
- Design questions needing `insert` / `remove` / `getRandom` all in O(1)
- Any unordered collection where you delete arbitrary elements by value
- Free-list / slot-reuse style bookkeeping

---

##### **Pattern: LC 380 - Insert Delete GetRandom O(1)**

```java
// java
// LC 380 - Insert Delete GetRandom O(1)
// IDEA: ArrayList for O(1) random access + HashMap<value, index>;
//       delete by swapping the victim with the LAST element
class RandomizedSet {
    // time = O(1) for insert / remove / getRandom, space = O(n)
    private final List<Integer> vals = new ArrayList<>();
    private final Map<Integer, Integer> idx = new HashMap<>(); // value -> index in vals
    private final Random rand = new Random();

    public boolean insert(int val) {
        if (idx.containsKey(val)) return false;
        idx.put(val, vals.size()); // new element goes to the tail
        vals.add(val);
        return true;
    }

    public boolean remove(int val) {
        Integer i = idx.get(val);
        if (i == null) return false;

        /**
         *  NOTE !!!  swap-with-last, then pop
         *
         *   - move the LAST value into the hole at index i
         *   - re-point the moved value's index in the map
         *   - remove the tail slot (O(1))
         *
         *   Order matters: `idx.remove(val)` MUST come after
         *   `idx.put(lastVal, i)`, otherwise the self-delete case
         *   (val IS the last element) leaves a stale entry behind.
         */
        int lastIdx = vals.size() - 1;
        int lastVal = vals.get(lastIdx);
        vals.set(i, lastVal);
        idx.put(lastVal, i);
        vals.remove(lastIdx); // remove by INDEX (int), not by object
        idx.remove(val);
        return true;
    }

    public int getRandom() {
        return vals.get(rand.nextInt(vals.size()));
    }
}
```

```python
# python
# LC 380 - Insert Delete GetRandom O(1)
# IDEA: list for O(1) random access + dict {value: index};
#       delete by swapping the victim with the LAST element
import random

class RandomizedSet(object):
    # time = O(1) for insert / remove / getRandom, space = O(n)
    def __init__(self):
        self.vals = []   # dense array of values
        self.idx = {}    # value -> index in self.vals

    def insert(self, val):
        if val in self.idx:
            return False
        self.idx[val] = len(self.vals)
        self.vals.append(val)
        return True

    def remove(self, val):
        if val not in self.idx:
            return False
        i = self.idx[val]
        last = self.vals[-1]
        ### NOTE : plug the hole with the last element, then pop
        self.vals[i] = last
        self.idx[last] = i
        self.vals.pop()
        ### NOTE : delete AFTER the re-index, so val == last still works
        del self.idx[val]
        return True

    def getRandom(self):
        return random.choice(self.vals)
```

**Why the array must stay dense:** `getRandom` needs a uniform pick, which is only `arr[rand(0, size)]` if there are **no holes**. Tombstoning (marking slots dead) breaks uniformity — hence swap-with-last.

**Common Mistakes:**
- ❌ `list.remove(value)` in Java on a `List<Integer>` — ambiguous overload; use `remove(int index)` or `remove(Integer.valueOf(v))`
- ❌ Deleting the map entry **before** re-pointing the moved element (breaks when the victim *is* the last element)
- ❌ Using `vals.remove(i)` (remove-at-middle) instead of pop-tail → back to O(n)
- ❌ Trying `HashSet` + `iterator.next()` for `getRandom` → O(n) and not uniform

---

#### 1-2-6) Bounded Candidate Enumeration (Only a Few Answers Are Possible)

**Key Idea:** some "make everything equal / make everything valid" problems look like they need a search over all values, but the constraints pin the answer to a **tiny candidate set** — usually derived from index `0`. Enumerate those 1-2 candidates and verify each in O(n).

**Recognition:** *"if a valid answer `x` exists, then position 0 must already contain `x`"* → candidates = `{tops[0], bottoms[0]}`, so total work is `2 * O(n)`.

---

##### **Pattern: LC 1007 - Minimum Domino Rotations For Equal Row**

**Problem:** given `tops[]` / `bottoms[]` of dominoes, find the min rotations so that one whole row is the same value (or `-1`).

**Insight:** the target value must appear on domino `0` — otherwise domino 0 can never show it. So only `tops[0]` and `bottoms[0]` need checking.

```java
// java
// LC 1007 - Minimum Domino Rotations For Equal Row
// IDEA: the answer must be tops[0] or bottoms[0] -> just verify both
public int minDominoRotations(int[] tops, int[] bottoms) {
    // time = O(n), space = O(1)
    int res = check(tops[0], tops, bottoms);
    // if tops[0] works, or tops[0] == bottoms[0] (only 1 candidate), we are done
    if (res != -1 || tops[0] == bottoms[0]) return res;
    return check(bottoms[0], tops, bottoms);
}

// how many rotations to make EVERY domino show x (on one row) ? -1 if impossible
private int check(int x, int[] tops, int[] bottoms) {
    int rotTop = 0, rotBottom = 0;
    for (int i = 0; i < tops.length; i++) {
        // x missing on this domino entirely -> impossible
        if (tops[i] != x && bottoms[i] != x) return -1;
        if (tops[i] != x) rotTop++;          // need a flip to put x on TOP
        else if (bottoms[i] != x) rotBottom++; // need a flip to put x on BOTTOM
    }
    return Math.min(rotTop, rotBottom);
}
```

```python
# python
# LC 1007 - Minimum Domino Rotations For Equal Row
# IDEA: the answer must be tops[0] or bottoms[0] -> just verify both
class Solution(object):
    def minDominoRotations(self, tops, bottoms):
        # time = O(n), space = O(1)
        n = len(tops)

        def check(x):
            rot_top = rot_bottom = 0
            for i in range(n):
                if tops[i] != x and bottoms[i] != x:
                    return -1
                if tops[i] != x:
                    rot_top += 1
                elif bottoms[i] != x:
                    rot_bottom += 1
            return min(rot_top, rot_bottom)

        res = check(tops[0])
        if res != -1 or tops[0] == bottoms[0]:
            return res
        return check(bottoms[0])
```

**Common Mistakes:**
- ❌ Looping all values `1..6` — works here but misses the transferable insight (and breaks when the value range is large)
- ❌ Counting `rotTop` and `rotBottom` in the same `if` (must be `if / elif`: a domino with `x` on **both** faces needs no rotation at all)
- ❌ Forgetting the `tops[0] == bottoms[0]` short-circuit → checking the same candidate twice

---

#### 1-2-7) Other High-Frequency Array Problems (Quick Reference)

| Problem | LC# | Diff | One-line technique |
|---------|-----|------|--------------------|
| Verifying an Alien Dictionary | 953 | Easy | Map `char -> rank`, then compare **adjacent pairs** only; shorter-is-prefix ⇒ must come first |
| Prison Cells After N Days | 957 | Medium | Simulate until a state **repeats**, then `N %= cycle_len` — turns `N = 10^9` into O(cycle) |
| Longest Common Prefix | 14 | Easy | Vertical scan: fix column `j`, compare that char across all strings; stop at first mismatch |
| Minimum Moves to Equal Array Elements | 453 | Medium | "+1 to n-1 elements" ≡ "-1 to one element" ⇒ answer = `sum(nums) - n * min(nums)` |

---


## 2) Pattern Selection

Most problems tagged **array** are not array problems. They are window, pointer, prefix-sum or
sorting problems that happen to arrive in an array, and each of those has its own sheet. This
table exists to get you out of this file fast — it only keeps what genuinely belongs to the
array itself: **rewriting it in place, and using its indices as storage.**

| The problem is really about… | Go to | Not here, because |
|---|---|---|
| a contiguous run whose length or content varies | [sliding_window.md](./sliding_window.md) | the window's expand/shrink rule is the whole problem |
| two indices converging, or a fast/slow pair | [2_pointers.md](./2_pointers.md) | the invariant lives between the pointers, not in the array |
| range sums or counts answered repeatedly | [prefix_sum.md](./prefix_sum.md) | the precomputation *is* the technique |
| many range **updates**, few reads | [difference_array.md](./difference_array.md) | you update endpoints, not ranges |
| finding a value or a boundary in sorted data | [binary_search.md](./binary_search.md) | the array being sorted is the premise, not the trick |
| pairs or triples summing to a target | [n_sum.md](./n_sum.md) | sorting plus pointers, once, for the whole family |
| ordering itself — custom keys, stability, partial sorts | [sort.md](./sort.md) | the comparator is the answer |
| a sort key with a tie-break, or a mixed-direction sort | [python_trick.md](./python_trick.md#multi-key-tuple-sort-keylambda-x-x0-x1-) and [java_trick_strings_sorting.md](./java_trick_strings_sorting.md#custom-sort--comparator-return-value-rules-) | it is a comparator question, not an array one |
| a 2D grid | [matrix.md](./matrix.md) | traversal order and boundary handling dominate |
| the best sum/product ending at each index | [kadane_algorithm.md](./kadane_algorithm.md) | it is a one-line DP recurrence |
| buying and selling with constraints | [stock_trading.md](./stock_trading.md) | the state machine is the point |

### What this sheet does own

| If you need to… | Technique | Written out at |
|---|---|---|
| overwrite in place without losing unread data | **backward write pointer** — fill from the end, where the free space is | [1-2-4)](#1-2-4-backward-write-pointer-in-place-merge--in-place-overwrite-) |
| record "I have seen value `v`" with no extra space | **index as hash** — negate `nums[v]`, always dereference through `abs()` | [1-2-5)](#1-2-5-array--hash-index-map-o1-delete-via-swap-with-last-), [examples 1)](./array_examples.md#1-first-missing-positive--lc-41-) |
| put every value at the index it belongs to | **cyclic sort** — swap until `nums[i] == i + 1`, then scan for the gap | [examples 1)](./array_examples.md#1-first-missing-positive--lc-41-) |
| delete an arbitrary element in O(1) | **swap with last, then pop** — plus a value → index map | [1-2-5)](#1-2-5-array--hash-index-map-o1-delete-via-swap-with-last-) |
| find the majority element in O(1) space | **Boyer-Moore vote** — a candidate and a counter, nothing else | [1-2-1)](#1-2-1-boyer-moore-majority-vote-algorithm) |
| count occurrences when values are small and bounded | **frequency array**, not a hash map | [1-2-2)](#1-2-2-frequency-array--running-count) |
| rotate by `k` in place | **reverse all, reverse the first `k`, reverse the rest** | [examples 2)](./array_examples.md#2-rotate-array--lc-189-) |
| answer when only a handful of results are possible | **bounded candidate enumeration** — try them all | [1-2-6)](#1-2-6-bounded-candidate-enumeration-only-a-few-answers-are-possible) |

### The three traps

1. **Forward overwriting destroys unread input.** Merging into `nums1` from the front clobbers
   the values still to be read. Fill backwards; the tail is the part that is guaranteed free.
2. **Sign-marking without `abs()`.** Once a slot has been negated, reading it raw gives a
   negative index. Every read goes through `abs(...)`, and restore the signs if the caller
   still needs the array.
3. **Index-as-hash needs the values to be legal indices.** Clamp or skip anything outside
   `1..n` *before* using values as offsets, or the trick becomes an out-of-range crash.

## 3) Worked Examples

Thirteen problems live in **[array_examples.md](./array_examples.md)**:

| Group | Problems |
|---|---|
| [In-place rewriting & index tricks](./array_examples.md#in-place-rewriting--index-tricks) | LC 41, 287, 189, 238, 670 |
| [Scanning & running state](./array_examples.md#scanning--running-state) | LC 121, 1567, 334, 849 |
| [Counting, bookings & simulation](./array_examples.md#counting-bookings--simulation) | LC 1109, 1375, 1041, 406, 251 |

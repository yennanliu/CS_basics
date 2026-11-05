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

### 0-2) Recognition

Most "array" problems route out to a pattern sheet (see [2) Pattern Selection](#2-pattern-selection)).
These are the signals that stay here. Constraint cues follow the ruler in
[complexity_cheatsheet.md](./complexity_cheatsheet.md).

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "merge into `nums1` **in place**", "the last n slots are 0 and should be ignored" | O(1) extra space follow-up | [Template 6](#template-6-backward-write-pointer--lc-88) backward write pointer | the free space is at the front (remove duplicates) → forward slow/fast, [2_pointers.md](./2_pointers.md) |
| "`insert` / `remove` / `getRandom` in **average O(1)**" | ~10⁵ calls | [Template 7](#template-7-swap-with-last-delete--lc-380) swap-with-last delete | duplicates allowed (LC 381) → value maps to a set of indices |
| "appears **more than ⌊n/2⌋** times", "O(1) extra space" | n ≤ 5·10⁴, one pass | [Template 2](#template-2-majority-vote-one-candidate--lc-169) majority vote | "most frequent" / "top k" → counts, [hash_map.md](./hash_map.md) |
| "all elements appearing **more than ⌊n/3⌋** times" | O(1) space | [Template 3](#template-3-majority-vote-k--1-candidates--lc-229) k − 1 candidates | |
| "permutation of `1..n`", "for every prefix, how many …" | values ≤ n → an array is the hash map | [Template 4](#template-4-frequency-array--running-count--lc-2657) frequency array + running count | values up to 10⁹ → a hash map with the same crossing test |
| "sum of X over **all substrings / subarrays**" | n ≤ 10⁵ → cannot list O(n²) subarrays | [Template 5](#template-5-index-contribution-counting--lc-2063) `(i + 1) * (n - i)` | X counts only when the element is the min / max (LC 907) → [monotonic_stack.md](./monotonic_stack.md) |
| "make every element of a row equal, minimum operations, or −1" | each element has ~2 choices | [Template 8](#template-8-bounded-candidate-enumeration--lc-1007) candidates from index 0 | the target can be any value → sort / median, [sort.md](./sort.md) |
| "design an iterator" over a 2D array, `next()` / `hasNext()` | O(1) amortized per call | [Template 1](#template-1-lazy-two-cursor-iterator--lc-251-) two cursors | unbounded nesting (LC 341) → a stack of iterators, [iterator.md](./iterator.md) |

## 1) General form

### 1-1) Basic OP

#### 1-1-0) Split Array
```python
# python
# itertools.islice(seq, [start,] stop [, step]) slices any iterator without building a list
import itertools

x = itertools.islice(range(10), 0, 9, 2)
print(list(x))   # -> [0, 2, 4, 6, 8]

y = itertools.islice(range(10), 0, 10, 3)
print(list(y))   # -> [0, 3, 6, 9]
```

#### 1-1-1) Insert into Array
```python
# python
p = [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
p.insert(1, [6, 1])   # O(n): shifts everything after index 1
print(p)   # -> [[7, 0], [6, 1], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
```

#### 1-1-2) Delete from Array
```python
# python
p = [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
p.remove([7, 1])      # O(n): removes the FIRST equal element; ValueError if absent
print(p)   # -> [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4]]
```

#### 1-1-3) check if element in Array
```python
# python
p = [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4]]
print([7, 0] in p)    # -> True   (O(n) scan; use a set for repeated lookups)
```

#### 1-1-4) append to array (head, tail)
```python
# python
p = [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4]]
p.append([0, 0])      # tail: O(1) amortized
print(p)   # -> [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4], [0, 0]]
p.insert(0, [9, 9])   # head: O(n); use collections.deque.appendleft for O(1)
print(p[0])   # -> [9, 9]
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

```text
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
list.sort((a, b) -> Integer.compare(a[0], b[0]));                  // List, in place

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
        while (!list.isEmpty() && !list.get(0).isIntger()){
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

##### Template 1: Lazy Two-Cursor Iterator — LC 251 ⭐⭐⭐⭐

**Recognise** — "design an iterator" / "implement `next()` and `hasNext()`" over a 2D or nested
array, often with a follow-up of "the input is huge" or "use O(1) extra space". Calls ≤ 10⁵ →
each call must be O(1) amortized.

**Key point** — `(row, col)` always points into the **original** array, and after `hasNext()`
returns it sits on a real element; only `next()` ever moves past one. So nothing is copied, and
each row is skipped once over the whole pass.

| | Eager (flatten in the constructor) | **Lazy (two cursors)** |
|---|---|---|
| Constructor | O(N): copies every element | O(1) |
| Extra space | O(N) | **O(1)** |
| `next()` / `hasNext()` | O(1) | O(1) amortized: each row is skipped once over the whole pass, O(R + N) total |
| Works on a huge or changing source | no: it is a snapshot | yes: nothing is pre-read |

`N` = total number of elements, `R` = number of rows. The eager version is in
[array_examples.md §13](./array_examples.md#13-flatten-2d-vector--lc-251). The lazy one is
what an interviewer means by "design an iterator", and it is the 2D case of the
[`hasNext()` / `next()` contract](./iterator.md#1-2-the-hasnext--next-contract-) in
iterator.md: the depth is fixed at two, so two indices do the job of a stack.

```python
# python
# LC 251 - Flatten 2D Vector
# IDEA: lazy iterator - two cursors (row, col) into the ORIGINAL 2D array;
#       hasNext() skips exhausted / empty rows, next() reads one element and steps col
# time = O(1) amortized per call (O(R + N) over a full pass), space = O(1) extra
class Vector2D:

    def __init__(self, vec):
        self.vec = vec      # no copy: iterate the input where it is
        self.row = 0        # which inner list
        self.col = 0        # which index inside vec[row]

    def hasNext(self):
        # normalize: move (row, col) onto the next real element, if there is one.
        # re-entrant -> a second call finds col already valid and returns at once
        while self.row < len(self.vec):
            if self.col < len(self.vec[self.row]):
                return True
            # this row is used up (or was empty from the start): go to the next one
            self.row += 1
            self.col = 0
        return False

    def next(self):
        # normalize first, so next() is correct even if the caller never called hasNext()
        if not self.hasNext():
            raise StopIteration
        val = self.vec[self.row][self.col]
        self.col += 1
        return val
```

```java
// java
// LC 251 - Flatten 2D Vector
// IDEA: same two cursors; hasNext() normalizes, next() is the only method that consumes
// time = O(1) amortized per call, space = O(1) extra
class Vector2D {
    private final int[][] vec;
    private int row = 0;   // which inner array
    private int col = 0;   // which index inside vec[row]

    public Vector2D(int[][] vec) {
        this.vec = vec;
    }

    public boolean hasNext() {
        // skip every used-up or empty row until (row, col) points at an element
        while (row < vec.length) {
            if (col < vec[row].length) {
                return true;
            }
            row++;
            col = 0;
        }
        return false;
    }

    public int next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        int val = vec[row][col];
        col++;
        return val;
    }
}
```

**Complexity**
- **Time O(1) amortized per call**: the `while` in `hasNext()` can skip several rows at once,
  but `row` only moves forward, so over a full pass it advances R times in total: O(R + N).
- **Space O(1) extra**: `self.vec = vec` keeps a reference, not a copy; the state is two ints.

**Dry run**

```text
vec = [[1, 2], [], [3]]          (row, col) after each call

next()     -> 1                  (0, 1)
next()     -> 2                  (0, 2)   row 0 is used up, but nothing moves yet
hasNext()  -> true               (2, 0)   skipped row 0 (used up) and row 1 (empty)
hasNext()  -> true               (2, 0)   idempotent: already on an element
next()     -> 3                  (2, 1)
hasNext()  -> false              (3, 0)   row == len(vec)
```

**Traps**
- **A `next()` that trusts the cursor.** `self.col += 1; return vec[row][col - 1]` works only
  if `hasNext()` ran first. On `[[], [3]]`, calling `next()` straight away reads the empty row 0
  and crashes. Normalizing inside `next()` costs nothing and removes that dependency.
- **A `hasNext()` that consumes.** It may move past rows that hold nothing. It must never step
  past a real element; only `next()` does that.
- **`if` where `while` is needed.** Two empty rows in a row (`[[1], [], [], [2]]`) need more
  than one skip.
- **Forgetting `col = 0`** when moving to the next row: on `[[1, 2], [3]]` the second row is
  read from index 2 and skipped.

**Not this if** — the nesting depth is not fixed (LC 341 Flatten Nested List Iterator): two
cursors cannot track an unbounded depth, so keep a stack of iterators instead
([iterator.md](./iterator.md#1-2-the-hasnext--next-contract-)).

**Drill** — LC 251 ⭐ · LC 284 *(peek: cache one element ahead)* · LC 281 *(zigzag: a queue of
cursors)* · LC 341 *(unbounded depth: a stack of cursors)*

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
        
        # compare column by column; a missing revision counts as 0
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

Find the element(s) that appear more than ⌊n/k⌋ times in O(1) extra space by pairing different
elements and cancelling them out. [streaming_algorithms.md](./streaming_algorithms.md) explains
why the same idea works on a stream.

##### Template 2: Majority Vote, One Candidate — LC 169

**Recognise** — "the element that appears **more than ⌊n/2⌋ times**", "you may assume the
majority element always exists", with a follow-up of "O(1) extra space". n ≤ 5·10⁴ → one O(n)
pass.

**Key point** — every decrement cancels one copy of `candidate` against one *different* value.
A value with more than n/2 copies cannot be cancelled out completely, so it is the candidate
standing at the end.

```python
# python
# LC 169 - Majority Element
# IDEA: Boyer-Moore vote - adopt a candidate when count is 0, +1 on a match, -1 otherwise
# time = O(n), space = O(1)
def majorityElement(nums):
    candidate = None
    count = 0
    for num in nums:
        if count == 0:
            candidate = num       # everything before here cancelled out: start fresh
            count = 1
        elif num == candidate:
            count += 1
        else:
            count -= 1            # pair num with one copy of candidate and drop both
    # LC 169 guarantees a majority exists; without that guarantee,
    # verify: return candidate if nums.count(candidate) > len(nums) // 2 else None
    return candidate
```

```java
// java
// LC 169 - Majority Element
// IDEA: Boyer-Moore vote - adopt a candidate when count is 0, +1 on a match, -1 otherwise
// time = O(n), space = O(1)
public int majorityElement(int[] nums) {
    int candidate = nums[0];
    int count = 0;
    for (int num : nums) {
        if (count == 0) {
            candidate = num;      // everything before here cancelled out: start fresh
            count = 1;
        } else if (num == candidate) {
            count++;
        } else {
            count--;              // pair num with one copy of candidate and drop both
        }
    }
    return candidate;             // guaranteed to exist in LC 169; verify otherwise
}
```

**Complexity**
- **Time O(n)**: the single `for num in nums` loop does O(1) work per element (plus one more
  O(n) pass if you verify).
- **Space O(1)**: the whole state is `candidate` and `count`.

**Dry run**

```text
nums = [2,2,1,1,1,2,2]
num | candidate | count | action
 2  |     2     |   1   | count was 0 -> adopt
 2  |     2     |   2   | match
 1  |     2     |   1   | cancel
 1  |     2     |   0   | cancel
 1  |     1     |   1   | count was 0 -> adopt
 2  |     1     |   0   | cancel
 2  |     2     |   1   | count was 0 -> adopt   -> answer 2 (4 of 7)
```

**Traps**
- **Skipping the verify pass when no majority is guaranteed.** `[1,2,3]` ends with candidate 3,
  which appears once. The vote only says *if* a majority exists, *this* is it.
- **The candidate is not the most frequent element.** `[1,1,2,2,3]` (no majority) ends on 3,
  not 1 or 2. Boyer-Moore answers "> n/2", never "mode".

**Not this if** — the threshold is "most frequent" or "top k" rather than "more than n/k": counts
are needed, so use a hash map and bucket or heap ([hash_map.md](./hash_map.md)).

**Drill** — LC 169 ⭐ · LC 1150 *(sorted: binary search instead)* · LC 229 *(> n/3: Template 3)* ·
LC 1157 *(majority in a range: random sampling or a segment tree of votes)*

##### Template 3: Majority Vote, k − 1 Candidates — LC 229

**Recognise** — "all elements that appear **more than ⌊n/3⌋ times**" (or ⌊n/k⌋), with "O(1)
space" as the follow-up. At most k − 1 values can clear an n/k bar, so the answer is a list of at
most k − 1.

**Key point** — a value different from all k − 1 candidates cancels **one copy of each**, k
values removed at once. A value with more than n/k copies takes part in at most its own count of
cancellations, so it is never wiped out; but survivors are only *candidates*, so a second pass
must count them.

```python
# python
# LC 229 - Majority Element II
# IDEA: Boyer-Moore with two candidates; a value matching neither cancels one of each;
#       then a second pass verifies both, because survivors are only candidates
# time = O(n), space = O(1)
def majorityElement(nums):
    cand1, cand2 = None, None
    count1, count2 = 0, 0
    for num in nums:
        if num == cand1:              # check both matches BEFORE adopting
            count1 += 1
        elif num == cand2:
            count2 += 1
        elif count1 == 0:
            cand1, count1 = num, 1
        elif count2 == 0:
            cand2, count2 = num, 1
        else:
            count1 -= 1               # num differs from both: cancel three values at once
            count2 -= 1
    result = []
    for cand in (cand1, cand2):       # verify: required here
        if cand is not None and nums.count(cand) > len(nums) // 3:
            result.append(cand)
    return result
```

```java
// java
// LC 229 - Majority Element II
// IDEA: Boyer-Moore with two candidates, then count both again to verify
// time = O(n), space = O(1)
public List<Integer> majorityElement(int[] nums) {
    int cand1 = 0, cand2 = 0;         // safe: a num matching cand1 is never also adopted as cand2
    int count1 = 0, count2 = 0;
    for (int num : nums) {
        if (num == cand1) {
            count1++;
        } else if (num == cand2) {
            count2++;
        } else if (count1 == 0) {
            cand1 = num;
            count1 = 1;
        } else if (count2 == 0) {
            cand2 = num;
            count2 = 1;
        } else {
            count1--;
            count2--;
        }
    }
    count1 = 0;
    count2 = 0;
    for (int num : nums) {            // second pass: survivors are only candidates
        if (num == cand1) {
            count1++;
        } else if (num == cand2) {
            count2++;
        }
    }
    List<Integer> result = new ArrayList<>();
    if (count1 > nums.length / 3) {
        result.add(cand1);
    }
    if (count2 > nums.length / 3) {
        result.add(cand2);
    }
    return result;
}
```

The same loop generalises to any k with a map of at most k − 1 candidates:

```python
# python
# IDEA: generalised Boyer-Moore - at most k-1 candidates; a newcomer that finds the map
#       full decrements every candidate (and drops the ones that reach 0)
# time = O(n * k), space = O(k)
def frequent_over_n_over_k(nums, k):
    candidates = {}                   # value -> surviving count, at most k - 1 keys
    for num in nums:
        if num in candidates:
            candidates[num] += 1
        elif len(candidates) < k - 1:
            candidates[num] = 1
        else:
            for key in list(candidates):
                candidates[key] -= 1
                if candidates[key] == 0:
                    del candidates[key]
    result = []
    for cand in candidates:           # verify each survivor
        if nums.count(cand) > len(nums) // k:
            result.append(cand)
    return result
```

**Complexity**
- **Time O(n)**: two linear passes, the voting `for` and the verify `nums.count` (O(n · k) for
  the general version, because a full map is decremented key by key).
- **Space O(1)**: two candidates and two counters (O(k) in general).

**Traps**
- **Skipping verification.** `[1,2,3]` leaves 1 and 2 as candidates; neither appears more than
  once. Unlike LC 169, LC 229 guarantees nothing, so the second pass is part of the answer.
- **Adopting before checking both matches.** Put `count1 == 0` before `num == cand2` and
  `[1,2,3,2]` ends with `cand1 == cand2 == 2`: the same value is tracked twice and reported as
  `[2, 2]`.

**Drill** — LC 229 ⭐ · LC 169 *(k = 2)* · LC 2404 *(most frequent even: counts, not votes)*

#### 1-2-2) Frequency Array + Running Count

When values are bounded (a permutation of `1..n`, lowercase letters), an array indexed by value
is the hash map, and a counter updated the moment a frequency crosses a threshold replaces any
re-scan.

##### Template 4: Frequency Array + Running Count — LC 2657

**Recognise** — "permutation of `1..n`", "values in `[1, n]`", together with "for every prefix
/ at every index, how many …". n ≤ 50 in LC 2657, but the shape is meant for n ≤ 10⁵ → O(n),
so recounting each prefix (O(n²)) is the thing to avoid.

**Key point** — `count[v]` is how many times `v` has been seen so far, and `common` changes only
on the step where some `count[v]` **reaches** the threshold (here 2: once in A, once in B). Each
value crosses it at most once, so `common` is exactly the number of values that have.

```python
# python
# LC 2657 - Find the Prefix Common Array of Two Arrays
# IDEA: frequency array indexed by value; bump `common` the moment a count reaches 2
# time = O(n), space = O(n)
def findThePrefixCommonArray(A, B):
    n = len(A)
    count = [0] * (n + 1)        # values are 1..n, so index n must exist
    common = 0
    res = [0] * n
    for i in range(n):
        count[A[i]] += 1         # increment first ...
        if count[A[i]] == 2:     # ... then test, so the crossing step is the one counted
            common += 1
        count[B[i]] += 1
        if count[B[i]] == 2:
            common += 1
        res[i] = common
    return res
```

```java
// java
// LC 2657 - Find the Prefix Common Array of Two Arrays
// IDEA: frequency array indexed by value; bump commonCount the moment a count reaches 2
// time = O(n), space = O(n)
public int[] findThePrefixCommonArray(int[] A, int[] B) {
    int n = A.length;
    int[] frequency = new int[n + 1];   // values are 1..n
    int commonCount = 0;
    int[] res = new int[n];
    for (int i = 0; i < n; i++) {
        frequency[A[i]]++;
        if (frequency[A[i]] == 2) {
            commonCount++;              // now seen in both arrays
        }
        frequency[B[i]]++;
        if (frequency[B[i]] == 2) {
            commonCount++;
        }
        res[i] = commonCount;
    }
    return res;
}
```

**Complexity**
- **Time O(n)**: one `for i in range(n)` with two O(1) array updates per step, no re-scan of the
  prefix.
- **Space O(n)**: `count = [0] * (n + 1)`, sized by the value range (O(max value) in general).

**Dry run**

```text
A = [1,3,2,4], B = [3,1,2,4]
i | A[i] B[i] | counts that reached 2 | common
0 |  1    3   | -                     | 0
1 |  3    1   | 3, 1                  | 2
2 |  2    2   | 2 (0 -> 1 -> 2 in one step) | 3
3 |  4    4   | 4                     | 4      -> [0, 2, 3, 4]
```

**Traps**
- **`[0] * n` for values `1..n`.** `A = [1]` indexes `count[1]` in a list of length 1:
  IndexError (or `ArrayIndexOutOfBoundsException`). Size it `n + 1`.
- **Testing before incrementing.** `if count[x] == 2: common += 1` and then `count[x] += 1`
  counts the step *after* the crossing, so `A = [1], B = [1]` returns `[0]` instead of `[1]`.
- **`>= 2` instead of `== 2`** when the arrays are not permutations: a value seen three times
  is counted twice. Test for the crossing, not the state.

**Not this if** — values are unbounded or sparse (`-10⁹ ≤ v ≤ 10⁹`): an array that size cannot be
allocated, so use a hash map ([hash_map.md](./hash_map.md)) with the same crossing test.

**Drill** — LC 2657 ⭐ · LC 217 *(threshold 2, return early)* · LC 448 *(count 0: the missing
values)* · LC 645 *(one value at 2, one at 0)* · LC 442 *(the same in place, with the sign trick)*

#### 1-2-3) Index Contribution Counting (Count Each Element's Appearances Across All Subarrays)

Instead of enumerating every subarray, ask how many subarrays contain one index, and sum that
contribution over the indices in one pass.

##### Template 5: Index Contribution Counting — LC 2063

**Recognise** — "sum of X **over all substrings / subarrays**", where each element adds to X on
its own (a vowel counts 1, a value adds itself). n ≤ 10⁵ → there are ~5·10⁹ subarrays, so they
cannot be listed; O(n) is expected.

**Key point** — a subarray containing index `i` is a choice of left end in `0..i` and right end
in `i..n-1`, so `i` lies in exactly `(i + 1) * (n - i)` subarrays. When an element's
contribution does not depend on which subarray it is in, the total is the sum of
`value(i) * (i + 1) * (n - i)`.

```text
# subarrays containing index i
   = (choices for LEFT boundary) × (choices for RIGHT boundary)
   = (i + 1) × (n - i)
     left  boundary ∈ {0, 1, ..., i}     → (i + 1) choices
     right boundary ∈ {i, i+1, ..., n-1} → (n - i) choices
```

```python
# python
# LC 2063 - Vowels of All Substrings
# IDEA: each vowel at index i appears in (i+1)*(n-i) substrings; sum those products
# time = O(n), space = O(1)
def countVowels(word):
    vowels = set("aeiou")
    n = len(word)
    total = 0
    for i in range(n):
        if word[i] in vowels:
            starting_choices = i + 1      # left boundary: 0..i
            ending_choices = n - i        # right boundary: i..n-1
            total += starting_choices * ending_choices
    return total
```

```java
// java
// LC 2063 - Vowels of All Substrings
// IDEA: each vowel at index i appears in (i+1)*(n-i) substrings; sum in a long
// time = O(n), space = O(1)
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
```

**Complexity**
- **Time O(n)**: one `for i in range(n)`; the product replaces an O(n²) enumeration of
  `(left, right)` pairs.
- **Space O(1)**: one running total.

**Dry run**

```text
word = "aba" (n = 3)
i | char | vowel? | (i+1) × (n-i)
0 |  a   |  yes   | 1 × 3 = 3
1 |  b   |  no    | -
2 |  a   |  yes   | 3 × 1 = 3        total = 6
check: "a"=1 "ab"=1 "aba"=2 "b"=0 "ba"=1 "a"=1 -> 6
```

**Traps**
- **Off by one on a boundary.** `i * (n - i)` drops the subarrays that start at `i`; for `"a"` it
  returns 0 instead of 1. The left end has `i + 1` choices, the right end `n - i`.
- **`int` overflow in Java.** At n = 10⁵ the middle index alone contributes ~2.5·10⁹, past
  `Integer.MAX_VALUE`. Accumulate in a `long` and widen *before* multiplying.
- **"Contains i" vs "starts / ends at i".** Subarrays starting at `i`: `n - i`; ending at `i`:
  `i + 1`; containing `i`: the product. Mixing them is the usual wrong answer.

**Not this if** — the contribution depends on the subarray, e.g. the element counts only when it
is the minimum (LC 907): its range is bounded by the nearest smaller elements, so the spans come
from a [monotonic stack](./monotonic_stack.md), not from `i + 1` and `n - i`.

**Drill** — LC 2063 ⭐ · LC 1588 *(odd-length subarrays: count the odd (left, right) pairs)* ·
LC 828 *(unique characters: spans to the previous and next same char)* · LC 907 *(minimum: monotonic
stack spans)* · LC 2104 *(max contribution minus min contribution)*

#### 1-2-4) Backward Write Pointer (In-Place Merge / In-Place Overwrite) ⭐⭐⭐⭐⭐

When the output is written into the same array that is still being read, and the free space is
at the tail, write from the back.

##### Template 6: Backward Write Pointer — LC 88

**Recognise** — "merge **in place**", "`nums1` has length `m + n`, the last `n` slots are 0 and
should be ignored", "do not return anything". m + n ≤ 200 in LC 88, but the follow-up asks for
O(m + n) time and O(1) extra space, which rules out copying `nums1` first.

**Key point** — `write - p1` equals the number of unmerged items left in `nums2`, which is never
negative, so `write >= p1` always holds: the slot being written was already read or is padding.
Writing the **larger** tail each step fills `nums1` from the largest value down.

```python
# python
# LC 88 - Merge Sorted Array
# IDEA: fill nums1 from the BACK, always writing the larger of the two tails
# time = O(m + n), space = O(1)
def merge(nums1, m, nums2, n):
    p1 = m - 1              # last real value in nums1
    p2 = n - 1              # last value in nums2
    write = m + n - 1       # last slot of nums1
    while p2 >= 0:          # once nums2 is used up, nums1[0..p1] is already in place
        if p1 >= 0 and nums1[p1] > nums2[p2]:
            nums1[write] = nums1[p1]
            p1 -= 1
        else:
            nums1[write] = nums2[p2]
            p2 -= 1
        write -= 1
```

```java
// java
// LC 88 - Merge Sorted Array
// IDEA: fill nums1 from the BACK, always writing the larger of the two tails
// time = O(m + n), space = O(1)
public void merge(int[] nums1, int m, int[] nums2, int n) {
    int p1 = m - 1;         // last real value in nums1
    int p2 = n - 1;         // last value in nums2
    int write = m + n - 1;  // last slot of nums1
    while (p2 >= 0) {       // once nums2 is used up, nums1[0..p1] is already in place
        if (p1 >= 0 && nums1[p1] > nums2[p2]) {
            nums1[write--] = nums1[p1--];
        } else {
            nums1[write--] = nums2[p2--];
        }
    }
}
```

**Complexity**
- **Time O(m + n)**: every iteration of `while p2 >= 0` moves `write` back by one, and `write`
  starts at `m + n - 1`.
- **Space O(1)**: three indices; the merge happens inside `nums1`.

**Dry run**

```text
nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
[1, 2, 3, _, _, _]   p2 -> 6   6 > 3     -> write 6
[1, 2, 3, _, _, 6]   p2 -> 5   5 > 3     -> write 5
[1, 2, 3, _, 5, 6]   p2 -> 2   3 > 2     -> write 3 (from nums1)
[1, 2, _, 3, 5, 6]   p2 -> 2   2 > 2? no -> write 2 (from nums2)
[1, 2, 2, 3, 5, 6]   p2 = -1   -> done, [1, 2] was already in place
```

**Traps**
- **Merging forwards.** Writing at `write = 0` overwrites `nums1[0]` before it is read:
  `[4,5,6,0,0,0]` with `[1,2,3]` loses 4 and 5.
- **No `p1 >= 0` guard.** Once `nums1`'s real values run out, `nums1[p1]` is `nums1[-1]`: Java
  throws, and Python reads the padding as data. `nums1 = [0]`, `m = 0`, `nums2 = [-1]` copies the
  padding 0 forward and then raises IndexError.
- **Looping on `p1` too, then copying the `nums1` tail.** Unneeded work: whatever is left of
  `nums1` is already in its final slots, so loop on `p2` only.

**Not this if** — the output is never longer than what has been read and the free space is at the
front (remove duplicates, remove element): a **forward** slow/fast pointer is safe there
([2_pointers.md](./2_pointers.md)).

**Drill** — LC 88 ⭐ · LC 977 *(squares: fill the result from the back, largest first)* ·
LC 1089 *(duplicate zeros: count first, then write backwards)* · LC 443
*(string compression: forward write, read ahead)*

#### 1-2-5) Array + Hash Index Map (O(1) Delete via Swap-With-Last) ⭐⭐⭐⭐⭐

Deleting from the middle of an array is O(n) only because of the shifting. If order does not
matter, plug the hole with the last element and pop the tail.

##### Template 7: Swap-With-Last Delete — LC 380

**Recognise** — "implement `insert`, `remove` and `getRandom`, each in **average O(1)** time",
"each element must have the same probability of being returned". Up to 2·10⁵ calls → every
operation O(1).

**Key point** — `vals` is always dense (no holes), so `vals[random index]` is a uniform pick, and
`idx[v]` is always the position of `v` in `vals`. A delete keeps both true by moving the last
value into the hole, re-pointing its index, and popping the tail, which is the only O(1) delete a
list has.

```text
remove(val):
1. i = idx[val]                  (hash map: value -> index)
2. vals[i] = vals[-1]            (plug the hole with the last value)
3. idx[vals[-1]] = i             (the moved value's index changed)
4. vals.pop(); del idx[val]      (O(1) tail removal)
```

```python
# python
# LC 380 - Insert Delete GetRandom O(1)
# IDEA: list for O(1) random access + dict {value: index};
#       delete by swapping the victim with the LAST element, then pop
# time = O(1) per insert / remove / getRandom, space = O(n)
import random

class RandomizedSet(object):

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
        self.vals[i] = last          # plug the hole with the last element
        self.idx[last] = i
        self.vals.pop()
        del self.idx[val]            # after the re-index, so val == last still works
        return True

    def getRandom(self):
        return random.choice(self.vals)
```

```java
// java
// LC 380 - Insert Delete GetRandom O(1)
// IDEA: ArrayList for O(1) random access + HashMap<value, index>;
//       delete by swapping the victim with the LAST element, then pop
// time = O(1) per insert / remove / getRandom, space = O(n)
class RandomizedSet {
    private final List<Integer> vals = new ArrayList<>();
    private final Map<Integer, Integer> idx = new HashMap<>(); // value -> index in vals
    private final Random rand = new Random();

    public boolean insert(int val) {
        if (idx.containsKey(val)) {
            return false;
        }
        idx.put(val, vals.size());   // the new value goes to the tail
        vals.add(val);
        return true;
    }

    public boolean remove(int val) {
        Integer i = idx.get(val);
        if (i == null) {
            return false;
        }
        int lastIdx = vals.size() - 1;
        int lastVal = vals.get(lastIdx);
        vals.set(i, lastVal);        // plug the hole with the last element
        idx.put(lastVal, i);
        vals.remove(lastIdx);        // remove by INDEX (int), not by object
        idx.remove(val);             // after the re-index, so val == lastVal still works
        return true;
    }

    public int getRandom() {
        return vals.get(rand.nextInt(vals.size()));
    }
}
```

**Complexity**
- **Time O(1) average per call**: each method does a constant number of hash-map operations and
  touches only the tail of `vals` (`append`, `pop`); nothing shifts.
- **Space O(n)**: `vals` and `idx` each hold one entry per stored value.

**Traps**
- **Deleting the map entry before re-pointing.** When the victim *is* the last element
  (`insert(5); remove(5)`), `del idx[val]` first and then `idx[last] = i` puts 5 back into the
  map, so a later `insert(5)` returns False.
- **Removing from the middle.** `vals.remove(val)` / `vals.remove(i)` shifts the rest: O(n) per
  delete, the exact cost the swap avoids.
- **Java's `remove` overload.** On a `List<Integer>`, `vals.remove(lastVal)` with an `int`
  removes by **index**; pass the index deliberately, or `Integer.valueOf(v)` to remove by value.
- **Tombstones instead of a swap.** Marking slots dead leaves holes, so `getRandom` either is
  not uniform or has to retry without bound.

**Not this if** — duplicates are allowed (LC 381): one value has several positions, so `idx` maps
a value to a **set** of indices and the swap updates one member of the moved value's set.

**Drill** — LC 380 ⭐ · LC 381 *(duplicates: value -> set of indices)* · LC 710 *(random pick with
a blacklist: remap the blacklisted low indices to the tail)*

#### 1-2-6) Bounded Candidate Enumeration (Only a Few Answers Are Possible)

Some "make everything equal" problems look like a search over every value, but the constraints
pin the answer to one or two candidates. Enumerate those and verify each in O(n).

##### Template 8: Bounded Candidate Enumeration — LC 1007

**Recognise** — "make **every** element of a row / all elements equal to the same value, minimum
operations, or −1", where each element offers only a couple of choices. n ≤ 2·10⁴ → O(n) per
candidate is fine, as long as the candidates are few.

**Key point** — if a value `x` fills the whole row, domino 0 must already show `x` on one face,
so `x ∈ {tops[0], bottoms[0]}`. Two candidates, each verified in one pass, is the whole search.

```python
# python
# LC 1007 - Minimum Domino Rotations For Equal Row
# IDEA: the answer must be tops[0] or bottoms[0] -> verify both candidates
# time = O(n), space = O(1)
def minDominoRotations(tops, bottoms):
    n = len(tops)

    def rotations_to_show(x):
        # flips needed so one whole row shows x; -1 if some domino lacks x entirely
        rot_top = 0      # flips to put x on top
        rot_bottom = 0   # flips to put x on bottom
        for i in range(n):
            if tops[i] != x and bottoms[i] != x:
                return -1
            if tops[i] != x:
                rot_top += 1
            elif bottoms[i] != x:      # elif: x on both faces needs no flip at all
                rot_bottom += 1
        return min(rot_top, rot_bottom)

    res = rotations_to_show(tops[0])
    if res != -1 or tops[0] == bottoms[0]:   # one candidate only when the faces match
        return res
    return rotations_to_show(bottoms[0])
```

```java
// java
// LC 1007 - Minimum Domino Rotations For Equal Row
// IDEA: the answer must be tops[0] or bottoms[0] -> verify both candidates
// time = O(n), space = O(1)
public int minDominoRotations(int[] tops, int[] bottoms) {
    int res = rotationsToShow(tops[0], tops, bottoms);
    if (res != -1 || tops[0] == bottoms[0]) {
        return res;
    }
    return rotationsToShow(bottoms[0], tops, bottoms);
}

// flips needed so one whole row shows x; -1 if some domino lacks x entirely
private int rotationsToShow(int x, int[] tops, int[] bottoms) {
    int rotTop = 0;
    int rotBottom = 0;
    for (int i = 0; i < tops.length; i++) {
        if (tops[i] != x && bottoms[i] != x) {
            return -1;
        }
        if (tops[i] != x) {
            rotTop++;
        } else if (bottoms[i] != x) {
            rotBottom++;
        }
    }
    return Math.min(rotTop, rotBottom);
}
```

**Complexity**
- **Time O(n)**: at most two calls to `rotations_to_show`, each one `for i in range(n)` pass.
- **Space O(1)**: two counters per candidate.

**Traps**
- **`else` where `elif` is needed.** A domino showing `x` on both faces needs no flip. With
  `else: rot_bottom += 1` it is counted as a bottom flip, so `tops = [2,1]`, `bottoms = [2,2]`
  returns 1 instead of 0.
- **Returning after the first candidate fails** without trying `bottoms[0]`:
  `tops = [1,2,3]`, `bottoms = [2,2,2]` fails for 1 and needs 0 flips for 2.
- **Looping over every value `1..6`.** It passes LC 1007, but the argument that matters ("the
  answer must be on domino 0") is the transferable one, and it is what survives a value range of
  10⁹.

**Drill** — LC 1007 ⭐ · LC 169 *(the vote leaves one candidate; verify it)* · LC 2128 *(row 0
pins every other row: each must equal it or its complement)*

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

The worked problems live in **[array_examples.md](./array_examples.md)**:

| Group | Problems |
|---|---|
| [In-place rewriting & index tricks](./array_examples.md#in-place-rewriting--index-tricks) | LC 41, 287, 189, 238, 670 |
| [Scanning & running state](./array_examples.md#scanning--running-state) | LC 121, 1567, 334, 849 |
| [Counting, bookings & simulation](./array_examples.md#counting-bookings--simulation) | LC 1109, 1375, 1041, 406, 251 |

## 4) Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** | LC 88 · LC 169 · LC 238 ⭐ · LC 380 | backward write; the vote; prefix × suffix products ([array_examples](./array_examples.md#3-product-of-array-except-self--lc-238-)); swap-with-last delete |
| **Practice** | LC 189 · LC 229 · LC 251 · LC 1007 · LC 2063 · LC 2657 | rotate by three reversals; k − 1 candidates; lazy iterator; candidates from index 0; contribution counting; frequency array |
| **Stretch** | LC 41 · LC 381 · LC 828 | cyclic sort / index as hash; swap-with-last with duplicates; contribution with per-character spans |

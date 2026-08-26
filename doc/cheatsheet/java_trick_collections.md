# Java Collections & Arrays Cheatsheet

> **Scope** — The Java library APIs for holding data: arrays and 2D arrays, lists, maps, queues, heaps, stacks and pairs — how to initialise, copy, iterate and index each one, and the traps in doing so.
> **See also**: [java_trick.md](./java_trick.md) — the language semantics behind these APIs, including why copying an array of objects still shares the objects; [java_trick_strings_sorting.md](./java_trick_strings_sorting.md) — String work and every comparator; [complexity_cheatsheet.md](./complexity_cheatsheet.md) — what each operation costs; [heap.md](./heap.md), [hash_map.md](./hash_map.md), [queue.md](./queue.md), [stack.md](./stack.md) — the structures themselves rather than their Java APIs.

## LeetCode Problem Lists

- [Array](https://leetcode.com/problem-list/array/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## Overview

Split out of [java_trick.md](./java_trick.md), which had grown to 3,418 lines with these APIs
scattered across five different numbering schemes. Everything here is *library surface*: the
language rules that explain the surprising behaviour stay in the parent sheet.

### Key Properties
- **Complexity**: see [complexity_cheatsheet.md](./complexity_cheatsheet.md); the notes below flag it only where the obvious call is the slow one
- **Core Idea**: the array/collection divide runs through all of it — fixed size and covariant vs growable and generic — and most conversion pain comes from crossing it
- **When to Use**: reach for this when you know *what* you want to store and need the exact call


## Arrays

### Arrays vs collections — the key differences

**Critical Distinction**:

| Method | Mutability | Affects Original Array | Best Use Case |
|--------|------------|----------------------|---------------|
| `Arrays.asList()` | **Fixed-size** (no add/remove) | ✅ **Yes** | Quick conversion for read-only operations |
| `new ArrayList()` | **Fully mutable** | ❌ **No** | When you need to modify the collection |

```java
// Arrays.asList - Fixed size, backed by original array
Integer[] arr = {1, 2, 3};
List<Integer> list1 = Arrays.asList(arr);
list1.set(0, 99);        // ✅ Works - modifies original array
// list1.add(4);         // ❌ Throws UnsupportedOperationException

// new ArrayList - Fully mutable, independent copy
List<Integer> list2 = new ArrayList<>(Arrays.asList(arr));
list2.add(4);            // ✅ Works - doesn't affect original array
```

**Recommendation**: Use `new ArrayList<>(Arrays.asList(arr))` when you need full mutability.


### Array initialization patterns


```java
// 1D Array Initialization
int[] arr1 = new int[5];                    // [0, 0, 0, 0, 0]
int[] arr2 = {1, 2, 3, 4, 5};              // Direct initialization
int[] arr3 = new int[]{1, 2, 3, 4, 5};     // Explicit initialization

// 2D Array Initialization  
int[][] matrix = new int[3][4];             // 3 rows, 4 columns (all zeros)
int[][] matrix2 = {{1, 2}, {3, 4}, {5, 6}}; // Direct 2D initialization

// Dynamic 2D array (common in LeetCode)
int k = 4;
int[][] result = new int[k][2];             // k rows, 2 columns each
result[0] = new int[]{0, 1};                // Assign first row
result[1] = new int[]{2, 3};                // Assign second row

// Printing arrays
System.out.println(Arrays.toString(arr2));      // 1D: [1, 2, 3, 4, 5]
System.out.println(Arrays.deepToString(result)); // 2D: [[0, 1], [2, 3], [0, 0], [0, 0]]
```

### Array / collection copying ⭐


> **Core Rule**: `arr2 = arr` copies the **reference**, not the data. Both variables point to the same array — mutating one mutates the other.

#### 1-D Array

```java
int[] arr = {3, 1, 4, 1, 5};

// ❌ Reference copy — arr2 and arr share the same memory
int[] arr2 = arr;
Arrays.sort(arr2);        // also sorts arr!
System.out.println(Arrays.toString(arr));   // [1, 1, 3, 4, 5]  ← original changed

// ✅ Shallow copy — independent array with same values
int[] arr3 = arr.clone();
Arrays.sort(arr3);        // arr is NOT affected
System.out.println(Arrays.toString(arr));   // [3, 1, 4, 1, 5]  ← original intact

// ✅ Alternative: Arrays.copyOf (explicit size)
int[] arr4 = Arrays.copyOf(arr, arr.length);

// ✅ Alternative: Arrays.copyOfRange (sub-range)
int[] arr5 = Arrays.copyOfRange(arr, 1, 4);  // [1, 4, 1]
```

#### 2-D Array (shallow vs deep)

```java
int[][] matrix = {{1, 2}, {3, 4}};

// ❌ Reference copy
int[][] m2 = matrix;

// ⚠ Shallow clone — outer array is new, but inner arrays are still shared
int[][] m3 = matrix.clone();
m3[0][0] = 99;            // also changes matrix[0][0]!

// ✅ Deep copy — fully independent
int[][] m4 = new int[matrix.length][];
for (int i = 0; i < matrix.length; i++) {
    m4[i] = matrix[i].clone();
}
```

#### List

```java
List<Integer> original = new ArrayList<>(Arrays.asList(1, 2, 3));

// ❌ Reference copy
List<Integer> ref = original;
ref.add(4);               // also adds to original!

// ✅ Shallow copy — independent list, same Integer objects
List<Integer> copy = new ArrayList<>(original);
copy.add(99);             // original is NOT affected
```

#### Quick Reference

| Type | Reference (wrong) | Shallow copy (correct) | Deep copy |
|------|-------------------|------------------------|-----------|
| `int[]` | `arr2 = arr` | `arr.clone()` / `Arrays.copyOf(arr, n)` | N/A (primitives) |
| `int[][]` | `m2 = matrix` | `matrix.clone()` ⚠ (inner shared) | loop + `row.clone()` |
| `List<T>` | `list2 = list` | `new ArrayList<>(list)` | deep-copy each element |
| `String[]` | `s2 = s` | `s.clone()` | N/A (Strings are immutable) |

**When this matters most**: sorting a copy before comparing with the original (e.g. LC 769, LC 75, LC 242), or BFS/DFS where you need a snapshot of the current state.


### Array ↔ List conversions


```java
// Array → List Conversion
Integer[] arr = {1, 2, 3, 4, 5};

// Method 1: Fixed-size list (backed by array)
List<Integer> list1 = Arrays.asList(arr);

// Method 2: Mutable list (recommended)
List<Integer> list2 = new ArrayList<>(Arrays.asList(arr));

// Method 3: Using streams (Java 8+)
List<Integer> list3 = Arrays.stream(arr).collect(Collectors.toList());

// List → Array Conversion
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

// Method 1: Traditional approach
Integer[] arr1 = list.toArray(new Integer[list.size()]);

// Method 2: Simplified (Java 8+)
Integer[] arr2 = list.toArray(Integer[]::new);

// Method 3: For primitive arrays
int[] primitiveArr = list.stream().mapToInt(Integer::intValue).toArray();
```

**Performance Note**: `toArray(new T[size])` is generally faster than `toArray()` because it avoids internal resizing.


### Assigning values into an `int[]`

```java
// java
// LC 347
// NOTE !! we define size when init int[]
int[] top = new int[k];

for(int i = k - 1; i >= 0; --i) {
    // assign val to int[] via below
    top[i] = heap.poll();
}
```

### `Arrays.fill` on a 1D array


```java
// java
// LC 300

/** NOTE !!! ONLY work for 1 D (since array is 1 dimension) */
int[] dp = new int[10];

// fill op
Arrays.fill(dp,1);
```

### `Arrays.copyOfRange` — taking a sub-array


```java
// java
// LC 976
// https://leetcode.com/problems/largest-perimeter-triangle/description/
nums = [1,2,1,10, 11, 22, 33]
int i = 2;
int[] tmp = Arrays.copyOfRange(nums, i, i+3);
```

### `Arrays.toString` — printing an array


```java
// java
// LC 997

/** 
 *  NOTE !!!
 * 
 *   via `Arrays.toString()`,
 *   we can print arrays value
 * 
 *  -> how to remember ?
 * 
 *  -> int[]  is `array`
 *  -> and `Arrays` is the array Util in java
 *  -> so it has toString method()
 * 
 * 
 */

// ...

int[] toTrust = new int[n + 1];
int[] trusted = new int[n + 1];

System.out.println(">>> toTrust = " + Arrays.toString(toTrust));
System.out.println(">>> trusted = " + Arrays.toString(trusted));

// ...
```

### Max value in an array

```java
// java
// LC 875
// https://stackoverflow.com/questions/1484347/finding-the-max-min-value-in-an-array-of-primitives-using-java
int[] piles = new int[5];
int r = Arrays.stream(piles).max().getAsInt();
```


## 2D Arrays & Matrices

### Initialising a 2D array


```java
// java
// LC 417
public int[][] DIRECTIONS = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
```

### Initialising an `M x N` boolean matrix

```java
// java
// LC 695
// LC 200
public static void main(String[] args) {


/** 
 *  NOTE !!!
 *   
 *  if `boolean[][]`, the default val is `false`
 *  if `Boolean[][]`, the default val is `null`
 */

// ex1
Boolean[][] x = new Boolean[3][4];
System.out.println(x);
System.out.println(x[0][0]); // null

// ex2
boolean[][] y = new boolean[3][4];
System.out.println(y);
System.out.println(y[0][0]); // false

// ex3
boolean[][] seen;
seen = new boolean[3][4];

int x = 3;
int y = 4;
boolean visit = boolean[y][x];
}
```

### Accessing an `M x N` boolean matrix


```java
// java
// LC 130

// ...

int l = board.length;
int w = board[0].length;

for(int i = 0; i < l; i++){
    for(int j = 0; j < w; j++){
        // NOTE !!! below
        /**
         *  NOTE !!!
         *
         *  board[y][x],
         *
         *  so the first is Y-coordination
         *  and the second is X-coordination
         *
         */
        if(board[i][j] == k){
            // do sth
        }
    }
}

// ...
```

## Lists

### Initialising a list

```java
// java


// LC 102
public static void main(String[] args) {

   List<Integer> tmpArray = new ArrayList<>();
   System.out.println(tmpArray);
   tmpArray.add(1);
   tmpArray.add(2);
   System.out.println(tmpArray);

   System.out.println("--->");

   List<List<Integer>> res = new ArrayList<>();
   System.out.println(res);
   res.add(tmpArray);
   System.out.println(res);
}

// init a list with 2D content
// LC 406

// example 1
List<List<Integer>> commonCells = new ArrayList<>();

// example 2
List<int[]> result = new ArrayList<>(); //return value
```

### Replacing a list value at an index


```java
// java

List<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(3);

System.out.println("list = " + list); // list = [1, 2, 3]

list.set(0, 0);
System.out.println("(after op) list = " + list); // list = [0, 2, 3]

// LC 24
```


#### 1-0-0-2) `Reverse` loop over a list

```java
// java

List<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(3);


/** 
 *  NOTE !!!
 * 
 *  for reverse loop,
 * 
 *  we start from size - 1,
 *     end at >= 0
 * 
 */
for(int i = list.size() - 1; i >= 0; i--){
    System.out.println(list.get(i));
}

```


### Inserting at an index

```java
// java
// LC 102
public List<List<Integer>> levelOrder(TreeNode root) {

    List<List<Integer>> levels = new ArrayList<List<Integer>>();
    // ...
    while ( !queue.isEmpty() ) {
        // ...
        for(int i = 0; i < level_length; ++i) {
            // fulfill the current level
            // NOTE !!! this trick
            levels.get(level).add(node.val);
            // ...
        }
        // ...
    }
    // ..
}
```

```java
// java
// ...


/**
*  NOTE !!!
*
*   via below, we can retrieve List val by idx,
*   append new val to the existing array (with same idx)
*
*
*   code breakdown:
*
*    •   res is a List<List<Integer>>, where each inner list represents a level of the tree.
*    •   res.get(depth) retrieves the list at the given depth.
*    •   .add(curRoot.val) adds the current node’s value to the corresponding depth level.
*
*/

List<List<Integer>> res = new ArrayList<>();


// insert curRoot.val to current val in list at `depth` index
// NOTE !!! below
res.get(depth).add(curRoot.val);


// ...
```

### Appending to a 2D list

```java
// java
// LC 417

List<List<Integer>> commonCells = new ArrayList<>();
for (int i = 0; i < numRows; i++) {
    for (int j = 0; j < numCols; j++) {

        if (pacificReachable[i][j] && atlanticReachable[i][j]) {

            // NOTE code here
             commonCells.add(Arrays.asList(i, j));
        }
    }
}
```


### Reversing a list

```java
// java
// LC 107

// ...
List<List<Integer>> levels = new ArrayList<List<Integer>>();
Collections.reverse(levels);

// NOTE : reverse != decreasing order
// ...


List<Integer> list2 = new ArrayList<>();
list2.add(1);
list2.add(2);
list2.add(3);

System.out.println("list2 = " + list2); // list2 = [1, 2, 3]

/** Reverse List 
*
*   // NOTE !!! reverse in place, e.g. NO return val
*/
Collections.reverse(list2);
System.out.println("list2 = " + list2); // list2 = [3, 2, 1]
```

## Maps

### HashMap advanced operations


#### Nested HashMap Pattern
```java
// Nested HashMap for complex relationships (LC 399 - Graph representation)
HashMap<String, HashMap<String, Double>> graph = new HashMap<>();

// Efficient way to initialize nested structure
for (int i = 0; i < equations.size(); i++) {
    String from = equations.get(i).get(0);
    String to = equations.get(i).get(1);
    double value = values[i];
    
    // putIfAbsent prevents overwriting existing nested maps
    graph.putIfAbsent(from, new HashMap<>());
    graph.putIfAbsent(to, new HashMap<>());
    
    graph.get(from).put(to, value);
    graph.get(to).put(from, 1.0 / value);  // Bidirectional relationship
}
```

#### Essential HashMap Methods
```java
Map<String, Integer> map = new HashMap<>();

// Safe operations
map.putIfAbsent(key, defaultValue);           // Only put if key doesn't exist
int count = map.getOrDefault(key, 0) + 1;     // Get with fallback
map.put(key, count);                          // Update count

// Atomic operations (Java 8+)
map.merge(key, 1, Integer::sum);              // Increment counter atomically
map.compute(key, (k, v) -> v == null ? 1 : v + 1); // Custom computation
```

#### Elegant Map Value Update with `putIfAbsent`
```java
// Elegant: use putIfAbsent + get (e.g. LC 399 - graph with adjacency list)
Map<String, List<Node>> graph = new HashMap<>();
for (int i = 0; i < equations.size(); i++) {
    String u = equations.get(i).get(0);
    String v = equations.get(i).get(1);
    double val = values[i];

    graph.putIfAbsent(u, new ArrayList<>());
    graph.putIfAbsent(v, new ArrayList<>());
    graph.get(u).add(new Node(v, val));
    graph.get(v).add(new Node(u, 1.0 / val));
}

// Verbose (avoid): manual containsKey check + put back
if (!map.containsKey(ai)) {
    map.put(ai, new ArrayList<>());
}
List<MyInfo> tmpList = map.get(ai);
tmpList.add(new MyInfo(ai, bi, val));
map.put(ai, tmpList); // unnecessary - list is already in the map
```

### Returning a default value — `getOrDefault`

```java

// LC 424
// NOTE : map.getOrDefault(key,0) syntax :  if can find key, return its value, else, return default 0
map.put(key, map.getOrDefault(key,0)+1);


// e.g.
map.getOrDefault(key,0)
```

### Looping over a map

```java
// java

// LC 742
/** NOTE
 *  
 *  Map.Entry<TreeNode, List<TreeNode>> entry : g.entrySet()
 * 
 */
Map<TreeNode, List<TreeNode>> g;
for (Map.Entry<TreeNode, List<TreeNode>> entry : g.entrySet()) {
        if (entry.getKey() != null && entry.getKey().val == k) {
            q.offer(entry.getKey());
            break;
        }
    }
// ...
```

### Reading key and value together


```java
// java
// LC 501

List<Integer> modes = new ArrayList<>();
/**
 *  NOTE !!! we use `Entry`
 *           to access both map's key and value
 */
for (Map.Entry<Integer, Integer> entry : node_cnt.entrySet()) {
    if (entry.getValue() == maxFreq) {
        modes.add(entry.getKey());
    }
}
        
```


### Tracking element counts in insertion order


```java
// java
// LC 767

// ...

// Step 1: Count the frequency of each character
Map<Character, Integer> charCountMap = new HashMap<>();
for (char c : S.toCharArray()) {
    charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
}

// Step 2: Use a priority queue (max heap) to keep characters sorted by
// frequency
/** NOTE !!!
 *
 *  we use PQ to track the characters count sorted in order
 */
PriorityQueue<Map.Entry<Character, Integer>> maxHeap = new PriorityQueue<>(
        (a, b) -> b.getValue() - a.getValue());
maxHeap.addAll(charCountMap.entrySet());

// ...
```

### `TreeMap` basics

- java.util.TreeMap.floorKey()
- will return max val in its key set, if empty, return null
- 還有一種Map，它在內部對Key進行排序，Map就是SortedMap。
- SortedMap保證遍歷時以Key的順序來進行排序。預設按字母排序：
- 使用TreeMap時，輸入的Key必須實作Comparable介面。
- https://www.yxjc123.com/post/v0i7dl
- https://liaoxuefeng.com/books/java/collection/tree-map/index.html


### Ordering a HashMap by key with `TreeMap`

```java
// java
// LC 853
// V1
HashMap<Integer, Integer> map = new HashMap<>();

for (int i = 0; i < position.length; i++){
    int p = -1 * position[i]; // for inverse sorting
    int s = speed[i];
    map.put(p, s);
}

// order by map key
Map<Integer, Integer> tree_map = new TreeMap(map);
```

```java
// java
// LC 853
// order Map key instead
HashMap<Integer, Integer> map = new HashMap<>();
Arrays.sort(map.keySet().toArray());
```

```java
// java
// LC 346
// sort array in descending order
Arrays.sort(tmp, (x, y) -> Integer.compare(-x[1], -y[1]));
```

- `floorEntry` method in `TreeMap`
- https://blog.csdn.net/a1510841693/article/details/124323418
- floorEntry() : It returns a key-value mapping associated with the greatest key less than or equal to the given key, or null if there is no such key.

```java
// floorEntry
// LC 1146

// ...
 TreeMap<Integer, Integer>[] historyRecords;
// ...
public int get(int index, int snapId) {
    return historyRecords[index].floorEntry(snapId).getValue();
}
// ...
```

### `TreeMap` key ordering — ascending vs descending ⭐


> **Core Rule**: `TreeMap` always keeps keys **sorted**. Default order is ascending (small → big). Pass `Comparator.reverseOrder()` to flip it to descending (big → small).

```java
// Default TreeMap — ascending key order (small → big)
TreeMap<Integer, Integer> asc = new TreeMap<>();
asc.put(3, 30); asc.put(1, 10); asc.put(2, 20);
for (int k : asc.keySet()) { /* visits 1, 2, 3 */ }

// Reverse-order TreeMap — descending key order (big → small)
TreeMap<Integer, Integer> desc = new TreeMap<>(Comparator.reverseOrder());
desc.put(3, 30); desc.put(1, 10); desc.put(2, 20);
for (int k : desc.keySet()) { /* visits 3, 2, 1 */ }
```

**Alternative — `descendingKeySet()` on a default TreeMap:**
```java
// No need to create a reverse-order TreeMap; iterate existing one in reverse
TreeMap<Integer, Integer> map = new TreeMap<>();
map.put(1, 10); map.put(3, 30); map.put(2, 20);

// Forward (small → big)
for (int k : map.keySet()) { /* 1, 2, 3 */ }

// Reverse (big → small) — descendingKeySet() returns a view, O(1)
for (int k : map.descendingKeySet()) { /* 3, 2, 1 */ }
```

**Applied pattern — LC 362 Design Hit Counter (5-minute sliding window):**
```java
// IDEA: TreeMap (reverse order) so we can break early on out-of-window timestamps
private TreeMap<Integer, Integer> map;

public HitCounter() {
    // Keys visited big → small; break as soon as key <= timestamp - 300
    map = new TreeMap<>(Comparator.reverseOrder());
}

public void hit(int timestamp) {
    map.put(timestamp, map.getOrDefault(timestamp, 0) + 1);
}

public int getHits(int timestamp) {
    int cnt = 0;
    for (int k : map.keySet()) {
        if (k <= timestamp - 300) break;   // earlier timestamps are all invalid
        cnt += map.get(k);
    }
    return cnt;
}
```

**Why descending order helps here:** iterating big → small lets us `break` the moment we hit a key outside the 5-minute window, instead of scanning the entire map.

**Summary:**

| Goal | How |
|------|-----|
| Ascending iteration (default) | `new TreeMap<>()` |
| Descending iteration (via constructor) | `new TreeMap<>(Comparator.reverseOrder())` |
| Descending iteration (on existing map) | `map.descendingKeySet()` |
| Nearest key ≤ target | `map.floorKey(target)` |
| Nearest key ≥ target | `map.ceilingKey(target)` |

**Similar LC problems using TreeMap ordering:**
| Problem | LC # | Key Usage |
|---------|------|-----------|
| Design Hit Counter | 362 | Reverse-order iteration + early break |
| Snapshot Array | 1146 | `floorEntry(snapId)` for last value before snap |
| Time Based Key-Value Store | 981 | `floorKey(timestamp)` |
| My Calendar I | 729 | `floorEntry` / `ceilingEntry` for overlap check |

---

### Arrays CANNOT be used as HashMap keys ⭐


> **Core Rule**: Never use `int[]` or `Integer[]` as a `HashMap` key — they use memory address for `.equals()` and `.hashCode()`, not element values.

```java
// LC 2013 - Detect Squares

/** NOTE !!!
 *
 *  CAN'T use `Integer[]{x, y}` as HashMap key
 *
 *  Array Keys in HashMap:
 *  In Java, int[] or Integer[] use the default `memory address`
 *  for .equals() and .hashCode().
 *  This means new Integer[]{1, 2} will NOT match a
 *  previously stored new Integer[]{1, 2}.
 */

// ❌ WRONG — array identity, not content
Map<Integer[], Integer> pointCount = new HashMap<>();
pointCount.put(new Integer[]{1, 2}, 1);
pointCount.get(new Integer[]{1, 2});  // returns null! different object
```

#### Correct Alternatives

**Option 1: String key (simplest)**
```java
// ✅ Use "x,y" string as key
Map<String, Integer> pointCount = new HashMap<>();
pointCount.put(x + "," + y, pointCount.getOrDefault(x + "," + y, 0) + 1);
pointCount.getOrDefault(x + "," + y, 0);  // works correctly
```

**Option 2: Nested Map**
```java
// ✅ Map<Integer, Map<Integer, Integer>> — no string encoding needed
Map<Integer, Map<Integer, Integer>> pointCount = new HashMap<>();
pointCount.putIfAbsent(x, new HashMap<>());
pointCount.get(x).put(y, pointCount.get(x).getOrDefault(y, 0) + 1);
```

**Option 3: Custom Point class with `equals()` + `hashCode()` overridden**
```java
// ✅ Custom class
class Point {
    int x, y;
    Point(int x, int y) { this.x = x; this.y = y; }

    @Override
    public boolean equals(Object o) {
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

Map<Point, Integer> pointCount = new HashMap<>();
pointCount.put(new Point(1, 2), 1);
pointCount.get(new Point(1, 2));  // ✅ returns 1
```

#### Summary Table

| Key Type | Works? | Why |
|----------|--------|-----|
| `int[]` / `Integer[]` | ❌ | Uses memory address for `equals()`/`hashCode()` |
| `String` (e.g. `"x,y"`) | ✅ | Value-based equality built in |
| `Map<Integer, Map<Integer, Integer>>` | ✅ | Nested map avoids the problem entirely |
| Custom class with `equals()` + `hashCode()` | ✅ | Explicit value-based identity |
| `List<Integer>` | ✅ | `ArrayList.equals()` is content-based |

**Note**: `List<Integer>` (e.g. `Arrays.asList(x, y)`) also works as a map key because `ArrayList` overrides `equals()` and `hashCode()` to compare elements — but it is slower than a `String` key.

## Queues, Heaps & Stacks

### PriorityQueue (heap) basics


**Key Concept**: Java's `PriorityQueue` is a **min-heap by default**.

#### Min-Heap Implementation
```java
// Method 1: Default min-heap (natural ordering)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Method 2: Min-heap with explicit comparator
PriorityQueue<Integer> minHeap2 = new PriorityQueue<>((o1, o2) -> o1 - o2);

// Method 3: Traditional comparator (verbose)
PriorityQueue<Integer> minHeap3 = new PriorityQueue<>(new Comparator<Integer>() {
    @Override
    public int compare(Integer o1, Integer o2) {
        return o1 - o2;  // Ascending order
    }
});
```

#### Max-Heap Implementation

```java
// Method 1: Using Collections.reverseOrder() - RECOMMENDED
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

// Method 2: Custom lambda comparator
PriorityQueue<Integer> maxHeap2 = new PriorityQueue<>((o1, o2) -> o2 - o1);

// Method 3: Traditional comparator
PriorityQueue<Integer> maxHeap3 = new PriorityQueue<>(new Comparator<Integer>() {
    @Override
    public int compare(Integer o1, Integer o2) {
        return o2 - o1;  // Descending order
    }
});
```

**Common Use Cases**: Top-K problems, finding median, scheduling tasks

### PriorityQueue examples


```java
// Min-heap (default) - smallest element first
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.addAll(Arrays.asList(5, 10, 1, 3));
while (!minHeap.isEmpty()) {
    System.out.print(minHeap.poll() + " ");  // Output: 1 3 5 10
}

// Max-heap - largest element first
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
maxHeap.addAll(Arrays.asList(5, 10, 1, 3));
while (!maxHeap.isEmpty()) {
    System.out.print(maxHeap.poll() + " ");  // Output: 10 5 3 1
}
```

### PriorityQueue with custom ordering

```java
// java
// LC 347
// ...

// Step 1. Count the frequency of each element
Map<Integer, Integer> countMap = new HashMap<>();
for (int num : nums) {
countMap.put(num, countMap.getOrDefault(num, 0) + 1);
}

/** NOTE !!! how to init PQ below */
// Step 2. Use a Min-Heap (Priority Queue) to keep track of top K elements
PriorityQueue<Map.Entry<Integer, Integer>> heap = new PriorityQueue<>(
    (a, b) -> a.getValue() - b.getValue()
);

// ...
```

### Initialising a queue


- https://stackoverflow.com/questions/4626812/how-do-i-instantiate-a-queue-object-in-java

- A Queue is an `interface`, which means you `cannot` construct a Queue directly.
- Consinder use one of below implementation:
```text
AbstractQueue, ArrayBlockingQueue, ArrayDeque, ConcurrentLinkedQueue, DelayQueue, LinkedBlockingQueue, LinkedList, PriorityBlockingQueue, PriorityQueue, or SynchronousQueue.
```


### `add()` vs `offer()`


| Method | Failure Behavior | Return Type | Best Use Case |
|--------|------------------|-------------|---------------|
| `add(e)` | **Throws exception** | `boolean` | When failure should stop execution |
| `offer(e)` | **Returns false** | `boolean` | When you want graceful failure handling |

```java
Queue<Integer> queue = new LinkedList<>();

// add() - throws exception on failure
try {
    queue.add(42);      // Returns true if successful
} catch (IllegalStateException e) {
    // Handle capacity exceeded
}

// offer() - returns false on failure (preferred for bounded queues)
if (queue.offer(42)) {
    System.out.println("Element added successfully");
} else {
    System.out.println("Queue is full");
}
```

**Recommendation**: Use `offer()` for bounded queues, `add()` for unlimited queues like `LinkedList`.


### Queue removal methods


```java
// java

// In Java, the remove() method is commonly used with various types of collections such as Queue, List, and Set. When used with a Queue, the remove() method is used to remove and return the front element of the queue.

/*

 boolean remove(Object o);



- Purpose:
    - Removes the first occurrence of the specified element from the queue. If the element exists in the queue, it is removed. If it doesn't exist, the queue remains unchanged.

- Return Type:
    - Returns true if the element was successfully removed.

    - Returns false if the element was not found in the queue (i.e., the queue remains unchanged).

- Throws:
    - It may throw a NullPointerException if you pass null as an argument and the queue does not permit null elements (this depends on the specific implementation of Queue).


*/


// Create a Queue (LinkedList implements Queue)
Queue<Integer> queue = new LinkedList<>();

// Add elements to the Queue
queue.add(10);
queue.add(20);
queue.add(30);
queue.add(7);  // Adding element 7
queue.add(40);

System.out.println("Original queue: " + queue);

// Remove element 7 from the queue
boolean removed = queue.remove(7);  // Removes the first occurrence of 7

// Print the result of removal
System.out.println("Was element 7 removed? " + removed);  // true

// Print the modified queue
System.out.println("Queue after removal of 7: " + queue);

// Try to remove element 7 again
removed = queue.remove(7);  // Element 7 no longer exists in the queue

// Print the result of trying to remove 7 again
System.out.println("Was element 7 removed again? " + removed);  // false

System.out.println("queue: " + queue); // queue: [10, 20, 30, 40]

queue.remove();
System.out.println("queue: " + queue); // queue: [20, 30, 40]
```


### Pushing an array onto a stack

```java
// java
// LC 739
Stack<int[]> stack = new Stack<>();

int[] init = new int[2];
init[0]  = temperatures[0];
init[1] = 0;
stack.push(init);
```

### Looping over a stack

```java
// java
// LC 71
Stack<String> st = new Stack<>();
st.push("a");
st.push("b");
st.push("c");

// NOTE !!! loop over elements in stack
for(String x: st){
    System.out.println(x);
}
```

## Pairs

### The `Pair` data structure


- Pair offers a (key, value) structure
- offer getKey, getValue method
- can be used in other data structure (e.g. queue, hashmap...)
- available in default Java lib, or apache.common lib or other lib

```java
// java
// LC 355
// https://leetcode.com/problems/design-twitter/solutions/2720611/java-simple-hashmap-stack/

// https://blog.csdn.net/neweastsun/article/details/80294811
// https://blog.51cto.com/u_5650011/5386895

/** 
* 
* 
* 
*/
// init
Pair<Integer, String> p1 = new Pair<>(1, "one");
// get key
Integer k1 = p1.getKey();
// get value
String v1 = p1.getValue();

// use with other data structure
Queue<Pair<Integer, String>> q = new LinkedList<>();
q.add(p1);
```

- Or, you can define your own pair data structure:

```java
// java
public class MyPair<U, V> {

public U first;
public V second;

MyPair(U first, V second){
    this.first = first;
    this.second = second;
}

// getter

// setter
}
```

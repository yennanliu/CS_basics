# Java 容器與陣列速查表

> **範圍** — 存放資料用的 Java 函式庫 API：陣列與二維陣列、list、map、佇列、堆積、堆疊與 pair — 每一種怎麼初始化、複製、走訪、索引，以及過程中的陷阱。
> **另見**：[java_trick.md](./java_trick.md) — 這些 API 背後的語言語意，包含為什麼複製一個物件陣列之後物件本身還是共用的；[java_trick_strings_sorting.md](./java_trick_strings_sorting.md) — String 相關操作與各種 comparator；[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 每個操作的代價；[heap.md](./heap.md)、[hash_map.md](./hash_map.md)、[queue.md](./queue.md)、[stack.md](./stack.md) — 結構本身，而不是它們的 Java API。

## LeetCode 題目清單

- [Array](https://leetcode.com/problem-list/array/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## 總覽

從 [java_trick.md](./java_trick.md) 拆出來 — 那份文件已經長到 3,418 行，這些 API 散落在五套不同的編號體系裡。這裡收的全是*函式庫表面*：解釋那些反直覺行為的語言規則留在母文件。

### Key Properties
- **複雜度**：見 [complexity_cheatsheet.md](./complexity_cheatsheet.md)；底下只在「最直覺的那個呼叫剛好最慢」時才特別標註
- **核心想法**：陣列與容器的分界貫穿全篇 — 固定大小且共變 vs 可成長且泛型 — 大部分轉換上的痛苦都來自跨過這條界線
- **什麼時候用**：當你已經知道*要存什麼*，只需要那個確切的呼叫寫法


## 陣列

### 陣列 vs 容器 — 關鍵差異

**關鍵區別**：

| 方法 | 可變性 | 影響原陣列 | 最佳使用時機 |
|--------|------------|----------------------|---------------|
| `Arrays.asList()` | **固定大小**（不能 add/remove） | ✅ **會** | 唯讀操作時的快速轉換 |
| `new ArrayList()` | **完全可變** | ❌ **不會** | 需要修改容器內容時 |

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

**建議**：需要完全可變時，用 `new ArrayList<>(Arrays.asList(arr))`。


### 陣列初始化的幾種寫法


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

### 陣列／容器的複製 ⭐


> **核心規則**：`arr2 = arr` 複製的是**參考**，不是資料。兩個變數指向同一個陣列 — 改一個等於改另一個。

#### 一維陣列

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

#### 二維陣列（淺複製 vs 深複製）

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

#### 快速參考

| 型別 | 參考（錯的） | 淺複製（正確） | 深複製 |
|------|-------------------|------------------------|-----------|
| `int[]` | `arr2 = arr` | `arr.clone()` / `Arrays.copyOf(arr, n)` | 不適用（基本型別） |
| `int[][]` | `m2 = matrix` | `matrix.clone()` ⚠（內層仍共用） | 迴圈 + `row.clone()` |
| `List<T>` | `list2 = list` | `new ArrayList<>(list)` | 逐個元素深複製 |
| `String[]` | `s2 = s` | `s.clone()` | 不適用（String 不可變） |

**最常踩到的場合**：先複製再排序，然後跟原陣列比對（例如 LC 769、LC 75、LC 242），或是 BFS/DFS 中需要當前狀態的快照時。


### 陣列 ↔ List 互轉


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

**效能備註**：`toArray(new T[size])` 通常比 `toArray()` 快，因為它省掉內部重新配置。


### 把值寫進 `int[]`

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

### 一維陣列上的 `Arrays.fill`


```java
// java
// LC 300

/** NOTE !!! ONLY work for 1 D (since array is 1 dimension) */
int[] dp = new int[10];

// fill op
Arrays.fill(dp,1);
```

### `Arrays.copyOfRange` — 取子陣列


```java
// java
// LC 976
// https://leetcode.com/problems/largest-perimeter-triangle/description/
nums = [1,2,1,10, 11, 22, 33]
int i = 2;
int[] tmp = Arrays.copyOfRange(nums, i, i+3);
```

### `Arrays.toString` — 印出陣列


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

### 陣列中的最大值

```java
// java
// LC 875
// https://stackoverflow.com/questions/1484347/finding-the-max-min-value-in-an-array-of-primitives-using-java
int[] piles = new int[5];
int r = Arrays.stream(piles).max().getAsInt();
```


## 二維陣列與矩陣

### 初始化二維陣列


```java
// java
// LC 417
public int[][] DIRECTIONS = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
```

### 初始化 `M x N` 的 boolean 矩陣

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

### 存取 `M x N` 的 boolean 矩陣


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

## List

### 初始化 list

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

### 替換 list 中某個索引的值


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


#### 1-0-0-2) 反向走訪 list

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


### 在指定索引插入

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

### 往二維 list 追加元素

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


### 反轉 list

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

## Map

### HashMap 進階操作


#### 巢狀 HashMap 模式
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

#### 必備的 HashMap 方法
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

#### 用 `putIfAbsent` 優雅地更新 map 的值
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

### 回傳預設值 — `getOrDefault`

```java

// LC 424
// NOTE : map.getOrDefault(key,0) syntax :  if can find key, return its value, else, return default 0
map.put(key, map.getOrDefault(key,0)+1);


// e.g.
map.getOrDefault(key,0)
```

### 走訪 map

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

### 同時取出 key 和 value


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


### 依插入順序追蹤元素計數


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

### `TreeMap` 基礎

- java.util.TreeMap.floorKey()
- 會回傳 key 集合中的最大值；若為空則回傳 null
- 還有一種 Map 會在內部對 key 進行排序，也就是 SortedMap。
- SortedMap 保證走訪時依 key 的順序進行。預設按字母排序。
- 使用 TreeMap 時，輸入的 key 必須實作 Comparable 介面。
- https://www.yxjc123.com/post/v0i7dl
- https://liaoxuefeng.com/books/java/collection/tree-map/index.html


### 用 `TreeMap` 把 HashMap 依 key 排序

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

- `TreeMap` 的 `floorEntry` 方法
- https://blog.csdn.net/a1510841693/article/details/124323418
- floorEntry()：回傳「小於或等於指定 key 的最大 key」所對應的 key-value entry；若沒有這種 key 就回傳 null。

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

### `TreeMap` 的 key 順序 — 遞增 vs 遞減 ⭐


> **核心規則**：`TreeMap` 永遠讓 key 保持**排序**。預設是遞增（小 → 大）。傳入 `Comparator.reverseOrder()` 就翻成遞減（大 → 小）。

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

**另一種做法 — 在預設 TreeMap 上用 `descendingKeySet()`：**
```java
// No need to create a reverse-order TreeMap; iterate existing one in reverse
TreeMap<Integer, Integer> map = new TreeMap<>();
map.put(1, 10); map.put(3, 30); map.put(2, 20);

// Forward (small → big)
for (int k : map.keySet()) { /* 1, 2, 3 */ }

// Reverse (big → small) — descendingKeySet() returns a view, O(1)
for (int k : map.descendingKeySet()) { /* 3, 2, 1 */ }
```

**應用模式 — LC 362 Design Hit Counter（5 分鐘滑動視窗）：**
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

**為什麼這裡遞減順序有幫助**：由大到小走訪，一碰到落在 5 分鐘視窗外的 key 就能直接 `break`，不必掃完整個 map。

**小結：**

| 目標 | 做法 |
|------|-----|
| 遞增走訪（預設） | `new TreeMap<>()` |
| 遞減走訪（用建構子） | `new TreeMap<>(Comparator.reverseOrder())` |
| 遞減走訪（在既有 map 上） | `map.descendingKeySet()` |
| 最接近且 ≤ target 的 key | `map.floorKey(target)` |
| 最接近且 ≥ target 的 key | `map.ceilingKey(target)` |

**用到 TreeMap 排序的類似 LC 題目：**
| 題目 | LC # | 關鍵用法 |
|---------|------|-----------|
| Design Hit Counter | 362 | 反向走訪 + 提早 break |
| Snapshot Array | 1146 | 用 `floorEntry(snapId)` 找快照前的最後一個值 |
| Time Based Key-Value Store | 981 | `floorKey(timestamp)` |
| My Calendar I | 729 | 用 `floorEntry` / `ceilingEntry` 檢查重疊 |

---

### 陣列不能當 HashMap 的 key ⭐


> **核心規則**：絕對不要拿 `int[]` 或 `Integer[]` 當 `HashMap` 的 key — 它們的 `.equals()` 和 `.hashCode()` 是用記憶體位址，不是元素的值。

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

#### 正確的替代方案

**選項 1：用 String 當 key（最簡單）**
```java
// ✅ Use "x,y" string as key
Map<String, Integer> pointCount = new HashMap<>();
pointCount.put(x + "," + y, pointCount.getOrDefault(x + "," + y, 0) + 1);
pointCount.getOrDefault(x + "," + y, 0);  // works correctly
```

**選項 2：巢狀 Map**
```java
// ✅ Map<Integer, Map<Integer, Integer>> — no string encoding needed
Map<Integer, Map<Integer, Integer>> pointCount = new HashMap<>();
pointCount.putIfAbsent(x, new HashMap<>());
pointCount.get(x).put(y, pointCount.get(x).getOrDefault(y, 0) + 1);
```

**選項 3：自訂 Point 類別，覆寫 `equals()` + `hashCode()`**
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

#### 總結表

| key 型別 | 可行嗎？ | 原因 |
|----------|--------|-----|
| `int[]` / `Integer[]` | ❌ | `equals()`/`hashCode()` 用的是記憶體位址 |
| `String`（例如 `"x,y"`） | ✅ | 本來就是依值比較 |
| `Map<Integer, Map<Integer, Integer>>` | ✅ | 巢狀 map 直接繞開這個問題 |
| 覆寫 `equals()` + `hashCode()` 的自訂類別 | ✅ | 明確定義依值判斷的身分 |
| `List<Integer>` | ✅ | `ArrayList.equals()` 是依內容比較 |

**備註**：`List<Integer>`（例如 `Arrays.asList(x, y)`）也能當 map 的 key，因為 `ArrayList` 覆寫了 `equals()` 和 `hashCode()` 來比較元素 — 但它比 `String` key 慢。

## 佇列、堆積與堆疊

### PriorityQueue（堆積）基礎


**關鍵概念**：Java 的 `PriorityQueue` **預設是最小堆積**。

#### 最小堆積的寫法
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

#### 最大堆積的寫法

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

**常見用途**：Top-K 問題、求中位數、任務排程

### PriorityQueue 範例


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

### 自訂排序的 PriorityQueue

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

### 初始化佇列


- https://stackoverflow.com/questions/4626812/how-do-i-instantiate-a-queue-object-in-java

- Queue 是一個 `interface`，也就是說你`不能`直接建構一個 Queue。
- 請改用下列其中一種實作：
```text
AbstractQueue, ArrayBlockingQueue, ArrayDeque, ConcurrentLinkedQueue, DelayQueue, LinkedBlockingQueue, LinkedList, PriorityBlockingQueue, PriorityQueue, or SynchronousQueue.
```


### `add()` vs `offer()`


| 方法 | 失敗時的行為 | 回傳型別 | 最佳使用時機 |
|--------|------------------|-------------|---------------|
| `add(e)` | **丟出例外** | `boolean` | 失敗就該中止執行時 |
| `offer(e)` | **回傳 false** | `boolean` | 想優雅地處理失敗時 |

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

**建議**：有容量上限的佇列用 `offer()`，像 `LinkedList` 這種無上限的用 `add()`。


### 佇列的移除方法


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


### 把陣列推進堆疊

```java
// java
// LC 739
Stack<int[]> stack = new Stack<>();

int[] init = new int[2];
init[0]  = temperatures[0];
init[1] = 0;
stack.push(init);
```

### 走訪堆疊

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

## Pair

### `Pair` 資料結構


- Pair 提供 (key, value) 結構
- 提供 getKey、getValue 方法
- 可以放進其他資料結構裡（例如佇列、雜湊表……）
- 預設的 Java 函式庫、apache.common 或其他函式庫都有

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

- 或者，你也可以自己定義 pair 結構：

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

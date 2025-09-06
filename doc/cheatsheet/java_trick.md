---
layout: cheatsheet
title: "Java Programming Tricks"
description: "Essential Java programming tricks and patterns for competitive programming"
category: "Language"
difficulty: "Medium"
tags: ["java", "tricks", "patterns", "competitive-programming"]
patterns:
  - Java syntax shortcuts
  - Collections best practices
  - Performance optimizations
---

# Java Programming Tricks & Patterns

## Overview

This document provides essential Java programming tricks, patterns, and best practices commonly used in competitive programming, interviews, and algorithm implementation.

### References
- [Java Syntax Sugar Guide](https://javaguide.cn/java/basis/syntactic-sugar.html#%E5%86%85%E9%83%A8%E7%B1%BB)
- [Java Collections Best Practices](https://javaguide.cn/java/collection/java-collection-precautions-for-use.html)

## 1) Core Data Structures

### 1.1) Priority Queue (Heap)

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

### 1.2) Character Operations

**Key Methods**: `charAt()`, character comparisons, ASCII operations

```java
// Basic character access
String s = "www.google.com";
char result = s.charAt(6);  // Returns 'g'

// Character comparison in palindrome check (LC 647)
while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
    l--;
    r++;
    count++;
}

// Character to index mapping (lowercase a-z)
char c = 'c';
int index = c - 'a';  // Returns 2 (c is 3rd letter, 0-indexed)
```

**Performance Note**: `charAt(i)` is O(1) for strings, making it efficient for character-by-character processing.

## 2) Array and Collection Operations

### 2.1) Arrays vs Collections Key Differences
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


### 2.2) Array Initialization Patterns

```java
// 1D Array Initialization
int[] arr1 = new int[5];                    // [0, 0, 0, 0, 0]
int[] arr2 = {1, 2, 3, 4, 5};              // Direct initialization
int[] arr3 = new int[]{1, 2, 3, 4, 5};     // Explicit initialization

// 2D Array Initialization  
int[][] matrix = new int[3][4];             // 3 rows, 4 columns (all zeros)
int[][] matrix2 = {% raw %}{{1, 2}, {3, 4}, {5, 6}}{% endraw %}; // Direct 2D initialization

// Dynamic 2D array (common in LeetCode)
int k = 4;
int[][] result = new int[k][2];             // k rows, 2 columns each
result[0] = new int[]{0, 1};                // Assign first row
result[1] = new int[]{2, 3};                // Assign second row

// Printing arrays
System.out.println(Arrays.toString(arr2));      // 1D: [1, 2, 3, 4, 5]
System.out.println(Arrays.deepToString(result)); // 2D: [[0, 1], [2, 3], [0, 0], [0, 0]]
```


### 2.3) Array ↔ List Conversions

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


### 2.4) HashMap Advanced Operations

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

## 3) String Operations

### 3.1) String to Character Array
```java
// Method 1: toCharArray() - Most efficient for character processing
String s = "hello";
char[] chars = s.toCharArray();
for (char c : chars) {
    System.out.println(c);  // h, e, l, l, o
}

// Method 2: charAt() - Good for selective access
for (int i = 0; i < s.length(); i++) {
    char c = s.charAt(i);
    // Process character
}

// Method 3: split("") - Creates String array (less efficient)
String[] chars2 = s.split("");  // ["h", "e", "l", "l", "o"]
```

**Performance**: `toCharArray()` > `charAt()` > `split("")` for character iteration

**Quick Reference: Array vs List**
```java
// Array - Fixed size, primitive/object types
int[] intArray = {0, 1, 2, 3};           // Primitive array
String[] stringArray = {"a", "b", "c"}; // Object array

// List - Dynamic size, object types only
List<Integer> intList = new ArrayList<>();   // Wrapper type required
List<String> stringList = new ArrayList<>(); // Object type
```

### 1-0-1) Init a List
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

#### 1-0-0-1) `Reverse` loop over a list

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


### 1-0-2) append value to a 2D list
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


### 1-0-3) Paste value to List with `index`
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

### 1-0-3) Reverse List
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

### 1-0-4) Reverse String
```java
// java (via StringBuilder)
// LC 567

private String reverseString(String input){

if (input.equals(null) || input.length() == 0){
    return null;
}

StringBuilder builder = new StringBuilder(input).reverse();
return builder.toString();
} 
```

### 3.2) Substring Operations

```java
String s = "hello world";

// substring(start, end) - [start, end) interval
System.out.println(s.substring(0, 1));   // "h"
System.out.println(s.substring(0, 5));   // "hello"
System.out.println(s.substring(6));      // "world" (from index 6 to end)
System.out.println(s.substring(2, 8));   // "llo wo"

// Common patterns
String firstChar = s.substring(0, 1);           // First character
String lastChar = s.substring(s.length() - 1);  // Last character
String withoutFirst = s.substring(1);           // Remove first character
String withoutLast = s.substring(0, s.length() - 1); // Remove last character
```

**Important**: `substring(start, end)` uses **[start, end)** interval - includes start, excludes end.


### 3.3) String Character Replacement

```java
// Pattern: Replace character at specific index
String original = "apple";
char[] replacements = {'1', '2', '3', '4', '5'};

// Method 1: Using substring (creates new strings)
for (int i = 0; i < original.length(); i++) {
    String modified = original.substring(0, i) + 
                     replacements[i] + 
                     original.substring(i + 1);
    System.out.println(modified);
    // Output: "1pple", "a2ple", "ap3le", "app4e", "appl5"
}

// Method 2: Using StringBuilder (more efficient)
for (int i = 0; i < original.length(); i++) {
    StringBuilder sb = new StringBuilder(original);
    sb.setCharAt(i, replacements[i]);
    System.out.println(sb.toString());
}

// Method 3: Using char array (most efficient for multiple changes)
char[] chars = original.toCharArray();
chars[2] = 'X';  // Replace specific character
String result = new String(chars);  // "apXle"
```


### 1-0-4-2) Sort String

```java
// java
// LC 49

/** NOTE !!!
*
*  We sort String via below op
*
*  step 1) string to char array
*  step 2) sort char array via "Arrays.sort"
*  step 3) char array to string (String x_str  = new String(x_array))
*
*/
String x = "cba";
char[] x_array = x.toCharArray();
Arrays.sort(x_array);
String x_str  = new String(x_array);
```

### 1-0-5) Access elements in a String
```java
// java (via .split(""))

String word = "heloooo 123 111";
for (String x : word.split("")){
System.out.println(x);
}

// LC 208
// ..
for (String c : word.split("")) {
cur = cur.children.get(c);
if (cur == null) {
    return false;
}
}
// ..
```


### 1-0-6) Check if a `str A` can be formed by the oter `str B` by deleting some of characters
```java
// LC 524
private boolean canForm(String x, String s){

    /** 
     * NOTE !!!
     * 
     *  via below algorithm, we can check
     *  if "s" can be formed by the other str
     *  by some element deletion
     *  
     *  e.g.:
     *  
     *  check if "apple" can be formed by "applezz"
     *  
     *  NOTE !!!
     *  
     *   "i" as idx for s
     *   "j" as idx for x  (str in dict)
     *   
     *   we go thorough element in "s",
     *   plus, we also check condition : i < s.length() && j < x.length()
     *   and once looping is completed
     *   then we check if j == x.length(),
     *   since ONLY when idx (j) reach 
     *   
     * 
     */
    int j = 0;
    // V1 (below 2 approaches are both OK)
    for (int i = 0; i < s.length() && j < x.length(); i++){
        // NOTE !!! if element are the same, then we move x idx (j)
        if (x.charAt(j) == s.charAt(i))
            j++;
    }

    // V2
//        for (int i = 0; i < y.length(); i++){
//            if (j >= x.length()){
//                return j == x.length();
//            }
//            if (x.charAt(j) == y.charAt(i))
//                j++;
//        }
    /** NOTE !!! 
     * 
     *  if j == x.length() 
     *  -> means s idx can go through,
     *  -> means s can be formed by x (str in dict)
     */
    return j == x.length();
}
```

### 1-0-7) Access element in StringBuilder

```java
// java
// LC 767

// access element in sb vis `sb.charAt[idx]`

// ...

 StringBuilder sb = new StringBuilder("#");


/** NOTE !!! below */
if (currentChar != sb.charAt(sb.length() - 1)) {
    // ...
}
// ...
```


### 1-0-8) Update val with idx in StringBuilder

```java
// java
// LC 127

// modify val with idx 

// ...
/** NOTE !!! below */

StringBuilder sb = new StringBuilder(word);

/** NOTE !!! StringBuilder can update val per idx */

sb.setCharAt(idx, c);  // modify val to c per idx

// ...
```


### 1-1) Swap elements in char array

```java
// java
// LC 345
// https://leetcode.com/problems/reverse-vowels-of-a-string/description/

void swap(char[] chars, int x, int y) {
    char temp = chars[x];
    chars[x] = chars[y];
    chars[y] = temp;
}
```   

### 1-1-1) Assign value to an integer array
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

### 1-1-2) Init a `M x N` boolean matrix
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

### 1-1-3) Access `M x N` boolean matrix

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

### 1-2) Char array to String

- so can 1) access element 2) loop over it

```java
// java
// LC 345
// https://leetcode.com/problems/reverse-vowels-of-a-string/description/

// string -> char array
String s ="abcd";
char[] list = s.toCharArray();
System.out.println(list);


// char array -> string
char[] y = list;
String.valueOf(list);     
```  

### 1-2-1) Array to String

```java
// java
// V1
// https://youtu.be/xOppee_iSvo?t=206
Integer[] data = {5, 5, 7, 8, 9, 0};
Arrays.toString(data);


// V2
// LC 49
char array [] = strs.toCharArray();
Arrays.sort(array);
String.valueOf(array);
```  

### 1-3) Stack to String

```java
// java
// LC 844
// https://leetcode.com/problems/backspace-string-compare/editorial/
Stack<Character> ans = new Stack();
ans.push("a");
ans.push("b");
ans.push("c");


String.valueOf(ans);
```  

## 4) Sorting Operations

### 4.1) Array Sorting

#### Basic Array Sorting
```java
// Primitive arrays - natural order
int[] numbers = {5, 2, 8, 1, 9};
Arrays.sort(numbers);  // [1, 2, 5, 8, 9]

// Object arrays with custom comparator
String[] words = {"apple", "banana", "cherry"};
Arrays.sort(words);                              // Natural order (lexicographic)
Arrays.sort(words, Collections.reverseOrder());  // Reverse order
```

#### 2D Array Sorting
```java
// Sort by first element
int[][] intervals = {% raw %}{{15,20}, {0,30}, {5,10}}{% endraw %};
Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
// Result: {% raw %}{{0,30}, {5,10}, {15,20}}{% endraw %}

// Multi-criteria sorting (primary: descending, secondary: ascending)
Arrays.sort(people, (a, b) -> {
    if (a[0] != b[0]) {
        return Integer.compare(b[0], a[0]);  // First element descending
    }
    return Integer.compare(a[1], b[1]);      // Second element ascending
});

// Traditional Comparator (more verbose but clear)
Arrays.sort(people, new Comparator<int[]>() {
    @Override
    public int compare(int[] o1, int[] o2) {
        return o1[0] == o2[0] ? o1[1] - o2[1] : o2[0] - o1[0];
    }
});
```




### 4.2) In-Place vs Stream Sorting

**Critical Difference**: Mutability and performance implications

| Method | Modifies Original | Performance | Memory Usage | Return Type |
|--------|-------------------|-------------|--------------|-------------|
| `Arrays.sort(arr)` | ✅ **Yes** (in-place) | **Faster** | **Lower** | `void` |
| `Arrays.stream(arr).sorted()` | ❌ **No** (creates copy) | **Slower** | **Higher** | `Stream<T>` |

#### In-Place Sorting (Recommended)
```java
int[][] intervals = {% raw %}{{15,20}, {0,30}, {5,10}}{% endraw %};

// Sorts original array directly
Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
// intervals is now: {% raw %}{{0,30}, {5,10}, {15,20}}{% endraw %}
```

#### Stream Sorting (Functional Style)
```java
int[][] intervals = {% raw %}{{15,20}, {0,30}, {5,10}}{% endraw %};

// Original array unchanged, returns sorted stream
int[][] sorted = Arrays.stream(intervals)
    .sorted((a, b) -> Integer.compare(a[0], b[0]))
    .toArray(int[][]::new);  // Must collect to get array

// Original intervals still: {% raw %}{{15,20}, {0,30}, {5,10}}{% endraw %}
// sorted is: {% raw %}{{0,30}, {5,10}, {15,20}}{% endraw %}
```


**Demonstration:**
```java
int[][] intervals = {% raw %}{{15,20}, {0,30}, {5,10}}{% endraw %};
System.out.println("Original: " + Arrays.deepToString(intervals));

// Stream sorting - original unchanged
Arrays.stream(intervals).sorted((a,b) -> Integer.compare(a[0], b[0]));
System.out.println("After stream (no collect): " + Arrays.deepToString(intervals));
// Still: [[15, 20], [0, 30], [5, 10]]

// In-place sorting - original modified
Arrays.sort(intervals, (a,b) -> Integer.compare(a[0], b[0]));
System.out.println("After Arrays.sort: " + Arrays.deepToString(intervals));
// Now: [[0, 30], [5, 10], [15, 20]]
```


### 4.3) Collections Sorting

**Key Principle**: 
- **`Arrays.sort()`** → For arrays (primitive & object types)
- **`Collections.sort()`** → For collections (List, etc.)

#### List Sorting Examples


#### Array Sorting (Object Types)
```java
Integer[] numbers = {5, 5, 7, 8, 9, 0};

// Ascending order (natural)
Arrays.sort(numbers);

// Descending order - Method 1 (recommended)
Arrays.sort(numbers, Collections.reverseOrder());

// Descending order - Method 2 (custom comparator)
Arrays.sort(numbers, (a, b) -> b - a);
```

#### List Sorting
```java
List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9));

// Method 1: Collections.sort() - modifies original list
Collections.sort(list);                              // Ascending
Collections.sort(list, Collections.reverseOrder()); // Descending

// Method 2: List.sort() - Java 8+ (preferred)
list.sort(Integer::compareTo);                       // Ascending
list.sort((a, b) -> b - a);                         // Descending

// Method 3: Stream API - creates new list
List<Integer> sortedList = list.stream()
    .sorted(Collections.reverseOrder())
    .collect(Collectors.toList());
```


#### Complex Object Sorting
```java
// Multi-criteria sorting example
List<Integer[]> statusList = new ArrayList<>();
statusList.add(new Integer[]{1, 2});
statusList.add(new Integer[]{1, 1});
statusList.add(new Integer[]{2, 3});

statusList.sort((x, y) -> {
    if (!x[0].equals(y[0])) {
        return x[0] - y[0];  // Primary: first element ascending
    }
    return x[1] - y[1];      // Secondary: second element ascending
});
```

**Performance Comparison:**
```java
// For large datasets
List<Integer> largeList = /* millions of elements */;

// Fastest - in-place sorting
Collections.sort(largeList);  

// Slower - creates new collection
List<Integer> sorted = largeList.stream().sorted().collect(Collectors.toList());
```

### 1-4-4) Custom sorting (List)
```java
// java
// LC 524
/** NOTE !!!!
 *
 *  custom sorting list
 *  via Collections.sort and new Comparator<String>()
 */
Collections.sort(collected, new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
        /**
         * // First compare by length
         * // NOTE !! inverse order, e.g. longest str at first
         */
        int lengthComparison = Integer.compare(o2.length(), o1.length());
        /**
         *  // If lengths are equal, compare lexicographically
         *  // NOTE !!! if lengths are the same, we compare  lexicographically
         */
        if (lengthComparison == 0) {
            return o1.compareTo(o2); // lexicographical order
        }
        return lengthComparison; // sort by length
    }
});
```

### 1-4-5) Sort on `Hash Map's key and value` *****


```java
// LC 692


// IDEA: map sorting
HashMap<String, Integer> freq = new HashMap<>();
for (int i = 0; i < words.length; i++) {
    freq.put(words[i], freq.getOrDefault(words[i], 0) + 1);
}
List<String> res = new ArrayList(freq.keySet());

/**
 * NOTE !!!
 *
 *  we directly sort over map's keySet
 *  (with the data val, key that read from map)
 *
 *
 *  example:
 *
 *          Collections.sort(res,
 *                 (w1, w2) -> freq.get(w1).equals(freq.get(w2)) ? w1.compareTo(w2) : freq.get(w2) - freq.get(w1));
 */
Collections.sort(res, (x, y) -> {
    int valDiff = freq.get(y) - freq.get(x); // sort on `value` bigger number first (decreasing order)
    if (valDiff == 0){
        // Sort on `key ` with `lexicographically` order (increasing order)
        //return y.length() - x.length(); // ?
        return x.compareTo(y);
    }
    return valDiff;
});
```

### 1-4-6) sort string on `lexicographical` order

```java
// LC 692. Top K Frequent Words

String a = "abcd";
String b = "defg";

// sort on lexicographical

System.out.println(a.compareTo(b));
```


### 1-5) `Arrays.copyOfRange` : Get sub array

```java
// java
// LC 976
// https://leetcode.com/problems/largest-perimeter-triangle/description/
nums = [1,2,1,10, 11, 22, 33]
int i = 2;
int[] tmp = Arrays.copyOfRange(nums, i, i+3);
```

### 1-6) `Arrays.toString(array)`: Print `array` value 

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

### 1-7) Put array into stack
```java
// java
// LC 739
Stack<int[]> stack = new Stack<>();

int[] init = new int[2];
init[0]  = temperatures[0];
init[1] = 0;
stack.push(init);
```

### 1-7-1) Loop over elements in stack
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

### 1-8) remove element in String (`StringBuilder`)
```java
// LC 22
StringBuilder b = new StringBuilder("wefew");
System.out.println(b.toString());
b.deleteCharAt(2);
System.out.println(b.toString());
```

### 1-9-1) Order HashMap by key (`TreeMap`)
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

### 1-9-2) Sort by Map key, value

```java
// java
// (GPT)

// Create a HashMap
HashMap<String, Integer> map = new HashMap<>();
map.put("apple", 5);
map.put("banana", 2);
map.put("cherry", 8);
map.put("date", 1);

// Print the original map
System.out.println("Original map: " + map);

// Sort the map by values
List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
list.sort(Map.Entry.comparingByValue());

// Create a new LinkedHashMap to preserve the order of the sorted entries
LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

//        // V1 : via Entry
//        for (Map.Entry<String, Integer> entry : list) {
//            sortedMap.put(entry.getKey(), entry.getValue());
//        }


// V2 : via key

// NOTE !!! below
// Get the list of keys
List<String> keys = new ArrayList<>(map.keySet());


// NOTE !!! below
// Sort the keys based on their corresponding values
keys.sort((k1, k2) -> map.get(k1).compareTo(map.get(k2)));

/**
 * You can modify the code to avoid using Map.Entry by converting the
 * HashMap to a list of keys and then sorting the keys based on
 * their corresponding values. Here is the modified version:
 */
for (String key : keys) {
    sortedMap.put(key, map.get(key));
}

// Print the sorted map
System.out.println("Sorted map: " + sortedMap);

```

### 1-9-3) Access Map's `key, value` on the same time

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


### 1-10) Get max val from an Array
```java
// java
// LC 875
// https://stackoverflow.com/questions/1484347/finding-the-max-min-value-in-an-array-of-primitives-using-java
int[] piles = new int[5];
int r = Arrays.stream(piles).max().getAsInt();
```


### 1-12) Get most freq element in an array

### 1-13) Pair data structure

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

### 1-14) k++ VS k++
```java
// java
// LC 78

/** NOTE HERE !!!
*
*  ++i : i+1 first,  then do op
*  i++ : do op first, then i+1
*
*/
```

### 1-15) Get/copy current object (e.g. Array, List...) instance
```java
// java
// LC 46
List<List<Integer>> ans = new ArrayList<>();
List<Integer> cur = new ArrayList<>();
//...
ans.add(new ArrayList<>(cur));
//...
```

### 1-16) Check if a string is Palindrome
```java
// java
// LC 131
boolean isPalindrome(String s, int low, int high) {
while (low < high) {
    if (s.charAt(low++) != s.charAt(high--)) return false;
}
return true;
}   
```

### 1-17) init 2D array

```java
// java
// LC 417
public int[][] DIRECTIONS = new int[][]{% raw %}{{0, 1}, {1, 0}, {-1, 0}, {0, -1}}{% endraw %};
```

### 1-18) Arrays.fill (1 D)

```java
// java
// LC 300

/** NOTE !!! ONLY work for 1 D (since array is 1 dimension) */
int[] dp = new int[10];

// fill op
Arrays.fill(dp,1);
```

## 5) Queue Operations

### 5.1) Priority Queue Examples

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

### 1-19-1) PQ (priority queue) with custom logic
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

### 5.2) Queue Methods: `add()` vs `offer()`

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


### 5.3) Queue Removal Methods

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


### 1-20) Hashmap return defalut val
```java

// LC 424
// NOTE : map.getOrDefault(key,0) syntax :  if can find key, return its value, else, return default 0
map.put(key, map.getOrDefault(key,0)+1);


// e.g.
map.getOrDefault(key,0)
```

# 2) Other tricks

### 2-1) Init var, modify it in another method, and use it
```java
// java
// LC 131
// ...
public List<List<String>> partition_1(String s) {
    /** NOTE : 
     * 
     *  we can init result, pass it to method, modify it, and return as ans 
     */
    List<List<String>> result = new ArrayList<List<String>>();
    dfs_1(0, result, new ArrayList<String>(), s);
    return result;
}
// ...
void dfs_1(int start, List<List<String>> result, List<String> currentList, String s) {
// ..
}
```

### 2-2) Get max, min from 3 numbers

```java
// java

// LC 152
max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
min = Math.min(Math.min(temp * nums[i], min * nums[i]), nums[i]);
```

### 2-3) Use long to avoid int overflow

```java
// java
// LC 98

// ...

/**
*  NOTE !!!
*
*  Use long to handle edge cases for Integer.MIN_VALUE and Integer.MAX_VALUE
*  -> use long to handle overflow issue (NOT use int type)
*/
long smallest_val = Long.MIN_VALUE;
long biggest_val = Long.MAX_VALUE;

return check_(root, smallest_val, biggest_val);

// ...
```


### 2-4) get "1" count from integer's binary format
```java
// java

/**
*  Integer.bitCount
*
*  -> java default get number of "1" from binary representation of a 10 based integer
*
*  -> e.g.
*      Integer.bitCount(0) = 0
*      Integer.bitCount(1) = 1
*      Integer.bitCount(2) = 1
*      Integer.bitCount(3) = 2
*
*  Ref
*      - https://blog.csdn.net/weixin_42092787/article/details/106607426
*/

// LC 338

```

### 2-5) Init Queue

- https://stackoverflow.com/questions/4626812/how-do-i-instantiate-a-queue-object-in-java

- A Queue is an `interface`, which means you `cannot` construct a Queue directly.
- Consinder use one of below implementation:
```
AbstractQueue, ArrayBlockingQueue, ArrayDeque, ConcurrentLinkedQueue, DelayQueue, LinkedBlockingQueue, LinkedList, PriorityBlockingQueue, PriorityQueue, or SynchronousQueue.
```


### 2-6) loop over elements in map
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

### 2-7) TreeMap
- java.util.TreeMap.floorKey()
- will return max val in its key set, if empty, return null
- 還有一種Map，它在內部對Key進行排序，Map就是SortedMap。
- SortedMap保證遍歷時以Key的順序來進行排序。預設按字母排序：
- 使用TreeMap時，輸入的Key必須實作Comparable介面。
- https://www.yxjc123.com/post/v0i7dl
- https://liaoxuefeng.com/books/java/collection/tree-map/index.html


### 2-8) `random.nextInt`
```java
// java
// LC 528

/** bound : range of random int can be returned */
//  @param bound the upper bound (exclusive).  Must be positive.
Random random = new Random();

//  * @param bound the upper bound (exclusive).  Must be positive.
System.out.println(random.nextInt(10));
System.out.println(random.nextInt(10));
System.out.println(random.nextInt(100));
```


### 2-9) HashMap - Track element count in order

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

### 3-1) Primitive type pass in func

```java
// java
// LC 695

/**
 *  NOTE !!!!
 *
 *   DON'T pass `area` as parameter to the DFS func (getBiggestArea)
 *
 *   Reason:
 *
 *   1) in java, primitives pass by value.
 *      `int` is one of the primitives
 *
 *   2) meaning when we pass `area` to dfs,
 *      it actually sent as a new COPY everytime,
 *      which makes us CAN'T track/persist the new area value
 */

// ...

 private int _getArea(int[][] grid, boolean[][] seen, int x, int y){

    // ...
    return 1 + _getArea(grid, seen, x+1, y) +
                _getArea(grid, seen, x-1, y) +
                _getArea(grid, seen, x, y+1) +
                _getArea(grid, seen, x, y-1);

}
```

### 3-2) Map characters to idx

A **classic Java trick** for mapping characters to array indices — especially when working with the lowercase English alphabet (`'a'` to `'z'`).

---

### 👇 Line in question:

```java
orderMap[order.charAt(i) - 'a'] = i;
```

---

### 🔍 What's happening?

Let’s say:
```java
order = "hlabcdefgijkmnopqrstuvwxyz";
```

So `order.charAt(i)` gets a character from the alien alphabet — for example:

- `i = 0`: `order.charAt(0)` = `'h'`
- `'h' - 'a'` → this subtracts ASCII/Unicode values:  
  `'h' = 104`, `'a' = 97`  
  ➜ `104 - 97 = 7`

So:
```java
orderMap[7] = 0;
```

This tells us:
> In the alien alphabet, `'h'` is at position `0`.

---

### 🧠 So what is `'c' - 'a'`?

| Character | ASCII | `'char' - 'a'` |
|-----------|-------|----------------|
| `'a'`     | 97    | `0`            |
| `'b'`     | 98    | `1`            |
| `'c'`     | 99    | `2`            |
| `'z'`     | 122   | `25`           |

So `'c' - 'a' == 2'` — we use this to convert `'c'` to the index `2`.

---

### ✅ Why do we use this?

Because we can now use a simple array of size 26:

```java
int[] orderMap = new int[26];
```

And **store the rank** of each character in constant time using:

```java
orderMap[c - 'a']
```

Which is way faster than repeatedly doing:

```java
order.indexOf(c)  // O(n) each time
```

---

### ✅ TL;DR

```java
order.charAt(i) - 'a'
```

✔️ Converts a letter to a **0-based index** (e.g. `'a'` → 0, `'z'` → 25)  
✔️ Works because characters are basically integers in Java (`char` uses UTF-16)  
✔️ Super efficient for problems limited to lowercase letters

---


### 6.2) Character Range Iteration

```java
// Iterate through lowercase alphabet
for (char c = 'a'; c <= 'z'; c++) {
    System.out.print(c + " ");  // Output: a b c d e f g h i j k l m n o p q r s t u v w x y z
}

// Iterate through uppercase alphabet  
for (char c = 'A'; c <= 'Z'; c++) {
    System.out.print(c + " ");  // Output: A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
}

// Generate all single-character replacements (LC 127)
String word = "hit";
for (int i = 0; i < word.length(); i++) {
    for (char c = 'a'; c <= 'z'; c++) {
        if (c != word.charAt(i)) {
            String newWord = word.substring(0, i) + c + word.substring(i + 1);
            // Process newWord
        }
    }
}
```


## 7) Quick Reference & Summary

### 7.1) Most Common Patterns

#### Data Structure Initialization
```java
// Arrays
int[] arr = new int[n];                    // Fixed size
int[][] matrix = new int[rows][cols];      // 2D array

// Collections
List<Integer> list = new ArrayList<>();   // Dynamic list
Map<String, Integer> map = new HashMap<>(); // Key-value store
Set<Integer> set = new HashSet<>();        // Unique elements
Queue<Integer> queue = new LinkedList<>(); // FIFO operations

// Priority Queues
PriorityQueue<Integer> minHeap = new PriorityQueue<>();              // Min-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // Max-heap
```

#### Essential Conversions
```java
// String ↔ Character Array
String str = "hello";
char[] chars = str.toCharArray();          // String to char array
String newStr = new String(chars);         // Char array to String

// Array ↔ List  
Integer[] arr = {1, 2, 3};
List<Integer> list = new ArrayList<>(Arrays.asList(arr));  // Array to List
Integer[] newArr = list.toArray(new Integer[0]);           // List to Array

// Character to Index (for a-z)
int index = character - 'a';               // 'a'→0, 'b'→1, ..., 'z'→25
```

#### Common Operations
```java
// HashMap with default values
map.getOrDefault(key, defaultValue);
map.putIfAbsent(key, value);

// Sorting  
Arrays.sort(array);                        // In-place array sort
Collections.sort(list);                    // In-place list sort
list.sort(Collections.reverseOrder());     // Reverse order

// String operations
s.charAt(i);                               // Get character at index
s.substring(start, end);                   // [start, end) substring
StringBuilder sb = new StringBuilder();     // Mutable string
```

### 7.2) Performance Tips

| Operation | Efficient Approach | Avoid |
|-----------|-------------------|-------|
| **String Building** | `StringBuilder` | String concatenation in loops |
| **Character Access** | `toCharArray()` then iterate | `charAt()` in tight loops |
| **Sorting** | `Arrays.sort()`, `Collections.sort()` | Stream sorting for large data |
| **Array Printing** | `Arrays.toString()`, `Arrays.deepToString()` | Manual iteration |
| **Character Mapping** | `char - 'a'` | `indexOf()` repeated calls |

### 7.3) Common LeetCode Patterns

#### Frequency Counting
```java
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray()) {
    freq.put(c, freq.getOrDefault(c, 0) + 1);
}
```

#### Two Pointers with Character Comparison
```java
int left = 0, right = s.length() - 1;
while (left < right) {
    if (s.charAt(left) != s.charAt(right)) return false;
    left++;
    right--;
}
```

#### Priority Queue for Top-K Problems
```java
PriorityQueue<Integer> heap = new PriorityQueue<>();
for (int num : nums) {
    heap.offer(num);
    if (heap.size() > k) heap.poll();
}
```

### 7.4) Memory Management

- **Primitive arrays**: More memory efficient than object arrays
- **ArrayList**: Automatically resizes, initial capacity matters for large datasets  
- **StringBuilder**: Use for string concatenation in loops
- **Character arrays**: More efficient than String manipulation for character processing

## 8) Advanced Examples & Recursion Patterns

### 8.1) Recursion Parameter Passing

```java
// LC 104
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Recursion/MaximumDepthOfBinaryTree.java

**Important Concept**: In Java, primitives are **passed by value** (creates copies).

```java
// ❌ WRONG: Primitive parameter won't persist changes across recursive calls
public int wrongDepth(TreeNode root, int depth) {
    if (root == null) return depth;
    depth++;  // This increment is lost after recursion returns
    return Math.max(wrongDepth(root.left, depth), wrongDepth(root.right, depth));
}

// ✅ CORRECT: Use instance variables for state that needs to persist
class Solution {
    private int maxDepth = 0;  // Instance variable persists across calls
    
    public int maxDepth(TreeNode root) {
        depthHelper(root, 0);
        return maxDepth;
    }
    
    private void depthHelper(TreeNode root, int currentDepth) {
        if (root == null) return;
        
        maxDepth = Math.max(maxDepth, currentDepth + 1);  // Update global state
        depthHelper(root.left, currentDepth + 1);
        depthHelper(root.right, currentDepth + 1);
    }
}

// ✅ ALTERNATIVE: Return and combine values (functional approach)
public int maxDepth(TreeNode root) {
    if (root == null) return 0;
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
}
```

**Key Takeaway**: When you need to track state across recursive calls, either use instance variables or design the recursion to return and combine values.
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

```java
// java
String x = "abcxyz";
for ( Character c: x.toCharArray()){
    /** 
     *  c = a, a - ? = 0
     *  c = b, a - ? = -1
     *  c = c, a - ? = -2
     *  c = x, a - ? = -23
     *  c = y, a - ? = -24
     *  c = z, a - ? = -25
     */
    System.out.println(" c = " + c +
            ", a - ? = " + ('a' - c)
    );
}
```

**Performance Note**: `charAt(i)` is O(1) for strings, making it efficient for character-by-character processing.


### 1.3) Char Digit to Integer Value (`char - '0'`)

**Key Concept**: Convert a character digit ('0'-'9') to its integer value using ASCII subtraction.

```java
// Basic conversion: char digit → int value
char c = '3';
int value = c - '0';  // value = 3

// Why it works:
// '0' = 48 (ASCII)
// '1' = 49
// '2' = 50
// '3' = 51  →  '3' - '0' = 51 - 48 = 3
// ...
// '9' = 57  →  '9' - '0' = 57 - 48 = 9
```

**Common Pattern: Increment/Decrement Digit Characters (LC 752 - Open the Lock)**

```java
// LC 752 - Open the Lock
// Rotate lock wheel: '0' → '1' → '2' ... '9' → '0' (wrap around)

String cur = "0000";

for (int i = 0; i < 4; i++) {
    char c = cur.charAt(i);

    // Get integer value from char
    int digit = c - '0';  // e.g., '3' → 3

    // Calculate rotations with wrap-around
    int valPlus = (digit + 1) % 10;   // 9 → 0 (wrap)
    int valMinus = (digit - 1 + 10) % 10;  // 0 → 9 (wrap)

    // Convert back to char for new string
    char charPlus = (char) ('0' + valPlus);   // 4 → '4'
    char charMinus = (char) ('0' + valMinus); // 2 → '2'

    // Build new lock combinations
    String str1 = cur.substring(0, i) + charPlus + cur.substring(i + 1);
    String str2 = cur.substring(0, i) + charMinus + cur.substring(i + 1);
}
```

**Quick Reference Table:**

| Operation | Code | Example |
|-----------|------|---------|
| Char → Int | `c - '0'` | `'7' - '0'` → `7` |
| Int → Char | `(char)('0' + n)` | `(char)('0' + 7)` → `'7'` |
| Increment (wrap) | `(digit + 1) % 10` | `9 + 1` → `0` |
| Decrement (wrap) | `(digit - 1 + 10) % 10` | `0 - 1` → `9` |

**Why `+ 10` in decrement?**
```java
// Without +10: (0 - 1) % 10 = -1  ❌ (negative!)
// With +10:    (0 - 1 + 10) % 10 = 9  ✅

// The +10 ensures the value is always positive before modulo
int valMinus = (digit - 1 + 10) % 10;
```

**Comparison: Letter vs Digit Mapping**

| Type | Char → Index | Index → Char | Range |
|------|--------------|--------------|-------|
| **Letters** | `c - 'a'` | `(char)('a' + i)` | 'a'-'z' → 0-25 |
| **Digits** | `c - '0'` | `(char)('0' + i)` | '0'-'9' → 0-9 |


### 1.4) Replace char at idx in String

```java
// LC 127

String s = "abcd";

char[] arr = s.toCharArray();

arr[0] = 'z';

String newS = new String(arr);

//System.out.println("s =  "  + new String());
```


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

### 2.2) Array Initialization Patterns


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

### 1-0-0-1) `Replace` list val at index

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

```java
// java
// LC 752. Open the Lock
while (!q.isEmpty()) {
    // ...
    // process all nodes in current layer
    for (int k = 0; k < size; k++) {
        // ...
        // Move 4 directions (wheel rotations)
        for (int i = 0; i < 4; i++) {
            // ...

            /**  NOTE !!!  
             *  
             *  Instead of using stringBuilder,
             *  we use `substring` for update string per given idx
             */
            String str1 = cur.substring(0, i) + valMinus + cur.substring(i + 1);
            String str2 = cur.substring(0, i) + valPlus + cur.substring(i + 1);
            
            // ...
        }
    }
    // ...
}
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


### 3.4) String to Integer Parsing (`Integer.parseInt`)

**Key Behavior**: `Integer.parseInt()` automatically strips leading zeros.

```java
// Integer.parseInt handles leading zeros automatically
Integer.parseInt("001");    // 1
Integer.parseInt("00001");  // 1
Integer.parseInt("0100");   // 100
Integer.parseInt("0");      // 0
Integer.parseInt("42");     // 42
```

**Common Pattern: Version Number Comparison (LC 165)**
```java
// java
// LC 165. Compare Version Numbers
// Split version strings and compare revision by revision
// Integer.parseInt handles leading zeros so "1.01" == "1.001"

String[] v1 = version1.split("\\.");  // NOTE: "." is regex special char, must escape
String[] v2 = version2.split("\\.");

int n = Math.max(v1.length, v2.length);

for (int i = 0; i < n; i++) {
    // If index out of bounds, treat missing revision as 0
    int num1 = (i < v1.length) ? Integer.parseInt(v1[i]) : 0;
    int num2 = (i < v2.length) ? Integer.parseInt(v2[i]) : 0;

    if (num1 > num2) return 1;
    if (num1 < num2) return -1;
}

return 0;  // All revisions equal
```

**Key Tricks:**
```
1. Leading zeros handled automatically:
   Integer.parseInt("001") == 1  ✅  (no manual stripping needed)

2. Split on "." requires regex escaping:
   str.split("\\.")   ✅
   str.split(".")     ❌  ("." in regex means "any character")

3. Handle unequal lengths with ternary + default 0:
   int num = (i < arr.length) ? Integer.parseInt(arr[i]) : 0;
   This treats "1.0" and "1.0.0.0" as equal
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
int[][] intervals = {{15,20}, {0,30}, {5,10}};
Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
// Result: {{0,30}, {5,10}, {15,20}}

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
int[][] intervals = {{15,20}, {0,30}, {5,10}};

// Sorts original array directly
Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
// intervals is now: {{0,30}, {5,10}, {15,20}}
```

#### Stream Sorting (Functional Style)
```java
int[][] intervals = {{15,20}, {0,30}, {5,10}};

// Original array unchanged, returns sorted stream
int[][] sorted = Arrays.stream(intervals)
    .sorted((a, b) -> Integer.compare(a[0], b[0]))
    .toArray(int[][]::new);  // Must collect to get array

// Original intervals still: {{15,20}, {0,30}, {5,10}}
// sorted is: {{0,30}, {5,10}, {15,20}}
```


**Demonstration:**
```java
int[][] intervals = {{15,20}, {0,30}, {5,10}};
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

### 1-4-4-1) Custom Sort — Comparator Return Value Rules ⭐

> **Core Rule**: The sign of the comparator's return value determines element order.

| Return Value | Meaning | Effect |
|---|---|---|
| **negative** (e.g. -1) | o1 < o2 | o1 comes **before** o2 |
| **positive** (e.g. +1) | o1 > o2 | o1 comes **after** o2 |
| **0** | o1 == o2 | order **unchanged** |

```java
// LC 905 - Sort Array By Parity
// IDEA: Custom Comparator — return -1/0/1 to control ordering
// Even numbers before odd numbers using explicit return values

// Method 1: Explicit -1 / 0 / 1 (most readable for interviews)
Collections.sort(list, new Comparator<Integer>() {
    @Override
    public int compare(Integer o1, Integer o2) {
        // o1 even, o2 odd  → o1 should come first  → return negative
        if (o1 % 2 == 0 && o2 % 2 == 1) return -1;
        // o1 odd,  o2 even → o2 should come first  → return positive
        if (o1 % 2 == 1 && o2 % 2 == 0) return 1;
        // both same parity → order unchanged
        return 0;
    }
});

// Method 2: Lambda shorthand — compare parity values directly (0=even, 1=odd)
// Integer.compare(v1, v2):  returns -1 if v1 < v2, 0 if equal, +1 if v1 > v2
Collections.sort(list, (o1, o2) -> Integer.compare(o1 % 2, o2 % 2));
// even(0) before odd(1) → ascending parity order = evens first ✓

// Method 3: Two-pointer in-place (O(N) time, O(1) space — most efficient)
public int[] sortArrayByParity(int[] nums) {
    int l = 0, r = nums.length - 1;
    while (l < r) {
        if (nums[l] % 2 > nums[r] % 2) { // l is odd, r is even → swap
            int tmp = nums[l]; nums[l] = nums[r]; nums[r] = tmp;
        }
        if (nums[l] % 2 == 0) l++;  // l is even → move right
        if (nums[r] % 2 == 1) r--;  // r is odd  → move left
    }
    return nums;
}
```

#### Comparator Mental Model
```
compare(o1, o2):
  return NEGATIVE  →  keep o1 before o2   (o1 is "smaller")
  return POSITIVE  →  move o1 after  o2   (o1 is "larger")
  return 0         →  no change

Tip: think of it as: "what is o1 - o2?"
  o1 < o2  →  negative  →  ascending order (small first)
  o1 > o2  →  positive  →  o2 goes first  (for descending: flip to o2 - o1)
```

#### Common Patterns Summary
```java
// Ascending (natural order)
(a, b) -> a - b                          // ⚠ may overflow for large ints
(a, b) -> Integer.compare(a, b)          // ✅ safe

// Descending
(a, b) -> b - a                          // ⚠ may overflow
(a, b) -> Integer.compare(b, a)          // ✅ safe

// Multi-criteria: primary DESC, secondary ASC
(a, b) -> a[0] != b[0] ? b[0] - a[0] : a[1] - b[1]

// Custom property (e.g. sort by string length, then lexicographic)
(s1, s2) -> s1.length() != s2.length()
    ? s2.length() - s1.length()          // longer first
    : s1.compareTo(s2)                   // lexicographic if same length
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

### 1-16) `equals()` vs `==` — When to Use Which

> **Core Rule**: `==` compares **references** (are they the same object?). `equals()` compares **content** (do they have the same value?).

#### The Rule

```
==        → compares memory addresses (reference identity)
equals()  → compares logical content (value equality)
```

#### Comparing Collections (List, Set, Map)

```java
// LC 872 — Leaf-Similar Trees
List<Integer> list1 = new ArrayList<>();
List<Integer> list2 = new ArrayList<>();

getLeafSeq(root1, list1);
getLeafSeq(root2, list2);

/** NOTE !!!
 *
 *  Use `equals()` to compare two lists by content.
 *  `==` would check if they are the SAME object (always false here).
 */
list1 == list2        // ❌ false — different objects, even if same content
list1.equals(list2)   // ✅ true  — compares element-by-element
```

#### Comparing Strings

```java
String a = new String("hello");
String b = new String("hello");

a == b          // ❌ false — different objects
a.equals(b)     // ✅ true  — same content

// BUT: string literals from pool may share reference
String c = "hello";
String d = "hello";
c == d          // ✅ true (same pooled object) — but DON'T rely on this!
c.equals(d)     // ✅ true — always use this
```

#### Comparing Wrapper Types (Integer, Long, etc.)

```java
Integer x = 127;
Integer y = 127;
x == y          // ✅ true (cached: -128 to 127)

Integer a = 128;
Integer b = 128;
a == b          // ❌ false — outside cache range, different objects!
a.equals(b)     // ✅ true  — always correct

// SAFE alternative for null-safe comparison
Objects.equals(a, b)  // ✅ handles null without NPE
```

#### Comparing Primitives

```java
int i = 5;
int j = 5;
i == j          // ✅ correct — primitives have no .equals()
                // Primitives are ALWAYS compared by value with ==
```

#### Summary Table

| Type | Use `==` | Use `equals()` | Pitfall |
|------|----------|----------------|---------|
| `int`, `long`, `char`, ... (primitives) | **Yes** | N/A (no method) | None |
| `String` | **No** | **Yes** | Literals may share ref, but don't rely on it |
| `Integer`, `Long`, ... (wrappers) | **No** | **Yes** | `==` works for -128..127 only (cache) |
| `List`, `Set`, `Map` | **No** | **Yes** | `==` checks identity, not content |
| Custom objects | **No** | **Yes** (if overridden) | Default `equals()` is same as `==` |
| `null` check | **Yes** (`x == null`) | **No** (NPE!) | Always use `==` for null check |

#### Interview Quick Rule

```
Q: "Should I use == or equals()?"

→ Is it a primitive (int, long, boolean, char, double)?
  YES → use ==

→ Is it checking for null?
  YES → use ==  (x == null)

→ Everything else (String, Integer, List, Object...)?
  → use equals()
  → use Objects.equals(a, b) if either could be null
```

### 1-16-1) Check if a string is Palindrome
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
public int[][] DIRECTIONS = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
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

### 2-1) Init var, modify it in another method, and use it — Pass-by-Reference Pattern ⭐

> **Core Concept**: Reference types (StringBuilder, List, int[], Map, etc.) are passed by **reference**, not by value. Changes made inside a function persist after the function returns.

#### Pattern 1: StringBuilder for Path/String Building (LC 694)

```java
// LC 694 - Number of Distinct Islands
// Pattern: Create placeholder → Pass to DFS → Use modified result

Set<String> uniqueIslands = new HashSet<>();

for (int r = 0; r < rows; r++) {
    for (int c = 0; c < cols; c++) {
        if (grid[r][c] == 1) {
            // Step 1: Create empty StringBuilder
            StringBuilder pathSignature = new StringBuilder();

            // Step 2: Pass to DFS — it will modify pathSignature in place
            /** NOTE !!!
             *  We pass `pathSignature` as a reference.
             *  DFS will call pathSignature.append(...)
             *  These changes PERSIST after DFS returns
             */
            dfs(grid, r, c, pathSignature, 'S');

            // Step 3: After DFS returns, pathSignature is populated
            if (pathSignature.length() > 0) {
                uniqueIslands.add(pathSignature.toString());
            }
        }
    }
}

private void dfs(int[][] grid, int r, int c, StringBuilder path, char direction) {
    // Base case
    if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 0) {
        return;
    }

    grid[r][c] = 0;

    // ✅ MODIFY the passed reference
    // This directly modifies the caller's StringBuilder object
    path.append(direction);

    // Explore neighbors
    dfs(grid, r + 1, c, path, 'D');
    dfs(grid, r - 1, c, path, 'U');
    dfs(grid, r, c + 1, path, 'R');
    dfs(grid, r, c - 1, path, 'L');

    // Backtrack: undo the append
    path.append('O');
}
```

**Memory Model:**
```
Main thread:
pathSignature = StringBuilder{} at memory address 0x1000

    Call dfs(..., pathSignature, 'S')
    ├── path parameter = reference to 0x1000
    ├── path.append('S')  → modifies object at 0x1000 → "S"
    │
    ├── Call dfs(..., path, 'D')
    │   ├── path parameter = reference to 0x1000 (SAME object!)
    │   ├── path.append('D')  → modifies object at 0x1000 → "SD"
    │   ├── path.append('O')  → modifies object at 0x1000 → "SDO"
    │   └── return
    │
    ├── path.append('O')  → modifies object at 0x1000 → "SDOO"
    └── return

Back in main:
pathSignature = StringBuilder{"SDOO"}  ✅ (MODIFIED!)
```

#### Pattern 2: List for Collecting Results (LC 113 Path Sum II)

```java
// LC 113 - Path Sum II
// Similar pattern but with List<Integer> for path collection

public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    dfs(root, targetSum, path, result);
    return result;
}

private void dfs(TreeNode node, int remain, List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;

    // Modify the passed List
    path.add(node.val);

    if (node.left == null && node.right == null && remain == node.val) {
        // Snapshot the path before backtracking
        result.add(new ArrayList<>(path));
    } else {
        dfs(node.left, remain - node.val, path, result);
        dfs(node.right, remain - node.val, path, result);
    }

    // Backtrack: undo the add
    path.remove(path.size() - 1);
}
```

**Key Difference from Primitives:**
```java
// ❌ PRIMITIVE: Changes don't persist
private void addToSum(int currentSum) {
    currentSum += 5;  // Only affects local copy
}
int mySum = 10;
addToSum(mySum);
System.out.println(mySum);  // Still 10, NOT 15!

// ✅ REFERENCE: Changes persist
private void addToList(List<Integer> list) {
    list.add(5);  // Affects the original list object
}
List<Integer> myList = new ArrayList<>();
addToList(myList);
System.out.println(myList);  // [5] ✅ MODIFIED!
```

#### Pattern 3: General Pattern — Create, Pass, Modify, Use

```java
// Generic template for this pattern:

public Type method() {
    // 1. Create placeholder (reference type)
    SomeRefType placeholder = new SomeRefType();

    // 2. Pass to helper function
    helperFunction(placeholder, otherParams);

    // 3. Use modified result
    return placeholder;  // or use directly
}

private void helperFunction(SomeRefType data, OtherParams...) {
    // Modify the reference — changes persist in caller
    data.modify(...);

    // Recurse if needed
    helperFunction(data, newParams);

    // Undo if backtracking required
    data.undo(...);
}
```

#### Common Reference Types for This Pattern

| Type | Modification Methods | Backtrack Required? | Use Case |
|------|----------------------|-------------------|----------|
| `StringBuilder` | `append(x)`, `setCharAt(i, c)`, `deleteCharAt(i)` | ✅ Yes | String building with backtracking |
| `List<T>` | `add(x)`, `remove(i)`, `set(i, x)` | ✅ Yes | Path/result collection |
| `int[]` / `char[]` | `arr[i] = value` | ✅ Yes | Array modification |
| `Map<K,V>` | `put(k, v)`, `remove(k)` | ✅ Yes | Frequency tracking |
| `Queue<T>` | `add(x)`, `poll()`, `offer(x)` | ✅ Maybe | BFS level-by-level |
| `Set<T>` | `add(x)`, `remove(x)` | ✅ Yes | Visited tracking |
| Primitive `int`, `long` | N/A (pass-by-value) | ❌ No | Only for return or instance vars |
| `String` | N/A (immutable) | ❌ No | Use StringBuilder instead |

**When to Backtrack:**
```
Rule: If the parameter is a reference type that gets MODIFIED, you must UNDO the modification.

Path/List building:  path.add(val) → must do path.remove(...)
StringBuilder:       sb.append(...) → must do sb.deleteCharAt(...)
Array modification:  arr[i] = val   → must do arr[i] = oldVal
Map/Set:             data.add(x)    → must do data.remove(x)

Primitives:          No backtrack needed (they're copied)
```

#### Pattern 4: List Collection (LC 131 Palindrome Partitioning)

```java
// LC 131
public List<List<String>> partition(String s) {
    /**
     * NOTE: we can init result, pass it to method,
     * modify it, and return as ans
     */
    List<List<String>> result = new ArrayList<>();
    dfs(0, result, new ArrayList<String>(), s);
    return result;
}

private void dfs(int start, List<List<String>> result, List<String> currentList, String s) {
    // Base case: reached end of string
    if (start == s.length()) {
        // Snapshot: add a copy of currentList (don't add reference)
        result.add(new ArrayList<>(currentList));
        return;
    }

    // Try all palindromes starting from 'start'
    for (int end = start; end < s.length(); end++) {
        if (isPalindrome(s, start, end)) {
            // Add to current partition
            currentList.add(s.substring(start, end + 1));

            // Recurse
            dfs(end + 1, result, currentList, s);

            // Backtrack: remove what we added
            currentList.remove(currentList.size() - 1);
        }
    }
}

private boolean isPalindrome(String s, int l, int r) {
    while (l < r) {
        if (s.charAt(l) != s.charAt(r)) return false;
        l++;
        r--;
    }
    return true;
}
```

**Critical Distinction:**
```java
// ❌ WRONG: Adds reference to currentList (not a snapshot)
result.add(currentList);  // All entries point to same list!

// ✅ CORRECT: Add a copy
result.add(new ArrayList<>(currentList));  // Each entry is independent
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


### 8.2) Primitive vs Reference Types in Recursion — Backtracking Rule ⭐

> **Core Rule**: Primitives are pass-by-value → each call gets its own copy → **no backtracking needed**.
> Reference types (collections, arrays, objects) are pass-by-reference → shared state → **must backtrack**.

#### The Rule

```
Primitive param (int, long, double...)  →  copy per call  →  NO backtrack needed
Reference param (List, int[], HashMap)  →  shared object  →  MUST backtrack (add + remove)
Global / instance variable              →  shared state   →  MUST backtrack
```

#### Case 1: Primitive — No Backtracking Needed (LC 112 Path Sum)

```java
// LC 112 - Path Sum
// curSum is a primitive int → each recursive call gets its OWN COPY
// → no backtracking needed

public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) return false;
    if (root.left == null && root.right == null) return root.val == targetSum;
    getPathHelper(root, 0);
    return pathSumMap.containsValue(targetSum);
}

private void getPathHelper(TreeNode root, Integer curSum) {
    if (root == null) return;

    int newSum = curSum + root.val;  // new local variable — does NOT affect parent's curSum

    if (root.left == null && root.right == null) {
        pathSumMap.put(newSum, newSum);
    }

    /** NOTE !!!
     *
     *  No backtrack on curSum / newSum needed!
     *
     *  Reason:
     *   - curSum is a primitive (int / Integer with autoboxing creates a new object).
     *   - Each recursive call receives its OWN COPY of curSum.
     *   - Modifying newSum inside the call does NOT affect the parent's curSum.
     *   - When the call returns, the parent's curSum is completely unchanged.
     *
     *  Stack visualization for tree 5 -> 4 -> 11 -> 7:
     *
     *    getPathHelper(5,   curSum=0)    newSum=5
     *      getPathHelper(4,  curSum=5)   newSum=9
     *        getPathHelper(11, curSum=9) newSum=20
     *          getPathHelper(7, curSum=20) newSum=27  ← leaf, store 27
     *          ← returns, parent curSum still 20 ✅
     *        ← returns, parent curSum still 9 ✅
     *      ← returns, parent curSum still 5 ✅
     */
    getPathHelper(root.left, newSum);
    getPathHelper(root.right, newSum);
}
```

**Memory model:**
```
Stack frame:  getPathHelper(node=4, curSum=5)
              ├── newSum = 9     ← local to THIS frame
              ├── calls getPathHelper(node.left, newSum=9)
              │     └── newSum = 20  ← different frame, isolated
              └── curSum is still 5 when left call returns ✅
```

#### Case 2: Global Variable — Backtracking IS Needed (LC 112 V0-2)

```java
// BAD pattern: using an instance variable for path sum
// → ALL recursive calls share the SAME curSum → must backtrack!

private int curSum = 0;  // shared across ALL calls ← danger!

public boolean hasPathSum_0_2(TreeNode root, int targetSum) {
    curSum += root.val;  // modifies shared state

    if (root.left == null && root.right == null) {
        if (curSum == targetSum) {
            curSum -= root.val;  // MUST backtrack before returning
            return true;
        }
    }

    if (hasPathSum_0_2(root.left, targetSum)) {
        curSum -= root.val;  // MUST backtrack
        return true;
    }
    if (hasPathSum_0_2(root.right, targetSum)) {
        curSum -= root.val;  // MUST backtrack
        return true;
    }

    curSum -= root.val;  // MUST backtrack on failure path too
    return false;
}
```

#### Case 3: Reference Type (List) — Backtracking IS Needed (LC 113 Path Sum II)

```java
// List is passed by reference → shared object → must backtrack
private void dfs(TreeNode node, int remain, List<Integer> path, List<List<Integer>> res) {
    if (node == null) return;

    path.add(node.val);          // ← mutates shared list

    if (node.left == null && node.right == null && remain == node.val) {
        res.add(new ArrayList<>(path));  // snapshot before backtrack
    } else {
        dfs(node.left,  remain - node.val, path, res);
        dfs(node.right, remain - node.val, path, res);
    }

    path.remove(path.size() - 1);  // ← MUST backtrack: undo the add
}
```

#### Case 4: StringBuilder — Backtracking IS Needed (LC 988 Smallest String Starting From Leaf)

```java
// LC 988 - Smallest String Starting From Leaf
// StringBuilder is a reference type → shared object → MUST backtrack

private String smallest = "";

public String smallestFromLeaf(TreeNode root) {
    dfs(root, new StringBuilder());
    return smallest;
}

private void dfs(TreeNode node, StringBuilder sb) {
    if (node == null)
        return;

    /** NOTE !!!
     *
     *  PRE-ORDER DFS: process current node first
     */
    // 1. Add current character (0 -> 'a', 1 -> 'b', etc.)
    sb.append((char) ('a' + node.val));

    /** NOTE !!!
     *
     *  ONLY treat as result when reach `leaf`
     */
    // 2. Leaf check: If we reach a leaf, we have a candidate path
    if (node.left == null && node.right == null) {

        /** NOTE !!!
         *
         *  We reverse current StringBuilder to fit the requirement
         *  (string from leaf to root)
         */
        String currentStr = new StringBuilder(sb).reverse().toString();

        /** NOTE !!!
         *
         *  How we get the `lexicographically smaller` one:
         *  currentStr.compareTo(smallest) < 0
         */
        if (smallest.equals("") || currentStr.compareTo(smallest) < 0) {
            smallest = currentStr;
        }
    }

    // 3. Standard DFS
    dfs(node.left, sb);
    dfs(node.right, sb);

    /** NOTE !!!
     *
     *  For StringBuilder (NOT a primitive type),
     *  we MUST do BACKTRACK (undo)
     *
     *  Reason:
     *   - StringBuilder is a reference type (object)
     *   - All recursive calls share the SAME StringBuilder instance
     *   - After exploring subtrees, we must remove the current char
     *     to restore sb to its state before this call
     *   - Without this, the path would keep growing incorrectly
     *
     *  Memory model:
     *
     *    dfs(node=5, sb="e")
     *      ├── sb.append('a') → sb="ea"
     *      ├── dfs(left, sb="ea")
     *      │     └── sb.append('b') → sb="eab"
     *      │     └── sb.deleteCharAt() → sb="ea"  ← backtrack!
     *      ├── dfs(right, sb="ea")  ← sb is correctly "ea", not "eab"
     *      │     └── ...
     *      └── sb.deleteCharAt() → sb="e"  ← backtrack to parent state
     */
    // 4. BACKTRACK: Remove the last character before returning to parent
    sb.deleteCharAt(sb.length() - 1);
}
```

#### Summary Table

| State Type | Example | Backtrack? | Reason |
|---|---|---|---|
| Primitive param | `int curSum` | **No** | Each call gets own copy |
| Wrapper param (autoboxed) | `Integer curSum` | **No** | Autoboxing creates new object |
| Local variable | `int newSum = curSum + val` | **No** | Belongs to current stack frame only |
| Instance/global variable | `this.curSum` | **Yes** | Shared across all calls |
| Collection param | `List<Integer> path` | **Yes** | Reference, mutated in-place |
| Array param | `int[] path` | **Yes** | Reference, mutated in-place |
| StringBuilder param | `StringBuilder sb` | **Yes** | Reference, mutated in-place via `append()`/`deleteCharAt()` |

#### Interview Tips

```
Q: "Do I need to backtrack this variable?"

Decision tree:
1. Is it a primitive (int, long, boolean, char...)?
   → Passed as value → NO backtrack needed

2. Is it a local variable inside the current stack frame?
   → NOT shared → NO backtrack needed

3. Is it an instance/class variable?
   → Shared → YES, must backtrack

4. Is it a collection or array passed as parameter?
   → Reference = shared → YES, must backtrack

Quick mental model:
  "If I change this variable, will the CALLER see the change?"
  YES → backtrack required
  NO  → no backtrack needed
```

---

## 9) Others

### 9.0) Modifying Custom Class Fields Directly (`v.field -= 1`) ⭐

> **Core Question**: When can you write `v.cnt -= 1` and when can't you?

#### When you CAN modify directly

All three conditions must hold:

1. **Field is accessible** (not `private`, or you are inside the class)
2. **Field is not `final`**
3. **Reference is not `null`**

```java
class ValCnt {
    char val;
    int cnt;           // package-private, non-final
    ValCnt(char val, int cnt) { this.val = val; this.cnt = cnt; }
}

ValCnt v = new ValCnt('a', 3);
v.cnt -= 1;   // ✅ allowed: accessible + non-final + non-null
```

#### When you CANNOT modify directly

**Case 1 — `private` field** (outside the class)
```java
class ValCnt {
    private int cnt;   // private!
}

v.cnt -= 1;   // ❌ compile error — use a setter/method instead
```

**Case 2 — `final` field**
```java
class ValCnt {
    final int cnt;
}

v.cnt -= 1;   // ❌ compile error — cannot assign to final variable
```

**Case 3 — null reference**
```java
ValCnt v = null;
v.cnt -= 1;   // ❌ NullPointerException at runtime
```

#### Common misconception: `final` reference vs `final` field

```java
final ValCnt v = new ValCnt('a', 3);

v.cnt -= 1;              // ✅ allowed — final only locks the REFERENCE
v = new ValCnt('b', 1);  // ❌ compile error — cannot reassign final reference
```

`final` on the variable means you cannot point `v` to a different object.
It does **not** prevent mutating the object's fields.

#### `Integer` (wrapper) field — works but autoboxes

```java
class Holder { Integer cnt; }

Holder h = new Holder();
h.cnt = 3;
h.cnt -= 1;  // ✅ works, but really: h.cnt = Integer.valueOf(h.cnt.intValue() - 1)
             //    creates a new Integer object; fails if field is final
```

#### Summary table

| Situation | `v.cnt -= 1` allowed? |
|-----------|----------------------|
| `int cnt` (package-private) | ✅ yes |
| `private int cnt` (outside class) | ❌ compile error |
| `final int cnt` | ❌ compile error |
| `final ValCnt v` (reference is final) | ✅ yes — field still mutable |
| `v == null` | ❌ NullPointerException |
| `Integer cnt` (wrapper) | ✅ works, autoboxes to new object |

#### Interview trap: `v.cnt--` vs `--v.cnt` vs `v.cnt -= 1`

All three decrement `cnt` by 1. The difference is the **returned expression value**:

```java
v.cnt = 3;

int a = v.cnt--;   // a = 3  (returns BEFORE decrement), v.cnt = 2
int b = --v.cnt;   // b = 1  (returns AFTER  decrement), v.cnt = 1
v.cnt -= 1;        // no return value used, v.cnt = 0
```

Use `v.cnt -= 1` when you only care about the side-effect, not the return value.

---

### 9.1) Java `value` assign

- https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/LinkedList/ReverseLinkedList.java


```java

    public ListNode reverseList_0_0_1(ListNode head) {
            // edge
            if(head == null || head.next == null) {
                return head;
            }

            ListNode _prev = null;
            /**
             *  NOTE !!!
             *
             *   we CAN'T just assign `_prev` init val to `res`
             *   and return `res` as result
             *   e.g. this is WRONG: return res;
             *
             *  Reason:
             *   - At this point, res is just a reference to null.
             *   - As you update _prev during the loop,
             *     res DOES NOT magically follow _prev.
             *     It stays stuck at the value it was assigned
             *     when you created it → null.
             *
             *   So by the end:
             *    - _prev points to the new head of the reversed list ✅
             *    - res is still null ❌
             *
             *
             *  -> Java references don’t “track” each other after assignment.
             *    res = _prev copies the reference value `at that moment`
             *    If _prev later changes, res won’t update.
             *
             */
            ListNode res = _prev;
            while(head != null){
                ListNode _next = head.next;
                head.next = _prev;
                //_prev.next = head;
                _prev = head;
                head = _next;
            }

            //return res;
            return _prev;
        }

```

### 9.2) Java `re-construct` nodes

- LC 116
- https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Recursion/PopulatingNextRightPointersInEachNode.java

```java
public Node connect_0_1(Node root) {
    if (root == null)
        return null;

    Queue<Node> q = new LinkedList<>();
    q.add(root);

    while (!q.isEmpty()) {
        int size = q.size();
        /**
         *  NOTE !!!!
         *
         *   via `prev` node,
         *   we can easily re-connect left - right node in each layer
         *
         *   pattern as below:
         *
         *     Node prev = null;
         *     for(int i = 0; i < size; i++){
         *         TreeNode cur = q.poll();
         *         // NOTE !!!! this
         *         if(prev == null){
         *             prev.next = cur;
         *         }
         *         // NOTE !!!! this
         *         prev = cur;
         *
         *         // ....
         *     }
         *
         */
        Node prev = null; // prev tracks previous node in this level

        for (int i = 0; i < size; i++) {
            Node cur = q.poll();

            if (prev != null) {
                prev.next = cur; // link previous node to current
            }
            prev = cur;

            if (cur.left != null)
                q.add(cur.left);
            if (cur.right != null)
                q.add(cur.right);
        }

        // last node in level should point to null
        if (prev != null)
            prev.next = null;
    }

    return root;
}
```

### 9.3) Pre-calculate Perfect Squares up to N

> **Trick**: Pre-compute all perfect squares ≤ N into a list, then iterate over the list instead of recalculating `i * i` each time. Useful in BFS/DP problems like LC 279 (Perfect Squares).

```java
// Pre-calculate perfect squares up to n
List<Integer> squares = new ArrayList<>();
for (int i = 1; i * i <= n; i++) {
    squares.add(i * i);
}

// Usage: iterate over pre-computed squares
for (int square : squares) {
    int nextVal = remaining - square;
    if (nextVal < 0) break; // squares are sorted, early termination
    // ...
}
```


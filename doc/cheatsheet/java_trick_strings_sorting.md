# Java Strings & Sorting Cheatsheet

> **Scope** — Java String and StringBuilder work — converting to and from `char[]`, slicing, parsing, building and mutating — together with everything comparator-shaped: array, collection and map sorting, and the return-value rules that decide the order.
> **See also**: [java_trick.md](./java_trick.md) — why `charAt` returns a number and the other language semantics behind these calls; [java_trick_collections.md](./java_trick_collections.md) — the containers being sorted; [sort.md](./sort.md) — sorting as an algorithm rather than an API; [string.md](./string.md) — string algorithms rather than string handling.

## LeetCode Problem Lists

- [String](https://leetcode.com/problem-list/string/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

## Overview

Split out of [java_trick.md](./java_trick.md). Strings and sorting share a sheet because in Java
they share a failure mode: **`String` is immutable and `Arrays.sort` on primitives cannot take a
comparator**, so both push you toward a conversion (`toCharArray`, `Integer[]`, `StringBuilder`)
before you can do the obvious thing.

### Key Properties
- **Complexity**: `substring` and `+` build a new String — O(n) each, so a loop that concatenates is O(n²); `StringBuilder` is the fix
- **Core Idea**: mutate a `char[]` or a `StringBuilder`, then convert once at the end
- **When to Use**: reach for this when the algorithm is settled and the remaining question is which API expresses it


## String ↔ char[]

### String to character array

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

### Character array to String


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

### Swapping elements in a char array


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

### Array to String


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

### Stack to String


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

## Reading & Slicing Strings

### Accessing elements in a String

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


### Substring operations


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


### String to integer parsing (`Integer.parseInt`)


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
```text
1. Leading zeros handled automatically:
   Integer.parseInt("001") == 1  ✅  (no manual stripping needed)

2. Split on "." requires regex escaping:
   str.split("\\.")   ✅
   str.split(".")     ❌  ("." in regex means "any character")

3. Handle unequal lengths with ternary + default 0:
   int num = (i < arr.length) ? Integer.parseInt(arr[i]) : 0;
   This treats "1.0" and "1.0.0.0" as equal
```

### Checking whether a String is a palindrome

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

### Checking whether one String is a subsequence of another

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

## Building & Mutating Strings

### Replacing a char at an index in a String


```java
// LC 127

String s = "abcd";

char[] arr = s.toCharArray();

arr[0] = 'z';

String newS = new String(arr);

//System.out.println("s =  "  + new String());
```


### String character replacement


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


### Reversing a String

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

### Accessing an element in a `StringBuilder`


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


### Updating a `StringBuilder` value by index


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


### Removing an element from a `StringBuilder`

```java
// LC 22
StringBuilder b = new StringBuilder("wefew");
System.out.println(b.toString());
b.deleteCharAt(2);
System.out.println(b.toString());
```

## Sorting

### Array sorting


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




### In-place vs stream sorting


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


### Collections sorting


**Key Principle**: 
- **`Arrays.sort()`** → For arrays (primitive & object types)
- **`Collections.sort()`** → For collections (List, etc.)

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

### Custom sorting a List

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

### Custom sort — comparator return-value rules ⭐


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
```text
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

### Sorting on a HashMap's keys and values



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

### Sorting by map key, then value


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

### Sorting the characters of a String


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

### Lexicographical string ordering


```java
// LC 692. Top K Frequent Words

String a = "abcd";
String b = "defg";

// sort on lexicographical

System.out.println(a.compareTo(b));
```

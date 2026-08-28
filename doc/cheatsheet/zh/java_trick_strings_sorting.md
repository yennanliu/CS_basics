# Java 字串與排序速查表

> **範圍** — Java 的 String 與 StringBuilder 操作 — 與 `char[]` 互轉、切片、解析、組字串與修改 — 加上所有跟 comparator 有關的東西：陣列、集合與 map 的排序，以及決定順序的回傳值規則。
> **另見**：[java_trick.md](./java_trick.md) — 為什麼 `charAt` 回傳的是數字，以及這些呼叫背後的其他語言語意；[java_trick_collections.md](./java_trick_collections.md) — 被排序的那些容器；[sort.md](./sort.md) — 把排序當演算法而不是當 API 來看；[string.md](./string.md) — 字串演算法而不是字串處理。

## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

## 總覽

從 [java_trick.md](./java_trick.md) 拆出來的。字串與排序會共用一份文件，是因為在 Java 裡它們共用同一種踩雷方式：**`String` 是不可變的，而 `Arrays.sort` 對基本型別不吃 comparator**，所以兩邊都會逼你先做一次轉換（`toCharArray`、`Integer[]`、`StringBuilder`），才能做那件顯而易見的事。

### 關鍵性質
- **複雜度**：`substring` 和 `+` 都會建出一個新的 String — 各是 O(n)，所以在迴圈裡串接就是 O(n²)；解法是 `StringBuilder`
- **核心想法**：改 `char[]` 或 `StringBuilder`，最後再轉換一次
- **什麼時候用**：演算法已經定案，剩下的問題只是「該用哪個 API 把它寫出來」的時候


## String ↔ char[]

### String 轉字元陣列

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

**效能**：逐字元走訪時 `toCharArray()` > `charAt()` > `split("")`

**速查：陣列 vs List**
```java
// Array - Fixed size, primitive/object types
int[] intArray = {0, 1, 2, 3};           // Primitive array
String[] stringArray = {"a", "b", "c"}; // Object array

// List - Dynamic size, object types only
List<Integer> intList = new ArrayList<>();   // Wrapper type required
List<String> stringList = new ArrayList<>(); // Object type
```

### 字元陣列轉 String


- 這樣才能 1) 存取元素 2) 走訪它

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

### 在 char 陣列裡交換元素


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

### 陣列轉 String


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

### 堆疊轉 String


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

## 讀取與切片字串

### 存取 String 裡的元素

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


### Substring 操作


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

**重要**：`substring(start, end)` 用的是 **[start, end)** 區間 — 含頭不含尾。


### 字串轉整數（`Integer.parseInt`）


**關鍵行為**：`Integer.parseInt()` 會自動去掉前導的 0。

```java
// Integer.parseInt handles leading zeros automatically
Integer.parseInt("001");    // 1
Integer.parseInt("00001");  // 1
Integer.parseInt("0100");   // 100
Integer.parseInt("0");      // 0
Integer.parseInt("42");     // 42
```

**常見模式：版本號比較（LC 165）**
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

**關鍵技巧：**
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

### 檢查一個 String 是不是回文

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

### 檢查一個 String 是不是另一個的子序列

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

## 組字串與修改字串

### 替換 String 中某個索引上的字元


```java
// LC 127

String s = "abcd";

char[] arr = s.toCharArray();

arr[0] = 'z';

String newS = new String(arr);

//System.out.println("s =  "  + new String());
```


### 字串字元替換


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


### 反轉 String

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

### 存取 `StringBuilder` 裡的元素


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


### 依索引更新 `StringBuilder` 的值


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


### 從 `StringBuilder` 移除元素

```java
// LC 22
StringBuilder b = new StringBuilder("wefew");
System.out.println(b.toString());
b.deleteCharAt(2);
System.out.println(b.toString());
```

## 排序

### 陣列排序


#### 基本陣列排序
```java
// Primitive arrays - natural order
int[] numbers = {5, 2, 8, 1, 9};
Arrays.sort(numbers);  // [1, 2, 5, 8, 9]

// Object arrays with custom comparator
String[] words = {"apple", "banana", "cherry"};
Arrays.sort(words);                              // Natural order (lexicographic)
Arrays.sort(words, Collections.reverseOrder());  // Reverse order
```

#### 二維陣列排序
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




### 原地排序 vs stream 排序


**關鍵差異**：可變性與效能上的影響

| 方法 | 是否修改原物件 | 效能 | 記憶體用量 | 回傳型別 |
|--------|-------------------|-------------|--------------|-------------|
| `Arrays.sort(arr)` | ✅ **會**（原地） | **較快** | **較低** | `void` |
| `Arrays.stream(arr).sorted()` | ❌ **不會**（會複製一份） | **較慢** | **較高** | `Stream<T>` |

#### 原地排序（推薦）
```java
int[][] intervals = {{15,20}, {0,30}, {5,10}};

// Sorts original array directly
Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
// intervals is now: {{0,30}, {5,10}, {15,20}}
```

#### Stream 排序（函數式風格）
```java
int[][] intervals = {{15,20}, {0,30}, {5,10}};

// Original array unchanged, returns sorted stream
int[][] sorted = Arrays.stream(intervals)
    .sorted((a, b) -> Integer.compare(a[0], b[0]))
    .toArray(int[][]::new);  // Must collect to get array

// Original intervals still: {{15,20}, {0,30}, {5,10}}
// sorted is: {{0,30}, {5,10}, {15,20}}
```


**示範：**
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


### Collections 排序


**核心原則**：
- **`Arrays.sort()`** → 用於陣列（基本型別與物件型別）
- **`Collections.sort()`** → 用於集合（List 等）

#### 陣列排序（物件型別）
```java
Integer[] numbers = {5, 5, 7, 8, 9, 0};

// Ascending order (natural)
Arrays.sort(numbers);

// Descending order - Method 1 (recommended)
Arrays.sort(numbers, Collections.reverseOrder());

// Descending order - Method 2 (custom comparator)
Arrays.sort(numbers, (a, b) -> b - a);
```

#### List 排序
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


#### 複雜物件排序
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

**效能比較：**
```java
// For large datasets
List<Integer> largeList = /* millions of elements */;

// Fastest - in-place sorting
Collections.sort(largeList);  

// Slower - creates new collection
List<Integer> sorted = largeList.stream().sorted().collect(Collectors.toList());
```

### 自訂排序一個 List

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

### 自訂排序 — comparator 回傳值規則 ⭐


> **核心規則**：comparator 回傳值的正負號決定元素順序。

| 回傳值 | 意義 | 效果 |
|---|---|---|
| **負數**（例如 -1） | o1 < o2 | o1 排在 o2 **前面** |
| **正數**（例如 +1） | o1 > o2 | o1 排在 o2 **後面** |
| **0** | o1 == o2 | 順序**不變** |

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

#### Comparator 的心智模型
```text
compare(o1, o2):
  return NEGATIVE  →  keep o1 before o2   (o1 is "smaller")
  return POSITIVE  →  move o1 after  o2   (o1 is "larger")
  return 0         →  no change

Tip: think of it as: "what is o1 - o2?"
  o1 < o2  →  negative  →  ascending order (small first)
  o1 > o2  →  positive  →  o2 goes first  (for descending: flip to o2 - o1)
```

#### 常見模式總整理
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

### 依 HashMap 的 key 與 value 排序



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

### 先依 map key 再依 value 排序


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

### 排序一個 String 裡的字元


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

### 字串的字典序比較


```java
// LC 692. Top K Frequent Words

String a = "abcd";
String b = "defg";

// sort on lexicographical

System.out.println(a.compareTo(b));
```

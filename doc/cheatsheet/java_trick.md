# Java Tricks

# Ref

- [Syntax sugar](https://javaguide.cn/java/basis/syntactic-sugar.html#%E5%86%85%E9%83%A8%E7%B1%BB)

# 0) Basic data structures

- Heap
    - heap (PQ) is `min-heap` default in `java`

- Min-heap
    - LC 703
```java
// java

// V1
// - This creates a min-heap using the natural ordering of Integer objects (i.e., integers will be ordered in ascending order by default).
// - No custom comparator is provided, so Java uses the default comparison of integers, which is already based on their numerical value.
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// V2
// - This also creates a min-heap, but it uses a custom comparator.
PriorityQueue<Integer> minHeap = new PriorityQueue<>(new Comparator<Integer>() {
    @Override
    public int compare(Integer o1, Integer o2) {
        int diff = o1 - o2;
        return diff;
    }
});
```

- Max-heap
    - LC 1046
```java
// java

// V1
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comperator.reverseOrder());

// V2
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(new Comparator<Integer>() {
    @Override
    public int compare(Integer o1, Integer o2) {
        int diff = o2 - o1;
        return diff;
    }
});
```       

- Character
    - https://www.runoob.com/java/java-character.html
    - https://www.runoob.com/java/java-string-charat.html
    - `charAt() ` offer a method a access String element with idx
    ```java
    // java
    String s = "www.google.com";
    char result = s.charAt(6);
    System.out.println(result);

    // LC 647
    // ...
    while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
        l--;
        r++;
        ans++;
    }
    // ...
    ```

# 1) Basic op


### 0-0)
- [Arrays.asList vs new ArrayList()](https://www.baeldung.com/java-arrays-aslist-vs-new-arraylist#:~:text=asList%20method%20returns%20a%20type,the%20add%20and%20remove%20methods.)

- Conclusion:
- `Arrays.asList` : only wrap existing array, `add`, `remove` methods (but has modify method) are NOT implemented
- `new ArrayList` : implement "add", "remove" and "modify" methods, not affect original array -> `preferable`


### 0-0-0-1) Init an array

```java
// java

// LC 973

/** NOTE !!!
 *  
 *  below init an array with (k length, 2 width) (aka k x 2 dimension)
 * 
 */
int[][] ans = new int[k][2];



int k = 4;
int[][] x = new int[k][2];
System.out.println(">>> before " + Arrays.deepToString(x));

x[0] = new int[]{0,1};
x[1] = new int[]{0,2};

System.out.println(">>> after " + Arrays.deepToString(x));

int[] x2 = new int[]{1, 2, 3};
System.out.println(Arrays.toString(x2));  // Output: [1, 2, 3]


// how to print context in 2D array ?

System.out.println(">>> before " + Arrays.deepToString(x));
```


### 0-0-1) Array <--> List

```java
// java
// LC 57

/** 
*  
* 1) Array -> List
*  
*/

Integer [] arr1 = new Integer[]{1,2,3};

/** Arrays.asList */
List<Integer> list1 = Arrays.asList(arr1); // NOTE here !!!


/** 
*  
* 2) List -> Array
*  
*/

List<Integer> list2 = new ArrayList<>();
list2.add(1);
list2.add(2);
list2.add(3);

/** 
 *  NOTE !!! `toArray`
 *    
 *  list2.toArray with size
 * 
 */
Integer [] arr2 = list2.toArray(new Integer[list2.size()]); // NOTE here !!!
```

- https://javaguide.cn/java/collection/java-collection-precautions-for-use.html#%E6%95%B0%E7%BB%84%E8%BD%AC%E9%9B%86%E5%90%88


### 0-0-2) HashMap append to value if key existed/Not existed

```java
// java
// LC 399

HashMap<String, HashMap<String, Double>> gr = new HashMap<>();

for (int i = 0; i < equations.size(); i++) {
    String dividend = equations.get(i).get(0);
    String divisor = equations.get(i).get(1);
    double value = values[i];

    gr.putIfAbsent(dividend, new HashMap<>());
    gr.putIfAbsent(divisor, new HashMap<>());

    gr.get(dividend).put(divisor, value);
    gr.get(divisor).put(dividend, 1.0 / value);
}
```

- NOTE: can use nested map structure (e.g. `HashMap<String, HashMap<String, Double>>`) to store `keyA - valB - resC` info

### 1-0) String to Char array
```java
// java
// LC 844
// V1
String s = "abc";
for (char c: S.toCharArray()) {
    // do sth
    System.out.println("c = " + c);
}

// V2
// LC 49
String strs = "scvsdacvdsa";
char[] array = strs.toCharArray();

// or, can use myStr1.split(""), can access element in string as well 
```

```java
// Java
// Array VS List

// List
List<String> _list = new ArrayList<>(); // this is List (with "List" keyword)


// Array
int[] _array = {0,1,2,3}; // this is Array (without "List" keyword)
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
*    •   .add(curRoot.val) adds the current node's value to the corresponding depth level.
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

### 1-0-4-0) get sub string

```java
// java
// LC 127

String x = "abcd";
/** 
 *  1st idx start from 0
 *  2nd ind start from 1
 *  
 *   -> e.g.  [1st_idx, 2nd_idx]
 * 
 */

System.out.println(x.substring(0,1)); // a
System.out.println(x.substring(0,2)); // ab
System.out.println(x.substring(2,3)); // c
System.out.println(x.substring(2,4)); // cd
//System.out.println(x.substring(2,10)); // `StringIndexOutOfBoundsException`
```


### 1-0-4-1) replace `index = k` element at String

```java
// java
// LC 127

String y = "apple";
List<String> replaces = new ArrayList<>();
replaces.add("1");
replaces.add("2");
replaces.add("3");
replaces.add("4");
replaces.add("5");

for(int i = 0; i < y.length(); i++){
    String y_ = y.substring(0,i) + replaces.get(i) + y.substring(i+1, y.length());
    System.out.println("y_ = " + y_);
    /**
     *  result:
     *
     * y_ = 1pple
     * y_ = a2ple
     * y_ = ap3le
     * y_ = app4e
     * y_ = appl5
     *
     */
}
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

### 1-4) Sort Array (`Arrays.sort`)

```java
// 1) Sort integer Array
// LC 252, LC 452
/// https://leetcode.com/problems/meeting-rooms/editorial/
int[][] intervals = new int[][]{ {15,20}, {0,30}, {5,10} };

Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));


// 2) Sort with 1 attr first, then sort on the other attr
// LC 406
// V1
Arrays.sort(people, (a, b) -> {
int x = Integer.compare(b[0], a[0]);
if(x == 0) return Integer.compare(a[1], b[1]);
else return x; });

// V2
Arrays.sort(people, new Comparator<int[]>() {
@Override
public int compare(int[] o1, int[] o2) {
    // if the heights are equal, compare k-values
    return o1[0] == o2[0] ? o1[1] - o2[1] : o2[0] - o1[0];
}
}); 
```


### 1-4-1) Sort 2D array
```java
// java
// LC 452
Arrays.sort(points, (a, b) -> Integer.compare(a[0], b[0]));
```


### 1-4-2) Arrays.sort VS Arrays.stream(intervals).sorted()

- The two methods Arrays.stream(intervals).sorted() and Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0])) perform different operations:

1) `Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]))`:

- modify the `original` array in place directly (no new array created)
- This sorts the array intervals `in place`.
- The sorting is based on the first element of each sub-array (a[0] and b[0]).
- The Integer.compare(a[0], b[0]) comparator ensures that the array is sorted in ascending order based on the first elements of the sub-arrays.
- After this operation, the `original` array intervals is modified and sorted accordingly.

2) `Arrays.stream(intervals).sorted()`:

- `NOT` modify the `original` array, but create the other `sorted` array
- This creates a stream of the array intervals.
- The .sorted() method sorts the stream in natural order.
- However, this does `NOT` modify the `original` array intervals in place.
- The result of this sorting is a sorted stream, but if you do not collect or process this sorted stream, the original array remains unchanged.


```java
// java
// example code :

int[][] intervals = new int[][]{ {15,20}, {0,30}, {5,10} };
    System.out.println("---> intervals before sorting");
    for (int[] interval : intervals){
        System.out.println("1st = " + interval[0] + ", 2nd = " + interval[1]);
    }

    // Using Arrays.stream(intervals).sorted();
    Arrays.stream(intervals).sorted();
    System.out.println("---> intervals after Arrays.stream(intervals).sorted()");
    for (int[] interval : intervals){
        System.out.println("1st = " + interval[0] + ", 2nd = " + interval[1]);
    }

    // Using Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
    System.out.println("---> intervals after Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]))");
    for (int[] interval : intervals){
        System.out.println("1st = " + interval[0] + ", 2nd = " + interval[1]);
    }
```


### 1-4-3) Sort List VS Array

#### Summary !!!!

- `Arrays.sort`
    - `array` sorting (Primitive Types 基本數據類型)

- `Collections.sort`  (Reference Types 引用類型）
    - `list, HashMap` .. sorting


```java
// java
// LC 214
// https://stackoverflow.com/questions/1694751/java-array-sort-descending

/* -------------------------- */
/** Sort Array */
/* -------------------------- */
Integer[] _array = {5, 5, 7, 8, 9, 0};

// V1
Arrays.sort(_array, Collections.reverseOrder());

// V2
Arrays.sort(_array, (a,b) -> Integer.compare(-a, -b));



/* -------------------------- */
/** Sort List */
/* -------------------------- */

// V1
List<Integer> _list = new ArrayList();
Collections.sort(_list, Collections.reverseOrder());

// V2
Collections.sort(_list, (a,b) -> Integer.compare(-a, -b));

// V3
/** Using the Stream API: 
You can use stream(), sorted(), and collect(Collectors.toList()) to sort the list.
*/
List<Integer> _list2 = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5));
// System.out.println(_list2);
_list2 = _list2.stream()
            .sorted((a, b) -> b - a)  // Using Comparator to sort in reverse order
            .collect(Collectors.toList());
// System.out.println(_list2);


// V3
// LC 731
List<Integer[]> statusList;

statusList.sort((x, y) -> {
  if (!x[0].equals(y[0])) {
      return x[0] - y[0];
  }
  return x[1] - y[1]; // start (+1) comes before end (-1)
});            



// example
Integer[] _array = new Integer[4];
_array[0] = 10;
_array[1] = -2;
_array[2] = 0;
_array[3] = 99;
Arrays.stream(_array).forEach(System.out::println);
// Array sort
Arrays.sort(_array, Collections.reverseOrder());
System.out.println("---");
Arrays.stream(_array).forEach(System.out::println);

System.out.println("--->");

List<Integer> _list = Arrays.asList(1,2,3,4);
_list.forEach(System.out::println);
// List sort
Collections.sort(_list, Collections.reverseOrder());
System.out.println("---");
_list.forEach(System.out::println);
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
public int[][] DIRECTIONS = new int[][]{ {0, 1}, {1, 0}, {-1, 0}, {0, -1} };
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

### 1-19) PQ (priority queue)
```java

// Small PQ (default min-heap)
PriorityQueue<Integer> smallPQ = new PriorityQueue<>();

// Big PQ (max-heap)
PriorityQueue<Integer> bigPQ = new PriorityQueue<>(Comparator.reverseOrder());

// Add elements to PQs
smallPQ.add(5);
smallPQ.add(10);
smallPQ.add(1);

bigPQ.add(5);
bigPQ.add(10);
bigPQ.add(1);

// Print elements from PQs

// small PQ
System.out.println("Small PQ (min-heap):");

while (!smallPQ.isEmpty()) {
    System.out.println(smallPQ.poll());
}

// big PQ
System.out.println("Big PQ (max-heap):");

while (!bigPQ.isEmpty()) {
    System.out.println(bigPQ.poll());
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

### 1-19-2) `add()` VS `offer()` in Queue

- `add(E e)` Method
    - Behavior:
        - Adds the specified element to the queue.
        - If the queue has a capacity restriction and it is `full`, add() throws an `exception` (IllegalStateException).
    - If the queue allows `unlimited` elements (like LinkedList), add() behaves similarly to `offer()`` and does not throw an exception.
    - Usage:
        - Use add() when you want to enforce that the addition must succeed and you want an exception if it fails.
    - Return Type:
        - Returns true if the element was successfully added.
    - Exception Handling:
        - Throws an exception if the operation fails due to capacity limits.


- `offer(E e)` Method
    - Behavior:
        - Adds the specified element to the queue.
        - If the queue has a capacity restriction and it is `full`, `offer() returns false instead of throwing an exception`.
        - It is a more graceful way of adding elements to a queue when capacity may be a concern.
    - Usage:
        - Use offer() when you want to handle the addition failure more gracefully without relying on exceptions.
    - Return Type:
        - Returns true if the element was added successfully, and false otherwise.s
    - Exception Handling:
        - Does not throw an exception if the operation fails; returns false instead.


### 1-19-2) Queue `remove` method

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

Let's say:
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


### 3-3) Loop over `abc...z` char

```java
// LC 127
for (char c = 'a'; c <= 'z'; c++) {
  System.out.println(c);
  // output as below:
  /** a b c d e f g h i j k l m n o p q r s t u v w x y z */
}
```
# Java Tricks

# Ref

- [Syntax sugar](https://javaguide.cn/java/basis/syntactic-sugar.html#%E5%86%85%E9%83%A8%E7%B1%BB)

# 0) Basic data structures

- Java heap:

    - Default : min-heap
        - LC 703
    ```java
    // java
    PriorityQueue<Integer> heap = new PriorityQueue<>();
    ```

    - Define max-heap
        - LC 1046
    ```java
    // java
    PriorityQueue<Integer> heap = new PriorityQueue<>(Comperator.reverseOrder());
    ```       

# 1) Basic op


### 0-0)
- [Arrays.asList vs new ArrayList()](https://www.baeldung.com/java-arrays-aslist-vs-new-arraylist#:~:text=asList%20method%20returns%20a%20type,the%20add%20and%20remove%20methods.)

- Conclusion:
    - Arrays.asList : only wrap existing array, it NOT implements "add", "remove" methods (but has modify method)
    - new ArrayList : implement "add", "remove" and "modify" methods, not affect original array -> `preferable`


### 0-0-1) Array <--> List

```java
// java
// LC 57

int[] newInterval = new int[]{1,2,3};

/** 
 *  
 * 1) Array -> List
 *  
 */
List<int[]> intervalList = new ArrayList<>(Arrays.asList(newInterval));



List<int[]> merged = new ArrayList<>();

/** 
 *  
 * 2) List -> Array
 *  
 */

merged.toArray(new int[merged.size()][]);
```



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
List<List<Integer>> commonCells = new ArrayList<>();

List<int[]> result = new ArrayList<>(); //return value
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


### 1-0-3) Paste value to List with index
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

### 1-0-3) Reverse List
```java
// java
// LC 107

// ...
List<List<Integer>> levels = new ArrayList<List<Integer>>();
Collections.reverse(levels);

// NOTE : reverse != decreasing order
// ...
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

### 1-1-2) Init M X N boolean matrix
```java
// java
// LC 695
// LC 200
public static void main(String[] args) {

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

### 1-2) Char array to String

```java
// java
// LC 345
// https://leetcode.com/problems/reverse-vowels-of-a-string/description/
String s ="abcd";
char[] list=s.toCharArray();
System.out.println(list);
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
return String.valueOf(array);
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

### 1-4) Sort Array

```java
// 1) Sort integer Array
// LC 252, LC 452
/// https://leetcode.com/problems/meeting-rooms/editorial/
intervals = [[0,30],[5,10],[15,20]]
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

### 1-4-2) Sort List VS Array
```java
// java
// LC 214
// https://stackoverflow.com/questions/1694751/java-array-sort-descending

// for Array
Integer[] _array = {5, 5, 7, 8, 9, 0};
Arrays.sort(_array, Collections.reverseOrder());

// for List
List<Integer> _list = new ArrayList();
Collections.sort(_list, Collections.reverseOrder());


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


### 1-5) Get sub array
```java
// java
// LC 976
// https://leetcode.com/problems/largest-perimeter-triangle/description/
nums = [1,2,1,10, 11, 22, 33]
int i = 2;
int[] tmp = Arrays.copyOfRange(nums, i, i+3);
```

### 1-6) Print elements in array
```java
// java
// LC 155
Object[] array = this.queue.toArray();
System.out.println("--> getMin : array = " + Arrays.toString(array));
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

### 1-8) remove element in String
```java
// LC 22
StringBuilder b = new StringBuilder("wefew");
System.out.println(b.toString());
b.deleteCharAt(2);
System.out.println(b.toString());
```

### 1-9) Order HashMap by key
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

### 1-10) Get max val from an Array
```java
// java
// LC 875
// https://stackoverflow.com/questions/1484347/finding-the-max-min-value-in-an-array-of-primitives-using-java
int[] piles = new int[5];
int r = Arrays.stream(piles).max().getAsInt();
```


### 1-11) Sort by Map key, value

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
public int[][] DIRECTIONS = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
```

### 1-18) Arrays.fill

```java
// java
// LC 300

// example 1
int[] dp = new int[10];
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
System.out.println("Small PQ (min-heap):");
while (!smallPQ.isEmpty()) {
    System.out.println(smallPQ.poll());
}

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

# 2) Other tricks

### 2-1) Init var, modify it in another method, and use it
```java
// java
// LC 131
// ...
public List<List<String>> partition_1(String s) {
        /** NOTE : we can init result, pass it to method, modify it, and return as ans */
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
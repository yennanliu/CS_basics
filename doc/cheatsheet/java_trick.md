# Java Tricks

# 0) Basic data structures

- Java heap:
    - LC 703
    - Default : min-heap
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

### 1-0-1) Init an List
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
```

### 1-0-2) Paste value to List with index
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
// ...
```

### 1-0-4) Reverse String
```java
// java
// LC 567
private String reverseString(String input){

    if (input.equals(null) || input.length() == 0){
        return null;
    }

    StringBuilder builder = new StringBuilder(input).reverse();
    return builder.toString();
} 
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
// LC 252
/// https://leetcode.com/problems/meeting-rooms/editorial/
intervals = [[0,30],[5,10],[15,20]]
Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
```

### 1-4-1) Sort 2D array
```java
// java
// LC 452
Arrays.sort(points, (a, b) -> Integer.compare(a[0], b[0]));
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
# Java Tricks

# 0) Basic data structures

# 1) Basic op


### 1-0) String to Char array
```java
// java
// LC 844
String s = "abc";
for (char c: S.toCharArray()) {
        // do sth
        System.out.println("c = " + c);
    }
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
// https://youtu.be/xOppee_iSvo?t=206
Integer[] data = {5, 5, 7, 8, 9, 0};
Arrays.toString(data);   
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

### 1-5) Get sub array
```java
// java
// LC 976
// https://leetcode.com/problems/largest-perimeter-triangle/description/
nums = [1,2,1,10, 11, 22, 33]
int i = 2;
int[] tmp = Arrays.copyOfRange(nums, i, i+3);
```
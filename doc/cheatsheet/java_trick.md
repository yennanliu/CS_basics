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
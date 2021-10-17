# Sliding window 
- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E6%BB%91%E5%8A%A8%E7%AA%97%E5%8F%A3%E6%8A%80%E5%B7%A7.md

## 0) Concept  
1. two pointers
2. while loop
3. some conditions for start and end

### 0-1) Types

### 0-2) Pattern
```java
// java
int left = 0, right = 0;

while (right < s.size()) {
    window.add(s[right]);
    right++;
    // NOTE : must of the trick is dealing with "valid" conditions
    //        and how to cache some conditions for verfication
    while (valid) {
        window.remove(s[left]);
        left++;
    }
}
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example
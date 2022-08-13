# Recursion

For a problem, if there exists a recursive solution, we can follow the guidelines below to implement it. 

```
For instance, we define the problem as the function F(X) to implement, where X is the input of the function which also defines the scope of the problem.

Then, in the function F(X), we will:

1. Break the problem down into smaller scopes, such as x0 belongs X, x0 belongs X ..., xn belogns X 
2. Call function F(x_0), F(x_1), ..., F(x_n) recursively to solve the subproblems of X;
3. Finally, process the results from the recursive function calls to solve the problem corresponding to X.
```

## 0) Concept

- Same concept is used in
	- DFS
	- backtrack
	- tree

- Optimization
    - Memoization
        - https://leetcode.com/explore/learn/card/recursion-i/255/recursion-memoization/1495/
        - use a cache save "already calculated" result, so when same request comes again, return cache directly. `hash map` is a good candidate for cache implementation.
        - example : fibonacci number
        ```python
        # V1 : without Memoization (cache):
        def fibonacci(n):
            """
            :type n: int
            :rtype: int
            """
            if n < 2:
                return n
            else:
                return fibonacci(n-1) + fibonacci(n-2)

        # V2 : with Memoization (cache):
        def fibonacci(n):
            cache = {}
            def help(n):
                if n in cache:
                    return cache[n]
                if n < 2:
                    res = n
                else:
                    res = help(n-1) + help(n-2)
                cache[n] = res
                return res
            return help(n)
        ```

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Symmetric Tree
```python
# LC 101. Symmetric Tree
# V0
# IDEA : Recursive
class Solution(object):
    def isSymmetric(self, root):
        if not root:
            return True
        return self.mirror(root.left, root.right)

    def mirror(self, left, right):
        if not left or not right:
            return left == right
        if left.val != right.val:
            return False
        return self.mirror(left.left, right.right) and self.mirror(left.right, right.left)
```

### 2-2) One Edit Distance
```python
# LC 161. One Edit Distance
# V0
# IDER : RECURSION
class Solution:
    def isOneEditDistance(self, s, t):
        m = len(s)
        n = len(t)
        if abs(m - n) > 1:
            return False
        if m > n:
            return self.isOneEditDistance(t, s)
        for i in range(m):
            if s[i] != t[i]:
                # case 1
                if m == n:
                    return s[i + 1:] == t[i + 1:]
                # case 2
                return s[i:] == t[i + 1:]
        return m != n # double check this condition
```
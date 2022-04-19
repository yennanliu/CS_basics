# Recursion

## 0) Concept
- Same concept is used in
	- DFS
	- backtrack
	- tree 

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
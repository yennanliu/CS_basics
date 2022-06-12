# Set

## 0) Concept
- Types
	- HashSet
	- LinkedHashSet
	- TreeSet

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Lowest Common Ancestor of a Binary Tree III
```python
# LC 1650. Lowest Common Ancestor of a Binary Tree III
# NOTE : there are also dict, recursive.. approaches

# V0''
# IDEA : set
class Solution:
    def lowestCommonAncestor(self, p, q):
        visited = set()
        while p:
            visited.add(p)
            p = p.parent
        while q:
            if q in visited:
                return q
            q = q.parent
```

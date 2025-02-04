# Recursion

For a problem, if there exists a recursive solution, we can follow the guidelines below to implement it. 

```
For instance, we define the problem as the function F(X) to implement, where X is the input of the function which also defines the scope of the problem.

Then, in the function F(X), we will:

1. Break the problem down into smaller scopes, such as x0 belongs X, x0 belongs X ..., xn belongs X 
2. Call function F(x_0), F(x_1), ..., F(x_n) recursively to solve the subproblems of X;
3. Finally, process the results from the recursive function calls to solve the problem corresponding to X.
```

- Tips
    - When in doubt, write down the `recurrence relationship`
    - Whenever possible, apply `memoization`
    - When stack overflows, `tail recursion` might come to help 
        - https://leetcode.com/explore/learn/card/recursion-i/253/conclusion/1650/
- Ref
    - https://medium.com/appworks-school/%E9%80%B2%E5%85%A5%E9%81%9E%E8%BF%B4-recursion-%E7%9A%84%E4%B8%96%E7%95%8C-%E4%B8%80-59fa4b394ef6
    - https://labuladong.online/algo/practice-in-action/two-views-of-backtrack/#%E6%9A%B4%E5%8A%9B%E7%A9%B7%E4%B8%BE%E6%80%9D%E7%BB%B4%E6%96%B9%E6%B3%95-%E7%90%83%E7%9B%92%E6%A8%A1%E5%9E%8B

- Time complexity
    - think as `tree` structure
```
        fib(5)
       /      \
    fib(4)    fib(3)
    /    \     /    \
fib(3)  fib(2) fib(2) fib(1)
 /   \    /  \   /  \
fib(2) fib(1) fib(1) fib(0)
 /   \
fib(1) fib(0)
```

## 0) Concept

- Same concept is used in
	- DFS
	- backtrack
	- tree

- Complexity analysis
    - Time complexity
        - https://leetcode.com/explore/learn/card/recursion-i/256/complexity-analysis/1669/
        - Given a recursion algorithm, its time complexity O(T) is typically the product of `the number of recursion invocations `(denoted as R) and `the time complexity of calculation` (denoted as O(s)) that incurs along with each recursion call: `O(T) = R * O(S)`
    - Space complexity
        - https://leetcode.com/explore/learn/card/recursion-i/256/complexity-analysis/1671/
        - Recursion `Related` Space
            - stack
                - local variables (used in recursive func calls)
                - input param
                - output variables
                - "stackoverflow"
                    - where the stack allocated for a program reaches its maximum space limit and the program crashes. 
        - Recursion `Non-Related` Space
            - heap
                - global variables (call before func, can be used by all funcs)
                - memoization (keep track all intermediate results)
                - NOTE : it's important to consider memoization space usage when use memoization in code

- Optimization
    - Memoization
        - https://leetcode.com/explore/learn/card/recursion-i/255/recursion-memoization/1495/
        - use a cache save "already calculated" result, so when same request comes again, return cache directly. `hash map` is a good candidate for cache implementation.
        - Example 1 : fibonacci number
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
        - Example 2 : Climbing Stairs
        ```python
        # 070  Climbing Stairs
        # V0 : without MEMORIZATION
        class Solution:
            # Time:  O(2^n)
            # Space: O(n)
            def climbStairs(self, n):
                if n == 1:
                    return 1
                if n == 2:
                    return 2
                return self.climbStairs(n - 1) + self.climbStairs(n - 2)
        # V0' :  RECURSION + MEMORIZATION
        # https://leetcode.com/explore/learn/card/recursion-i/255/recursion-memoization/1662/
        class Solution(object):
            def climbStairs(self, n):
                cache = {}
                def help(n):
                    if n in cache:
                        return cache[n]
                    if n <= 2:
                        res = n
                    else:
                        res = help(n-2) + help(n-1)
                    cache[n] = res
                    return res
                return help(n)
        ```

- Divide & Conquer
    - https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2869/

```
# template

1. Divide. Divide the problem S into a set of subproblems: {S1, S2, ... Sn} where n >= 2. i.e. there are usually more than one subproblem.

2. Conquer. Solve each subproblem recursively. 

3. Combine. Combine the results of each subproblem.
```

```python
# pseudo code
def divide_and_conquer( S ):
    # (1). Divide the problem into a set of subproblems.
    [S1, S2, ... Sn] = divide(S)

    # (2). Solve the subproblem recursively,
    #   obtain the results of subproblems as [R1, R2... Rn].
    rets = [divide_and_conquer(Si) for Si in [S1, S2, ... Sn]]
    [R1, R2,... Rn] = rets

    # (3). combine the results from the subproblems.
    #   and return the combined result.
    return combine([R1, R2,... Rn])
```

- Recursion to iteration (Unfold Recursion)
    - Reason
        - Risk of Stackoverflow
        - Efficiency
        - Complexity
    - Tips
        - The good news is that we can always convert a recursion to iteration. In order to do so, in general, we use a data structure of `stack or queue`, which replaces the role of the system call stack during the process of recursion.
    - Steps
        - Step 1: We use a stack or queue data structure within the function, to replace the role of the system call stack. At each occurrence of recursion, we simply push the parameters as a new element into the data structure that we created, instead of invoking a recursion.
        - Step 2: In addition, we create a loop over the data structure that we created before. The chain invocation of recursion would then be replaced with the iteration within the loop.
    - LC
        - 100
 
    - Ref
        - https://leetcode.com/explore/learn/card/recursion-ii/503/recursion-to-iteration/2693/


### 0-1) Types

- Basics
- Divide and Conquer
    - know some classical examples of divide-and-conquer algorithms, e.g. `merge sort` and `quick sort`.
    - know how to apply a pseudocode template to implement the divide-and-conquer algorithms.
    - know a theoretical tool called master theorem to calculate the time complexity for certain types of divide-and-conquer algorithms.
    - Steps
        - Divide -> Conquer -> Combine
    - LC 22
    - LC 84
    - LC 315
    - LC 327
    - LC 493
    - LC 1649
    - LC 426
- Backtracking
- master theorem
- Recursively and call the other recursion function
    - LC 572

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

- Endless for loop elment in list
```python
# LC 022
# ...
_list = ["(", ")"]
for x in _list:
    _tmp = tmp + x
    help(_tmp)
# ...
```

### 1-2) Top down Recursion
```
# LC 110
```

### 1-3) Bottom up Recursion
```
# LC 110
```

### 1-4) Pass previous status to next recursion
```java
// java
// LC 404
// V1
// https://leetcode.com/problems/sum-of-left-leaves/editorial/
// IDEA : Recursive Tree Traversal (Pre-order)
// NOTE!!! we can pass previous status as param to the method (isLeft)
private int processSubtree_2(TreeNode subtree, boolean isLeft) {

    // Base case: This is an empty subtree.
    if (subtree == null) {
        return 0;
    }

    // Base case: This is a leaf node.
    if (subtree.left == null && subtree.right == null) {
        if (isLeft){
            return subtree.val;
        }else{
            return 0;
        }
    }

    // Recursive case: We need to add and return the results of the
    // left and right subtrees.
    return processSubtree_2(subtree.left, true) + processSubtree_2(subtree.right, false);
}
```

### 1-5) `any` true status in recursion call

```java
// LC 572
// java
    public boolean isSubtree_0_1(TreeNode s, TreeNode t) {
        // ...
        /**
         * NOTE !!! isSameTree and isSubtree with sub left, sub right tree
         *
         * e.g.
         *   isSubtree(s.left, t)
         *   isSubtree(s.right, t)
         *
         *   -> only take sub tree on s (root), but use the same t (sub root)
         *
         *
         *  NOTE !!!
         *   -> use "OR", so any `true` status can be found and return
         */
        return isSameTree(s, t) || isSubtree_0_1(s.left, t) || isSubtree_0_1(s.right, t);
    }
```

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

### 2-3) Merge Two Sorted Lists
```python
# LC 021 Merge Two Sorted Lists
# NOTE : there is also iteration solution

# V0''
# IDEA : RECURSION
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        if not l1 or not l2:
            return l1 or l2
        if l1.val < l2.val:
            l1.next = self.mergeTwoLists(l1.next, l2)
            return l1
        else:
            l2.next = self.mergeTwoLists(l1, l2.next)
            return l2
```

### 2-4) Subtree of Another Tree
```python
# LC 572 Subtree of Another Tree

# V0
# IDEA : BFS + DFS
# bfs
class Solution(object):
    def isSubtree(self, root, subRoot):
        
        # dfs
        # IDEA : LC 100 Same tree
        def check(p, q):
            if (not p and not q):
                return True
            elif (not p and q) or (p and not q):
                return False
            elif (p.left and not q.left) or (p.right and not q.right):
                return False
            elif (not p.left and q.left) or (not p.right and q.right):
                return False
            return p.val == q.val and check(p.left, q.left) and check(p.right, q.right)
        
        # bfs
        if (not root and subRoot) or (root and not subRoot):
            return False
        q = [root]
        cache = []
        while q:
            for i in range(len(q)):
                tmp = q.pop(0)
                if tmp.val == subRoot.val:
                    ### NOTE : here we don't return res
                    #          -> since we may have `root = [1,1], subRoot = [1]` case
                    #          -> so we have a cache, collect all possible res
                    #          -> then check if there is "True" in cache
                    res = check(tmp, subRoot)
                    cache.append(res)
                    #return res
                if tmp.left:
                    q.append(tmp.left)
                if tmp.right:
                    q.append(tmp.right)
        #print ("cache = " + str(cache))
        # check if there is "True" in cache
        return True in cache

# V0'
# IDEA : DFS + DFS (LC 100 Same tree)
class Solution(object):
    def isSubtree(self, root, subRoot):
        # IDEA : LC 100 Same tree
        def isSameTree(p, q):
            if not p and not q:
                return True
            if (not p and q) or (p and not q):
                return False
            if p.val != q.val:
                return False
            return isSameTree(p.left, q.left) and isSameTree(p.right, q.right)
        def help(root, subRoot):
            # edge case
            if not root and subRoot:
                return False
            if not root and not subRoot:
                return True
            # use isSameTree
            if isSameTree(root, subRoot):
                #return True
                res.append(True)
            if root.left:
                help(root.left, subRoot)
            if root.right:
                help(root.right, subRoot)
        res = []
        help(root, subRoot)
        #print ("res = " + str(res))
        return True in res

# V0' 
# IDEA : DFS + DFS 
class Solution(object):
    def isSubtree(self, s, t):
        if not s and not t:
            return True
        if not s or not t:
            return False
        return self.isSameTree(s, t) or self.isSubtree(s.left, t) or self.isSubtree(s.right, t)
        
    def isSameTree(self, s, t):
        if not s and not t:
            return True
        if not s or not t:
            return False
        return s.val == t.val and self.isSameTree(s.left, t.left) and self.isSameTree(s.right, t.right)
```

```java
// java
// LC 572
 public boolean isSubtree(TreeNode root, TreeNode subRoot) {

        // If this node is Empty, then no tree is rooted at this Node
        // Hence, "tree rooted at node" cannot be equal to "tree rooted at subRoot"
        // "tree rooted at subRoot" will always be non-empty, as per constraints
        if (root == null) {
            return false;
        }

        // Check if the "tree rooted at root" is identical to "tree roooted at subRoot"
        if (isIdentical(root, subRoot)) {
            return true;
        }

        // If not, check for "tree rooted at root.left" and "tree rooted at root.right"
        // If either of them returns true, return true
        // NOTE !!! either left or right tree equals subRoot is acceptable
        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    /** NOTE !!! check this help func */
    private boolean isIdentical(TreeNode node1, TreeNode node2) {

        // If any of the nodes is null, then both must be null
        if (node1 == null || node2 == null) {
            return node1 == null && node2 == null;
        }

        // If both nodes are non-empty, then they are identical only if
        // 1. Their values are equal
        // 2. Their left subtrees are identical
        // 3. Their right subtrees are identical
        return node1.val == node2.val && isIdentical(node1.left, node2.left) && isIdentical(node1.right, node2.right);
    }
```
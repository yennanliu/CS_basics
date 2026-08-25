# DFS — Worked Examples

> **Scope** — The worked-solution archive for [dfs.md](./dfs.md): one canonical solution per problem for the DFS problems the core templates cover, plus the pattern-and-difficulty index of the whole DFS problem set.
> **See also** — *parent sheet*: [dfs.md](./dfs.md) — the ten core templates and the pattern-selection flowchart, which is where the *technique* is explained; [dfs_advanced.md](./dfs_advanced.md) — the rare/hard DFS templates and their examples.
> *Neighbouring sheets*: [tree.md](./tree.md), [tree2.md](./tree2.md) and [bst.md](./bst.md) own most of the tree problems repeated here; [bfs.md](./bfs.md) — the breadth-first solution to several of the same grid problems; [backtrack.md](./backtrack.md), [union_find.md](./union_find.md) — the alternative engines mentioned in the notes.

## LeetCode Problem Lists

- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)

## Overview
This file holds the long tail of DFS solutions. It teaches nothing on its own — every entry is an
instance of a template in [dfs.md](./dfs.md), and the *why* lives there. Use it to check a solution,
to compare two spellings of the same recursion, or to pick the next problem from the
[Problems by Pattern](#problems-by-pattern) index at the bottom.

### Key Properties
- **One canonical solution per problem per language.** A second block appears only where the note
  above it says what the variant teaches that the first one does not.
- **Complexity**: per template — see the
  [Template Comparison Table in dfs.md](./dfs.md#template-comparison-table).
- **Problems the templates already solve** (LC 200 flood fill, LC 694 signatures, LC 1254 two-pass,
  LC 1219 backtracking, LC 399 ratio queries) are **not** repeated here — they are worked in place in
  [dfs.md](./dfs.md).

## LC Examples

### 0) Basic Operations

Small self-contained recursions worth being able to write from memory.

#### 0-1) DFS traversal form (act, then recurse by comparison)
```python
# python
# form I : tree transversal
def dfs(root, target):

    if root.val == target:
       # do sth

    if root.val < target:
       dfs(root.left, target)
       # do sth

    if root.val > target:
       dfs(root.right, target)
       # do sth
```

#### 0-2) Add 1 to all node.value in Binary tree?
```python
# Example) Add 1 to all node.value in Binary tree?
def dfs(root):
    if not root:
        return 
    root.val += 1 
    dfs(root.left)
    dfs(root.right)
```

#### 0-3) check if 2 Binary tree are the same
```python
# Example) check if 2 Binary tree are the same ? 
def dfs(root1, root2):
    if root1 == root2 == None:
        return True 
    if root1 is not None and root2 is None:
        return False 
    if root1 is None and root2 is not None:
        return False 
    else:
        if root1.val != root2.value:
            return False 
    return dfs(root1.left, root2.left) \
           and dfs(root1.right, root2.right)
```

#### 0-4) check if a value exist in the BST
```python
# Example) check if a value exist in the BST
def dfs(root, value):
    if not root:
        return False
    if root.val == value:
        return True
    return dfs(root.left, value) or dfs(root.right, value)

# optimized : BST prpoerty :  root.right > root.val > root.left
def dfs(root, value):
    if not root:
        return False
    if root.val == value:
        return True
    if root.val > value:
        return dfs(root.left, value) 
    if root.val < value:
        return dfs(root.right, value)
```

#### 0-5) get sum of sub tree

```python
# get sum of sub tree
# LC 508 Most Frequent Subtree Sum
def get_sum(root):
    if not root:
        return 0
    ### NOTE THIS !!!
    #  -> we need to do get_sum(root.left), get_sum(root.right) on the same time
    s = get_sum(root.left) + root.val + get_sum(root.right)
    res.append(s)
    return s
```

#### 0-6) get `aggregated sum` for every node in tree
```python
# LC 663 Equal Tree Partition
# LC 508 Most Frequent Subtree Sum
seen = []
def _sum(root):
    if not root:
        return 0
    seen.append( root.val + _sum(root.left) + _sum(root.right) )
```

#### 0-7) Convert BST to Greater Tree
```python
# Convert BST to Greater Tree 
# LC 538
_sum = 0
def dfs(root):
    dfs(root.right)
    _sum += root.val
    root.val = _sum
    dfs(root.left)
```

#### 0-8) Serialize and Deserialize Binary Tree

> Python version: [2-20) LC 297](#2-20-serialize-and-deserialize-binary-tree--lc-297) below.

```java
// java
// LC 297
public class Codec{
    public String serialize(TreeNode root) {

        /** NOTE !!!
         *
         *     if root == null, return "#"
         */
        if (root == null){
            return "#";
        }

        /** NOTE !!! return result via pre-order, split with "," */
        return root.val + "," + serialize(root.left) + "," + serialize(root.right);
    }

    public TreeNode deserialize(String data) {

        /** NOTE !!!
         *
         *   1) init queue and append serialize output
         *   2) even use queue, but helper func still using DFS
         */
        Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
        return helper(queue);
    }

    private TreeNode helper(Queue<String> queue) {

        // get val from queue first
        String s = queue.poll();

        if (s.equals("#")){
            return null;
        }
        /** NOTE !!! init current node  */
        TreeNode root = new TreeNode(Integer.valueOf(s));
        /** NOTE !!!
         *
         *    since serialize is "pre-order",
         *    deserialize we use "pre-order" as well
         *    e.g. root -> left sub tree -> right sub tree
         *    -> so we get sub tree via below :
         *
         *       root.left = helper(queue);
         *       root.right = helper(queue);
         *
         */
        root.left = helper(queue);
        root.right = helper(queue);
        /** NOTE !!! don't forget to return final deserialize result  */
        return root;
    }
}
```

#### 0-9) Serialize and Deserialize BST
```python
# LC 449. Serialize and Deserialize BST
# please check below 2) LC Example
# NOTE : there is also a bfs approach
# V1'
# IDEA : BST property
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/212043/Python-solution
class Codec:

    def serialize(self, root):
        """Encodes a tree to a single string.
        
        :type root: TreeNode
        :rtype: str
        """
        def dfs(root):
            if not root:
                return 
            res.append(str(root.val) + ",")
            dfs(root.left)
            dfs(root.right)
            
        res = []
        dfs(root)
        return "".join(res)

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        
        :type data: str
        :rtype: TreeNode
        """
        lst = data.split(",")
        lst.pop()
        stack = []
        head = None
        for n in lst:
            n = int(n)
            if not head:
                head = TreeNode(n)
                stack.append(head)
            else:
                node = TreeNode(n)
                if n < stack[-1].val:
                    stack[-1].left = node
                else:
                    while stack and stack[-1].val < n: 
                        u = stack.pop()
                    u.right = node
                stack.append(node)
        return head
```

#### 0-10) find longest distance between nodes
```java
// java
// LC 543 Diameter of Binary Tree
// V1
// IDEA : DFS
// https://leetcode.com/problems/diameter-of-binary-tree/editorial/

int diameter;

public int diameterOfBinaryTree_2(TreeNode root) {
    diameter = 0;
    longestPath(root);
    return diameter;
}
private int longestPath(TreeNode node){
    if(node == null) return 0;
    // recursively find the longest path in
    // both left child and right child
    int leftPath = longestPath(node.left);
    int rightPath = longestPath(node.right);

    // update the diameter if left_path plus right_path is larger
    diameter = Math.max(diameter, leftPath + rightPath);

    // return the longest one between left_path and right_path;
    // remember to add 1 for the path connecting the node and its parent
    return Math.max(leftPath, rightPath) + 1;
}
```

#### 0-11) Compare node val with path
```java
// java
// LC 1448

private void dfsCheckGoodNode(TreeNode node, int maxSoFar) {
    if (node == null)
        return;

    // Check if the current node is good
    if (node.val >= maxSoFar) {
        res++;
        maxSoFar = node.val; // Update max value seen so far
    }

    // Recur for left and right children
    dfsCheckGoodNode(node.left, maxSoFar);
    dfsCheckGoodNode(node.right, maxSoFar);
}
```

#### 0-12) Grid DFS with a `visited` set
```python
def grid_dfs(grid, x, y, visited):
    if x < 0 or x >= len(grid) or y < 0 or y >= len(grid[0]):
        return
    if (x, y) in visited or grid[x][y] == 0:
        return
    
    visited.add((x, y))
    
    # 4-directional movement
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
    for dx, dy in directions:
        grid_dfs(grid, x + dx, y + dy, visited)
```

#### 0-13) Closure: reading outer-scope variables inside a nested `dfs`
```python
# we don't need to declare y,z in func, but we can use them in the func directly
# and can get the returned value as well, this trick is being used a lot in the dfs
def test():
    def func(x):
        print ("x = " + str(x) + " y = " + str(y))
        for i in range(3):
            z.append(i)

    x = 0
    y = 100
    z = []
    func(x)
test()
print (z)
```

### 2-1) Validate Binary Search Tree — LC 98
```python
# 098 Validate Binary Search Tree
### NOTE : there is also bfs solution
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Recursion/validate-binary-search-tree.py
class Solution(object):
    def isValidBST(self, root):
        return self.valid(root, float('-inf'), float('inf'))
        
    def valid(self, root, min_, max_):
        if not root: return True
        if root.val >= max_ or root.val <= min_:
            return False
        return self.valid(root.left, min_, root.val) and self.valid(root.right, root.val, max_)
```

### 2-2) Insert into a Binary Search Tree — LC 701

```java
// java
// LC 701

public TreeNode insertIntoBST_0_1(TreeNode root, int val) {
    if (root == null) {
        return new TreeNode(val);
    }

    /** 
     *  NOTE !!! 
     *  
     *   via below, we can still `MODIFY root value`,
     *   even it's not declared as a global variable
     *   
     *   -> e.g. we have root as input,
     *      within `insertNodeHelper` method,
     *      we append `new sub tree` to root as its left, right sub tree
     *
     */
    insertNodeHelper(root, val); // helper modifies the tree in-place
    return root;
}

public void insertNodeHelper(TreeNode root, int val) {
    if (val < root.val) {
        if (root.left == null) {
            root.left = new TreeNode(val);
        } else {
            /** NOTE !!!
             * 
             *  no need to return val,
             *  since we `append sub tree` to root directly
             *  in the method (e.g. root.left == ..., root.right = ...)
             */
            insertNodeHelper(root.left, val);
        }
    } else {
        if (root.right == null) {
            root.right = new TreeNode(val);
        } else {
            insertNodeHelper(root.right, val);
        }
    }
}
```
```python
# python
# 701 Insert into a Binary Search Tree
class Solution(object):
    def insertIntoBST(self, root, val):
        """
        NOTE !!!
            1) we ALWAYS do op first, then do recursive
                -> e.g.
                        ...
                        if not root: 
                            return TreeNode(val)
                        if root.val < val:
                            root.right = self.insertIntoBST(root.right, val)
                        ...
        """
        if not root: 
            return TreeNode(val)

        if root.val < val: 
            root.right = self.insertIntoBST(root.right, val)

        elif root.val > val: 
            root.left = self.insertIntoBST(root.left, val)

        return root
```

### 2-3) Delete Node in a BST — LC 450
```python
# 450 Delete Node in a BST
# V0
# IDEA : RECURSION + BST PROPERTY
#### 2 CASES :
#   -> CASE 1 : root.val == key and NO right subtree 
#                -> swap root and root.left, return root.left
#   -> CASE 2 : root.val == key and THERE IS right subtree
#                -> 1) go to 1st RIGHT sub tree
#                -> 2) iterate to deepest LEFT subtree
#                -> 3) swap root and  `deepest LEFT subtree` then return root
class Solution(object):
    def deleteNode(self, root, key):
        if not root: return None
        if root.val == key:
            # case 1 : NO right subtree 
            if not root.right:
                left = root.left
                return left
            # case 2 : THERE IS right subtree
            else:
                ### NOTE : find min in "right" sub-tree
                #           -> because BST property, we ONLY go to 1st right tree (make sure we find the min of right sub-tree)
                #           -> then go to deepest left sub-tree
                right = root.right
                while right.left:
                    right = right.left
                ### NOTE : we need to swap root, right ON THE SAME TIME
                root.val, right.val = right.val, root.val
        root.left = self.deleteNode(root.left, key)
        root.right = self.deleteNode(root.right, key)
        return root
```

```java
// java
// LC 450
// V0
// IDEA: DFS + BST property
/**
 *
 * (when found a node to delete)
 *
 *    // Case 1: No children
 *
 *    // Case 2: One child
 *
 *    // Case 3: Two children
 *
 */
/**
 *
 *  Summary of Deletion Strategy:
 *
 *
 *  | Case         | Description        | What Happens                                  |
 * |--------------|--------------------|-----------------------------------------------|
 * | Leaf         | No children         | Return `null`                                 |
 * | One Child    | One child           | Replace node with its child                   |
 * | Two Children | Both children       | Replace with in-order successor, then delete the successor |
 *
 *
 *  `in-order successor`:  Left → root → Right
 */

public TreeNode deleteNode(TreeNode root, int key) {
    return deleteNodeHelper_0(root, key);
}

private TreeNode deleteNodeHelper_0(TreeNode root, int key) {
    if (root == null) {
        return null;
    }

    /**
     * CASE 1)  NOT found a node to delete
     */
    if (key < root.val) {
        // search in left subtree
        /**
         *  NOTE !!!
         *
         *   we assign `left sub tree` as res from deleteNodeHelper_0(root.left, key)
         *
         *   -> NOT return `deleteNodeHelper_0(root.left, key)`
         *      as res directly, since it deleteNodeHelper_0
         *      could NOT be a null val, we need it to assign root.left,
         *      so we can keep `whole BST info`
         */
        root.left = deleteNodeHelper_0(root.left, key);
    } else if (key > root.val) {
        // search in right subtree
        /**
         *  NOTE !!!
         *
         *   we assign `right sub tree` as res from deleteNodeHelper_0(root.right, key)
         */
        root.right = deleteNodeHelper_0(root.right, key);
    }
    /**
     * CASE 2)  Found a node to delete
     */
    else {
        // Case 1: No left child
        if (root.left == null) {
            return root.right;
        }

        // Case 2: No right child
        if (root.right == null) {
            return root.left;
        }

        /**
         *  NOTE !!!! below
         *
         *  step 1) find `min` val  (`sub right tree`)
         *  step 2) set root val as min val
         *  step 3)  delete the `min` val node from sub right tree
         *             - `recursively` call `deleteNodeHelper`
         *
         */
        // Case 3: Two children → find inorder successor
        /**
         *  NOTE !!!
         *
         *   we need to find a `min` tree from `sub right tree`
         *   as a node to `swap` with current node.
         *
         *   Reason:
         *      since it is a BST, so  `left < root < right`.
         *      so after swapping `min` from sub right tree.
         *      with current node
         *          -> the tree `remains` BST.
         *          we DON'T have to do any further modification.
         *
         */
        TreeNode minNode = findMin_0(root.right);
        root.val = minNode.val; // copy value
        root.right = deleteNodeHelper(root.right, minNode.val); // delete successor
    }

    return root;
}

private TreeNode findMin_0(TreeNode node) {
    while (node.left != null) {
        node = node.left;
    }
    return node;
}
```

### 2-4) Find Duplicate Subtrees — LC 652
```python
# form IV : check if duplicated SUBTREES in tree
# LC 652 Find Duplicate Subtrees
# python
m = collections.defaultdict(int)   # { subtree_signature : count }
def dfs(root, m, res):
    if not root:
        return "#"                  # null marker -> makes signature unambiguous

    ### NOTE : serialize CURRENT subtree (post-order) -> use signature as hash key
    # str(root.val) avoids int+str TypeError; "#" + commas avoid ambiguity (e.g. 1,12 vs 11,2)
    path = str(root.val) + "," + dfs(root.left, m, res) + "," + dfs(root.right, m, res)

    if m[path] == 1:                # seen exactly once before -> this is the 2nd time -> duplicate
        res.append(root)            # collect the ROOT NODE (not the path string)

    m[path] += 1
    return path                     # return signature so PARENT can build its own signature
```

#### ⭐ LC 652 — Find Duplicate Subtrees (deep dive)

> "I think this is a tree *path* problem?" — **No.** A path problem (LC 112 / 113 / 257)
> tracks a *root → leaf* line of nodes. LC 652 instead asks whether two **whole subtrees**
> are structurally identical. The trick is to give every subtree a **canonical signature**
> and let a hashmap count how many times each signature appears. It belongs to
> **Pattern 8 (Path Signatures / Shape Encoding)** — the tree analogue of "distinct islands".

**1) Core Idea**

- **Post-order serialization**: a subtree is fully described by `val + signature(left) + signature(right)`.
  Children must be encoded *before* the parent → **post-order DFS** (bottom-up).
- **Hashmap counting**: identical subtrees produce identical signature strings.
  Increment a counter per signature; when it first hits **2**, that subtree is a duplicate.
- **Append `root`, append once**: collect the node the **second** time a signature appears
  (using `if count == 1` *before* incrementing, or `if count == 2` *after*) so each duplicate
  kind is reported exactly once — even if it occurs 3+ times.

**2) Pattern / Recognition**

| Signal | What it tells you |
|--------|-------------------|
| "duplicate / identical **subtrees**", "same structure & values" | serialize + hashmap |
| need to compare *whole subtrees*, not a single root→leaf line | NOT a path problem |
| answer is built bottom-up from children | **post-order** DFS |
| need a delimiter (`,`) + null marker (`#`) | avoid signature ambiguity |

```text
Encoding rules (why each piece matters):
  "#"   -> null child       (distinguishes shapes: a node w/ 1 child vs 2)
  ","   -> field delimiter  (so vals "1,12" never collide with "11,2")
  post-order -> children serialized first, parent reuses their result
Complexity: O(n) nodes, but each signature is O(n) long -> O(n^2) time / space worst case.
  (Use an int-id map instead of raw strings to get true O(n) — see V2 in the .py file.)
```

**3) Similar LC**

| LC | Problem | Relation |
|----|---------|----------|
| 652 | Find Duplicate Subtrees | this problem — subtree signature + count |
| 694 | Number of Distinct Islands | grid analogue — encode shape, dedupe via `set` |
| 449 | Serialize / Deserialize BST | same serialization idea, encode→decode |
| 297 | Serialize / Deserialize Binary Tree | canonical (pre/post-order + `#`) encoding |
| 572 | Subtree of Another Tree | match one subtree (can also use signature compare) |
| 508 | Most Frequent Subtree Sum | bottom-up subtree aggregate + hashmap count |
| 1948 | Delete Duplicate Folders in System | generalizes 652 — serialize subtrees, mark duplicates |

### 2-5) Trim a BST — LC 669
```python
# python
# 669 Trim a Binary Search Tree
class Solution:
    def trimBST(self, root, L, R):
        if not root:
            return 
        # NOTICE HERE 
        # SINCE IT'S BST
        # SO if root.val < L, THE root.right MUST LARGER THAN L
        # SO USE self.trimBST(root.right, L, R) TO FIND THE NEXT "VALIDATE" ROOT AFTER TRIM
        # THE REASON USE self.trimBST(root.right, L, R) IS THAT MAYBE NEXT ROOT IS TRIMMED AS WELL, SO KEEP FINDING VIA RECURSION
        if root.val < L:
            return self.trimBST(root.right, L, R)
        # NOTICE HERE 
        # SINCE IT'S BST
        # SO if root.val > R, THE root.left MUST SMALLER THAN R
        # SO USE self.trimBST(root.left, L, R) TO FIND THE NEXT "VALIDATE" ROOT AFTER TRIM
        if root.val > R:
            return self.trimBST(root.left, L, R)
        root.left = self.trimBST(root.left, L, R)
        root.right = self.trimBST(root.right, L, R)
        return root 
```

### 2-6) Maximum Width of Binary Tree — LC 662
```python
# 662 Maximum Width of Binary Tree
class Solution(object):
    def widthOfBinaryTree(self, root):
        self.ans = 0
        left = {}
        def dfs(node, depth = 0, pos = 0):
            if node:
                left.setdefault(depth, pos)
                self.ans = max(self.ans, pos - left[depth] + 1)
                dfs(node.left, depth + 1, pos * 2)
                dfs(node.right, depth + 1, pos * 2 + 1)

        dfs(root)
        return self.ans
```

### 2-7) Equal Tree Partition — LC 663
```python
# 663 Equal Tree Partition
# V0
# IDEA : DFS + cache
class Solution(object):
    def checkEqualTree(self, root):
        seen = []

        def sum_(node):
            if not node: return 0
            seen.append(sum_(node.left) + sum_(node.right) + node.val)
            return seen[-1]

        sum_(root)
        #print ("seen = " + str(seen))
        return seen[-1] / 2.0 in seen[:-1]
```

### 2-8) Split BST — LC 776
```python
# 776 Split BST
# V0
# IDEA : BST properties (left < root < right) + recursion
# https://blog.csdn.net/magicbean2/article/details/79679927
# https://www.itdaan.com/tw/d58594b92742689b5769f9827365e8b4
### STEPS
#  -> 1) check whether root.val > or < V
#     -> if root.val > V : 
#           - NO NEED TO MODIFY ALL RIGHT SUB TREE
#           - BUT NEED TO re-connect nodes in LEFT SUB TREE WHICH IS BIGGER THAN V (root.left = right)
#     -> if root.val < V : 
#           - NO NEED TO MODIFY ALL LEFT SUB TREE
#           - BUT NEED TO re-connect nodes in RIGHT SUB TREE WHICH IS SMALLER THAN V (root.right = left)
# -> 2) return result
class Solution(object):
    def splitBST(self, root, V):
        if not root: return [None, None]
        ### NOTE : if root.val <= V
        if root.val > V:
            left, right = self.splitBST(root.left, V)
            root.left = right
            return [left, root]
        ### NOTE : if root.val > V
        else:
            left, right = self.splitBST(root.right, V)
            root.right = left
            return [root, right]
```

### 2-9) Most Frequent Subtree Sum — LC 508
```python
# LC 508 Most Frequent Subtree Sum
# V0
# IDEA : DFS + TREE
class Solution(object):
    def findFrequentTreeSum(self, root):
        """
        ### NOTE : this trick : get sum of sub tree
        # LC 663 Equal Tree Partition
        """
        def get_sum(root):
            if not root:
                return 0
            s = get_sum(root.left) + root.val + get_sum(root.right)
            res.append(s)
            return s

        if not root:
            return []
        res = []
        get_sum(root)
        counts = collections.Counter(res)
        _max = max(counts.values())
        return [x for x in counts if counts[x] == _max]
```

### 2-10) Convert BST to Greater Tree — LC 538
```python
# LC 538 Convert BST to Greater Tree
# V0
# IDEA : DFS + recursion
#      -> NOTE : via DFS, the op will being executed in `INVERSE` order (last visit will be run first, then previous, then ...)
#      -> e.g. node1 -> node2 -> ... nodeN
#      ->      will run nodeN -> nodeN-1 ... node1
class Solution(object):

    def convertBST(self, root):
        self.sum = 0
        self.dfs(root)
        return root

    def dfs(self, node):
        if not node: 
            return
        #print ("node.val = " + str(node.val))
        self.dfs(node.right)
        self.sum += node.val
        node.val = self.sum
        self.dfs(node.left)

# V0'
# NOTE : the implementation difference on cur VS self.cur
# 1) if cur : we need to ssign output of help() func to cur
# 2) if self.cur : no need to assign, plz check V0 as reference
class Solution(object):
    def convertBST(self, root):
        def help(cur, root):
            if not root:
                ### NOTE : if not root, still need to return cur
                return cur
            ### NOTE : need to assign output of help() func to cur
            cur = help(cur, root.right)
            cur += root.val
            root.val = cur
            ### NOTE : need to assign output of help() func to cur
            cur = help(cur, root.left)
            ### NOTE : need to return cur
            return cur

        if not root:
            return

        cur = 0
        help(cur, root)
        return root
```

### 2-11) Number of Islands — LC 200
```python
# LC 200 Number of Islands, check LC 694, 711 as well
# V0 
# IDEA : DFS
class Solution(object):
    def numIslands(self, grid):
        def dfs(grid, item):
            if grid[item[0]][item[1]] == "0":
                return

            ### NOTE : MAKE grid[item[0]][item[1]] = 0 -> avoid visit again
            grid[item[0]][item[1]] = 0
            moves = [(0,1),(0,-1),(1,0),(-1,0)]
            for move in moves:
                _x = item[0] + move[0]
                _y = item[1] + move[1]
                ### NOTE : the boundary
                #       -> _x < l, _y < w
                if 0 <= _x < l and 0 <= _y < w and grid[_x][_y] != 0:
                    dfs(grid, [_x, _y])
  
        if not grid:
            return 0
        res = 0
        l = len(grid)
        w = len(grid[0])
        for i in range(l):
            for j in range(w):
                if grid[i][j] == "1":
                    ### NOTE : we go through every "1" in grids, and run dfs once
                    #         -> once dfs completed, we make res += 1 in each iteration
                    dfs(grid, [i,j])
                    res += 1
        return res
```

#### The two ways to write the 4 neighbour calls
- Graph transversal (DFS): traversal in 4 directions (up, down, left, right)
```java
// java
// LC 200

/** NOTE !!!! BELOW approach has same effect */

// V1

// private boolean _is_island(char[][] grid, int x, int y, boolean[][] seen){}

// ....
_is_island(grid, x+1, y, seen);
_is_island(grid, x-1, y, seen);
_is_island(grid, x, y+1, seen);
_is_island(grid, x, y-1, seen);
// ....

// V2
// private boolean _is_island_2(char[][] grid, int x, int y, boolean[][] seen) {}

int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

for (int[] dir : directions) {
    int newX = x + dir[0];
    int newY = y + dir[1];
    _is_island(grid, newX, newY, seen);
}
```

### 2-12) Max Area of Island — LC 695
```python
# LC 695. Max Area of Island
# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79182435
# IDEA : DFS 
# * PLEASE NOTE THAT IT IS NEEDED TO GO THROUGH EVERY ELEMENT IN THE GRID 
#   AND RUN THE DFS WITH IN THIS PROBLEM
class Solution(object):
    def maxAreaOfIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        self.res = 0
        self.island = 0
        M, N = len(grid), len(grid[0])
        for i in range(M):
            for j in range(N):
                if grid[i][j]:
                    self.dfs(grid, i, j)
                    self.res = max(self.res, self.island)
                    self.island = 0
        return self.res
    
    def dfs(self, grid, i, j): # ensure grid[i][j] == 1
        M, N = len(grid), len(grid[0])
        grid[i][j] = 0
        self.island += 1
        dirs = [(0, 1), (0, -1), (-1, 0), (1, 0)]
        for d in dirs:
            x, y = i + d[0], j + d[1]
            if 0 <= x < M and 0 <= y < N and grid[x][y]:
                self.dfs(grid, x, y)
```

### 2-13) Binary Tree Paths — LC 257
```python
# LC 257. Binary Tree Paths
# V0 
# IDEA : DFS 
class Solution:
    # @param {TreeNode} root
    # @return {string[]}
    def binaryTreePaths(self, root):
        res, path_list = [], []
        self.dfs(root, path_list, res)
        return res

    def dfs(self, root, path_list, res):
        if not root:
            return
        path_list.append(str(root.val))
        if not root.left and not root.right:
            res.append('->'.join(path_list))
        if root.left:
            self.dfs(root.left, path_list, res)
        if root.right:
            self.dfs(root.right, path_list, res)
        path_list.pop()
```

### 2-14) Lowest Common Ancestor of a Binary Tree — LC 236
```python
# LC 236 Lowest Common Ancestor of a Binary Tree
# V0
# IDEA : RECURSION + POST ORDER TRANSVERSAL
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):

        ### NOTE here
        # if not root or find p in tree or find q in tree
        # -> then we quit the recursion and return root

        ### NOTE : we compare `p == root` and  `q == root`
        if not root or p == root or q == root:
            return root
        ### NOTE here
        #  -> not root.left, root.right, BUT left, right
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)

        ### NOTE here
        # find q and p on the same time -> LCA is the current node (root)
        # if left and right -> p, q MUST in left, right sub tree respectively

        ### NOTE : if left and right, means this root is OK for next recursive
        if left and right:
            return root
        ### NOTE here
        # if p, q both in left sub tree or both in right sub tree
        return left if left else right
```

### 2-15) Path Sum — LC 112
```python
# LC 112 Path Sum
# V0
# IDEA : DFS 
class Solution(object):
    def hasPathSum(self, root, sum):
        if not root:
            return False
        if not root.left and not root.right:
            return True if sum == root.val else False
        else:
            return self.hasPathSum(root.left, sum-root.val) or self.hasPathSum(root.right, sum-root.val)
```

### 2-16) Path Sum II — LC 113
```python
# LC 113 Path Sum II
# V0
# IDEA : DFS
class Solution(object):
    def pathSum(self, root, sum):
        if not root: return []
        res = []
        self.dfs(root, sum, res, [root.val])
        return res

    def dfs(self, root, target, res, path):
        if not root: return
        if sum(path) == target and not root.left and not root.right:
            res.append(path)
            return
        if root.left:
            self.dfs(root.left, target, res, path + [root.left.val])
        if root.right:
            self.dfs(root.right, target, res, path + [root.right.val])
```

```java
// java
// LC 113
// V0
// IDEA : DFS + backtracking
// NOTE !!! we have res attr, so can use this.res collect result
private List<List<Integer>> res = new ArrayList<>();

public List<List<Integer>> pathSum(TreeNode root, int targetSum) {

    if (root == null){
        return this.res;
    }

    List<Integer> cur = new ArrayList<>();
    getPath(root, cur, targetSum);
    return this.res;
}

 private void getPath(TreeNode root, List<Integer> cur, int targetSum){

    // return directly if root is null (not possible to go further, so just quit directly)
    if (root == null){
        return;
    }

    // NOTE !!! we add val to cache here instead of while calling method recursively ( e.g. getPath(root.left, cur, targetSum - root.val))
    //          -> so we just need to backtrack (cancel last operation) once (e.g. cur.remove(cur.size() - 1);)
    //          -> please check V0' for example with backtrack in recursively step
    cur.add(root.val);

    if (root.left == null && root.right == null && targetSum == root.val){
        this.res.add(new ArrayList<>(cur));
    }else{
        // NOTE !!! we update targetSum here (e.g. targetSum - root.val)
        getPath(root.left, cur, targetSum - root.val);
        getPath(root.right, cur, targetSum - root.val);
    }

     // NOTE !!! we do backtrack here (cancel previous adding to cur)
     cur.remove(cur.size() - 1);
}
```

### 2-17) Sum Root to Leaf Numbers — LC 129

**Pattern:**
Each root-to-leaf path represents a number formed by concatenating digits top-to-bottom (e.g. `1 -> 2 -> 3` = `123`). Recognize this as a **path-encoding DFS**: instead of collecting the path into a list/string and joining it only at the leaf (like LC 113 does with `sum`/`+`), carry a **running accumulated value** down the recursion and update it in O(1) per node — no post-processing needed at the leaf.

**Core Idea:**
Concatenating a digit `d` onto a number `curr` is just `curr * 10 + d` (same idea as building an integer from a string of digits). Pass this accumulator as a function argument so each recursive call is naturally scoped — no explicit backtrack (`path.pop()`) is needed, since each stack frame holds its own `curr` by value, not a shared mutable list:

```text
curr = 0
depth 1 (root=1):  curr = 0*10 + 1 = 1
depth 2 (node=2):   curr = 1*10 + 2 = 12
depth 3 (node=3):   curr = 12*10 + 3 = 123   <- leaf, add 123 to running total
```

At a leaf (`not root.left and not root.right`), `curr` already holds the full number for that path — just return it. Sum the leaf values returned by the left and right subtrees.

```python
# LC 129. Sum Root to Leaf Numbers
# time = O(n), space = O(h) — h = tree height (recursion stack)
class Solution(object):
    def sumNumbers(self, root):
        def dfs(node, curr):
            if not node:
                return 0
            curr = curr * 10 + node.val
            if not node.left and not node.right:
                return curr
            return dfs(node.left, curr) + dfs(node.right, curr)

        return dfs(root, 0)
```

**Path-list variant (equivalent, but needs explicit backtrack):**
```python
# Building path as a list instead of an accumulator — requires path.pop() to backtrack
class Solution(object):
    def sumNumbers(self, root):
        self.res = 0
        self.dfs(root, [])
        return self.res

    def dfs(self, root, path):
        if not root:
            return
        path.append(root.val)
        if not root.left and not root.right:
            self.res += int("".join(map(str, path)))
            path.pop()          # backtrack before returning
            return
        self.dfs(root.left, path)
        self.dfs(root.right, path)
        path.pop()              # backtrack
```

**Why the accumulator form is preferred:** passing `curr` as an immutable argument (`curr * 10 + node.val`) means every recursive branch gets its own independent copy for free — no shared mutable state, so no backtrack bookkeeping is needed. This is the same trade-off as LC 113's `path + [val]` (new list per call, no pop needed) vs. `path.append/pop` (shared list, needs explicit undo).

**Similar LC problems (root-to-leaf path-encoding via accumulator):**
| Problem | Pattern |
|---------|---------|
| LC 129 - Sum Root to Leaf Numbers | `curr = curr * 10 + val` — decimal digit concatenation |
| LC 257 - Binary Tree Paths | accumulate path as string `"->"`-joined, collect at leaf |
| LC 112 - Path Sum | accumulate remaining target via subtraction (`sum - root.val`) instead of building upward |
| LC 113 - Path Sum II | same as 112 but collects the actual path list at each valid leaf |
| LC 988 - Smallest String Starting From Leaf | accumulate path as string bottom-up (leaf-to-root), compare lexicographically |

### 2-18) Clone Graph — LC 133
```python
# 133 Clone graph
# note : there is also a BFS solution
# V0
# IDEA : DFS
# NOTE :
#  -> 1) we init node via : node_copy = Node(node.val, [])
#  -> 2) we copy graph via dict
class Solution(object):
    def cloneGraph(self, node):
        """
        :type node: Node
        :rtype: Node
        """
        node_copy = self.dfs(node, dict())
        return node_copy
    
    def dfs(self, node, hashd):
        if not node: return None
        if node in hashd: return hashd[node]
        node_copy = Node(node.val, [])
        hashd[node] = node_copy
        for n in node.neighbors:
            n_copy = self.dfs(n, hashd)
            if n_copy:
                node_copy.neighbors.append(n_copy)
        return node_copy
```

### 2-19) Sentence Similarity II — LC 737
```python
# LC 737. Sentence Similarity II
# NOTE : there is also union-find solution
# V0
# IDEA : DFS
from collections import defaultdict
class Solution(object):
    def areSentencesSimilarTwo(self, sentence1, sentence2, similarPairs):
        # helper func
        def dfs(w1, w2, visited):
            for j in d[w2]:
                if w1 == w2:
                    return True
                elif j not in visited:
                    visited.add(j)
                    if dfs(w1, j, visited):
                        return True
            return False
        
        # edge case
        if len(sentence1) != len(sentence2):
            return False
      
        d = defaultdict(list)
        for a, b in similarPairs:
            d[a].append(b)
            d[b].append(a)
            
        for i in range(len(sentence1)):
            visited =  set([sentence2[i]])
            if sentence1[i] != sentence2[i] and not dfs(sentence1[i],  sentence2[i], visited):
                return False
        return True
```

#### ⭐ LC 737 — Sentence Similarity II (deep dive)

> Despite the "sentence / words" framing, this is a **graph connectivity** problem,
> NOT a string problem. Each `similarPair` is an **undirected edge**; similarity is
> **transitive** (`a~b, b~c ⇒ a~c`), which is exactly "are these two nodes in the same
> connected component?". (Contrast LC 734 *Sentence Similarity I* — no transitivity,
> so a plain set lookup suffices, no graph needed.)

**1) Core Idea**

- **Build an undirected graph** from `similarPairs`: `graph[a].add(b)`, `graph[b].add(a)`.
- For each aligned word pair `(w1, w2)`:
  - `w1 == w2` → similar by definition (a word is similar to itself) → skip.
  - else **DFS/BFS** from `w1` trying to reach `w2`; if unreachable → return `False`.
- Length mismatch → immediately `False`.

```python
# clean reference (explicit graph + DFS reachability)
def areSentencesSimilarTwo(s1, s2, pairs):
    if len(s1) != len(s2):
        return False
    g = collections.defaultdict(set)
    for a, b in pairs:
        g[a].add(b); g[b].add(a)

    def connected(src, dst):
        if src == dst:
            return True
        stack, seen = [src], {src}          # seed seen w/ src to avoid re-visit
        while stack:
            w = stack.pop()
            if w == dst:
                return True
            for nei in g[w]:
                if nei not in seen:
                    seen.add(nei); stack.append(nei)
        return False

    return all(connected(a, b) for a, b in zip(s1, s2))
```

**2) Pattern / Recognition**

| Signal | What it tells you |
|--------|-------------------|
| relation is **transitive** (`a~b, b~c ⇒ a~c`) | connected-components problem |
| "are X and Y related/connected/in same group" | DFS / BFS / **Union-Find** |
| edges given as pairs, query many (x,y) reachability | prefer **Union-Find** (near O(1)/query) |
| must seed `visited` with the start node | avoid infinite loop on cycles |

```text
3 interchangeable engines (same idea, different machinery):
  DFS / BFS   -> per-query graph traversal      | O((V+E)) per query
  Union-Find  -> union all pairs, then find()    | ~O(α(n)) per query  <- best for many queries
Don't forget: w1 == w2 short-circuits TRUE even if the word isn't in the graph.
```

**3) Similar LC**

| LC | Problem | Relation |
|----|---------|----------|
| 737 | Sentence Similarity II | this problem — transitive → component check |
| 734 | Sentence Similarity I | NOT transitive → just set lookup (no graph) |
| 547 | Number of Provinces | count connected components (DFS / Union-Find) |
| 200 | Number of Islands | grid connected components |
| 990 | Satisfiability of Equality Equations | `==`/`!=` constraints → Union-Find |
| 684 | Redundant Connection | detect the edge that creates a cycle (Union-Find) |
| 399 | Evaluate Division | connectivity + weighted (ratio) edges |

**4) Concept — why an "early `return False`" does NOT break the overall DFS** ⭐⭐⭐⭐⭐

> A very common confusion with this template:
> ```python
> def helper(graph, node, target, visited):
>     if node == target:    return True
>     if node in visited:   return False     # <-- does this kill the whole search??
>     visited.add(node)
>     for nei in graph[node]:
>         if helper(graph, nei, target, visited):
>             return True                    # bubble success UP
>     return False                           # <-- and does this??
> ```
> **No.** A `return` only goes **one level up** the recursion stack — to the *caller*,
> NOT to the top-level call. A `False` just ends *that one branch* and lets the parent's
> `for` loop move on to its next neighbor. Only `True` propagates all the way up
> (because every caller does `if helper(...): return True`).

**Walkthrough** — graph `A→[B,C]`, `B→[D]`, `C→[E]`; call `helper(A, target=E)`:

```text
helper(A)  visited={A}        for nei in [B, C]:  -> loop PAUSES at B
 └─ helper(B)  visited={A,B}  for nei in [D]:
     └─ helper(D)  no neighbors -> return False   ── returns to helper(B) ONLY
    back in helper(B): `if False: return True` skipped; no more neighbors -> return False
back in helper(A): B branch failed, loop RESUMES -> nei = C
 └─ helper(C)  visited={A,B,D,C}  for nei in [E]:
     └─ helper(E)  E == target -> return True
    back in helper(C): `if True: return True`     -> helper(C) returns True
back in helper(A): `if True: return True`         -> helper(A) returns True
```

```text
            helper(A) ───────────────► True
            ├─ helper(B) ──► False        (dead branch, did NOT stop search)
            │   └─ helper(D) ──► False
            └─ helper(C) ──► True
                └─ helper(E) ──► True
```

The first `False` (from the `B→D` branch) **did not** stop the search — it only
ended that branch, and the loop in `helper(A)` continued on to `C`.

**Same logic for `if node in visited: return False`** — on a cyclic graph
(`A↔B`, `A↔C`): `helper(A)→helper(B)→helper(A)` hits `A in visited` and returns `False`
*to `helper(B)` only*. It means "don't re-search through A", not "give up". Control
returns to `helper(A)`'s loop, which then explores `C` normally. Nothing is cut off.

> **Key idea**: the bottom `return False` runs **only after every neighbor has been tried**.
> One child returning `False` just advances the `for` loop; the whole DFS reports `False`
> only when *all* branches are exhausted without reaching the target.

### 2-20) Serialize and Deserialize Binary Tree — LC 297
```python
# LC 297. Serialize and Deserialize Binary Tree
# V0
# IDRA : DFS
class Codec:

    def serialize(self, root):
        """ Encodes a tree to a single string.
        :type root: TreeNode
        :rtype: str
        """
        def rserialize(root, string):
            """ a recursive helper function for the serialize() function."""
            # check base case
            if root is None:
                string += 'None,'
            else:
                string += str(root.val) + ','
                string = rserialize(root.left, string)
                string = rserialize(root.right, string)
            return string
        
        return rserialize(root, '')    

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        :type data: str
        :rtype: TreeNode
        """
        def rdeserialize(l):
            """ a recursive helper function for deserialization."""
            if l[0] == 'None':
                l.pop(0)
                return None
                
            root = TreeNode(l[0])
            l.pop(0)
            root.left = rdeserialize(l)
            root.right = rdeserialize(l)
            return root

        data_list = data.split(',')
        root = rdeserialize(data_list)
        return root
```

### 2-21) Serialize and Deserialize BST — LC 449
```python
# LC 449. Serialize and Deserialize BST
# V0
# IDEA : BFS + queue op
class Codec:
    def serialize(self, root):
        if not root:
            return '{}'

        res = [root.val]
        q = [root]

        while q:
            new_q = []
            for i in range(len(q)):
                tmp = q.pop(0)
                if tmp.left:
                    q.append(tmp.left)
                    res.extend( [tmp.left.val] )
                else:
                    res.append('#')
                if tmp.right:
                    q.append(tmp.right)
                    res.extend( [tmp.right.val] )
                else:
                    res.append('#')

        while res and res[-1] == '#':
                    res.pop()

        return '{' + ','.join(map(str, res)) + '}' 


    def deserialize(self, data):
        if data == '{}':
            return

        nodes = [ TreeNode(x) for x in data[1:-1].split(",") ]
        root = nodes.pop(0)
        p = [root]
        while p:
            new_p = []
            for n in p:
                if nodes:
                    left_node = nodes.pop(0)
                    if left_node.val != '#':
                        n.left = left_node
                        new_p.append(n.left)
                    else:
                        n.left = None
                if nodes:
                    right_node = nodes.pop(0)
                    if right_node.val != '#':
                        n.right = right_node
                        new_p.append(n.right)
                    else:
                        n.right = None
            p = new_p 
             
        return root

# V1
# IDEA : same as LC 297
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/93283/Python-solution-using-BST-property
class Codec:

    def serialize(self, root):
        vals = []
        self._preorder(root, vals)
        return ','.join(vals)
        
    def _preorder(self, node, vals):
        if node:
            vals.append(str(node.val))
            self._preorder(node.left, vals)
            self._preorder(node.right, vals)
        
    def deserialize(self, data):
        vals = collections.deque(map(int, data.split(','))) if data else []
        return self._build(vals, -float('inf'), float('inf'))

    def _build(self, vals, minVal, maxVal):
        if vals and minVal < vals[0] < maxVal:
            val = vals.popleft()
            root = TreeNode(val)
            root.left = self._build(vals, minVal, val)
            root.right = self._build(vals, val, maxVal)
            return root
        else:
            return None
```

### 2-22) Concatenated Words — LC 472
```python
# LC 472. Concatenated Words
# V1
# http://bookshadow.com/weblog/2016/12/18/leetcode-concatenated-words/
# IDEA : DFS 
class Solution(object):
    def findAllConcatenatedWordsInADict(self, words):
        """
        :type words: List[str]
        :rtype: List[str]
        """
        ans = []
        self.wordSet = set(words)
        for word in words:
            self.wordSet.remove(word) # avoid the search process find itself (word) when search all word in words  
            if self.search(word):
                ans.append(word)
            self.wordSet.add(word)    # add the word back for next search with new "word"
        return ans

    def search(self, word):
        if word in self.wordSet:
            return True
        for idx in range(1, len(word)):
            if word[:idx] in self.wordSet and self.search(word[idx:]):
                return True
        return False
```

### 2-23) Maximum Product of Splitted Binary Tree — LC 1339
```python
# LC 1339. Maximum Product of Splitted Binary Tree
# V0
# IDEA : DFS
class Solution(object):
    def maxProduct(self, root):
        all_sums = []

        def tree_sum(subroot):
            if subroot is None: return 0
            left_sum = tree_sum(subroot.left)
            right_sum = tree_sum(subroot.right)
            total_sum = left_sum + right_sum + subroot.val
            all_sums.append(total_sum)
            return total_sum

        total = tree_sum(root)
        best = 0
        for s in all_sums:
            best = max(best, s * (total - s))   
        return best % (10 ** 9 + 7)
```

### 2-24) Pacific Atlantic Water Flow — LC 417

```java
// java
// LC 417
// V0
// IDEA : DFS (fixed by GPT)

public List<List<Integer>> pacificAtlantic(int[][] heights) {

    if (heights == null || heights.length == 0 || heights[0].length == 0) {
        return new ArrayList<>();
    }

    int l = heights.length;
    int w = heights[0].length;

    /**
     *
     * The pacificReachable and atlanticReachable arrays are used to keep track
     * of which cells in the matrix can reach the Pacific and Atlantic oceans, respectively.
     *
     *
     * - pacificReachable[i][j] will be true if water
     *   can flow from cell (i, j) to the Pacific Ocean.
     *   The Pacific Ocean is on the top and left edges of the matrix.
     *
     * - atlanticReachable[i][j] will be true if water
     *   can flow from cell (i, j) to the Atlantic Ocean.
     *   The Atlantic Ocean is on the bottom and right edges of the matrix.
     *
     *
     * NOTE !!!!
     *
     * The pacificReachable and atlanticReachable arrays serve a dual purpose:
     *
     * Tracking Reachability: They track whether each cell can reach the respective ocean.
     *
     * Tracking Visited Cells: They also help in tracking whether a cell has already
     *                         been visited during the depth-first search (DFS)
     *                         to prevent redundant work and infinite loops.
     *
     *
     *   NOTE !!!
     *
     *    we use `boolean[][]` to track if a cell is reachable
     */
    boolean[][] pacificReachable = new boolean[l][w];
    boolean[][] atlanticReachable = new boolean[l][w];

    // check on x-axis
    /**
     *  NOTE !!!
     *
     *   we loop EVERY `cell` at x-axis  ( (x_1, 0), (x_2, 0), .... (x_1, l - 1), (x_2, l - 1) ... )
     *
     */
    for (int x = 0; x < w; x++) {
        dfs(heights, pacificReachable, 0, x);
        dfs(heights, atlanticReachable, l - 1, x);
    }

    // check on y-axis
    /**
     *  NOTE !!!
     *
     *   we loop EVERY `cell` at y-axis  (  (0, y_1), (0, y_2), .... (w-1, y_1), (w-1, y_2), ... )
     *
     */
    for (int y = 0; y < l; y++) {
        dfs(heights, pacificReachable, y, 0);
        dfs(heights, atlanticReachable, y, w - 1);
    }

    List<List<Integer>> commonCells = new ArrayList<>();
    for (int i = 0; i < l; i++) {
        for (int j = 0; j < w; j++) {
            if (pacificReachable[i][j] && atlanticReachable[i][j]) {
                commonCells.add(Arrays.asList(i, j));
            }
        }
    }
    return commonCells;
}

/**
 *  NOTE !!!
 *
 *   this dfs func return NOTHING,
 *   e.g. it updates the matrix value `in place`
 *
 *   example:  we pass `pacificReachable` as param to dfs,
 *             it modifies values in pacificReachable in place,
 *             but NOT return pacificReachable as response
 */
private void dfs(int[][] heights, boolean[][] reachable, int y, int x) {

    int l = heights.length;
    int w = heights[0].length;

    reachable[y][x] = true;

    int[][] directions = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
    for (int[] dir : directions) {
        int newY = y + dir[0];
        int newX = x + dir[1];

        /**
         *  NOTE !!!  only meet below conditions, then do recursion call
         *
         *  1. newX, newY still in range
         *  2. newX, newY is still not reachable (!reachable[newY][newX])
         *  3. heights[newY][newX] >= heights[y][x]
         *
         *
         *  NOTE !!!
         *
         *  The condition !reachable[newY][newX] in the dfs function
         *  ensures that each cell is only processed once
         *
         *  1. Avoid Infinite Loops
         *  2. Efficiency
         *  3. Correctness
         *
         *
         *  NOTE !!! "inverse" comparison
         *
         *  we use the "inverse" comparison, e.g.  heights[newY][newX] >= heights[y][x]
         *  so we start from "cur point" (heights[y][x]), and compare with "next point" (heights[newY][newX])
         *  if "next point" is "higher" than "cur point"  (e.g. heights[newY][newX] >= heights[y][x])
         *  -> then means water at "next point" can flow to "cur point"
         *  -> then we keep track back to next point of then "next point"
         *  -> repeat ...
         */
        if (newY >= 0 && newY < l && newX >= 0 && newX < w && !reachable[newY][newX] && heights[newY][newX] >= heights[y][x]) {
            dfs(heights, reachable, newY, newX);
        }
    }
} 
```

### 2-25) Minesweeper — LC 529

```java
// java
// LC 529

// (there is also BFS solution)

// V1
// IDEA: DFS + ARRAY OP (GPT)
public char[][] updateBoard_1(char[][] board, int[] click) {
    int rows = board.length;
    int cols = board[0].length;

    int x = click[0], y = click[1];

    // Edge case: 1x1 grid
    if (rows == 1 && cols == 1) {
        if (board[0][0] == 'M') {
            board[0][0] = 'X';
        } else {
            board[0][0] = 'B'; // Fix: properly set 'B' if it's 'E'
        }
        return board;
    }

    // If a mine is clicked, mark as 'X'
    if (board[x][y] == 'M') {
        board[x][y] = 'X';
        return board;
    }

    // Otherwise, reveal cells recursively
    reveal_1(board, x, y);
    return board;
}

private void reveal_1(char[][] board, int x, int y) {
    int rows = board.length;
    int cols = board[0].length;

// Boundary check and already revealed check
/** NOTE !!!
 *
 *  - 1) 'E' represents an unrevealed empty square,
 *
 *  - 2) board[x][y] != 'E'
 *      -> ensures that we only process unrevealed empty cells ('E')
 *         and avoid unnecessary recursion.
 *
 *   - 3) board[x][y] != 'E'
 *   •  Avoids re-processing non-‘E’ cells
 *   •  The board can have:
 *      •   'M' → Mine (already handled separately)
 *      •   'X' → Clicked mine (game over case)
 *      •   'B' → Blank (already processed)
 *      •   '1' to '8' → Number (already processed)
 *  •   If a cell is not 'E', it means:
 *      •   It has already been processed
 *      •   It does not need further expansion
 *  •   This prevents infinite loops and redundant checks.
 *
 *
 *  - 4) example:
 *
 *     input:
 *          E E E
 *          E M E
 *          E E E
 *
 *   Click at (0,0)
 *      1.  We call reveal(board, 0, 0), which:
 *          •   Counts 1 mine nearby → Updates board[0][0] = '1'
 *          •   Does NOT recurse further, avoiding unnecessary work.
 *
 *      What If We Didn’t Check board[x][y] != 'E'?
 *          •   It might try to expand into already processed cells, leading to redundant computations or infinite recursion.
 *
 */
if (x < 0 || x >= rows || y < 0 || y >= cols || board[x][y] != 'E') {
        return;
    }

    // Directions for 8 neighbors
    int[][] directions = {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            { 0, -1 }, { 0, 1 },
            { 1, -1 }, { 1, 0 }, { 1, 1 }
    };

    // Count adjacent mines
    int mineCount = 0;
    for (int[] dir : directions) {
        int newX = x + dir[0];
        int newY = y + dir[1];
        if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && board[newX][newY] == 'M') {
            mineCount++;
        }
    }

    // If there are adjacent mines, show count
    if (mineCount > 0) {
        board[x][y] = (char) ('0' + mineCount);
    } else {
        // Otherwise, reveal this cell and recurse on neighbors
        board[x][y] = 'B';
        for (int[] dir : directions) {
            reveal_1(board, x + dir[0], y + dir[1]);
        }
    }
}
```

### 2-26) K-th Largest Perfect Subtree Size in Binary Tree — LC 3319

```java
// java
// LC 3319

// V0-1
// IDEA: DFS (fixed by gpt)
//  Time Complexity: O(N log N)
//  Space Complexity: O(N)
/**
*  Objective recap:
*
*   We want to:
*    •   Find all perfect binary subtrees in the given tree.
*    •   A perfect binary tree is one where:
*        •   Every node has 0 or 2 children (i.e., full),
*        •   All leaf nodes are at the `same depth`.
*    •   Return the k-th largest size among these perfect subtrees.
*    •   If there are fewer than k perfect subtrees, return -1.
*
*/
// This is a class-level list that stores the sizes of all perfect subtrees we discover during traversal.
List<Integer> perfectSizes = new ArrayList<>();

public int kthLargestPerfectSubtree_0_1(TreeNode root, int k) {
    dfs(root);
    if (perfectSizes.size() < k)
        return -1;

    Collections.sort(perfectSizes, Collections.reverseOrder());
    return perfectSizes.get(k - 1);
}

// Helper class to store information about each subtree
/**
*
* It returns a helper object SubtreeInfo, which contains:
*    •   height: depth of the subtree rooted at node.
*    •   size: number of nodes in the subtree.
*    •   isPerfect: boolean indicating whether this subtree is perfect.
*
*/
private static class SubtreeInfo {
    int height;
    int size;
    boolean isPerfect;

    SubtreeInfo(int height, int size, boolean isPerfect) {
        this.height = height;
        this.size = size;
        this.isPerfect = isPerfect;
    }
}

/**
* Inside dfs():
*    1.  Base case:
*        •   If node == null, we return a SubtreeInfo with height 0, size 0, and isPerfect = true.
*    2.  Recurse on left and right children.
*    3.  Check if the subtree rooted at this node is perfect:
*
*/
private SubtreeInfo dfs(TreeNode node) {
    if (node == null) {
        return new SubtreeInfo(0, 0, true);
    }

    SubtreeInfo left = dfs(node.left);
    SubtreeInfo right = dfs(node.right);

/**  NOTE !!!  below logic:
 *
 * This ensures:
 *  •   Both left and right subtrees are perfect.
 *  •   Their `heights` are the same → leaves are at the `same level`.
 */
boolean isPerfect = left.isPerfect && right.isPerfect
        && (left.height == right.height);


    int size = left.size + right.size + 1;
    int height = Math.max(left.height, right.height) + 1;

    /**
     *  NOTE !!!
     *
     *  If the current subtree is perfect, we record its size:
     *
     */
    if (isPerfect) {
        perfectSizes.add(size);
    }

    return new SubtreeInfo(height, size, isPerfect);
}
```

### 2-27) Satisfiability of Equality Equations — LC 990

**Pattern — connectivity / contradiction check (equality grouping)**

- **Description**: Given equality (`==`) and inequality (`!=`) constraints, decide if they are all satisfiable. Build a graph from the `==` edges, then verify no `!=` pair is actually connected.
- **Recognition**: "equality equations", "variables are equal/not equal", "satisfiability", "group by equivalence then detect contradiction", relations that are **transitive** (`a==b`, `b==c` ⟹ `a==c`)
- **Key Technique**: **Two-phase** processing — (1) build an **undirected** graph from all `==` relations; (2) for each `!=` relation, DFS to check reachability. If two "must-be-different" variables are connected → contradiction → return False.
- **Examples**: LC 990 (Satisfiability of Equality Equations)
- **Core Algorithm Idea** (⭐⭐⭐⭐⭐):
  1. **Graph Construction**: for every `x==y`, add **both** `x→y` and `y→x` (undirected). The `==` relation is symmetric AND transitive, so connected components = equivalence classes.
  2. **Contradiction Scan**: for every `x!=y`, run DFS from `x`; if it can reach `y`, the two are forced equal by the graph but required unequal → **unsatisfiable**.
  3. Process **all `==` first**, then **all `!=`** — a `!=` seen before its group is fully built would give a wrong answer.
- **Important Notes**:
  - ⚠️ **Graph MUST be bidirectional.** Calling `dfs(a,b)` and `dfs(b,a)` on a *single-direction* graph is NOT equivalent — for `a==b, b==c`, one-directional `dfs(c, a)` finds no outgoing edge and wrongly returns False. Store both directions instead.
  - **No need** to pre-check `if y in graph[x]` before DFS — the DFS naturally covers the direct-edge case (`cur == target` on the first hop's recursion).
  - The self-inequality `a!=a` is inherently unsatisfiable; DFS returns True immediately since `cur == target` (the gemini variant guards it explicitly).
  - `visited` set is **reset per `!=` query** so each reachability check explores independently.
- **Alternative (cleaner): Union-Find** — `union(x,y)` for each `==`; then for each `!=`, if `find(x)==find(y)` return False. `O(N·α)` time, usually the preferred interview answer. See [union_find.md](./union_find.md).
- **DFS vs Union-Find trade-off**: DFS query is `O(V+E)` per `!=` check (can be `O(N²)` overall); Union-Find is near-`O(1)` per query — but DFS reinforces the graph-connectivity mental model.
- **Similar Classic LC Problems**:
  - LC 990 - Satisfiability of Equality Equations (canonical equality grouping + contradiction)
  - LC 547 - Number of Provinces (connected components via DFS/Union-Find)
  - LC 200 - Number of Islands (connectivity grouping on a grid)
  - LC 721 - Accounts Merge (merge by shared email → components)
  - LC 684 - Redundant Connection (detect the edge that creates a cycle — Union-Find)
  - LC 399 - Evaluate Division (transitive relations, weighted variant — [dfs.md Template 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399))
  - LC 785 - Is Graph Bipartite? (2-coloring = a "different-group" constraint check)

```python
# python
# LC 990 - Satisfiability of Equality Equations
# IDEA: DFS — group `==` variables into a graph, then check `!=` contradictions
# time = O(N^2) worst case (DFS per `!=`), space = O(N)
class Solution(object):
    def equationsPossible(self, equations):
        same_group = {}

        # 1) init nodes so graph[x] never KeyErrors
        for eq in equations:
            a, b = eq[0], eq[3]
            same_group.setdefault(a, [])
            same_group.setdefault(b, [])

        # 2) build UNDIRECTED graph from `==` only (bi-directional is required!)
        for eq in equations:
            a, b = eq[0], eq[3]
            if eq[1:3] == "==":
                same_group[a].append(b)
                same_group[b].append(a)

        # 3) verify each `!=` : if a can reach b, it's a contradiction
        for eq in equations:
            a, b = eq[0], eq[3]
            if eq[1:3] == "!=":
                visited = set()
                if self.helper(a, b, same_group, visited):
                    return False
        return True

    def helper(self, cur, target, graph, visited):
        if cur == target:          # reachable → forced equal → contradiction
            return True
        if cur in visited:
            return False
        visited.add(cur)
        for nxt in graph[cur]:
            if self.helper(nxt, target, graph, visited):
                return True
        return False
```

**Union-Find alternative** (cleaner, near-`O(N·α)`):

```python
# python
# LC 990 - Union-Find
class Solution:
    def equationsPossible(self, equations):
        uf = {}
        def find(x):
            uf.setdefault(x, x)
            if x != uf[x]:
                uf[x] = find(uf[x])   # path compression
            return uf[x]
        def union(x, y):
            uf[find(x)] = find(y)

        for e in equations:
            if e[1] == '=':
                union(e[0], e[-1])
        for e in equations:
            if e[1] == '!':
                if find(e[0]) == find(e[-1]):
                    return False
        return True
```

**Gotcha**: the `==` graph MUST be bidirectional. For `a==b, b==c`, a single-direction graph makes `dfs(c, a)` fail (no outgoing edge from `c`) and wrongly reports satisfiable — store both `x→y` and `y→x`.

---

### 2-28) Print Binary Tree — LC 655

> **DFS + fixed-size matrix**. Pre-compute the tree height to size a `(height+1) × (2^(height+1)-1)` string grid, place the root at the middle column, then DFS placing each child at a **halving horizontal offset** `2^(height-row-1)`.

**Key idea**: the grid dimensions are fixed *before* traversal (derived purely from height), so DFS only needs `(row, col)` — no dynamic sizing. Each level down halves the horizontal spread, which mirrors how a binary tree branches.

```python
# python
# LC 655 - Print Binary Tree
# IDEA: DFS + matrix — size grid from height, place root center, halve offset per level
# time = O(H * 2^H) (grid size), space = O(H * 2^H)
class Solution(object):
    def printTree(self, root):
        if not root:
            return []

        # 0-based height: single node -> 0, so leaf sits on last row
        self.height = self.get_tree_height(root)

        rows = self.height + 1
        cols = 2 ** (self.height + 1) - 1

        self.matrix = [[""] * cols for _ in range(rows)]

        # root goes in the middle of the top row
        self.helper(root, 0, (cols - 1) // 2)
        return self.matrix

    def get_tree_height(self, root):
        if not root:
            return -1              # NOTE: -1 so a leaf has height 0
        return 1 + max(
            self.get_tree_height(root.left),
            self.get_tree_height(root.right),
        )

    def helper(self, node, row, col):
        if not node:
            return
        self.matrix[row][col] = str(node.val)
        if row == self.height:      # last row -> no children to place
            return
        # offset HALVES each level down
        offset = 2 ** (self.height - row - 1)
        self.helper(node.left,  row + 1, col - offset)
        self.helper(node.right, row + 1, col + offset)
```

**Why `get_tree_height` returns `-1` for null**: it makes a single-node tree height `0`, so `rows = 1` and the node lands on the only row. If null returned `0`, every height would be off by one and the grid would be one row too tall.

**Offset intuition**: at the top row a child must jump a quarter of the whole width; one level deeper, half of that; and so on. `2^(height-row-1)` encodes exactly this geometric halving so children never collide and the layout stays symmetric.

| Step | Formula | Why |
|------|---------|-----|
| Rows | `height + 1` | one row per level |
| Cols | `2^(height+1) - 1` | widest possible bottom row, keeps it symmetric |
| Root col | `(cols - 1) // 2` | dead center of top row |
| Child offset | `2^(height - row - 1)` | halves each level so subtrees don't overlap |

---

### 2-29) Add One Row to Tree — LC 623 ⭐⭐⭐⭐

> **DFS with a countdown depth**. Insert a row of `val` nodes at `depth`. Instead of tracking an
> absolute level, **decrement `d` on every recursive call** and let the base case fire when
> `d == 2` — at that point the *current* node is the parent whose children must be rewired.
> The original left subtree hangs under the new left node's `.left`, the original right subtree
> under the new right node's `.right`.

**1) Core Idea**

- **Countdown, don't count up.** BFS needs `cur_depth == depth - 1`; DFS just passes `d - 1`
  downward and stops at `d == 2`, so no depth variable is threaded through the recursion.
  `d == 2` means "my children are the target row" — i.e. **I am the `depth - 1` parent**.
- **Two base cases, in this order**:
  - `d == 1` → there is no parent row; make a **new root** and hang the whole original tree on
    its **left**. This can only happen on the *top-level* call (see the note below).
  - `d == 2` → rewire *this* node's children: create two `val` nodes, reattach the old subtrees.
- **Cache before overwrite.** `root.left = TreeNode(v)` destroys the original pointer. Python's
  tuple assignment does this safely *if the order is right*:
  ```python
  root.left, root.left.left = TreeNode(v), root.left
  #    ^target 1  ^target 2      ^new node    ^OLD subtree (RHS evaluated FIRST)
  ```
  The whole RHS is evaluated before any assignment (so `root.left` there is still the *old*
  child), then targets are assigned **left → right**: `root.left` becomes the new node, then
  `root.left.left` (the new node) receives the old subtree. Swap the two targets and it breaks.
- **Outer-side reattach**: old left → `new_left.left`, old right → `new_right.right`. Using the
  inner sides mirrors the subtree.
- **`None` children are fine** — a node at `depth - 1` with no children still gets two new
  children, and `new.left = None` is exactly right. Only `root` itself needs a null guard.
- **DFS prunes naturally**: recursion stops at `d == 2`, so it never walks below the inserted
  row — the nodes it never visits are the ones it must not touch. No `break`/`return` guard
  needed like in the BFS version.

**2) Pattern**

```python
# python — LC 623 Add One Row to Tree (DFS countdown, reassign child links)
# time = O(N), space = O(h)   N = #nodes visited (only those above `d`), h = tree height
class Solution(object):
    def addOneRow(self, root, v, d):
        if not root:
            return None

        # (1) no depth-1 row exists -> new node becomes the new root
        if d == 1:
            new_root = TreeNode(v)
            new_root.left = root
            return new_root

        # (2) `root` IS the depth-1 parent -> splice the new row under it
        if d == 2:
            root.left,  root.left.left   = TreeNode(v), root.left   # outer side
            root.right, root.right.right = TreeNode(v), root.right  # outer side
            return root

        # (3) still above the target row -> count down
        root.left  = self.addOneRow(root.left,  v, d - 1)
        root.right = self.addOneRow(root.right, v, d - 1)
        return root
```

**Variant — mutate in place, ignore the return value** (also correct, and why):

```python
# python — recursive calls are NOT reassigned
else:
    self.addOneRow(root.left,  v, d - 1)
    self.addOneRow(root.right, v, d - 1)
return root
```

This works because the only branch that *replaces* a node (rather than mutating it) is
`d == 1`, and `d` never reaches `1` inside the recursion — it descends `d → d-1` and halts at
`2`. So every recursive call mutates its argument in place and the parent's pointer stays valid.
Prefer the **reassigning** form anyway: it is correct regardless of which base case fires, and it
survives refactors that change the base cases.

```text
Visual — root = [4,2,null,3,1], val = 1, depth = 3

d=3 at node 4  -> above target, recurse into children with d=2
d=2 at node 2  -> node 2 IS the depth-1 parent: cache (3, 1), splice
d=2 at node None -> null guard returns None (nothing to insert)

before                 after
    4                      4
   /                      /
  2                      2
 / \                    / \
3   1                  1   1        <- new row (val = 1) at depth 3
                      /     \
                     3       1      <- old children, OUTER sides

depth == 1 case: brand-new node becomes root, whole old tree hangs on its LEFT.
```

**DFS vs BFS for this problem**

| | DFS (this section) | BFS (see [bfs.md §2-17](./bfs.md)) |
|---|---|---|
| Depth tracking | implicit — countdown `d - 1`, stop at `d == 2` | explicit `cur_depth`, stop at `depth - 1` |
| Space | `O(h)` recursion stack | `O(W)` queue (max level width) |
| Stopping | automatic (recursion just ends) | needs an explicit `break`/`return` |
| Code length | shortest | more verbose but no stack risk |
| Risk | ⚠️ `depth` up to `10^4` in the constraints → a skewed tree can exceed Python's default recursion limit (1000) | none |

> Because the constraints allow a tree depth of `10^4`, the DFS version may need
> `sys.setrecursionlimit(...)` on a degenerate (linked-list-shaped) tree; the BFS version has no
> such limit. DFS is the cleaner interview answer, BFS the safer one at maximum input size.

**Common pitfalls**

| Pitfall | Why it breaks |
|---|---|
| Stopping at `d == 1` in the recursion | too deep — the pointers to rewire live on the parent, and `d == 1` is the *new-root* case |
| `root.left.left, root.left = root.left, TreeNode(v)` | targets in the wrong order — `root.left.left` is written on the **old** child, then overwritten away |
| `new_left.right = old_left` (inner sides) | mirrors the subtree; must be `.left` / `.right` respectively |
| Skipping `if not root: return None` | `d == 2` dereferences `root.left` on a null node |
| Not reassigning `root.left = self.addOneRow(...)` | only safe by accident (see variant above); breaks if a base case starts returning a *new* node |

**3) Similar LC**

| LC | Problem | Relation |
|----|---------|----------|
| 623 | Add One Row to Tree | this — DFS countdown to `d == 2`, rewire child pointers |
| 226 | Invert Binary Tree | same cache-then-reassign child pointers hazard |
| 617 | Merge Two Binary Trees | DFS returning the (possibly new) subtree root — the reassigning form |
| 654 | Maximum Binary Tree | build nodes during DFS and return them upward |
| 971 | Flip Binary Tree To Match Preorder | mutate left/right links mid-traversal |
| 116 / 117 | Populating Next Right Pointers | pointer rewiring, but per level (BFS-friendly) |
| 655 | Print Binary Tree | [2-28)](#2-28-print-binary-tree--lc-655) — DFS carrying a derived depth/offset downward |
| 111 / 104 | Min / Max Depth of Binary Tree | the depth-counting recursion this builds on |

> **Pattern takeaway**: "do X at depth `d`" ⇒ recurse with `d - 1` and act at **`d == 2`**, because
> the node you can actually mutate is the *parent* of the target row. Evaluate the old child
> pointers before assigning the new ones, reattach on the outer sides, and return the subtree
> root so the caller's link stays correct.

---

## Problems by Pattern

### Pattern-Based Problem Classification

`Template N` refers to [dfs.md → Templates & Algorithms](./dfs.md#templates--algorithms);
`*adv* TN` refers to [dfs_advanced.md](./dfs_advanced.md).

#### **Pattern 1: Tree Traversal Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Binary Tree Inorder Traversal | 94 | Easy | Stack/Recursion | Template 1 |
| Binary Tree Preorder Traversal | 144 | Easy | Stack/Recursion | Template 1 |
| Binary Tree Postorder Traversal | 145 | Easy | Stack/Recursion | Template 1 |
| Serialize and Deserialize Binary Tree | 297 | Hard | DFS encoding | Template 1 |
| Serialize and Deserialize BST | 449 | Medium | BST property | Template 1 |
| Binary Tree Paths | 257 | Easy | Path tracking | Template 3 |
| Same Tree | 100 | Easy | Simultaneous DFS | Template 1 |

#### **Pattern 2: Path Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Path Sum | 112 | Easy | DFS traversal | Template 3 |
| Path Sum II | 113 | Medium | Backtracking | Template 3 |
| Binary Tree Maximum Path Sum | 124 | Hard | Global max | Template 6 |
| Diameter of Binary Tree | 543 | Easy | Bottom-up | Template 6 |
| Longest Univalue Path | 687 | Medium | Bottom-up | Template 6 |
| Sum Root to Leaf Numbers | 129 | Medium | Path tracking | Template 3 |

#### **Pattern 3: Graph Traversal Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Number of Islands | 200 | Medium | Grid DFS | Template 2 |
| Max Area of Island | 695 | Medium | Grid DFS | Template 2 |
| Clone Graph | 133 | Medium | HashMap | Template 2 |
| Course Schedule | 207 | Medium | Cycle detection | Template 2 |
| Course Schedule II | 210 | Medium | Topological sort | Template 2 |
| Pacific Atlantic Water Flow | 417 | Medium | Multi-source | Template 2 |
| Evaluate Division | 399 | Medium | Graph traversal | Template 2 |
| Minesweeper | 529 | Medium | Grid exploration | Template 2 |

#### **Pattern 4: Backtracking Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Permutations | 46 | Medium | Backtrack | Template 4 |
| Subsets | 78 | Medium | Backtrack | Template 4 |
| Combination Sum | 39 | Medium | Backtrack | Template 4 |
| Letter Combinations | 17 | Medium | Backtrack | Template 4 |
| Generate Parentheses | 22 | Medium | Backtrack | Template 4 |
| Word Search | 79 | Medium | Grid backtrack | Template 4 |
| N-Queens | 51 | Hard | Backtrack | Template 4 |

#### **Pattern 5: Tree Modification Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Delete Node in BST | 450 | Medium | BST delete | Template 5 |
| Insert into BST | 701 | Medium | BST insert | Template 5 |
| Trim a Binary Search Tree | 669 | Medium | Conditional trim | Template 5 |
| Convert BST to Greater Tree | 538 | Medium | Reverse inorder | Template 5 |
| Invert Binary Tree | 226 | Easy | Tree swap | Template 5 |
| Flatten Binary Tree | 114 | Medium | In-place modify | Template 5 |

#### **Pattern 6: Subtree & Aggregation Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Most Frequent Subtree Sum | 508 | Medium | HashMap | Template 6 |
| Find Duplicate Subtrees | 652 | Medium | Serialization | Template 6 |
| Lowest Common Ancestor | 236 | Medium | Bottom-up | Template 6 |
| Equal Tree Partition | 663 | Medium | Subtree sum | Template 6 |
| Maximum Product of Splitted Tree | 1339 | Medium | All sums | Template 6 |
| Validate Binary Search Tree | 98 | Medium | Min/Max bounds | Template 1 |
| Split BST | 776 | Medium | Recursive split | Template 5 |

#### **Pattern 7: Boundary Elimination (2-Pass DFS)**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Number of Closed Islands | 1254 | Medium | Boundary flood | Template 7 |
| Surrounded Regions | 130 | Medium | Border elimination | Template 7 |
| Pacific Atlantic Water Flow | 417 | Medium | Two oceans | Template 7 |
| Number of Enclaves | 1020 | Medium | Border-connected | Template 7 |

#### **Pattern 8: Path Signatures (Shape Encoding)**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Number of Distinct Islands | 694 | Medium | Directional encoding | Template 8 |
| Number of Distinct Islands II | 711 | Hard | Handle rotations/reflections | Template 8 |
| Find Duplicate Subtrees | 652 | Medium | Tree serialization | Template 8 |
| Most Frequent Subtree Sum | 508 | Medium | Subtree signature | Template 8 |

#### **Pattern 9: DFS with Validation (Sub-Component Detection)**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Count Sub Islands | 1905 | Medium | Boolean flag propagation | *adv* T1 |
| Number of Islands | 200 | Medium | Basic component counting | Template 2 |
| Max Area of Island | 695 | Medium | Component size tracking | Template 2 |
| Island Perimeter | 463 | Easy | Edge counting | Template 2 |
| Making A Large Island | 827 | Hard | Component merging | Template 2 |

#### **Pattern 10: Bidirectional Graph with Direction Tracking**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Reorder Routes to Make All Paths Lead to the City Zero | 1466 | Medium | Bidirectional graph + direction flags | *adv* T2 |
| Minimum Number of Days to Disconnect Island | 1568 | Hard | Graph modification (related) | - |
| Remove Max Number of Edges to Keep Graph Fully Traversable | 1579 | Hard | Edge orientation (related) | - |

#### **Pattern 11: Component Pair Counting (Unreachable Pairs)**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Count Unreachable Pairs of Nodes in an Undirected Graph | 2316 | Medium | Component counting + cumulative multiplication | *adv* T3 |
| Number of Connected Components in an Undirected Graph | 323 | Medium | Basic component counting | Template 2 |
| Number of Provinces | 547 | Medium | Component detection | Template 2 |

### Complete Problem List by Difficulty

#### Easy Problems (Foundation)
- LC 94: Binary Tree Inorder Traversal - Basic DFS
- LC 100: Same Tree - Parallel DFS
- LC 101: Symmetric Tree - Mirror DFS
- LC 104: Maximum Depth - Simple recursion
- LC 112: Path Sum - Path tracking
- LC 144: Binary Tree Preorder Traversal - Stack usage
- LC 145: Binary Tree Postorder Traversal - Stack manipulation
- LC 226: Invert Binary Tree - Tree modification
- LC 257: Binary Tree Paths - Path collection
- LC 543: Diameter of Binary Tree - Global max pattern
- LC 572: Subtree of Another Tree - Tree matching

#### Medium Problems (Core)
- LC 98: Validate BST - Bounds checking
- LC 113: Path Sum II - Backtracking paths
- LC 130: Surrounded Regions - Boundary elimination
- LC 133: Clone Graph - HashMap + DFS
- LC 200: Number of Islands - Grid DFS
- LC 207: Course Schedule - Cycle detection
- LC 210: Course Schedule II - Topological sort
- LC 236: Lowest Common Ancestor - Bottom-up DFS
- LC 297: Serialize/Deserialize Tree - DFS encoding
- LC 399: Evaluate Division - Graph DFS
- LC 417: Pacific Atlantic Water Flow - Multi-source DFS
- LC 450: Delete Node in BST - Tree restructuring
- LC 449: Serialize/Deserialize BST - BST property
- LC 472: Concatenated Words - Word break DFS
- LC 508: Most Frequent Subtree Sum - Aggregation
- LC 529: Minesweeper - Grid exploration
- LC 538: Convert BST to Greater Tree - Reverse inorder
- LC 652: Find Duplicate Subtrees - Serialization
- LC 663: Equal Tree Partition - Subtree sums
- LC 669: Trim BST - Conditional modification
- LC 695: Max Area of Island - Connected component
- LC 701: Insert into BST - BST insertion
- LC 1466: Reorder Routes to Make All Paths Lead to the City Zero - Bidirectional graph with direction tracking
- LC 1905: Count Sub Islands - DFS with validation
- LC 2316: Count Unreachable Pairs of Nodes in an Undirected Graph - Component pair counting
- LC 737: Sentence Similarity II - Graph connectivity
- LC 776: Split BST - Advanced manipulation
- LC 1020: Number of Enclaves - Boundary elimination
- LC 1254: Number of Closed Islands - 2-Pass DFS
- LC 1339: Maximum Product of Splitted Tree - All subtree sums

#### Hard Problems (Advanced)
- LC 124: Binary Tree Maximum Path Sum - Global optimization
- LC 297: Serialize and Deserialize Binary Tree - Complex encoding
- LC 51: N-Queens - Complex backtracking
- LC 329: Longest Increasing Path in Matrix - Memoized DFS
- LC 3319: K-th Largest Perfect Subtree - Complex aggregation
- LC 332: Reconstruct Itinerary - Euler path (Hierholzer), see *adv* Template 4
- LC 753: Cracking the Safe - Euler circuit on a de Bruijn graph, see *adv* Template 4
- LC 1192: Critical Connections in a Network - Tarjan bridges (low-link), see *adv* Template 5

#### Additional High-Frequency DFS Problems (reference)

These are classic FAANG DFS questions that reuse templates already covered above — listed for
completeness, no new technique.

- LC 388: Longest Absolute File Path - Depth-indexed stack DFS (*adv* Template 7)
- LC 419: Battleships in a Board - Component counting without flood fill (Template 2 variation)
- LC 211: Design Add and Search Words Data Structure - Trie + wildcard DFS (*adv* Template 6)
- LC 676: Implement Magic Dictionary - Trie DFS with a mismatch budget (*adv* Template 6 variation)
- LC 1233: Remove Sub-Folders from the Filesystem - Path trie DFS with early cut (*adv* Template 7 variation)
- LC 863: All Nodes Distance K in Binary Tree - DFS to add parent links, then treat the tree as a graph
- LC 337: House Robber III - Post-order DFS returning a `(rob, skip)` state pair per node
- LC 947: Most Stones Removed with Same Row or Column - Connected components over row/column keys
- LC 690: Employee Importance - DFS over an `id -> employee` map instead of an adjacency list
- LC 341: Flatten Nested List Iterator - DFS flattening of a nested structure with an explicit stack
- LC 430: Flatten a Multilevel Doubly Linked List - DFS on a linked list; splice the child list inline
- LC 934: Shortest Bridge - DFS to mark one island, then BFS outward to reach the other

## Summary & Quick Reference

| Looking for | Go to |
|---|---|
| the technique behind any solution here | [dfs.md → Templates & Algorithms](./dfs.md#templates--algorithms) |
| which template a problem belongs to | the [Problems by Pattern](#problems-by-pattern) index above |
| a rare pattern (Euler path, Tarjan, trie DFS, `parent[]` trees) | [dfs_advanced.md](./dfs_advanced.md) |
| the BFS solution to the same grid/tree problem | [bfs.md](./bfs.md) |
| the tree-specific write-up of a tree problem | [tree.md](./tree.md), [tree2.md](./tree2.md), [bst.md](./bst.md) |

**Reading a multi-block entry**: where two code blocks share one heading, the note between them says
what the second one teaches — a different complexity, a different language idiom, or a distinct trick.
Anything else is a single canonical solution.

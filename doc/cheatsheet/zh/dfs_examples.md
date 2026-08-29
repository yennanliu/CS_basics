# DFS — 實戰題解

> **範圍** — [dfs.md](./dfs.md) 的題解檔案庫：核心模板涵蓋的每道 DFS 題目各一份標準解法，外加整個 DFS 題庫依模式與難度的索引。
> **另見** — *母表*：[dfs.md](./dfs.md) — 十個核心模板和模式選擇流程圖，*技巧*本身在那裡講；[dfs_advanced.md](./dfs_advanced.md) — 冷門／困難的 DFS 模板與範例。
> *鄰近的表*：[tree.md](./tree.md)、[tree2.md](./tree2.md) 和 [bst.md](./bst.md) 才是這裡重複出現的多數樹題的正主；[bfs.md](./bfs.md) — 同樣幾道網格題的廣度優先解法；[backtrack.md](./backtrack.md)、[union_find.md](./union_find.md) — 註解裡提到的替代引擎。

## LeetCode 題目清單

- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)

## 總覽
這份檔案裝的是 DFS 解法的長尾。它本身不教任何東西 —— 每一條都是 [dfs.md](./dfs.md) 某個模板的實例，*為什麼*在那邊。用它來對照解法、比較同一段遞迴的兩種寫法，或是從最下面的[依模式分類的題目](#problems-by-pattern)索引挑下一題。

### 關鍵性質
- **每題每種語言一份標準解法。** 只有在上方的註解說明「第二份跟第一份到底教了什麼不一樣的東西」時，才會出現第二段程式碼。
- **複雜度**：依模板而定 —— 見
  [dfs.md 的模板比較表](./dfs.md#template-comparison-table)。
- **模板已經解掉的題目**（LC 200 flood fill、LC 694 簽章、LC 1254 兩趟掃描、
  LC 1219 回溯、LC 399 比值查詢）這裡**不會**重複 —— 它們直接寫在
  [dfs.md](./dfs.md) 裡。

## LC 範例

### 0) 基本操作

幾段小而完整的遞迴，值得練到能默寫。

#### 0-1) DFS 走訪寫法（先動作，再靠比較往下遞迴）
```python
# python
# DFS traversal form: act on the node, then recurse by comparison.
# NOTE: comparing target against root.val to pick a side only works on a BST --
#       on a general binary tree you must recurse into BOTH children.
def dfs(root, target):
    # base case: a missing child ends the walk. Without it root.val raises AttributeError.
    if not root:
        return

    if root.val == target:
        pass          # do sth

    if root.val < target:
        dfs(root.left, target)
        pass          # do sth

    if root.val > target:
        dfs(root.right, target)
        pass          # do sth
```

#### 0-2) 把二元樹每個 node.value 都加 1？
```python
# Example) Add 1 to all node.value in Binary tree?
def dfs(root):
    if not root:
        return 
    root.val += 1 
    dfs(root.left)
    dfs(root.right)
```

#### 0-3) 檢查兩棵二元樹是否相同
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
        if root1.val != root2.val:
            return False 
    return dfs(root1.left, root2.left) \
           and dfs(root1.right, root2.right)
```

#### 0-4) 檢查某個值是否存在於 BST
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

#### 0-5) 取得子樹的總和

```python
# get sum of sub tree
# LC 508 Most Frequent Subtree Sum
# NOTE: `res` must exist before the first call -- pass it in rather than relying
#       on a module-level name, or the recursion raises NameError.
def get_sum(root, res):
    if not root:
        return 0
    ### NOTE THIS !!!
    #  -> we need to do get_sum(root.left), get_sum(root.right) on the same time
    s = get_sum(root.left, res) + root.val + get_sum(root.right, res)
    res.append(s)
    return s

# caller
res = []
get_sum(root, res)
```

#### 0-6) 取得樹中每個節點的 `累加總和`
```python
# LC 663 Equal Tree Partition
# LC 508 Most Frequent Subtree Sum
seen = []
def _sum(root):
    if not root:
        return 0
    seen.append( root.val + _sum(root.left) + _sum(root.right) )
```

#### 0-7) 把 BST 轉成 Greater Tree
```python
# Convert BST to Greater Tree 
# LC 538
# NOTE: `_sum` is read AND written, so it must be declared global -- without the
#       declaration `_sum += root.val` makes it local and raises UnboundLocalError.
#       The base case is what stops the walk at a missing child.
_sum = 0
def dfs(root):
    global _sum
    if not root:
        return
    dfs(root.right)
    _sum += root.val
    root.val = _sum
    dfs(root.left)
```

#### 0-8) Serialize and Deserialize Binary Tree

> Python 版本：見下方 [2-20) LC 297](#2-20-serialize-and-deserialize-binary-tree--lc-297)。

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

#### 0-10) 找節點之間的最長距離
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

#### 0-11) 把節點的值和路徑比較
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

#### 0-12) 用 `visited` 集合做網格 DFS
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

#### 0-13) 閉包：在巢狀的 `dfs` 裡讀取外層作用域的變數
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
    # NOTE: `z` is local to test(), so it has to be returned -- reading it at module
    #       scope raises NameError. That is the point: the closure can *see* z, but
    #       the caller cannot.
    return z

print(test())   # [0, 1, 2]
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
        root.right = deleteNodeHelper_0(root.right, minNode.val); // delete successor
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

#### 深入探討 —— 子樹簽章 + 雜湊表，這不是路徑題 ⭐⭐⭐⭐

> 「我覺得這是一題樹的*路徑*問題？」—— **不是。**路徑題（LC 112 / 113 / 257）
> 追蹤的是一條 *root → leaf* 的節點線。LC 652 問的則是兩棵**完整子樹**
> 結構上是否一模一樣。訣竅是給每棵子樹一個**標準簽章**，
> 再用雜湊表數每個簽章出現幾次。它屬於
> **[dfs.md 模板 8 —— 路徑簽章／形狀編碼](./dfs.md#template-8-path-signature-shape-encoding--lc-694)**
> —— 也就是「相異島嶼」在樹上的對應版本。

**1) 核心想法**

- **後序序列化**：一棵子樹可以被 `val + signature(left) + signature(right)` 完整描述。
  子節點必須*先*編碼，父節點才編碼 → **後序 DFS**（由下而上）。
- **雜湊表計數**：相同的子樹會產生相同的簽章字串。
  每個簽章各記一個計數器；第一次數到 **2** 的時候，那棵子樹就是重複的。
- **加進 `root`，而且只加一次**：在簽章**第二次**出現時才收集節點
  （在遞增*前*用 `if count == 1`，或在遞增*後*用 `if count == 2`），這樣每種重複子樹
  都只回報一次 —— 就算它出現 3 次以上也一樣。

**2) 模式／辨識**

| 訊號 | 它在告訴你什麼 |
|--------|-------------------|
| 「重複／完全相同的**子樹**」、「結構和值都一樣」 | 序列化 + 雜湊表 |
| 要比較的是*整棵子樹*，不是單一條 root→leaf 線 | 這**不是**路徑題 |
| 答案是由子節點往上組出來的 | **後序** DFS |
| 需要分隔符（`,`）+ null 標記（`#`） | 避免簽章有歧義 |

```text
Encoding rules (why each piece matters):
  "#"   -> null child       (distinguishes shapes: a node w/ 1 child vs 2)
  ","   -> field delimiter  (so vals "1,12" never collide with "11,2")
  post-order -> children serialized first, parent reuses their result
Complexity: O(n) nodes, but each signature is O(n) long -> O(n^2) time / space worst case.
  (Use an int-id map instead of raw strings to get true O(n) — see V2 in the .py file.)
```

**3) 相似 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 652 | Find Duplicate Subtrees | 本題 —— 子樹簽章 + 計數 |
| 694 | Number of Distinct Islands | 網格版本 —— 把形狀編碼，用 `set` 去重 |
| 449 | Serialize / Deserialize BST | 同樣的序列化想法，編碼→解碼 |
| 297 | Serialize / Deserialize Binary Tree | 標準的（前序／後序 + `#`）編碼 |
| 572 | Subtree of Another Tree | 比對單一子樹（也可以用簽章比較） |
| 508 | Most Frequent Subtree Sum | 由下而上的子樹彙總 + 雜湊表計數 |
| 1948 | Delete Duplicate Folders in System | 652 的一般化 —— 把子樹序列化、標出重複的 |

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

#### 四方向鄰居呼叫的兩種寫法
- 圖的走訪（DFS）：往 4 個方向走（上、下、左、右）
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

**模式：**
每一條 root 到 leaf 的路徑，都代表一個由上往下把數字接起來的數（例如 `1 -> 2 -> 3` = `123`）。要看出這是一題**路徑編碼 DFS**：不要像 LC 113 那樣把路徑收進 list／字串、到葉子才合併（用 `sum`／`+`），而是把一個**累積值**沿著遞迴往下帶，每個節點用 O(1) 更新它 —— 到葉子時不需要任何後處理。

**核心想法：**
把一個數字 `d` 接到 `curr` 後面，就只是 `curr * 10 + d`（跟從數字字串組出整數是同一個想法）。把這個累加器當成函式參數傳下去，每個遞迴呼叫自然就有自己的作用域 —— 不需要明確回溯（`path.pop()`），因為每個 stack frame 都持有自己那份 `curr`（傳值），而不是共用一個可變的 list：

```text
curr = 0
depth 1 (root=1):  curr = 0*10 + 1 = 1
depth 2 (node=2):   curr = 1*10 + 2 = 12
depth 3 (node=3):   curr = 12*10 + 3 = 123   <- leaf, add 123 to running total
```

到葉子時（`not root.left and not root.right`），`curr` 已經是那條路徑的完整數字了 —— 直接回傳即可。再把左右子樹回傳的葉子值加總。

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

**路徑 list 版本（等價，但需要明確回溯）：**
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

**為什麼偏好累加器寫法：**把 `curr` 當成不可變參數傳下去（`curr * 10 + node.val`），代表每條遞迴分支都免費拿到自己獨立的一份 —— 沒有共用的可變狀態，也就不需要回溯的記帳。這和 LC 113 的 `path + [val]`（每次呼叫都新建 list，不用 pop）對上 `path.append/pop`（共用 list，需要明確復原）是同一組取捨。

**相似 LC 題目（用累加器做 root-to-leaf 路徑編碼）：**
| 題目 | 模式 |
|---------|---------|
| LC 129 - Sum Root to Leaf Numbers | `curr = curr * 10 + val` —— 十進位數字接龍 |
| LC 257 - Binary Tree Paths | 把路徑累積成用 `"->"` 串起來的字串，到葉子收集 |
| LC 112 - Path Sum | 用相減累積剩下的目標值（`sum - root.val`），而不是往上組 |
| LC 113 - Path Sum II | 和 112 一樣，但在每個合法葉子收集實際的路徑 list |
| LC 988 - Smallest String Starting From Leaf | 由下而上（葉到根）把路徑累積成字串，再比字典序 |

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

#### 深入探討 —— 傳遞性的相似其實就是圖的連通性 ⭐⭐⭐⭐

> 雖然題目包著「句子／單字」的外皮，這其實是一題**圖連通性**問題，
> **不是**字串問題。每個 `similarPair` 都是一條**無向邊**；相似關係具有
> **傳遞性**（`a~b, b~c ⇒ a~c`），這正好就是「這兩個節點在不在同一個
> 連通分量裡？」。（對照 LC 734 *Sentence Similarity I* —— 沒有傳遞性，
> 所以查一個 set 就夠了，不需要圖。）

**1) 核心想法**

- 從 `similarPairs` **建一張無向圖**：`graph[a].add(b)`、`graph[b].add(a)`。
- 對每組對齊的單字 `(w1, w2)`：
  - `w1 == w2` → 依定義相似（一個字和自己相似）→ 跳過。
  - 否則從 `w1` 做 **DFS/BFS** 試著走到 `w2`；走不到就 return `False`。
- 長度不一樣 → 直接 `False`。

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

**2) 模式／辨識**

| 訊號 | 它在告訴你什麼 |
|--------|-------------------|
| 關係具有**傳遞性**（`a~b, b~c ⇒ a~c`） | 連通分量問題 |
| 「X 和 Y 有沒有關聯／連通／在同一組」 | DFS / BFS / **併查集** |
| 邊以成對形式給定，而且要查很多組 (x,y) 可達性 | 優先用**併查集**（每次查詢接近 O(1)） |
| 一開始就要把起點放進 `visited` | 避免在有環的圖上無限迴圈 |

```text
3 interchangeable engines (same idea, different machinery):
  DFS / BFS   -> per-query graph traversal      | O((V+E)) per query
  Union-Find  -> union all pairs, then find()    | ~O(α(n)) per query  <- best for many queries
Don't forget: w1 == w2 short-circuits TRUE even if the word isn't in the graph.
```

**3) 相似 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 737 | Sentence Similarity II | 本題 —— 有傳遞性 → 檢查連通分量 |
| 734 | Sentence Similarity I | **沒有**傳遞性 → 查 set 就好（不用建圖） |
| 547 | Number of Provinces | 數連通分量（DFS／併查集） |
| 200 | Number of Islands | 網格上的連通分量 |
| 990 | Satisfiability of Equality Equations | `==`／`!=` 限制 → 併查集 |
| 684 | Redundant Connection | 找出造成環的那條邊（併查集） |
| 399 | Evaluate Division | 連通性 + 帶權（比值）邊 |

**4) 觀念 —— 為什麼提早 `return False` 不會毀掉整個 DFS**

> 這個模板最常見的困惑：
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
> **不會。**一個 `return` 只會往遞迴堆疊上跳**一層** —— 回到*呼叫它的人*，
> **不是**回到最外層的呼叫。`False` 只是結束*那一條分支*，讓父層的
> `for` 迴圈繼續走下一個鄰居。只有 `True` 會一路往上傳
> （因為每個呼叫端都寫著 `if helper(...): return True`）。

**走查** —— 圖 `A→[B,C]`、`B→[D]`、`C→[E]`；呼叫 `helper(A, target=E)`：

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

第一個 `False`（來自 `B→D` 那條分支）**沒有**中止搜尋 —— 它只結束了那條分支，
`helper(A)` 裡的迴圈接著繼續走 `C`。

**`if node in visited: return False` 也是同樣的道理** —— 在有環的圖上
（`A↔B`、`A↔C`）：`helper(A)→helper(B)→helper(A)` 撞到 `A in visited` 就回傳 `False`
*而且只回傳給 `helper(B)`*。它的意思是「別再從 A 重搜一次」，不是「放棄」。控制權
回到 `helper(A)` 的迴圈，接著正常地去探索 `C`。什麼都沒被切掉。

> **核心想法**：最下面那行 `return False` **只有在每個鄰居都試過之後**才會執行。
> 某個子節點回傳 `False` 只是讓 `for` 迴圈往前走；整個 DFS 只有在*所有*分支都走完
> 卻沒碰到目標時，才會回報 `False`。

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
                
            # NOTE: the token is text -- without int() every node value is a str,
            #       and any later comparison or arithmetic on the rebuilt tree is wrong.
            root = TreeNode(int(l[0]))
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

**模式 —— 連通性／矛盾檢查（等式分群）**

- **描述**：給一堆等式（`==`）和不等式（`!=`）限制，判斷它們能否同時成立。用 `==` 的邊建圖，再驗證沒有任何 `!=` 的兩端其實是連通的。
- **辨識**：「等式方程」、「變數相等／不相等」、「可滿足性」、「先按等價關係分群再找矛盾」，以及具有**傳遞性**的關係（`a==b`、`b==c` ⟹ `a==c`）
- **關鍵技巧**：**兩階段**處理 —— (1) 用所有 `==` 關係建一張**無向**圖；(2) 對每個 `!=` 關係做 DFS 檢查可達性。如果兩個「必須不同」的變數是連通的 → 矛盾 → 回傳 False。
- **例題**：LC 990（Satisfiability of Equality Equations）
- **核心演算法想法**：
  1. **建圖**：對每個 `x==y`，**兩個方向都要加** `x→y` 和 `y→x`（無向）。`==` 這個關係同時是對稱且傳遞的，所以連通分量 = 等價類。
  2. **掃矛盾**：對每個 `x!=y`，從 `x` 跑 DFS；如果走得到 `y`，代表圖逼著它們相等、但題目要求不等 → **無法滿足**。
  3. 一定要**先處理完所有 `==`**，再處理**所有 `!=`** —— 在群組還沒建完就看 `!=`，會得到錯的答案。
- **重要提醒**：
  - ⚠️ **圖一定要是雙向的。**在*單向*圖上呼叫 `dfs(a,b)` 和 `dfs(b,a)` **並不等價** —— 對於 `a==b, b==c`，單向的 `dfs(c, a)` 找不到任何出邊，就會錯誤地回傳 False。兩個方向都要存。
  - DFS 之前**不需要**先檢查 `if y in graph[x]` —— DFS 自然涵蓋了直接相鄰的情況（第一跳的遞迴就會命中 `cur == target`）。
  - `a!=a` 這種自我不等式本質上無法滿足；DFS 會因為 `cur == target` 立刻回傳 True（gemini 那個版本有明確擋掉）。
  - `visited` 集合要**每個 `!=` 查詢重設一次**，讓每次可達性檢查各自獨立探索。
- **另一種（更乾淨的）做法：併查集** —— 每個 `==` 做一次 `union(x,y)`；接著對每個 `!=`，如果 `find(x)==find(y)` 就回傳 False。時間 `O(N·α)`，通常也是面試比較想聽到的答案。見 [union_find.md](./union_find.md)。
- **DFS vs 併查集的取捨**：DFS 每次 `!=` 檢查是 `O(V+E)`（整體可能到 `O(N²)`）；併查集每次查詢接近 `O(1)` —— 但 DFS 能強化「圖連通性」的心智模型。
- **相似的經典 LC 題目**：
  - LC 990 - Satisfiability of Equality Equations（等式分群 + 找矛盾的標準題）
  - LC 547 - Number of Provinces（用 DFS／併查集數連通分量）
  - LC 200 - Number of Islands（網格上的連通分群）
  - LC 721 - Accounts Merge（依共用 email 合併 → 分量）
  - LC 684 - Redundant Connection（找出造成環的那條邊 —— 併查集）
  - LC 399 - Evaluate Division（傳遞關係的帶權版本 —— [dfs.md 模板 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399)）
  - LC 785 - Is Graph Bipartite?（二著色 = 一種「必須不同組」的限制檢查）

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

**併查集版本**（更乾淨，接近 `O(N·α)`）：

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

**容易踩到的坑**：`==` 的圖一定要雙向。對於 `a==b, b==c`，單向圖會讓 `dfs(c, a)` 失敗（`c` 沒有出邊），錯誤地回報可滿足 —— `x→y` 和 `y→x` 都要存。

---

### 2-28) Print Binary Tree — LC 655

> **DFS + 固定大小的矩陣**。先算出樹高，據此開一個 `(height+1) × (2^(height+1)-1)` 的字串網格，把 root 放在正中間那一欄，然後用 DFS 把每個子節點放在**每層減半的水平位移** `2^(height-row-1)` 處。

**核心想法**：網格的尺寸在走訪*之前*就定好了（純粹由樹高推出來），所以 DFS 只需要 `(row, col)` —— 不用動態調整大小。每往下一層，水平展開就減半，剛好對應二元樹分岔的方式。

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

**為什麼 `get_tree_height` 對 null 回傳 `-1`**：這樣單節點樹的高度就是 `0`，於是 `rows = 1`，節點正好落在唯一那一列。如果 null 回傳 `0`，所有高度都會多算一，網格就會多出一列。

**位移的直覺**：最上面那列，子節點必須跳整個寬度的四分之一；再往下一層，就是它的一半；以此類推。`2^(height-row-1)` 精準地編碼了這個等比減半，子節點才不會撞在一起，版面也才會對稱。

| 步驟 | 公式 | 為什麼 |
|------|---------|-----|
| 列數 | `height + 1` | 每層一列 |
| 欄數 | `2^(height+1) - 1` | 最底層可能的最大寬度，也讓版面對稱 |
| root 的欄 | `(cols - 1) // 2` | 最上面那列的正中央 |
| 子節點位移 | `2^(height - row - 1)` | 每層減半，子樹才不會重疊 |

---

### 2-29) Add One Row to Tree — LC 623 ⭐⭐⭐⭐

> **帶倒數深度的 DFS**。在 `depth` 這一層插入一排值為 `val` 的節點。不要去追絕對層數，
> 而是**每次遞迴呼叫都把 `d` 減 1**，等 `d == 2` 時觸發 base case —— 這時*當下*這個
> 節點就是那個要重接子指標的父節點。原本的左子樹掛到新左節點的 `.left`，
> 原本的右子樹掛到新右節點的 `.right`。

**1) 核心想法**

- **往下數，不要往上數。** BFS 需要 `cur_depth == depth - 1`；DFS 只要把 `d - 1`
  往下傳，在 `d == 2` 停下來就好，遞迴裡不用穿一個深度變數。
  `d == 2` 的意思是「我的子節點就是目標那一列」—— 也就是**我是 `depth - 1` 那個父節點**。
- **兩個 base case，順序如下**：
  - `d == 1` → 沒有父層可言；建一個**新的 root**，把整棵原樹掛在它的
    **左邊**。這只可能在*最外層*的呼叫發生（見下面的說明）。
  - `d == 2` → 重接*這個*節點的子指標：建兩個 `val` 節點，再把舊的子樹接回去。
- **覆蓋前先存起來。** `root.left = TreeNode(v)` 會毀掉原本的指標。Python 的
  tuple 賦值*在順序正確時*能安全處理這件事：
  ```python
  root.left, root.left.left = TreeNode(v), root.left
  #    ^target 1  ^target 2      ^new node    ^OLD subtree (RHS evaluated FIRST)
  ```
  整個右手邊會在任何賦值發生前先算完（所以那裡的 `root.left` 還是*舊的*子節點），
  接著目標由**左到右**依序賦值：`root.left` 先變成新節點，然後
  `root.left.left`（也就是那個新節點）接到舊子樹。兩個目標調換順序就壞了。
- **接在外側**：舊左 → `new_left.left`，舊右 → `new_right.right`。接在內側會把子樹鏡射掉。
- **子節點是 `None` 沒關係** —— 位於 `depth - 1` 但沒有子節點的節點，一樣會長出兩個新
  子節點，而 `new.left = None` 正是我們要的。只有 `root` 自己需要防 null。
- **DFS 自然會剪枝**：遞迴在 `d == 2` 就停了，所以永遠不會走到插入那列的下面 ——
  它沒去走訪的節點，剛好就是它不該碰的那些。不像 BFS 版本還要 `break`／`return` 來擋。

**2) 模式**

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

**變形 —— 原地修改、忽略回傳值**（也是對的，以及為什麼）：

```python
# python — the (3) branch of the function above, with the recursive calls
#          NOT reassigned. Shown with its enclosing method so it parses on its own.
class Solution(object):
    def addOneRow(self, root, v, d):
        # ... branches (1) and (2) unchanged, see above ...

        # (3) still above the target row -> count down
        self.addOneRow(root.left,  v, d - 1)
        self.addOneRow(root.right, v, d - 1)
        return root
```

這之所以可行，是因為唯一會*替換*節點（而不是就地修改）的分支是 `d == 1`，而遞迴裡
`d` 永遠到不了 `1` —— 它一路 `d → d-1` 往下降，在 `2` 就停住了。所以每次遞迴呼叫都是
就地修改自己的參數，父節點的指標始終有效。
但還是建議用**重新賦值**的寫法：不管觸發哪個 base case 它都正確，而且改寫 base case
之後也還撐得住。

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

**這題的 DFS vs BFS**

| | DFS（本節） | BFS（見 [bfs.md §2-17](./bfs.md)） |
|---|---|---|
| 深度追蹤 | 隱含 —— 倒數 `d - 1`，在 `d == 2` 停 | 明確的 `cur_depth`，在 `depth - 1` 停 |
| 空間 | `O(h)` 遞迴堆疊 | `O(W)` 佇列（最大層寬） |
| 停止方式 | 自動（遞迴自己結束） | 需要明確的 `break`／`return` |
| 程式長度 | 最短 | 比較囉唆，但沒有爆堆疊的風險 |
| 風險 | ⚠️ 限制裡 `depth` 可到 `10^4` → 極度傾斜的樹可能超過 Python 預設的遞迴上限（1000） | 沒有 |

> 因為限制允許樹深達 `10^4`，DFS 版本在退化成鏈結串列形狀的樹上可能需要
> `sys.setrecursionlimit(...)`；BFS 版本沒有這個限制。DFS 是面試時比較漂亮的答案，
> BFS 則是在最大輸入下比較安全的那個。

**容易踩到的坑**

| 坑 | 為什麼會壞 |
|---|---|
| 遞迴停在 `d == 1` | 太深了 —— 要重接的指標在父節點身上，而 `d == 1` 是*新 root* 的情況 |
| `root.left.left, root.left = root.left, TreeNode(v)` | 目標順序錯了 —— `root.left.left` 寫到了**舊**子節點上，接著整個被蓋掉 |
| `new_left.right = old_left`（接在內側） | 會把子樹鏡射掉；必須分別是 `.left` / `.right` |
| 省略 `if not root: return None` | `d == 2` 時會在 null 節點上取 `root.left` |
| 沒有寫 `root.left = self.addOneRow(...)` 重新賦值 | 只是碰巧安全（見上面的變形）；一旦某個 base case 開始回傳*新*節點就會壞 |

**3) 相似 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 623 | Add One Row to Tree | 本題 —— DFS 倒數到 `d == 2`，重接子指標 |
| 226 | Invert Binary Tree | 同樣有「先存再賦值」的子指標陷阱 |
| 617 | Merge Two Binary Trees | DFS 回傳（可能是新的）子樹 root —— 重新賦值的寫法 |
| 654 | Maximum Binary Tree | 在 DFS 過程中建節點並往上回傳 |
| 971 | Flip Binary Tree To Match Preorder | 在走訪途中修改左右連結 |
| 116 / 117 | Populating Next Right Pointers | 也是重接指標，但是逐層做（適合 BFS） |
| 655 | Print Binary Tree | [2-28)](#2-28-print-binary-tree--lc-655) —— DFS 把推導出來的深度／位移往下帶 |
| 111 / 104 | Min / Max Depth of Binary Tree | 這題所仰賴的數深度遞迴 |

> **模式帶走的重點**：「在深度 `d` 做 X」⇒ 用 `d - 1` 遞迴，並在 **`d == 2`** 動手，因為
> 你真正能修改的節點是目標那列的*父節點*。先把舊的子指標算完再賦值新的、接在外側，
> 並回傳子樹 root，呼叫端的連結才會正確。

---

## 依模式分類的題目

### 依模式的題目分類

`Template N` 指的是 [dfs.md → Templates & Algorithms](./dfs.md#templates--algorithms)；
`*adv* TN` 指的是 [dfs_advanced.md](./dfs_advanced.md)。

#### **模式 1：樹的走訪**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Binary Tree Inorder Traversal | 94 | Easy | 堆疊／遞迴 | Template 1 |
| Binary Tree Preorder Traversal | 144 | Easy | 堆疊／遞迴 | Template 1 |
| Binary Tree Postorder Traversal | 145 | Easy | 堆疊／遞迴 | Template 1 |
| Serialize and Deserialize Binary Tree | 297 | Hard | DFS 編碼 | Template 1 |
| Serialize and Deserialize BST | 449 | Medium | BST 性質 | Template 1 |
| Binary Tree Paths | 257 | Easy | 追蹤路徑 | Template 3 |
| Same Tree | 100 | Easy | 同步 DFS | Template 1 |

#### **模式 2：路徑問題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Path Sum | 112 | Easy | DFS 走訪 | Template 3 |
| Path Sum II | 113 | Medium | 回溯 | Template 3 |
| Binary Tree Maximum Path Sum | 124 | Hard | 全域最大值 | Template 6 |
| Diameter of Binary Tree | 543 | Easy | 由下而上 | Template 6 |
| Longest Univalue Path | 687 | Medium | 由下而上 | Template 6 |
| Sum Root to Leaf Numbers | 129 | Medium | 追蹤路徑 | Template 3 |

#### **模式 3：圖的走訪**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Number of Islands | 200 | Medium | 網格 DFS | Template 2 |
| Max Area of Island | 695 | Medium | 網格 DFS | Template 2 |
| Clone Graph | 133 | Medium | HashMap | Template 2 |
| Course Schedule | 207 | Medium | 偵測環 | Template 2 |
| Course Schedule II | 210 | Medium | 拓撲排序 | Template 2 |
| Pacific Atlantic Water Flow | 417 | Medium | 多源 | Template 2 |
| Evaluate Division | 399 | Medium | 圖的走訪 | Template 2 |
| Minesweeper | 529 | Medium | 網格探索 | Template 2 |

#### **模式 4：回溯**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Permutations | 46 | Medium | 回溯 | Template 4 |
| Subsets | 78 | Medium | 回溯 | Template 4 |
| Combination Sum | 39 | Medium | 回溯 | Template 4 |
| Letter Combinations | 17 | Medium | 回溯 | Template 4 |
| Generate Parentheses | 22 | Medium | 回溯 | Template 4 |
| Word Search | 79 | Medium | 網格回溯 | Template 4 |
| N-Queens | 51 | Hard | 回溯 | Template 4 |

#### **模式 5：修改樹結構**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Delete Node in BST | 450 | Medium | BST 刪除 | Template 5 |
| Insert into BST | 701 | Medium | BST 插入 | Template 5 |
| Trim a Binary Search Tree | 669 | Medium | 條件式修剪 | Template 5 |
| Convert BST to Greater Tree | 538 | Medium | 反向中序 | Template 5 |
| Invert Binary Tree | 226 | Easy | 交換左右子樹 | Template 5 |
| Flatten Binary Tree | 114 | Medium | 原地修改 | Template 5 |

#### **模式 6：子樹與彙總**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Most Frequent Subtree Sum | 508 | Medium | HashMap | Template 6 |
| Find Duplicate Subtrees | 652 | Medium | 序列化 | Template 6 |
| Lowest Common Ancestor | 236 | Medium | 由下而上 | Template 6 |
| Equal Tree Partition | 663 | Medium | 子樹總和 | Template 6 |
| Maximum Product of Splitted Tree | 1339 | Medium | 所有子樹總和 | Template 6 |
| Validate Binary Search Tree | 98 | Medium | 上下界 | Template 1 |
| Split BST | 776 | Medium | 遞迴切分 | Template 5 |

#### **模式 7：邊界消去（兩趟 DFS）**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Number of Closed Islands | 1254 | Medium | 從邊界淹水 | Template 7 |
| Surrounded Regions | 130 | Medium | 消去邊界相連者 | Template 7 |
| Pacific Atlantic Water Flow | 417 | Medium | 兩個海洋 | Template 7 |
| Number of Enclaves | 1020 | Medium | 與邊界相連 | Template 7 |

#### **模式 8：路徑簽章（形狀編碼）**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Number of Distinct Islands | 694 | Medium | 方向編碼 | Template 8 |
| Number of Distinct Islands II | 711 | Hard | 處理旋轉／鏡射 | Template 8 |
| Find Duplicate Subtrees | 652 | Medium | 樹的序列化 | Template 8 |
| Most Frequent Subtree Sum | 508 | Medium | 子樹簽章 | Template 8 |

#### **模式 9：帶驗證的 DFS（偵測子分量）**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Count Sub Islands | 1905 | Medium | 布林旗標往上傳 | *adv* T1 |
| Number of Islands | 200 | Medium | 基本的分量計數 | Template 2 |
| Max Area of Island | 695 | Medium | 追蹤分量大小 | Template 2 |
| Island Perimeter | 463 | Easy | 數邊 | Template 2 |
| Making A Large Island | 827 | Hard | 合併分量 | Template 2 |

#### **模式 10：帶方向追蹤的雙向圖**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Reorder Routes to Make All Paths Lead to the City Zero | 1466 | Medium | 雙向圖 + 方向旗標 | *adv* T2 |
| Minimum Number of Days to Disconnect Island | 1568 | Hard | 修改圖結構（相關） | - |
| Remove Max Number of Edges to Keep Graph Fully Traversable | 1579 | Hard | 邊的定向（相關） | - |

#### **模式 11：分量配對計數（不可達的配對）**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Count Unreachable Pairs of Nodes in an Undirected Graph | 2316 | Medium | 分量計數 + 累乘 | *adv* T3 |
| Number of Connected Components in an Undirected Graph | 323 | Medium | 基本的分量計數 | Template 2 |
| Number of Provinces | 547 | Medium | 偵測分量 | Template 2 |

### 依難度的完整題目清單

#### Easy 題（基礎）
- LC 94: Binary Tree Inorder Traversal - 基本 DFS
- LC 100: Same Tree - 平行 DFS
- LC 101: Symmetric Tree - 鏡像 DFS
- LC 104: Maximum Depth - 簡單遞迴
- LC 112: Path Sum - 追蹤路徑
- LC 144: Binary Tree Preorder Traversal - 使用堆疊
- LC 145: Binary Tree Postorder Traversal - 操作堆疊
- LC 226: Invert Binary Tree - 修改樹結構
- LC 257: Binary Tree Paths - 收集路徑
- LC 543: Diameter of Binary Tree - 全域最大值模式
- LC 572: Subtree of Another Tree - 子樹比對

#### Medium 題（核心）
- LC 98: Validate BST - 上下界檢查
- LC 113: Path Sum II - 回溯路徑
- LC 130: Surrounded Regions - 邊界消去
- LC 133: Clone Graph - HashMap + DFS
- LC 200: Number of Islands - 網格 DFS
- LC 207: Course Schedule - 偵測環
- LC 210: Course Schedule II - 拓撲排序
- LC 236: Lowest Common Ancestor - 由下而上的 DFS
- LC 297: Serialize/Deserialize Tree - DFS 編碼
- LC 399: Evaluate Division - 圖的 DFS
- LC 417: Pacific Atlantic Water Flow - 多源 DFS
- LC 450: Delete Node in BST - 重構樹結構
- LC 449: Serialize/Deserialize BST - BST 性質
- LC 472: Concatenated Words - Word break DFS
- LC 508: Most Frequent Subtree Sum - 彙總
- LC 529: Minesweeper - 網格探索
- LC 538: Convert BST to Greater Tree - 反向中序
- LC 652: Find Duplicate Subtrees - 序列化
- LC 663: Equal Tree Partition - 子樹總和
- LC 669: Trim BST - 條件式修改
- LC 695: Max Area of Island - 連通分量
- LC 701: Insert into BST - BST 插入
- LC 1466: Reorder Routes to Make All Paths Lead to the City Zero - 帶方向追蹤的雙向圖
- LC 1905: Count Sub Islands - 帶驗證的 DFS
- LC 2316: Count Unreachable Pairs of Nodes in an Undirected Graph - 分量配對計數
- LC 737: Sentence Similarity II - 圖的連通性
- LC 776: Split BST - 進階操作
- LC 1020: Number of Enclaves - 邊界消去
- LC 1254: Number of Closed Islands - 兩趟 DFS
- LC 1339: Maximum Product of Splitted Tree - 所有子樹總和

#### Hard 題（進階）
- LC 124: Binary Tree Maximum Path Sum - 全域最佳化
- LC 297: Serialize and Deserialize Binary Tree - 複雜編碼
- LC 51: N-Queens - 複雜回溯
- LC 329: Longest Increasing Path in Matrix - 記憶化 DFS
- LC 3319: K-th Largest Perfect Subtree - 複雜彙總
- LC 332: Reconstruct Itinerary - 尤拉路徑（Hierholzer），見 *adv* Template 4
- LC 753: Cracking the Safe - de Bruijn 圖上的尤拉迴路，見 *adv* Template 4
- LC 1192: Critical Connections in a Network - Tarjan 橋（low-link），見 *adv* Template 5

#### 其他高頻 DFS 題（參考）

這些是經典的 FAANG DFS 題，用的都是上面已經涵蓋的模板 —— 列出來只是求完整，沒有新技巧。

- LC 388: Longest Absolute File Path - 以深度為索引的堆疊 DFS（*adv* Template 7）
- LC 419: Battleships in a Board - 不用 flood fill 的分量計數（Template 2 變形）
- LC 211: Design Add and Search Words Data Structure - 字典樹（Trie）+ 萬用字元 DFS（*adv* Template 6）
- LC 676: Implement Magic Dictionary - 帶「錯配額度」的 Trie DFS（*adv* Template 6 變形）
- LC 1233: Remove Sub-Folders from the Filesystem - 路徑 Trie DFS + 提早剪枝（*adv* Template 7 變形）
- LC 863: All Nodes Distance K in Binary Tree - 用 DFS 補上 parent 連結，再把樹當成圖處理
- LC 337: House Robber III - 後序 DFS，每個節點回傳一組 `(rob, skip)` 狀態
- LC 947: Most Stones Removed with Same Row or Column - 在 row/column key 上的連通分量
- LC 690: Employee Importance - 在 `id -> employee` 的 map 上做 DFS，而不是鄰接串列
- LC 341: Flatten Nested List Iterator - 用明確的堆疊對巢狀結構做 DFS 展平
- LC 430: Flatten a Multilevel Doubly Linked List - 在鏈結串列上做 DFS；就地把 child 串列接進去
- LC 934: Shortest Bridge - 先用 DFS 標記一座島，再從它往外 BFS 到另一座

## 重點整理與速查

| 你想找 | 去哪裡 |
|---|---|
| 這裡任一解法背後的技巧 | [dfs.md → Templates & Algorithms](./dfs.md#templates--algorithms) |
| 某題屬於哪個模板 | 上面的[依模式分類的題目](#problems-by-pattern)索引 |
| 冷門模式（尤拉路徑、Tarjan、trie DFS、`parent[]` 樹） | [dfs_advanced.md](./dfs_advanced.md) |
| 同一道網格／樹題目的 BFS 解法 | [bfs.md](./bfs.md) |
| 某道樹題以樹為主軸的完整說明 | [tree.md](./tree.md)、[tree2.md](./tree2.md)、[bst.md](./bst.md) |

**怎麼讀有多段程式碼的條目**：同一個標題下有兩段程式碼時，中間的註解會說明第二段教了什麼 —— 不同的複雜度、不同語言的慣用寫法，或是一個獨立的技巧。其他情況都只是單一份標準解法。

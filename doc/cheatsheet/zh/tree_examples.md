# 樹 — LeetCode 實戰題解

> **範圍** — 其他檔案所教的樹模式，其題解檔案庫：每題每種語言各一份標準解法，依「題目在問什麼」分組，而不是依技巧分組。
> **另見**：[tree.md](./tree.md) — 這些範例所套用的觀念、走訪策略與模板；[tree_lca_distance.md](./tree_lca_distance.md) — LCA、距離與路徑題；[tree_codec.md](./tree_codec.md) — 序列化與 codec 題；[tree_construction.md](./tree_construction.md) — 建樹題。

## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)

## 總覽

十九題實戰題解，依它們問的問題分組。每一題都套用 [tree.md](./tree.md) 裡九種模式的其中一種；模式本身在那邊解釋，這裡不重複。

### 關鍵性質
- **複雜度**：除了 LC 222（完全二元樹上 O(log² N)）和 LC 545（掃三趟，仍是 O(N)）之外，全部都是 O(N) 時間
- **核心想法**：每個範例都是 tree.md 九種模式的其中一種，再加上該題自己的轉折
- **什麼時候用**：模式已經懂了、想看它落在真實題目上的時候

## 題型分類

| 分組 | 題目 | 主導模式 |
|-------|----------|------------------|
| **走訪與層序** | LC 199、662、2415 | 逐層記帳的 BFS |
| **結構與性質** | LC 222、101、100、951、98、110、545 | 後序驗證 |
| **高度、深度與路徑** | LC 104、111、124、1448 | 由下而上算高度／由上而下算深度 |
| **修改與多狀態** | LC 1110、114、617、226、968 | 後序重接指標、狀態往上回傳 |

## 走訪與層序範例

### 1) Tree Right Side View — LC 199

```java
// java

// LC 199
List<Integer> res = new ArrayList<>();
Queue<TreeNode> q = new LinkedList<>();
/** NOTE !!! seed the queue -- without this the loop never runs and res comes back empty */
if (root != null) {
    q.offer(root);
}
while (!q.isEmpty()) {
    TreeNode rightSide = null;
    int qLen = q.size();

    /**
     *  NOTE !!!
     *
     *   1) via for loop, we can get `most right node` (since the order is root -> left -> right)
     *   2) via `TreeNode rightSide = null;`, we can get the `most right node` object
     *      - rightSide could be `right sub tree` or `left sub tree`
     *
     *      e.g.
     *         1
     *       2   3
     *       
     *       
     *       1
     *     2   3
     *   4
     *
     */
    for (int i = 0; i < qLen; i++) {
        TreeNode node = q.poll();
        if (node != null) {
            rightSide = node;
            q.offer(node.left);
            q.offer(node.right);
        }
    }
    if (rightSide != null) {
        res.add(rightSide.val);
    }
}
```


**Python — DFS（右子樹優先的前序），一樣 O(n) 而且不用佇列：**

```python 
# LC 199 Binary Tree Right Side View
# V0
# IDEA : DFS
class Solution(object):
    def rightSideView(self, root):
        def dfs(root, layer):
            if not root:
                return
            if len(res) <= layer+1:
            #if len(res) == layer:     # this works as well
                res.append([])
            res[layer].append(root.val)
            if root.right:
                dfs(root.right, layer+1)
            if root.left:
                dfs(root.left, layer+1)
        if not root:
            return []
        res =[[]]
        dfs(root, 0)
        return [x[0] for x in res if len(x) > 0]
```

### 2) Maximum Width of Binary Tree — LC 662
```python
# LC 662 Maximum Width of Binary Tree
# V0
# IDEA : defaultdict + DFS
from collections import defaultdict
class Solution:
    def widthOfBinaryTree(self, root):
        
        def dfs(node, level, idx):
            if node:
                d[level] += [idx]
                dfs(node.left, level+1, 2*idx)
                dfs(node.right, level+1, 2*idx+1)
                
        d = defaultdict(list)
        dfs(root, 0, 0)
        return max(v[-1] - v[0] + 1 for _, v in d.items())

# V0'
# IDEA : BFS
# IDEA : GIVEN index = idx -> its left tree index = idx*2 ; its right tree index = idx*2 + 1
#        -> SO GO THROUGH ALL LAYERS IN THE TREE, CALCULATE THEIR WIDTH, AND RETRUN THE MAX WIDTH WHICH IS THE NEEDED RESPONSE
from collections import defaultdict
class Solution(object):
    def widthOfBinaryTree(self, root):
        # edge case
        if not root:
            return 0
        layer = 0
        idx = 0
        q = [[root, layer, idx]]
        res = defaultdict(list)
        while q:
            for i in range(len(q)):
                tmp, layer, idx = q.pop(0)
                res[layer].append(idx)
                if tmp.left:
                    q.append([tmp.left, layer+1, idx*2])
                if tmp.right:
                    q.append([tmp.right, layer+1, idx*2+1])
        #print ("res = " + str(res))
        # NOTE: res[x] is the LIST of indices on level x, so compare its LENGTH.
        #       Every level contributes -- dropping single-node levels leaves max()
        #       with an empty sequence on a one-node tree.
        _res = [max(res[x]) - min(res[x]) + 1 for x in res if len(res[x]) >= 1]
        #print ("_res = " + str(_res))
        return max(_res)
```

### 3) Reverse Odd Levels of Binary Tree — LC 2415

```java
// java
// LC 2415

// V0-1
// IDEA: DFS + `left, right, layer as helper func parameter` (fixed by gpt)
public TreeNode reverseOddLevels_0_1(TreeNode root) {
    if (root == null)
        return null;

    reverseHelper(root.left, root.right, 1);
    return root;
}

/**
 *  NOTE !!!
 *
 *   we NEED to setup 3 parameter in the helper func
 *
 *   1. left node
 *   2. right node
 *   3. layer
 *
 *
 *  NOTE !!!
 *
 *   the helper func return NOTHING !!! (e.g. void)
 */
private void reverseHelper(TreeNode left, TreeNode right, int level) {
    if (left == null || right == null)
        return;

    // Swap values if we're at an odd level
    if (level % 2 == 1) {
        int temp = left.val;
        left.val = right.val;
        right.val = temp;
    }

    /**  NOTE !!! below
     *
     *
     */
    // Recurse into symmetric children
    reverseHelper(left.left, right.right, level + 1);
    reverseHelper(left.right, right.left, level + 1);
}
```

## 結構與性質範例

### 4) 節點計數演算法 — LC 222
```java
// get nodes count of binary tree

// get nodes count of perfect tree

// get nodes count of complete tree
// LC 222

// dfs
class Solution {
    public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Recursively count the nodes in the left subtree
        int leftCount = countNodes(root.left);

        // Recursively count the nodes in the right subtree
        int rightCount = countNodes(root.right);

        // Return the total count (current node + left subtree + right subtree)
        return 1 + leftCount + rightCount;
    }
}


// bfs
public int countNodes_2(TreeNode root) {

    if (root == null){
        return 0;
    }
    List<TreeNode> collected = new ArrayList<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.add(root);
    while (!q.isEmpty()){
        TreeNode cur = q.poll();
        collected.add(cur);
        if (cur.left != null) {
            q.add(cur.left);
        }
        if (cur.right != null) {
            q.add(cur.right);
        }
    }

    //return this.count;
    System.out.println("collected = " + collected.toString());
    return collected.size();
}
```


> 計數問題有三種樣態 —— 一般樹、完美樹、完全樹（LC 222）—— 差別只在「結構讓你能跳過多少東西」。

#### 在 `basic` 二元樹上數節點
```java
// java
// algorithm book (labu) p. 250
public int countNodes (TreeNode root){
    if (root == null) return 0;
    return 1 + countNodes(root.left) + countNodes(root.right);
}
```

#### 在 `perfect` 二元樹上數節點
```java
// java
// algorithm book (labu) p. 250
public int countNodes(TreeNode root){
    int h = 0;
    // get tree depth
    while (root != null){
        root = root.left;
        h += 1;
    }
    // total nodes = 2**n + 1
    return (int)Math.pow(2, h) - 1;
}
```

#### 在 `complete` 二元樹上數節點
```java
// java
// algorithm book (labu) p. 251
public int countNodes(TreeNode root){

    TreeNode l = root;
    TreeNode r = root;
    int hl = 0;
    int hr = 0;

    while (l != null){
        l = l.left;
        hl += 1;
    }

    while (r != null){
        r = r.right;
        hr += 1;
    }

    // if left, right sub tree have SAME depth -> this is a perfect binary tree
    if (hl == hr){
        return (int)Math.pow(2, hl) - 1;
    }

    // if left, right sub tree have DIFFERENT depth, then we follow the simple bianry tree approach
    return 1 + countNodes(root.left) + countNodes(root.right);
}
```

### 5) 檢查對稱樹 — LC 101
```python
# LC 101
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

### 6) Same Tree — LC 100
```python
# LC 100 Same tree
# V0
# IDEA : Recursion
class Solution(object):
    def isSameTree(self, p, q):
        
        def dfs(p, q):
            ### NOTE : we need to put this as 1st condition, or will cause "not sub tree" error
            if not p and not q:
                return True
            ### NOTE : elif (but not `if`)
            elif (not p and q) or (p and not q):
                return False
            ### NOTE : elif (but not `if`)
            elif p.val != q.val:
                return False
            return dfs(p.left, q.left) and dfs(p.right, q.right)
        
        res = dfs(p, q)
        return res
```

#### 變形 — Flip Equivalent Binary Trees (LC 951)

**轉折**：骨架和 LC 100 一樣，但每個節點的左右子樹允許**互換** —— 所以不是做一次遞迴檢查，而是兩種配對都試，再 `or` 起來。

```java
// java
// LC 951 - Flip Equivalent Binary Trees
// IDEA: LC 100 (Same Tree) + at each node accept `left-left / right-right`
//       OR the flipped `left-right / right-left` pairing
class Solution {
    public boolean flipEquiv(TreeNode root1, TreeNode root2) {
        // time = O(N), space = O(H)
        if (root1 == null && root2 == null) return true;
        if (root1 == null || root2 == null || root1.val != root2.val) return false;

        return (flipEquiv(root1.left, root2.left) && flipEquiv(root1.right, root2.right))   // not flipped
            || (flipEquiv(root1.left, root2.right) && flipEquiv(root1.right, root2.left));  // flipped
    }
}
```

```python
# python
# LC 951 - Flip Equivalent Binary Trees
# IDEA: Same Tree check, but allow the children pair to be swapped at each node
class Solution:
    def flipEquiv(self, root1, root2):
        # time = O(N), space = O(H)
        if not root1 and not root2:
            return True
        if not root1 or not root2 or root1.val != root2.val:
            return False
        return (self.flipEquiv(root1.left, root2.left) and
                self.flipEquiv(root1.right, root2.right)) or \
               (self.flipEquiv(root1.left, root2.right) and
                self.flipEquiv(root1.right, root2.left))
```

> 千萬不要想先把兩棵樹「正規化」（例如把子樹依值排序）—— 值只在這題的限制下才唯一；雙向檢查才是通用寫法。

### 7) Validate Binary Search Tree — LC 98
```python
# 98. Validate Binary Search Tree
# V0
# IDEA : BFS
#  -> trick : we make sure current tree and all of sub tree are valid BST
#   -> not only compare tmp.val with tmp.left.val, tmp.right.val,
#   -> but we need compare if tmp.left.val is SMALLER then `previous node val`
#   -> but we need compare if tmp.right.val is BIGGER then `previous node val`
class Solution(object):
    def isValidBST(self, root):
        if not root:
            return True
        _min = -float('inf')
        _max = float('inf')
        ### NOTE : we set q like below
        q = [[root, _min, _max]]
        while q:
            for i in range(len(q)):
                tmp, _min, _max = q.pop(0)
                if tmp.left:
                    ### NOTE : below condition
                    if tmp.left.val >= tmp.val or tmp.left.val <= _min:
                        return False
                    ### NOTE : we append tmp.val as _max
                    q.append([tmp.left, _min, tmp.val])
                if tmp.right:
                    ### NOTE : below condition
                    if tmp.right.val <= tmp.val or tmp.right.val >= _max:
                        return False
                    ### NOTE : we append tmp.val as _min
                    q.append([tmp.right, tmp.val, _max])
        return True

# V0'
# IDEA: RECURSION 
class Solution(object):
    def isValidBST(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        return self.valid(root, float('-inf'), float('inf'))
        
    def valid(self, root, min_, max_):
        if not root: return True
        if root.val >= max_ or root.val <= min_:
            return False
        return self.valid(root.left, min_, root.val) and self.valid(root.right, root.val, max_)
```

### 8) Balanced Binary Tree — LC 110
```java
// java
// LC 110


// V0
// IDEA : DFS
// https://www.bilibili.com/video/BV1Ug411S7my/?share_source=copy_web
public boolean isBalanced(TreeNode root) {
    // edge
    if (root == null) {
        return true;
    }
    if (root.left == null && root.right == null) {
        return true;
    }

    int leftDepth = getDepthDFS(root.left);
    int rightDepth = getDepthDFS(root.right);

    // check if `current` node is `balanced`
    if (Math.abs(leftDepth - rightDepth) > 1) {
        return false;
    }

    // dfs call
    // recursively check if `sub left node` and  `sub right node` are `balanced`
    return isBalanced(root.left) && isBalanced(root.right);
}

// LC 104
public int getDepthDFS(TreeNode root) {
    if (root == null) {
        return 0;
    }

  return Math.max(getDepthDFS(root.left), getDepthDFS(root.right)) + 1;
}

// V1
// IDEA :  TOP DOWN RECURSION
// https://leetcode.com/problems/balanced-binary-tree/editorial/
// Recursively obtain the height of a tree. An empty tree has -1 height
private int height(TreeNode root) {
    // An empty tree has height -1
    if (root == null) {
        return -1;
    }
    return 1 + Math.max(height(root.left), height(root.right));
}

public boolean isBalanced(TreeNode root) {
    // An empty tree satisfies the definition of a balanced tree
    if (root == null) {
        return true;
    }

    // Check if subtrees have height within 1. If they do, check if the
    // subtrees are balanced
    return Math.abs(height(root.left) - height(root.right)) < 2
            && isBalanced(root.left)
            && isBalanced(root.right);
}
```

### 9) Boundary of Binary Tree — LC 545
```python
# LC 545. Boundary of Binary Tree
# V0
# IDEA : DFS
# https://xiaoguan.gitbooks.io/leetcode/content/LeetCode/545-boundary-of-binary-tree-medium.html
# https://www.cnblogs.com/lightwindy/p/9583723.html
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        def leftBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            nodes.append(root.val)
            """
            NOTE this !!!
            """
            if not root.left:
                leftBoundary(root.right, nodes)
            else:
                leftBoundary(root.left, nodes)
 
        def rightBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            """
            NOTE this !!!
            """
            if not root.right:
                rightBoundary(root.left, nodes)
            else:
                rightBoundary(root.right, nodes)
            nodes.append(root.val)
 
        def leaves(root, nodes):
            if not root:
                return
            if not root.left and not root.right:
                nodes.append(root.val)
                return
            leaves(root.left, nodes)
            leaves(root.right, nodes)
 
        if not root:
            return []
 
        nodes = [root.val]
        leftBoundary(root.left, nodes)
        """
        NOTE this !!!
        """
        leaves(root.left, nodes)
        leaves(root.right, nodes)
        rightBoundary(root.right, nodes)
        return nodes

# V0'
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        if not root: return []

        left_bd_nodes = [root]
        cur = root.left
        while cur:
            left_bd_nodes.append(cur)
            cur = cur.left or cur.right

        right_bd_nodes = [root]
        cur = root.right
        while cur:
            right_bd_nodes.append(cur)
            cur = cur.right or cur.left

        leaf_nodes = []
        stack = [root]
        while stack:
            node = stack.pop()
            if node.right:
                stack.append(node.right)
            if node.left:
                stack.append(node.left)
            if not node.left and not node.right:
                leaf_nodes.append(node)

        ans = []
        seen = set()
        def visit(node):
            if node not in seen:
                seen.add(node)
                ans.append(node.val)

        for node in left_bd_nodes: visit(node)
        for node in leaf_nodes: visit(node)
        for node in reversed(right_bd_nodes): visit(node)

        return ans
```

## 高度、深度與路徑範例

### 10) 樹的高度與深度操作

#### **核心觀念：高度 vs 深度**

| 觀念 | 定義 | 方向 | 走訪 | 用途 |
|---------|------------|-----------|-----------|----------|
| **高度** | 節點到最深葉子的距離 | 由下而上 | 後序 DFS | 樹的性質、平衡檢查 |
| **深度** | 根節點到目標節點的距離 | 由上而下 | 前序 DFS | 節點距離、找層數 |

**關鍵洞見**：`getDepth()` 是計算節點間距離最基本的演算法（從根走到任一目標節點）。

#### **視覺比較**

```text
        1           Height of 1: 2 (to deepest leaf)
       / \          Depth of 1: 0 (root)
      2   3         Height of 2: 1, Depth of 2: 1
     / \            Height of 4: 0, Depth of 4: 2
    4   5           Height of 5: 0, Depth of 5: 2

Height measures "how far down can I go?"
Depth measures "how far am I from the root?"
```

#### **1. 取得高度（後序 DFS）**

**目的**：計算節點到它最深的後代葉子的距離。

```java
// java
/**
 * 🌳 Get Height - Bottom-up approach
 *
 * Time: O(N) - visit each node once
 * Space: O(h) - recursion stack depth
 *
 * Returns: Height of tree (number of edges from node to deepest leaf)
 */
public int getHeight(TreeNode root) {
    if (root == null) {
        return -1;  // ✅ Return -1 for null (so leaf height = 0)
    }

    // Post-order: process children first, then current node
    int leftHeight = getHeight(root.left);
    int rightHeight = getHeight(root.right);

    return Math.max(leftHeight, rightHeight) + 1;
}

/**
 * Why return -1 for null?
 *
 * • By definition: leaf node has height 0
 * • If root is a leaf:
 *   - leftHeight = -1 (null)
 *   - rightHeight = -1 (null)
 *   - max(-1, -1) + 1 = 0 ✅ (correct leaf height)
 *
 * Alternative: return 0 for null
 * Then leaf height would be 1 (which is also valid but less standard)
 */
```

```python
# python
def get_height(node):
    """
    Height: Distance from node to deepest leaf (bottom-up)

    Returns -1 for null so leaf height = 0
    """
    if not node:
        return -1

    left_height = get_height(node.left)
    right_height = get_height(node.right)

    return max(left_height, right_height) + 1
```

#### **2. 取得深度（前序 DFS）**

**目的**：計算根到目標節點的距離。**這就是核心的求距離演算法。**

```java
// java
/**
 * 🌿 Get Depth - Top-down approach
 *
 * This is the fundamental algorithm for finding distance between nodes:
 * - Distance from root to any target node
 * - Can be used as building block for node-to-node distance (via LCA)
 *
 * Time: O(N) - worst case visit all nodes
 * Space: O(h) - recursion stack depth
 *
 * Returns:
 * - Depth (number of edges from root to target) if found
 * - -1 if target not found
 */
public int getDepth(TreeNode root, TreeNode target) {
    return getDepthHelper(root, target, 0);
}

private int getDepthHelper(TreeNode root, TreeNode target, int depth) {
    if (root == null) {
        return -1; // ❌ Target not found in this branch
    }

    if (root == target) {
        return depth; // ✅ Found! Return current distance from root
    }

    // Pre-order: try left subtree first
    int leftDepth = getDepthHelper(root.left, target, depth + 1);
    if (leftDepth != -1) {
        return leftDepth; // Found in left subtree
    }

    // If not in left, try right subtree
    return getDepthHelper(root.right, target, depth + 1);
}

/**
 * Why return -1 for null?
 *
 * • -1 is a sentinel value meaning "not found"
 * • Allows us to distinguish between:
 *   - "Found at root" (depth = 0)
 *   - "Not found" (depth = -1)
 * • Enables early termination when target is found
 */
```

```python
# python
def get_depth(root, target, depth=0):
    """
    Depth: Distance from root to target node (top-down)

    This is the core distance algorithm - finds distance from root to any node.

    Returns:
    - depth (number of edges) if target found
    - -1 if target not found
    """
    if not root:
        return -1  # Not found

    if root == target or root.val == target:
        return depth  # Found!

    # Try left subtree
    left_depth = get_depth(root.left, target, depth + 1)
    if left_depth != -1:
        return left_depth

    # Try right subtree
    return get_depth(root.right, target, depth + 1)
```

#### **3. 比較表**

| 面向 | 取得高度 | 取得深度 |
|--------|------------|-----------|
| **走訪順序** | 後序（left → right → root） | 前序（root → left → right） |
| **方向** | 由下而上（葉子到節點） | 由上而下（根到節點） |
| **null 的回傳值** | `-1`（讓葉子高度 = 0） | `-1`（代表「找不到」） |
| **參數傳遞** | 沒有（由子節點算出來） | 遞迴時把 `depth` 往下傳 |
| **用途** | 樹的平衡、樹的性質 | **距離計算**、找層數 |
| **什麼時候用** | 需要先拿到子節點的資料 | 需要把父節點的資料給子節點 |
| **例題** | LC 104 (Max Depth)、LC 110 (Balanced Tree) | LC 1740 (Distance in Tree)、LC 863 (Distance K) |

#### **4. 和節點間距離的關係**

```java
/**
 * Finding distance between ANY two nodes uses getDepth() as building block:
 *
 * 1. Find Lowest Common Ancestor (LCA) of node1 and node2
 * 2. Distance = getDepth(LCA, node1) + getDepth(LCA, node2)
 *
 * See LC 1740 for full implementation.
 */
public int findDistance(TreeNode root, int p, int q) {
    TreeNode lca = findLCA(root, p, q);
    return getDepth(lca, p) + getDepth(lca, q);
}
```

#### **5. 常見變形**

```java
// java

// Variation 1: Get depth by value instead of node reference
public int getDepth(TreeNode root, int targetVal) {
    return getDepthHelper(root, targetVal, 0);
}

private int getDepthHelper(TreeNode node, int targetVal, int depth) {
    if (node == null) {
        return -1;
    }
    if (node.val == targetVal) {
        return depth;
    }

    int left = getDepthHelper(node.left, targetVal, depth + 1);
    if (left != -1) return left;

    return getDepthHelper(node.right, targetVal, depth + 1);
}

// Variation 2: Get height returning 0 for null (leaf height = 1)
public int getHeightAlternative(TreeNode root) {
    if (root == null) {
        return 0;  // Leaf node height = 1 with this approach
    }
    return 1 + Math.max(getHeightAlternative(root.left),
                        getHeightAlternative(root.right));
}

// Variation 3: Check if tree is balanced (height difference ≤ 1)
public boolean isBalanced(TreeNode root) {
    return checkBalance(root) != -1;
}

private int checkBalance(TreeNode node) {
    if (node == null) {
        return 0;
    }

    int leftHeight = checkBalance(node.left);
    if (leftHeight == -1) return -1;

    int rightHeight = checkBalance(node.right);
    if (rightHeight == -1) return -1;

    if (Math.abs(leftHeight - rightHeight) > 1) {
        return -1;  // Unbalanced
    }

    return 1 + Math.max(leftHeight, rightHeight);
}
```

#### **6. 帶走的重點**

1. **高度（後序）**：用來算樹的性質，需要先有子節點的資訊
2. **深度（前序）**：**核心的距離演算法**，把資訊往下傳給子節點
3. **null 回傳 -1**：
   - 高度：讓葉子高度 = 0（標準定義）
   - 深度：代表「找不到目標」
4. **節點間距離** = 搭配 LCA，用 `getDepth()` 算兩次
5. **依資訊流向選擇**：
   - 需要子節點的資料？→ 用高度（後序）
   - 需要父節點的資料？→ 用深度（前序）


> 上面高度／深度想法的具體實作。

#### 取得最大深度

- LC 104 : Maximum Depth of Binary Tree
- LC 110 : Balanced Binary Tree

```java
// java
// V0
// IDEA : RECURSIVE (DFS)
public int maxDepth(TreeNode root) {

    if (root == null){
        return 0;
    }

    // NOTE : below conditon is optional (have or not use is OK)
//        if (root.left == null && root.right == null){
//            return 1;
//        }

    int leftD = maxDepth(root.left) + 1;
    int rightD = maxDepth(root.right) + 1;

    return Math.max(leftD, rightD);
}
```

```python
#-----------------
# BFS
#-----------------
# ....
layer = 1
q = [[layer, root]]
res = []
while q:
    # NOTE !!! FIFO, so we pop first added element (new element added at right hand side)
    layer, tmp = q.pop(0)      # NOTE: pop from the QUEUE, not from the tree node
    """
    KEY here !!!!
    """
    if tmp and not tmp.left and not tmp.right:
        res.append(layer)
    if tmp.left:
        q.append([layer+1, tmp.left])
    if tmp.right:
        q.append([layer+1, tmp.right])
    # ...
```

#### 取得最小深度
- LC 111 : Minimum Depth of Binary Tree 

```java
// java

// V0'
// IDEA : DFS
public int minDepth(TreeNode root) {

    if (root == null){
        return  0;
    }

    return getDepth(root);
}

private int getDepth(TreeNode root){

    if (root == null){
        return 0;
    }

    /**
     *  NOTE !!! below condition
     *  -> we need to go till meat a node, then calculate min depths (number of node)
     *  -> Note: A leaf is a node with no children.
     *  -> plz check below example for idea
     *  example : [2,null,3,null,4,null,5,null,6]
     *
     *
     */
    if (root.left == null) {
        return 1 + getDepth(root.right);
    } else if (root.right == null) {
        return 1 + getDepth(root.left);
    }

    return 1 + Math.min(getDepth(root.left), getDepth(root.right));
}
```

### 11) Maximum Depth of Binary Tree — LC 104
```python
# LC 104 Maximum Depth of Binary Tree
# V0
# IDEA : DFS
class Solution(object):
    def maxDepth(self, root):

        if root == None:
            return 0
        return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))

# V0'
# bfs
class Solution(object):
    def maxDepth(self, root):
        # edge case
        if not root:
            return 0
        res = 0
        layer = 0
        q = [[root, layer]]
        while q:
            for i in range(len(q)):
                tmp, layer = q.pop(0)
                res = max(res, layer)
                if tmp.left:
                    q.append([tmp.left, layer+1])
                if tmp.right:
                    q.append([tmp.right, layer+1])                   
        return res + 1
```

### 12) Minimum Depth of Binary Tree — LC 111

> 參考：[MinimumDepthOfBinaryTree.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Recursion/MinimumDepthOfBinaryTree.java)

```python
# LC 111 Minimum Depth of Binary Tree

# V0
# IDEA : BFS
class Solution(object):
    def minDepth(self, root):
        # edge case
        if not root:
            return 0
        if root and not root.left and not root.right:
            return 1
        layer = 1
        q = [[layer, root]]
        res = []
        while q:
            for i in range(len(q)):
                layer, tmp = q.pop(0)
                """
                NOTE !!! : via below condition, we get "layer" of " A leaf is a node with no children."
                """
                if tmp and not tmp.left and not tmp.right:
                    res.append(layer)
                if tmp.left:
                    q.append([layer+1, tmp.left])
                if tmp.right:
                    q.append([layer+1, tmp.right])
        # get min
        #print ("res = " + str(res))
        return min(res)

# V0'
# IDEA : DFS
# compare with LC 104 : Maximum Depth of Binary Tree
class Solution(object):
    def minDepth(self, root):
        if not root:
            return 0
        ### NOTE here : we need min depth, so if not root.left, then we need to return directly
        if not root.left:
            return 1 + self.minDepth(root.right)
        ### NOTE here : we need min depth, so if not root.right, then we need to return directly
        elif not root.right:
            return 1 + self.minDepth(root.left)
        else:
            return 1 + min(self.minDepth(root.left), self.minDepth(root.right))
```

### 13) Binary Tree Maximum Path Sum — LC 124

```java
// java
// LC 124

// V0-1
// IDEA: DFS (GPT)
private int maxSum = Integer.MIN_VALUE;

public int maxPathSum_0_1(TreeNode root) {
    if (root == null) {
        return Integer.MIN_VALUE; // Handle null case
    }

    dfs(root);
    return maxSum;
}

/** NOTE !!!
 *
 *  the response type of dfs is `integer`
 *  e.g. the `max path sum` per input node
 */
private int dfs(TreeNode node) {
    if (node == null) {
        return 0;
    }

    // Compute max path sum of left and right children, discard negative values
    /**
     *  NOTE !!!
     *
     *   we cache `leftMax` on current node
     *   we cache `rightMax` on current node
     *
     *   so we can update global `max path sum` below
     */
    int leftMax = Math.max(dfs(node.left), 0);
    int rightMax = Math.max(dfs(node.right), 0);

    // Update global max sum with current node as the highest ancestor
    /**
     *  NOTE !!!
     *
     *  we update global `max path sum`,
     *  but the `maxSum` is NOT return as method reponse,
     *  we simply update the global variable `maxSum`
     *
     *  -> the method return val is local max path (node.val + Math.max(leftMax, rightMax))
     */
    maxSum = Math.max(maxSum, node.val + leftMax + rightMax);

    // Return max sum path including this node (but only one subtree path)
    /**
     *  NOTE !!!
     *
     *
     *  -> the method return val is local max path (node.val + Math.max(leftMax, rightMax)),
     *     instead of `maxSum`
     *
     */
    return node.val + Math.max(leftMax, rightMax);
}

```

```python
# 124. Binary Tree Maximum Path Sum
# V0
# IDEA : DFS
# https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/209995/Python-solution
class Solution(object):
    def maxPathSum(self, root):
        def dfs(root):
            if not root:
                return 0
            l_max = dfs(root.left)
            r_max = dfs(root.right)
            """
            handle if l_max < 0:
                    -> start again from root.val
                   else:
                    -> l_max += root.val
            """
            if l_max < 0:
                l_max = root.val
            else:
                l_max += root.val
            """
            handle if r_max < 0:
                    -> start again from root.val
                   else:
                    -> r_max += root.val
            """
            if r_max < 0:
                r_max = root.val
            else:
                r_max += root.val

            self.maximum = max(self.maximum, l_max + r_max - root.val)
            return max(l_max, r_max)
           
        self.maximum = -float('inf')
        dfs(root)
        return self.maximum
```

### 14) Count Good Nodes in Binary Tree — LC 1448
```java
// java
// LC 1448
 // V1
    // IDEA : DFS
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    private int numGoodNodes = 0;

    public int goodNodes_2(TreeNode root) {
        dfs(root, Integer.MIN_VALUE);
        return numGoodNodes;
    }

    private void dfs(TreeNode node, int maxSoFar) {
        if (maxSoFar <= node.val) {
            numGoodNodes++;
        }

        if (node.right != null) {
            dfs(node.right, Math.max(node.val, maxSoFar));
        }

        if (node.left != null) {
            dfs(node.left, Math.max(node.val, maxSoFar));
        }
    }


    // V2
    // IDEA : DFS + Iterative
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    class Pair {

        public TreeNode node;
        public int maxSoFar;

        public Pair(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }

    public int goodNodes_3(TreeNode root) {
        int numGoodNodes = 0;
        Stack<Pair> stack = new Stack<>();
        stack.push(new Pair(root, Integer.MIN_VALUE));

        while (stack.size() > 0) {
            Pair curr = stack.pop();
            if (curr.maxSoFar <= curr.node.val) {
                numGoodNodes++;
            }

            if (curr.node.left != null) {
                stack.push(new Pair(curr.node.left, Math.max(curr.node.val, curr.maxSoFar)));
            }

            if (curr.node.right != null) {
                stack.push(new Pair(curr.node.right, Math.max(curr.node.val, curr.maxSoFar)));
            }
        }

        return numGoodNodes;
    }
```

## 修改與多狀態範例

### 15) Delete Nodes And Return Forest — LC 1110

**題目**：給一棵二元樹的 root 和一組要刪除的值，把那些節點移除，回傳剩下那些樹的 root 列表（森林）。

**核心想法**： 
- 用 DFS 追蹤兩個狀態：目前節點該不該刪、以及父節點有沒有被刪
- 一個節點如果自己沒被刪、但父節點被刪了，它就成為森林的一個 root
- 後序 DFS 先處理子節點，才能乾淨地把連結切斷

**做法 1：DFS + 狀態追蹤（推薦）**

```java
public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
    HashSet<Integer> deleteSet = new HashSet<>();
    for (int x : to_delete) {
        deleteSet.add(x);
    }
    
    List<TreeNode> forest = new ArrayList<>();
    dfs(root, deleteSet, true, forest);  // root has no parent → treated as deleted
    return forest;
}

private TreeNode dfs(TreeNode node, HashSet<Integer> deleteSet, boolean isParentDeleted, List<TreeNode> forest) {
    if (node == null)
        return null;

    boolean isDeleted = deleteSet.contains(node.val);

    // If this node is a new root (NOT deleted AND parent WAS deleted or doesn't exist)
    if (!isDeleted && isParentDeleted) {
        forest.add(node);
    }

    // Post-order: process children first (their isParentDeleted = current node's isDeleted)
    node.left = dfs(node.left, deleteSet, isDeleted, forest);
    node.right = dfs(node.right, deleteSet, isDeleted, forest);

    // Return null to parent if deleted (automatically disconnects), else return node
    return isDeleted ? null : node;
}
```

**複雜度**：時間 O(N)、空間 O(N)
- 每個節點恰好走訪一次
- HashSet 操作：O(1)
- 遞迴深度：O(h)，最差 O(N)

**做法 2：BFS（層序走訪）**

```java
public List<TreeNode> delNodes_BFS(TreeNode root, int[] to_delete) {
    Set<Integer> deleteSet = new HashSet<>();
    for (int val : to_delete) {
        deleteSet.add(val);
    }
    
    List<TreeNode> forest = new ArrayList<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.add(root);
    
    while (!q.isEmpty()) {
        TreeNode curNode = q.poll();
        
        // Disconnect children if they need to be deleted
        if (curNode.left != null) {
            q.add(curNode.left);
            if (deleteSet.contains(curNode.left.val)) {
                curNode.left = null;  // Disconnect
            }
        }
        
        if (curNode.right != null) {
            q.add(curNode.right);
            if (deleteSet.contains(curNode.right.val)) {
                curNode.right = null;  // Disconnect
            }
        }
        
        // If current node is deleted, add its children as forest roots
        if (deleteSet.contains(curNode.val)) {
            if (curNode.left != null) {
                forest.add(curNode.left);
            }
            if (curNode.right != null) {
                forest.add(curNode.right);
            }
        }
    }
    
    // Add original root if not deleted
    if (!deleteSet.contains(root.val)) {
        forest.add(root);
    }
    
    return forest;
}
```

**複雜度**：時間 O(N)、空間 O(N)

**範例走查**： 
```text
Input: root = [1,2,3,4,5,6,7], to_delete = [3,5]

       1
      / \
     2   3
    / \ /  \
   4  5 6   7

Step 1: DFS processes:
- Node 4: isParentDeleted=false (parent 2 not deleted) → NOT a root
- Node 5: isDeleted=true, Node 2 disconnects it
- Node 2: isParentDeleted=false (parent 1 not deleted) → NOT a root
- Node 6: isParentDeleted=true (parent 3 deleted) → IS a root! Add 6
- Node 7: isParentDeleted=true (parent 3 deleted) → IS a root! Add 7
- Node 3: isDeleted=true, Node 1 disconnects it
- Node 1: isParentDeleted=true (root, treated as parent deleted) and NOT deleted → IS a root! Add 1

Result: [1(with subtree [2,4]), 6, 7]
```

**關鍵洞見**：
1. **雙狀態模式**：同時追蹤 `isDeleted` 和 `isParentDeleted`
2. **森林 root 的條件**：`(!isDeleted && isParentDeleted)` 或 `(!isDeleted && isRoot)`
3. **後序 DFS**：子節點先處理完，父節點才做決定，連結才切得乾淨
4. **自動斷開**：`dfs()` 回傳 null，父節點的對應子指標就自動變成 null
5. **BFS 為什麼可行**：先把所有子節點入列再處理，自然就會發現哪些節點變成 root

**容易踩到的坑** ⚠️：
1. **忘了 root 的特例**：root 沒有父節點，所以要把它當成「父節點已被刪」，它才能成為森林 root
2. **走訪順序錯了**：必須先處理子節點，才知道這個節點是不是被刪
3. **沒有正確斷開**：BFS 做法需要明確寫 `curNode.left = null` 來斷開
4. **漏掉森林 root**：初始的 root 和「父節點被刪」的節點兩種都要檢查

**相似題目**：
| 題目 | LC # | 關鍵差異 |
|---------|------|-----------------|
| Delete Nodes And Return Forest | 1110 | 基礎模式 |
| Delete Leaves With Given Value | 1325 | 遞迴式刪除（子節點處理完之後才刪） |
| Trim a Binary Search Tree | 669 | 用範圍過濾，而不是依值刪除 |
| Lowest Common Ancestor III | 1676 | 在刪除後的森林裡找 LCA |

**常見應用：**
- 修剪樹，結果會產生多棵子樹
- 選擇性移除節點以形成森林
- 檔案系統操作（刪節點、保留剩餘結構）
- 帶連鎖刪除的階層式資料管理

**模式辨識：**
- ✅ 要刪掉特定節點，但保留其餘的樹結構
- ✅ 結果是一片森林（多個樹根）
- ✅ 被刪節點的子節點要留下來
- ✅ 狀態同時取決於目前節點和父節點的決定

### 16) Flatten Binary Tree to Linked List — LC 114

**題目**：**原地**把一棵二元樹壓平成一條「鏈結串列」，每個 `right` 指標指向**前序**的下一個節點，每個 `left` 都是 `null`。

```text
Input:                Output (right-linked, all left = null):

      1                 1
     / \                 \
    2   5                 2
   / \   \                 \
  3   4   6                 3
                            \
                             4
                              \
                               5
                                \
                                 6
```

**核心想法 —— 後序 DFS 回傳「尾巴」**

最乾淨的遞迴解法會先把左右子樹壓平（後序），再重接目前節點。關鍵技巧：**每次 `helper` 呼叫都回傳它壓平那棵子樹的*尾巴*（前序的最後一個節點）**，父節點才知道要把原本的右子樹接到哪裡。

```python
# python
# LC 114 Flatten Binary Tree to Linked List
# IDEA: DFS (post-order) — return the TAIL of each flattened subtree
class Solution(object):
    def flatten(self, root):
        """Do not return anything, modify root in-place instead."""
        self.helper(root)

    def helper(self, node):
        # Base case: an empty subtree has no tail
        if not node:
            return None

        # 1) Flatten BOTH subtrees first (post-order)
        left_tail  = self.helper(node.left)   # last node of flattened left
        right_tail = self.helper(node.right)  # last node of flattened right

        # 2) If a left subtree exists, splice it between node and node.right
        if left_tail:
            left_tail.right = node.right  # left's tail -> original right
            node.right = node.left        # move left subtree to the right
            node.left = None              # left must be null per problem

        # 3) Return the tail of THIS flattened subtree (pre-order last node)
        #    priority: right_tail > left_tail > node itself
        if right_tail:
            return right_tail
        if left_tail:
            return left_tail
        return node
```

**為什麼要回傳尾巴？**把左子樹搬到右邊時，必須把*原本的*右子樹接到壓平後左子樹的**尾端**，而不是它的 root。唯一知道尾端在哪的，就是那次壓平左子樹的遞迴呼叫。所以它要回傳自己的尾巴。

#### 重接指標的視覺化（`if left_tail:` 那一段）

```text
Before:                 After:
      node                node
     /    \                  \
   left   right             left            (node.right = node.left)
                               \
                              ...  (flattened left chain)
                                  \
                                  right     (left_tail.right = node.right)
```

#### 實際跑一遍 —— `root = [1,2,5,3,4,null,6]`

```text
        1
       / \
      2   5
     / \   \
    3   4   6
```

後序會先走到**最左最深**的節點。`helper` 回傳值（每次呼叫交回的尾巴）的追蹤：

```text
helper(3): no children          -> left_tail=None, right_tail=None       -> return 3
helper(4): no children          -> return 4
helper(2): left_tail=3, right_tail=4
           left_tail(3).right = node.right (4)   =>  3 -> 4
           node.right = node.left (3)            =>  2 -> 3
           node.left = None
           subtree now: 2 -> 3 -> 4              -> return right_tail = 4
helper(6): no children          -> return 6
helper(5): left_tail=None, right_tail=6
           (no left subtree, nothing to rewire)
           subtree: 5 -> 6                       -> return right_tail = 6
helper(1): left_tail=4 (tail of "2->3->4"), right_tail=6
           left_tail(4).right = node.right (5)   =>  4 -> 5
           node.right = node.left (2)            =>  1 -> 2
           node.left = None
           => 1 -> 2 -> 3 -> 4 -> 5 -> 6         -> return 6
```

最後壓平的串列（所有 `left=None`）：`1 -> 2 -> 3 -> 4 -> 5 -> 6` ✅（和前序一致）。

#### 模式與其他做法

| 做法 | 想法 | 時間 | 空間 | 備註 |
|----------|------|------|-------|-------|
| **後序 + 回傳尾巴**（上面那個） | 壓平子樹，再用回傳的尾巴接起來 | O(N) | O(h) 遞迴 | 乾淨、直覺 |
| **反向前序 + `prev`** | 依 `right → left → node` 走訪，設 `node.right = prev` | O(N) | O(h) | 等於反過來把串列建起來（見 `V0-1`） |
| **前序收集到 list** | 用前序把節點存起來，再用迴圈重接 | O(N) | O(N) | 最好想 |
| **Morris 式迭代** | 對每個節點找左子樹最右端，接上去 | O(N) | **O(1)** | *追問*（真正原地）的最佳解 |

**反向前序（`prev` 指標）—— 漂亮的 O(h) 版本：**

```python
# Visit right first, then left, building the list from tail to head
class Solution(object):
    def __init__(self):
        self.prev = None
    def flatten(self, root):
        if not root:
            return
        self.flatten(root.right)
        self.flatten(root.left)
        root.right = self.prev   # link to the chain built so far
        root.left = None
        self.prev = root         # current node becomes new head
```

**真正 O(1) 空間（迭代版，追問的答案）：**

```python
class Solution(object):
    def flatten(self, root):
        curr = root
        while curr:
            if curr.left:
                rightmost = curr.left
                while rightmost.right:        # find left subtree's rightmost
                    rightmost = rightmost.right
                rightmost.right = curr.right  # splice original right after it
                curr.right = curr.left        # move left to right
                curr.left = None
            curr = curr.right                 # advance down the new right spine
```

**關鍵洞見**：回傳值優先序 `right_tail > left_tail > node` 對應的就是**前序的最後一個節點** —— 前序會結束在最右邊那條分支，所以右子樹的尾巴（如果有的話）就是整體的尾巴。

**相似題目**：

| 題目 | LC # | 關鍵差異 |
|---------|------|-----------------|
| Flatten Binary Tree to Linked List | 114 | 基礎模式（原地前序壓平） |
| Binary Tree Preorder Traversal | 144 | 走訪順序相同，只是把值收集起來 |
| Convert BST to Sorted Doubly Linked List | 426 | 用中序壓平成雙向鏈結串列 |
| Increasing Order Search Tree | 897 | 用中序壓平成只有右子的鏈 |
| Flatten a Multilevel Doubly Linked List | 430 | 在串列上做同樣的「把子鏈接在 next 之前」 |

### 17) Merge Two Binary Trees — LC 617

```python
# LC 617 Merge Two Binary Trees
# NOTE !!! there is also BFS solution
# V0
# IDEA : DFS + BACKTRACK
class Solution:
    def mergeTrees(self, t1, t2):
        return self.dfs(t1,t2)

    def dfs(self, t1, t2):
        if not t1 and not t2:
            return
        
        if t1 and t2:
            ### NOTE here
            newT = TreeNode(t1.val +  t2.val)
            newT.right = self.mergeTrees(t1.right, t2.right)
            newT.left = self.mergeTrees(t1.left, t2.left)   
            return newT
        
        ### NOTE here
        else:
            return t1 or t2
```

```java
// java
// V0
// IDEA : RECURSIVE
public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {

    if (t1 == null && t2 == null){
        return null;
    }

    if (t1 != null && t2 != null){
        t1.val += t2.val;
    }

    if (t1 == null && t2 != null){
        // NOTE!!! return t2 directly here
        return t2;
    }

    if (t1 != null && t2 == null){
        // NOTE!!! return t1 directly here
        return t1;
    }

    t1.left = mergeTrees(t1.left, t2.left);
    t1.right = mergeTrees(t1.right, t2.right);

    return t1;
}
```

### 18) Invert Binary Tree — LC 226
```python
# LC 226 Invert Binary Tree

# V0
# IDEA : DFS
# -> below code shows a good example that tree is a type of "linked list"
# -> we don't really modify tree's "value", but we modify the pointer
# -> e.g. make root.left point to root.right, make root.right point to root.left
class Solution(object):
    def invertTree(self, root):
        def dfs(root):
            if not root:
                return root
            ### NOTE THIS
            if root:
                # NOTE : have to do root.left, root.right ON THE SAME TIME
                root.left, root.right = dfs(root.right), dfs(root.left)
        dfs(root)
        return root

# V0'
# IDEA BFS
class Solution(object):
    def invertTree(self, root):
        if root == None:
            return root
        queue = [root]
        while queue:
            # queue = queue[::-1] <-- this one is NOT working
            for i in range(len(queue)):         
                tmp = queue.pop()
                ### NOTE here !!!!!!
                # -> we do invert op via below
                tmp.left, tmp.right = tmp.right, tmp.left
                if tmp.left:
                    queue.append(tmp.left)
                if tmp.right:
                    queue.append(tmp.right)
        return root
```

```java
// java
// DFS
    // V0
    // IDEA : DFS
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        /** NOTE !!!!
         *
         *   instead of calling invertTree and assign value to sub tree directly,
         *   we need to CACHE invertTree result, and assign later
         *   -> since assign directly will cause tree changed, and affect the other invertTree call
         *
         *   e.g. below is WRONG,
         *      root.left = invertTree(root.right);
         *      root.right = invertTree(root.left);
         *
         *   need to cache result
         *
         *       TreeNode left = invertTree(root.left);
         *       TreeNode right = invertTree(root.right);
         *
         *   then assign to sub tree
         *
         *       root.left = right;
         *       root.right = left;
         */
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        /** NOTE !!!! below is WRONG */
//        root.left = invertTree(root.right);
//        root.right = invertTree(root.left);
        return root;
    }
```

### 19) Binary Tree Cameras — LC 968（由下而上的多狀態貪婪）


> 參考：[BinaryTreeCameras.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/BinarySearchTree/BinaryTreeCameras.java)

有些題目需要每個節點回傳一個**狀態**（而不是數值）給父節點，父節點再根據子節點的狀態做**貪婪決策**。這是一種獨立的由下而上模式。

**核心想法 —— 三狀態貪婪：**
```text
State 0: NOT covered (needs a camera from parent)
State 1: HAS a camera (covers parent, self, children)
State 2: COVERED (by a child's camera, but has no camera itself)

null nodes → return 2 (covered), so leaves are forced to be state 0 (uncovered),
which forces their parents to place cameras — this is the greedy insight.
```

**為什麼要由下而上（後序）？**
- 葉子是放攝影機最「浪費」的位置（往上只蓋到 1 個節點）
- 先處理葉子，就會逼著攝影機裝到它們的父節點（一次蓋 3 個節點）
- 這種由下往上的貪婪策略會讓攝影機總數最少

```java
// LC 968 — Binary Tree Cameras: bottom-up greedy with 3 states
int cameraCnt = 0;

public int minCameraCover(TreeNode root) {
    // If root itself is uncovered, it needs a camera too
    if (dfs(root) == 0) {
        cameraCnt++;
    }
    return cameraCnt;
}

private int dfs(TreeNode node) {
    // null = covered (so leaves become uncovered → forces parent to place camera)
    if (node == null) return 2;

    int left = dfs(node.left);    // post-order: solve children first
    int right = dfs(node.right);

    // Rule 1: Any child uncovered → MUST place camera here
    if (left == 0 || right == 0) {
        cameraCnt++;
        return 1;  // has camera
    }

    // Rule 2: Any child has camera → this node is covered
    if (left == 1 || right == 1) {
        return 2;  // covered
    }

    // Rule 3: Both children covered (no cameras) → this node is NOT covered
    // Rely on parent to cover it (greedy: delay camera placement upward)
    return 0;  // uncovered
}
```

**視覺化 —— 由下而上的貪婪為什麼成立：**
```text
        1 ← if uncovered, add camera here (special root check)
       / \
      2   3 ← children covered (state 2), no camera needed
     / \
    4   5 ← camera HERE (state 1), covers parent + children
   / \
  6   7 ← uncovered (state 0), forces parent to place camera

Processing order (post-order): 6,7 → 4,5 → 2,3 → 1
  node 6,7: null children return 2 → both children covered → return 0 (uncovered)
  node 4: left=0 (uncovered!) → place camera → return 1
  node 5: similar logic
  node 2: left=1 (has camera) → return 2 (covered)
  node 1: depends on children's states
```

**狀態轉移規則（每個節點的決策）：**

| 左子狀態 | 右子狀態 | 決策 | 回傳 |
|:----------:|:-----------:|----------|:------:|
| 0（未覆蓋） | 任意 | 放攝影機 | 1 |
| 任意 | 0（未覆蓋） | 放攝影機 | 1 |
| 1（有攝影機） | 任意非 0 | 被子節點覆蓋 | 2 |
| 任意非 0 | 1（有攝影機） | 被子節點覆蓋 | 2 |
| 2（已覆蓋） | 2（已覆蓋） | 自己沒被覆蓋，交給父節點 | 0 |

**關鍵洞見 —— 為什麼 `null → 2`（已覆蓋）？**
如果 null 回傳 0（未覆蓋），每個葉子都會被迫裝攝影機 —— 太浪費。把 null 當成「已覆蓋」，葉子就會是狀態 0（未覆蓋），逼著它們的**父節點**裝攝影機，這嚴格更好（一次蓋 3 個節點，而不是 1 個）。

**同樣用「由下而上帶狀態的貪婪」的相似 LC 題目：**

| LC # | 題目 | 狀態 | 貪婪洞見 |
|------|---------|--------|----------------|
| 968 | Binary Tree Cameras | 0/1/2（未覆蓋／有攝影機／已覆蓋） | 把攝影機往上拖延，裝在葉子的父節點 |
| 337 | House Robber III | 每個節點 rob/skip | Max(搶自己 + 跳過子節點, 跳過自己 + 子節點的最佳解) |
| 979 | Distribute Coins in Binary Tree | 每棵子樹多出來的硬幣數 | 每條邊搬一次算 1 步；由下而上累加 |excess| |
| 1373 | Max Sum BST in Binary Tree | BST 合法／不合法 + 總和 | 由下而上驗證 BST 性質，同時追蹤最大總和 |

## 重點整理

| 題目 | 模式 | 一定要記住的一件事 |
|---|---|---|
| LC 199 | 依深度做 BFS / DFS | 取每層**最後**一個節點（或用 right → left 走訪時取第一個） |
| LC 222 | 完全樹遞迴 | 比較左右脊的高度 → O(log² N)，不是 O(N) |
| LC 662 | 用索引編碼的 BFS | 節點 `i` → 子節點 `2i`、`2i+1`；寬度 = 最後 − 最前 + 1 |
| LC 2415 | 層序 + 交換 | 反轉的是一層的**值**，不是節點 |
| LC 101 | 鏡像 DFS | 比較 `(a.left, b.right)` 和 `(a.right, b.left)` |
| LC 100 / 951 | 成對 DFS | LC 951 額外允許左右子樹互換 |
| LC 98 | 中序／上下界 | 把 `(low, high)` 往下傳 —— 只和直接子節點比較是不夠的 |
| LC 110 | 後序高度 | 回傳 `-1` 代表「已經不平衡」，提早收工 |
| LC 545 | 掃三趟 | 左邊界、葉子、反轉的右邊界 —— 記得把角落去重 |
| LC 104 / 111 | 高度 | 最小深度需要防 null 子節點的判斷；最大深度不用 |
| LC 124 | 後序 + 全域最大值 | 往上只回傳一條分支，`left + node + right` 記在全域 |
| LC 1448 | 由上而下帶著目前最大值 | 把路徑上的最大值當參數往下傳 |
| LC 1110 | 雙狀態 DFS | 父節點死了但自己活著的節點，就是森林的 root |
| LC 114 | 後序回傳尾巴 | 或用 tree.md 裡 O(1) 空間的 Morris 重接法 |
| LC 617 / 226 | 結構遞迴 | 往下走的時候建立或交換，再回傳節點 |
| LC 968 | 由下而上的三狀態貪婪 | `null → 已覆蓋`，這會逼著攝影機裝到葉子的父節點 |

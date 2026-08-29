# 樹的 LCA、距離與路徑問題

> **範圍** — 最近共同祖先、節點間距離、父節點表（雙向）走訪，以及根到葉的路徑模板——所有答案是一條路徑或一個交會點、而不是某種形狀的樹問題。
> **另見**：[tree.md](./tree.md) — 這些東西所依賴的模式目錄與走訪模板；[tree_examples.md](./tree_examples.md) — 其餘的樹題詳解；[tree_backtrack.md](./tree_backtrack.md) — 回程要復原狀態的根→葉路徑；[bst.md](./bst.md) — 有序樹上的 LCA（LC 235）。

## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)

## 概觀

這個家族被三個問題主宰：*兩個節點在哪裡交會*（LCA）、*它們相隔多遠*（距離），
以及*哪些根到葉的路徑滿足某個性質*（路徑和／路徑長度）。三者都是一次 DFS——
差別只在那個數字是當參數**往下**走，還是當回傳值**往上**冒，以及你能不能**往上**走到父節點。

### 關鍵性質
- **複雜度**：這裡所有模板都是 O(N) 時間；遞迴空間 O(H)，一旦建出父節點表或圖就是 O(N)
- **核心想法**：LCA 就是分岔點 — `dist(p, q) = dist(lca, p) + dist(lca, q)`
- **什麼時候用**：答案是一條路徑、一段距離或一個交會點，而不是某個子樹性質
- **哨兵規則**：距離的輔助函式在「找不到」時必須回傳 `-1`（不能是 `0`），因為 `0` 是一個合法的距離

## 題型分類

| 類別 | 它回答的問題 | 模板 | 例題 |
|----------|--------------------|----------|----------|
| **LCA** | `p` 和 `q` 在哪裡交會？ | 後序 DFS，回傳第一個同時看到兩者的節點 | LC 236, 235, 1650, 865, 1123 |
| **距離（往下）** | 從某個祖先到目標有幾條邊？ | 前序 DFS 帶著 `depth`，用 `-1` 當哨兵 | LC 1740 |
| **距離（任意方向）** | 哪些節點距離為 `k`，不限方向？ | 父節點表 → 無向圖 → BFS | LC 863, 742 |
| **根到葉的路徑** | 哪些路徑的和／長相符合 X？ | 前序 DFS＋回溯 | LC 112, 113, 257 |
| **任意到任意的向下路徑** | 有幾條子路徑的和是 X？ | 前序 DFS＋前綴和 HashMap | LC 437 |
| **最長路徑** | 通過某節點的最長路徑有多長？ | 後序 DFS 回傳高度，配一個全域最大值 | LC 543, 687 |

## 模板與演算法

### 1) 最近共同祖先（LCA） — LC 236

```python
# LC 236 Lowest Common Ancestor of a Binary Tree
# LC 235 Lowest Common Ancestor of a Binary Search Tree
# LC 1650 Lowest Common Ancestor of a Binary Tree III
# V0
# IDEA : RECURSION + POST ORDER TRANSVERSAL
### NOTE : we need POST ORDER TRANSVERSAL for this problem
#          -> left -> right -> root
#          -> we can make sure that if p == q, then the root must be p and q's "common ancestor"
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        ### NOTE here
        # if not root or find p in tree or find q in tree
        # -> then we quit the recursion and return root
        if not root or p == root or q == root:
            return root
        ### NOTE here
        #  -> not root.left, root.right, BUT left, right
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        ### NOTE here
        # find q and p on the same time -> LCA is the current node (root)
        # if left and right -> p, q MUST in left, right sub tree respectively
        if left and right:
            return root
        ### NOTE here
        # if p, q both in left sub tree or both in right sub tree
        return left if left else right
```

```java
// java
// algorithm book p. 271
TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q){
    // base case
    if (root == null) return null;
    if (root == p || root == q) return root;
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    // case 1
    if (left != null && right != null){
        return root;
    }
    // case 2
    if (left == null && right == null){
        return null;
    }
    // case 3
    return left == null ? right: left;
}
```

#### LCA 變形 — 含所有最深節點的最小子樹（LC 865 / LC 1123） ⭐⭐⭐⭐

##### **1. 核心想法**

**關鍵洞見**：這題其實是換皮的 LCA。它沒有直接給你目標節點 `p` 和 `q`，目標是**隱含的**——所有位於最大深度的節點。

```text
Standard LCA (LC 236)                 Deepest Subtree LCA (LC 865)
-----------------------               --------------------------------
Targets p, q are GIVEN                Targets = nodes at max depth (discovered)
Find where p and q paths meet         Find where left/right deepest paths meet
```

竅門在於**一次後序走訪同時算出兩件事**：你得走完整棵樹才知道哪些節點最深，
但同時又需要那些節點的 LCA。所以每次遞迴呼叫都回傳一個**配對**：

```text
dfs(node) -> (depth, lca_candidate)
             ^^^^^  ^^^^^^^^^^^^^^^
             max depth      the answer for THIS subtree only
             below node
```

`depth` 那一半就是 LC 104（`max depth`）。`lca_candidate` 那一半跟著它一起往上帶，
並在**每個節點都依兩邊深度重新決定一次**。

**三種情況**（這個比較本身就是整個演算法）：

| 情況 | 意義 | 回傳 |
|------|---------|--------|
| `left.depth > right.depth` | 最深的節點全在左邊 | `(left.depth + 1, left.node)` — 把左邊的答案往上冒 |
| `right.depth > left.depth` | 最深的節點全在右邊 | `(right.depth + 1, right.node)` — 把右邊的答案往上冒 |
| `left.depth == right.depth` | **兩邊**都有最深節點 | `(left.depth + 1, node)` — **當前節點就是 LCA** |

**情況 3 為什麼對**：兩邊都在同一個深度觸底，代表最深節點集橫跨當前節點，
所以沒有任何一個子節點能全部包住——當前節點就是能包住它們的最小子樹。
而且只要往上的路上又出現深度打平，答案就會被覆寫，所以最後根呼叫拿到的是*最低*的那一個。

**情況 1/2 為什麼對**：如果有一邊嚴格更深，那較淺的那邊根本沒有最深節點，
所以答案完全落在較深的子樹裡——原封不動往上傳就好（**不要**換成當前節點）。

**base case**：`dfs(null) -> (0, None)`。null 子節點深度為 0，會讓葉子透過情況 3 回傳
`(1, leaf)`——一個葉子當然是它自己的 LCA。

##### **2. 模式：後序 DFS 回傳 `(metric, payload)`**

這是它的一般化形狀——**由下往上聚合，同時帶著一個候選答案**。
只要題目說*「滿足…的最小子樹」*或*「兩側極端交會的那個節點」*，就掏這招出來。

```text
# pattern skeleton
def dfs(node):
    if not node:
        return (BASE_METRIC, None)

    l_metric, l_ans = dfs(node.left)      # post-order: children FIRST
    r_metric, r_ans = dfs(node.right)

    if l_metric > r_metric:               # one side dominates -> pass its answer up
        return (l_metric + 1, l_ans)
    if r_metric > l_metric:
        return (r_metric + 1, r_ans)
    return (l_metric + 1, node)           # tie -> current node is the meeting point
```

**模式檢查清單：**
- **走訪順序**：後序（必須先知道兩個子節點才能決定）
- **回傳型別**：tuple／輔助類別——一個*純量指標*＋一個*節點參照*
- **決策依據**：比較兩個子節點的指標，絕不靠全域狀態
- **不做第二趟**：不要先算最大深度再重掃一次；一趟就夠了
- **time = O(N)**、**space = O(H)** — H = 樹高（遞迴堆疊）

> **和「全域變數」風格的對比**（LC 543 / LC 124）：那類題目只回傳一個純量，
> 答案藏在成員變數裡。這裡我們把答案放*在 tuple 裡*回傳，因為答案是在往上的路上
> 被**挑選**出來的，不是被取最大值取出來的。

**常見陷阱：**
- ❌ 情況 1/2 也回傳 `node` → 你永遠只會拿回根節點
- ❌ 打平那個情況用 `>=` 而不是 `==` → 錯誤地把情況 1 併進情況 3
- ❌ 走兩趟（先找最大深度，再找該深度所有節點的 LCA）→ 可行，但多花 O(N) 空間、程式碼也長得多

```java
// java
// LC 865 / LC 1123 — Smallest Subtree with All the Deepest Nodes
// Same as: LCA of the deepest leaves

// Helper class carries both the LCA candidate and its max depth below
class Result {
    TreeNode node;
    int dist;
    Result(TreeNode node, int dist) {
        this.node = node;
        this.dist = dist;
    }
}

/**
 * time = O(N)
 * space = O(H)  — recursion stack; O(log N) balanced, O(N) skewed
 */
public TreeNode subtreeWithAllDeepest(TreeNode root) {
    return dfs(root).node;
}

private Result dfs(TreeNode node) {
    if (node == null) {
        return new Result(null, 0);
    }

    Result left  = dfs(node.left);
    Result right = dfs(node.right);

    // Case 1: left subtree is deeper — LCA is buried there
    if (left.dist > right.dist) {
        return new Result(left.node, left.dist + 1);
    }

    // Case 2: right subtree is deeper — LCA is buried there
    if (right.dist > left.dist) {
        return new Result(right.node, right.dist + 1);
    }

    // Case 3: equal depth — current node is the LCA of all deepest nodes
    return new Result(node, left.dist + 1);
}
```

```python
# python
# LC 865 / LC 1123 — Smallest Subtree with All the Deepest Nodes
# IDEA: post-order DFS returning (depth, lca_node)
# time = O(N), space = O(H)
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        return self.helper(root)[1]

    def helper(self, node):
        # base case: null has depth 0 and no LCA
        if not node:
            return (0, None)

        # NOTE !!! post-order — children resolved BEFORE the decision
        left_depth, left_node = self.helper(node.left)
        right_depth, right_node = self.helper(node.right)

        # case 1) left deeper -> all deepest nodes on left, keep left's answer
        if left_depth > right_depth:
            return (left_depth + 1, left_node)

        # case 2) right deeper -> all deepest nodes on right, keep right's answer
        if right_depth > left_depth:
            return (right_depth + 1, right_node)

        # case 3) SAME depth -> deepest nodes on both sides -> current node is LCA
        return (left_depth + 1, node)
```

**視覺化：**
```text
        [3]          ← left.dist(3) == right.dist(2)? No → left wins
       /   \
     [5]   [1]       ← left.dist(2) == right.dist(1)? No → left wins
    /   \
  [6]  [2]           ← left.dist(0) == right.dist(1)? No → right wins
       /  \
      [7] [4]        ← both null, dist=0 → node [2] is LCA ✓
```

**小訣竅**：只要題目問「包含 [條件 X] 的最小子樹」，就想到**後序 DFS＋LCA 邏輯**。

##### **3. 相似的 LC 題目**

**完全同一題（同樣的程式碼，換個說法）：**

| 題目 | LC # | 說明 |
|---------|------|------|
| Smallest Subtree with all the Deepest Nodes | 865 | 就是這題 |
| Lowest Common Ancestor of Deepest Leaves | 1123 | **字面上就是同一題** — 解答直接複製貼上 |

**同一個模式 — 後序 DFS 回傳 `(metric, node)`／交會點邏輯：**

| 題目 | LC # | 回傳的指標 | 關鍵差異 |
|---------|------|-----------------|----------------|
| Lowest Common Ancestor of a Binary Tree | 236 | `node`（找到或 null） | 目標 `p`、`q` 是**給定的**；打平情況 = 兩個子節點都非 null |
| LCA of a BST | 235 | — | BST 性質讓你能用 O(H) 直接往下走，不需要後序 |
| LCA of a Binary Tree II | 1644 | `(node, count)` | `p`/`q` 可能不存在 → 還得回傳找到幾個 |
| LCA of a Binary Tree III | 1650 | — | 有父指標 → 變成「兩條鏈結串列的交點」 |
| LCA of a Binary Tree IV | 1676 | `node` | 目標節點有 N 個，不是 2 個 |
| Find Distance in a Binary Tree | 1740 | depth | 先找 LCA，再算 `d(root,p) + d(root,q) - 2*d(root,lca)` |

**同樣的「深度那一半」 — 只取 `(depth, …)` 這個部分：**

| 題目 | LC # | 差在哪 |
|---------|------|--------------|
| Maximum Depth of Binary Tree | 104 | **只**回傳深度 — 就是這題扣掉 payload |
| Balanced Binary Tree | 110 | 回傳深度＋一個 bool；`abs(l - r) > 1` 就短路 |
| Find Bottom Left Tree Value | 513 | 一樣找最深節點，但要**最左邊**的 → 用 BFS 層序更簡單 |
| Find Leaves of Binary Tree | 366 | 依高度把節點分組，而不是挑一個出來 |
| Maximum Depth of N-ary Tree | 559 | 迴圈跑 `children`，不是 `left`/`right` |

**同樣的「在每個節點合併左右」後序形狀（但走全域變數風格）：**

| 題目 | LC # | 合併出來的值 |
|---------|------|----------------|
| Diameter of Binary Tree | 543 | `left + right` 條邊，答案存在成員變數 |
| Binary Tree Maximum Path Sum | 124 | `left + right + node.val`，負的夾成 0 |
| Longest Univalue Path | 687 | 只有 `child.val == node.val` 時才往左／右延伸 |
| Count Good Nodes in Binary Tree | 1448 | 改成前序 — 資訊**往下**流，不是往上 |

**判斷提示：**
```text
"smallest subtree containing X"      -> post-order (metric, node)   [LC 865]
"LCA of given nodes p, q"            -> post-order found-or-null     [LC 236]
"longest/max path through any node"  -> post-order + global var      [LC 543, 124]
"deepest / leftmost / level info"    -> BFS level-order              [LC 513, 199]
```

### 2) Move Parent 模式 - 雙向樹走訪

**核心概念**：建一張父節點表，把樹變成圖，再用 BFS 做多方向的探索。

#### **模式總覽**
```text
Standard Tree (Unidirectional)        →    Tree with Parent Map (Bidirectional)

      1                                          1
     / \              Build Parent Map          / \
    2   3             ===============>         2 ← 3
   / \                                        / \
  4   5                                      4 ← 5

Can only go down (left/right)          Can go down (left/right) AND up (parent)
```

#### **LC 863: All Nodes Distance K in Binary Tree**

```java
// java
// IDEA: DFS + Parent Map + BFS
/**
 * Why this works?
 *
 * Tree → Graph → BFS (visiting)
 *
 * • From target you need to explore all directions reachable in k steps:
 *   left, right, and up (to parent).
 *   Converting the tree to an undirected graph (children + parent edges)
 *   and then running BFS from target to depth k returns the desired nodes.
 *
 * • visited ensures we don't revisit nodes (which would otherwise make the BFS
 *   incorrect/infinite once parent edges are present).
 */

public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
    List<Integer> res = new ArrayList<>();
    // parentMap stores parent pointers for every node (node -> parent)
    Map<TreeNode, TreeNode> parentMap = new HashMap<>();

    if (root == null) return res;

    // Step 1: Build parent map for all nodes
    buildParentMap(root, null, parentMap);

    // Step 2: BFS starting from target, stop at distance k
    Queue<TreeNode> queue = new LinkedList<>();
    Set<TreeNode> visited = new HashSet<>();
    queue.offer(target);
    visited.add(target);
    int dist = 0;

    /**
     * • Each loop iteration processes one BFS "level"
     *   (all nodes at the same distance from target).
     *
     * • If current distance dist equals k, the nodes currently
     *   in queue are exactly the nodes at distance k.
     */
    while (!queue.isEmpty()) {
        int size = queue.size();

        if (dist == k) {
            // Collect all nodes currently in the queue
            for (TreeNode node : queue) {
                res.add(node.val);
            }
            break;
        }

        /**
         * NOTE!!!
         * For each node, we visit cur.left, cur.right, and its parent via BFS
         *
         * • Process the size nodes of the current level:
         *   - For each cur, try to move to cur.left, cur.right, and its parent
         *   - visited.add(node) returns true only if node was not already present
         *     That both checks and marks in one call
         *
         * • After processing the whole level, increment dist and continue
         */
        for (int i = 0; i < size; i++) {
            TreeNode cur = queue.poll();

            // Explore neighbors: left, right, parent
            if (cur.left != null && visited.add(cur.left)) {
                queue.offer(cur.left);
            }
            if (cur.right != null && visited.add(cur.right)) {
                queue.offer(cur.right);
            }
            TreeNode parent = parentMap.get(cur);
            if (parent != null && visited.add(parent)) {
                queue.offer(parent);
            }
        }
        dist++;
    }

    return res;
}

/**
 * NOTE!!! Helper function to build parent map
 *
 * • We need to be able to move upwards from any node (to parent).
 *   A binary tree node only knows left/right children, so we precompute
 *   parents by a DFS.
 *
 * • Simple DFS that records parent of each node (parentMap.put(node, parent))
 * • For root we pass parent = null
 * • After this every node maps to its parent (or null for root)
 */
private void buildParentMap(TreeNode node, TreeNode parent,
                            Map<TreeNode, TreeNode> parentMap) {
    if (node == null) return;

    parentMap.put(node, parent);
    buildParentMap(node.left, node, parentMap);
    buildParentMap(node.right, node, parentMap);
}
```

```python
# python
# LC 863. All Nodes Distance K in Binary Tree
from collections import defaultdict, deque

def distanceK(root, target, k):
    """
    IDEA: Build bidirectional graph + BFS

    Step 1: DFS to build parent-child bidirectional edges
    Step 2: BFS from target to find all nodes at distance k
    """

    # Build undirected graph
    graph = defaultdict(list)

    def build_graph(parent, child):
        """DFS to build bidirectional edges"""
        if parent and child:
            graph[parent.val].append(child.val)
            graph[child.val].append(parent.val)
        if child.left:
            build_graph(child, child.left)
        if child.right:
            build_graph(child, child.right)

    # Build graph from root
    build_graph(None, root)

    # BFS from target
    queue = deque([(target.val, 0)])
    visited = {target.val}
    result = []

    while queue:
        node_val, dist = queue.popleft()

        if dist == k:
            result.append(node_val)
            continue

        # Explore all neighbors (left, right, parent)
        for neighbor in graph[node_val]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append((neighbor, dist + 1))

    return result
```

#### **重點**
1. **建父節點表**：O(N) 時間、O(N) 空間
2. **BFS 探索**：最壞情況 O(N) 時間
3. **visited 集合**：不加就會無窮迴圈，非常關鍵
4. **應用場合**：
   - 跟距離有關的問題
   - 找任意兩節點之間的路徑
   - 找最近且滿足某性質的節點
   - 需要往上走訪的問題

#### **模式比較：標準樹走訪 vs Move Parent**

| 面向 | 標準樹走訪 | Move Parent 模式 |
|--------|-------------------------|---------------------|
| **方向** | 單向（只能往下） | 雙向（往下＋往上） |
| **前處理** | 不需要 | 建父節點表（O(N)） |
| **空間複雜度** | O(h) 遞迴堆疊 | O(N) 父節點表＋visited |
| **要記 visited 嗎** | 通常不用 | **非常關鍵**，用來防環 |
| **走訪方式** | 遞迴 DFS | DFS（建表）＋BFS（探索） |
| **適用場景** | 標準樹題 | 距離、路徑、多方向 |
| **圖的轉換** | 樹還是樹 | 樹 → 無向圖 |

#### **要避開的常見錯誤**
1. ❌ 忘了 visited 集合 → 無窮迴圈
2. ❌ 沒處理根節點沒有父節點的情況 → NPE
3. ❌ 算距離時用 DFS 而不是 BFS → 結果不對
4. ❌ 用值而不是節點參照來建圖 → 有重複值時就爛掉

#### **Move Parent 食譜**

**模板結構：**
```text
1. Build parent map (DFS preprocessing)
2. Convert tree to undirected graph (children + parent edges)
3. BFS from target node, exploring all neighbors (left, right, parent)
4. Track visited nodes to avoid cycles
5. Stop at desired distance/condition
```

### 3) 兩節點之間的距離 — LC 1740


```java
// java
// LC 1740 Find Distance in a Binary Tree

// V1
/**
 *  IDEA of `getPathLen` help func:
 *
 *  🧠 Summary of Logic Flow
 *  1.  Stop when null (return -1) or when target is found (return distance).
 *  2.  Search left first. If found, return immediately.
 *  3.  Otherwise, search right.
 *  4.  If neither side contains the target, the function will bubble up -1.
 */
private int getPathLen(TreeNode root, int target, int dist) {
    /** NOTE !!!
     *
     *   base case:
     *
     *  •   Base case #1:
     *        if we hit a null node,
     *        -> the target DOES NOT exist in this branch.
     *
     *  •   Returning -1 is a sentinel value
     *      meaning “not found in this subtree”
     */
    if (root == null) {
        return -1;  // not found
    }
    /**
     *  NOTE !!!
     *
     *  •   Base case #2: if the current node matches the target, return dist, which is the current number of edges from the starting node (typically the LCA) to this node.
     *  •   This is the successful termination of the recursion.
     */
    if (root.val == target) {
        return dist;
    }

    /**
     *  NOTE !!!
     *
     *  •   Recurse into the left subtree.
     *  •   Increment dist by 1 because we moved down one level.
     *  •   Store the result in left.
     *        - If target is in this subtree,
     *          left will contain the distance.
     *       - Otherwise, left will be -1.
     */
    int left = getPathLen(root.left, target, dist + 1);
    /**
     *  •   If we found the target in the left subtree,
     *      return that distance immediately.
     *  •   This avoids unnecessary searching in the right subtree.
     */
    if (left != -1) {
        return left;
    }

    /**
     *  NOTE !!!
     *
     *  •   If not found on the left, search the right subtree with dist + 1.
     *  •   Return the result directly:
     *       - Either a valid distance if found,
     *       - Or -1 if not found in right subtree either.
     *
     */
    int right = getPathLen(root.right, target, dist + 1);
    return right;
}
    

// V2
public int findDistance(TreeNode root, int p, int q) {
    TreeNode lca = findLCA(root, p, q);
    return getDistance(lca, p) + getDistance(lca, q);
}

private TreeNode findLCA(TreeNode node, int p, int q) {
    if (node == null || node.val == p || node.val == q) {
        return node;
    }
    TreeNode left = findLCA(node.left, p, q);
    TreeNode right = findLCA(node.right, p, q);

    if (left != null && right != null) return node;
    return left != null ? left : right;
}

private int getDistance(TreeNode node, int target) {
    if (node == null) return -1;
    if (node.val == target) return 0;

    /** NOTE !!! check left BEFORE recursing right -- this is the short-circuit the
     *  walkthrough below describes. Evaluating both first still returns the right
     *  answer, but it visits the whole right subtree after the target was found. */
    int leftDist = getDistance(node.left, target);
    if (leftDist != -1) return leftDist + 1;

    int rightDist = getDistance(node.right, target);
    if (rightDist != -1) return rightDist + 1;
    return -1;
}
```

#### **Python — `get_dist` 輔助函式（關鍵部分）** ⭐⭐⭐⭐⭐

**想法：** `findDistance` = `get_lca`（找分岔點）＋ 兩次 `get_dist`（從 LCA 量各自那一段）。所有的竅門都在 `get_dist` 裡：

```python
# python
# LC 1740 Find Distance in a Binary Tree
class Solution:
    def findDistance(self, root, p, q):
        if p == q or not root:
            return 0
        # Step 1: find the split point (Lowest Common Ancestor)
        lca = self.get_lca(root, p, q)
        # Step 2: measure edge distance from LCA down to each target
        dist_p = self.get_dist(lca, p, 0)
        dist_q = self.get_dist(lca, q, 0)
        # dist is ALWAYS the sum — see the 2 structural cases below
        return dist_p + dist_q

    def get_lca(self, root, p, q):
        if not root:
            return None
        if root.val == p or root.val == q:  # match by VALUE (p, q are ints)
            return root
        left = self.get_lca(root.left, p, q)
        right = self.get_lca(root.right, p, q)
        if left and right:      # p, q split here -> this node is the LCA
            return root
        return left if left else right

    # NOTE !!! below helper func -- pre-order DFS
    def get_dist(self, root, target, dist):
        """
        NOTE !!!
        If not root, we return `-1` (sentinel = "not found in this branch"),
        but NOT `0`, since 0 is also a VALID answer (target found at current node).
        """
        if not root:
            return -1          # <-- sentinel: dead end, target not on this path

        if root.val == target:
            return dist        # <-- found: dist = # edges from LCA to here

        # NOTE !!! the right recursion is INSIDE the else path -- that is what makes
        #          the short-circuit real. Calling both first and then testing `left`
        #          walks the right subtree even when the target was already found.
        left = self.get_dist(root.left, target, dist + 1)
        if left != -1:
            # left subtree found the target -- pass that valid distance up, skip right
            return left

        # Otherwise return whatever the right subtree finds (a valid dist, or -1)
        return self.get_dist(root.right, target, dist + 1)
```

**「找不到」為什麼要用 `-1` 而不是 `0`？**

| 回傳值 | 意義 |
|--------------|---------|
| `0`          | **合法** — 目標就在當前節點（相隔 0 條邊） |
| `dist > 0`   | **合法** — 目標在起點下方 `dist` 條邊處 |
| `-1`         | **哨兵** — 目標不在這一支裡（死路） |

如果「找不到」回傳 `0`，那就沒辦法區分*「就在這裡，距離 0」*和*「找不到」*。所以 `0` 被保留給真正的距離，`-1` 是唯一安全的「找不到」旗標。

**合法距離是怎麼往上冒的（前序 DFS）：**
1. 碰到 `None` → 回傳 `-1`（這條路是死路）。
2. 碰到目標 → 回傳累積的 `dist`（一個 ≥ 0 的邊數）。
3. 否則帶著 `dist + 1` 遞迴 `left`／`right`。
   - `left != -1` → 目標在左子樹，**立刻**把那個距離往上傳（短路，跳過右邊）。
   - 否則回傳 `right`（不是右子樹的合法距離，就是兩邊都失敗的 `-1`）。

**為什麼 `dist_p + dist_q` 永遠正確** — 從 LCA 出發只有兩種形狀：

```text
case 1: p and q are in different subtrees      case 2: one target IS the LCA
                                                        (ancestor of the other)
          LCA                                        p (= LCA)
         /   \                                          \
        p     q                                          ...
                                                           q
   dist = dist_p + dist_q                        dist_p = 0, so dist = dist_q
```

兩種情況下，`get_dist(lca, p) + get_dist(lca, q)` 都剛好等於路徑 `p … q` 上的邊數。

#### **視覺化 — `get_dist` 實際上是怎麼走這棵樹的** 🎨

用 LC 1740 例子裡的樹：`root = [3,5,1,6,2,0,8,null,null,7,4]`、`p = 5`、`q = 0` → 答案 `3`。

```text
                      3          <- depth 0  (this is also the LCA of 5 and 0)
                   /     \
                  5       1      <- depth 1
                /   \    /  \
               6     2  0    8   <- depth 2
                    / \
                   7   4         <- depth 3
```

**第 1 步 — `get_lca(root, 5, 0)` → 節點 `3`**（5 在左子樹、0 在右子樹 → 它們在 `3` 分岔）。

**第 2 步 — `get_dist(3, target=5, depth=0)`**

```text
get_dist(3, 5, 0)                 3 != 5  -> recurse left with depth+1
└── get_dist(5, 5, 1)             5 == 5  -> RETURN 1  ✅
        (right subtree never visited — short-circuited by `if left != -1`)

=> dist_p = 1
```

**第 3 步 — `get_dist(3, target=0, depth=0)`** — 這步比較有意思，因為左半邊是**死路**：

```text
get_dist(3, 0, 0)                       3 != 0
│
├── get_dist(5, 0, 1)                   5 != 0
│   ├── get_dist(6, 0, 2)               6 != 0
│   │   ├── get_dist(None, 0, 3) -> -1      ❌ dead end
│   │   └── get_dist(None, 0, 3) -> -1      ❌ dead end
│   │   RETURN -1                            ❌ bubbles up
│   └── get_dist(2, 0, 2)               2 != 0
│       ├── get_dist(7, 0, 3) -> -1 (both children None)   ❌
│       └── get_dist(4, 0, 3) -> -1 (both children None)   ❌
│       RETURN -1                            ❌
│   RETURN -1   <- whole LEFT subtree of 3 says "not here"
│
│   (left == -1, so we DO NOT short-circuit — we must try right)
│
└── get_dist(1, 0, 1)                   1 != 0
    └── get_dist(0, 0, 2)               0 == 0  -> RETURN 2  ✅
    RETURN 2  <- passed up UNCHANGED (no `+1`!)
RETURN 2

=> dist_q = 2
```

**答案：** `dist_p + dist_q = 1 + 2 = 3` ✅（路徑 `5 - 3 - 1 - 0`，3 條邊）

**這段追蹤裡要注意的兩件事：**

```text
1) depth grows going DOWN      2) the found value flows UP untouched
   (as an argument)               (no accumulation on the way back)

     get_dist(.., depth=0)          RETURN 2  ▲
            │  depth+1                        │  same 2
            ▼                        RETURN 2 ▲
     get_dist(.., depth=1)                    │  same 2
            │  depth+1              RETURN 2  ▲
            ▼                                 │
     get_dist(.., depth=2)  ==  target  ->  emit `depth` (= 2)
```

因為目標值是**唯一的**，最多只有一支會回傳非 `-1` 的值——所以往上的路上根本沒有東西要 `max()` 或相加。這正是它和高度／深度類問題最關鍵的差別：

| | 數字的流動方向 | 合併步驟 |
|---|---|---|
| `get_height`（LC 104） | **由下往上**算出來 | `1 + max(left, right)` |
| `get_dist`（這個模式） | **由上往下**帶，再原樣回傳上去 | 沒有 — 只是把非 `-1` 的值往上傳 |

**哨兵傳遞速查：**

```text
            left      right     ->  return        meaning
            ----      -----         ------        -------
            -1        -1        ->  -1            target in NEITHER subtree
            d≥0       (skipped) ->  d             found left (short-circuit)
            -1        d≥0       ->  d             found right
```

#### **`get_dist` 的各種變形 — 計數器住在哪裡** ⭐⭐⭐⭐⭐

以下全部回傳**同樣的邊數**（目標不在子樹裡時回傳 `-1`），差別只在*計數器住在哪裡*。
上面那個由上往下的版本是標準版；下面只寫出那兩個不只是換個寫法的變形。

| # | 變形 | 簽名 | 計數器位置 | 會短路嗎？ | 說明 |
|---|---------|-----------|---------------|-----------------|-------|
| **V1** | 由上往下＋明確守衛 | `(node, target, depth)` | 當參數**往下**傳 | ✅ 會（`if left != -1`） | 上面那個標準版 — 最明確 |
| **V2** | 由上往下＋`max()` 小技巧 | `(node, target, depth)` | 當參數**往下**傳 | ❌ 不會 | 就是 V1 把 `!= -1` 守衛換成 `return max(left, right)`：目標唯一，所以最多一邊會回傳 `>= 0`，`max` 會把它冒上來（兩邊都失敗時剛好是 `-1`）。程式碼最短，但它一定會掃**兩邊**子樹 — 嚴格來說比 V1 多做事，所以優先用 V1 |
| **V3** | 由下往上（回傳時 `+1`） | `(node, target)` | 在回程路上**往上**累積 | ✅ 會 | 不需要 `depth` 參數 — 下面寫出來 |
| **V4** | 迭代式 BFS | `(root, target)` | 存**在佇列裡** | ✅ 會（提早回傳） | 沒有遞迴 → 沒有堆疊爆掉的風險 — 下面寫出來 |

##### **V3 — 由下往上的 DFS（不需要 `depth` 參數）**

**核心想法**：不把計數器*往下*帶，而是碰到目標時從 `0` 開始，回程每經過一條邊就加 `1`。這樣函式只需要兩個參數。

```python
# python
# time = O(N), space = O(H)
def get_dist(self, node, target):
    if not node:
        return -1
    if node.val == target:
        return 0                 # distance to itself is 0

    _left  = self.get_dist(node.left,  target)
    _right = self.get_dist(node.right, target)

    # If found on the left, add 1 for the current edge and return it
    if _left >= 0:
        return _left + 1

    # If found on the right, add 1 for the current edge and return it
    if _right >= 0:
        return _right + 1

    return -1
```

**方向對比（V1 vs V3），以 `get_dist(3, target=0)` 為例：**

```text
        V1 (top-down)                      V3 (bottom-up)
        depth flows DOWN                   +1 flows UP

   3   get_dist(3, .., depth=0)       3   return 1 + 1 = 2   ▲
   |            │                     |                      │  +1
   1   get_dist(1, .., depth=1)       1   return 0 + 1 = 1   ▲
   |            │                     |                      │  +1
   0   match -> return depth = 2      0   match -> return 0  ▲

   answer emitted at the BOTTOM       answer assembled on the WAY BACK
```

⚠️ **陷阱**：這裡千萬別寫 `1 + max(_left, _right)` — 兩邊都是 `-1` 時會得到 `0`，變成謊報*「就在這個節點找到」*。`+1` 一定要擋在 `>= 0` 的檢查後面（或者像 LeetCode-CA 那個版本，在 `1 + max(...)` **之前**先檢查 `if left == right == -1: return -1`）。

##### **V4 — 迭代式 BFS（層序，不用遞迴）**

**核心想法**：把 `(node, dist)` 配對推進佇列；節點所在的層數**就是**它離起點的距離。樹很深、遞迴有爆堆疊風險時很好用。

```python
# python
# time = O(N), space = O(W)  -- W = max tree width
from collections import deque

def get_dist(self, root, target):
    if not root:
        return -1

    q = deque([(root, 0)])   # [node, current_distance]

    while q:
        node, dist = q.popleft()

        if node.val == target:
            return dist      # first hit IS the answer (level == distance)

        if node.left:
            q.append((node.left, dist + 1))
        if node.right:
            q.append((node.right, dist + 1))

    return -1                # scanned everything, target not below `root`
```

**佇列追蹤 — 在範例樹上跑 `get_dist(3, target=0)`：**

```text
                  3(0)
                /      \
             5(1)      1(1)
            /   \     /   \
          6(2) 2(2) 0(2)  8(2)      <- (n) = dist stored alongside the node

 pop        queue after push                        check
 ----       -------------------                     -----
 (3,0)      [(5,1), (1,1)]                          3 != 0
 (5,1)      [(1,1), (6,2), (2,2)]                   5 != 0
 (1,1)      [(6,2), (2,2), (0,2), (8,2)]            1 != 0
 (6,2)      [(2,2), (0,2), (8,2)]                   6 != 0
 (2,2)      [(0,2), (8,2), (7,3), (4,3)]            2 != 0
 (0,2)      -                                       0 == 0  -> RETURN 2  ✅
```

⚠️ **注意**：這裡是在推入前就擋掉 `None` 子節點，所以迴圈*內部*不需要 `-1` 哨兵——哨兵就是佇列清空後那一行 `return -1`。

##### **該用哪一個？**

- **面試／講清楚** → **V1**（面試官想看的就是你怎麼推理哨兵的處理）
- **想寫得短、一行漂亮解** → **V2**（記得主動說出你放棄了短路）
- **參數少一點、想要「在目標處回傳 0」這個慣例** → **V3**（跟上面那個 Java 版 `getDistance` 一致）
- **樹很深／很斜，遞迴深度真的有風險** → **V4**

四種都是 `O(N)` 時間。空間：V1–V3 是 `O(H)`（遞迴堆疊，斜樹最壞 `H = N`），V4 是 `O(W)`（佇列寬度）。

**常見錯誤：**
- ❌ `None` 回傳 `0` 而不是 `-1` → 沒辦法區分「就在這個節點找到」和「找不到」
- ❌ 漏了 `if left != -1: return left` 這個守衛 → 回傳了右子樹的 `-1`，把找到的距離弄丟
- ❌ 在傳 depth 的版本裡又加 `+1` → 重複計數（往下的路上已經加過了）
- ❌ 從 `root` 而不是從 `lca` 呼叫 `get_dist` → 量到的是錯的路徑

**這個模式還會在哪裡出現：**

| 題目 | LC # | `get_dist` 怎麼被用 |
|---------|------|------------------------|
| Find Distance in a Binary Tree | 1740 | 基本款 — `get_lca` ＋ `get_dist` × 2 |
| All Nodes Distance K | 863 | 從目標往下的距離；往上的部分交給父節點表處理（第 2 節） |
| Maximum Depth | 104 | 同樣的 DFS 形狀，但改成由下往上的 `1 + max(...)`，沒有哨兵 |
| Path Sum | 112 | 一模一樣的由上往下累積，只是帶的是 `remaining_sum` 而不是 `depth` |
| Smallest Subtree w/ Deepest Nodes | 865/1123 | LCA 和深度合併成一個 `(depth, node)` 回傳 |

> **參考：** `leetcode_python/Tree/find-distance-in-a-binary-tree.py`

#### **模式辨識 — 這是不是一題 `get_dist`？**

**辨識條件：**
- ✅ 要的是**邊數**，不是節點數（深度 0 的節點相隔 0 條邊）
- ✅ 目標在樹裡是**唯一的**（值不重複）
- ✅ 距離是從某個已知起點（根或 LCA）**往下**量的
- ❌ 如果兩個節點可能在互不相干的子樹裡 → 先找 LCA（見第 1 節）
- ❌ 如果需要**所有**方向的距離（包含往上）→ 用第 2 節的 Move Parent 模式

### 4) 根到葉的路徑模板

#### 前序 DFS＋回溯模板（Java）

```java
// Template for root-to-leaf path collection (LC 112 / 113 / 257)
void dfs(TreeNode node, int remaining, List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;

    // 1. Pre-order: add current node FIRST
    path.add(node.val);
    remaining -= node.val;

    // 2. Check leaf condition
    if (node.left == null && node.right == null && remaining == 0) {
        result.add(new ArrayList<>(path));  // save a COPY
    } else {
        // 3. Recurse
        dfs(node.left, remaining, path, result);
        dfs(node.right, remaining, path, result);
    }

    // 4. Backtrack: remove current node
    path.remove(path.size() - 1);
}
```

#### 路徑更新策略：不可變字串 vs 可變 List＋回溯

> DFS 過程中追蹤路徑狀態有兩種做法。選對了，程式碼會簡單非常多。

**策略 1：不可變字串 — 在 DFS 呼叫裡直接把更新後的路徑傳下去（不需要回溯）**

關鍵洞見：當你把 `path + "->" + node.val` 直接當參數傳下去，每一層遞迴拿到的都是**自己的那份字串副本**。父層的 `path` 從沒被改過，所以**完全不需要顯式回溯**。

```java
// LC 257 — Binary Tree Paths (String path, no backtrack)
// Reference: ref_code/interviews-master/leetcode/tree/BinaryTreePaths.java
public List<String> binaryTreePaths(TreeNode root) {
    List<String> res = new ArrayList<>();
    if (root == null) return res;
    dfs(root, String.valueOf(root.val), res);
    return res;
}

private void dfs(TreeNode node, String path, List<String> res) {
    // 1. Leaf check: path is complete
    if (node.left == null && node.right == null) {
        res.add(path);
        return;
    }

    // 2. Traverse Left: path update happens INSIDE the DFS call
    if (node.left != null) {
        /** NOTE !!!
         *  We do `path update` within DFS call itself.
         *  path + "->" + node.left.val creates a NEW string,
         *  so `path` in the current frame is unchanged — no backtrack needed.
         */
        dfs(node.left, path + "->" + node.left.val, res);
    }

    // 3. Traverse Right: same pattern
    if (node.right != null) {
        /** NOTE !!!
         *  Same idea: path is NOT mutated here.
         *  Each branch gets its own copy of the string.
         */
        dfs(node.right, path + "->" + node.right.val, res);
    }
}
```

**策略 2：可變 List — 原地修改，然後回溯**

用可變的資料結構（例如 `List<Integer>`）時，**同一個物件**會被所有遞迴層共用。遞迴回來之後你**一定要**把改動還原。

```java
// LC 113 — Path Sum II (List path, explicit backtrack)
void dfs(TreeNode node, int remaining, List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;

    path.add(node.val);           // ← mutate shared list
    remaining -= node.val;

    if (node.left == null && node.right == null && remaining == 0) {
        result.add(new ArrayList<>(path));  // save a COPY
    } else {
        dfs(node.left, remaining, path, result);
        dfs(node.right, remaining, path, result);
    }

    path.remove(path.size() - 1); // ← BACKTRACK: undo mutation
}
```

**比較：**

| 面向 | 不可變字串 | 可變 List＋回溯 |
|--------|-----------------|--------------------------|
| 路徑在哪裡更新 | 在 DFS 呼叫的參數裡 | 在 DFS 呼叫之前 |
| 需要回溯嗎？ | 不用（每層都有自己的副本） | 要（必須還原修改） |
| 記憶體 | 每條路徑產生 O(N) 個新字串 | O(N)，共用同一個重複利用的 list |
| 最適合 | 字串路徑（LC 257） | 數值路徑（LC 113、112） |
| 出 bug 的風險 | 低（沒有共用狀態） | 中（容易忘記回溯） |

**經驗法則：**
- **不可變（String、int）** → 在呼叫裡把更新後的值傳下去 → 不用回溯
- **可變（List、StringBuilder）** → 呼叫前修改 → 呼叫後回溯

```python
# Python equivalent — immutable string path (LC 257)
def binaryTreePaths(root):
    res = []
    def dfs(node, path):
        if not node.left and not node.right:
            res.append(path)
            return
        if node.left:
            dfs(node.left, path + "->" + str(node.left.val))  # new string, no backtrack
        if node.right:
            dfs(node.right, path + "->" + str(node.right.val))
    if root:
        dfs(root, str(root.val))
    return res
```

#### 前序 DFS＋前綴和 HashMap 模板（Java）

> 用在路徑可以從**任何節點**開始／結束（不限根到葉）的時候。
> 靈感來自 LC 437 Path Sum III。

**核心想法 — 「樹上的 2-Sum」：**
```text
curSum - targetSum = ancestorSum
→ if ancestorSum exists in map, a valid sub-path ends at current node
```

**為什麼是前序？**
- 前綴和必須**由上往下**算（前序）
- 後序算出來的是子樹的和，不是根到節點的前綴和

```java
// Template: Pre-order DFS + Prefix Sum HashMap (LC 437)
int count = 0;
Map<Long, Integer> prefixMap = new HashMap<>();

int pathSum(TreeNode root, int targetSum) {
    prefixMap.put(0L, 1);  // base case: empty path has sum 0
    dfs(root, 0L, targetSum);
    return count;
}

void dfs(TreeNode node, long curSum, int targetSum) {
    if (node == null) return;

    // 1. Pre-order: update prefix sum with current node
    curSum += node.val;

    // 2. Check: curSum - targetSum = a previous prefix sum?
    //    → means a valid sub-path ends here
    //    (2-sum trick: curSum - ancestorSum = targetSum)
    count += prefixMap.getOrDefault(curSum - targetSum, 0);

    // 3. Record current prefix sum BEFORE recursing into children
    prefixMap.put(curSum, prefixMap.getOrDefault(curSum, 0) + 1);

    // 4. Recurse (pre-order: process node before children)
    dfs(node.left, curSum, targetSum);
    dfs(node.right, curSum, targetSum);

    // 5. BACKTRACK: remove curSum so sibling branches are not affected
    prefixMap.put(curSum, prefixMap.get(curSum) - 1);
}
```

**和根到葉回溯的關鍵差異：**

| 模式                      | 路徑限制          | 資料結構        | 回溯什麼？         |
|------------------------------|--------------------------|-----------------------|-------------------------|
| DFS＋路徑 list＋回溯  | 只能根 → 葉         | `List<Integer>` path  | 移除最後一個元素     |
| DFS＋前綴和＋回溯 | 任意節點 → 任意節點 ↓   | `Map<Long, Integer>`  | 把 map 的計數減一     |

## LC 範例

### 5-1) 找出具備特定性質的路徑

#### 路徑和問題
```python
# LC 112 Path Sum - Has Path with Target Sum
def hasPathSum(self, root, targetSum):
    if not root:
        return False
    if not root.left and not root.right:
        return root.val == targetSum
    return (self.hasPathSum(root.left, targetSum - root.val) or
            self.hasPathSum(root.right, targetSum - root.val))

# LC 113 Path Sum II - All Paths with Target Sum
def pathSum(self, root, targetSum):
    result = []

    def dfs(node, remaining, path):
        if not node:
            return

        path.append(node.val)

        if not node.left and not node.right and remaining == node.val:
            result.append(path[:])

        dfs(node.left, remaining - node.val, path)
        dfs(node.right, remaining - node.val, path)

        path.pop()  # backtrack

    dfs(root, targetSum, [])
    return result

# LC 437 Path Sum III - Number of Paths with Target Sum (any start/end)
def pathSum(self, root, targetSum):
    def dfs(node, current_sum):
        if not node:
            return 0

        current_sum += node.val
        result = prefix_sum.get(current_sum - targetSum, 0)

        prefix_sum[current_sum] = prefix_sum.get(current_sum, 0) + 1

        result += dfs(node.left, current_sum)
        result += dfs(node.right, current_sum)

        prefix_sum[current_sum] -= 1
        return result

    prefix_sum = {0: 1}
    return dfs(root, 0)
```

#### 路徑長度問題
```python
# LC 543 Diameter of Binary Tree - Longest Path Between Any Two Nodes
def diameterOfBinaryTree(self, root):
    self.diameter = 0

    def dfs(node):
        if not node:
            return 0

        left_depth = dfs(node.left)
        right_depth = dfs(node.right)

        # Update diameter through current node
        self.diameter = max(self.diameter, left_depth + right_depth)

        return 1 + max(left_depth, right_depth)

    dfs(root)
    return self.diameter

# LC 687 Longest Univalue Path - Longest Path with Same Values
def longestUnivaluePath(self, root):
    self.longest = 0

    def dfs(node):
        if not node:
            return 0

        left_length = dfs(node.left)
        right_length = dfs(node.right)

        left_path = left_length + 1 if node.left and node.left.val == node.val else 0
        right_path = right_length + 1 if node.right and node.right.val == node.val else 0

        self.longest = max(self.longest, left_path + right_path)

        return max(left_path, right_path)

    dfs(root)
    return self.longest
```

```java
// java
// LC 112 Path Sum
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) {
        return false;
    }

    if (root.left == null && root.right == null) {
        return root.val == targetSum;
    }

    return hasPathSum(root.left, targetSum - root.val) ||
           hasPathSum(root.right, targetSum - root.val);
}

// LC 113 Path Sum II
public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    dfs(root, targetSum, path, result);
    return result;
}

private void dfs(TreeNode node, int remaining, List<Integer> path,
                List<List<Integer>> result) {
    if (node == null) return;

    path.add(node.val);

    if (node.left == null && node.right == null && remaining == node.val) {
        result.add(new ArrayList<>(path));
    }

    dfs(node.left, remaining - node.val, path, result);
    dfs(node.right, remaining - node.val, path, result);

    path.remove(path.size() - 1); // backtrack
}
```

### 5-2) Closest Leaf in a Binary Tree（Move Parent 模式） — LC 742
```python
# LeetCode 742. Closest Leaf in a Binary Tree
# V0
# IDEA : DFS build GRAPH + BFS find ans (MOVE PARENT PATTERN)
# See section 2 (Move Parent Pattern) for detailed explanation of this pattern
### NOTE :  closest to a leaf means the least number of edges travelled on the binary tree to reach any leaf of the tree. Also, a node is called a leaf if it has no children.
#         -> We only consider the min distance between left (no sub tree) and k
### NOTE : we need DFS create the graph
# https://www.youtube.com/watch?v=x1wXkRrpavw
# https://blog.csdn.net/qq_17550379/article/details/87778889
import collections
class Solution:
    # build graph via DFS
    # node : current node
    # parent : parent of current node
    def buildGraph(self, node, parent, k):
        if not node:
            return
        # if node.val == k, THEN GET THE start point FROM current "node",
        # then build graph based on above
        if node.val == k:
            self.start = node
        if parent:
            self.graph[node].append(parent)
            self.graph[parent].append(node)
        self.buildGraph(node.left, node, k)
        self.buildGraph(node.right, node, k)

    # search via DFS
    def findClosestLeaf(self, root, k):


        self.start = None
        ### NOTE : we need DFS create the graph
        self.buildGraph(root, None, k)
        q, visited = [root], set()
        #q, visited = [self.start], set() # need to validate this
        self.graph = collections.defaultdict(list)
        while q:
            for i in range(len(q)):
                cur = q.pop(0)
                # add cur to visited, NOT to visit this node again
                visited.add(cur)
                ### NOTICE HERE 
                # if not cur.left and not cur.right: means this is the leaf (HAS NO ANY left/right node) of the tree
                # so the first value of this is what we want, just return cur.val as answer directly
                if not cur.left and not cur.right:
                    # return the answer
                    return cur.val
                # if not find the leaf, then go through all neighbors of current node, and search again
                for node in self.graph:
                    if node not in visited: # need to check if "if node not in visited" or "if node in visited"
                        q.append(node)
```

## 模式選擇策略

**第 2 步 — 套用模式：**

```text
Root-to-leaf path problem?
  → Pre-order DFS + backtracking
  → Pattern: add node → check leaf → recurse → remove node (backtrack)

Path sum from ANY node to ANY node (downward)?
  → Pre-order DFS + prefix sum HashMap (2-sum trick)
  → Pattern: map.put(0,1) → curSum += val → check (curSum-target) in map
             → add to map → recurse → backtrack (decrement map)

Subtree computation (bottom-up)?
  → Post-order DFS
  → Pattern: recurse left, recurse right → combine at current node

Identify or compare subtrees by structure?
  → Post-order DFS + serialize "val,left,right" + HashMap
  → Pattern: serialize(left) + serialize(right) → build key "val,L,R"
             → map.getOrDefault(key,0) == 1 → duplicate! → add to result
             → map.put(key, count+1) → return key to parent

BST / sorted property?
  → In-order DFS
  → Pattern: recurse left → process node → recurse right
```

## 總結

| 題目要你求… | 掏這個出來 | 章節 |
|---|---|---|
| 兩個節點的交會點 | 後序 LCA | 1) |
| 含最深節點的子樹 | 後序回傳 `(depth, node)` | 1) |
| **任意**方向距離 `k` 步的節點 | 父節點表＋BFS | 2) |
| 任意兩節點之間的邊數 | LCA ＋ 兩次 `get_dist` | 3) |
| 所有符合某規則的根到葉路徑 | 前序 DFS＋回溯 | 4) |
| 任意到任意向下路徑的數量 | 前序 DFS＋前綴和 HashMap | 4) |
| 通過任一節點的最長路徑 | 後序高度＋全域最大值 | 5-1) |

**在這裡代價最大的三個錯誤：**
- 「找不到」回傳 `0` 而不是 `-1` — `0` 是一個合法的距離
- 一旦有了父節點這種邊，就忘了 `visited` 集合 — BFS 會永遠繞不出來
- 從 `root` 而不是從 `lca` 呼叫 `get_dist` — 量到的是錯的路徑

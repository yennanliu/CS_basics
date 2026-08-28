# BST — LeetCode 實作範例

> **範圍** — `bst.md` 教的那些 BST 模板的解題檔案庫：每題每個語言一份標準解，外加雖然歸在 BST、卻完全用不到大小順序的「根到葉」與「節點到節點」路徑家族。
> **另見**：[bst.md](./bst.md) — 這些範例所套用的標準 BST 模板；[bst_advanced.md](./bst_advanced.md) — 順序統計查詢、延遲式迭代器、修復壞掉的 BST，以及各種建樹變形；[tree_backtrack.md](./tree_backtrack.md) — 回程要復原狀態的根→葉路徑。

## LeetCode 題目清單

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

## 總覽

六題 BST 實作題，它們的解法沒有被 [bst.md](./bst.md) 裡的模板直接寫死；接著是七種模式的路徑家族（Template 7）。凡是模板*用同一個語言*已經解掉的東西，這裡不重述——請回去看模板。

### 關鍵性質
- **複雜度**：底下每個解法都是 O(n) 時間，只有 LC 776 是 O(h)；空間是遞迴堆疊的 O(h)，
  除非程式碼註解另有說明
- **核心想法**：每題每個語言一份標準解；只有在上方註記說明「第一份沒教到什麼」時，
  才會出現第二個版本
- **什麼時候用**：等你能默寫出對應的模板之後

## LC 範例

### 1) Serialize and Deserialize BST — LC 449

兩種真的不一樣的編碼器：`V0` 是層序加 `#` 佔位符（適用任何二元樹，LC 297 的風格），
`V1` 是前序加上 `(min, max)` 界限，**完全不需要**佔位符——省下來的正是 BST 性質給你的紅利。

```python
# LC 449 Serialize and Deserialize BST
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

### 2) Split BST — LC 776

#### 模式：遞迴切分 BST
依一個目標值把 BST 切成兩棵合法的 BST。這是**切分**題，不是刪除題——所有節點都保留，只是重新分配到兩棵樹裡。

#### 原理：為什麼「切分」不等於「刪除」

| 操作 | 目標 | 會掉節點嗎？ | 回傳值 |
|-----------|------|-------------|--------------|
| **Delete**（LC 450） | 移掉一個節點，保留一棵樹 | 會（1 個節點） | 單一 `TreeNode` |
| **Split**（LC 776） | 分成兩棵樹 | 不會 | `TreeNode[2]` 陣列 |

#### 核心想法

```text
Return value: TreeNode[2]
  res[0] → BST with all values ≤ target
  res[1] → BST with all values > target
```

**依 root.val 與 target 的關係分成兩種情況：**

```text
Case 1: root.val <= target
  → root belongs to LEFT partition (res[0])
  → But root.right may contain nodes > target
  → So SPLIT root.right, reconnect the pieces

Case 2: root.val > target
  → root belongs to RIGHT partition (res[1])
  → But root.left may contain nodes <= target
  → So SPLIT root.left, reconnect the pieces
```

#### 逐步走一遍

```text
Input:        target = 2
         4
        / \
       2   6
      / \ / \
     1  3 5  7

Step 1: root=4, val=4 > target=2 → root goes to RIGHT partition
        Split root.left (the subtree rooted at 2)

Step 2: root=2, val=2 <= target=2 → root goes to LEFT partition
        Split root.right (the subtree rooted at 3)

Step 3: root=3, val=3 > target=2 → root goes to RIGHT partition
        Split root.left (null) → returns [null, null]
        root.left = null (from split[1])
        Return [null, 3]

Back to Step 2: split of node 2's right returned [null, 3]
        node 2's right = split[0] = null  (was 3, now detached)
        Return [2→(left:1, right:null), 3]

Back to Step 1: split of node 4's left returned [2, 3]
        node 4's left = split[1] = 3  (reconnect!)
        Return [2, 4→(left:3, right:6)]

Result:
  res[0] (≤ 2):     res[1] (> 2):
       2                  4
      /                  / \
     1                  3   6
                           / \
                          5   7
```

#### 關鍵洞見：重新接線

```text
When root.val <= target and we split root.right:
  split[0] = nodes from right subtree that are still <= target
  split[1] = nodes from right subtree that are > target

  root.right = split[0]   ← keep the small ones attached to root
  return [root, split[1]] ← root is left partition, split[1] is right partition

When root.val > target and we split root.left:
  split[0] = nodes from left subtree that are <= target
  split[1] = nodes from left subtree that are > target

  root.left = split[1]    ← keep the big ones attached to root
  return [split[0], root] ← split[0] is left partition, root is right partition
```

#### Python 實作
```python
# LC 776 Split BST
class Solution(object):
    def splitBST(self, root, V):
        if not root:
            return None, None
        # root belongs to LEFT partition
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]  # keep <= V part
            return root, result[1]  # [smallTree, bigTree]
        # root belongs to RIGHT partition
        else:
            result = self.splitBST(root.left, V)
            root.left = result[1]   # keep > V part
            return result[0], root  # [smallTree, bigTree]
```

#### Java 實作
```java
// LC 776 Split BST
// Time: O(H), Space: O(H) where H = height of BST
public TreeNode[] splitBST(TreeNode root, int target) {
    if (root == null) {
        return new TreeNode[]{null, null};
    }

    if (root.val <= target) {
        // root goes to LEFT partition (<=target)
        TreeNode[] split = splitBST(root.right, target);
        root.right = split[0];          // reconnect small part
        return new TreeNode[]{root, split[1]};
    } else {
        // root goes to RIGHT partition (>target)
        TreeNode[] split = splitBST(root.left, target);
        root.left = split[1];           // reconnect big part
        return new TreeNode[]{split[0], root};
    }
}
```

#### 相似題

| 題目 | LC # | 相似處 | 關鍵差異 |
|---------|------|-----------|----------------|
| **Split BST** | 776 | 核心模式 | 依值切分成 2 棵樹 |
| **Delete Node in BST** | 450 | 都會改動 BST 結構 | 刪除拿掉 1 個節點；切分全部保留 |
| **Trim a BST** | 669 | 都會移除範圍外的節點 | 修剪會丟掉節點；切分把全部留在 2 棵樹裡 |
| **Search in BST** | 700 | 同樣的左右分支邏輯 | 搜尋回傳 1 個節點；切分回傳 2 棵樹 |
| **Insert into BST** | 701 | 同樣的遞迴 BST 走訪 | 插入是新增；切分是分割 |
| **Merge Two BSTs** | - | 反向操作 | 切分 → 2 棵樹；合併 → 1 棵樹 |

#### 複雜度
- **時間**：O(H) — 只走過一條根到葉路徑上的節點
- **空間**：O(H) — 遞迴堆疊深度 = 樹高

### 3) Binary Search Tree Iterator — LC 173

**提早（eager）**版本：在建構子裡把整棵樹攤平，之後從一個 list 供貨。
留著它是因為它是延遲式 O(h) 堆疊版的比較基準——延遲版的寫法以及
eager vs lazy 的對照表，看 [bst_advanced.md](./bst_advanced.md) 的 `Template 5b`。

```python
# LC 173. Binary Search Tree Iterator
# IDEA : STACK + tree
class BSTIterator(object):
    def __init__(self, root):
        """
        :type root: TreeNode
        """
        self.stack = []
        self.inOrder(root)
    
    def inOrder(self, root):
        if not root:
            return
        """
        NOTE !!! how we do inorder traversal here

        irOrder : left -> root -> right
        """
        self.inOrder(root.left)
        self.stack.append(root.val)
        self.inOrder(root.right)
    
    def hasNext(self):
        """
        :rtype: bool
        """
        return len(self.stack) > 0

    def next(self):
        """
        :rtype: int
        """
        return self.stack.pop(0)  # NOTE here
```

### 4) Validate Binary Search Tree — LC 98

**BFS** 版本：把 `(node, min, max)` 帶在佇列裡，而不是帶在遞迴堆疊上。
留著它是因為 [bst.md](./bst.md) 的 `Template 4` 是 DFS 版——這一版是拿來回答
「可以用迭代／不用遞迴寫嗎？」的。

```python
# LC 98 Validate Binary Search Tree
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
                    """
                    ### NOTE : below condition
                    ### NOTE : we compare "tmp.left.val" with others (BEFORE we visit tmp.left)
                    """
                    if tmp.left.val >= tmp.val or tmp.left.val <= _min:
                        return False
                    ### NOTE : we append tmp.val as _max
                    q.append([tmp.left, _min, tmp.val])
                if tmp.right:
                    """
                    ### NOTE : below condition
                    ### NOTE : we compare "tmp.right.val" with others (BEFORE we visit tmp.right)
                    """
                    if tmp.right.val <= tmp.val or tmp.right.val >= _max:
                        return False
                    ### NOTE : we append tmp.val as _min
                    q.append([tmp.right, tmp.val, _max])
        return True
```

### 5) Convert BST to Greater Tree — LC 538
用**反向中序**（右 → 根 → 左）搭配一個累計和。實作看 `bst.md` 的
[Reverse Inorder for Descending](./bst.md#pattern-reverse-inorder-for-descending) 模式。

### 6) Binary Search Tree to Greater Sum Tree — LC 1038
和 LC 538 一模一樣——反向中序走訪累加一個累計和。
看 `bst.md` 的 [Reverse Inorder for Descending](./bst.md#pattern-reverse-inorder-for-descending) 模式。

## 根到葉、節點到節點的路徑

放在這裡而不是 [bst.md](./bst.md)，是因為**這些題目都沒有用到 BST 的大小順序**——
它們在任何二元樹上都成立。把它們收成一個模板，是因為它們共用同一副 DFS 骨架，
差別只在往下帶什麼狀態、往上回傳什麼。

### Template 7: 路徑類問題

#### **模式總覽**
- **描述**：在二元樹中尋找或驗證路徑（根到葉、節點到節點）
- **辨識訊號**：「Path sum」、「root to leaf」、「maximum path」、「consecutive」
- **關鍵觀念**：用 DFS 搭配路徑追蹤、累加，或全域狀態
- **時間複雜度**：走訪所有節點是 O(n)
- **空間複雜度**：遞迴堆疊 + 路徑儲存為 O(h)

**📚 相關模式**：這些路徑題都用 DFS 走訪。一般性的樹上找路徑模式與技巧，看 **dfs.md Template 3 (Path Finding)**。這裡的範例聚焦在 BST 與一般二元樹都適用的常見路徑題。

#### **核心路徑模式**

##### **模式 7.1：單純的路徑和**（LC 112）
```python
def has_path_sum(root, target_sum):
    """
    Check if root-to-leaf path exists with given sum
    Time: O(n), Space: O(h)
    """
    if not root:
        return False

    # Leaf node check
    if not root.left and not root.right:
        return root.val == target_sum

    # Recurse with reduced sum
    remaining = target_sum - root.val
    return (has_path_sum(root.left, remaining) or
            has_path_sum(root.right, remaining))
```

##### **模式 7.2：Path Sum II — 列出所有路徑**（LC 113）
```python
def path_sum(root, target_sum):
    """
    Find all root-to-leaf paths with given sum
    Uses backtracking to track current path
    Time: O(n), Space: O(h)
    """
    result = []

    def dfs(node, remaining, path):
        if not node:
            return

        # Add current node to path
        path.append(node.val)

        # Check if leaf with target sum
        if not node.left and not node.right and remaining == node.val:
            result.append(path[:])  # Deep copy

        # Recurse on children
        new_remaining = remaining - node.val
        dfs(node.left, new_remaining, path)
        dfs(node.right, new_remaining, path)

        # Backtrack
        path.pop()

    dfs(root, target_sum, [])
    return result
```

##### **模式 7.3：Binary Tree Paths**（LC 257）
```python
def binary_tree_paths(root):
    """
    Find all root-to-leaf paths as strings
    Time: O(n), Space: O(h)
    """
    if not root:
        return []

    result = []

    def dfs(node, path):
        if not node:
            return

        # Add current node to path string
        path += str(node.val)

        # Leaf node - add complete path
        if not node.left and not node.right:
            result.append(path)
            return

        # Continue path with arrow
        path += "->"
        dfs(node.left, path)
        dfs(node.right, path)

    dfs(root, "")
    return result
```

##### **模式 7.4：Sum Root to Leaf Numbers**（LC 129）
```python
def sum_numbers(root):
    """
    Sum all numbers formed by root-to-leaf paths
    Example: 1->2->3 represents 123
    Time: O(n), Space: O(h)
    """
    def dfs(node, current_sum):
        if not node:
            return 0

        # Build number: current_sum * 10 + node.val
        current_sum = current_sum * 10 + node.val

        # Leaf node - return the number
        if not node.left and not node.right:
            return current_sum

        # Sum from both subtrees
        return dfs(node.left, current_sum) + dfs(node.right, current_sum)

    return dfs(root, 0)
```

##### **模式 7.5：Binary Tree Maximum Path Sum**（LC 124）
```python
def max_path_sum(root):
    """
    Find maximum path sum (any node to any node)
    Uses global variable to track maximum
    Time: O(n), Space: O(h)
    """
    max_sum = float('-inf')

    def dfs(node):
        nonlocal max_sum

        if not node:
            return 0

        # Get max contribution from left and right
        # Use max(0, ...) to ignore negative paths
        left_max = max(0, dfs(node.left))
        right_max = max(0, dfs(node.right))

        # Update global max with path through current node
        path_sum = node.val + left_max + right_max
        max_sum = max(max_sum, path_sum)

        # Return max path going through this node (one side only)
        return node.val + max(left_max, right_max)

    dfs(root)
    return max_sum
```

##### **模式 7.6：Binary Tree Longest Consecutive Sequence**（LC 298） ⭐⭐⭐⭐

###### **核心想法**

- **題目**：找出最長的路徑，其上每一步的值都剛好 `+1`。
- **方向是固定的**：路徑必須從**父 → 子**。`3-2-1` 不算，只有 `1-2-3` 算。
- **路徑是一條「鏈」，不是「V 字」**：和 LC 124（Max Path Sum）不同，你**絕不能**在某個節點把左右兩支接起來。每個答案都是一條由上往下的單鏈。
- **往下走時可以換邊**：`root.left` 接著 `.right` 再接 `.left` 完全沒問題——「連續」限制的是*值*，不是你走哪個子節點指標。

```text
    1
     \
      2      <-- valid input; path 1 -> 2 -> 3 -> 4 has length 4
     /
    3
     \
      4
```

- **核心想法**：某個節點的連續長度**只取決於它的父節點**，所以把 `(parent_val, current_len)` **往下**帶進遞迴（由上而下的 DFS）。每個節點要嘛**延續**父節點的連續段（`node.val == parent.val + 1`），要嘛**重新開一段**（`len = 1`）。
- **答案藏在哪**：沒有任何單一節點的回傳值就是答案——用一個**全域最大值**追蹤，每個節點都更新。

###### **模式**

**模板 A — 由上而下（把連續長度往下帶） ⭐ 首選**

```python
# python
# LC 298 - Binary Tree Longest Consecutive Sequence
# IDEA: TOP-DOWN DFS, pass (parent_val, current_len) downward
# time = O(n), each node visited once
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def longestConsecutive(self, root):
        if not root:
            return 0

        self.max_len = 0

        def dfs(node, parent_val, cur_len):
            if not node:
                return

            # extend the streak, or restart it at this node
            if node.val == parent_val + 1:
                cur_len += 1
            else:
                cur_len = 1

            self.max_len = max(self.max_len, cur_len)

            # NOTE: pass node.val down as the NEW parent value
            dfs(node.left, node.val, cur_len)
            dfs(node.right, node.val, cur_len)

        # trick: seed with (root.val - 1, 0) so root always counts as length 1
        dfs(root, root.val - 1, 0)
        return self.max_len
```

```java
// java
// LC 298 - Binary Tree Longest Consecutive Sequence
// IDEA: TOP-DOWN DFS
// time = O(n), space = O(h)
class Solution {
    private int maxLen = 0;

    public int longestConsecutive(TreeNode root) {
        if (root == null) return 0;
        dfs(root, root.val - 1, 0);
        return maxLen;
    }

    private void dfs(TreeNode node, int parentVal, int curLen) {
        if (node == null) return;
        curLen = (node.val == parentVal + 1) ? curLen + 1 : 1;
        maxLen = Math.max(maxLen, curLen);
        dfs(node.left, node.val, curLen);
        dfs(node.right, node.val, curLen);
    }
}
```

**模板 B — 由下而上（回傳「從這個節點開始」的連續長度）**

```python
# python
# IDEA: POST-ORDER DFS, return "longest consecutive path STARTING at this node"
# time = O(n), space = O(h)
class Solution(object):
    def longestConsecutive(self, root):
        self.max_len = 0

        def helper(node):
            if not node:
                return 0

            left_len = helper(node.left)
            right_len = helper(node.right)

            cur_len = 1
            # NOTE: only take the child's length if the child continues the streak
            if node.left and node.left.val == node.val + 1:
                cur_len = max(cur_len, left_len + 1)
            if node.right and node.right.val == node.val + 1:
                cur_len = max(cur_len, right_len + 1)

            self.max_len = max(self.max_len, cur_len)
            return cur_len          # <-- ONE side only, never left + right

        helper(root)
        return self.max_len
```

**模板 C — 迭代式 DFS（堆疊存 `(node, len)`）**

```python
# python
# IDEA: DFS with explicit stack; the streak length travels WITH the node
# time = O(n), space = O(n)
class Solution(object):
    def longestConsecutive(self, root):
        if not root:
            return 0

        stack = [(root, 1)]
        max_len = 1
        while stack:
            node, path_len = stack.pop()
            for child in (node.left, node.right):
                if child:
                    new_len = path_len + 1 if child.val == node.val + 1 else 1
                    max_len = max(max_len, new_len)
                    stack.append((child, new_len))

        return max_len
```

###### **視覺化追蹤** — 模板 A 跑範例樹

```text
   1
    \
     3
    / \
   2   4
        \
         5

dfs(1, parent=0, len=0)   -> 1 == 0+1  -> len=1   max=1
  dfs(3, parent=1, len=1) -> 3 != 1+1  -> len=1   max=1   (streak breaks)
    dfs(2, parent=3, len=1) -> 2 != 4  -> len=1   max=1
    dfs(4, parent=3, len=1) -> 4 == 3+1-> len=2   max=2
      dfs(5, parent=4, len=2) -> 5 == 5-> len=3   max=3   <-- answer
```

###### **🚫 為什麼 `path = "{}-{}-{}".format(root.val, _left, _right)` 這招行不通**

一個很誘人的想法是：把**每棵子樹序列化成字串**（像 LC 297 / LC 652 那樣），把所有字串收進 map，再把每個字串解析回來量它的連續長度：

```python
# 🚫 WRONG
def helper(self, root):
    if not root:
        return "#"
    _left  = self.helper(root.left)
    _right = self.helper(root.right)
    path = "{}-{}-{}".format(root.val, _left, _right)   # <-- the bug
    self.p_map[path] = 1
    return path
```

**1. 序列化出來的是*一棵樹*，不是*一條路徑*（致命傷）**

`"{val}-{left}-{right}"` 把**兩邊**子樹都塞進同一個扁平字串。但連續序列是**一條根→後代的單鏈**——它永遠只能包含兩個子節點的其中一個。攤平會把兩條*沒有*任何父子邊相連的兄弟分支併在一起，於是 `split("-")` 產生出來的相鄰元素，在樹上根本從來不相鄰。

```text
   3
  / \
 9   4
      \
       5

serialize(3) = "3-9-#-#-4-#-5-#-#"
split         = [3, 9, #, #, 4, #, 5, #, #]
                       ^^^^^^^^
   `9` and `4` are SIBLINGS — no edge between them.
   `4` and `5` are a real edge but they are separated by `#`s.
   The linear scan `_list[i] == _list[i-1] + 1` is scanning a
   pre-order dump, NOT a path. It cannot recover the answer.
```

**2. 它同時也弄丟了方向與起點**

就算你把 `#` 濾掉，前序 dump 也沒辦法告訴你「`x` 到底是 `y` 的*父節點*，還是它的叔叔？」。`+1` 檢查需要的是**父子邊**，而那正是字串丟掉的資訊。遞迴本來就免費握有那條邊（往 `node.left/right` 遞迴時手上就有 `node` 和 `node.val`）——序列化把它丟掉，然後再想辦法重建。

**3. 目標與資料結構不匹配**

子樹序列化存在的目的是回答**「兩棵子樹一樣嗎？」**（LC 652 Find Duplicate Subtrees、LC 297 Serialize/Deserialize）。LC 298 問的是**一條往下的鏈**，所以自然的狀態是一個純量（`cur_len`），不是字串。

**4. 掩蓋真正問題的那些 bug**

| 行 | Bug | 後果 |
|------|-----|--------|
| `self.p_map().keys()` | dict 不能被呼叫 | `TypeError` |
| `len = 0` | 蓋掉內建的 `len()` | 下一次 `len(_list)` 就 `TypeError: 'int' object is not callable` |
| `p_map[path] = 1` | 少了 `self.` | `NameError` |
| `range(1, len(_list) - 1)` | 差一錯誤，漏掉最後一個元素 | 就算跑得起來，數字也是錯的 |
| `return len(_list) - 1` | 無緣無故減一 | 數字錯 |

**5. 代價**

每個節點的字串長度是 O(它子樹大小)，所以全部建出來是**最壞 O(n²) 時間、O(n²) 空間**，而單純的 DFS 只要 **O(n) / O(h)**。

> **重點**：當你要的量是**沿著一條根→節點的單鏈**定義出來的，就把它當成**參數往下帶進遞迴**。只有在需要拿*整棵子樹*互相比較時，才動用序列化。

###### **相似的 LeetCode 題目**

| LC # | 題目 | 與 LC 298 的關係 | 關鍵差異 |
|------|---------|--------------------|----------------|
| **549** | Binary Tree Longest Consecutive Sequence II | 直接的續集 ⭐ | 路徑可以走**子 → 父 → 子**（一個「V」），而且可以遞增**或**遞減 → 每個節點回傳 `inc + dec - 1` |
| **124** | Binary Tree Maximum Path Sum | 同樣的「全域最大值 + 只回傳一邊」骨架 | 路徑可以在節點處轉彎；用的是總和而不是 `+1` 步進 |
| **687** | Longest Univalue Path | 同一副骨架 | 要求值**相等**而不是 `+1`；數的是**邊**不是節點 |
| **543** | Diameter of Binary Tree | 同一副骨架 | 對值完全沒有限制；純粹數邊 |
| **1372** | Longest ZigZag Path in a Binary Tree | 由上而下帶狀態 | 狀態是 `(direction, length)` 而不是 `(parent_val, length)` |
| **129** | Sum Root to Leaf Numbers | 由上而下帶狀態 | 往下帶一個累積出來的數字；必須走到葉節點 |
| **112 / 113** | Path Sum I / II | 由上而下帶狀態 | 往下帶剩餘的和；只算根→**葉** |
| **128** | Longest Consecutive Sequence（陣列版） | 同樣的「連續」概念 | 未排序陣列 + 雜湊集合，沒有樹，與順序無關 |
| **652** | Find Duplicate Subtrees | 序列化*真正*該用的地方 | 比較的是整棵子樹 → `"{val}-{left}-{right}"` 在這裡是*正確的* |

###### **重點整理**

1. **狀態往下帶，不要往上傳** — `cur_len` 只取決於父節點，所以它該待在參數列裡。
2. **要寫 `else: cur_len = 1`，絕不是 `return`** — 連續段斷掉時是從當前節點重新開始，不是結束走訪。
3. **只回傳一邊**（模板 B） — `left + right` 會造出一條轉彎的路徑，而 LC 298 不允許（那是 LC 549）。
4. **用 `root.val - 1` 當種子**，這樣根節點自然算成長度 1 的連續段，不用寫特例。
5. **不要序列化** — 攤平的子樹字串沒辦法表達一條往下的單一路徑（見上）。

##### **模式 7.7：Path Sum III**（LC 437）
```python
def path_sum_3(root, target_sum):
    """
    Count paths with given sum (not necessarily root-to-leaf)
    Uses prefix sum technique with hash map
    Time: O(n), Space: O(n)
    """
    from collections import defaultdict

    def dfs(node, current_sum, prefix_sums):
        if not node:
            return 0

        # Update current sum
        current_sum += node.val

        # Count paths ending at current node
        # If (current_sum - target_sum) exists, we found path(s)
        count = prefix_sums[current_sum - target_sum]

        # Add current sum to prefix map
        prefix_sums[current_sum] += 1

        # Recurse on children
        count += dfs(node.left, current_sum, prefix_sums)
        count += dfs(node.right, current_sum, prefix_sums)

        # Backtrack: remove current sum from map
        prefix_sums[current_sum] -= 1

        return count

    # Initialize with 0 sum (for paths starting from root)
    prefix_sums = defaultdict(int)
    prefix_sums[0] = 1

    return dfs(root, 0, prefix_sums)
```

#### **Java 實作**
```java
// Pattern 7.1: Simple Path Sum (LC 112)
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) return false;

    // Leaf node check
    if (root.left == null && root.right == null) {
        return root.val == targetSum;
    }

    int remaining = targetSum - root.val;
    return hasPathSum(root.left, remaining) ||
           hasPathSum(root.right, remaining);
}

// Pattern 7.2: Path Sum II (LC 113)
public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
    List<List<Integer>> result = new ArrayList<>();
    dfs(root, targetSum, new ArrayList<>(), result);
    return result;
}

private void dfs(TreeNode node, int remaining,
                 List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;

    path.add(node.val);

    if (node.left == null && node.right == null && remaining == node.val) {
        result.add(new ArrayList<>(path));
    }

    int newRemaining = remaining - node.val;
    dfs(node.left, newRemaining, path, result);
    dfs(node.right, newRemaining, path, result);

    path.remove(path.size() - 1);  // Backtrack
}

// Pattern 7.5: Maximum Path Sum (LC 124)
private int maxSum = Integer.MIN_VALUE;

public int maxPathSum(TreeNode root) {
    dfs(root);
    return maxSum;
}

private int dfs(TreeNode node) {
    if (node == null) return 0;

    int leftMax = Math.max(0, dfs(node.left));
    int rightMax = Math.max(0, dfs(node.right));

    maxSum = Math.max(maxSum, node.val + leftMax + rightMax);

    return node.val + Math.max(leftMax, rightMax);
}

// Pattern 7.7: Path Sum III (LC 437)
public int pathSum(TreeNode root, int targetSum) {
    Map<Long, Integer> prefixSums = new HashMap<>();
    prefixSums.put(0L, 1);
    return dfs(root, 0L, targetSum, prefixSums);
}

private int dfs(TreeNode node, long currentSum, int target,
                Map<Long, Integer> prefixSums) {
    if (node == null) return 0;

    currentSum += node.val;
    int count = prefixSums.getOrDefault(currentSum - target, 0);

    prefixSums.put(currentSum, prefixSums.getOrDefault(currentSum, 0) + 1);

    count += dfs(node.left, currentSum, target, prefixSums);
    count += dfs(node.right, currentSum, target, prefixSums);

    prefixSums.put(currentSum, prefixSums.get(currentSum) - 1);

    return count;
}
```

#### **路徑模式總表**
| 題型 | 做法 | 關鍵技巧 | 時間 | 空間 | LC # |
|--------------|----------|---------------|------|-------|------|
| **單純路徑和** | DFS 遞迴 | 遞減目標和 | O(n) | O(h) | 112 |
| **所有路徑** | DFS + 回溯 | 追蹤路徑 | O(n) | O(h) | 113 |
| **路徑字串** | DFS + 字串 | 串接 | O(n) | O(h) | 257 |
| **數字加總** | DFS + 累加 | 組出數字 | O(n) | O(h) | 129 |
| **最大路徑和** | DFS + 全域 | 追蹤最大值 | O(n) | O(h) | 124 |
| **連續序列** | DFS + 計數器 | 追蹤長度 | O(n) | O(h) | 298 |
| **前綴和** | DFS + hashmap | 前綴和技巧 | O(n) | O(n) | 437 |

#### **關鍵觀念與原則**

1. **根到葉的路徑**
   - 一定要檢查葉節點：`not node.left and not node.right`
   - 每一層都把目標和扣掉
   - 在葉節點回傳結果

2. **回溯模式**
   - 把當前節點加進路徑
   - 對子節點遞迴
   - 把當前節點從路徑移除（復原狀態）
   - 要列出所有路徑時必備

3. **全域狀態**
   - 用 nonlocal／類別變數存最大值
   - 走訪過程中更新
   - 回傳的是「貢獻值」，不是最終答案

4. **穿過節點的路徑**
   - 求最大路徑時：left_max + node.val + right_max
   - 回傳時：node.val + max(left_max, right_max)
   - 用 max(0, ...) 把負的貢獻忽略掉

5. **前綴和技巧**
   - 追蹤從根累積下來的總和
   - 用 hashmap：prefixSum[currentSum - target] = count
   - 回溯時把計數減回去

#### **常見錯誤與陷阱**

**🚫 錯誤 1：沒有檢查葉節點**
```python
# BAD: Doesn't verify it's a leaf
if root.val == target:
    return True

# GOOD: Check both children are None
if not root.left and not root.right and root.val == target:
    return True
```

**🚫 錯誤 2：忘記回溯**
```python
# BAD: Path grows indefinitely
def dfs(node, path):
    path.append(node.val)
    dfs(node.left, path)

# GOOD: Remove after recursion
def dfs(node, path):
    path.append(node.val)
    dfs(node.left, path)
    path.pop()
```

**🚫 錯誤 3：結果裡放的是淺拷貝**
```python
# BAD: All results reference same list
result.append(path)

# GOOD: Create deep copy
result.append(path[:])  # or list(path)
```

**🚫 錯誤 4：最大路徑的邏輯寫錯**
```python
# BAD: Includes both subtrees in return
def dfs(node):
    left = dfs(node.left)
    right = dfs(node.right)
    return node.val + left + right  # Wrong!

# GOOD: Return one path only
return node.val + max(left, right)
```

**🚫 錯誤 5：沒有處理負值**
```python
# BAD: Negative paths reduce maximum
left_max = dfs(node.left)

# GOOD: Ignore negative contributions
left_max = max(0, dfs(node.left))
```

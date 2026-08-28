# 樹的模式模板 — 完整指南

> **範圍** — 每個樹的模式配一份編號好、可直接複製貼上的**模板**，Python *與* Java 都有 — 樹模板的唯一集散地。模板優先，不談理論：某一題該用哪種走訪，是 [tree.md](./tree.md) 要回答的問題。
> **另見**：[tree.md](./tree.md) — 概念、樹的種類、什麼時候用哪種走訪；[tree_lca_distance.md](./tree_lca_distance.md) — LCA、節點距離與根到葉的路徑，這份表單完全交給它；[tree_construction.md](./tree_construction.md) 與 [tree_codec.md](./tree_codec.md) — 從編碼建樹，以及把樹序列化回去；[binary_tree.md](./binary_tree.md) — DFS 的狀態怎麼在二元樹裡流動；[bst.md](./bst.md) — 有序的樹。

> **注意：** 這份檔案收錄的是詳細的走訪模板與實作程式碼。樹的概念、種類與演算法模式，請看 [tree.md](./tree.md)。

## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

## 概觀

這份文件提供所有樹題型模式的詳細模板，依分類整理，附上範例程式碼、說明，以及對應的 LeetCode 題目。

---

## 1) 樹走訪模板

### 1.1) 前序模板 — LC 144

**模式**：根 → 左 → 右
**適用情境**：處理子節點之前需要先拿到父節點的資料
**時間複雜度**：O(n)
**空間複雜度**：O(h)，遞迴堆疊

#### 模板程式碼

```python
# Python - Recursive
def preorder_traversal(root):
    result = []

    def preorder(node):
        if not node:
            return

        # Process root first
        result.append(node.val)

        # Then left subtree
        preorder(node.left)

        # Then right subtree
        preorder(node.right)

    preorder(root)
    return result

# Python - Iterative
def preorder_iterative(root):
    if not root:
        return []

    result = []
    stack = [root]

    while stack:
        node = stack.pop()
        result.append(node.val)

        # Add right first (LIFO - will process left first)
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)

    return result
```

```java
// Java - Recursive
public void preorderTraversal(TreeNode root, List<Integer> result) {
    if (root == null) return;

    result.add(root.val);              // Process root
    preorderTraversal(root.left, result);   // Left subtree
    preorderTraversal(root.right, result);  // Right subtree
}

// Java - Iterative
public List<Integer> preorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;

    Stack<TreeNode> stack = new Stack<>();
    stack.push(root);

    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        result.add(node.val);

        if (node.right != null) stack.push(node.right);
        if (node.left != null) stack.push(node.left);
    }

    return result;
}
```

#### LeetCode 題目
- LC 144: Binary Tree Preorder Traversal (Easy)
- LC 589: N-ary Tree Preorder Traversal (Easy)

---

### 1.2) 中序模板 — LC 94 ⭐⭐⭐⭐⭐

**模式**：左 → 根 → 右
**適用情境**：BST 的排序順序、樹的驗證
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Recursive
def inorder_traversal(root):
    result = []

    def inorder(node):
        if not node:
            return

        # Left subtree first
        inorder(node.left)

        # Process root
        result.append(node.val)

        # Right subtree
        inorder(node.right)

    inorder(root)
    return result

# Python - Iterative
def inorder_iterative(root):
    result = []
    stack = []
    current = root

    while stack or current:
        # Go to leftmost node
        while current:
            stack.append(current)
            current = current.left

        # Process current node
        current = stack.pop()
        result.append(current.val)

        # Move to right subtree
        current = current.right

    return result
```

```java
// Java - Recursive
public void inorderTraversal(TreeNode root, List<Integer> result) {
    if (root == null) return;

    inorderTraversal(root.left, result);    // Left subtree
    result.add(root.val);                   // Current node
    inorderTraversal(root.right, result);   // Right subtree
}

// Java - Iterative
public List<Integer> inorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    Stack<TreeNode> stack = new Stack<>();
    TreeNode current = root;

    while (!stack.isEmpty() || current != null) {
        while (current != null) {
            stack.push(current);
            current = current.left;
        }

        current = stack.pop();
        result.add(current.val);
        current = current.right;
    }

    return result;
}
```

#### LeetCode 題目
- LC 94: Binary Tree Inorder Traversal (Easy)
- LC 98: Validate Binary Search Tree (Medium)
- LC 230: Kth Smallest Element in a BST (Medium)

---

### 1.3) 後序模板 — LC 145

**模式**：左 → 右 → 根
**適用情境**：處理父節點之前需要先拿到子節點的資料
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Recursive
def postorder_traversal(root):
    result = []

    def postorder(node):
        if not node:
            return

        # Left subtree first
        postorder(node.left)

        # Right subtree
        postorder(node.right)

        # Process root last
        result.append(node.val)

    postorder(root)
    return result

# Python - Iterative (Two Stacks)
def postorder_iterative(root):
    if not root:
        return []

    stack1 = [root]
    stack2 = []

    # Collect nodes in reverse postorder
    while stack1:
        node = stack1.pop()
        stack2.append(node)

        if node.left:
            stack1.append(node.left)
        if node.right:
            stack1.append(node.right)

    # Pop from stack2 to get postorder
    result = []
    while stack2:
        result.append(stack2.pop().val)

    return result
```

```java
// Java - Recursive
public void postorderTraversal(TreeNode root, List<Integer> result) {
    if (root == null) return;

    postorderTraversal(root.left, result);   // Left subtree
    postorderTraversal(root.right, result);  // Right subtree
    result.add(root.val);                    // Current node
}
```

#### LeetCode 題目
- LC 145: Binary Tree Postorder Traversal (Easy)
- LC 590: N-ary Tree Postorder Traversal (Easy)

---

### 1.4) BFS 模板（層序） — LC 102 ⭐⭐⭐⭐⭐

**模式**：一層一層處理節點
**適用情境**：最短路徑、以層為單位的問題
**時間複雜度**：O(n)
**空間複雜度**：O(w)，w 是最大寬度

#### 模板程式碼

```python
# Python - BFS with Level Grouping
from collections import deque

def level_order_traversal(root):
    if not root:
        return []

    result = []
    queue = deque([root])

    while queue:
        level_size = len(queue)
        current_level = []

        for _ in range(level_size):
            node = queue.popleft()
            current_level.append(node.val)

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

        result.append(current_level)

    return result

# Python - Simple BFS (Flat List)
def level_order_simple(root):
    if not root:
        return []

    result = []
    queue = deque([root])

    while queue:
        node = queue.popleft()
        result.append(node.val)

        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right)

    return result
```

```java
// Java - BFS with Level Grouping
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        List<Integer> currentLevel = new ArrayList<>();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            currentLevel.add(node.val);

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

        result.add(currentLevel);
    }

    return result;
}
```

#### LeetCode 題目
- LC 102: Binary Tree Level Order Traversal (Medium)
- LC 107: Binary Tree Level Order Traversal II (Medium)
- LC 103: Binary Tree Zigzag Level Order Traversal (Medium)
- LC 199: Binary Tree Right Side View (Medium)
- LC 637: Average of Levels in Binary Tree (Easy) — 同一個迴圈，只是把每層收集起來改成做彙總

---

### 1.5) BFS + 方向模板 — LC 103

**模式**：每一層交替方向
**適用情境**：鋸齒狀走訪
**時間複雜度**：O(n)
**空間複雜度**：O(w)

#### 模板程式碼

```python
# Python - Zigzag Level Order
from collections import deque

def zigzag_level_order(root):
    if not root:
        return []

    result = []
    queue = deque([root])
    left_to_right = True

    while queue:
        level_size = len(queue)
        current_level = deque()

        for _ in range(level_size):
            node = queue.popleft()

            # Add to level based on direction
            if left_to_right:
                current_level.append(node.val)
            else:
                current_level.appendleft(node.val)

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

        result.append(list(current_level))
        left_to_right = not left_to_right

    return result
```

```java
// Java - Zigzag Level Order
public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    boolean leftToRight = true;

    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        LinkedList<Integer> currentLevel = new LinkedList<>();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();

            if (leftToRight) {
                currentLevel.addLast(node.val);
            } else {
                currentLevel.addFirst(node.val);
            }

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

        result.add(currentLevel);
        leftToRight = !leftToRight;
    }

    return result;
}
```

#### LeetCode 題目
- LC 103: Binary Tree Zigzag Level Order Traversal (Medium)

---

## 2) 樹的性質模板

### 2.1) 後序求高度模板 — LC 104

**模式**：由下而上算高度
**適用情境**：計算樹的高度／深度
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Height Calculation
def max_depth(root):
    if not root:
        return 0

    left_height = max_depth(root.left)
    right_height = max_depth(root.right)

    return 1 + max(left_height, right_height)
```

```java
// Java - Height Calculation
public int maxDepth(TreeNode root) {
    if (root == null) {
        return 0;
    }

    int leftHeight = maxDepth(root.left);
    int rightHeight = maxDepth(root.right);

    return 1 + Math.max(leftHeight, rightHeight);
}
```

#### LeetCode 題目
- LC 104: Maximum Depth of Binary Tree (Easy)

---

### 2.2) BFS 提早結束模板 — LC 111

**模式**：條件成立就停
**適用情境**：到葉節點的最小深度
**時間複雜度**：最壞 O(n)，實際上通常更好
**空間複雜度**：O(w)

#### 模板程式碼

```python
# Python - Minimum Depth
from collections import deque

def min_depth(root):
    if not root:
        return 0

    queue = deque([(root, 1)])

    while queue:
        node, depth = queue.popleft()

        # Found first leaf - return immediately
        if not node.left and not node.right:
            return depth

        if node.left:
            queue.append((node.left, depth + 1))
        if node.right:
            queue.append((node.right, depth + 1))

    return 0
```

```java
// Java - Minimum Depth
public int minDepth(TreeNode root) {
    if (root == null) return 0;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    int depth = 1;

    while (!queue.isEmpty()) {
        int levelSize = queue.size();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();

            if (node.left == null && node.right == null) {
                return depth;
            }

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

        depth++;
    }

    return depth;
}
```

#### LeetCode 題目
- LC 111: Minimum Depth of Binary Tree (Easy)

---

### 2.3) 高度驗證模板 — LC 110 ⭐⭐⭐

**模式**：算高度的同時順便驗證樹的性質
**適用情境**：判斷樹是否平衡
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Balanced Tree Check
def is_balanced(root):
    def check_height(node):
        if not node:
            return 0

        left_height = check_height(node.left)
        if left_height == -1:
            return -1

        right_height = check_height(node.right)
        if right_height == -1:
            return -1

        # Check balance condition
        if abs(left_height - right_height) > 1:
            return -1

        return 1 + max(left_height, right_height)

    return check_height(root) != -1
```

```java
// Java - Balanced Tree Check
public boolean isBalanced(TreeNode root) {
    return checkHeight(root) != -1;
}

private int checkHeight(TreeNode node) {
    if (node == null) {
        return 0;
    }

    int leftHeight = checkHeight(node.left);
    if (leftHeight == -1) return -1;

    int rightHeight = checkHeight(node.right);
    if (rightHeight == -1) return -1;

    if (Math.abs(leftHeight - rightHeight) > 1) {
        return -1;
    }

    return 1 + Math.max(leftHeight, rightHeight);
}
```

#### LeetCode 題目
- LC 110: Balanced Binary Tree (Easy)

---

### 2.4) 鏡像驗證模板 — LC 101

**模式**：比較對稱的兩棵子樹
**適用情境**：判斷樹是否對稱
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Symmetric Tree
def is_symmetric(root):
    def is_mirror(left, right):
        if not left and not right:
            return True
        if not left or not right:
            return False

        return (left.val == right.val and
                is_mirror(left.left, right.right) and
                is_mirror(left.right, right.left))

    if not root:
        return True
    return is_mirror(root.left, root.right)
```

```java
// Java - Symmetric Tree
public boolean isSymmetric(TreeNode root) {
    if (root == null) return true;
    return isMirror(root.left, root.right);
}

private boolean isMirror(TreeNode left, TreeNode right) {
    if (left == null && right == null) return true;
    if (left == null || right == null) return false;

    return left.val == right.val &&
           isMirror(left.left, right.right) &&
           isMirror(left.right, right.left);
}
```

#### LeetCode 題目
- LC 101: Symmetric Tree (Easy)

---

### 2.5) 樹比較模板 — LC 100

**模式**：逐節點比較兩棵樹
**適用情境**：判斷兩棵樹是否完全相同
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Same Tree
def is_same_tree(p, q):
    if not p and not q:
        return True
    if not p or not q:
        return False

    return (p.val == q.val and
            is_same_tree(p.left, q.left) and
            is_same_tree(p.right, q.right))
```

```java
// Java - Same Tree
public boolean isSameTree(TreeNode p, TreeNode q) {
    if (p == null && q == null) return true;
    if (p == null || q == null) return false;

    return p.val == q.val &&
           isSameTree(p.left, q.left) &&
           isSameTree(p.right, q.right);
}
```

#### LeetCode 題目
- LC 100: Same Tree (Easy)
- LC 572: Subtree of Another Tree (Easy)
- LC 951: Flip Equivalent Binary Trees (Medium) — **變形**：子節點可以互換，所以兩種配對都接受：`(l,l && r,r) || (l,r && r,l)`

---

### 2.6) 最小深度 — 遞迴寫法與它的獨子陷阱

> 上面的 `2.2)` 用 BFS 解 LC 111，那才是比較好的答案，因為它碰到第一個葉節點就停。
> 但遞迴寫法還是值得知道，因為經典錯誤就藏在這裡：對只有**一個**子節點的節點來說，
> `1 + min(left, right)` 是錯的 — 缺的那一側回傳 0，於是這個節點被當成葉節點。
> 下面那兩道守衛就是修正。

**模式**：找到葉節點的最小深度
**適用情境**：到葉節點的最短路徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Minimum Depth DFS
def min_depth(root):
    if not root:
        return 0

    # If one child is missing, only consider the other
    if not root.left:
        return 1 + min_depth(root.right)
    if not root.right:
        return 1 + min_depth(root.left)

    return 1 + min(min_depth(root.left), min_depth(root.right))
```

```java
// Java - Minimum Depth DFS
public int minDepth(TreeNode root) {
    if (root == null) return 0;

    if (root.left == null) {
        return 1 + minDepth(root.right);
    }
    if (root.right == null) {
        return 1 + minDepth(root.left);
    }

    return 1 + Math.min(minDepth(root.left), minDepth(root.right));
}
```

#### LeetCode 題目
- LC 111: Minimum Depth of Binary Tree (Easy)

---


### 2.7) 最大深度上最左邊的值 — LC 513

**模式**：找出最大深度那一層最左邊的節點
**適用情境**：樹的左下角值
**時間複雜度**：O(n)
**空間複雜度**：O(w)

#### 模板程式碼

```python
# Python - Find Bottom Left Tree Value
from collections import deque

def find_bottom_left_value(root):
    queue = deque([root])
    leftmost = root.val

    while queue:
        level_size = len(queue)

        for i in range(level_size):
            node = queue.popleft()

            # First node of level
            if i == 0:
                leftmost = node.val

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

    return leftmost
```

```java
// Java - Find Bottom Left Tree Value
public int findBottomLeftValue(TreeNode root) {
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    int leftmost = root.val;

    while (!queue.isEmpty()) {
        int levelSize = queue.size();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();

            if (i == 0) {
                leftmost = node.val;
            }

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
    }

    return leftmost;
}
```

#### LeetCode 題目
- LC 513: Find Bottom Left Tree Value (Medium)

---


## 3) 路徑類模板

### 3.1) 全域最大值更新模板 — LC 124 ⭐⭐⭐⭐

**模式**：走訪過程中追蹤全域最大值
**適用情境**：最大路徑和問題
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Binary Tree Maximum Path Sum
def max_path_sum(root):
    max_sum = float('-inf')

    def max_gain(node):
        nonlocal max_sum

        if not node:
            return 0

        # Max sum on left and right (ignore negative)
        left_gain = max(max_gain(node.left), 0)
        right_gain = max(max_gain(node.right), 0)

        # Update global max with path through current node
        current_path_sum = node.val + left_gain + right_gain
        max_sum = max(max_sum, current_path_sum)

        # Return max gain if continue from this node
        return node.val + max(left_gain, right_gain)

    max_gain(root)
    return max_sum
```

```java
// Java - Binary Tree Maximum Path Sum
private int maxSum = Integer.MIN_VALUE;

public int maxPathSum(TreeNode root) {
    maxGain(root);
    return maxSum;
}

private int maxGain(TreeNode node) {
    if (node == null) return 0;

    int leftGain = Math.max(maxGain(node.left), 0);
    int rightGain = Math.max(maxGain(node.right), 0);

    int currentPathSum = node.val + leftGain + rightGain;
    maxSum = Math.max(maxSum, currentPathSum);

    return node.val + Math.max(leftGain, rightGain);
}
```

#### LeetCode 題目
- LC 124: Binary Tree Maximum Path Sum (Hard)

---

### 3.2) 路徑累加模板 — LC 112

**模式**：沿路徑累計總和
**適用情境**：判斷有沒有和為某值的路徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Path Sum
def has_path_sum(root, target_sum):
    if not root:
        return False

    # Leaf node - check if sum matches
    if not root.left and not root.right:
        return root.val == target_sum

    # Recurse with updated target
    remaining = target_sum - root.val
    return (has_path_sum(root.left, remaining) or
            has_path_sum(root.right, remaining))
```

```java
// Java - Path Sum
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) {
        return false;
    }

    if (root.left == null && root.right == null) {
        return root.val == targetSum;
    }

    int remaining = targetSum - root.val;
    return hasPathSum(root.left, remaining) ||
           hasPathSum(root.right, remaining);
}
```

#### LeetCode 題目
- LC 112: Path Sum (Easy)

---

### 3.3) 路徑 + 回溯模板 — LC 113 ⭐⭐⭐⭐

**模式**：用回溯把所有路徑收集起來
**適用情境**：找出所有符合條件的路徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Path Sum II
def path_sum(root, target_sum):
    result = []

    def dfs(node, remaining, path):
        if not node:
            return

        path.append(node.val)

        if not node.left and not node.right and remaining == node.val:
            result.append(path[:])

        dfs(node.left, remaining - node.val, path)
        dfs(node.right, remaining - node.val, path)

        path.pop()  # Backtrack

    dfs(root, target_sum, [])
    return result
```

```java
// Java - Path Sum II
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

    path.remove(path.size() - 1);  // Backtrack
}
```

#### LeetCode 題目
- LC 113: Path Sum II (Medium)
- LC 257: Binary Tree Paths (Easy)

---

### 3.4) 路徑計數模板 — LC 437

**模式**：用前綴和數路徑
**適用情境**：和為目標值的路徑（起訖點任意）
**時間複雜度**：O(n)
**空間複雜度**：O(n)

#### 模板程式碼

```python
# Python - Path Sum III
def path_sum(root, target_sum):
    def dfs(node, current_sum):
        if not node:
            return 0

        current_sum += node.val

        # Count paths ending at current node
        count = prefix_sum.get(current_sum - target_sum, 0)

        # Add current sum to map
        prefix_sum[current_sum] = prefix_sum.get(current_sum, 0) + 1

        # Recurse to children
        count += dfs(node.left, current_sum)
        count += dfs(node.right, current_sum)

        # Backtrack
        prefix_sum[current_sum] -= 1

        return count

    prefix_sum = {0: 1}
    return dfs(root, 0)
```

```java
// Java - Path Sum III
private int count = 0;

public int pathSum(TreeNode root, int targetSum) {
    Map<Long, Integer> prefixSum = new HashMap<>();
    prefixSum.put(0L, 1);
    dfs(root, 0L, targetSum, prefixSum);
    return count;
}

private void dfs(TreeNode node, long currentSum, int targetSum,
                 Map<Long, Integer> prefixSum) {
    if (node == null) return;

    currentSum += node.val;

    count += prefixSum.getOrDefault(currentSum - targetSum, 0);

    prefixSum.put(currentSum, prefixSum.getOrDefault(currentSum, 0) + 1);

    dfs(node.left, currentSum, targetSum, prefixSum);
    dfs(node.right, currentSum, targetSum, prefixSum);

    prefixSum.put(currentSum, prefixSum.get(currentSum) - 1);
}
```

#### LeetCode 題目
- LC 437: Path Sum III (Medium)

---

### 3.5) 路徑數值組建模板 — LC 129

**模式**：從根到葉一路組出一個數值
**適用情境**：計算根到葉路徑所代表的數字
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Sum Root to Leaf Numbers
def sum_numbers(root):
    def dfs(node, current_number):
        if not node:
            return 0

        current_number = current_number * 10 + node.val

        # Leaf node - return the number
        if not node.left and not node.right:
            return current_number

        # Sum from both subtrees
        return dfs(node.left, current_number) + dfs(node.right, current_number)

    return dfs(root, 0)
```

```java
// Java - Sum Root to Leaf Numbers
public int sumNumbers(TreeNode root) {
    return dfs(root, 0);
}

private int dfs(TreeNode node, int currentNumber) {
    if (node == null) return 0;

    currentNumber = currentNumber * 10 + node.val;

    if (node.left == null && node.right == null) {
        return currentNumber;
    }

    return dfs(node.left, currentNumber) + dfs(node.right, currentNumber);
}
```

#### LeetCode 題目
- LC 129: Sum Root to Leaf Numbers (Medium)

---

### 3.6) 路徑狀態追蹤模板 — LC 1448

**模式**：沿路徑追蹤最大值
**適用情境**：數出好節點（值 >= 路徑上最大值的節點）
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Count Good Nodes
def good_nodes(root):
    def dfs(node, max_so_far):
        if not node:
            return 0

        count = 1 if node.val >= max_so_far else 0

        new_max = max(max_so_far, node.val)
        count += dfs(node.left, new_max)
        count += dfs(node.right, new_max)

        return count

    return dfs(root, float('-inf'))
```

```java
// Java - Count Good Nodes
public int goodNodes(TreeNode root) {
    return dfs(root, Integer.MIN_VALUE);
}

private int dfs(TreeNode node, int maxSoFar) {
    if (node == null) return 0;

    int count = node.val >= maxSoFar ? 1 : 0;

    int newMax = Math.max(maxSoFar, node.val);
    count += dfs(node.left, newMax);
    count += dfs(node.right, newMax);

    return count;
}
```

#### LeetCode 題目
- LC 1448: Count Good Nodes in Binary Tree (Medium)

---

### 3.7) 最長路徑模板 — LC 543

**模式**：找出任意兩節點之間的最長路徑
**適用情境**：樹的直徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Diameter of Binary Tree
def diameter_of_binary_tree(root):
    diameter = 0

    def depth(node):
        nonlocal diameter

        if not node:
            return 0

        left_depth = depth(node.left)
        right_depth = depth(node.right)

        # Update diameter
        diameter = max(diameter, left_depth + right_depth)

        return 1 + max(left_depth, right_depth)

    depth(root)
    return diameter
```

```java
// Java - Diameter of Binary Tree
private int diameter = 0;

public int diameterOfBinaryTree(TreeNode root) {
    depth(root);
    return diameter;
}

private int depth(TreeNode node) {
    if (node == null) return 0;

    int leftDepth = depth(node.left);
    int rightDepth = depth(node.right);

    diameter = Math.max(diameter, leftDepth + rightDepth);

    return 1 + Math.max(leftDepth, rightDepth);
}
```

#### LeetCode 題目
- LC 543: Diameter of Binary Tree (Easy)

---

### 3.8) 同值路徑模板 — LC 687

**模式**：找出值都相同的最長路徑
**適用情境**：最長同值路徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Longest Univalue Path
def longest_univalue_path(root):
    longest = 0

    def dfs(node):
        nonlocal longest

        if not node:
            return 0

        left_length = dfs(node.left)
        right_length = dfs(node.right)

        left_path = left_length + 1 if node.left and node.left.val == node.val else 0
        right_path = right_length + 1 if node.right and node.right.val == node.val else 0

        longest = max(longest, left_path + right_path)

        return max(left_path, right_path)

    dfs(root)
    return longest
```

```java
// Java - Longest Univalue Path
private int longest = 0;

public int longestUnivaluePath(TreeNode root) {
    dfs(root);
    return longest;
}

private int dfs(TreeNode node) {
    if (node == null) return 0;

    int leftLength = dfs(node.left);
    int rightLength = dfs(node.right);

    int leftPath = 0, rightPath = 0;

    if (node.left != null && node.left.val == node.val) {
        leftPath = leftLength + 1;
    }
    if (node.right != null && node.right.val == node.val) {
        rightPath = rightLength + 1;
    }

    longest = Math.max(longest, leftPath + rightPath);

    return Math.max(leftPath, rightPath);
}
```

#### LeetCode 題目
- LC 687: Longest Univalue Path (Medium)

---

## 4) 距離與 LCA 模板

這四份模板已經搬出這份表單。現在由 [tree_lca_distance.md](./tree_lca_distance.md)
負責，而且每一份都講得比這裡能容納的篇幅長上好幾倍：

| 這一節原本放什麼 | 現在在哪裡 |
|---|---|
| 4.1) LCA 標準模板 — LC 236 | [LCA — LC 236](./tree_lca_distance.md#1-lowest-common-ancestor-lca--lc-236)，還多了 LC 865 / 1123 的最深節點變形 |
| 4.2) 值比較模板 — LC 235 | 同一節 — LC 235 是同一份模板在 BST 上的捷徑 |
| 4.3) 路徑距離模板 — LC 1740 | [Distance Between Nodes — LC 1740](./tree_lca_distance.md#3-distance-between-nodes--lc-1740) |
| 4.4) 樹轉圖模板 — LC 863 | [Move Parent Pattern](./tree_lca_distance.md#2-move-parent-pattern---bidirectional-tree-traversal)，那是一般化的形式 — LC 863 和 LC 742 都是它的實例 |

**從這裡該帶走的想法**：樹上每一個「距離」問題，骨子裡都是 LCA 問題，因為兩個節點之間
唯一的路徑必定經過它們的最低共同祖先 — `dist(p, q) = depth(p) + depth(q) - 2·depth(lca)`。
如果還需要*往上*走，就加上父指標，把樹當成無向圖來處理。


## 6) 樹的建構模板

建構也搬出這份表單了；由兩份 Tier 1 表單分別接手：

| 這一節原本放什麼 | 現在在哪裡 |
|---|---|
| 6.1) 建樹模板 — LC 105 / 106 | [tree_construction.md](./tree_construction.md#2-construct-binary-tree-from-preorder-and-inorder-traversal--lc-105) — 這份表單原本的 Java 模板和它的 LC 106 後序變形都併過去了 |
| 6.2) 字串轉換模板 — LC 297 | [tree_codec.md](./tree_codec.md) — 整個 codec 家族，LC 297 / 449 / 331 |
| 6.3) 字串建構模板 — LC 606 | [tree_codec.md](./tree_codec.md) — 括號格式與省略成對括號的規則 |

**從這裡該帶走的想法**：每一個建構問題都是同一套遞迴 — 從編碼中認出根節點、算出輸入中
有多少屬於各棵子樹，然後遞迴下去。只有第一步不一樣：前序的開頭、後序的結尾、某個區間的
最大值，或是第一個 `(` 之前的 token。


## 7) 樹的修改模板

### 7.1) 樹翻轉模板 — LC 226

**模式**：交換左右子樹
**適用情境**：把樹鏡像／翻轉
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Invert Binary Tree
def invert_tree(root):
    if not root:
        return None

    # Swap children
    root.left, root.right = root.right, root.left

    # Recursively invert subtrees
    invert_tree(root.left)
    invert_tree(root.right)

    return root
```

```java
// Java - Invert Binary Tree
public TreeNode invertTree(TreeNode root) {
    if (root == null) return null;

    // Cache children
    TreeNode left = invertTree(root.left);
    TreeNode right = invertTree(root.right);

    // Swap
    root.left = right;
    root.right = left;

    return root;
}
```

#### LeetCode 題目
- LC 226: Invert Binary Tree (Easy)

---

### 7.2) 樹攤平模板 — LC 114

**模式**：把樹攤平成鏈結串列
**適用情境**：轉成全部往右倒的樹
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```python
# Python - Flatten Binary Tree to Linked List
def flatten(root):
    if not root:
        return

    flatten(root.left)
    flatten(root.right)

    # Save right subtree
    right = root.right

    # Move left subtree to right
    root.right = root.left
    root.left = None

    # Attach original right subtree to end
    current = root
    while current.right:
        current = current.right
    current.right = right
```

```java
// Java - Flatten Binary Tree to Linked List
public void flatten(TreeNode root) {
    if (root == null) return;

    flatten(root.left);
    flatten(root.right);

    TreeNode right = root.right;

    root.right = root.left;
    root.left = null;

    TreeNode current = root;
    while (current.right != null) {
        current = current.right;
    }
    current.right = right;
}
```

#### LeetCode 題目
- LC 114: Flatten Binary Tree to Linked List (Medium)

---

### 7.3) 樹合併模板 — LC 617

**模式**：逐節點合併兩棵樹
**適用情境**：把兩棵樹疊在一起
**時間複雜度**：O(min(n, m))
**空間複雜度**：O(min(h1, h2))

#### 模板程式碼

```python
# Python - Merge Two Binary Trees
def merge_trees(t1, t2):
    if not t1 and not t2:
        return None
    if not t1:
        return t2
    if not t2:
        return t1

    # Merge current nodes
    merged = TreeNode(t1.val + t2.val)

    # Recursively merge children
    merged.left = merge_trees(t1.left, t2.left)
    merged.right = merge_trees(t1.right, t2.right)

    return merged
```

```java
// Java - Merge Two Binary Trees
public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
    if (t1 == null && t2 == null) return null;
    if (t1 == null) return t2;
    if (t2 == null) return t1;

    TreeNode merged = new TreeNode(t1.val + t2.val);

    merged.left = mergeTrees(t1.left, t2.left);
    merged.right = mergeTrees(t1.right, t2.right);

    return merged;
}
```

#### LeetCode 題目
- LC 617: Merge Two Binary Trees (Easy)

---

## 8) 進階樹模板

### 8.1) O(1) 空間的層串接模板 — LC 117 ⭐⭐⭐⭐⭐

**模式**：把已經串好的那一層當成**鏈結串列**，然後用 **dummy head + tail** 指標建出下一層
**適用情境**：不用 BFS 佇列就把每一層的 `next` 指標接起來
**核心想法**：每一層自己已經知道順序時，你根本不需要佇列 — 沿著 `next` 走過去，把子節點接到一個有哨兵頭的串列上，然後往下降到 `dummy.next`
**時間複雜度**：O(n)
**空間複雜度**：O(1) — 沒有佇列，也沒有遞迴

#### 模板程式碼

```java
// java
// LC 117 - Populating Next Right Pointers in Each Node II
// IDEA: walk the current level through its own `next` chain; build the next level
//       onto a dummy head so missing children need no special cases
// time = O(N), space = O(1)
public Node connect(Node root) {
    Node curr = root;
    while (curr != null) {
        Node dummy = new Node(0);   // sentinel head of the level below
        Node tail  = dummy;
        for (Node node = curr; node != null; node = node.next) {
            if (node.left  != null) { tail.next = node.left;  tail = tail.next; }
            if (node.right != null) { tail.next = node.right; tail = tail.next; }
        }
        curr = dummy.next;          // descend to the level we just linked
    }
    return root;
}
```

```python
# python
# LC 117 - Populating Next Right Pointers in Each Node II
# IDEA: current level is already a linked list via `next`;
#       append its children to a dummy-headed list, then descend
# time = O(N), space = O(1)
def connect(root):
    curr = root
    while curr:
        dummy = Node(0)          # sentinel head of the level below
        tail = dummy
        node = curr
        while node:
            if node.left:
                tail.next = node.left
                tail = tail.next
            if node.right:
                tail.next = node.right
                tail = tail.next
            node = node.next
        curr = dummy.next        # descend to the level we just linked
    return root
```

**變形 — LC 116（完美二元樹）**：每個節點不是 0 個就是 2 個子節點，所以 dummy head 沒有必要 — 直接接 `node.left → node.right` 和 `node.right → node.next.left`，然後直接掉到 `leftmost.left`。

```java
// java
// LC 116 - Populating Next Right Pointers in Each Node (perfect tree)
// IDEA: perfect tree ⇒ children always exist ⇒ link them directly from the parent level
// time = O(N), space = O(1)
public Node connect(Node root) {
    Node leftmost = root;
    while (leftmost != null && leftmost.left != null) {
        for (Node node = leftmost; node != null; node = node.next) {
            node.left.next = node.right;                       // same parent
            if (node.next != null) node.right.next = node.next.left;  // across parents
        }
        leftmost = leftmost.left;
    }
    return root;
}
```

```python
# python
# LC 116 - Populating Next Right Pointers in Each Node (perfect tree)
# time = O(N), space = O(1)
def connect(root):
    leftmost = root
    while leftmost and leftmost.left:
        node = leftmost
        while node:
            node.left.next = node.right
            if node.next:
                node.right.next = node.next.left
            node = node.next
        leftmost = leftmost.left
    return root
```

#### LeetCode 題目
- LC 117: Populating Next Right Pointers in Each Node II (Medium)
- LC 116: Populating Next Right Pointers in Each Node (Medium)


**為什麼要 dummy head**：子節點可能缺席（LC 117 是*一般*二元樹，不是完美二元樹），所以你沒辦法
靠位置算出「下一個節點是誰」。dummy 加上 `tail` 指標會自動跳過那些洞 — 這正是為什麼*同一份*
程式碼可以同時解掉 LC 116 和 LC 117。

**追蹤**（`root = [1,2,3,4,5,null,7]`）：

```text
level 1:  1                      dummy -> 2 -> 3
level 2:  2 -> 3                 dummy -> 4 -> 5 -> 7   (3 has no left child; dummy skips the hole)
level 3:  4 -> 5 -> 7            dummy -> null  -> stop
```

**什麼時候可以重用這招**：任何「串接／比較同一層節點」的題目，只要節點本身帶著一個備用指標
（LC 116、LC 117）。如果節點**沒有** `next` 欄位，就退回 [1.4)](#14-bfs-template-level-order--lc-102-) 的佇列 BFS。

### 8.2) 後序樹 DP（回傳一對值）模板 — LC 337 ⭐⭐⭐⭐

**模式**：每個節點回傳**兩個（或 k 個）答案** — 每種狀態各一個 — 而不是單一個數字
**適用情境**：相鄰節點的限制（「不能同時取一個節點和它的子節點」），以及任何「父節點的選擇取決於子節點有沒有被取」的樹 DP
**核心想法**：回傳 `{take, skip}` 就不需要記憶化了 — 一趟單純的後序走訪本來就是 O(n)
**時間複雜度**：O(n)
**空間複雜度**：O(h)

#### 模板程式碼

```java
// java
// LC 337 - House Robber III
// IDEA: post-order tree DP — each node returns {best if robbed, best if skipped}
// time = O(N), space = O(H)
public int rob(TreeNode root) {
    int[] res = dfs(root);
    return Math.max(res[0], res[1]);
}

// returns {maxIfRobCurrent, maxIfSkipCurrent}
private int[] dfs(TreeNode node) {
    if (node == null) return new int[]{0, 0};

    int[] l = dfs(node.left);
    int[] r = dfs(node.right);

    int rob  = node.val + l[1] + r[1];                          // children MUST be skipped
    int skip = Math.max(l[0], l[1]) + Math.max(r[0], r[1]);     // children are free to choose

    return new int[]{rob, skip};
}
```

```python
# python
# LC 337 - House Robber III
# IDEA: post-order tree DP — return (rob_this, skip_this) per node
# time = O(N), space = O(H)
def rob(root):
    def dfs(node):
        if not node:
            return (0, 0)

        l = dfs(node.left)
        r = dfs(node.right)

        rob_this  = node.val + l[1] + r[1]   # children must be skipped
        skip_this = max(l) + max(r)          # children choose freely

        return (rob_this, skip_this)

    return max(dfs(root))
```

#### LeetCode 題目
- LC 337: House Robber III (Medium)

---

### 8.3) 座標對應走訪模板 — LC 987

**模式**：用 DFS 幫每個節點標上 `(col, row)`，然後**依 (col, row, val) 排序**
**適用情境**：垂直／依欄輸出，而且平手時必須有確定的排序規則
**核心想法**：`left → col - 1`、`right → col + 1`、深度 → `row`。光靠 BFS 對 LC 987 是*不夠*的：兩個節點可能有相同的 `(col, row)`，而平手時要用**值**來決勝，所以要先收集再排序
**時間複雜度**：O(n log n)
**空間複雜度**：O(n)

#### 模板程式碼

```java
// java
// LC 987 - Vertical Order Traversal of a Binary Tree
// IDEA: DFS collecting (col, row, val) triples, then sort col → row → val
// time = O(N log N), space = O(N)
public List<List<Integer>> verticalTraversal(TreeNode root) {
    List<int[]> nodes = new ArrayList<>();   // {col, row, val}
    dfs(root, 0, 0, nodes);

    nodes.sort((a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0])
                       : a[1] != b[1] ? Integer.compare(a[1], b[1])
                       : Integer.compare(a[2], b[2]));

    List<List<Integer>> res = new ArrayList<>();
    Integer prevCol = null;
    for (int[] n : nodes) {
        if (prevCol == null || n[0] != prevCol) {
            res.add(new ArrayList<>());
            prevCol = n[0];
        }
        res.get(res.size() - 1).add(n[2]);
    }
    return res;
}

private void dfs(TreeNode node, int row, int col, List<int[]> nodes) {
    if (node == null) return;
    nodes.add(new int[]{col, row, node.val});
    dfs(node.left,  row + 1, col - 1, nodes);
    dfs(node.right, row + 1, col + 1, nodes);
}
```

```python
# python
# LC 987 - Vertical Order Traversal of a Binary Tree
# IDEA: DFS collecting (col, row, val); plain tuple sort gives col → row → val
# time = O(N log N), space = O(N)
def vertical_traversal(root):
    nodes = []

    def dfs(node, row, col):
        if not node:
            return
        nodes.append((col, row, node.val))
        dfs(node.left,  row + 1, col - 1)
        dfs(node.right, row + 1, col + 1)

    dfs(root, 0, 0)
    nodes.sort()

    res, prev_col = [], None
    for col, row, val in nodes:
        if col != prev_col:
            res.append([])
            prev_col = col
        res[-1].append(val)
    return res
```

#### LeetCode 題目
- LC 987: Vertical Order Traversal of a Binary Tree (Hard)

---

### 8.4) 完全樹節點計數模板 — LC 222

**模式**：利用**完全樹**的形狀跳過整棵子樹，而不是走訪全部 `n` 個節點
**適用情境**：樹是完全樹時，用比 O(n) 更快的方式數節點
**核心想法**：如果最左深度 == 最右深度，這棵子樹就是**完美的** → 直接 `2^d - 1`，不用遞迴。否則就遞迴下去；每一層只有一個子樹不完美，所以遞迴深度是 O(log n)，而每一步要做一次 O(log n) 的深度探測
**時間複雜度**：O(log² n)
**空間複雜度**：O(log n)

#### 模板程式碼

```java
// java
// LC 222 - Count Complete Tree Nodes
// IDEA: perfect subtree ⇒ 2^d - 1 in O(log n); otherwise recurse on both children
// time = O(log^2 N), space = O(log N)
public int countNodes(TreeNode root) {
    if (root == null) return 0;

    int ld = leftDepth(root), rd = rightDepth(root);
    if (ld == rd) return (1 << ld) - 1;   // perfect subtree — no traversal needed

    return 1 + countNodes(root.left) + countNodes(root.right);
}

private int leftDepth(TreeNode n)  { int d = 0; while (n != null) { d++; n = n.left;  } return d; }
private int rightDepth(TreeNode n) { int d = 0; while (n != null) { d++; n = n.right; } return d; }
```

```python
# python
# LC 222 - Count Complete Tree Nodes
# IDEA: leftmost depth == rightmost depth ⇒ perfect subtree ⇒ 2^d - 1
# time = O(log^2 N), space = O(log N)
def count_nodes(root):
    if not root:
        return 0

    ld, node = 0, root
    while node:
        ld += 1
        node = node.left

    rd, node = 0, root
    while node:
        rd += 1
        node = node.right

    if ld == rd:
        return (1 << ld) - 1          # perfect subtree

    return 1 + count_nodes(root.left) + count_nodes(root.right)
```

#### LeetCode 題目
- LC 222: Count Complete Tree Nodes (Medium)

---

## 總表：所有模板

| 模板名稱 | 模式 | 時間 | 空間 | LeetCode 題目 |
|--------------|---------|------|-------|-------------------|
| **前序模板** | 根 → 左 → 右 | O(n) | O(h) | LC 144 |
| **中序模板** | 左 → 根 → 右 | O(n) | O(h) | LC 94, 98, 230 |
| **後序模板** | 左 → 右 → 根 | O(n) | O(h) | LC 145 |
| **BFS 模板** | 一層一層 | O(n) | O(w) | LC 102, 103, 107, 199 |
| **BFS + 方向** | 各層交替方向 | O(n) | O(w) | LC 103 |
| **後序求高度** | 由下而上算高度 | O(n) | O(h) | LC 104 |
| **BFS 提早結束** | 條件成立就停 | O(n) | O(w) | LC 111 |
| **高度驗證** | 平衡檢查 | O(n) | O(h) | LC 110 |
| **鏡像驗證** | 對稱檢查 | O(n) | O(h) | LC 101 |
| **樹比較** | 比較兩棵樹 | O(n) | O(h) | LC 100, 572 |
| **全域最大值更新** | 追蹤全域最大值 | O(n) | O(h) | LC 124 |
| **路徑累加** | 沿路徑累計總和 | O(n) | O(h) | LC 112 |
| **路徑 + 回溯** | 收集所有路徑 | O(n) | O(h) | LC 113, 257 |
| **路徑計數** | 用前綴和數路徑 | O(n) | O(n) | LC 437 |
| **路徑數值組建** | 組出路徑的數值 | O(n) | O(h) | LC 129 |
| **路徑狀態追蹤** | 追蹤路徑上的最大值 | O(n) | O(h) | LC 1448 |
| **最長路徑** | 計算直徑 | O(n) | O(h) | LC 543 |
| **同值路徑** | 同值路徑 | O(n) | O(h) | LC 687 |
| **LCA 標準** → [tree_lca_distance.md](./tree_lca_distance.md#1-lowest-common-ancestor-lca--lc-236) | 找 LCA | O(n) | O(h) | LC 236 |
| **值比較** → [tree_lca_distance.md](./tree_lca_distance.md#1-lowest-common-ancestor-lca--lc-236) | BST 上的 LCA | O(h) | O(1) | LC 235 |
| **路徑距離** → [tree_lca_distance.md](./tree_lca_distance.md#3-distance-between-nodes--lc-1740) | 透過 LCA 算距離 | O(n) | O(h) | LC 1740 |
| **樹轉圖** → [tree_lca_distance.md](./tree_lca_distance.md#2-move-parent-pattern---bidirectional-tree-traversal) | 為了查詢而轉換 | O(n) | O(n) | LC 863, 742 |
| **最小深度（遞迴）** | 獨子守衛 | O(n) | O(h) | LC 111 |
| **深度最深的最左值** | 左下角的值 | O(n) | O(w) | LC 513 |
| **建樹** → [tree_construction.md](./tree_construction.md#2-construct-binary-tree-from-preorder-and-inorder-traversal--lc-105) | 從陣列建樹 | O(n) | O(n) | LC 105, 106 |
| **字串轉換** → [tree_codec.md](./tree_codec.md) | 序列化／反序列化 | O(n) | O(n) | LC 297, 449 |
| **字串建構** → [tree_codec.md](./tree_codec.md) | 樹轉字串 | O(n) | O(h) | LC 606 |
| **樹翻轉** | 鏡像樹 | O(n) | O(h) | LC 226 |
| **樹攤平** | 攤平成串列 | O(n) | O(h) | LC 114 |
| **樹合併** | 合併兩棵樹 | O(n) | O(h) | LC 617 |
| **O(1) 層串接** | dummy head + `next` 鏈 | O(n) | O(1) | LC 117, 116 |
| **後序樹 DP** | 回傳 {take, skip} 這一對 | O(n) | O(h) | LC 337 |
| **座標對應走訪** | 依 (col, row, val) 排序 | O(n log n) | O(n) | LC 987 |
| **完全樹節點計數** | 完美子樹 ⇒ 2^d − 1 | O(log² n) | O(log n) | LC 222 |

---

## 速查指南

### 各模板的使用時機

1. **需要在處理子節點前先處理根？** → 用前序模板
2. **需要排序好的順序（BST）？** → 用中序模板
3. **父節點需要子節點的資料？** → 用後序模板
4. **需要一層一層處理？** → 用 BFS 模板
5. **需要追蹤路徑上的總和／數值？** → 用路徑追蹤類模板
6. **需要找 LCA？** → [tree_lca_distance.md](./tree_lca_distance.md)
7. **需要從陣列建樹？** → [tree_construction.md](./tree_construction.md)
8. **需要修改樹的結構？** → 用樹的修改模板
9. **需要驗證樹的性質？** → 用驗證類模板
10. **需要算兩節點之間的距離？** → [tree_lca_distance.md](./tree_lca_distance.md)

---

## 練習建議

### Easy 題（從這裡開始）
- LC 144, 94, 145：基本走訪
- LC 100, 101：樹的比較
- LC 104, 111：深度計算
- LC 226：樹翻轉
- LC 617：樹合併

### Medium 題（把功力堆起來）
- LC 102, 103, 107：層序的各種變形
- LC 105, 106：樹的建構
- LC 113, 129, 437：路徑問題
- LC 236：LCA
- LC 114：樹攤平

### Hard 題（進入精通）
- LC 124：最大路徑和
- LC 297：序列化
- LC 1740：距離計算

---

**注意**：所有模板都假設以下的 TreeNode 定義：
```python
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
```

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}
```

## LC 範例 — 沒有專屬模板的題目

> 這份檔案裡其他每一題，都在上面自己編號的模板中解掉了。下面這兩題沒有專屬的模板小節，所以放在這裡。

### 2-1) Validate Binary Search Tree (LC 98) — 帶上下界的 DFS
> 遞迴往下傳合法範圍 (lo, hi)；每個節點的值都必須嚴格落在界線之內。

```java
// LC 98 - Validate Binary Search Tree
// IDEA: DFS with min/max bounds — value must be in (lo, hi)
// time = O(N), space = O(H)
public boolean isValidBST(TreeNode root) {
    return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
}
private boolean validate(TreeNode node, long lo, long hi) {
    if (node == null) return true;
    if (node.val <= lo || node.val >= hi) return false;
    return validate(node.left, lo, node.val) && validate(node.right, node.val, hi);
}
```

### 2-2) Binary Tree Right Side View (LC 199) — BFS 層序
> 一層一層跑 BFS；把每層的最後一個節點記下來，那就是從右邊看得到的。

```java
// LC 199 - Binary Tree Right Side View
// IDEA: BFS — collect rightmost (last) node value per level
// time = O(N), space = O(N)
public List<Integer> rightSideView(TreeNode root) {
    List<Integer> res = new ArrayList<>();
    if (root == null) return res;
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    while (!q.isEmpty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            if (i == size - 1) res.add(node.val);
            if (node.left  != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
    }
    return res;
}
```

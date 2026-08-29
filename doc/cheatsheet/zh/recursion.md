# 遞迴

> **範圍** — 把遞迴當成一種機制來談：base case、往下傳的狀態 vs 往上回傳的結果、呼叫堆疊的成本，以及怎麼改寫成迭代。*使用*遞迴的那些題型家族各自有專屬檔案。
> **另見**：[recursion_to_dp.md](./recursion_to_dp.md) — 把遞迴加上記憶化變成 DP；[backtrack.md](./backtrack.md) — 帶復原動作的遞迴；[dfs.md](./dfs.md) — 在圖與樹上的遞迴；[advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — 帶合併步驟的遞迴。

## LeetCode 題目清單

- [Recursion](https://leetcode.com/problem-list/recursion/)

## 0) 速查

**什麼時候該用遞迴？**
- 問題有**重疊子問題**，而且每次都能縮小規模
- 你能清楚定義出 **base case** 和**遞迴情況**
- 問題天生就能拆成自己的小規模版本
- **樹／圖走訪**或**回溯**類的問題

**快速決策指南**

| 使用情境 | 模式 | 核心想法 |
|----------|---------|----------|
| 需要來自父節點的資訊 | **由上而下** | 一邊走訪一邊把上下文往下傳 |
| 需要來自子節點的結果 | **由下而上** | 先解子節點，再合併結果 |
| 需要切開再合併結果 | **分治法** | 切分問題、各自求解、合併 |
| 需要窮舉所有可能 | **回溯** | 帶決策的 DFS |
| 多次遞迴呼叫、子問題重複 | **記憶化** | 把結果快取起來，避免重複計算 |

### 核心原理

對於問題 F(X)，X 是輸入：

```text
1. Break down into smaller scopes: x₀, x₁, ..., xₙ ∈ X
2. Recursively solve: F(x₀), F(x₁), ..., F(xₙ)
3. Combine results to solve F(X)
```

### 小技巧

- **拿不定主意時**：先把**遞迴關係式**寫下來（F(n) 跟 F(n-1)、F(n-2) 等等是什麼關係）
- **有重複呼叫時**：加上**記憶化**（把中間結果快取起來）
- **怕堆疊溢位時**：用**尾遞迴**，或改寫成**迭代**

---

## 1) 概念

### 1-1) 複雜度分析

**時間複雜度**：
把遞迴想成一個**樹狀結構**：
```text
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

給定一個遞迴演算法：**O(T) = R × O(S)**
- **R** = 遞迴呼叫的次數
- **O(S)** = 每次呼叫本身做的事的時間複雜度
- 沒有記憶化的 Fibonacci：**O(2^n)**（指數級）

**空間複雜度**：

**與遞迴相關的空間**（呼叫堆疊）：
- 遞迴函式呼叫裡的區域變數
- 輸入參數
- 輸出變數
- **堆疊溢位風險**：配置的堆疊空間碰到系統上限時

**與遞迴無關的空間**（heap）：
- 全域變數
- 記憶化的快取（存中間結果）
- **重點**：分析整體複雜度時，記憶化用掉的空間也要算進去

### 1-2) 相關概念

遞迴會用在：
- **DFS**（深度優先搜尋）—— 樹／圖走訪
- **回溯** —— 帶剪枝地窮舉所有可能
- **樹的問題** —— 遞迴演算法天生就合用
- **動態規劃** —— 搭配記憶化最佳化

---

## 2) 模式

### 2-1) 基本操作

無限地走過 list 裡的元素（在回溯／生成類題目很常見）：
```python
# Example: LC 22 (Generate Parentheses)
_list = ["(", ")"]
for x in _list:
    _tmp = tmp + x
    help(_tmp)
```

---

### 2-2) 由上而下的遞迴 —— LC 112

**定義**：從根開始，在每個節點依據父節點傳下來的資訊做決定。也就是所謂的「前序」做法。

**時間複雜度**：
- 通常是 O(n)，n 是節點數
- 如果重複解同樣的子問題又沒有記憶化，可能變成 O(n²)

**空間複雜度**：
- O(h)，h 是遞迴樹的高度（呼叫堆疊）
- 用了記憶化的話還要 O(n) 的額外空間

**使用情境**：
- 需要把資訊從父節點傳給子節點
- 帶累積狀態的樹走訪
- 路徑類問題
- 驗證類問題

**優點**：
- 直覺、好懂
- 對「資訊由父流向子」的問題很自然
- 適合做提早結束的判斷

**缺點**：
- 沒有記憶化的話可能重複計算
- 呼叫堆疊會讓空間複雜度偏高

**模式**：
```python
def topDown(node, parentInfo):
    # Base case
    if not node:
        return baseResult

    # Use parentInfo to make decision
    currentResult = processWithParentInfo(node, parentInfo)

    # Pass updated info to children
    newParentInfo = updateParentInfo(parentInfo, node)
    leftResult = topDown(node.left, newParentInfo)
    rightResult = topDown(node.right, newParentInfo)

    # Combine results
    return combineResults(currentResult, leftResult, rightResult)
```

**常見的 LeetCode 題目**：
- LC 104: Maximum Depth of Binary Tree
- LC 110: Balanced Binary Tree
- LC 112: Path Sum
- LC 113: Path Sum II
- LC 124: Binary Tree Maximum Path Sum
- LC 236: Lowest Common Ancestor
- LC 257: Binary Tree Paths
- LC 404: Sum of Left Leaves
- LC 437: Path Sum III

**範例 —— Path Sum（LC 112）**：
```python
def hasPathSum(self, root, targetSum):
    def topDown(node, currentSum):
        if not node:
            return False

        currentSum += node.val

        # Leaf node check
        if not node.left and not node.right:
            return currentSum == targetSum

        # Continue to children with updated sum
        return (topDown(node.left, currentSum) or
                topDown(node.right, currentSum))

    return topDown(root, 0)
```

### 2-3) 由下而上的遞迴 —— LC 104

**定義**：從葉節點開始，把子節點的結果合併起來，一層一層堆出答案。也就是所謂的「後序」做法。

**時間複雜度**：
- 通常是 O(n)，n 是節點數
- 一般來說效率更好，因為每個節點剛好走一次

**空間複雜度**：
- O(h)，h 是遞迴樹的高度（呼叫堆疊）
- 通常不需要記憶化的額外空間

**使用情境**：
- 答案取決於子樹的結果
- 計算樹的性質（高度、直徑等）
- 聚合類問題
- 樹上的動態規劃

**優點**：
- 效率較好 —— 每個子問題剛好解一次
- 對「資訊由子流向父」的問題很自然
- 程式碼通常比較乾淨
- 多數情況下效能更好

**缺點**：
- 某些問題想起來沒那麼直覺
- 遞迴呼叫可能需要回傳多個值

**模式**：
```python
def bottomUp(node):
    # Base case
    if not node:
        return baseResult

    # Get results from children first
    leftResult = bottomUp(node.left)
    rightResult = bottomUp(node.right)

    # Process current node using children results
    currentResult = processNode(node, leftResult, rightResult)

    return currentResult
```

**常見的 LeetCode 題目**：
- LC 104: Maximum Depth of Binary Tree
- LC 110: Balanced Binary Tree
- LC 543: Diameter of Binary Tree
- LC 124: Binary Tree Maximum Path Sum
- LC 968: Binary Tree Cameras
- LC 979: Distribute Coins in Binary Tree
- LC 1120: Maximum Average Subtree
- LC 1130: Minimum Cost Tree From Leaf Values
- LC 1372: Longest ZigZag Path in a Binary Tree

**範例 —— Maximum Depth（LC 104）**：
```python
def maxDepth(self, root):
    def bottomUp(node):
        if not node:
            return 0

        # Get depths from children
        leftDepth = bottomUp(node.left)
        rightDepth = bottomUp(node.right)

        # Current depth is max of children + 1
        return max(leftDepth, rightDepth) + 1

    return bottomUp(root)
```

**範例 —— Balanced Binary Tree（LC 110）**：
```python
def isBalanced(self, root):
    def bottomUp(node):
        if not node:
            return True, 0  # (isBalanced, height)

        # Check left subtree
        leftBalanced, leftHeight = bottomUp(node.left)
        if not leftBalanced:
            return False, 0

        # Check right subtree
        rightBalanced, rightHeight = bottomUp(node.right)
        if not rightBalanced:
            return False, 0

        # Check current node balance
        isCurrentBalanced = abs(leftHeight - rightHeight) <= 1
        currentHeight = max(leftHeight, rightHeight) + 1

        return isCurrentBalanced, currentHeight

    balanced, _ = bottomUp(root)
    return balanced
```

**比較表**：

| 面向 | 由上而下 | 由下而上 |
|--------|----------|-----------|
| **方向** | 根 → 葉 | 葉 → 根 |
| **資訊流向** | 父 → 子 | 子 → 父 |
| **什麼時候用** | 需要父節點的上下文 | 需要子樹的結果 |
| **效率** | 可能有重複計算 | 通常比較好 |
| **直覺度** | 路徑類問題比較直覺 | 聚合類問題比較直覺 |
| **需要記憶化嗎** | 常常需要 | 很少需要 |

---

### 2-4) 把狀態傳給下一層遞迴 —— LC 404

把累積的狀態／上下文當成參數傳給子層的遞迴呼叫。需要記住父節點的資訊時很好用。

**範例：LC 404（Sum of Left Leaves）**
```java
// LC 404 - Sum of Left Leaves
// IDEA: Pre-order traversal, pass isLeft flag to track if node is left child
private int processSubtree(TreeNode subtree, boolean isLeft) {
    // Base case: empty subtree
    if (subtree == null) {
        return 0;
    }

    // Base case: leaf node
    if (subtree.left == null && subtree.right == null) {
        return isLeft ? subtree.val : 0;
    }

    // Recursive case: process left and right subtrees
    return processSubtree(subtree.left, true) + processSubtree(subtree.right, false);
}
```

**關鍵洞見**：把 `isLeft` 當參數傳下去，就能追蹤父節點的上下文，不需要全域狀態。

---

### 2-5) 遞迴中的「任一為真」 —— LC 572

當你要在多個遞迴呼叫中找出「有沒有任何一個為真」，就用 OR 邏輯。只要有一個遞迴呼叫回傳 true 就提早收工。

**範例：LC 572（Subtree of Another Tree）**

```java
// LC 572 - Subtree of Another Tree
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    // Check if subtree rooted at 'root' matches 'subRoot'
    // Use OR: if ANY recursive call returns true, short-circuit and return true
    return isSameTree(root, subRoot) 
        || isSubtree(root.left, subRoot) 
        || isSubtree(root.right, subRoot);
}

private boolean isSameTree(TreeNode node1, TreeNode node2) {
    if (node1 == null || node2 == null) {
        return node1 == null && node2 == null;
    }
    return node1.val == node2.val 
        && isSameTree(node1.left, node2.left) 
        && isSameTree(node1.right, node2.right);
}
```

**關鍵洞見**：用 OR（`||`）可以在找到 true 的當下就離開，省掉不必要的遞迴呼叫。

---

### 2-6) 笛卡兒積式建構 —— LC 95

**定義**：切分一個區間，對每種切法遞迴生成所有子結果，再用笛卡兒積把它們組起來，藉此生成所有可能的結構。這是**分治法**的一種形式，只是「合併」那一步變成列舉所有左 × 右的組合。

**時間複雜度**：O(4^n / n^(3/2)) —— Catalan 數的成長速度

**空間複雜度**：O(4^n / n^(3/2)) —— 存下所有生成的結構

**使用情境**：
- 生成所有結構相異的樹（BST、完滿二元樹）
- 列舉一個運算式所有加括號／切分的方式
- 任何「切分區間，再合併所有子結果」的問題

**模式**：
```text
1. Pick each element i in [start, end] as the "root" / split point
2. Recursively build all left results from [start, i-1]
3. Recursively build all right results from [i+1, end]
4. Cartesian product: for each left × right, construct and collect result
5. Base case: empty range → return [null/None] (one empty result, NOT empty list)
```

```java
// Template: Recursive Construction via Cartesian Product
private List<TreeNode> build(int start, int end) {
    List<TreeNode> res = new ArrayList<>();
    if (start > end) {
        res.add(null);  // CRITICAL: null = valid empty subtree
        return res;
    }
    for (int i = start; i <= end; i++) {
        List<TreeNode> lefts = build(start, i - 1);
        List<TreeNode> rights = build(i + 1, end);
        for (TreeNode l : lefts)
            for (TreeNode r : rights)
                res.add(new TreeNode(i, l, r));
    }
    return res;
}
```

**關鍵洞見**：base case 必須回傳 `[null]`（裝著 null 的 list），**不是**空 list。否則笛卡兒積會把所有左／右子樹為空的樹整批弄丟。

**最佳化**：用 `Map<Pair<Integer,Integer>, List<TreeNode>>` 加上記憶化，避免重算重疊的子問題。

**常見的 LeetCode 題目**：
- LC 95: Unique Binary Search Trees II
- LC 96: Unique Binary Search Trees（Catalan 計數）
- LC 241: Different Ways to Add Parentheses
- LC 894: All Possible Full Binary Trees
- LC 1382: Balance a Binary Search Tree

**範例 —— LC 95: Unique Binary Search Trees II**：
```python
def generateTrees(n):
    if n == 0: return []
    def generate(start, end):
        if start > end:
            return [None]
        all_trees = []
        for i in range(start, end + 1):
            for left in generate(start, i - 1):
                for right in generate(i + 1, end):
                    root = TreeNode(i)
                    root.left = left
                    root.right = right
                    all_trees.append(root)
        return all_trees
    return generate(1, n)
```

---

## 3) 進階技巧

### 3-1) 記憶化 —— LC 70

**想法**：把遞迴呼叫的結果快取起來，同一個子問題再出現時就不用重算。

**什麼時候用**：
- 遞迴呼叫會重複（重疊子問題）
- 不做記憶化的話時間複雜度是指數級
- 拿空間換時間（用雜湊表當快取）

**範例 1：Fibonacci**
```python
# Without memoization: O(2^n) — exponential
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)

# With memoization: O(n) — linear
def fibonacci(n):
    cache = {}
    def helper(n):
        if n in cache:
            return cache[n]
        if n < 2:
            res = n
        else:
            res = helper(n - 1) + helper(n - 2)
        cache[n] = res
        return res
    return helper(n)
```

**範例 2：Climbing Stairs（LC 70）**
```python
# Without memoization: O(2^n)
class Solution:
    def climbStairs(self, n):
        if n == 1:
            return 1
        if n == 2:
            return 2
        return self.climbStairs(n - 1) + self.climbStairs(n - 2)

# With memoization: O(n)
class Solution:
    def climbStairs(self, n):
        cache = {}
        def helper(n):
            if n in cache:
                return cache[n]
            if n <= 2:
                res = n
            else:
                res = helper(n - 2) + helper(n - 1)
            cache[n] = res
            return res
        return helper(n)
```

**參考**：https://leetcode.com/explore/learn/card/recursion-i/255/recursion-memoization/1495/

---

### 3-2) 分治法 —— LC 23

**模板**：
```text
1. Divide: Split problem into subproblems
2. Conquer: Solve each subproblem recursively
3. Combine: Merge subproblem results
```

**虛擬碼**：
```python
def divide_and_conquer(problem):
    # (1) Divide
    subproblems = divide(problem)
    
    # (2) Conquer
    results = [divide_and_conquer(sub) for sub in subproblems]
    
    # (3) Combine
    return combine(results)
```

**常見例子**：
- 合併排序 —— O(n log n)
- 快速排序 —— 平均 O(n log n)
- 二分搜尋 —— O(log n)

**常見的 LeetCode 題目**：
- LC 22: Generate Parentheses
- LC 84: Largest Rectangle in Histogram
- LC 315: Count of Smaller Numbers After Self
- LC 493: Reverse Pairs
- LC 1649: Create Sorted Array Through Instructions

**參考**：https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2869/

---

### 3-3) 把遞迴改寫成迭代（展開遞迴）

**為什麼要改**：
- 避免堆疊溢位的風險
- 改善空間／時間效率
- 減少函式呼叫的開銷

**怎麼改**：
```text
1. Use a stack or queue to replace the system call stack
2. At each recursion point, push parameters onto data structure
3. Replace recursive chain with loop over the data structure
```

**範例**：https://leetcode.com/explore/learn/card/recursion-ii/503/recursion-to-iteration/2693/

---

## 4) 完整的 LeetCode 範例

### 4-1) Symmetric Tree（LC 101）

**模式**：由下而上的遞迴，同時比較兩棵子樹。

```python
class Solution:
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

---

### 4-2) One Edit Distance（LC 161）

**模式**：提早剪枝（長度差的絕對值 > 1），再逐一檢查每個位置。

```python
class Solution:
    def isOneEditDistance(self, s, t):
        m, n = len(s), len(t)
        if abs(m - n) > 1:
            return False
        if m > n:
            return self.isOneEditDistance(t, s)
        for i in range(m):
            if s[i] != t[i]:
                if m == n:
                    return s[i + 1:] == t[i + 1:]
                return s[i:] == t[i + 1:]
        return m != n
```

---

### 4-3) Merge Two Sorted Lists（LC 21）

**模式**：單純的遞迴，順手更新區域狀態。

```python
class Solution:
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

---

### 4-4) Subtree of Another Tree（LC 572）

**模式**：搭配遞迴輔助函式的「任一為真」。

```python
class Solution:
    def isSubtree(self, root, subRoot):
        def isSameTree(p, q):
            if not p and not q:
                return True
            if not p or not q:
                return False
            return (p.val == q.val and 
                    isSameTree(p.left, q.left) and 
                    isSameTree(p.right, q.right))
        
        if not root and not subRoot:
            return True
        if not root or not subRoot:
            return False
        # Use OR: if any recursive call returns True, stop early
        return (isSameTree(root, subRoot) or 
                self.isSubtree(root.left, subRoot) or 
                self.isSubtree(root.right, subRoot))
```

**Java 版本**：
```java
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    if (root == null) {
        return false;
    }
    if (isIdentical(root, subRoot)) {
        return true;
    }
    return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
}

private boolean isIdentical(TreeNode node1, TreeNode node2) {
    if (node1 == null || node2 == null) {
        return node1 == null && node2 == null;
    }
    return node1.val == node2.val && 
           isIdentical(node1.left, node2.left) && 
           isIdentical(node1.right, node2.right);
}
```

---

## 5) 更多遞迴模板

上面那些章節都以樹為中心。下面四個模板涵蓋面試會出現的其他遞迴形狀：**鏈結串列重接**、**遞迴下降剖析**、**折半遞迴**，以及**純遞迴關係化簡**。

**快速決策表**

| 題目裡的訊號 | 模板 | 例題 |
|-----------------------|----------|----------|
| 重建／重排鏈結串列 | **5-1) 重接指標並回傳新的 head** | LC 206, 24, 25, 203, 234 |
| 字串裡有巢狀括號／文法 | **5-2) 遞迴下降（共用游標）** | LC 394, 224, 1106, 736 |
| `n` 每一步是按*倍率*縮小 | **5-3) 折半遞迴** | LC 50, 1922, 231/326/342 |
| 從 `f(n-1)` 推出封閉形式的 `f(n)` | **5-4) 遞迴關係化簡** | LC 779, 1823, 273 |

---

### 5-1) 遞迴式鏈結串列重接 —— LC 206 / 24 / 25 ⭐⭐⭐⭐⭐

**定義**：一個遞迴的鏈結串列函式收下某段子串列的 head，然後**回傳已經處理完的那段子串列的新 head**。呼叫者再把回傳的 head 接到自己的節點上。所有指標手術都發生在遞迴呼叫*回來之後*（也就是說，在串列上這是由下而上的做法）。

**三步驟契約** —— 這三步做對，所有串列遞迴都會自己掉出來：

```text
1. Base case   : list too short to change -> return head unchanged
2. Recurse     : newTail/rest = f(<node further down the list>)
3. Rewire      : fix current node's `next`, then RETURN the node that is now first
```

**時間**：O(n) —— 每個節點碰一次。**空間**：O(n) 的呼叫堆疊（LC 25 是 O(n/k)）。

**關鍵洞見**：絕對不要想「原地改、回傳 void」。回傳值*就是*新的 head；LC 206 忘了設 `head.next = null` 就是那個經典的環狀 bug。

**範例 —— LC 206: Reverse Linked List**

```java
// java
// LC 206 - Reverse Linked List
// IDEA: recursion returns the NEW head; on the way back up, make my successor point at me
// time = O(n), space = O(n) (call stack)
public ListNode reverseList(ListNode head) {
    // base: empty or single node -> already reversed
    if (head == null || head.next == null) return head;

    ListNode newHead = reverseList(head.next); // newHead = tail of original list
    head.next.next = head;                     // successor now points back at me
    head.next = null;                          // CRITICAL: cut old link, else cycle
    return newHead;                            // head never changes going back up
}
```

```python
# python
# LC 206 - Reverse Linked List
# IDEA: recursion returns the NEW head; on the way back up, make my successor point at me
# time = O(n), space = O(n) (call stack)
def reverseList(head):
    if not head or not head.next:
        return head
    new_head = reverseList(head.next)
    head.next.next = head
    head.next = None      # CRITICAL: cut old link, else cycle
    return new_head
```

**範例 —— LC 24: Swap Nodes in Pairs**

```java
// java
// LC 24 - Swap Nodes in Pairs
// IDEA: swap the first two nodes, recurse on the rest, return the 2nd node as new head
// time = O(n), space = O(n)
public ListNode swapPairs(ListNode head) {
    if (head == null || head.next == null) return head; // 0 or 1 node left

    ListNode second = head.next;
    head.next = swapPairs(second.next); // rest of list, already swapped
    second.next = head;
    return second;                      // second is now the head of this pair
}
```

```python
# python
# LC 24 - Swap Nodes in Pairs
# IDEA: swap the first two nodes, recurse on the rest, return the 2nd node as new head
# time = O(n), space = O(n)
def swapPairs(head):
    if not head or not head.next:
        return head
    second = head.next
    head.next = swapPairs(second.next)
    second.next = head
    return second
```

**範例 —— LC 25: Reverse Nodes in k-Group**（把 LC 24 從 k=2 推廣到任意 k）

```java
// java
// LC 25 - Reverse Nodes in k-Group
// IDEA: probe k nodes ahead; if a full group exists, recurse on the remainder FIRST,
//       then reverse this group with the recursive result as its new tail
// time = O(n), space = O(n/k) recursion depth
public ListNode reverseKGroup(ListNode head, int k) {
    // step 1: is there a full group of k? if not, leave the tail untouched
    ListNode node = head;
    for (int i = 0; i < k; i++) {
        if (node == null) return head;
        node = node.next;
    }
    // node = (k+1)-th node = start of the remainder

    // step 2: solve the remainder first, it becomes what this group points to
    ListNode prev = reverseKGroup(node, k);

    // step 3: standard iterative reverse of exactly k nodes onto `prev`
    ListNode cur = head;
    for (int i = 0; i < k; i++) {
        ListNode nxt = cur.next;
        cur.next = prev;
        prev = cur;
        cur = nxt;
    }
    return prev; // prev = k-th node = new head of this group
}
```

```python
# python
# LC 25 - Reverse Nodes in k-Group
# IDEA: probe k nodes ahead; if a full group exists, recurse on the remainder FIRST,
#       then reverse this group with the recursive result as its new tail
# time = O(n), space = O(n/k) recursion depth
def reverseKGroup(head, k):
    node = head
    for _ in range(k):
        if not node:
            return head          # fewer than k nodes left -> keep as-is
        node = node.next

    prev = reverseKGroup(node, k)  # reversed remainder
    cur = head
    for _ in range(k):
        nxt = cur.next
        cur.next = prev
        prev = cur
        cur = nxt
    return prev
```

**各種變形**

- **LC 203（Remove Linked List Elements）** —— 變化點：做的是*刪除*而不是重排，所以「重接」那一步變成有條件的 return。不需要 dummy 節點：

```java
// java
// LC 203 - Remove Linked List Elements
// IDEA: clean the rest first, then decide whether to keep myself
// time = O(n), space = O(n)
public ListNode removeElements(ListNode head, int val) {
    if (head == null) return null;
    head.next = removeElements(head.next, val);
    return head.val == val ? head.next : head; // skip myself if I match
}
```

- **LC 234（Palindrome Linked List）** —— 變化點：完全不重接；把**呼叫堆疊當成反向迭代器**。遞迴回溯的過程往回走，同時用一個成員變數往前走。

```java
// java
// LC 234 - Palindrome Linked List
// IDEA: recursion unwinds back-to-front; `front` pointer moves front-to-back in lockstep
// time = O(n), space = O(n) (O(1) space alternative: reverse the 2nd half iteratively)
private ListNode front;
public boolean isPalindrome(ListNode head) {
    front = head;
    return check(head);
}
private boolean check(ListNode node) {
    if (node == null) return true;
    if (!check(node.next)) return false; // go to the end first
    if (node.val != front.val) return false;
    front = front.next;                  // compare back-node vs front-node
    return true;
}
```

```python
# python
# LC 234 - Palindrome Linked List
# IDEA: recursion unwinds back-to-front; `front` pointer moves front-to-back in lockstep
# time = O(n), space = O(n)
def isPalindrome(head):
    front = head
    def check(node):
        nonlocal front
        if not node:
            return True
        if not check(node.next):
            return False
        if node.val != front.val:
            return False
        front = front.next
        return True
    return check(head)
```

- **LC 143（Reorder List）** —— 變化點：是組合而不是新的遞迴。用快慢指標從中間切開，用 **LC 206** 反轉後半段，再把兩半交錯合併（**LC 21** 的合併步驟）。

**常見的 LeetCode 題目**
- LC 206: Reverse Linked List（基本模板）
- LC 24: Swap Nodes in Pairs
- LC 25: Reverse Nodes in k-Group（LC 24 的困難版）
- LC 203: Remove Linked List Elements
- LC 234: Palindrome Linked List
- LC 143: Reorder List
- LC 21: Merge Two Sorted Lists（見 4-3）

---

### 5-2) 遞迴下降剖析 —— LC 394 / 224 ⭐⭐⭐⭐⭐

**定義**：剖析巢狀字串時，**每條文法規則寫一個函式**，所有函式共用同一個**游標**（索引）。每個函式剛好吃掉自己那條規則的字元，然後把游標停在它們的後面。巢狀括號 = 遞迴；多層優先序 = **相互遞迴**（`expr` 呼叫 `term`，`term` 呼叫 `expr`）。

**時間**：O(n) 個 token（文法會展開時是 O(output)，例如 LC 394）。
**空間**：O(巢狀深度)。

**模式**：
```text
1. Keep the cursor OUTSIDE the recursion (field in Java, `nonlocal` in Python).
   Passing `int i` by value does not work - the caller must see how far the callee ate.
2. One function per grammar rule; each one:
     - reads the tokens of its own rule
     - recurses at the nesting point ('(' , '[' , a sub-expression)
     - returns its value with the cursor sitting on the NEXT unconsumed char
3. Be explicit about who consumes the closing delimiter (pick a convention, keep it).
```

**關鍵洞見**：最常見的 bug 就是把游標寫成*參數*而不是共用狀態 —— 這樣父層會把子層已經吃掉的字元再剖析一次。先把文法用 BNF 寫下來；程式碼只是它的機械式翻譯。

**範例 —— LC 394: Decode String**（文法：`str := (char | int '[' str ']')*`）

```java
// java
// LC 394 - Decode String
// IDEA: recursive descent with a shared cursor; each call handles ONE bracket level
// time = O(total output length), space = O(nesting depth)
private int i = 0;

public String decodeString(String s) {
    i = 0;
    return parse(s);
}

// parses until end-of-string or the ']' that closes the current level
private String parse(String s) {
    StringBuilder sb = new StringBuilder();
    while (i < s.length() && s.charAt(i) != ']') {
        char c = s.charAt(i);
        if (Character.isDigit(c)) {
            int k = 0;
            while (Character.isDigit(s.charAt(i))) {   // multi-digit repeat count
                k = k * 10 + (s.charAt(i) - '0');
                i++;
            }
            i++;                        // consume '['
            String inner = parse(s);    // recurse: body of this bracket
            i++;                        // consume ']'
            for (int t = 0; t < k; t++) sb.append(inner);
        } else {
            sb.append(c);
            i++;
        }
    }
    return sb.toString();
}
```

```python
# python
# LC 394 - Decode String
# IDEA: recursive descent with a shared cursor; each call handles ONE bracket level
# time = O(total output length), space = O(nesting depth)
def decodeString(s):
    i = 0

    def parse():
        nonlocal i
        out = []
        while i < len(s) and s[i] != "]":
            if s[i].isdigit():
                k = 0
                while s[i].isdigit():          # multi-digit repeat count
                    k = k * 10 + int(s[i]); i += 1
                i += 1                          # consume '['
                inner = parse()                 # recurse: body of this bracket
                i += 1                          # consume ']'
                out.append(inner * k)
            else:
                out.append(s[i]); i += 1
        return "".join(out)

    return parse()
```

**範例 —— LC 224: Basic Calculator**（兩層文法上的相互遞迴）

```text
expr := term (('+' | '-') term)*
term := number | '(' expr ')' | '-' term      # unary minus, e.g. "-(3+4)"
```

```java
// java
// LC 224 - Basic Calculator
// IDEA: one function per grammar rule; expr <-> term is mutual recursion, '(' re-enters expr
// time = O(n), space = O(paren depth)
private int p = 0;

public int calculate(String s) {
    p = 0;
    return expr(s);
}

private int expr(String s) {           // left-to-right +/- chain
    int res = term(s);
    while (true) {
        skipSpace(s);
        if (p >= s.length()) break;
        char c = s.charAt(p);
        if (c == '+')      { p++; res += term(s); }
        else if (c == '-') { p++; res -= term(s); }
        else break;                    // hit ')' -> let the caller consume it
    }
    return res;
}

private int term(String s) {           // number | '(' expr ')' | unary minus
    skipSpace(s);
    char c = s.charAt(p);
    if (c == '(') {
        p++;                           // consume '('
        int v = expr(s);               // mutual recursion
        skipSpace(s);
        p++;                           // consume ')'
        return v;
    }
    if (c == '-') { p++; return -term(s); }
    int v = 0;
    while (p < s.length() && Character.isDigit(s.charAt(p))) {
        v = v * 10 + (s.charAt(p) - '0');
        p++;
    }
    return v;
}

private void skipSpace(String s) {
    while (p < s.length() && s.charAt(p) == ' ') p++;
}
```

```python
# python
# LC 224 - Basic Calculator
# IDEA: one function per grammar rule; expr <-> term is mutual recursion, '(' re-enters expr
# time = O(n), space = O(paren depth)
def calculate(s):
    i = 0

    def skip_space():
        nonlocal i
        while i < len(s) and s[i] == " ":
            i += 1

    def expr():                       # term (('+'|'-') term)*
        nonlocal i
        res = term()
        while True:
            skip_space()
            if i >= len(s):
                break
            if s[i] == "+":
                i += 1; res += term()
            elif s[i] == "-":
                i += 1; res -= term()
            else:
                break                 # ')' -> caller consumes it
        return res

    def term():                       # number | '(' expr ')' | '-' term
        nonlocal i
        skip_space()
        if s[i] == "(":
            i += 1
            v = expr()
            skip_space()
            i += 1
            return v
        if s[i] == "-":
            i += 1
            return -term()
        v = 0
        while i < len(s) and s[i].isdigit():
            v = v * 10 + int(s[i]); i += 1
        return v

    return expr()
```

**各種變形**
- **LC 1106（Parsing A Boolean Expression）** —— 同一個游標模板；文法是 `expr := 't' | 'f' | '!(' expr ')' | ('&'|'|') '(' expr (',' expr)* ')'`，所以遞迴要收集一*串*子結果，再用 `and`／`or` 摺起來。
- **LC 736（Parse Lisp Expression）** —— 同一個模板再加一個**作用域堆疊**：`let` 會綁定變數，所以每次遞迴呼叫都要帶著（或 push/pop）一份環境表。
- **LC 770（Basic Calculator IV）** —— 同一個模板，但每個子結果是一個*多項式*（排序後的變數 tuple → 係數的對照表）而不是整數。

**常見的 LeetCode 題目**
- LC 394: Decode String
- LC 224: Basic Calculator
- LC 1106: Parsing A Boolean Expression
- LC 736: Parse Lisp Expression
- LC 770: Basic Calculator IV

---

### 5-3) 折半遞迴（快速冪） —— LC 50 ⭐⭐⭐⭐

**定義**：當參數是按**倍率**縮小（通常是 /2）而不是減 1 時，遞迴深度就從 O(n) 掉到 O(log n)。最經典的例子就是二進位快速冪：

```text
x^n = (x^(n/2))^2            if n is even
x^n = (x^(n/2))^2 * x        if n is odd
x^0 = 1                      base case
```

**時間**：O(log n)。**空間**：O(log n) 的呼叫堆疊。

**關鍵洞見**：`half` 只算**一次**，然後把它平方。寫成 `fastPow(x, n/2) * fastPow(x, n/2)` 看起來一樣，卻讓遞迴樹重新展開成 O(n)。

**範例 —— LC 50: Pow(x, n)**

```java
// java
// LC 50 - Pow(x, n)
// IDEA: binary exponentiation - halve the exponent every call, square the result
// time = O(log n), space = O(log n)
public double myPow(double x, int n) {
    long N = n;                     // widen: -Integer.MIN_VALUE overflows an int
    if (N < 0) {
        x = 1 / x;
        N = -N;
    }
    return fastPow(x, N);
}

private double fastPow(double x, long n) {
    if (n == 0) return 1.0;
    double half = fastPow(x, n / 2);   // compute ONCE
    return (n % 2 == 0) ? half * half : half * half * x;
}
```

```python
# python
# LC 50 - Pow(x, n)
# IDEA: binary exponentiation - halve the exponent every call, square the result
# time = O(log n), space = O(log n)
def myPow(x, n):
    if n < 0:
        x, n = 1 / x, -n

    def fast(x, n):
        if n == 0:
            return 1.0
        half = fast(x, n // 2)          # compute ONCE
        return half * half if n % 2 == 0 else half * half * x

    return fast(x, n)
```

**各種變形**

- **LC 1922（Count Good Numbers）** —— 變化點：同樣的遞迴，但要**取模**，而且 `n` 可以到 10^15，所以非 O(log n) 不可。偶數索引有 5 種選擇（0,2,4,6,8），奇數索引有 4 種（質數 2,3,5,7）→ `5^ceil(n/2) * 4^floor(n/2) mod 1e9+7`。

```java
// java
// LC 1922 - Count Good Numbers
// IDEA: modular fast power; ceil(n/2) even slots x 5 choices, floor(n/2) odd slots x 4
// time = O(log n), space = O(log n)
private static final int MOD = 1_000_000_007;

public int countGoodNumbers(long n) {
    return (int) (powMod(5, (n + 1) / 2) * powMod(4, n / 2) % MOD);
}

private long powMod(long b, long e) {
    if (e == 0) return 1;
    long half = powMod(b, e / 2);
    long sq = half * half % MOD;
    return (e % 2 == 0) ? sq : sq * b % MOD;
}
```

```python
# python
# LC 1922 - Count Good Numbers
# IDEA: modular fast power; ceil(n/2) even slots x 5 choices, floor(n/2) odd slots x 4
# time = O(log n), space = O(log n)
def countGoodNumbers(n):
    MOD = 10 ** 9 + 7

    def pow_mod(b, e):
        if e == 0:
            return 1
        half = pow_mod(b, e // 2)
        sq = half * half % MOD
        return sq if e % 2 == 0 else sq * b % MOD

    return pow_mod(5, (n + 1) // 2) * pow_mod(4, n // 2) % MOD
```

- **LC 231 / 326 / 342（Power of Two / Three / Four）** —— 變化點：方向*反過來* —— 把 `n` 一路除到 1，而不是往上乘出來。一個模板涵蓋三題（換底數就好）；記得擋掉 `n < 1`，否則遞迴永遠不會結束。

```java
// java
// LC 326 - Power of Three (same shape for LC 231 base 2, LC 342 base 4)
// IDEA: peel one factor per call; n is a power of b iff it divides down to exactly 1
// time = O(log n), space = O(log n)
public boolean isPowerOfThree(int n) {
    if (n < 1) return false;   // 0 and negatives are never powers
    if (n == 1) return true;   // 3^0
    return n % 3 == 0 && isPowerOfThree(n / 3);
}
```

```python
# python
# LC 326 - Power of Three (same shape for LC 231 base 2, LC 342 base 4)
# IDEA: peel one factor per call; n is a power of b iff it divides down to exactly 1
# time = O(log n), space = O(log n)
def isPowerOfThree(n):
    if n < 1:
        return False
    if n == 1:
        return True
    return n % 3 == 0 and isPowerOfThree(n // 3)
```

**常見的 LeetCode 題目**
- LC 50: Pow(x, n)
- LC 1922: Count Good Numbers（模數快速冪）
- LC 231: Power of Two
- LC 326: Power of Three
- LC 342: Power of Four

---

### 5-4) 遞迴關係化簡（沒有樹，也沒有搜尋） —— LC 779 / 1823 ⭐⭐⭐⭐

**定義**：有些問題**根本沒有資料結構可以走訪**。整個解就是一行把 `f(n)` 和 `f(n-1)`（或 `f(n/2)`、`f(n/1000)`……）連起來的遞迴關係式。面試考的是你能不能*推導*出那個關係；推出來之後程式碼只有 3 行。

**怎麼推導**：
```text
1. Write out the answer for n = 1, 2, 3, 4 by hand.
2. Ask: "given the answer for n-1, what single operation produces the answer for n?"
   - index mapping   -> which position in row n-1 does position k in row n come from?
   - shift/rotation  -> after one round, what does the smaller problem's answer become?
3. Base case = the smallest n you can answer without thinking.
```

**時間**：O(遞迴的深度)。**空間**：O(深度) 的呼叫堆疊（尾遞迴形狀可以輕鬆改成 O(1) 的迴圈）。

**範例 —— LC 779: K-th Symbol in Grammar**

第 `n` 列是把第 `n-1` 列的每個 `0` 換成 `01`、每個 `1` 換成 `10`。所以第 `n` 列的位置 `k` 來自第 `n-1` 列的位置 `(k+1)/2`：**`k` 為奇數就複製父元素，`k` 為偶數就翻轉它。**

```java
// java
// LC 779 - K-th Symbol in Grammar
// IDEA: position k of row n comes from position (k+1)/2 of row n-1; even k flips the bit
// time = O(n), space = O(n)
public int kthGrammar(int n, int k) {
    if (n == 1) return 0;                            // row 1 is just "0"
    int parent = kthGrammar(n - 1, (k + 1) / 2);
    return (k % 2 == 1) ? parent : 1 - parent;       // odd = copy, even = flip
}
```

```python
# python
# LC 779 - K-th Symbol in Grammar
# IDEA: position k of row n comes from position (k+1)//2 of row n-1; even k flips the bit
# time = O(n), space = O(n)
def kthGrammar(n, k):
    if n == 1:
        return 0
    parent = kthGrammar(n - 1, (k + 1) // 2)
    return parent if k % 2 == 1 else 1 - parent
```

**範例 —— LC 1823: Find the Winner of the Circular Game**（Josephus 遞迴式）

第一次淘汰之後剩下 `n-1` 個人，而且計數要從往前 `k` 個位置重新開始 —— 所以小問題的答案只要平移 `k`（再對 `n` 取模）就好。

```java
// java
// LC 1823 - Find the Winner of the Circular Game
// IDEA: Josephus recurrence (0-indexed): f(1) = 0, f(n) = (f(n-1) + k) % n
// time = O(n), space = O(n)
public int findTheWinner(int n, int k) {
    return winner(n, k) + 1;              // convert 0-indexed seat to 1-indexed
}

private int winner(int n, int k) {
    if (n == 1) return 0;
    return (winner(n - 1, k) + k) % n;    // shift the smaller answer by k
}
```

```python
# python
# LC 1823 - Find the Winner of the Circular Game
# IDEA: Josephus recurrence (0-indexed): f(1) = 0, f(n) = (f(n-1) + k) % n
# time = O(n), space = O(n)
def findTheWinner(n, k):
    def winner(m):
        if m == 1:
            return 0
        return (winner(m - 1) + k) % m
    return winner(n) + 1
```

**變形 —— 按數量級拆解，而不是一次減 1：LC 273（Integer to English Words）**

變化點：這個遞迴式每次剝掉**最大的量級單位**（Billion／Million／Thousand／Hundred），再對餘下的部分遞迴，所以深度是 O(log10 n) 而不是 O(n)。

```java
// java
// LC 273 - Integer to English Words
// IDEA: split off the biggest unit, name it, recurse on the remainder; helper always
//       returns a space-terminated chunk so concatenation is uniform
// time = O(log10(n)), space = O(log10(n))
private static final String[] BELOW_20 = {"", "One", "Two", "Three", "Four", "Five", "Six",
        "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",
        "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
private static final String[] TENS = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty",
        "Sixty", "Seventy", "Eighty", "Ninety"};

public String numberToWords(int num) {
    if (num == 0) return "Zero";          // only place "Zero" is ever printed
    return words(num).trim();
}

private String words(int num) {
    if (num == 0)         return "";
    if (num < 20)         return BELOW_20[num] + " ";
    if (num < 100)        return TENS[num / 10] + " " + words(num % 10);
    if (num < 1000)       return BELOW_20[num / 100] + " Hundred " + words(num % 100);
    if (num < 1000000)    return words(num / 1000) + "Thousand " + words(num % 1000);
    if (num < 1000000000) return words(num / 1000000) + "Million " + words(num % 1000000);
    return words(num / 1000000000) + "Billion " + words(num % 1000000000);
}
```

```python
# python
# LC 273 - Integer to English Words
# IDEA: split off the biggest unit, name it, recurse on the remainder; helper always
#       returns a space-terminated chunk so concatenation is uniform
# time = O(log10(n)), space = O(log10(n))
BELOW_20 = ["", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"]
TENS = ["", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty",
        "Ninety"]

def numberToWords(num):
    if num == 0:
        return "Zero"

    def words(n):
        if n == 0:
            return ""
        if n < 20:
            return BELOW_20[n] + " "
        if n < 100:
            return TENS[n // 10] + " " + words(n % 10)
        if n < 1000:
            return BELOW_20[n // 100] + " Hundred " + words(n % 100)
        if n < 10 ** 6:
            return words(n // 1000) + "Thousand " + words(n % 1000)
        if n < 10 ** 9:
            return words(n // 10 ** 6) + "Million " + words(n % 10 ** 6)
        return words(n // 10 ** 9) + "Billion " + words(n % 10 ** 9)

    return words(num).strip()
```

**常見的 LeetCode 題目**
- LC 779: K-th Symbol in Grammar（索引對映）
- LC 1823: Find the Winner of the Circular Game（Josephus）
- LC 390: Elimination Game（對反轉後的半數問題遞迴）
- LC 273: Integer to English Words（按數量級拆解）
- LC 233: Number of Digit One（逐位遞迴）
- LC 509: Fibonacci Number（教科書等級的遞迴式 —— 記得加記憶化）

---

### 5-5) 其他掛著 Recursion 標籤的經典題

這些題目都落在上面已經談過的模式裡，列出來只是求完整：

| LC | 題目 | 屬於 |
|----|---------|------|
| 10 | Regular Expression Matching | 對 `(i, j)` 做由上而下的遞迴 + 記憶化 —— 見 `recursion_to_dp.md` |
| 44 | Wildcard Matching | 跟 LC 10 一樣，只是 `*` 配對的是一整段，而不是「前一個字元重複 0 次以上」 |
| 486 | Predict the Winner | 對 `(l, r)` 做 minimax 遞迴 + 記憶化 —— 見 `recursion_to_dp.md` |

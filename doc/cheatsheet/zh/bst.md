# BST (Binary Search Tree)（二元搜尋樹）

> **範圍** — 只談有序的樹 —— `left < root < right` 這條不變式換來了什麼（O(log n) 搜尋、中序即排序、依範圍剪枝、順序統計）。

> **另見**：[bst_examples.md](./bst_examples.md) — 這些模板對應的 LC 完整解法庫，外加樹的路徑和題型家族；[bst_advanced.md](./bst_advanced.md) — 順序統計（rank）查詢、O(h) 的延遲式迭代器、修復壞掉的 BST，以及各種建構變形的目錄；[binary_tree.md](./binary_tree.md) — 無序的二元樹；[tree.md](./tree.md) — 樹的通用概念；[segment_tree.md](./segment_tree.md) — 在陣列（而非樹）上做區間查詢。

## LeetCode 題目清單

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)

## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大  |
| -------------- | -------- | -------- | -------- | -------- |
| BST（平均）  | O(log n) | O(log n) | O(log n) | O(log n) |

> 表中是平均情況（樹大致平衡）。**最差情況（不平衡／退化成鏈）：所有操作都是 O(n)。** 最小／最大 = 最左／最右節點，也就是 O(h)。完整走訪永遠是 **O(n)**。空間是儲存的 **O(n)** 加上遞迴的 **O(h)**。

## 總覽
**二元搜尋樹（BST）** 是一種二元樹，每個節點都遵守排序性質：左子 < 父 < 右子。有了這條性質，搜尋、插入、刪除都能做得很有效率。

### 關鍵性質
- **複雜度**：見上方的[時間複雜度](#time-complexity)表
- **核心性質**：所有節點都滿足 `left < root < right`
- **中序走訪**：輸出遞增排序的序列
- **什麼時候用**：有序資料的操作、區間查詢、順序統計

### 參考資料
- [BST Visualizer](https://www.cs.usfca.edu/~galles/visualization/BST.html)
- [fucking-algorithm - BST pt.1](https://labuladong.github.io/algo/2/21/43/)
- [fucking-algorithm - BST pt.2](https://labuladong.github.io/algo/2/21/44/)
- [fucking-algorithm - BST pt.3](https://labuladong.github.io/algo/2/21/42/)

```java
// below will print BST elements in ascending ordering
// java
void traverse(TreeNode root) {
    if (root == null) return;
    traverse(root.left);
    // in-order traversal
    print(root.val);
    traverse(root.right);
}
```

### 樹節點的基本操作

所有 BST 模板都建立在這種樸素遞迴之上。下面兩段都沒有用到排序性質 ——
當 `left < root < right` 幫不上忙時，你就會退回這個形狀。

#### **改動每個節點 —— 樸素的前序遞迴**
```java
// java
void plusOne(TreeNode root){
    if (root == null){
        return;
    }
    root.val += 1;
    plusOne(root.left);
    plusOne(root.right);
}
```

```python
# python
class TreeNode(object):
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

root = TreeNode(0)
root.left = TreeNode(1)
root.right =  TreeNode(2)

print (root.val)
print (root.left.val)
print (root.right.val)

print("==============")

def add_one(root):
    if not root:
        return
    root.val += 1
    add_one(root.left)
    add_one(root.right)

add_one(root)
print (root.val)
print (root.left.val)
print (root.right.val)
```

#### **判斷兩棵樹是否相同 —— LC 100**
```java
// java
boolean isSameTree(TreeNode root1, TreeNode root2){
    // if all null, then same
    if (root1 == null && root2 == null){
        return true;
    }
    if (root1 == null || root2 == null){
        return false;
    }
    if (root1.val != root2.val){
        return false;
    }

    return isSameTree(root1.left, root2.left) && isSameTree(root1.right, root2.right);
}
```

> LC 100 的 Python 版放在 [tree_examples.md](./tree_examples.md)。

## 題型分類

### **模式 1：BST 搜尋與驗證**
- **說明**：找元素，或驗證 BST 性質
- **辨識關鍵字**：「search」、「find」、「validate」、「is valid BST」
- **例題**：LC 98、LC 700、LC 270、LC 285
- **模板**：用搜尋模板搭配 BST 性質

### **模式 2：BST 插入與刪除**
- **說明**：改動 BST 結構，同時維持性質
- **辨識關鍵字**：「insert」、「delete」、「remove」、「add node」
- **例題**：LC 450、LC 701、LC 669
- **模板**：用修改模板

### **模式 3：BST 走訪與轉換**
- **說明**：利用中序性質做排序／轉換
- **辨識關鍵字**：「Kth smallest」、「convert」、「flatten」、「sorted order」
- **例題**：LC 230、LC 173、LC 426、LC 538
- **模板**：用中序模板

### **模式 4：BST 建構**
- **說明**：從各種輸入建出 BST，或重建／平衡既有的 BST
- **辨識關鍵字**：「construct」、「build」、「generate」、「serialize」、「balance」
- **例題**：LC 108、LC 109、LC 95、LC 96、LC 449、LC 1008、LC 1382
- **模板**：用建構模板

### **模式 5：BST 性質與最佳化**
- **說明**：在 BST 中找最佳值或某種性質
- **辨識關鍵字**：「closest」、「LCA」、「range」、「distance」
- **例題**：LC 235、LC 530、LC 783、LC 776
- **模板**：用性質模板

### **模式 6：路徑問題**
- **說明**：牽涉根到葉、或節點到節點路徑的題目
- **辨識關鍵字**：「path sum」、「root to leaf」、「maximum path」、「consecutive sequence」
- **例題**：LC 112、LC 113、LC 257、LC 124、LC 129、LC 298、LC 437
- **模板**：DFS 搭配路徑追蹤、回溯或全域狀態

## 模板與演算法

### 模板比較表
| 模板類型 | 適用情境 | 關鍵操作 | 時間 | 空間 | 什麼時候用 |
|---------------|----------|---------------|------|-------|-------------|
| **搜尋模板** | 找值 | 二分搜尋 | O(log n) | O(1)/O(h) | 查詢某個值 |
| **插入模板** | 加節點 | 找位置 + 插入 | O(log n) | O(1)/O(h) | 加入新值 |
| **刪除模板** | 移除節點 | 找到 + 重接 | O(log n) | O(h) | 刪掉某個值 |
| **中序模板** | 有序操作 | 左-根-右 | O(n) | O(h) | 第 k 個元素、區間 |
| **建構模板** | 建 BST | 分治法 | O(n) | O(n) | 從陣列建樹 |
| **路徑模板** | 根到葉路徑 | DFS + 追蹤 | O(n) | O(h) | 路徑和、序列 |

> **其他模板在哪裡。** 模板編號在整個 BST 家族中是連貫的，所以下面的跳號不是筆誤：
>
> - **模板 5b**（延遲式 BST 迭代器）、**模板 8**（修復壞掉的 BST）、**模板 9**
>   （順序統計／rank 查詢）以及 **模板 3c / 4b / 6b**（拆離、邊界傳遞、建構變形）
>   → [bst_advanced.md](./bst_advanced.md)
> - **模板 7**（根到葉與節點到節點的路徑問題 —— 不限於 BST）以及完整的
>   LC 解法庫 → [bst_examples.md](./bst_examples.md)

### 模板 1：BST 搜尋
```python
def search_bst(root, val):
    """
    Search for a value in BST
    Time: O(log n) average, O(n) worst
    """
    if not root or root.val == val:
        return root
    
    if val < root.val:
        return search_bst(root.left, val)
    return search_bst(root.right, val)

# Iterative version
def search_bst_iterative(root, val):
    while root and root.val != val:
        root = root.left if val < root.val else root.right
    return root
```

**Java** —— 同一個名字下放了兩種寫法（它們沒辦法同時編譯，挑一種用）。
`V1` 是通用的整棵樹掃描，刻意留著：它同樣回傳正確答案但要 O(n)，
而它跟 `V2` 那條 O(h) 下降路徑的對比，正是 BST 性質的價值所在。
```java
// java
// V1 : general (for tree and BST)
boolean isInBST(TreeNode root, int target){
    if (root == null) return false;
    if (root.val == target) return true;

    return isInBST(root.left, target) || isInBST(root.right, target);
}

// V2 : optimization for BST
boolean isInBST(TreeNode root, int target){
    if (root == null) return false;
    if (root.val == target) return true;

    // optimize here
    if (root.val < target){
        return isInBST(root.right, target);
    }
    // root.val > target
    return isInBST(root.left, target);
}

```

### 模板 2：BST 插入
```python
def insert_bst(root, val):
    """
    Insert value into BST
    Always inserts as a leaf node
    """
    if not root:
        return TreeNode(val)
    
    if val < root.val:
        root.left = insert_bst(root.left, val)
    elif val > root.val:
        root.right = insert_bst(root.right, val)
    # If val == root.val, typically don't insert duplicates
    
    return root
```

**Java**：
```java
// java
TreeNode insertIntoBST(TreeNode root, int val){
    // if null root, then just find a space and insert new value
    if (root == null) return new TreeNode(val);
    // if already exist, no need to insert, return directly
    if (root.val == val) return root;

    if (root.val < val){
        root.right = insertIntoBST(root.right, val);
    }
    if (root.val > val){
        root.left = insertIntoBST(root.left, val);
    }
    return root;
}
```

### 模板 3：BST 刪除
```python
def delete_bst(root, key):
    """
    Delete a node from BST
    Three cases: no child, one child, two children
    """
    if not root:
        return None
    
    if key < root.val:
        root.left = delete_bst(root.left, key)
    elif key > root.val:
        root.right = delete_bst(root.right, key)
    else:
        # Node found - handle 3 cases
        if not root.left:  # No left child or leaf
            return root.right
        if not root.right:  # No right child
            return root.left
        
        # Two children: find inorder successor
        min_node = find_min(root.right)
        root.val = min_node.val
        root.right = delete_bst(root.right, min_node.val)
    
    return root

def find_min(node):
    """Find minimum value in BST (leftmost node)"""
    while node.left:
        node = node.left
    return node
```

**Java —— LC 450**。下面第一段是**虛擬碼骨架**（只有大綱，`// delete`
分支是故意留空的），用來呈現遞迴的形狀；接在後面的才是可以跑的實作，
中間會把三種刪除情況、以及改用「左子樹最大值」交換的替代作法都寫清楚。
```java
// java

// pseudo java code
TreeNode deleteNode(TreeNode root, int key){
    if (root.val == key){
        // delete
    }
    else if (root.val > key){
        // to left sub tree
        root.left = deleteNode(root.left, key);
    }
    else if (root.val < key){
        // to right sub tree
        root.right = deleteNode(root.right, key);
    }
    return root;
}

/** 
 *   NOTE : 3 cases (algorithm book (labu) p.246)
 * 
 *   1) the value (to-delete value) is at the bottom (no sub left/right tree) -> delete directly
 *   
 *   2) there is only one left/right tree -> replace value with the sub-tree, then delete sub-tree
 * 
 *   3) there is BOTH left/right tree
 *      -> approach 3-1)  find the MIN right sub-tree and replace with value, then delete MIN right sub-tree
 *      -> approach 3-2)  find the MAX left sub-tree and replace with value, then delete MAX left sub-tree
 */

// java code
TreeNode deleteNode(TreeNode root, int key){
    if (root.val == key){
        // case 1) & case 2)
        if (root.left == null) return root.right;
        if (root.right == null) return root.left;
        
        // case 3)
        TreeNode minNode = getMin(root.right);
        root.val = minNode.val;
        root.right = deleteNode(root.right, minNode.val);
    }
    else if (root.val > key){
        // to left sub tree
        root.left = deleteNode(root.left, key);
    }
    else if (root.val < key){
        // to right sub tree
        root.right = deleteNode(root.right, key);
    }
    return root;
}

// help func
TreeNode getMin(TreeNode node){
    // min node is on the left of BST
    while (node.left != null) node = node.left;
    return node;
}
```

### 模板 3b：修剪 BST（依範圍剪枝）⭐

#### **核心想法**

```text
Goal: keep ONLY nodes whose value lies in [low, high],
      WITHOUT changing the relative structure of the survivors.

Key insight — exploit BST property (left < root < right):
  - If root.val < low  → the ENTIRE left subtree is also < low.
                         Discard root AND its left subtree.
                         The answer must come from the RIGHT subtree.
  - If root.val > high → the ENTIRE right subtree is also > high.
                         Discard root AND its right subtree.
                         The answer must come from the LEFT subtree.
  - If low <= root.val <= high → keep root, recursively trim BOTH children.

Why return the recursive call (not None)?
  When a node is out of range we don't just delete it — its valid
  descendants must be "promoted" to take its place. Returning
  trimBST(child, ...) reconnects the next valid node to the parent.
```

```text
Visual (low=1, high=3):

        3                 3
       / \               /
      0   4    trim →   2
       \               /
        2             1
       /
      1

  - root 3 in range → keep, trim children
  - left child 0 < low(1) → drop 0 AND its (empty) left, recurse right → 2
  - 2 in range → keep, trim children
  - 1 in range → keep (leaf)
  - right child 4 > high(3) → drop 4 AND its (empty) right → None
```

#### **模式**

```python
# LC 669 - Trim a Binary Search Tree
# IDEA: BST PROPERTY + DFS (post-order reconnect)
# Time: O(n), Space: O(h)
class Solution(object):
    def trimBST(self, root, low, high):
        # Base case: empty tree needs no trimming
        if not root:
            return None

        # root too small → left subtree all < low too.
        # Drop root + left, answer is in the trimmed right subtree.
        if root.val < low:
            return self.trimBST(root.right, low, high)

        # root too large → right subtree all > high too.
        # Drop root + right, answer is in the trimmed left subtree.
        if root.val > high:
            return self.trimBST(root.left, low, high)

        # root in range → keep it, trim & reconnect both children.
        root.left = self.trimBST(root.left, low, high)
        root.right = self.trimBST(root.right, low, high)
        return root
```

```java
// java - LC 669
// Time: O(n), Space: O(h)
public TreeNode trimBST(TreeNode root, int low, int high) {
    if (root == null) return null;

    // root too small → promote trimmed right subtree
    if (root.val < low) return trimBST(root.right, low, high);

    // root too large → promote trimmed left subtree
    if (root.val > high) return trimBST(root.left, low, high);

    // in range → keep node, trim both children
    root.left = trimBST(root.left, low, high);
    root.right = trimBST(root.right, low, high);
    return root;
}
```

**🚫 常見錯誤**：對超出範圍的節點直接回傳 `None`，會連它**合法的子孫**一起刪掉。
你必須回傳*修剪後還活著的那棵子樹*，下一個合法節點才會被重新接回父節點。

```python
# BAD: drops valid descendants of an out-of-range node
if root.val < low or root.val > high:
    return None   # loses the in-range nodes hanging below!

# GOOD: promote the side that may still contain valid nodes
if root.val < low:  return self.trimBST(root.right, low, high)
if root.val > high: return self.trimBST(root.left,  low, high)
```

#### **類似的 LeetCode 題目**
| 題目 | LC # | 難度 | 與 Trim 的關係 |
|---------|------|------------|------------------|
| Trim a Binary Search Tree | 669 | Medium | 核心題 —— 剪掉 `[low, high]` 之外的節點 |
| Delete Node in a BST | 450 | Medium | 同樣的「遞迴 + 用回傳值重接」模式 |
| Range Sum of BST | 938 | Easy | 同樣的 BST 剪枝邏輯，但是求和而不是重組結構 |
| Split BST | 776 | Medium | 依值拆成兩棵樹（修剪的鏡像版） |
| Convert Sorted Array to BST | 108 | Easy | 遞迴建構並回傳子樹根（同樣的重接慣用法） |
| Insert into a BST | 701 | Medium | 遞迴 + 回傳重接後的子節點指標 |

#### 比較：Trim vs Split vs Delete

| 操作 | LC # | 會刪節點嗎？ | 回傳值 | 關鍵差異 |
|-----------|------|----------------|--------------|----------------|
| **Trim** | 669 | 會（範圍外的） | 單一 `TreeNode` | 保留 [L,R] 內的節點，其餘丟掉 |
| **Split** | 776 | 不會 | `TreeNode[2]` | 拆成 2 棵樹，**所有**節點都留著 |
| **Delete** | 450 | 會（1 個） | 單一 `TreeNode` | 剛好移除 1 個指定節點 |

#### **變形：求和而非重組結構 —— LC 938**

跟 `trimBST` 一樣的三路剪枝；只有合併那一步不同。
```python
def range_sum_bst(root, low, high):
    """Prune branches that can't contain values in range"""
    if not root:
        return 0
    
    # Prune left subtree if root is already too small
    if root.val < low:
        return range_sum_bst(root.right, low, high)
    
    # Prune right subtree if root is already too large
    if root.val > high:
        return range_sum_bst(root.left, low, high)
    
    # Root is in range, include it and check both subtrees
    return (root.val + 
            range_sum_bst(root.left, low, high) +
            range_sum_bst(root.right, low, high))
```

### 模板 4：BST 驗證
```python
def validate_bst(root):
    """
    Validate if tree is a valid BST
    Uses min/max bounds approach
    """
    def validate(node, min_val, max_val):
        if not node:
            return True
        
        if node.val <= min_val or node.val >= max_val:
            return False
        
        return (validate(node.left, min_val, node.val) and
                validate(node.right, node.val, max_val))
    
    return validate(root, float('-inf'), float('inf'))
```

**Java** —— 用節點指標取代 `float('inf')` 這種哨兵值，這樣 `Integer.MIN_VALUE` 仍然是合法的節點值：
```java
// java
boolean isValidBST(TreeNode root){
    return isValidBST(root, null, null);
}

// help func
boolean isValidBST(TreeNode root, TreeNode min, TreeNode max){
    if (root == null){
        return true;
    }
    if (min != null && root.val <= min.val){
        return false;
    }
    if (max != null && root.val >= max.val){
        return false;
    }
    return isValidBST(root.left, min, root) && isValidBST(root.right, root, max);
}
```

### 模板 5：BST 中序操作
```python
def kth_smallest(root, k):
    """
    Find kth smallest element using inorder property
    Inorder traversal of BST gives sorted order
    """
    def inorder(node):
        if not node:
            return []
        return inorder(node.left) + [node.val] + inorder(node.right)
    
    return inorder(root)[k-1]

# Optimized with early stopping
def kth_smallest_optimized(root, k):
    stack = []
    while True:
        while root:
            stack.append(root)
            root = root.left
        root = stack.pop()
        k -= 1
        if k == 0:
            return root.val
        root = root.right
```

#### **模式：反向中序做遞減**

右 → 根 → 左的順序會依遞減走訪各個值，所以維護一個累加和，就能把每個節點
換成「所有比它大的值的總和」（LC 538 / LC 1038）。以下是草稿 —— `self.sum`
假設這段是寫在某個 `Solution` 類別裡：

```python
def convert_to_greater_tree(root):
    """Process nodes from largest to smallest"""
    self.sum = 0
    def reverse_inorder(node):
        if not node:
            return
        reverse_inorder(node.right)
        self.sum += node.val
        node.val = self.sum
        reverse_inorder(node.left)
    reverse_inorder(root)
    return root
```

### 模板 6：BST 建構

#### **模式總覽**
- **說明**：從各種輸入（陣列、串列、走訪序列）建出 BST
- **辨識關鍵字**：「construct」、「build」、「generate」、「serialize」、「from preorder/inorder」
- **核心概念**：用遞迴分治搭配 BST 性質
- **時間複雜度**：多數建構是 O(n)，少數是 O(n log n)
- **空間複雜度**：樹本身 O(n) + 遞迴堆疊 O(h)

#### **核心建構模式**

##### **模式 6.1：從有序陣列建**（LC 108）
```python
def sorted_array_to_bst(nums):
    """
    Convert sorted array to balanced BST
    Uses binary search approach - pick middle as root
    Time: O(n), Space: O(n)
    """
    if not nums:
        return None

    mid = len(nums) // 2
    root = TreeNode(nums[mid])
    root.left = sorted_array_to_bst(nums[:mid])
    root.right = sorted_array_to_bst(nums[mid+1:])

    return root

# Optimized version with index (no array slicing)
def sorted_array_to_bst_optimized(nums):
    def build(left, right):
        if left > right:
            return None

        mid = (left + right) // 2
        root = TreeNode(nums[mid])
        root.left = build(left, mid - 1)
        root.right = build(mid + 1, right)
        return root

    return build(0, len(nums) - 1)
```

#### **Java 實作：從有序陣列建（LC 108）**
```java
// Pattern 6.1: From Sorted Array (LC 108)
public TreeNode sortedArrayToBST(int[] nums) {
    return buildBST(nums, 0, nums.length - 1);
}

private TreeNode buildBST(int[] nums, int left, int right) {
    if (left > right) return null;

    int mid = left + (right - left) / 2;
    TreeNode root = new TreeNode(nums[mid]);
    root.left = buildBST(nums, left, mid - 1);
    root.right = buildBST(nums, mid + 1, right);
    return root;
}
```

#### **其他建構輸入**

`模式 6.2`（有序鏈結串列，LC 109）、`6.3`（前序，LC 1008）、`6.4`（平衡既有的
BST，LC 1382）、`6.5`（生成所有相異 BST，LC 95）與 `6.6`（計算數量，LC 96）
統一收在 [bst_advanced.md](./bst_advanced.md) 的 **模板 6b**。

值得記在腦子裡的一句話：**LC 1382 = LC 94 + LC 108** —— 用中序走訪把 BST 攤平
（模板 5），再用上面那套「取中間當根」的遞迴重建。

#### **建構模式總表**
| 輸入型態 | 做法 | 關鍵技巧 | 時間 | 空間 | LC # |
|------------|----------|---------------|------|-------|------|
| **有序陣列** | 二分搜尋 | 取中間當根 | O(n) | O(n) | 108 |
| **有序串列** | 雙指標 | 找中間節點 | O(n log n) | O(log n) | 109 |
| **前序** | 邊界檢查 | 用 min/max | O(n) | O(h) | 1008 |
| **平衡 BST** | 中序 + 重建 | 收集有序節點 | O(n) | O(n) | 1382 |
| **生成全部** | 組合列舉 | 每個都當一次根 | O(4^n/n^1.5) | O(4^n/n^1.5) | 95 |
| **計算相異數量** | 動態規劃 | 卡塔蘭數 | O(n²) | O(n) | 96 |
| **序列化** | 前序編碼 | BST 性質 | O(n) | O(n) | 449 |

### 模板 10：用 BST 排序性質求 LCA —— LC 235 ⭐⭐⭐⭐

**核心想法**：在 BST 裡，找最近共同祖先永遠不必兩邊子樹都搜。
把*兩個*目標都拿去跟 `root.val` 比，只往那條還可能同時容納它們的邊下降。
第一個**沒有**把兩個目標指向同一側的節點，就是**分岔點**，而分岔點就是 LCA。

```python
# python
# LC 235 Lowest Common Ancestor of a Binary Search Tree
# IDEA: BST PROPERTY — descend while p and q sit on the SAME side of root
# time = O(h), space = O(h) recursion (O(1) if rewritten as a while-loop)
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        if not root:
            return root

        p_val = p.val
        q_val = q.val

        if root.val < p_val and root.val < q_val:
            return self.lowestCommonAncestor(root.right, p, q)

        elif root.val > p_val and root.val > q_val:
            return self.lowestCommonAncestor(root.left, p, q)

        else:
            return root
```

> 對比 **LC 236**（一般二元樹的 LCA）：沒有排序性質可用，就只能對*兩邊*子樹都做後序走訪再合併結果 ——
> 見 [tree_lca_distance.md](./tree_lca_distance.md)。

## 依模式分類的題目

> 只有索引，沒有程式碼。`模板`欄沿用整個家族的編號：模板 1–6 與 10 在本頁，模板 5b / 8 / 9 / 3c / 4b / 6b 在 [bst_advanced.md](./bst_advanced.md)，模板 7（路徑問題）在 [bst_examples.md](./bst_examples.md)。

### 依模式分類的題目對照

#### **模式 1：BST 搜尋與驗證**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Validate Binary Search Tree | 98 | Medium | 上下界 | 模板 4 |
| Search in a BST | 700 | Easy | 二分搜尋 | 模板 1 |
| Closest Binary Search Tree Value | 270 | Easy | 二分搜尋 | 模板 1 |
| Inorder Successor in BST | 285 | Medium | 中序性質 | 模板 1 |
| Two Sum IV - Input is BST | 653 | Easy | 雜湊 + 走訪 | 模板 5 |
| Find Mode in BST | 501 | Easy | 中序走訪 | 模板 5 |

#### **模式 2：BST 插入與刪除**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Insert into a BST | 701 | Medium | 遞迴插入 | 模板 2 |
| Delete Node in a BST | 450 | Medium | 三種情況 | 模板 3 |
| Trim a Binary Search Tree | 669 | Medium | 遞迴修剪 | 模板 3 |

#### **模式 3：BST 走訪與轉換**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Kth Smallest Element in BST | 230 | Medium | 中序走訪 | 模板 5 |
| BST Iterator | 173 | Medium | 堆疊 + 中序 | 模板 5 |
| Convert BST to Greater Tree | 538 | Medium | 反向中序 | 模板 5 |
| Binary Search Tree to Greater Sum Tree | 1038 | Medium | 反向中序 | 模板 5 |
| Convert Sorted List to BST | 109 | Medium | 雙指標 | 模板 6b |
| Flatten BST to Sorted List | 426 | Medium | 中序 + 串接 | 模板 5 |
| Increasing Order Search Tree | 897 | Easy | 中序重建 | 模板 5 |
| All Elements in Two BSTs | 1305 | Medium | 兩個延遲式迭代器 + 合併 | 模板 5b |

#### **模式 4：BST 建構**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Convert Sorted Array to BST | 108 | Easy | 二分搜尋 | 模板 6 |
| Unique Binary Search Trees | 96 | Medium | DP／卡塔蘭數 | 特例 |
| Unique Binary Search Trees II | 95 | Medium | 生成全部 | 模板 6b |
| Serialize and Deserialize BST | 449 | Medium | 前序編碼 | 特例 |
| Construct BST from Preorder | 1008 | Medium | 堆疊／遞迴 | 模板 6b |
| Balance a Binary Search Tree | 1382 | Medium | 中序 + 重建 | 模板 6b |

#### **模式 5：BST 性質與範圍問題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Lowest Common Ancestor of BST | 235 | Easy | BST 性質 | 模板 10 |
| Minimum Distance Between BST Nodes | 783 | Easy | 中序相鄰差 | 模板 5 |
| Minimum Absolute Difference in BST | 530 | Easy | 中序相鄰差 | 模板 5 |
| Range Sum of BST | 938 | Easy | DFS + 剪枝 | 模板 1 |
| Split BST | 776 | Medium | 遞迴拆分 | 特例 |
| Largest BST Subtree | 333 | Medium | 由下而上驗證 | 模板 4 |
| Kth Largest Element in a Stream | 703 | Easy | 附帶大小資訊的 BST（rank 查詢） | 模板 9 |
| Maximum Difference Between Node and Ancestor | 1026 | Medium | 邊界往下傳遞（min/max） | 模板 4b |
| Delete Nodes And Return Forest | 1110 | Medium | 遞迴 + 回傳 `null` 以拆離 | 模板 3c |

#### **模式 6：路徑問題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Path Sum | 112 | Easy | DFS 遞迴 | 模板 7 |
| Path Sum II | 113 | Medium | DFS + 回溯 | 模板 7 |
| Binary Tree Paths | 257 | Easy | DFS + 路徑追蹤 | 模板 7 |
| Sum Root to Leaf Numbers | 129 | Medium | DFS + 累加 | 模板 7 |
| Binary Tree Maximum Path Sum | 124 | Hard | DFS + 全域最大值 | 模板 7 |
| Binary Tree Longest Consecutive Sequence | 298 | Medium | DFS + 計數器 | 模板 7 |
| Path Sum III | 437 | Medium | 前綴和 + DFS | 模板 7 |

### 依難度的完整題目清單

#### Easy（打底）
- LC 700: Search in a Binary Search Tree - 基本 BST 搜尋
- LC 270: Closest Binary Search Tree Value - 改寫過的搜尋
- LC 108: Convert Sorted Array to BST - 基本建構
- LC 235: Lowest Common Ancestor of a BST - 用 BST 性質
- LC 653: Two Sum IV - Input is a BST - 樹上的雙指標
- LC 530: Minimum Absolute Difference in BST - 中序性質
- LC 783: Minimum Distance Between BST Nodes - 中序走訪
- LC 897: Increasing Order Search Tree - 中序重建
- LC 938: Range Sum of BST - DFS + 剪枝
- LC 501: Find Mode in Binary Search Tree - 中序 + 計數
- LC 112: Path Sum - 基本的 DFS 路徑和
- LC 257: Binary Tree Paths - DFS 路徑追蹤

#### Medium（核心）
- LC 98: Validate Binary Search Tree - 經典驗證題
- LC 173: Binary Search Tree Iterator - 設計題
- LC 230: Kth Smallest Element in a BST - 中序的應用
- LC 450: Delete Node in a BST - 複雜的結構重組
- LC 701: Insert into a BST - 基本修改
- LC 285: Inorder Successor in BST - BST 上的移動
- LC 96: Unique Binary Search Trees - 卡塔蘭數
- LC 95: Unique Binary Search Trees II - 生成所有樹
- LC 109: Convert Sorted List to BST - 串列轉樹
- LC 449: Serialize and Deserialize BST - 編碼／解碼
- LC 538: Convert BST to Greater Tree - 反向中序
- LC 669: Trim a Binary Search Tree - 遞迴修剪
- LC 776: Split BST - 進階操作
- LC 333: Largest BST Subtree - 子樹驗證
- LC 1008: Construct BST from Preorder - 堆疊解法
- LC 1038: Binary Search Tree to Greater Sum Tree - 累加
- LC 1382: Balance a Binary Search Tree - 中序 + 重建成平衡 BST
- LC 426: Convert BST to Sorted Doubly Linked List - 原地轉換
- LC 113: Path Sum II - 所有和為目標值的根到葉路徑
- LC 129: Sum Root to Leaf Numbers - DFS 累加
- LC 298: Binary Tree Longest Consecutive Sequence - 追蹤序列長度
- LC 437: Path Sum III - 任意路徑和為目標值（前綴和）

#### Hard（進階）
- LC 99: Recover Binary Search Tree - 修復被交換的節點（見 [bst_advanced.md](./bst_advanced.md) 的模板 8）
- LC 1373: Maximum Sum BST in Binary Tree - 複雜驗證
- LC 124: Binary Tree Maximum Path Sum - 節點到節點的最大路徑

## 總結與速查

**複雜度** —— 全篇只有一張表，就在本文件開頭：見
[時間複雜度](#time-complexity)。以下全部都在講*該挑哪一個*模板。

### 決策流程圖
```text
BST Problem Analysis Flowchart:

1. Does the problem require finding/searching a value?
   ├── YES → Use Search Template (1)
   │   ├── Exact match? → Basic binary search
   │   ├── Closest value? → Track min difference
   │   └── Range query? → Prune based on BST property
   └── NO → Continue to 2

2. Does the problem require modifying the BST structure?
   ├── YES → Check modification type
   │   ├── Insert new node? → Use Insertion Template (2)
   │   ├── Delete existing node? → Use Deletion Template (3)
   │   └── Trim/Split tree? → Use modified Deletion Template
   └── NO → Continue to 3

3. Does the problem use the sorted property of BST?
   ├── YES → Use Inorder Template (5)
   │   ├── Kth element? → Inorder with counter
   │   ├── Convert to list? → Inorder traversal
   │   └── Range sum? → Modified inorder
   └── NO → Continue to 4

4. Does the problem require validating BST properties?
   ├── YES → Use Validation Template (4)
   │   ├── Entire tree? → Min/max bounds approach
   │   └── Find largest valid subtree? → Bottom-up validation
   └── NO → Continue to 5

5. Does the problem involve constructing a BST?
   ├── YES → Use Construction Template (6)
   │   ├── From sorted array? → Binary search approach
   │   ├── From traversal? → Use BST properties
   │   └── Generate all possible? → Recursive generation
   └── NO → Continue to 6

6. Does the problem involve paths in the tree?
   ├── YES → Use Path Template (7)
   │   ├── Root-to-leaf sum? → DFS with target reduction
   │   ├── All paths? → DFS with backtracking
   │   ├── Max path sum? → DFS with global variable
   │   └── Any path with sum? → Prefix sum technique
   └── NO → Fall back to the plain BST descent (Template 1) or reconsider

```

### 該用哪個模板？—— 快速挑選

| 題目問的是… | 用哪個模板 | 關鍵技巧 | 典型 LC 題 |
|-------------------|--------------|---------------|---------------------|
| 「找／搜尋某個值」 | 模板 1 | 二分搜尋性質 | 700, 270, 938 |
| 「插入 BST」 | 模板 2 | 遞迴插入 | 701 |
| 「從 BST 刪除」 | 模板 3 | 三種情況處理 | 450, 669 |
| 「是不是合法 BST？」 | 模板 4 | 上下界 | 98, 333 |
| 「第 k 小／大」 | 模板 5 | 中序走訪 | 230, 173 |
| 「兩節點的 LCA」 | 模板 10 | 只往同時容納兩者的那側下降 | 235 |
| 「把有序陣列轉成樹」 | 模板 6.1 | 二分取中間 | 108 |
| 「平衡 BST」 | 模板 6.4 → [advanced](./bst_advanced.md) | 中序 + 重建 | 1382 |
| 「和為目標值的路徑」 | 模板 7.1 → [examples](./bst_examples.md) | DFS + 遞減目標值 | 112 |
| 「所有和為目標值的路徑」 | 模板 7.2 → [examples](./bst_examples.md) | DFS + 回溯 | 113 |
| 「最大路徑和」 | 模板 7.5 → [examples](./bst_examples.md) | DFS + 全域最大值 | 124 |
| 「任意和為目標值的路徑」 | 模板 7.7 → [examples](./bst_examples.md) | 前綴和 | 437 |
| 「修復壞掉的 BST」 | 模板 8 → [advanced](./bst_advanced.md) | 中序 + 用 `prev` 偵測下降 | 99, 501, 530 |
| 「串流中的第 k 大／rank 查詢」 | 模板 9 → [advanced](./bst_advanced.md) | 附帶大小資訊的 BST | 703 |

### 辨識模式

**關鍵字 → 模板對照：**
- **「search」、「find」、「closest」** → 模板 1（搜尋）
- **「insert」、「add node」** → 模板 2（插入）
- **「delete」、「remove」、「trim」** → 模板 3（刪除）
- **「valid」、「validate」、「is BST」** → 模板 4（驗證）
- **「Kth」、「sorted」、「inorder」、「iterator」** → 模板 5（中序）
- **「lowest common ancestor」、「split point」** → 模板 10（用排序性質求 LCA）
- **「construct」、「build」、「convert」、「balance」、「generate」** → 模板 6（建構）
- **「path」、「sum」、「maximum path」、「consecutive」** → 模板 7（路徑問題）
- **「recover」、「swapped」、「fix」、「adjacent values」、「successor」、「mode」** → 模板 8（中序 + `prev`）
- **「stream」、「after each insert」、「rank」、「how many are less than」** → 模板 9（順序統計 BST）

### 解題步驟
1. **確認能不能用 BST 性質**：`left < root < right` 派得上用場嗎？
2. **選對模板**：依操作類型決定
3. **想清楚邊界情況**：空樹、單一節點、重複值
4. **用剪枝最佳化**：跳過不必要的子樹
5. **拿退化的樹測**：最差情況

### 常見錯誤與提示

**🚫 常見錯誤：**
- **沒用到 BST 性質**：把 BST 當成一般二元樹在處理
- **忘了中序 = 有序**：白白錯過最佳化機會
- **刪除處理錯誤**：沒有涵蓋全部三種情況
- **驗證寫錯**：只檢查父子關係，沒檢查整棵子樹
- **邊走訪邊改動**：可能破壞 BST 性質

**✅ 最佳實務：**
- **一定要用上 BST 性質**：能剪的搜尋空間就剪掉
- **需要排序就用中序**：不要另外再排一次
- **明確處理重複值**：先決定你的 BST 允不允許重複
- **考慮樹的平衡**：面試時要主動提到最差 O(n)
- **測邊界情況**：空樹、單一節點、全左／全右

### 面試提示
1. **釐清 BST 的性質**：可以有重複值嗎？樹是平衡的嗎？
2. **講出複雜度**：平均 O(log n)、最差 O(n) 都要說
3. **提一下自平衡結構**：情境合適時提 AVL／紅黑樹
4. **善用 BST 性質**：讓對方看到你懂這個最佳化
5. **處理所有情況**：刪除尤其要涵蓋 0、1、2 個子節點

### 相關主題
- **自平衡 BST**：AVL 樹、紅黑樹（保證 O(log n)）
- **B 樹**：用於資料庫索引（每個節點多個鍵）
- **二元堆積**：性質不同（父 > 子）
- **字典樹（Trie）**：字串用的前綴樹
- **線段樹**：區間查詢與更新

### Java 實作備註
```java
// Java BST Node
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int x) { val = x; }
}

// Iterative inorder with Stack
Stack<TreeNode> stack = new Stack<>();
TreeNode curr = root;
while (curr != null || !stack.isEmpty()) {
    while (curr != null) {
        stack.push(curr);
        curr = curr.left;
    }
    curr = stack.pop();
    // Process curr
    curr = curr.right;
}
```

### Python 實作備註
```python
# TreeNode class
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

# Generator for memory-efficient inorder
def inorder_generator(root):
    if root:
        yield from inorder_generator(root.left)
        yield root.val
        yield from inorder_generator(root.right)
```

### 挑模板的實戰心法

1. **善用 BST 性質**：題目能用到 `left < root < right`，就用模板 1-6
2. **路徑問題**：多數在任意二元樹上都成立，不限 BST（模板 7）
3. **建構**：先看輸入型態（陣列／串列／走訪序列）→ 對應不同的模板變體
4. **修改**：改完之後一律回傳 root（模板 2、3、6）
5. **不確定時**：先想想中序走訪有沒有幫助（模板 5）

---
**面試必會題**：LC 98, 108, 112, 113, 124, 173, 230, 235, 450, 700, 701, 1382
**進階題**：LC 99, 124, 298, 333, 437, 776, 1373
**關鍵字**：BST、二元搜尋樹、中序、有序、驗證、搜尋樹、路徑和、DFS、回溯、平衡、建構

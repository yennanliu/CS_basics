# 樹的序列化與字串編解碼

> **範圍** — 把樹變成字串、再變回來 — 用子樹指紋做身分辨識與重複偵測，以及完整的編碼／解碼家族（括號式、逗號加 null 標記、深度前綴）。
> **另見**：[tree.md](./tree.md) — 編碼器賴以建立的走訪模板；[tree_construction.md](./tree_construction.md) — 從走訪陣列與索引區間建樹；[tree_examples.md](./tree_examples.md) — 其餘的樹題詳解；[trie.md](./trie.md) — LC 1948 要雜湊的前綴樹結構。

## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [String](https://leetcode.com/problem-list/string/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

## 總覽

每一道「樹 ⟷ 字串」的題目，都只是某個**編解碼器**的一半；而這兩半其實是同一段前序遞迴，往相反方向讀而已。有兩件不同的工作共用這套機制：

- **身分辨識** — 把子樹序列化成一把 key，讓兩棵子樹能用 O(1) 比較。要用**後序**，因為父節點的 key 是由子節點的 key 組出來的。LC 652、572、508。
- **重建** — 序列化到能把字串解析回同一棵樹。要用**前序**，外加一個沒有歧義的 null 表示法。LC 297、449、606、536、1028。

### 關鍵性質
- **複雜度**：搭配 `StringBuilder`／list-join 和一個共用的解析游標，時間與空間都是 O(N)；用土法煉鋼的 `+=` 或每次呼叫都切字串會退化成 O(N²)
- **核心想法**：`key(node) = f(node.val, key(left), key(right))` — 格式會變，遞迴不會
- **什麼時候用**：比較子樹、偵測重複，或任何「樹 ⟷ 字串」的來回轉換
- **不可妥協的兩件事**：明確的 null 標記，以及分隔符 — 少了任何一個，`1,2` 就有歧義

## 題型分類

| 類別 | 走訪 | null 的表示法 | 例題 |
|----------|-----------|--------------------|----------|
| **子樹指紋** | 後序 | `#` | LC 652, 572, 508, 250, 1948 |
| **括號式編解碼** | 前序 | 省略成對括號，但要遵守左側佔位規則 | LC 606, 536 |
| **逗號 + 標記編解碼** | 前序 | 明寫 `#` / `N` | LC 297, 449, 331 |
| **深度前綴編解碼** | 前序 | 由破折號的數量隱含 | LC 1028 |
| **雙走訪建樹** | — | 沒有 — 這正是*為什麼*需要第二個陣列 | LC 105, 106 |

## 模板與演算法

### 1) 後序 DFS + 節點路徑序列化模板（Java）

> 當你需要用結構 + 數值來**辨識或比較子樹**時使用。
> 靈感來自 LC 652 Find Duplicate Subtrees。

**核心想法 — 子樹指紋：**
```text
Serialize each subtree as a unique string: "val,left,right"
  → null nodes become "#" (marker) to preserve tree structure
  → Store in Map<String, Integer> to count occurrences
  → If count reaches 2, it's a duplicate → add to result
```

**為什麼要用後序？**
- 在組出當前節點的字串**之前**，必須先知道左右子樹的身分
- 先處理子節點（由下往上）→ 再到父節點合併
- 前序會在還不知道子節點結構時就先把字串組好

**圖解：為什麼 LC 652 用前序會壞掉**
```text
Tree:        1
            / \
           2   2
          /     \
         4       4

Pre-order serialization (WRONG — builds string top-down):
  node 2 (left)  → "2,4,#"   ← built before knowing full subtree
  node 2 (right) → "2,#,4"   ← different string, but structurally different → OK here

Post-order serialization (CORRECT — builds string bottom-up):
  node 4 (left)  → "4,#,#"
  node 4 (right) → "4,#,#"   ← same! correctly identified as duplicate
  node 2 (left)  → "2,4,#,#,#"
  node 2 (right) → "2,#,4,#,#"  ← different (4 is left child vs right child)

Key insight: only post-order guarantees the full subtree structure is
captured before building the parent's key.
```

**為什麼 null 要寫成 `#`？**
- 避免歧義：`"1,2"` vs `"12"` — 光有分隔符是不夠的
- `"1,#,#"` vs `"1,2,#"` — null 標記才能區分葉節點和內部節點

```java
// Template: Post-order DFS + Subtree Serialization (LC 652)
Map<String, Integer> pathMap = new HashMap<>();  // { serialized_string : count }
List<TreeNode> result = new ArrayList<>();

List<TreeNode> findDuplicateSubtrees(TreeNode root) {
    serialize(root);
    return result;
}

private String serialize(TreeNode node) {
    if (node == null) {
        return "#";  // null marker — preserves tree structure
    }

    // 1. Post-order: recurse into children FIRST
    String left  = serialize(node.left);
    String right = serialize(node.right);

    // 2. Build current subtree's unique identity
    //    Use delimiter to prevent "1,11" vs "11,1" ambiguity
    String key = node.val + "," + left + "," + right;

    // 3. Count occurrences
    int count = pathMap.getOrDefault(key, 0);

    // 4. Add to result ONLY when count == 1 (second occurrence = first duplicate)
    //    count == 1 means: this subtree appeared before, so current is a duplicate
    if (count == 1) {
        result.add(node);
    }

    // 5. Update count regardless
    pathMap.put(key, count + 1);

    // 6. Return serialization so parent can use it
    return key;
}
```

**序列化格式 — 為什麼 `val,left,right` 行得通：**

```text
Tree:       1
           / \
          2   3
         /
        4

Serialization (post-order):
  node 4 → "4,#,#"
  node 2 → "2,4,#,#,#"    (val=2, left="4,#,#", right="#")
  node 3 → "3,#,#"
  node 1 → "1,2,4,#,#,#,3,#,#"
```

**重複偵測的邏輯：**
```text
count == 0  → first time seen, just record
count == 1  → seen exactly once before → current is a DUPLICATE → add to result
count >= 2  → already recorded, skip (avoid adding same duplicate multiple times)
```

**分隔符的各種變體（都可以，挑一種然後從頭用到尾）：**
```java
// All of these produce unambiguous serializations:
node.val + "," + left + "," + right   // V0, V1 in FindDuplicateSubtrees.java (recommended)
node.val + "$" + left + "$" + right   // V2 (alternative)
node.val + " "  + left + " "  + right // V3 (using space)
node.val + "#"  + left + "#"  + right // V4 (using # as delimiter)
// Avoid concatenating without delimiter: "112" is ambiguous (1+12 vs 11+2)
```

**實作重點 — 計數邏輯的兩種寫法：**

下面這兩種計數方式是等價的，也都很常見：

```java
// Approach 1: Add when count == 1 (this subtree appeared before)
int count = pathMap.getOrDefault(key, 0);
if (count == 1) {
    result.add(node);  // 2nd occurrence → first duplicate
}
pathMap.put(key, count + 1);
```

```java
// Approach 2: Add when count == 2 (we just found second occurrence)
pathMap.put(key, pathMap.getOrDefault(key, 0) + 1);
if (pathMap.get(key) == 2) {
    result.add(node);  // Just became a duplicate
}
```

**該用哪一種？** 兩種都對。寫法 1 稍微乾淨一點（先檢查再更新），寫法 2 比較直覺（先加一再收進答案）。看個人喜好。

**面試小技巧（來自 LC 652）：**
> 如果題目要求**用結構來辨識／比較子樹**，
> 就用**後序 DFS + 序列化成 `"val,left,right"` 字串 + HashMap**。

**模式總結 — 後序 DFS 的 3 種變體：**

| 模式                           | DFS 回傳   | Map 的 key            | 適用情境                          |
|-----------------------------------|--------------------|--------------------|------------------------------------|
| 算高度                | `int`（高度）     | —                  | 深度、平衡、直徑           |
| 子樹序列化 + 計數     | `String`（序列）  | 序列化後的字串  | 重複子樹（LC 652）        |
| 子樹和／DP                  | `int`（結果）      | —                  | 最大路徑和、子樹和（LC 124） |
| 貪婪多狀態                | `int`（狀態）      | —                  | 監視器、著色、覆蓋（LC 968） |

**同樣用「後序 + 子樹序列化」的相似題：**

| LC #  | 題目                              | 模式變體                                                    | Difficulty |
|-------|--------------------------------------|--------------------------------------------------------------------|------------|
| 652   | Find Duplicate Subtrees              | 序列化 → HashMap 計數；count == 1 時把節點收進答案               | Medium     |
| 572   | Subtree of Another Tree              | 兩棵樹都序列化；檢查 `s` 的序列是否包含 `t` 的序列 | Easy   |
| 508   | Most Frequent Subtree Sum            | 後序算子樹和 → HashMap 統計次數 → 回傳次數最多的 key | Medium  |
| 250   | Count Univalue Subtrees              | 後序：葉節點 OR（子樹皆同值 AND 值與父節點相同）     | Medium     |
| 297   | Serialize and Deserialize Binary Tree| 用前／後序把樹編成字串，再重建回來             | Hard       |
| 449   | Serialize and Deserialize BST        | 後序序列化，順便利用 BST 的排序性質          | Medium     |
| 1948  | Delete Duplicate Folders in System   | 字典樹（Trie）+ 後序子樹雜湊 — LC 652 的進階變體        | Hard       |

**實作風格的差異：**

解法的架構有好幾種等價寫法：

```java
// Style 1: Instance variables (mutable state in class)
class Solution {
    Map<String, Integer> pathMap = new HashMap<>();
    List<TreeNode> result = new ArrayList<>();

    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        serialize(root);
        return result;
    }

    private String serialize(TreeNode node) {
        // ... implementation ...
    }
}
```

```java
// Style 2: Local variables + pass through parameters
public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
    Map<String, Integer> pathMap = new HashMap<>();
    List<TreeNode> result = new ArrayList<>();
    serialize(root, pathMap, result);
    return result;
}

private String serialize(TreeNode node, Map<String, Integer> pathMap, List<TreeNode> result) {
    // ... implementation ...
}
```

**該用哪種風格？** 風格 1（用實例變數）比較常見，面試時也比較乾淨。風格 2 比較函式式。兩種都成立。

---

**為什麼 LC 652 非用後序不可：**
```text
Goal: build a unique "fingerprint" string for each subtree.

Pre-order (root → left → right):
  → builds the key at the ROOT first, before children are known
  → can't include children's serialized forms at build time
  → would require a separate recursive pass → O(N²) and messy

Post-order (left → right → root):
  → left child already returned its serialized string
  → right child already returned its serialized string
  → current key = val + "," + leftKey + "," + rightKey   ← O(1) to build
  → naturally bubbles the full subtree fingerprint up to the parent
  → one DFS pass is enough: O(N) time

Rule: if you need the COMPLETE subtree structure in the key, use post-order.
```

**容易踩到的坑：**
- ❌ 忘了 null 標記 → 產生像 "1,2" 這種有歧義的字串（到底是 1,2 還是 12？）
- ❌ 檢查 `count == 0` 而不是 `count == 1` → 會把每一次出現都收進來，而不是只收重複的
- ❌ 檢查 `count == 2` 又在同一行加進去 → 同一個重複子樹可能被加好幾次
- ✅ 一定要加 null 標記和分隔符，序列化才沒有歧義
- ✅ 只在 count 從 0→1 或恰好變成 2 的那一刻，把結果加**一次**

### 2) 節點路徑模式 — 子樹序列化

節點路徑模式會把每棵子樹序列化成一個唯一的字串，方便快速比較與偵測重複。

#### **LC 652: Find Duplicate Subtrees**

> Java 實作：上面第 1) 節的模板就是這一題的解法。

```python
# python
# LC 652 Find Duplicate Subtrees
from collections import defaultdict

def findDuplicateSubtrees(root):
    """
    IDEA: Node Path Pattern

    Time: O(N²) worst case (string operations), O(N) with optimization
    Space: O(N) for HashMap and recursion
    """
    result = []
    path_count = defaultdict(int)

    def get_node_path(node):
        if not node:
            return "#"

        # Post-order: left → right → node
        left = get_node_path(node.left)
        right = get_node_path(node.right)

        # Build serialization string
        path = f"{node.val},{left},{right}"

        # Track frequency
        if path_count[path] == 1:
            result.append(node)
        path_count[path] += 1

        return path

    get_node_path(root)
    return result
```

#### **LC 572: Subtree of Another Tree**

```java
// java
// LC 572 Subtree of Another Tree
/**
 * IDEA: Node Path Pattern for Subtree Matching
 *
 * Approach 1: Serialize both trees and check if subRoot is substring of root
 * Approach 2: Traditional recursive comparison (shown below)
 */

// Method 1: Using Node Path Serialization
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    String rootPath = serialize(root);
    String subPath = serialize(subRoot);

    // NOTE !!! serialize() already wraps every node in commas, so subPath arrives
    //          delimited on both sides. Adding another pair searches for ",,...,,"
    //          and returns false even for root = [1], subRoot = [1].
    return rootPath.contains(subPath);
}

private String serialize(TreeNode node) {
    if (node == null) {
        return "#";
    }

    // Post-order serialization with delimiters
    String left = serialize(node.left);
    String right = serialize(node.right);

    // Add delimiters to prevent false matches (e.g., "12" in "123")
    return "," + node.val + "," + left + "," + right + ",";
}

// Method 2: Traditional Recursive Comparison
public boolean isSubtree_v2(TreeNode root, TreeNode subRoot) {
    if (root == null) {
        return false;
    }

    // Check if current tree matches OR check subtrees
    return isSameTree(root, subRoot) ||
           isSubtree_v2(root.left, subRoot) ||
           isSubtree_v2(root.right, subRoot);
}

private boolean isSameTree(TreeNode p, TreeNode q) {
    if (p == null && q == null) return true;
    if (p == null || q == null) return false;
    if (p.val != q.val) return false;

    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
}
```

```python
# python
# LC 572 Subtree of Another Tree

# Method 1: Node Path Serialization
def isSubtree(root, subRoot):
    """
    Serialize both trees and check substring match

    Time: O(M + N) where M, N are tree sizes
    Space: O(M + N) for serialization strings
    """
    def serialize(node):
        if not node:
            return "#"

        # Post-order with delimiters
        left = serialize(node.left)
        right = serialize(node.right)

        return f",{node.val},{left},{right},"

    root_path = serialize(root)
    sub_path = serialize(subRoot)

    return sub_path in root_path

# Method 2: Traditional Recursive
def isSubtree_v2(root, subRoot):
    if not root:
        return False

    def is_same(p, q):
        if not p and not q:
            return True
        if not p or not q:
            return False
        return (p.val == q.val and
                is_same(p.left, q.left) and
                is_same(p.right, q.right))

    return (is_same(root, subRoot) or
            isSubtree_v2(root.left, subRoot) or
            isSubtree_v2(root.right, subRoot))
```

#### **節點路徑模式的心得**

**什麼時候用節點路徑模式：**
- 找重複或相似的子樹
- 檢查一棵樹是不是另一棵樹的子樹
- 數有幾種不同的樹結構
- 樹的樣式比對題

**實作重點：**
1. **後序走訪**：先處理子節點，再處理父節點
2. **null 標記**：用 "#" 或 "null" 表示空節點
3. **分隔符**：用 "," 分開數值，避免歧義
4. **HashMap 追蹤**：記錄「序列 → 出現次數」的對應
5. **提早收手**：偵測重複時，在 count == 1 的那一刻收進答案

**容易踩到的坑：**
- ❌ 忘了分隔符 → "12" 會誤配到 "1,2"
- ❌ 沒處理空節點 → 不同的結構會序列化成一樣的字串
- ❌ 用前序 → 比較難組出完整的子樹表示
- ❌ 沒有快取序列化字串 → 時間複雜度變成 O(N²)

**複雜度分析：**
- **時間**：最壞 O(N²)（字串串接），用 StringBuilder 則是 O(N)
- **空間**：HashMap 加遞迴堆疊是 O(N)，存下所有路徑則是 O(N²)

### 3) 樹 ⟷ 字串 編解碼模式 ⭐⭐⭐⭐⭐

**核心想法**：每一道「樹 ↔ 字串」的題目都是某個**編解碼器**的一半。兩半是同一段遞迴，只是方向相反：

| 方向 | 名稱 | 遞迴回傳 | 走訪 | 模板長相 |
|-----------|------|-------------------|-----------|----------------|
| 樹 → 字串 | **encode** / 序列化 | **這棵子樹**的字串 | 前序（根先） | `f(node) = FMT(val, f(left), f(right))` |
| 字串 → 樹 | **decode** / 反序列化 | 節點 + **它吃掉了多少字串** | 前序（根先） | `g(i) = (node(val), i')` — 一個遞迴下降解析器 |

```text
      encode                                    decode
   ┌──────────┐                              ┌──────────┐
   │  tree    │ ── "{}({})({})".format() ──► │  string  │
   │          │ ◄── parse val, then '(' ──── │          │
   └──────────┘                              └──────────┘
   returns str going UP                      returns (node, idx) going UP
```

**唯一的不變式**：`decode(encode(t)) == t`。它成立的前提是編碼器和解碼器在**三件事**上達成共識 — 而這三個問題就是這個模式的全部：

1. **分隔符** — 我怎麼知道一個值到哪裡結束？（`,` / `(` `)` / `-` 前綴）
2. **null 的表示** — 用明確的標記（`#`、`null`），**還是**靠結構上的巢狀（括號）？
3. **順序** — 前序／後序／層序（兩邊必須一致）

#### **0) 編碼 — 通用的一行式** 🎯

```python
# python — every tree→string problem is this, with a different FMT + NULL
def encode(node):
    if not node:
        return NULL          # "" for parens, "#" for comma-format
    return FMT.format(node.val, encode(node.left), encode(node.right))
```

| LC | 格式 | `FMT` | `NULL` | 輸出範例 |
|----|--------|-------|--------|----------------|
| **606** Construct String from Binary Tree | 巢狀括號 | `"{}({})({})"` | `""`（成對省略） | `1(2(4))(3)` |
| **536** Construct Binary Tree from String | *（同一種格式，解碼側）* | — | — | `4(2(3)(1))(6(5))` |
| **297** Serialize/Deserialize Binary Tree | 逗號前序 | `"{},{},{}"` | `"#"` | `1,2,#,#,3,#,#` |
| **449** Serialize/Deserialize BST | 逗號前序 | `"{},{},{}"` | `"#"`（或省略 — BST 的順序已隱含） | `2,1,3` |
| **652 / 572** 子樹身分 key | 逗號 **後序** | `"{},{},{}"`（子節點在前） | `"#"` | `4,#,#,2,#,#,1` |
| **1028** Recover a Tree From Preorder | 深度前綴的行 | `"-"*depth + str(val)` | 省略（深度隱含形狀） | `1-2--3--4-5--6--7` |
| **331** Verify Preorder Serialization | 逗號前序 | *（只驗證 — 從不建樹）* | `"#"` | `9,3,4,#,#,1,#,#,2,#,6,#,#` |

> **為什麼括號不需要 `#`、逗號卻需要**：巢狀本身帶有*位置*資訊 — `(A)(B)` 在結構上就說了「A 是左、B 是右」。扁平的逗號串沒有巢狀，所以缺席的子節點必須明寫出來，否則 `1,2` 可能代表「2 是 1 的左子節點」，也可能是「2 是 1 的右子節點」。

#### **1) LC 606 — 括號格式與省略規則** ⭐⭐⭐⭐⭐

完整格式是 `val(left)(right)`。題目說空的 `()` 要拿掉 — **但不是永遠**：

```text
 Case                      Full form        Emitted        Why
 ─────────────────────────────────────────────────────────────────────────────────
 leaf                      1()()            1              both pairs useless
 left only                 1(2)()           1(2)           trailing () carries no info
 right only  ⚠️            1()(3)           1()(3)         MUST keep the left placeholder!
 both                      1(2)(3)          1(2)(3)        nothing to drop
```

**整個技巧就在第 3 列**：如果你把空的左括號拿掉，輸出會變成 `1(3)`，而它會被解讀成「3 是**左**子節點」— 對應關係不再是一對一。所以規則是：

> 只要**存在任一個**子節點，就保留**左**邊那對括號。只有真的有右子節點時，才保留右邊那對。

這樣 4 種情況就塌成 2 個 `if`：

```python
# python
# LC 606 - Construct String from Binary Tree  (2-rule form — preferred)
# IDEA: pre-order DFS returning a string; left pair kept if ANY child exists
# time = O(N) with a list/StringBuilder (O(N^2) with naive `+` on strings), space = O(H)
class Solution:
    def tree2str(self, root):
        if not root:
            return ""
        s = str(root.val)
        if root.left or root.right:          # left placeholder needed if ANY child
            s += "(" + self.tree2str(root.left) + ")"
        if root.right:                       # right only when it exists
            s += "(" + self.tree2str(root.right) + ")"
        return s
```

```python
# python
# LC 606 - same thing, written as the explicit `format` template (matches the FMT table above)
# time = O(N), space = O(H)
class Solution:
    def tree2str(self, root):
        if not root:
            return ""
        # Case 1: leaf -> bare value
        if not root.left and not root.right:
            return str(root.val)
        # Case 2: no right child -> omit the trailing ()
        if not root.right:
            return "{}({})".format(root.val, self.tree2str(root.left))
        # Case 3: right child exists -> left may render as the empty "()" placeholder
        return "{}({})({})".format(
            root.val,
            self.tree2str(root.left),
            self.tree2str(root.right)
        )
```

```java
// java
// LC 606 - Construct String from Binary Tree
// IDEA: pre-order DFS + StringBuilder (avoid O(N^2) string concat)
// time = O(N), space = O(H)
public String tree2str(TreeNode root) {
    StringBuilder sb = new StringBuilder();
    dfs(root, sb);
    return sb.toString();
}

private void dfs(TreeNode node, StringBuilder sb) {
    if (node == null) return;
    sb.append(node.val);
    if (node.left != null || node.right != null) {   // left placeholder rule
        sb.append('(');
        dfs(node.left, sb);
        sb.append(')');
    }
    if (node.right != null) {
        sb.append('(');
        dfs(node.right, sb);
        sb.append(')');
    }
}
```

**圖解追蹤** — `root = [1,2,3,4]`：

```text
        1              tree2str(4) = "4"                      (leaf)
       / \             tree2str(2) = "2" + "(4)"     = "2(4)"  (left only)
      2   3            tree2str(3) = "3"                      (leaf)
     /                 tree2str(1) = "1" + "(2(4))" + "(3)"
    4                              = "1(2(4))(3)"
```

`root = [1,2,3,null,4]` — 需要佔位的情況：

```text
        1              tree2str(4) = "4"
       / \             tree2str(2) = "2" + "()" + "(4)" = "2()(4)"   ← left pair KEPT
      2   3                          ^^^^ empty, but required
       \
        4              tree2str(1) = "1(2()(4))(3)"
```

#### **2) LC 536 — 把同一種格式解碼（遞迴下降 + 索引指標）** ⭐⭐⭐⭐⭐

解碼器完全對映編碼器：先讀一個值，再讀最多兩棵括號包起來的子樹。
**把游標一路帶著走**，讓每次呼叫都回報它停在哪裡 — 這樣才能維持 O(N)。

```python
# python
# LC 536 - Construct Binary Tree from String   (input: "4(2(3)(1))(6(5))")
# IDEA: recursive descent parser; helper returns (node, next_index)
# time = O(N)  each char consumed once
# space = O(H) recursion depth
class Solution(object):
    def str2tree(self, s):
        if not s:
            return None
        root, _ = self.helper(s, 0)
        return root

    def helper(self, s, idx):
        n = len(s)

        # 1) parse the value: optional '-' then digits (multi-digit!)
        sign = 1
        if s[idx] == '-':
            sign = -1
            idx += 1
        num = 0
        while idx < n and s[idx].isdigit():
            num = num * 10 + int(s[idx])
            idx += 1

        node = TreeNode(sign * num)

        # 2) first '(' -> LEFT subtree
        if idx < n and s[idx] == '(':
            node.left, idx = self.helper(s, idx + 1)   # +1 skips '('
            idx += 1                                   # skip the matching ')'

        # 3) second '(' -> RIGHT subtree
        if idx < n and s[idx] == '(':
            node.right, idx = self.helper(s, idx + 1)
            idx += 1

        return node, idx
```

```java
// java
// LC 536 - Construct Binary Tree from String
// IDEA: recursive descent; int[] idx acts as a by-reference cursor
// time = O(N), space = O(H)
public TreeNode str2tree(String s) {
    if (s == null || s.isEmpty()) return null;
    return helper(s, new int[]{0});
}

private TreeNode helper(String s, int[] idx) {
    // 1) value (handles '-' and multi-digit)
    int sign = 1;
    if (s.charAt(idx[0]) == '-') { sign = -1; idx[0]++; }
    int num = 0;
    while (idx[0] < s.length() && Character.isDigit(s.charAt(idx[0]))) {
        num = num * 10 + (s.charAt(idx[0]) - '0');
        idx[0]++;
    }
    TreeNode node = new TreeNode(sign * num);

    // 2) left
    if (idx[0] < s.length() && s.charAt(idx[0]) == '(') {
        idx[0]++;                       // skip '('
        node.left = helper(s, idx);
        idx[0]++;                       // skip ')'
    }
    // 3) right
    if (idx[0] < s.length() && s.charAt(idx[0]) == '(') {
        idx[0]++;
        node.right = helper(s, idx);
        idx[0]++;
    }
    return node;
}
```

**另一種做法 — 掃描括號平衡 + 切字串**（比較好懂，但因為切字串所以是 O(N²)）：

```python
# python
# LC 536 - split on the '(' that closes at balance == 0
# time = O(N^2) (string slicing), space = O(N)
class Solution(object):
    def str2tree(self, s):
        if not s:
            return None

        first = s.find('(')
        if first == -1:                       # no children -> pure value
            return TreeNode(int(s))

        root = TreeNode(int(s[:first]))       # int() handles the '-' for us

        # find the ')' matching the FIRST '(' via parenthesis balance
        bal, left_end = 0, -1
        for i in range(first, len(s)):
            if s[i] == '(':
                bal += 1
            elif s[i] == ')':
                bal -= 1
            if bal == 0:
                left_end = i
                break

        root.left = self.str2tree(s[first + 1: left_end])   # inside 1st pair
        if left_end + 1 < len(s):
            root.right = self.str2tree(s[left_end + 2: -1]) # inside 2nd pair
        return root
```

> ⚠️ **606 的輸出不見得是合法的 536 輸入。** LC 536 保證空樹是 `""`，絕不會是 `"()"` — 所以 LC 606 那個 `2()(4)` 的佔位形式不會出現。如果你真的要一個能來回轉換的解碼器，在解析器開頭加一道防護：
> ```python
> if idx < n and s[idx] == ')':   # empty placeholder pair
>     return None, idx
> ```
> 少了它，`helper` 會讀到零個數字，然後開開心心建出一個假的 `TreeNode(0)`。

#### **3) LC 297 — 同一段遞迴，換成逗號格式 + 明確的 null** ⭐⭐⭐⭐⭐

```python
# python
# LC 297 - Serialize and Deserialize Binary Tree
# IDEA: pre-order with "#" for null; decode consumes the token stream via iter()
# time = O(N) both ways, space = O(N)
class Codec:
    def serialize(self, root):
        def dfs(node):
            if not node:
                return "#"
            # SAME shape as LC 606 -- only FMT and NULL changed
            return "{},{},{}".format(node.val, dfs(node.left), dfs(node.right))
        return dfs(root)

    def deserialize(self, data):
        it = iter(data.split(","))          # the cursor, for free
        def dfs():
            v = next(it)
            if v == "#":
                return None
            node = TreeNode(int(v))
            node.left = dfs()               # pre-order: left BEFORE right
            node.right = dfs()
            return node
        return dfs()
```

Python 的 `iter()` + `next()` 小技巧，其實就是 LC 536 裡的 `idx` 游標 — 迭代器*本身*就是索引，所以你不必把它塞進回傳值一路傳下去。

#### **4) LC 1028 — 用深度當分隔符**

```python
# python
# LC 1028 - Recover a Tree From Preorder Traversal   ("1-2--3--4-5--6--7")
# IDEA: dashes = depth; stack holds the current root-to-node path
# time = O(N), space = O(H)
class Solution:
    def recoverFromPreorder(self, traversal):
        stack, i, n = [], 0, len(traversal)
        while i < n:
            depth = 0
            while traversal[i] == '-':      # 1) count dashes -> depth
                depth += 1
                i += 1
            j = i
            while j < n and traversal[j] != '-':
                j += 1
            node = TreeNode(int(traversal[i:j]))
            i = j

            while len(stack) > depth:       # 2) pop back up to this node's parent
                stack.pop()
            if stack:
                parent = stack[-1]
                if not parent.left:         # 3) pre-order -> left fills first
                    parent.left = node
                else:
                    parent.right = node
            stack.append(node)
        return stack[0] if stack else None
```

#### **編解碼模式的心得** ⭐⭐⭐⭐⭐

**編碼檢查清單（樹 → 字串）**
1. Base case 回傳的是 **null 的表示法**，不是 `None` — `""`（括號式）或 `"#"`（逗號式）。
2. 先遞迴、再合併 — 父節點的字串是由子節點的字串組出來的。
3. 用 `StringBuilder` / list-join 才有 O(N)；在迴圈裡土法 `+=` 是 O(N²)。
4. 要重建就用**前序**（LC 297/606），要做身分 key 就用**後序**（LC 652/572）。

**解碼檢查清單（字串 → 樹）**
1. 所有層共用**同一個游標** — `int[]` / 實例欄位 / `iter()`。回傳 `(node, idx)` 也可以；切字串不行（O(N²)）。
2. 用 `while isdigit()` 迴圈解析數值 — 數值可能是**多位數**，也可能是**負的**。
3. 分隔符要明確吃掉：`(` 之後 `+1`、`)` 之後 `+1`。這裡的差一是第一名的 bug。
4. 永遠左先右後 — 這才使它成為前序編碼的反函數。

**容易踩到的坑**
- ❌ LC 606 把空的左 `()` 拿掉 → `1(3)` 會被解讀成左子節點（對應關係壞掉）
- ❌ 用 `int(s[i])` 而不是數字迴圈 → `"12"` 變成數值 `1`
- ❌ 忘了 `'-'` → LC 536 明講允許負數
- ❌ 每層遞迴都重新切字串 → O(N²)
- ❌ 扁平／逗號格式沒有 null 標記 → `1,2` 有歧義（左子節點還是右子節點？）
- ❌ 只有前序而**沒有** null 根本解不回來 — 這正是 LC 105/106 需要*第二個*走訪的原因，而 LC 297 靠把 null 明寫出來，一個走訪就夠了

**相關題目**

| LC | 題目 | 方向 | 關鍵差異 |
|----|---------|-----------|----------------|
| 606 | Construct String from Binary Tree | 樹 → 字串 | 括號 + 省略規則（保留左側佔位） |
| 536 | Construct Binary Tree from String | 字串 → 樹 | 遞迴下降、游標、負值 |
| 297 | Serialize and Deserialize Binary Tree | 雙向 | 逗號 + `#` null 標記 |
| 449 | Serialize and Deserialize BST | 雙向 | BST 的順序讓你可以省掉 null 標記 |
| 331 | Verify Preorder Serialization | 只驗證 | 數空位 — 從不建樹 |
| 652 | Find Duplicate Subtrees | 樹 → 字串 | **後序** key + HashMap（見第 2 節） |
| 572 | Subtree of Another Tree | 樹 → 字串 | 兩棵都序列化，再做子字串檢查（見第 2 節） |
| 1028 | Recover a Tree From Preorder Traversal | 字串 → 樹 | 用深度前綴 + 堆疊取代括號 |
| 105 / 106 | Construct Tree from 2 traversals | 陣列 → 樹 | *因為*沒有 null，所以需要第二個走訪 |


> 同一段編碼／解碼遞迴，按各種走訪順序分別寫出來。

#### 用前序走訪序列化二元樹
```java
// java
// algorithm book (labu) p.256

String SEP = ",";
String NULL = "#";

/* main func : serialize binary tree to string */
String serialize(TreeNode root){
    StringBuilder sb = new StringBuilder();
    serialize(root, sb);
    return sb.toString();
}

/* help func : put binary tree to StringBuilder */
void serialize(TreeNode root, StringBuilder sb){
    if (root == null){
        sb.append(NULL).append(SEP);
        return;
    }

    /********* pre-order traverse *********/
    sb.append(root.val).append(SEP);
    /**************************************/

    serialize(root.left, sb);
    serialize(root.right, sb);
}
``` 

#### 用前序走訪反序列化二元樹
```java
// java
// algorithm book (labu) p.256

String SEP = ",";
String NULL = "#";

/* main func : dserialize string to binary tree */
TreeNode deserlialize(String data){
    // transform string to linkedlist
    LinkedList<String> nodes = new LinkedList<>();
    for (String s: data.split(SEP)){
        nodes.addLast(s);
    }
    return deserlialize(nodes);
}


/* **** help func : build binary tree via linkedlist (nodes) */
TreeNode deserlialize(LinkedList<String> nodes){
    if (nodes.isEmpty()) return null;

    /********* pre-order traverse *********/
    // the 1st element on left is the root node of the binary tree
    String first = nodes.removeFirst();
    if (first.equals(NULL)) return null;
    TreeNode root = new TreeNode(Integer.parseInt(first));
    /**************************************/

    root.left = deserlialize(nodes);
    root.right = deserlialize(nodes);

    return root;
}
```

#### 用後序走訪序列化二元樹
```java
// java
// algorithm book (labu) p.258

String SEP = ",";
String NULL = "#";

StringBuilder sb = new StringBuilder();

/* help func : pit binary tree to StringBuilder*/
void serialize(TreeNode root, StringBuilder sb){
    if (root == null){
        sb.append(NULL).append(SEP);
        return;
    }

    serialize(root.left, sb);
    serialize(root.right, sb);

    /********* post-order traverse *********/
    sb.append(root.val).append(SEP);
    /**************************************/
}
```

#### 用後序走訪反序列化二元樹
```java
// java
// algorithm book (labu) p.260

/* main func : deserialize string to binary tree */
TreeNode deserlialize(String data){
    LinkedList<String> nodes = new LinkedList<>();
    for (String s : data.split(SEP)){
        nodes.addLast(s);
    }
    return deserlialize(nodes);
}

/* help func : build binary tree via linkedlist */
TreeNode deserlialize(LinkedList<String> nodes){
    if (nodes.isEmpty()) return null;
    // get element from last to beginning
    String last = nodes.removeLast();

    if (last.equals(NULL)) return null;
    TreeNode root = new TreeNode(Integer.parseInt(last));
    // build right sub tree first, then left sub tree
    root.right = deserlialize(nodes);
    root.left = deserlialize(nodes);

    return root;
}
```

#### 用層序走訪序列化二元樹
```java
// java
// layer traverse : https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/tree.md#1-1-basic-op
// algorithm book (labu) p.263
String SEP = ",";
String NULL = "#";

/* Serialize binary tree to string */
String serialize(TreeNode root){

    if (root == null) return "";
    StringBuilder sb = new StringBuilder();

    // init queue, put root into it
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    while (!q.isEmpty()){
        TreeNode cur = q.poll();

        /***** layer traverse ******/
        if (cur == null){
            sb.append(NULL).append(SEP);
            continue;
        }
        sb.append(cur.val).append(SEP);
        /**************************/

        q.offer(cur.left);
        q.offer(cur.right);
    }
    return sb.toString();
}
```

#### 用層序走訪反序列化二元樹
```java
// java
// algorithm book (labu) p.264

String SEP = ",";
String NULL = "#";

/* Deserialize binary tree to string */
TreeNode deserlialize(String data){
    
    if (data.isEmpty()) return null;

    String[] nodes = data.split(SEP);

    // root's value = 1st element's value
    TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));

    // queue records parent node, put root into queue
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    for (int i = 1; i < nodes.length){

        // queue saves parent nodes
        TreeNode parent = q.poll();
        
        // parent node's left sub node
        String left = nodes[i++];
        if (!left.equals(NULL)){
            parent.left = new TreeNode(Integer.parseInt(left));
            q.offer(parent.left);
        }else {
            parent.left = null;
        }
        // parent node's right sub node
        String right = nodes[i++];
        if (!right.equals(NULL)){
            parent.right = new TreeNode(Integer.parseInt(right));
            q.offer(parent.right);
        }else{
            parent.right = null;
        }
    }

    return root;
}
```


#### 序列化與反序列化二元樹

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

## 總結

| 題目要你… | 走訪 | 格式 |
|---|---|---|
| 比較／計數子樹 | 後序 | `val,left,right` 加 `#`，再做雜湊 |
| 對一般二元樹做來回轉換 | 前序 | 逗號 + 明確的 null 標記（LC 297） |
| 對 BST 做來回轉換 | 前序 | 不需要標記 — 數值範圍已經隱含（LC 449） |
| 產出 LC 606 的括號形式 | 前序 | 只要**存在任一個**子節點就保留左邊那對括號 |
| 把括號形式解析回樹 | 遞迴下降 | 一個共用游標、數字迴圈、吃掉 `(` 和 `)` |
| 只驗證、不建樹 | 前序 | 數空位（LC 331） |

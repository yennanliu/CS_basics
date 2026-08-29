# BST — 進階模式與深入探討

> **範圍** — 第一輪學習可以先跳過的 BST 內容：順序統計（rank）查詢、只用 O(h) 空間的惰性迭代器、用中序走訪偵測損壞 BST 的下降點、完整的建構變形目錄，以及超出標準刪除與驗證模板的拆離／邊界變形。
> **另見** — [bst.md](./bst.md) — 本文每一節所延伸的標準模板；[bst_examples.md](./bst_examples.md) — 完整的 LC 解題彙整；[segment_tree.md](./segment_tree.md) — 在陣列（而非樹）上做區間查詢；[binary_indexed_tree.md](./binary_indexed_tree.md) — 不建節點樹也能做前綴和排名。

## LeetCode 題目清單

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Design](https://leetcode.com/problem-list/design/)

## 總覽

共六組模板，每一組都是對某個 BST 想法的深入探討——
而 [bst.md](./bst.md) 對這些想法往往只用一兩行帶過。請先讀 `bst.md`：本文每一節都假設
你已經能憑記憶寫出標準的 search / insert / delete / validate / inorder /
construct 形式。

### 關鍵性質
- **複雜度**：排名與迭代器模板每次操作 O(h)——平衡樹上是 O(log n)，
  歪斜樹上則是 O(n)；整棵樹的走訪（模板 8、6b）為 O(n)
- **核心想法**：每一組都是在某個標準模板上加東西——加上子樹大小欄位、加上
  顯式堆疊、加上 `prev` 指標，或是換一種輸入格式
- **使用時機**：面試的追問——「如果資料會一直變動呢？」、「能不能用 O(1)
  空間做？」、「如果輸入改成鏈結串列呢？」

## 模板與演算法

| 模板 | 延伸自 | 回答的問題 | 核心 LC |
|---|---|---|---|
| **5b** — 惰性迭代器 | 模板 5（中序） | 用 O(h) 空間串流輸出排序值 | 173, 1305 |
| **8** — 修復損壞的 BST | 模板 5（中序） | 哪兩個節點被交換了？ | 99 |
| **9** — 順序統計樹 | 模板 1 + 2 | 在持續插入下查第 k 大／排名 | 703 |
| **6b** — 建構變形 | 模板 6 | 從串列／前序／不平衡 BST 建樹 | 109, 1008, 1382, 95, 96 |
| **3c** — 拆離變形 | 模板 3（刪除） | 刪成一片*森林*；以值交換方式刪除 | 1110, 450 |
| **4b** — 邊界傳遞 | 模板 4（驗證） | 用邊界來*度量*，而不是用來拒絕 | 1026 |

### 模板 5b：BST 惰性走訪（迭代器模式）

#### **模式總覽**
- **說明**：用一個堆疊按需模擬中序走訪——只走到需要的地方，不必一開始就走完整棵樹
- **辨識訊號**：「BST Iterator」、「下一個最小值」、「串流走訪」、資料量大到把所有節點載入成本很高
- **關鍵洞見**：只把左脊（left spine）推入堆疊；彈出某個節點時，再把它右子樹的左脊推進去
- **時間複雜度**：每次 `next()` 攤還 O(1)，空間 O(h)，h 為樹高
- **空間複雜度**：O(h)——遠優於預先蒐集所有節點的 O(n)

#### **核心想法**

```text
Pre-collect ALL nodes (eager):              Lazy traversal:
  O(n) space, O(n) init time                 O(h) space, O(1) amortized per call

  [1, 3, 5, 7, 9, ...]   ← full list        Stack: only current left spine
  load everything first                      push more only when needed
```

**三步驟模式：**
```text
1. INIT:    Push entire left spine from root into stack
             (leftmost path = smallest values on top)

2. next():  Pop top node (= current smallest)
             → if it has a right child, push that subtree's left spine
             → return popped node's value

3. hasNext(): stack is non-empty
```

**圖解走訪：**
```text
BST:          7
             / \
            3   15
               /  \
              9   20

Init: pushLeft(7) → stack = [7, 3]  (3 on top)

next() → pop 3, no right child → return 3,  stack = [7]
next() → pop 7, pushLeft(15)   → return 7,  stack = [15, 9]
next() → pop 9, no right child → return 9,  stack = [15]
next() → pop 15, pushLeft(20)  → return 15, stack = [20]
next() → pop 20, no right child → return 20, stack = []
hasNext() → false
```

#### **Java 實作**
```java
class BSTIterator {
    private Stack<TreeNode> stack = new Stack<>();

    public BSTIterator(TreeNode root) {
        pushLeft(root);
    }

    private void pushLeft(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    public int next() {
        TreeNode node = stack.pop();
        if (node.right != null) {
            pushLeft(node.right);   // lazily expand right subtree
        }
        return node.val;
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }
}
```

#### **Python 實作**
```python
class BSTIterator:
    def __init__(self, root):
        self.stack = []
        self._push_left(root)

    def _push_left(self, node):
        while node:
            self.stack.append(node)
            node = node.left

    def next(self) -> int:
        node = self.stack.pop()
        if node.right:
            self._push_left(node.right)
        return node.val

    def hasNext(self) -> bool:
        return bool(self.stack)
```

#### **積極式 vs 惰性式比較**
| 做法 | 初始化時間 | 初始化空間 | next() 時間 | next() 空間 | 適用情境 |
|----------|-----------|------------|-------------|--------------|----------|
| **積極式**（全部蒐集） | O(n) | O(n) | O(1) | O(1) | 小樹、呼叫次數多 |
| **惰性式**（堆疊） | O(h) | O(h) | 攤還 O(1) | O(1) | 大樹、只走一部分 |

#### **相似 LeetCode 題目**
| 題目 | LC # | 難度 | 惰性走訪如何套用 |
|---------|------|------------|----------------------------|
| Binary Search Tree Iterator | 173 | Medium | 核心模式——具備 `next()` / `hasNext()` 的迭代器 |
| Kth Smallest in BST | 230 | Medium | 彈出 k 次後就停，不必全部蒐集 |
| Inorder Successor in BST | 285 | Medium | 惰性走訪的其中一步 |
| Two Sum IV - Input is BST | 653 | Easy | 兩個迭代器（正向 + 反向）在中間相遇 |
| All Elements in Two BSTs | 1305 | Medium | 合併兩個惰性迭代器 |

#### **變形：合併兩個惰性迭代器（LC 1305）**

> **轉折**：**同時並行維護兩個獨立的左脊堆疊**，每次都從堆疊頂較小的那一邊彈出
> ——也就是合併排序的合併步驟，只是對象從兩個陣列換成兩棵 BST。這在空間上勝過
> 天真的「兩棵都攤平成串列再合併」：額外空間是 `O(h1 + h2)` 而不是 `O(m + n)`。

```java
// java
// LC 1305 - All Elements in Two Binary Search Trees
// IDEA: TWO LAZY IN-ORDER ITERATORS + merge step
// time = O(m + n), space = O(h1 + h2) excluding the output list
public List<Integer> getAllElements(TreeNode root1, TreeNode root2) {
    Deque<TreeNode> s1 = new ArrayDeque<>(), s2 = new ArrayDeque<>();
    List<Integer> res = new ArrayList<>();
    pushLeft(s1, root1);
    pushLeft(s2, root2);

    while (!s1.isEmpty() || !s2.isEmpty()) {
        // take from whichever iterator currently exposes the smaller value
        Deque<TreeNode> pick;
        if (s2.isEmpty() || (!s1.isEmpty() && s1.peek().val <= s2.peek().val)) {
            pick = s1;
        } else {
            pick = s2;
        }
        TreeNode node = pick.pop();
        pushLeft(pick, node.right);   // lazily expand only the stack we consumed
        res.add(node.val);
    }
    return res;
}

private void pushLeft(Deque<TreeNode> st, TreeNode node) {
    while (node != null) { st.push(node); node = node.left; }
}
```

```python
# python
# LC 1305 - All Elements in Two Binary Search Trees
# IDEA: TWO LAZY IN-ORDER ITERATORS + merge step
# time = O(m + n), space = O(h1 + h2) excluding the output list
class Solution(object):
    def getAllElements(self, root1, root2):
        st1, st2, res = [], [], []

        def push_left(st, node):
            while node:
                st.append(node)
                node = node.left

        push_left(st1, root1)
        push_left(st2, root2)

        while st1 or st2:
            # pop from the iterator exposing the smaller current value
            if not st2 or (st1 and st1[-1].val <= st2[-1].val):
                node = st1.pop()
                push_left(st1, node.right)
            else:
                node = st2.pop()
                push_left(st2, node.right)
            res.append(node.val)

        return res
```

**🚫 常見錯誤**：直接寫 `if st1[-1].val <= st2[-1].val` 卻沒先檢查兩個堆疊都非空
——其中一棵樹一定會比另一棵早很多耗盡。要*先*處理空堆疊的保護，再做比較。

#### **重點整理**
```text
1. Push LEFT SPINE only — this gives smallest-first access
2. On pop: expand right subtree's left spine lazily
3. Stack depth = O(h), not O(n) — critical for tall/large trees
4. Amortized O(1) per next(): each node is pushed and popped exactly once
5. Enables partial traversal — stop early without wasting work
```

### 模板 8：修復／還原 BST 問題 ⭐⭐⭐⭐⭐

#### **模式總覽**
- **說明**：利用中序走訪的性質偵測並修復 BST 中的違規之處
- **辨識訊號**：「recover」、「fix」、「被交換的節點」、「無效的 BST」
- **關鍵洞見**：**合法 BST 的中序走訪 = 嚴格遞增序列**
- **時間複雜度**：O(n)
- **空間複雜度**：遞迴為 O(h)，用 Morris 走訪則為 O(1)

#### **核心想法**

**別盯著樹看——去看它印出來的中序序列。**

合法 BST 的中序走訪是嚴格遞增的。只交換兩個節點的*值*，會以一種可完全刻畫的方式
破壞該序列，而刻畫方式就是**下降點**（`prev.val > cur.val` 的位置）。所以整個問題化簡成：

```text
1. Walk in-order, keeping ONE extra pointer: `prev` (the previously visited node)
2. Every time prev.val > cur.val  -> that's a DROP
      first  = prev   (only on the FIRST drop)   <- the "too large" node
      second = cur    (on EVERY drop)            <- the "too small" node
3. Swap first.val <-> second.val
```

完全不需要儲存整個序列——偵測所需的狀態只有 `prev`，這正是為什麼它能塞進一個
普通的遞迴中序走訪裡（以及稍後為了 O(1) 空間而塞進 Morris 走訪裡）。



```text
Core Insight:
  Valid BST in-order traversal → strictly INCREASING sequence

  Example valid BST:        In-order: [1, 2, 3, 4, 5, 6, 7]
         4                            ↑  ↑  ↑  ↑  ↑  ↑  ↑
        / \                          strictly increasing ✓
       2   6
      / \ / \
     1  3 5  7

  If two nodes SWAPPED:     In-order: [1, 6, 3, 4, 5, 2, 7]
         4                                 ↓        ↓
        / \                            DROP here  DROP here
       6   2    (swapped!)                 ↓        ↓
      / \ / \                         first=6   second=2
     1  3 5  7

Finding "drops" in sequence = finding swapped nodes!
```

#### **被交換節點的兩種情況**

```text
Case 1: ADJACENT nodes swapped (1 drop)
  Valid:   [1, 2, 3, 4, 5]
  Swapped: [1, 3, 2, 4, 5]  ← swap 2 and 3
                ↓
           one drop: 3 > 2
           first = 3 (prev at drop)
           second = 2 (current at drop)

Case 2: DISTANT nodes swapped (2 drops)
  Valid:   [1, 2, 3, 4, 5, 6, 7]
  Swapped: [1, 6, 3, 4, 5, 2, 7]  ← swap 2 and 6
                ↓           ↓
           drop 1: 6 > 3    drop 2: 5 > 2
           first = 6        second = 2 (update!)

Key: first is set at FIRST drop, second is ALWAYS updated at each drop
```

> **一條規則涵蓋兩種情況**：`if (first == null) first = prev; second = cur;`
> 只有 1 個下降點時，它指派該下降點的 `(prev, cur)`；有 2 個下降點時，它保留第一個的
> `prev`，並用最後一個的 `cur` 覆寫 `second`。不需要分情況討論。

#### **模式**

```python
# python
# LC 99 - Recover Binary Search Tree
# IDEA: IN-ORDER DFS + BST PROPERTY (in-order of a valid BST is strictly increasing)
# time = O(n), space = O(h) recursion stack
class Solution(object):
    def recoverTree(self, root):
        self.first = None    # the "too large" node  (prev at the 1st drop)
        self.second = None   # the "too small" node  (cur  at the last drop)
        self.prev = None     # previously visited node in in-order order

        self.helper(root)

        # NOTE !!! swap VALUES, never rewire the nodes
        if self.first and self.second:
            self.first.val, self.second.val = self.second.val, self.first.val

    def helper(self, node):
        if not node:
            return

        # 1) left
        self.helper(node.left)

        # 2) current : compare against the previous in-order node
        #    NOTE !!! `self.prev` guard — prev is None only for the leftmost node
        if self.prev and self.prev.val > node.val:
            if self.first is None:
                self.first = self.prev   # FIRST drop only
            self.second = node           # EVERY drop (overwrites on the 2nd)

        self.prev = node                 # current becomes previous

        # 3) right
        self.helper(node.right)
```

```java
// java
// LC 99 Recover Binary Search Tree
// Time: O(N), Space: O(H)

/**
 * Key variables:
 * - first:  The first node where order is violated (the larger one in first drop)
 * - second: The second node where order is violated (the smaller one in last drop)
 * - prev:   The previously visited node in in-order traversal
 */
private TreeNode first = null;
private TreeNode second = null;
// NOTE: keep prev = null and guard with `prev != null`.
// A `new TreeNode(Integer.MIN_VALUE)` sentinel is NOT safe here — LC 99 allows
// node values down to -2^31, so a real node can equal the sentinel.
private TreeNode prev = null;

public void recoverTree(TreeNode root) {
    if (root == null) return;

    // Step 1: In-order traversal to find the two swapped nodes
    inorder(root);

    // Step 2: Swap the values (not the nodes themselves!)
    if (first != null && second != null) {
        int temp = first.val;
        first.val = second.val;
        second.val = temp;
    }
}

private void inorder(TreeNode root) {
    if (root == null) return;

    // 1. Go Left
    inorder(root.left);

    // 2. Process current node - detect violation
    if (prev != null && root.val < prev.val) {
        // Found a DROP in the sequence!
        if (first == null) {
            first = prev;    // First drop: prev is the "too large" node
        }
        second = root;       // Always update: current is the "too small" node
    }

    // Update prev for next comparison
    prev = root;

    // 3. Go Right
    inorder(root.right);
}
```

#### **追問：用 Morris 走訪達到 O(1) 空間**

偵測邏輯*完全不變*——改變的只有走訪機制。Morris 會把每個節點的中序前驅串接指向它，
走完之後再把串接拆掉。

```python
# python
# LC 99 - Recover BST, O(1) extra space
# IDEA: MORRIS IN-ORDER + the same (first / second / prev) drop detection
# time = O(n) (each edge traversed at most 3x), space = O(1)
class Solution(object):
    def recoverTree(self, root):
        first = second = prev = None
        cur = root

        while cur:
            if not cur.left:
                # no left subtree -> visit cur, then go right
                if prev and prev.val > cur.val:
                    if not first:
                        first = prev
                    second = cur
                prev = cur
                cur = cur.right
            else:
                # find in-order predecessor of cur
                pred = cur.left
                while pred.right and pred.right is not cur:
                    pred = pred.right

                if not pred.right:
                    pred.right = cur      # thread it, descend left
                    cur = cur.left
                else:
                    pred.right = None     # NOTE !!! un-thread (restore structure)
                    if prev and prev.val > cur.val:
                        if not first:
                            first = prev
                        second = cur
                    prev = cur
                    cur = cur.right

        if first and second:
            first.val, second.val = second.val, first.val
```

> Morris 會暫時**修改**這棵樹（右指標變成串接線）。所有串接線都會在走訪結束前移除，
> 因此最終結構完好無損——但走訪進行到一半時，這棵樹無法安全地被並行讀取。

#### **這個模式為什麼有效**

```text
The algorithm handles BOTH cases with ONE logic:

1. "first" is set only ONCE at the first drop
   → This captures the "too large" node that was swapped

2. "second" is ALWAYS updated at every drop
   → For adjacent swap: only 1 drop, second = the "too small" node ✓
   → For distant swap: 2 drops, second gets overwritten to correct node ✓

Example (distant swap: 2 and 6):
  Sequence: [1, 6, 3, 4, 5, 2, 7]

  At index 2 (val=3): prev=6, curr=3, 6 > 3 → DROP!
    first = 6 (set once)
    second = 3

  At index 5 (val=2): prev=5, curr=2, 5 > 2 → DROP!
    first = 6 (unchanged)
    second = 2 (updated!) ← This is the correct second node

  Swap 6 and 2 → BST recovered ✓
```

#### **相似 LeetCode 題目**

以下這些題目都是**同一個骨架**：帶著 `prev` 指標的中序走訪。
變的只有「處理當前節點」那一步。

| 題目 | LC # | 難度 | `prev` 那一步做什麼 | 為什麼用中序？ |
|---------|------|------------|---------------------------|---------------|
| **Recover BST** | 99 | Medium | 記錄下降點（`prev.val > cur.val`） | 在排序序列中找出被交換的節點 |
| **Validate BST** | 98 | Medium | 只要有下降點就拒絕 | 驗證序列嚴格遞增 |
| **Min Diff in BST** | 530 / 783 | Easy | `ans = min(ans, cur.val - prev.val)` | 排序後最接近的一對必定相鄰 |
| **Find Mode in BST** | 501 | Easy | 計算相等值的連續長度 | 排序後重複值必定相連 |
| **Kth Smallest** | 230 | Medium | 遞減計數器，歸零時停止 | 中序即為排序順序 |
| **Inorder Successor in BST** | 285 | Medium | 當 `prev == target` 時回傳 `cur` | 後繼 = 中序的下一個節點 |
| **BST Iterator** | 173 | Medium | 用顯式堆疊暫停／恢復 | 惰性模擬中序走訪 |
| **Convert BST to Sorted DLL** | 426 | Medium | 串接 `prev.right = cur; cur.left = prev` | 排序順序即串列順序 |
| **Increasing Order Search Tree** | 897 | Easy | 把每個節點重新掛到 `prev.right` | 攤平成只有右子的鏈 |
| **Two Sum IV** | 653 | Easy | 在排序串列上做雙指標 | 中序給出排序陣列 |
| **Convert to Greater Tree** | 538 / 1038 | Medium | **反向**中序 + 累計和 | 以遞減順序處理 |

**辨識訊號** — 只要題目問的是 BST 中*相鄰值之間的順序關係*（後繼、最小差、眾數、
把順序弄壞的一次交換），就先想中序 + `prev`，其他先放一邊。

#### **常見錯誤**

**🚫 錯誤 1：試圖交換節點而不是交換值**

```text
Why swap VALUES, not NODE OBJECTS?

  Swapping node objects means rewiring parent/child pointers for BOTH nodes,
  plus handling edge cases (root, adjacent nodes, left/right children).
  This is extremely complex and error-prone.

  Swapping values is O(1) and preserves the tree structure:
  - All parent→child links stay the same
  - All left/right subtree relationships stay the same
  - Only the .val fields change — the BST property is restored

  Example:
       4                    4
      / \    swap vals      / \
     6   2   of 6 & 2 →   2   6    ← valid BST!
    / \ / \               / \ / \
   1  3 5  7             1  3 5  7

  The nodes stay in the SAME positions. Only their values change.
  Tree structure (edges) is completely untouched.
```

```java
// BAD: Swapping node references does NOTHING to the tree
TreeNode temp = first;
first = second;       // Only swaps local variable pointers!
second = temp;        // The actual tree is unchanged.

// GOOD: Swap the values stored inside the nodes
int temp = first.val;
first.val = second.val;
second.val = temp;
```

**🚫 錯誤 2：只處理相鄰交換的情況**
```java
// BAD: Stops after first drop
if (prev.val > root.val) {
    first = prev;
    second = root;
    return;  // Wrong! Miss the second drop for distant swaps
}

// GOOD: Always update second, handles both cases
if (prev.val > root.val) {
    if (first == null) first = prev;
    second = root;  // Always update!
}
```

**🚫 錯誤 3：用哨兵 `prev` 取代 null 判斷**
```java
// BAD: LC 99 allows node values down to -2^31, so a real node can EQUAL
//      the sentinel — and a float('-inf') sentinel in Python isn't a TreeNode.
TreeNode prev = new TreeNode(Integer.MIN_VALUE);

// GOOD: prev = null, and guard the comparison. null happens exactly once
//       (the leftmost node), where there is nothing to compare against anyway.
TreeNode prev = null;
...
if (prev != null && prev.val > root.val) { ... }
```

**🚫 錯誤 4：在第二個下降點時重設 `first`**
```python
# BAD: overwrites the correct answer on a distant swap
if prev.val > node.val:
    first = prev      # gets clobbered at the 2nd drop -> wrong pair
    second = node

# GOOD: first is write-once, second is write-always
if prev.val > node.val:
    if first is None:
        first = prev
    second = node
```

#### **複雜度分析**
- **時間**：O(N)——每個節點恰好拜訪一次
- **空間**：O(H)——遞迴堆疊（H = 樹高）
- **追問 O(1) 空間**：改用 Morris 走訪（會暫時修改樹）

#### **重點整理**

```text
1. In-order traversal of valid BST = STRICTLY INCREASING sequence
   → This is the MOST IMPORTANT property for BST problems

2. Detecting violations = finding "drops" in the sequence
   → prev.val > curr.val means we found a violation

3. Two-drop pattern handles both adjacent and distant swaps
   → first: set at FIRST drop (the larger misplaced node)
   → second: ALWAYS update (the smaller misplaced node)

4. Swap VALUES, not nodes
   → Much simpler, preserves tree structure
```

### 模板 9：把 BST 當成有序集合／順序統計樹 ⭐⭐⭐⭐⭐

#### **模式總覽**
- **說明**：把 BST 當成一個**動態的有序多重集合**——值一個接一個串流進來，而你必須在
  每次插入之後回答*排名／順序*類的問題（第 k 大、第 k 小、「有多少個小於 x」）。
- **辨識訊號**：「in a stream」、「每次插入後」、「目前為止的第 k 大」、「動態中位數」、
  「比它小的元素個數」，以及任何你會想拿 `TreeMap` /
  `SortedList` 來解的場合。
- **關鍵洞見**：**替每個節點加上 `count` = 其子樹的大小。** 多了這一個欄位，
  BST 就變成*順序統計樹*：一次排名查詢就化為一次從根往下的走訪，跟搜尋一模一樣。
- **時間**：每次插入與每次排名查詢皆 O(h)——平衡樹上是 O(log n)，最差是 O(n)
- **空間**：樹本身 O(n)

#### **核心想法**

```text
Plain BST answers "is x present?".
Augmented BST answers "what is the kth largest?" — because each node knows
how many values live beneath it.

           node
          /    \
      left      right
    (a nodes) (b nodes)

  The b values in `right` are ALL larger than node.val.
  So, ranking from the largest downwards:

    ranks 1 .. b        → live in the right subtree
    rank  b + 1         → is node itself
    ranks b + 2 ..      → live in the left subtree

  kthLargest(node, k):
      b = size(node.right)
      if k <= b       → recurse right, k unchanged
      if k == b + 1   → return node.val
      else            → recurse left with k - (b + 1)
                         (we just skipped the right subtree AND node)
```

#### **圖解追蹤** — `k = 3`，串流 `[4, 5, 8, 2]` 之後再 `add(3)`

```text
BST after [4,5,8,2] then inserting 3   (count in parentheses)

              4(5)
             /    \
          2(2)     5(2)
             \        \
             3(1)      8(1)

kthLargest(root=4, k=3):
  b = size(node.right = 5) = 2      → subtree {5, 8} holds the 1st and 2nd largest
  k = 3 == b + 1                    → node 4 IS the 3rd largest → return 4   ✓
  (sorted values {2,3,4,5,8}; 3rd largest = 4)

add(5) next → tree grows to {2,3,4,5,5,8}, kthLargest(root, 3) = 5
```

#### **Java 實作**
```java
// java
// LC 703 - Kth Largest Element in a Stream
// IDEA: ORDER-STATISTIC BST — augment each node with subtree size, then rank-descend
// time = O(h) per add (O(log n) balanced, O(n) skewed), space = O(n)
class KthLargest {

    private static class Node {
        int val;
        int count = 1;          // number of nodes in the subtree rooted here
        Node left, right;
        Node(int v) { val = v; }
    }

    private final int k;
    private Node root;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        for (int v : nums) root = insert(root, v);
    }

    private Node insert(Node node, int val) {
        if (node == null) return new Node(val);
        node.count++;                                    // one more node below/at this root
        if (val < node.val) node.left = insert(node.left, val);
        else node.right = insert(node.right, val);       // duplicates go right (multiset)
        return node;
    }

    private int size(Node node) { return node == null ? 0 : node.count; }

    private int kthLargest(Node node, int k) {
        int rightSize = size(node.right);                // count of values > node.val
        if (k <= rightSize) return kthLargest(node.right, k);
        if (k == rightSize + 1) return node.val;         // node itself is the answer
        return kthLargest(node.left, k - rightSize - 1); // skipped right subtree + node
    }

    public int add(int val) {
        root = insert(root, val);
        return kthLargest(root, k);
    }
}
```

#### **Python 實作**
```python
# python
# LC 703 - Kth Largest Element in a Stream
# IDEA: ORDER-STATISTIC BST — augment each node with subtree size, then rank-descend
# time = O(h) per add (O(log n) balanced, O(n) skewed), space = O(n)
class CountNode(object):
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None
        self.count = 1          # size of the subtree rooted here


class KthLargest(object):
    def __init__(self, k, nums):
        self.k = k
        self.root = None
        for v in nums:
            self.root = self._insert(self.root, v)

    def _insert(self, node, val):
        if not node:
            return CountNode(val)
        node.count += 1                                   # subtree grew by 1
        if val < node.val:
            node.left = self._insert(node.left, val)
        else:
            node.right = self._insert(node.right, val)    # duplicates go right
        return node

    def _size(self, node):
        return node.count if node else 0

    def _kth_largest(self, node, k):
        right_size = self._size(node.right)               # values greater than node.val
        if k <= right_size:
            return self._kth_largest(node.right, k)
        if k == right_size + 1:
            return node.val                               # node itself
        return self._kth_largest(node.left, k - right_size - 1)

    def add(self, val):
        self.root = self._insert(self.root, val)
        return self._kth_largest(self.root, self.k)
```

#### **LC 703：BST vs 最小堆積**

| 做法 | add() 時間 | 空間 | 能回答任意排名嗎？ | 備註 |
|----------|-----------|-------|----------------------------|-------|
| **大小為 k 的最小堆積** | O(log k) | O(k) | ❌ 只能答第 k 大 | 面試最短的答案；先講這個 |
| **順序統計 BST** | 平均 O(log n) / 最差 O(n) | O(n) | ✅ 任意 k、第 k 小、x 的排名 | 可推廣；追問時會需要 |
| **排序串列 + 二分插入** | O(n) 搬移 | O(n) | ✅ | 搜尋是 O(log n)，但插入要搬記憶體 |

> **面試策略**：先針對題目字面給出堆積解，接著說
> *「如果 k 每次查詢都可能不同，或還要回答『有多少個小於 x』，我會在 BST 上加子樹計數
> ——那就是順序統計樹，任意排名都是 O(log n)。」*
> 再補一句：*「上線環境我會用平衡 BST（`TreeMap` / 紅黑樹），這樣歪斜造成的 O(n)
> 最差情況就不會發生。」*

#### **`count` 欄位能做的不只是第 k 大**

```text
With size-augmented nodes, all of these are O(h) descents:

  kthLargest(k)        → shown above
  kthSmallest(k)       → mirror it: use size(node.left)
  rank(x)              → count of values < x: add size(left)+1 each time you go right
  countInRange(lo,hi)  → rank(hi) - rank(lo)
  median              → kthSmallest(n/2) using size(root)

  ⚠️ The ONLY maintenance cost: every insert/delete must fix `count`
     on every node along the path. Forgetting this silently corrupts all ranks.
```

#### **相似 LeetCode 題目**
| 題目 | LC # | 難度 | 關聯 |
|---------|------|------------|----------|
| Kth Largest Element in a Stream | 703 | Easy | 核心題——動態排名查詢 |
| Kth Smallest Element in a BST | 230 | Medium | **靜態**版本；經典追問是*「如果 BST 經常被修改呢？」*→ 答案就是這個模板 |
| BST Iterator | 173 | Medium | 串流式中序，但是依位置而非依排名 |
| Insert into a BST | 701 | Medium | 這個模板的插入部分，只是沒有 `count` |

#### **重點整理**
```text
1. A bare BST answers membership; a SIZE-AUGMENTED BST answers RANK
2. kth largest = descend using size(right): k<=b → right, k==b+1 → node, else left with k-b-1
3. Bump `count` on the way DOWN during insert — every node on the path
4. Duplicates: send them consistently to one side (right here) → BST becomes a multiset
5. State the O(n) skewed worst case and name TreeMap / Red-Black as the fix

This is the standard answer to "what if the data keeps changing?" follow-ups
on LC 230 and any kth-element question.
```

### 模板 6b：BST 建構變形

[bst.md](./bst.md) 中 `模板 6` 沒有納入的部分：非陣列輸入、重建既有 BST 的情況，
以及列舉／計數這一組。

#### **核心建構模式（續）**

##### **模式 6.2：從排序鏈結串列建樹**（LC 109）
```python
def sorted_list_to_bst(head):
    """
    Convert sorted linked list to balanced BST
    Two approaches: 1) Two pointers to find middle
                   2) Inorder simulation
    Time: O(n log n) or O(n), Space: O(log n)
    """
    # Approach 1: Find middle with slow-fast pointers
    def find_middle(left, right):
        slow = fast = left
        while fast != right and fast.next != right:
            slow = slow.next
            fast = fast.next.next
        return slow

    def convert(left, right):
        if left == right:
            return None

        mid = find_middle(left, right)
        root = TreeNode(mid.val)
        root.left = convert(left, mid)
        root.right = convert(mid.next, right)
        return root

    return convert(head, None)
```

##### **模式 6.3：從前序走訪建樹**（LC 1008）
```python
def bst_from_preorder(preorder):
    """
    Construct BST from preorder traversal
    Use BST property: left < root < right
    Time: O(n), Space: O(h)
    """
    def build(min_val, max_val):
        nonlocal idx
        if idx >= len(preorder):
            return None

        val = preorder[idx]
        if val < min_val or val > max_val:
            return None

        idx += 1
        root = TreeNode(val)
        root.left = build(min_val, val)
        root.right = build(val, max_val)
        return root

    idx = 0
    return build(float('-inf'), float('inf'))
```

##### **模式 6.4：平衡一棵 BST**（LC 1382）⭐⭐⭐⭐⭐

**a. 核心想法**

> **別去重新平衡這棵樹——把它攤平再重建。** LC 1382 = **LC 94（中序）+ LC 108（排序陣列 → BST）** 黏在一起。

整個模式建立在 **BST ⟷ 排序陣列的對偶關係**上：

```text
   in-order DFS                      mid-as-root recursion
  ────────────────►                  ─────────────────────►
  BST  ──────────► sorted array ───► balanced BST
  (any shape)      [1,2,3,4]         (height = ceil(log2(n+1)))
   any BST flattens to               any sorted array rebuilds
   the SAME sorted order             to a BALANCED BST
```

**兩個問題、兩個答案——這就是題目的全部：**

| 問題 | 答案 | 為什麼 |
|----------|--------|-----|
| 用哪種走訪？ | **中序**（左 → 根 → 右） | 對 BST 而言，中序是*唯一*會產出**排序**輸出的走訪——而重建 BST 需要排序好的輸入 |
| 哪個元素當根？ | **中間**那個 | 中點把區間切成大小相差 ≤ 1 的兩半 → 大小平衡在*每一層*都成立 → 深度條件遞迴地被滿足 |

**為什麼「中點當根」會得到平衡樹（關鍵論證）：**

```text
build(l, r) puts nodes[mid] on top, with
   left  subtree = build(l, mid-1)   -> size = mid - l
   right subtree = build(mid+1, r)   -> size = r - mid
   |left size - right size| <= 1     at EVERY node
=> heights differ by at most 1       at EVERY node   ← exactly the problem's definition
=> total height = floor(log2(n)) + 1 = ceil(log2(n+1))
```

*（已驗證：n=7 → h=3、n=15 → h=4、n=100 → h=7、n=10000 → h=14）*

**存值 vs 存節點——兩種合法寫法：**

| 寫法 | 蒐集 | 重建 | 備註 |
|--------|---------|---------|------|
| **蒐集值** | `arr.append(node.val)` | `TreeNode(arr[mid])` | 配置 n 個新節點；舊樹不動。最容易推理。 |
| **蒐集節點**（重用） | `nodes.append(node)` | `root = nodes[mid]` | 不需配置記憶體。**之所以安全，是因為你對每個重用節點都同時指派了 `.left` 和 `.right`**——所有殘留指標都被覆寫掉了。 |

> ⚠️ 如果你重用節點卻*有條件地*指派子節點（例如只在區間非空時才設 `.left`），舊樹的殘留指標就會活下來，造成環或重複子樹。務必兩邊都指派——是 `build()` 回傳 `None` 才把它們清掉的。

**b. 模式**

```python
# python
# LC 1382 - Balance a Binary Search Tree
# IDEA: IN-ORDER DFS (BST -> sorted array) + mid-as-root rebuild (LC 108)
# time  = O(n)  -- n for the traversal, n for the rebuild
# space = O(n)  -- the array; + O(h_in) recursion for in-order, O(log n) for the build
class Solution(object):
    def balanceBST(self, root):
        if not root:
            return None

        self.arr = []
        self.in_order(root)                              # step 1: BST -> sorted
        return self.build(0, len(self.arr) - 1)          # step 2: sorted -> balanced BST

    # NOTE !!! in-order, because ONLY in-order gives sorted output for a BST
    def in_order(self, node):
        if not node:
            return
        self.in_order(node.left)
        self.arr.append(node.val)
        self.in_order(node.right)

    # NOTE !!! index bounds, NOT slicing -> avoids O(n log n) copying
    def build(self, l, r):
        # base case: empty range -> None (this also CLEARS stale child pointers)
        if l > r:
            return None

        mid = l + (r - l) // 2                           # middle becomes root
        node = TreeNode(self.arr[mid])
        node.left = self.build(l, mid - 1)               # left half
        node.right = self.build(mid + 1, r)              # right half
        return node
```

```python
# python
# LC 1382 - node-reuse variant (no new allocations)
# time = O(n), space = O(n) for the node list
class Solution(object):
    def balanceBST(self, root):
        nodes = []

        def in_order(node):
            if not node:
                return
            in_order(node.left)
            nodes.append(node)                # collect NODES, not values
            in_order(node.right)

        def build(l, r):
            if l > r:
                return None
            mid = l + (r - l) // 2
            node = nodes[mid]
            node.left = build(l, mid - 1)      # MUST assign both children,
            node.right = build(mid + 1, r)     # else old pointers linger
            return node

        in_order(root)
        return build(0, len(nodes) - 1)
```

```java
// java
// LC 1382 - Balance a Binary Search Tree
// IDEA: in-order into a list, then rebuild with mid-as-root
// time = O(n), space = O(n)
public TreeNode balanceBST(TreeNode root) {
    List<Integer> sorted = new ArrayList<>();
    inorder(root, sorted);
    return build(sorted, 0, sorted.size() - 1);
}

private void inorder(TreeNode node, List<Integer> out) {
    if (node == null) return;
    inorder(node.left, out);
    out.add(node.val);           // in-order => ascending
    inorder(node.right, out);
}

private TreeNode build(List<Integer> a, int l, int r) {
    if (l > r) return null;
    int mid = l + (r - l) / 2;   // NOTE: not (l + r) / 2 -> avoids int overflow
    TreeNode node = new TreeNode(a.get(mid));
    node.left  = build(a, l, mid - 1);
    node.right = build(a, mid + 1, r);
    return node;
}
```

**圖解追蹤** — `root = [1,null,2,null,3,null,4]`（完全右歪斜，h = 4）：

```text
 step 1: in-order          step 2: build(0, 3), mid = 0 + (3-0)//2 = 1
   1                          arr = [1, 2, 3, 4]
    \                                    ^ mid
     2       in-order                            2                h = 3
      \      ────────►  [1, 2, 3, 4]           /   \
       3                                      1     3      build(0,0) | build(2,3)
        \                                            \
         4                                            4     build(3,3), mid=3
```

> **兩種中點慣例都會被接受**（LC 說「回傳任一即可」）：
> - 對索引邊界用 `mid = (l + r) // 2` → 根為 `2` → `[2,1,3,null,null,null,4]`（符合 LC 的範例輸出）
> - 用切片搭配 `mid = len(arr) // 2` → 根為 `3` → `[3,2,4,1]`
>
> 兩者高度都是 3，中序都是 `[1,2,3,4]`。面試時別浪費時間糾結——直接說「取左中或右中，兩種都平衡」。

**常見陷阱**

```python
# ❌ Pre-order / post-order -> NOT sorted -> the rebuilt tree isn't a BST at all
# ❌ Slicing: build(arr[:mid]) + build(arr[mid+1:])  -> O(n log n) copying (works, but wasteful)
# ❌ Reusing nodes with only `if l <= mid-1: node.left = ...`  -> stale pointers survive
# ❌ mid = (l + r) / 2 in Java -> int overflow on huge ranges; use l + (r - l) / 2
# ❌ Recursive in-order on a FULLY SKEWED BST with n = 10^4 (LC 1382's max):
#      Python default recursionlimit is 1000 -> RecursionError.
#      Fix: iterative in-order (stack), or sys.setrecursionlimit(10**5)
```

```python
# ✅ iterative in-order — immune to the skewed-tree recursion blowup
def in_order_iter(root):
    out, stack, cur = [], [], root
    while cur or stack:
        while cur:
            stack.append(cur)
            cur = cur.left
        cur = stack.pop()
        out.append(cur.val)
        cur = cur.right
    return out
```

> 注意：**重建**這段遞迴依建構方式只會有 `O(log n)` 深，所以只有*攤平*那一步有風險。

**選讀：O(1) 空間——Day–Stout–Warren（DSW）**

面試很少考，但它就是「你能不能**原地**做？」的答案：

```text
1. VINE     : right-rotate away every left child  -> a right-leaning linked list
              (root)-> 1 -> 2 -> 3 -> 4 -> ...
2. COUNT    : walk the vine to get n
3. COMPRESS : m = 2^floor(log2(n+1)) - 1          (size of the largest perfect tree)
              make_rotations(n - m)               (level the excess leaves)
              while m > 1: m //= 2; make_rotations(m)   (halve repeatedly)
```
每次 `make_rotations(k)` 沿著藤蔓做 `k` 次左旋，一次摺疊一層。**time = O(n), space = O(1)**——相較之下攤平重建需要 O(n) 空間。參見 `leetcode_python/Binary_Search_Tree/balance-a-binary-search-tree.py` 中的 `V2-2`（需要 `import math`）。

**c. 相似 LC**

**直系兄弟——「排序序列 → 平衡 BST」**（同一套 `build(l, r)` 中點當根的遞迴）：

| 題目 | LC # | 難度 | 輸入 | 與 1382 的差異 |
|---------|------|------------|-------|----------------------|
| Balance a Binary Search Tree | **1382** | Medium | 不平衡的 BST | 標準題：需要先做中序攤平那一步 |
| Convert Sorted Array to BST | **108** | Easy | 排序陣列 | **1382 少掉第 1 步**——陣列直接給你 |
| Convert Sorted List to BST | **109** | Medium | 排序鏈結串列 | 沒有隨機存取 → 用快慢指標找中點（O(n log n)），或用中序模擬（O(n)） |
| Construct BST from Preorder Traversal | 1008 | Medium | 前序陣列 | 改用 `(min, max)` 邊界而非中點——見模式 6.3 |
| Serialize and Deserialize BST | 449 | Medium | BST → 字串 → BST | 來回轉換；反序列化就是同一套帶邊界的重建 |

**另一半——「BST → 排序序列」**（1382 的第 1 步，到處都用得上）：

| 題目 | LC # | 拿中序序列來做什麼 |
|---------|------|------------------------------------------|
| Binary Tree Inorder Traversal | 94 | 攤平本身 |
| Kth Smallest Element in a BST | 230 | 走到第 k 個就停（提早結束） |
| Validate Binary Search Tree | 98 | 檢查序列是否嚴格遞增 |
| Recover Binary Search Tree | 99 | 找出序列中的 1–2 個逆序（模板 8） |
| Minimum Distance Between BST Nodes | 783 / 530 | 相鄰元素之間的最小差 |
| Convert BST to Sorted Doubly Linked List | 426 | 重接中序鄰居，而不是重建 |
| Increasing Order Search Tree | 897 | 重建成右歪斜的藤蔓——**恰好是 1382 的反面** |
| All Elements in Two BSTs | 1305 | 合併兩條中序串流（模板 5b） |
| Convert BST to Greater Tree | 538 / 1038 | **反向**中序 + 累計後綴和 |

**也值得一起看：**

| 題目 | LC # | 關聯 |
|---------|------|----------|
| Balanced Binary Tree | 110 | *檢查*那個由 1382 所*產生*的平衡條件 |
| Maximum Depth of Binary Tree | 104 | 「相差超過 1」背後的高度度量 |
| Unique BSTs II / I | 95 / 96 | 同樣的挑中點遞迴，但改成列舉／計數*所有*根，而不只是中間那個（模式 6.5 / 6.6） |

**重點整理**
1. **腦中出現 BST + 「排序」→ 想中序。** 中序是雙向的橋樑。
2. **「平衡」→ 挑中間那個。** 中點當根讓 `|left| - |right| ≤ 1` 遞迴地成立。
3. **1382 = 94 + 108。** 認出這點，15 行就寫完。
4. 優先用**索引邊界**而非切片；若重用節點，**兩個子節點都要指派**。
5. **要 O(1) 空間？** → 說「Day–Stout–Warren：先拉成藤蔓，再壓縮」。

##### **模式 6.5：產生所有相異 BST（用笛卡兒積做遞迴建構）**（LC 95）

**核心想法**：
- 依序挑 `[start, end]` 中的每個數字 `i` 當根
- `[start, i-1]` 內的所有數字必須構成**左**子樹（BST 性質）
- `[i+1, end]` 內的所有數字必須構成**右**子樹（BST 性質）
- 以 `i` 為根的相異樹總數 = 所有左子樹 × 所有右子樹的**笛卡兒積**
- 基底情況：當 `start > end` 時回傳 `[null]`（空子樹，不是空串列）
- 總數符合 **Catalan 數**：C(n) = (2n)! / ((n+1)! × n!)

**做法**：
1. **純遞迴** — 直接列舉所有組合（有重疊子問題）
2. **記憶化遞迴** — 快取 `(start, end) → List<TreeNode>` 以避免重算
3. **迭代式 DP** — 由下而上的表格 `dp[start][end]`，依視窗大小遞增填表
4. **空間優化 DP** — `dp[numberOfNodes]`，右子樹用複製 + 位移處理

```python
def generate_trees(n):
    """
    Generate all structurally unique BSTs with n nodes
    Catalan number of trees: C(n) = (2n)! / ((n+1)! * n!)
    Time: O(4^n / n^(3/2)), Space: O(4^n / n^(3/2))
    """
    if n == 0:
        return []

    def generate(start, end):
        if start > end:
            return [None]

        all_trees = []
        for root_val in range(start, end + 1):
            left_trees = generate(start, root_val - 1)
            right_trees = generate(root_val + 1, end)

            for left in left_trees:
                for right in right_trees:
                    root = TreeNode(root_val)
                    root.left = left
                    root.right = right
                    all_trees.append(root)

        return all_trees

    return generate(1, n)
```

```java
// Java — Plain Recursion (LC 95)
public List<TreeNode> generateTrees(int n) {
    if (n == 0) return new ArrayList<>();
    return buildTrees(1, n);
}

private List<TreeNode> buildTrees(int start, int end) {
    List<TreeNode> allTrees = new ArrayList<>();
    if (start > end) {
        allTrees.add(null);  // important: null represents empty subtree
        return allTrees;
    }
    for (int i = start; i <= end; i++) {
        // left subtree candidates: [start, i-1]
        List<TreeNode> leftSubtrees = buildTrees(start, i - 1);
        // right subtree candidates: [i+1, end]
        List<TreeNode> rightSubtrees = buildTrees(i + 1, end);
        // Cartesian product: connect each left × right to root i
        for (TreeNode left : leftSubtrees) {
            for (TreeNode right : rightSubtrees) {
                TreeNode root = new TreeNode(i);
                root.left = left;
                root.right = right;
                allTrees.add(root);
            }
        }
    }
    return allTrees;
}
```

```java
// Java — Memoized Recursion (LC 95)
public List<TreeNode> generateTrees_memo(int n) {
    Map<Pair<Integer, Integer>, List<TreeNode>> memo = new HashMap<>();
    return allPossibleBST(1, n, memo);
}

private List<TreeNode> allPossibleBST(int start, int end,
        Map<Pair<Integer, Integer>, List<TreeNode>> memo) {
    List<TreeNode> res = new ArrayList<>();
    if (start > end) { res.add(null); return res; }
    if (memo.containsKey(new Pair<>(start, end)))
        return memo.get(new Pair<>(start, end));
    for (int i = start; i <= end; ++i) {
        List<TreeNode> leftSubs = allPossibleBST(start, i - 1, memo);
        List<TreeNode> rightSubs = allPossibleBST(i + 1, end, memo);
        for (TreeNode left : leftSubs)
            for (TreeNode right : rightSubs)
                res.add(new TreeNode(i, left, right));
    }
    memo.put(new Pair<>(start, end), res);
    return res;
}
```

**相似 LeetCode 題目**：
- LC 95: Unique Binary Search Trees II（產生全部）
- LC 96: Unique Binary Search Trees（只計數——Catalan 數 DP）
- LC 241: Different Ways to Add Parentheses（同樣的笛卡兒積模式）
- LC 894: All Possible Full Binary Trees
- LC 1382: Balance a Binary Search Tree

##### **模式 6.6：計算相異 BST 的數量**（LC 96）
```python
def num_trees(n):
    """
    Count number of structurally unique BSTs with n nodes
    Uses Catalan Number: C(n) = C(0)*C(n-1) + C(1)*C(n-2) + ... + C(n-1)*C(0)
    Time: O(n^2), Space: O(n)
    """
    dp = [0] * (n + 1)
    dp[0] = dp[1] = 1

    for nodes in range(2, n + 1):
        for root in range(1, nodes + 1):
            left = root - 1
            right = nodes - root
            dp[nodes] += dp[left] * dp[right]

    return dp[n]
```

#### **Java 實作：從前序建樹（LC 1008）**
```java
// Pattern 6.3: From Preorder (LC 1008)
private int idx = 0;

public TreeNode bstFromPreorder(int[] preorder) {
    return build(preorder, Integer.MIN_VALUE, Integer.MAX_VALUE);
}

private TreeNode build(int[] preorder, int min, int max) {
    if (idx >= preorder.length) return null;

    int val = preorder[idx];
    if (val < min || val > max) return null;

    idx++;
    TreeNode root = new TreeNode(val);
    root.left = build(preorder, min, val);
    root.right = build(preorder, val, max);
    return root;
}
```

#### **關鍵概念與原則**

1. **平衡建構**
   - 永遠挑中間元素當根，以確保平衡
   - 平衡 BST 的高度是 O(log n)
   - 不平衡則可能退化成 O(n)

2. **Catalan 數**
   - n 個節點的相異 BST 數量 = 第 n 個 Catalan 數
   - 公式：C(n) = (2n)! / ((n+1)! × n!)
   - 遞迴式：C(n) = Σ C(i) × C(n-1-i)，i 從 0 到 n-1

3. **走訪的性質**
   - **前序**：可唯一重建 BST（根在最前）
   - **中序**：給出排序序列（還需要前序或後序才夠）
   - **後序**：可唯一重建 BST（根在最後）

4. **優化技巧**
   - 用索引取代陣列切片（每次呼叫省下 O(n) 空間）
   - 對「產生全部」類問題快取結果
   - 能用迭代就用迭代

#### **常見錯誤與陷阱**

**🚫 錯誤 1：陣列切片的額外開銷**
```python
# BAD: Creates new arrays O(n) space each recursion
def build(nums):
    mid = len(nums) // 2
    root.left = build(nums[:mid])  # O(n) space

# GOOD: Use indices
def build(left, right):
    mid = (left + right) // 2
    root.left = build(left, mid - 1)
```

**🚫 錯誤 2：差一錯誤**
```python
# BAD: Wrong boundary
mid = len(nums) // 2
root.left = build(nums[:mid-1])  # Skips element!

# GOOD: Correct boundaries
root.left = build(nums[:mid])  # Includes all left elements
```

**🚫 錯誤 3：前序建樹時沒檢查邊界**
```python
# BAD: No bounds checking
def build():
    val = preorder[idx]
    root = TreeNode(val)  # May violate BST property!

# GOOD: Check min/max bounds
def build(min_val, max_val):
    if val < min_val or val > max_val:
        return None
```

**🚫 錯誤 4：寫錯 Catalan 遞迴式**
```python
# BAD: Wrong combination
for i in range(1, n+1):
    dp[n] += dp[i] + dp[n-i]  # Should multiply!

# GOOD: Multiply left and right counts
for i in range(1, n+1):
    dp[n] += dp[i-1] * dp[n-i]
```

**🚫 錯誤 5：中點算錯**
```python
# BAD: Can overflow in Java/C++
mid = (left + right) / 2

# GOOD: Prevent overflow
mid = left + (right - left) // 2
```

### 模板 3c：拆離與刪除的變形

[bst.md](./bst.md) 中的 `模板 3` 移除一個節點並回傳一個根。以下兩者改變了「移除」的意義：
LC 1110 回傳一片*森林*，而下面的 LC 450 變形是以交換值取代重接指標。

#### **變形：刪除節點並回傳森林（LC 1110）**

> **轉折**：一樣是*遞迴 + 用回傳值重新接回*的慣用寫法，但對象是**一般二元樹**，而且你必須**蒐集被孤立的根**，而不是回傳單一個根。一個節點會成為新的森林根，恰好發生在它的父節點剛被刪除時——所以把這個資訊當成 `isRoot` 旗標往遞迴下方傳。

```java
// java
// LC 1110 - Delete Nodes And Return Forest
// IDEA: DFS + "return null to detach" + pass `isRoot` down
// time = O(n), space = O(h + d)   d = |to_delete|
// NOTE: keep the state LOCAL — instance fields would leak between calls
//       (LeetCode reuses one Solution object for every test case)
public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
    Set<Integer> toDelete = new HashSet<>();
    for (int v : to_delete) toDelete.add(v);
    List<TreeNode> forest = new ArrayList<>();
    walk(root, true, toDelete, forest);
    return forest;
}

private TreeNode walk(TreeNode node, boolean isRoot, Set<Integer> toDelete, List<TreeNode> forest) {
    if (node == null) return null;
    boolean deleted = toDelete.contains(node.val);

    // a surviving node whose parent vanished starts a new tree
    if (isRoot && !deleted) forest.add(node);

    // children are "roots" iff THIS node is being deleted
    node.left  = walk(node.left,  deleted, toDelete, forest);
    node.right = walk(node.right, deleted, toDelete, forest);

    // returning null is what actually detaches this node from its parent
    return deleted ? null : node;
}
```

```python
# python
# LC 1110 - Delete Nodes And Return Forest
# IDEA: DFS + "return None to detach" + pass `is_root` down
# time = O(n), space = O(h + d)
class Solution(object):
    def delNodes(self, root, to_delete):
        to_del = set(to_delete)
        forest = []

        def dfs(node, is_root):
            if not node:
                return None
            deleted = node.val in to_del

            # surviving node with a deleted parent → new forest root
            if is_root and not deleted:
                forest.append(node)

            # children become roots only if THIS node is deleted
            node.left = dfs(node.left, deleted)
            node.right = dfs(node.right, deleted)

            return None if deleted else node

        dfs(root, True)
        return forest
```

**為什麼用後序？** 你必須*先*遞迴再回傳，否則會在存活的後代被提拔進森林之前就把節點拆掉
——這跟在 `trimBST` 裡太早回傳 `None` 是同一個陷阱。

#### **變形：以值交換方式刪除（LC 450，Python）**

> **轉折**：不回傳替代的子樹，而是把目標的值與其右子樹的最小值交換，然後往**兩個**子節點
> 都遞迴下去。之所以放在標準寫法旁邊，是因為這是大多數人第一次會寫出來的形狀——也因為
> 它的代價值得看清楚：兩邊都遞迴會讓複雜度變成 O(n)，而不是 O(h)。

```python
# python code

# LC 450 Delete Node in a BST
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
                root.val, right.val = right.val, root.val
        root.left = self.deleteNode(root.left, key)
        root.right = self.deleteNode(root.right, key)
        return root
```

### 模板 4b：驗證之外的邊界傳遞

`模板 4` 把一個合法的 `(min, max)` 視窗沿著遞迴往下傳，並在節點跨出視窗時**拒絕**。
保留這個視窗，只改變你在邊界上要做的事，同一套骨架就能回答另一個問題。

#### **變形：用邊界傳遞來*度量*而非驗證（LC 1026）**

> **轉折**：跟 `isValidBST` 一模一樣的 `(min, max)` 沿遞迴往下傳，
> 但節點跨出邊界時你不是**拒絕**，而是**度量**它跨出了多遠。沿著當前路徑攜帶所有祖先的
> 動態最小／最大值；答案就是任一葉節點處達到的最大 `max - min`。這對任何二元樹都適用
> ——不需要 BST 的順序性質。

```java
// java
// LC 1026 - Maximum Difference Between Node and Ancestor
// IDEA: BOUNDS PROPAGATION (same skeleton as LC 98, but maximize instead of reject)
// time = O(n), space = O(h)
public int maxAncestorDiff(TreeNode root) {
    if (root == null) return 0;
    return dfs(root, root.val, root.val);
}

private int dfs(TreeNode node, int mn, int mx) {
    // at a leaf's child, the widest ancestor spread on this path is mx - mn
    if (node == null) return mx - mn;

    mn = Math.min(mn, node.val);
    mx = Math.max(mx, node.val);

    return Math.max(dfs(node.left, mn, mx), dfs(node.right, mn, mx));
}
```

```python
# python
# LC 1026 - Maximum Difference Between Node and Ancestor
# IDEA: BOUNDS PROPAGATION (same skeleton as LC 98, but maximize instead of reject)
# time = O(n), space = O(h)
class Solution(object):
    def maxAncestorDiff(self, root):
        def dfs(node, mn, mx):
            if not node:
                return mx - mn          # widest spread seen on this path
            mn = min(mn, node.val)
            mx = max(mx, node.val)
            return max(dfs(node.left, mn, mx), dfs(node.right, mn, mx))

        if not root:
            return 0
        return dfs(root, root.val, root.val)
```

```text
Key idea: |a - b| over an ancestor/descendant pair on a path is maximized by
          the path's MIN and MAX — so you never need to compare all pairs,
          only carry two numbers down.

Validate BST (98)          Max ancestor diff (1026)
  bounds = legal window      bounds = running min/max of ancestors
  node outside → return F    node outside → widen the window
  combine with AND           combine with MAX
```

## 總結

| 如果追問是… | 就拿出 | 代價 |
|---|---|---|
| 「如果一直插入呢？」（LC 230 → 703） | 模板 9 — 替節點加上子樹大小 `count` | 每次插入*與*每次排名查詢皆 O(h) |
| 「別把整個走訪結果都實體化」（LC 173） | 模板 5b — 只推左脊 | O(h) 空間，每次 `next()` 攤還 O(1) |
| 「有兩個節點被交換了，修好它」（LC 99） | 模板 8 — 中序 + 一個 `prev` 指標，`first` 只寫一次 | O(n) / O(h)，用 Morris 則 O(1) |
| 「能用 O(1) 空間做嗎？」（LC 99） | Morris 中序串接 | O(n) 時間，樹先被修改再還原 |
| 「能用 O(1) 空間做嗎？」（LC 1382） | Day–Stout–Warren：先拉成藤蔓，再壓縮 | O(n) 時間，O(1) 空間 |
| 「輸入是鏈結串列／前序輸出」 | 模板 6b — 模式 6.2 / 6.3 | 用中序模擬做到 O(n)，或用 `(min, max)` 邊界 |
| 「一次刪掉好幾個節點」（LC 1110） | 模板 3c — 回傳 `null` 來拆離，並把 `isRoot` 旗標往下傳 | O(n) / O(h + d) |
| 「祖先與後代之間最多能差多少？」（LC 1026） | 模板 4b — 攜帶動態最小／最大值，改成取最大而非拒絕 | O(n) / O(h) |

**貫穿這一切的單一想法**：BST 的中序走訪就是一個排序序列，而這裡每個模板要不是
*擴充節點*讓一次往下走訪就能回答排名問題，就是*改變你一次要實體化多少那個序列*。

---
**先修**：[bst.md](./bst.md) 中的標準模板。
**解題彙整**：[bst_examples.md](./bst_examples.md)。
**關鍵字**：順序統計樹、排名查詢、子樹大小、惰性迭代器、Morris 走訪、Day–Stout–Warren、Catalan 數、森林、邊界傳遞

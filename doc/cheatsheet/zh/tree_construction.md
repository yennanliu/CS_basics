# 樹的建構

> **範圍** — 從扁平的東西把二元樹建起來：兩組走訪陣列、單一陣列上的索引區間，或是帶括號／中序的字串 — 也就是所有「只讀取樹」的題目的反方向。
> **另見**：[tree_codec.md](./tree_codec.md) — 編碼那一側與完整的 codec 家族，包含 LC 536 的 parser 程式碼；[tree.md](./tree.md) — 走訪模板與模式總表；[tree_examples.md](./tree_examples.md) — 其餘詳解過的樹題；[bst.md](./bst.md) — 建立與重建有序樹（LC 108、449）。

## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Divide and Conquer](https://leetcode.com/problem-list/divide-and-conquer/)

## 總覽

每一道建構題在每一步都只回答一個問題：**這個區間的根節點是誰？** 一旦根被指認出來，區間就一分為二，同一個函式再遞迴下去。題目之間唯一不同的，只是*根節點怎麼被辨認出來*：

```text
LC 105 / 106   root = the next element of the pre/post-order array, split by its index in inorder
LC 654         root = the MAXIMUM of the range
LC 108         root = the MIDDLE of the range (sorted input -> balanced BST)
LC 536         root = the value before the first '(' — parentheses delimit the child ranges
LC 1597        root = the LAST lowest-precedence operator outside any parenthesis
```

### 關鍵性質
- **複雜度**：用一個「值 → 中序索引」的雜湊表，或用單一共用游標，可達 O(N)；若每次呼叫都重掃或重切片則是 O(N²)
- **核心想法**：找出區間的根，對兩個子區間遞迴，永遠不要複製輸入
- **何時使用**：輸出是一個 `TreeNode`，而不是從樹上讀出來的某個值
- **陷阱**：傳切片後的副本而不是索引邊界，正是把 O(N) 變成 O(N²) 的元凶

## 題型分類

| 分類 | 根節點怎麼找 | 範例 |
|----------|------------------|----------|
| **兩組走訪** | 它在*另一組*走訪中的位置 | LC 105, 106, 889 |
| **單一陣列上的索引區間** | 區間的最大值／中間值 | LC 654, 108, 1008 |
| **帶括號的字串** | 第一個 `(` 之前的那個值 | LC 536, 1597 |
| **線性 codec 串流** | 下一個 token | LC 297, 449, 1028 |

## 模板與演算法

### 1) Maximum Binary Tree — LC 654（依索引區間從陣列建樹）⭐⭐⭐⭐

**模式**：*「依索引區間從陣列建樹」* — 它是 LC 105（用前序 + 中序建樹）的通用兄弟。這個遞迴永遠是同樣三步：

1. 在 `[lo, hi]` 內挑出**根節點的索引**（這題是最大值的索引；LC 105 是前序的第一個；LC 108 是中間），
2. 對 `[lo, rootIdx - 1]` 遞迴 → 左子樹，
3. 對 `[rootIdx + 1, hi]` 遞迴 → 右子樹。

**關鍵想法**：絕對不要對陣列切片／複製 — 把 `(lo, hi)` 索引往下傳。基底情況 `lo > hi` 回傳 `null`，這正是空子區間不需要特別處理就能運作的原因。

```java
// java
// LC 654 - Maximum Binary Tree
// IDEA: root of range [lo, hi] = the MAX element; recurse on the two sub-ranges
class Solution {
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        // time = O(N^2) worst case (already-sorted input), O(N log N) average
        // space = O(N) for the recursion stack in the worst case
        return build(nums, 0, nums.length - 1);
    }

    private TreeNode build(int[] nums, int lo, int hi) {
        if (lo > hi) return null;             // NOTE: empty range -> null child

        int idx = lo;                          // find index of max in [lo, hi]
        for (int i = lo + 1; i <= hi; i++) {
            if (nums[i] > nums[idx]) idx = i;
        }

        TreeNode root = new TreeNode(nums[idx]);
        root.left  = build(nums, lo, idx - 1);   // everything LEFT of the max
        root.right = build(nums, idx + 1, hi);   // everything RIGHT of the max
        return root;
    }
}
```

```python
# python
# LC 654 - Maximum Binary Tree
# IDEA: pick max index as root of the range, recurse on left / right sub-ranges
class Solution:
    def constructMaximumBinaryTree(self, nums):
        # time = O(N^2) worst case, space = O(N)
        def build(lo, hi):
            if lo > hi:
                return None
            idx = lo
            for i in range(lo + 1, hi + 1):
                if nums[i] > nums[idx]:
                    idx = i
            root = TreeNode(nums[idx])
            root.left = build(lo, idx - 1)
            root.right = build(idx + 1, hi)
            return root

        return build(0, len(nums) - 1)
```

**變形 — Convert Sorted Array to BST（LC 108）**：骨架完全相同，唯一改變的是*挑根的規則* — 取 `mid = (lo + hi) // 2` 而不是取最大值的位置，這讓成本降到 `O(N)`，並產生一棵高度平衡的樹。

```python
# python
# LC 108 - Convert Sorted Array to Binary Search Tree
# IDEA: same "build by index range" skeleton as LC 654, root = MIDDLE element
class Solution:
    def sortedArrayToBST(self, nums):
        # time = O(N), space = O(log N)
        def build(lo, hi):
            if lo > hi:
                return None
            mid = (lo + hi) // 2          # middle -> balanced tree
            root = TreeNode(nums[mid])
            root.left = build(lo, mid - 1)
            root.right = build(mid + 1, hi)
            return root

        return build(0, len(nums) - 1)
```

| 題目 | `[lo, hi]` 內的挑根規則 | 時間 |
|---------|--------------------------------------|------|
| LC 654 Maximum Binary Tree | **最大**值的索引 | 最壞 O(N^2)／用單調堆疊可達 O(N) |
| LC 108 Sorted Array → BST  | **中間**索引 | O(N) |
| LC 105 Preorder + Inorder  | 前序的第一個，再依它在中序中的位置切開（用 HashMap 做 O(1) 查找） | O(N) |

### 2) Construct Binary Tree from Preorder and Inorder Traversal — LC 105
```python
#  Construct Binary Tree from Preorder and Inorder Traversal
# V0
# IDEA: the pre-order head is the root; its position in in-order splits the
#       remaining elements into the two subtrees. LC 105 builds a GENERAL binary
#       tree -- no BST ordering is assumed or required.
# time = O(N^2), space = O(N^2)
#   -> `inorder.index` rescans the range on every call and each recursion copies
#      four slices. Readable, but NOT the O(N) the table above quotes -- that is
#      the Java form below, which passes index bounds and a value -> index map.
class Solution(object):
    def buildTree(self, preorder, inorder):
        if len(preorder) == 0:
            return None
        if len(preorder) == 1:
            return TreeNode(preorder[0])
        ### NOTE : init root like below (via TreeNode and root value (preorder[0]))
        root = TreeNode(preorder[0])
        # get the index of root.val in order to SPLIT TREE
        index = inorder.index(root.val)  # the index of root at inorder, and we can also get the length of left-sub-tree, right-sub-tree ( preorder[1:index+1]) for following using
        # recursion for root.left
        #### NOTE : preorder[1 : index + 1] (for left sub tree)
        root.left = self.buildTree(preorder[1 : index + 1], inorder[ : index]) ### the two traversals cover the SAME elements, so the left subtree has the same length in both -- that is what lets one index split both arrays
        # recursion for root.right 
        root.right = self.buildTree(preorder[index + 1 : ], inorder[index + 1 :]) ### same on the right: everything after `index` in in-order is the right subtree
        return root
```

**同一種切法的 Java 版** — 用一個「值 → 中序索引」的 `HashMap`，把
`inorder.index(...)` 的掃描變成 O(1)，這正是讓整個建樹從 O(n²) 降到 O(n) 的關鍵：

```java
// Java - Build Tree from Preorder and Inorder
private int preIndex = 0;
private Map<Integer, Integer> inorderMap = new HashMap<>();

public TreeNode buildTree(int[] preorder, int[] inorder) {
    for (int i = 0; i < inorder.length; i++) {
        inorderMap.put(inorder[i], i);
    }
    return build(preorder, 0, inorder.length - 1);
}

private TreeNode build(int[] preorder, int left, int right) {
    if (left > right) return null;

    int rootVal = preorder[preIndex++];
    TreeNode root = new TreeNode(rootVal);

    int index = inorderMap.get(rootVal);

    root.left = build(preorder, left, index - 1);
    root.right = build(preorder, index + 1, right);

    return root;
}
```

**中序 + 後序 — LC 106。** 唯一改變的是根從哪裡來：改成後序的*最後*一個元素，而不是前序的第一個，所以兩個子切片的位置各偏移一格。

```python
def build_tree_post(inorder, postorder):
    if not inorder or not postorder:
        return None

    # Last element in postorder is root
    root_val = postorder[-1]
    root = TreeNode(root_val)

    root_index = inorder.index(root_val)

    root.left = build_tree_post(inorder[:root_index], postorder[:root_index])
    root.right = build_tree_post(inorder[root_index+1:], postorder[root_index:-1])

    return root
```

### 3) Construct Binary Tree from String — LC 536（遞迴下降剖析）⭐⭐⭐⭐

> 參考：`leetcode_python/Tree/construct-binary-tree-from-string.py`
>
> ```text
> Input:  "4(2(3)(1))(6(5))"
>
>        4
>      /   \
>     2     6
>    / \   /
>   3   1 5
> ```
> 字元只會是 `'('`、`')'`、`'-'` 與 `'0'`–`'9'`。空樹是 `""`，**絕不會**是 `"()"`。

#### 核心想法

**這個字串是一種前序序列化，只是由括號 — 而不是 null 標記 — 承載結構。** 所以這不是一道「附帶剖析步驟」的樹題；它是一道**剖析題**，只是輸出剛好是一棵樹。先把文法寫出來，程式碼自然就掉出來了：

```text
tree   := number ( '(' tree ')' )? ( '(' tree ')' )?
number := '-'? digit+
```

三條規則就搞定一切：

1. **一個節點是一個數字，後面可選地跟著 1 或 2 個帶括號的子樹。**
2. **第一組永遠是左子節點**（題目保證）— 所以「只有右子節點」的節點*根本無法表達*。這正是為什麼 `"()"` 被禁止當成空樹。
3. **每個 `(...)` 群組都是平衡的**，所以 parser 必須知道對應的 `)` 在哪裡。

**回答「這個子樹到哪裡結束？」有兩種方式** — 這是唯一真正需要做的設計抉擇：

| | **索引游標**（V0' / V2） | **平衡計數器 + 切片**（V0 / V1） |
|---|---|---|
| 做法 | 一個共用的 `i` 走過整個字串；每次呼叫都**回傳新的 `i`** | 遇 `(` 加 1、遇 `)` 減 1；回到 0 時切開 |
| 遞迴簽名 | `helper(s, i) -> (node, i)` | `str2tree(substring)` |
| 怎麼找到配對 | 根本不需要找 — 被呼叫者離開時 `i` 已經越過自己的群組 | 明確地掃描到平衡為 0 的位置 |
| 時間 | **O(n)** — 每個字元只被消耗一次 | 在 Python 裡是 O(n²)（`s = s[1:]` 每一步都複製字串） |
| 空間 | 只有 **O(h)** 的堆疊 | 切片要 O(n) |
| 結論 | **該寫的就是這個** | 比較好講解，面試也還行，但是平方複雜度 |

**為什麼游標必須被回傳（或設成全域）**：建完左子節點之後，父層完全不知道吃掉了多少字元。回傳 `i` 就是把那個狀態往上串回去的手段。在 Python 你會看到三種寫法 — 回傳 tuple、用 `self.idx`、或用 `nonlocal` — 它們其實是同一個技巧。

**兩個剖析陷阱**（兩個都很容易漏掉，而且都被輸入的字母集考到）：
- **多位數的數字** — `int(s[i])` 是錯的；你必須用 `while s[i].isdigit()` 一路吃下去。
- **負數** — 要在數字迴圈**之前**檢查 `'-'`。注意 `'-'` 在這裡不會有歧義：
  它只可能是符號，絕不可能是減法，因為它永遠跟在 `(` 或字串開頭之後。

**複雜度**：游標版本是 `O(n)` 時間、`O(h)` 空間（`h` = 樹高，退化成鏈狀時為 `O(n)`）。

#### 圖解追蹤（索引游標）

```text
        0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
   s =  4 ( 2 ( 3 ) ( 1 ) )  (  6  (  5  )  )

parse(i=0)  num "4"        -> i=1
  s[1]=='(' -> i=2, recurse LEFT
  parse(i=2)  num "2"      -> i=3
    s[3]=='(' -> i=4, recurse LEFT
    parse(i=4)  num "3"    -> i=5   s[5]==')' -> no children, return (3, 5)
    i=5 -> skip ')' -> i=6
    s[6]=='(' -> i=7, recurse RIGHT
    parse(i=7)  num "1"    -> i=8   s[8]==')' -> return (1, 8)
    i=8 -> skip ')' -> i=9
  return (2, 9)
  i=9 -> skip ')' -> i=10
  s[10]=='(' -> i=11, recurse RIGHT
  parse(i=11) num "6"      -> i=12
    s[12]=='(' -> i=13, recurse LEFT
    parse(i=13) num "5"    -> i=14  return (5, 14)
    i=14 -> skip ')' -> i=15
  return (6, 15)
  i=15 -> skip ')' -> i=16 == len -> done
```

**抓住那個節奏**：每次剖析子節點，前後都被 `i += 1`（吃掉 `(`）……`i += 1`（吃掉 `)`）夾住。
漏掉最後那個跳過動作，是最常見的一個 bug — parser 之後會看到一個多出來的 `)`，並把該節點當成沒有子節點。

> **程式碼**：游標版本的實作（Python **與** Java）以及 O(n²) 的切片變體，都放在
> [tree_codec.md](./tree_codec.md) 的 *3) Tree ⟷ String Codec Pattern* 一節，LC 536 就擺在它的反向題
> LC 606 旁邊。這裡不重複。

#### 模式總結

| 步驟 | 程式碼 | 為什麼 |
|------|------|-----|
| 擋掉空字串 | `if not s: return None` | `""` 就是空樹 |
| 剖析正負號 | `if s[i] == '-': i += 1` | 這裡的 `-` 只可能代表「負數」 |
| 剖析數字 | `while s[i].isdigit(): i += 1` | 值可能是多位數 |
| 左子節點 | `if s[i] == '(': i+=1; left, i = go(i); i+=1` | 第一組**永遠**是左邊 |
| 右子節點 | 同一段程式碼，再重複一次 | 最多只有 2 組 |
| 回傳 | `return node, i` | 把游標交還給父層 |

**通用化的遞迴下降骨架**（LC 394 / 726 / 1106 / 385 也適用）：

```text
parse(i):
    consume the ATOM at i            # number, letter, literal
    while next char opens a group:   # '(' , '[' , '{'
        i += 1                       # eat the opener
        child, i = parse(i)          # recurse
        i += 1                       # eat the closer
    return built_node, i
```

#### 常見陷阱

| 陷阱 | 症狀 | 修正 |
|---------|---------|-----|
| 用 `int(s[i])` 取值 | `"42"` 變成節點 `4`，後面還剩垃圾 | 用迴圈 `while s[i].isdigit()` |
| 在數字之後才檢查正負號 | `-4` 會爆掉，或被剖析成 `4` | 在數字迴圈**之前**檢查 `'-'` |
| 子節點之後漏掉 `i += 1` | 看到多出來的 `)`；右子節點被默默丟掉 | 每次遞迴呼叫之後都要吃掉右括號 |
| 以為某一組可能是右子節點 | `"4(2)"` 會建出錯的樹 | 第一組永遠是**左邊** |
| 迴圈裡寫 `s = s[1:]`（Python） | 會過，但是 **O(n²)** | 改成移動游標，而不是切片 |
| 把 `"()"` 當成空子節點 | 崩潰／冒出幽靈 `0` 節點 | 那不是合法輸入 — `""` 才是空樹 |

> **關於檔案裡兩個 `TODO: validate` 變體的說明**：我拿 V0-1（平衡掃描 + 切片）與 V0-2
> （游標）對照 V0'/V2，測了 `"4(2(3)(1))(6(5))"`、`""`、`"42"`、`"-4"`、`"-4(2(-3))"`、`"4(2)"`、
> `"1(2(3(4)))"`、`"10(-20(30)(-40))(50)"`、`"0"` — **四種寫法在所有合法輸入上都一致**。
> 它們只在非法的 `"4()(6)"` 上分歧（游標版本會拋例外，V0-2 會憑空造出一個 `0` 節點），
> 而那正是題目敘述明確排除掉的情況。

#### 相似 LC

| LC # | 題目 | 共通模式 | 關鍵差異 |
|------|---------|---------------|----------------|
| **536** | **Construct Binary Tree from String** | **遞迴下降，回傳游標** | **由括號界定子節點** |
| 606 | Construct String from Binary Tree | 536 的**完全反向** | 樹 → 字串；當右子節點存在而左邊缺少時，必須保留 `()` |
| 1597 | Build Binary Expression Tree From Infix | 帶括號的遞迴下降 | 由運算子優先級決定根（見第 4 節） |
| 297 | Serialize and Deserialize Binary Tree | 前序 + 游標 | 用 **null 標記**（`#`）而不是括號 |
| 449 | Serialize and Deserialize BST | 前序 + 游標 | BST 的上下界讓標記變得不必要 |
| 428 | Serialize and Deserialize N-ary Tree | 同樣的下降 | 子節點數任意 → 用迴圈，而不是 2 個 `if` |
| 105 / 106 | Construct from Preorder + Inorder | 從線性編碼建樹 | 索引區間跨在**兩個**陣列上（見第 2 節） |
| 331 | Verify Preorder Serialization | 消耗一段前序串流 | 只做驗證，不建樹（計算空位） |
| 394 | Decode String | `k[...]` 的巢狀群組 | 是重複，而不是子節點 |
| 385 | Mini Parser | 巢狀 `[...]` 的下降 | 建出 NestedInteger，也要處理負數 |
| 726 | Number of Atoms | 巢狀 `(...)` + 倍數 | 多字元的原子符號 + 要合併的計數 |
| 1106 | Parsing A Boolean Expression | `&(...)`, `|(...)`, `!(...)` | 運算子在群組之前，且是 n 元 |
| 224 / 227 / 772 | Basic Calculator I / II / III | 同樣的括號下降 | 有優先級與求值，但不保留樹 |
| 20 | Valid Parentheses | 平衡計數器這個原語 | 只做配對，也就是 V0 用到的那個子步驟 |

### 4) Build Binary Expression Tree From Infix Expression — LC 1597
```python
# LC 1597 Build Binary Expression Tree From Infix Expression
# V0
# IDEA : LC 224 Basic Calculator
class Solution(object):

    def help(self, numSt, opSt):
        right = numSt.pop()
        left = numSt.pop()
        # Node(val=op, left=lhs, right=rhs)
        return Node(opSt.pop(), left, right)

    def expTree(self, s):
        # hashmap for operator ordering
        pr = {'*': 1, '/': 1, '+': 2, '-': 2, ')': 3, '(': 4}
        numSt = []
        opSt = []
        i = 0
        while i < len(s):
            c = s[i]
            i += 1
            # check if int(c) if string
            if c.isnumeric():
                numSt.append(Node(c))
            else:                
                if c == '(':
                    opSt.append('(')
                else:
                    while(len(opSt) > 0 and pr[c] >= pr[opSt[-1]]):
                        numSt.append(self.help(numSt, opSt))
                    if c == ')':
                        opSt.pop() # Now what remains is the closing bracket ')'
                    else:
                        opSt.append(c)
        while len(opSt) > 0:
            numSt.append(self.help(numSt, opSt))
        print (">>> numSt = {}, opSt = {}".format(str(numSt), opSt))
        return numSt.pop()

# V0'
# IDEA : RECURSIVE
class Solution:
    def expTree(self, s):
        n = len(s)
        if n == 1:
            return Node(s)

        fstOpIdx = None
        kets = 0
        for i in range(n-1, 0, -1):
            if s[i] == ")":
                kets += 1
            elif s[i] == "(":
                kets -= 1
            elif kets == 0:
                if s[i] in "+-":
                    fstOpIdx = i
                    break
                elif s[i] in "*/" and fstOpIdx is None:
                    fstOpIdx = i
        if fstOpIdx is None:
            return self.expTree(s[1:-1])
        rtNd = Node(s[fstOpIdx])
        rtNd.left = self.expTree(s[:fstOpIdx])
        rtNd.right = self.expTree(s[fstOpIdx+1:])
        return rtNd
```

## 總結

| 輸入 | 區間的根 | 對什麼遞迴 | 複雜度 |
|---|---|---|---|
| 前序 + 中序 | `preorder[cursor++]` | 根在中序中位置左右兩側的索引區間 | 搭配「值 → 索引」對照表可達 O(N) |
| 未排序的陣列 | 最大值的索引 | `[lo, maxIdx-1]`、`[maxIdx+1, hi]` | 最壞 O(N²)，用單調堆疊可達 O(N) |
| 已排序的陣列 | 中間索引 | `[lo, mid-1]`、`[mid+1, hi]` | O(N) |
| `4(2(3)(1))(6(5))` | 第一個 `(` 之前的那串數字 | 兩個帶括號的群組，透過一個共用游標 | O(N) |
| `2-3*4` | 括號外優先級最低的最後一個運算子 | 兩側的子字串 | 樸素做法 O(N²)，用兩個堆疊可達 O(N) |

**能一次治好所有這些題的唯一習慣**：往下傳 `(lo, hi)` 索引邊界或一個共用游標 — 絕對不要傳輸入的切片副本。

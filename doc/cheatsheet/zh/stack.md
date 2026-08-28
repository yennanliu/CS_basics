# 堆疊（Stack）

> **範圍** — LIFO 的基本功，加上堆疊的經典模板：括號配對、min-stack、單調堆疊的精簡版、用顯式堆疊做走訪，以及作用域／上下文帳本。
> **另見**：[stack_expression_parsing.md](./stack_expression_parsing.md) — 計算機、decode string 與後綴式求值，整個運算式剖析家族；[stack_examples.md](./stack_examples.md) — 這些模板背後的解題實作庫；[monotonic_stack.md](./monotonic_stack.md) — next greater／previous smaller／span 類問題的深入版；[queue.md](./queue.md) — FIFO 的對照組；[iterator.md](./iterator.md) — 以堆疊為底的迭代器。

## LeetCode 題目清單

- [Stack](https://leetcode.com/problem-list/stack/)
- [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)

## 時間複雜度

| 資料結構 | 搜尋 | 插入 | 刪除 | 最小／最大 |
| -------------- | -------- | -------- | -------- | -------- |
| 堆疊          | O(n)     | O(1)     | O(1)     | O(n)     |

> 插入 = push，刪除 = pop，peek —— 全都發生在頂端，全都是 **O(1)**。最小／最大值可以靠一個輔助的 min／max 堆疊做到 **O(1)**（見 [monotonic_stack.md](./monotonic_stack.md)）。空間是 **O(n)**。

## 總覽

<p align="center"><img src="../pic/stack.jpeg"></p>

**堆疊**是具有後進先出（LIFO）性質的資料結構。每個操作都在堆疊頂端加入或移除元素。

### 關鍵性質
- **複雜度**：見上方的[時間複雜度](#time-complexity)表
- **核心原理**：最後放進去的元素最先被拿出來
- **適用場景**：牽涉到順序反轉、樣式配對，或是要維護上下文的問題

<p align="center"><img src="../pic/stack_101.png"></p>

### 參考資料

- 文章
    - [fuck-Algorithm - single stack](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E5%8D%95%E8%B0%83%E6%A0%88.md)
    - [fuck-Algorithm - implement array via stack / stack via array ](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%98%9F%E5%88%97%E5%AE%9E%E7%8E%B0%E6%A0%88%E6%A0%88%E5%AE%9E%E7%8E%B0%E9%98%9F%E5%88%97.md)
    - [Java Stack](https://blog.csdn.net/oChangWen/article/details/72859556) — 底層實作：陣列
- 影片
    - [Stack Fundamentals](https://www.bilibili.com/list/525438321?sort_field=pubtime&spm_id_from=333.999.0.0&oid=779764003&bvid=BV1my4y1Z7jj)

## 題型分類

九種形狀幾乎涵蓋所有堆疊題。**在哪裡**這一欄告訴你程式碼放在下面哪個模板，或是解法搬到了哪一份表。

| 題型 | 堆疊裡放的是什麼 | LC | 在哪裡 |
|---|---|---|---|
| **括號／巢狀驗證** | 還欠一個右括號的左括號 | 20, 921, 1541, 1614 | [模板 2](#template-2-bracket-matching--lc-20-) |
| **括號修復／計量** | 未配對括號的*索引* | 1249, 32, 856 | [stack_examples.md](./stack_examples.md) |
| **單調 —— next greater／smaller** | 還在等答案的元素 | 496, 503, 739, 84, 907, 2104 | [模板 3](#template-3-monotonic-stack--next-greater--smaller--lc-739-)、[monotonic_stack.md](./monotonic_stack.md) |
| **單調 —— 貪婪移除** | 目前建出來最好的前綴 | 402, 316, 1081, 1673 | [stack_examples.md](./stack_examples.md) |
| **單調 —— span 累積** | `[value, span]` 配對，串流式處理 | 901, 735 | [stack_examples.md](./stack_examples.md) |
| **放 `[element, count]` 配對的堆疊** | 前綴的遊程壓縮表示 | 1047, 1209, 1544 | [stack_examples.md](./stack_examples.md) |
| **運算式剖析** | 運算元／延後的項／未關閉的作用域 | 224, 227, 772, 394, 150, 682 | [stack_expression_parsing.md](./stack_expression_parsing.md) |
| **作用域／上下文帳本** | *外層*的上下文，以深度為鍵 | 388, 636, 591, 71 | [模板 6](#template-6-scope--context-ledger--lc-388-lc-636-) |
| **順序反轉／暫停的走訪** | 還沒做完的工作 | 144, 145, 173, 341, 445 | [模板 5](#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) |

### 值得知道的堆疊變形

- 單一堆疊
- 用堆疊做出佇列
     - LC 232（用 `2 stack`）
- 用佇列做出堆疊
- **放 (char, count) 配對的堆疊**
     - 存 `[element, count]` 配對，而不是原始元素
     - LC 1047（k=2 的特例，單純 pop）
     - LC 1209（移除 k 個連續重複字元）
     - LC 1544（Make The String Great）
     - LC 394（Decode String，用堆疊記重複次數）
     - LC 726（Number of Atoms）

## 模板與演算法

### 模板對照表

| 模板 | 堆疊元素 | 迴圈形狀 | 複雜度 | 什麼時候用 |
|---|---|---|---|---|
| 1 —— 基本操作 | 任何東西 | — | 每次操作 O(1) | push／pop／peek 的慣用寫法 |
| 2 —— 括號配對 | 左括號字元 | 掃一趟，遇右括號就 pop | O(n)／O(n) | 驗證巢狀，且括號種類 >1 |
| 3 —— 單調堆疊 | `(value, index)` | `for` 裡包一層 `while` | O(n)／O(n) | next greater／smaller／span |
| 4 —— Min stack | 值 + 當下最小值 | — | 每次操作 O(1) | 堆疊上的 O(1) `getMin()` |
| 5 —— 顯式堆疊 | 待處理的節點 | `while stack` | O(n)／O(h) | 迭代式走訪、順序反轉 |
| 6 —— 作用域帳本 | 每層深度的外層上下文 | 掃一趟，截到當前深度 | O(n)／O(depth) | 有縮排的輸入、start/end 事件 |

### 模板 1：堆疊基本操作

**push（放入）：**
```java
// Java
Stack<Integer> stack = new Stack<>();
stack.push(element);  // O(1)
```

```python
# Python
stack = []
stack.append(element)  # O(1)
```

**pop（移除頂端）：**
```java
// Java
int top = stack.pop();  // O(1), throws if empty
```

```python
# Python
top = stack.pop()  # O(1), raises if empty
```

**peek（看頂端）：**
```java
// Java
int top = stack.peek();  // O(1), throws if empty
if (!stack.isEmpty()) {
    top = stack.peek();
}
```

```python
# Python
top = stack[-1]  # O(1), raises if empty
if stack:
    top = stack[-1]
```

---

### 模板 2：括號配對 —— LC 20 ⭐⭐⭐⭐⭐

> 面試出現頻率最高的堆疊模式。**把左括號 push 進去，遇到右括號就檢查頂端是不是它的另一半。** 只要括號**不只一種**，就非用堆疊不可（計數器不夠），因為順序有意義：`([)]` 是不合法的。

```text
Core Idea:
  - Opener  -> push it (we owe a matching closer)
  - Closer  -> stack must be non-empty AND top must be the matching opener
               (else fail fast)
  - End     -> stack must be EMPTY (no unmatched openers left)

When to Use:
  - Validate / repair / measure balanced sequences
  - Any "nesting must be well-formed" check (brackets, tags, expressions)

Counter vs Stack (interview discriminator):
  - ONE bracket type only  -> a running balance counter is enough, O(1) space
                              (LC 921, LC 1541, LC 1614)
  - MULTIPLE bracket types -> MUST use a stack (LC 20)
  - Need the POSITION of the offending bracket -> stack of INDICES
                              (LC 1249, LC 32)

Similar LC:
  - LC 20    Valid Parentheses                        (base template)
  - LC 1249  Minimum Remove to Make Valid Parentheses (stack of indices -> delete)
  - LC 921   Minimum Add to Make Parentheses Valid    (balance counter)
  - LC 32    Longest Valid Parentheses                (index stack + `-1` base)
  - LC 856   Score of Parentheses                     (stack of partial scores)
  - LC 1541  Minimum Insertions to Balance a Parentheses String  ( `(` needs `))` )
  - LC 1614  Maximum Nesting Depth of the Parentheses (max depth = max balance)
```

```java
// java
// LC 20 - Valid Parentheses
// IDEA: STACK — push openers, pop-and-verify on closers, stack must end empty
// time = O(n), space = O(n)
public boolean isValid(String s) {

    // closer -> its matching opener
    Map<Character, Character> pairs = new HashMap<>();
    pairs.put(')', '(');
    pairs.put(']', '[');
    pairs.put('}', '{');

    Deque<Character> st = new ArrayDeque<>();

    for (char c : s.toCharArray()) {
        if (pairs.containsKey(c)) {
            /**
             *  NOTE !!!  a closer needs BOTH checks:
             *   1) stack NOT empty  (e.g. ")" alone)
             *   2) top is the matching opener (e.g. "(]" must fail)
             *
             *  NOTE !!! unbox to `char` before comparing —
             *  comparing two Character objects with `!=` compares REFERENCES.
             */
            if (st.isEmpty()) {
                return false;
            }
            char top = st.pop();
            if (top != pairs.get(c)) {
                return false;
            }
        } else {
            st.push(c);
        }
    }

    /** NOTE !!! leftover openers => invalid (e.g. "(((") */
    return st.isEmpty();
}
```

```python
# python
# LC 20 - Valid Parentheses
# IDEA: STACK — push openers, pop-and-verify on closers, stack must end empty
# time = O(n), space = O(n)
class Solution(object):
    def isValid(self, s):
        pairs = {')': '(', ']': '[', '}': '{'}
        stack = []
        for c in s:
            # closer
            if c in pairs:
                # NOTE !!! empty stack OR wrong partner -> invalid
                if not stack or stack.pop() != pairs[c]:
                    return False
            # opener
            else:
                stack.append(c)
        # NOTE !!! leftover openers -> invalid
        return not stack
```

**括號家族 —— 同一個模板的四種變形**（解法在 [stack_examples.md](./stack_examples.md)）：

| LC | 變形 | 堆疊裡放什麼 |
|----|-------|-------------|
| 1249 | 不只驗證，還要修復 | 未配對 `(` 的*索引* |
| 921 | 只有一種括號，用計數器就夠 —— O(1) 空間 | 什麼都不放（堆疊退化成一個 size） |
| 32 | 最長合法區段的長度 | 索引，外加一個 `-1` 當**基準**哨兵 |
| 856 | 從巢狀結構摺出一個分數 | 每一層的部分**結果** |

---

### 模板 3：單調堆疊 —— Next Greater／Smaller —— LC 739 ⭐⭐⭐⭐

> **核心想法**：堆疊裡放的是**還在等答案**的元素，並保持單調順序。把比較方向翻過來就換一個方向 —— `top < cur` 時 pop 是找 *next greater*，`top > cur` 時 pop 是找 *next smaller*。每個元素只 push 一次、最多 pop 一次，所以 `for` 裡包 `while` 仍然是 **O(n)**。
>
> 這份表只保留**一個**精簡版。完整家族（previous smaller、span、直方圖、子陣列最小／最大值總和，以及貪婪移除的各種變形）是 [monotonic_stack.md](./monotonic_stack.md) 的主題；解題實作在 [stack_examples.md](./stack_examples.md)。

- 存 `(value, index)` —— 索引才能把「找到了」變成一段*距離*或一個*寬度*。

```python
# python
# LC 739, LC 503 - Find next `big number`
# ...
stack = [] # [[idx, val]]
for i, val in enumerate(tmp):
    while stack and stack[-1][1] < val:
        _idx, _val = stack.pop(-1)
        res[tmp[_idx]] = i - _idx
    stack.append([i, val]) 
# ...
```

```java
// java
// LC 239
// LC 496
// ...

// Traverse the array from right to left
for (int i = 0; i < n; i++) {
    // Maintain a decreasing monotonic stack
    /** NOTE !!! below */
    while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
        stack.pop();  // Pop elements from the stack that are smaller or equal to the current element
    }
    
    // If stack is not empty, the next greater element is at the top of the stack
    if (!stack.isEmpty()) {
        result[i] = nums[stack.peek()];
    }
    
    // Push the current element's index onto the stack
    stack.push(i);
}

// ...
```

| 想找的東西 | pop 的條件 | 答案怎麼讀出來 | LC |
|---|---|---|---|
| next **greater** 元素／下一個更暖的日子 | `top < cur` | `cur` 把 `top` pop 掉的當下 | 496, 503, 739 |
| next **smaller** 元素，或左右邊界 | `top > cur` | `cur` 把 `top` pop 掉的當下 | 84, 907, 2104 |
| 往回到上一個更大值的 **span** | `top <= cur`，並累加被 pop 掉的 span | 累加起來的計數 | 901 |
| **環狀**陣列 | 一樣的做法，跑 `nums * 2` 並用 `idx % n` | 一樣 | 503 |

---

### 模板 4：Min Stack —— O(1) getMin —— LC 155 ⭐⭐⭐⭐

**模式：2 個堆疊（主堆疊 + 追蹤最小值的堆疊）**

```text
Key Insight:
  minStack does NOT store elements in sorted order.
  Instead, minStack[i] stores the minimum value seen
  in the main stack up to position i.

  -> This lets getMin() return the current minimum in O(1)
     by simply reading minStack[-1] (the top).

  Example: push -2, 0, -3
    stack    = [-2,  0, -3]
    minStack = [-2, -2, -3]   ← each entry is min-so-far, not sorted elements

  After pop():
    stack    = [-2,  0]
    minStack = [-2, -2]       ← getMin() correctly returns -2

When to Use:
  - Need O(1) getMin() on a stack
  - minStack mirrors the main stack size (one entry per push/pop)
  - Both stacks are always the same length
```

```python
# LC 155. Min Stack
# V0
# IDEA: 2 STACKS
class MinStack(object):

    def __init__(self):
        self.stack = []
        self.minStack = []

    def push(self, val):
        self.stack.append(val)
        # minStack tracks running minimum, NOT sorted elements
        if not self.minStack:
            self.minStack.append(val)
        else:
            self.minStack.append(min(val, self.minStack[-1]))

    def pop(self):
        # both stacks must stay in sync — always pop together
        self.minStack.pop()
        return self.stack.pop()

    def top(self):
        return self.stack[-1]

    def getMin(self):
        # top of minStack is always the current minimum — O(1)
        return self.minStack[-1]
```

```python
# V1: single stack storing (value, current_min) tuples
class MinStack(object):

    def __init__(self):
        self.stack = []

    def push(self, x):
        if not self.stack:
            self.stack.append((x, x))
        else:
            self.stack.append((x, min(x, self.stack[-1][1])))

    def pop(self):
        self.stack.pop()

    def top(self):
        return self.stack[-1][0]

    def getMin(self):
        return self.stack[-1][1]
```

---

### 模板 5：顯式堆疊 —— 迭代式走訪 —— LC 144, LC 145 ⭐⭐⭐⭐

> **核心想法**：把遞迴的呼叫堆疊**攤開來自己管**。把還沒做的工作 push 進去，pop 出來就做。**前序**要先 push `right` 再 push `left`，因為堆疊會把你餵進去的東西反過來吐。**後序**是樹裡最划算的小把戲：用 `root → right → left` 跑一次前序，然後把**輸出反轉**。
>
> 同一個堆疊的*暫停*版本 —— 必須在元素之間停下來的迭代器 —— 是 [stack_examples.md](./stack_examples.md) 裡的 LC 173／LC 341，更完整的家族在 [iterator.md](./iterator.md)。

```python
# python
# LC 144 - Binary Tree Preorder Traversal
# IDEA: EXPLICIT STACK — push RIGHT before LEFT so LEFT is popped first
# time = O(n), space = O(h)
class Solution(object):
    def preorderTraversal(self, root):
        if not root:
            return []
        res, stack = [], [root]
        while stack:
            node = stack.pop()
            res.append(node.val)
            # NOTE !!! right first -> left ends up on TOP
            if node.right:
                stack.append(node.right)
            if node.left:
                stack.append(node.left)
        return res


# LC 145 - Binary Tree Postorder Traversal
# IDEA: preorder variant (root -> RIGHT -> LEFT), then REVERSE => left-right-root
# time = O(n), space = O(h)
class Solution(object):
    def postorderTraversal(self, root):
        if not root:
            return []
        res, stack = [], [root]
        while stack:
            node = stack.pop()
            res.append(node.val)
            # NOTE !!! mirrored order compared with preorder
            if node.left:
                stack.append(node.left)
            if node.right:
                stack.append(node.right)
        return res[::-1]   # root-right-left  ->  left-right-root
```

```java
// java
// LC 144 - Binary Tree Preorder Traversal
// IDEA: EXPLICIT STACK — push RIGHT before LEFT so LEFT is popped first
// time = O(n), space = O(h)
public List<Integer> preorderTraversal(TreeNode root) {
    List<Integer> res = new ArrayList<>();
    if (root == null) {
        return res;
    }
    Deque<TreeNode> st = new ArrayDeque<>();
    st.push(root);
    while (!st.isEmpty()) {
        TreeNode node = st.pop();
        res.add(node.val);
        /** NOTE !!! right pushed FIRST, so left is popped FIRST */
        if (node.right != null) {
            st.push(node.right);
        }
        if (node.left != null) {
            st.push(node.left);
        }
    }
    return res;
}

// LC 145 - Binary Tree Postorder Traversal
// IDEA: preorder with LEFT/RIGHT swapped (root-right-left), then REVERSE
// time = O(n), space = O(h)
public List<Integer> postorderTraversal(TreeNode root) {
    LinkedList<Integer> res = new LinkedList<>();
    if (root == null) {
        return res;
    }
    Deque<TreeNode> st = new ArrayDeque<>();
    st.push(root);
    while (!st.isEmpty()) {
        TreeNode node = st.pop();
        /** NOTE !!! addFirst == "append then reverse", done incrementally */
        res.addFirst(node.val);
        if (node.left != null) {
            st.push(node.left);
        }
        if (node.right != null) {
            st.push(node.right);
        }
    }
    return res;
}
```

---

### 模板 6：作用域／上下文帳本 —— LC 388, LC 636 ⭐⭐⭐⭐⭐

> **核心想法**：堆疊裡放的不是*字元*，而是**外層的上下文**（一段路徑前綴、一個正在執行的函式、一個未關閉的標籤）。進入一個作用域就 **push** 上下文，離開就 **pop**，答案是拿 `stack[-1]`／`stack[depth]` —— 也就是你當下所在的那層上下文 —— 算出來的。
>
> 這是 Google 出現頻率最高的那批堆疊題背後的模式，而且它*不是*括號配對：這裡的「括號」是隱含的（縮排深度、start/end 的 log 事件）。

```text
Core Idea:
  - Stack index == NESTING DEPTH. stack[d] = accumulated context at depth d.
  - On entering depth d : trim the stack down to d, then push the new context
  - On leaving  a scope : pop, and hand the accumulated value back to the parent
  - The parent is ALWAYS stack[-1] — that is what makes it a stack problem

When to Use:
  - Indented / tab-delimited input   -> depth = number of leading tabs (LC 388)
  - start/end (or open/close) events -> the "running" item is the stack top (LC 636)
  - Any "who is my parent?" question during a single left-to-right scan

Similar LC:
  - LC 388  Longest Absolute File Path       (depth-indexed prefix lengths)
  - LC 636  Exclusive Time of Functions      (running function = stack top)
  - LC 591  Tag Validator                    (open-tag stack + scope rules)
  - LC 71   Simplify Path                    (stack_examples.md; ".." pops the parent dir)
```

```java
// java
// LC 388 - Longest Absolute File Path
// IDEA: SCOPE STACK indexed by DEPTH — stack.get(d) = length of the path prefix at depth d
// time = O(n), space = O(depth)
public int lengthLongestPath(String input) {

    int res = 0;

    /**
     *  NOTE !!!
     *   stack.get(d) = length of "dir1/dir2/.../" for the current branch at depth d
     *   (already includes the trailing '/')
     *   -> index in the list IS the nesting depth
     */
    List<Integer> stack = new ArrayList<>();
    stack.add(0); // depth 0 has an empty prefix

    for (String line : input.split("\n")) {

        // depth = number of leading '\t'
        int depth = 0;
        while (depth < line.length() && line.charAt(depth) == '\t') {
            depth++;
        }
        String name = line.substring(depth);

        /** NOTE !!! we LEFT the previous deeper scopes -> pop back to `depth` */
        while (stack.size() > depth + 1) {
            stack.remove(stack.size() - 1);
        }

        if (name.contains(".")) {
            // a FILE never becomes a parent -> just measure it
            res = Math.max(res, stack.get(depth) + name.length());
        } else {
            // a DIRECTORY becomes the context of depth+1 (+1 for the '/')
            stack.add(stack.get(depth) + name.length() + 1);
        }
    }

    return res;
}
```

```python
# python
# LC 388 - Longest Absolute File Path
# IDEA: SCOPE STACK indexed by DEPTH — stack[d] = length of the path prefix at depth d
# time = O(n), space = O(depth)
class Solution(object):
    def lengthLongestPath(self, input):
        res = 0
        stack = [0]   # stack[d] = prefix length at depth d (trailing '/' included)

        for line in input.split('\n'):
            name = line.lstrip('\t')
            depth = len(line) - len(name)   # number of leading tabs

            # NOTE !!! we left deeper scopes -> trim the stack back to this depth
            while len(stack) > depth + 1:
                stack.pop()

            if '.' in name:
                # file: measure, never push (a file has no children)
                res = max(res, stack[depth] + len(name))
            else:
                # dir: becomes the prefix for depth+1, +1 for the '/'
                stack.append(stack[depth] + len(name) + 1)

        return res

# Trace: "dir\n\tsubdir2\n\t\tfile.ext"
#   "dir"        d=0 -> stack = [0, 4]           ("dir/")
#   "subdir2"    d=1 -> stack = [0, 4, 12]       ("dir/subdir2/")
#   "file.ext"   d=2 -> res = 12 + 8 = 20
```

```java
// java
// LC 636 - Exclusive Time of Functions
// IDEA: SCOPE STACK of function ids — the RUNNING function is always the stack top
// time = O(n), space = O(n)
public int[] exclusiveTime(int n, List<String> logs) {

    int[] res = new int[n];
    Deque<Integer> stack = new ArrayDeque<>(); // ids of functions currently RUNNING
    int prev = 0;  // timestamp where the current "run slice" started

    for (String log : logs) {
        String[] p = log.split(":");       // {id, "start"|"end", timestamp}
        int id = Integer.parseInt(p[0]);
        int t = Integer.parseInt(p[2]);

        if (p[1].equals("start")) {
            /**
             *  NOTE !!!
             *   the caller (stack top) ran during [prev, t) -> credit it,
             *   then it gets PREEMPTED by the new callee
             */
            if (!stack.isEmpty()) {
                res[stack.peek()] += t - prev;
            }
            stack.push(id);
            prev = t;
        } else {
            /**
             *  NOTE !!!
             *   "end at t" is INCLUSIVE -> the slice is [prev, t], hence `+ 1`
             *   and the caller resumes at t + 1
             */
            res[stack.pop()] += t - prev + 1;
            prev = t + 1;
        }
    }

    return res;
}
```

```python
# python
# LC 636 - Exclusive Time of Functions
# IDEA: SCOPE STACK of function ids — the RUNNING function is always the stack top
# time = O(n), space = O(n)
class Solution(object):
    def exclusiveTime(self, n, logs):
        res = [0] * n
        stack = []    # ids of functions currently RUNNING (top = executing now)
        prev = 0      # start of the current time slice

        for log in logs:
            fid, typ, t = log.split(':')
            fid, t = int(fid), int(t)

            if typ == 'start':
                # the caller ran on [prev, t) before being preempted
                if stack:
                    res[stack[-1]] += t - prev
                stack.append(fid)
                prev = t
            else:
                # 'end' timestamp is INCLUSIVE -> [prev, t] => + 1
                res[stack.pop()] += t - prev + 1
                prev = t + 1

        return res
```

---

## 摘要與速查

### 決策表 —— 該用哪一種堆疊模式？

| 問題類型 | 模式 | 核心想法 | 例題 |
|--------------|---------|----------|----------|
| 找 **next greater／smaller** 元素 | 單調堆疊 | 維持遞增／遞減的順序 | LC 496, 503, 739 |
| **移除相鄰重複字元** | 放 [element, count] 配對的堆疊 | 記次數，湊到 k 就 pop | LC 1047, 1209, 1544 |
| 帶括號的**字串解碼** | 帶計數的堆疊 | 用配對處理巢狀重複 | LC 394, 726 |
| **算術運算式** | 帶運算子的堆疊 | 處理優先序與求值 | LC 224, 227 |
| **移除 k 位數**使數字最小 | 貪婪 + 單調 | 划算就把較大的數字 pop 掉 | LC 402 |
| 有重複字元時求**字典序最小** | 單調 + 最後出現位置 | 貪婪移除，搭配「後面還會出現」的檢查 | LC 316, 1081 |
| **串流／線上**頻率統計 | 帶 span 配對的堆疊 | 用配對累加計數 | LC 901 |
| **用 LIFO 做出 FIFO** | 兩個堆疊 | 用 input／output 兩個堆疊模擬佇列 | LC 232 |
| **括號平衡**驗證 | 括號配對 | push 左括號，遇右括號 pop 並驗證 | LC 20, 1249, 32 |
| **巢狀上下文**（縮排、start/end 事件） | 作用域／上下文帳本 | `stack[depth]` = 外層上下文 | LC 388, 636, 591 |
| **反轉**一個只能往前走的序列 | 全部 push，再全部 pop | pop 出來就是逆序 | LC 445, 234, 143 |
| **迭代式**走訪／延遲式迭代器 | 顯式堆疊 | 堆疊裡放的是還沒做的工作 | LC 144, 145, 173, 341 |
| **後綴式／RPN** 求值 | 運算元堆疊 | 遇運算子就 pop 兩個、push 結果 | LC 150, 682 |

**怎麼用**：在最左欄找到你的問題目標，再拿對應的模式和例題當起點。

### 各模式的複雜度

這裡列的是每個*模式*的成本。結構本身每個*操作*的成本在最上面的[時間複雜度](#time-complexity)表。

| 模式 | 時間 | 空間 | 為什麼 |
|---|---|---|---|
| 括號配對 | O(n) | O(n)，只有一種括號時 O(1) | 掃一趟，每個字元最多 push 一次 |
| 單調堆疊 | O(n) | O(n) | 每個元素 push 一次、最多 pop 一次 |
| 貪婪移除（丟掉 `k` 個） | O(n) | O(n) | 同樣的攤還分析；`k` 限制了 pop 的次數 |
| `[element, count]` 配對 | O(n) | O(n) | 堆疊就是前綴的遊程編碼 |
| Min stack | 每次操作 O(1) | O(n) | 每次 push 多存一筆輔助資料 |
| 顯式堆疊走訪 | O(n) | O(h) | 待處理的只有當前那條 root 到節點的路徑 |
| 作用域帳本 | O(n) | O(depth) | 每個未關閉的作用域一筆 |
| 運算式剖析 | O(n) | O(n) | 堆疊深度 = 巢狀深度 |

### 常見陷阱

- **單調堆疊**：處理「next greater／smaller」問題的關鍵模式 —— 先確認題目要的是遞增還是遞減
- **配對堆疊**：移除相鄰重複或巢狀計數的題目，堆疊裡存 `[element, count]` 配對
- **貪婪移除**：有些題目適合在維持某個不變量的前提下，貪婪地把元素丟掉
- **先檢查堆疊是否為空**：每個由右括號觸發的 `pop()`／`peek()` 前面都要有 `!stack.isEmpty()`。
- **Java 的 `Character` vs `char`**：對兩個裝箱的 `Character` 用 `!=` 比的是參考 —— 比較前先拆箱。
- 這類題目的**整數除法是往零截斷**；Python 的 `//` 是*往下取整*，所以要寫 `int(a / b)`。
- 當堆疊必須吐出由左到右的順序時，**子節點要反著 push**。

### 其他內容在哪裡

| 你在找 | 檔案 |
|---|---|
| 計算機（LC 224／227／772）、decode string（LC 394）、後綴式（LC 150） | [stack_expression_parsing.md](./stack_expression_parsing.md) |
| 上面提到那些題目的解題實作 | [stack_examples.md](./stack_examples.md) |
| next greater／previous smaller／直方圖的理論 | [monotonic_stack.md](./monotonic_stack.md) |
| 迭代器設計（LC 173, 341, 284） | [iterator.md](./iterator.md) |
| FIFO、雙端佇列、單調佇列 | [queue.md](./queue.md)、[monotonic_queue.md](./monotonic_queue.md) |

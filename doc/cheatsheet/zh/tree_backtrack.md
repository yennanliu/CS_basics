# 樹的回溯模板（gpt）

> **範圍** — 根到葉的**路徑**問題：路徑本身就是狀態，回程時必須把它復原。
> **另見**：[tree.md](./tree.md) — 一般的樹走訪；[binary_tree.md](./binary_tree.md) — DFS 狀態流動的方向；[backtrack.md](./backtrack.md) — 樹以外的回溯。

很多**樹的路徑問題**其實共用同一套 DFS＋回溯骨架。下面這個模板適用於這類題目：

* LeetCode 113 Path Sum II
* LeetCode 257 Binary Tree Paths
* LeetCode 129 Sum Root to Leaf Numbers
* LeetCode 437 Path Sum III（稍微改一下）

先給**通用模板**，再看每題怎麼套進去。

---

## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

## 1️⃣ 通用樹回溯模板

```java
void dfs(TreeNode node, ...) {

    if (node == null) return;

    // 1. choose (update path / sum / state)
    
    // 2. check goal (usually leaf condition)
    
    // 3. explore children
    dfs(node.left, ...);
    dfs(node.right, ...);

    // 4. undo choice (backtrack)
}
```

處理**路徑問題**時，通常要維護：

```text
List<Integer> path
some running state (sum / number / string)
```

---

## 2️⃣ 完整的通用模板

```java
void dfs(TreeNode node, List<Integer> path, int state) {

    if (node == null) return;

    // choose
    path.add(node.val);
    state = update(state, node.val);

    // goal check
    if (isLeaf(node) && condition(state)) {
        save(path);
    }

    // explore
    dfs(node.left, path, state);
    dfs(node.right, path, state);

    // undo (backtrack)
    path.remove(path.size() - 1);
}
```

關鍵步驟：

| 步驟    | 用途             |
| ------- | ------------------- |
| choose  | 把節點加進路徑    |
| check   | 看看是否達成目標 |
| explore | 對子節點做 DFS       |
| undo    | 還原狀態       |

---

## 3️⃣ 套用到 LC 113（Path Sum II）

以 LeetCode 113 Path Sum II 為例：

狀態 = **剩餘的和**

```java
void dfs(TreeNode node, int remain, List<Integer> path) {

    if (node == null) return;

    path.add(node.val);

    if (node.left == null && node.right == null && remain == node.val) {
        res.add(new ArrayList<>(path));
    }

    dfs(node.left, remain - node.val, path);
    dfs(node.right, remain - node.val, path);

    path.remove(path.size() - 1);
}
```

---

## 4️⃣ 套用到 LC 257（Binary Tree Paths）

以 LeetCode 257 Binary Tree Paths 為例：

狀態 = **路徑字串**

```java
void dfs(TreeNode node, String path) {

    if (node == null) return;

    path += node.val;

    if (node.left == null && node.right == null) {
        res.add(path);
        return;
    }

    path += "->";

    dfs(node.left, path);
    dfs(node.right, path);
}
```

---

## 5️⃣ 套用到 LC 129（根 → 葉的數字）

以 LeetCode 129 Sum Root to Leaf Numbers 為例：

狀態 = **數字**

```text
123 means path: 1 → 2 → 3
```

```java
void dfs(TreeNode node, int num) {

    if (node == null) return;

    num = num * 10 + node.val;

    if (node.left == null && node.right == null) {
        total += num;
        return;
    }

    dfs(node.left, num);
    dfs(node.right, num);
}
```

---

## 6️⃣ 特殊情況：LC 437（Path Sum III）

LeetCode 437 Path Sum III 不太一樣，因為路徑**可以從任何節點開始**，不限於根。

所以我們從**每個節點**都跑一次 DFS。

但內層的 DFS 仍然照著同一個模板走。

---

## 7️⃣ 心智模型（非常重要）

只要看到：

```text
root → leaf
path
all paths
sum of paths
string of paths
```

馬上就該想到：

```text
DFS + Backtracking
```

模板：

```text
add node
check leaf
dfs children
remove node
```

---

## 8️⃣ 面試速記密技 ⭐

把這個**五行骨架**背起來就好：

```java
path.add(node.val);

if (leaf condition)
    save result;

dfs(left)
dfs(right)

path.remove(path.size()-1);
```

**大多數根到葉的題目**用這個就能解掉。

---

✅ **面試小訣竅**

如果題目裡出現這些字眼：

```text
all paths
root to leaf
return list of paths
```

那幾乎**保證是 DFS＋回溯**。

---

## 9️⃣ 快速決策表（該用哪個路徑模板？）

| 路徑上要做的事 | 模板 | 復原步驟 | 例題 |
|------------------|----------|-----------|---------|
| 收集所有根到葉的路徑 | push 節點／pop 節點 | `path.remove(size-1)` | LC 113、LC 257 |
| 把路徑摺疊成一個純量（和／數字） | 用傳值的方式帶狀態 | *不用復原* — 值是複製的 | LC 129 |
| 找到通往**某一個**節點的路徑 | push，**只在失敗時** pop | 子樹回傳 false 時才 pop | **LC 236** |
| 統計和為目標值的子路徑數 | 路徑上的前綴和計數器 | `count[cur] -= 1` | **LC 437** |
| 走訪**隱含**的樹（數字／文字） | 從父節點狀態重建子節點 | 除回去／彈堆疊 | **LC 386**、**LC 388** |

⭐⭐⭐⭐⭐ 這幾種之間唯一的差別，就是**「路徑狀態」是什麼**，以及**要怎麼把它復原**。

---

## 🔟 模板：根→節點的路徑，只在失敗時復原（LC 236）

**核心想法**：標準模板在探索完子節點後會**無條件** pop。
但當你在找**某一個特定節點**時，改成**只有子樹失敗才** pop——
這樣搜尋成功時，`path` 裡剛好留著完整的根→目標路徑。

**模式**：分別建出 `path(root→p)` 和 `path(root→q)`，兩者**最後一個共同前綴元素就是 LCA**。

```java
// java
// LC 236 - Lowest Common Ancestor of a Binary Tree
// IDEA: collect root->p and root->q paths with backtracking (pop on failure only),
//       then walk both paths together; last shared node = LCA.
class Solution {
    // time = O(N), space = O(H)  (H = tree height, O(N) worst case)
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        List<TreeNode> pathP = new ArrayList<>(), pathQ = new ArrayList<>();
        findPath(root, p, pathP);
        findPath(root, q, pathQ);

        TreeNode lca = null;
        for (int i = 0; i < Math.min(pathP.size(), pathQ.size()); i++) {
            if (pathP.get(i) != pathQ.get(i)) break;   // prefix ended
            lca = pathP.get(i);
        }
        return lca;
    }

    private boolean findPath(TreeNode node, TreeNode target, List<TreeNode> path) {
        if (node == null) return false;

        path.add(node);                                 // choose
        if (node == target) return true;                // goal -> KEEP the path

        if (findPath(node.left, target, path)) return true;
        if (findPath(node.right, target, path)) return true;

        path.remove(path.size() - 1);                   // undo ONLY on failure
        return false;
    }
}
```

```python
# python
# LC 236 - Lowest Common Ancestor of a Binary Tree
# IDEA: same backtracking, pop only when the subtree does not contain the target
class Solution(object):
    # time = O(N), space = O(H)
    def lowestCommonAncestor(self, root, p, q):
        def find_path(node, target, path):
            if not node:
                return False
            path.append(node)               # choose
            if node is target:
                return True                 # goal -> keep path
            if find_path(node.left, target, path) or find_path(node.right, target, path):
                return True
            path.pop()                      # undo ONLY on failure
            return False

        path_p, path_q = [], []
        find_path(root, p, path_p)
        find_path(root, q, path_q)

        lca = None
        for a, b in zip(path_p, path_q):
            if a is not b:
                break
            lca = a
        return lca
```

> **為什麼要學這個，而不是那個有名的三行後序 LCA？**
> 面試官追問時，活下來的是路徑版：*「回傳 p 和 q 之間的距離」*、
> *「印出 p 到 q 的路徑」*、*「不保證兩個節點都存在」*（看那個布林回傳值）。
> 三行版在只有 p 或 q 其中一個存在時，還是會默默回傳一個節點。

---

## 1️⃣1️⃣ 模板：**在路徑上**做前綴和＋復原（LC 437）

第 6️⃣ 節提到 LC 437 要從每個節點跑 DFS——那是 `O(N^2)`。
`O(N)` 的版本，就是那個經典的**「回溯一個 HashMap，而不是一個 list」**模板。

**核心想法**：把當前的根→節點路徑當成一個陣列，套用經典的子陣列和技巧。
`count[prefix]` 記錄**當前路徑上**有多少個祖先的累積和是這個值。
這個 map 就是路徑狀態，所以**回程時一定要復原**。

```java
// java
// LC 437 - Path Sum III
// IDEA: running sum along the path + HashMap<prefixSum, count>;
//       number of paths ending at node = count[cur - target]. Undo the map when leaving the node.
class Solution {
    // time = O(N), space = O(H)
    public int pathSum(TreeNode root, int targetSum) {
        Map<Long, Integer> prefix = new HashMap<>();
        prefix.put(0L, 1);                       // empty prefix = the path starts at root
        return dfs(root, 0L, targetSum, prefix);
    }

    private int dfs(TreeNode node, long cur, int target, Map<Long, Integer> prefix) {
        if (node == null) return 0;

        cur += node.val;                                       // choose
        int cnt = prefix.getOrDefault(cur - target, 0);        // goal check
        prefix.merge(cur, 1, Integer::sum);

        cnt += dfs(node.left, cur, target, prefix)             // explore
             + dfs(node.right, cur, target, prefix);

        prefix.merge(cur, -1, Integer::sum);                   // undo (backtrack)
        return cnt;
    }
}
```

```python
# python
# LC 437 - Path Sum III
# IDEA: prefix-sum counter over the current root->node path, decremented on the way back up
from collections import defaultdict

class Solution(object):
    # time = O(N), space = O(H)
    def pathSum(self, root, targetSum):
        prefix = defaultdict(int)
        prefix[0] = 1                       # empty prefix

        def dfs(node, cur):
            if not node:
                return 0
            cur += node.val                 # choose
            cnt = prefix[cur - targetSum]   # goal check
            prefix[cur] += 1

            cnt += dfs(node.left, cur) + dfs(node.right, cur)   # explore

            prefix[cur] -= 1                # undo (backtrack)
            return cnt

        return dfs(root, 0)
```

**容易踩到的坑**
- `prefix[0] = 1` 是讓合法路徑能**從根開始**的關鍵。
- Java 要用 `long`：節點值可到 `10^9`，路徑可深達 `1000`。
- 忘記做遞減，會把長在**兄弟**子樹裡的路徑也算進來 → 答案錯，而且不會 crash。

---

## 1️⃣2️⃣ 模板：在**隱含**的樹上回溯（LC 386）

不是每棵樹都由 `TreeNode` 組成。`1..n` 依字典序排列，本身就是一棵 **10 元的 trie**：
`1 → 10 → 100 …`，而字典序恰好就是它的**前序走訪**。

**模式**：子節點 = `parent * 10 + d`。`child > n` 時剪枝。

```java
// java
// LC 386 - Lexicographical Numbers
// IDEA: preorder DFS over the implicit 10-ary trie of numbers; child = cur*10 + d
class Solution {
    // time = O(N), space = O(log N) recursion depth
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>();
        for (int i = 1; i <= 9; i++) dfs(i, n, res);   // 9 trie roots
        return res;
    }

    private void dfs(int cur, int n, List<Integer> res) {
        if (cur > n) return;
        res.add(cur);                                   // preorder: visit before children
        for (int d = 0; d <= 9; d++) {
            int next = cur * 10 + d;
            if (next > n) break;                        // prune (d only grows)
            dfs(next, n, res);
        }
    }
}
```

```python
# python
# LC 386 - Lexicographical Numbers
# IDEA: preorder DFS on the implicit 10-ary trie of 1..n
class Solution(object):
    # time = O(N), space = O(log N)
    def lexicalOrder(self, n):
        res = []

        def dfs(cur):
            if cur > n:
                return
            res.append(cur)
            for d in range(10):
                nxt = cur * 10 + d
                if nxt > n:
                    break
                dfs(nxt)

        for i in range(1, 10):
            dfs(i)
        return res
```

**追問（`O(1)` 額外空間）** — 面試官最愛的變化。同樣的走法，改成**迭代**；
「往上回到父節點」變成*除以 10*：

```java
// java
// LC 386 - Lexicographical Numbers (O(1) space, explicit backtracking)
// IDEA: go down (*10) when possible, else move right (+1),
//       else climb (/10) until a right sibling exists.
class Solution {
    // time = O(N), space = O(1) excluding output
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>();
        int cur = 1;
        for (int i = 0; i < n; i++) {
            res.add(cur);
            if ((long) cur * 10 <= n) {
                cur *= 10;                                  // go deeper
            } else {
                while (cur % 10 == 9 || cur + 1 > n) cur /= 10;   // backtrack up
                cur++;                                      // next sibling
            }
        }
        return res;
    }
}
```

```python
# python
# LC 386 - Lexicographical Numbers (O(1) space, explicit backtracking)
class Solution(object):
    # time = O(N), space = O(1) excluding output
    def lexicalOrder(self, n):
        res, cur = [], 1
        for _ in range(n):
            res.append(cur)
            if cur * 10 <= n:
                cur *= 10                       # go deeper
            else:
                while cur % 10 == 9 or cur + 1 > n:
                    cur //= 10                  # backtrack up
                cur += 1                        # next sibling
        return res
```

---

## 1️⃣3️⃣ 模板：用深度當鍵的路徑狀態**堆疊**（LC 388）

一段有縮排的文字就是一棵序列化的樹，tab 的數量**就是**深度。
你完全不用把樹建出來——只要保留**當前根→節點的路徑長度**，
一旦跳到比較淺的深度，較深的那些項目就自動被丟掉（也就是回溯）。

**核心想法**：`pathLen[d]` = 深度 `d` 處的目錄前綴長度。
由上往下掃描時寫入 `pathLen[depth + 1]`，會**覆蓋掉**任何殘留的兄弟分支，
這正好就是經典模板裡的「pop」。

```java
// java
// LC 388 - Longest Absolute File Path
// IDEA: implicit tree from indentation; pathLen[depth] = prefix length of current root->node path.
//       A shallower line overwrites the deeper entries => automatic backtrack.
class Solution {
    // time = O(N) over input chars, space = O(D) depth
    public int lengthLongestPath(String input) {
        Map<Integer, Integer> pathLen = new HashMap<>();
        pathLen.put(0, 0);                              // depth 0 has no prefix
        int best = 0;

        for (String line : input.split("\n")) {
            int depth = line.lastIndexOf('\t') + 1;     // tabs are always leading
            String name = line.substring(depth);

            if (name.contains(".")) {                   // leaf = file
                best = Math.max(best, pathLen.get(depth) + name.length());
            } else {                                    // internal node = dir
                pathLen.put(depth + 1, pathLen.get(depth) + name.length() + 1);  // +1 for '/'
            }
        }
        return best;
    }
}
```

```python
# python
# LC 388 - Longest Absolute File Path
# IDEA: depth-keyed path-length map; deeper stale entries are overwritten, never reused
class Solution(object):
    # time = O(N), space = O(D)
    def lengthLongestPath(self, input):
        best = 0
        path_len = {0: 0}

        for line in input.split('\n'):
            name = line.lstrip('\t')
            depth = len(line) - len(name)

            if '.' in name:                                        # file -> leaf
                best = max(best, path_len[depth] + len(name))
            else:                                                  # dir -> internal node
                path_len[depth + 1] = path_len[depth] + len(name) + 1   # +1 for '/'

        return best
```

**容易踩到的坑**
- 完全**沒有檔案**時要回傳 `0`（`"a"` → `0`），而不是最長的目錄路徑。
- 那個 `+1` 是 `/` 分隔符；深度 `0` 的檔案剛好不會多一個分隔符。
- 用 `Deque<Integer>` 一路彈到 `size == depth`，是同一件事的顯式堆疊寫法。

---

## 1️⃣4️⃣ 變形：狀態往**下**流，結構往**上**改（LC 1110）

骨架一樣，只是不再把東西 push 進路徑，而是把一個**布林值往下傳**，
再把**重接好的子樹往上回傳**。

> **轉折**：一個節點會變成新的森林根，條件是*（它的父節點被刪掉，或它本身就是樹根）* **而且** *它自己沒被刪掉*——
> 所以你要帶的「狀態」就只是 `parentWasDeleted`。用**前序**收集根，用**後序**重接子節點。

```java
// java
// LC 1110 - Delete Nodes And Return Forest
// IDEA: pass "is this node a forest root" down; return null upward for deleted nodes so the parent unlinks it.
class Solution {
    // time = O(N), space = O(H)
    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        Set<Integer> toDel = new HashSet<>();
        for (int v : to_delete) toDel.add(v);
        List<TreeNode> res = new ArrayList<>();
        dfs(root, true, toDel, res);
        return res;
    }

    private TreeNode dfs(TreeNode node, boolean isRoot, Set<Integer> toDel, List<TreeNode> res) {
        if (node == null) return null;

        boolean deleted = toDel.contains(node.val);
        if (isRoot && !deleted) res.add(node);                 // new forest root

        node.left  = dfs(node.left,  deleted, toDel, res);     // children are roots iff I'm deleted
        node.right = dfs(node.right, deleted, toDel, res);

        return deleted ? null : node;                          // unlink myself from my parent
    }
}
```

```python
# python
# LC 1110 - Delete Nodes And Return Forest
# IDEA: carry "parent was deleted" down, return None up so the parent drops the link
class Solution(object):
    # time = O(N), space = O(H)
    def delNodes(self, root, to_delete):
        to_del = set(to_delete)
        res = []

        def dfs(node, is_root):
            if not node:
                return None
            deleted = node.val in to_del
            if is_root and not deleted:
                res.append(node)                 # new forest root

            node.left = dfs(node.left, deleted)  # children become roots iff I'm deleted
            node.right = dfs(node.right, deleted)

            return None if deleted else node     # unlink myself

        dfs(root, True)
        return res
```

---

## 1️⃣5️⃣ 路徑模板總表

| LC | 題目 | 路徑狀態 | 復原方式 |
|----|---------|-----------|------|
| 113 | Path Sum II | `List<Integer>` ＋剩餘的和 | `path.remove(size-1)` |
| 257 | Binary Tree Paths | 字串（傳值） | 隱含 |
| 129 | Sum Root to Leaf Numbers | `int num`（傳值） | 隱含 |
| **236** | Lowest Common Ancestor | 節點 list，失敗才 pop | 有條件的 pop |
| **437** | Path Sum III | `Map<prefixSum, count>` | `count[cur] -= 1` |
| **386** | Lexicographical Numbers | 目前的數字 | `cur /= 10` |
| **388** | Longest Absolute File Path | `pathLen[depth]` | 覆寫／彈到該深度 |
| **1110** | Delete Nodes And Return Forest | `parentWasDeleted` 旗標 | 往上回傳 `null` |

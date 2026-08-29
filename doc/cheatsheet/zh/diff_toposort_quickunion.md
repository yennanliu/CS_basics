# 快速比較：拓撲排序 vs Quick Union（併查集）

> **範圍** — **只是一份決策文件** —— 拓撲排序 vs 併查集：各自回答什麼問題、直覺選錯的地方在哪，以及兩者都能解的題目。
> **另見**：[topology_sorting.md](./topology_sorting.md)；[union_find.md](./union_find.md)；[graph.md](./graph.md)。

| 面向 | 拓撲排序 | Quick Union（併查集） |
|:---|:---|:---|
| 目的 | 在滿足相依關係的前提下**排出節點順序**（只適用 **DAG**） | **找連通分量**與**偵測環**（**無向圖**） |
| 適用對象 | **有向圖（DAG）** | **無向圖** |
| 能偵測環？ | ✅（排不出拓撲序就代表有環） | ✅（兩個節點已經同一個 parent 就代表有環） |
| 處理邊的方向？ | ✅（方向有意義：`u ➔ v`） | ❌（忽略方向：只管有沒有連在一起） |
| 輸出 | 有序的節點列表（`[u, v, w]`） | 連通分量或環的偵測結果 |
| 常見用途 | 排課、build 系統、相依關係解析 | Kruskal MST、動態連通性、併查集題型 |
| 時間複雜度 | O(V + E) | 每次操作接近 O(1)（搭配路徑壓縮的攤還成本） |
| 空間複雜度 | O(V + E) | O(V) |

---

## LeetCode 題目清單

- [Topological Sort](https://leetcode.com/problem-list/topological-sort/)
- [Union-Find](https://leetcode.com/problem-list/union-find/)

## 🏩 觀念上的差別

| | 拓撲排序 | Quick Union |
|:-|:-|:-|
| 遵守相依順序 | ✅ | ❌ |
| 全部連在一起了嗎？ | ❌ | ✅ |
| 偵測有向環？ | ✅ | ❌ |

---

## ⚙️ 演算法核心想法

### ➔ 拓撲排序（給 DAG 用）
- 想成**生產線**：  
  - 車架沒好之前，車子組不起來。
- 先處理沒有入邊的節點。
- 如果卡住了（還有節點沒處理，但已經找不到「入度為 0」的節點），就代表**有環**。

兩種常見寫法：
- **BFS** 搭配入度陣列。
- **DFS** 搭配遞迴 + 後序。

---

### ➔ Quick Union（併查集）
- 想成**朋友圈**：
  - Alice 認識 Bob、Bob 認識 Charlie → 同一圈。
- 每個節點都指向一個 *parent*。
- 兩個節點已經有相同的 root → 代表**有環**（就無向圖而言）。
- 兩個關鍵最佳化：
  - **路徑壓縮**（find 的過程順手把樹壓平）
  - **按 rank／size 合併**（把小的樹掛到大的底下）

---

## 🚀 視覺範例

想像同一組輸入：

```text
Courses: 0 -> 1 -> 2
```

| | 拓撲排序 | Quick Union |
|:-|:-|:-|
| 會發生什麼？ | 輸出 [0, 1, 2]（順序有意義） | 單純把它們歸成一群（在意的是連通，不是順序） |
| 為什麼？ | 因為 0 一定要在 1 之前完成，1 又要在 2 之前 | 只在乎它們有沒有連在一起 |
| 環的偵測？ | 出現回邊（例如 2 ➔ 0）→ **有環** | 想連兩個已經連通的節點 → **有環** |

---

## 🧪 類比

- **拓撲排序**像**蓋摩天大樓**：  
  - 一定要照順序**由下往上**把每層蓋完。

- **Quick Union** 像**找朋友群**：  
  - 誰先講到話不重要，只要找出連在一起的群體。

---

## 📜 重點整理

| 問題 | 答案 |
|:---|:---|
| 它們解的是同一個問題嗎？ | ❌ |
| 兩者都能偵測環嗎？ | ✅（但情境不同） |
| Course Schedule（有向圖）該用哪個？ | **拓撲排序** ✅ |
| 每次操作哪個比較快？ | **Quick Union**（攤還約 O(1)） ✅ |
| 哪個能處理相依順序？ | **拓撲排序** ✅ |

---

## ✅ 最終心智模型

| | 拓撲排序 | Quick Union |
|:-|:-|:-|
| 圖的型別 | 有向 | 無向 |
| 目標 | 遵守順序、偵測環 | 連接分量、偵測無向環 |
| 典型題目 | 排程、編譯順序 | Kruskal MST、動態連通性 |

---

## 🧭 決策表 —— 哪一題該用哪個工具 ⭐⭐⭐⭐⭐

**怎麼讀這張表**：判斷的問題永遠是同樣兩句 ——
1. 邊是**有向**的嗎（`u` 一定要排在 `v` 前面）？ → **拓撲排序**
2. 你只需要知道**「這些是不是同一坨？」**、而且**不在乎順序**？ → **併查集**

### **選拓撲排序**

| LC | 題目 | 為什麼是拓撲排序 |
|:---|:---|:---|
| 210 | Course Schedule II | 有向的先修關係，**而且**真的要印出一個順序 |
| 802 | Find Eventual Safe States | 有向；「安全」= 走不到環 → 在反向圖上剝掉**出度 0** 的點 |
| 851 | Loud and Rich | `richer` 是一個有向的偏序 → DAG + 記憶化／拓撲 DP |
| 1462 | Course Schedule IV | 有向先修關係 + 可達性查詢（先拓撲序再做傳遞閉包） |
| 2115 | Find All Possible Recipes from Given Supplies | 一道食譜要**所有**材料都有了才解鎖 → 經典的入度倒數 |
| 1857 | Largest Color Value in a Directed Graph | 拓撲序 + 在 26 個顏色計數器上做 DP；有環就回傳 `-1` |
| 1203 | Sort Items by Groups Respecting Dependencies | 兩層巢狀拓撲排序（先群組，再排群組內的項目） |
| 310 | Minimum Height Trees | 雖然是無向圖，但答案是一個**剝除順序**（一層一層剝掉度數 1 的葉子）—— 併查集生不出這個 |
| 1591 | Strange Printer II | 顏色必須照相依順序印出來 → 在顏色上建 DAG |

### **選併查集**

| LC | 題目 | 為什麼是併查集 |
|:---|:---|:---|
| 547 | Number of Provinces | 無向、問「有幾坨」—— 完全不涉及順序 |
| 684 | Redundant Connection | 無向；第一條兩端已經同 root 的邊就是把環閉起來的那條 |
| 721 | Accounts Merge | 以 email 為 key 的傳遞式合併；方向沒有意義 |
| 990 | Satisfiability of Equality Equations | `==` 是等價關係 → 先把所有 `==` union 起來，再逐一驗證 `!=` |
| 947 | Most Stones Removed with Same Row or Column | 依 **row-key／col-key** 合併，不是依格子相鄰 |
| 839 | Similar String Groups | 由兩兩相似關係構成的分量 |
| 1319 | Number of Operations to Make Network Connected | 答案 = `components - 1`，可行的條件是 `edges >= n-1` |
| 1971 | Find if Path Exists in Graph | 純粹的無向可達性 |
| 1584 | Min Cost to Connect All Points | Kruskal MST = 邊排序 + 併查集 |
| 1489 | Find Critical and Pseudo-Critical Edges in MST | 強制納入／強制排除某條邊，重跑 Kruskal |
| 2092 | Find All People With Secret | 依時間戳分組，再對沒趕上的人**復原**（重設 parent） |
| 1697 | Checking Existence of Edge Length Limited Paths | 離線做法：查詢和邊都按權重排序，逐步 union |
| 1202 | Smallest String With Swaps | 可互換的索引構成分量 → 在每個分量內把字元排序 |
| 1722 | Minimize Hamming Distance After Swap Operations | 同樣的想法：在每個可交換分量內比較多重集合 |
| 1559 | Detect Cycles in 2D Grid | 網格上的無向環；只 union 同字元的**右邊 + 下面**鄰居，避免重複計算 |
| 1632 | Rank Transform of a Matrix | **兩個都要**：併查集把同列／同行的相等值歸群，再用拓撲式的排序指派 rank |

### **兩個都別選（標籤是陷阱）**

| LC | 題目 | 實際上該用 |
|:---|:---|:---|
| 200 | Number of Islands | DFS/BFS flood fill —— 併查集雖然正確但比較重。只有島嶼是**逐步加入／合併**時併查集才划算（見 827 Making A Large Island） |
| 128 | Longest Consecutive Sequence | 雜湊集合往外擴，O(n)。併查集可行，但是過度設計 |
| 329 | Longest Increasing Path in a Matrix | 記憶化 DFS。它被標成拓撲排序是因為嚴格遞增的規則隱含了一個 DAG，但你從來不需要真的把順序生出來；併查集則**根本做不到**（路徑*長度*是有方向的） |
| 785 | Is Graph Bipartite? | BFS/DFS 二著色。併查集只能靠「把每個節點和它鄰居的敵人 union 起來」的技巧（和 886 Possible Bipartition 是同一招） |
| 130 | Surrounded Regions / 1254 Number of Closed Islands | 從邊界往內 DFS 更單純；併查集版本得多造一個人工的虛擬「外部」節點 |

---

## ⚠️ 直覺選錯的陷阱 ⭐⭐⭐⭐

- **「圖上找環 → 併查集」** —— 只有**無向**時才成立。LC 802（Find Eventual Safe States）和 LC 210（Course Schedule II）都是有向的；併查集根本分不出 `u ➔ v` 和 `v ➔ u`。
- **LC 684 → LC 685（Redundant Connection II）** —— 經典誘餌。684 是無向的，單純併查集就好。685 是**有向**的，所以一個節點可以有**兩個 parent** 卻完全沒有環。單純的併查集會給出錯的邊；看下面的模板。
- **LC 310（Minimum Height Trees）** —— 無向樹會讓人直接聯想到併查集，但併查集回報的是*分量*，不是*重心*。正解是在無向圖上跑一個**形狀像拓撲排序**的演算法：反覆移除度數為 1 的節點。
- **LC 947（Most Stones Removed）** —— 把網格上相鄰的石頭 union 起來是錯的。兩顆石頭只要**同列或同行**就有關係，距離多遠都算。
- **LC 1361（Validate Binary Tree Nodes）** —— 這題確實**兩種**都能解；兩種都要會（模板在下面）。
- **經驗法則**：要求的輸出是一個**序列**，併查集出局。要求的輸出是**數量／是不是在一起的是非題**，拓撲排序就是殺雞用牛刀。

---

## 🔀 同一題、兩種工具 —— LC 1361 Validate Binary Tree Nodes ⭐⭐⭐⭐

給 `n` 個節點以及 `leftChild[i]` / `rightChild[i]`（`-1` = 沒有），判斷它們是否構成**剛好一棵**合法的二元樹。
兩種工具都能用，因為失敗只有三種樣態：某個節點有**兩個 parent**、出現**環**，或是變成**森林**。

```java
// java
// time = O(n log n) (path halving only; O(n * α(n)) needs union by size/rank too), space = O(n)
// IDEA: union-find view -> a tree is "n nodes, no node with 2 parents,
//       no edge that closes a cycle, and exactly 1 component at the end"
// LC 1361 - Validate Binary Tree Nodes  (approach 1: union-find)
class SolutionUF {
    int[] parent;

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];   // path compression
            x = parent[x];
        }
        return x;
    }

    public boolean validateBinaryTreeNodes(int n, int[] leftChild, int[] rightChild) {
        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int[] indeg = new int[n];
        int components = n;

        for (int i = 0; i < n; i++) {
            for (int child : new int[]{ leftChild[i], rightChild[i] }) {
                if (child == -1) continue;
                if (++indeg[child] > 1) return false;   // 2 parents -> not a tree
                int ru = find(i), rv = find(child);
                if (ru == rv) return false;             // edge closes a cycle
                parent[rv] = ru;
                components--;
            }
        }
        return components == 1;                          // forest -> false
    }
}
```

```java
// java
// time = O(n), space = O(n)
// IDEA: topological / indegree view -> exactly one indegree-0 root,
//       then a BFS from that root must reach all n nodes
// LC 1361 - Validate Binary Tree Nodes  (approach 2: indegree + BFS)
class SolutionTopo {
    public boolean validateBinaryTreeNodes(int n, int[] leftChild, int[] rightChild) {
        int[] indeg = new int[n];
        for (int i = 0; i < n; i++) {
            if (leftChild[i]  != -1 && ++indeg[leftChild[i]]  > 1) return false;
            if (rightChild[i] != -1 && ++indeg[rightChild[i]] > 1) return false;
        }

        int root = -1;
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                if (root != -1) return false;   // 2+ roots -> forest
                root = i;
            }
        }
        if (root == -1) return false;           // no root -> cycle

        Deque<Integer> q = new ArrayDeque<>();
        q.offer(root);
        int seen = 0;
        while (!q.isEmpty()) {
            int cur = q.poll();
            seen++;
            if (leftChild[cur]  != -1) q.offer(leftChild[cur]);
            if (rightChild[cur] != -1) q.offer(rightChild[cur]);
        }
        return seen == n;                        // unreached nodes -> cycle / forest
    }
}
```

```python
# python
# time = O(n log n) (path halving only; O(n * α(n)) needs union by size/rank too), space = O(n)
# IDEA: union-find view -> reject a 2nd parent, reject a cycle-closing edge,
#       then require exactly 1 remaining component
# LC 1361 - Validate Binary Tree Nodes  (approach 1: union-find)
class SolutionUF:
    def validateBinaryTreeNodes(self, n, leftChild, rightChild):
        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]   # path compression
                x = parent[x]
            return x

        indeg = [0] * n
        components = n

        for i in range(n):
            for child in (leftChild[i], rightChild[i]):
                if child == -1:
                    continue
                indeg[child] += 1
                if indeg[child] > 1:
                    return False                # 2 parents -> not a tree
                ru, rv = find(i), find(child)
                if ru == rv:
                    return False                # edge closes a cycle
                parent[rv] = ru
                components -= 1

        return components == 1                  # forest -> False
```

```python
# python
# time = O(n), space = O(n)
# IDEA: topological / indegree view -> exactly one indegree-0 root,
#       then BFS from it must visit all n nodes
# LC 1361 - Validate Binary Tree Nodes  (approach 2: indegree + BFS)
from collections import deque

class SolutionTopo:
    def validateBinaryTreeNodes(self, n, leftChild, rightChild):
        indeg = [0] * n
        for i in range(n):
            for child in (leftChild[i], rightChild[i]):
                if child == -1:
                    continue
                indeg[child] += 1
                if indeg[child] > 1:
                    return False

        root = -1
        for i in range(n):
            if indeg[i] == 0:
                if root != -1:
                    return False       # 2+ roots -> forest
                root = i
        if root == -1:
            return False               # no root -> cycle

        q, seen = deque([root]), 0
        while q:
            cur = q.popleft()
            seen += 1
            for child in (leftChild[cur], rightChild[cur]):
                if child != -1:
                    q.append(child)
        return seen == n
```

**帶走的重點**：輸入是*有向*的時候，入度（拓撲）視角比較自然 —— 它直接對應到那幾種失敗樣態。併查集在這裡之所以還能用，只是因為加上「最多一個 parent」這道防線之後，二元樹同時也是一棵合法的*無向*樹。

---

## 🧩 只用併查集會錯的情況 —— LC 685 Redundant Connection II ⭐⭐⭐⭐

**LC 684**（無向）：單純併查集，回傳第一條兩端已經同 root 的邊。✅
**LC 685**（有向）：只用併查集是**錯的**，因為有根樹還要求*每個節點恰好有一個 parent*。有兩種失敗樣態，而且可能同時發生：

| 情況 | 症狀 | 答案 |
|:---|:---|:---|
| A | 某個節點有**兩個 parent**，但沒有環 | 兩條 parent 邊中**較後面**的那條 |
| B | 只有**環**，每個節點都只有 1 個 parent | 把環閉起來的那條邊 |
| C | **兩者都有**（環經過那個雙 parent 的節點） | 落在環上的那條**較前面**的 parent 邊 |

```java
// java
// time = O(n log n) (path halving only; O(n * α(n)) needs union by size/rank too), space = O(n)
// IDEA: 1) scan for a node with 2 parents -> remember cand1 (1st edge) & cand2 (2nd edge)
//       2) union all edges EXCEPT cand2. A cycle now means cand2 was innocent:
//          answer is cand1 (if it exists) else the cycle edge. No cycle -> cand2.
// LC 685 - Redundant Connection II
class Solution {
    private int find(int[] p, int x) {
        while (p[x] != x) {
            p[x] = p[p[x]];
            x = p[x];
        }
        return x;
    }

    public int[] findRedundantDirectedConnection(int[][] edges) {
        int n = edges.length;
        int[] parentOf = new int[n + 1];        // parent recorded in the INPUT
        int[] cand1 = null, cand2 = null;

        for (int[] e : edges) {
            int u = e[0], v = e[1];
            if (parentOf[v] != 0) {             // v already had a parent
                cand1 = new int[]{ parentOf[v], v };
                cand2 = e;
            } else {
                parentOf[v] = u;
            }
        }

        int[] p = new int[n + 1];
        for (int i = 0; i <= n; i++) p[i] = i;

        for (int[] e : edges) {
            if (e == cand2) continue;           // tentatively drop the 2nd parent edge
            int ru = find(p, e[0]), rv = find(p, e[1]);
            if (ru == rv) return (cand1 == null) ? e : cand1;   // case B / case C
            p[rv] = ru;
        }
        return cand2;                            // case A
    }
}
```

```python
# python
# time = O(n log n) (path halving only; O(n * α(n)) needs union by size/rank too), space = O(n)
# IDEA: 1) find a node with 2 parents -> cand1 (1st edge), cand2 (2nd edge)
#       2) union everything except cand2; a cycle means cand2 was innocent
#          -> answer cand1 if it exists else the cycle edge; no cycle -> cand2
# LC 685 - Redundant Connection II
class Solution:
    def findRedundantDirectedConnection(self, edges):
        n = len(edges)
        parent_of = [0] * (n + 1)          # parent recorded in the INPUT
        cand1 = cand2 = None

        for u, v in edges:
            if parent_of[v]:               # v already had a parent
                cand1, cand2 = [parent_of[v], v], [u, v]
            else:
                parent_of[v] = u

        p = list(range(n + 1))

        def find(x):
            while p[x] != x:
                p[x] = p[p[x]]
                x = p[x]
            return x

        for e in edges:
            if e == cand2:                 # tentatively drop the 2nd parent edge
                continue
            ru, rv = find(e[0]), find(e[1])
            if ru == rv:                   # case B / case C
                return e if cand1 is None else cand1
            p[rv] = ru
        return cand2                       # case A
```

**為什麼這是「選錯工具」的標準教材**：併查集回答的是*「這些連在一起了嗎？」*。它永遠回答不了*「這個節點的 parent 是不是太多了？」* —— 那是一個**入度**問題。有向的題目幾乎都得再疊一層入度的帳。

---

## 📚 深入閱讀

這一頁只是**決策輔助**。完整的模板、變形和題目走查請看：

- [`topology_sorting.md`](./topology_sorting.md) — BFS（Kahn）+ DFS 模板、字典序、環的回報
- [`union_find.md`](./union_find.md) — 路徑壓縮、按 rank/size 合併、虛擬節點、帶權／可回滾的 DSU
- [`graph.md`](./graph.md) — 一般走訪、二分圖檢查、MST、最短路徑

---

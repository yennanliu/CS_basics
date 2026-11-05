<!-- 74f33bb3bacd -->
# BST — 實戰題解

> **範圍** — 在 `bst.md` 某個模板上多加一步的 BST 題，每題以一張題目卡寫一次，放在它歸約到的那張卡底下；模板或進階卡已經寫出的題目，這裡只列指標、不重解。
> **另見**：[bst.md](./bst.md) — 這些題目歸約到的 BST 模板；[bst_advanced.md](./bst_advanced.md) — 延遲式迭代器、修復壞掉的 BST、rank 查詢與各種建構變形；[tree_lca_distance.md](./tree_lca_distance.md) — 根到葉與任意點到任意點的路徑題（LC 112、113、124、129、257、437），用不到大小順序；[binary_tree.md](./binary_tree.md) — LC 298 以及其他無序樹的 DFS 寫法。

<!-- 58caebeae94c -->
## LeetCode 題目清單

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

<!-- 7241af92bbd5 -->
## 總覽

BST 題組，依各題歸約到的 [bst.md](./bst.md) 模板分組，順序與那份講義的模板順序相同。模板本身已經解掉的題目，
在索引裡只是一列指向它的家，不會再抄一份。原本放在這裡的路徑和家族在任何二元樹上都成立，所以它們在
[tree_lca_distance.md](./tree_lca_distance.md)。

<!-- 72178203350c -->
## 索引

| LC | 題目 | 歸約到 | 難度 |
|---|---|---|---|
| 776 | [Split BST](#split-bst--lc-776) | [bst § Template 3b](./bst.md#template-3b-trim-to-a-range--lc-669-) — 兩邊都留下，而不是丟掉一邊 | Medium |
| 98 | Validate Binary Search Tree — 在 [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) 寫出，含迭代版 | [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) | Medium |
| 449 | [Serialize and Deserialize BST](#serialize-and-deserialize-bst--lc-449) | [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) — 由上下界決定每個前序值放哪裡 | Medium |
| 538 | Convert BST to Greater Tree — 在 [bst § Template 5](./bst.md#pattern-reverse-inorder-for-descending) 寫出 | [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230)，反向 | Medium |
| 1038 | Binary Search Tree to Greater Sum Tree — 與 LC 538 同一題、同一份程式碼 | [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230)，反向 | Medium |
| 173 | Binary Search Tree Iterator — 在 [bst_advanced § Template 1](./bst_advanced.md#template-1-lazy-in-order-iterator--lc-173-) 寫出 | [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230)，在兩次呼叫之間暫停 | Medium |

<!-- 26a8df74efc1 -->
## 修剪到範圍的例題

<!-- f696ac145595 -->
### Split BST — LC 776

> **關鍵** — 修剪會丟掉根在範圍外的那一側；拆分則把它留下來當第二棵樹。每個節點回傳的兩棵樹裡，
> 有一棵就是節點自己，另一棵只來自「可能跨過 `target` 的那個子節點」的拆分，所以只走一條路徑。

**歸約到**：[bst § Template 3b](./bst.md#template-3b-trim-to-a-range--lc-669-) —— 同樣是「另一側整個落在切點的同一邊」，
只是什麼都不丟。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 接錯一半：`root.val <= target` 時，`root.right` 要接 `split[0]`（小的那半），不是 `split[1]`。
  接反的話，上例中 3 會跑到 2 底下。
- 兩個子節點都拆是 O(n)，結果仍然正確；但只有一個子節點可能跨過切點。

| 操作 | LC | 移除的節點 | 回傳 |
|---|---|---|---|
| Delete | 450 | 一個 | 一個 root |
| Trim | 669 | 所有在 `[low, high]` 之外的節點 | 一個 root |
| Split | 776 | 沒有 | 兩個 root |

<!-- 15eec8bab463 -->
## 用上下界驗證的例題

<!-- f0290e755f2d -->
### Serialize and Deserialize BST — LC 449

> **關鍵** — BST 光靠前序就能決定整棵樹：下一個值落在目前子樹的 `(low, high)` 區間內，
> 它才屬於這棵子樹。所以編碼不需要 null 標記，這正是它比 LC 297 的編碼更精簡的原因。

**歸約到**：[bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) —— 同一個往下帶的區間，
拿來決定值放哪裡，而不是拿來拒絕。

<!--CODE-->

**陷阱**
- `"".split(",")` 是 `[""]`，而 `int("")` 會丟例外：空樹要單獨擋掉。
- 嚴格的 `<` 假設值都不同，BST 保證這點。若有重複值，光靠前序就無法決定形狀。
- LC 297 那種帶 `#` 標記的層序編碼在這裡也會過，但它沒用到 BST，寫出的符號大約多一倍。
  要能說出為什麼有了上下界就不需要標記。

**相似題** — LC 1008（只做反序列化那一半，[bst_advanced § Template 4](./bst_advanced.md#template-4-preorder-build-with-bounds--lc-1008)） · LC 297（任意二元樹：一定要標記）

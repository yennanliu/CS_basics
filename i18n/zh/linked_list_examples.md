<!-- f6695f0bc2ec -->
# 鏈結串列 — 範例題解

> **範圍** — [linked_list.md](./linked_list.md) 背後的題解存放處：反轉、合併、切分、複製、攤平與串列排序類題目，每題每語言各一份標準解，並依各自演練的技巧分組。
> **另見**：[linked_list.md](./linked_list.md) — 母文件，擁有虛擬頭節點技巧、reverse-k 基本操作、基本操作與這些解法所演練的選擇表；[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — 環偵測與快慢指標家族本身；[design.md](./design.md) — LRU 以及其他「串列 + 雜湊表」的設計題；[heap.md](./heap.md) — LC 23 走堆積(heap)的那條路；[monotonic_stack.md](./monotonic_stack.md) — LC 1019 背後的理論；[prefix_sum.md](./prefix_sum.md) — LC 1171 背後的理論。

<!-- 1d43e2ef29a3 -->
## LeetCode 題目清單

- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Doubly-Linked List](https://leetcode.com/problem-list/doubly-linked-list/)

<!-- 5a0855959bfc -->
## 總覽

這裡是 [linked_list.md](./linked_list.md) 的長尾。母文件保留技巧本身 —
虛擬頭節點、reverse-k 輔助函式、快慢指標、基本操作 — 而這份檔案收容那些*應用*它們的題目，
免得技巧被上千行的解法給埋沒。

<!-- ccfdf7f452d9 -->
### 關鍵性質
- **複雜度**：見母文件的 [Time Complexity](./linked_list.md#time-complexity) 表格；除非解法自己的註解另有說明，下面每一份解法都是 O(n) 時間、O(1) 空間
- **核心想法**：每一節都是對某個母文件技巧的一次演練 — 技巧才是要背下來的東西，這些只是反覆練習
- **什麼時候用**：當你已經知道這題要用哪個技巧，想看它從頭到尾完整寫一遍時

<!-- 0aa0ea8d9ade -->
### 哪些解法刻意*不*放在這裡

有三題改放在母文件裡，因為它們的解說本身*就是*重點，程式碼只是附帶的產物：

| LC | 題目 | 為什麼放在母文件 |
|---|---|---|
| 206 | Reverse Linked List | 它*就是*那個基本操作 — [1-1-1) / 1-1-2)](./linked_list.md#1-1-1-reverse-linked-list-iteration--lc-206) |
| 19 | Remove Nth Node From End | 重點在虛擬節點的分情況討論 — [Why Dummy Node?](./linked_list.md#why-dummy-node-visual-comparison-lc-19)。下面只放兩種 Java 寫法 |
| 92 | Reverse Linked List II | Python 版就是 reverse-k 輔助函式套用一次 — [Reverse K Nodes Helper Pattern](./linked_list.md#reverse-k-nodes-helper-pattern-)。下面只放行內展開的 Java 版 |

<!-- afd61950e8fc -->
## 反轉與重排

<!-- 9f44339411b3 -->
### 1) Reverse Linked List II — LC 92

> **核心想法**：*定位*到位置 `left` 前面的那個節點，*反轉* `right - left + 1` 個節點，
> 再把兩端*接回去*。Python 版就是母文件裡的
> [Reverse K Nodes Helper Pattern](./linked_list.md#reverse-k-nodes-helper-pattern-)；
> 下面的 Java 則是同一趟走訪，只是不用輔助函式、直接行內展開。

<!--CODE-->

<!-- c0b97737fe69 -->
### 2) Reorder List — LC 143


<!--CODE-->

<!--CODE-->

<!-- 336fd585c9a9 -->
### 3) Swap Nodes in Pairs — LC 24


> **兩兩交換相鄰節點**，但不能動到值 — 只能重接 `next` 指標。
> `1 -> 2 -> 3 -> 4`  變成  `2 -> 1 -> 4 -> 3`

<!-- 6403643e9b07 -->
#### **1. 核心想法**

每次交換其實牽涉到 **3 個錨點**，而不是 2 個：

<!--CODE-->

- `prev`   — 這一對**前面**的那個節點（第一輪時是 `dummy`）。它掌握著進來的那條連結。
- `first`  — 這一對的**第 1 個**節點（交換後會變成第 2 個）。
- `second` — 這一對的**第 2 個**節點（交換後會變成第 1 個，也就是新的頭）。

交換後這一對翻轉過來，`prev` 指向新的頭：

<!--CODE-->

我們之所以需要 `prev`（因此需要**虛擬頭節點**，見 [Dummy Head Technique](./linked_list.md#dummy-head-technique)），是因為**這一對前面的節點也必須重新指向** — 否則前一對會一直黏在舊的頭（`first`）上，而不是新的頭（`second`）。

<!-- f546b5d628eb -->
#### **2. 模式 — 我們怎麼把節點`接回去`**

有 **3 條連結要重接**，而且**順序很重要**。可以想成*「先從右邊拆開，再往左邊接回去」*：

<!--CODE-->

**為什麼一定是這個順序？** 每條連結都會蓋掉某個我們還需要的指標，所以要在覆寫*之前*先存起來：

| 步驟 | 寫入的連結 | 為什麼必須排在這裡 |
|------|--------------|------------------------|
| **(A)** `first.next = second.next` | 在步驟 (B) 摧毀 `second.next` **之前**先抓到 `rest` 的把手。`first`（未來的尾巴）現在正確地指向這一對之後的節點。 |
| **(B)** `second.next = first` | 現在可以安全翻轉了：`second` 指回 `first`。這一對內部完成反轉。 |
| **(C)** `prev.next = second` | 最後把前端勾上：這一對前面的節點現在指向新的頭 `second`。 |

> ⚠️ 如果你在 **(A)** *之前*就做 **(C)** 或 **(B)**，就會覆寫掉 `second.next`，因而**失去對 `rest` 的參照** — 串列的尾巴就整段掉了。

<!-- a9c253fa73ca -->
#### **圖解**（`dummy -> 1 -> 2 -> 3 -> 4`，第一輪）

<!--CODE-->

第二輪用同樣方式交換 `(3,4)`，得到 `dummy -> 2 -> 1 -> 4 -> 3`；回傳 `dummy.next = 2`。

<!-- 4e07ec39a48e -->
#### **完整逐步演練**（`[1, 2, 3, 4]`，迴圈的每一輪）

我們照著下面這個迴圈逐字追蹤，追 4 個指標（`prev`、`first`、`second`、`head`），以及每做完 3 次重接 `(A)(B)(C)` 之後的串列狀態：

<!--CODE-->

**初始狀態**（在 `dummy.next = head`、`prev = dummy` 之後）：
<!--CODE-->

---

**第 1 輪** — `head=1`、`head.next=2` → 進入迴圈

<!--CODE-->
第 1 輪後的狀態：
<!--CODE-->

---

**第 2 輪** — `head=3`、`head.next=4` → 進入迴圈

<!--CODE-->
第 2 輪後的狀態：
<!--CODE-->

---

**第 3 輪** — `head = None` → 迴圈條件 `head and head.next` 為 `False` → **結束**

<!--CODE-->

**指標總結表：**

| 輪次 | `first` | `second` | (A) 後 `first.next=` | (B) 後 `second.next=` | (C) 後 `prev.next=` | 新的 `prev` | 新的 `head` |
|------|---------|----------|--------------------------|---------------------------|-------------------------|------------|------------|
| 1 | `1` | `2` | `3` | `1` | `2`（dummy→2） | `1` | `3` |
| 2 | `3` | `4` | `None` | `3` | `4`（1→4） | `3` | `None` |
| — | 停止：`head=None` | | | | | | 回傳 `dummy.next=2` |

> **奇數長度備註** — 以 `[1, 2, 3]` 為例，迴圈只跑一輪（交換 `1,2` → `2 -> 1 -> 3`），接著 `head=3` 但 `head.next=None`，條件不成立，落單的尾巴 `3` 就原封不動留著：結果是 `2 -> 1 -> 3`。

> **等價的指標走訪變體**（讓 `head` 自己停在 dummy 上走，用 `head.next` / `head.next.next` 當作那一對）。同樣是 3 次重接，只是全部改成相對於 `head` 來表達：
<!--CODE-->

<!-- 2c18d954cb57 -->
#### **遞迴視角**（同樣的重接，由上而下）

<!--CODE-->
遞迴回傳的是每一段交換後的**新頭**，再由呼叫端接上去 — 這正是迭代版裡 `prev.next = second` 在做的事。

<!-- c9dd83ad61ce -->
#### **3. 相似 LC**

| # | 題目 | 與 LC 24 的關係 |
|---|---------|------------------------|
| 206 | Reverse Linked List | 兩兩交換就是 **k=2 的分段反轉**；206 則是反轉整條串列。見 [1-1-1](./linked_list.md#1-1-1-reverse-linked-list-iteration--lc-206) |
| 25  | Reverse Nodes in k-Group | **一般化**：LC 24 恰好是 `k=2` 的情形。同樣是「接好前端 + 內部反轉」。見 [1-1-4](./linked_list.md#1-1-4-reverse-nodes-in-k-group--linked-list-iteration--lc-25) |
| 92  | Reverse Linked List II | 反轉一個**子區間** `[m, n]`；同樣重複使用「把 `prev` 勾到新頭、尾巴接回其餘部分」。見 [1)](#1-reverse-linked-list-ii--lc-92) |
| 143 | Reorder List | 把兩半交錯合併 — 另一種「成對重接 `next` 指標」的合併。見 [2)](#2-reorder-list--lc-143) |
| 1721 | Swapping Nodes in a Linked List | 更簡單 — 通常直接交換**值**；但若要交換節點，就需要同樣的三錨點功夫 |
| 61  | Rotate List | 重接一個切點；同樣需要那套指標記帳的紀律 |

<!-- 2f0028ed88d7 -->
## 合併與切分

<!-- e32a4f32844a -->
### 4) Merge Two Sorted Lists — LC 21

<!--CODE-->

<!-- 993aa1c066e4 -->
### 5) Merge K Sorted Lists — LC 23

<!--CODE-->

<!-- fc6dacd00507 -->
### 6) Split Linked List in Parts — LC 725

<!--CODE-->

<!-- 86a2bbc78507 -->
## 快慢指標與結構

<!-- ccd55ebd47a0 -->
### 7) Palindrome Linked List — LC 234

<!--CODE-->

<!-- 0ec1b075c981 -->
### 8) Intersection of Two Linked Lists — LC 160

<!--CODE-->

<!-- dfffc5972b7c -->
### 9) Remove Nth Node From End of List — LC 19

> Python 解法與完整的「為什麼要虛擬節點」說明住在
> [linked_list.md](./linked_list.md#why-dummy-node-visual-comparison-lc-19)。下面是兩種
> **Java** 寫法：用快慢指標一趟走完，以及先算長度的兩趟做法。

<!--CODE-->

<!--CODE-->

<!-- c9ae5992ae69 -->
## 複製、攤平與連通元件

<!-- 108dd35e1fd3 -->
### 10) Copy List with Random Pointer — LC 138

<!--CODE-->

<!--CODE-->

<!-- a9ef3b526c52 -->
### 11) Linked List Components — LC 817


<!--CODE-->

<!-- 9582e13c742b -->
### 12) Flatten a Multilevel Doubly Linked List — LC 430 ⭐⭐⭐⭐


**模式**：**就地接合（in-place splice）**。只要某個節點有 `child`，就把整條子鏈接到 `cur` 和 `cur.next` 之間，把兩個接縫上的 `prev` 指標補好，然後繼續往前走 — 接進來的子鏈自然會被走到，所以不用遞迴或堆疊就能處理巢狀。

**關鍵想法**：不要遞迴。每次接合需要三個指標：`next`（存起來的後繼）、`child`（新的後繼）、`tail`（子鏈的最後一個節點）。務必把 `cur.child` 設成 null — 題目要求最後不能有任何 `child` 指標殘留。

<!--CODE-->

<!--CODE-->

**視覺追蹤**：
<!--CODE-->

**相似的 LC 題目**：
| # | 題目 | 關鍵差異 |
|---|---------|----------------|
| 114 | Flatten Binary Tree to Linked List | 同樣的接合，只是在樹上：把 `left` 子樹掛到 `root` 和 `right` 之間 |
| 116 / 117 | Populating Next Right Pointers in Each Node (I / II) | 反向操作 — 用 O(1) 空間為樹的每一層*建出*一條鏈結串列（`next` 鏈） |

---

<!-- e52f2acb6a4a -->
## 串列上的算術與排序

<!-- 7760a403628b -->
### 13) Plus One Linked List — LC 369

<!--CODE-->

<!-- 0cbdea7dc968 -->
### 14) Sort List（在鏈結串列上做合併排序） — LC 148 ⭐⭐⭐⭐⭐


**模式**：唯一能在鏈結串列上以 `O(1)` 額外資料空間跑出 `O(n log n)` 的排序。三個動作：**從中間切開 → 遞迴排序兩半 → 合併兩條已排序的串列**（重用 [4) LC 21](#4-merge-two-sorted-lists--lc-21)）。

**關鍵想法**：`slow` 必須停在中點**前面**那個節點，我們才能用 `slow.next = null` 把串列實體切開。把 `fast` 從 `head.next`（而非 `head`）開始，可保證兩節點的情形 `[2,1]` 會切成 `[2]` + `[1]`，而不是 `[2,1]` + `[]`（那會無窮遞迴）。

<!--CODE-->

<!--CODE-->

**視覺追蹤**（`4 -> 2 -> 1 -> 3`）：
<!--CODE-->

**變體 — LC 147 Insertion Sort List**（巧思：`O(n^2)`，但穩定且適合單趟處理；沿著帶虛擬頭的已排序前綴走，找出每個節點該插入的位置）：
<!--CODE-->
<!--CODE-->

**相似的 LC 題目**：
| # | 題目 | 關鍵差異 |
|---|---------|----------------|
| 21 | Merge Two Sorted Lists | 只有 `merge` 那一步。見 [4)](#4-merge-two-sorted-lists--lc-21) |
| 23 | Merge k Sorted Lists | 同樣的分治，只是對 `k` 條串列做。見 [5)](#5-merge-k-sorted-lists--lc-23) |
| 147 | Insertion Sort List | 上面那個 `O(n^2)` 的變體 |
| 109 | Convert Sorted List to BST | 重用同一套「從中間切開」的切分，然後建出一棵樹 |

---

<!-- 181e088cc367 -->
## 借用到串列上的陣列技巧

<!-- 259ae7351509 -->
### 15) 鏈結串列上的前綴和 + 雜湊表 — LC 1171 ⭐⭐⭐⭐


**模式**：把經典的陣列技巧「**前綴和相等 ⇒ 兩者之間那一段總和為 0**」移植到鏈結串列上。差別在於不是去數子陣列，而是**把 `next` 重接、直接跳過**那段總和為零的區段。

**關鍵想法**：在帶虛擬頭的串列上走兩趟。
1. 建立 `prefixSum -> 到達該前綴和的最後一個節點` 的對照表。
2. 再走一趟；在每個節點設 `cur.next = lastSeen[prefix].next`，這會刪掉該前綴和第一次與最後一次出現之間的所有東西。

從 `dummy`（值為 `0`）開始，正是讓「從 `head` 起算就總和為零」的前綴也能被刪掉的原因。

<!--CODE-->

<!--CODE-->

**視覺追蹤**（`1 -> 2 -> -3 -> 3 -> 1`）：
<!--CODE-->

---

<!-- 262bd5576c72 -->
### 16) 鏈結串列上的單調堆疊 — LC 1019 ⭐⭐⭐


**模式**：「下一個更大元素」需要**往回看**，而單向鏈結串列做不到。先把值實體化成陣列，再跑標準的**索引遞減單調堆疊** — 見 [monotonic_stack.md](./monotonic_stack.md)。

**關鍵想法**：堆疊裡放索引，不是值。當進來的值大過 `vals[stack.top]` 時，那個索引的答案就找到了 — pop 出來並記錄。最後仍留在堆疊上的，代表沒有更大的節點 ⇒ `0`。

<!--CODE-->

<!--CODE-->

**視覺追蹤**（`2 -> 7 -> 4 -> 3 -> 5`）：
<!--CODE-->

> 相關：**LC 2487 Remove Nodes From Linked List**（已列在 *Remove Elements* 模式底下）用的是同一套單調堆疊想法，只是拿來*刪除*節點，而不是回報節點。

---

<!-- 51fe32fe030d -->
## 相關題目 — 速查


> 快慢指標類技巧（環偵測、用間距找倒數第 n 個、切半 + 反轉判回文、換頭求交點、用 k 間距做旋轉）住在姊妹文件 [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — 這裡不重複。

| # | 題目 | 一句話想法 |
|---|---------|----------------|
| 142 | Linked List Cycle II | Floyd 環偵測，接著把其中一個指標拉回 `head` 重新走，找出入環節點 — 見 [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| 2130 | Maximum Twin Sum of a Linked List | 從中間切開 + 反轉後半（LC 234 那套回文機制），再兩兩配對 — 見 [7)](#7-palindrome-linked-list--lc-234) |
| 109 | Convert Sorted List to BST | LC 148 的「從中間切開」；中間節點成為 BST 的根，再對兩半遞迴 |
| 382 | Linked List Random Node | **蓄水池抽樣**：一趟走完，以 `1/i` 的機率保留第 `i` 個節點 — O(1) 空間，也不需要先知道長度 |
| 707 | Design Linked List | 虛擬頭 + 一個 `size` 計數器；每個操作都是「走到索引 `i-1`，然後接合」（見 [Dummy Head Technique](./linked_list.md#dummy-head-technique)） |
| 705 / 706 | Design HashSet / HashMap | **鏈結法（separate chaining）** — 一個桶陣列，每個桶是一條線性掃描的鏈結串列 |
| 622 | Design Circular Queue | 固定大小的環；鏈結串列版本就是把尾巴接回頭 |
| 1669 | Merge In Between Linked Lists | 純粹的接合：走到節點 `a-1` 與節點 `b+1`，把 `list2` 的頭尾勾在中間 |

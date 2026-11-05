# Cheatsheet 審查 — 2026 年 11 月：它們是面試教材嗎？

這份審查讀過 [`doc/cheatsheet/`](cheatsheet/) 底下全部 135 份 markdown（共 150,285 行），問一個前兩次審查都沒問過的問題：**這份 cheatsheet 能不能讓你在 FAANG coding 面試中更快？** 目標是 Google L3，Python 為主、Java 為輔。

> English version: [`cheatsheet-review-2026-11.md`](cheatsheet-review-2026-11.md) · 執行進度：[`cheatsheet-review-2026-11-progress.md`](cheatsheet-review-2026-11-progress.md)

| 審查 | 問的問題 | 現況 |
|---|---|---|
| [2026 年 8 月](cheatsheet-review-2026-08.md) | **格式**一致嗎？ | 維持住了：0 個未標語言的 code fence、0 個標題跳級、135/135 都有 Scope 行 |
| [拆檔計畫](cheatsheet-split-plan-2026-08.md) | 檔案是不是**太大**？ | Tier 1–3 完成；十份拆成二十四份，沒有超過 3,200 行的 |
| **本次** | 讀者能否在時間壓力下**認出 pattern、想起模板、避開陷阱**？ | 規則寫進本 PR 的 [`00_template.md`](cheatsheet/00_template.md) 與 [`CLAUDE.md`](../CLAUDE.md#cheatsheet-style-guide)；各 cheatsheet 的修改列在下方，這次先不動 |

審查方式：依分類分六路平行閱讀，同一套評分標準；另用一支統計腳本掃過每個檔案。[§ 正確性](#9-正確性--大家在背的模板裡有-bug) 中標為 **已驗證** 的 bug，都已親自重讀確認。

所有數字已於 2026-11-05 對 `master`（`6ad1564dd`）重新量過。初稿之後 master 又進了 84 個 commit，在六份 cheatsheet 裡加了約 500 行。各項發現的數字最多只變動 1，九個已驗證的 bug 也一個都還沒修。新加的段落（`binary_tree.md` §0-5、`time_space_complexity.md` 的 V + E vs V × E、`prefix_sum.md` Template 5）都是 Python 在前，每個區塊都先寫 `IDEA` 再寫 `time`。其中兩段已列進[現有的好範例](#好的樣子已經存在)。

---

## 結論

寫得最好的段落，比大部分公開的面試教材都好；寫得最差的，則是某人的解題資料夾直接貼在一個標題底下。**兩者的差別在形狀，不在知識。** 好的段落都用同樣的順序做同樣五件事，差的段落則跳過它們：

1. 說明**怎麼從題目敘述認出它**；
2. 在程式碼**之前**，用一行寫出**關鍵（key point）**：這題為什麼能這樣解，也就是程式碼每一步都維持成立的那件事（不變量）；
3. 給**一份**標準模板，先 Python 後 Java，附 `time = O(...)`；
4. 用最小的輸入 **dry run** 不直觀的那一步；
5. 點名**陷阱**，以及會觸發它的輸入。

整個 cheatsheet 沒有一個入口。手上拿著題目的讀者，找不到一頁能回答「我該打開哪一份？」。自稱負責這件事的 [`lc_pattern.md`](cheatsheet/lc_pattern.md)，內文連到的 cheatsheet 數量是 **零**。

確認了九個模板 bug，而且都在讀者會背下來的程式碼裡：`dp.md` 的第一個模板、`binary_tree.md` 裡標著「✅ correct」的片段、DFS 拓撲排序、Bellman-Ford 的 K 條邊模板。

---

## 量測結果

| 檢查項目 | 現在 | 說明 |
|---|---:|---|
| 檔案數 / 行數 | 135 / 150,285 | 其中 3 份是轉址 stub |
| Skeleton B / A / 混用 / 都不是 | 92 / 20 / 8 / 15 | 「混用」是模板明文禁止的 |
| 有**辨識**標題（signal / when to use / how to identify）的檔案 | **37 / 135** | 55 份 tier ≥ 4 的 cheatsheet 完全沒有 |
| 有 **pitfalls / traps** 標題的檔案 | 70 / 135 | |
| 有**決策 / 選擇**段落的檔案 | 61 / 135 | |
| ≥ 4 行的 Java/Python 區塊帶有 `time = O(...)` | 1,621 / 3,184 = **51%** | 8 月 35%、拆檔計畫時 47% |
| Python vs Java code fence | 1,824 vs 1,425 | 整體 Python 較多，但 *examples* 類檔案常是 Java 打頭（§5） |
| `### … LC n` 例題標題 | 1,330 | |
| 標題下**直接接程式碼**、沒有任何文字 | **401（30%）** | 最多的：`dfs_examples` 25、`heap_examples` 22、`design_examples` 14 |
| 標題後 14 行內沒有 insight / idea / trick 的 | **570（43%）** | |
| `NOTE !!!` 註解牆 | **69 個檔案共 583 行** | |
| `V0'` 這類沒說明理由的變體 | 23 個檔案共 68 處 | |
| IPython `In [n]:` 互動紀錄 | 10 個檔案共 224 行 | `python_trick` 119 行、`array` 13 個段落 |
| 標題中的 emoji | 24 個檔案共 195 個標題 | 全部 emoji 共 1,272 個 |
| `1-1)` / `1-1-3)` 這類難懂的編號 | 665 個標題 | |
| 提到 `gpt`（「fixed by gpt」、「comments created by gpt」） | 17 個檔案共 25 行 | |
| 部落格連結（csdn / cnblogs / bilibili / itdaan） | 19 個檔案共 30 行 | 有些在程式碼區塊裡 |
| Java 減法 comparator `(a, b) -> a - b` | 13 個檔案共 26 處 | `java_trick_strings_sorting` 自己就警告不要這樣寫 |
| 只寫一個 `pass` 的程式碼 | 10 個檔案共 25 行 | 證明草稿與「萬用模板」 |

各檔的「面試實用度」評分（1–5，閱讀後給分，不是算出來的）：

| 分數 | 檔案 |
|---|---|
| **5 — 照著學** | `sliding_window`、`prefix_sum_advanced`、`minimum_spanning_tree`、`dp_loop_order`、`stack_expression_parsing`、`patience_sorting`、`bit_manipulation`、`python_complexity` |
| **≤ 2 — 最需要整理** | `dfs_examples`（1）、`2_pointers_examples`、`2_pointers_linkedlist`、`n_sum`、`add_x_sum`、`array_examples`、`string_examples`、`dp_examples`、`graph_examples`、`Bellman-Ford`、`Floyd-Warshall`、`topology_sorting`、`topology_sorting_examples`、`hashing`、`queue`、`set`、`stack_examples`、`advanced_divide_and_conquer`、`binary_search_examples`、`sort`、`math`、`lc_pattern`、`python_trick`、`design_examples`、`streaming_algorithms`、`advanced_simulation`、`array_overlap_explaination` |

低分的大多是 `_examples` 附屬檔：拆檔計畫把長尾的題目搬了進去，之後就沒人整理過。

---

## 發現

### 1. 沒有入口

面試者的第一個問題是「*這是哪一類題？*」，整套 cheatsheet 沒有一處能從頭到尾回答它：

- [`lc_pattern.md`](cheatsheet/lc_pattern.md)（tier 5）的方向是 pattern → 題目，從來不是 signal → pattern。內文連到的 cheatsheet 是**零**份。Union-Find、Topological Sort、Greedy 各出現兩次（`:225`/`:571`、`:234`/`:594`、`:85`/`:549`）。還有歸錯類的題目：LC 1 放在「Two Sum (sorted)」、LC 424 放在「Fixed Size Window」、LC 279 放在篩法底下。
- [`lc_category.md`](cheatsheet/lc_category.md) 對應的是外部分類法，不是題目訊號。
- **約束尺規**（n ≤ 20 → 2ⁿ、n ≤ 10⁵ → n log n）有兩份，分別在 `complexity_cheatsheet.md:668` 和 `time_space_complexity.md:30`，但沒有任何地方連過去。
- 真正做到 signal → pattern 的表格品質都很好，但散在各處、各管各的：`dfs.md:31`、`graph.md:617`、`Collection.md:288`、`shortest_path_comparison.md:149`、`sliding_window.md:767`、`greedy.md:457`。

### 2. 辨識是缺掉的那一層

只有 37 個檔案有辨識相關的標題。Arrays & Strings / Linked List 的 24 份裡，**沒有一份**用到約束提示；整個 cheatsheet 只有 `dp_bitmask`（n ≤ 20）和 `Floyd-Warshall`（n ≤ 400）用到。

就算有辨識，也常只是重複標題：`tree2.md` 的 *Use Case* 欄，在 Maximum Path Sum 模板底下寫著「Maximum path sum problems」。

價值最高的形式是**反辨識**：「看起來像 X，其實不是」。整套只出現一次（`difference_array.md:972`），而那正是全套最好的表格之一。

### 3. 例題是存檔，不是教學

43% 的 LC 例題標題沒有 key insight，30% 直接進程式碼。`_examples` 檔案寫到一半會換風格：後半段較新，照 *Core idea → Trace → Py/Java → Pitfalls → Similar* 寫；前半段較舊，是貼進來的解題檔：

- 程式碼裡的 `NOTE !!!` 註解牆：`stack_examples` 44、`2_pointers_examples` 26、`backtrack_examples` 26、`string` 17、`tree_examples` 15、`tree_lca_distance` 15、`heap_examples` 14。
- **V0 有時是最差的解**，只是因為最早寫而留著。例如：
  - LC 39（`backtrack_examples:183`）是暴力加去重；
  - LC 62（`dp_examples:154`）用 BFS 解；
  - LC 146（`design_examples:31`）是 O(n) 的 list 版 LRU，hash map + 雙向鏈結串列的正解根本沒有；
  - LC 15（`n_sum:514`）用 `tmp not in res` 線性掃描。
- 寫作過程的殘留物：「fixed by gpt」、部落格網址、註解掉的 `print`，還有註解和下一行程式碼說法相反的 `### NOTE`（`dfs_examples:761`）。

### 4. 一題多處

拆檔計畫解決的是單一檔案*內部*的大小問題，跨檔案的歸屬仍然沒解決：

| 題目 / 主題 | 在哪些檔案被完整解過 | 檔案數 |
|---|---|---:|
| LC 200 Number of Islands | bfs、dfs、graph、union_find 各家族 | 6 |
| 0/1 背包 | `dp` T4、`dp_pattern` §6、`dp_advanced`、`knapsack`、`knapsack_01_zh`、`dp_loop_order` | 6 |
| LC 309 含冷凍期的股票 | `dp`、`dp_pattern`、`dp_advanced`、`dp_examples`、`stock_trading` | 5 |
| 最短路徑比較表 | `Dijkstra`、`Bellman-Ford`、`Floyd-Warshall`、`shortest_path_comparison`，而且各份已經不一致 | 4 |
| LC 300 LIS | `binary_search`、`binary_search_examples`、`patience_sorting`、`sort` | 4 |
| LC 113 / 437 樹路徑 | `tree2`、`tree_backtrack`、`binary_tree`、`bst_examples`（約 600 行和 BST 無關的路徑題） | 4 |
| LC 743 | `Dijkstra.md` 3 次、`Dijkstra_examples` 1 次 | 4 |
| LC 215 quickselect | `2_pointers_quickselect`、`advanced_divide_and_conquer`、`sort`、`heap` | 4 |
| `monotonic_stack` ↔ `stack_examples` | 標題共用 10 個 LC 題號（32、84、402、496、503、735、739、901、907、2104） | 2 |
| KMP / rolling hash | `string_matching_kmp_rolling_hash` ↔ `advanced_string_algorithms`（LC 28、459、686、1044、796） | 2 |
| 樹模板 | `tree2` 說自己是「唯一的家」，`binary_tree` 卻還留著 Template 1–7 | 2 |

Scope 行規定了誰擁有哪個*主題*，卻沒有規定誰擁有哪*一題*。所以找 LC 113 的讀者可能落在四個地方之一，那四處的寫法可能不同，其中兩處甚至可能是錯的。

### 5. 在最關鍵的地方沒做到 Python 優先

整體 Python 較多（1,824 vs 1,425 個 fence），但講模板和進階主題的檔案常常 Java 打頭：

- `dp_monotonic_stack` Python 0 vs Java 7；`dp_string` 1 vs 12；`knapsack` 3 vs 13；`dp_bitmask` 3 vs 10。
- `binary_search_on_answer` Python 11 vs Java 17，每個例題都 Java 在前。
- `greedy_examples` 15 個段落中 13 個 Java 在前；`combinatorics_math_patterns` 21 個中 17 個。
- `sliding_window_advanced` 有六個「Template (Java)」標題。
- `Bellman-Ford` 和 `Floyd-Warshall` 完全沒有 Java；`n_sum` 的一般形式甚至是 **C++**。

### 6. 雜訊降低每頁能吸收的內容

- **Emoji**：195 個標題有 emoji（`dp_advanced` 32、`knapsack` 21；`Dijkstra` 全檔 52、`java_trick` 61）。`lc_pattern` 每個 H2 前面都有 🔹。網站已經把 `⭐` 渲染成徽章，其他 emoji 只會跟它搶注意力。
- **`1-1-2-1)` 這類編號**（665 個標題）：講話時沒辦法唸，段落一搬就壞。例如 `bst_advanced` 的模板順序是 5b、8、9、6b、3c、4b；`bfs.md` 還得在 `:138` 自己解釋編號為什麼跳號。
- **把 REPL 紀錄當模板**：tier 5 的 `array.md` 在 `:53-112` 用 `In [28]: p.insert(1, [6,1])` 開場。
- **空泛的填充內容**：
  - 「Common Mistakes：忘記正確處理視窗狀態」這類通用提醒；
  - 程式碼只有 `pass` 的證明（`greedy:493-554`）；
  - 用未定義名稱寫成的「萬用模板」虛擬碼（`binary_tree:453`、`tree:417`、`heap:192`）；
  - 編造的統計（「Template Usage Frequency: 15 problems」、`Dijkstra.md:775` 的毫秒 benchmark）；
  - 一張 45 行、結尾是「Design custom solution」的流程圖（`palindrome:508`）。
- **貼上的對話紀錄**：`tree_backtrack.md` 的 H1 是「Template (gpt)」，第 6 行以「Great — many…」開頭。
- **英文 cheatsheet 裡夾中文**：共 18 個檔案，大多只有一兩行，主要集中在四個檔案：`dp_advanced` 34 行、`difference_array` 22 行、`dp_loop_order` 16 行、`backtrack` 10 行。`dp_advanced:882-940` 的「中文速記」幾乎照抄 `knapsack_01_zh.md`；`dp_loop_order` §6 把自己的英文內容用中文再講一遍。100% 的中文翻譯檢查完全看不到這些。（`knapsack_01_zh.md` 本身沒有問題：它是一份有自己 Scope line 的 0/1 背包中文詳解，不是 `knapsack.md` 的副本。）

### 7. 有些地方的教學順序是反的

- `binary_search.md` 的標準模板在 **1,604 行中的第 1,194 行**。
- `stock_trading.md` 最好的段落（§7，每道股票題都能套進的那一條遞迴式）放在**最後**，前面是它其實已經讓人用不到的逐題程式碼。
- `linked_list.md` 先講 LRU/LFU（`:597`），後講反轉串列（`:1362`）。
- `monotonic_stack`（`:686`）、`queue`（`:1197`）、`sort`（`:1411`）、`intervals`（`:660`）仍把 `LC Examples` 放在 `Summary` **之後**，正是 8 月列出的反模式；`dp_pattern:1941`、`kadane:937`、`recursion_to_dp:636`、`dp_advanced:2906` 也一樣。
- `monotonic_stack` 前約 680 行都是通論，真正的內容從 §2-2 才開始。

### 8. 沒有練習路線

沒有一份 cheatsheet 同時做到「題目由易到難排序」**和**「標出必會題」。「Problems by Pattern」表雖然有難度，但沒有排序；星號標的是段落，不是題目。

repo 其實已經有答案：[`data/l3_core.json`](../data/l3_core.json) 是 L3 核心題組，[`data/progress.txt`](../data/progress.txt) 記著每題的結果。只是 cheatsheet 從來沒有顯示它們。

### 9. 正確性 — 大家在背的模板裡有 bug

**已驗證**：本次審查重讀過程式碼，問題明確。其餘由閱讀者回報並附 file:line，修正時必須重新確認。

| 位置 | 問題 | |
|---|---|---|
| `dp.md:151-170` | Template 1「1-D Linear DP — LC 53」寫成 `dp[i] = max(dp[i-1], nums[i])`，回傳的是**最大元素**，不是最大子陣列和。而這是 tier 5 DP cheatsheet 的第一個模板。 | **已驗證** |
| `binary_tree.md:194-203` | 標著「✅ correct」的 reset-don't-stop 片段只在 `else` 裡遞迴：連續段延續時不會遞迴，右子樹從不走訪，最大值也從不更新。 | **已驗證** |
| `topology_sorting.md:263-275` | 先 `stack.insert(0, v)` 再 `return stack[::-1]`，反轉兩次，結果是**反向**的拓撲序；「append 是錯的」那句註解剛好說反了。 | **已驗證** |
| `Bellman-Ford.md:176-180` | K 條邊模板在 `dist[dst]` 一輪沒變時就提前停止；但 `dst` 可能在之後某輪才變小，提前停止可能回傳偏高的成本。 | **已驗證** |
| `2_pointers_linkedlist.md:53-79` | `detectCycle` 標成 LC 141（實為 142），而且無環檢查被註解掉；串列無環時，第二個迴圈會 NPE。 | **已驗證** |
| `greedy.md:80` | 「Template 2 — LC 621」取任務時完全沒有冷卻時間 `n`，回傳的是 list，並沒有解出 LC 621。 | **已驗證** |
| `binary_search_examples.md:223, 299` | `mid = (left + right) / 2` 在 Python 3 是浮點除法：LC 209 會 `TypeError`，LC 367 會答錯。 | **已驗證** |
| `stack.md:336-354` | next-greater 模板片段用到未定義的 `tmp`，`res[tmp[_idx]]` 應為 `res[_idx]`；Java 註解寫從右到左，迴圈卻是從左到右。 | **已驗證** |
| `Collection.md:327` | 說「`Counter` … 讀取時會建立 key」是錯的：`Counter.__missing__` 回傳 0 但不會插入，只有 `defaultdict` 會。 | **已驗證** |
| `heap.md:575` | 「Template 1 — LC 215」回傳整個 heap，不是第 k 大；它的 Java 解的是 LC 347。 | 回報 |
| `add_x_sum.md:203` | LC 371 的「V0」是 `log(exp(a) * exp(b))`：超過約 709 就溢位，還有浮點誤差。 | 回報 |
| `design_examples.md:31-77` | 標準 LRU 每次操作 O(n)；LC 146 要求 O(1)。 | 回報 |
| `binary_search_on_answer.md:52, 164, 1428` | 「求最大值時 `left = mid + 1`」和同檔 `left = mid` 搭配取上中位的程式碼互相矛盾。 | 回報 |
| `advanced_divide_and_conquer.md:56` | 「LC 315」回傳的是逆序對總數；LC 315 要的是每個位置各自的計數。 | 回報 |
| `Floyd-Warshall.md:478` | 對 LC 1627（n ≤ 10⁴）用 O(n³)，和自己的規模表矛盾。 | 回報 |
| `backtrack.md:90, 247` | Subsets 的提示用了排列的寫法；LC 377 被歸為傳 `i`（可重用起點）的類型。 | 回報 |
| `tree.md:958` | 「Re-rooting DP」底下其實是 LC 310 的剝葉子解法。 | 回報 |
| `2_pointers_quickselect.md:46` | ⭐5 模板用固定的 Lomuto pivot，遇到 LC 215 全部相等的測資會退化成 O(n²)。 | 回報 |
| `lc_pattern.md:49, 55, 60, 279, 552, 554, 621` | 題目歸錯 pattern。 | 回報 |
| 26 個 Java comparator | `intervals`、`scanning_line`、`scanning_line_examples`、`greedy_examples` 等處的 `a - b` 減法比較。 | 回報 |

---

## 好的樣子已經存在

以下段落就是範本。新模板是從它們歸納出來的，不是憑空設計。

| 段落 | 為什麼好 |
|---|---|
| [`minimum_spanning_tree.md`](cheatsheet/minimum_spanning_tree.md) `:57-70`、`:166-194`、`:307-327` | 在程式碼**之前**解釋為什麼貪婪是安全的。同一張圖用狀態表分別走過 Kruskal 和 Prim（「same tree, different order」）。「Kruskal or Prim?」依**你拿到的輸入**來選。第二段程式碼寫明了存在的理由。 |
| [`dp.md`](cheatsheet/dp.md) `:383-540`（Template 1b、LC 139） | 程式碼前先給 pattern / state / base / transition / answer / complexity 表，接著是一行 insight、dp trace、附複雜度的 Python + Java、「Three traps」，以及誠實的複雜度比較表（n² vs n³ vs n·L²）。 |
| [`dp_loop_order.md`](cheatsheet/dp_loop_order.md) `:16-190` | 教的是*判斷力*：一條規則、一張失敗表（什麼壞了 → 症狀 → 代表題）、三步驟決策流程、十秒鐘的 shuffle 測試。 |
| [`prefix_sum_advanced.md`](cheatsheet/prefix_sum_advanced.md) `:489-690` | 把新 pattern 對應到已知的 pattern，用三個條件說明「何時使用」，再給出說明 undo 步驟為何存在的**最小失敗案例**，最後寫「何時*不要*用」。 |
| [`tree_lca_distance.md`](cheatsheet/tree_lca_distance.md) `:95-110` | 家族表：每個變體一列，寫出*和基準相比改了什麼*、適用的模板，最後是「決定用哪個模板的那一個問題」。 |
| [`binary_tree.md`](cheatsheet/binary_tree.md) `:221-235`（§0-5，depth vs height） | 同樣的家族表，往下一層：depth、height、distance 各一列，寫出*誰先知道*、*往哪個方向流*，讓「用參數還是用回傳值」只需要決定一次。單位（邊數 vs 節點數）在程式碼之前就講清楚。 |
| [`time_space_complexity.md`](cheatsheet/time_space_complexity.md) `:423`（V + E vs V × E） | 先講一條規則，再給 *看到什麼 → 時間 → 空間 → 例題* 的表。最後一列是「這是 bug，不是演算法」，正是 §2 說幾乎各處都缺的反辨識。 |
| [`stack_expression_parsing.md`](cheatsheet/stack_expression_parsing.md) `:12-60`、`:618-628` | 把整個題型拆成 3 個子問題，逐字元追蹤 `"3+2*2"`，最後是 Trap → Fix 表。 |
| [`patience_sorting.md`](cheatsheet/patience_sorting.md) `:25-100` | 短篇 cheatsheet 的範本：三條規則 → trace → 兩行證明 → 「何時適用」→ 一行判別。 |
| [`difference_array.md`](cheatsheet/difference_array.md) `:972-979` | 唯一的**反辨識**表：「看起來像差分陣列，其實不是 — 判別點」。 |
| [`Collection.md`](cheatsheet/Collection.md) `:288-313` | 題目中的訊號 → 該用什麼 → 為什麼 → LC。這就是大多數 cheatsheet 缺的辨識層。 |
| [`scanning_line.md`](cheatsheet/scanning_line.md) tie-break 段落 | 把掃描線最常見的 bug 變成只需決定一次的事。 |

---

## 構想

六個構想，每一個都是在改變 *cheatsheet 的寫法*，而不是一次性的修補。

1. **每個模板都是一張 *pattern card*。** 標題寫出**模式名稱**和**代表題**：`### Template 2: Variable-Size Window — LC 3`。模式比整份 cheatsheet 的主題小一層，所以 `hash_map.md` 裡的卡片叫 *Complement Lookup — LC 1*（找補數），不會叫「Hash Table」。標題之後的欄位順序固定：**辨識（Recognise）→ 關鍵（Key point）→ 模板（Template）→ 複雜度（Complexity）→ *Dry run* → 陷阱（Traps）→ *不是這個（Not this if）* → 練習（Drill）**。**關鍵**寫在程式碼之前，說明這題為什麼能這樣解，也就是程式碼每一步都維持成立的那件事。**複雜度**指出是哪一行決定了 time 和 space，這正是面試時要講出口的話。有不直觀的步驟時一定要寫 *Dry run*。有相似題時一定要寫*不是這個*，它放在陷阱後面，因為是給已經會這個模式的讀者看的。選填欄位只有在內容為空時才能省略，其餘欄位的順序不變。讀者記住順序之後，在任何一份 cheatsheet 都能直接找到「陷阱是什麼？」，不必從頭讀。上面每個範本其實都已經具備大部分的形狀。
2. **每個例題都是一張 *problem card*。** 開頭是一行 `Key point`（關鍵）引言，加上一個「Reduces to」連到它所屬的 card；接著是一份附 `time =` 的解法；需要時再加 dry run 和陷阱。帶著 `NOTE !!!` 的原始程式碼是解題檔，而解題檔已經放在 `leetcode_python/` 和 `leetcode_java/`。cheatsheet 應該連過去，而不是再貼一份。
3. **一題一個家。** 一道完整解過的 LC 題只放在一份 cheatsheet，其他地方的提及都只是一列指標。主題已經有這條規則（Scope 行），題目也需要。
4. **一個入口。** 把 `lc_pattern.md` 重寫成「訊號 → pattern → cheatsheet → card」，附約束尺規和一欄反辨識，每一列都連到一張 card。
5. **把資料裡已有的練習路線秀出來。** L3 核心題組和練習紀錄的結果都已經存在。cheatsheet 的練習清單應該從它們產生，排好序並標出必會題，而不是手打、然後慢慢過時。
6. **三種檔案角色，各有一種骨架。** 一個主題的**核心**檔（`X.md`）負責概念和所有模板；**例題集**（`X_examples.md`）放經典題，按核心檔的模板分組、順序相同；**進階**檔（`X_advanced.md`）放核心模板做不到的技巧，並說明什麼時候不必用。拆檔計畫產生了 27 份例題集和 9 份進階檔，卻從沒規定它們長什麼樣子，所以 §3 提到的存檔大多是 `_examples` 檔。`00_template.md` 的 Skeleton C 和 D 就是給它們的骨架；A 和 B 仍用於核心檔。

---

## 改進提案

依**每小時的價值**排序。每一項都可以獨立審查，就像拆檔計畫的各個 tier 一樣。

### P0 — 修正已確認的 bug（一個小 PR）

修正 [§9](#9-正確性--大家在背的模板裡有-bug) 中九個**已驗證**的項目，再重新確認並修正**回報**的項目。每段改過的程式碼都要用題目自己的範例做冒煙測試，做法同 `/lc-cheatsheet` 的第 6 步。另外把 26 個減法 comparator 全面改成 `Integer.compare`，就像 8 月處理 `heap.md` 那樣。

*規模：約 25 段程式碼。每個改到的段落也要重新對應它的中文 overlay（cheatsheet 的中文翻譯有 100% 檢查）。*

### P1 — 入口

把 [`lc_pattern.md`](cheatsheet/lc_pattern.md) 重寫成路由表：

| 題目敘述中的訊號 | 約束提示 | Pattern | Card | 看起來像，其實不是 |
|---|---|---|---|---|
| 「最長 / 最短的 contiguous subarray」、「at most k distinct」 | n ≤ 10⁵ | 可變長度滑動視窗 | [sliding_window § Template 2](cheatsheet/sliding_window.md#template-2-grow-then-shrink--the-while-invariant-) | 條件是精確的總和 `sum == k`，且數值可能為負 → prefix sum + hash map（LC 560） |

- 用連結把約束尺規接進來。兩份尺規合併到 `complexity_cheatsheet.md`，`time_space_complexity.md` 改成指標。
- 修正歸錯類的題目，刪掉重複的段落。
- 在 `cheatsheet_meta.json` 的 `startHere` 中把它設為第一階。

### P2 — 為 24 份 tier 5 cheatsheet 套上 pattern card

第一批是 L3 面試最倚重的 14 份：`binary_search`、`sliding_window`、`2_pointers`、`hash_map`、`tree`、`binary_tree`、`bfs`、`dfs`、`graph`、`heap`、`dp`、`backtrack`、`stack`、`linked_list`。其餘 10 份放在第二批；其中兩份由別的項目處理（`lc_pattern` 由 P1 重寫，`knapsack_01_zh` 在 P3 當 0/1 背包的中文本家）。每一份要做的事：

- 把每個模板改成完整的 pattern card：標題寫出模式名稱和代表題，欄位照**辨識 → 關鍵 → 模板 → 複雜度 → Dry run → 陷阱 → 不是這個 → 練習**的順序排。缺的補上；`NOTE !!!` 改寫成**陷阱**條目；
- 把標準模板移到前約 300 行內（`binary_search`）；
- 把放在最後的精華移到前面（`stock_trading` §7、`monotonic_stack` §2-2 以後）；
- 加上 `## Drill Path`（見 P5）；
- 刪掉「萬用模板」佔位和 `pass` 證明：要嘛改成能跑的程式碼，要嘛拿掉。

*指標：有辨識標題的檔案從 37 份增加到涵蓋所有 tier ≥ 4 主檔；這 24 份裡，每個模板都有**關鍵**和**複雜度**。*

### P3 — 一題一個家

依 [§4](#4-一題多處) 的熱點表，像拆檔計畫一樣一個家族一個家族處理：

1. 最短路徑比較 → 由 `shortest_path_comparison.md` 擁有，其他三份演算法 cheatsheet 只留指標。
2. 0/1 背包 → 英文由 `knapsack.md` 擁有。`knapsack_01_zh.md` 保留為獨立的中文詳解：它不是 `knapsack.md` 的翻譯，而 `knapsack.md` 已經有自己的 overlay，所以它不能改成 overlay。`dp_advanced` 裡幾乎照抄它的「中文速記」改成一行連結。
3. 樹路徑 → LC 112/113/124/437 由 `tree_lca_distance §4`（root-to-leaf 路徑模板）擁有；`bst_examples` 的 Template 7 移出（那不是 BST 內容）。LC 1110 也出現在 4 個檔案，但它是刪除節點、回傳森林，屬於修改樹的題目而不是路徑題，所以它的家要和修改樹的內容一起決定，不放這裡。
4. 單調題由 `monotonic_stack` 擁有，`stack_examples` 留其餘的。
5. KMP / rolling hash → 只留一個擁有者。
6. Quickselect → 由 `2_pointers_quickselect` 擁有（改名為 `quickselect.md`，舊檔留 stub），並把三路切分版設為 ⭐5 模板。

只是*相似*的區塊絕對不刪：要先確認是同一題、同一語言、同一解法（沿用拆檔計畫的規則）。

### P4 — 用 Skeleton C 和 D 重建附屬檔

先處理 13 份評分 ≤ 3 的 `_examples` 檔，再處理其他的；每份都和它核心檔的 P2 那一組放在同一個 PR。每份例題集（Skeleton C）：

- `##` 段落改成跟核心檔的模板一一對應、順序相同，最上面加 `## Index` 總表；
- 例題集裡的模板移回核心檔（`bst_examples` 有一個）；找不到對應模板的題目，先在核心檔補一張 pattern card。不設 `## Other Examples`；

每個例題：

- 補上 `> **Key point** —` 那一行，以及 `**Reduces to**: [Template N](#…)`；
- **V0 必須是標準解**，而不是最早寫的那份。暴力解的 V0 要降級或刪除；第二種解法要寫明理由才能留；
- 把 `NOTE !!!` 註解牆濃縮成一兩條文字；刪掉「gpt」、部落格網址、註解掉的 `print`；
- 改為連到 `leetcode_python/` 裡的完整解題檔，不再貼第二份。

每份進階檔（Skeleton D）：加上連到核心卡片的 `### Prerequisites`，以及 `## When Not to Use` 段落。9 份裡沒有一份有前者，只有 `prefix_sum_advanced` 有後者。

*指標：直接接程式碼的例題 401 → 0；沒有 insight 的 570 → < 100；`NOTE !!!` 583 → < 50；每份 `_examples` 都有 `## Index`，每份 `_advanced` 都有 `## When Not to Use`。*

### P5 — 從資料產生練習路線

每份主檔加一個 `## Drill Path`：**Core**（在 [`l3_core.json`](../data/l3_core.json) 裡的題目，加 ⭐）→ **Practice** → **Stretch**，每段內由易到難排序。第一階段根據核心題組手寫。

第二階段（構想，不在本 PR）：讓 `build-site.js` 在 Drill Path 的每個 LC 題號旁，顯示 `progress.txt` 裡的結果（`ok` / `again` / 未做），做法就像 `build-roadmap.js` 已經對 roadmap 做的那樣。

### P6 — 雜訊與語言

- 每一組程式碼都先 Python 後 Java。替只有 Java 的模板檔（`dp_monotonic_stack`、`dp_string`、`knapsack`、`binary_search_on_answer`）補 Python，並換掉 `n_sum` 的 C++。（`Bellman-Ford` / `Floyd-Warshall` 補 Java 放到最後：Java 是輔助語言。）
- IPython 互動紀錄改成一般的 ` ```python `，輸出寫成 `# -> …` 註解。
- 重寫 `tree_backtrack.md` 像對話紀錄的開頭；修正 `array_overlap_explaination` 的檔名拼字（舊名留 stub）。
- 標題的 emoji 在 P2 改寫該份檔案時一起拿掉，不另外做全面清理。拿掉 emoji 會改掉錨點（`🔥 Template 1` 的 id 是 `-template-1-…`），所以同一個 commit 要修好所有連到它的連結，並重新對應中文 overlay。✅ / ❌ 只在表格儲存格中使用。
- 英文檔裡的中文：英文已經講過同樣內容的（如 `dp_loop_order` §6），刪掉中文段落，因為中文頁本來就會顯示該段的翻譯；英文沒有的內容，先寫成英文段落，中文放進 `i18n/zh/` overlay。overlay 只能翻譯英文檔已有的段落，所以中文不能直接搬過去。
- **不做：** 改掉 `n-n)` 編號。它會改錨點（cheatsheet 之間有 15 個連結指向 `#1-1-…`，檔案內更多），而且只改新段落的話，同一份檔案會混用兩種風格。已有的檔案維持原本的風格，新檔案用 `Template 1…N`。也不把 `knapsack_01_zh.md` 改成 overlay（見 P3）。

### P7 — 只能變好、不能變差的 lint

新增 `script/check_cheatsheets.py`，作為 `check_readme.py` 的兄弟，採用同樣的 **baseline 棘輪**：今天的數字當 baseline，新增就失敗，修正後執行 `--update-baseline`。由 `check-readme.yml` 或新的 workflow 執行。每條規則都對應上面的一個發現：

| 規則 | 對應發現 |
|---|---|
| 不得出現 `NOTE !!!`、`In [n]:`、`gpt`、csdn/cnblogs/bilibili 連結 | §3、§6 |
| 標題中除了結尾的 `⭐` 之外不得有 emoji | §6 |
| `### … LC n` 標題後，第一個 code fence 之前必須有文字 | §3 |
| 不得出現 Java `(a, b) -> a - b` comparator | §9 |
| 不得把 Python `(l + r) / 2` 當索引使用 | §9 |
| `## LC Examples` 不得在 `## Summary` 之後 | §7 |
| tier ≥ 4 的主檔要有辨識段落和 `## Drill Path` | §2、§8 |
| `_examples` 要有 `## Index`、不得有 `Template N` 標題；`_advanced` 要有 `## When Not to Use` | §3、Skeleton C / D |

需要判斷的規則（一題一個家、V0 是標準解）留在 code review，不做成 lint。

---

## 建議順序

| 步驟 | 項目 | 為什麼現在做 |
|---|---|---|
| 1 | **本 PR**：模板、風格指南、這份審查 | 之後每個 PR 都以它們為審查依據 |
| 2 | P0 bug | 錯的模板今天就有人在背 |
| 3 | P7 lint + baseline | 在 P2–P6 把數字降下來的同時，防止它們繼續變多 |
| 4 | P1 入口 | 只改一個檔案，每行的價值最高 |
| 5 | P2 tier 5 card，一個家族一個 PR | tree → graph → arrays/strings → dp → hashing/stacks，沿用拆檔計畫的順序 |
| 6 | P3 + P4 依家族一起做 | 去重和整理會動到同樣的段落 |
| 7 | P5 第二階段、P6 | 收尾打磨 |

## 不提議的事

- **核心檔不新增骨架。** 核心檔仍用 A 和 B。C 和 D 只用於拆檔計畫已經產生的 `_examples` 和 `_advanced` 附屬檔；pattern card 則是放在任何一種骨架的模板段落*裡面*。
- **不在一個 PR 裡重寫整份 cheatsheet。** 每次修改 cheatsheet 都要重新對應中文 overlay，而那套系統就是為段落大小的 diff 設計的。
- **只是相似的變體不刪**（沿用拆檔計畫的規則）。
- **cheatsheet 裡不手打數量**（`linked_list.md:1767` 的「Seventeen problems」早就錯了）。要嘛從資料算，要嘛不寫。

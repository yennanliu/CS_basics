# 專案審查 — 2026 年 9 月

> **範圍** — 這個 repo 能不能讓一位候選人更快通過 FAANG SWE 的 coding loop？
> 一個問題，沿五個軸線提問：LeetCode 覆蓋率、解題程式碼、文件、網站，以及專案前進的方向。
> 先前的幾份審查問的是更窄的問題 —
> [`site-review-2026-08-31.md`](./site-review-2026-08-31.md) 問建出來的網站是否*正確*，
> [`cheatsheet-review-2026-08.md`](./cheatsheet-review-2026-08.md) 問 cheatsheet 是否*有條理*，
> PR #155 的草稿問一份 cheatsheet 是否教會人推導。這一份問的是：整件事是否對準了目標。
> **另見**：[`lc-readiness-guide.md`](./lc-readiness-guide.md) — 本審查借用其標準的個人 readiness 評分。
> **English version**: [`project-review-2026-09.md`](./project-review-2026-09.md) — 本文是它的繁體中文版，數字與結論相同。

審查基準為 `953902764`（master，2026-09-23），對照的線上網站為
<https://yennj12.js.org/CS_basics/>。以下每一個數字都是用[附錄](#附錄--數字是怎麼算出來的)裡的指令從
tree 裡算出來的；沒有一個是憑記憶寫的。全文的標準是這個專案早已為自己定下的那一個：
**Google L3 coding loop** — 35–45 分鐘內乾淨地解出一題 medium、說出複雜度、處理好邊界、全程出聲 —
不是 L5，也不是 system design 那一輪。

## 目錄

- [結論](#結論)
- [量了什麼](#量了什麼)
- [1. LC 覆蓋率 — 廣度已經完成；缺口是深度形的](#1-lc-覆蓋率--廣度已經完成缺口是深度形的)
- [2. 解題程式碼 — 是嘗試的存檔，不是一組範本解答](#2-解題程式碼--是嘗試的存檔不是一組範本解答)
- [3. 文件 — 形狀對了，入口不對](#3-文件--形狀對了入口不對)
- [4. 網站 — 工具很強，缺一個練習迴圈](#4-網站--工具很強缺一個練習迴圈)
- [5. 方向 — repo 為累積而最佳化；目標需要的是轉換](#5-方向--repo-為累積而最佳化目標需要的是轉換)
- [優先行動清單](#優先行動清單)
- [本審查未涵蓋的部分](#本審查未涵蓋的部分)
- [附錄 — 數字是怎麼算出來的](#附錄--數字是怎麼算出來的)

---

## 結論

**當作參考資料，這是 A。當作備考教材，這是 B−，而兩個分數出自同一個原因。**

這個 repo 已經做完了大多數面試 repo 永遠做不完的那一段：Blind 75、NeetCode 150、NeetCode 250 和
LeetCode Top 100 Liked 上的每一題都已經編入索引、解出、連好連結（3,270 題，2,897 個 Python 與
1,210 個 Java 解法），有 135 份總計 145,000 行的 cheatsheet、36 個視覺化 demo、一張依相依順序排列的
roadmap、208 題的複雜度測驗、一個由六年練習紀錄驅動的間隔重複規劃器，還有一條會走遍每一個產生頁面、
遇到壞連結就失敗的 CI pipeline。網站自己的測試有 418 個，全綠。這些東西幾乎都不需要再多。

這套教材還沒做到的，是**轉換**。repo 自己的紀錄就這麼說：

- 在 1,266 個有追蹤狀態的題目中，**997 個是 `AGAIN`，269 個是 `OK`**。
  在面試官真正會抽題的清單上，比例更差 — Blind 75 是 **15 OK / 60 AGAIN**，NeetCode 150 是
  24 / 125，Top 100 Liked 是 16 / 84。150 個有追蹤的 Hard 題裡，**6** 個是 OK。
- **125 個題目在記錄了十二次以上之後仍然掛著 `AGAIN`。** 這個標記只會累積、永不畢業，所以它是地板而不是
  量測 — 但地板低到這個程度，仍然說明從*解出*到*穩固*的迴圈沒有閉合。
- 通過次數曲線最陡的地方，正是 coding round 所在之處：Recursion 平均每題 **7.4** 次，BST 7.0，
  Stack 與 Backtracking 6.3，BFS 6.2，Tree 6.1 — 對比 Math 1.1、SQL 1.6、Bit Manipulation 1.8。
- 驗證幾乎不存在於產出物中：**2,897 個 Python 檔案裡只有 73 個**會執行任何東西，Java **沒有測試目錄**，
  而八月的 readiness 評估發現 31–44% 的提交被拒率，且 Easy *高於* Hard。

所以最有價值的工作已不再是新增 — 而是閉合迴圈：在一組固定的 75 題上、以明確的標準把 `AGAIN` 轉成 `OK`；
給一個其他工具都有的網站加上限時練習模式；在目前一開頭就給答案的 cheatsheet 裡教推導
（暴力解 → 觀察 → 模板）；並清理解題存檔，讓讀者能在平均三個版本裡找到那一個正典解答。

| 軸線 | 分數 | 最能推動它的一件事 |
|---|---|---|
| LC 覆蓋率 | **A**（廣度）/ **C**（掌握度） | 以四個問題的標準，把 `/lc-again` 當成一場戰役跑過 Blind 75 的 60 個 `AGAIN` |
| 解題程式碼 | **B−** | 每個檔案標出一個正典解法；清掉 AI 署名與重複 javadoc 的殘留；把 docstring 的範例輸出成可執行的檢查 |
| 文件 | **B** | 每份 tier-5 sheet 頂端放一頁「在面試間裡」區塊；給 `doc/` 一份說明哪份計畫是現行版的索引 |
| 網站 | **B+** | 一個接上 random picker 與練習紀錄的限時練習模式 |
| 方向 | **B** | 停止新增題目；出一份命名為「L3 core」的清單，每月量它的 OK 佔比 |

---

## 量了什麼

| | 數量 | 來源 |
|---|---|---|
| 索引題數（不重複 LC id） | 3,270 — 800 Easy · 1,715 Medium · 747 Hard · 8 未知 | `README.md`，經 `parseReadmeProblems` |
| README 列數 | 3,291 — 1,310 在主 `##` 表格，1,981 在 `## Newly Added (kamyu104 gap)` 之下；21 個 id 兩邊都有 | `README.md` |
| 有追蹤狀態的列 | 1,266 — 997 `AGAIN`，269 `OK`（全在主表格；匯入的每一列都是空白） | status 欄 |
| status 欄的不同寫法 | 420 種 | status 欄 |
| 連結的解法 | 4,296 個檔案 — 2,897 Python、1,210 Java、1,006 兩者皆有、165 MySQL、21 C++、6 Scala、4 Shell | solution 欄 |
| 失效的解法連結 | 42 — 16 Python、21 C++（`C++/` 目錄不在 repo 裡）、4 Shell、1 MySQL | 連結檢查 |
| 沒有任何 README 列指向的解法檔 | 53 Python、**347 Java**（佔 `LeetCodeJava/` 的 22%） | 走訪目錄 |
| 有 `time = O(...)` 註解 | Python 2,867 / 2,897（99%）· Java 1,247 / 1,554（80%） | grep |
| 解法版本數（`# V0`、`# V1`…） | Python 2,897 個檔案共 8,479 個區塊（平均 2.9；753 個檔案 ≥ 4）· Java 1,554 個檔案共 5,990 個 | grep |
| IDEA 行帶有工具署名（`(gpt)`、`fixed by gemini`）的檔案 | 457 Java、202 Python | grep |
| 會執行任何東西的檔案（`assert`、`__main__`、`main()`） | 73 Python、2 Java；master 上不存在 `leetcode_java/src/test` | grep |
| Cheatsheets | 135 個檔案，145,163 行；14 份超過 1,900 行；所有 fence 都有語言標記；42 份提到 invariant | `doc/cheatsheet/` |
| FAQs | 49 份，100% 已翻譯 | `doc/faq/` |
| 練習紀錄 | 827 天，2020-04-29 → 2026-09-23；最近 30 天有 30 天、最近 90 天有 87 天；3,261 次嘗試提及、860 個不同題目；一個不可能的日期（`20260229`，第 269 行） | `data/progress.txt` |
| 精選清單覆蓋率 | Blind 75 · NC150 · NC250 · Top 100：**100% 索引**；NeetCode All 905 / 972（缺的 67 題中 29 題是 JavaScript-only 那一組） | `data/problem_lists.json` |
| 精選清單上的 Java | Blind 75 與 Top 100：完整；NC150 缺一題（LC 704）；300 個帶 `google` 標籤的列與 13 個 `MUST` 列沒有 Java | solution 欄 |
| 網站 | 18 個手寫頁面、36 個視覺化 demo、29 個 roadmap 主題、208 題測驗、14 個 agent skill；418 個單元測試通過 | `site/`、`data/`、`.claude/skills/` |

---

## 1. LC 覆蓋率 — 廣度已經完成；缺口是深度形的

### 1.1 重要清單的覆蓋率是 100%，所以覆蓋率不是槓桿

Blind 75、NeetCode 150、NeetCode 250 和 Top 100 Liked 上的每一題都有 README 列和解法，而且
Blind 75 與 Top 100 在**兩種語言**都完整。NeetCode All 的 972 題中有 905 題已索引，剩下的 29 題是
roadmap 刻意對應到 `null` 的 JavaScript-only 那一組。以 NeetCode 自己的分類看，最薄的組是
Greedy（59/67）、Heap（29/33）與 Trees（88/93）— 到處都只差個位數。

八月的 readiness 評估在 L3 標準下把 Volume 與 Breadth 都評為 A，只點出三個圖論形的主題缺口
（Dijkstra 7、topological sort 8、sweep line 3）。**再多解題也推不動分數。** 因此本節的每一項建議
都是關於已經在這裡的題目。

### 1.2 掌握度紀錄在面試官會抽的清單上說「還沒準備好」

| 清單 | 已索引 | `OK` | `AGAIN` | OK 佔比 |
|---|---|---|---|---|
| Blind 75 | 75 | 15 | 60 | 20% |
| NeetCode 150 | 150 | 24 | 125 | 16% |
| NeetCode 250 | 250 | 39 | 206 | 16% |
| Top 100 Liked | 100 | 16 | 84 | 16% |
| 全部有追蹤者 | 1,266 | 269 | 997 | 21% |

依難度：Easy 168 OK / 171 AGAIN，Medium 95 / 682，**Hard 6 / 144**。

這是地板而不是量測 — [`lc-readiness-guide.md`](./lc-readiness-guide.md) 已經解釋過原因
（`AGAIN` 只會被*加上*），而 `/lc-again` 就是為了修它而存在。但 `/lc-again` 是逐題的工具，
而 backlog 是一份清單。**建議：** 把它當成一場有固定範圍、有終點線的戰役來跑，而不是有空才做：

1. 範圍：Blind 75 上的 60 個 `AGAIN` 列。六十題，一天兩題，四週。
2. 標準：`/lc-again` 的四個問題 — 不靠提示重新推導、說出 invariant、點名決定複雜度的那一行、處理好
   邊界 — 加上 readiness guide 的時間盒（一題 medium 20 分鐘內）。
3. 終點線：Blind 75 的 OK 佔比 ≥ 80%。接著是 NeetCode 150 剩下的 50 題。

把這個佔比發佈在首頁，放在現有的 `269 OK / 997 AGAIN` 計數旁邊，讓這個數字被看見。兩個計數都已在
build 時算好；清單交集只是 `build-site.js` 裡五行的加法。

### 1.3 成本曲線在面試核心主題上最陡

依 README 主段落計算，每個有追蹤題目的平均記錄通過次數：

| 段落 | 列數 | `AGAIN` | 平均次數 |
|---|---|---|---|
| Recursion | 30 | 28 | **7.4** |
| Binary Search Tree | 20 | 16 | **7.0** |
| Queue | 5 | 4 | 7.0 |
| Stack | 44 | 41 | **6.3** |
| Backtracking | 34 | **34** | **6.3** |
| Prefix Sum | 3 | 3 | 6.3 |
| Breadth-First Search | 44 | 39 | **6.2** |
| Tree | 62 | 51 | **6.1** |
| Linked list | 24 | 21 | 5.7 |
| Depth-First Search | 56 | 51 | 5.0 |
| Heap | 28 | 25 | 4.5 |
| Binary Search | 48 | 41 | 4.3 |
| Dynamic Programming | 95 | 89 | 4.2 |
| … | | | |
| Bit Manipulation | 28 | 16 | 1.8 |
| SQL | 165 | 76 | 1.6 |
| Math | 93 | 71 | 1.1 |

Backtracking 是 34 題 34 個 `AGAIN`。遞迴結構的成本是陣列操作的三到六倍，而 Google L3 loop
大部分就是遞迴結構。

**建議：** 為成本最高的約 40 題（12 次以上、面試核心段落）各寫一張*推導卡*，寫一次，之後複習卡片而不是
重解題：一句話的 invariant、遞迴的契約（這個 call 回傳什麼、可以假設什麼）、決定複雜度的那一行、
一直失敗的那兩個邊界。readiness guide 的「Acting on it」§3 早就說了*「為慢性清單寫 invariant；
停止重解」*；repo 裡還沒有一個地方可以讓 invariant 住下來。`/lc-cheatsheet 1650 into binary_tree as variation`
接近，但它是歸檔進一份 2,000 行的 sheet；一張卡片想要的是屬於那一列自己的筆記。最便宜的家是新開一份
`doc/derivation_cards.md`，每題一小節，從 README 列的 Note 欄連過去。

### 1.4 README 的主題分類低估了它實際持有的內容

主表格裡 `Scan Line` 有 1 列、`Prefix Sum` 3 列、`Slide Window` 12 列、`Graph` 21 列，旁邊是
`SQL` 165 列與 `Math` 93 列。sliding-window 題在這裡並不少 — 它們依第一次遇到的技巧被歸檔在 Array、
String 和 Hash Table 之下 — 所以打開 `## Slide Window` 的讀者會以為 repo 只有十二題。roadmap 和
review plan 已經靠讀 tag 欄而不是標題解決了這件事；README 頁面和 explorer 沒有。**建議：** 當 PR #171
的就地過濾落地之後，段落標題就不那麼重要；在那之前，把該列的 `**tag**` 加進 explorer 的 tag facet
（它已經在 `lc-problems.json` 的 `tags` 裡），並在 `## Slide Window` 上說明索引是 tag 而不是標題。

### 1.5 匯入的表格組稀釋了給訪客看的索引

3,291 列中有 1,981 列 — 60% — 位於 `## Newly Added (kamyu104 gap)` 之下，status 欄是空的。首頁的
`3,270 problems` 把它們算進去；`269 / 997` 沒有，而頁面上沒有任何東西說明哪些列是練過的、哪些是匯入的。
21 個 id 兩邊都出現（其中有 `363 381 499 604 631 641 715 874 937 959`），而 `CLAUDE.md` 已經記錄了匯入
組正是 `/lc-python` 過去誤歸檔的地方。**建議：** 要嘛 (a) 給匯入列一個明確的 `imported` 狀態，讓 explorer
能提供*只看有追蹤者*的 facet、計數能寫成 `1,266 practised · 3,270 indexed`，要嘛 (b) 把匯入組移到自己的
頁面。(a) 是一趟 `fix_readme_tags.py`；(b) 是一個 `build-site.js` 的改動。兩者都能讓那 21 個重複變成 build 錯誤。

### 1.6 索引資料品質：沒有任何東西在把關

- **42 個解法連結失效。** 十六個 Python 路徑 — 打字錯誤（`letcode_python/…`、`eetcode_python/…`、
  `logger_rate_lmiter.py.py`）、八個 concurrency 題（LC 1114–1279）連到不存在的 `Python/` 前綴、
  四個從未 commit 的檔案 — 加上 21 個連到不在 repo 裡的 `C++/…`、四個檔案已搬到 `archived/` 的
  `leetcode_shell/` 連結，以及一個 SQL 檔。`e2e-check.js` 看不到任何一個，因為它們是絕對的 GitHub URL。
- **347 個 Java 檔與 53 個 Python 檔沒有任何列連到**，所以它們對索引、對 `find_missing_java.py`、
  對網站都是隱形的。對 Java 來說那是 22% 的目錄。
- **status 欄有 420 種不同寫法**，包括 `not start`、`Again (1)`、`Again******* (2)(again)`，以及
  `again` 的各種大小寫。`/lc-again` 刻意不標準化它們；別的東西也沒做。
- 練習紀錄裡有**一個不可能的日期**，第 269 行的 `20260229` — 以字串解析，所以能排序、能排程，但任何對
  它做日期運算的程式都會炸。

沒有任何東西像 `e2e-check.js` 把關 `_site/`、`check_skills.py` 把關 `.claude/skills/` 那樣把關 README
（`check_lc_format.py` 雖然叫這個名字，稽核的是 cheatsheet 的 LC 範例標頭）。**建議：** 一支同一模子的
`script/check_readme.py` — 透過網站已在用的 parser 讀列，遇到失效的解法連結、沒被連到的解法檔、兩個表格組
間重複的 id、不符合單一文法（`(OK|AGAIN)\*{0,}( \(\d+\))?( \(MUST\))?`）的 status 欄、無法解析的紀錄
日期就失敗 — 從 `validate-pages.yml` 執行。文法遷移是對 1,266 個欄位跑一次 regex；420 種寫法會收斂成幾十種。

### 1.7 選定面試語言，並說出來

Python 有 2,897 個解法，Java 1,210 個。兩者都完整覆蓋 Blind 75 與 Top 100；NC150 的 Java 只缺 LC 704。
但 300 個帶 `google` 標籤的列與 13 個 `MUST` 列沒有 Java。repo 裡沒有任何地方說面試會用哪種語言。
如果是 Java，那 13 個 `MUST` 列是 `/lc-java` 該優先補的；如果是 Python，Java 的缺口根本不是優先事項，
而 `CLAUDE.md` 裡 `1244 vs 2898` 的框架量的就是錯的東西。**建議：** 在 README 的 Resource 段落加一行寫明
面試語言，並給 `find_missing_java.py` 一個 `--must` flag，讓真正重要的報告是一條指令。

---

## 2. 解題程式碼 — 是嘗試的存檔，不是一組範本解答

house layout 很好且一致地被套用：2,897 個 Python 檔中的 2,897 個都以題目 docstring 和 `# V0` 區塊開頭；
99% 帶有 `time =` 行。問題在於這個 layout 裡面裝的東西。

### 2.1 每個檔案平均三個解法，沒有標出正典的那一個

2,897 個 Python 檔共 8,479 個 `# V…` 區塊；753 個檔案有四個以上版本。Java 是 1,554 個檔共 5,990 個。
依慣例 `V0` 是作者自己的、`V1+` 是參考 — 但 `V0` 常常是*第一次*嘗試（見 2.2），而一個準備面試的讀者需要的是
*最好的*那一個和它為什麼最好，不是時間順序。

cheatsheet style guide 已經為新 cheatsheet 程式碼定下規則：*每題一個正典解法；第二個版本需要一個明說的理由。*
解法目錄沒有對應的規則。**建議：** 採用一個 skill 會寫、網站能讀的標記 — `# V0 ★` 或一行 `# CANONICAL` —
並讓 `/lc-python` 和 `/lc-java` 設定它。之後 explorer 和 roadmap 就能直接連到正典區塊的 anchor，後續也能
再做一趟合併，把八月 cheatsheet 審查數出來的近似重複的 `V0'`/`V0''` 寫法（僅 sheet 裡就約 450 個）收攏。
那一趟不要刪版本；Java 裡 `// V` 標記就是方法的名字，`CLAUDE.md` 記錄了重新編號的代價。

### 2.2 工具署名與除錯旁白滲進了存檔

457 個 Java 檔與 202 個 Python 檔帶著像 `// IDEA: DFS (fixed by gpt)`、`// IDEA: DP (gemini)`、
`# IDEA: PREFIX SUM (gpt)` 的 IDEA 行。抽樣的
`SlideWindow/CountSubarraysWhereMaxElementAppearsAtLeastKTimes.java` 還把助理自己的旁白當程式註解留著 —
*「1. Correctly find the maximum element… The original loop was missing the comparison logic」* — 並有
**兩個** javadoc 區塊以兩種格式陳述複雜度（先 `Time Complexity: O(N)` 再 `time = O(N)`）。

這些對讀者都沒有幫助，有些還有害：`V0` 區塊上的 *「fixed by gemini」* 告訴讀者作者的版本錯了卻沒說錯在哪，
而那正是學習者唯一想知道的事。**建議：** 一趟 `db49955`（1,481 個 Java 檔的標頭清理）精神的標準化 — 把署名移到
單一一行結尾的 `# ref:`、刪掉除錯敘述、去除重複的複雜度 javadoc — 並在兩個歸檔 skill 的 `## Do not` 各加一條規則：
*IDEA 行不放工具署名；說錯在哪，不說誰修的。*

### 2.3 驗證不存在於產出物裡，而它是量出來最弱的訊號

2,897 個 Python 檔中 73 個會執行任何東西；Java 有兩個 `main` 方法、沒有 `src/test`（PR #129 加了三個排序
測試）。同時，八月 readiness 評估最弱的訊號是 **31–44% 的提交被拒率，而且 Easy（44%）高於 Hard（31%）** —
這是有速度但沒有驗證習慣的簽名 — readiness guide 的第二優先就是*「限時、不執行的練習」*。

每個 Python 檔已經在 docstring 裡帶著題目的範例，`/lc-python` 歸檔前也已經拿它們做 smoke test。
那個測試跑一次就被丟掉。**建議：** 讓 `/lc-python` 把它跑過的東西輸出成一個由 docstring 範例組成的
`if __name__ == "__main__":` assert 區塊，並讓 `python-syntax-check.yml` *執行*帶有這個區塊的檔案。
成本：每個新檔幾行。效果：存檔隨時間變成一套回歸測試，而且 — 真正的重點 — 在執行前先寫 assert，正是面試
在評分的驗證習慣，每次歸檔都練一次。

Java 方面，PR #129 的 `pom.xml` 與 `src/test` 目錄是對的基礎；把它合進來，並讓 `/lc-java` 每次歸檔加一個
JUnit 方法。

### 2.4 複雜度註解：先補完 Java，再檢查它們

Python 在 99%；Java 在 80%（307 個檔案沒有 `time = O` javadoc）。`/add-time-space` 正是為此而存在，
文件裡也寫明是逐目錄的掃描。覆蓋完整之後，價值更高的檢查是*正確性*：複雜度測驗的評分器（`site/complexity.js`）
已經能解析並正規化一個 bound，所以一支把每個檔案的 `time =` 行拿去和 README 列的複雜度欄比對的 script，
可以找出不一致的那些。目前沒有任何東西在比對它們。

---

## 3. 文件 — 形狀對了，入口不對

### 3.1 cheatsheet 是一座被要求當課程用的參考圖書館

135 份 sheet，145,163 行，其中 14 份超過 1,900 行。每一份都有 Scope 行（0 份缺 — 八月的清理守住了），
兩種骨架的規則有效，一條 12 份的「start here」階梯存在，卡片帶有 1–5 的 tier。這是一座非常好的圖書館。

它是一份很差的*六週教學大綱*，因為閱讀的單位是 sheet，而 sheet 有 2,000 行。tier-5 那一組是 22 份 —
大致對應 roadmap 的 29 個主題 — 而一個候選人讀不完 40,000 行。**建議：** 每份 tier-5 sheet 在 Scope 行
正下方放一個一頁的區塊，固定形狀：模板（一種語言，約 20 行）、一句話的 invariant、用來證明它的三題、
面試官會追問的 follow-up。sheet 的 `Pattern Selection Strategy` 和 `Summary` 段落已經有這些內容；
只是要搬到頂端並刪減。之後 `build-site.js` 就能把這個區塊渲染成 `cheatsheets.html` 上卡片的展開狀態，
索引變成大綱，sheet 保持作為參考。

### 3.2 辨識很強；推導很薄

135 份 sheet 中 42 份用了 *invariant* 這個詞；42 份提到該對面試官說什麼。PR #155 的草稿審查直接量了這件事 —
**22 份 tier-5 sheet 中有 17 份從未點名一個暴力解基準**，一開頭就是最佳模板、沒有退路 — 並找到一個正確性缺陷
（把 `(l+r)/2` overflow 列為陷阱的同一份模板卻出貨了它；19 個這樣的區塊對比 48 個安全的）。本審查確認了那項
發現的形狀，並支持合入那個 PR。兩點補充：

- `lc-coach` 的 `references/talk-track.md` 與 `references/patterns.md` 已經持有面試間裡的結構
  （暴力解 → 觀察 → 模板 → 複雜度那一行 → follow-up）。sheet 應該*連結*到它們而不是重述，讓 coach 和 sheet 一致。
- `pattern_recognition.md`（157 行，建成 `patterns.html`）是辨識這一側槓桿最高、也最小的文件。每一列把一個
  關鍵字對應到一個模式和一份 sheet；加上*視覺化 demo* 與*暴力解基準*兩欄，它就變成候選人前一晚該讀的那一頁。

### 3.3 `doc/` 沒有索引，讀者分不出哪份計畫是現行版

`doc/` 裡有三份 Google 備考計畫（`goog_swe_prep_plan_claude.md`、`_gpt.md`、`_gpt_v2.md`）、六個
`leetcode_company_V1`–`V6` 的 vendored PDF 目錄、一份 10 行的 `code_interview_general_cheatsheet.md`、
15 行的 `routine.md`、29 行的 `priority_queue.md`，以及文字傾印（`crack_fanng_interview.txt`、`tech_blog.txt`、
`solved_1000_LC.txt`）。它們旁邊坐著*確實*現行的文件 — `lc-readiness-guide.md`、`google_swe_lc_essentials.md`、
`g_swe_final_week_review.md`、兩份審查和 skills plan — 而沒有任何東西說明哪個是哪個。（八月審查對
`lc_category.md` 的發現已經修好：它現在是指向上游分類的指標，正如它應該的樣子。）

**建議：** 一份 `doc/README.md`，三張清單 — *現行*、*歷史*、*vendored* — 並把被取代的備考計畫搬進
`archived/`（它已經為此存在）。把三份備考計畫合併成一份寫明 L3 標準的，並依八月審查所說把
`code_interview_general_cheatsheet.md` 併入鄰居。

### 3.4 說清楚哪些文件服務 coding loop

四十九份 FAQ（Java、JVM、Kafka、Spark、Redis、Flink、Airflow）和十一個 `system_design/` 案例 — 每個都只有
單一檔案 — 是後端與資料工程的面試材料。它們很好、100% 已翻譯，且不是 Google L3 coding loop 會考的東西。
專案自己的記憶說在目標改變前先降低 system design 的優先度。那是對的決定；缺的那一步是*標示*它，在首頁和
`doc/README.md` 上，讓一個為 coding loop 準備的訪客不會把晚上耗在 `faq_kafka.md` 裡。

### 3.5 格式規則守住了

135 份 sheet 每一份都有 Scope 行，每個開頭 fence 都帶語言標記，沒有一份缺 `cheatsheet_meta.json` 條目
（build 會失敗）。八月清理的規則在沒有 markdown 本身把關的情況下守了一個月；仍未被強制的只剩*「複雜度只說一次」*
與*「一個正典解法」*那一對，它們需要的是讀者而不是 regex。

---

## 4. 網站 — 工具很強，缺一個練習迴圈

以工程來說，網站是這個專案最強的部分：`build.sh` 是唯一的配方，`e2e-check.js` 走遍每一頁並對真正出過事的
那幾類壞法失敗，planner 和 problems filter 是從*建好的*頁面原樣抬出來在 jsdom 裡跑的，418 個單元測試通過。
八月 31 日審查的修正全部守住。以下談的是網站為候選人做了什麼，而不是它是否正常運作。

### 4.1 第一次造訪看到什麼

線上首頁顯示三組各四張卡片、一條九張卡片的 agent-skills 帶，以及六個計數
（`3,270 · 134 · 49 · 36 · 269 OK · 997 AGAIN`）。初次造訪者會被兩件事絆到：

- **`OK` / `AGAIN` 沒有解釋。** 它們是頁面上最重要的兩個數字，卻是內部詞彙。計數下面一句話 —
  *「這位工程師已重新乾淨推導出的題目，對比仍在複習清單上的」* — 就能解決。
- **十五個入口，沒有一條時間盒路徑。** 有 45 分鐘的候選人找不到一張卡片說*有 45 分鐘就從這裡開始*。見 4.2。

### 4.2 沒有限時練習模式

沒有任何頁面實作計時器。random picker 抽一題；review plan 挑一個 session；兩者都不計時、都不在揭露解法前
要求說出複雜度、都不產生可以貼進 `/lc-log` 的那一行。考慮到量出來的弱點是限時執行與驗證，這是網站擁有的和
目標需要的之間最大的單一缺口。

**建議 — 一個 *session* 頁面，或 random picker 上的一個模式：** 選長度（20 / 35 / 45 分鐘）和清單；頁面抽題、
開始倒數、隱藏解法連結；結束時要求填 time 與 space bound（用測驗已在用的 `complexity.js` parser 評分），
*然後*才揭露 repo 的解法；最後印出這個 session 的 `progress.txt` 行（`1234(ok), 567(again!!)`）供貼上。
完全在 client 端、不需要新資料，並重用網站已擁有的三樣東西 — 清單選擇器、複雜度評分器、紀錄文法。

### 4.3 三份彼此不通的進度紀錄

進度住在三個地方：README 的 status 欄（`OK`/`AGAIN`，經 `/lc-again` 手動編輯）、`data/progress.txt`
（紀錄，經 `/lc-log`），以及 roadmap 每個瀏覽器自己的 `localStorage` 勾選框 — 頁面自己說它們*「只存在這個
瀏覽器裡，永不上傳」*。review plan 已經在 build 時把前兩者合併成 `progress.json`。**建議：** 讓 roadmap 讀同一份
`progress.json`，把 `OK` 渲染為完成、`AGAIN` 為進行中，`localStorage` 只留給瀏覽器自己的勾選。這樣 roadmap 的
鎖才有意義，候選人也能看到一張自己在哪裡的圖。

### 4.4 explorer 的 facet 不包含候選人會用的那些

`lc-explorer.html` 依 tag、難度和 acceptance rate 過濾。它不依狀態（`OK` / `AGAIN` / 未追蹤）、不依精選清單
（`blind75` … 標籤就在 tag 欄裡，所以只差一個 facet）、不依可用語言、也不依通過次數過濾。資料全都在
`lc-problems.json` 裡，或離它一個 build 步驟。把*狀態*和*清單*加成一級 facet，explorer 就變成 §1.2 要求的
戰役追蹤器。

### 4.5 八月 31 日審查的待辦，仍然開著

這裡沒有重新驗證；列出來以免遺失：搜尋只索引標題與小標，`search.html` 載入時抓約 900 KB，TOC 不是 sticky，
網站預設深色且不讀 `prefers-color-scheme`（`site/` 裡沒有任何樣式表引用它），`https://` 轉導到 `http://`。
沒有一項是面試槓桿；色彩模式那一項是五行的修正。

### 4.6 進行中、值得合入的

PR #171（就地過濾題目索引）、PR #155（面試官視角的 cheatsheet 審查）、PR #129（Java `pom.xml` + CI 裡的
`src/test`）。每一個都關掉了本審查原本會建議的某件事。十一個 PR 開著、122 個遠端分支存在，其中不少是早前
session 留下的 `backup-*` 和 `worktree-*`；掃一次分支會讓進行中的集合變得可讀。

---

## 5. 方向 — repo 為累積而最佳化；目標需要的是轉換

### 5.1 兩個專案共用一個 repository

CS_basics 是一份**個人備考紀錄**（status 欄、練習紀錄、readiness script、`/lc-log`、`/lc-again`、`/lc-coach`），
也是一份**公開參考資料**（cheatsheet、視覺化 demo、roadmap、測驗、FAQ）。首頁的引言 — *「一位工程師面試準備背後的
筆記與解法」* — 誠實地說出了這一點。張力出現在個人層滲進公開層卻沒有解釋的地方（首頁上的 `AGAIN`、存檔裡的
`(fixed by gemini)`、`doc/` 裡的三份備考計畫）。宣告這兩層 — 網站上一個*我的狀態*切換、一份把紀錄和圖書館分開的
`doc/README.md` — 能一次解決 §3.3、§4.1 和 §2.2 的大部分。

### 5.2 活動量驚人；把它對準轉換

紀錄顯示**最近 30 天有 30 天**在練習、最近 90 天有 87 天 — 2025–26 兩年間共 469 個有紀錄的日子。這些努力目前
大多花在*新*的嘗試上：紀錄裡 650 個 `again` 註記對 163 個 `ok`，Blind 75 的 OK 佔比是 20%。readiness guide
自己的優先順序把 volume 放在最後，並說*「如果 Volume 已經是 A，再多解題也推不動總分。」* Volume 是 A。

**一個 90 天的方向，依 readiness guide 給的順序：**

1. **Signal（第 1–12 週）：** 每週兩次限時 session，用 §4.2 的 session 頁面或 LeetCode virtual contest。
   九年一場比賽是量出來最弱的數字。
2. **驗證（第 1–12 週）：** 每次歸檔都先寫 assert 再執行（§2.3）。
3. **成本曲線（第 1–8 週）：** 為成本最高的 40 題寫推導卡（§1.3）；複習卡片，不重解題。
4. **掌握度（第 2–10 週）：** Blind 75 的 `/lc-again` 戰役到 80% OK（§1.2），接著 NeetCode 150。
5. **Volume：** 不做。這一季把題數凍結在 3,270，並在 README 裡說明。

### 5.3 每月量，在固定的集合上

在 `data/roadmap.json` 出一份命名的清單 — 叫它 **`l3-core`**（`from: curated`，或 `list:` 指向
`problem_lists.json` 裡一個新 flag）：Blind 75 ∪（NeetCode 150 ∩ `google`），大約 100 題。
接著是每個月該看的數字，全部都是 build 已經算好、或離它一步的：

| 指標 | 現在 | 目標（90 天） |
|---|---|---|
| Blind 75 的 OK 佔比 | 20% | 80% |
| NeetCode 150 的 OK 佔比 | 16% | 50% |
| 12 次以上仍 `AGAIN` 的題目 | 125 | < 60 |
| 平均次數 — Recursion / BST / Backtracking | 7.4 / 7.0 / 6.3 | 持平（停止重解它們） |
| 紀錄裡的限時 session | 以紀錄文法計為 0 | 24 |
| 提交被拒率（readiness script） | 31–44% | < 25%，且 Easy 低於 Hard |

`eval_lc_readiness.py --json data/readiness-YYYY-MM.json` 已經是追蹤指令；guide 說每月一次。把 OK 佔比那一行
加進它的輸出，就完成了。

---

## 優先行動清單

依每單位努力對目標的影響排序。**S** 是一個下午，**M** 是一個週末，**L** 是數週的線。

| # | 行動 | 軸線 | 工作量 | 位置 |
|---|---|---|---|---|
| 1 | Blind 75 的 `/lc-again` 戰役到 80% OK，用四個問題的標準 | 掌握度 | L（練習時間，不是程式） | `README.md` status 欄 |
| 2 | 限時 session 模式：倒數、揭露前檢查複雜度、輸出 `progress.txt` 行 | 網站 | M | `site/pages/lc-random-picker.html` 或新的 `lc-session.html` |
| 3 | `/lc-python` 把 docstring 範例輸出成 `__main__` assert；CI 執行它們 | 程式碼 | S + S | `.claude/skills/lc-python/SKILL.md`、`python-syntax-check.yml` |
| 4 | 為約 40 個成本最高的題目寫推導卡 | 文件 | L | 新的 `doc/derivation_cards.md`，從 Note 欄連過去 |
| 5 | 每份 tier-5 sheet 的 Scope 行下放一頁「在面試間裡」區塊；合入 PR #155 | 文件 | L | `doc/cheatsheet/*.md`、`build-site.js` 卡片渲染 |
| 6 | 把關索引：失效連結、未連結檔案、重複 id、status 文法、紀錄日期 | 覆蓋率 | M | 新的 `script/check_readme.py`、`validate-pages.yml` |
| 7 | 標準化存檔：署名 → `ref:` 行、刪除除錯敘述、去重 javadoc | 程式碼 | M（腳本化） | `leetcode_python/`、`leetcode_java/` |
| 8 | 由歸檔 skill 寫、網站讀的正典解法標記 | 程式碼 | S 然後 L | skills、`build-leetcode.js` |
| 9 | explorer facet：狀態、精選清單、語言 | 網站 | S | `site/pages/lc-explorer.html`、`build-leetcode.js` |
| 10 | roadmap 讀 `progress.json`；`OK` = 完成 | 網站 | S | `site/roadmap.js`、`build-roadmap.js` |
| 11 | `l3-core` 清單 + 首頁與 readiness JSON 裡的 OK 佔比 | 方向 | S | `data/roadmap.json`、`build-site.js`、`eval_lc_readiness.py` |
| 12 | `doc/README.md`（現行 / 歷史 / vendored）；歸檔兩份被取代的備考計畫 | 文件 | S | `doc/` |
| 13 | 在首頁解釋 `OK` / `AGAIN`；把後端 FAQ 和 system design 標示為非 coding-loop | 網站 | S | `build-site.js` |
| 14 | 標記匯入列（`imported` 狀態）讓計數與 facet 能區分；讓 21 個重複成為 build 錯誤 | 覆蓋率 | M | `fix_readme_tags.py`、`build-roadmap.js` |
| 15 | 用 `/add-time-space` 補完 Java 的 `time =`（307 個檔）；再與 README 欄比對 | 程式碼 | M | `leetcode_java/` |
| 16 | 寫明面試語言；若是 Java 就補齊 13 個沒有 Java 的 `MUST` 列 | 覆蓋率 | S | `README.md`、`/lc-java` |
| 17 | 掃除過期的 `backup-*` / `worktree-*` 分支；關閉或合入十一個開著的 PR | 方向 | S | GitHub |

第 1–5 項是會推動分數的。第 6–17 項是防止它滑回去的。

---

## 本審查未涵蓋的部分

- **個別解法的正確性。** 4,000 個檔案被計數、抽樣、grep，沒有被逐一閱讀。唯一完整讀過的檔案（§2.2）是因為
  它最近才被選中，不是因為它被懷疑。
- **逐份 cheatsheet 的內容。** 那是 PR #155 的工作；本審查只量了整體語料並確認其發現的形狀。
- **`system_design/`、FAQ 與翻譯 backlog**，除了指出它們在這個專案為自己設定的 L3 coding-loop 標準之外。
- **LeetCode 端的數字**（被拒率、比賽場數、依 LeetCode tag 的主題缺口）。它們引自八月的 readiness 評估，沒有重新抓取。
- **八月 31 日的網站發現**列為仍開著，沒有重新驗證。

---

## 附錄 — 數字是怎麼算出來的

全部在 repo 根目錄、`953902764`、已安裝 `site/node_modules` 的情況下執行。

```bash
# README：不重複題數、難度、語言、精選清單覆蓋率
node -e '
const fs=require("fs");
const {parseReadmeProblems}=require("./site/build-roadmap.js");
const p=parseReadmeProblems(fs.readFileSync("README.md","utf8"));
const pl=JSON.parse(fs.readFileSync("data/problem_lists.json","utf8"));
console.log(p.size);
for (const L of Object.keys(pl.counts)) {
  const items=pl.problems.filter(x=>x.lists.includes(L));
  console.log(L, items.length, items.filter(x=>p.has(String(x.id))).length);
}'

# README：status 欄，依表格組與段落
#（parseReadmeProblems 會丟掉 status 欄，所以直接讀列）
awk -F'|' '/^## Newly Added/ { nw = 1 }
           /^\| *[0-9]{3,4} *\|/ { s = $(NF-1); gsub(/^ +| +$/, "", s); print (nw ? "NEW" : "MAIN"), s }' README.md \
  | sort | uniq -c | sort -rn | head

# 無法解析的解法連結，以及沒有任何列連到的檔案
node -e '
const fs=require("fs"),path=require("path");
const {parseReadmeProblems}=require("./site/build-roadmap.js");
const p=parseReadmeProblems(fs.readFileSync("README.md","utf8"));
const linked=new Set(), broken=[];
for (const r of p.values()) for (const [lang,u] of Object.entries(r.solutions)) {
  const rel=u.replace(/^https:\/\/github.com\/yennanliu\/CS_basics\/blob\/master\//,"");
  linked.add(rel); if (!fs.existsSync(rel)) broken.push(r.id+" "+lang+" "+rel);
}
const walk=(d,ext)=>fs.readdirSync(d,{withFileTypes:true}).flatMap(f=>f.isDirectory()?walk(path.join(d,f.name),ext):f.name.endsWith(ext)?[path.join(d,f.name)]:[]);
console.log("broken", broken.length);
console.log("py unlinked", walk("leetcode_python",".py").filter(f=>!linked.has(f)).length);
console.log("java unlinked", walk("leetcode_java/src/main/java/LeetCodeJava",".java").filter(f=>!linked.has(f)).length);'

# 解法存檔：版本數、署名、驗證、複雜度行
grep -hoE "^# V[0-9]" leetcode_python/*/*.py | wc -l
grep -rhoE "^\s*// V[0-9]" leetcode_java/src/main/java/LeetCodeJava | wc -l
grep -rliE "fixed by gemini|by gpt|\(gemini\)|\(gpt\)" leetcode_java/src/main/java/LeetCodeJava | wc -l
grep -liE "gemini|gpt" leetcode_python/*/*.py | wc -l
grep -lE "^\s*(assert |if __name__)" leetcode_python/*/*.py | wc -l
grep -rl "public static void main" leetcode_java/src/main/java/LeetCodeJava | wc -l
grep -l "time *= *O" leetcode_python/*/*.py | wc -l
grep -rl "time = O" leetcode_java/src/main/java/LeetCodeJava | wc -l

# Cheatsheets 與 FAQs
ls doc/cheatsheet/*.md | wc -l; wc -l doc/cheatsheet/*.md | tail -1
grep -li "invariant" doc/cheatsheet/*.md | wc -l
find doc/faq -name '*.md' | wc -l

# 練習紀錄
grep -oE "^[0-9]{8}" data/progress.txt | sort -u | wc -l
grep -oE "[0-9]{1,4}\((again!*|ok\*?|todo)" data/progress.txt | sed -E 's/.*\(//; s/[!*]//g' | sort | uniq -c

# 網站
npm test --prefix site           # 418 個測試
ls site/pages | wc -l; ls algo_demo/*.html | wc -l
python3 -c "import json;print(len(json.load(open('data/roadmap.json'))['nodes']), len(json.load(open('data/complexity_quiz.json'))['questions']))"
```

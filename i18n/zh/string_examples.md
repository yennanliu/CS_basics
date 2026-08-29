<!-- 8597a27dbfe5 -->
# 字串演算法 — 實作範例

> **範圍** — 字串類 LeetCode 的範例庫，每題每種語言只留一份標準解，並歸檔到它所實作的母文件模板底下；概念、模式目錄與模板本身都留在主字串文件。
> **另見**：[string.md](./string.md) — 母文件，每個範例對應的模板都在那裡；[string_operations.md](./string_operations.md) — 語言層級的字串 API，從同一份檔案拆出；[palindrome.md](./palindrome.md) — 回文家族的深入版；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 子字串搜尋；[sliding_window.md](./sliding_window.md) — 字元視窗類題目；[2_pointers_examples.md](./2_pointers_examples.md) — 雙指標範例庫，LC 165、524、763、809 和 953 那邊也有。

<!-- 4a0eabd6b81f -->
## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)

<!-- 5e7683f50573 -->
## 總覽

一題一節，編號連續不跳號。每題只出現一次。
先去 [string.md](./string.md) 讀它對應的模板，再回來看範例。

<!-- b9bf3d8e8f13 -->
### 題目索引

| 分組 | 題目 |
|---|---|
| **解析與比較** | [2-1)](#2-1-compare-version-number--lc-165), [2-2)](#2-2-add-two-numbers-ii--decode-string), [2-4)](#2-4-monotone-increasing-digits--lc-738), [2-5)](#2-5-validate-ip-address--lc-468), [2-8)](#2-8-roman-to-integer--lc-13), [2-13)](#2-13-verifying-an-alien-dictionary--lc-953) |
| **分組與遊程編碼** | [2-3)](#2-3-count-and-say--lc-38), [2-18)](#2-18-expressive-words--lc-809) |
| **建構與格式化** | [2-6)](#2-6-license-key-formatting--lc-482), [2-16)](#2-16-ambiguous-coordinates--lc-816), [2-19)](#2-19-integer-to-english-words--lc-273) |
| **搜尋與比對** | [2-7)](#2-7-repeated-string-match--lc-686), [2-10)](#2-10-palindromic-substrings--lc-647), [2-11)](#2-11-repeated-substring-pattern--lc-459), [2-20)](#2-20-rotate-string--lc-796) |
| **計數與貢獻法** | [2-9)](#2-9-count-unique-characters-of-all-substrings-of-a-given-string--lc-828), [2-15)](#2-15-count-pairs-of-equal-substrings-with-minimum-difference--lc-1794) |
| **雙指標與原地操作** | [2-12)](#2-12-reverse-only-letters--lc-917), [2-14)](#2-14-longest-word-in-dictionary-through-deleting--lc-524) |
| **前綴驗證** | [2-17)](#2-17-longest-word-in-dictionary--lc-720) |
| **雙序列 DP** | [2-21)](#2-21-space-optimised-two-sequence-dp--lc-72-lc-1143) |
| **參考** | [2-22)](#2-22-additional-high-frequency-problems-reference) |

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 09cb03276ac6 -->
### 2-1) Compare Version Number — LC 165
> **因重複而刪除**：另一份 Python 版本，從前面 pop 之後再跑兩個收尾迴圈 — 拆解再比對的做法完全相同；留下來的版本改成把較短的版本號補零。

- 同時走訪兩個字串，逐段比較數字
<!--CODE-->

<!-- 5ebbe5eb6f49 -->
### 2-2) Add Two Numbers II,  Decode String
> **因重複而刪除**：`str_2_int_v2` — 一樣是逐位累加，只是寫成 `(res + int(i) % 10) * 10` 再除回來。

- String -> Int
<!--CODE-->

<!-- e34b7ccf2ede -->
### 2-6) License Key Formatting — LC 482
> **因重複而刪除**：一份 30 行的「字串操作 + 暴力」版本，先去除分隔符、重新分組再接回去；留下來的反向掃描是同一個想法，九行就寫完。

<!--CODE-->

<!-- 0eb0b82b9736 -->
### 2-7) Repeated String Match — LC 686
> **因重複而刪除**：另一份 Python 版本，用同樣的 `(res-1)*sa <= 2*max(sa,sb)` 上界，只是多加了一堆邊界情況分支。

<!--CODE-->

<!-- 041f74879b22 -->
### 2-9) Count Unique Characters of All Substrings of a Given String — LC 828
> **因重複而刪除**：V1 那份跟這份逐字元完全相同，差別只在用 `string.ascii_uppercase` 取代字面字母表 — 而那段程式碼從沒寫出必要的 `import string`。它的說明保留下來，放在留存的程式碼上方。

<!--CODE-->

<!-- 14576683eec6 -->
### 2-10) Palindromic Substrings — LC 647
> 這題的 O(n) 中心擴展法與 Manacher 解法在 [palindrome.md](./palindrome.md)；這裡留的是 O(n³) 暴力解，用來對照。

<!--CODE-->

<!-- 92a18f94d8f2 -->
### 2-11) Repeated Substring Pattern — LC 459
> KMP／Z-array 的解法在 [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) 和 [advanced_string_algorithms.md](./advanced_string_algorithms.md)。

<!--CODE-->

<!-- 2831427b65bd -->
### 2-12) Reverse Only Letters — LC 917

**模式：選擇性反轉字元**
- 只反轉英文字母
- 非字母字元留在原本的位置
- 兩種做法：雙指標或堆疊

<!-- 72a742eaedcf -->
#### 做法 1：雙指標（最佳解）
<!--CODE-->

**字元判斷方法** — `Character.isLetter` / `isDigit` / `isLetterOrDigit` 以及 Python 的 `isalpha` / `isdigit` / `isalnum` — 已移到 [string_operations.md](./string_operations.md#character-classification--case)。

<!-- 0e574ef12bca -->
#### 做法 2：堆疊（FILO）
<!--CODE-->

**堆疊模式視覺化：**
<!--CODE-->

**比較：**
| 做法 | 時間 | 空間 | 什麼時候用 |
|----------|------|-------|-------------|
| 雙指標 | O(N) | O(N) | 原地修改，最佳解 |
| 堆疊 | O(N) | O(N) | 需要保留原字串，邏輯較直觀 |

**類似題目：**
- LC 917 Reverse Only Letters（本模式）
- LC 345 Reverse Vowels of a String（選擇性反轉）
- LC 344 Reverse String（整段反轉）
- LC 541 Reverse String II（選擇性區間）
- LC 151 Reverse Words in a String（以單字為單位反轉）

<!-- aa313a560a4d -->
### 2-13) Verifying an Alien Dictionary — LC 953

**模式：自訂字典序比較**
- 把每個字元對應到它在外星字母序中的排名
- 逐字元比較相鄰的兩個單字
- 處理前綴情況：較短的單字必須排在前面

<!-- 804be6577593 -->
#### 做法：陣列映射 + 相鄰單字比較
<!--CODE-->

**關鍵洞見：**
<!--CODE-->

**類似題目：**
- LC 953 Verifying an Alien Dictionary（本模式）
- LC 269 Alien Dictionary（拓撲排序，更難）
- LC 242 Valid Anagram（字元次數映射）

<!-- 7db0c0fc999c -->
### 2-14) Longest Word in Dictionary through Deleting — LC 524

**模式：子序列檢查 + 追蹤最佳候選**
- 檢查字典中的單字能不能由 `s` 刪除若干字元得到（也就是是不是 `s` 的子序列）
- 追蹤最佳結果：長度最長者勝，長度相同時比字典序
- 雙指標的子序列檢查是核心技巧

<!-- 910556de6a67 -->
#### 做法 1：走訪 + 子序列檢查 + 就地更新最佳解（最佳解）
<!--CODE-->

<!-- f2c57b439d8e -->
#### 做法 2：先排序 + 回傳第一個符合的
<!--CODE-->

**關鍵洞見：**
<!--CODE-->

**類似題目：**
- LC 524 Longest Word in Dictionary through Deleting（本模式）
- LC 392 Is Subsequence（雙指標子序列檢查的原型）
- LC 720 Longest Word in Dictionary（前綴導向，不同模式）
- LC 1055 Shortest Way to Form String（子序列，要掃很多趟）

<!-- 35da5df77834 -->
### 2-15) Count Pairs of Equal Substrings With Minimum Difference — LC 1794

**模式：首／末出現位置 + 最小差值計數**
- LC 1794. Count Pairs of Equal Substrings With Minimum Difference (Medium)

<!-- b27c87491922 -->
#### 核心想法
<!--CODE-->

<!-- b6fc270c2b68 -->
#### Java 實作（O(n + m)）
<!--CODE-->

**關鍵技巧：**
<!--CODE-->

**類似題目：**
- LC 1624 Largest Substring Between Two Equal Characters（首／末出現位置的跨度）
- LC 387 First Unique Character in a String（追蹤第一次出現位置）
- LC 1 Two Sum（用雜湊表做 O(1) 配對／查詢）
- LC 242 Valid Anagram（字元次數陣列）
- LC 567 Permutation in String（字元位置映射 + 滑動視窗）

<!-- 36b0c8e33d1f -->
### 2-16) Ambiguous Coordinates — LC 816

**模式：枚舉切點 + 產生合法的數字格式**
- LC 816. Ambiguous Coordinates (Medium)
- 給一串數字如 `"(123)"`，插入一個逗號和（可選的）小數點，還原出所有可能的 `"(x, y)"` 座標。

<!-- a77c7919a6e6 -->
#### 合法性規則（麻煩的地方）
<!--CODE-->

<!-- c0b196a5bd76 -->
#### Python（V0 — 明寫輔助函式）
<!--CODE-->

<!-- 098792c015b2 -->
#### Python（精簡版 — 產生器）
<!--CODE-->

**實例演練：**
<!--CODE-->

**關鍵技巧：**
<!--CODE-->

**類似題目：**
- LC 93 Restore IP Addresses（枚舉切點 + 每段合法性）
- LC 468 Validate IP Address（每段的前導零／範圍規則）
- LC 282 Expression Add Operators（在數字之間插入運算子）

<!-- 571bec01c372 -->
### 2-17) Longest Word in Dictionary — LC 720
**模式：逐步前綴驗證** — 先把單字排序，維護一個「已經可以被建出來」的集合，然後只檢查**直接前綴**。從母文件的模板目錄搬過來：這是單一題目，不是一個家族。

> **因重複而刪除**：`longestWord_v2`，唯一的差別是寫 `word[:len(word)-1]` 而不是 `word[:-1]`。

<!--CODE-->

<!--CODE-->

**關鍵洞見：**

1. **為什麼只檢查直接前綴就夠：**
   - 排序保證較短的單字先被處理
   - 如果 "worl" 合法，它所有的前綴（"wor"、"wo"、"w"）一定早就合法了
   - 這是**歸納法**：檢查直接前綴就足夠

2. **為什麼排序行得通：**
<!--CODE-->

3. **複雜度拆解：**
   - 排序：O(N log N)
   - 處理：O(N * M)，M = 平均單字長度
   - 空間：O(N * M)，用於 HashSet
   - 總計：O(N log N + N*M)

4. **類似題目：**
   - LC 720 Longest Word in Dictionary（本模式）
   - LC 745 Prefix and Suffix Search（Trie 變形）
   - LC 648 Replace Words（Trie + 前綴比對）

<!-- 8340556360d2 -->
### 2-18) Expressive Words — LC 809
> 實作 [string.md](./string.md#template-3-run-length-grouping-consecutive-character-groups--lc-696-) 的 **模板 3：遊程分組** — 先讀那邊。

*變化點*：不是掃一個字串的分組，而是替**兩個**字串都建出 `(char, count)` 分組再對齊。兩個字串相符的條件是：分組*序列*要一一對上，而且每個來源分組要嘛大小相同，要嘛「可以被拉長」（`>= 3`）。

<!--CODE-->

<!--CODE-->

- ⚠️ 擴展只能**變長**：`"aaa"` 無法對上 `"aaaa"`（需要 `n1 > n2`）。
- ⚠️ 大小為 2 的分組永遠拉不出來（從 `"a"` 變成 `"aa"` 不合法）— 這就是 `>= 3` 規則。

<!-- e59f005db553 -->
### 2-19) Integer to English Words — LC 273
> 實作 [string.md](./string.md#template-5-greedy-line-packing--space-distribution-text-wrapping--lc-68-) 的 **模板 5：貪婪排版 + 空白分配** — 同樣是「把片段收進 list，最後一次 join」的紀律，只是切法從按寬度換成按 3 位數一組。

*變化點*：同樣是「把片段收進 list，最後一次 join」的紀律，但切分規則變成**每 3 位數一組**，而不是「能塞幾個字就塞幾個」。用 `List<String>` 再 join，比 `StringBuilder` + `trim()` 好，因為這樣根本不可能出現連續兩個空白的 bug。

<!--CODE-->

<!--CODE-->

- ⚠️ 只有 `num == 0` 才輸出 "Zero"；中間出現的整組零必須**什麼都不輸出**。
- ⚠️ 10..19 是不規則的英文字 — 要在進到十位數分支**之前**先處理 `n < 20`。

<!-- 592515474ab8 -->
### 2-20) Rotate String — LC 796
> 暴力解，O(n²)。O(n) 的寫法是 `goal in (s + s)` 這個小技巧加上 KMP — 見 [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md)。

<!--CODE-->

<!-- 29fb51ccedf3 -->
### 2-21) Space-Optimised Two-Sequence DP — LC 72, LC 1143
> 這個**家族**歸 [dp_string.md](./dp_string.md) 管，二維模板歸 [dp.md](./dp.md) 管；這裡留下的是 O(min(m, n)) 空間的 **Python** 滾動列寫法，那兩份文件只有 Java 版。

<!--CODE-->

<!-- 19e23567232a -->
### 2-22) Additional High-Frequency Problems (Reference)
沒有新模板 — 每題都是一句話就講完的想法，但出現頻率極高。

| 題目 | LC # | 一句話想法 | 難度 |
|---------|------|---------------|------------|
| Longest Common Prefix | 14 | 垂直掃描：比較所有單字的第 `i` 欄，第一次不合就停 | Easy |
| Isomorphic Strings | 205 | 兩張映射表（`s→t` **和** `t→s`）— 只用一張會誤判 `"ab" → "aa"` 合法 | Easy |
| Find and Replace Pattern | 890 | 就是 LC 205 的雙向映射，對清單裡每個單字各跑一次 | Medium |
| Ransom Note | 383 | 統計雜誌的字元次數，照勒索信逐字扣，扣成負的就失敗 | Easy |
| Most Common Word | 819 | 轉小寫 + 以非字母切分，跳過禁用集合，取次數最大者 | Easy |
| Reorder Data in Log Files | 937 | `split(" ", 2)` → id + 內容；自訂比較器：字母日誌按 (內容, id) 排，數字日誌保持原順序（穩定排序） | Medium |
| Bulls and Cows | 299 | 掃一趟：字元相同 → bulls；否則累加兩個計數陣列，cows = `sum(min(cntS[d], cntG[d]))` | Medium |

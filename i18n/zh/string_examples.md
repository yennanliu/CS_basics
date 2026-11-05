<!-- 80fb7bddbe88 -->
# 字串演算法 — 實戰題解

> **範圍** — 收錄不是模板錨點題的字串 LeetCode 實戰題，每題都寫成一張題目卡，歸檔到它所歸約的 [string.md](./string.md) 模板底下；概念、模式卡與錨點題本身都留在主字串文件。
> **另見**：[string.md](./string.md) — 母文件，每張卡歸約到的模板都在那裡；[string_operations.md](./string_operations.md) — 語言層級的字串 API，從同一份檔案拆出；[palindrome.md](./palindrome.md) — 回文家族的深入版；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 子字串搜尋；[sliding_window.md](./sliding_window.md) — 字元視窗類題目；[2_pointers_examples.md](./2_pointers_examples.md) — 雙指標題目集，LC 524 與 647 住在那裡（LC 165 是 2_pointers.md 模板 9 的錨定題）。

<!-- 4a0eabd6b81f -->
## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)

<!-- 2720af84af1b -->
## 總覽

下面每一節對應 [string.md](./string.md) 的一個模板，順序和那份文件一樣；每一題都是一張題目卡：先寫關鍵，再寫它歸約到哪張卡、差在哪一點，最後是一份標準解。這裡沒有題目的模板（5 到 8）就不開節，它們的錨點題在核心卡裡已經解完。先讀模板，再看題目卡。

<!-- 30c765a8b9a2 -->
## 索引

| [string.md](./string.md) 裡的卡 | 題目（易 → 難） |
|---|---|
| [Template 1：雙指標掃描與原地反轉](./string.md#template-1-two-pointer-scan--in-place-reversal--lc-125-) | [LC 917](#reverse-only-letters--lc-917) |
| [Template 2：字元頻率與 anagram 簽章](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) | [LC 1794](#count-pairs-of-equal-substrings-with-minimum-difference--lc-1794) · [LC 720](#longest-word-in-dictionary--lc-720) · [LC 828](#count-unique-characters-of-all-substrings-of-a-given-string--lc-828) |
| [Template 3：連續段分組](./string.md#template-3-run-length-grouping-consecutive-character-groups--lc-696-) | [LC 38](#count-and-say--lc-38) · [LC 809](#expressive-words--lc-809-) |
| [Template 4：分階段解析與重建](./string.md#template-4-parse--rebuild-by-phases--lc-8-) | [LC 13](#roman-to-integer--lc-13-) · [LC 482](#license-key-formatting--lc-482) · [LC 468](#validate-ip-address--lc-468) · [LC 816](#ambiguous-coordinates--lc-816) · [LC 273](#integer-to-english-words--lc-273-) |

**住在別處** — 完整解法在別的文件；列在這裡，讓搜尋這個題號的人有地方落腳。

| LC | 題目 | 所在 |
|---|---|---|
| 14 | Longest Common Prefix | [python_trick.md](./python_trick.md#dropping-the-last-character--s-1-shrinking-a-prefix) — 前綴縮短法；或直向掃描，把每個字的第 `i` 欄拿來比 |
| 72 | Edit Distance | [dp.md § Template 8](./dp.md#template-8-edit-distance--lc-72-) |
| 165 | Compare Version Numbers | [2_pointers.md § Template 9](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) — 每個字串一個指標，每輪解析一段版本號 |
| 205 · 890 | Isomorphic Strings · Find and Replace Pattern | [hash_map.md § Template 7](./hash_map.md#template-7-bijection-with-two-maps--lc-205) — 兩張 map，`s→t` **和** `t→s` |
| 394 | Decode String | [stack_expression_parsing.md](./stack_expression_parsing.md) |
| 445 | Add Two Numbers II | [add_x_sum.md](./add_x_sum.md) |
| 459 · 686 · 796 | Repeated Substring Pattern · Repeated String Match · Rotate String | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 524 | Longest Word in Dictionary through Deleting | [2_pointers_examples.md](./2_pointers_examples.md#longest-word-in-dictionary-through-deleting--lc-524-) — 對每個單字跑一次 LC 392 的子序列檢查 |
| 647 | Palindromic Substrings | [2_pointers_examples.md](./2_pointers_examples.md#palindromic-substrings--lc-647-) — 中心擴張；Manacher 在 [palindrome.md](./palindrome.md) |
| 738 | Monotone Increasing Digits | [greedy_examples.md](./greedy_examples.md) — 遇到下降就借位，後綴全部填 `9` |
| 953 | Verifying an Alien Dictionary | [hash_map.md § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) |
| 1143 | Longest Common Subsequence | [dp.md § Template 9](./dp.md#template-9-longest-common-subsequence--lc-1143) |

**一行解** — 不需要開卡，一行就講得完。

| LC | 題目 | 一行思路 | 卡 |
|---|---|---|---|
| 383 | Ransom Note | 數雜誌的字母，信裡每個字母扣一次，扣到負數就失敗 | [Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) |
| 819 | Most Common Word | 轉小寫、依非字母切開、跳過禁用字集合，取次數最多的 | [Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) |
| 299 | Bulls and Cows | 一趟：位置相同且數字相同是 bulls；其餘各自累加到兩個計數陣列，cows = `sum(min(cntS[d], cntG[d]))` | [Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) |
| 937 | Reorder Data in Log Files | `split(" ", 1)` 拆成 id 和內容；字母 log 依 (內容, id) 排序，數字 log 保持原順序（穩定排序） | [Template 6](./string.md#template-6-parse-structured-text-delimiter-split--depthstack--lc-388-) |

<!-- 247c11d12857 -->
## 雙指標掃描範例

<!-- 95e744d61b17 -->
### Reverse Only Letters — LC 917

> **關鍵** — 非字母永遠不動，所以它們是兩個指標要跨過去的牆；每一次交換都把左邊數來第 k 個字母和右邊數來第 k 個字母配對，這正好就是只反轉字母。

**歸約到**：[string.md § Template 1](./string.md#template-1-two-pointer-scan--in-place-reversal--lc-125-) — 就是 LC 125 的掃描，只是把比較換成交換，跳過的是非字母而不是非英數字元。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 兩個內層跳過迴圈之後直接交換、沒有再檢查 `left < right`：`"7_28]"`（沒有字母）會把兩個非字母對調。像上面那樣每輪只移動一個指標就不會出事。
- 用 `isalnum` 取代 `isalpha`：`"a1b"` 會把 `1` 也搬走。只有字母會動。

**相似題** — LC 344（整個反轉）· LC 345（只交換母音）· LC 151（反轉單字順序）

<!-- 3b09f56f3fb7 -->
## 字元頻率與簽章範例

<!-- 3efeadfc6fe4 -->
### Count Pairs of Equal Substrings With Minimum Difference — LC 1794

> **關鍵** — 任何一對相等的子字串都能縮成它的第一個字元，而 `j - a` 不會變大，所以只需要看單一字元；對每個字母，最佳配對就是它在 `firstString` 的**第一個**索引配上它在 `secondString` 的**最後一個**索引。

**歸約到**：[string.md § Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) — 每個字串各一張以字母表為索引的表，只是格子裡放的是索引（第一次或最後一次出現），不是次數。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 把每一對差值相同的*子字串*都算進去：答案數的是達到最小值的四元組，而每個字母剛好貢獻一個，所以 `("abcd", "bccda")` 是 1，不會更多。
- 在 `firstString` 留最後一個索引，或在 `secondString` 留第一個索引：兩者都會讓 `i - a` 變大，就錯過最小值。

**相似題** — LC 1624（第一次／最後一次出現的跨度）· LC 387（第一個不重複的字元）

<!-- fb50f96555ea -->
### Longest Word in Dictionary — LC 720

> **關鍵** — 排序之後，一個單字少一個字元的前綴一定比它先被處理，所以只要拿**直接前綴**去查「已經能蓋出來的單字」集合就夠了：用歸納法，更短的前綴在那個前綴放進集合時早就檢查過。

**歸約到**：[string.md § Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) — 用整個字串當 key 做雜湊查詢，而不是用字母次數；跨多個單字查前綴的一般解是 trie（[trie.md](./trie.md)），這裡用排序後的集合取代它，因為每個單字只會問一個前綴。

<!--CODE-->

<!--CODE-->

**陷阱**
- 更新 `best` 時用 `>=` 而不是 `>`：`["a","b"]` 會回傳 `"b"`；排序已經讓字典序較小的字先到，所以只有嚴格更長的字才能取代它。
- 先把單字加進 `buildable` 再檢查前綴：只有 `["banana"]` 時就會從一個不是單一字母的字開始一條鏈。

**相似題** — LC 648（取代單字，trie）· LC 1858（所有前綴都在的最長單字，trie）

<!-- ad09a978ec1e -->
### Count Unique Characters of All Substrings of a Given String — LC 828

> **關鍵** — 把總和翻過來算：不要去數每個子字串裡有幾個唯一字母，而是對每個位置數「這個字母在其中只出現一次」的子字串有幾個。那就是（到前一個相同字母的距離）×（到下一個相同字母的距離）。

**歸約到**：[array.md § Template 5: Index Contribution Counting](./array.md) — 同樣是「每個元素貢獻 左邊選法 × 右邊選法」的總和，跨度由同一個字母的前後兩次出現夾住；每個字母記住最後兩個位置的那張表，就是 Template 2 的字母表，只是存的是索引。

<!--CODE-->

<!--CODE-->

**陷阱**
- 忘了最後那一趟：迴圈結束時，每個字母的最後一次出現都還沒結算，所以 `"ABC"` 會回傳 0 而不是 10。
- 列舉所有子字串再用集合數：最好也是 O(n²)，而 n ≤ 10⁵。

**相似題** — LC 2063（所有子字串的母音數）· LC 907（子陣列最小值總和）· LC 2262（字串的總吸引力）

<!-- 5712b0abef5e -->
## 連續段分組範例

<!-- 8ae53ef62337 -->
### Count and Say — LC 38

> **關鍵** — 每一項就是前一項壓縮成組、再念成 `次數 + 字元`；整個序列只需要分組走訪，跑 n − 1 次。

**歸約到**：[string.md § Template 3](./string.md#template-3-run-length-grouping-consecutive-character-groups--lc-696-) — 同一個分組走訪，每組輸出 `str(count) + char`，而不是把相鄰組長合併。

<!--CODE-->

**陷阱**
- 在走訪裡用 `+=` 拼出這一項：對項長是平方時間；先加進 list，最後 join 一次。
- 在迴圈外手動收最後一組：`i = j` 的走法會自己收掉每一組，包括最後一組，所以 `n = 2` 不用特例就回傳 `"11"`。

<!-- 06960fe1ba54 -->
### Expressive Words — LC 809 ⭐⭐⭐⭐

> **關鍵** — 一個單字能對上 `s`，若且唯若兩個字串的組序列相同，而且每一對組要嘛長度相等，要嘛 `s` 那組比較長**而且**至少 3。

**歸約到**：[string.md § Template 3](./string.md#template-3-run-length-grouping-consecutive-character-groups--lc-696-) — **兩個**字串的組，各用一個指標同步走，逐對比較組長，而不是累加 `min(prev, cur)`。

<!--CODE-->

<!--CODE-->

<!--CODE-->

| `s` 的組 vs 單字的組 | `s` 的組 ≥ 3？ | 結果 |
|---|---|---|
| 相等 | — | 對上 |
| `s` 較短 | — | 不行：拉長永遠不會變短 |
| `s` 較長 | 是 | 對上：單字那組被拉長了 |
| `s` 較長 | 否 | 不行：長度 2 的組不可能是拉長出來的 |

**陷阱**
- 少了最後的 `i == len(s) and j == len(word)`：`s = "abcd"`、單字 `"abc"` 會對上迴圈看到的每一組，卻把 `d` 留著沒讀。
- 允許 `run_s < run_w`：`s = "aaa"`、單字 `"aaaa"` 不是 stretchy；延伸只會變長。
- 把長度 2 的組當成可以拉長：`s = "aa"`、單字 `"a"` 不合法；拉長後的組一定要到 3。

**相似題** — LC 443（字串壓縮）· LC 1446（最長的組）· LC 925（長按鍵入：同樣的同步走訪，沒有 ≥ 3 的規則）

<!-- bbf89d498559 -->
## 解析與重建範例

<!-- 991897310a33 -->
### Roman to Integer — LC 13 ⭐⭐⭐⭐

> **關鍵** — 從右往左讀：比右邊符號小的符號是減法前綴（`IV`、`CM`），所以要減；其他一律加。一個比較就取代六種特殊組合。

**歸約到**：[string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — LC 12 數值表的解析方向：一個索引，每個字元一條規則，規則由已經讀過的鄰居決定。

<!--CODE-->

<!--CODE-->

**陷阱**
- 相等時也減（`cur <= prev`）：`"III"` 會回傳 1 而不是 3。
- 把六種組合（`IV`、`IX`、`XL`、…）寫死成兩字元 token 也能過，但面試官會追問；一個比較就涵蓋全部。

**相似題** — LC 12（重建方向，在核心卡裡）· LC 273（下面）

<!-- 9267f8970b60 -->
### License Key Formatting — LC 482

> **關鍵** — 只有第一組可能比較短，所以從**右邊**開始建：每 k 個字元放一個破折號，最後反轉一次。左邊完全不需要算組大小。

**歸約到**：[string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — 重建方向：讀的時候丟掉舊分隔符，依固定規則寫入新的，從尾端掃是因為規則錨定在尾端。

<!--CODE-->

**陷阱**
- 組滿時在字元*之後*才寫破折號：`"a-a-a-a-"`、k = 1 會多出開頭的 `-`。像上面那樣在下一個字元之前插入。
- 輸入只有破折號（`"---"`）時必須回傳 `""`，不是 `"-"`。

<!-- 844c42afca92 -->
### Validate IP Address — LC 468

> **關鍵** — 分隔符的數量決定文法（3 個點 → IPv4，7 個冒號 → IPv6），接著每個欄位對照一張短的規則清單；第一條被打破的規則就回答 `"Neither"`。

**歸約到**：[string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — 依明確規則解析，只是階段變成「用分隔符切出的欄位」，而不是用一個索引走過的字元類別。

<!--CODE-->

**陷阱**
- 漏掉空欄位檢查：`"1.1..1"` 點的數量對（3 個），但有一個空欄位；`len(part) >= 1` 會擋掉它。
- `"01.01.01.01"` 是不合法的 IPv4（前導零），而 `"0db8"` 是合法的 IPv6 欄位：前導零規則只屬於 IPv4。
- `str.isdigit()` 會接受 `"²"`；要跟 `'0'` … `'9'` 比。

**相似題** — LC 93（復原 IP 位址：同樣的欄位規則，改成列舉）

<!-- 2a8e8a648d3b -->
### Ambiguous Coordinates — LC 816

> **關鍵** — 兩個互相獨立的選擇：逗號放哪裡，以及每一半的小數點放哪裡（或不放）。一半合法的條件是整數部分沒有前導零、小數部分沒有尾隨零，所以答案就是兩半合法格式的笛卡兒積。

**歸約到**：[string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — 和解析數字一樣的前導零／格式規則，只是套用到每個切割位置，而不是單一個給定字串。

<!--CODE-->

<!--CODE-->

**陷阱**
- 單獨的 `"0"` 是合法的整數，`"00"` 和 `"01"` 不是：要先判斷 `n == 1` 再套前導零規則，否則 `"(00011)"` 會漏掉 `(0, 0.011)`。
- 任何以 `0` 結尾的小數部分都不合法，包括 `"1.0"`；否則 `"1.0"` 和 `"1"` 會同時出現。

**相似題** — LC 93（復原 IP 位址）· LC 282（給表達式加運算子）

<!-- ed65336a0cbc -->
### Integer to English Words — LC 273 ⭐⭐⭐⭐

> **關鍵** — 英文以 3 位數為一塊來念數字，每塊後面接它的單位（Thousand、Million、Billion）；1–999 只要拼一次、每塊重用，而全為零的那塊什麼都不說。

**歸約到**：[string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — 重建方向，就像 LC 12 的貪婪表：這裡的表是單位清單，每一塊再用 below-20 和十位數兩張表拼出來。每次剝掉最大單位的遞迴寫法在 [recursion.md](./recursion.md)。

<!--CODE-->

<!--CODE-->

**陷阱**
- `1000010` 必須念成 `"One Million Ten"`：中間那塊是 0，把它的單位也輸出就會變成 `"One Million Thousand Ten"`。
- `num == 0` 是唯一會寫出 `"Zero"` 的地方；內部的零什麼都不貢獻。
- 10 到 19 是不規則的字：要在十位數分支之前先處理 `n < 20`，否則 15 會變成 `"Ten Five"`。
- 用 `" "` 拼字串再 trim 容易出現雙空白；把單字放進 list 再 join 就不可能出現。

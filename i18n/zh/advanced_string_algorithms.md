<!-- 97ca55462869 -->
# 進階字串演算法

> **範圍** — 比較重的字串工具：後綴結構、Z-algorithm、Manacher，以及字串 DP — 這些對主字串文件來說太專門的部分。
> **另見**：[string.md](./string.md) — 日常字串的題目目錄與模板；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 專講子字串搜尋；[palindrome.md](./palindrome.md) — 回文題家族；[trie.md](./trie.md) — 前綴結構。

<!-- 98814009baf3 -->
## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)
- [String Matching](https://leetcode.com/problem-list/string-matching/)
- [Rolling Hash](https://leetcode.com/problem-list/rolling-hash/)
- [Suffix Array](https://leetcode.com/problem-list/suffix-array/)

<!-- d9704cc9f28e -->
## 總覽
**進階字串演算法**指的是基本操作之外、比較講究的字串處理技巧。這些演算法在模式比對、回文偵測和複雜字串操作上，能給出有理論保證的最佳解。

<!-- 1a28ef5e502e -->
### 關鍵性質
- **時間複雜度**：最佳演算法通常是 O(n) 或 O(n + m)
- **空間複雜度**：預處理結構需要 O(n)
- **核心想法**：先把字串預處理過，之後查詢和比對就能很快
- **什麼時候用**：字串模式複雜、要查很多次、需要最佳化
- **主要演算法**：KMP、Manacher、Z-Algorithm、Rolling Hash、後綴陣列

<!-- 7d7134bfd559 -->
### 共同特徵
- **預處理**：先建輔助結構，換取後續操作的速度
- **辨識模式**：找出重複出現的結構與週期
- **線性時間**：靠一些巧妙的技巧壓到最佳複雜度
- **多次查詢**：同一個字串反覆操作時特別划算
- **理論基礎**：建立在字串理論與自動機之上

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- ffba067f6669 -->
### **分類 1：模式比對**
- **描述**：在文字中高效率地找出模式出現的位置
- **例題**：LC 28（Find Index of First Occurrence）、LC 459（Repeated Substring Pattern）
- **模式**：用 KMP、Z-Algorithm、Rolling Hash 做到 O(n + m)

<!-- 6e2c89a85814 -->
### **分類 2：回文問題**
- **描述**：找出所有回文，或最長的回文子字串
- **例題**：LC 5（Longest Palindromic Substring）、LC 647（Palindromic Substrings）
- **模式**：用 Manacher 演算法做到 O(n) 的回文偵測

<!-- f4e981edd39c -->
### **分類 3：字串週期性**
- **描述**：偵測重複模式與字串週期
- **例題**：LC 459（Repeated Substring Pattern）、LC 1316（Distinct Echo Substrings）
- **模式**：用 failure function 或 Z-array 做週期偵測

<!-- 19803f39d712 -->
### **分類 4：後綴類問題**
- **描述**：牽涉到字串後綴與字典序的問題
- **例題**：LC 1044（Longest Duplicate Substring）、LC 1316（Distinct Echo Substrings）
- **模式**：後綴陣列、最長共同前綴、rolling hash

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 5af14970af2f -->
### 模板比較表
| 演算法 | 用途 | 時間複雜度 | 空間複雜度 | 什麼時候用 |
|-----------|----------|-----------------|------------------|-------------|
| **KMP** | 模式比對 | O(n + m) | O(m) | 單一模式搜尋 |
| **Manacher** | 所有回文 | O(n) | O(n) | 回文類問題 |
| **Z-Algorithm** | 字串比對 | O(n) | O(n) | 模式比對的各種變形 |
| **Rolling Hash** | 子字串比較 | O(n) | O(1) | 多模式搜尋 |
| **DFA／狀態機** | 格式驗證／斷詞 | O(n) | O(1) | 規則亂成一團的 `if/else` 解析（LC 65） |

<!-- 8fe923ed7642 -->
### 模板 1：KMP（Knuth-Morris-Pratt）演算法 — LC 28
<!--CODE-->

<!-- 284c70d99f7c -->
### 模板 2：Manacher 演算法 — LC 5
<!--CODE-->

<!-- 44455f5eab16 -->
### 模板 3：Z-Algorithm — LC 459
<!--CODE-->

<!-- 1a4e65d9748e -->
### 模板 4：進階 Rolling Hash — LC 1044
<!--CODE-->

<!-- 2115aa308ed9 -->
### 模板 5：後綴陣列建構 — LC 1044
<!--CODE-->

<!-- 37e5f5cf7e3b -->
### 模板 6：DFA／狀態機（字串驗證） — LC 65 ⭐⭐⭐⭐

> **關鍵想法**：當一個字串格式的規則全是「這個只能接在那個後面」這種糾纏在一起的條件時，
> 別再寫巢狀 `if` 了，改寫**轉移表**。先把每個字元歸到少數幾個**字元類別**，
> 再讓一個 `state` 整數在手刻的 DFA 上跑。
> 一趟掃完、O(1) 記憶體，而且所有規則都集中在一張看得懂的表裡。
>
> **什麼時候該拿出來用**：格式驗證／斷詞（LC 65 Valid Number）這種題，
> 臨時拼湊的 `if/else` 解法正是多數人在邊界情況上翻船的地方（`"."`、`"4e+"`、`"3."`、`".9"`）。
> 這跟 KMP 底下的自動機是同一個概念（LPS 陣列**本身**就是一台比對自動機）—
> 差別只在這裡的自動機是手寫的，不是從模式推導出來的。

**做法**
1. 列出字元類別（這裡是 `digit`、`sign`、`dot`、`exp`）— 其他字元一律直接拒絕。
2. 列出狀態，每個狀態代表一種「我目前合法看過的東西」。
3. 填表；`-1` 代表死狀態。
4. 標出**接受狀態**；字串合法的充要條件是它停在其中之一。

<!--CODE-->

<!--CODE-->

**為什麼是狀態 2/4/7、而不是別的** — 一個合法數字只有三種結尾方式：

<!--CODE-->

**面試小技巧**：先在白板上把狀態圖畫出來，再抄成表。這張表讓解法可以自己被檢查 — 面試官讀一格就能驗證一條規則。

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- d8ea24fd4d5f -->
### **模式比對類**
| 題目 | LC # | 最佳演算法 | 時間複雜度 | 難度 |
|---------|------|----------------|-----------------|------------|
| Find Index of First Occurrence | 28 | KMP | O(n + m) | Medium |
| Repeated Substring Pattern | 459 | KMP/Z-Algorithm | O(n) | Easy |
| Shortest Palindrome | 214 | KMP + Reverse | O(n) | Hard |

<!-- bb668977aab7 -->
### **回文類**
| 題目 | LC # | 最佳演算法 | 時間複雜度 | 難度 |
|---------|------|----------------|-----------------|------------|
| Longest Palindromic Substring | 5 | Manacher | O(n) | Medium |
| Palindromic Substrings | 647 | Manacher | O(n) | Medium |
| Shortest Palindrome | 214 | Manacher/KMP | O(n) | Hard |

<!-- 892a927d65a0 -->
### **進階字串類**
| 題目 | LC # | 最佳演算法 | 時間複雜度 | 難度 |
|---------|------|----------------|-----------------|------------|
| Longest Duplicate Substring | 1044 | Rolling Hash + 二分搜尋 | O(n log n) | Hard |
| Distinct Echo Substrings | 1316 | Rolling Hash | O(n²) | Hard |
| Find All Anagrams | 438 | Rolling Hash | O(n) | Medium |

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- a2a0af9fe4a7 -->
### 2-1) Find Index of First Occurrence（LC 28） — KMP
> 為模式建 LPS（failure）陣列；比對失敗時直接跳過那些不必要的比較。

<!--CODE-->

<!--CODE-->

<!-- 13dbe1e0a462 -->
#### 變形：LC 686 Repeated String Match — *難的是界定 haystack 要多長，不是搜尋本身*

> 搜尋部分跟 LC 28 的 KMP 一模一樣；唯一的新東西是證明 haystack 到底要接多長。
> 如果 `b` 塞得進重複的 `a`，那它一定塞得進 `ceil(|b|/|a|)` 份（長度夠了）或
> `ceil(|b|/|a|) + 1` 份（多一份是為了涵蓋從某一份中間開始的比對）。再多接就沒有新的對齊方式了 —
> 所以**只有兩個候選，都不行就回 `-1`**。
>（`string.md` 給的是單純字串串接的解法；這裡是 O(N+M) 的比對版本。）

<!--CODE-->

<!--CODE-->

**常見錯誤**：寫成 `while len(a*k) < len(b) + 2*len(a)` 這種用猜的迴圈，或一路重複到某個隨便訂的上限。把界限明確講出來 — 面試官考的是這段論證，不是搜尋。

<!-- ada86780a06c -->
### 2-2) Longest Palindromic Substring（LC 5） — 中心擴展
> 試過每一個中心（奇數長度與偶數長度）；只要字元相同就往外擴。

<!--CODE-->

<!--CODE-->

<!-- 45e89a255577 -->
### 2-3) Longest Duplicate Substring（LC 1044） — 二分搜尋 + Rolling Hash
> 對長度做二分搜尋；用 Rabin-Karp rolling hash 檢查該長度的重複子字串是否存在。

<!--CODE-->

<!--CODE-->

<!-- 0efce9d07dd4 -->
#### 變形：LC 718 Maximum Length of Repeated Subarray — *一樣是二分搜尋 + hash，但跨**兩個**序列，而且處理的是整數不是字元*

> LC 1044 是在一個字串裡找重複；LC 718 是在兩個陣列之間找共同的區塊。
> 單調的判定條件完全一樣（「若存在長度 `L` 的共同區塊，那長度 `L-1` 的也存在」），
> 所以對 `L` 做二分搜尋，並把兩個陣列的每個視窗都 hash 起來：先把 `nums1` 的所有視窗雜湊值丟進集合，
> 再掃 `nums2` 看有沒有命中。值本來就是整數，直接餵進 rolling hash 即可 — 不需要 `ord()`。
>
> 這題面試上預期的答案是 O(N·M) 的 LCS 型 DP（見 `dp_pattern.md`）。當面試官追問能不能比 O(N·M) 更快時，
> 再把這個當成 follow-up 端出來 — 它是 O((N+M)·log min(N,M))。

<!--CODE-->

<!--CODE-->

**兩個陷阱**：(1) 要用**上中點** `(lo + hi + 1) // 2` 搭配 `lo = mid` / `hi = mid - 1` 的更新方式，
否則搜尋不會終止；(2) 只用一個 32-bit 左右的模數很容易碰撞 —
記得說出來你會在雜湊命中時比對實際視窗來驗證，或是改用下面的 `DoubleHash` 類別。

<!-- 5ecab1c9ff42 -->
## 進階技巧

<!-- 2c28a2c4c858 -->
### 多模式比對
<!--CODE-->

<!-- 192aab4214f4 -->
### 字串雜湊的最佳化
<!--CODE-->

<!-- fd4e4f0a995f -->
## 效能最佳化建議

<!-- 061164d6d9d3 -->
### 演算法選擇指南
<!--CODE-->

<!-- c6902d84d085 -->
### 這個進階演算法真的划算嗎？ ⭐⭐⭐⭐⭐

面試官其實很少*想*看到 Manacher 或後綴陣列。他們要的是直觀的解法、寫對寫乾淨，
再加一句「如果限制真的逼我，我會這樣把它壓下來」。先給預期的答案，再把進階版當 follow-up 提出來。

| 題目 | 面試官預期的解法 | 進階選項 | 值得換嗎？ |
|---------|------------------------------|-----------------|------------------|
| LC 28 Find First Occurrence | 滑動比對，O(N·M) | **KMP**，O(N+M) | **值得** — 這題**就是**在考 KMP；用暴力解會被讀成「不會」 |
| LC 5 Longest Palindromic Substring | 中心擴展，O(N²) | **Manacher**，O(N) | **不值得** — 只有被要求 O(N) 才寫 Manacher。中心擴展就是被接受的答案（見 `palindrome.md`） |
| LC 647 Palindromic Substrings | 中心擴展，O(N²) | Manacher，O(N) | **不值得** — 理由同上 |
| LC 686 Repeated String Match | `b in a*k` 加上界限論證 | KMP 搜尋 | **很少需要** — 重點是**界限的證明**，不是比對器 |
| LC 718 Max Length of Repeated Subarray | LCS 型 DP，O(N·M) | 二分搜尋 + rolling hash，O((N+M)·log N) | **只在 follow-up 時** — 先 DP，再提這個 |
| LC 1044 Longest Duplicate Substring | （沒有簡單解 — O(N²) 會 TLE） | **二分搜尋 + Rabin-Karp** | **值得** — 進階解是唯一過得了的解 |
| LC 1316 Distinct Echo Substrings | — | Rolling hash | **值得** — 雜湊本來就是這題預設的工具 |
| LC 214 Shortest Palindrome | 反轉 + 前綴檢查，O(N²) | 在 `s + '#' + rev(s)` 上跑 KMP 的 prefix function | **被要求 O(N) 時值得** — 見 `palindrome.md` |
| LC 10 / LC 44 Regex & Wildcard Matching | **二維 DP** — 不需要字串自動機 | 自己建 NFA/DFA | **不值得** — DP 就是答案；見 `dp_pattern.md`、`recursion_to_dp.md` |
| LC 65 Valid Number | 東拼西湊的 flag 大亂鬥 | **DFA 表**（模板 6） | **值得** — 表格版**更短**，而且可以證明它涵蓋了所有邊界情況 |

**經驗法則**：在下列情況才動用進階結構 — (a) 暴力解的複雜度在題目給的限制下真的會 TLE、
(b) 同一個字串要被查很多次，預處理攤提得下來，或 (c) 進階版其實**更容易**寫對（LC 65 的 DFA、LC 28 的 KMP）。
其他時候它就是個包袱 — 更多程式碼、更多 bug、更少時間講話。

<!-- 3c814dab5a92 -->
### 鄰近的模式各自住在哪裡

下面這些屬於其他 cheatsheet，在這裡只做交叉引用，讓這頁專心處理後綴結構、自動機、雜湊和線性時間比對：

- **LC 336 Palindrome Pairs** — 反轉單字的 trie／雜湊表拆分 → `trie.md`、`palindrome.md`
- **LC 208 / 211 / 212 / 472 / 648**（Trie、Add & Search Words、Word Search II、Concatenated Words、Replace Words）→ `trie.md`
- **LC 10 / 44**（Regular Expression & Wildcard Matching） — 兩個字串的布林 DP 表格 → `dp_pattern.md`、`recursion_to_dp.md`
- **LC 3 / 438**（Longest Substring Without Repeating Characters、Find All Anagrams） — 視窗 + 計數，不需要雜湊 → `sliding_window.md`、`string.md`
- **LC 8 / 12 / 13 / 43 / 68 / 273 / 443**（解析、格式化、大數字串） — 一般的字串操作 → `string.md`
- **LC 20 / 224 / 227 / 394 / 1249**（括號與運算式解析） — 堆疊機，不是字串演算法 → `stack.md`、`string.md`

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- 59f56fa99276 -->
### 演算法複雜度比較

| 演算法 | 時間複雜度 | 空間複雜度 | 最適合的場景 |
|-----------|-----------------|------------------|---------------|
| KMP | O(n + m) | O(m) | 單一模式搜尋 |
| Manacher | O(n) | O(n) | 所有回文 |
| Z-Algorithm | O(n) | O(n) | 模式比對的各種變形 |
| Rolling Hash | O(n) | O(1) 攤提 | 多次查詢 |
| 後綴陣列 | O(n log n) | O(n) | 複雜的字串操作 |

<!-- fd214e5c29fe -->
### 常見錯誤與建議

**🚫 常見錯誤：**
- 沒處理空字串或空模式
- 索引計算的差一錯誤
- 只用單一雜湊，碰撞問題沒解決
- failure function 算錯

**✅ 最佳實務：**
- 一定要先驗證輸入字串
- 用雙雜湊來抵抗碰撞
- 預期會多次查詢時就先預處理
- 依題目限制挑演算法
- 用邊界情況和長字串測過

<!-- 010334db2828 -->
### 面試建議
1. **先辨認核心需求**：是模式比對、回文，還是多次查詢
2. **挑對演算法**：依時間／空間限制決定
3. **處理邊界情況**：空字串、單一字元
4. **考慮預處理**：查詢次數多的時候
5. **實作要小心**：索引管理是關鍵
6. **測試要徹底**：各種長度與模式的字串都試過

這份進階字串演算法 cheatsheet 收錄了字串處理與模式比對上最講究的那些技巧。

<!-- 760c54ae042f -->
# 位元運算

> **範圍** — 位元層級的操作，以及建立在其上的各種技巧：遮罩、XOR 恆等式、最低位 1、子集列舉，還有 bitmask DP。
> **另見**：[bit_manipulation_examples.md](./bit_manipulation_examples.md) — 支撐這些技巧的十四道詳解題；[math.md](./math.md) — 不靠位元的數值操作；[combinatorics_math_patterns.md](./combinatorics_math_patterns.md) — 計數；[dp.md](./dp.md) — bitmask DP 所屬的完整 DP 目錄。

<!-- 5992a4abfe2f -->
## LeetCode 題目清單

- [Bit Manipulation](https://leetcode.com/problem-list/bit-manipulation/)
- [Bitmask](https://leetcode.com/problem-list/bitmask/)

<!-- 2316f11979bc -->
## 總覽

位元運算直接操作整數的**二進位表示**。因為每個運算都只是一道 CPU 指令，位元技巧能把許多
`O(n)` 的掃描變成 `O(1)` 的算術，也能讓一個小整數當成最多 32/64 個旗標的緊湊**集合**
（bitmask）。

<!-- f0caaa439244 -->
### 關鍵性質
- **時間複雜度**：每個位元運算 `O(1)`；整個字組的掃描是 `O(number of bits)` ≈ `O(32)`
- **空間複雜度**：`O(1)` — 一個遮罩重複使用同一個整數，取代陣列或集合
- **核心想法**：用 `&` `|` `^` `~` `<<` `>>` 讀取／翻轉個別位元；XOR 會讓成對的值互相抵消（`a ^ a = 0`）
- **什麼時候用**：配對／抵消類的題目、算 1 的個數、子集列舉（bitmask）、判斷 2 的次方、不用 `+` 做加法、把旗標打包進一個數字

<!-- 4e444dcfa6c1 -->
### 速查表 — 一定要背起來的技巧 ⭐⭐⭐⭐⭐

| 目標 | 表達式 |
| ---- | ---------- |
| 測試第 `i` 位是不是 1 | `(x >> i) & 1` |
| 把第 `i` 位設為 1 | `x \| (1 << i)` |
| 把第 `i` 位清成 0 | `x & ~(1 << i)` |
| 翻轉第 `i` 位 | `x ^ (1 << i)` |
| 取出最低位的 1 | `x & -x` |
| 清掉最低位的 1 | `x & (x - 1)` |
| 是不是 2 的次方？ | `x > 0 && (x & (x - 1)) == 0` |
| 是不是偶數？ | `(x & 1) == 0` |
| XOR 自我抵消 | `a ^ a = 0`, `a ^ 0 = a` |

<!-- b056f1d3238b -->
### 參考資料
- [LeetCode — Bit Manipulation card](https://leetcode.com/explore/learn/card/bit-manipulation/)
- [bit_manipulation.md](https://github.com/yennanliu/CS_basics/blob/master/doc/bit_manipulation.md)
- [leetcode-easy-bitwise-xor-summary](https://steveyang.blog/2022/07/02/leetcode-easy-bitwise-xor-summary/)

<!-- 506fdba8a820 -->
## 0) 概念
- 進位制
    - [Ref](https://leetcode.com/explore/learn/card/bit-manipulation/669/bit-manipulation-concepts/4494/)
    - 一個 X 進位數字的實際數值，由每一位的數字和它的位置共同決定。
    - 例子：
        - 123.45（10 進位）= 1 * 10^2 + 2 * 10^1 + 3 * 10^0 + 4 * 10^(-1) + 5 * 10^(-2)
        - 720.5（8 進位）= 7 * 8^2 + 2 * 8^1 + 0 * 8^0 + 5 * 8^(-1)
    - 在電腦科學裡最常用的是二進位，只有兩個數字：0 和 1。八進位（base-8）和十六進位（base 16）也很常用。八進位有八個數字：0、1、2、3、4、5、6、7。

- [bit VS byte VS char](http://web.ntnu.edu.tw/~algo/Bit.html)
    - 基礎
        - bit：二進位數字（以 2 為底：0, 1）
        - 十六進位數字：以 16 為底：0123456789abcdef（大小寫視為相同）
    - byte：8 bytes（字節）
    - char：16 bytes（字符）
    - 參考：
        - [java example](https://github.com/yennanliu/JavaHelloWorld/blob/main/src/main/java/Advances/IOFlow/demo1.java#L25)
- 參考
    - [bit_manipulation.md](https://github.com/yennanliu/CS_basics/blob/master/doc/bit_manipulation.md)
    - [python-operators.html](https://www.runoob.com/python/python-operators.html)
    - [leetcode-easy-bitwise-xor-summary](https://steveyang.blog/2022/07/02/leetcode-easy-bitwise-xor-summary/)

<p align="center"><img src="../pic/bit_basic1.png"></p>
<p align="center"><img src="../pic/bit_basic2.png"></p>

<!-- b6471f7cfe0c -->
## 1) 核心運算

<!-- 6baaf9dacf96 -->
### 1-1) 六個運算子

| 運算子 | 名稱 | 規則 | 範例（4-bit） |
| -- | ---- | ---- | --------------- |
| `&`  | AND | **兩邊都是** 1 才是 1 | `0110 & 1010 = 0010` |
| `\|`  | OR  | **任一邊是** 1 就是 1 | `0110 \| 1010 = 1110` |
| `^`  | XOR | 兩邊**不同**才是 1 | `0110 ^ 1010 = 1100` |
| `~`  | NOT | 每個位元都翻轉（`~x = -x - 1`） | `~0110 = ...1001` |
| `<<` | 左移 | 尾端補 `n` 個 0 → `x * 2^n` | `0011 << 1 = 0110` |
| `>>` | 右移 | 丟掉低位的 `n` 個位元 → `x // 2^n` | `0110 >> 1 = 0011` |

> **XOR 恆等式**（許多 LC 題目的核心）：`a ^ a = 0`、`a ^ 0 = a`，
> XOR 具**交換律與結合律** → 把整份清單 XOR 起來，出現偶數次的值全部抵消，
> 只剩下出現奇數次的那一個。

<!-- 4b88af175a8f -->
### 1-2) 單一位元的技巧（附程式碼）

<!--CODE-->

<!--CODE-->

<!-- 9f3ec976face -->
### 1-3) 計算 1 的個數（population count）

**核心想法**：`x & (x - 1)` 會清掉最低位的 1，所以迴圈**每個 1 只跑一次**
（Brian Kernighan 演算法）→ 是 `O(popcount)` 而不是 `O(32)`。

<!--CODE-->

<!--CODE-->

**視覺追蹤** — `count_bits(12)`，`12 = 1100`：

<!--CODE-->

<!-- dcb256aec0ef -->
### 1-4) 沿著位元「欄」做計數 — LC 461 / LC 477

**模式**：不要對數字兩兩配對跑迴圈，改成對 **32 個位元位置**跑迴圈，問每一欄各貢獻多少。
這能把很多看起來像 `O(n^2)` 的問題壓成 `O(32n)`。

**核心想法**：在位元位置 `i`，若有 `ones` 個數字在該位是 1、`n - ones` 個是 0，
那麼在這一位上**剛好有 `ones * (n - ones)` 對數字不同**。把 32 個位置加總即可。

<!--CODE-->

<!--CODE-->

**為什麼可行** — `[4, 14, 2]` = `00100, 01110, 00010`：

<!--CODE-->

<!-- 842e256c769e -->
### 1-5) 把 bitmask 當成**字元集合** — LC 318 ⭐⭐⭐⭐

**模式**：小寫字母的集合只需要 **26 個位元**，所以整個單字可以壓成一個 `int`。
接著所有集合問題都變成單一指令：

| 集合問題 | 位元表達式 |
| ------------ | -------------- |
| 兩個單字有共同字母嗎？ | `(maskA & maskB) != 0` |
| 兩者互斥嗎？ | `(maskA & maskB) == 0` |
| 字母的聯集 | `maskA \| maskB` |
| 有幾個相異字母？ | `Integer.bitCount(mask)` / `bin(mask).count("1")` |
| 單字裡有重複字母嗎？ | 建構過程中檢查：`(mask & bit) != 0` |

這把每一對都要花 `O(len)` 的字串比較，換成 `O(1)` 的 AND。

<!--CODE-->

<!--CODE-->

<!-- 67b2f63ca715 -->
#### **變形 A — 逐步累積互斥遮罩的聯集（LC 1239）**

*變化點*：不是只挑**兩個**互斥的單字，而是貪婪地把**所有**可達的聯集都長出來。
維護一份可達遮罩清單；一個單字只有在 `cur & m == 0` 時才能加進某個遮罩。

<!--CODE-->

<!--CODE-->

<!-- d64599cdae95 -->
#### **變形 B — 把固定寬度的符號打包成滾動的 int key（LC 187）**

*變化點*：字母表只有 4 個符號（`A C G T`），所以每個字元只需要 **2 個位元**，
10 個字元的視窗就是一個 20 位元的整數。用 `hash = ((hash << 2) | code) & mask` 滑動視窗
— 這是 `O(1)` 的滾動 key，不必每一步都去雜湊一段 10 字元的子字串。

<!--CODE-->

<!--CODE-->

> **更多字母遮罩的練習**（同樣的 26 位元編碼，沒有新技術）：
> LC 1255（Maximum Score Words Formed by Letters）、LC 2135（Count Words Obtained After
> Adding a Letter）、LC 1684（Count the Number of Consistent Strings — `word & ~allowed == 0`）。

<!-- eef797ad92f1 -->
## 3) Bitmask DP

**bitmask** 讓一個整數代表**一組已走訪／已選取的項目**（第 `i` 位是 1 ⇔ 項目 `i` 在集合裡）。
當 DP 狀態需要記錄「我用掉了 ≤ 約 20 個項目中的哪個子集」時，遮罩**本身就是**狀態 —
這讓指數級的子集問題能在 `O(2^n · n)` 內跑完。

<!-- 96a0803ef26f -->
### 3-1) 子集列舉（LC 78 回顧）

讓 `mask` 從 `0` 跑到 `2^n − 1`，就會**不重不漏**地走過每一個子集；用位元測試挑出成員
（見 [2-12](./bit_manipulation_examples.md#12-subsets--lc-78--the-bitmask-enumeration-)）。幾個好用的遮罩慣用寫法：

<!--CODE-->

<!-- 9256e44f0c55 -->
### 3-2) TSP 型的 bitmask DP（Held–Karp）

**旅行推銷員**家族是 bitmask DP 的代表題：`dp[mask][i]` = 一條**恰好走訪 `mask` 中所有城市**
且目前停在城市 `i` 的路徑的最小成本。

<!--CODE-->

<!--CODE-->

> **什麼時候該想到 bitmask DP**：`n` 很小（≤ 約 20，讓 `2^n` 還算得動），而且狀態是
> 「我用過／走訪過哪個子集」。相關 LC：847（Shortest Path Visiting All Nodes）、
> 1349（Maximum Students Taking Exam）、691（Stickers to Spell Word）、526（Beautiful Arrangement）。

<!-- 4cae482389d2 -->
### 3-3) 「一次填滿一個桶」的 bitmask DP — LC 698 ⭐⭐⭐⭐⭐

**模式**：切成 `k` 個相等群組的題目，看起來需要 `k` 層巢狀搜尋。
訣竅是**不要再去追蹤你正在填哪一個桶**，只追蹤：

<!--CODE-->

**核心想法**：`% target` 正是讓「開始下一個桶」這個轉移完全免費的關鍵 — 不需要為桶的計數器多開一個狀態維度。

**真正有用的剪枝**：把 `nums` 由小到大排序，然後一旦 `dp[mask] + nums[i] > target` 就
`break`（不是 `continue`）— 後面的每個元素都更大，一樣會失敗。

<!--CODE-->

<!--CODE-->

**視覺追蹤** — `nums = [1,2,2,3]`（已排序），`k = 2`，`target = 4`。
第 `i` 位 = `nums[i]` 已被使用；這裡只畫出成功的那條路徑（迴圈其實也會填其他遮罩）：

<!--CODE-->

<!-- 28d8287fdb9d -->
#### **變形 — 同一個模板，`k` 寫死（LC 473）**

*變化點*：LC 473（Matchsticks to Square）**就是** `k = 4` 的 LC 698，其他完全一樣。

<!-- 3c78f20027b1 -->
#### **變形 — 遮罩當成遊戲狀態，而不是 DP 表（LC 464）**

*變化點*：在 LC 464（Can I Win）裡，遮罩代表「`1..maxChoosable` 中哪些數字已被拿走」，
遞迴是 minimax 而不是求成本：`win(mask)` 為 `true` 的條件是**存在**某個沒用過的 `i`，
它要嘛立刻達到總和，要嘛讓對手落入必敗狀態 `!win(mask | (1 << (i-1)))`。
只用 `mask` 做記憶化 — 剩下的總和已經被它隱含決定了。
先用 `maxChoosable * (maxChoosable + 1) / 2 < desiredTotal` 剪枝 → 誰都贏不了。

> **更多 bitmask DP 練習**：LC 1125（Smallest Sufficient Team — 集合覆蓋，`dp[skillMask]`）、
> LC 980（Unique Paths III — 已走訪格子的遮罩）、LC 864（Shortest Path to Get All Keys —
> BFS 狀態 = `(cell, keyMask)`）、LC 1494（Parallel Courses II — 用 `sub = (sub - 1) & mask`
> 列舉目前可修課程集合的**子遮罩**）。

> **不是 bitmask DP，但和位元相鄰**：LC 421（Maximum XOR of Two Numbers in an Array）和
> LC 1707（Maximum XOR With an Element From Array）是用**二元／XOR 字典樹（Trie）**解的 —
> 請看 `trie.md`，這裡不重複。

<!-- a6e5f1afdf84 -->
## 範例詳解

十四道題目放在 **[bit_manipulation_examples.md](./bit_manipulation_examples.md)**，
依照它們倚賴位元運算子的哪個性質分組：

| 分組 | 用到的性質 | 題目 |
|---|---|---|
| [XOR — 抵消成對元素](./bit_manipulation_examples.md#xor--cancelling-pairs) | `x ^ x == 0`，所以任何成對的東西都會消失 | LC 136, 137, 260, 268 |
| [計數與轉換位元](./bit_manipulation_examples.md#counting--transforming-bits) | `x & (x-1)` 清掉最低位的 1 | LC 191, 338, 190, 231 |
| [不用算術做算術](./bit_manipulation_examples.md#arithmetic-without-arithmetic) | XOR 是不帶進位的加法；AND 找出進位 | LC 371, 67, 29 |
| [列舉與建構](./bit_manipulation_examples.md#enumerating-and-constructing-with-bits) | 一個整數*就是*一個子集，往上數就能走遍所有子集 | LC 78, 89, 201 |

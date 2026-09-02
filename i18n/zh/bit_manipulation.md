<!-- a09d9cef8fd7 -->
# 位元運算

> **範圍** — 整數在位元層次上是怎麼被表示的（二補數、固定位寬、位移），以及建立在其上的各種運算與技巧：遮罩、XOR 恆等式、最低位 1、子集列舉，還有 bitmask DP。
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

<!-- 5c6cc3d4230c -->
### 參考資料
- [LeetCode — Bit Manipulation card](https://leetcode.com/explore/learn/card/bit-manipulation/)
- [bit_manipulation.md](https://github.com/yennanliu/CS_basics/blob/master/doc/bit_manipulation.md)
- [leetcode-easy-bitwise-xor-summary](https://steveyang.blog/2022/07/02/leetcode-easy-bitwise-xor-summary/)
- [bit VS byte VS char](http://web.ntnu.edu.tw/~algo/Bit.html) — 各種位寬，附 Java 範例
- [Python operators](https://www.runoob.com/python/python-operators.html) — 運算子優先順序表

<!-- f602e4415587 -->
## 0) 電腦科學基礎 ⭐⭐⭐⭐⭐

幾乎每一個看起來「很巧妙」的位元技巧，都只是**機器如何儲存整數**的直接後果。
五個事實就講完了 — 把它們學起來，下面的技巧就不再需要硬背。

| # | 事實 | 它解釋了什麼 |
| - | ---- | ------------ |
| 1 | 一個數字是「數字 × **位值**」，而二進位的位值就是 2 的次方 | 用手讀寫二進位／十六進位 |
| 2 | `int` 是一個**固定寬度的 32 位元盒子**，滿了會繞回來 | 溢位、`MIN_VALUE`、為什麼需要遮罩 |
| 3 | 負數是用**二補數**存的 | `~x = -x-1`、`x & -x`、負數的 `>>` |
| 4 | 位移**就是**乘以／除以 2 的次方 | `<<`、`>>`、`>>>` 和它們的邊界狀況 |
| 5 | 一個 int 的各個位元**就是** 32 個項目的子集 | bitmask、子集列舉、bitmask DP |

<!-- 15d5b90c8918 -->
### 0-1) 位值、二進位與十六進位

一個 `X` 進位數字的實際數值，由每一位的數字**和它的位置**共同決定
（[參考](https://leetcode.com/explore/learn/card/bit-manipulation/669/bit-manipulation-concepts/4494/)）：

<!--CODE-->

要**讀**二進位，就把帶著 1 的那些位值加起來；要**寫**二進位，就一直除以 2、由下往上記下餘數。
整個轉換就只有這樣。

**十六進位就是四個一組的二進位。** 一個十六進位數字 = 4 個位元（一個 *nibble*），
所以一個 32 位元的 `int` 剛好是 8 個十六進位數字 — 這也是遮罩都寫成這種形式的原因：

<!--CODE-->

2 的次方值得背熟 — 它們同時會以「題目限制」和「遮罩」兩種身分出現：

| `n`   | 4  | 8   | 10   | 16    | 20    | 31            | 32            |
| ----- | -- | --- | ---- | ----- | ----- | ------------- | ------------- |
| `2^n` | 16 | 256 | 1024 | 65536 | 約一百萬 | 2 147 483 648 | 4 294 967 296 |

<p align="center"><img src="../pic/bit_basic1.png"></p>
<p align="center"><img src="../pic/bit_basic2.png"></p>

<!-- 98787204bec0 -->
### 0-2) 固定寬度 — `int` 是一個 32 位元的盒子

| Java 型別 | 寬度 | 範圍 |
| --------- | ---- | ---- |
| `byte`    | 8 **bits** | `-128 … 127` |
| `char`    | 16 **bits** | `0 … 65535`（無號） |
| `int`     | 32 bits | `-2^31 … 2^31 - 1` |
| `long`    | 64 bits | `-2^63 … 2^63 - 1` |

- 第 `i` 位帶著位值 `2^i`。在 `int` 裡，**第 31 位是符號位**，所以真正能用的大小只有 31 位 —
  這就是為什麼那麼多解法都在跑 `for i in 0..31`。
- 任何超出盒子的運算都會**無聲地繞回來**：`Integer.MAX_VALUE + 1 == Integer.MIN_VALUE`。
  題目寫「假設結果可以用 32 位元整數表示」時，其實就是在告訴你這正是它要考的邊界狀況。
- `1 << 31` 已經是**負數**了。想要 `2^31` 這個*數值*時，要寫 `1L << 31`。

<!-- ef3856b1a806 -->
### 0-3) 二補數 — 負數是怎麼存的 ⭐⭐⭐⭐⭐

**規則**：`-x` 是以 `~x + 1` 的形式儲存的 — *每個位元都翻轉，然後加一*。
等價地說，`-x` 的位元樣式就是無號數 `2^32 - x`。

<!--CODE-->

為什麼用這種表示法，而不是「符號位 + 大小」？因為這樣**一個加法器就能處理兩種符號** —
不管運算元是正是負，`a + b` 都走同一組電路，而且零只有一種寫法。

三個直接推論：

| 事實 | 會在哪裡遇到 |
| ---- | ------------ |
| `~x == -x - 1` | 在沒有無號型別的語言裡改寫 `~` |
| 最高位是 1 ⇔ 負數 | `for i in 0..31` 那種逐位掃描的迴圈 |
| `x >> 31` 不是 `0`（非負）就是 `-1`（負） | 無分支的 `abs`、取出符號 |

**會咬人的不對稱**：範圍裡的負數比正數多一個，所以 `Integer.MIN_VALUE` **沒有對應的正數** —
`-Integer.MIN_VALUE` 和 `Math.abs(Integer.MIN_VALUE)` 算完都還是 `Integer.MIN_VALUE`。
這一個值正是 LC 29（Divide Two Integers）藏起來的測資；在取負號之前就要先處理掉它。

<!-- fdfad9acb40a -->
### 0-4) 為什麼 `x & (x - 1)` 和 `x & -x` 有效

兩者都是從二補數直接推出來的。推導過一次，就再也不用去背哪個是哪個：

<!--CODE-->

所以 `x & (x - 1)` 的意思是**「丟掉最低位的 1」**（把它包成迴圈 → Brian Kernighan 的
popcount，見 [§1-3](#1-3-counting-set-bits-population-count)）；`x & -x` 的意思是
**「只留下最低位的 1」**（這也是 Fenwick tree 的前進規則 — 見
[binary_indexed_tree.md](./binary_indexed_tree.md)）。

<!-- dc4341d77bf4 -->
### 0-5) 位移：左移、算術右移、邏輯右移

| 運算子 | 名稱 | 補進來的是 | 效果 |
| ------ | ---- | ---------- | ---- |
| `x << n`  | 左移 | 右邊補 0 | `x * 2^n`；被擠出最高位的位元會**消失** |
| `x >> n`  | **算術**右移 | 複製**符號位** | `floor(x / 2^n)` |
| `x >>> n` | **邏輯**右移（只有 Java 有） | 補 0 | 把整個位元樣式當成無號數 |

<!--CODE-->

**什麼時候需要 `>>>`**：任何要走完一個可能為負的 `int` 全部 32 個位元的迴圈 —
LC 190（Reverse Bits）、LC 191（Number of 1 Bits）、LC 338。
用 `>>` 的話，負數會永無止盡地補進 1，`while (x != 0)` 永遠不會結束。

另外兩條會讓人意外的規則（上面都驗證過了）：

- **Java 會把位移量取低 5 位**：`1 << 32` 等同 `1 << 0`，也就是 `1` — **不是** `0`。
  要嘛改移 `long`（取低 6 位），要嘛把位移拆成兩次。
- **`+` 比 `<<` 更緊**：`x << 1 + 2` 其實是 `x << 3`。見 [§0-7](#0-7-precedence--parenthesise-everything-)。

<!-- 94b3c5b40b57 -->
### 0-6) 在這件事上 Python 不是 Java ⭐⭐⭐⭐⭐

Python 的整數是**任意精度**的，行為上就像有無限多個符號位。
沒有盒子，所以沒有東西會溢位 — 也沒有 `>>>`，因為根本沒有一個「最高位」可以停。

| | Java（`int`，32 位元） | Python（無上限） |
| --- | --- | --- |
| `1 << 31` | `-2147483648`（撞到符號位了） | `2147483648` |
| `Integer.MAX_VALUE + 1` | 繞回 `MIN_VALUE` | 就繼續變大 |
| `-1 >> 100` | `-1` | `-1`（無限多個符號位） |
| 邏輯右移 | `x >>> n` | **沒有** — 要自己用遮罩模擬 |
| `~5` | `-6` | `-6`（一樣） |

所以一段倚賴 32 位元繞回行為的 Python 迴圈，必須**自己把盒子做出來**：

<!--CODE-->

這正是為什麼 LC 371（Sum of Two Integers）在 Python 看起來比 Java 醜那麼多：
進位迴圈其實是同樣的三行，但每一步都得 `& MASK`，最後的結果還得用 `to_signed` 轉回來。

> **經驗法則**：在 Python 裡，只要 `x` 可能為負，就用 `for i in range(32): (x >> i) & 1`
> 逐位掃描，而不要用 `while x:` — 那個 `while` 不會停。

<!-- 5bba21700ebf -->
### 0-7) 優先順序 — 全部加上括號 ⭐⭐⭐⭐

由緊到鬆 — 這條鏈在 C、Java、Python 裡都一樣：

<!--CODE-->

這個順序會造成兩種出錯：

<!--CODE-->

**比較運算子則是三個語言唯一不一致的地方**：C 和 Java 把 `==` 塞在 `>>` 和 `&` *中間*，
Python 卻把它放在 `|` *下面*。所以 `x & 1 == 0` 在三個語言裡是三件不同的事：

| 語言 | `x & 1 == 0` 實際被解讀成 | 結果 |
| ---- | ------------------------- | ---- |
| C / C++  | `x & (1 == 0)` → `x & 0` | **無聲地永遠是 0** |
| Java     | `x & (1 == 0)` → `int & boolean` | 編譯錯誤（`bad operand types`） |
| Python   | `(x & 1) == 0` | 正確 — 在 Python 裡比較運算子綁得比 `&` *更鬆* |

不要去賭你現在在哪個語言裡。**就寫 `(x & 1) == 0`。**

<!-- 6d4cb26c3e30 -->
### 0-8) 一個 bitmask **就是**一個集合

最後一塊基礎：`n` 個項目的所有子集，和整數 `0 … 2^n - 1` 是**一對一對應**的。
看懂這件事之後，「bitmask」就不需要再多解釋了 — 每一個集合操作都只是一道指令。

| 集合語言 | 位元語言 |
| -------- | -------- |
| `S = {}` / `S = {0..n-1}` | `0` / `(1 << n) - 1` |
| `i ∈ S` | `(mask >> i) & 1` |
| `S ∪ {i}` / `S \ {i}` / 翻轉 `i` | `mask \| (1<<i)` / `mask & ~(1<<i)` / `mask ^ (1<<i)` |
| `A ∪ B` / `A ∩ B` / `A \ B` | `a \| b` / `a & b` / `a & ~b` |
| `A ⊆ B` | `(a & b) == a` |
| `A ∩ B = ∅` | `(a & b) == 0` |
| `\|S\|` | `Integer.bitCount(mask)` / `bin(mask).count("1")` |
| 在 `n` 個項目內取補集 | `mask ^ ((1 << n) - 1)` |

**兩個計數事實**能告訴你 bitmask 解法塞不塞得進題目的限制：

- 子集有 `2^n` 個，所以 `O(2^n · n)` 的 DP 需要 `n ≤ 約 20`（`2^20 ≈ 10^6`）；
- 把*所有*遮罩加總起來，**子**遮罩的總數是 `3^n` 而不是 `4^n` — 這正是
  [§2-1](#2-1-subset-enumeration-lc-78-recap) 裡 `sub = (sub - 1) & mask` 迴圈跑得動的原因。

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

<!-- e9fad9465afe -->
### 1-3) 計算 1 的個數（population count）

**核心想法**：`x & (x - 1)` 會清掉最低位的 1（原因見 [§0-4](#0-4-why-x--x---1-and-x---x-work)），
所以迴圈**每個 1 只跑一次**（Brian Kernighan 演算法）→ 是 `O(popcount)` 而不是 `O(32)`。

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

<!-- ac01a9681408 -->
## 2) Bitmask DP

**bitmask** 讓一個整數代表**一組已走訪／已選取的項目**（第 `i` 位是 1 ⇔ 項目 `i` 在集合裡）。
當 DP 狀態需要記錄「我用掉了 ≤ 約 20 個項目中的哪個子集」時，遮罩**本身就是**狀態 —
這讓指數級的子集問題能在 `O(2^n · n)` 內跑完。

<!-- c9648be0b509 -->
### 2-1) 子集列舉（LC 78 回顧）

讓 `mask` 從 `0` 跑到 `2^n − 1`，就會**不重不漏**地走過每一個子集；用位元測試挑出成員
（見[詳解題第 12 節](./bit_manipulation_examples.md#12-subsets--lc-78--the-bitmask-enumeration-)）。幾個好用的遮罩慣用寫法：

<!--CODE-->

<!-- 536bc2dfcd1d -->
### 2-2) TSP 型的 bitmask DP（Held–Karp）

**旅行推銷員**家族是 bitmask DP 的代表題：`dp[mask][i]` = 一條**恰好走訪 `mask` 中所有城市**
且目前停在城市 `i` 的路徑的最小成本。

<!--CODE-->

<!--CODE-->

> **什麼時候該想到 bitmask DP**：`n` 很小（≤ 約 20，讓 `2^n` 還算得動），而且狀態是
> 「我用過／走訪過哪個子集」。相關 LC：847（Shortest Path Visiting All Nodes）、
> 1349（Maximum Students Taking Exam）、691（Stickers to Spell Word）、526（Beautiful Arrangement）。

<!-- 31cb0d39f80c -->
### 2-3) 「一次填滿一個桶」的 bitmask DP — LC 698 ⭐⭐⭐⭐⭐

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

<!-- cf146c401303 -->
## 總結

<!-- b40a11452f76 -->
### 從題目敘述挑出該用的技巧

| 題目說… | 就用 | 章節 |
| ------- | ---- | ---- |
| 「每個元素都出現兩次，只有一個例外」 | 把整個陣列 XOR 起來 | [XOR — 抵消成對元素](./bit_manipulation_examples.md#xor--cancelling-pairs) |
| 「數 1 的個數」／「對 `0..n` 的每個 `i`」 | `x & (x-1)` 迴圈，或對 `i >> 1` 做 DP | [§1-3](#1-3-counting-set-bits-population-count) |
| 對某個位元性質「所有配對加總」 | 跑 32 個**位元欄**，不要跑配對 | [§1-4](#1-4-counting-over-bit-columns--lc-461--lc-477) |
| 小寫單字、「有共同字母」／「字元不重複」 | 26 位元的字母遮罩 | [§1-5](#1-5-a-bitmask-as-a-character-set--lc-318-) |
| 固定的小字母表 + 滑動視窗 | 每個符號打包 `k` 位元，用 `<<` 加遮罩滾動 | [變形 B](#variation-b--pack-fixed-width-symbols-into-a-rolling-int-key-lc-187) |
| 「選一個子集」，`n ≤ 約 20` | `dp[mask]`，bitmask DP | [§2](#2-bitmask-dp) |
| 「切成 `k` 個總和相等的群組」 | `dp[mask]` = 目前桶的填充量，配 `% target` | [§2-3](#2-3-fill-buckets-one-at-a-time-bitmask-dp--lc-698-) |
| 「不用 `+` 或 `/` 做加法／除法」 | XOR 是和，AND 是進位 | [不用算術做算術](./bit_manipulation_examples.md#arithmetic-without-arithmetic) |
| 「兩個數字的最大 XOR」 | 二元字典樹 — 見 [trie.md](./trie.md) | — |

<!-- 2d37034aeb5b -->
### 讓位元運算題掛掉的五個 bug

1. **對負的 `int` 跑 `while (x != 0) x >>= 1`** — 算術右移會一直補 1 進來。
   Java 要用 `>>>`，Python 要改成 `for i in range(32)`。（[§0-5](#0-5-shifts-left-arithmetic-right-and-logical-right)、[§0-6](#0-6-python-is-not-java-here-)）
2. **少了括號** — 要寫 `(x & 1) == 0`，絕不要寫 `x & 1 == 0`。（[§0-7](#0-7-precedence--parenthesise-everything-)）
3. **`Integer.MIN_VALUE` 沒有對應的正數** — `Math.abs` 和一元 `-` 算完都還是它本身，
   所以「先取負再相除」會無聲地壞掉。（[§0-3](#0-3-twos-complement--how-negatives-are-stored-)）
4. **`1 << i` 在 `i >= 31` 時溢位** — 要用 `1L << i`（也別忘了 Java 會把位移量取低 5 位，
   所以 `1 << 32 == 1`）。（[§0-2](#0-2-fixed-width--an-int-is-a-32-bit-box)、[§0-5](#0-5-shifts-left-arithmetic-right-and-logical-right)）
5. **把 32 位元的迴圈原封不動搬到 Python** — Python 永遠不會溢位，所以每一步都要
   `& 0xFFFFFFFF`，最後還要把結果轉回有號數。（[§0-6](#0-6-python-is-not-java-here-)）

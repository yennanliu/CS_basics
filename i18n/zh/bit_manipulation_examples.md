<!-- e39ce9db3234 -->
# Bit Manipulation — 實戰題解

> **範圍** — [bit_manipulation.md](./bit_manipulation.md) 背後的題解檔案庫：十四題，依照它們吃到位元運算子的哪個性質分組 —— XOR 把成對元素抵銷、清掉最低的 set bit、不用算術做算術，或是用一個整數代表一個子集合。
> **另見**：[bit_manipulation.md](./bit_manipulation.md) — 母表：運算子、單一位元的小技巧、按位元欄位計數、把 bitmask 當字元集合用，以及 bitmask DP；[dp_bitmask.md](./dp_bitmask.md) — 子集合 DP 本身；[math.md](./math.md) — 這些題目刻意避開的算術。

<!-- 2c4ef99078e0 -->
## LeetCode 題目清單

- [Bit Manipulation](https://leetcode.com/problem-list/bit-manipulation/)

<!-- dfb8df367a25 -->
## 總覽

這裡是 [bit_manipulation.md](./bit_manipulation.md) 的長尾。原本那份檔案有 88% 都是範例尾巴 —— 在 Tier 3 裡僅次於 `binary_indexed_tree`。母表留下運算子和技巧；這份檔案留下*套用*它們的題目。

<!-- d11445d9c622 -->
### 關鍵性質
- **複雜度**：除非某個解法另有說明，否則每個數字 O(1)、掃一整個陣列 O(32n) —— 這正是要動用位元運算的理由
- **核心想法**：四個性質幾乎包辦了所有事情，下面的分組就是那四個
- **什麼時候用**：限制是 O(1) 空間、不准用算術運算子，或集合小到塞得進一個 `int` 的時候

<!-- d792bd574bf1 -->
## XOR —— 把成對元素抵銷

<!-- 37530f2a6484 -->
### 1) Single Number — LC 136 ⭐⭐⭐⭐⭐


**核心想法**：把每個元素 XOR 起來。成對的會互相抵銷（`a ^ a = 0`），剩下的就是那個落單的數字。

<!--CODE-->

<!--CODE-->

<!-- 456b44de6811 -->
### 2) Single Number II — LC 137 —— 對位元計數再取 mod 3


每個元素都出現 **3 次**，只有一個例外。單純 XOR 沒用（它只抵銷成對的）。改用**位元計數 mod 3**：對 32 個位元位置各自統計所有數字在該位上有幾個 1，`count % 3` 就是那個唯一數字在該位的值。

<!--CODE-->

<!--CODE-->

<!-- c7dc5f88e466 -->
### 3) Single Number III — LC 260 —— 用最低的相異位元切開


有兩個數字只出現一次，其餘成對。全部 XOR 起來 = `a ^ b`（那兩個落單的）。用 `xor & -xor` 抓出**任一個**相異位元，再依這個位元把所有數字分成兩組 —— 兩個落單的會各自落在不同組，組內 XOR 就把它還原出來。

<!--CODE-->

<!--CODE-->

<!-- 0d4efdb6fa60 -->
### 4) Missing Number — LC 268


`nums` 裝了 `[0, n]` 裡 `n` 個相異的值，少了一個。把索引 `0..n` 和所有值全部 XOR 起來 —— 每個出現過的數字都會和自己的索引抵銷，剩下的就是缺的那個。

<!--CODE-->

<!--CODE-->

<!-- 2994007b4d07 -->
## 計數與轉換位元

<!-- 1b0251f30bbb -->
### 5) Number of 1 Bits — LC 191


**核心想法**：`n & (n - 1)` 會清掉最低的 set bit —— 迴圈跑幾次就等於有幾個 set bit。

<!--CODE-->

<!--CODE-->

<!-- fa2ae1796f79 -->
### 6) Counting Bits — LC 338 —— 在 `i >> 1` 上做 DP


對 `[0, n]` 裡每個 `i` 回傳 `popcount(i)`。**在位元上做 DP**：`dp[i] = dp[i >> 1] + (i & 1)` —— `i` 的 set bit 就是 `i/2` 的那些，再加上自己的最低位。整體 `O(n)`。

<!--CODE-->

<!--CODE-->

<!-- db7ab2cc6ab9 -->
### 8) Power of Two — LC 231 —— `n & (n - 1) == 0`

<!--CODE-->

<!--CODE-->

<!-- 84f170813018 -->
## 不用算術做算術

<!-- 79dd739428de -->
### 9) Sum of Two Integers — LC 371 —— 進位靠 AND、加總靠 XOR ⭐⭐⭐⭐

<!--CODE-->

<!--CODE-->

<!-- ebcdaa729ad1 -->
### 11) Divide Two Integers — LC 29 —— 移位相減 ⭐⭐⭐⭐⭐


**模式**：*二進位版的移位相減長除法。*一次減一個 `divisor` 是 `O(quotient)`，會 TLE。改成從高位到低位，對每個位移量 `shift` 問一句「`divisor << shift` 還塞得進剩下的被除數嗎？」—— 塞得下就減掉，並把商的第 `shift` 位設為 1。這就是小學長除法的二進位版，所以只要 **32 步**。

**核心想法**：測試條件寫成 `(a >> shift) >= b`，而不是 `(b << shift) <= a` —— 右移的寫法永遠不會溢位。

**溢位陷阱**：`Integer.MIN_VALUE / -1` = `2^31`，塞不進 `int` → 題目要求把它夾到 `Integer.MAX_VALUE`。在 Java 取絕對值後要用 `long` 運算，因為 `Math.abs(Integer.MIN_VALUE)` 仍然是負的。

<!--CODE-->

<!--CODE-->

**視覺追蹤** —— `divide(10, 3)`：

<!--CODE-->

> **相關**：LC 371（Sum of Two Integers）用 XOR／進位迴圈對 `+` 做了同一套「不用運算子做算術」的想法 —— 見 [2-5](#9-sum-of-two-integers--lc-371--carry-via-and-sum-via-xor-) 以及 `add_x_sum.md` 裡的 XOR-carry 模板。

<!-- bb416f1ca7b1 -->
## 用位元列舉與建構

<!-- 9cc3f3e2a667 -->
### 12) Subsets — LC 78 —— bitmask 列舉法 ⭐⭐⭐⭐


`n` 個元素的陣列，每個子集合都對應到 `[0, 2^n)` 裡的一個 `n` 位元數字：第 `i` 位是 1 ⇔ 選入 `nums[i]`。列舉 mask 是回溯之外的迭代式做法。

<!--CODE-->

<!--CODE-->

<!-- 03b7ea46a755 -->
### 14) Bitwise AND of Numbers Range — LC 201 —— 共同前綴


`[left, right]` 裡所有數字的 AND = 它們的**二進位共同前綴**後面補上 0（任何相異的低位，在區間中某處一定會變成 0）。把左右兩端一起右移到相等為止，數移了幾次，再把共同前綴移回去。

<!--CODE-->

<!--CODE-->

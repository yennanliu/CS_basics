<!-- d69bb892545e -->
# 把 `xxx` 加起來

> **範圍** — 逐位數相加，涵蓋面試官會丟給你的四種輸入形式：字串、整數、陣列、鏈結串列。
> **另見**：[math.md](./math.md) — 更廣泛的數值操作；[bit_manipulation.md](./bit_manipulation.md) — 不用 `+` 做加法；[linked_list.md](./linked_list.md) — 串列節點版本（LC 2、445）；[string.md](./string.md) — 字串組裝。

- https://leetcode.com/problems/add-strings/solution/

`Facebook` 面試官很愛這題，而且主要會出四種變形。演算法怎麼選，取決於輸入格式：

1. 字串（也就是本題）。用小學那套逐位數相加。注意：在字串不可變的語言（例如 Java 和 Python）裡，做不到常數空間。兩個例子：
    - LC 067 : Add Binary：兩個二進位字串相加。
    - LC 415 : Add Strings：兩個以字串表示的非負整數相加，不能直接轉成整數。

2. 整數。面試官通常會要求你不用 + 和 - 運算子做加法。這時用位元運算。例子：
    - LC 371 : Sum of Two Integers：不用 + 和 - 把兩個整數相加。

3. 陣列。一樣是課本上的加法。例子：
    - LC 989 : Add to Array Form of Integer。

4. 鏈結串列。哨兵頭節點 + 課本加法。幾個例子：
    - LC 66 : Plus One。
    - LC 002 : Add Two Numbers。
    - LC 445 : Add Two Numbers II。

<!-- 4b6c5e15de3e -->
## LeetCode 題目清單

- [Math](https://leetcode.com/problem-list/math/)
- [String](https://leetcode.com/problem-list/string/)
- [Bit Manipulation](https://leetcode.com/problem-list/bit-manipulation/)

<!-- 5fa13fe640ce -->
## 2) LC 範例

<!-- 98f31e746e9d -->
### 2-1) Add Binary — LC 67
<!--CODE-->

<!-- c525f5d1682a -->
### 2-2) Add Strings — LC 415

<!--CODE-->


<!--CODE-->

<!-- 58dd8016f464 -->
### 2-3) Sum of Two Integers — LC 371
<!--CODE-->

<!-- 553e06461bc1 -->
### 2-4) Add to Array Form of Integer — LC 989
<!--CODE-->

<!-- 68f4dd654860 -->
### 2-5) Plus One — LC 66
<!--CODE-->

<!-- bdcebf45a24b -->
### 2-6) Add Two Numbers — LC 2
<!--CODE-->

<!-- 495474ab9f2b -->
### 2-7) Add Two Numbers II — LC 445
<!--CODE-->

<!-- 001cd34fbf72 -->
## 3) 更多模板

<!-- 8c62a3652920 -->
### 快速決策表

| 目標 | 模板 | LC |
|------|----------|-----|
| **不用** `+` / `-` 把兩數相加 | XOR（和）+ AND<<1（進位）迴圈 | 371 |
| 兩個數字字串**相乘** | 位數格子 + `i+j` / `i+j+1` 索引規則 | 43 |
| 在**任意進位 k** 下相加／解析 | 同一套進位迴圈，把 `10` 換成 `k` | 67 (k=2), 415 (k=10) |
| **解析** *雙射*進位制（沒有 `0` 這個位數） | `res = res*k + digit`，digit 從 1 開始算 | 171 (bijective k=26) |
| **安全地**逐位數組出一個整數 | `res = res*10 + d`，加上乘之前的溢位防護 | 7 |

<!-- 73817e1ae1ae -->
### 3-1) 不用 `+` / `-` 相加兩整數 — LC 371 ⭐⭐⭐⭐⭐

**核心想法**：把二進位加法拆成兩個互不相干的部分。
- `a ^ b` = **忽略**所有進位之後的和
- `(a & b) << 1` = **只有**進位的部分（兩個位元都是 1 才產生進位，而且會落在左邊一位）

一直迴圈到進位變成 0 為止。這就是課本的進位流程，只是所有位元同時平行做完。

**視覺追蹤**（`a = 3 (011)`、`b = 5 (101)`）：

<!--CODE-->

<!--CODE-->

<!--CODE-->

> **變形 — 減法**：`a - b` 就是 `getSum(a, ~b + 1)`（二補數取負）。

<!-- 8a5bdf07a68e -->
### 3-2) Multiply Strings — LC 43 ⭐⭐⭐⭐

LC 415（Add Strings）很自然的下一題：一樣不准轉成大整數，但改成乘法。

**核心想法**：`num1[i] * num2[j]` 一定落在結果陣列的**剛好兩個**格子上：
- `pos[i + j + 1]` -> 個位數
- `pos[i + j]`     -> 進位

所以結果長度最多是 `m + n`。邊累加進 `pos` 邊做正規化，最後把開頭的 0 去掉。

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- 2a52e613c0d6 -->
### 3-3) 在任意進位 k 下相加／解析

LC 67（2 進位）和 LC 415（10 進位）的迴圈其實是**同一套**模板 — 差別只在取模的數字。寫成通用版本，之後都能重用：

<!--CODE-->

<!--CODE-->

<!-- 2d64e02697ca -->
#### **實作範例：LC 171 — Excel Sheet Column Number（*雙射* 26 進位）**

同一個想法的**反方向**，而且**不是**單純的 k 進位進位迴圈：從前往後折疊每個位數，`res = res * base + digit`。
麻煩的地方：Excel 是**從 1 開始編號**的（`A = 1 ... Z = 26`），所以沒有 `0` 這個位數 — 這叫做*雙射* 26 進位。

<!--CODE-->

<!--CODE-->

> **變形 — LC 7 Reverse Integer**：一樣是 `res = res * 10 + digit` 的累加，但結果必須留在 32 位元範圍內，所以要在**乘之前**檢查（在 Java 裡溢位發生後就抓不到了）。

<!--CODE-->

<!--CODE-->

<!-- 606bb988f39e -->
## 4) 其他相關 LC

- LC 8 : String to Integer (atoi) — 一樣是 `res = res * 10 + digit` 的累加，另外加上正負號解析／略過空白／溢位時**夾住邊界值**（而不是回傳 0）。

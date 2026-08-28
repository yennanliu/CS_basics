# 字串 DP（雙序列網格模式）

> **範圍** — 在一或兩個字串上做的 DP：`dp[m+1][n+1]` 的雙序列網格、以前綴為基礎（1-indexed）的表格設計，以及 LCS、編輯距離、交錯字串、萬用字元／括號比對的完整範例。
> **另見**：[dp.md](./dp.md) — 編輯距離與 LCS 的模板本身；[palindrome.md](./palindrome.md) — 回文專屬的 DP；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 非 DP 的子字串搜尋。

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 概觀

### 關鍵性質

- **複雜度**：完整表格是 `O(m * n)` 時間與空間；壓成兩列之後空間降到 `O(min(m, n))`。
- **核心想法**：`dp[i][j]` = **前綴 `s1[:i]` 與前綴 `s2[:j]`** 的答案。這一族的每一題都是同一張
  網格 — 變的只有「配到／沒配到」時的轉移。
- **什麼時候用**：兩個字串（或一個字串加一個 pattern）逐位比較，問的是最長／最小成本／方法數／可不可行。

### 題型分類

| 分類 | 問的問題 | LC |
|----------|----------|----|
| **最長共同** | 兩者共用了多少？ | 1143, 583, 712 |
| **轉換成本** | 從 `s1` 改成 `s2` 最便宜的編輯序列？ | 72, 161 |
| **路徑計數** | `s2` 在 `s1` 裡出現幾種方式？ | 115 |
| **可行性** | 這兩個能不能交錯／能不能配上這個 pattern？ | 97, 10, 44 |
| **單字串萬用字元** | 帶 `*` 的括號字串是否合法？ | 678 |

### 參考資料

- [dp.md](./dp.md) — 編輯距離（LC 72）與 LCS（LC 1143）的模板
- [palindrome.md](./palindrome.md) — 單字串的回文 DP 家族

## 模板與演算法

### 雙字串網格

#### **「雙字串／雙序列網格」模式** 🧩

這是字串問題裡最重要的 DP 模式之一。一旦你認出這個模式，一整類問題都會突然變好解。

**核心結構：**
- 開一個 2D 陣列 `dp[m+1][n+1]`，其中：
  - **列（i）**：代表字串 A 的前綴（前 i 個字元）
  - **行（j）**：代表字串 B 的前綴（前 j 個字元）
  - **格子 `dp[i][j]`**：存這兩個特定前綴的答案

**在網格上移動（怎麼選擇走法）：**

把這張網格想成一個從 `(0,0)` 走到 `(m,n)` 的遊戲：

1. **對角走（`dp[i-1][j-1]`）**：你同時「用掉」或「配對」**兩個**字串各一個字元
2. **往下走（`dp[i-1][j]`）**：你「跳過」或「刪掉」字串 A 的一個字元
3. **往右走（`dp[i][j-1]`）**：你「跳過」或「插入」字串 B 的一個字元

**模式對照表：**

| 題目 | 目標 | 配到時（`s1[i-1] == s2[j-1]`） | 沒配到時 | 關鍵洞見 |
|---------|------|-----------------------------------|----------------|-------------|
| **LC 1143: LCS** | 最長共同長度 | `1 + dp[i-1][j-1]`（對角 + 1） | `max(dp[i-1][j], dp[i][j-1])` | 配到就走對角，否則取「跳過任一字串」的較大值 |
| **LC 97: Interleaving String** | s3 能否由 s1+s2 交錯而成？ | `dp[i-1][j] \|\| dp[i][j-1]` | `false` | 檢查能不能從任一字串取一個字元湊出來 |
| **LC 115: Distinct Subsequences** | 數出現次數 | `dp[i-1][j-1] + dp[i-1][j]` | `dp[i-1][j]` | 可以用這次配對，也可以跳過 s 的字元 |
| **LC 72: Edit Distance** | 變成一樣的最少編輯次數 | `dp[i-1][j-1]`（不花成本） | `1 + min(top, left, diagonal)` | 配到就不用動，否則三種操作都試一遍 |
| **LC 583: Delete Operation** | 變成一樣的最少刪除次數 | `dp[i-1][j-1]` | `1 + min(dp[i-1][j], dp[i][j-1])` | 從任一字串刪 |
| **LC 712: Min ASCII Delete Sum** | 變成一樣的最小 ASCII 總和 | `dp[i-1][j-1]` | `min(dp[i-1][j] + s1[i], dp[i][j-1] + s2[j])` | 追蹤 ASCII 成本 |

**「空字串」的基底情況模式** 💡

這是雙字串 DP 裡**最重要**的一個模式：

* `dp[0][0]`：兩個字串都空的狀態（通常是 `0` 或 `true`）
* 第一列 `dp[0][j]`：字串 A 是空的，只有字串 B 有字元
* 第一行 `dp[i][0]`：字串 B 是空的，只有字串 A 有字元

**為什麼是 `m+1` 和 `n+1`？**
- 那個 `+1` 是留給「空字串」這個基底情況的位置
- 沒有它的話，處理第一個字元時 `dp[i-1][j]` 這種轉移會直接爆掉
- `dp[i][j]` 代表用了字串 1 的前 `i` 個字元、字串 2 的前 `j` 個字元

**通用模板：**
```java
// java
// IDEA: the shared skeleton of every two-sequence grid DP
// time = O(m * n), space = O(m * n)
public int stringDP(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[][] dp = new int[m + 1][n + 1];

    // Step 1: Initialize base cases (empty string states)
    dp[0][0] = 0; // Both strings empty

    // Initialize first row (s1 is empty)
    for (int j = 1; j <= n; j++) {
        dp[0][j] = initValueForEmptyS1(j);
    }

    // Initialize first column (s2 is empty)
    for (int i = 1; i <= m; i++) {
        dp[i][0] = initValueForEmptyS2(i);
    }

    // Step 2: Fill the DP table
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            // NOTE: Use i-1 and j-1 to access string characters
            if (s1.charAt(i-1) == s2.charAt(j-1)) {
                // Characters match
                dp[i][j] = transitionOnMatch(dp, i, j);
            } else {
                // Characters don't match
                dp[i][j] = transitionOnMismatch(dp, i, j);
            }
        }
    }

    return dp[m][n];
}
```

**空間最佳化的秘密** ⚡

在每一個「雙字串」問題裡，你真正會看到的只有：
- **目前這一列**（`dp[i][j]`）
- **上面那一列**（`dp[i-1][j]`）
- **對角那格**（`dp[i-1][j-1]`）

也就是說，你**永遠可以把空間從 O(m×n) 降到 O(n)**，做法是：
1. 用一個一維陣列存上一列
2. 用一個變數存對角的值
3. 一列一列處理時滾動更新

**空間最佳化版 LCS 範例：**
```java
// java
// LC 1143 - Longest Common Subsequence
// IDEA: diagonal + 1 on a match, else the better of dropping one character
// time = O(m * n), space = O(m * n)
public int longestCommonSubsequence(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[] prev = new int[n + 1];

    for (int i = 1; i <= m; i++) {
        int[] curr = new int[n + 1];
        for (int j = 1; j <= n; j++) {
            if (s1.charAt(i-1) == s2.charAt(j-1)) {
                curr[j] = prev[j-1] + 1; // Diagonal
            } else {
                curr[j] = Math.max(prev[j], curr[j-1]); // Top or left
            }
        }
        prev = curr; // Roll forward
    }

    return prev[n];
}
```

---

### **深入：以前綴為基礎的索引模式（LCS 與其變形）** 🔍

這一小節專門講清楚 **1-indexed DP 表格**這個概念 — 它是字串 DP 寫對的關鍵。

#### **為什麼要用 1-indexed 的 DP 表格？**

在字串問題裡建 2D DP 表時，我們用 `dp[m+1][n+1]` 而不是 `dp[m][n]`。這看起來只是多背一個差一錯誤，其實它很優雅：

**關鍵洞見**：`dp[i][j]` 代表的是 **string1 長度為 i 的前綴、string2 長度為 j 的前綴之間的答案**。

```text
✅ CORRECT: 1-indexed approach
   dp[i][j] = answer for string1.substring(0, i) and string2.substring(0, j)
   - Row index i ∈ [0, m] where m = string1.length()
   - Col index j ∈ [0, n] where n = string2.length()
   - Character comparison: string1.charAt(i-1) vs string2.charAt(j-1)

❌ WRONG: 0-indexed approach (will cause boundary issues)
   - No room for "empty string" base case
   - First iteration accesses negative indices
```

#### **前綴的概念：為什麼取字元要用 dp[i-1] 和 dp[j-1]**

```text
Example: string1 = "abcde", string2 = "ace"

When i=3, j=2 (processing prefixes "abc" and "ac"):
  - DP state represents: LCS("abc", "ac")
  - We compare: string1.charAt(3-1) = 'c' with string2.charAt(2-1) = 'c'
  - The characters at POSITION (i-1) and (j-1) are what define the LAST character of each prefix

Index Mapping:
  i=0: prefix length 0 (empty string)
  i=1: prefix length 1 (first 1 char) → access string1[0]
  i=2: prefix length 2 (first 2 chars) → access string1[1]
  i=3: prefix length 3 (first 3 chars) → access string1[2]
  ...
  Therefore: when at dp[i][j], compare string1[i-1] with string2[j-1]
```

#### **三向轉移的邏輯（用 LCS 當例子）**

```java
// java
// IDEA: the match / mismatch branch, isolated
// Pattern: Two cases only
if (string1.charAt(i - 1) == string2.charAt(j - 1)) {
    // CASE 1: Characters match → extend previous best result
    // The matching characters contribute +1 to the LCS length
    dp[i][j] = 1 + dp[i - 1][j - 1];  // Diagonal: both strings move forward
} else {
    // CASE 2: Characters don't match → take best of skipping either string
    // We have two choices:
    //   Option A: Skip current char from string1 → dp[i-1][j]
    //   Option B: Skip current char from string2 → dp[i][j-1]
    // Take whichever gives the better result
    dp[i][j] = Math.max(dp[i - 1][j],    // Skip from string1
                       dp[i][j - 1]);    // Skip from string2
}
```

**為什麼這樣行得通：**
- **對角（dp[i-1][j-1]）**：字元配到時，我們把兩個字元都「用掉」去組答案。取較短前綴的最佳結果，再加 1。
- **往下（dp[i-1][j]）**：字元沒配到時，跳過 string1 目前的字元，看看還能不能跟 string2 找到好的 LCS。
- **往右（dp[i][j-1]）**：或者反過來跳過 string2 的字元，看能不能跟 string1 找到好的 LCS。

#### **完整的 LCS 網格範例**

```text
string1 = "abcde"
string2 = "ace"

DP Grid (showing values):
        ""  a   c   e
    ""  0   0   0   0
    a   0   1   1   1
    b   0   1   1   1
    c   0   1   2   2
    d   0   1   2   2
    e   0   1   2   3

How to read:
  dp[4][2] = 2 means LCS("abcd", "ac") has length 2
  dp[5][3] = 3 means LCS("abcde", "ace") has length 3 ✓

Key moments:
  - dp[1][1]: Compare 'a' with 'a' → match! → dp[0][0] + 1 = 1
  - dp[2][1]: Compare 'b' with 'a' → no match → max(dp[1][1], dp[2][0]) = 1
  - dp[3][2]: Compare 'c' with 'c' → match! → dp[2][1] + 1 = 2
  - dp[5][3]: Compare 'e' with 'e' → match! → dp[4][2] + 1 = 3
```

#### **Java 實作寫法**

```java
// java
// LC 1143 - Longest Common Subsequence
// IDEA: same recurrence, written as the canonical 1-indexed table
// time = O(m * n), space = O(m * n)
public int longestCommonSubsequence(String text1, String text2) {
    int m = text1.length();
    int n = text2.length();

    // Create (m+1) × (n+1) table to handle "empty string" base case
    int[][] dp = new int[m + 1][n + 1];
    // dp[0][j] and dp[i][0] are already 0 (empty string has LCS of 0)

    // Loop uses 1-based indices to represent prefix lengths
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            // i represents prefix length in text1
            // j represents prefix length in text2
            // Compare character at position i-1 and j-1 (0-indexed)
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                // Characters match: extend the best previous result
                dp[i][j] = 1 + dp[i - 1][j - 1];
            } else {
                // Characters don't match: choose best by skipping either string
                dp[i][j] = Math.max(dp[i - 1][j],    // Skip from text1
                                   dp[i][j - 1]);    // Skip from text2
            }
        }
    }

    return dp[m][n];  // Answer for full strings
}
```

#### **什麼時候用這個模式** 📋

出現以下情況時，就用 **1-indexed、以前綴為基礎的 2D DP**：

| 條件 | 例題 |
|-----------|------------------|
| 輸入是兩個字串／序列 | LC 1143 (LCS), LC 72 (Edit Distance) |
| 答案取決於逐字元比較前綴 | LC 583 (Delete Ops), LC 712 (Min ASCII Delete) |
| 三向轉移（配到／跳過 1／跳過 2）或雙向轉移 | LC 1143, 97, 115 |
| 需要把「空字串」當成基底情況處理 | 所有雙字串 DP 問題 |

#### **用到這個模式的類似 LeetCode 題目**

| 題目 | 目標 | 配到時 | 沒配到時 | 複雜度 |
|---------|------|-----------|----------------|-----------|
| **LC 1143: LCS** | 最長共同子序列的長度 | `1 + dp[i-1][j-1]` | `max(dp[i-1][j], dp[i][j-1])` | O(m×n) |
| **LC 72: Edit Distance** | 轉換所需的最少操作數 | `dp[i-1][j-1]`（不花成本） | `1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])` | O(m×n) |
| **LC 583: Delete Operation** | 變成一樣的最少刪除次數 | `dp[i-1][j-1]` | `1 + min(dp[i-1][j], dp[i][j-1])` | O(m×n) |
| **LC 97: Interleaving String** | s3 能否由 s1+s2 交錯而成？ | `dp[i-1][j] \|\| dp[i][j-1]` | `false` | O(m×n) |
| **LC 115: Distinct Subsequences** | 數 `s2` 作為 `s1` 子序列的出現次數 | `dp[i-1][j-1] + dp[i-1][j]` | `dp[i-1][j]` | O(m×n) |
| **LC 712: Min ASCII Delete Sum** | 讓兩字串相等的最小成本 | `dp[i-1][j-1]` | `min(dp[i-1][j] + cost1, dp[i][j-1] + cost2)` | O(m×n) |

#### **常見陷阱** ⚠️

1. **直接用 0-indexed 的 DP** → 會出現負索引存取，也沒有空字串的位置
2. **比較 `string[i]` 而不是 `string[i-1]`** → 字元比較上的差一錯誤
3. **忘了初始化第一列／第一行** → 有些題目需要特別的初始化
4. **沒配到時的轉移寫錯** → 一定要對應你這題自己的邏輯

---

**快速辨識檢查表** ✅

看到下面這些訊號，就用「雙字串網格」模式：
- [ ] 輸入是兩個字串／序列
- [ ] 需要比較兩個字串的字元
- [ ] 答案取決於前綴（s1 的前 i 個字元、s2 的前 j 個字元）
- [ ] 關鍵字：「共同」、「比對」、「轉換」、「交錯」、「子序列」

**常見題目：**
- LC 1143 (LCS) - 找最長共同子序列
- LC 72 (Edit Distance) - 轉換所需的最少編輯次數
- LC 97 (Interleaving String) - s3 能不能由交錯組出來？
- LC 115 (Distinct Subsequences) - 數出現次數
- LC 583 (Delete Operation) - 變成一樣的最少刪除次數
- LC 712 (Min ASCII Delete Sum) - 變成一樣的最小 ASCII 成本
- LC 10 (Regular Expression Matching) - 帶 * 和 . 的 pattern 比對
- LC 44 (Wildcard Matching) - 帶 * 和 ? 的 pattern 比對

---

### **經典字串 DP 模式（細節）**

| 題型 | 模式 | 複雜度 | 備註 |
|--------------|---------|------------|-------|
| **編輯距離** | dp[i][j] = 把 s1[:i] 變成 s2[:j] 的操作數 | O(m×n) | 插入／刪除／取代 |
| **LCS** | dp[i][j] = s1[:i] 與 s2[:j] 的 LCS 長度 | O(m×n) | 雙序列；配到就走對角 |
| **LIS** | dp[i] = 以 i 結尾的最長遞增子序列 | O(n²) | **單**序列 — 不是這張網格；用 patience sorting 可到 O(n log n) |
| **回文** | dp[i][j] = s[i:j+1] 是不是回文 | O(n²) | 從中心往外擴 |
| **Word Break** | dp[i] = s[:i] 能不能切開 | O(n³) | 檢查所有可能的切點 |

## LC 範例

### **交錯字串模式（LC 97）** 🧩

**模式**：雙字串網格 DP（布林）

**核心想法**：給三個字串 `s1`、`s2`、`s3`，判斷 `s3` 是不是由 `s1` 和 `s2` 在保持各自相對順序的前提下交錯而成。可以把它想成在 2D 網格裡從 `(0,0)` 走到 `(m,n)`：**往下**走代表從 `s1` 取一個字元，**往右**走代表從 `s2` 取一個字元。

**DP 定義**：
- `dp[i][j]` = `s1[0..i-1]` 和 `s2[0..j-1]` 能不能組出 `s3[0..i+j-1]`？

**關鍵遞迴式**：
```text
dp[i][j] = (dp[i-1][j] && s1[i-1] == s3[i+j-1])   // take from s1
         || (dp[i][j-1] && s2[j-1] == s3[i+j-1])   // take from s2
```

**基底情況**：
- `dp[0][0] = true`（空 + 空 = 空）
- 第一行：`dp[i][0] = dp[i-1][0] && s1[i-1] == s3[i-1]`（只有 s1 有貢獻）
- 第一列：`dp[0][j] = dp[0][j-1] && s2[j-1] == s3[j-1]`（只有 s2 有貢獻）

**提早結束**：如果 `len(s1) + len(s2) != len(s3)`，直接回傳 `false`。

**空間最佳化**：因為每一列只依賴目前列和上一列，可以壓成一維的 `dp[n+1]`。

| 做法 | 時間 | 空間 |
|----------|------|-------|
| 2D DP | O(m×n) | O(m×n) |
| 1D DP（空間最佳化） | O(m×n) | O(min(m,n)) |
| 由上而下記憶化 | O(m×n) | O(m×n) |
| 暴力遞迴 | O(2^(m+n)) | O(m+n) |

**類似的 LeetCode 題目**：

| 題目 | 相似之處 | 主要差別 |
|---------|-----------|----------------|
| LC 1143 (LCS) | 雙字串網格，比較前綴 | 求最大長度，不是布林判斷 |
| LC 72 (Edit Distance) | 雙字串網格，三種轉移 | 求最小成本，不是可行性 |
| LC 115 (Distinct Subsequences) | 雙字串網格，數路徑 | 數方法數，不是回答是／否 |
| LC 583 (Delete Operation for Two Strings) | 雙字串網格 | 求最少刪除次數 |
| LC 44 (Wildcard Matching) | 雙字串布林 DP 網格 | 帶 `*` 和 `?` 的 pattern 比對 |
| LC 10 (Regular Expression Matching) | 雙字串布林 DP 網格 | 帶 `*` 和 `.` 的 pattern 比對 |

**參考**：2D DP、1D DP、由上而下與由下而上的實作，見 `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/InterleavingString.java`。

---

### **合法括號字串模式（LC 678）** 🌟

**題目**：給一個由 '('、')' 和 '*' 組成的字串，其中 '*' 可以當成 '('、')' 或空字串，判斷這個字串是否合法。

這題示範了**多種 DP 典範**，很適合用來理解：
- 帶萬用字元的狀態追蹤
- 貪婪 vs DP 的取捨
- 區間 DP 模式
- 空間最佳化技巧

#### **做法 1：貪婪（追蹤最小／最大平衡值）** ⚡ 最佳解

**時間**：O(n) | **空間**：O(1)

**關鍵洞見**：在每個位置追蹤未配對左括號數量的**可能範圍**。

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: greedy — carry the min/max possible open count instead of a DP table
// time = O(n), space = O(1)
public boolean checkValidString(String s) {
    int minParenCnt = 0; // minimum possible unmatched '('
    int maxParenCnt = 0; // maximum possible unmatched '('

    for (char c : s.toCharArray()) {
        if (c == '(') {
            minParenCnt++;
            maxParenCnt++;
        } else if (c == ')') {
            minParenCnt--;
            maxParenCnt--;
        } else { // '*' - wildcard
            minParenCnt--; // treat '*' as ')'
            maxParenCnt++; // treat '*' as '('
        }

        // If maxParenCnt < 0: too many unmatched ')'
        if (maxParenCnt < 0) return false;

        // If minParenCnt < 0: reset to 0 (can use '*' as empty)
        if (minParenCnt < 0) minParenCnt = 0;
    }

    // Valid if we can have 0 unmatched '('
    return minParenCnt == 0;
}
```

**為什麼行得通**：
- `maxParenCnt < 0` → 不可能平衡了（')' 太多）
- `minParenCnt = 0` → 可以把 '*' 當成空字串，重置掉負的平衡值
- 最後 `minParenCnt == 0` → 至少存在一種合法的配對方式

---

#### **做法 2：2D DP（位置 × 未配對數）** 📊

**時間**：O(n²) | **空間**：O(n²)

**DP 定義**：
- `dp[i][j]`：處理完前 `i` 個字元後，能不能剛好剩下 `j` 個未配對的 '('？

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: dp[i][open] = can s[i:] close with `open` brackets already outstanding
// time = O(n^2), space = O(n^2)
public boolean checkValidString(String s) {
    int n = s.length();
    boolean[][] dp = new boolean[n + 1][n + 1];
    dp[0][0] = true; // empty string, 0 open parens

    for (int i = 1; i <= n; i++) {
        char c = s.charAt(i - 1);
        for (int j = 0; j <= n; j++) {
            if (c == '(') {
                // Add one open paren
                if (j > 0) dp[i][j] = dp[i - 1][j - 1];
            } else if (c == ')') {
                // Close one open paren
                if (j < n) dp[i][j] = dp[i - 1][j + 1];
            } else { // '*'
                // Option 1: treat '*' as empty
                dp[i][j] = dp[i - 1][j];
                // Option 2: treat '*' as '('
                if (j > 0) dp[i][j] |= dp[i - 1][j - 1];
                // Option 3: treat '*' as ')'
                if (j < n) dp[i][j] |= dp[i - 1][j + 1];
            }
        }
    }

    return dp[n][0]; // n chars processed, 0 open parens
}
```

**狀態轉移**：
- `'('`：`dp[i][j] = dp[i-1][j-1]`（未配對數 +1）
- `')'`：`dp[i][j] = dp[i-1][j+1]`（未配對數 -1）
- `'*'`：`dp[i][j] = dp[i-1][j] || dp[i-1][j-1] || dp[i-1][j+1]`（三種都試）

---

#### **做法 3：區間 DP（判斷區間是否合法）** 🎯

**時間**：O(n³) | **空間**：O(n²)

**DP 定義**：
- `dp[i][j]`：子字串 `s[i..j]` 是否合法？

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: interval DP — dp[i][j] = is s[i..j] a valid string on its own
// time = O(n^3), space = O(n^2)
public boolean checkValidString(String s) {
    int n = s.length();
    if (n == 0) return true;

    boolean[][] dp = new boolean[n][n];

    // Base case: single character valid only if '*'
    for (int i = 0; i < n; i++) {
        if (s.charAt(i) == '*') dp[i][i] = true;
    }

    // Fill table for increasing lengths
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;

            // Option A: s[i] and s[j] form a matching pair
            if ((s.charAt(i) == '(' || s.charAt(i) == '*') &&
                (s.charAt(j) == ')' || s.charAt(j) == '*')) {
                if (len == 2 || dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                }
            }

            // Option B: Split at some point k
            if (!dp[i][j]) {
                for (int k = i; k < j; k++) {
                    if (dp[i][k] && dp[k + 1][j]) {
                        dp[i][j] = true;
                        break;
                    }
                }
            }
        }
    }

    return dp[0][n - 1];
}
```

**關鍵模式**：這是經典的**區間 DP**，類似：
- LC 312 (Burst Balloons)
- LC 1039 (Minimum Score Triangulation)
- LC 1547 (Minimum Cost to Cut a Stick)

---

#### **做法 4：由上而下 DP（遞迴 + 記憶化）** 🔄

**時間**：O(n²) | **空間**：O(n²) + 遞迴堆疊

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: top-down recursion on (index, open) with memoisation
// time = O(n^2), space = O(n^2)
public boolean checkValidString(String s) {
    int n = s.length();
    Boolean[][] memo = new Boolean[n + 1][n + 1];
    return dfs(0, 0, s, memo);
}

private boolean dfs(int i, int open, String s, Boolean[][] memo) {
    // Too many closing parens
    if (open < 0) return false;

    // End of string: valid if all matched
    if (i == s.length()) return open == 0;

    // Memoization
    if (memo[i][open] != null) return memo[i][open];

    boolean result;
    if (s.charAt(i) == '(') {
        result = dfs(i + 1, open + 1, s, memo);
    } else if (s.charAt(i) == ')') {
        result = dfs(i + 1, open - 1, s, memo);
    } else { // '*'
        result = dfs(i + 1, open, s, memo) ||        // empty
                 dfs(i + 1, open + 1, s, memo) ||    // '('
                 dfs(i + 1, open - 1, s, memo);      // ')'
    }

    memo[i][open] = result;
    return result;
}
```

---

#### **做法 5：由下而上 DP** 📈

**時間**：O(n²) | **空間**：O(n²)

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: same states as approach 4, filled bottom-up
// time = O(n^2), space = O(n^2)
public boolean checkValidString(String s) {
    int n = s.length();
    boolean[][] dp = new boolean[n + 1][n + 1];
    dp[n][0] = true; // base: end with 0 open parens

    for (int i = n - 1; i >= 0; i--) {
        for (int open = 0; open < n; open++) {
            boolean res = false;
            if (s.charAt(i) == '*') {
                res |= dp[i + 1][open + 1];           // treat as '('
                if (open > 0) res |= dp[i + 1][open - 1]; // treat as ')'
                res |= dp[i + 1][open];                // treat as empty
            } else {
                if (s.charAt(i) == '(') {
                    res |= dp[i + 1][open + 1];
                } else if (open > 0) {
                    res |= dp[i + 1][open - 1];
                }
            }
            dp[i][open] = res;
        }
    }
    return dp[0][0];
}
```

---

#### **做法 6：空間最佳化 DP** ⚡

**時間**：O(n²) | **空間**：O(n)

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: bottom-up rolled down to one row over `open`
// time = O(n^2), space = O(n)
public boolean checkValidString(String s) {
    int n = s.length();
    boolean[] dp = new boolean[n + 1];
    dp[0] = true;

    for (int i = n - 1; i >= 0; i--) {
        boolean[] newDp = new boolean[n + 1];
        for (int open = 0; open < n; open++) {
            if (s.charAt(i) == '*') {
                newDp[open] = dp[open + 1] ||
                              (open > 0 && dp[open - 1]) ||
                              dp[open];
            } else if (s.charAt(i) == '(') {
                newDp[open] = dp[open + 1];
            } else if (open > 0) {
                newDp[open] = dp[open - 1];
            }
        }
        dp = newDp;
    }
    return dp[0];
}
```

**空間最佳化技巧**：滾動陣列 — 只保留目前列和上一列。

---

#### **做法 7：用堆疊（兩個堆疊）** 📚

**時間**：O(n) | **空間**：O(n)

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: two stacks — one of '(' indices, one of '*' indices; match leftovers by position
// time = O(n), space = O(n)
public boolean checkValidString(String s) {
    Stack<Integer> leftStack = new Stack<>();  // indices of '('
    Stack<Integer> starStack = new Stack<>();  // indices of '*'

    // First pass: match ')' with '(' or '*'
    for (int i = 0; i < s.length(); i++) {
        char ch = s.charAt(i);
        if (ch == '(') {
            leftStack.push(i);
        } else if (ch == '*') {
            starStack.push(i);
        } else { // ')'
            if (!leftStack.isEmpty()) {
                leftStack.pop();
            } else if (!starStack.isEmpty()) {
                starStack.pop();
            } else {
                return false; // unmatched ')'
            }
        }
    }

    // Second pass: match remaining '(' with '*'
    while (!leftStack.isEmpty() && !starStack.isEmpty()) {
        // '*' must come after '(' to be valid
        if (leftStack.pop() > starStack.pop()) {
            return false;
        }
    }

    return leftStack.isEmpty();
}
```

**關鍵洞見**：存**索引**，才能確保把 '*' 當 ')' 用時，它確實出現在 '(' 之後。

---

#### **各做法比較總表**

| 做法 | 時間 | 空間 | 最適合 | 取捨 |
|----------|------|-------|----------|------------|
| **貪婪（min/max）** | O(n) | O(1) | 上線用的程式碼 | 第一次看最難懂 |
| **2D DP（位置 × 數量）** | O(n²) | O(n²) | 學狀態轉移 | 吃空間但直覺 |
| **區間 DP** | O(n³) | O(n²) | 理解區間類問題 | 最慢，但把區間模式攤開來看 |
| **由上而下 DP** | O(n²) | O(n²) | 習慣遞迴思考的人 | 有堆疊開銷 |
| **由下而上 DP** | O(n²) | O(n²) | 想避開遞迴 | 要反過來想 |
| **空間最佳化** | O(n²) | O(n) | 記憶體吃緊時 | 實作比較複雜 |
| **用堆疊** | O(n) | O(n) | 靠索引追蹤的洞見 | 需要跑兩趟 |

#### **重點整理** 💡

1. **貪婪是最佳解** — 能認出什麼時候貪婪成立，是這題的關鍵
2. **萬用字元的處理**：永遠要把所有可能性都考慮進去（'('、')'、空）
3. **平衡值追蹤**：很多括號題最後都化約成追蹤未配對左括號的數量
4. **位置很重要**：萬用字元可以代表不同東西時，它出現的位置就有意義（堆疊解法）
5. **多種典範**：同一題可以用區間 DP、狀態 DP、貪婪和堆疊解出來

#### **相關題目**
- LC 20 (Valid Parentheses) - 沒有 '*' 的簡單版
- LC 32 (Longest Valid Parentheses) - 找最長的合法子字串
- LC 301 (Remove Invalid Parentheses) - 刪最少字元讓它合法
- LC 921 (Minimum Add to Make Parentheses Valid) - 最少需要補幾個

**參考**：`leetcode_java/src/main/java/LeetCodeJava/String/ValidParenthesisString.java`

---

## 總結

| 題目在問什麼 | dp[i][j] 存什麼 | 配到時 | 沒配到時 |
|---|---|---|---|
| 最長的共同部分 | LCS 長度 | `1 + dp[i-1][j-1]` | `max(dp[i-1][j], dp[i][j-1])` |
| 最便宜的改寫 | 編輯成本 | `dp[i-1][j-1]` | `1 + min(三個鄰居)` |
| 有幾種嵌入方式 | 方法數 | `dp[i-1][j-1] + dp[i-1][j]` | `dp[i-1][j]` |
| 到底可不可行 | 布林 | 所有合法走法取 OR | `False` |

**避開大多數 bug 的三條規則**

1. 表格開成 `dp[m+1][n+1]`，取字元時寫 `s1[i-1]` / `s2[j-1]` — 第 `0` 列／第 `0` 行是空前綴，
   有了它基底情況才寫得出來。
2. 主迴圈*之前*先填好第 `0` 列和第 `0` 行；它們編碼的是「跟空字串比對」。
3. 答案是 `dp[m][n]`，絕不是 `dp[m-1][n-1]`。

# Palindrome (回文)

> **範圍** — 回文題型家族：中心擴散、雙指標驗證、回文 DP、Manacher，以及怎麼判斷一題該用哪一種。
> **另見**：[2_pointers.md](./2_pointers.md) — 底層的收斂概念；[dp.md](./dp.md) — 區間 DP 的觀點；[string.md](./string.md) — 一般字串處理；[advanced_string_algorithms.md](./advanced_string_algorithms.md) — Manacher 放在更大的脈絡裡看。

## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)
- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 總覽
**回文**是正著讀、反著讀都一樣的序列。它是字串處理與陣列處理的基本概念，面試出現頻率很高。

### 關鍵性質
- **時間複雜度**：基本操作 O(n)，子字串類問題 O(n²)
- **空間複雜度**：雙指標 O(1)，遞迴／DP 解法 O(n)
- **核心想法**：從兩端往中間比對字元／元素
- **什麼時候用**：字串驗證、子字串問題、數字處理、切割問題

### 題型分類

#### **模式 1：基本回文驗證**
- **說明**：判斷給定的字串／陣列／數字是不是回文
- **例題**：LC 9、125、234 — Valid Palindrome、Palindrome Number、Palindrome Linked List
- **做法**：兩端雙指標，或反轉後比對

#### **模式 2：回文子字串問題**  
- **說明**：找最長回文子字串，或計算回文子字串個數
- **例題**：LC 5、647、214 — Longest Palindromic Substring、Palindromic Substrings
- **做法**：中心擴散或動態規劃

#### **模式 3：回文構造與切割**
- **說明**：把字串切成數個回文段，或構造出回文
- **例題**：LC 131、132、336 — Palindrome Partitioning I/II、Palindrome Pairs
- **做法**：回溯 + 回文檢查、動態規劃

#### **模式 4：回文改造**
- **說明**：透過插入、刪除把字串變成回文
- **例題**：LC 516、1312、1332 — Longest Palindromic Subsequence、Minimum Insertions
- **做法**：帶編輯距離概念的動態規劃

#### **模式 5：進階回文問題**
- **說明**：附加額外限制的複雜回文題
- **例題**：LC 1147、1177、1930 — Longest Chunked Palindrome、Can Make Palindrome
- **做法**：滑動視窗、雜湊表、位元運算

#### **模式 6：數字回文**
- **說明**：牽涉整數與數學運算的回文題
- **例題**：LC 9、479、564 — Palindrome Number、Largest Palindrome Product
- **做法**：數學操作、轉字串

### 參考資料
- [LeetCode Palindrome Problems](https://leetcode.com/tag/string/)
- Algorithm Design Manual - String Algorithms

## 模板與演算法

### 模板比較表
| 模板類型 | 適用情境 | 時間複雜度 | 什麼時候用 |
|---------------|----------|-----------------|-------------|
| **雙指標** | 基本驗證 | O(n) | 單純判斷回文 |
| **中心擴散** | 找最長子字串 | O(n²) | 子字串問題 |
| **反轉後比對** | 簡易檢查 | O(n) | 空間不是限制時 |
| **DP（二維）** | 複雜的計數 | O(n²) | 有多個重疊子問題 |
| **遞迴 + 記憶化** | 子序列問題 | O(n²) | 子問題重疊 |
| **回溯** | 切割 | O(2^n) | 要列出所有切法 |
| **Manacher**（模板 7） | 最長回文子字串 | O(n) | n 很大（10^5+），O(n²) 會 TLE |
| **KMP 前綴函數**（模板 8） | 最長回文前綴／後綴 | O(n) | 把字串補成回文（LC 214） |
| **HashMap 拆分**（模板 9） | 單字的回文配對 | O(n·L²) | 哪些串接起來是回文（LC 336） |

### 模板 1：雙指標（最常用）
```python
def is_palindrome_two_pointers(s):
    """
    Basic palindrome check using two pointers
    Time: O(n), Space: O(1)
    """
    left, right = 0, len(s) - 1
    
    while left < right:
        # Skip non-alphanumeric characters (for valid palindrome problems)
        while left < right and not s[left].isalnum():
            left += 1
        while left < right and not s[right].isalnum():
            right -= 1
            
        # Compare characters (case-insensitive)
        if s[left].lower() != s[right].lower():
            return False
            
        left += 1
        right -= 1
    
    return True
```

```java
// Java version
class Solution {
    public boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            
            if (Character.toLowerCase(s.charAt(left)) != 
                Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
}
```

### 模板 2：中心擴散（子字串問題）
```python
def longest_palindromic_substring(s):
    """
    Find longest palindromic substring using center expansion
    Time: O(n²), Space: O(1)
    """
    if not s:
        return ""
    
    start, max_len = 0, 1
    
    def expand_around_center(left, right):
        # Expand while characters match
        while left >= 0 and right < len(s) and s[left] == s[right]:
            left -= 1
            right += 1
        return right - left - 1  # Length of palindrome
    
    for i in range(len(s)):
        # Check for odd-length palindromes (center at i)
        len1 = expand_around_center(i, i)
        # Check for even-length palindromes (center between i and i+1)
        len2 = expand_around_center(i, i + 1)
        
        current_max = max(len1, len2)
        if current_max > max_len:
            max_len = current_max
            start = i - (current_max - 1) // 2
    
    return s[start:start + max_len]
```

```java
// Java version
class Solution {
    public String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";
        
        int start = 0, end = 0;
        
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);     // odd length
            int len2 = expandAroundCenter(s, i, i + 1); // even length
            int len = Math.max(len1, len2);
            
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        
        return s.substring(start, end + 1);
    }
    
    private int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }
}
```

### 模板 3：動態規劃（複雜計數）
```python
def count_palindromic_substrings(s):
    """
    Count all palindromic substrings using DP
    Time: O(n²), Space: O(n²)
    """
    n = len(s)
    # dp[i][j] = True if s[i:j+1] is palindrome
    dp = [[False] * n for _ in range(n)]
    count = 0
    
    # Single characters are palindromes
    for i in range(n):
        dp[i][i] = True
        count += 1
    
    # Check for palindromes of length 2
    for i in range(n - 1):
        if s[i] == s[i + 1]:
            dp[i][i + 1] = True
            count += 1
    
    # Check for palindromes of length 3+
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            
            # Check if s[i:j+1] is palindrome
            if s[i] == s[j] and dp[i + 1][j - 1]:
                dp[i][j] = True
                count += 1
    
    return count

# Space-optimized version using center expansion
def count_palindromic_substrings_optimized(s):
    """
    Time: O(n²), Space: O(1)
    """
    count = 0
    
    def expand_and_count(left, right):
        nonlocal count
        while left >= 0 and right < len(s) and s[left] == s[right]:
            count += 1
            left -= 1
            right += 1
    
    for i in range(len(s)):
        # Odd length palindromes
        expand_and_count(i, i)
        # Even length palindromes
        expand_and_count(i, i + 1)
    
    return count
```

### 模板 4：回溯（切割）
```python
def palindrome_partitioning(s):
    """
    Find all palindrome partitions using backtracking
    Time: O(n * 2^n), Space: O(n)
    """
    result = []
    current_partition = []
    
    def is_palindrome(start, end):
        while start < end:
            if s[start] != s[end]:
                return False
            start += 1
            end -= 1
        return True
    
    def backtrack(start):
        # Base case: reached end of string
        if start >= len(s):
            result.append(current_partition[:])
            return
        
        # Try all possible endings for current substring
        for end in range(start, len(s)):
            # If current substring is palindrome
            if is_palindrome(start, end):
                current_partition.append(s[start:end + 1])
                backtrack(end + 1)
                current_partition.pop()
    
    backtrack(0)
    return result

# Optimized with memoization
def palindrome_partitioning_memo(s):
    """
    With palindrome check memoization
    """
    n = len(s)
    # Precompute palindrome checks
    is_palin = [[False] * n for _ in range(n)]
    
    # Fill palindrome table
    for i in range(n):
        is_palin[i][i] = True
    
    for i in range(n - 1):
        is_palin[i][i + 1] = (s[i] == s[i + 1])
    
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            is_palin[i][j] = (s[i] == s[j] and is_palin[i + 1][j - 1])
    
    result = []
    current_partition = []
    
    def backtrack(start):
        if start >= n:
            result.append(current_partition[:])
            return
        
        for end in range(start, n):
            if is_palin[start][end]:
                current_partition.append(s[start:end + 1])
                backtrack(end + 1)
                current_partition.pop()
    
    backtrack(0)
    return result
```

### 模板 5：最長回文子序列（DP）— LC 516
```python
def longest_palindromic_subsequence(s):
    """
    Find length of longest palindromic subsequence using DP
    Time: O(n²), Space: O(n²)
    """
    n = len(s)
    # dp[i][j] = length of LPS in s[i:j+1]
    dp = [[0] * n for _ in range(n)]
    
    # Single characters have LPS length 1
    for i in range(n):
        dp[i][i] = 1
    
    # Fill for substrings of length 2+
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            
            if s[i] == s[j]:
                dp[i][j] = dp[i + 1][j - 1] + 2
            else:
                dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
    
    return dp[0][n - 1]

# Space-optimized version
def longest_palindromic_subsequence_optimized(s):
    """
    Time: O(n²), Space: O(n)
    """
    n = len(s)
    dp = [1] * n
    
    for i in range(n - 2, -1, -1):
        prev = 0
        for j in range(i + 1, n):
            temp = dp[j]
            if s[i] == s[j]:
                dp[j] = prev + 2
            else:
                dp[j] = max(dp[j], dp[j - 1])
            prev = temp
    
    return dp[n - 1]
```

### 模板 6：數字回文
```python
def is_palindrome_number(x):
    """
    Check if integer is palindrome without converting to string
    Time: O(log x), Space: O(1)
    """
    # Negative numbers are not palindromes
    if x < 0:
        return False
    
    # Single digit numbers are palindromes
    if x < 10:
        return True
    
    # Numbers ending in 0 (except 0) are not palindromes
    if x % 10 == 0:
        return False
    
    reversed_half = 0
    
    # Reverse only half of the number
    while x > reversed_half:
        reversed_half = reversed_half * 10 + x % 10
        x //= 10
    
    # For even length: x == reversed_half
    # For odd length: x == reversed_half // 10
    return x == reversed_half or x == reversed_half // 10
```

```java
// Java version
class Solution {
    public boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        
        int reversedHalf = 0;
        while (x > reversedHalf) {
            reversedHalf = reversedHalf * 10 + x % 10;
            x /= 10;
        }
        
        return x == reversedHalf || x == reversedHalf / 10;
    }
}
```

## 依模式分類的題目

### **模式 1：基本回文驗證**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Valid Palindrome | 125 | 雙指標、過濾字元 | Easy | 雙指標 |
| Palindrome Number | 9 | 數學反轉 | Easy | 數字回文 |
| Palindrome Linked List | 234 | 雙指標、反轉後半段 | Easy | 雙指標 |
| Valid Palindrome II | 680 | 雙指標、允許刪一個 | Easy | 雙指標 |

### **模式 2：回文子字串問題**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Longest Palindromic Substring | 5 | 中心擴散 | Medium | 中心擴散 |
| Palindromic Substrings | 647 | 中心擴散／DP | Medium | 中心擴散 |
| Shortest Palindrome | 214 | KMP／中心擴散 | Hard | 中心擴散 |
| Longest Palindromic Subsequence | 516 | DP（二維） | Medium | DP 子序列 |
| Palindromic Substring Queries | 1177 | 位元運算 | Medium | 進階 |
| Minimum Insertion Steps to Make Palindrome | 1312 | DP（編輯距離） | Hard | DP 子序列 |

### **模式 3：回文構造與切割**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Palindrome Partitioning | 131 | 回溯 + 回文檢查 | Medium | 回溯 |
| Palindrome Partitioning II | 132 | DP + 回文預處理 | Hard | DP + 回溯 |
| Palindrome Pairs | 336 | HashMap + 字串處理 | Hard | 進階 |
| Valid Palindrome III | 1216 | DP（允許刪 k 個） | Hard | DP 改造 |
| Break a Palindrome | 1328 | 貪婪 + 字串處理 | Medium | 貪婪 |

### **模式 4：回文改造（插入／刪除）**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Minimum Deletions to Make Palindrome | 516* | DP（LPS 差值） | Medium | DP 子序列 |
| Minimum Insertions for Palindrome | 1312 | DP（編輯距離） | Hard | DP 子序列 |
| Delete Operation for Two Strings | 583* | DP（LCS 概念） | Medium | DP 子序列 |
| Longest Palindromic Subsequence II | 1682 | 帶限制的 DP | Medium | DP 子序列 |
| Count Different Palindromic Subsequences | 730 | DP + 字元計數 | Hard | DP 進階 |

### **模式 5：進階回文問題**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Longest Chunked Palindrome Decomposition | 1147 | 貪婪 + 雙指標 | Hard | 進階 |
| Check If Word Is Valid After Substitutions | 1003* | 堆疊（類回文結構） | Medium | 進階 |
| Unique Length-3 Palindromic Subsequences | 1930 | 集合 + 字元分析 | Medium | 進階 |
| Maximum Product of Length of Palindromic Subsequences | 1930* | Bitmask + DP | Hard | 進階 |
| Find Palindrome With Fixed Length | 2217 | 數學構造 | Medium | 數字回文 |

### **模式 6：數字回文**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Palindrome Number | 9 | 數學反轉 | Easy | 數字回文 |
| Largest Palindrome Product | 479 | 數學構造 | Hard | 數字回文 |
| Super Palindromes | 906 | 數學列舉 | Hard | 數字回文 |
| Closest Palindrome | 564 | 數學分析 | Hard | 數字回文 |
| Palindromic Prime Numbers | Custom | 數論 | Medium | 數字回文 |

### **補充練習題**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Reverse String | 344 | 雙指標基本功 | Easy | 雙指標 |
| Reverse Words in a String III | 557 | 每個單字用一次雙指標 | Easy | 雙指標 |
| Find the Difference | 389 | 字元計數 | Easy | 進階 |
| Reverse Only Letters | 917 | 雙指標加過濾 | Easy | 雙指標 |
| Valid Parentheses | 20 | 堆疊（對稱概念） | Easy | 進階 |
| Longest Palindromic Path in Tree | Custom | 樹上 DP | Hard | 進階 |
| Palindrome Removal | Custom | 區間 DP | Hard | DP 進階 |
| Count Palindromic Paths | Custom | 樹的走訪 | Medium | 進階 |

### **題型分類總結**
- **題目總數**：40+ 題已分類
- **Easy**：8 題（20%）
- **Medium**：20 題（50%） 
- **Hard**：12+ 題（30%）
- **最常見的模式**：中心擴散、雙指標、動態規劃
- **進階技巧**：回溯、位元運算、數學構造

### **模板使用比例**
- **雙指標**：25% 的題目
- **中心擴散**：20% 的題目  
- **動態規劃**：30% 的題目
- **回溯**：10% 的題目
- **進階／混合**：15% 的題目

## 模式選擇框架

### 回文問題決策流程圖

```text
Problem Analysis Flowchart:

1. Is this a simple validation problem (check if input is palindrome)?
   ├── YES → Use Template 1: Two Pointers
   │   ├── String/Array input → Two pointers from ends
   │   ├── Linked List input → Two pointers + reverse half
   │   └── Integer input → Template 6: Number Palindrome
   └── NO → Continue to 2

2. Do you need to find/count palindromic substrings?
   ├── YES → 
   │   ├── Find longest substring → Template 2: Center Expansion
   │   ├── Count all substrings → Template 2: Center Expansion (optimized)
   │   └── Complex substring queries → Template 3: Dynamic Programming
   └── NO → Continue to 3

3. Do you need to partition/construct palindromes?
   ├── YES →
   │   ├── Find all partitions → Template 4: Backtracking
   │   ├── Minimum cuts needed → Template 3: DP + preprocessing
   │   └── Construct specific palindrome → Advanced techniques
   └── NO → Continue to 4

4. Do you need to modify string to make it palindrome?
   ├── YES →
   │   ├── Minimum insertions/deletions → Template 5: DP Subsequence
   │   ├── With k operations allowed → Template 3: DP with constraints
   │   └── Optimal transformation → Advanced DP techniques
   └── NO → Continue to 5

5. Is this a number-based palindrome problem?
   ├── YES → Template 6: Number Palindrome
   │   ├── Check palindrome number → Mathematical reversal
   │   ├── Generate palindromes → Mathematical construction
   │   └── Complex number constraints → Advanced math
   └── NO → Continue to 6

6. Advanced/Complex palindrome problem?
   ├── YES → Hybrid approach needed
   │   ├── Multiple patterns combined → Use multiple templates
   │   ├── Additional constraints → Modify existing templates
   │   └── Novel problem type → Design custom solution
   └── NO → Re-analyze problem requirements
```

### 模板選擇指南

#### **每個模板該用在哪：**

**模板 1 — 雙指標：**
- ✅ 單純的回文驗證
- ✅ 需要過濾字元（空白、標點）的題目
- ✅ 鏈結串列的回文檢查
- ✅ 要求 O(1) 空間時
- ❌ 要找出所有回文子字串
- ❌ 複雜的計數／構造問題

**模板 2 — 中心擴散：**
- ✅ 找最長回文子字串
- ✅ 計算回文子字串個數
- ✅ 需要知道子字串實際位置時
- ✅ 計數時要求 O(1) 空間
- ❌ 子序列問題（不連續）
- ❌ 複雜的編輯距離情境

**模板 3 — 動態規劃：**
- ✅ 子問題重疊的計數題
- ✅ 變成回文的最少操作次數
- ✅ 同一字串要查詢多次
- ✅ 多重限制組合
- ❌ 單純驗證（殺雞用牛刀）
- ❌ 空間非常吃緊時

**模板 4 — 回溯：**
- ✅ 列出所有回文切割方式
- ✅ 找特定的切割樣式
- ✅ 限制滿足問題
- ✅ 需要所有可行解時
- ❌ 只要算解的個數（改用 DP）
- ❌ 輸入非常大（指數時間）

**模板 5 — DP 子序列：**
- ✅ 最長回文子序列
- ✅ 最少編輯操作
- ✅ 刪除／插入字元的問題
- ✅ 順序重要但不要求連續時
- ❌ 連續子字串問題
- ❌ 單純的驗證任務

**模板 6 — 數字回文：**
- ✅ 整數回文驗證
- ✅ 用數學構造回文
- ✅ 數論相關問題
- ✅ 轉字串太沒效率時
- ❌ 以字串為主的回文題
- ❌ 複雜的字元處理

### 解題策略

#### **一步一步來：**

1. **辨認核心任務：**
   - 驗證 vs 尋找 vs 計數 vs 構造
   - 只要一個答案 vs 要所有答案

2. **分析輸入限制：**
   - 是字串、陣列、數字，還是鏈結串列？
   - 規模限制（會影響演算法選擇）
   - 有沒有特殊字元、需不需要過濾？

3. **確認輸出需求：**
   - 布林值、個數、實際的回文，還是位置？
   - 一個答案還是全部答案？

4. **選定基礎模板：**
   - 用上面的決策流程圖
   - 考量時間／空間複雜度的要求

5. **針對題目調整模板：**
   - 需要的話加上過濾邏輯
   - 依大小寫敏感度調整
   - 子問題重疊就加上記憶化

6. **需要時再最佳化：**
   - 空間最佳化（二維 DP → 一維 DP）
   - 提早結束的條件
   - 多次查詢就先做預處理

### 常見題型辨識

#### **關鍵字訊號：**
- **「判斷是不是回文」** → 雙指標
- **「最長回文子字串」** → 中心擴散  
- **「計算回文子字串個數」** → 中心擴散或 DP
- **「所有回文切割」** → 回溯
- **「最少插入／刪除次數」** → DP 子序列
- **「回文數字」** → 數字回文
- **「改 k 個字元能不能變回文」** → 帶限制的 DP

## 總結與速查

### 複雜度速查
| 模板 | 時間複雜度 | 空間複雜度 | 最適合 | 備註 |
|----------|----------------|------------------|----------|--------|
| **雙指標** | O(n) | O(1) | 基本驗證 | 單純檢查時效率最好 |
| **中心擴散** | O(n²) | O(1) | 子字串問題 | 時間／空間取捨得宜 |
| **反轉後比對** | O(n) | O(n) | 簡單題 | 好寫好懂 |
| **DP（二維）** | O(n²) | O(n²) | 複雜計數 | 能處理重疊子問題 |
| **DP 子序列** | O(n²) | O(n²) → O(n) | 編輯距離類 | 空間可壓縮 |
| **回溯** | O(n·2^n) | O(n) | 產生所有切割 | 指數時間 |
| **數字回文** | O(log n) | O(1) | 整數問題 | 不必轉成字串 |

### 模板速查
| 模板 | 用途 | 關鍵程式片段 |
|----------|---------|------------------|
| **雙指標** | 驗證 | `while left < right: check s[left] == s[right]` |
| **中心擴散** | 子字串 | `expand_around_center(left, right)` |
| **DP 計數** | 複雜計數 | `dp[i][j] = (s[i] == s[j]) and dp[i+1][j-1]` |
| **回溯** | 所有切割 | `if is_palindrome(start, end): backtrack(end+1)` |
| **DP 子序列** | LPS／編輯距離 | `dp[i][j] = dp[i+1][j-1] + 2 if s[i] == s[j]` |
| **數學做法** | 整數回文 | `reversed_half = reversed_half * 10 + x % 10` |

### 常見模式與技巧

#### **雙指標的各種變形**
```python
# Basic two pointers
def is_palindrome_basic(s):
    left, right = 0, len(s) - 1
    while left < right:
        if s[left] != s[right]:
            return False
        left += 1
        right -= 1
    return True

# With character filtering
def is_palindrome_alphanumeric(s):
    left, right = 0, len(s) - 1
    while left < right:
        while left < right and not s[left].isalnum():
            left += 1
        while left < right and not s[right].isalnum():
            right -= 1
        if s[left].lower() != s[right].lower():
            return False
        left += 1
        right -= 1
    return True

# Allow one mismatch
def is_palindrome_one_delete(s):
    def helper(left, right, deleted):
        while left < right:
            if s[left] != s[right]:
                if deleted:  # Already used one delete
                    return False
                # Try deleting left or right character
                return (helper(left + 1, right, True) or 
                       helper(left, right - 1, True))
            left += 1
            right -= 1
        return True
    return helper(0, len(s) - 1, False)
```

#### **中心擴散的各種寫法**
```python
# Standard center expansion
def expand_around_center(s, left, right):
    while left >= 0 and right < len(s) and s[left] == s[right]:
        left -= 1
        right += 1
    return right - left - 1

# With counting
def count_palindromes_at_center(s, left, right):
    count = 0
    while left >= 0 and right < len(s) and s[left] == s[right]:
        count += 1
        left -= 1
        right += 1
    return count

# For all centers
def find_all_palindromes(s):
    palindromes = []
    for i in range(len(s)):
        # Odd length
        left, right = i, i
        while left >= 0 and right < len(s) and s[left] == s[right]:
            palindromes.append(s[left:right+1])
            left -= 1
            right += 1
        # Even length
        left, right = i, i + 1
        while left >= 0 and right < len(s) and s[left] == s[right]:
            palindromes.append(s[left:right+1])
            left -= 1
            right += 1
    return palindromes
```

#### **DP 最佳化技巧**
```python
# Space optimization: 2D DP to 1D
def longest_palindromic_subsequence_1D(s):
    n = len(s)
    dp = [1] * n  # dp[j] represents length for current i to j
    
    for i in range(n - 2, -1, -1):
        prev = 0  # dp[i+1][j-1]
        for j in range(i + 1, n):
            temp = dp[j]  # Save current dp[j] for next iteration's prev
            if s[i] == s[j]:
                dp[j] = prev + 2
            else:
                dp[j] = max(dp[j], dp[j-1])
            prev = temp
    
    return dp[n-1]

# Preprocessing palindrome table
def precompute_palindromes(s):
    n = len(s)
    is_palin = [[False] * n for _ in range(n)]
    
    # Single characters
    for i in range(n):
        is_palin[i][i] = True
    
    # Two characters
    for i in range(n - 1):
        is_palin[i][i+1] = (s[i] == s[i+1])
    
    # Three+ characters
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            is_palin[i][j] = (s[i] == s[j] and is_palin[i+1][j-1])
    
    return is_palin
```

### 解題步驟

1. **分析題目**
   - 仔細讀題，抓出關鍵需求
   - 判斷是驗證、尋找、計數，還是構造
   - 記下限制（大小寫敏感、特殊字元等等）

2. **辨識模式**
   - 用關鍵字訊號猜出可能的做法
   - 對照決策流程圖挑模板
   - 複雜題可以考慮混合做法

3. **選定模板**
   - 依需求挑最合適的模板
   - 考量時間／空間複雜度限制
   - 先想好邊界情況與最佳化空間

4. **實作策略**
   - 先寫出基本模板骨架
   - 再加上這題特有的調整
   - 處理邊界情況（空字串、單一字元等等）

5. **測試與最佳化**
   - 用題目給的範例測
   - 想想邊界情況："", "a", "ab", "aba"
   - 需要的話壓空間（二維 DP → 一維 DP）

### 常見錯誤與建議

**常見錯誤：**
- 中心擴散的邊界檢查出現 **差一錯誤**
- 題目要求區分 **大小寫** 時沒處理
- 判斷英數字時的 **字元過濾** 邏輯寫錯
- 題目要求時沒有做 **空間最佳化**
- 數字回文題發生 **整數溢位**
- **回溯** 沒寫好終止條件或剪枝
- 子序列問題的 **DP 狀態定義** 定錯

**好習慣：**
- 存取陣列元素前 **一律先檢查邊界**
- **空輸入** 與單一字元的情況分開處理
- left/right 或 start/end 指標 **命名要一致**
- **把想法寫成註解**，DP 轉移式尤其要寫
- 先寫出正確解，**再考慮空間最佳化**
- 送出前 **把邊界情況測過一輪**
- **善用輔助函式**，讓主邏輯乾淨好讀

### 面試技巧

1. **先釐清題目**
   - 問清楚大小寫要不要區分
   - 確認空白／標點是否忽略
   - 搞懂期望的輸出格式

2. **講清楚做法**
   - 說明你選這個模板的理由
   - 一開始就講時間／空間複雜度
   - 談談可能的最佳化

3. **有條理地寫**
   - 先搭骨架，再補細節
   - 明確處理邊界情況
   - 變數名稱要有意義

4. **測試要徹底**
   - 拿範例一步一步走過
   - 測邊界情況："", "a", "ab", "aba", "abcba"
   - 驗證自己的複雜度分析

5. **常見追問**
   - 「空間複雜度還能再壓嗎？」
   - 「如果允許 k 個字元不匹配呢？」
   - 「輸入非常大要怎麼處理？」
   - 「能不能不用額外空間？」

### 相關主題
- **雙指標**：一般的雙指標技巧與應用
- **動態規劃**：編輯距離、最長公共子序列
- **字串演算法**：模式比對、字串處理
- **回溯**：限制滿足、組合問題  
- **雜湊表**：字元頻率統計、變位詞問題
- **滑動視窗**：帶限制的子字串問題
- **樹演算法**：路徑問題、對稱樹驗證

## LC 範例

### 2-1) Longest Palindromic Substring (LC 5) — 中心擴散
> 對每個中心（字元本身或字元之間的縫），只要還是回文就往外擴。

```java
// LC 5 - Longest Palindromic Substring
// IDEA: Expand around center — O(N^2) with O(1) space
// time = O(N^2), space = O(1)
public String longestPalindrome(String s) {
    int start = 0, maxLen = 1;
    for (int i = 0; i < s.length(); i++) {
        // odd length
        int len1 = expand(s, i, i);
        // even length
        int len2 = expand(s, i, i + 1);
        int len = Math.max(len1, len2);
        if (len > maxLen) {
            maxLen = len;
            start = i - (len - 1) / 2;
        }
    }
    return s.substring(start, start + maxLen);
}
private int expand(String s, int l, int r) {
    while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
    return r - l - 1;
}
```

### 2-2) Valid Palindrome (LC 125) — 雙指標
> 跳過非英數字元，從兩端往中間比。

```java
// LC 125 - Valid Palindrome
// IDEA: Two pointers — skip non-alphanumeric, compare chars
// time = O(N), space = O(1)
public boolean isPalindrome(String s) {
    int l = 0, r = s.length() - 1;
    while (l < r) {
        while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
        while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;
        if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) return false;
        l++; r--;
    }
    return true;
}
```

### 2-3) Palindromic Substrings (LC 647) — 計算所有回文
> 對每個中心擴散，每擴成功一次就算一個回文。

```java
// LC 647 - Palindromic Substrings
// IDEA: Expand around center — count all palindromes
// time = O(N^2), space = O(1)
public int countSubstrings(String s) {
    int count = 0;
    for (int i = 0; i < s.length(); i++) {
        count += expand(s, i, i);     // odd length
        count += expand(s, i, i + 1); // even length
    }
    return count;
}
private int expand(String s, int l, int r) {
    int cnt = 0;
    while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
        cnt++; l--; r++;
    }
    return cnt;
}
```

### 2-4) Palindrome Number (LC 9) — 數學反轉
> 反轉數字的後半段再比對；負數與尾端是 0 的數字都不是回文。

```java
// LC 9 - Palindrome Number
// IDEA: Reverse second half; compare with first half (avoids string conversion)
// time = O(log N), space = O(1)
public boolean isPalindrome(int x) {
    if (x < 0 || (x % 10 == 0 && x != 0)) return false;
    int rev = 0;
    while (x > rev) { rev = rev * 10 + x % 10; x /= 10; }
    return x == rev || x == rev / 10;  // even/odd length
}
```

### 2-5) Valid Palindrome II (LC 680) — 雙指標 + 跳過一個
> 雙指標；碰到不匹配時，分別試著跳過左邊或右邊的字元，再檢查剩下的部分。

```java
// LC 680 - Valid Palindrome II
// IDEA: Two pointers — on mismatch, try skipping left or right character
// time = O(N), space = O(1)
public boolean validPalindrome(String s) {
    int l = 0, r = s.length() - 1;
    while (l < r) {
        if (s.charAt(l) != s.charAt(r))
            return isPalin(s, l+1, r) || isPalin(s, l, r-1);
        l++; r--;
    }
    return true;
}
private boolean isPalin(String s, int l, int r) {
    while (l < r) { if (s.charAt(l++) != s.charAt(r--)) return false; }
    return true;
}
```

### 2-6) Palindrome Linked List (LC 234) — 快慢指標 + 反轉
> 用快慢指標找中點，反轉後半段，再比對兩半。

```java
// LC 234 - Palindrome Linked List
// IDEA: Find mid (slow/fast), reverse second half, compare with first half
// time = O(N), space = O(1)
public boolean isPalindrome(ListNode head) {
    ListNode slow = head, fast = head;
    while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
    ListNode rev = reverse(slow);
    ListNode p = head, q = rev;
    while (q != null) { if (p.val != q.val) return false; p = p.next; q = q.next; }
    return true;
}
private ListNode reverse(ListNode head) {
    ListNode prev = null;
    while (head != null) { ListNode next = head.next; head.next = prev; prev = head; head = next; }
    return prev;
}
```

### 2-7) Longest Palindromic Subsequence (LC 516) — 區間 DP
> dp[i][j] = s[i..j] 的 LPS 長度；兩端相同就加 2，否則取子問題的較大值。

```java
// LC 516 - Longest Palindromic Subsequence
// IDEA: Interval DP — dp[i][j] = LPS length of s[i..j]
// time = O(N^2), space = O(N^2)
public int longestPalindromeSubseq(String s) {
    int n = s.length();
    int[][] dp = new int[n][n];
    for (int i = 0; i < n; i++) dp[i][i] = 1;
    for (int len = 2; len <= n; len++)
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            dp[i][j] = s.charAt(i) == s.charAt(j)
                ? dp[i+1][j-1] + 2
                : Math.max(dp[i+1][j], dp[i][j-1]);
        }
    return dp[0][n-1];
}
```

#### 2-7 的變形：Valid Palindrome III (LC 1216) — 「最多刪 k 個」
> **轉折**：同一張 LPS 表，只有最後問的問題不一樣 — 把不屬於 LPS 的字元刪掉，就是把 `s` 變成回文最省的做法，所以答案是 `N - LPS(s) <= k`。

```java
// LC 1216 - Valid Palindrome III
// IDEA: min deletions to make palindrome = N - LPS(s); answer = (N - LPS) <= k
// time = O(N^2), space = O(N^2)
public boolean isValidPalindrome(String s, int k) {
    int n = s.length();
    int[][] dp = new int[n][n];                 // dp[i][j] = LPS length of s[i..j]
    for (int i = 0; i < n; i++) dp[i][i] = 1;
    for (int len = 2; len <= n; len++)
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            dp[i][j] = s.charAt(i) == s.charAt(j)
                ? dp[i+1][j-1] + 2
                : Math.max(dp[i+1][j], dp[i][j-1]);
        }
    return n - dp[0][n-1] <= k;
}
```

```python
# python
# LC 1216 - Valid Palindrome III
# IDEA: min deletions to make palindrome = N - LPS(s); answer = (N - LPS) <= k
# time = O(N^2), space = O(N^2)
def isValidPalindrome(s, k):
    n = len(s)
    dp = [[0] * n for _ in range(n)]            # dp[i][j] = LPS length of s[i..j]
    for i in range(n):
        dp[i][i] = 1
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            dp[i][j] = (dp[i+1][j-1] + 2) if s[i] == s[j] else max(dp[i+1][j], dp[i][j-1])
    return n - dp[0][n-1] <= k
```

### 2-8) Minimum Insertion Steps to Make a String Palindrome (LC 1312) — 區間 DP
> 最少插入次數 = N − LPS 長度；等價於用插入把字串補成回文。

```java
// LC 1312 - Minimum Insertion Steps to Make String Palindrome
// IDEA: minInsertions = s.length() - LPS(s)
// time = O(N^2), space = O(N^2)
public int minInsertions(String s) {
    int n = s.length();
    int[][] dp = new int[n][n];
    for (int len = 2; len <= n; len++)
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            dp[i][j] = s.charAt(i) == s.charAt(j)
                ? dp[i+1][j-1]
                : Math.min(dp[i+1][j], dp[i][j-1]) + 1;
        }
    return dp[0][n-1];
}
```

### 2-9) Palindrome Partitioning (LC 131) — 回溯
> DFS 加回溯；在每個索引嘗試所有是回文的前綴當作下一段。

```java
// LC 131 - Palindrome Partitioning
// IDEA: Backtracking — at each position try all palindromic prefixes; recurse on suffix
// time = O(N * 2^N), space = O(N)
public List<List<String>> partition(String s) {
    List<List<String>> res = new ArrayList<>();
    backtrack(s, 0, new ArrayList<>(), res);
    return res;
}
private void backtrack(String s, int start, List<String> path, List<List<String>> res) {
    if (start == s.length()) { res.add(new ArrayList<>(path)); return; }
    for (int end = start + 1; end <= s.length(); end++) {
        String sub = s.substring(start, end);
        if (isPalin(sub)) {
            path.add(sub);
            backtrack(s, end, path, res);
            path.remove(path.size() - 1);
        }
    }
}
private boolean isPalin(String s) {
    int l = 0, r = s.length() - 1;
    while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false;
    return true;
}
```

### 2-10) Palindrome Partitioning II (LC 132) — DP
> dp[i] = s[0..i] 的最少切割次數；先預先算好回文表，或用中心擴散求。

```java
// LC 132 - Palindrome Partitioning II
// IDEA: DP — dp[i] = min cuts for s[0..i]; precompute isPalin table
// time = O(N^2), space = O(N^2)
public int minCut(String s) {
    int n = s.length();
    boolean[][] isPalin = new boolean[n][n];
    for (int i = n-1; i >= 0; i--)
        for (int j = i; j < n; j++)
            isPalin[i][j] = s.charAt(i) == s.charAt(j) && (j - i < 2 || isPalin[i+1][j-1]);
    int[] dp = new int[n];
    Arrays.fill(dp, Integer.MAX_VALUE);
    for (int i = 0; i < n; i++) {
        if (isPalin[0][i]) { dp[i] = 0; continue; }
        for (int j = 1; j <= i; j++)
            if (isPalin[j][i]) dp[i] = Math.min(dp[i], dp[j-1] + 1);
    }
    return dp[n-1];
}
```

### 2-11) Find Longest Awesome Substring (LC 1542) — Bitmask + 前綴 XOR
> awesome 的定義是「最多一個字元出現奇數次」；用前綴 XOR 記錄狀態。

```java
// LC 1542 - Find Longest Awesome Substring
// IDEA: Prefix XOR bitmask; awesome substring iff XOR has at most 1 set bit
// time = O(10 * N), space = O(1024)
public int longestAwesome(String s) {
    int[] seen = new int[1024];
    Arrays.fill(seen, s.length());
    seen[0] = -1;
    int prefix = 0, ans = 0;
    for (int i = 0; i < s.length(); i++) {
        prefix ^= 1 << (s.charAt(i) - '0');
        if (seen[prefix] <= s.length()) ans = Math.max(ans, i - seen[prefix]);
        else seen[prefix] = i;
        for (int d = 0; d <= 9; d++) {  // try one odd-count digit
            int mask = prefix ^ (1 << d);
            if (seen[mask] <= s.length()) ans = Math.max(ans, i - seen[mask]);
        }
    }
    return ans;
}
```

## 進階模板（線性時間與字串比對）

> 這幾個模板補上上面 O(N²) 模板留下的缺口：**Manacher** 讓「最長回文子字串」變成線性，**KMP** 把「把字串補成回文」變成查前綴函數，而 **雜湊表拆分** 技巧讓「哪些配對串起來是回文」從 O(N²·L) 降到接近線性。

### 模板 7：Manacher 演算法（O(N) 求最長回文子字串）— LC 5 ⭐⭐⭐⭐

**核心想法**：插入分隔符（`#`），讓每個回文長度都變成**奇數**（不用再分奇偶兩種情況），再維護一個「目前最靠右的回文」`[center, right]`。對窗內的新索引 `i`，它的**鏡像** `2*center - i` 已經算過半徑 — 直接拿來當免費的下界，只要往外多擴的部分才需要真的比對。每個字元最多被 `right` 納入一次 ⇒ 線性。

**什麼時候用**：N 很大（10⁵+），O(N²) 的中心擴散會 TLE；或是面試官問「有沒有比 O(N²) 更好的做法？」。

```java
// java
// LC 5 - Longest Palindromic Substring (Manacher)
// IDEA: transform s -> ^#a#b#a#$ so all palindromes are odd length; reuse mirror radii inside [center, right]
// time = O(N), space = O(N)
public String longestPalindrome(String s) {
    if (s == null || s.isEmpty()) return "";
    StringBuilder sb = new StringBuilder("^");        // ^ and $ are sentinels: stop expansion, no bounds check
    for (char c : s.toCharArray()) sb.append('#').append(c);
    sb.append("#$");
    char[] t = sb.toString().toCharArray();
    int n = t.length;
    int[] p = new int[n];                              // p[i] = palindrome radius at i (== length in original s)
    int center = 0, right = 0;
    for (int i = 1; i < n - 1; i++) {
        if (i < right) p[i] = Math.min(right - i, p[2 * center - i]);   // mirror trick
        while (t[i + p[i] + 1] == t[i - p[i] - 1]) p[i]++;              // expand beyond what was reused
        if (i + p[i] > right) { center = i; right = i + p[i]; }         // new rightmost palindrome
    }
    int best = 0, bestCenter = 0;
    for (int i = 1; i < n - 1; i++)
        if (p[i] > best) { best = p[i]; bestCenter = i; }
    int start = (bestCenter - best) / 2;               // map back to index in s
    return s.substring(start, start + best);
}
```

```python
# python
# LC 5 - Longest Palindromic Substring (Manacher)
# IDEA: transform s -> ^#a#b#a#$ so all palindromes are odd length; reuse mirror radii inside [center, right]
# time = O(N), space = O(N)
def longestPalindrome(s):
    if not s:
        return ""
    t = "^#" + "#".join(s) + "#$"                  # sentinels ^ $ never match -> no bounds check
    n = len(t)
    p = [0] * n                                    # p[i] = radius at i == palindrome length in original s
    center = right = 0
    for i in range(1, n - 1):
        if i < right:
            p[i] = min(right - i, p[2 * center - i])   # mirror trick
        while t[i + p[i] + 1] == t[i - p[i] - 1]:      # expand beyond reused radius
            p[i] += 1
        if i + p[i] > right:
            center, right = i, i + p[i]
    best = max(range(1, n - 1), key=lambda i: p[i])
    start = (best - p[best]) // 2                  # map back to index in s
    return s[start:start + p[best]]
```

**容易踩到的坑**
- 轉換後字串的 `p[i]` 等於原字串裡的回文**長度**（這正是補 `#` 的用意）。
- 在 `s` 裡的起始索引是 `(center - radius) / 2`。
- 鏡像值只能用到 `right - i` 為止；超過 `right` 的部分沒驗證過，必須實際擴。

---

### 模板 8：用 KMP 前綴函數延展回文 — LC 214 ⭐⭐⭐⭐

**核心想法**：「在前面補最少字元讓 `s` 變回文」⇔ 找出 `s` 的**最長回文前綴**。組出 `t = s + "#" + reverse(s)`，跑 KMP 的失配函數：`fail[last]` 就是「同時是 `s` 的前綴、也是 `reverse(s)` 的後綴」的最長長度 — 也就是最長回文前綴。`#` 分隔符是為了避免比對跨過中線。

**什麼時候用**：任何「在其中一端加字元把它變成回文」的題目，或需要在 O(N) 內求最長回文前綴／後綴時。

```java
// java
// LC 214 - Shortest Palindrome
// IDEA: longest palindromic prefix = KMP failure value of (s + "#" + reverse(s)); prepend the reversed remainder
// time = O(N), space = O(N)
public String shortestPalindrome(String s) {
    if (s.length() < 2) return s;
    String rev = new StringBuilder(s).reverse().toString();
    String t = s + "#" + rev;                     // '#' must not appear in s -> blocks overlap
    int[] fail = new int[t.length()];             // fail[i] = longest proper prefix that is also suffix of t[0..i]
    for (int i = 1; i < t.length(); i++) {
        int j = fail[i - 1];
        while (j > 0 && t.charAt(i) != t.charAt(j)) j = fail[j - 1];
        if (t.charAt(i) == t.charAt(j)) j++;
        fail[i] = j;
    }
    int longestPalPrefix = fail[t.length() - 1];
    return new StringBuilder(s.substring(longestPalPrefix)).reverse() + s;
}
```

```python
# python
# LC 214 - Shortest Palindrome
# IDEA: longest palindromic prefix = KMP failure value of (s + "#" + reverse(s)); prepend the reversed remainder
# time = O(N), space = O(N)
def shortestPalindrome(s):
    if len(s) < 2:
        return s
    t = s + "#" + s[::-1]                      # '#' must not appear in s -> blocks overlap
    fail = [0] * len(t)                        # fail[i] = longest proper prefix == suffix of t[0..i]
    for i in range(1, len(t)):
        j = fail[i - 1]
        while j > 0 and t[i] != t[j]:
            j = fail[j - 1]
        if t[i] == t[j]:
            j += 1
        fail[i] = j
    return s[fail[-1]:][::-1] + s              # prepend reverse of the non-palindromic tail
```

**容易踩到的坑**
- 少了 `#` 分隔符，`s = "aaaa"` 的前綴比對會滑過邊界，回傳偏大的值。
- 鏡像版本：想改成在**後面**接字元，就改用最長回文**後綴** — 組出 `t = reverse(s) + "#" + s`。

---

### 模板 9：用 HashMap 拆分求回文配對 — LC 336 ⭐⭐⭐

**核心想法**：`words[i] + words[j]` 只有兩種可能的形狀會是回文。把每個單字 `w` 在每個位置切成 `prefix + suffix`：
- 若 `prefix` 是回文，而 `reverse(suffix)` 剛好是另一個單字 ⇒ `reverse(suffix) + w` 是回文（那個單字放**前面**），
- 若 `suffix` 是回文，而 `reverse(prefix)` 剛好是另一個單字 ⇒ `w + reverse(prefix)` 是回文（那個單字放**後面**）。

這把 O(N²·L) 的兩兩比對換成 O(N·L²) 的查表。（把反轉後的單字建成字典樹（Trie）可以得到同樣的複雜度，而且不用雜湊。）

```java
// java
// LC 336 - Palindrome Pairs
// IDEA: split each word at every cut; palindromic half + reversed other half looked up in a HashMap
// time = O(N * L^2), space = O(N * L)
public List<List<Integer>> palindromePairs(String[] words) {
    Map<String, Integer> idx = new HashMap<>();
    for (int i = 0; i < words.length; i++) idx.put(words[i], i);
    List<List<Integer>> res = new ArrayList<>();
    for (int i = 0; i < words.length; i++) {
        String w = words[i];
        for (int j = 0; j <= w.length(); j++) {          // note: <= so the empty suffix is covered
            String prefix = w.substring(0, j), suffix = w.substring(j);
            if (isPalin(prefix)) {                       // rev(suffix) + prefix + suffix
                Integer k = idx.get(new StringBuilder(suffix).reverse().toString());
                if (k != null && k != i) res.add(Arrays.asList(k, i));
            }
            if (j < w.length() && isPalin(suffix)) {     // j < len avoids double-counting equal-length pairs
                Integer k = idx.get(new StringBuilder(prefix).reverse().toString());
                if (k != null && k != i) res.add(Arrays.asList(i, k));
            }
        }
    }
    return res;
}
// isPalin(...) — reuse the helper from section 2-9
```

```python
# python
# LC 336 - Palindrome Pairs
# IDEA: split each word at every cut; palindromic half + reversed other half looked up in a dict
# time = O(N * L^2), space = O(N * L)
def palindromePairs(words):
    idx = {w: i for i, w in enumerate(words)}
    res = []
    for i, w in enumerate(words):
        for j in range(len(w) + 1):                  # note: +1 so the empty suffix is covered
            prefix, suffix = w[:j], w[j:]
            if prefix == prefix[::-1]:               # rev(suffix) + prefix + suffix
                k = idx.get(suffix[::-1])
                if k is not None and k != i:
                    res.append([k, i])
            if j < len(w) and suffix == suffix[::-1]:  # j < len avoids double-counting equal-length pairs
                k = idx.get(prefix[::-1])
                if k is not None and k != i:
                    res.append([i, k])
    return res
```

**容易踩到的坑**
- 第二個分支上的 `j < len(w)` 條件，就是用來擋掉像 `["bat","tab"]` 產生兩次 `[0,1]` 這種重複。
- `words` 裡的空字串會跟每個回文單字配成一對 — 靠 `j` 的範圍端點就處理掉了，不要特判把它拿掉。
- 一定要檢查 `k != i`；否則回文單字會跟自己配成一對。

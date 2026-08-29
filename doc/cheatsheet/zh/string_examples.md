# 字串演算法 — 實作範例

> **範圍** — 字串類 LeetCode 的範例庫，每題每種語言只留一份標準解，並歸檔到它所實作的母文件模板底下；概念、模式目錄與模板本身都留在主字串文件。
> **另見**：[string.md](./string.md) — 母文件，每個範例對應的模板都在那裡；[string_operations.md](./string_operations.md) — 語言層級的字串 API，從同一份檔案拆出；[palindrome.md](./palindrome.md) — 回文家族的深入版；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 子字串搜尋；[sliding_window.md](./sliding_window.md) — 字元視窗類題目；[2_pointers_examples.md](./2_pointers_examples.md) — 雙指標範例庫，LC 165、524、763、809 和 953 那邊也有。

## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)

## 總覽

一題一節，編號連續不跳號。每題只出現一次。
先去 [string.md](./string.md) 讀它對應的模板，再回來看範例。

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

## LC 範例

### 2-1) Compare Version Number — LC 165
> **因重複而刪除**：另一份 Python 版本，從前面 pop 之後再跑兩個收尾迴圈 — 拆解再比對的做法完全相同；留下來的版本改成把較短的版本號補零。

- 同時走訪兩個字串，逐段比較數字
```python
# 165 Compare Version Number
# IDEA : STRING
class Solution(object):
    def compareVersion(self, version1, version2):
        v1_split = version1.split('.')
        v2_split = version2.split('.')
        v1_len, v2_len = len(v1_split), len(v2_split)
        maxLen = max(v1_len, v2_len)
        for i in range(maxLen):
            temp1, temp2 = 0, 0
            if i < v1_len:
                temp1 = int(v1_split[i])
            if i < v2_len:
                temp2 = int(v2_split[i])
            if temp1 < temp2:
                return -1
            elif temp1 > temp2:
                return 1
        return 0
```

### 2-2) Add Two Numbers II,  Decode String
> **因重複而刪除**：`str_2_int_v2` — 一樣是逐位累加，只是寫成 `(res + int(i) % 10) * 10` 再除回來。

- String -> Int
```text
# 445 Add Two Numbers II
# 394 Decode String
def str_2_int(x):
    r=0
    for i in x:
        r = int(r)*10 + int(i)
        print (i, r)
    return r

# example 1
x="131"
r=str_2_int(x)
print (r)
# 1 1
# 3 13
# 1 131
# 131

# examle 2
In [62]: z
Out[62]: '5634'

In [63]: ans = 0

In [64]: for i in z:
    ...:     ans = 10 * ans + int(i)
    ...:

In [65]: ans
Out[65]: 5634
```

### 2-3) Count and say — LC 38
```python
# LC 038 Count and say
# IDEA : ITERATION
class Solution:
    def countAndSay(self, n):
        
        val = ""
        res = "1"
        
        for _ in range(n-1):
            cnt = 1
            for j in range(len(res)-1):
                if res[j]==res[j+1]:
                    cnt+=1
                else:
                    val += str(cnt) + res[j]
                    cnt = 1
            val += str(cnt)+res[-1]
            res = val
            val = ""
        return res
```

### 2-4) Monotone Increasing Digits — LC 738
```python
# LC 738 Monotone Increasing Digits
class Solution:
    def monotoneIncreasingDigits(self, N):
        s = list(str(N));
        ### NOTICE HERE 
        for i in range(len(s) - 2,-1,-1):
            # if int(s[i]) > int(s[i+1]) -> the string is not `monotone increase`
            # -> we need to find the next biggest int, 
            # -> so we need to make all right hand side digit as '9'
            # -> and minus current digit with 1  (s[i] = str(int(s[i]) - 1))
            if int(s[i]) > int(s[i+1]):
                ### NOTICE HERE 
                for j in range(i+1,len(s)):
                    s[j] = '9'
                s[i] = str(int(s[i]) - 1)
        s = "".join(s)        
        return int(s) 
```

### 2-5) Validate IP Address — LC 468
```python
# LC 468. Validate IP Address
# IDEA : Divide and Conquer
class Solution:
    def validate_IPv4(self, IP):
        nums = IP.split('.')
        for x in nums:
            # Validate integer in range (0, 255):
            # 1. length of chunk is between 1 and 3
            if len(x) == 0 or len(x) > 3:
                return "Neither"
            # 2. no extra leading zeros
            # 3. only digits are allowed
            # 4. less than 255
            if x[0] == '0' and len(x) != 1 or not x.isdigit() or int(x) > 255:
                return "Neither"
        return "IPv4"
    
    def validate_IPv6(self, IP):
        nums = IP.split(':')
        hexdigits = '0123456789abcdefABCDEF'
        for x in nums:
            # Validate hexadecimal in range (0, 2**16):
            # 1. at least one and not more than 4 hexdigits in one chunk
            # 2. only hexdigits are allowed: 0-9, a-f, A-F
            if len(x) == 0 or len(x) > 4 or not all(c in hexdigits for c in x):
                return "Neither"
        return "IPv6"
        
    def validIPAddress(self, IP):
        if IP.count('.') == 3:
            return self.validate_IPv4(IP)
        elif IP.count(':') == 7:
            return self.validate_IPv6(IP)
        else:
            return "Neither"
```

### 2-6) License Key Formatting — LC 482
> **因重複而刪除**：一份 30 行的「字串操作 + 暴力」版本，先去除分隔符、重新分組再接回去；留下來的反向掃描是同一個想法，九行就寫完。

```python
# LC 482. License Key Formatting
# ref : LC 725. Split Linked List in Parts

class Solution(object):
    def licenseKeyFormatting(self, S, K):
        result = []
        for i in reversed(range(len(S))):
            if S[i] == '-':
                continue
            if len(result) % (K + 1) == K:
                result += '-'
            result += S[i].upper()
        return "".join(reversed(result))
```

### 2-7) Repeated String Match — LC 686
> **因重複而刪除**：另一份 Python 版本，用同樣的 `(res-1)*sa <= 2*max(sa,sb)` 上界，只是多加了一堆邊界情況分支。

```python
# LC 686. Repeated String Match
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/repeated-string-match/discuss/108090/Intuitive-Python-2-liner
# -> if there is a sufficient solution, B must "inside" A
# -> Let n be the answer, 
# -> Let x be the theoretical lower bound, which is ceil(len(B)/len(A)).
# -> the value of n can br ONLY "x" or "x + 1"
# -> e.g. : in the case where len(B) is a multiple of len(A) like in A = "abcd" and B = "cdabcdab") and not more. Because if B is already in A * n, B is definitely in A * (n + 1).
# --> So all we need to check whether are:
#       -> 1) B in A * x
#         or
#       -> 2) B in A * (x+1)
# -> return -1 if above contitions are not met
class Solution(object):
    def repeatedStringMatch(self, A, B):
        sa, sb = len(A), len(B)
        x = 1
        while (x - 1) * sa <= 2 * max(sa, sb):
            if B in A * x: 
                return x
            x += 1
        return -1
```

### 2-8) Roman to Integer — LC 13
```python
# LC 13. Roman to Integer
class Solution(object):
    def romanToInt(self, s):
        # helper ref
        roman = {"I":1, "V":5, "X":10, "L":50, "C":100, "D":500, "M":1000}
        # NOTE : we init res as below
        res = roman[s[-1]]
        N = len(s)
        """
        2 cases:
            case 1) XY, X > Y -> res = X - Y
            case 2) XY, X < Y -> res = X + Y
        """
        for i in range(N - 2, -1, -1):
            # case 1
            if roman[s[i]] < roman[s[i + 1]]:
                res -= roman[s[i]]
            # case 2
            else:
                res += roman[s[i]]
        return res
```

### 2-9) Count Unique Characters of All Substrings of a Given String — LC 828
> **因重複而刪除**：V1 那份跟這份逐字元完全相同，差別只在用 `string.ascii_uppercase` 取代字面字母表 — 而那段程式碼從沒寫出必要的 `import string`。它的說明保留下來，放在留存的程式碼上方。

```python
# LC 828. Count Unique Characters of All Substrings of a Given String
# https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/discuss/128952/C%2B%2BJavaPython-One-pass-O(N)
# IDEA :
# Let's think about how a character can be found as a unique character.
# Think about string "XAXAXXAX" and focus on making the second "A" a unique character.
# We can take "XA(XAXX)AX" and between "()" is our substring.
# We can see here, to make the second "A" counted as a uniq character, we need to:
# insert "(" somewhere between the first and second A
# insert ")" somewhere between the second and third A
# For step 1 we have "A(XA" and "AX(A", 2 possibility.
# For step 2 we have "A)XXA", "AX)XA" and "AXX)A", 3 possibilities.
# So there are in total 2 * 3 = 6 ways to make the second A a unique character in a substring.
# In other words, there are only 6 substring, in which this A contribute 1 point as unique string.
# Instead of counting all unique characters and struggling with all possible substrings,
# we can count for every char in S, how many ways to be found as a unique char.
# We count and sum, and it will be out answer.
class Solution(object):
     def uniqueLetterString(self, S):
            index = {c: [-1, -1] for c in 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'}
            res = 0
            for i, c in enumerate(S):
                k, j = index[c]
                res += (i - j) * (j - k)
                index[c] = [j, i]
            for c in index:
                k, j = index[c]
                res += (len(S) - j) * (j - k)
            return res % (10**9 + 7)
```

### 2-10) Palindromic Substrings — LC 647
> 這題的 O(n) 中心擴展法與 Manacher 解法在 [palindrome.md](./palindrome.md)；這裡留的是 O(n³) 暴力解，用來對照。

```python
# LC 647. Palindromic Substrings
# IDEA : BRUTE FORCE
class Solution(object):
    def countSubstrings(self, s):
        count = 0
        # NOTE: since i from 0 to len(s) - 1, so for j we need to "+1" then can get go throgh all elements in str
        for i in range(len(s)):
            # Note : for j we need to "+1"
            for j in range(i+1, len(s)+1):
                if s[i:j] == s[i:j][::-1]:
                    count += 1
        return count
```

### 2-11) Repeated Substring Pattern — LC 459
> KMP／Z-array 的解法在 [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) 和 [advanced_string_algorithms.md](./advanced_string_algorithms.md)。

```python
# LC 459. Repeated Substring Pattern
# IDEA : # only have to go through till HALF of s's length, since it's not possbile to find the SubstringPattern if len(s[:x]) > size//2
class Solution(object):
    def repeatedSubstringPattern(self, s):
        _len_s = len(s)
        i = 0
        tmp = ""
        while i < _len_s:
            if i == 0:
                multiply = 0
            if i != 0:
                multiply = _len_s // i
            if multiply * tmp == s:
                return True
            if i > _len_s // 2:
                return False
            tmp += s[i]
            i += 1
        return False
```

### 2-12) Reverse Only Letters — LC 917

**模式：選擇性反轉字元**
- 只反轉英文字母
- 非字母字元留在原本的位置
- 兩種做法：雙指標或堆疊

#### 做法 1：雙指標（最佳解）
```java
// java
// LC 917. Reverse Only Letters
/**
 * Pattern: Two pointers with selective swap
 *
 * Key Technique:
 *   - Use Character.isLetter() to check if char is alphabetic
 *   - Skip non-letters on both sides
 *   - Swap only when both pointers point to letters
 *
 * Example:
 *   s = "ab-cd"
 *
 *   [a,b,-,c,d]    l=0, r=4, both letters, swap
 *    l       r     -> [d,b,-,c,a]
 *
 *   [d,b,-,c,a]    l=1, r=3, both letters, swap
 *      l   r       -> [d,c,-,b,a]
 *
 *   [d,c,-,b,a]    l=2, r=2, l >= r, done!
 *        lr
 *
 * Example 2:
 *   s = "a-bC-dEf-ghIj"
 *
 *   [a,-,b,C,-,d,E,f,-,g,h,I,j]
 *    l                       r    both letters, swap
 *   -> [j,-,b,C,-,d,E,f,-,g,h,I,a]
 *
 *   [j,-,b,C,-,d,E,f,-,g,h,I,a]
 *        l                   r    both letters, swap
 *   -> [j,-,I,C,-,d,E,f,-,g,h,b,a]
 *   ... continue ...
 *
 * Time: O(N), Space: O(N) for char array
 */
public String reverseOnlyLetters(String s) {
    // Convert to char array for easy swapping
    char[] arr = s.toCharArray();
    int l = 0;
    int r = s.length() - 1;

    while (l < r) {
        /** NOTE !!!
         *
         *  Character.isLetter() - Key method to check if char is alphabetic
         *
         *  IMPORTANT: Check both conditions:
         *    1. l < r (pointers haven't crossed)
         *    2. !Character.isLetter(arr[l]) (current char is not letter)
         */
        // Move left pointer until it hits a letter
        while (l < r && !Character.isLetter(arr[l])) {
            l++;
        }

        // Move right pointer until it hits a letter
        while (l < r && !Character.isLetter(arr[r])) {
            r--;
        }

        // Swap the letters
        char tmp = arr[l];
        arr[l] = arr[r];
        arr[r] = tmp;

        // Move pointers inward
        l++;
        r--;
    }

    return new String(arr);
}
```

**字元判斷方法** — `Character.isLetter` / `isDigit` / `isLetterOrDigit` 以及 Python 的 `isalpha` / `isdigit` / `isalnum` — 已移到 [string_operations.md](./string_operations.md#character-classification--case)。

#### 做法 2：堆疊（FILO）
```java
// java
// LC 917. Reverse Only Letters
/**  IDEA: Stack-based reversal (FILO - First In Last Out)
 *
 *  Steps:
 *   1. First pass: Loop over string, save only LETTERS in stack
 *   2. Second pass: Loop over string again
 *      - For NON-letters: append in original order
 *      - For letters: pop from stack (reverse order due to FILO)
 *
 * Example:
 *   s = "ab-cd"
 *
 *   First pass: Stack = [a, b, c, d]  (top -> d)
 *
 *   Second pass:
 *     i=0, 'a' is letter  -> pop 'd' -> result = "d"
 *     i=1, 'b' is letter  -> pop 'c' -> result = "dc"
 *     i=2, '-' NOT letter -> append '-' -> result = "dc-"
 *     i=3, 'c' is letter  -> pop 'b' -> result = "dc-b"
 *     i=4, 'd' is letter  -> pop 'a' -> result = "dc-ba"
 *
 * Time: O(N), Space: O(N) for stack
 */
public String reverseOnlyLetters(String s) {
    // NOTE !!! Stack: FILO (First In, Last Out)
    Stack<Character> letters = new Stack<>();

    // First pass: Save all letters in stack
    for (char c : s.toCharArray()) {
        if (Character.isLetter(c)) {
            letters.push(c);
        }
    }

    StringBuilder ans = new StringBuilder();

    // Second pass: Build result
    for (char c : s.toCharArray()) {
        if (Character.isLetter(c)) {
            // For letters: pop from stack (reversed order)
            ans.append(letters.pop());
        } else {
            // For non-letters: keep original position
            ans.append(c);
        }
    }

    return ans.toString();
}
```

**堆疊模式視覺化：**
```text
Input: "Test1ng-Leet=code-Q!"

Step 1: Build Stack (push letters only)
Stack building:
  T -> [T]
  e -> [T, e]
  s -> [T, e, s]
  t -> [T, e, s, t]
  (skip '1')
  n -> [T, e, s, t, n]
  g -> [T, e, s, t, n, g]
  (skip '-')
  L -> [T, e, s, t, n, g, L]
  ... continue ...

Final Stack (bottom to top):
  [T, e, s, t, n, g, L, e, e, t, c, o, d, e, Q]
   ^                                          ^
   bottom                                    top

Step 2: Build Result (pop letters, keep non-letters)
  Position 0: 'T' is letter -> pop 'Q' -> result = "Q"
  Position 1: 'e' is letter -> pop 'e' -> result = "Qe"
  Position 2: 's' is letter -> pop 'd' -> result = "Qed"
  Position 3: 't' is letter -> pop 'o' -> result = "Qedo"
  Position 4: '1' NOT letter -> append '1' -> result = "Qedo1"
  Position 5: 'n' is letter -> pop 'c' -> result = "Qedo1c"
  Position 6: 'g' is letter -> pop 't' -> result = "Qedo1ct"
  Position 7: '-' NOT letter -> append '-' -> result = "Qedo1ct-"
  ... continue ...

Final: "Qedo1ct-eeLg=ntse-T!"
```

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

### 2-13) Verifying an Alien Dictionary — LC 953

**模式：自訂字典序比較**
- 把每個字元對應到它在外星字母序中的排名
- 逐字元比較相鄰的兩個單字
- 處理前綴情況：較短的單字必須排在前面

#### 做法：陣列映射 + 相鄰單字比較
```java
// java
// LC 953. Verifying an Alien Dictionary
/**
 * Pattern: Custom order mapping + pairwise comparison
 *
 * Key Technique:
 *   - Use int[26] array to map each character to its alien rank (O(1) lookup)
 *   - Compare adjacent word pairs only (if each pair is sorted, whole list is sorted)
 *   - On first differing character, compare their ranks to determine order
 *   - If one word is a prefix of the other, shorter word must come first
 *
 * Example:
 *   words = ["hello","leetcode"], order = "hlabcdefgijkmnopqrstuvwxyz"
 *
 *   Alien rank mapping:
 *     h->0, l->1, a->2, b->3, c->4, ...
 *
 *   Compare "hello" vs "leetcode":
 *     h(rank=0) vs l(rank=1) -> 0 < 1 -> sorted!
 *
 * Example 2:
 *   words = ["apple","app"], order = "abcdefghijklmnopqrstuvwxyz"
 *
 *   Compare "apple" vs "app":
 *     a==a, p==p, p==p -> all equal up to minLen
 *     len("apple")=5 > len("app")=3 -> NOT sorted!
 *     (longer word cannot come before its prefix)
 *
 * Time: O(M) where M = total characters across all words
 * Space: O(1) - fixed size array of 26
 */
public boolean isAlienSorted(String[] words, String order) {
    // 1. Map each character to its alien rank for O(1) lookup
    int[] alienOrder = new int[26];
    for (int i = 0; i < order.length(); i++) {
        alienOrder[order.charAt(i) - 'a'] = i;
    }

    // 2. Compare adjacent words
    for (int i = 0; i < words.length - 1; i++) {
        if (!isSorted(words[i], words[i + 1], alienOrder)) {
            return false;
        }
    }

    return true;
}

private boolean isSorted(String w1, String w2, int[] alienOrder) {
    int len1 = w1.length();
    int len2 = w2.length();
    int minLen = Math.min(len1, len2);

    for (int i = 0; i < minLen; i++) {
        char c1 = w1.charAt(i);
        char c2 = w2.charAt(i);

        if (c1 != c2) {
            // If characters differ, the first one must have a smaller rank
            return alienOrder[c1 - 'a'] < alienOrder[c2 - 'a'];
        }
    }

    // If we reach here, one word is a prefix of the other.
    // "apple" is NOT allowed to come before "app".
    // The shorter word must come first.
    return len1 <= len2;
}
```

**關鍵洞見：**
```text
Why int[26] array instead of HashMap?
  - Characters are lowercase English letters only (a-z)
  - alienOrder[ch - 'a'] = rank  ->  O(1) lookup, no boxing overhead
  - Classic trick: char - 'a' maps 'a'->0, 'b'->1, ..., 'z'->25

Why compare only adjacent pairs?
  - If words[0] <= words[1] and words[1] <= words[2], then words[0] <= words[2]
  - Transitivity means we only need N-1 comparisons

Why return len1 <= len2 at the end?
  - If all characters match up to minLen, the shorter word must come first
  - "app" < "apple" in any lexicographic order
  - "apple" before "app" is INVALID (Example 3 in problem)
```

**類似題目：**
- LC 953 Verifying an Alien Dictionary（本模式）
- LC 269 Alien Dictionary（拓撲排序，更難）
- LC 242 Valid Anagram（字元次數映射）

### 2-14) Longest Word in Dictionary through Deleting — LC 524

**模式：子序列檢查 + 追蹤最佳候選**
- 檢查字典中的單字能不能由 `s` 刪除若干字元得到（也就是是不是 `s` 的子序列）
- 追蹤最佳結果：長度最長者勝，長度相同時比字典序
- 雙指標的子序列檢查是核心技巧

#### 做法 1：走訪 + 子序列檢查 + 就地更新最佳解（最佳解）
```java
// java
// LC 524. Longest Word in Dictionary through Deleting
/**
 * Pattern: Two-pointer subsequence check + greedy best tracking
 *
 * Core Idea:
 *   - For each word in dictionary, check if it's a subsequence of s
 *   - Subsequence check: two pointers, one on s and one on word
 *     - If chars match, advance both pointers
 *     - If not, only advance s pointer (skip/delete char from s)
 *     - Word is subsequence if its pointer reaches the end
 *   - Track best candidate: longer length wins, same length -> smaller lexicographic order
 *
 * Example:
 *   s = "abpcplea", dictionary = ["ale","apple","monkey","plea"]
 *
 *   Check "ale":    a-b-p-c-p-l-e-a
 *                   ^         ^ ^       -> match a,l,e -> subsequence ✅ (len=3)
 *   Check "apple":  a-b-p-c-p-l-e-a
 *                   ^ ^ ^   ^ ^         -> match a,p,p,l,e -> subsequence ✅ (len=5)
 *   Check "monkey": no 'm' early enough -> ❌
 *   Check "plea":   a-b-p-c-p-l-e-a
 *                       ^     ^ ^ ^     -> match p,l,e,a -> subsequence ✅ (len=4)
 *
 *   Best: "apple" (longest at len=5)
 *
 * Time: O(N * M) where N = dictionary size, M = length of s
 * Space: O(1) extra (just pointers and result string)
 */
public String findLongestWord(String s, List<String> dictionary) {
    String res = "";

    for (String word : dictionary) {
        // 1. Check if word is a subsequence of s
        if (isSubsequence(s, word)) {
            // 2. Update best: longer wins, ties broken by lexicographic order
            /**
             * NOTE !!!
             *
             *  word.compareTo(res) < 0 means word is lexicographically SMALLER
             *  We want the smallest lexicographic order among same-length candidates
             */
            if (word.length() > res.length() ||
                    (word.length() == res.length() && word.compareTo(res) < 0)) {
                res = word;
            }
        }
    }
    return res;
}

/**
 * Two-pointer subsequence check
 *
 *  s = source string (we "delete" chars from this)
 *  target = dictionary word (check if this is a subsequence of s)
 *
 *  i moves through s (always advances)
 *  j moves through target (advances only on match)
 */
private boolean isSubsequence(String s, String target) {
    int i = 0, j = 0;
    while (i < s.length() && j < target.length()) {
        if (s.charAt(i) == target.charAt(j)) {
            j++; // Match found, advance target pointer
        }
        i++; // Always advance source pointer
    }
    // If j reached end, all chars of target were found in order
    return j == target.length();
}
```

#### 做法 2：先排序 + 回傳第一個符合的
```java
// java
// LC 524. Longest Word in Dictionary through Deleting
/**
 * Pattern: Pre-sort dictionary by (length DESC, lexicographic ASC),
 *          then return the first subsequence match
 *
 * Key Trick:
 *   - Sort so that longest words come first
 *   - Among same-length words, lexicographically smaller comes first
 *   - First valid subsequence match IS the answer (no need to track best)
 *
 * Time: O(N log N * K + N * M)  where K = avg word length (for sort comparisons)
 * Space: O(log N) for sorting
 */
public String findLongestWord_sort(String s, List<String> d) {
    // Sort: longer first, then lexicographic order for ties
    Collections.sort(d, (s1, s2) ->
        s2.length() != s1.length() ? s2.length() - s1.length() : s1.compareTo(s2)
    );

    for (String str : d) {
        if (isSubsequence(s, str))
            return str;  // First match is guaranteed to be the best
    }
    return "";
}
```

**關鍵洞見：**
```text
Two-pointer subsequence check:
  - i (source pointer) ALWAYS advances
  - j (target pointer) advances ONLY on character match
  - If j == target.length() at the end, target is a subsequence
  - This is the same pattern as LC 392 (Is Subsequence)

Best candidate selection (without sorting):
  - word.length() > res.length()  ->  longer is always better
  - word.compareTo(res) < 0       ->  lexicographically smaller wins ties
  - Combined: no need to sort the dictionary at all

Sorting approach trade-off:
  - Pro: simpler logic (return first match)
  - Con: O(N log N) sorting overhead
  - Approach 1 (no sort) is generally preferred
```

**類似題目：**
- LC 524 Longest Word in Dictionary through Deleting（本模式）
- LC 392 Is Subsequence（雙指標子序列檢查的原型）
- LC 720 Longest Word in Dictionary（前綴導向，不同模式）
- LC 1055 Shortest Way to Form String（子序列，要掃很多趟）

### 2-15) Count Pairs of Equal Substrings With Minimum Difference — LC 1794

**模式：首／末出現位置 + 最小差值計數**
- LC 1794. Count Pairs of Equal Substrings With Minimum Difference (Medium)

#### 核心想法
```text
Non-obvious key insight: optimal quadruples ALWAYS use single-character substrings.

Why? For quadruple (i, j, a, b) minimizing j - a:
  - Extending in firstString (j > i) increases j → diff gets larger
  - Extending in secondString (b > a) decreases a → diff also gets larger
  - Therefore i == j, a == b is always optimal → single characters only

For each character c shared by both strings:
  - FIRST occurrence in firstString  → smallest i, minimizes diff
  - LAST  occurrence in secondString → largest  a, minimizes diff
  - diff = i - a; track minimum and count characters achieving it
```

#### Java 實作（O(n + m)）
```java
// LC 1794 - Count Pairs of Equal Substrings With Minimum Difference
/**
 * Time: O(n + m)  Space: O(1) — fixed 26-char arrays
 *
 * Trick: last[c] = j + 1  so 0 means "not present in secondString"
 */
public int countQuadruples(String firstString, String secondString) {
    int[] last = new int[26];

    // Record LAST occurrence of each char in secondString (+1 offset)
    for (int j = 0; j < secondString.length(); j++) {
        last[secondString.charAt(j) - 'a'] = j + 1;
    }

    int minDiff = Integer.MAX_VALUE;
    int count = 0;
    boolean[] visited = new boolean[26]; // only use FIRST occurrence in firstString

    for (int i = 0; i < firstString.length(); i++) {
        int charIdx = firstString.charAt(i) - 'a';
        if (visited[charIdx]) continue;
        visited[charIdx] = true;

        int j = last[charIdx];
        if (j > 0) { // character exists in secondString
            int diff = i - j; // j stored as actual_index + 1

            if (diff < minDiff) {
                minDiff = diff;
                count = 1;
            } else if (diff == minDiff) {
                count++;
            }
        }
    }

    return count;
}
```

**關鍵技巧：**
```text
+1 offset for "not found" sentinel:
  last[c] = 0  → character never appeared in secondString
  last[c] = k  → character last appeared at index k-1

Why FIRST in firstString + LAST in secondString:
  - Later occurrence of c in firstString → larger i → larger diff (bad)
  - Earlier occurrence of c in secondString → smaller a → larger diff (bad)
  - First + Last gives the tightest (minimum) i - a for each character
```

**類似題目：**
- LC 1624 Largest Substring Between Two Equal Characters（首／末出現位置的跨度）
- LC 387 First Unique Character in a String（追蹤第一次出現位置）
- LC 1 Two Sum（用雜湊表做 O(1) 配對／查詢）
- LC 242 Valid Anagram（字元次數陣列）
- LC 567 Permutation in String（字元位置映射 + 滑動視窗）

### 2-16) Ambiguous Coordinates — LC 816

**模式：枚舉切點 + 產生合法的數字格式**
- LC 816. Ambiguous Coordinates (Medium)
- 給一串數字如 `"(123)"`，插入一個逗號和（可選的）小數點，還原出所有可能的 `"(x, y)"` 座標。

#### 核心想法
```text
2 nested decisions:
  1) WHERE to split the digit string into left / right (the comma position)
  2) HOW to format each half as a valid number (integer or decimal)

For each split position i (1 <= i < len):
  left  = digits[:i]
  right = digits[i:]
  -> enumerate valid formats of left  x valid formats of right
  -> combine into "(left, right)"

A half can be either:
  (A) a whole integer (no decimal point)
  (B) a decimal: insert '.' at every interior position
```

#### 合法性規則（麻煩的地方）
```text
Whole integer  s:
  - valid only if s == single digit, OR s does NOT start with '0'
  - "0" ok, "10" ok, "01" / "00" invalid

Decimal  int_part . dec_part:
  - int_part: no leading zero unless it is exactly "0"
      -> "0.5" ok, "05.1" invalid
  - dec_part: cannot end with '0' (no trailing zero)
      -> "1.5" ok, "1.50" invalid, "1.0" invalid
```

#### Python（V0 — 明寫輔助函式）
```python
# LC 816 - Ambiguous Coordinates
class Solution(object):
    def ambiguousCoordinates(self, s):
        # time  = O(n^4) : O(n) splits * O(n) decimal pos * O(n) string build
        # space = O(n^2) for results
        digits = s[1:-1]            # strip outer parentheses
        res = []
        for i in range(1, len(digits)):
            lefts  = self.get_valid_formats(digits[:i])
            rights = self.get_valid_formats(digits[i:])
            for l in lefts:
                for r in rights:
                    res.append("({}, {})".format(l, r))
        return res

    def get_valid_formats(self, sub):
        ans = []
        n = len(sub)
        # (A) whole integer: no leading zero unless single char
        if n == 1 or not sub.startswith('0'):
            ans.append(sub)
        # (B) decimal: insert '.' at every interior position
        for i in range(1, n):
            int_part, dec_part = sub[:i], sub[i:]
            if len(int_part) > 1 and int_part.startswith('0'):
                continue            # leading zero in integer part
            if dec_part.endswith('0'):
                continue            # trailing zero in decimal part
            ans.append(int_part + "." + dec_part)
        return ans
```

#### Python（精簡版 — 產生器）
```python
class Solution(object):
    def ambiguousCoordinates(self, s):
        digits = s[1:-1]
        res = []

        def generate(part):
            n = len(part)
            if n == 1 or part[0] != '0':   # whole integer
                yield part
            for i in range(1, n):          # decimal versions
                left, right = part[:i], part[i:]
                if len(left) > 1 and left[0] == '0':
                    continue
                if right[-1] == '0':
                    continue
                yield left + "." + right

        for i in range(1, len(digits)):
            for l in generate(digits[:i]):
                for r in generate(digits[i:]):
                    res.append("(" + l + ", " + r + ")")
        return res
```

**實例演練：**
```text
s = "(0123)"  ->  digits = "0123"

split "0" | "123":
  "0" valid formats     -> ["0"]
  "123" valid formats   -> ["123", "1.23", "12.3"]
  -> (0, 123), (0, 1.23), (0, 12.3)

split "01" | "23":
  "01" -> invalid as integer (leading zero), "0.1" ok
  "23" -> ["23", "2.3"]
  -> (0.1, 23), (0.1, 2.3)

split "012" | "3":
  "012" -> "0.12" ok (only)
  "3"   -> ["3"]
  -> (0.12, 3)

Final: 6 coordinates
```

**關鍵技巧：**
```text
- "0" alone is always a valid integer; "00", "01" never are.
- Decimal: a digit sequence is invalid if it ends in '0' (else two
  representations collide, e.g. "1.50" == "1.5").
- Two independent halves -> cross-product (left choices x right choices).
```

**類似題目：**
- LC 93 Restore IP Addresses（枚舉切點 + 每段合法性）
- LC 468 Validate IP Address（每段的前導零／範圍規則）
- LC 282 Expression Add Operators（在數字之間插入運算子）

### 2-17) Longest Word in Dictionary — LC 720
**模式：逐步前綴驗證** — 先把單字排序，維護一個「已經可以被建出來」的集合，然後只檢查**直接前綴**。從母文件的模板目錄搬過來：這是單一題目，不是一個家族。

> **因重複而刪除**：`longestWord_v2`，唯一的差別是寫 `word[:len(word)-1]` 而不是 `word[:-1]`。

```python
# Python - LC 720 Longest Word in Dictionary
def longestWord(words):
    """
    Pattern: Build words incrementally by validating immediate prefix

    Key Insight:
      - A word is valid if ALL its prefixes exist in dictionary
      - Instead of checking all prefixes, we only check the immediate prefix
      - This works because we process words in sorted order (shorter first)
      - If "worl" is valid, then "wor", "wo", "w" must already be valid

    Example:
      words = ["w","wo","wor","worl","world"]

      After sorting: ["w","wo","wor","worl","world"]

      Process:
        "w"     -> len==1, add to built, result="w"
        "wo"    -> "w" in built ✓, add "wo", result="wo"
        "wor"   -> "wo" in built ✓, add "wor", result="wor"
        "worl"  -> "wor" in built ✓, add "worl", result="worl"
        "world" -> "worl" in built ✓, add "world", result="world"

    Time: O(n log n) for sorting + O(n*m) for processing (m = avg word length)
    Space: O(n*m) for storing all words in set
    """
    if not words:
        return ""

    # words.sort() works here because shorter words sort before longer ones lexicographically
    # Sort lexicographically (automatically handles tie-breaking)
    words.sort()

    built = set()
    result = ""

    for word in words:
        # Word is valid if:
        # 1. Single character (base case), OR
        # 2. Its immediate prefix exists in built set
        if len(word) == 1 or word[:-1] in built:
            built.add(word)

            # Update result if current word is longer
            # (sorting ensures alphabetical order for ties)
            if len(word) > len(result):
                result = word

    return result
```

```java
// Java - LC 720 Longest Word in Dictionary
public String longestWord(String[] words) {
    /**
     * Pattern: Incremental Prefix Validation
     *
     * Core Trick:
     *   word.substring(0, word.length() - 1)
     *
     *   Only check if the IMMEDIATE prefix exists (not all prefixes)
     *   This works because sorting guarantees shorter words are processed first
     *
     * Why Sorting is Critical:
     *   Arrays.sort(words) ensures:
     *   1. Shorter words come before longer words (alphabetically)
     *   2. When we reach "world", "worl" has already been validated
     *   3. If "worl" wasn't valid, it wouldn't be in builtWords
     *
     * Example:
     *   Input: ["a","banana","app","appl","ap","apply","apple"]
     *   After sort: ["a","ap","app","appl","apple","apply","banana"]
     *
     *   Process:
     *     "a"      -> len==1, add ✓
     *     "ap"     -> "a" exists ✓, add ✓
     *     "app"    -> "ap" exists ✓, add ✓
     *     "appl"   -> "app" exists ✓, add ✓
     *     "apple"  -> "appl" exists ✓, add ✓
     *     "apply"  -> "appl" exists ✓, add ✓
     *     "banana" -> "banan" NOT exists ✗, skip
     *
     *   Result: "apple" (longer and lexicographically smaller than "apply")
     *
     * time = O(N log N) for sorting + O(N*M) for processing
     * space = O(N*M) for HashSet storage
     */
    if (words == null || words.length == 0) {
        return "";
    }

    // Sort lexicographically (handles both length and alphabetical order)
    Arrays.sort(words);

    Set<String> built = new HashSet<>();
    String result = "";

    for (String word : words) {
        // Word is valid if:
        // 1. Length == 1 (base case: single char always buildable), OR
        // 2. Its prefix (all chars except last) exists in built set

        /** NOTE !!! KEY TRICK
         *
         * word.substring(0, word.length() - 1)
         *
         * Get the immediate prefix (remove last character)
         *
         * Why not check ALL prefixes?
         *   - We could do:
         *     for (int i = 1; i < word.length(); i++) {
         *         if (!built.contains(word.substring(0, i))) return false;
         *     }
         *
         *   - But that's unnecessary because:
         *     If "worl" is valid, then "wor", "wo", "w" must already be valid
         *     (due to incremental building from sorted order)
         *
         * Inductive Logic:
         *   If immediate prefix exists AND is valid,
         *   Then all shorter prefixes must also exist (by induction)
         */
        if (word.length() == 1 || built.contains(word.substring(0, word.length() - 1))) {
            built.add(word);

            // Update result if current word is longer
            // (sorting ensures lexicographical order is maintained)
            if (word.length() > result.length()) {
                result = word;
            }
        }
    }

    return result;
}
```

**關鍵洞見：**

1. **為什麼只檢查直接前綴就夠：**
   - 排序保證較短的單字先被處理
   - 如果 "worl" 合法，它所有的前綴（"wor"、"wo"、"w"）一定早就合法了
   - 這是**歸納法**：檢查直接前綴就足夠

2. **為什麼排序行得通：**
   ```text
   Before: ["world","worl","wor","wo","w"]
   After:  ["w","wo","wor","worl","world"]

   When processing "world":
     - "worl" has already been processed
     - If "worl" is in built, all shorter prefixes are guaranteed valid
   ```

3. **複雜度拆解：**
   - 排序：O(N log N)
   - 處理：O(N * M)，M = 平均單字長度
   - 空間：O(N * M)，用於 HashSet
   - 總計：O(N log N + N*M)

4. **類似題目：**
   - LC 720 Longest Word in Dictionary（本模式）
   - LC 745 Prefix and Suffix Search（Trie 變形）
   - LC 648 Replace Words（Trie + 前綴比對）

### 2-18) Expressive Words — LC 809
> 實作 [string.md](./string.md#template-3-run-length-grouping-consecutive-character-groups--lc-696-) 的 **模板 3：遊程分組** — 先讀那邊。

*變化點*：不是掃一個字串的分組，而是替**兩個**字串都建出 `(char, count)` 分組再對齊。兩個字串相符的條件是：分組*序列*要一一對上，而且每個來源分組要嘛大小相同，要嘛「可以被拉長」（`>= 3`）。

```python
# python
# LC 809 - Expressive Words
# time = O(n + sum(len(w))), space = O(n)
# IDEA: reduce both strings to (char, run-length) groups, then compare group-by-group
def groups(x):
    res, i = [], 0
    while i < len(x):
        j = i
        while j < len(x) and x[j] == x[i]:
            j += 1
        res.append((x[i], j - i))
        i = j
    return res

def expressiveWords(s, words):
    gs = groups(s)
    cnt = 0
    for w in words:
        gw = groups(w)
        # NOTE !!! group COUNT must match first ("abc" vs "abbc" can never match)
        if len(gw) != len(gs):
            continue
        # each pair: same char AND (equal length OR s-group is stretchable: >= 3 and longer)
        if all(c1 == c2 and (n1 == n2 or (n1 >= 3 and n1 > n2))
               for (c1, n1), (c2, n2) in zip(gs, gw)):
            cnt += 1
    return cnt
```

```java
// java
// LC 809 - Expressive Words
// time = O(n + sum(len(w))), space = O(n)
// IDEA: reduce both strings to (char, run-length) groups, then compare group-by-group
public int expressiveWords(String s, String[] words) {
    List<int[]> gs = groups(s);
    int cnt = 0;
    for (String w : words) {
        List<int[]> gw = groups(w);
        if (gw.size() != gs.size()) continue;   // NOTE !!! group count must match
        boolean ok = true;
        for (int i = 0; i < gs.size(); i++) {
            int[] a = gs.get(i), b = gw.get(i);
            if (a[0] != b[0]) { ok = false; break; }
            /** NOTE !!! equal counts are fine; otherwise s-group must be >= 3 AND longer */
            if (a[1] != b[1] && (a[1] < 3 || a[1] < b[1])) { ok = false; break; }
        }
        if (ok) cnt++;
    }
    return cnt;
}

private List<int[]> groups(String x) {
    List<int[]> res = new ArrayList<>();
    int i = 0;
    while (i < x.length()) {
        int j = i;
        while (j < x.length() && x.charAt(j) == x.charAt(i)) j++;
        res.add(new int[]{x.charAt(i), j - i});
        i = j;
    }
    return res;
}
```

- ⚠️ 擴展只能**變長**：`"aaa"` 無法對上 `"aaaa"`（需要 `n1 > n2`）。
- ⚠️ 大小為 2 的分組永遠拉不出來（從 `"a"` 變成 `"aa"` 不合法）— 這就是 `>= 3` 規則。

### 2-19) Integer to English Words — LC 273
> 實作 [string.md](./string.md#template-5-greedy-line-packing--space-distribution-text-wrapping--lc-68-) 的 **模板 5：貪婪排版 + 空白分配** — 同樣是「把片段收進 list，最後一次 join」的紀律，只是切法從按寬度換成按 3 位數一組。

*變化點*：同樣是「把片段收進 list，最後一次 join」的紀律，但切分規則變成**每 3 位數一組**，而不是「能塞幾個字就塞幾個」。用 `List<String>` 再 join，比 `StringBuilder` + `trim()` 好，因為這樣根本不可能出現連續兩個空白的 bug。

```java
// java
// LC 273 - Integer to English Words
// time = O(1) (num <= 2^31-1 -> at most 4 chunks), space = O(1)
// IDEA: split number into 3-digit chunks, spell each chunk, tag it with Thousand/Million/Billion
private static final String[] BELOW_20 = {"", "One", "Two", "Three", "Four", "Five",
    "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen",
    "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
private static final String[] TENS = {"", "", "Twenty", "Thirty", "Forty", "Fifty",
    "Sixty", "Seventy", "Eighty", "Ninety"};
private static final String[] THOUSANDS = {"", "Thousand", "Million", "Billion"};

public String numberToWords(int num) {
    if (num == 0) return "Zero";   // NOTE !!! only place "Zero" is ever emitted
    List<String> parts = new ArrayList<>();
    int i = 0;
    while (num > 0) {
        if (num % 1000 != 0) {          // NOTE !!! skip all-zero chunks (1,000,010 -> "One Million Ten")
            List<String> chunk = three(num % 1000);
            if (i > 0) chunk.add(THOUSANDS[i]);
            parts.addAll(0, chunk);     // prepend: we scan chunks low -> high
        }
        num /= 1000;
        i++;
    }
    return String.join(" ", parts);
}

// spell 1..999
private List<String> three(int n) {
    List<String> out = new ArrayList<>();
    if (n == 0) return out;
    if (n < 20) { out.add(BELOW_20[n]); return out; }
    if (n < 100) { out.add(TENS[n / 10]); out.addAll(three(n % 10)); return out; }
    out.add(BELOW_20[n / 100]);
    out.add("Hundred");
    out.addAll(three(n % 100));
    return out;
}
```

```python
# python
# LC 273 - Integer to English Words
# time = O(1), space = O(1)
# IDEA: split number into 3-digit chunks, spell each chunk, tag with Thousand/Million/Billion
BELOW_20 = ["", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
            "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
            "Sixteen", "Seventeen", "Eighteen", "Nineteen"]
TENS = ["", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"]
THOUSANDS = ["", "Thousand", "Million", "Billion"]

def numberToWords(num):
    if num == 0:
        return "Zero"

    def three(n):           # spell 1..999 as a list of words
        if n == 0:
            return []
        if n < 20:
            return [BELOW_20[n]]
        if n < 100:
            return [TENS[n // 10]] + three(n % 10)
        return [BELOW_20[n // 100], "Hundred"] + three(n % 100)

    parts, i = [], 0
    while num:
        if num % 1000:      # NOTE !!! skip all-zero chunks
            parts = three(num % 1000) + ([THOUSANDS[i]] if i else []) + parts
        num //= 1000
        i += 1
    return " ".join(parts)
```

- ⚠️ 只有 `num == 0` 才輸出 "Zero"；中間出現的整組零必須**什麼都不輸出**。
- ⚠️ 10..19 是不規則的英文字 — 要在進到十位數分支**之前**先處理 `n < 20`。

### 2-20) Rotate String — LC 796
> 暴力解，O(n²)。O(n) 的寫法是 `goal in (s + s)` 這個小技巧加上 KMP — 見 [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md)。

```python
# LC 796. Rotate String
class Solution(object):
    def rotateString(self, A, B):
        for i in range(len(A)):
            if A[i:] + A[:i] == B:
                return True
        return False
```

### 2-21) Space-Optimised Two-Sequence DP — LC 72, LC 1143
> 這個**家族**歸 [dp_string.md](./dp_string.md) 管，二維模板歸 [dp.md](./dp.md) 管；這裡留下的是 O(min(m, n)) 空間的 **Python** 滾動列寫法，那兩份文件只有 Java 版。

```python
# LC 72 Edit Distance — rolling-row 1D DP
def minDistance(word1, word2):
    m, n = len(word1), len(word2)
    dp = list(range(n + 1))
    for i in range(1, m + 1):
        prev = dp[:]
        dp[0] = i
        for j in range(1, n + 1):
            if word1[i-1] == word2[j-1]:
                dp[j] = prev[j-1]
            else:
                dp[j] = 1 + min(prev[j], dp[j-1], prev[j-1])
    return dp[n]

# LC 1143 Longest Common Subsequence
def longestCommonSubsequence(text1, text2):
    m, n = len(text1), len(text2)
    dp = [0] * (n + 1)
    for c in text1:
        prev, dp = dp[:], [0] * (n + 1)
        for j, d in enumerate(text2):
            dp[j+1] = prev[j] + 1 if c == d else max(prev[j+1], dp[j])
    return dp[n]
```

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

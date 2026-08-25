# String Algorithms — Worked Examples

> **Scope** — The worked string LeetCode archive, one canonical solution per problem per language, each filed under the parent sheet's template that it instantiates; the concepts, the pattern catalogue and the templates themselves stay in the main string sheet.
> **See also**: [string.md](./string.md) — the parent sheet, with the template each example instantiates; [string_operations.md](./string_operations.md) — the language-level string API, split out of the same file; [palindrome.md](./palindrome.md) — the palindrome family in depth; [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — substring search; [sliding_window.md](./sliding_window.md) — character-window problems; [2_pointers_examples.md](./2_pointers_examples.md) — the two-pointer archive, which also carries LC 165, 524, 763, 809 and 953.

## LeetCode Problem Lists

- [String](https://leetcode.com/problem-list/string/)

## Overview

One section per problem, numbered in a single consecutive run. Each problem appears exactly once.
Read the template it instantiates in [string.md](./string.md) first, then the example.

### Problem Index

| Group | Problems |
|---|---|
| **Parsing & Comparison** | [2-1)](#2-1-compare-version-number--lc-165), [2-2)](#2-2-add-two-numbers-ii--decode-string), [2-4)](#2-4-monotone-increasing-digits--lc-738), [2-5)](#2-5-validate-ip-address--lc-468), [2-8)](#2-8-roman-to-integer--lc-13), [2-13)](#2-13-verifying-an-alien-dictionary--lc-953) |
| **Grouping & Run-Length** | [2-3)](#2-3-count-and-say--lc-38), [2-18)](#2-18-expressive-words--lc-809) |
| **Building & Formatting** | [2-6)](#2-6-license-key-formatting--lc-482), [2-16)](#2-16-ambiguous-coordinates--lc-816), [2-19)](#2-19-integer-to-english-words--lc-273) |
| **Search & Matching** | [2-7)](#2-7-repeated-string-match--lc-686), [2-10)](#2-10-palindromic-substrings--lc-647), [2-11)](#2-11-repeated-substring-pattern--lc-459), [2-20)](#2-20-rotate-string--lc-796) |
| **Counting & Contribution** | [2-9)](#2-9-count-unique-characters-of-all-substrings-of-a-given-string--lc-828), [2-15)](#2-15-count-pairs-of-equal-substrings-with-minimum-difference--lc-1794) |
| **Two-Pointer & In-Place** | [2-12)](#2-12-reverse-only-letters--lc-917), [2-14)](#2-14-longest-word-in-dictionary-through-deleting--lc-524) |
| **Prefix Validation** | [2-17)](#2-17-longest-word-in-dictionary--lc-720) |
| **Two-Sequence DP** | [2-21)](#2-21-space-optimised-two-sequence-dp--lc-72-lc-1143) |
| **Reference** | [2-22)](#2-22-additional-high-frequency-problems-reference) |

## LC Examples

### 2-1) Compare Version Number — LC 165
> **Dropped as a duplicate**: a second Python variant that popped from the front and then ran two leftover loops — same split-and-compare approach; the surviving version zero-pads the shorter version instead.

- go through 2 string, keep comparing digits in eash string
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
> **Dropped as a duplicate**: `str_2_int_v2` — the same digit accumulation written as `(res + int(i) % 10) * 10` then divided back down.

- String -> Int
```python
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
> **Dropped as a duplicate**: a 30-line "string op + brute force" variant that stripped, regrouped and re-joined; the surviving reverse scan is the same idea in nine lines.

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
> **Dropped as a duplicate**: a second Python variant with the same `(res-1)*sa <= 2*max(sa,sb)` bound plus extra edge-case branches.

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
> **Dropped as a duplicate**: the V1 copy was character-for-character identical to this one except for `string.ascii_uppercase` in place of the literal alphabet — which needs an `import string` the block never showed. Its explanation is kept, above the surviving code.

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
> The O(n) centre-expansion and Manacher treatments of this problem live in [palindrome.md](./palindrome.md); this is the O(n³) brute force kept for contrast.

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
> The KMP / Z-array treatments live in [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) and [advanced_string_algorithms.md](./advanced_string_algorithms.md).

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

**Pattern: Selective Character Reversal**
- Reverse only alphabetic characters
- Keep non-alphabetic characters in original positions
- Two approaches: Two Pointers or Stack

#### Approach 1: Two Pointers (Optimal)
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

**Character validation methods** — `Character.isLetter` / `isDigit` / `isLetterOrDigit` and Python's `isalpha` / `isdigit` / `isalnum` — moved to [string_operations.md](./string_operations.md#character-classification--case).

#### Approach 2: Stack (FILO)
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

**Stack Pattern Visualization:**
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

**Comparison:**
| Approach | Time | Space | When to Use |
|----------|------|-------|-------------|
| Two Pointers | O(N) | O(N) | In-place modification, optimal |
| Stack | O(N) | O(N) | Need to preserve original, clearer logic |

**Similar Problems:**
- LC 917 Reverse Only Letters (this pattern)
- LC 345 Reverse Vowels of a String (selective reversal)
- LC 344 Reverse String (full reversal)
- LC 541 Reverse String II (selective ranges)
- LC 151 Reverse Words in a String (word-level reversal)

### 2-13) Verifying an Alien Dictionary — LC 953

**Pattern: Custom Lexicographic Order Comparison**
- Map each character to its rank in the alien order
- Compare adjacent words character by character
- Handle prefix case: shorter word must come first

#### Approach: Array Mapping + Adjacent Word Comparison
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

**Key Insights:**
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

**Similar Problems:**
- LC 953 Verifying an Alien Dictionary (this pattern)
- LC 269 Alien Dictionary (topological sort, harder)
- LC 242 Valid Anagram (character frequency mapping)

### 2-14) Longest Word in Dictionary through Deleting — LC 524

**Pattern: Subsequence Check + Best Candidate Tracking**
- Check if a dictionary word can be formed by deleting characters from `s` (i.e., is a subsequence of `s`)
- Track the best result: longest length wins, ties broken by lexicographic order
- Two-pointer subsequence check is the core technique

#### Approach 1: Iterate + Subsequence Check + In-place Best Tracking (Optimal)
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

#### Approach 2: Sort First + Return First Match
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

**Key Insights:**
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

**Similar Problems:**
- LC 524 Longest Word in Dictionary through Deleting (this pattern)
- LC 392 Is Subsequence (core two-pointer subsequence check)
- LC 720 Longest Word in Dictionary (prefix-based, different pattern)
- LC 1055 Shortest Way to Form String (subsequence with multiple passes)

### 2-15) Count Pairs of Equal Substrings With Minimum Difference — LC 1794

**Pattern: First/Last Character Occurrence + Minimum Difference Counting**
- LC 1794. Count Pairs of Equal Substrings With Minimum Difference (Medium)

#### Core Idea
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

#### Java Implementation (O(n + m))
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

**Key Tricks:**
```text
+1 offset for "not found" sentinel:
  last[c] = 0  → character never appeared in secondString
  last[c] = k  → character last appeared at index k-1

Why FIRST in firstString + LAST in secondString:
  - Later occurrence of c in firstString → larger i → larger diff (bad)
  - Earlier occurrence of c in secondString → smaller a → larger diff (bad)
  - First + Last gives the tightest (minimum) i - a for each character
```

**Similar Problems:**
- LC 1624 Largest Substring Between Two Equal Characters (first/last occurrence span)
- LC 387 First Unique Character in a String (first occurrence tracking)
- LC 1 Two Sum (hash map for O(1) pairing/lookup)
- LC 242 Valid Anagram (character frequency array)
- LC 567 Permutation in String (character position mapping + sliding window)

### 2-16) Ambiguous Coordinates — LC 816

**Pattern: Enumerate Splits + Generate Valid Number Formats**
- LC 816. Ambiguous Coordinates (Medium)
- Given digits like `"(123)"`, restore all possible `"(x, y)"` coordinates by inserting a comma and (optionally) decimal points.

#### Core Idea
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

#### Validity Rules (the tricky part)
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

#### Python (V0 — explicit helper)
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

#### Python (concise — generator)
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

**Worked Example:**
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

**Key Tricks:**
```text
- "0" alone is always a valid integer; "00", "01" never are.
- Decimal: a digit sequence is invalid if it ends in '0' (else two
  representations collide, e.g. "1.50" == "1.5").
- Two independent halves -> cross-product (left choices x right choices).
```

**Similar Problems:**
- LC 93 Restore IP Addresses (enumerate split positions + segment validity)
- LC 468 Validate IP Address (per-segment leading-zero / range rules)
- LC 282 Expression Add Operators (insert operators between digits)

### 2-17) Longest Word in Dictionary — LC 720
**Pattern: Incremental Prefix Validation** — sort the words, keep a set of the ones already buildable, and check only the **immediate** prefix. Moved here from the parent sheet's template catalogue: one problem, not a family.

> **Dropped as a duplicate**: `longestWord_v2`, which differed only in writing `word[:len(word)-1]` instead of `word[:-1]`.

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

**Key Insights:**

1. **Why Only Check Immediate Prefix:**
   - Sorting ensures shorter words are processed first
   - If "worl" is valid, all its prefixes ("wor", "wo", "w") must already be valid
   - This is **inductive reasoning**: checking immediate prefix is sufficient

2. **Why Sorting Works:**
   ```text
   Before: ["world","worl","wor","wo","w"]
   After:  ["w","wo","wor","worl","world"]

   When processing "world":
     - "worl" has already been processed
     - If "worl" is in built, all shorter prefixes are guaranteed valid
   ```

3. **Complexity Breakdown:**
   - Sorting: O(N log N)
   - Processing: O(N * M) where M = average word length
   - Space: O(N * M) for HashSet
   - Overall: O(N log N + N*M)

4. **Similar Problems:**
   - LC 720 Longest Word in Dictionary (this pattern)
   - LC 745 Prefix and Suffix Search (Trie variation)
   - LC 648 Replace Words (Trie + prefix matching)

### 2-18) Expressive Words — LC 809
> Instantiates **Template 3: Run-Length Grouping** in [string.md](./string.md#template-3-run-length-grouping-consecutive-character-groups--lc-696-) — read that first.

*Twist*: instead of scanning one string's groups, build `(char, count)` groups for **both**
strings and zip them. Two strings match iff the group *sequences* line up and each source group
is either the same size or "stretchable" (`>= 3`).

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

- ⚠️ Extension only **grows** groups: `"aaa"` cannot match `"aaaa"` (need `n1 > n2`).
- ⚠️ A group of size 2 can never be stretched (`"aa"` from `"a"` is invalid) — the `>= 3` rule.

### 2-19) Integer to English Words — LC 273
> Instantiates **Template 5: Greedy Line Packing + Space Distribution** in [string.md](./string.md#template-5-greedy-line-packing--space-distribution-text-wrapping--lc-68-) — same "collect pieces into a list, join once" discipline, chunked by 3 digits instead of by width.

*Twist*: same "build pieces into a list, join once at the end" discipline, but the chunking rule
is **groups of 3 digits** instead of "as many words as fit". Building a `List<String>` and
joining beats `StringBuilder` + `trim()` because it makes double-space bugs impossible.

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

- ⚠️ `num == 0` is the only "Zero"; an inner zero chunk must emit **nothing**.
- ⚠️ 10..19 are irregular words — handle `n < 20` **before** the tens branch.

### 2-20) Rotate String — LC 796
> Brute force, O(n²). The O(n) form is the `goal in (s + s)` trick plus KMP — see [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md).

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
> The **family** is owned by [dp_string.md](./dp_string.md) and the 2D templates by [dp.md](./dp.md); what is kept here is the O(min(m, n))-space **Python** rolling-row form, which those sheets carry only in Java.

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
No new template — each is a one-line idea, but they show up constantly.

| Problem | LC # | One-line idea | Difficulty |
|---------|------|---------------|------------|
| Longest Common Prefix | 14 | Vertical scan: compare column `i` across all words, stop at first mismatch | Easy |
| Isomorphic Strings | 205 | Two maps (`s→t` **and** `t→s`) — a single map wrongly accepts `"ab" → "aa"` | Easy |
| Find and Replace Pattern | 890 | The same two-map bijection as LC 205, run against every word in the list | Medium |
| Ransom Note | 383 | Char-count of magazine, decrement per note char, fail on negative | Easy |
| Most Common Word | 819 | Lowercase + split on non-letters, skip banned set, take max count | Easy |
| Reorder Data in Log Files | 937 | `split(" ", 2)` → id + body; custom comparator: letter-logs by (body, id), digit-logs keep original order (stable sort) | Medium |
| Bulls and Cows | 299 | One pass: equal chars → bulls; else bump two count arrays, cows = `sum(min(cntS[d], cntG[d]))` | Medium |

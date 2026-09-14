"""

5. Longest Palindromic Substring
Medium

Given a string s, return the longest palindromic substring in s.


Example 1:

Input: s = "babad"
Output: "bab"
Explanation: "aba" is also a valid answer.
Example 2:

Input: s = "cbbd"
Output: "bb"
 

Constraints:

1 <= s.length <= 1000
s consist of only digits and English letters.

"""


# V0
# IDEA : TWO POINTERS + IDX EXPANSION
# -> DEAL WITH odd, even len cases
#  -> step 1) for loop on idx 
#  -> step 2) and start from "center" 
#  -> step 3) and do a while loop
#  -> step 4) check if len of sub str > 1
# time = O(n^2)
# space = O(1)
class Solution(object):
    def longestPalindrome(self, s):
        # edge
        if not s or len(s) == 0:
            return ""
        if len(s) == 1:
            return s

        res = s[0] # ???
        n = len(s)

        for i in range(n):
            # case 1) l, r = i
            l = r = i
            while l >= 0 and r < n and s[l] == s[r]:
                if r - l + 1 > len(res):
                    res = s[l:r+1] # ???
                l -= 1
                r += 1

            # case 2) l=i,  r = i-1
            l = i
            r = i - 1
            while l >= 0 and r < n and s[l] == s[r]:
                if r - l + 1 > len(res):
                    res = s[l:r+1] # ???
                l -= 1
                r += 1


        return res


# V0-1
# IDEA : TWO POINTERS + IDX EXPANSION
# time = O(n^2)
# space = O(1)
class Solution(object):
    def longestPalindrome(self, s):
        if not s:
            return ""

        res = s[0]
        n = len(s)

        for i in range(n):

            # odd length palindrome
            l = r = i
            while l >= 0 and r < n and s[l] == s[r]:
                if r - l + 1 > len(res):
                    res = s[l:r+1]
                l -= 1
                r += 1

            # even length palindrome
            l = i
            r = i + 1
            while l >= 0 and r < n and s[l] == s[r]:
                if r - l + 1 > len(res):
                    res = s[l:r+1]
                l -= 1
                r += 1

        return res



# V0-3
# IDEA: 2D DP (gemini)
"""

**狀態定義與轉移邏輯**

* **狀態定義**：dp[i][j] 表示字串從索引 i 到 j（即 `s[i:j+1]`）是否為回文子字串（型態為 `bool`）。
* **底線條件（Base Cases）**：
* 長度為 1 時：所有單一字元皆為回文，即 $dp[i][i] = \text{True}$。
* 長度為 2 時：若 $s[i] == s[i+1]$，則 $dp[i][i+1] = \text{True}$。


* **狀態轉移方程式**：
* 當長度 l >= 3  時，若首尾字元相同（s[i] == s[j]）且內部子字串為回文（$dp[i+1][j-1] == \text{True}$），則當前區間亦為回文：

dp[i][j] = (s[i] == s[j]) and dp[i+1][j-1]




* **走訪順序（Order of Iteration）**：必須**外層依子字串長度 $l$ 從 $2$ 遞增到 $n$**，內層枚舉起點 $i$，確保在計算 $dp[i][j]$ 時，較小的內部子狀態 $dp[i+1][j-1]$ 已經被預先計算完畢。

**複雜度分析**

* **時間複雜度**：O(N^2) — 雙重迴圈遍歷所有長度與起點組合，共計約 $\frac{N(N-1)}{2}$ 個狀態。
* **空間複雜度**： O(N^2)$ — 使用 $N \times N$ 的二維陣列儲存所有子字串的布林判定結果。


"""
class Solution(object):

    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: str
        """
        if not s:
            return ""

        n = len(s)
        dp = [[False] * n for _ in range(n)]
        start, max_len = 0, 1

        # 長度為 1 的子字串必定是回文
        for i in range(n):
            dp[i][i] = True

        """
        NOTE !!!

        1. double loop

        2. first loop:
            
                -> loop on `sub str len` (2, n+1)


        3. second loop:

                -> loop on `idx` (0, n - l + 1)


        4. j = i + l - 1


        5.

            dp[i][j]
                 表示字串從索引 i 到 j（即 `s[i:j+1]`）是否為回文子字串（型態為 `bool`）

        """
        # 由短到長枚舉子字串長度 l
        for l in range(2, n + 1):
            for i in range(n - l + 1):
                j = i + l - 1
                if s[i] == s[j]:
                    if l == 2 or dp[i + 1][j - 1]:
                        dp[i][j] = True
                        if l > max_len:
                            max_len = l
                            start = i

        return s[start : start + max_len]


# V0-2
# IDEA: 2D DP (gpt)
"""
DP def

    dp[i][j] = True

    -> s[i:j+1] 是否為 palindrome。


DP eq

    ```
        dp[i][j] =
            True,  if s[i] == s[j] and (j-i == 1 or dp[i+1][j-1])
            False, otherwise
    ```


----


CORE IDEA:

    兩端相同 + 中間是 palindrome → 整段就是 palindrome。


"""
class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: str
        """
        # edge
        if not s:
            return ""

        n = len(s)

        # dp[i][j] = whether s[i:j+1] is a palindrome
        dp = [[False] * n for _ in range(n)]

        # single character is always palindrome
        for i in range(n):
            dp[i][i] = True

        start = 0
        max_len = 1

        # length = 2 ~ n
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1

                # first and last chars must be the same
                if s[i] == s[j]:
                    # length 2: "aa"
                    # length > 2: inner part must also be palindrome
                    if length == 2 or dp[i + 1][j - 1]:
                        dp[i][j] = True

                        if length > max_len:
                            max_len = length
                            start = i

        return s[start:start + max_len]



# V0-4
# IDEA: 2D DP
"""

- DP def
    dp[i][j] = True  if s[i:j+1] is a palindrome

- DP eq
    ```
    s[i] == s[j]
    and
    (j - i <= 2 or dp[i+1][j-1])
    ```

"""
# time = O(n^2)
# space = O(n^2)
class Solution(object):
    def longestPalindrome(self, s):
        if not s:
            return ""

        n = len(s)

        dp = [[False] * n for _ in range(n)]

        start = 0
        max_len = 1

        # length 1
        for i in range(n):
            dp[i][i] = True

        # length 2+
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1

                if s[i] != s[j]:
                    dp[i][j] = False
                else:
                    if length <= 3:
                        dp[i][j] = True
                    else:
                        dp[i][j] = dp[i + 1][j - 1]

                if dp[i][j] and length > max_len:
                    start = i
                    max_len = length

        return s[start:start + max_len]


# V0-3
# IDEA : TWO POINTERS + IDX EXPANSION
# -> DEAL WITH odd, even len cases
#  -> step 1) for loop on idx 
#  -> step 2) and start from "center" 
#  -> step 3) and do a while loop
#  -> step 4) check if len of sub str > 1
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1025355/Easy-to-understand-solution-with-O(n2)-time-complexity
# time = O(n^2)
# space = O(1)
class Solution:
    # The logic I have used is very simple, iterate over each character in the array and assming that its the center of a palindrome step in either direction to see how far you can go by keeping the property of palindrome true. The trick is that the palindrome can be of odd or even length and in each case the center will be different.
    # For odd length palindrome i am considering the index being iterating on is the center, thereby also catching the scenario of a palindrome with a length of 1.
    # For even length palindrome I am considering the index being iterating over and the next element on the left is the center.
    def longestPalindrome(self, s):

        if len(s) <= 1:
            return s

        res = []

        for idx in range(len(s)):
        
            """
            # CASE 1) : odd len
            # Check for odd length palindrome with idx at its center

            -> NOTE : the only difference (between odd, even len)

            -> NOTE !!!  : 2 idx : left = right = idx
            """
            left = right = idx
            # note the condition !!!
            while left >= 0 and right < len(s) and s[left] == s[right]:
                # note !!! this
                if right - left + 1 > len(res):
                    # note !!! this
                    res = s[left:right + 1]
                left -= 1
                right += 1
              
            """"
            # CASE 2) : even len  
            # Check for even length palindrome with idx and idx-1 as its center

            -> NOTE : the only difference (between odd, even len)

            -> NOTE !!!  : 
                -> we init 
                    left = idx - 1
                    right = idx
            """
            left = idx - 1
            right = idx
            # note the condition !!!
            while left >= 0 and right < len(s) and s[left] == s[right]:
                # note !!! this
                if right - left + 1 > len(res):
                    # note !!! this
                    res = s[left:right + 1]
                left -= 1
                right += 1

        return res

# V0-4
# IDEA : TWO POINTER + RECURSION
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1057629/Python.-Super-simple-and-easy-understanding-solution.-O(n2).
# time = O(n^2)
# space = O(1)
class Solution:
    def longestPalindrome(self, s):
        res = ""
        length = len(s)
        def helper(left, right):
            while left >= 0 and right < length and s[left] == s[right]:
                left -= 1
                right += 1      
            return s[left + 1 : right]
        
        for index in range(len(s)):
            res = max(helper(index, index), helper(index, index + 1), res, key = len)           
        return res

# V0-5
# IDEA : TWO POINTERS
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1025496/Python-Clean-and-Simple
# time = O(n^2)
# space = O(n)
class Solution:
    def longestPalindrome(self, s):
        left = 0
        size = 1
        for i in range(len(s)):
            odd = s[i-size-1:i+1]
            even = s[i-size:i+1]
            if 0 <= i-size-1 and odd == odd[::-1]:
                left = i-size-1
                size += 2
            elif 0 <= i-size and even == even[::-1]:
                left = i-size
                size += 1
        return s[left:left+size]

# V0-6
# IDEA : DP
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1194142/Super-Clean-DP-Python-Solution
# time = O(n^2)
# space = O(n^2)
class Solution:
    def longestPalindrome(self, s):
            dp = [[0]*len(s) for _ in range(len(s))]   

            longest = ""
            for i in range(len(s)):
                for j in range(len(s) - i):
                    if (i == 0) or (i == 1 and s[j] == s[j+i]) or (s[j] == s[j+i] and dp[i-2][j+1]):
                        dp[i][j] = 1
                        longest = s[j:j+i+1]
            return longest

# V0-7
# IDEA : BRUTE FORCE (TIME OUT ERROR)
# brute force
# time = O(n^3)
# space = O(n)
class Solution(object):
    def longestPalindrome(self, s):
        def check(_str):
            return _str == _str[::-1]
        if len(s) == 0:
            return ""
        res = ""
        tmp = ""
        for i in range(len(s)):
            for j in range(i+1, len(s)+1):
                tmp = s[i:j]
                print ("tmp = " + str(tmp) + " check(tmp) = " + str(check(tmp)) )
                if check(tmp):
                    res = tmp if len(tmp) > len(res) else res
        return res

# V1
# IDEA : LOOPING ON "MIDDLE"
# time = O(n^2)
# space = O(1)
class Solution:
    """
    @param s: input string
    @return: the longest palindromic substring
    """
    def longestPalindrome(self, s):
        if not s:
            return ""

        longest = ""
        for middle in range(len(s)):
            sub = self.find_palindrome_from(s, middle, middle)
            if len(sub) > len(longest):
                longest = sub
            sub = self.find_palindrome_from(s, middle, middle + 1)
            if len(sub) > len(longest):
                longest = sub

        return longest

    def find_palindrome_from(self, string, left, right):
        while left >= 0 and right < len(string) and string[left] == string[right]:
            left -= 1
            right += 1
        return string[left + 1:right]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79573621
# https://blog.csdn.net/qqxx6661/article/details/76864410
# IDEA : DP
# STATUS EQUATION :
# dp[i, j] = 1                                        if i == j
#          = s[i] == s[j]                             if j = i + 1
#          = s[i] == s[j] && dp[i + 1][j - 1]         if j > i + 1
# time = O(n^2)
# space = O(n^2)
class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: str
        """
        if len(set(s)) == 1: return s
        n = len(s)
        start, end, maxL = 0, 0, 0
        dp = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(i):
                dp[j][i] = (s[j] == s[i]) & ((i - j < 2) | dp[j + 1][i - 1])
                if dp[j][i] and maxL < i - j + 1:
                    maxL = i - j + 1
                    start = j
                    end = i
            dp[i][i] = 1
        return s[start : end + 1]

# V1''
# https://www.jiuzhang.com/solution/longest-palindromic-substring/#tag-highlight-lang-python
# time = O(n^2)
# space = O(n^2)
class Solution:
    """
    @param s: input string
    @return: the longest palindromic substring
    """
    def longestPalindrome(self, s):
        if not s:
            return ""
            
        n = len(s)
        is_palindrome = [[False] * n for _ in range(n)]
        
        for i in range(n):
            is_palindrome[i][i] = True
        for i in range(1, n):
            is_palindrome[i][i - 1] = True
            
        longest, start, end = 1, 0, 0
        for length in range(1, n):
            for i in range(n - length):
                j = i + length
                is_palindrome[i][j] = s[i] == s[j] and is_palindrome[i + 1][j - 1]
                if is_palindrome[i][j] and length + 1 > longest:
                    longest = length + 1
                    start, end = i, j
                    
        return s[start:end + 1]

# V1'''
# https://www.jiuzhang.com/solution/longest-palindromic-substring/#tag-highlight-lang-python
# time = O(n^2)
# space = O(1)
class Solution:
    """
    @param s: input string
    @return: the longest palindromic substring
    """
    def longestPalindrome(self, s):
        if not s:
            return ""
            
        longest = ""
        for middle in range(len(s)):
            sub = self.find_palindrome_from(s, middle, middle)
            if len(sub) > len(longest):
                longest = sub
            sub = self.find_palindrome_from(s, middle, middle + 1)
            if len(sub) > len(longest):
                longest = sub

        return longest
        
    def find_palindrome_from(self, string, left, right):
        while left >= 0 and right < len(string) and string[left] == string[right]:
            left -= 1
            right += 1    
        return string[left + 1:right]

# V2
# time = O(n)
# space = O(n)
class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: str
        """
        def preProcess(s):
            if not s:
                return ['^', '$']
            T = ['^']
            for c in s:
                T +=  ['#', c]
            T += ['#', '$']
            return T

        T = preProcess(s)
        P = [0] * len(T)
        center, right = 0, 0
        for i in range(1, len(T) - 1):
            i_mirror = 2 * center - i
            if right > i:
                P[i] = min(right - i, P[i_mirror])
            else:
                P[i] = 0

            while T[i + 1 + P[i]] == T[i - 1 - P[i]]:
                P[i] += 1

            if i + P[i] > right:
                center, right = i, i + P[i]

        max_i = 0
        for i in range(1, len(T) - 1):
            if P[i] > P[max_i]:
                max_i = i
        start = (max_i - 1 - P[max_i]) / 2
        return s[start : start + P[max_i]]

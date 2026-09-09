"""

926. Flip String to Monotone Increasing
Medium

A binary string is monotone increasing if it consists of some number of 0's (possibly none), followed by some number of 1's (also possibly none).

You are given a binary string s. You can flip s[i] changing it from 0 to 1 or from 1 to 0.

Return the minimum number of flips to make s monotone increasing.

 

Example 1:

Input: s = "00110"
Output: 1
Explanation: We flip the last digit to get 00111.
Example 2:

Input: s = "010110"
Output: 2
Explanation: We flip to get 011111, or alternatively 000111.
Example 3:

Input: s = "00011000"
Output: 2
Explanation: We flip to get 00000000.
 

Constraints:

1 <= s.length <= 105
s[i] is either '0' or '1'.

"""



# V0
# IDEA: 1D DP (dp + one_so_far) (gpt)
"""
NOTE !!!

DP def

    
     - dp[i] 代表的是：長度為 i 的前綴 (prefix) 子字串 s[0 ... i-1] 
       達到單調遞增的最少翻轉次數。

     - dp[i] = 把前 i 個字元 s[:i] 變成 monotone increasing
       所需要的最少 flip 次數。

DP eq
"""
class Solution(object):
    def minFlipsMonoIncr(self, s):
        """
        :type s: str
        :rtype: int
        """
        if len(s) <= 1:
            return 0

        n = len(s)

        # dp[i] = minimum flips to make s[:i] monotone increasing
        dp = [0] * (n + 1)

        one_so_far = 0

        for i in range(1, n + 1):

            """
            NOTE !!!

            val is `cur` val, NOT the `prev` val

            e.g.

            val = s[i - 1]
                -> val = 目前正在處理的 character


            ---

            
            ->


            ### 1. 為什麼 `val = s[i - 1]` 是「當前字元」而不是前一個字元？

            因為程式碼中的 `dp` 採用了 **長度觀點（1-based DP）**：

            * `dp[i]` 代表的是：長度為 $i$ 的前綴子字串 `s[0 ... i-1]` 達到單調遞增的最少翻轉次數。
            * 迴圈 `for i in range(1, n + 1)` 中的 $i$ 代表**當前正在處理前綴的長度**：
            * 當 $i = 1$ 時：考慮前綴長度為 1，最後一個字元（即當前字元）是 `s[1 - 1]` = `s[0]`。
            * 當 $i = 2$ 時：考慮前綴長度為 2，最後一個字元（即當前字元）是 `s[2 - 1]` = `s[1]`。
            * 當 $i = k$ 時：考慮前綴長度為 $k$，最後一個字元（即當前字元）是 `s[k - 1]`。



            因此，`val = s[i - 1]` 取出的恰好就是當前正在處理的那一個字元，而不是前一個字元！

            """

            # current character
            val = s[i - 1]

            

            """
            NOTE !!!


            1. we track
                if cur val == "0" or == "1"


            2. if cur val == "0" 
                -> either we
                    - flip ALL prev 1 to 0
                    - flip cur val to 1
            """
            if val == '0':
                # Current is 0.
                #
                # Option 1:
                # Keep current 0.
                # Then all previous 1s must become 0.
                #
                # Option 2:
                # Flip current 0 -> 1.
                dp[i] = min(
                    one_so_far,
                    dp[i - 1] + 1
                )

            else:
                # Current is 1.
                #
                # Keep current 1.
                dp[i] = dp[i - 1]

                one_so_far += 1

        return dp[n]


# V0-0-1
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)  # n = len(s)
# space = O(1)
class Solution:
    def minFlipsMonoIncr(self, s):
        ones = 0
        """
        NOTE !!! :
            -> assume all element in s is "0"
            -> then we adjust this hypothesis below

        ones : # of ones on [:k]
        zeros : # of zero on [k+1:]
        """
        res = zeros = s.count("0")
        # go through s
        for c in s:
            """
            case 1) if current c == "1"
                -> all right MUST be current zeros
                -> while left ones need to plus 1
            """
            if c == "1":
                ones, zeros = (ones + 1, zeros)
            # """
            # case 2) if current c == "0"
            #     -> all right MUST be current - 1 zeros
            #     -> while left ones be the same
            # """
            else:
                ones, zeros = (ones, zeros - 1)
            """
            NOTE : the op (flip) we need to take :
                -> num(ones) + nums(zeros)
                since we ones "1" on [:k] and zeros on [k+1:]
                -> so in order to make the string "Monotone Increasing"
                -> we need to 
                    -> flip "1" on [:k] to "0"
                    -> flip "0" in [k+1:] to "1"

                    -> so (ones + zeros) op
            """
            res = min(res, ones + zeros)
        return res


# V0-1
# IDEA: 1D DP (dp + one_so_far) (gpt)
class Solution(object):
    def minFlipsMonoIncr(self, s):
        """
        :type s: str
        :rtype: int
        """
        if len(s) <= 1:
            return 0

        n = len(s)

        # dp[i] = minimum flips to make s[:i] monotone increasing
        dp = [0] * (n + 1)

        one_so_far = 0

        for i in range(1, n + 1):
            val = s[i - 1]

            if val == '0':
                # Option 1:
                # keep this 0
                # -> flip all previous 1s to 0
                #
                # Option 2:
                # flip this 0 -> 1
                dp[i] = min(
                    one_so_far,
                    dp[i - 1] + 1
                )

            else:
                # val == '1'

                # Keep this 1
                dp[i] = dp[i - 1]

                one_so_far += 1

        return dp[n]


# V0-2
# IDEA: 1D DP (gpt)
class Solution(object):
    def minFlipsMonoIncr(self, s):
        """
        :type s: str
        :rtype: int
        """
        if len(s) <= 1:
            return 0

        # dp = minimum flips so far
        # ones = number of 1s seen so far
        dp = 0
        ones = 0

        for ch in s:
            if ch == '1':
                # Keep this 1
                ones += 1
            else:
                # ch == '0'

                # Option 1:
                # flip this 0 -> 1
                #
                # Option 2:
                # flip all previous 1s -> 0
                dp = min(dp + 1, ones)

        return dp


# V0'
# IDEA : PREFIX SUM
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)  # n = len(S)
# space = O(n)  # prefix sum array P
class Solution(object):
    def minFlipsMonoIncr(self, S):
        # get pre-fix sum
        P = [0]
        for x in S:
            P.append(P[-1] + int(x))
        # find min
        res = float('inf')
        for j in range(len(P)):
            res = min(res, P[j] + len(S)-j-(P[-1]-P[j]))
        return res

# V0''
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)  # n = len(s)
# space = O(1)
class Solution:
    def minFlipsMonoIncr(self, s, ones = 0):
        res = zeros = s.count("0")
        for c in s:
            ones, zeros = (ones + 1, zeros) if c == "1" else (ones, zeros - 1)
            res = min(res, ones + zeros)
        return res

# V1
# IDEA : PREFIX SUM
# https://leetcode.com/problems/flip-string-to-monotone-increasing/solution/
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)  # n = len(S)
# space = O(n)  # prefix sum array P
class Solution(object):
    def minFlipsMonoIncr(self, S):
        # get pre-fix sum
        P = [0]
        for x in S:
            P.append(P[-1] + int(x))
        # return min
        return min(P[j] + len(S)-j-(P[-1]-P[j])
                   for j in range(len(P)))

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83247054
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)  # n = len(S)
# space = O(n)  # prefix sum array P
class Solution(object):
    def minFlipsMonoIncr(self, S):
        """
        :type S: str
        :rtype: int
        """
        N = len(S)
        P = [0] # how many ones
        res = float('inf')
        for s in S:
            P.append(P[-1] + int(s))
        return min(P[i] + (N - P[-1]) - (i - P[i]) for i in range(len(P)))
    
# V1''
# https://leetcode.com/problems/flip-string-to-monotone-increasing/discuss/184080/Python-3-liner
# IDEA
# We start with assuming "111.." section occupies all string, s.
# Then we update "000.." section as s[:i + 1] and "111.." section as s[i + 1:] during iteration as well as the result
# "zeros" variable counts all misplaced "0"s and "ones" variable counts all misplaced "1"s
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)  # n = len(s)
# space = O(1)
class Solution:
    def minFlipsMonoIncr(self, s, ones = 0):
        res = zeros = s.count("0")
        for c in s:
            ones, zeros = (ones + 1, zeros) if c == "1" else (ones, zeros - 1)
            res = min(res, ones + zeros)
        return res

# V1'''
# https://leetcode.com/problems/flip-string-to-monotone-increasing/discuss/184080/Python-3-liner
# IDEA :
#  -> We start with assuming "111.." section occupies all string, s.
#  -> Then we update "000.." section as s[:i + 1] and "111.." section as s[i + 1:] during iteration as well as the result
#  -> "zeros" variable counts all misplaced "0"s and "ones" variable counts all misplaced "1"s
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)  # n = len(s)
# space = O(1)
class Solution:
    def minFlipsMonoIncr(self, s):
        res = cur = s.count("0")
        for c in s:
            cur = cur + 1 if c == "1" else cur - 1
            res = min(res, cur)
        return res

# V1'''''
# https://leetcode.com/problems/flip-string-to-monotone-increasing/discuss/184080/Python-3-liner
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)  # n = len(s)
# space = O(1)
class Solution:
    def minFlipsMonoIncr(self, s):
        res = cur = s.count("0")
        for c in s: res, cur = c == "1" and (res, cur + 1) or (min(res, cur - 1), cur - 1)
        return res

# V1''''''
# https://www.jiuzhang.com/solution/flip-string-to-monotone-increasing/#tag-highlight-lang-python
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)  # n = len(S)
# space = O(1)
class Solution:
    """
    @param S: a string
    @return: the minimum number
    """
    def minFlipsMonoIncr(self, S):
        # Write your code here.
        m, n = 0, 0
        for s in S:
            m += int(s)
            n = min(m, n + 1 - int(s))
        return n

# V2
"""

DP def
    the final string is  0...0 1...1, so it is decided by ONE cut point k:
    everything left of k must be '0', everything from k on must be '1'

    ones[k] : # of '1' in s[0:k]      -> each must be flipped to '0'
    zeros[k]: # of '0' in s[k:]       -> each must be flipped to '1'

DP eq

     cost(k) = ones[k] + zeros[k]

     sweeping k left to right:

        s[k] == '1' : ones += 1                # a '1' joins the left part
        s[k] == '0' : zeros -= 1               # a '0' leaves the right part


    -> e.g. equivalent 2-state form
         dp[i][0] = dp[i-1][0] + (s[i] == '1')            # prefix all 0s
         dp[i][1] = min(dp[i-1][0], dp[i-1][1]) + (s[i] == '0')

     init: assume ALL characters become '0' -> res = zeros = s.count("0")
     ans = min over k of cost(k)

"""
# time = O(n)
# space = O(1)
class Solution(object):
    def minFlipsMonoIncr(self, S):
        """
        :type S: str
        :rtype: int
        """
        flip0, flip1 = 0, 0
        for c in S:
            flip0 += int(c == '1')
            flip1 = min(flip0, flip1 + int(c == '0'))
        return flip1

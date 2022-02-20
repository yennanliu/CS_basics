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
# IDEA : PREFIX SUM
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

# V0'
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
class Solution:
    def minFlipsMonoIncr(self, s, ones = 0):
        res = zeros = s.count("0")
        for c in s:
            ones, zeros = (ones + 1, zeros) if c == "1" else (ones, zeros - 1)
            res = min(res, ones + zeros)
        return res

# V1'''
# https://leetcode.com/problems/flip-string-to-monotone-increasing/discuss/184080/Python-3-liner
class Solution:
    def minFlipsMonoIncr(self, s):
        res = cur = s.count("0")
        for c in s:
            cur = cur + 1 if c == "1" else cur - 1
            res = min(res, cur)
        return res

# V1'''''
# https://leetcode.com/problems/flip-string-to-monotone-increasing/discuss/184080/Python-3-liner
class Solution:
    def minFlipsMonoIncr(self, s):
        res = cur = s.count("0")
        for c in s: res, cur = c == "1" and (res, cur + 1) or (min(res, cur - 1), cur - 1)
        return res

# V1''''''
# https://www.jiuzhang.com/solution/flip-string-to-monotone-increasing/#tag-highlight-lang-python
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
# Time:  O(n)
# Space: O(1)
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
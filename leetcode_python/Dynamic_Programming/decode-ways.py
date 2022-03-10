"""

91. Decode Ways
Medium

A message containing letters from A-Z can be encoded into numbers using the following mapping:

'A' -> "1"
'B' -> "2"
...
'Z' -> "26"
To decode an encoded message, all the digits must be grouped then mapped back into letters using the reverse of the mapping above (there may be multiple ways). For example, "11106" can be mapped into:

"AAJF" with the grouping (1 1 10 6)
"KJF" with the grouping (11 10 6)
Note that the grouping (1 11 06) is invalid because "06" cannot be mapped into 'F' since "6" is different from "06".

Given a string s containing only digits, return the number of ways to decode it.

The test cases are generated so that the answer fits in a 32-bit integer.

 

Example 1:

Input: s = "12"
Output: 2
Explanation: "12" could be decoded as "AB" (1 2) or "L" (12).
Example 2:

Input: s = "226"
Output: 3
Explanation: "226" could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).
Example 3:

Input: s = "06"
Output: 0
Explanation: "06" cannot be mapped to "F" because of the leading zero ("6" is different from "06").
 

Constraints:

1 <= s.length <= 100
s contains only digits and may contain leading zero(s).

"""

# V0
# IDEA : DP
# IDEA : 
# dp[i]=⎧⎩⎨⎪⎪dp[i−1]+dp[i−2], 10 <=s[i-2:2]<=26 and s[i−2:2]!=[10 or 20]
# dp[i−2],s[i-2:i] = [10 or 20]
# dp[i−1],others
# DEMO 
# my_s_list = ["22", "223", "2234", "22342", "223423", "2234234"]
# s  = Solution()
# for i in my_s_list:
#     output = s.numDecodings(i)
#     print ('output :', output)
# dp : [1, 1, 2]
# output : 2
# dp : [1, 1, 2, 3]
# output : 3
# dp : [1, 1, 2, 3, 3]
# output : 3
# dp : [1, 1, 2, 3, 3, 3]
# output : 3
# dp : [1, 1, 2, 3, 3, 3, 6]
# output : 6
# dp : [1, 1, 2, 3, 3, 3, 6, 6]
# output : 6
class Solution:
    # @param s, a string
    # @return an integer
    def numDecodings(self, s):
        if s=="" or s[0]=='0': return 0
        dp=[1,1]
        for i in range(2,len(s)+1):
            if 10 <=int(s[i-2:i]) <=26 and s[i-1]!='0':
                dp.append(dp[i-1]+dp[i-2])
            elif int(s[i-2:i])==10 or int(s[i-2:i])==20:
                dp.append(dp[i-2])
            elif s[i-1]!='0':
                dp.append(dp[i-1])
            else:
                return 0
        return dp[len(s)]

# V1 
# https://blog.csdn.net/qian2729/article/details/50570960
# https://www.cnblogs.com/zuoyuan/p/3783897.html
# IDEA : 
# dp[i]=⎧⎩⎨⎪⎪dp[i−1]+dp[i−2], 10 <=s[i-2:2]<=26 and s[i−2:2]!=[10 or 20]
# dp[i−2],s[i-2:i] = [10 or 20]
# dp[i−1],others
# DEMO 
# my_s_list = ["22", "223", "2234", "22342", "223423", "2234234"]
# s  = Solution()
# for i in my_s_list:
#     output = s.numDecodings(i)
#     print ('output :', output)
# dp : [1, 1, 2]
# output : 2
# dp : [1, 1, 2, 3]
# output : 3
# dp : [1, 1, 2, 3, 3]
# output : 3
# dp : [1, 1, 2, 3, 3, 3]
# output : 3
# dp : [1, 1, 2, 3, 3, 3, 6]
# output : 6
# dp : [1, 1, 2, 3, 3, 3, 6, 6]
# output : 6
class Solution(object):
    def numDecodings(self, s):
        """
        :type s: str
        :rtype: int
        """
        if len(s) == 0 or s[0] == '0':
            return 0
        dp = [0] * (max(len(s) + 1,2))
        dp[0],dp[1] = 1,1

        for i in range(2,len(s) + 1):
            if 10 <= int(s[i - 2:i]) <= 26 and s[i - 1] != '0':
                dp[i] = dp[i - 1] + dp[i - 2]
            elif int(s[i-2:i]) == 10 or int(s[i-2:i]) == 20:
                dp[i] = dp[i - 2]
            elif s[i-1] != '0':
                dp[i] = dp[i - 1]
            else:
                return 0
        return dp[len(s)]

# V1
# IDEA : Recursive Approach with Memoization
# https://leetcode.com/problems/decode-ways/solution/
class Solution:

    @lru_cache(maxsize=None)
    def recursiveWithMemo(self, index, s) -> int:
        # If you reach the end of the string
        # Return 1 for success.
        if index == len(s):
            return 1

        # If the string starts with a zero, it can't be decoded
        if s[index] == '0':
            return 0

        if index == len(s)-1:
            return 1
        
        answer = self.recursiveWithMemo(index + 1, s)
        if int(s[index : index + 2]) <= 26:
            answer += self.recursiveWithMemo(index + 2, s)

        return answer

    def numDecodings(self, s: str) -> int:
        return self.recursiveWithMemo(0, s)

# V1
# IDEA : Iterative Approach
# https://leetcode.com/problems/decode-ways/solution/
class Solution:
    def numDecodings(self, s: str) -> int:
        # Array to store the subproblem results
        dp = [0 for _ in range(len(s) + 1)]

        dp[0] = 1
        # Ways to decode a string of size 1 is 1. Unless the string is '0'.
        # '0' doesn't have a single digit decode.
        dp[1] = 0 if s[0] == '0' else 1


        for i in range(2, len(dp)):

            # Check if successful single digit decode is possible.
            if s[i - 1] != '0':
                dp[i] = dp[i - 1]

            # Check if successful two digit decode is possible.
            two_digit = int(s[i - 2 : i])
            if two_digit >= 10 and two_digit <= 26:
                dp[i] += dp[i - 2]
                
        return dp[len(s)]

# V1
# IDEA : Iterative, Constant Space
# https://leetcode.com/problems/decode-ways/solution/
class Solution:
    def numDecodings(self, s: str) -> int:
        if s[0] == "0":
            return 0
    
        two_back = 1
        one_back = 1
        for i in range(1, len(s)):
            current = 0
            if s[i] != "0":
                current = one_back
            two_digit = int(s[i - 1: i + 1])
            if two_digit >= 10 and two_digit <= 26:
                current += two_back
            two_back = one_back
            one_back = current
        
        return one_back

# V1'
# https://www.jiuzhang.com/solution/decode-ways/#tag-highlight-lang-python
# IDEA : DP
class Solution:
    # @param {string} s a string,  encoded message
    # @return {int} an integer, the number of ways decoding
    def numDecodings(self, s):
        if s == "" or s[0] == '0':
            return 0

        dp = [1, 1]
        for i in range(2,len(s) + 1):
            if 10 <= int(s[i - 2 : i]) <=26 and s[i - 1] != '0':
                dp.append(dp[i - 1] + dp[i - 2])
            elif int(s[i-2 : i]) == 10 or int(s[i - 2 : i]) == 20:
                dp.append(dp[i - 2])
            elif s[i-1] != '0':
                dp.append(dp[i-1])
            else:
                return 0
        return dp[len(s)]

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def numDecodings(self, s):
        """
        :type s: str
        :rtype: int
        """
        if len(s) == 0 or s[0] == '0':
            return 0
        prev, prev_prev = 1, 0
        for i in range(len(s)):
            cur = 0
            if s[i] != '0':
                cur = prev
            if i > 0 and (s[i - 1] == '1' or (s[i - 1] == '2' and s[i] <= '6')):
                cur += prev_prev
            prev, prev_prev = cur, prev
        return prev
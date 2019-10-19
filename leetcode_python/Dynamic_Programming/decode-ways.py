# V0
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
# idea : 
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

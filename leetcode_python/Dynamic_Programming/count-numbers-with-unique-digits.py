# V0 : DEV 
# DP state equation : dp[i]=dp[i−1]∗(11−i)
# https://www.jiuzhang.com/solution/count-numbers-with-unique-digits/

# V1
# http://bookshadow.com/weblog/2016/06/13/leetcode-count-numbers-with-unique-digits/
"""

DP def
    dp[i]: how many numbers with EXACTLY i digits have all-distinct digits

           -> the leading digit cannot be 0

DP eq

     dp[1] = 9                       # 1..9

     dp[i] = dp[i-1] * (11 - i)      # = dp[i-1] * (10 - (i-1))


    -> e.g. after fixing i-1 distinct digits there are
              10 - (i-1) unused digits left for the new position

     ans = 1 + dp[1] + dp[2] + ... + dp[n]      # the leading 1 is the number 0

"""
# time = O(n)
# space = O(1)
class Solution(object):
    def countNumbersWithUniqueDigits(self, n):
        """
        :type n: int
        :rtype: int
        """
        nums = [9]
        for x in range(9, 0, -1):
            nums += nums[-1] * x,
        return sum(nums[:n]) + 1


# V2
"""

DP def
    dp[i]: how many numbers with EXACTLY i digits have all-distinct digits

           -> the leading digit cannot be 0

DP eq

     dp[1] = 9                       # 1..9

     dp[i] = dp[i-1] * (11 - i)      # = dp[i-1] * (10 - (i-1))


    -> e.g. after fixing i-1 distinct digits there are
              10 - (i-1) unused digits left for the new position

     ans = 1 + dp[1] + dp[2] + ... + dp[n]      # the leading 1 is the number 0

"""
# time = O(n)
# space = O(1)
class Solution(object):
    def countNumbersWithUniqueDigits(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n == 0:
            return 1
        count, fk = 10, 9
        for k in range(2, n+1):
            fk *= 10 - (k-1)
            count += fk
        return count

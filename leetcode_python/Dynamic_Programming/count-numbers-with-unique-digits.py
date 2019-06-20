# V0 : DEV 
# DP state equation : dp[i]=dp[i−1]∗(11−i)
# https://www.jiuzhang.com/solution/count-numbers-with-unique-digits/

# V1 
# http://bookshadow.com/weblog/2016/06/13/leetcode-count-numbers-with-unique-digits/
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
# Time:  O(n)
# Space: O(1)
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

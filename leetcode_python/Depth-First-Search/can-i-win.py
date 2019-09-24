# V0 

# V1 
# http://bookshadow.com/weblog/2016/11/20/leetcode-can-i-win/
class Solution(object):
    def canIWin(self, maxChoosableInteger, desiredTotal):
        """
        :type maxChoosableInteger: int
        :type desiredTotal: int
        :rtype: bool
        """
        dp = dict()
        def search(state, total):
            for x in range(maxChoosableInteger, 0, -1):
                if not state & (1 << (x - 1)):
                    if total + x >= desiredTotal:
                        dp[state] = True
                        return True
                    break
            for x in range(1, maxChoosableInteger + 1):
                if not state & (1 << (x - 1)):
                    nstate = state | (1 << (x - 1))
                    if nstate not in dp:
                        dp[nstate] = search(nstate, total + x)
                    if not dp[nstate]:
                        dp[state] = True
                        return True
            dp[state] = False
            return False
        if maxChoosableInteger >= desiredTotal: return True
        if (1 + maxChoosableInteger) * maxChoosableInteger < 2 * desiredTotal: return False
        return search(0, 0)

# V1
# https://www.jiuzhang.com/solution/can-i-win/#tag-highlight-lang-python
# IDEA : DP
class Solution:
    """
    @param maxChoosableInteger: a Integer
    @param desiredTotal: a Integer
    @return: if the first player to move can force a win
    """

    def canIWin(self, maxChoosableInteger, desiredTotal):
        if (1 + maxChoosableInteger) * maxChoosableInteger / 2 < desiredTotal:
            return False
        self.memo = {}
        return self.helper(range(1, maxChoosableInteger + 1), desiredTotal)

    def helper(self, nums, desiredTotal):

        hash = str(nums)
        if hash in self.memo:
            return self.memo[hash]

        if nums[-1] >= desiredTotal:
            return True

        for i in range(len(nums)):
            if not self.helper(nums[:i] + nums[i+1:], desiredTotal - nums[i]):
                self.memo[hash] = True
                return True
        self.memo[hash] = False
        return False
        
# V2 
# Time:  O(n!)
# Space: O(n)
class Solution(object):
    def canIWin(self, maxChoosableInteger, desiredTotal):
        """
        :type maxChoosableInteger: int
        :type desiredTotal: int
        :rtype: bool
        """
        def canIWinHelper(maxChoosableInteger, desiredTotal, visited, lookup):
            if visited in lookup:
                return lookup[visited]

            mask = 1
            for i in range(maxChoosableInteger):
                if visited & mask == 0:
                    if i + 1 >= desiredTotal or \
                       not canIWinHelper(maxChoosableInteger, desiredTotal - (i + 1), visited | mask, lookup):
                        lookup[visited] = True
                        return True
                mask <<= 1
            lookup[visited] = False
            return False

        if (1 + maxChoosableInteger) * (maxChoosableInteger / 2) < desiredTotal:
            return False

        return canIWinHelper(maxChoosableInteger, desiredTotal, 0, {})


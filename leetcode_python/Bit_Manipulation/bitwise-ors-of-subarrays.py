# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83511833
# IDEA : DP
# STATUS EQ : dp[i] = [b | A[i] for b in dp[i - 1]] + A[i]
# time = O(n * 30)  # n = len(A); each cur has at most O(log(max(A))) distinct values
# space = O(n * 30)  # res can hold up to n * 30 distinct OR values
class Solution(object):
    def subarrayBitwiseORs(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        res = set()
        cur = set()
        for a in A:
            cur = {n | a for n in cur} | {a}
            res |= cur
        return len(res)
        
# V2
# time = O(32 * n)  # n = len(A)
# space = O(32 * n)  # result/curr can hold up to O(32 * n) distinct OR values
class Solution(object):
    def subarrayBitwiseORs(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        result, curr = set(), {0}
        for i in A:
            curr = {i} | {i | j for j in curr}
            result |= curr
        return len(result)
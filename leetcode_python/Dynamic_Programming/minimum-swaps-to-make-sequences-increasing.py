# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83269027
class Solution(object):
    def minSwap(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: int
        """
        N = len(A)
        keep = [float('inf')] * N
        swap = [float('inf')] * N
        keep[0] = 0
        swap[0] = 1
        for i in range(1, N):
            if A[i] > A[i - 1] and B[i] > B[i - 1]:
                keep[i] = keep[i - 1]
                swap[i] = swap[i - 1] + 1
            if A[i] > B[i - 1] and B[i] > A[i - 1]:
                keep[i] = min(keep[i], swap[i - 1])
                swap[i] = min(swap[i], keep[i - 1] + 1)
        return min(keep[N - 1], swap[N - 1])

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83269027
class Solution(object):
    def minSwap(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: int
        """
        N = len(A)
        dp = [[float('inf'), float('inf')] for _ in range(N)]
        dp[0][0] = 0
        dp[0][1] = 1
        for i in range(1, N):
            if A[i] > A[i - 1] and B[i] > B[i - 1]:
                dp[i][0] = dp[i - 1][0]
                dp[i][1] = dp[i - 1][1] + 1
            if A[i] > B[i - 1] and B[i] > A[i - 1]:
                dp[i][0] = min(dp[i][0], dp[i - 1][1])
                dp[i][1] = min(dp[i][1], dp[i - 1][0] + 1)
        return min(dp[N - 1][0], dp[N - 1][1])

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/83269027
class Solution(object):
    def minSwap(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: int
        """
        N = len(A)
        keep, swap = 0, 1
        for i in range(1, N):
            curswap, curkeep = float('inf'), float('inf')
            if A[i] > A[i - 1] and B[i] > B[i - 1]:
                curkeep, curswap = keep, swap + 1
            if A[i] > B[i - 1] and B[i] > A[i - 1]:
                curkeep, curswap = min(curkeep, swap), min(curswap, keep + 1)
            keep, swap = curkeep, curswap
        return min(keep, swap)

# V1'''
# https://www.jiuzhang.com/solution/minimum-swaps-to-make-sequences-increasing/#tag-highlight-lang-python
class Solution:
    def minSwap(self, A, B):
        if len(A) == 0 or len(A) != len(B):
            return 0
        non_swapped, swapped = [0] * len(A), [1] + [0] * (len(A) - 1)
        for i in range(1, len(A)):
            swps, no_swps = set(), set()
            if A[i - 1] < A[i] and B[i - 1] < B[i]:
                swps.add(swapped[i - 1] + 1)
                no_swps.add(non_swapped[i - 1])
            if B[i - 1] < A[i] and A[i - 1] < B[i]:
                swps.add(non_swapped[i - 1] + 1)
                no_swps.add(swapped[i - 1])
            swapped[i], non_swapped[i] = min(swps), min(no_swps)
        return min(swapped[-1], non_swapped[-1])

# V2
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def minSwap(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: int
        """
        dp_no_swap, dp_swap = [0]*2, [1]*2
        for i in range(1, len(A)):
            dp_no_swap[i%2], dp_swap[i%2] = float("inf"), float("inf")
            if A[i-1] < A[i] and B[i-1] < B[i]:
                dp_no_swap[i%2] = min(dp_no_swap[i%2], dp_no_swap[(i-1)%2])
                dp_swap[i%2] = min(dp_swap[i%2], dp_swap[(i-1)%2]+1)
            if A[i-1] < B[i] and B[i-1] < A[i]:
                dp_no_swap[i%2] = min(dp_no_swap[i%2], dp_swap[(i-1)%2])
                dp_swap[i%2] = min(dp_swap[i%2], dp_no_swap[(i-1)%2]+1)
        return min(dp_no_swap[(len(A)-1)%2], dp_swap[(len(A)-1)%2])

"""

873. Length of Longest Fibonacci Subsequence
Medium

A sequence x_1, x_2, ..., x_n is Fibonacci-like if:

n >= 3
x_i + x_i+1 == x_i+2 for all i + 2 <= n

Given a strictly increasing array arr of positive integers forming a sequence, return the length of the longest Fibonacci-like subsequence of arr. If one does not exist, return 0.

A subsequence is derived from another sequence arr by deleting any number of elements (including none) from arr, without changing the order of the remaining elements. For example, [3, 5, 8] is a subsequence of [3, 4, 5, 6, 7, 8].

Example 1:

Input: arr = [1,2,3,4,5,6,7,8]
Output: 5
Explanation: The longest subsequence that is fibonacci-like: [1,2,3,5,8].

Example 2:

Input: arr = [1,3,7,11,12,14,18]
Output: 3
Explanation: The longest subsequence that is fibonacci-like: [1,11,12], [3,11,14] or [7,11,18].

Constraints:

3 <= arr.length <= 1000
1 <= arr[i] < arr[i + 1] <= 10^9

"""

# V0

# V1 
# http://bookshadow.com/weblog/2018/07/22/leetcode-length-of-longest-fibonacci-subsequence/
# IDEA : DP
# STATUS EQ : dp[y][x + y] = max(dp[y][x + y], dp[x][y] + 1)
# time = O(n^2)
# space = O(n^2)  # dp dict keyed by (y, x+y) pairs
class Solution(object):
    def lenLongestFibSubseq(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        vset = set(A)
        dp = collections.defaultdict(lambda: collections.defaultdict(int))
        size = len(A)
        ans = 0
        for i in range(size):
            x = A[i]
            for j in range(i + 1, size):
                y = A[j]
                if x + y not in vset: continue
                dp[y][x + y] = max(dp[y][x + y], dp[x][y] + 1)
                ans = max(dp[y][x + y], ans)
        return ans and ans + 2 or 0

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82715323
# IDEA : GREEDY 
# time = O(n^2 log(max(A)))  # n = len(A); while loop grows Fibonacci-fast
# space = O(n)
class Solution(object):
    def lenLongestFibSubseq(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        s = set(A)
        n = len(A)
        res = 0
        for i in range(n - 1):
            for j in range(i + 1, n):
                a, b = A[i], A[j]
                count = 2
                while a + b in s:
                    a, b = b, a + b
                    count += 1
                    res = max(res, count)
        return res if res > 2 else 0
        
# V2 
# time = O(n^2)
# space = O(n)
class Solution(object):
    def lenLongestFibSubseq(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        lookup = set(A)
        result = 2
        for i in range(len(A)):
            for j in range(i+1, len(A)):
                x, y, l = A[i], A[j], 2
                while x+y in lookup:
                    x, y, l = y, x+y, l+1
                result = max(result, l)
        return result if result > 2 else 0
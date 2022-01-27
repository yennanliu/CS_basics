"""

646. Maximum Length of Pair Chain
Medium

You are given an array of n pairs pairs where pairs[i] = [lefti, righti] and lefti < righti.

A pair p2 = [c, d] follows a pair p1 = [a, b] if b < c. A chain of pairs can be formed in this fashion.

Return the length longest chain which can be formed.

You do not need to use up all the given intervals. You can select pairs in any order.

 

Example 1:

Input: pairs = [[1,2],[2,3],[3,4]]
Output: 2
Explanation: The longest chain is [1,2] -> [3,4].
Example 2:

Input: pairs = [[1,2],[7,8],[4,5]]
Output: 3
Explanation: The longest chain is [1,2] -> [4,5] -> [7,8].
 

Constraints:

n == pairs.length
1 <= n <= 1000
-1000 <= lefti < righti <= 1000

"""

# V0
# IDEA : GREEDY + sorting + 2 pointers
# IDEA :
# -> SORT ON "1st element" (0 index)
# -> define i pointer, cnt
# -> loop over pairts
# -> if j == 0 or pairs[j][0] > pairs[i][1]
#    -> make i = j, and cnt += 1
class Solution(object):
    def findLongestChain(self, pairs):
        pairs.sort(key=lambda x: x[1])
        cnt = 0
        i = 0
        for j in range(len(pairs)):
            if j == 0 or pairs[j][0] > pairs[i][1]:
                i = j
                cnt += 1
        return cnt

# V0 
# IDEA : GREEDY + sorting
# ->  we sort on pair's "2nd" element (0 index) -> possible cases that we can get sub pairs with max length with the needed conditions
# ->  we need to find the "max length" of "continous or non-continous" sub pairs (with condition)
#      -> so start from the "sorted 1st pair" CAN ALWAYS MAKE US GET THE MAX LENGTH of sub pairs with the condition ( we define a pair (c, d) can follow another pair (a, b) if and only if b < c. Chain of pairs can be formed in this fashion.)
##########
# DEMO
#     ...: 
#     ...: x = [[-10,-8],[8,9],[-5,0],[6,10],[-6,-4],[1,7],[9,10],[-4,7]]
#     ...: s = Solution()
#     ...: r = s.findLongestChain(x)
#     ...: print (r)
# pairs = [[-10, -8], [-6, -4], [-5, 0], [1, 7], [-4, 7], [8, 9], [6, 10], [9, 10]]
# x = -10 y = -8
#  currTime = -8 ans = 1
# x = -6 y = -4
#  currTime = -4 ans = 2
# x = -5 y = 0
#  currTime = -4 ans = 2
# x = 1 y = 7
#  currTime = 7 ans = 3
# x = -4 y = 7
#  currTime = 7 ans = 3
# x = 8 y = 9
#  currTime = 9 ans = 4
# x = 6 y = 10
#  currTime = 9 ans = 4
# x = 9 y = 10
#  currTime = 9 ans = 4
# 4
class Solution(object):
    def findLongestChain(self, pairs):
        pairs = sorted(pairs, key=lambda x : x[1])
        ### NOTICE HERE
        currTime, ans = float('-inf'), 0
        for x, y in pairs:
            ### NOTICE HERE
            if currTime < x:
                currTime = y
                ans += 1
        return ans

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79826524
# http://bookshadow.com/weblog/2017/07/24/leetcode-maximum-length-of-pair-chain/
# IDEA : GREEDY
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        pairs.sort(key=lambda x: x[1])
        currTime, ans = float('-inf'), 0
        for x, y in pairs:
            if currTime < x:
                currTime = y
                ans += 1
        return ans

### Test case :
s=Solution()
assert s.findLongestChain([[1,2], [2,3], [3,4]]) == 2
assert s.findLongestChain([]) == 0
assert s.findLongestChain([[1,1]]) == 1
assert s.findLongestChain([[1,2]]) == 1
assert s.findLongestChain([[1,2], [2,3], [3,4], [9,10]]) == 3
assert s.findLongestChain([[1,2], [2,3], [2,3], [3,4]]) == 2
assert s.findLongestChain([[1,2], [1,2], [1,2]]) == 1
assert s.findLongestChain([[i, i+1] for i in range(10)]) == 5

# V1'
# https://leetcode.com/problems/maximum-length-of-pair-chain/solution/
# IDEA : DP 
# time complexity : O(N^2)
# space complexity : O(N)
class Solution(object): #Time Limit Exceeded
    def findLongestChain(self, pairs):
        pairs.sort()
        dp = [1] * len(pairs)

        for j in range(len(pairs)):
            for i in range(j):
                if pairs[i][1] < pairs[j][0]:
                    dp[j] = max(dp[j], dp[i] + 1)

        return max(dp)

# V1''
# https://leetcode.com/problems/maximum-length-of-pair-chain/solution/
# IDEA : GREEDY
class Solution(object):
    def findLongestChain(self, pairs):
        cur, ans = float('-inf'), 0
        for x, y in sorted(pairs, key = operator.itemgetter(1)):
            if cur < x:
                cur = y
                ans += 1
        return ans

# V1'''
# https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105608/Python-DP-solution
# IDEA : DP
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        pairs.sort()
        n = len(pairs)
        dp = [1] * n
        for i in range(n):
            for j in range(i):
                if pairs[i][0] > pairs[j][1] and dp[i] < dp[j] + 1:
                    dp[i] = dp[j] + 1
        return max(dp)

# V1''''
# https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105640/O(nlogn)-Python-solution-binary-search-easy-to-understand
# IDEA : BINARY SEARCH
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        # sort by x for pairs (x1, y1), (x2, y2), (x3, y3)...
        pairs.sort()
            
        # min_end_y[i] is the ending tuple minimum y of length=i chain
        min_end_y = [float('inf')] * len(pairs)
        for x, y in pairs:
            # since (a, b) can chain (c, d) iff b < c, use bisect_left
            i = bisect.bisect_left(min_end_y, x)
            # greedy method, for the same length chain, the smaller y the better
            min_end_y[i] = min(min_end_y[i], y)  
        
        return bisect.bisect_left(min_end_y, float('inf'))

# V2 
# Time:  O(nlogn)
# Space: O(1)
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        pairs.sort(key=lambda x: x[1])
        cnt, i = 0, 0
        for j in range(len(pairs)):
            if j == 0 or pairs[i][1] < pairs[j][0]:
                cnt += 1
                i = j
        return cnt
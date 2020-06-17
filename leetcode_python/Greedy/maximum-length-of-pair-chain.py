# V0 
# IDEA : GREEDY
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
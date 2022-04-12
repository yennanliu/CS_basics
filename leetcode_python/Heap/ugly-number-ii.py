"""

264. Ugly Number II
Medium

An ugly number is a positive integer whose prime factors are limited to 2, 3, and 5.

Given an integer n, return the nth ugly number.

 

Example 1:

Input: n = 10
Output: 12
Explanation: [1, 2, 3, 4, 5, 6, 8, 9, 10, 12] is the sequence of the first 10 ugly numbers.
Example 2:

Input: n = 1
Output: 1
Explanation: 1 has no prime factors, therefore all of its prime factors are limited to 2, 3, and 5.
 

Constraints:

1 <= n <= 1690

"""

# V0 
# IDEA : HEAP
# using brute force is too slow -> time out error
# -> so here we generate "ugly number" by ourself, and order them via heap (heappush)
# -> and return the i-th element as request
import heapq
class Solution(object):
    def nthUglyNumber(self, n):
        # NOTE : we init heap as [1], visited = set([1])
        heap = [1]
        visited = set([1])      
        for i in range(n):
            # NOTE !!! trick here, we use last element via heappop
            val = heapq.heappop(heap)
            # and we genrate ugly by ourself
            for factor in [2,3,5]:
                if val*factor not in visited:
                    heapq.heappush(heap, val*factor)
                    visited.add(val*factor)    
        return val

# V1
# IDEA : HEAP
# using brute force is too slow -> time out error
# -> so here we generate "ugly number" by ourself, and order them via heap (heappush)
# -> and return the i-th element as request
# https://leetcode.com/problems/ugly-number-ii/discuss/337790/Python-Heap
class Solution(object):
    def nthUglyNumber(self, n):
        heap = [1]
        visited = set([1])      
        for i in range(n):
            val = heapq.heappop(heap)
            
            for factor in [2,3,5]:
                if val*factor not in visited:
                    heapq.heappush(heap, val*factor)
                    visited.add(val*factor)    
        return val

# V1'
# IDEA : 3 INDEX
# https://leetcode.com/problems/ugly-number-ii/discuss/719826/Python-3-index
class Solution:
    def nthUglyNumber(self, n):
        U = [1]
        i1 = i2 = i3 = 0
        for i in range(2,n+1):
            while U[i1]*2 <= U[-1]:
                i1 += 1
            while U[i2]*3 <= U[-1]:
                i2 += 1
            while U[i3]*5 <= U[-1]:
                i3 += 1
            p1 = U[i1]*2
            p2 = U[i2]*3
            p3 = U[i3]*5
            if p1 == min(p1,p2,p3):
                U.append(p1)
                continue
            elif p2 == min(p1,p2,p3):
                U.append(p2)
                continue
            else:
                U.append(p3)
                continue
        return U[-1]

# V1''
# IDEA : DP
# https://blog.csdn.net/fuxuemingzhu/article/details/49231615
# https://blog.csdn.net/weixin_37725502/article/details/79424819
# https://blog.51cto.com/u_15302258/3083799
class Solution(object):
    def nthUglyNumber(self, n):
        if n < 0:
            return 0
        dp = [1] * n
        index2, index3, index5 = 0, 0, 0
        for i in range(1, n):
            dp[i] = min(2 * dp[index2], 3 * dp[index3], 5 * dp[index5])
            if dp[i] == 2 * dp[index2]: index2 += 1
            if dp[i] == 3 * dp[index3]: index3 += 1
            if dp[i] == 5 * dp[index5]: index5 += 1
        return dp[n - 1]

# V1'''
# IDEA : DP
# https://leetcode.com/problems/ugly-number-ii/discuss/69467/Python-solution-using-dp.
class Solution:
    def nthUglyNumber(self, n):
        ugly = [0] * n
        nxt = ugly[0] = 1
        i2 = i3 = i5 = 0
        nxt2, nxt3, nxt5 = ugly[i2]*2, ugly[i3]*3, ugly[i5]*5
        for i in xrange(1, n):
            nxt = min(nxt2, nxt3, nxt5)
            ugly[i] = nxt
            if nxt == nxt2:
                i2 += 1
                nxt2 = ugly[i2]*2
            if nxt == nxt3:
                i3 += 1
                nxt3 = ugly[i3]*3
            if nxt == nxt5:
                i5 += 1
                nxt5 = ugly[i5]*5
        return nxt # ugly[-1]

# V2 
# Time:  O(n)
# Space: O(1)
import heapq
class Solution(object):
    # @param {integer} n
    # @return {integer}
    def nthUglyNumber(self, n):
        ugly_number = 0

        heap = []
        heapq.heappush(heap, 1)
        for _ in range(n):
            ugly_number = heapq.heappop(heap)
            if ugly_number % 2 == 0:
                heapq.heappush(heap, ugly_number * 2)
            elif ugly_number % 3 == 0:
                heapq.heappush(heap, ugly_number * 2)
                heapq.heappush(heap, ugly_number * 3)
            else:
                heapq.heappush(heap, ugly_number * 2)
                heapq.heappush(heap, ugly_number * 3)
                heapq.heappush(heap, ugly_number * 5)

        return ugly_number

    def nthUglyNumber2(self, n):
        ugly = [1]
        i2 = i3 = i5 = 0
        while len(ugly) < n:
            while ugly[i2] * 2 <= ugly[-1]: i2 += 1
            while ugly[i3] * 3 <= ugly[-1]: i3 += 1
            while ugly[i5] * 5 <= ugly[-1]: i5 += 1
            ugly.append(min(ugly[i2] * 2, ugly[i3] * 3, ugly[i5] * 5))
        return ugly[-1]

    def nthUglyNumber3(self, n):
        q2, q3, q5 = [2], [3], [5]
        ugly = 1
        for u in heapq.merge(q2, q3, q5):
            if n == 1:
                return ugly
            if u > ugly:
                ugly = u
                n -= 1
                q2 += 2 * u,
                q3 += 3 * u,
                q5 += 5 * u,

class Solution2(object):
    ugly = sorted(2**a * 3**b * 5**c
                  for a in range(32) for b in range(20) for c in range(14))

    def nthUglyNumber(self, n):
        return self.ugly[n-1]

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82997932
class Solution:
    def soupServings(self, N):
        """
        :type N: int
        :rtype: float
        """
        self.memo = dict()
        if N > 5600: return 1.0
        return self.solve(N, N)
    
    def solve(self, A, B):
        if (A, B) in self.memo:
            return self.memo[(A, B)]
        if A <= 0 and B > 0: return 1
        if A <= 0 and B <= 0: return 0.5
        if A > 0 and B <= 0: return 0
        prob = 0.25 * (self.solve(A - 100, B) + self.solve(A - 75, B - 25)
                       + self.solve(A - 50, B - 50) + self.solve(A - 25, B - 75))
        self.memo[(A, B)] = prob
        return prob
        
# V2
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def soupServings(self, N):
        """
        :type N: int
        :rtype: float
        """
        def dp(a, b, lookup):
            if (a, b) in lookup:
                return lookup[a, b]
            if a <= 0 and b <= 0:
                return 0.5
            if a <= 0:
                return 1.0
            if b <= 0:
                return 0.0
            lookup[a, b] = 0.25 * (dp(a-4, b, lookup) +
                                   dp(a-3, b-1, lookup) +
                                   dp(a-2, b-2, lookup) +
                                   dp(a-1, b-3, lookup))
            return lookup[a, b]

        if N >= 4800:
            return 1.0
        lookup = {}
        N = (N+24)//25
        return dp(N, N, lookup)
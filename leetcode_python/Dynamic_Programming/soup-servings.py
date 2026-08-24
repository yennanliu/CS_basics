# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82997932
"""

DP def
    all four servings are multiples of 25, so scale by 25 and the state is
    just the pair of remaining amounts

    dp(A, B): PROBABILITY that A empties first, plus HALF the probability

              that both empty at the same time

DP eq

     dp(A, B) = 0.25 * ( dp(A-100, B) + dp(A-75, B-25)

                       + dp(A-50, B-50) + dp(A-25, B-75) )

     base:  A <= 0 and B >  0  ->  1        # A alone ran out
            A <= 0 and B <= 0  ->  0.5      # both at once, counted half
            A >  0 and B <= 0  ->  0        # B alone ran out


    -> e.g. NOTE !!! for large N the answer converges to 1.0 extremely fast
              (A drains faster than B on average), so N > 5600 returns 1.0
              outright - that early exit is what bounds the state space

     ans = dp(N, N)

"""
# time = O(1)  # N capped at 5600 by early return, so states are bounded
# space = O(1)
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
"""

DP def
    all four servings are multiples of 25, so scale by 25 and the state is
    just the pair of remaining amounts

    dp(A, B): PROBABILITY that A empties first, plus HALF the probability

              that both empty at the same time

DP eq

     dp(A, B) = 0.25 * ( dp(A-100, B) + dp(A-75, B-25)

                       + dp(A-50, B-50) + dp(A-25, B-75) )

     base:  A <= 0 and B >  0  ->  1        # A alone ran out
            A <= 0 and B <= 0  ->  0.5      # both at once, counted half
            A >  0 and B <= 0  ->  0        # B alone ran out


    -> e.g. NOTE !!! for large N the answer converges to 1.0 extremely fast
              (A drains faster than B on average), so N > 5600 returns 1.0
              outright - that early exit is what bounds the state space

     ans = dp(N, N)

"""
# time = O(1)
# space = O(1)
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
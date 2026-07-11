# V1 : dev 

# V2
# https://blog.csdn.net/danspace1/article/details/88956805
# time = O(n * k)  # n = len(deck), k = number of distinct card values
# space = O(k)
class Solution(object):
    def hasGroupsSizeX(self, deck):
        """
        :type deck: List[int]
        :rtype: bool
        """
        def check(i):
            for n in d:
                if d[n] % i != 0:
                    return False
            return True
        
        if len(deck) < 2:
            return False
        d = collections.Counter(deck)
        m = min(d.values())
        for i in range(2, m+1):
            # check if the great common divisor is i
            if check(i):
                return True
        return False

# V3
# time = O(n * (logn)^2)
# space = O(n)
import collections
from functools import reduce
class Solution(object):
    def hasGroupsSizeX(self, deck):
        """
        :type deck: List[int]
        :rtype: bool
        """
        def gcd(a, b):  # Time: O((logn)^2)
            while b:
                a, b = b, a % b
            return a

        vals = list(collections.Counter(deck).values())
        return reduce(gcd, vals) >= 2

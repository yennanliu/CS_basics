"""

914. X of a Kind in a Deck of Cards
Easy

You are given an integer array deck where deck[i] represents the number written on the i^th card.

Partition the cards into one or more groups such that:

Each group has exactly x cards where x > 1, and
All the cards in one group have the same integer written on them.

Return true if such partition is possible, or false otherwise.

Example 1:

Input: deck = [1,2,3,4,4,3,2,1]
Output: true
Explanation: Possible partition [1,1],[2,2],[3,3],[4,4].

Example 2:

Input: deck = [1,1,1,2,2,2,3,3]
Output: false
Explanation: No possible partition.

Constraints:

1 <= deck.length <= 10^4
0 <= deck[i] < 10^4

"""

# V0

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

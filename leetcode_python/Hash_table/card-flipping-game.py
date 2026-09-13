"""

822. Card Flipping Game
Medium

You are given two 0-indexed integer arrays fronts and backs of length n, where the i^th card has the positive integer fronts[i] printed on the front and backs[i] printed on the back. Initially, each card is placed on a table such that the front number is facing up and the other is facing down. You may flip over any number of cards (possibly zero).

After flipping the cards, an integer is considered good if it is facing down on some card and not facing up on any card.

Return the minimum possible good integer after flipping the cards. If there are no good integers, return 0.

Example 1:

Input: fronts = [1,2,4,4,7], backs = [1,3,4,1,3]
Output: 2
Explanation:
If we flip the second card, the face up numbers are [1,3,4,4,7] and the face down are [1,2,4,1,3].
2 is the minimum good integer as it appears facing down but not facing up.
It can be shown that 2 is the minimum possible good integer obtainable after flipping some cards.

Example 2:

Input: fronts = [1], backs = [1]
Output: 0
Explanation:
There are no good integers no matter how we flip the cards, so we return 0.

Constraints:

n == fronts.length == backs.length
1 <= n <= 1000
1 <= fronts[i], backs[i] <= 2000

"""

# V0 

# V1
# http://bookshadow.com/weblog/2018/04/22/leetcode-card-flipping-game/
# IDEA : GREEDY
# time = O(n log n)
# space = O(n)
class Solution(object):
    def flipgame(self, fronts, backs):
        """
        :type fronts: List[int]
        :type backs: List[int]
        :rtype: int
        """
        numbers = set(fronts + backs)
        for n in sorted(numbers):
            if all(f != n or b != n for f, b in zip(fronts, backs)):
                return n
        return 0

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82861796
# time = O(n)
# space = O(n)
class Solution:
    def flipgame(self, fronts, backs):
        """
        :type fronts: List[int]
        :type backs: List[int]
        :rtype: int
        """
        s = set()
        res = float('inf')
        for f, b in zip(fronts, backs):
            if f == b:
                s.add(f)
        for f in fronts:
            if f not in s:
                res = min(res, f)
        for b in backs:
            if b not in s:
                res = min(res, b)
        return 0 if res == float('inf') else res
        
# V1''
# https://www.jiuzhang.com/solution/card-flipping-game/#tag-highlight-lang-python
class Solution:
    """
    @param fronts: 
    @param backs: 
    @return: nothing
    find the min value on two sides of the same card across different times
    """
    # time = O(n)
    # space = O(n)
    def flipgame(self, f, b):
        same = {x for x, y in zip(f, b) if x == y}
        return min([i for i in f + b if i not in same] or [0])

# V2
# time = O(n)
# space = O(n)
import itertools
class Solution(object):
    def flipgame(self, fronts, backs):
        """
        :type fronts: List[int]
        :type backs: List[int]
        :rtype: int
        """
        same = {n for i, n in enumerate(fronts) if n == backs[i]}
        result = float("inf")
        for n in itertools.chain(fronts, backs):
            if n not in same:
                result = min(result, n)
        return result if result < float("inf") else 0
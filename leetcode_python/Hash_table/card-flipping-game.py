# V0 

# V1 
# http://bookshadow.com/weblog/2018/04/22/leetcode-card-flipping-game/
# IDEA : GREEDY 
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
        
# V1'
# https://www.jiuzhang.com/solution/card-flipping-game/#tag-highlight-lang-python
class Solution:
    """
    @param fronts: 
    @param backs: 
    @return: nothing
    find the min value on two sides of the same card across different times
    """
    def flipgame(self, f, b):
        same = {x for x, y in zip(f, b) if x == y}
        return min([i for i in f + b if i not in same] or [0])

# V2 
# Time:  O(n)
# Space: O(n)
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
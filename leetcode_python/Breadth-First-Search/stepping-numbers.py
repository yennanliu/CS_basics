"""

1215. Stepping Numbers
Medium

A stepping number is an integer such that all of its adjacent digits have an absolute
difference of exactly 1.

For example, 321 is a stepping number while 421 is not.

Given two integers low and high, return a sorted list of all the stepping numbers in the
inclusive range [low, high].

Example 1:

Input: low = 0, high = 21
Output: [0,1,2,3,4,5,6,7,8,9,10,12,21]

Example 2:

Input: low = 10, high = 15
Output: [10,12]


Constraints:

0 <= low <= high <= 2 * 10^9

"""

# V0
# IDEA : BFS (build stepping numbers digit by digit)
#        seed the queue with 1..9, then from a number v with last digit x
#        the only 1-digit-longer stepping numbers are v*10 + (x-1) and v*10 + (x+1).
#        0 is a special case (single digit, cannot be extended, since 0*10+1 = 1 is a dup)
# time = O(2^d), d = number of digits of high
# space = O(2^d)
from collections import deque
class Solution(object):
    def countSteppingNumbers(self, low, high):
        res = []
        if low == 0:
            res.append(0)

        q = deque(range(1, 10))
        while q:
            v = q.popleft()
            # queue is generated in increasing order -> safe to stop here
            if v > high:
                break
            if v >= low:
                res.append(v)
            x = v % 10
            if x > 0:
                q.append(v * 10 + x - 1)
            if x < 9:
                q.append(v * 10 + x + 1)

        res.sort()
        return res


# V1
# IDEA : DFS (same generation rule, recursive)
# time = O(2^d), d = number of digits of high
# space = O(2^d)
class Solution_1(object):
    def countSteppingNumbers(self, low, high):
        res = set()
        if low == 0:
            res.add(0)

        def dfs(v):
            if v > high:
                return
            if v >= low:
                res.add(v)
            x = v % 10
            if x > 0:
                dfs(v * 10 + x - 1)
            if x < 9:
                dfs(v * 10 + x + 1)

        for d in range(1, 10):
            dfs(d)

        return sorted(res)

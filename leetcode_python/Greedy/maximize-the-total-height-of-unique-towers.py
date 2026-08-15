"""

3301. Maximize the Total Height of Unique Towers
Medium

You are given an array maximumHeight, where maximumHeight[i] denotes the maximum height the ith tower can be assigned.

Your task is to assign a height to each tower so that:

The height of the ith tower is a positive integer and does not exceed maximumHeight[i].
No two towers have the same height.

Return the maximum possible total sum of the tower heights. If it's not possible to assign heights, return -1.


Example 1:

Input: maximumHeight = [2,3,4,3]
Output: 10
Explanation:
We can assign heights in the following way: [1, 2, 4, 3].

Example 2:

Input: maximumHeight = [15,10]
Output: 25
Explanation:
We can assign heights in the following way: [15, 10].

Example 3:

Input: maximumHeight = [2,2,1]
Output: -1
Explanation:
It's impossible to assign positive heights to each index so that no two towers have the same height.


Constraints:

1 <= maximumHeight.length <= 10^5
1 <= maximumHeight[i] <= 10^9

"""

# V0
# IDEA : SORT DESCENDING AND KEEP EACH TOWER AS TALL AS THE RULES ALLOW
#
#   assign the tallest caps first. a tower can take its own cap unless a
#   previously assigned tower already used that height, in which case it must
#   drop just below :
#
#       height = min(cap, previous height - 1)
#
#   giving away as little as possible at each step leaves the most room for
#   the rest, and processing in descending order guarantees the previous
#   height is the binding one.
#
#   if that ever falls to 0 the assignment is impossible.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maximumTotalSum(self, maximumHeight):
        total = 0
        prev = float('inf')
        for cap in sorted(maximumHeight, reverse=True):
            h = min(cap, prev - 1)
            if h <= 0:
                return -1
            total += h
            prev = h
        return total

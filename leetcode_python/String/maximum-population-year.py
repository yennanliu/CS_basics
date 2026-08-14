"""

1854. Maximum Population Year
Easy

You are given a 2D integer array logs where each logs[i] = [birthi, deathi] indicates the birth and death years of the ith person.

The population of some year x is the number of people alive during that year. The ith person is counted in year x's population if x is in the inclusive range [birthi, deathi - 1]. Note that the person is not counted in the year that they die.

Return the earliest year with the maximum population.


Example 1:

Input: logs = [[1993,1999],[2000,2010]]
Output: 1993
Explanation: The maximum population is 1, and 1993 is the earliest year with this population.

Example 2:

Input: logs = [[1950,1961],[1960,1971],[1970,1981]]
Output: 1960
Explanation:
The maximum population is 2, and it had happened in years 1960 and 1970.
The earlier year between them is 1960.


Constraints:

1 <= logs.length <= 100
1950 <= birthi < deathi <= 2050

"""

# V0
# IDEA : DIFFERENCE ARRAY / SWEEP LINE (years are bounded to 1950..2050)
#
#   a person alive on [birth, death - 1] contributes
#     diff[birth] += 1  and  diff[death] -= 1
#   the running prefix sum of diff at year y == population of year y.
#
#   scanning years left -> right and keeping a STRICT "<" comparison
#   makes us keep the FIRST (earliest) year that reaches the maximum.
#
#   NOTE : offset the years by 1950 so the array is only 101 slots long.
#
# time = O(n + Y), Y = 101 year slots
# space = O(Y)
class Solution(object):
    def maximumPopulation(self, logs):
        OFFSET = 1950
        diff = [0] * 102

        for birth, death in logs:
            diff[birth - OFFSET] += 1
            diff[death - OFFSET] -= 1

        cur = 0
        best = 0
        res = 0
        for i in range(len(diff)):
            cur += diff[i]
            if cur > best:
                best = cur
                res = i

        return res + OFFSET

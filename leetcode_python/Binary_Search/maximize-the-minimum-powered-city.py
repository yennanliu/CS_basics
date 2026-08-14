"""

2528. Maximize the Minimum Powered City
Hard

You are given a 0-indexed integer array stations of length n, where stations[i]
represents the number of power stations in the ith city.

Each power station can provide power to every city in a fixed range. In other
words, if the range is denoted by r, then a power station at city i can provide
power to all cities j such that |i - j| <= r and 0 <= i, j <= n - 1.

Note that |x| denotes absolute value. For example, |7 - 5| = 2 and |3 - 10| = 7.

The power of a city is the total number of power stations it is being provided
power from.

The government has sanctioned building k more power stations, each of which can
be built in any city, and have the same range as the pre-existing ones.

Given the two integers r and k, return the maximum possible minimum power of a
city, if the additional power stations are built optimally.

Note that you can build the k power stations in multiple cities.


Example 1:

Input: stations = [1,2,4,5,0], r = 1, k = 2
Output: 5
Explanation:
One of the optimal ways is to install both the power stations at city 1.
So stations will become [1,4,4,5,0].
- City 0 is provided by 1 + 4 = 5 power stations.
- City 1 is provided by 1 + 4 + 4 = 9 power stations.
- City 2 is provided by 4 + 4 + 5 = 13 power stations.
- City 3 is provided by 5 + 4 = 9 power stations.
- City 4 is provided by 5 + 0 = 5 power stations.
So the minimum power of a city is 5.
Since it is not possible to obtain a larger power, we return 5.

Example 2:

Input: stations = [4,4,4,4], r = 0, k = 3
Output: 4
Explanation:
It can be proved that we cannot make the minimum power of a city greater than 4.


Constraints:

n == stations.length
1 <= n <= 10^5
0 <= stations[i] <= 10^5
0 <= r <= n - 1
0 <= k <= 10^9

"""

# V0
# IDEA : BINARY SEARCH ON ANSWER + DIFFERENCE ARRAY + GREEDY (sweep left->right)
#
#   "maximize the minimum" with a monotone feasibility test -> binary search.
#   If a floor x is achievable, every smaller floor is too, so binary search
#   the largest feasible x.
#
#   base powers : each station at i covers [i-r, i+r]; build them with a
#   difference array + prefix sum in O(n) instead of O(n*r).
#
#   check(x) : sweep i from left to right maintaining the running power of
#   city i (base power + the effect of stations we have added so far, again
#   tracked with a difference array). If city i is short by `need`, we MUST
#   add `need` stations now -- city i can only be helped by builds in
#   [i-r, i+r], and everything left of i is already settled. Greedily build
#   them at j = min(i + r, n - 1), the rightmost city that still covers i,
#   so the new coverage window [j-r, j+r] reaches as far right as possible.
#   Feasible iff total added <= k.
#
#   NOTE : the greedy "push builds as far right as legally possible" is what
#          makes one left-to-right pass optimal -- building any earlier is
#          never better since it covers a strict prefix-shifted window.
#
#   NOTE : the running total `t` must be bumped by `need` immediately after
#          the build, because the new window starts covering city i itself.
#
#   NOTE : answers exceed 32-bit (up to sum(stations) + k ~ 1.1e10), so the
#          hi bound is sum(stations) + k, not something like 10^5.
#
# time = O(n * log(sum(stations) + k)), space = O(n)
class Solution(object):
    def maxPower(self, stations, r, k):
        n = len(stations)

        # base[i] = power of city i before any new build
        diff = [0] * (n + 1)
        for i, v in enumerate(stations):
            if v:
                lo, hi = max(0, i - r), min(i + r, n - 1)
                diff[lo] += v
                diff[hi + 1] -= v
        base = [0] * n
        run = 0
        for i in range(n):
            run += diff[i]
            base[i] = run

        def check(x):
            add = [0] * (n + 1)
            budget = k
            t = 0
            for i in range(n):
                t += add[i]
                need = x - (base[i] + t)
                if need > 0:
                    if need > budget:
                        return False
                    budget -= need
                    # build at the rightmost city that still covers i
                    j = min(i + r, n - 1)
                    lo, hi = max(0, j - r), min(j + r, n - 1)
                    add[lo] += need
                    add[hi + 1] -= need
                    t += need
            return True

        lo, hi = 0, sum(stations) + k
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if check(mid):
                lo = mid
            else:
                hi = mid - 1
        return lo

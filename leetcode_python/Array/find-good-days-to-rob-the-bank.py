"""

2100. Find Good Days to Rob the Bank
Medium

You and a gang of thieves are planning on robbing a bank. You are given a 0-indexed integer array security, where security[i] is the number of guards on duty on the ith day. The days are numbered starting from 0. You are also given an integer time.

The ith day is a good day to rob the bank if:

There are at least time days before and after the ith day,
The number of guards at the bank for the time days before i are non-increasing, and
The number of guards at the bank for the time days after i are non-decreasing.

More formally, this means day i is a good day to rob the bank if and only if security[i - time] >= security[i - time + 1] >= ... >= security[i] <= ... <= security[i + time - 1] <= security[i + time].

Return a list of all days (0-indexed) that are good days to rob the bank. The order that the days are returned in does not matter.


Example 1:

Input: security = [5,3,3,3,5,6,2], time = 2
Output: [2,3]
Explanation:
On day 2, we have security[0] >= security[1] >= security[2] <= security[3] <= security[4].
On day 3, we have security[1] >= security[2] >= security[3] <= security[4] <= security[5].
No other days satisfy this condition, so days 2 and 3 are the only good days to rob the bank.

Example 2:

Input: security = [1,1,1,1,1], time = 0
Output: [0,1,2,3,4]
Explanation:
Since time equals 0, every day is a good day to rob the bank, so return every day.

Example 3:

Input: security = [1,2,3,4,5,6], time = 2
Output: []
Explanation:
No day has 2 days before it that have a non-increasing number of guards.
Thus, no day is a good day to rob the bank, so return an empty list.


Constraints:

1 <= security.length <= 10^5
0 <= security[i], time <= 10^5

"""

# V0
# IDEA : TWO PREFIX ARRAYS — LENGTH OF THE NON-INCREASING RUN ENDING HERE
#                            AND THE NON-DECREASING RUN STARTING HERE
#
#   dec[i] = how many consecutive days up to i are non-increasing
#            dec[i] = dec[i-1] + 1 if security[i] <= security[i-1] else 0
#   inc[i] = how many consecutive days from i onward are non-decreasing
#            inc[i] = inc[i+1] + 1 if security[i] <= security[i+1] else 0
#
#   day i is good iff  dec[i] >= time and inc[i] >= time.
#
#   NOTE : time == 0 makes every day good, which falls out of the same test.
#
# time = O(n), space = O(n)
class Solution(object):
    def goodDaysToRobBank(self, security, time):
        n = len(security)
        dec = [0] * n
        inc = [0] * n
        for i in range(1, n):
            if security[i] <= security[i - 1]:
                dec[i] = dec[i - 1] + 1
        for i in range(n - 2, -1, -1):
            if security[i] <= security[i + 1]:
                inc[i] = inc[i + 1] + 1
        return [i for i in range(n) if dec[i] >= time and inc[i] >= time]


# V0-1
# IDEA : PREFIX COUNTS OF MONOTONICITY VIOLATIONS + RANGE QUERIES
#
#   up[i]   = how many k <= i have security[k] > security[k-1]  (a rise)
#   down[i] = how many k <= i have security[k] < security[k-1]  (a fall)
#
#   the block [i - time, i] is non-increasing  <=>  it contains no rise
#       <=>  up[i] - up[i - time] == 0
#   the block [i, i + time] is non-decreasing  <=>  it contains no fall
#       <=>  down[i + time] - down[i] == 0
#
#   so each day becomes two O(1) prefix-sum range queries; no run lengths are
#   tracked at all.
#
#   NOTE : time == 0 makes both queries compare a cell with itself, so every
#          day passes, as required.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def goodDaysToRobBank(self, security, time):
        n = len(security)
        up = [0] * n
        down = [0] * n
        for i in range(1, n):
            up[i] = up[i - 1] + (1 if security[i] > security[i - 1] else 0)
            down[i] = down[i - 1] + (1 if security[i] < security[i - 1] else 0)
        res = []
        for i in range(time, n - time):
            if up[i] - up[i - time] == 0 and down[i + time] - down[i] == 0:
                res.append(i)
        return res


# V0-2
# IDEA : BRUTE FORCE - VERIFY THE 2 * time NEIGHBOURS OF EVERY CANDIDATE DAY
#
#   straight from the definition: for every day that has room on both sides,
#   walk the "time" steps before it demanding non-increasing values and the
#   "time" steps after it demanding non-decreasing values, bailing out at the
#   first violation.
#
#   NOTE : O(n * time) - the definition-level reference, too slow for the
#          n = time = 10^5 upper bound but useful to check the fast versions
#          against.
#
# time = O(n * time)
# space = O(1) besides the output
class Solution(object):
    def goodDaysToRobBank(self, security, time):
        n = len(security)
        res = []
        for i in range(time, n - time):
            ok = True
            for k in range(i - time, i):
                if security[k] < security[k + 1]:
                    ok = False
                    break
            if ok:
                for k in range(i, i + time):
                    if security[k] > security[k + 1]:
                        ok = False
                        break
            if ok:
                res.append(i)
        return res

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
# IDEA : TWO PREFIX RUNS — HOW FAR THE NON-INCREASING / NON-DECREASING STREAK REACHES
#
#   non_inc[i] = length of the non-increasing run ENDING at i
#   non_dec[i] = length of the non-decreasing run STARTING at i
#
#   both are one-liners on a single pass :
#       non_inc[i] = non_inc[i-1] + 1  when security[i-1] >= security[i]
#       non_dec[i] = non_dec[i+1] + 1  when security[i] <= security[i+1]
#
#   day i is good iff both runs are long enough — non_inc[i] >= time and
#   non_dec[i] >= time. that also covers the "at least time days on each
#   side" requirement, since a run of length time cannot leave the array.
#
#   NOTE : time == 0 makes every day good, and the runs (>= 1 everywhere)
#          already say so.
#
# time = O(n), space = O(n)
class Solution(object):
    def goodDaysToRobBank(self, security, time):
        n = len(security)
        non_inc = [0] * n
        non_dec = [0] * n
        for i in range(1, n):
            if security[i - 1] >= security[i]:
                non_inc[i] = non_inc[i - 1] + 1
        for i in range(n - 2, -1, -1):
            if security[i] <= security[i + 1]:
                non_dec[i] = non_dec[i + 1] + 1
        return [i for i in range(n) if non_inc[i] >= time and non_dec[i] >= time]

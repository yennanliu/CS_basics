"""

1997. First Day Where You Have Been in All the Rooms
Medium

There are n rooms you need to visit, labeled from 0 to n - 1. Each day is labeled, starting from 0. You will go in and visit one room a day.

Initially on day 0, you visit room 0. The order you visit the rooms for the coming days is determined by the following rules and a given 0-indexed array nextVisit of length n:

Assuming that on a day, you visit room i,
if you have been in room i an odd number of times (including the current visit), on the next day you will visit a room with a lower or equal room number specified by nextVisit[i] where 0 <= nextVisit[i] <= i;
if you have been in room i an even number of times (including the current visit), on the next day you will visit room (i + 1) mod n.

Return the label of the first day where you have been in all the rooms. It can be shown that such a day exists. Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: nextVisit = [0,0]
Output: 2
Explanation:
- On day 0, you visit room 0. The total times you have been in room 0 is 1, which is odd.
  On the next day you will visit room nextVisit[0] = 0
- On day 1, you visit room 0, The total times you have been in room 0 is 2, which is even.
  On the next day you will visit room (0 + 1) mod 2 = 1
- On day 2, you visit room 1. This is the first day where you have been in all the rooms.

Example 2:

Input: nextVisit = [0,0,2]
Output: 6
Explanation:
Your room visiting order for each day is: [0,0,1,0,0,1,2,...].
Day 6 is the first day where you have been in all the rooms.

Example 3:

Input: nextVisit = [0,1,2,0]
Output: 6
Explanation:
Your room visiting order for each day is: [0,0,1,1,2,2,3,...].
Day 6 is the first day where you have been in all the rooms.


Constraints:

n == nextVisit.length
2 <= n <= 10^5
0 <= nextVisit[i] <= i

"""

# V0
# IDEA : DP ON "FIRST ARRIVAL DAY" (prefix invariant)
#
#   f[i] = the day we set foot in room i for the FIRST time. answer = f[n-1].
#
#   key invariant : whenever we first reach room i, every room < i has been
#   visited an EVEN number of times, so the walk from any room j back up to i
#   costs the same as it did the first time.
#
#   from f[i-1] (odd visit count at i-1) we bounce to room nextVisit[i-1],
#   which costs 1 day. re-climbing from nextVisit[i-1] back to i-1 costs
#   f[i-1] - f[nextVisit[i-1]] days, and one more day steps to i :
#
#       f[i] = 2 * f[i-1] - f[nextVisit[i-1]] + 2
#
#   NOTE : reduce mod 1e9+7 and add MOD before the mod so the subtraction
#          cannot make the stored value negative (matters in languages
#          without python's floor-mod, and keeps values canonical here).
#
# time = O(n), space = O(n)
class Solution(object):
    def firstDayBeenInAllRooms(self, nextVisit):
        MOD = 10 ** 9 + 7
        n = len(nextVisit)
        f = [0] * n
        for i in range(1, n):
            f[i] = (2 * f[i - 1] - f[nextVisit[i - 1]] + 2 + MOD) % MOD
        return f[n - 1]

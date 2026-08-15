"""

3376. Minimum Time to Break Locks I
Medium

Bob is stuck in a dungeon and must break n locks, each requiring some amount of energy to break. The required energy for each lock is stored in an array called strength where strength[i] indicates the energy needed to break the ith lock.

To break a lock, Bob uses a sword with the following characteristics:

The initial energy of the sword is 0.
The initial factor X by which the energy of the sword increases is 1.
Every minute, the energy of the sword increases by the current factor X.
To break the ith lock, the energy of the sword must reach at least strength[i].
After breaking a lock, the energy of the sword resets to 0, and the factor X increases by a given value K.

Your task is to determine the minimum time in minutes required for Bob to break all n locks and escape the dungeon.

Return the minimum time required for Bob to break all n locks.


Example 1:

Input: strength = [3,4,1], K = 1
Output: 4
Explanation:
Time  Energy  X  Action            Updated X
0     0       1  Nothing           1
1     1       1  Break 3rd Lock    2
2     2       2  Nothing           2
3     4       2  Break 2nd Lock    3
4     3       3  Break 1st Lock    3

Example 2:

Input: strength = [2,5,4], K = 2
Output: 5
Explanation:
Time  Energy  X  Action            Updated X
0     0       1  Nothing           1
1     1       1  Nothing           1
2     2       1  Break 1st Lock    3
3     3       3  Nothing           3
4     6       3  Break 2nd Lock    5
5     5       5  Break 3rd Lock    7


Constraints:

n == strength.length
1 <= n <= 8
1 <= K <= 10
1 <= strength[i] <= 1e6

"""

# V0
# IDEA : n <= 8, SO TRY EVERY ORDER — THE COST OF AN ORDER IS FORCED
#
#   the only decision is the SEQUENCE of locks. once that is fixed, breaking
#   the t-th lock takes ceil(strength / X) minutes with X = 1 + t*K, since
#   waiting longer than necessary never helps and the factor only changes on
#   a break.
#
#   8! = 40320 orders, each scored in 8 steps.
#
# time = O(n! * n), space = O(n)
import itertools


class Solution(object):
    def findMinimumTime(self, strength, K):
        n = len(strength)
        best = float('inf')
        for order in itertools.permutations(strength):
            total = 0
            x = 1
            for s in order:
                total += -(-s // x)             # ceil(s / x)
                x += K
            if total < best:
                best = total
        return best

"""

2511. Maximum Enemy Forts That Can Be Captured
Easy

You are given a 0-indexed integer array forts of length n representing the positions of several forts. forts[i] can be -1, 0, or 1 where:

-1 represents there is no fort at the ith position.
0 indicates there is an enemy fort at the ith position.
1 indicates the fort at the ith the position is under your command.

Now you have decided to move your army from one of your forts at position i to an empty position j such that:

0 <= i, j <= n - 1
The army travels over enemy forts only. Formally, for all k where min(i,j) < k < max(i,j), forts[k] == 0.

While moving the army, all the enemy forts that come in the way are captured.

Return the maximum number of enemy forts that can be captured. In case it is impossible to move your army, or you do not have any fort under your command, return 0.


Example 1:

Input: forts = [1,0,0,-1,0,0,0,0,1]
Output: 4
Explanation:
- Moving the army from position 0 to position 3 captures 2 enemy forts, at 1 and 2.
- Moving the army from position 8 to position 3 captures 4 enemy forts.
Since 4 is the maximum number of enemy forts that can be captured, we return 4.

Example 2:

Input: forts = [0,0,1,-1]
Output: 0
Explanation: Since no enemy fort can be captured, 0 is returned.


Constraints:

1 <= forts.length <= 1000
-1 <= forts[i] <= 1

"""

# V0
# IDEA : TWO POINTERS (scan between consecutive non-zero forts)
#
#   the army can only travel across a maximal run of 0's, and that run must be
#   bounded by a 1 on one side and a -1 on the other (order does not matter).
#
#   so : walk a pointer i over the non-zero entries, push j forward through the
#   zeros, and whenever forts[i] + forts[j] == 0 (i.e. one is 1 and the other
#   is -1) the run between them has j - i - 1 capturable enemy forts.
#
#   NOTE : forts[i] + forts[j] == 0 is only a valid test because both are
#          non-zero here — two 0's would also sum to 0 but j stops at the first
#          non-zero and i is skipped when forts[i] == 0.
#
# time = O(n), space = O(1)
class Solution(object):
    def captureForts(self, forts):
        n = len(forts)
        res = 0
        i = 0
        while i < n:
            j = i + 1
            if forts[i] != 0:
                while j < n and forts[j] == 0:
                    j += 1
                if j < n and forts[i] + forts[j] == 0:
                    res = max(res, j - i - 1)
            i = j
        return res

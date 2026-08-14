"""

1817. Finding the Users Active Minutes
Medium

You are given the logs for users' actions on LeetCode, and an integer k. The logs are represented by a 2D integer array logs where each logs[i] = [IDi, timei] indicates that the user with IDi performed an action at the minute timei.

Multiple users can perform actions simultaneously, and a single user can perform multiple actions in the same minute.

The user active minutes (UAM) for a given user is defined as the number of unique minutes in which the user performed an action on LeetCode. A minute can only be counted once, even if multiple actions occur during it.

You are to calculate a 1-indexed array answer of size k such that, for each j (1 <= j <= k), answer[j] is the number of users whose UAM equals j.

Return the array answer as described above.


Example 1:

Input: logs = [[0,5],[1,2],[0,2],[0,5],[1,3]], k = 5
Output: [0,2,0,0,0]
Explanation:
The user with ID=0 performed actions at minutes 5, 2, and 5 again. Hence, they have a UAM of 2 (minute 5 is only counted once).
The user with ID=1 performed actions at minutes 2 and 3. Hence, they have a UAM of 2.
Since both users have a UAM of 2, answer[2] is 2, and the remaining answer[j] values are 0.

Example 2:

Input: logs = [[1,1],[2,2],[2,3]], k = 4
Output: [1,1,0,0]
Explanation:
The user with ID=1 performed a single action at minute 1. Hence, they have a UAM of 1.
The user with ID=2 performed actions at minutes 2 and 3. Hence, they have a UAM of 2.
There is one user with a UAM of 1 and one with a UAM of 2.
Hence, answer[1] = 1, answer[2] = 1, and the remaining values are 0.


Constraints:

1 <= logs.length <= 10^4
0 <= IDi <= 10^9
1 <= timei <= 10^5
k is in the range [The maximum UAM for a user, 10^5].

"""

# V0
# IDEA : HASH MAP OF SETS (id -> set of distinct minutes), then bucket the sizes
#
#   step 1 : group the log entries by user id into a SET of minutes -- the set
#            does the de-duplication that the "unique minutes" rule asks for.
#   step 2 : each user's UAM is len(that set); bump answer[UAM - 1].
#
#   NOTE : the output is described as 1-indexed but returned as a plain list,
#          so answer[j] lives at index j - 1.
#   NOTE : k >= max UAM is guaranteed, so no bounds check is needed.
#
# time = O(n + k), space = O(n)
from collections import defaultdict
class Solution(object):
    def findingUsersActiveMinutes(self, logs, k):
        seen = defaultdict(set)
        for uid, t in logs:
            seen[uid].add(t)

        res = [0] * k
        for minutes in seen.values():
            res[len(minutes) - 1] += 1
        return res

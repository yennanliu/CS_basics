"""

2244. Minimum Rounds to Complete All Tasks
Medium

You are given a 0-indexed integer array tasks, where tasks[i] represents the difficulty level of a task. In each round, you can complete either 2 or 3 tasks of the same difficulty level.

Return the minimum rounds required to complete all the tasks, or -1 if it is not possible to complete all the tasks.


Example 1:

Input: tasks = [2,2,3,3,2,4,4,4,4,4]
Output: 4
Explanation: To complete all the tasks, a possible plan is:
- In the first round, you complete 3 tasks of difficulty level 2.
- In the second round, you complete 2 tasks of difficulty level 3.
- In the third round, you complete 3 tasks of difficulty level 4.
- In the fourth round, you complete 2 tasks of difficulty level 4.
It can be shown that all the tasks cannot be completed in fewer than 4 rounds, so the answer is 4.

Example 2:

Input: tasks = [2,3,3]
Output: -1
Explanation: There is only 1 task of difficulty level 2, but in each round, you can only complete either 2 or 3 tasks of the same difficulty level. Hence, you cannot complete all the tasks, and the answer is -1.


Constraints:

1 <= tasks.length <= 10^5
1 <= tasks[i] <= 10^9

"""

# V0
# IDEA : PER DIFFICULTY, GREEDILY USE AS MANY 3s AS POSSIBLE
#
#   difficulty levels are independent, so handle each count c on its own :
#     c == 1        -> impossible, no round can consume a single task -> -1
#     otherwise     -> ceil(c / 3) rounds
#
#   why ceil(c / 3) is always achievable : c % 3 == 0 uses all triples;
#   c % 3 == 1 swaps one triple for two pairs (needs c >= 4, guaranteed since
#   c != 1); c % 3 == 2 adds one pair. in every case the count matches
#   ceil(c / 3), written here as (c + 2) // 3.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def minimumRounds(self, tasks):
        res = 0
        for c in Counter(tasks).values():
            if c == 1:
                return -1
            res += (c + 2) // 3
        return res

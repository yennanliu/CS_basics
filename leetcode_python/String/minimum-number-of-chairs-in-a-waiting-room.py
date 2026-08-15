"""

3168. Minimum Number of Chairs in a Waiting Room
Easy

You are given a string s. Simulate events at each second i:

If s[i] == 'E', a person enters the waiting room and takes one of the chairs in it.
If s[i] == 'L', a person leaves the waiting room, freeing up a chair.

Return the minimum number of chairs needed so that a chair is available for every person who enters the waiting room given that it is initially empty.


Example 1:

Input: s = "EEEEEEE"
Output: 7
Explanation:
After each second, a person enters the waiting room and no person leaves it. Therefore, a minimum of 7 chairs is needed.

Example 2:

Input: s = "ELELEEL"
Output: 2
Explanation:
Let's consider that there are 2 chairs in the waiting room. The table below shows the state of the waiting room at each second.

Example 3:

Input: s = "ELEELEELLL"
Output: 3
Explanation:
Let's consider that there are 3 chairs in the waiting room. The table below shows the state of the waiting room at each second.


Constraints:

1 <= s.length <= 50
s consists only of the letters 'E' and 'L'.

"""

# V0
# IDEA : THE ANSWER IS THE PEAK OCCUPANCY
#
#   'E' adds one occupant and 'L' removes one, so the running count IS the
#   number of chairs in use at that second. enough chairs must exist for the
#   busiest moment, so the answer is the maximum the counter ever reaches.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumChairs(self, s):
        cur = 0
        best = 0
        for ch in s:
            cur += 1 if ch == 'E' else -1
            best = max(best, cur)
        return best

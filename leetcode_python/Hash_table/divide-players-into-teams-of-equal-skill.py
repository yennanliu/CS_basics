"""

2491. Divide Players Into Teams of Equal Skill
Medium

You are given a positive integer array skill of even length n where skill[i] denotes the skill of the ith player. Divide the players into n / 2 teams of size 2 such that the total skill of each team is equal.

The chemistry of a team is equal to the product of the skills of the players on that team.

Return the sum of the chemistry of all the teams, or return -1 if there is no way to divide the players into teams such that the total skill of each team is equal.


Example 1:

Input: skill = [3,2,5,1,3,4]
Output: 22
Explanation:
Divide the players into the following teams: (1, 5), (2, 4), (3, 3), where each team has a total skill of 6.
The sum of the chemistry of all the teams is: 1 * 5 + 2 * 4 + 3 * 3 = 5 + 8 + 9 = 22.

Example 2:

Input: skill = [3,4]
Output: 12
Explanation:
The two players form a team with a total skill of 7.
The sum of the chemistry of all the teams is: 3 * 4 = 12.

Example 3:

Input: skill = [1,1,2,3]
Output: -1
Explanation:
There is no way to divide the players into teams such that the total skill of each team is equal.


Constraints:

2 <= skill.length <= 10^5
skill.length is even.
1 <= skill[i] <= 1000

"""

# V0
# IDEA : SORT AND PAIR THE ENDS — THE TARGET SUM IS FORCED
#
#   if every team sums to the same value t, then n/2 teams cover the whole
#   array, so  t = 2 * sum(skill) / n. that is fixed before any choices.
#
#   after sorting, the smallest player MUST partner the largest (nobody else
#   can lift the smallest to t), and peeling that pair leaves the same
#   problem — so the outside-in pairing is forced, not merely convenient.
#
#   if any pair misses t, no valid division exists.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def dividePlayers(self, skill):
        a = sorted(skill)
        n = len(a)
        target = a[0] + a[-1]

        res = 0
        for i in range(n // 2):
            lo, hi = a[i], a[n - 1 - i]
            if lo + hi != target:
                return -1
            res += lo * hi
        return res

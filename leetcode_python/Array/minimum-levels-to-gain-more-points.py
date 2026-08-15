"""

3096. Minimum Levels to Gain More Points
Medium

You are given a binary array possible of length n.

Alice and Bob are playing a game that consists of n levels. Some of the levels in the game are impossible to clear while others can always be cleared. In particular, if possible[i] == 0, then the ith level is impossible to clear for both the players. A player gains 1 point on clearing a level and loses 1 point if the player fails to clear it.

At the start of the game, Alice will play some levels in the given order starting from the 0th level, after which Bob will play for the rest of the levels.

Alice wants to know the minimum number of levels she should play to gain more points than Bob, if both players play optimally to maximize their points.

Return the minimum number of levels Alice should play to gain more points. If this is not possible, return -1.

Note that each player must play at least 1 level.


Example 1:

Input: possible = [1,0,1,0]
Output: 1
Explanation:
Let's look at all the levels that Alice can play up to:
If Alice plays only level 0 and Bob plays the rest of the levels, Alice has 1 point, while Bob has -1 + 1 - 1 = -1 point.
If Alice plays till level 1 and Bob plays the rest of the levels, Alice has 1 - 1 = 0 points, while Bob has 1 - 1 = 0 points.
If Alice plays till level 2 and Bob plays the rest of the levels, Alice has 1 - 1 + 1 = 1 point, while Bob has -1 point.
Alice must play a minimum of 1 level to gain more points.

Example 2:

Input: possible = [1,1,1,1,1]
Output: 3
Explanation:
Let's look at all the levels that Alice can play up to:
If Alice plays only level 0 and Bob plays the rest of the levels, Alice has 1 point, while Bob has 4 points.
If Alice plays till level 1 and Bob plays the rest of the levels, Alice has 2 points, while Bob has 3 points.
If Alice plays till level 2 and Bob plays the rest of the levels, Alice has 3 points, while Bob has 2 points.
If Alice plays till level 3 and Bob plays the rest of the levels, Alice has 4 points, while Bob has 1 point.
Alice must play a minimum of 3 levels to gain more points.

Example 3:

Input: possible = [0,0]
Output: -1
Explanation:
The only possible way is for both players to play 1 level each. Alice plays level 0 and loses 1 point. Bob plays level 1 and loses 1 point. As both players have equal points, Alice can't gain more points than Bob.


Constraints:

2 <= n == possible.length <= 10^5
possible[i] is either 0 or 1.

"""

# V0
# IDEA : SCORES ARE PREFIX SUMS OF +1 / -1 — SCAN FOR THE FIRST SPLIT THAT WINS
#
#   "playing optimally" is a red herring : a clearable level always scores
#   +1 and an impossible one always -1, with no choice involved. so map the
#   array to +-1 and let `total` be its whole sum.
#
#   if Alice stops after i levels her score is prefix[i] and Bob's is
#   total - prefix[i], so she wins exactly when
#
#       prefix[i] > total - prefix[i]     <=>     2 * prefix[i] > total
#
#   scan i = 1 .. n-1 (both players must play at least one level) and return
#   the first i that satisfies it.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumLevels(self, possible):
        n = len(possible)
        total = sum(1 if v == 1 else -1 for v in possible)
        prefix = 0
        for i in range(n - 1):                 # Alice plays levels 0..i
            prefix += 1 if possible[i] == 1 else -1
            if 2 * prefix > total:
                return i + 1
        return -1

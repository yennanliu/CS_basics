"""

2410. Maximum Matching of Players With Trainers
Medium

You are given a 0-indexed integer array players, where players[i] represents the ability of the ith player. You are also given a 0-indexed integer array trainers, where trainers[j] represents the training capacity of the jth trainer.

The ith player can match with the jth trainer if the player's ability is less than or equal to the trainer's training capacity. Additionally, the ith player can be matched with at most one trainer, and the jth trainer can be matched with at most one player.

Return the maximum number of matchings between players and trainers that satisfy these conditions.


Example 1:

Input: players = [4,7,9], trainers = [8,2,5,8]
Output: 2
Explanation:
One of the ways we can form two matchings is as follows:
- players[0] can be matched with trainers[0] since 4 <= 8.
- players[1] can be matched with trainers[3] since 7 <= 8.
It can be proven that 2 is the maximum number of matchings that can be formed.

Example 2:

Input: players = [1,1,1], trainers = [10]
Output: 1
Explanation:
The trainer can be matched with any of the 3 players.
Each player can only be matched with one trainer, so the maximum answer is 1.


Constraints:

1 <= players.length, trainers.length <= 10^5
1 <= players[i], trainers[j] <= 10^9

"""

# V0
# IDEA : SORT BOTH, THEN A GREEDY TWO-POINTER SWEEP
#
#   walk the players from weakest to strongest and hand each one the WEAKEST
#   trainer that can still take them. spending a stronger trainer on a weaker
#   player is never better (exchange argument), so this maximises the count.
#
#   the trainer pointer only moves forward : trainers too weak for the
#   current player are too weak for every later one as well, so they are
#   discarded permanently.
#
# time = O(n log n + m log m), space = O(n + m)
class Solution(object):
    def matchPlayersAndTrainers(self, players, trainers):
        players = sorted(players)
        trainers = sorted(trainers)

        res = 0
        j = 0
        for ability in players:
            while j < len(trainers) and trainers[j] < ability:
                j += 1                     # too weak for this and all later players
            if j == len(trainers):
                break
            res += 1
            j += 1
        return res

"""

3175. Find The First Player To Win K Games in a Row
Medium

A competition consists of n players numbered from 0 to n - 1.

You are given an integer array skills of size n and a positive integer k, where skills[i] is the skill level of player i. All integers in skills are unique.

All players are standing in a queue in order from player 0 to player n - 1.

The competition process is as follows:

The first two players in the queue play a game, and the player with the higher skill level wins.
After the game, the winner stays at the beginning of the queue, and the loser goes to the end of it.

The winner of the competition is the first player who wins k games in a row.

Return the initial index of the winning player.


Example 1:

Input: skills = [4,2,6,3,9], k = 2
Output: 2
Explanation:
Initially, the queue of players is [0,1,2,3,4]. The following process happens:
Players 0 and 1 play a game, since the skill of player 0 is higher than that of player 1, player 0 wins. The resulting queue is [0,2,3,4,1].
Players 0 and 2 play a game, since the skill of player 2 is higher than that of player 0, player 2 wins. The resulting queue is [2,3,4,1,0].
Players 2 and 3 play a game, since the skill of player 2 is higher than that of player 3, player 2 wins. The resulting queue is [2,4,1,0,3].
Player 2 won k = 2 games in a row, so the winner is player 2.

Example 2:

Input: skills = [2,5,4], k = 3
Output: 1
Explanation:
Initially, the queue of players is [0,1,2]. The following process happens:
Players 0 and 1 play a game, since the skill of player 1 is higher than that of player 0, player 1 wins. The resulting queue is [1,2,0].
Players 1 and 2 play a game, since the skill of player 1 is higher than that of player 2, player 1 wins. The resulting queue is [1,0,2].
Players 1 and 0 play a game, since the skill of player 1 is higher than that of player 0, player 1 wins. The resulting queue is [1,2,0].
Player 1 won k = 3 games in a row, so the winner is player 1.


Constraints:

n == skills.length
2 <= n <= 10^5
1 <= k <= 10^9
1 <= skills[i] <= 1e6
All integers in skills are unique.

"""

# V0
# IDEA : ONE SWEEP IS ENOUGH — AFTER IT, THE STRONGEST PLAYER NEVER LOSES
#
#   the queue never reorders the untouched tail, so a single pass over the
#   players plays out every "new challenger" game. track the current champion
#   and its streak; the first time the streak reaches k, that champion wins.
#
#   if nobody reaches k during that pass, the strongest player has by then
#   taken the front, and from there it beats everyone forever — so the answer
#   is simply the index of the maximum skill. this also covers k larger than
#   n, where no early winner is possible.
#
# time = O(n), space = O(1)
class Solution(object):
    def findWinningPlayer(self, skills, k):
        champ = 0
        streak = 0
        for i in range(1, len(skills)):
            if skills[i] > skills[champ]:
                champ = i
                streak = 1
            else:
                streak += 1
            if streak == k:
                return champ
        return champ

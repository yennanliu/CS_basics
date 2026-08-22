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


# V0-1
# IDEA : LITERAL QUEUE SIMULATION (WITH THE ONLY BOUND THAT MAKES IT FINITE)
#
#   play the games exactly as described with a deque : the two front players
#   meet, the winner stays at the front, the loser is pushed to the back.
#
#   the one thing that has to be reasoned about is termination : a streak of
#   n - 1 wins means having beaten EVERY other player, i.e. being the strongest
#   player, and the strongest player then wins forever. so any k >= n can be
#   answered straight away with argmax(skills), and for k < n the simulation
#   provably ends within O(n) games.
#
# time = O(n)
# space = O(n)
from collections import deque
class Solution(object):
    def findWinningPlayer(self, skills, k):
        n = len(skills)
        if k >= n:
            return skills.index(max(skills))

        q = deque(range(n))
        champ = q.popleft()
        streak = 0
        while True:
            other = q.popleft()
            if skills[champ] > skills[other]:
                q.append(other)
                streak += 1
            else:
                q.append(champ)
                champ = other
                streak = 1
            if streak == k:
                return champ


# V0-2
# IDEA : NEXT GREATER ELEMENT (MONOTONIC STACK) OVER THE PREFIX MAXIMA
#
#   turn the process into a closed formula instead of stepping through it.
#
#   1) only a PREFIX MAXIMUM ever reaches the front : player i takes over
#      exactly when skills[i] beats everything before it.
#   2) once at the front, player i keeps winning against i+1, i+2, ... until
#      the first j > i with skills[j] > skills[i]. so with nge[i] = that j
#      (or n when there is none), its streak is nge[i] - i wins — one less for
#      player 0, which starts at the front without having won anything.
#   3) champions appear in increasing index order, so the first prefix maximum
#      whose streak reaches k is the answer; a prefix maximum with nge == n is
#      the global maximum and wins arbitrarily many games, so it always ends
#      the competition.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def findWinningPlayer(self, skills, k):
        n = len(skills)
        nge = [n] * n
        stack = []
        for i in range(n):
            while stack and skills[stack[-1]] < skills[i]:
                nge[stack.pop()] = i
            stack.append(i)

        mx = -1
        for i in range(n):
            if skills[i] > mx:
                if nge[i] == n:
                    return i
                wins = nge[i] - i - (1 if i == 0 else 0)
                if wins >= k:
                    return i
                mx = skills[i]
        return n - 1

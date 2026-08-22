"""

1535. Find the Winner of an Array Game
Medium

Given an integer array arr of distinct integers and an integer k.

A game will be played between the first two elements of the array (i.e. arr[0] and arr[1]). In each round of the game, we compare arr[0] with arr[1], the larger integer wins and remains at position 0, and the smaller integer moves to the end of the array. The game ends when an integer wins k consecutive rounds.

Return the integer which will win the game.

It is guaranteed that there will be a winner of the game.


Example 1:

Input: arr = [2,1,3,5,4,6,7], k = 2
Output: 5
Explanation: Let's see the rounds of the game:
Round |       arr       | winner | win_count
  1   | [2,1,3,5,4,6,7] | 2      | 1
  2   | [2,3,5,4,6,7,1] | 3      | 1
  3   | [3,5,4,6,7,1,2] | 5      | 1
  4   | [5,4,6,7,1,2,3] | 5      | 2
So we can see that 4 rounds will be played and 5 is the winner because it wins 2 consecutive games.

Example 2:

Input: arr = [3,2,1], k = 10
Output: 3
Explanation: 3 will win the first 10 rounds consecutively.


Constraints:

2 <= arr.length <= 10^5
1 <= arr[i] <= 10^6
arr contains distinct integers.
1 <= k <= 10^9

"""

# V0
# IDEA : ONE LINEAR SWEEP (no queue simulation needed)
#
#   losers go to the BACK of the array, so during the first pass the
#   current champion always faces the next un-played element. that makes
#   the whole first pass equivalent to :
#
#     champ = arr[0] ; for x in arr[1:] : champ = max(champ, x)
#
#   keeping a streak counter (reset to 1 whenever the champion changes).
#
#   NOTE : k can be 10^9 but that never matters — if nobody reaches k
#          within the single sweep, the array maximum has beaten everyone
#          and will then win forever, so it is the answer.
#
# time = O(n), space = O(1)
class Solution(object):
    def getWinner(self, arr, k):
        champ = arr[0]
        streak = 0
        for i in range(1, len(arr)):
            if arr[i] > champ:
                champ = arr[i]
                streak = 1
            else:
                streak += 1
            if streak == k:
                break
        return champ


# V0-1
# IDEA : LITERAL SIMULATION WITH A DEQUE
#
#   play the game exactly as written : take the two front elements, the
#   loser is pushed to the back, the winner stays at the front. the only
#   extra ingredient needed to make it terminate is a stop condition --
#   once the champion is the array maximum nothing can ever beat it, so it
#   is the answer no matter how big k is (k can be 10^9).
#
# time = O(n)
# space = O(n)
from collections import deque
class Solution(object):
    def getWinner(self, arr, k):
        top = max(arr)
        dq = deque(arr)
        champ = dq.popleft()
        streak = 0
        while champ != top and streak < k:
            other = dq.popleft()
            if other > champ:
                dq.append(champ)
                champ, streak = other, 1
            else:
                dq.append(other)
                streak += 1
        return champ


# V0-2
# IDEA : TWO PASSES -- PREFIX MAXIMA, THEN A CLOSED-FORM TEST PER CANDIDATE
#
#   arr[i] can only ever hold the crown if it beats everybody before it,
#   i.e. arr[i] == max(arr[:i + 1]) (a prefix record). such a record wins
#   round i and then needs k - 1 more wins, against arr[i + 1 .. i + k - 1];
#   since it already dominates the whole prefix, "it beats them all"
#   collapses to arr[i] == max(arr[:i + k]) -- a single lookup in the
#   prefix-maximum table, no streak counter at all.
#   arr[0] is the exception : it has not won a round when the game starts,
#   so it must survive k opponents, arr[0] == max(arr[:k + 1]).
#   the global maximum always passes the test, so the scan always returns.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def getWinner(self, arr, k):
        n = len(arr)
        pmax = [0] * n
        run = arr[0]
        for i in range(n):
            run = max(run, arr[i])
            pmax[i] = run
        if pmax[min(n - 1, k)] == arr[0]:
            return arr[0]
        for i in range(1, n):
            if arr[i] == pmax[i] and arr[i] == pmax[min(n - 1, i + k - 1)]:
                return arr[i]
        return pmax[n - 1]

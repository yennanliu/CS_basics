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

"""

2029. Stone Game IX
Medium

Alice and Bob continue their games with stones. There is a row of n stones, and each stone has an associated value. You are given an integer array stones, where stones[i] is the value of the ith stone.

Alice and Bob take turns, with Alice starting first. On each turn, the player may remove any stone from stones. The player who removes a stone loses if the sum of the values of all removed stones is divisible by 3. Bob will win automatically if there are no remaining stones (even if it is Alice's turn).

Assuming both players play optimally, return true if Alice wins and false otherwise.


Example 1:

Input: stones = [2,1]
Output: true
Explanation: The game will be played as follows:
- Turn 1: Alice can remove either stone.
- Turn 2: Bob removes the remaining stone.
The sum of the removed stones is 1 + 2 = 3 and is divisible by 3. Therefore, Bob loses and Alice wins the game.

Example 2:

Input: stones = [2]
Output: false
Explanation: Alice will remove the only stone, and the sum of the values on the removed stones is 2.
Therefore, Alice loses the game.

Example 3:

Input: stones = [5,1,2,4,3]
Output: false
Explanation: Bob will always win. One possible way for Bob to win is shown below:
- Turn 1: Alice can remove the second stone with value 1. Sum of removed stones = 1.
- Turn 2: Bob removes the fifth stone with value 3. Sum of removed stones = 1 + 3 = 4.
- Turn 3: Alices removes the fourth stone with value 4. Sum of removed stones = 1 + 3 + 4 = 8.
- Turn 4: Bob removes the third stone with value 2. Sum of removed stones = 1 + 3 + 4 + 2 = 10.
- Turn 5: Alice removes the first stone with value 5. Sum of removed stones = 1 + 3 + 4 + 2 + 5 = 15.
Alice loses the game because the sum of the removed stones (15) is divisible by 3. Bob wins the game.


Constraints:

1 <= stones.length <= 10^5
1 <= stones[i] <= 10^4

"""

# V0
# IDEA : MATH ON REMAINDERS mod 3  (only the counts c0, c1, c2 matter)
#
#   a stone with value % 3 == 0 never changes the running remainder — it is
#   a pure "pass". so c0 only matters by its PARITY :
#
#   * c0 is EVEN : the passes cancel out. the forced play is to alternate
#     1,1,2,1,2,1,2,... or 2,2,1,2,1,2,1,... — Alice wins iff she has at
#     least one of each remainder type, i.e. c1 >= 1 and c2 >= 1.
#
#   * c0 is ODD : the extra pass flips who is on the move. now Alice wins
#     iff one pile exceeds the other by more than 2, i.e.
#         abs(c1 - c2) > 2
#
#   NOTE : taking a stone whose remainder makes the running sum divisible by
#          3 loses immediately, which is what pins the play into the forced
#          alternation above.
#
# time = O(n), space = O(1)
class Solution(object):
    def stoneGameIX(self, stones):
        cnt = [0, 0, 0]
        for x in stones:
            cnt[x % 3] += 1
        if cnt[0] % 2 == 0:
            return cnt[1] >= 1 and cnt[2] >= 1
        return abs(cnt[1] - cnt[2]) > 2

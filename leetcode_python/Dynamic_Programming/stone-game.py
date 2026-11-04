"""

877. Stone Game
Medium

Alice and Bob play a game with piles of stones. There are an even number of
piles arranged in a row, and each pile has a positive integer number of stones
piles[i].

The objective of the game is to end with the most stones. The total number of
stones across all the piles is odd, so there are no ties.

Alice and Bob take turns, with Alice starting first. Each turn, a player takes
the entire pile of stones either from the beginning or from the end of the row.
This continues until there are no more piles left, at which point the person
with the most stones wins.

Assuming Alice and Bob play optimally, return true if Alice wins the game, or
false if Bob wins.

Example 1:

Input: piles = [5,3,4,5]

Output: true

Explanation:

Alice starts first, and can only take the first 5 or the last 5.
Say she takes the first 5, so that the row becomes [3, 4, 5].
If Bob takes 3, then the board is [4, 5], and Alice takes 5 to win with 10 points.
If Bob takes the last 5, then the board is [3, 4], and Alice takes 4 to win with 9 points.
This demonstrated that taking the first 5 was a winning move for Alice, so we return true.

Example 2:

Input: piles = [3,7,2,3]

Output: true

Constraints:

2 <= piles.length <= 500
piles.length is even.
1 <= piles[i] <= 500
sum(piles[i]) is odd.

"""

# V0
# IDEA : INTERVAL GAME DP (SCORE DIFFERENCE), BOTTOM-UP 2D
#
#   dp[i][j] = best (mover's score - opponent's score) the player TO MOVE can
#              force on piles[i..j]
#
#   The mover takes one end; then the opponent becomes the mover on what is
#   left, and their best difference counts AGAINST us -> subtract it:
#
#     dp[i][j] = max( piles[i] - dp[i+1][j],     # take the LEFT  pile
#                     piles[j] - dp[i][j-1] )    # take the RIGHT pile
#     dp[i][i] = piles[i]                        # one pile: take it
#
#   Alice moves first on the whole row -> she wins iff dp[0][n-1] > 0.
#
#   e.g. piles = [5,3,4,5] -> dp[0][3] = 1 > 0 -> True
#
#   NOTE : the same DP solves LC 486 (Predict the Winner), where the answer is
#          NOT always True - learn this one, not V2's shortcut.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def stoneGame(self, piles):
        """
        :type piles: List[int]
        :rtype: bool
        """
        n = len(piles)

        # dp[i][j] = best score difference for the player to move on piles[i..j]
        dp = [[0] * n for _ in range(n)]

        # base case: a single pile goes to whoever moves
        for i in range(n):
            dp[i][i] = piles[i]

        # NOTE !!! fill by interval LENGTH, so dp[i+1][j] and dp[i][j-1]
        #          (both one pile shorter) are ready before dp[i][j]
        for length in range(2, n + 1):
            for i in range(0, n - length + 1):
                j = i + length - 1
                take_left = piles[i] - dp[i + 1][j]
                take_right = piles[j] - dp[i][j - 1]
                dp[i][j] = max(take_left, take_right)

        return dp[0][n - 1] > 0


# V0-1
# IDEA : INTERVAL GAME DP, ROLLING 1D ARRAY
#
#   Same recurrence as V0. Iterate i from right to left; for a fixed i,
#   dp[j] still holds row i+1 (= dp[i+1][j]) until it is overwritten, and
#   dp[j-1] was just overwritten in this row (= dp[i][j-1]).
#
# time = O(n^2), space = O(n)
class Solution2(object):
    def stoneGame(self, piles):
        """
        :type piles: List[int]
        :rtype: bool
        """
        n = len(piles)

        # dp[j] = best score difference on piles[i..j] for the current i
        dp = [0] * n

        for i in range(n - 1, -1, -1):
            # base case: interval [i, i]
            dp[i] = piles[i]
            for j in range(i + 1, n):
                take_left = piles[i] - dp[j]        # dp[j] is still dp[i+1][j]
                take_right = piles[j] - dp[j - 1]   # dp[j-1] is already dp[i][j-1]
                dp[j] = max(take_left, take_right)

        return dp[n - 1] > 0


# V1
# IDEA : TOP-DOWN MINIMAX WITH MEMO (FIRST / SECOND PLAYER SCORES)
#
#   first(i, j)  = stones the player who picks FIRST on piles[i..j] collects
#   second(i, j) = stones the player who picks SECOND on piles[i..j] collects
#
#     first(i, j)  = max( piles[i] + second(i+1, j), piles[j] + second(i, j-1) )
#     second(i, j) = min( first(i+1, j), first(i, j-1) )   # the opponent picks
#                                                          # the end that is worst for us
#     first(i, i) = piles[i], second(i, i) = 0
#
#   Alice wins iff first(0, n-1) is more than half of all stones.
#
#   NOTE : recursion depth is n (<= 500) - fine in Python, but V0 avoids it.
#
# time = O(n^2), space = O(n^2)
class Solution3(object):
    def stoneGame(self, piles):
        """
        :type piles: List[int]
        :rtype: bool
        """
        self.piles = piles
        self.first_memo = {}
        self.second_memo = {}

        total = 0
        for stones in piles:
            total += stones

        alice = self.first(0, len(piles) - 1)
        # compare doubled values to avoid integer division
        return alice * 2 > total

    def first(self, i, j):
        """Stones collected by the player who picks first on piles[i..j]."""
        if i == j:
            return self.piles[i]
        if (i, j) in self.first_memo:
            return self.first_memo[(i, j)]

        take_left = self.piles[i] + self.second(i + 1, j)
        take_right = self.piles[j] + self.second(i, j - 1)
        best = max(take_left, take_right)

        self.first_memo[(i, j)] = best
        return best

    def second(self, i, j):
        """Stones collected by the player who picks second on piles[i..j]."""
        if i == j:
            return 0
        if (i, j) in self.second_memo:
            return self.second_memo[(i, j)]

        # the opponent moves first and leaves us the worse of the two intervals
        worst = min(self.first(i + 1, j), self.first(i, j - 1))

        self.second_memo[(i, j)] = worst
        return worst


# V2
# IDEA : MATH - ALICE ALWAYS WINS
#
#   With an EVEN number of piles, colour them by index parity. Alice can take
#   every even-index pile (or every odd-index one): if she takes an end of one
#   colour, both ends left for Bob are the other colour, so he is forced to
#   follow. She picks whichever colour has the larger sum; the total is odd,
#   so the two sums differ -> she always wins.
#
#   NOTE : correct only because of THIS problem's constraints (even count,
#          odd total). Say it in an interview after V0, not instead of it.
#
# time = O(1), space = O(1)
class Solution4(object):
    def stoneGame(self, piles):
        """
        :type piles: List[int]
        :rtype: bool
        """
        return True

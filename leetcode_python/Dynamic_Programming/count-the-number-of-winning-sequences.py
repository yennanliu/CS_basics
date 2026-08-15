"""

3320. Count The Number of Winning Sequences
Hard

Alice and Bob are playing a fantasy battle game consisting of n rounds where they summon one of three magical creatures each round: a Fire Dragon, a Water Serpent, or an Earth Golem. In each round, players simultaneously summon their creature and are awarded points as follows:

If one player summons a Fire Dragon and the other summons an Earth Golem, the player who summoned the Fire Dragon is awarded a point.
If one player summons a Water Serpent and the other summons a Fire Dragon, the player who summoned the Water Serpent is awarded a point.
If one player summons an Earth Golem and the other summons a Water Serpent, the player who summoned the Earth Golem is awarded a point.
If both players summon the same creature, no player is awarded a point.

You are given a string s consisting of n characters 'F', 'W', and 'E', representing the sequence of creatures Alice will summon in each round:

If s[i] == 'F', Alice summons a Fire Dragon.
If s[i] == 'W', Alice summons a Water Serpent.
If s[i] == 'E', Alice summons an Earth Golem.

Bob's sequence of moves is unknown, but it is guaranteed that Bob will never summon the same creature in two consecutive rounds. Bob beats Alice if the total number of points awarded to Bob after n rounds is strictly greater than the points awarded to Alice.

Return the number of distinct sequences Bob can use to beat Alice.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: s = "FFF"
Output: 3
Explanation:
Bob can beat Alice by making one of the following sequences of moves: "WFW", "FWF", or "WEW". Note that other winning sequences like "WWE" or "EWW" are invalid since Bob cannot make the same move twice in a row.

Example 2:

Input: s = "FWEFW"
Output: 18
Explanation:
Bob can beat Alice by making one of the following sequences of moves: "FWFWF", "FWFWE", "FWEFE", "FWEWE", "FEFWF", "FEFWE", "FEFEW", "FEWFE", "WFEFE", "WFEWE", "WEFWF", "WEFWE", "WEFEF", "WEFEW", "WEWFE", "WEWFW", "EWFWF", or "EWFWE".


Constraints:

1 <= s.length <= 1000
s[i] is one of 'F', 'W', or 'E'.

"""

# V0
# IDEA : DP OVER (ROUND, BOB'S LAST CREATURE, SCORE DIFFERENCE)
#
#   the only things carried between rounds are what Bob played last (he may
#   not repeat) and how far ahead he is. so
#
#       dp[c][d] = sequences where Bob last played c and leads by d
#
#   the score difference lives in [-n, n], stored with an offset.
#
#   the transition would naively loop over the 3x3 (previous, next) pairs,
#   but "next != previous" means the incoming mass for a choice c' is
#   simply (total over all c) - dp[c'], and the round's outcome only shifts
#   the difference by -1, 0 or +1 — an index offset on the row.
#
#   that turns each round into three subtractions plus three shifts over the
#   reachable window, which keeps 1000 rounds affordable.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def countWinningSequences(self, s):
        MOD = 10 ** 9 + 7
        n = len(s)
        creatures = "FWE"
        # beats[a][b] : +1 if a beats b, -1 if b beats a, else 0
        BEATS = {('F', 'E'): 1, ('W', 'F'): 1, ('E', 'W'): 1}

        def outcome(bob, alice):
            if bob == alice:
                return 0
            return 1 if BEATS.get((bob, alice)) else -1

        size = 2 * n + 1
        off = n
        dp = [[0] * size for _ in range(3)]     # by Bob's last creature

        # round 0
        for ci, c in enumerate(creatures):
            dp[ci][off + outcome(c, s[0])] += 1

        for i in range(1, n):
            total = [(dp[0][d] + dp[1][d] + dp[2][d]) % MOD for d in range(size)]
            nxt = [[0] * size for _ in range(3)]
            for ci, c in enumerate(creatures):
                delta = outcome(c, s[i])
                row = nxt[ci]
                prev = dp[ci]
                for d in range(size):
                    v = total[d] - prev[d]
                    if v:
                        nd = d + delta
                        if 0 <= nd < size:
                            row[nd] = (row[nd] + v) % MOD
            dp = nxt

        res = 0
        for ci in range(3):
            for d in range(off + 1, size):       # Bob strictly ahead
                res += dp[ci][d]
        return res % MOD

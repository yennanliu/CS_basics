"""

2836. Maximize Value of Function in a Ball Passing Game
Hard

You are given an integer array receiver of length n and an integer k. n players are playing a ball-passing game.

You choose the starting player, i. The game proceeds as follows: player i passes the ball to player receiver[i], who then passes it to receiver[receiver[i]], and so on, for k passes in total. The game's score is the sum of the indices of the players who touched the ball, including repetitions, i.e. i + receiver[i] + receiver[receiver[i]] + ... + receiver^(k)[i].

Return the maximum possible score.

Notes:

- receiver may contain duplicates.
- receiver[i] may be equal to i.


Example 1:

Input: receiver = [2,0,1], k = 4
Output: 6
Explanation: Starting with player i = 2 the initial score is 2:
Pass 1: player 2 passes to player 1, score 3
Pass 2: player 1 passes to player 0, score 3
Pass 3: player 0 passes to player 2, score 5
Pass 4: player 2 passes to player 1, score 6

Example 2:

Input: receiver = [1,1,1,2,3], k = 3
Output: 10
Explanation: Starting with player i = 4 the initial score is 4:
Pass 1: player 4 passes to player 3, score 7
Pass 2: player 3 passes to player 2, score 9
Pass 3: player 2 passes to player 1, score 10


Constraints:

1 <= receiver.length == n <= 10^5
0 <= receiver[i] <= n - 1
1 <= k <= 10^10

"""

# V0
# IDEA : BINARY LIFTING (a.k.a. sparse table on the functional graph)
#
#   k can be 10^10, so simulating every pass is hopeless. `receiver` is a
#   functional graph (exactly one outgoing edge per node), so jumping is
#   composable and we can pre-compute power-of-two jumps.
#
#   up[j][i] = player reached after 2^j passes starting at i
#   sm[j][i] = sum of the indices TOUCHED on those 2^j passes, counting the
#              start i but NOT the final landing player
#
#   base (j = 0, one pass): up[0][i] = receiver[i], sm[0][i] = i
#   step: up[j][i]  = up[j-1][ up[j-1][i] ]
#         sm[j][i]  = sm[j-1][i] + sm[j-1][ up[j-1][i] ]
#
#   then for each start i we decompose k into its set bits, chaining the jumps
#   and accumulating the sums.
#
#   NOTE : `sm` excludes the landing player on purpose - that is what makes the
#          two halves compose without double counting the joint node. the final
#          answer adds the very last player back once: total = acc + p.
#   NOTE : answers are big (n * k ~ 10^15) - fine in Python, but a Java/C++
#          port must use 64-bit.
#
# time = O(n * log k), space = O(n * log k)
class Solution(object):
    def getMaxFunctionValue(self, receiver, k):
        n = len(receiver)
        m = max(1, k.bit_length())

        up = [list(receiver)]
        sm = [list(range(n))]
        for j in range(1, m):
            pu, ps = up[j - 1], sm[j - 1]
            cu = [0] * n
            cs = [0] * n
            for i in range(n):
                mid = pu[i]
                cu[i] = pu[mid]
                cs[i] = ps[i] + ps[mid]
            up.append(cu)
            sm.append(cs)

        ans = 0
        for i in range(n):
            p = i
            acc = 0
            for j in range(m):
                if (k >> j) & 1:
                    acc += sm[j][p]
                    p = up[j][p]
            if acc + p > ans:
                ans = acc + p
        return ans

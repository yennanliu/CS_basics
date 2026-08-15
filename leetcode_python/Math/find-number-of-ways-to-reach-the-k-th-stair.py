"""

3154. Find Number of Ways to Reach the K-th Stair
Hard

You are given a non-negative integer k. There exists a staircase with an infinite number of stairs, with the lowest stair numbered 0.

Alice has an integer jump, with an initial value of 0. She starts on stair 1 and wants to reach stair k using any number of operations. If she is on stair i, in one operation she can:

Go down to stair i - 1. This operation cannot be used consecutively or on stair 0.
Go up to stair i + 2^jump. And then, jump becomes jump + 1.

Return the total number of ways Alice can reach stair k.

Note that it is possible that Alice reaches the stair k, and performs some operations to reach the stair k again.


Example 1:

Input: k = 0
Output: 2
Explanation:
The 2 possible ways of reaching stair 0 are:
Alice starts at stair 1.
    Using an operation of the first type, she goes down 1 stair to reach stair 0.
Alice starts at stair 1.
    Using an operation of the first type, she goes down 1 stair to reach stair 0.
    Using an operation of the second type, she goes up 2^0 stairs to reach stair 1.
    Using an operation of the first type, she goes down 1 stair to reach stair 0.

Example 2:

Input: k = 1
Output: 4
Explanation:
The 4 possible ways of reaching stair 1 are:
Alice starts at stair 1, and is already at stair 1.
Alice starts at stair 1.
    Using an operation of the first type, she goes down 1 stair to reach stair 0.
    Using an operation of the second type, she goes up 2^0 stairs to reach stair 1.
Alice starts at stair 1.
    Using an operation of the second type, she goes up 2^0 stairs to reach stair 2.
    Using an operation of the first type, she goes down 1 stair to reach stair 1.
Alice starts at stair 1.
    Using an operation of the first type, she goes down 1 stair to reach stair 0.
    Using an operation of the second type, she goes up 2^0 stairs to reach stair 1.
    Using an operation of the second type, she goes up 2^1 stairs to reach stair 3.
    Using an operation of the first type, she goes down 1 stair to reach stair 2.
    Using an operation of the first type, she goes down 1 stair to reach stair 1.


Constraints:

0 <= k <= 10^9

"""

# V0
# IDEA : THE UP-MOVES ARE FORCED — SO ONLY *WHERE* THE DOWN-MOVES GO IS FREE
#
#   the j-th up-move always adds 2^(j-1), so after exactly j of them Alice
#   has climbed 1 + (2^j - 1) = 2^j, whatever order things happened in. each
#   down-move subtracts 1, so landing on k needs
#
#       d = 2^j - k     down-moves
#
#   the down-moves cannot be consecutive, which means they must sit in
#   distinct "slots" — before the 1st up-move, between consecutive up-moves,
#   and after the last — giving j + 1 slots holding at most one each. so the
#   count for a given j is C(j + 1, d), and 0 <= d <= j + 1 bounds which j
#   contribute at all.
#
#   2^j overtakes k + j + 1 quickly, so around 60 values of j cover the whole
#   10^9 range.
#
# time = O(log k * ...) — about 60 binomials, space = O(1)
class Solution(object):
    def waysToReachStair(self, k):

        def comb(n, r):
            if r < 0 or r > n:
                return 0
            res = 1
            for t in range(r):
                res = res * (n - t) // (t + 1)
            return res

        res = 0
        for j in range(62):
            d = (1 << j) - k              # down-moves needed
            if d < 0:
                continue
            if d > j + 1:
                break                     # 2^j only grows faster from here
            res += comb(j + 1, d)
        return res

"""

1643. Kth Smallest Instructions
Hard

Bob is standing at cell (0, 0), and he wants to reach destination: (row, column). He can only travel right and down. You are going to help Bob by providing instructions for him to reach destination.

The instructions are represented as a string, where each character is either:

'H', meaning move horizontally (go right), or
'V', meaning move vertically (go down).

Multiple instructions will lead Bob to destination. For example, if destination is (2, 3), both "HHHVV" and "HVHVH" are valid instructions.

However, Bob is very picky. Bob has a lucky number k, and he wants the kth lexicographically smallest instructions that will lead him to destination. k is 1-indexed.

Given an integer array destination and an integer k, return the kth lexicographically smallest instructions that will take Bob to destination.


Example 1:

Input: destination = [2,3], k = 1
Output: "HHHVV"
Explanation: All the instructions that reach (2, 3) in lexicographic order are as follows:
["HHHVV", "HHVHV", "HHVVH", "HVHHV", "HVHVH", "HVVHH", "VHHHV", "VHHVH", "VHVHH", "VVHHH"].

Example 2:

Input: destination = [2,3], k = 2
Output: "HHVHV"

Example 3:

Input: destination = [2,3], k = 3
Output: "HHVVH"


Constraints:

destination.length == 2
1 <= row, column <= 15
1 <= k <= nCr(row + column, row), where nCr(a, b) denotes a choose b.

"""

# V0
# IDEA : COMBINATORIAL COUNTING, build the answer greedily left to right
#
#   'H' < 'V', so at every step try 'H' first. If we commit to 'H' here,
#   the remaining suffix has (h-1) H's and v V's, giving
#     C(h - 1 + v, h - 1)
#   distinct completions -- all of which come BEFORE any string starting
#   with 'V' at this position.
#
#   so: if k <= that count, take 'H'; otherwise take 'V' and subtract the
#   count from k (we skipped that whole block).
#
#   NOTE : when h hits 0 the rest is forced to all 'V'.
#
# time = O((row + col)^2), space = O(row + col)
class Solution(object):
    def kthSmallestPath(self, destination, k):
        v, h = destination[0], destination[1]

        def comb(a, b):
            # a choose b, exact integer arithmetic
            if b < 0 or b > a:
                return 0
            b = min(b, a - b)
            res = 1
            for i in range(b):
                res = res * (a - i) // (i + 1)
            return res

        out = []
        for _ in range(h + v):
            if h == 0:
                out.append('V')
                v -= 1
                continue
            cnt = comb(h - 1 + v, h - 1)     # completions if we place 'H'
            if k <= cnt:
                out.append('H')
                h -= 1
            else:
                out.append('V')
                v -= 1
                k -= cnt
        return "".join(out)

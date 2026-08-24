"""

2320. Count Number of Ways to Place Houses
Medium

There is a street with n * 2 plots, where there are n plots on each side of the street. The plots on each side are numbered from 1 to n. On each plot, a house can be placed.

Return the number of ways houses can be placed such that no two houses are adjacent to each other on the same side of the street. Since the answer may be very large, return it modulo 10^9 + 7.

Note that if a house is placed on the ith plot on one side of the street, a house can also be placed on the ith plot on the other side of the street.


Example 1:

Input: n = 1
Output: 4
Explanation:
Possible arrangements:
1. All plots are empty.
2. A house is placed on one side of the street.
3. A house is placed on the other side of the street.
4. Two houses are placed, one on each side of the street.

Example 2:

Input: n = 2
Output: 9
Explanation: The 9 possible arrangements are shown in the diagram above.


Constraints:

1 <= n <= 10^4

"""

# V0
# IDEA : FIBONACCI PER SIDE, THEN SQUARE (the two sides are independent)
#
#   the adjacency rule only applies within one side, so
#       answer = (ways for one side)^2
#
#   for a single side, let
#       a = arrangements of the first i plots ending with a house
#       b = arrangements of the first i plots ending with an empty plot
#   then a' = b, b' = a + b   -> total(i) = a + b is Fibonacci-like,
#   with total(1) = 2, total(2) = 3, total(3) = 5, ...
#
#   NOTE : n = 1 -> one side has 2 options -> 2 * 2 = 4, matching Example 1.
#
"""

DP def
    the "no two adjacent" rule only applies WITHIN one side, and the two
    sides are independent -> ans = (ways for one side)^2

    a[i]: arrangements of the first i plots ENDING WITH a house
    b[i]: arrangements of the first i plots ENDING WITH an empty plot

DP eq

     a[i] = b[i-1]              # a house needs an empty plot before it

     b[i] = a[i-1] + b[i-1]     # an empty plot follows anything


    -> e.g. total(i) = a[i] + b[i] is Fibonacci-like
              total(1) = 2, total(2) = 3, total(3) = 5, ...

     init: a[1] = b[1] = 1
     ans = total(n)^2 % (10^9 + 7)

"""
# time = O(n), space = O(1)
class Solution(object):
    def countHousePlacements(self, n):
        MOD = 10 ** 9 + 7
        with_house, empty = 1, 1        # n = 1
        for _ in range(n - 1):
            with_house, empty = empty, (with_house + empty) % MOD
        one_side = (with_house + empty) % MOD
        return one_side * one_side % MOD

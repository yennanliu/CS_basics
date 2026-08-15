"""

3044. Most Frequent Prime
Medium

You are given a m x n 0-indexed 2D matrix mat. From every cell, you can create numbers in the following way:

There could be at most 8 paths from the cells namely: east, south-east, south, south-west, west, north-west, north, and north-east.
Select a path from them and append digits in this path to the number being formed by traveling in this direction.
Note that numbers are generated at every step, for example, if the digits along the path are 1, 9, 1, then there will be three numbers generated along the way: 1, 19, 191.

Return the most frequent prime number greater than 10 out of all the numbers created by traversing the matrix or -1 if no such prime number exists. If there are multiple prime numbers with the highest frequency, then return the largest of them.

Note: It is invalid to change the direction during the search.


Example 1:

Input: mat = [[1,1],[9,9],[1,1]]
Output: 19
Explanation:
From cell (0,0) there are 3 possible directions and the numbers greater than 10 which can be created in those directions are:
East: [11], South-East: [19], South: [19,191].
Numbers greater than 10 created are [11, 19, 19, 191].
Similarly, for other cells the numbers created are all present in the array [11, 19, 19, 191].
As 19 is the most frequent prime, we return 19.

Example 2:

Input: mat = [[7]]
Output: -1
Explanation: The only number which can be formed is 7. It is a prime smaller than 10, so we return -1.

Example 3:

Input: mat = [[9,7,8],[4,6,5],[2,8,6]]
Output: 97
Explanation:
From cell (0,0) the numbers created are [97, 978, 96, 966, 94, 942].
From cell (0,1) the numbers created are [78, 786, 74, 742, 76, 768, 79, 794].
Numbers created from other cells are all in the array [97, 978, 96, 966, 94, 942, 78, 786, 74, 742, 76, 768, 79, 794].
As 97 is the most frequent prime, we return 97.


Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 6
1 <= mat[i][j] <= 9

"""

# V0
# IDEA : THE GRID IS TINY — ENUMERATE EVERY RAY AND TEST PRIMALITY
#
#   6x6 cells x 8 directions x at most 6 steps gives well under 2000
#   candidate numbers, each below 10^6, so brute force is comfortable.
#
#   walk each ray digit by digit, building the number as
#       value = value * 10 + digit
#   and tally every value above 10 that is prime.
#
#   the tie-break wants the LARGEST among the most frequent, so rank the
#   candidates by (frequency, value) and take the maximum.
#
# time = O(m * n * max(m,n) * sqrt(max value)), space = O(number of primes seen)
from collections import Counter


class Solution(object):
    def mostFrequentPrime(self, mat):
        m, n = len(mat), len(mat[0])
        DIRS = ((0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1), (-1, 0), (-1, 1))

        def is_prime(x):
            if x < 2:
                return False
            if x % 2 == 0:
                return x == 2
            d = 3
            while d * d <= x:
                if x % d == 0:
                    return False
                d += 2
            return True

        cnt = Counter()
        for i in range(m):
            for j in range(n):
                for di, dj in DIRS:
                    value = 0
                    r, c = i, j
                    while 0 <= r < m and 0 <= c < n:
                        value = value * 10 + mat[r][c]
                        if value > 10 and is_prime(value):
                            cnt[value] += 1
                        r += di
                        c += dj

        if not cnt:
            return -1
        return max(cnt, key=lambda v: (cnt[v], v))

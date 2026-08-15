"""

3145. Find Products of Elements of Big Array
Hard

The powerful array of a non-negative integer x is defined as the shortest sorted array of powers of two that sum up to x. The table below illustrates examples of how the powerful array is determined. It can be proven that the powerful array of x is unique.

    num     powerful array
    1       [1]
    8       [8]
    10      [2, 8]
    13      [1, 4, 8]
    23      [1, 2, 4, 16]

The array big_nums is created by concatenating the powerful arrays for every positive integer i in ascending order: 1, 2, 3, and so forth. Thus, big_nums begins as [1, 2, 1, 2, 4, 1, 4, 2, 4, 1, 2, 4, 8, ...].

You are given a 2D integer matrix queries, where for queries[i] = [from_i, to_i, mod_i] you should calculate (big_nums[from_i] * big_nums[from_i + 1] * ... * big_nums[to_i]) % mod_i.

Return an integer array answer such that answer[i] is the answer to the ith query.


Example 1:

Input: queries = [[1,3,7]]
Output: [4]
Explanation:
There is one query.
big_nums[1..3] = [2,1,2]. The product of them is 4. The remainder of 4 under 7 is 4.

Example 2:

Input: queries = [[2,5,3],[7,7,4]]
Output: [2,2]
Explanation:
There are two queries.
First query: big_nums[2..5] = [1,2,4,1]. The product of them is 8. The remainder of 8 under 3 is 2.
Second query: big_nums[7] = 2. The remainder of 2 under 4 is 2.


Constraints:

1 <= queries.length <= 500
queries[i].length == 3
0 <= queries[i][0] <= queries[i][1] <= 10^15
1 <= queries[i][2] <= 10^5

"""

# V0
# IDEA : EVERY ENTRY IS A POWER OF TWO — SO WORK WITH EXPONENT *SUMS*
#
#   big_nums holds only powers of two, so a product over a range is
#   2^(sum of the exponents in that range), and the whole problem reduces to
#   one prefix function
#
#       g(N) = sum of the exponents of big_nums[0 .. N-1]
#
#   with the answer being pow(2, g(to+1) - g(from), mod).
#
#   g is computed without touching the array. two closed forms over the
#   integers 1..n do the heavy lifting :
#       C(n) = total set bits in 1..n   (how many ENTRIES those numbers give)
#       S(n) = sum of the set-bit POSITIONS in 1..n
#   both from the standard per-bit count, since bit b cycles with period
#   2^(b+1) :
#       ones(n, b) = (n+1)//2^(b+1) * 2^b + max(0, (n+1) % 2^(b+1) - 2^b)
#
#   then binary search the largest n whose entries all fit inside the first N
#   (C(n) <= N), add S(n), and finish with the first few set bits of n+1 for
#   the partially consumed number.
#
#   NOTE : mod may be 1, and pow() already returns 0 there.
#
# time = O(q * log^2(max index)), space = O(1)
class Solution(object):
    def findProductsOfElements(self, queries):

        def ones(n, b):
            """how many of 1..n have bit b set"""
            p = 1 << b
            period = p << 1
            return (n + 1) // period * p + max(0, (n + 1) % period - p)

        def count_upto(n):
            """number of big_nums entries contributed by 1..n"""
            if n <= 0:
                return 0
            total = 0
            b = 0
            while (1 << b) <= n:
                total += ones(n, b)
                b += 1
            return total

        def expsum_upto(n):
            """sum of the exponents contributed by 1..n"""
            if n <= 0:
                return 0
            total = 0
            b = 0
            while (1 << b) <= n:
                total += b * ones(n, b)
                b += 1
            return total

        def g(N):
            """sum of exponents of the first N entries of big_nums"""
            if N <= 0:
                return 0
            lo, hi = 0, N                       # C(n) >= n, so n never exceeds N
            while lo < hi:
                mid = (lo + hi + 1) // 2
                if count_upto(mid) <= N:
                    lo = mid
                else:
                    hi = mid - 1
            res = expsum_upto(lo)
            rem = N - count_upto(lo)            # entries taken from the number lo+1
            v, b = lo + 1, 0
            while rem and v:
                if v & 1:
                    res += b
                    rem -= 1
                v >>= 1
                b += 1
            return res

        return [pow(2, g(to + 1) - g(frm), mod) for frm, to, mod in queries]

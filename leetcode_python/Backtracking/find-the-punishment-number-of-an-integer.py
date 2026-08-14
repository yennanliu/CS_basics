"""

2698. Find the Punishment Number of an Integer
Medium

Given a positive integer n, return the punishment number of n.

The punishment number of n is defined as the sum of the squares of all integers i such that:

1 <= i <= n
The decimal representation of i * i can be partitioned into contiguous substrings such that the sum of the integer values of these substrings equals i.


Example 1:

Input: n = 10
Output: 182
Explanation: There are exactly 3 integers i in the range [1, 10] that satisfy the conditions in the statement:
- 1 since 1 * 1 = 1
- 9 since 9 * 9 = 81 and 81 can be partitioned into 8 and 1 with a sum equal to 8 + 1 == 9.
- 10 since 10 * 10 = 100 and 100 can be partitioned into 10 and 0 with a sum equal to 10 + 0 == 10.
Hence, the punishment number of 10 is 1 + 81 + 100 = 182

Example 2:

Input: n = 37
Output: 1478
Explanation: There are exactly 4 integers i in the range [1, 37] that satisfy the conditions in the statement:
- 1 since 1 * 1 = 1.
- 9 since 9 * 9 = 81 and 81 can be partitioned into 8 + 1.
- 10 since 10 * 10 = 100 and 100 can be partitioned into 10 + 0.
- 36 since 36 * 36 = 1296 and 1296 can be partitioned into 1 + 29 + 6.
Hence, the punishment number of 37 is 1 + 81 + 100 + 1296 = 1478


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : BACKTRACKING OVER THE SPLIT POINTS OF str(i * i)
#
#   for each i, walk the digits of i*i and try every possible length for the
#   next chunk. carry `rest` = how much of i is still unmatched :
#     - consumed the whole string -> success iff rest == 0
#     - otherwise grow the chunk digit by digit (y = y * 10 + digit) and
#       recurse with rest - y
#
#   NOTE : PRUNING - once y > rest the chunk (and every longer one, since
#          appending digits only grows y) can never fit, so break out.
#          this is what keeps the search tiny.
#   NOTE : chunks may be "0" or have leading zeros ("100" -> 10 + 0), which
#          the running y = y*10 + d construction handles naturally.
#   NOTE : recursion depth is bounded by the digit count of i*i (<= 7 for
#          n <= 1000), so no stack concerns.
#
# time = O(n * 2^d) with d = digits of n^2 (<= 7), space = O(d)
class Solution(object):
    def punishmentNumber(self, n):
        def can_split(s, start, rest):
            if start == len(s):
                return rest == 0
            y = 0
            for j in range(start, len(s)):
                y = y * 10 + int(s[j])
                if y > rest:
                    break                     # pruning: longer chunk only bigger
                if can_split(s, j + 1, rest - y):
                    return True
            return False

        ans = 0
        for i in range(1, n + 1):
            sq = i * i
            if can_split(str(sq), 0, i):
                ans += sq
        return ans

"""

2217. Find Palindrome With Fixed Length
Medium

Given an integer array queries and a positive integer intLength, return an array answer where answer[i] is either the queries[i]th smallest positive palindrome of length intLength or -1 if no such palindrome exists.

A palindrome is a number that reads the same backwards and forwards. Palindromes cannot have leading zeros.


Example 1:

Input: queries = [1,2,3,4,5,90], intLength = 3
Output: [101,111,121,131,141,999]
Explanation:
The first few palindromes of length 3 are:
101, 111, 121, 131, 141, 151, 161, 171, 181, 191, 202, ...
The 90th palindrome of length 3 is 999.

Example 2:

Input: queries = [2,4,6], intLength = 4
Output: [1111,1331,1551]
Explanation:
The first six palindromes of length 4 are:
1001, 1111, 1221, 1331, 1441, and 1551.


Constraints:

1 <= queries.length <= 5 * 10^4
1 <= queries[i] <= 10^9
1 <= intLength <= 15

"""

# V0
# IDEA : MATH / CONSTRUCTION (a palindrome is fully decided by its left half)
#
#   let h = (intLength + 1) // 2  -> length of the "free" left half.
#   the k-th palindrome (1-indexed) has left half = 10^(h-1) + k - 1,
#   because left halves run 10^(h-1), 10^(h-1)+1, ... in increasing order
#   and the palindrome order matches the left-half order.
#
#   build : s = str(half); answer = s + reverse(s) with the middle digit
#           dropped when intLength is odd.
#   NOTE : if half > 10^h - 1 there is no such palindrome -> -1.
#
# time = O(q * L), q = len(queries), L = intLength
# space = O(q)
class Solution(object):
    def kthPalindrome(self, queries, intLength):
        h = (intLength + 1) // 2
        start = 10 ** (h - 1)
        end = 10 ** h - 1

        res = []
        for q in queries:
            v = start + q - 1
            if v > end:
                res.append(-1)
                continue
            s = str(v)
            # odd length -> the middle digit must not be duplicated
            s = s + s[::-1][intLength % 2:]
            res.append(int(s))
        return res

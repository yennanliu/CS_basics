# Time:  O(logn)
# Space: O(logn)

# X is a good number if after rotating each digit individually by 180 degrees,
# we get a valid number that is different from X.
# A number is valid if each digit remains a digit after rotation.
# 0, 1, and 8 rotate to themselves; 2 and 5 rotate to each other;
# 6 and 9 rotate to each other, and the rest of the numbers do not rotate to any other number.
#
# Now given a positive number N, how many numbers X from 1 to N are good?
#
# Example:
# Input: 10
# Output: 4
# Explanation:
# There are four good numbers in the range [1, 10] : 2, 5, 6, 9.
# Note that 1 and 10 are not good numbers, since they remain unchanged after rotating.
#
# Note:
# - N  will be in range [1, 10000].


# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79378135
"""

DP def
    a number is GOOD iff it contains no 3, 4 or 7 (those cannot be rotated)
    AND contains at least one 2, 5, 6 or 9 (so the rotation actually DIFFERS)

    as a digit DP over the decimal digits of N:

    dp(pos, has_diff, limit): how many ways to fill digits pos.. , where

        has_diff - a 2 / 5 / 6 / 9 has already been placed
        limit    - the prefix still matches N's prefix, so the digit is capped

DP eq

     dp(pos, has_diff, limit) = sum over d in {0,1,8,2,5,6,9}, d <= up, of

        dp( pos+1, has_diff or d in {2,5,6,9}, limit and d == up )

        (digits 3, 4, 7 are simply never placed)


    -> e.g. base: pos == n -> 1 if has_diff else 0

     the brute-force alternative just tests every number in 1..N by string
     scan - O(n log n), fine for small N but the digit DP is O(log N)

     ans = dp(0, False, True)

"""
# time = O(n * logn), n = N (checks each digit of every number in 1..N)
# space = O(logn)
class Solution(object):
    def rotatedDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        dmap = {"0" : "0", "1" : "1", "8" : "8", "2" : "5", "5" : "2", "6" : "9", "9" : "6"}
        res = 0
        for num in range(1, N + 1):
            if any(x in str(num) for x in ["3", "4", "7"]):
                continue
            if any(x in str(num) for x in ["2", "5", "6", "9"]):
                res += 1
        return res

# V1'
# http://bookshadow.com/weblog/2018/02/25/leetcode-rotated-digits/
"""

DP def
    a number is GOOD iff it contains no 3, 4 or 7 (those cannot be rotated)
    AND contains at least one 2, 5, 6 or 9 (so the rotation actually DIFFERS)

    as a digit DP over the decimal digits of N:

    dp(pos, has_diff, limit): how many ways to fill digits pos.. , where

        has_diff - a 2 / 5 / 6 / 9 has already been placed
        limit    - the prefix still matches N's prefix, so the digit is capped

DP eq

     dp(pos, has_diff, limit) = sum over d in {0,1,8,2,5,6,9}, d <= up, of

        dp( pos+1, has_diff or d in {2,5,6,9}, limit and d == up )

        (digits 3, 4, 7 are simply never placed)


    -> e.g. base: pos == n -> 1 if has_diff else 0

     the brute-force alternative just tests every number in 1..N by string
     scan - O(n log n), fine for small N but the digit DP is O(log N)

     ans = dp(0, False, True)

"""
# time = O(n * logn), n = N (checks each digit of every number in 1..N)
# space = O(logn)
class Solution(object):
    def rotatedDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        ans = 0
        for n in range(1, N + 1):
            nset = set(map(int, str(n)))
            if any(x in nset for x in (2, 5, 6, 9)):
                if not any(x in nset for x in (3, 4, 7)):
                    ans += 1
        return ans
             
# V2
# IDEA : digit DP
"""

DP def
    a number is GOOD iff it contains no 3, 4 or 7 (those cannot be rotated)
    AND contains at least one 2, 5, 6 or 9 (so the rotation actually DIFFERS)

    as a digit DP over the decimal digits of N:

    dp(pos, has_diff, limit): how many ways to fill digits pos.. , where

        has_diff - a 2 / 5 / 6 / 9 has already been placed
        limit    - the prefix still matches N's prefix, so the digit is capped

DP eq

     dp(pos, has_diff, limit) = sum over d in {0,1,8,2,5,6,9}, d <= up, of

        dp( pos+1, has_diff or d in {2,5,6,9}, limit and d == up )

        (digits 3, 4, 7 are simply never placed)


    -> e.g. base: pos == n -> 1 if has_diff else 0

     the brute-force alternative just tests every number in 1..N by string
     scan - O(n log n), fine for small N but the digit DP is O(log N)

     ans = dp(0, False, True)

"""
# time = O(logn), n = N (states over the O(logn) digits)
# space = O(logn)
class Solution(object):
    def rotatedDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        A = map(int, str(N))
        invalid, diff = set([3, 4, 7]), set([2, 5, 6, 9])
        def dp(A, i, is_prefix_equal, is_good, lookup):
            if i == len(A): return int(is_good)
            if (i, is_prefix_equal, is_good) not in lookup:
                result = 0
                for d in range(A[i]+1 if is_prefix_equal else 10):
                    if d in invalid: continue
                    result += dp(A, i+1,
                                 is_prefix_equal and d == A[i],
                                 is_good or d in diff,
                                 lookup)
                lookup[i, is_prefix_equal, is_good] = result
            return lookup[i, is_prefix_equal, is_good]

        lookup = {}
        return dp(A, 0, True, False, lookup)


# time = O(n), n = N
# space = O(n)
class Solution2(object):
    def rotatedDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        INVALID, SAME, DIFF = 0, 1, 2
        same, diff = [0, 1, 8], [2, 5, 6, 9]
        dp = [0] * (N+1)
        dp[0] = SAME
        for i in range(N//10+1):
            if dp[i] != INVALID:
                for j in same:
                    if i*10+j <= N:
                        dp[i*10+j] = max(SAME, dp[i])
                for j in diff:
                    if i*10+j <= N:
                        dp[i*10+j] = DIFF
        return dp.count(DIFF)


# time = O(nlogn) = O(n), n = N, because O(logn) = O(32) by this input
# space = O(logn) = O(1)
class Solution3(object):
    def rotatedDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        invalid, diff = set(['3', '4', '7']), set(['2', '5', '6', '9'])
        result = 0
        for i in range(N+1):
            lookup = set(list(str(i)))
            if invalid & lookup:
                continue
            if diff & lookup:
                result += 1
        return result

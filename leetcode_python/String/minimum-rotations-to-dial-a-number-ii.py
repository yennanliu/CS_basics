"""

4071. Minimum Rotations to Dial a Number II
Medium

You are given an integer n and a string s of length n consisting of digits.

The dial contains the digits 0 through 9 in order and is circular, so 0 and
9 are adjacent. The pointer initially points to 0.

To dial each digit of s in order, rotate the pointer until it points to that
digit. Each rotation moves the pointer to an adjacent digit, and you may
rotate in either direction. Dialing a digit that the pointer already points
to requires no rotations.

Before dialing, you may perform the following operation at most once:

Choose an index k such that 0 <= k < n and reverse the suffix s[k..n - 1].

Return the minimum total number of rotations needed to dial the string after
optimally choosing whether to perform the operation and which suffix to
reverse.

Example 1:

Input: n = 4, s = "1502"

Output: 9

Explanation:

Reverse the suffix starting at k = 1 to obtain "1205", then dial it.
0 -> 1 : 1,  1 -> 2 : 1,  2 -> 0 : 2,  0 -> 5 : 5

The total is 1 + 1 + 2 + 5 = 9, which is the minimum total number of
rotations.

Example 2:

Input: n = 4, s = "2916"

Output: 12

Explanation:

Choose not to reverse a suffix and dial "2916".
0 -> 2 : 2,  2 -> 9 : 3,  9 -> 1 : 2,  1 -> 6 : 5

The total is 2 + 3 + 2 + 5 = 12, which is the minimum total number of
rotations.

Example 3:

Input: n = 4, s = "4219"

Output: 6

Explanation:

Reverse the suffix starting at k = 0, which reverses the entire string, to
obtain "9124", then dial it.
0 -> 9 : 1,  9 -> 1 : 2,  1 -> 2 : 1,  2 -> 4 : 2

The total is 1 + 2 + 1 + 2 = 6, which is the minimum total number of
rotations.

Constraints:

1 <= n == s.length <= 10^5
s consists only of digits '0' to '9'

"""


# V0
# IDEA : TRY EVERY k + PREFIX / SUFFIX SUMS (price each reversal in O(1))
#
#   Brute force (the contest draft): build s[:k] + s[k:][::-1] for every k and
#   re-dial it -> O(n^2), TLE at n = 10^5. (The draft also built
#   s[:i] + s[i+1:][::-1], which DROPS s[i] - the suffix starts AT k.)
#
#   Split the cost of dialing s[:k] + reversed(s[k:]) into 3 parts:
#
#       1) prefix[k]   = cost to dial s[0..k-1] starting from 0
#       2) the jump    = dist(s[k-1], s[n-1])   (or dist(0, s[n-1]) if k == 0)
#                        -> reversed, the suffix starts with its LAST digit
#       3) inner[k]    = sum of dist(s[i], s[i+1]) for i in [k, n-2]
#
#   Part 3 is why this works: dist is symmetric, so walking the suffix
#   backwards costs exactly what walking it forwards does.
#   k = n - 1 reverses one digit, which is the same as not reversing at all,
#   so "no operation" is already one of the candidates.
#
#   e.g. s = "1502", k = 1 -> prefix = dist(0,1) = 1
#                             jump   = dist(1,2) = 1
#                             inner  = dist(5,0) + dist(0,2) = 5 + 2 = 7
#                             total  = 9
#
# time = O(n), space = O(n)
DIAL_SIZE = 10


class Solution(object):
    def minRotations(self, n, s):
        """
        :type n: int
        :type s: str
        :rtype: int
        """
        # edge case: nothing to dial
        if not s:
            return 0

        n = len(s)
        digits = [int(ch) for ch in s]

        # prefix[k] = cost to dial digits[0..k-1], pointer starting at 0
        prefix = [0] * (n + 1)
        pos = 0
        for i in range(n):
            prefix[i + 1] = prefix[i] + self.dist(pos, digits[i])
            pos = digits[i]

        # inner[k] = cost of walking digits[k..n-1] between adjacent digits
        #            (same in either direction, since dist is symmetric)
        inner = [0] * n
        for k in range(n - 2, -1, -1):
            inner[k] = inner[k + 1] + self.dist(digits[k], digits[k + 1])

        last_digit = digits[n - 1]
        best = prefix[n]  # no operation

        for k in range(n):
            # where the pointer rests before the reversed suffix starts
            if k == 0:
                pos_before = 0
            else:
                pos_before = digits[k - 1]

            # NOTE !!! the reversed suffix is dialed starting from s[n-1]
            total = prefix[k] + self.dist(pos_before, last_digit) + inner[k]
            best = min(best, total)

        return best

    def dist(self, a, b):
        """Fewest rotations between digits a and b on the circular dial."""
        direct = abs(a - b)
        return min(direct, DIAL_SIZE - direct)

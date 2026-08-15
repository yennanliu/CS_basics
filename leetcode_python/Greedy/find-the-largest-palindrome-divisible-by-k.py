"""

3260. Find the Largest Palindrome Divisible by K
Hard

You are given two positive integers n and k.

An integer x is called k-palindromic if:

x is a palindrome.
x is divisible by k.

Return the largest integer having n digits (as a string) that is k-palindromic.

Note that the integer must not have leading zeros.


Example 1:

Input: n = 3, k = 5
Output: "595"
Explanation:
595 is the largest k-palindromic integer with 3 digits.

Example 2:

Input: n = 1, k = 4
Output: "8"
Explanation:
4 and 8 are the only k-palindromic integers with 1 digit.

Example 3:

Input: n = 5, k = 6
Output: "89898"


Constraints:

1 <= n <= 10^5
1 <= k <= 9

"""

# V0
# IDEA : ONLY THE FIRST HALF IS FREE, AND EACH DIGIT HAS A FIXED WEIGHT MOD k
#
#   a palindrome is decided by its first ceil(n/2) digits : digit i also
#   appears at position n-1-i. so its value modulo k is
#
#       sum over i of digit[i] * weight[i]      (mod k)
#       weight[i] = (10^i + 10^(n-1-i)) % k,  or just 10^i for the middle
#
#   the answer wants the largest number, i.e. the lexicographically greatest
#   digit string, so walk the half left to right and take the biggest digit
#   that still leaves the rest COMPLETABLE.
#
#   completability is a tiny reachability table : reach[i] holds every
#   remainder attainable from positions i.. onward, built backwards over the
#   10 digit choices. picking digit d at position i is safe exactly when the
#   remainder it leaves can be cancelled by something in reach[i+1].
#
#   k <= 9, so there are at most 9 remainders and the whole thing is linear.
#
# time = O(n * 10 * k), space = O(n * k)
class Solution(object):
    def largestPalindrome(self, n, k):
        half = (n + 1) // 2

        weight = [0] * half
        for i in range(half):
            w = pow(10, n - 1 - i, k)
            if i != n - 1 - i:                 # not the middle digit
                w = (w + pow(10, i, k)) % k
            weight[i] = w % k

        # reach[i] = remainders obtainable from positions i .. half-1
        reach = [None] * (half + 1)
        reach[half] = set([0])
        for i in range(half - 1, -1, -1):
            cur = set()
            w = weight[i]
            for d in range(10):
                add = d * w % k
                for r in reach[i + 1]:
                    cur.add((add + r) % k)
            reach[i] = cur

        digits = []
        cur = 0
        for i in range(half):
            lo = 1 if i == 0 else 0            # no leading zero
            for d in range(9, lo - 1, -1):
                nxt = (cur + d * weight[i]) % k
                if (k - nxt) % k in reach[i + 1]:
                    digits.append(str(d))
                    cur = nxt
                    break

        left = ''.join(digits)
        if n % 2:
            return left + left[-2::-1]
        return left + left[::-1]

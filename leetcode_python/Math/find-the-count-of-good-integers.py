"""

3272. Find the Count of Good Integers
Hard

You are given two positive integers n and k.

An integer x is called k-palindromic if:

x is a palindrome.
x is divisible by k.

An integer is called good if its digits can be rearranged to form a k-palindromic integer. For example, for k = 2, 2020 can be rearranged to form the k-palindromic integer 0202, whereas 1010 cannot be rearranged to form a k-palindromic integer.

Return the count of good integers containing n digits.

Note that any integer must not have leading zeros, neither before nor after rearrangement. For example, 1010 cannot be rearranged to form 101.


Example 1:

Input: n = 3, k = 5
Output: 27
Explanation:
Some of the good integers are:
551 because it can be rearranged to form 515.
525 because it is already k-palindromic.

Example 2:

Input: n = 1, k = 4
Output: 2
Explanation:
The two good integers are 4 and 8.

Example 3:

Input: n = 5, k = 6
Output: 2468


Constraints:

1 <= n <= 10
1 <= k <= 9

"""

# V0
# IDEA : ENUMERATE THE PALINDROMES, THEN COUNT PERMUTATIONS PER DIGIT MULTISET
#
#   "good" only depends on an integer's MULTISET of digits, so the plan is :
#
#   1. build every n-digit palindrome by choosing its first half — at most
#      10^5 of them for n = 10 — and keep the ones divisible by k.
#   2. record each survivor's sorted digit string, deduplicated : two
#      palindromes with the same multiset describe the same set of good
#      integers.
#   3. for each surviving multiset, count the n-digit numbers using exactly
#      those digits :
#           total arrangements = n! / prod(count[d]!)
#           minus those starting with 0 = (n-1)! * count[0] / prod(count[d]!)
#      which simplifies to (n - count[0]) * (n-1)! / prod(count[d]!).
#
#   n <= 10 keeps the factorials exact in python integers.
#
# time = O(10^ceil(n/2) * n), space = O(number of multisets)
from math import factorial


class Solution(object):
    def countGoodIntegers(self, n, k):
        half = (n + 1) // 2
        seen = set()

        lo = 10 ** (half - 1)
        hi = 10 ** half
        for first in range(lo, hi):
            s = str(first)
            pal = s + (s[-2::-1] if n % 2 else s[::-1])
            if int(pal) % k:
                continue
            seen.add(''.join(sorted(pal)))

        res = 0
        for key in seen:
            cnt = [0] * 10
            for ch in key:
                cnt[int(ch)] += 1
            ways = factorial(n - 1) * (n - cnt[0])       # no leading zero
            for c in cnt:
                ways //= factorial(c)
            res += ways
        return res

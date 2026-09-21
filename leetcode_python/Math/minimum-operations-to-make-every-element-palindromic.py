"""

4053. Minimum Operations to Make Every Element Palindromic
Medium

You are given an integer array nums.

In one operation, you may choose an index i and either increment or
decrement nums[i] by 2.

Return the minimum number of operations required to make every element in
nums a positive palindrome. Different elements may be changed into
different palindromic integers.


Example 1:

Input: nums = [10,12,14,16]

Output: 9

Explanation:

One optimal sequence of operations is:

Decrement nums[0] by 2 once to change it from 10 to 8.
Decrement nums[1] by 2 twice to change it from 12 to 8.
Decrement nums[2] by 2 three times to change it from 14 to 8.
Increment nums[3] by 2 three times to change it from 16 to 22.

After 1 + 2 + 3 + 3 = 9 operations, nums = [8, 8, 8, 22], and every element
is a positive palindromic integer.

It can be shown that fewer than 9 operations cannot achieve this.

Example 2:

Input: nums = [9,10,11,10]

Output: 2

Explanation:

Decrement nums[1] and nums[3] by 2 once each.

After 2 operations, nums = [9, 8, 11, 8], and every element is a positive
palindromic integer. At least one operation is needed for each of these two
elements, so the minimum number of operations is 2.

Example 3:

Input: nums = [125]

Output: 2

Explanation:

Decrement nums[0] by 2 twice to change it from 125 to 121, which is a
positive palindromic integer. A single operation would change it to 123 or
127, neither of which is palindromic.


Constraints:

1 <= nums.length <= 10^5

1 <= nums[i] <= 10^9

"""

# V0
# IDEA : PARITY IS INVARIANT -> BISECT THE NEAREST SAME-PARITY PALINDROME
#
# NOTE !!! +2 / -2 NEVER changes a number's parity, so an EVEN x can only ever
#          land on an EVEN palindrome and an ODD x on an ODD one. that is the
#          whole problem - miss it and 10 "reaches" 9 in half an operation.
#
#   an operation touches one index, so the elements are independent and the
#   answer is just sum(cost(x)), with
#
#       cost(x) = |x - p| // 2   for the closest palindrome p, p % 2 == x % 2
#
#   over a SORTED list "closest" is only ever 2 candidates - the predecessor
#   and the successor - so precompute every palindrome once, split the list by
#   parity, and bisect each x :
#
#     e.g. x = 16 (even) -> pred = 8  -> (16 - 8) // 2 = 4
#                        -> succ = 22 -> (22 - 16) // 2 = 3   <-- the min
#
#   the precompute is cheap because a palindrome is fixed by its FIRST HALF :
#   there are only ~2 * 10^5 of them below 10^10, which is already well past
#   the 10^9 cap on nums[i].
#
# time = O(P log P + n log P), space = O(P)   # P = #palindromes ~ 2 * 10^5
import bisect


_PALINDROMES = {}   # parity -> sorted list of palindromes with that parity


def _build_palindromes(max_len=10):
    """every palindrome with <= max_len digits, bucketed by parity (cached)"""
    if _PALINDROMES:
        return _PALINDROMES

    by_parity = {0: [], 1: []}

    for length in range(1, max_len + 1):
        half_len = (length + 1) // 2
        for half in range(10 ** (half_len - 1), 10 ** half_len):
            s = str(half)
            # NOTE !!! an ODD length re-uses the middle digit only ONCE,
            #          hence s[-2::-1] instead of s[::-1]
            p = int(s + (s[-2::-1] if length % 2 else s[::-1]))
            by_parity[p % 2].append(p)

    for parity in by_parity:
        by_parity[parity].sort()

    _PALINDROMES.update(by_parity)
    return _PALINDROMES


class Solution(object):
    def minOperations(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge
        if not nums:
            return 0

        table = _build_palindromes()

        res = 0

        for x in nums:
            cand = table[x % 2]
            idx = bisect.bisect_left(cand, x)

            best = float('inf')
            # successor (idx lands ON x when x is already a palindrome -> 0)
            if idx < len(cand):
                best = min(best, (cand[idx] - x) // 2)
            # predecessor
            if idx > 0:
                best = min(best, (x - cand[idx - 1]) // 2)

            res += best

        return res

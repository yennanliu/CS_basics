"""

2844. Minimum Operations to Make a Special Number
Medium

You are given a 0-indexed string num representing a non-negative integer.

In one operation, you can pick any digit of num and delete it. Note that if you delete all the digits of num, num becomes 0.

Return the minimum number of operations required to make num special.

An integer x is considered special if it is divisible by 25.


Example 1:

Input: num = "2245047"
Output: 2
Explanation: Delete digits num[5] and num[6]. The resulting number is "22450" which is special since it is divisible by 25.
It can be shown that 2 is the minimum number of operations required to get a special number.

Example 2:

Input: num = "2908305"
Output: 3
Explanation: Delete digits num[3], num[4], and num[6]. The resulting number is "2900" which is special since it is divisible by 25.
It can be shown that 3 is the minimum number of operations required to get a special number.

Example 3:

Input: num = "10"
Output: 1
Explanation: Delete digit num[0]. The resulting number is "0" which is special since it is divisible by 25.
It can be shown that 1 is the minimum number of operations required to get a special number.


Constraints:

1 <= num.length <= 100
num only consists of digits '0' through '9'.
num does not contain any leading zeros.

"""

# V0
# IDEA : GREEDY - only the LAST TWO digits decide divisibility by 25
#
#   a number is divisible by 25 iff its last two digits form "00", "25", "50"
#   or "75" (or the number is the single digit 0, or empty which counts as 0).
#
#   deleting digits never reorders them, so the surviving number is a
#   subsequence. everything before the final pair is free - we keep it all -
#   which means the cost of ending with the pair (j, i), j < i, is
#
#       (digits strictly between j and i) + (digits after i)
#     = (i - j - 1) + (n - 1 - i)
#     = n - j - 2
#
#   independent of i. so for each of the 4 endings we scan from the RIGHT for
#   the last digit, then for the nearest matching first digit to its left -
#   the larger j is, the cheaper.
#
#   NOTE : don't forget the degenerate target 0: keeping a single '0' costs
#          n - 1, and wiping the string costs n. that is the fallback when no
#          two-digit ending is reachable.
#   NOTE : leading zeros in the RESULT are fine here - "00" reads as 0, which
#          is divisible by 25 (the no-leading-zero rule applies to the input).
#
# time = O(4 * n), space = O(1)
class Solution(object):
    def minimumOperations(self, num):
        n = len(num)

        # fallback: delete everything (-> 0), or keep exactly one '0'
        ans = n
        if '0' in num:
            ans = n - 1

        for tail in ("00", "25", "50", "75"):
            i = num.rfind(tail[1])
            if i <= 0:
                continue
            j = num.rfind(tail[0], 0, i)
            if j < 0:
                continue
            cost = n - j - 2
            if cost < ans:
                ans = cost
        return ans

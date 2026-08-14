"""

1737. Change Minimum Characters to Satisfy One of Three Conditions
Medium

You are given two strings a and b that consist of lowercase letters. In one operation, you can change any character in a or b to any lowercase letter.

Your goal is to satisfy one of the following three conditions:

Every letter in a is strictly less than every letter in b in the alphabet.
Every letter in b is strictly less than every letter in a in the alphabet.
Both a and b consist of only one distinct letter.

Return the minimum number of operations needed to achieve your goal.


Example 1:

Input: a = "aba", b = "caa"
Output: 2
Explanation: Consider the best way to make each condition true:
1) Change b to "ccc" in 2 operations, then every letter in a is less than every letter in b.
2) Change a to "bbb" and b to "aaa" in 3 operations, then every letter in b is less than every letter in a.
3) Change a to "aaa" and b to "aaa" in 2 operations, then a and b consist of one distinct letter.
The best way was done in 2 operations (either condition 1 or condition 3).

Example 2:

Input: a = "dabadd", b = "cda"
Output: 3
Explanation: The best way is to make condition 1 true by changing b to "eee".


Constraints:

1 <= a.length, b.length <= 10^5
a and b consist only of lowercase letters.

"""

# V0
# IDEA : LETTER COUNTS + ENUMERATE THE 26 SPLIT POINTS
#
#   only the letter HISTOGRAMS matter, not the positions - count once, then
#   every condition becomes arithmetic over 26 buckets.
#
#   condition 3 (both strings one letter c):
#       cost = (m + n) - cnt_a[c] - cnt_b[c]        -> try all 26 c
#
#   condition 1 (every letter of a < every letter of b): pick a boundary
#   letter i in 1..25 and demand a < i <= b. everything violating it must
#   be rewritten:
#       cost = #{chars of a with index >= i} + #{chars of b with index < i}
#   running prefix sums make each i an O(1) update.
#
#   condition 2 is condition 1 with a and b swapped.
#
#   NOTE : i starts at 1 - i = 0 would leave no letter available for a.
#
# time = O(m + n + 26), space = O(26)
class Solution(object):
    def minCharacters(self, a, b):
        m, n = len(a), len(b)

        ca = [0] * 26
        cb = [0] * 26
        for ch in a:
            ca[ord(ch) - 97] += 1
        for ch in b:
            cb[ord(ch) - 97] += 1

        res = m + n

        # condition 3 : collapse both strings onto one letter
        for i in range(26):
            res = min(res, m + n - ca[i] - cb[i])

        # conditions 1 & 2 : split at letter i
        pa = pb = 0        # chars with index < i
        for i in range(1, 26):
            pa += ca[i - 1]
            pb += cb[i - 1]
            res = min(res, (m - pa) + pb)   # a all < i, b all >= i
            res = min(res, (n - pb) + pa)   # b all < i, a all >= i

        return res

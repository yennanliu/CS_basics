"""

1363. Largest Multiple of Three
Hard

Given an array of digits digits, return the largest multiple of three that can be formed by concatenating some of the given digits in any order. If there is no answer return an empty string.

Since the answer may not fit in an integer data type, return the answer as a string. Note that the returning answer must not contain unnecessary leading zeros.


Example 1:

Input: digits = [8,1,9]
Output: "981"

Example 2:

Input: digits = [8,6,7,1,0]
Output: "8760"

Example 3:

Input: digits = [1]
Output: ""


Constraints:

1 <= digits.length <= 10^4
0 <= digits[i] <= 9

"""

# V0
# IDEA : DP (max count per remainder) + BACKTRACK THE CHOICES
#
#  A number is a multiple of 3 iff its DIGIT SUM is a multiple of 3,
#  and the digit ORDER is free -> we only need to pick a multiset of
#  digits whose sum % 3 == 0, then print them in descending order.
#  To get the largest number we first maximize the COUNT of digits,
#  and among equal counts prefer larger digits.
#
#  DP def:
#    - f[i][j] = max #digits picked from the first i (sorted asc) digits
#                such that their sum % 3 == j     (-inf = unreachable)
#
#  DP eq:
#    - f[i][j] = max(f[i-1][j],                       # skip digits[i-1]
#                    f[i-1][(j - x % 3 + 3) % 3] + 1) # take digits[i-1]
#
#  Then walk i from n down to 1 (largest digit first, i.e. most significant
#  position first) and take digits[i-1] whenever it lies on an optimal path
#  -> that is greedily the biggest digit we can afford at that position.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def largestMultipleOfThree(self, digits):
        digits = sorted(digits)
        n = len(digits)
        NEG = float("-inf")

        f = [[NEG] * 3 for _ in range(n + 1)]
        f[0][0] = 0
        for i in range(1, n + 1):
            x = digits[i - 1]
            for j in range(3):
                f[i][j] = max(f[i - 1][j], f[i - 1][(j - x % 3 + 3) % 3] + 1)

        if f[n][0] <= 0:
            return ""

        arr = []
        j = 0
        for i in range(n, 0, -1):
            k = (j - digits[i - 1] % 3 + 3) % 3
            if f[i - 1][k] + 1 == f[i][j]:
                arr.append(digits[i - 1])
                j = k

        # strip leading zeros, but keep a single "0"
        i = 0
        while i < len(arr) - 1 and arr[i] == 0:
            i += 1
        return "".join(map(str, arr[i:]))


# V1
# IDEA : GREEDY COUNTING - drop the fewest / smallest digits
#
#  sort desc, let r = sum % 3.
#  - r == 0 -> keep everything
#  - r == 1 -> drop one smallest digit with d % 3 == 1,
#              else drop two smallest digits with d % 3 == 2
#  - r == 2 -> drop one smallest digit with d % 3 == 2,
#              else drop two smallest digits with d % 3 == 1
#
# time = O(n log n)
# space = O(n)
class Solution2(object):
    def largestMultipleOfThree(self, digits):
        digits = sorted(digits, reverse=True)
        r = sum(digits) % 3

        if r != 0:
            # indices (from the back = smallest values) grouped by d % 3
            buckets = {1: [], 2: []}
            for i in range(len(digits) - 1, -1, -1):
                m = digits[i] % 3
                if m:
                    buckets[m].append(i)

            drop = None
            if buckets[r]:
                drop = [buckets[r][0]]
            else:
                other = 3 - r
                if len(buckets[other]) >= 2:
                    drop = buckets[other][:2]
            if drop is None:
                return ""
            for i in sorted(drop, reverse=True):
                digits.pop(i)

        if not digits:
            return ""
        if digits[0] == 0:
            return "0"
        return "".join(map(str, digits))

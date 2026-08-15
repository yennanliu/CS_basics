"""

2719. Count of Integers
Hard

You are given two numeric strings num1 and num2 and two integers max_sum and min_sum. We denote an integer x to be good if:

num1 <= x <= num2
min_sum <= digit_sum(x) <= max_sum.

Return the number of good integers. Since the answer may be large, return it modulo 10^9 + 7.

Note that digit_sum(x) denotes the sum of the digits of x.


Example 1:

Input: num1 = "1", num2 = "12", min_sum = 1, max_sum = 8
Output: 11
Explanation: There are 11 integers whose sum of digits lies between 1 and 8 are 1,2,3,4,5,6,7,8,10,11, and 12. Thus, we return 11.

Example 2:

Input: num1 = "1", num2 = "5", min_sum = 1, max_sum = 5
Output: 5
Explanation: The 5 integers whose sum of digits lies between 1 and 5 are 1,2,3,4, and 5. Thus, we return 5.


Constraints:

1 <= num1 <= num2 <= 10^22
1 <= min_sum <= max_sum <= 400

"""

# V0
# IDEA : DIGIT DP (count [0, hi] then subtract via prefix difference)
#
#   answer = f(num2) - f(num1 - 1), where f(hi) counts x in [0, hi] whose
#   digit sum falls inside [min_sum, max_sum].
#
#   f(hi) is a classic digit DP: scan the decimal digits left -> right,
#   carrying (pos, running digit sum, tight?) where `tight` means every digit
#   placed so far equals hi's prefix, so the current digit is capped by hi[pos].
#
#   NOTE : only the NON-tight states are memoizable — a tight state depends on
#          the actual prefix of hi, so it is visited at most once per pos anyway.
#   NOTE : leading zeros are harmless here, they add 0 to the digit sum, so no
#          separate "started" flag is needed (min_sum >= 1 already excludes 0).
#   NOTE : num1 - 1 must be done on the integer (num1 can be 10^22, way past
#          64-bit, but Python ints are unbounded so int(num1) - 1 is exact).
#   NOTE : prune s > max_sum early, it keeps the state space at ~n * max_sum.
#
# time = O(10 * n * max_sum), space = O(n * max_sum)   (n = len(num2) <= 23)
class Solution(object):
    def count(self, num1, num2, min_sum, max_sum):
        MOD = 10 ** 9 + 7

        def count_upto(num):
            n = len(num)
            memo = {}

            def dfs(pos, s, tight):
                if s > max_sum:
                    return 0
                if pos == n:
                    return 1 if s >= min_sum else 0
                if not tight and (pos, s) in memo:
                    return memo[(pos, s)]
                up = int(num[pos]) if tight else 9
                res = 0
                for d in range(up + 1):
                    res += dfs(pos + 1, s + d, tight and d == up)
                res %= MOD
                if not tight:
                    memo[(pos, s)] = res
                return res

            return dfs(0, 0, True)

        lo = str(int(num1) - 1)
        return (count_upto(num2) - count_upto(lo)) % MOD

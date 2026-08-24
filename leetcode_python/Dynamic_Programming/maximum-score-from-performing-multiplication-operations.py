"""

1770. Maximum Score from Performing Multiplication Operations
Medium

You are given two 0-indexed integer arrays nums and multipliers of size n and m respectively, where n >= m.

You begin with a score of 0. You want to perform exactly m operations. On the i^th operation (0-indexed) you will:

Choose one integer x from either the start or the end of the array nums.
Add multipliers[i] * x to your score.

Note that multipliers[0] corresponds to the first operation, multipliers[1] to the second operation, and so on.

Remove x from nums.

Return the maximum score after performing m operations.

Example 1:

Input: nums = [1,2,3], multipliers = [3,2,1]
Output: 14
Explanation: An optimal solution is as follows:
- Choose from the end, [1,2,3], adding 3 * 3 = 9 to the score.
- Choose from the end, [1,2], adding 2 * 2 = 4 to the score.
- Choose from the end, [1], adding 1 * 1 = 1 to the score.
The total score is 9 + 4 + 1 = 14.

Example 2:

Input: nums = [-5,-3,-3,-2,7,1], multipliers = [-10,-5,3,4,6]
Output: 102
Explanation: An optimal solution is as follows:
- Choose from the start, [-5,-3,-3,-2,7,1], adding -5 * -10 = 50 to the score.
- Choose from the start, [-3,-3,-2,7,1], adding -3 * -5 = 15 to the score.
- Choose from the start, [-3,-2,7,1], adding -3 * 3 = -9 to the score.
- Choose from the end, [-2,7,1], adding 1 * 4 = 4 to the score.
- Choose from the end, [-2,7], adding 7 * 6 = 42 to the score.
The total score is 50 + 15 - 9 + 4 + 42 = 102.

Constraints:

n == nums.length
m == multipliers.length
1 <= m <= 300
m <= n <= 10^5
-1000 <= nums[i], multipliers[i] <= 1000

"""

# V0
# IDEA : DP ON (operation index, how many were taken from the LEFT)
#
#   after k operations of which i came from the left, the remaining window is
#   fully determined : left index = i, right index = n - 1 - (k - i).
#   so one index is enough for the state.
#     f[k][i] = max( mult[k] * nums[i]            + f[k + 1][i + 1],
#                    mult[k] * nums[n - 1 - k + i] + f[k + 1][i]     )
#   answer = f[0][0], and f[m][*] = 0.
#   NOTE : n can be 10^5 but only m <= 300 elements are ever touched, so the
#          table is O(m^2), not O(n * m). rolling f in place over descending k
#          works because f[i] is written after f[i + 1] is read.
#
"""

DP def
    after k operations of which i came from the LEFT, the remaining window is
    fully determined: left index = i, right index = n - 1 - (k - i).
    so ONE index is enough for the state.

    f[k][i]: best score obtainable from operations k..m-1, given that i of the

             first k operations took from the left

DP eq

     f[k][i] = max(
                  mult[k] * nums[i]                + f[k+1][i+1],   # take LEFT
                  mult[k] * nums[n - 1 - (k - i)]  + f[k+1][i]      # take RIGHT
               )


    -> e.g. NOTE !!! n can be 10^5 but only m <= 300 elements are ever
              touched, so the table is O(m^2), not O(n * m)

     rolling f in place over DESCENDING k works because f[i] is written after
     f[i+1] is read

     init: f[m][*] = 0
     ans = f[0][0]

"""
# time = O(m^2), space = O(m)
class Solution(object):
    def maximumScore(self, nums, multipliers):
        n, m = len(nums), len(multipliers)
        f = [0] * (m + 1)
        for k in range(m - 1, -1, -1):
            mul = multipliers[k]
            for i in range(k + 1):
                j = n - 1 - (k - i)
                f[i] = max(mul * nums[i] + f[i + 1], mul * nums[j] + f[i])
        return f[0]

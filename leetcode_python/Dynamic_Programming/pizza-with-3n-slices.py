"""

1388. Pizza With 3n Slices
Hard

There is a pizza with 3n slices of varying size, you and your friends will take slices of pizza
as follows:

You will pick any pizza slice.
Your friend Alice will pick the next slice in the anti-clockwise direction of your pick.
Your friend Bob will pick the next slice in the clockwise direction of your pick.
Repeat until there are no more slices of pizzas.

Given an integer array slices that represent the sizes of the pizza slices in a clockwise direction,
return the maximum possible sum of slice sizes that you can pick.


Example 1:

Input: slices = [1,2,3,4,5,6]
Output: 10
Explanation: Pick pizza slice of size 4, Alice and Bob will pick slices with size 3 and 5
respectively. Then Pick slices with size 6, finally Alice and Bob will pick slice of size 2 and 1
respectively. Total = 4 + 6.

Example 2:

Input: slices = [8,9,8,6,1,1]
Output: 16
Explanation: Pick pizza slice of size 8 in each turn. If you pick slice with size 9 your partners
will pick slices of size 8.


Constraints:

3 * n == slices.length
1 <= slices.length <= 500
1 <= slices[i] <= 1000

"""

# V0
# IDEA : CIRCULAR HOUSE ROBBER, but taking EXACTLY n slices
#
#   restating the game : every slice you take burns its two neighbours (Alice
#   and Bob eat them), so your picks are pairwise NON-ADJACENT on the circle,
#   and after 3n slices you will have taken exactly n = len(slices) // 3.
#
#   -> maximise the sum of exactly n non-adjacent elements on a circle.
#
#   the circle is handled the usual way : slices[0] and slices[-1] are adjacent,
#   so at most one of them is taken. Solve the linear problem twice --
#   on slices[:-1] and on slices[1:] -- and take the better result.
#
#   linear DP :
#     f[i][j] = best sum using the first i elements and taking exactly j of them
#     f[i][j] = max( f[i-1][j],                 # skip element i
#                    f[i-2][j-1] + nums[i-1] )  # take it, so i-1 is blocked
#
"""

DP def
    restating the game: every slice you take burns its two neighbours (Alice
    and Bob eat them), so your picks are pairwise NON-ADJACENT on the circle,
    and you end up with exactly n = len(slices) // 3 of them

    -> maximise the sum of EXACTLY n non-adjacent elements on a CIRCLE

    f[i][j]: best sum using the first i elements and taking exactly j of them

DP eq

     f[i][j] = max(
                  f[i-1][j],                  # SKIP element i
                  f[i-2][j-1] + nums[i-1]     # TAKE it, so i-1 is blocked
               )


    -> e.g. the CIRCLE is handled the usual way: slices[0] and slices[-1]
              are adjacent, so at most one is taken -> solve the LINEAR
              problem twice, on slices[:-1] and on slices[1:], and take the
              better result

     ans = max( best(slices[:-1]), best(slices[1:]) )

"""
# time = O(n^2), space = O(n^2)
class Solution(object):
    def maxSizeSlices(self, slices):
        k = len(slices) // 3

        def best(nums):
            m = len(nums)
            f = [[0] * (k + 1) for _ in range(m + 1)]
            for i in range(1, m + 1):
                for j in range(1, k + 1):
                    take = (f[i - 2][j - 1] if i >= 2 else 0) + nums[i - 1]
                    f[i][j] = max(f[i - 1][j], take)
            return f[m][k]

        return max(best(slices[:-1]), best(slices[1:]))

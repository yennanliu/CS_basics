"""

956. Tallest Billboard
Hard

You are installing a billboard and want it to have the largest height. The billboard will have two steel supports, one on each side. Each steel support must be an equal height.

You are given a collection of rods that can be welded together. For example, if you have rods of lengths 1, 2, and 3, you can weld them together to make a support of length 6.

Return the largest possible height of your billboard installation. If you cannot support the billboard, return 0.

Example 1:

Input: rods = [1,2,3,6]
Output: 6
Explanation: We have two disjoint subsets {1,2,3} and {6}, which have the same sum = 6.

Example 2:

Input: rods = [1,2,3,4,5,6]
Output: 10
Explanation: We have two disjoint subsets {2,3,5} and {4,6}, which have the same sum = 10.

Example 3:

Input: rods = [1,2]
Output: 0
Explanation: The billboard cannot be supported, so we return 0.

Constraints:

1 <= rods.length <= 20
1 <= rods[i] <= 1000
sum(rods[i]) <= 5000

"""

# V0
# IDEA : DP on the DIFFERENCE between the two supports
#
#  DP def:
#     - dp[d] = the maximum height of the TALLER support, given that
#               (taller - shorter) == d
#     - the shorter support is then simply (dp[d] - d)
#     - dp[0] is the answer: both supports equal, height = dp[0]
#
#  For each rod h, from state (d, taller) we may:
#     1) drop the rod             -> state unchanged
#     2) weld it on taller side   -> diff = d + h,  taller = taller + h
#     3) weld it on shorter side  -> new shorter = (taller - d) + h,
#                                    new diff / taller recomputed via max/abs
#
#  We iterate over a SNAPSHOT of dp so every rod is used at most once
#  (0/1 knapsack semantics).
#
# time = O(n * S), n = len(rods), S = sum(rods)
# space = O(S)
class Solution(object):
    def tallestBillboard(self, rods):
        # dp[diff] = max height of the taller support
        dp = {0: 0}

        for h in rods:
            snapshot = dict(dp)
            for d, taller in snapshot.items():
                shorter = taller - d

                # case 2 : add rod to the taller side
                if dp.get(d + h, -1) < taller + h:
                    dp[d + h] = taller + h

                # case 3 : add rod to the shorter side (it may overtake the taller one)
                new_shorter = shorter + h
                new_diff = abs(new_shorter - taller)
                new_taller = max(new_shorter, taller)
                if dp.get(new_diff, -1) < new_taller:
                    dp[new_diff] = new_taller

        return dp[0]

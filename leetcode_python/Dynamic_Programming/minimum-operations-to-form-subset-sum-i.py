"""

4040. Minimum Operations to Form Subset Sum I
Medium

You are given an integer array nums and an integer sum.

In one operation you may choose an element with current value x and replace it
by either 2 * x or floor(x / 2).

For each element, all of its multiplication operations must be performed before
any of its division operations.

Return the minimum number of operations needed so that some subset of the
resulting array has a sum exactly equal to sum.
If it is impossible, return -1.

Example 1:

Input: nums = [5,6,10], sum = 4

Output: 3

Explanation:

5 -> 2 (one division) and 10 -> 5 -> 2 (two divisions) gives [2,6,2],
whose subset {2,2} sums to 4, for 3 operations in total.
(6 -> 3 with 5 -> 2 -> 1 is another 3-operation answer.)

Example 2:

Input: nums = [10,2], sum = 13

Output: 3

Explanation:

10 -> 5 (one division) and 2 -> 4 -> 8 (two multiplications) gives [5,8],
whose subset {5,8} sums to 13, for 3 operations in total.

Constraints:

(not recorded in the contest notes — the DP below indexes an array of size
sum + 1, so sum is small enough for that)

"""

# V0
# IDEA : GROUP 0/1 KNAPSACK OVER EACH ELEMENT'S REACHABLE VALUES
#
#   an element is used at most once, and the cheapest way to reach a value is
#   what matters -- so this is a knapsack whose "items" are GROUPS : for each
#   original x, the pairs (value it can become, operations that cost).
#
#   the group is only the two PURE chains, and that is what the
#   "multiply before divide" rule buys :
#
#     x, 2x, 4x, ...        (k doublings, cost k)
#     x, x//2, x//4, ...    (k halvings,  cost k)
#
#   a mixed run is never worth it -- k doublings then j halvings lands on
#   x * 2^(k-j) (the doubling loses no digits, so the halvings undo it exactly),
#   which the pure chain already reaches at cost |k - j| instead of k + j.
#   ONLY divide-then-multiply could reach something new (5 -> 2 -> 4), and the
#   rule forbids it.
#
#   e.g. x = 10, sum = 13 -> (10,0), (5,1), (2,2), (1,3)   [20 is over sum]
#
# NOTE !!! dp is read and new_dp written, so every option of one x competes
#          against the state BEFORE x -- that is what keeps the element used
#          at most once (updating dp in place would let 2 + 2 + ... reuse it)
#
# time = O(n * sum * log(max(x, sum))), space = O(sum)
class Solution(object):
    def minOperations(self, nums, sum):
        """
        :type nums: List[int]
        :type sum: int
        :rtype: int
        """
        INF = float('inf')

        # dp[s] = min operations to make some subset sum to s
        dp = [INF] * (sum + 1)
        dp[0] = 0

        for x in nums:

            # every (value, cost) this one x can become
            options = []

            # keep x as it is
            if x <= sum:
                options.append((x, 0))

            # x -> 2x -> 4x -> ...  (stop once past sum, it only grows)
            value, op = x, 0
            while value <= sum:
                if op > 0:
                    options.append((value, op))
                value *= 2
                op += 1

            # x -> x//2 -> x//4 -> ...  (stop at 0, it can never help a sum)
            value, op = x, 0
            while value > 0:
                value //= 2
                op += 1
                if value == 0:
                    break
                if value <= sum:
                    options.append((value, op))

            new_dp = dp[:]
            for value, cost in options:
                for s in range(value, sum + 1):
                    if dp[s - value] != INF:
                        new_dp[s] = min(new_dp[s], dp[s - value] + cost)

            dp = new_dp

        return -1 if dp[sum] == INF else dp[sum]

"""

3277. Maximum XOR Score Subarray Queries
Hard

You are given an array nums of n integers, and a 2D integer array queries of size q, where queries[i] = [l_i, r_i].

For each query, you must find the maximum XOR score of any subarray of nums[l_i..r_i].

The XOR score of an array a is found by repeatedly applying the following operations on a so that only one element remains, that is the score:

Simultaneously replace a[i] with a[i] XOR a[i + 1] for all indices i except the last one.
Remove the last element of a.

Return an array answer of size q where answer[i] is the answer to query i.


Example 1:

Input: nums = [2,8,4,32,16,1], queries = [[0,2],[1,4],[0,5]]
Output: [12,60,60]
Explanation:
In the first query, nums[0..2] has 6 subarrays [2], [8], [4], [2, 8], [8, 4], and [2, 8, 4] each with a respective XOR score of 2, 8, 4, 10, 12, and 6. The answer for the query is 12, the largest of all XOR scores.

Example 2:

Input: nums = [0,7,3,2,8,5,1], queries = [[0,3],[1,5],[2,4],[2,6],[5,6]]
Output: [7,14,11,14,5]


Constraints:

1 <= n == nums.length <= 2000
0 <= nums[i] <= 2^31 - 1
1 <= q == queries.length <= 10^5
queries[i].length == 2
queries[i] = [l_i, r_i]
0 <= l_i <= r_i <= n - 1

"""

# V0
# IDEA : THE XOR SCORE OBEYS A PASCAL-LIKE RECURRENCE, SO TABULATE IT
#
#   collapsing a[l..r] one step at a time turns out to satisfy
#
#       score(l, r) = score(l, r-1) XOR score(l+1, r)
#
#   — the same shape as a binomial triangle — with score(l, l) = nums[l]. so
#   the n^2 scores fill in by increasing length.
#
#   the queries want the BEST score over all subarrays inside a range, which
#   is itself a 2D running maximum :
#
#       best(l, r) = max(score(l, r), best(l, r-1), best(l+1, r))
#
#   filling both tables costs O(n^2) = 4 * 10^6 for n = 2000, after which
#   every query is a single lookup — which is what makes 10^5 queries cheap.
#
# time = O(n^2 + q), space = O(n^2)
class Solution(object):
    def maximumSubarrayXor(self, nums, queries):
        n = len(nums)
        score = [[0] * n for _ in range(n)]
        best = [[0] * n for _ in range(n)]
        for i in range(n):
            score[i][i] = nums[i]
            best[i][i] = nums[i]

        for length in range(2, n + 1):
            for l in range(0, n - length + 1):
                r = l + length - 1
                score[l][r] = score[l][r - 1] ^ score[l + 1][r]
                best[l][r] = max(score[l][r], best[l][r - 1], best[l + 1][r])

        return [best[l][r] for l, r in queries]

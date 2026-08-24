"""

3018. Maximum Number of Removal Queries That Can Be Processed I
Hard
🔒 (premium)

You are given a 0-indexed array nums and a 0-indexed array queries.

You can do the following operation at the beginning at most once:

Replace nums with a subsequence of nums.

We start processing queries in the given order; for each query, we do the following:

If the first and the last element of nums is less than queries[i], the processing of queries ends.
Otherwise, we choose either the first or the last element of nums if it is greater than or equal to queries[i], and we remove the chosen element from nums.

Return the maximum number of queries that can be processed by doing the operation optimally.


Example 1:

Input: nums = [1,2,3,4,5], queries = [1,2,3,4,6]
Output: 4
Explanation: We don't do any operation and process the queries as follows:
1- We choose and remove nums[0] since 1 <= 1, then nums becomes [2,3,4,5].
2- We choose and remove nums[0] since 2 <= 2, then nums becomes [3,4,5].
3- We choose and remove nums[0] since 3 <= 3, then nums becomes [4,5].
4- We choose and remove nums[0] since 4 <= 4, then nums becomes [5].
5- We can not choose any elements from nums since they are not greater than or equal to 5.
Hence, the answer is 4.
It can be shown that we can't process more than 4 queries.

Example 2:

Input: nums = [2,3,2], queries = [2,2,3]
Output: 3
Explanation: We don't do any operation and process the queries as follows:
1- We choose and remove nums[0] since 2 <= 2, then nums becomes [3,2].
2- We choose and remove nums[1] since 2 <= 2, then nums becomes [3].
3- We choose and remove nums[0] since 3 <= 3, then nums becomes [].
Hence, the answer is 3.
It can be shown that we can't process more than 3 queries.

Example 3:

Input: nums = [3,4,3], queries = [4,3,2]
Output: 2
Explanation: First we replace nums with the subsequence of nums [4,3].
Then we can process the queries as follows:
1- We choose and remove nums[0] since 4 <= 4, then nums becomes [3].
2- We choose and remove nums[0] since 3 <= 3, then nums becomes [].
3- We can not process any more queries since nums is empty.
Hence, the answer is 2.
It can be shown that we can't process more than 2 queries.


Constraints:

1 <= nums.length <= 1000
1 <= queries.length <= 1000
1 <= nums[i], queries[i] <= 10^9

"""

# V0
# IDEA : DP OVER (TAKEN FROM THE LEFT, TAKEN FROM THE RIGHT)
#
#   the up-front "replace with a subsequence" is the same as being allowed to
#   DISCARD any element for free as it reaches an end. so the whole process
#   is two pointers eating the original array inwards, and at each step the
#   element at an end is either
#       spent on the current query (only if it is >= that query), or
#       thrown away.
#
#   state = (i, j) : i elements consumed from the left, j from the right.
#   the value stored is the max number of queries already answered — and
#   because the queries are processed IN ORDER, that count doubles as the
#   index of the next query, so no third dimension is needed.
#
#   from (i, j) with k answered :
#       discard left  -> (i+1, j) keeps k
#       spend  left   -> (i+1, j) reaches k+1 when nums[i] >= queries[k]
#       discard right -> (i, j+1) keeps k
#       spend  right  -> (i, j+1) reaches k+1 when nums[n-1-j] >= queries[k]
#
"""

DP def
    the up-front "replace with a subsequence" is the same as being allowed to
    DISCARD any element for free once it reaches an end - so the process is
    two pointers eating the array inwards

    dp[i][j]: MAX number of queries already answered, given i elements

              consumed from the LEFT and j from the RIGHT

              -> queries are processed IN ORDER, so that count doubles as the
                 INDEX of the next query - no third dimension needed

DP eq

     from (i, j) with k answered:

        discard left  -> dp[i+1][j] >= k
        spend  left   -> dp[i+1][j] >= k+1   if nums[i]     >= queries[k]

        discard right -> dp[i][j+1] >= k
        spend  right  -> dp[i][j+1] >= k+1   if nums[n-1-j] >= queries[k]


    -> e.g. -1 marks unreachable states so they never seed anything

     init: dp[0][0] = 0
     ans = max k reached

"""
# time = O(n^2), space = O(n^2)
class Solution(object):
    def maximumProcessableQueries(self, nums, queries):
        n, m = len(nums), len(queries)
        NEG = -1
        dp = [[NEG] * (n + 1) for _ in range(n + 1)]
        dp[0][0] = 0
        best = 0

        for i in range(n + 1):
            for j in range(n + 1 - i):
                k = dp[i][j]
                if k == NEG:
                    continue
                best = max(best, k)
                if k == m or i + j == n:
                    continue
                # take from the left end
                if dp[i + 1][j] < k:
                    dp[i + 1][j] = k
                if nums[i] >= queries[k] and dp[i + 1][j] < k + 1:
                    dp[i + 1][j] = k + 1
                # take from the right end
                if dp[i][j + 1] < k:
                    dp[i][j + 1] = k
                if nums[n - 1 - j] >= queries[k] and dp[i][j + 1] < k + 1:
                    dp[i][j + 1] = k + 1

        return best

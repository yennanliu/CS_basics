"""

3177. Find the Maximum Length of a Good Subsequence II
Hard

You are given an integer array nums and a non-negative integer k. A sequence of integers seq is called good if there are at most k indices i in the range [0, seq.length - 2] such that seq[i] != seq[i + 1].

Return the maximum possible length of a good subsequence of nums.


Example 1:

Input: nums = [1,2,1,1,3], k = 2
Output: 4
Explanation:
The maximum length subsequence is [1,2,1,1,3].

Example 2:

Input: nums = [1,2,3,4,5,1], k = 0
Output: 2
Explanation:
The maximum length subsequence is [1,2,3,4,5,1].


Constraints:

1 <= nums.length <= 5 * 10^3
1 <= nums[i] <= 10^9
0 <= k <= min(nums.length, 50)

"""

# V0
# IDEA : DROP THE "WHICH EARLIER INDEX" LOOP — KEEP A BEST-PER-VALUE TABLE
#
#   LC 3176 scans every earlier index; at n = 5000 that is too slow. but the
#   scan only ever needs two facts about the prefix :
#
#       best_same[j][v] = longest good subsequence with j changes ending in v
#       best_any[j]     = the same, maximised over all values
#
#   appending the current x then costs nothing if the run continues (same
#   value, j changes) and one change otherwise :
#
#       new = max(best_same[j][x], best_any[j-1]) + 1
#
#   iterating j downwards keeps the tables at their "before x" state, so x is
#   never chained onto itself.
#
#   NOTE : best_any[j-1] may itself end in x, in which case no change was
#          really spent — that only means the sequence is counted with a
#          looser change budget, which is still a valid good subsequence, so
#          the maximum stays correct.
#
# time = O(n * k), space = O(n * k)
class Solution(object):
    def maximumLength(self, nums, k):
        best_same = [dict() for _ in range(k + 1)]     # changes -> {value: length}
        best_any = [0] * (k + 1)                       # changes -> best length
        res = 0

        for x in nums:
            for j in range(k, -1, -1):
                cand = best_same[j].get(x, 0)
                if j > 0 and best_any[j - 1] > cand:
                    cand = best_any[j - 1]
                cand += 1
                if cand > best_same[j].get(x, 0):
                    best_same[j][x] = cand
                if cand > best_any[j]:
                    best_any[j] = cand
                if cand > res:
                    res = cand
        return res

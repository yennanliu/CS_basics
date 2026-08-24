"""

689. Maximum Sum of 3 Non-Overlapping Subarrays
Hard

Given an integer array nums and an integer k, find three non-overlapping subarrays of
length k with maximum sum and return them.

Return the result as a list of indices representing the starting position of each
interval (0-indexed). If there are multiple answers, return the lexicographically
smallest one.

Example 1:

Input: nums = [1,2,1,2,6,7,5,1], k = 2
Output: [0,3,5]
Explanation: Subarrays [1, 2], [2, 6], [7, 5] correspond to the starting indices [0, 3, 5].
We could have also taken [2, 1], but an answer of [1, 3, 5] would be lexicographically larger.

Example 2:

Input: nums = [1,2,1,2,1,2,1,2,1], k = 2
Output: [0,2,4]

Constraints:

1 <= nums.length <= 2 * 10^4
1 <= nums[i] < 2^16
1 <= k <= floor(nums.length / 3)

"""

# V0
# IDEA : PREFIX SUM + BEST-ON-THE-LEFT / BEST-ON-THE-RIGHT ARRAYS
#
#   Step 1: collapse each length-k window into one number
#             w[i] = sum(nums[i : i+k])
#
#   Step 2: fix the MIDDLE window at index j. The other two are then independent:
#             - best window entirely to the left  -> any start in [0, j-k]
#             - best window entirely to the right -> any start in [j+k, m-1]
#           Precompute those with two sweeps:
#             left[i]  = argmax of w over [0, i]
#             right[i] = argmax of w over [i, m-1]
#
#   Step 3: scan j and keep the best triple.
#
#   Lexicographic tie-breaking -- all three comparisons must prefer the SMALLER
#   index on a tie:
#     - left  sweeps forward  with strict `>`  -> keeps the earliest maximum
#     - right sweeps backward with `>=`        -> keeps the earliest maximum
#     - the j loop runs ascending with strict `>` -> keeps the earliest triple
#
"""

DP def
    collapse each length-k window into one number:
        w[i] = sum(nums[i : i+k])

    then FIX the MIDDLE window at index j - the other two become independent:

    left[i] : index of the BEST window within w[0..i]     (earliest on a tie)
    right[i]: index of the BEST window within w[i..m-1]   (earliest on a tie)

DP eq

     left[i]  = i  if w[i] >  w[left[i-1]]  else left[i-1]

     right[i] = i  if w[i] >= w[right[i+1]] else right[i+1]

     ans = argmax over j of  w[left[j-k]] + w[j] + w[right[j+k]]


    -> e.g. LEXICOGRAPHIC tie-breaking - all three comparisons must prefer
              the SMALLER index:

         left  sweeps forward  with strict `>`   -> earliest maximum
         right sweeps backward with `>=`         -> earliest maximum
         the j loop runs ascending with strict `>` -> earliest triple

     ans = [left[j-k], j, right[j+k]] for the winning j

"""
# time = O(n)
# space = O(n)
class Solution(object):
    def maxSumOfThreeSubarrays(self, nums, k):
        n = len(nums)

        # prefix[i] = sum of nums[:i]
        prefix = [0]
        for x in nums:
            prefix.append(prefix[-1] + x)

        # w[i] = sum of the length-k window starting at i
        w = [prefix[i + k] - prefix[i] for i in range(n - k + 1)]
        m = len(w)

        # left[i] = index of the best window within w[0..i] (earliest on tie)
        left = [0] * m
        best = 0
        for i in range(m):
            if w[i] > w[best]:
                best = i
            left[i] = best

        # right[i] = index of the best window within w[i..m-1] (earliest on tie)
        right = [0] * m
        best = m - 1
        for i in range(m - 1, -1, -1):
            if w[i] >= w[best]:
                best = i
            right[i] = best

        res = []
        best_total = -1
        for j in range(k, m - k):
            i, l = left[j - k], right[j + k]
            total = w[i] + w[j] + w[l]
            if total > best_total:
                best_total = total
                res = [i, j, l]

        return res

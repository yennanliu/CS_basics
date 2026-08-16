"""

3555. Smallest Subarray to Sort in Every Sliding Window
Medium

You are given an integer array nums and an integer k.

For each contiguous subarray of length k, determine the minimum length of a
continuous segment that must be sorted so that the entire window becomes
non‑decreasing; if the window is already sorted, its required length is zero.

Return an array of length n − k + 1 where each element corresponds to the answer
for its window.

Example 1:

Input: nums = [1,3,2,4,5], k = 3

Output: [2,2,0]

Explanation:

nums[0...2] = [1, 3, 2]. Sort [3, 2] to get [1, 2, 3], the answer is 2.

nums[1...3] = [3, 2, 4]. Sort [3, 2] to get [2, 3, 4], the answer is 2.

nums[2...4] = [2, 4, 5] is already sorted, so the answer is 0.

Example 2:

Input: nums = [5,4,3,2,1], k = 4

Output: [4,4]

Explanation:

nums[0...3] = [5, 4, 3, 2]. The whole subarray must be sorted, so the answer is
4.

nums[1...4] = [4, 3, 2, 1]. The whole subarray must be sorted, so the answer is
4.

Constraints:

1 <= nums.length <= 1000

1 <= k <= nums.length

1 <= nums[i] <= 10^6

"""

# V0
# IDEA : PER WINDOW, PEEL OFF THE ALREADY-SETTLED PREFIX AND SUFFIX
#
#   sorting a segment fixes the window exactly when everything left of the
#   segment is already sorted *and* no larger than anything to its right, and
#   symmetrically on the other side.  so the shortest segment is bounded by
#   the last position that is still "wrong" from each end:
#     * scanning left to right with the running maximum, position i must be
#       inside the segment whenever w[i] < max(w[0..i-1]) -- something bigger
#       sits before it and has to move past it;
#     * scanning right to left with the running minimum, position i must be
#       inside whenever w[i] > min(w[i+1..]).
#   the answer is the span between the outermost such positions, and 0 when
#   none exist.
#
#   nums is at most 1000 long, so each of the n - k + 1 windows can afford its
#   own two linear scans rather than any incremental structure.
#
# time = O(n * k), space = O(k)
class Solution(object):
    def minSubarraySort(self, nums, k):
        n = len(nums)
        res = []
        for s in range(n - k + 1):
            w = nums[s:s + k]
            hi = -1
            run = w[0]
            for i in range(1, k):
                if w[i] < run:
                    hi = i
                elif w[i] > run:
                    run = w[i]
            if hi < 0:
                res.append(0)
                continue
            lo = k
            run = w[k - 1]
            for i in range(k - 2, -1, -1):
                if w[i] > run:
                    lo = i
                elif w[i] < run:
                    run = w[i]
            res.append(hi - lo + 1)
        return res

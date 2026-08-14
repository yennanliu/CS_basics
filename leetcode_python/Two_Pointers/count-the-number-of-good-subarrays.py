"""

2537. Count the Number of Good Subarrays
Medium

Given an integer array nums and an integer k, return the number of good subarrays of nums.

A subarray arr is good if there are at least k pairs of indices (i, j) such that i < j and arr[i] == arr[j].

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [1,1,1,1,1], k = 10
Output: 1
Explanation: The only good subarray is the array nums itself.

Example 2:

Input: nums = [3,1,4,3,2,2,4], k = 2
Output: 4
Explanation: There are 4 different good subarrays:
- [3,1,4,3,2,2] that has 2 pairs.
- [3,1,4,3,2,2,4] that has 3 pairs.
- [1,4,3,2,2,4] that has 2 pairs.
- [4,3,2,2,4] that has 2 pairs.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i], k <= 10^9

"""

# V0
# IDEA : SLIDING WINDOW (TWO POINTERS) + HASH COUNTER
#
#   the pair count of a window is MONOTONIC : growing the window can only add
#   pairs, shrinking can only remove them. so for each right end there is a
#   threshold `left` such that every start l < left gives a good subarray and
#   every l >= left does not.
#
#   maintain `pairs` incrementally :
#     - adding x on the right  -> pairs += cnt[x]  (then cnt[x] += 1)
#     - dropping y on the left -> cnt[y] -= 1      (then pairs -= cnt[y])
#   shrink from the left while the window still has >= k pairs; when the loop
#   stops, `left` is the first start that FAILS, so exactly `left` starts
#   (0 .. left-1) work for this right end.
#
#   NOTE : the order of the counter update vs the pairs update is opposite on
#          the two sides -- get it backwards and you off-by-one every window.
#
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def countGood(self, nums, k):
        cnt = defaultdict(int)
        ans = 0
        pairs = 0
        left = 0
        for x in nums:
            pairs += cnt[x]
            cnt[x] += 1
            while pairs >= k:
                y = nums[left]
                cnt[y] -= 1
                pairs -= cnt[y]
                left += 1
            ans += left
        return ans

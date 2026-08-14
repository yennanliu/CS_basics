"""

1983. Widest Pair of Indices With Equal Range Sum
Medium

You are given two 0-indexed binary arrays nums1 and nums2. Find the widest pair of indices (i, j) such that i <= j and nums1[i] + nums1[i+1] + ... + nums1[j] == nums2[i] + nums2[i+1] + ... + nums2[j].

The widest pair of indices is the pair with the largest distance between i and j. The distance between a pair of indices is defined as j - i + 1.

Return the distance of the widest pair of indices. If no pair of indices meets the conditions, return 0.


Example 1:

Input: nums1 = [1,1,0,1], nums2 = [0,1,1,0]
Output: 3
Explanation:
If i = 1 and j = 3:
nums1[1] + nums1[2] + nums1[3] = 1 + 0 + 1 = 2.
nums2[1] + nums2[2] + nums2[3] = 1 + 1 + 0 = 2.
The distance between i and j is j - i + 1 = 3 - 1 + 1 = 3.

Example 2:

Input: nums1 = [0,1], nums2 = [1,1]
Output: 1
Explanation:
If i = 1 and j = 1:
nums1[1] = 1.
nums2[1] = 1.
The distance between i and j is j - i + 1 = 1 - 1 + 1 = 1.

Example 3:

Input: nums1 = [0], nums2 = [1]
Output: 0
Explanation:
There are no pairs of indices that meet the requirements.


Constraints:

n == nums1.length == nums2.length
1 <= n <= 10^5
nums1[i] is either 0 or 1.
nums2[i] is either 0 or 1.

"""

# V0
# IDEA : PREFIX SUM OF THE DIFFERENCE + HASH TABLE (longest zero-sum subarray)
#
#   equal range sums  <=>  sum of (nums1[t] - nums2[t]) over [i..j] == 0,
#   so build diff = nums1 - nums2 and look for the LONGEST zero-sum subarray.
#
#   with running prefix s, a zero-sum window is any pair of indices sharing the
#   same s -> remember only the FIRST index for each prefix value (that gives
#   the widest window) in a hash table seeded with {0: -1}.
#
#   NOTE : the seed {0: -1} is what lets a window starting at index 0 count.
#
# time = O(n), space = O(n)
class Solution(object):
    def widestPairOfIndices(self, nums1, nums2):
        first = {0: -1}
        res = 0
        s = 0
        for i in range(len(nums1)):
            s += nums1[i] - nums2[i]
            if s in first:
                res = max(res, i - first[s])
            else:
                first[s] = i
        return res

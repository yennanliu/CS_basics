"""

219. Contains Duplicate II
Easy

Given an integer array nums and an integer k, return true if there are two
distinct indices i and j in the array such that nums[i] == nums[j] and
abs(i - j) <= k.

Example 1:

Input: nums = [1,2,3,1], k = 3
Output: true

Example 2:

Input: nums = [1,0,1,1], k = 1
Output: true

Example 3:

Input: nums = [1,2,3,1,2,3], k = 2
Output: false

Constraints:

1 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9
0 <= k <= 10^5

"""

# V0
# IDEA : HASH MAP  value -> LAST INDEX IT WAS SEEN AT
#
#   scanning left to right, the closest earlier copy of nums[i] is always the
#   LAST one seen, so it is the only one worth comparing against : if even the
#   nearest copy is more than k away, every older copy is further still.
#   so keep one index per value, and overwrite it on every sighting.
#
#   e.g. nums = [1,0,1,1], k = 1
#        i=2 : last[1] = 0 -> 2 - 0 = 2 > 1 -> no, last[1] = 2
#        i=3 : last[1] = 2 -> 3 - 2 = 1 <= 1 -> True
#
#   (brute force : compare every pair within distance k -> O(n * k), too slow)
#
# time = O(n), space = O(n)
class Solution(object):
    def containsNearbyDuplicate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        # value -> the most recent index it appeared at
        last_index = {}

        for i in range(len(nums)):
            value = nums[i]
            if value in last_index and i - last_index[value] <= k:
                return True
            # NOTE !!! overwrite even when the old copy was too far : the new
            #          index is closer to every later position
            last_index[value] = i

        return False


# V1
# IDEA : SLIDING WINDOW SET OF THE LAST k VALUES
#
#   a duplicate within distance k exists exactly when nums[i] already appears
#   among the k elements before it, nums[i-k .. i-1]. keep those k values in a
#   set; before looking at index i, evict nums[i-k-1], the one that just fell
#   out of range. any duplicate inside the window answers True immediately, so
#   the set never needs to hold two copies of a value.
#
#   use this when memory matters : the set holds at most k values, not n.
#
# time = O(n), space = O(min(n, k))
class Solution2(object):
    def containsNearbyDuplicate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        # the values at indices i-k .. i-1
        window = set()

        for i in range(len(nums)):
            # nums[i-k-1] is now more than k away from i : drop it
            if i > k:
                window.remove(nums[i - k - 1])

            if nums[i] in window:
                return True
            window.add(nums[i])

        return False

# V0 

# V1 
# https://www.hrwhisper.me/leetcode-contains-duplicate-i-ii-iii/
class Solution(object):
    def containsNearbyAlmostDuplicate(self, nums, k, t):
        """
        :type nums: List[int]
        :type k: int
        :type t: int
        :rtype: bool
        """
        if k <= 0 or t < 0: return False
        key_to_val = {}
        for i, num in enumerate(nums):
            key = num // (t + 1)
            if key in key_to_val \
                    or key + 1 in key_to_val and key_to_val[key + 1] - num <= t \
                    or key - 1 in key_to_val and num - key_to_val[key - 1] <= t:
                return True
            if i >=k:
                del key_to_val[nums[i-k] // (t + 1)]
            key_to_val[key] = num
        return False
        
# V2 
# Time:  O(n * t)
# Space: O(max(k, t))
import collections
class Solution(object):
    # @param {integer[]} nums
    # @param {integer} k
    # @param {integer} t
    # @return {boolean}
    def containsNearbyAlmostDuplicate(self, nums, k, t):
        if k < 0 or t < 0:
            return False
        window = collections.OrderedDict()
        for n in nums:
            # Make sure window size
            if len(window) > k:
                window.popitem(False)

            bucket = n if not t else n // t
            # At most 2t items.
            for m in (window.get(bucket - 1), window.get(bucket), window.get(bucket + 1)):
                if m is not None and abs(n - m) <= t:
                    return True
            window[bucket] = n
        return False
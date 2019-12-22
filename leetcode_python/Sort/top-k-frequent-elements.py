# V0
import collections
class Solution(object):
    def topKFrequent(self, nums, k):
        freq_dict = dict(collections.Counter(nums))
        # python sort a dict by value 
        # https://stackoverflow.com/questions/613183/how-do-i-sort-a-dictionary-by-value
        return [ x[0] for  x in sorted(list(freq_dict.items()), key=lambda x: x[1],reverse=True) ][:k]

# V1 
import collections
class Solution(object):
    def topKFrequent(self, nums, k):
        freq_dict = dict(collections.Counter(nums))
        # python sort a dict by value 
        # https://stackoverflow.com/questions/613183/how-do-i-sort-a-dictionary-by-value
        return [ x[0] for  x in sorted(list(freq_dict.items()), key=lambda x: x[1],reverse=True) ][:k]

# V1'
# https://www.jiuzhang.com/solution/top-k-frequent-elements/#tag-highlight-lang-python
class Solution:
    def topKFrequent(self, nums, k):
        # 统计元素的频率
        freq_dict = dict()
        for num in nums:
            freq_dict[num] = freq_dict.get(num, 0) + 1
            
        freq_dict_sorted = sorted(freq_dict.items(), key=lambda x: x[1], reverse=True)
        
        ret = list()
        for i in range(k):
            ret.append(freq_dict_sorted[i][0])
        return ret

# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def topKFrequent(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        counts = collections.Counter(nums)
        buckets = [[] for _ in range(len(nums)+1)]
        for i, count in counts.items():
            buckets[count].append(i)

        result = []
        for i in reversed(range(len(buckets))):
            for j in range(len(buckets[i])):
                result.append(buckets[i][j])
                if len(result) == k:
                    return result
        return result

# V3 
# Time:  O(n) ~ O(n^2), O(n) on average.
# Space: O(n)
# Quick Select Solution
from random import randint
class Solution2(object):
    def topKFrequent(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        counts = collections.Counter(nums)
        p = []
        for key, val in counts.items():
            p.append((-val, key))
        self.kthElement(p, k)

        result = []
        for i in range(k):
            result.append(p[i][1])
        return result

    def kthElement(self, nums, k):
        def PartitionAroundPivot(left, right, pivot_idx, nums):
            pivot_value = nums[pivot_idx]
            new_pivot_idx = left
            nums[pivot_idx], nums[right] = nums[right], nums[pivot_idx]
            for i in range(left, right):
                if nums[i] < pivot_value:
                    nums[i], nums[new_pivot_idx] = nums[new_pivot_idx], nums[i]
                    new_pivot_idx += 1

            nums[right], nums[new_pivot_idx] = nums[new_pivot_idx], nums[right]
            return new_pivot_idx

        left, right = 0, len(nums) - 1
        while left <= right:
            pivot_idx = randint(left, right)
            new_pivot_idx = PartitionAroundPivot(left, right, pivot_idx, nums)
            if new_pivot_idx == k - 1:
                return
            elif new_pivot_idx > k - 1:
                right = new_pivot_idx - 1
            else:  # new_pivot_idx < k - 1.
                left = new_pivot_idx + 1

# V4 
# Time:  O(nlogk)
# Space: O(n)
class Solution3(object):
    def topKFrequent(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        return [key for key, _ in collections.Counter(nums).most_common(k)]
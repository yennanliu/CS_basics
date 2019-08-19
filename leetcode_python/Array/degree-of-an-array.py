# V0 

# V1 
# http://bookshadow.com/weblog/2017/10/15/leetcode-degree-of-an-array/
import collections
class Solution(object):
    def findShortestSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        mins = {}
        maxs = {}
        cnts = collections.defaultdict(int)
        for idx, num in enumerate(nums):
            maxs[num] = max(maxs.get(num, -1), idx)
            mins[num] = min(mins.get(num, 0x7FFFFFFF), idx)
            cnts[num] += 1
        degree = max(cnts.values())
        ans = len(nums)
        for num in set(nums):
            if cnts[num] == degree:
                ans = min(ans, maxs[num] - mins[num] + 1)
        return ans

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79146067
import collections
class Solution(object):
    def findShortestSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums) == len(set(nums)):
            return 1
        counter = collections.Counter(nums)
        degree_num = counter.most_common(1)[0]
        most_numbers = [num for num in counter if counter[num] == degree_num[1]]
        scale = 100000000
        for most_number in most_numbers:
            appear = [i for i,num in enumerate(nums) if num == most_number]
            appear_scale = max(appear) - min(appear) + 1
            if appear_scale < scale:
                scale = appear_scale
        return scale

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79146067
# IDEA : STACK 
import collections, heapq
class Solution:
    def findShortestSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        count = collections.defaultdict(tuple)
        for i, num in enumerate(nums):
            if num not in count:
                count[num] = (1, i, i)
            else:
                count[num] = (count[num][0] + 1, count[num][1], i)
        heap = [(-times, end - start + 1) for times, start, end in count.values()]
        heapq.heapify(heap)
        return heapq.heappop(heap)[1]

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/79146067
# IDEA : DICT 
import collections
class Solution:
    def findShortestSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        left, right = dict(), dict()
        count = collections.defaultdict(int)
        for i, num in enumerate(nums):
            if num not in left:
                left[num] = i
            right[num] = i
            count[num] += 1
        degree = max(count.values())
        res = float("inf")
        for num, c in count.items():
            if c == degree:
                res = min(res, right[num] - left[num] + 1)
        return res

# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def findShortestSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        counts = collections.Counter(nums)
        left, right = {}, {}
        for i, num in enumerate(nums):
            left.setdefault(num, i)
            right[num] = i
        degree = max(counts.values())
        return min(right[num]-left[num]+1 \
                   for num in counts.keys() \
                   if counts[num] == degree)

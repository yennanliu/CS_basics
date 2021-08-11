"""

Given an integer array nums with possible duplicates, randomly output the index of a given target number. You can assume that the given target number must exist in the array.

Implement the Solution class:

Solution(int[] nums) Initializes the object with the array nums.
int pick(int target) Picks a random index i from nums where nums[i] == target. If there are multiple valid i's, then each index should have an equal probability of returning.
 

Example 1:

Input
["Solution", "pick", "pick", "pick"]
[[[1, 2, 3, 3, 3]], [3], [1], [3]]
Output
[null, 4, 0, 2]

Explanation
Solution solution = new Solution([1, 2, 3, 3, 3]);
solution.pick(3); // It should return either index 2, 3, or 4 randomly. Each index should have equal probability of returning.
solution.pick(1); // It should return 0. Since in the array only nums[0] is equal to 1.
solution.pick(3); // It should return either index 2, 3, or 4 randomly. Each index should have equal probability of returning.
 

Constraints:

1 <= nums.length <= 2 * 104
-231 <= nums[i] <= 231 - 1
target is an integer from nums.
At most 104 calls will be made to pick.

"""

# V0
class Solution(object):

    def __init__(self, nums):
        self.nums = nums

    def pick(self, target):
        idxs = []
        for i, num in enumerate(self.nums):
            if num == target:
                idxs.append(i)
        return idxs[random.randint(0, len(idxs) - 1)]

# V0'
from random import randint
import random 
class Solution(object):

    def __init__(self, nums):
        
        self.__nums = nums

    def pick(self, target):
        indexes = [ k for k, v in enumerate(self.__nums) if v == target ]
        return indexes[randint(0, len(indexes)-1)]

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79540576
class Solution(object):

    def __init__(self, nums):
        """
        
        :type nums: List[int]
        :type numsSize: int
        """
        self.nums = nums

    def pick(self, target):
        """
        :type target: int
        :rtype: int
        """
        idxs = []
        for i, num in enumerate(self.nums):
            if num == target:
                idxs.append(i)
        return idxs[random.randint(0, len(idxs) - 1)]
# Your Solution object will be instantiated and called as such:
# obj = Solution(nums)
# param_1 = obj.pick(target)

# V1'
# http://bookshadow.com/weblog/2016/09/11/leetcode-random-pick-index/
class Solution(object):

    def __init__(self, nums):
        """
        
        :type nums: List[int]
        :type numsSize: int
        """
        size = len(nums)
        self.next = [0] * (size + 1)
        self.head = collections.defaultdict(int)
        for i, n in enumerate(nums):
            self.next[i + 1] = self.head[n]
            self.head[n] = i + 1

    def pick(self, target):
        """
        :type target: int
        :rtype: int
        """
        cnt = 0
        idx = self.head[target]
        while idx > 0:
            cnt += 1
            idx = self.next[idx]
        c = int(random.random() * cnt)
        idx = self.head[target]
        for x in range(c):
            idx = self.next[idx]
        return idx - 1

# V1''
from random import randint
import random 
class Solution(object):

	def __init__(self, nums):
		"""
		:type nums: List[int]
		:type numsSize: int
		"""
		self.__nums = nums

	def pick(self, target):
		# if len(self.__nums) == 1 or self.__nums.count(target) == 1:
		# 	return 0
		# else:
		indexes = [ k for k, v in enumerate(self.__nums) if v == target ]
		# print ('self.__nums :', self.__nums)
		# print ('indexes :', indexes)
		# print ('randint(0, len(indexes)) :', randint(0, len(indexes)))
		return indexes[randint(0, len(indexes)-1)]
         
# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79540576
class Solution(object):

    def __init__(self, nums):
        """
        
        :type nums: List[int]
        :type numsSize: int
        """
        self.nums = nums

    def pick(self, target):
        """
        :type target: int
        :rtype: int
        """
        idxs = []
        for i, num in enumerate(self.nums):
            if num == target:
                idxs.append(i)
        return idxs[random.randint(0, len(idxs) - 1)]

# V3
# Time:  O(n)
# Space: O(1)
from random import randint
class Solution(object):

    def __init__(self, nums):
        """
        :type nums: List[int]
        :type numsSize: int
        """
        self.__nums = nums

    def pick(self, target):
        """
        :type target: int
        :rtype: int
        """
        reservoir = -1
        n = 0
        for i in range(len(self.__nums)):
            if self.__nums[i] != target:
                continue
            reservoir = i if randint(1, n+1) == 1 else reservoir
            n += 1
        return reservoir
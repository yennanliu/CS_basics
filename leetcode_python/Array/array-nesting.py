# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79460546
class Solution(object):
    def arrayNesting(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        visited = [False] * len(nums)
        ans = 0
        for i in range(len(nums)):
            road = 0
            while not visited[i]:
                road += 1
                # order of below 2 lines of code is unchangeable
                visited[i] = True
                i = nums[i]
            ans = max(ans, road)
        return ans

# V1'
# http://bookshadow.com/weblog/2017/05/28/leetcode-array-nesting/
class Solution(object):
    def arrayNesting(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        def search(idx):
            cnt = 0
            while nums[idx] >= 0:
                cnt += 1
                next = nums[idx]
                nums[idx] = -1
                idx = next
            return cnt
        ans = 0
        for x in range(len(nums)):
            if nums[x] >= 0:
                ans = max(ans, search(x))
        return ans

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def arrayNesting(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result = 0
        for num in nums:
            if num is not None:
                start, count = num, 0
                while nums[start] is not None:
                    temp = start
                    start = nums[start]
                    nums[temp] = None
                    count += 1
                result = max(result, count)
        return result
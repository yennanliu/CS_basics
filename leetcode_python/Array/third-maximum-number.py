"""
Given a non-empty array of integers, return the third maximum number in this array. If it does not exist, return the maximum number. The time complexity must be in O(n).

Example 1:
Input: [3, 2, 1]

Output: 1

Explanation: The third maximum is 1.
Example 2:
Input: [1, 2]

Output: 2

Explanation: The third maximum does not exist, so the maximum (2) is returned instead.
Example 3:
Input: [2, 2, 3, 1]

Output: 1

Explanation: Note that the third maximum here means the third maximum distinct number.
Both numbers with value 2 are both considered as second maximum.
"""

# V0 

# V1 
# http://bookshadow.com/weblog/2016/10/09/leetcode-third-maximum-number/
# IDEA : 3 VARIABLES 
class Solution(object):
    def thirdMax(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        a = b = c = None
        for n in nums:
            if n > a:
                a, b, c = n, a, b
            elif a > n > b:
                b, c = n, b
            elif b > n > c:
                c = n
        return c if c is not None else a

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79255652
# IDEA : REPLACE MAX 
class Solution(object):
    def thirdMax(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        def setMax(nums):
            _max = max(nums)
            for i, num in enumerate(nums):
                if num == _max:
                    nums[i] = float('-inf')
            return _max
        max1 = setMax(nums)
        max2 = setMax(nums)
        max3 = setMax(nums)
        return max3 if max3 != float('-inf') else max(max1, max2)

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79255652
# IDEA : SET 
class Solution(object):
    def thirdMax(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        nums_set = set(nums)
        if len(nums_set) < 3:
            return max(nums_set)
        nums_set.remove(max(nums_set))
        nums_set.remove(max(nums_set))
        _max = max(nums_set)
        return _max
        
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def thirdMax(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        count = 0
        top = [float("-inf")] * 3
        for num in nums:
            if num > top[0]:
                top[0], top[1], top[2] = num, top[0], top[1]
                count += 1
            elif num != top[0] and num > top[1]:
                top[1], top[2] = num, top[1]
                count += 1
            elif num != top[0] and num != top[1] and num >= top[2]:
                top[2] = num
                count += 1

        if count < 3:
            return top[0]
        return top[2]
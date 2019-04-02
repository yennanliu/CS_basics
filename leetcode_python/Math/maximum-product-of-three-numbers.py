# V1 
# idea : for getting the max product of three number from the given array : 
# case 1 : all 3 integers are positive  (1st, 2rd, and 3rd big)
# case 2 : 1 biggest positive integer, and 1st small and 2rd small negative integers 
class Solution(object):
	def maximumProduct(self, nums):
		sorted_nums = sorted(nums)
		nums_case1 = sorted_nums[-3:]
		nums_case2 = sorted_nums[:2] + sorted_nums[-1:]
		max_case1, max_case2 = 1,1 
		for i in nums_case1:
			max_case1 = max_case1*i
		for j in nums_case2:
			max_case2 = max_case2*j
		#print (max_case1, max_case2)
		return max(max_case1,max_case2)


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79169635
# https://blog.csdn.net/Chris_zhangrx/article/details/78975308
class Solution(object):
    def maximumProduct(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        nums.sort()
        right = nums[-3] * nums[-2] * nums[-1]
        left = nums[0] * nums[1] * nums[-1]
        return max(left, right)

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def maximumProduct(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        min1, min2 = float("inf"), float("inf")
        max1, max2, max3 = float("-inf"), float("-inf"), float("-inf")

        for n in nums:
            if n <= min1:
                min2 = min1
                min1 = n
            elif n <= min2:
                min2 = n

            if n >= max1:
                max3 = max2
                max2 = max1
                max1 = n
            elif n >= max2:
                max3 = max2
                max2 = n
            elif n >= max3:
                max3 = n

        return max(min1 * min2 * max1, max1 * max2 * max3)


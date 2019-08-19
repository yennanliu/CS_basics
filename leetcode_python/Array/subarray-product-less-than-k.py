# V0 

# V1
# https://www.jiuzhang.com/solution/subarray-product-less-than-k/#tag-highlight-lang-python
# IDEA : SLIDING WINDOW  
# MAINTAIN 2 INDEX : left, i, SO THE SLIDING WINDOW IS : [left, i]
# CHECK IF THE PRODUCT OF ALL DIGITS IN THE WINDOW [left, i] < k
# IF NOT, REMOVE CURRENT LEFT, AND DO LEFT ++
# REPEAT ABOVE PROCESS AND GO THOROUGH ALL ARRAY  
class Solution:
    def numSubarrayProductLessThanK(self, nums, k):
        
        product, i, result = 1, 0, 0
        
        for j, num in enumerate(nums):
            
            product *= num 
            
            while i <= j and product >= k:
                product //= nums[i]       # divided the number back, since this number already make the product > k 
                i += 1 
                
            result += j - i + 1 
            
        return result

# V1'
# http://bookshadow.com/weblog/2017/10/22/leetcode-subarray-product-less-than-k/
class Solution(object):
    def numSubarrayProductLessThanK(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        product = 1
        ll = rr = -1
        ans = 0
        for num in nums:
            rr += 1
            product *= num
            while ll + 1 <= rr and product >= k:
                product /= nums[ll + 1]
                ll += 1
            ans += rr - ll
        return ans

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def numSubarrayProductLessThanK(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        if k <= 1: return 0
        result, start, prod = 0, 0, 1
        for i, num in enumerate(nums):
            prod *= num
            while prod >= k:
                prod /= nums[start]
                start += 1
            result += i-start+1
        return result
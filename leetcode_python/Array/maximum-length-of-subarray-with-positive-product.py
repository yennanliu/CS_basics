"""

1567. Maximum Length of Subarray With Positive Product
Medium

Given an array of integers nums, find the maximum length of a subarray where the product of all its elements is positive.

A subarray of an array is a consecutive sequence of zero or more values taken out of that array.

Return the maximum length of a subarray with positive product.

 

Example 1:

Input: nums = [1,-2,-3,4]
Output: 4
Explanation: The array nums already has a positive product of 24.
Example 2:

Input: nums = [0,1,-2,-3,-4]
Output: 3
Explanation: The longest subarray with positive product is [1,-2,-3] which has a product of 6.
Notice that we cannot include 0 in the subarray since that'll make the product 0 which is not positive.
Example 3:

Input: nums = [-1,-2,-3,0,1]
Output: 2
Explanation: The longest subarray with positive product is [-1,-2] or [-2,-3].
 

Constraints:

1 <= nums.length <= 105
-109 <= nums[i] <= 109

"""

# V0
class Solution:
    def getMaxLen(self, nums):
        first_neg, zero = None, -1
        mx = neg = 0
        for i,v in enumerate(nums):
            if v == 0:
                first_neg, zero, neg = None, i, 0
                continue
            if v < 0:
                neg += 1
                if first_neg == None:
                    first_neg = i
            j = zero if not neg % 2 else first_neg if first_neg != None else 10**9
            mx = max(mx, i-j)
        return mx

# V0'
# IDEA : 2 POINTERS
class Solution:
    def getMaxLen(self, nums):
        res = 0
        k = -1 # most recent 0
        j = -1 # first negative after most recent 0
        cnt = 0 # count of negatives after most recent 0
        for i, n in enumerate(nums):
            if n == 0:
                k = i
                j = i
                cnt = 0
            elif n < 0:
                cnt += 1
                if cnt % 2 == 0:
                    res = max(res, i - k)
                else:
                    if cnt == 1:
                        j = i
                    else:
                        res = max(res, i - j)        
            else:
                if cnt % 2 == 0:
                    res = max(res, i - k)
                else:
                    res = max(res, i - j)
        return res

# V1
# IDEA : arrauy op
# https://leetcode.com/problems/maximum-length-of-subarray-with-positive-product/discuss/819419/Python-simple-and-short
# IDEA :
# For each element in the array, I find the earliest element we could stretch back to to 
# make the positive product. first_neg is the index of earliest negative 
# we've seen since seeing a zero. zero is the index of the last zero we saw. 
# neg is the number of negs seen since seeing zero. This is all the info we need to compute the max possible at any ending index i. 
# So we loop through all possible ends, and keep this data updated.
class Solution:
    def getMaxLen(self, nums):
        first_neg, zero = None, -1
        mx = neg = 0
        for i,v in enumerate(nums):
            if v == 0:
                first_neg, zero, neg = None, i, 0
                continue
            if v < 0:
                neg += 1
                if first_neg == None:
                    first_neg = i
            j = zero if not neg % 2 else first_neg if first_neg != None else 10**9
            mx = max(mx, i-j)
        return mx

# V1
# IDEA : ARRAY OP
# https://www.codeleading.com/article/43895450358/
# pos_len : len of sub array with prodcut > 0
# neg_len : len of sub array with prodcut < 0
class Solution:
    def getMaxLen(self, nums: List[int]) -> int:
        pos_len=0
        neg_len=0
        res=0
        for num in nums:
            if(num==0):
                pos_len,neg_len=0,0
            elif(num<0):
                pos_len,neg_len=neg_len,pos_len
                neg_len+=1
                if(pos_len>0):
                    pos_len+=1
            else:
                pos_len+=1
                if(neg_len>0):
                    neg_len+=1
            res=max(res,pos_len)
        return res

# V1
# IDEA : 2 POINTERS
class Solution:
    def getMaxLen(self, nums):
        res = 0
        k = -1 # most recent 0
        j = -1 # first negative after most recent 0
        cnt = 0 # count of negatives after most recent 0
        for i, n in enumerate(nums):
            if n == 0:
                k = i
                j = i
                cnt = 0
            elif n < 0:
                cnt += 1
                if cnt % 2 == 0:
                    res = max(res, i - k)
                else:
                    if cnt == 1:
                        j = i
                    else:
                        res = max(res, i - j)        
            else:
                if cnt % 2 == 0:
                    res = max(res, i - k)
                else:
                    res = max(res, i - j)
        return res

# V1
# IDEA : GREEDY
# https://leetcode.com/problems/maximum-length-of-subarray-with-positive-product/discuss/839439/Python-Greedy-Solution
class Solution:
    def getMaxLen(self, nums: List[int]) -> int:
        
        def helper(nums):
            result = 0
            positiveSoFar = 0
            negativeSoFar = 0

            for i in range(len(nums)):
                if nums[i] == 0:
                    positiveSoFar = 0
                    negativeSoFar = 0
                elif nums[i] > 0 :
                    positiveSoFar += 1
                    if negativeSoFar > 0:
                        negativeSoFar += 1

                elif nums[i] < 0:
                    if negativeSoFar > 0:
                        positiveSoFar = max(negativeSoFar, positiveSoFar) +1
                        negativeSoFar = 0
                    else:
                        negativeSoFar = positiveSoFar + 1
                        positiveSoFar = 0

                result = max(result, positiveSoFar)
            return result
        
        # scan from left and scan from right
        return max(helper(nums), helper(nums[::-1]))

# V1
# https://leetcode.com/problems/maximum-length-of-subarray-with-positive-product/discuss/821358/python
class Solution(object):
    def getMaxLen(self, nums):
        def func(nums):
            positive = 0
            negative = 0
            sign = 0
            res = 0
            for n in nums:
                if n > 0:
                    positive += 1
                    negative += 1
                if n < 0:
                    sign += 1
                    positive = 0
                    negative += 1
                    if sign % 2 == 0:
                        positive = negative
                if n == 0:
                    positive = 0
                    negative = 0
                    sign = 0
                res = max(res, positive)
            return res
        return max(func(nums), func(nums[::-1]))

# V1
# IDEA : DP
# https://leetcode.com/problems/maximum-length-of-subarray-with-positive-product/discuss/819279/Python-Easy-python-dp-solution
class Solution:
    def getMaxLen(self, nums: List[int]) -> int:
        pos, neg = 0, 0
        res = 0
        for n in nums:
            if n==0:
                neg, pos = 0, 0
            elif n>0:
                if neg>0: neg, pos = neg+1, pos+1
                else: neg, pos = 0, pos+1
            else:
                if neg>0: pos, neg = neg+1, pos+1
                else: neg, pos = pos+1, 0
            res = max(res,pos)
        return res
# V2
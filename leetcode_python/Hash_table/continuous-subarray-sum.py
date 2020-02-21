# V0 
# IDEA : HASH TABLE
# DEMO
#    ...: nums = [23,2,4,6,7]
#    ...: k = 6 
#    ...: s = Solution()
#    ...: r = s.checkSubarraySum(nums,k)
#    ...: print (r)
#    ...: 
# num  =  23 lookup : {0: -1}
# num  =  2 lookup : {0: -1, 5: 0}
# num  =  4 lookup : {0: -1, 5: 0, 1: 1}
# True
class Solution(object):
    def checkSubarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        count = 0
        lookup = {0: -1}                   # init the lookup hash table 
        for i, num in enumerate(nums):
            count += num                   # keep adding num up  (as count)
            if k:
                # beware of it
                count %= k                 # get mode of count by k, since it's as same as num when check if it's k's multiplier
            """
            ###  beware of it 
            via the "count in lookup" trick, we can compare with the 
            "whole sub-array sum with different start point" in the array 
            """
            if count in lookup:
                # beware of it
                if i - lookup[count] > 1:  # if there is any element in the nums that can sum up as k's multiplier and the length of this sub array is at least 2 (>1)
                    return True
            else:
                lookup[count] = i          # get the index of each sum 

        return False

# V1 
# http://bookshadow.com/weblog/2017/02/26/leetcode-continuous-subarray-sum/
class Solution(object):
    def checkSubarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        dmap = {0 : -1}
        total = 0
        for i, n in enumerate(nums):
            total += n
            m = total % k if k else total
            if m not in dmap: dmap[m] = i
            elif dmap[m] + 1 < i: return True
        return False

# V1'
# https://www.jiuzhang.com/solution/continuous-subarray-sum/#tag-highlight-lang-python
class Solution:
    # @param {int[]} A an integer array
    # @return {int[]}  A list of integers includes the index of the 
    #                  first number and the index of the last number
    def continuousSubarraySum(self, A):
        ans = -0x7fffffff
        sum = 0
        start, end = 0, -1
        result = [-1, -1]
        for x in A:
            if sum < 0:
                sum = x
                start = end + 1
                end = start
            else:
                sum += x
                end += 1
            if sum > ans:
                ans = sum
                result = [start, end]
        return result

# V2 
# Time:  O(n)
# Space: O(k)
class Solution(object):
    def checkSubarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        count = 0
        lookup = {0: -1}
        for i, num in enumerate(nums):
            count += num
            if k:
                count %= k
            if count in lookup:
                if i - lookup[count] > 1:
                    return True
            else:
                lookup[count] = i
        return False
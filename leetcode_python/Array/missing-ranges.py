"""

LeetCode 163. Missing Ranges

# https://www.goodtecher.com/leetcode-163-missing-ranges/

Description
https://leetcode.com/problems/missing-ranges/

You are given an inclusive range [lower, upper] and a sorted unique integer array nums, where all elements are in the inclusive range.

A number x is considered missing if x is in the range [lower, upper] and x is not in nums.

Return the smallest sorted list of ranges that cover every missing number exactly. That is, no element of nums is in any of the ranges, and each missing number is in one of the ranges.

Each range [a,b] in the list should be output as:

"a->b" if a != b
"a" if a == b
Example 1:

Input: nums = [0,1,3,50,75], lower = 0, upper = 99
Output: ["2","4->49","51->74","76->99"]
Explanation: The ranges are:
[2,2] --> "2"
[4,49] --> "4->49"
[51,74] --> "51->74"
[76,99] --> "76->99"
Example 2:

Input: nums = [], lower = 1, upper = 1
Output: ["1"]
Explanation: The only missing range is [1,1], which becomes "1".
Example 3:

Input: nums = [], lower = -3, upper = -1
Output: ["-3->-1"]
Explanation: The only missing range is [-3,-1], which becomes "-3->-1".
Example 4:

Input: nums = [-1], lower = -1, upper = -1
Output: []
Explanation: There are no missing ranges since there are no missing numbers.
Example 5:

Input: nums = [-1], lower = -2, upper = -1
Output: ["-2"]
Constraints:

-109 <= lower <= upper <= 109
0 <= nums.length <= 100
lower <= nums[i] <= upper
All the values of nums are unique.

"""
# V0

# V1 
# https://blog.csdn.net/qq_32424059/article/details/94437790
# IDEA : DOUBLE POINTERS
class Solution(object):
    def findMissingRanges(self, nums, lower, upper):
        start, end = lower, lower
        res = []
        for i in range(len(nums)):
            if nums[i] == end: # if NO missing interval 
                start, end = nums[i] + 1, nums[i] + 1
                
            elif nums[i] > end: # if there missing interval 
                end = max(end, nums[i] - 1)
                
                if end != start:
                    res.append(str(start) + "->" + str(end))
                else:
                    res.append(str(start))
                    
                start, end = nums[i] + 1, nums[i] + 1
                
        if start < upper: # deal with the remaining part 
            res.append(str(start) + "->" + str(upper))
        elif start == upper:
            res.append(str(start))
            
        return res   

# V1'
# https://github.com/qiyuangong/leetcode/blob/master/python/163_Missing_Ranges.py
class Solution(object):
    def findMissingRanges(self, nums, lower, upper):
        """
        :type nums: List[int]
        :type lower: int
        :type upper: int
        :rtype: List[str]
        """
        ranges = []
        prev = lower - 1
        for i in range(len(nums) + 1):
            if i == len(nums):
                curr = upper + 1
            else:
                curr = nums[i]
            if curr - prev > 2:
                ranges.append("%d->%d" % (prev + 1, curr - 1))
            elif curr - prev == 2:
                ranges.append("%d" % (prev + 1))
            prev = curr
        return ranges

# V1''
# Missing Ranges - Leetcode Challenge - Python Solution - Poopcode
class Solution(object):
    def findMissingRanges(self, nums, lower, upper):
        """
        :type nums: List[int]
        :type lower: int
        :type upper: int
        :rtype: List[str]
        """
        ranges = []
        prev = lower - 1
        for i in range(len(nums) + 1):
            if i == len(nums):
                curr = upper + 1
            else:
                curr = nums[i]
            if curr - prev > 2:
                ranges.append("%d->%d" % (prev + 1, curr - 1))
            elif curr - prev == 2:
                ranges.append("%d" % (prev + 1))
            prev = curr
        return ranges

# V1'''
# https://www.goodtecher.com/leetcode-163-missing-ranges/
class Solution:
    def findMissingRanges(self, nums, lower, upper):
        results = []
        
        if not nums:
            gap = self.helper(lower, upper)
            results.append(gap)
            
            return results
        
        prev = lower - 1
        
        for num in nums:
            if prev + 1 != num:                
                gap = self.helper(prev + 1, num - 1)
                results.append(gap)
            prev = num    
        
        if nums[-1] < upper:
            gap = self.helper(nums[-1] + 1, upper)
            results.append(gap)
                    
        return results
    
    def helper(self, left, right):
        if left == right:
            return str(left)
        
        return str(left) + "->" + str(right)
        
# V1'
# https://www.cnblogs.com/grandyang/p/5184890.html
# IDEA : C++ 
# class Solution {
# public:
#     vector<string> findMissingRanges(vector<int>& nums, int lower, int upper) {
#         vector<string> res;
#         for (int num : nums) {
#             if (num > lower) res.push_back(to_string(lower) + (num - 1 > lower ? ("->" + to_string(num - 1)) : ""));
#             if (num == upper) return res;
#             lower = num + 1;
#         }
#         if (lower <= upper) res.push_back(to_string(lower) + (upper > lower ? ("->" + to_string(upper)) : ""));
#         return res;
#     }
# };

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findMissingRanges(self, nums, lower, upper):
        def getRange(lower, upper):
            if lower == upper:
                return "{}".format(lower)
            else:
                return "{}->{}".format(lower, upper)
        ranges = []
        pre = lower - 1
        for i in range(len(nums) + 1):
            if i == len(nums):
                cur = upper + 1
            else:
                cur = nums[i]
            if cur - pre >= 2:
                ranges.append(getRange(pre + 1, cur - 1))
            pre = cur
        return ranges
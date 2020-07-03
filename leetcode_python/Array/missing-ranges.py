# Given a sorted integer array where the range of elements are in the inclusive range [lower, upper], return its missing ranges.
# For example, given [0, 1, 3, 50, 75], lower = 0 and upper = 99, return ["2", "4->49", "51->74", "76->99"].

# V0

# V1 
# https://blog.csdn.net/qq_32424059/article/details/94437790
# IDEA : DOUBLE POINTERS
class Solution(object):
    def findMissingRanges(self, nums, lower, upper):
        """
        :type nums: List[int]
        :type lower: int
        :type upper: int
        :rtype: List[str]
        """
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
        """
        :type nums: List[int]
        :type lower: int
        :type upper: int
        :rtype: List[str]
        """
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
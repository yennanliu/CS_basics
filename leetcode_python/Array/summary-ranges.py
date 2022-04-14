"""

228. Summary Ranges
Easy

You are given a sorted unique integer array nums.

A range [a,b] is the set of all integers from a to b (inclusive).

Return the smallest sorted list of ranges that cover all the numbers in the array exactly. That is, each element of nums is covered by exactly one of the ranges, and there is no integer x such that x is in one of the ranges but not in nums.

Each range [a,b] in the list should be output as:

"a->b" if a != b
"a" if a == b
 

Example 1:

Input: nums = [0,1,2,4,5,7]
Output: ["0->2","4->5","7"]
Explanation: The ranges are:
[0,2] --> "0->2"
[4,5] --> "4->5"
[7,7] --> "7"
Example 2:

Input: nums = [0,2,3,4,6,8,9]
Output: ["0","2->4","6","8->9"]
Explanation: The ranges are:
[0,0] --> "0"
[2,4] --> "2->4"
[6,6] --> "6"
[8,9] --> "8->9"
 

Constraints:

0 <= nums.length <= 20
-231 <= nums[i] <= 231 - 1
All the values of nums are unique.
nums is sorted in ascending order.

"""

# V0
class Solution:
    def summaryRanges(self, nums):
        if not nums:
            return []
        # deal with edge case
        nums = nums + [nums[-1]+2]
        res = []
        # set head = nums[0]
        head = nums[0]
        # start from index = 1 
        for i in range(1,len(nums)):
            if nums[i] - nums[i-1] > 1:
                if head == nums[i-1]:
                    res.append(str(head))
                else:
                    res.append(str(head) + "->" + str(nums[i-1]))
                head = nums[i]
        return res

# V0'
class Solution:
    def summaryRanges(self, nums):
        start, end, res = -2**31, -2**31, []
        for i in nums+[2**31-1]:
            if i - end == 1:
                end = i
            else:
                res += [str(start)] if start == end else [str(start) + "->" + str(end)]
                start = end = i
        return res[1:]

# V1 
# http://bookshadow.com/weblog/2015/06/26/leetcode-summary-ranges/
class Solution:
    # @param {integer[]} nums
    # @return {string[]}
    def summaryRanges(self, nums):
        x, size = 0, len(nums)
        ans = []
        while x < size:
            c, r = x, str(nums[x])
            while x + 1 < size and nums[x + 1] - nums[x] == 1:
                x += 1
            if x > c:
                r += "->" + str(nums[x])
            ans.append(r)
            x += 1
        return ans

### Test case : dev

# V1'
# https://leetcode.com/problems/summary-ranges/discuss/63335/Simple-Python-solution
class Solution:
    def summaryRanges(self, nums):
        if not nums:
            return []
        
        nums = nums + [nums[-1]+2]
        res = []
        head = nums[0]
        for i in range(1,len(nums)):
            if nums[i] - nums[i-1] > 1:
                if head == nums[i-1]:
                    res.append(str(head))
                else:
                    res.append(str(head) + "->" + str(nums[i-1]))
                head = nums[i]
        return res

# V1''
# https://leetcode.com/problems/summary-ranges/discuss/63335/Simple-Python-solution
class Solution:
    def summaryRanges(self, nums):
        start, end, res = -2**31, -2**31, []
        for i in nums+[2**31-1]:
            if i - end == 1:
                end = i
            else:
                res += [str(start)] if start == end else [str(start) + "->" + str(end)]
                start = end = i
        return res[1:]

# V1' 
# http://bookshadow.com/weblog/2015/06/26/leetcode-summary-ranges/
# IDEA : COLLECT THE RANGES, FORMTAT AND RETURN THEM 
def summaryRanges(self, nums):
    ranges = []
    for n in nums:
        if not ranges or n > ranges[-1][-1] + 1:
            ranges += [],
        ranges[-1][1:] = n,
    return ['->'.join(map(str, r)) for r in ranges]

# V1'' 
# http://bookshadow.com/weblog/2015/06/26/leetcode-summary-ranges/
# IDEA : A variation of solution 1', holding the current range in an extra variable r to make things easier.
def summaryRanges(self, nums):
    ranges = []
    for n in nums:
        if not ranges or n > ranges[-1][-1] + 1:
            ranges += [],
        ranges[-1][1:] = n,
    return ['->'.join(map(str, r)) for r in ranges]

# V1''' 
# http://bookshadow.com/weblog/2015/06/26/leetcode-summary-ranges/
def summaryRanges(self, nums):
    ranges = r = []
    for n in nums:
        if `n-1` not in r:
            r = []
            ranges += r,
        r[1:] = `n`,
    return map('->'.join, ranges)

# V2 
# Time:  O(n)
# Space: O(1)
import itertools
import re
class Solution(object):
    # @param {integer[]} nums
    # @return {string[]}
    def summaryRanges(self, nums):
        ranges = []
        if not nums:
            return ranges

        start, end = nums[0], nums[0]
        for i in range(1, len(nums) + 1):
            if i < len(nums) and nums[i] == end + 1:
                end = nums[i]
            else:
                interval = str(start)
                if start != end:
                    interval += "->" + str(end)
                ranges.append(interval)
                if i < len(nums):
                    start = end = nums[i]

        return ranges

# Time:  O(n)
# Space: O(n)
class Solution2(object):
    # @param {integer[]} nums
    # @return {string[]}
    def summaryRanges(self, nums):
        return [re.sub('->.*>', '->', '->'.join(repr(n) for _, n in g))
            for _, g in itertools.groupby(enumerate(nums), lambda i_n: i_n[1]-i_n[0])]
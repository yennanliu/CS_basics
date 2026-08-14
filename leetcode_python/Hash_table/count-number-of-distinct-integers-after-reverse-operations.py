"""

2442. Count Number of Distinct Integers After Reverse Operations
Medium

You are given an array nums consisting of positive integers.

You have to take each integer in the array, reverse its digits, and add it to the end of the array. You should apply this operation to the original integers in nums.

Return the number of distinct integers in the final array.


Example 1:

Input: nums = [1,13,10,12,31]
Output: 6
Explanation: After including the reverse of each number, the resulting array is [1,13,10,12,31,1,31,1,21,13].
The reversed integers that were added to the end of the array are underlined. Note that for the integer 10, after reversing it, it becomes 01 which is just 1.
The number of distinct integers in this array is 6 (The numbers 1, 10, 12, 13, 21, and 31).

Example 2:

Input: nums = [2,2,2]
Output: 1
Explanation: After including the reverse of each number, the resulting array is [2,2,2,2,2,2].
The number of distinct integers in this array is 1 (The number 2).


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : HASH SET (just collect every value and every reversed value)
#
#   the final array is nums + [reverse(x) for x in nums], so the distinct
#   count is simply |set(nums) union set(reversed)|.
#   NOTE : int(str(x)[::-1]) automatically drops leading zeros
#          (10 -> "01" -> 1), which is exactly what the problem wants.
#
# time = O(n * d), d = number of digits (<= 7 here)
# space = O(n)
class Solution(object):
    def countDistinctIntegers(self, nums):
        seen = set()
        for x in nums:
            seen.add(x)
            seen.add(int(str(x)[::-1]))
        return len(seen)

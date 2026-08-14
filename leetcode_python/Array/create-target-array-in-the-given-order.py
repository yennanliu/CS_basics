"""

1389. Create Target Array in the Given Order
Easy

Given two arrays of integers nums and index. Your task is to create target array under the
following rules:

Initially target array is empty.
From left to right read nums[i] and index[i], insert at index index[i] the value nums[i]
in target array.
Repeat the previous step until there are no elements to read in nums and index.

Return the target array.

It is guaranteed that the insertion operations will be valid.


Example 1:

Input: nums = [0,1,2,3,4], index = [0,1,2,2,1]
Output: [0,4,1,3,2]
Explanation:
nums       index     target
0            0        [0]
1            1        [0,1]
2            2        [0,1,2]
3            2        [0,1,3,2]
4            1        [0,4,1,3,2]

Example 2:

Input: nums = [1,2,3,4,0], index = [0,1,2,3,0]
Output: [0,1,2,3,4]
Explanation:
nums       index     target
1            0        [1]
2            1        [1,2]
3            2        [1,2,3]
4            3        [1,2,3,4]
0            0        [0,1,2,3,4]

Example 3:

Input: nums = [1], index = [0]
Output: [1]


Constraints:

1 <= nums.length, index.length <= 100
nums.length == index.length
0 <= nums[i] <= 100
0 <= index[i] <= i

"""

# V0
# IDEA : DIRECT SIMULATION (list.insert already does the shifting)
#
#   read the pairs left to right and insert nums[i] at position index[i].
#   list.insert shifts everything from that position one slot right, which is
#   exactly the rule described.
#
#   NOTE : the constraint index[i] <= i guarantees the position always exists,
#          so no bounds handling is needed.
#   NOTE : n <= 100, so the O(n) cost of each insert is irrelevant here.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def createTargetArray(self, nums, index):
        target = []
        for i in range(len(nums)):
            target.insert(index[i], nums[i])
        return target

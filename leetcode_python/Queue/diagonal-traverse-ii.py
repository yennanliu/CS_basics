"""

1424. Diagonal Traverse II
Medium

Given a 2D integer array nums, return all elements of nums in diagonal order as shown in the below images.


Example 1:

Input: nums = [[1,2,3],[4,5,6],[7,8,9]]
Output: [1,4,2,7,5,3,8,6,9]

Example 2:

Input: nums = [[1,2,3,4,5],[6,7],[8],[9,10,11],[12,13,14,15,16]]
Output: [1,6,2,8,7,3,9,4,12,10,5,13,11,14,15,16]


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i].length <= 10^5
1 <= sum(nums[i].length) <= 10^5
1 <= nums[i][j] <= 10^5

"""

# V0
# IDEA : BUCKET BY (i + j) DIAGONAL KEY
#
#  -> all cells on the same diagonal share the same (i + j)
#  -> within one diagonal, output order is "bottom-left -> top-right",
#     i.e. row index DECREASING (col index increasing)
#  -> so if we walk the rows in REVERSE order and append,
#     each bucket is already built in the required order
#     (no sorting needed)
#
# time = O(n)
# space = O(n)
# n = total number of elements
from collections import defaultdict
class Solution(object):
    def findDiagonalOrder(self, nums):
        buckets = defaultdict(list)
        max_key = 0

        # NOTE !!! walk rows bottom -> top
        for i in range(len(nums) - 1, -1, -1):
            row = nums[i]
            for j in range(len(row)):
                buckets[i + j].append(row[j])
                if i + j > max_key:
                    max_key = i + j

        res = []
        for key in range(max_key + 1):
            if key in buckets:
                res.extend(buckets[key])
        return res


# V1
# IDEA : SORT BY (i + j, j)
#
#  -> straightforward but O(n log n)
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def findDiagonalOrder(self, nums):
        arr = []
        for i, row in enumerate(nums):
            for j, v in enumerate(row):
                arr.append((i + j, j, v))
        arr.sort()
        return [x[2] for x in arr]

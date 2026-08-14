"""

2610. Convert an Array Into a 2D Array With Conditions
Medium

You are given an integer array nums. You need to create a 2D array from nums satisfying the following conditions:

The 2D array should contain only the elements of the array nums.
Each row in the 2D array contains distinct integers.
The number of rows in the 2D array should be minimal.

Return the resulting array. If there are multiple answers, return any of them.

Note that the 2D array can have a different number of elements on each row.


Example 1:

Input: nums = [1,3,4,1,2,3,1]
Output: [[1,3,4,2],[1,3],[1]]
Explanation: We can create a 2D array that contains the following rows:
- 1,3,4,2
- 1,3
- 1
All elements of nums were used, and each row of the 2D array contains distinct integers, so it is a valid answer.
It can be shown that we cannot have less than 3 rows in a valid array.

Example 2:

Input: nums = [1,2,3,4]
Output: [[4,3,2,1]]
Explanation: All elements of the array are distinct, so we can keep all of them in the first row of the 2D array.


Constraints:

1 <= nums.length <= 200
1 <= nums[i] <= nums.length

"""

# V0
# IDEA : COUNTING (bucket the c-th copy of a value into row c)
#
#   the minimal number of rows is exactly max(freq(v)) : a value appearing
#   f times needs f different rows, and that many rows always suffice.
#
#   so we can build the answer in ONE pass : when we meet a value for the
#   (c+1)-th time, drop it into row index c. two equal values can never
#   land in the same row because their occurrence counters differ.
#
#   NOTE : rows are created lazily -> `if c == len(ans): ans.append([])`
#          keeps the row count at exactly max frequency (no empty rows).
#
# time = O(n), space = O(n)
class Solution(object):
    def findMatrix(self, nums):
        cnt = {}
        ans = []
        for v in nums:
            c = cnt.get(v, 0)
            cnt[v] = c + 1
            if c == len(ans):
                ans.append([])
            ans[c].append(v)
        return ans

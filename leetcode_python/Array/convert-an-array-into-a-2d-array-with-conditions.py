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


# V0-1
# IDEA : FREQUENCY COUNTER + PEEL ONE FULL ROUND AT A TIME
#
#   the minimal row count is max(freq(v)) : a value seen f times needs f
#   different rows, and f rows always suffice. so count first, then build the
#   answer ROW BY ROW instead of element by element — each round emits one
#   copy of every value that still has a positive count and decrements it,
#   dropping values that hit 0. the loop body runs exactly max(freq) times.
#
#   NOTE : snapshot the keys (`list(cnt)`) before decrementing, otherwise the
#          dict is mutated while being iterated.
#
# time = O(n + d * r), d = #distinct values, r = max frequency
# space = O(n)
from collections import Counter
class Solution(object):
    def findMatrix(self, nums):
        cnt = Counter(nums)
        ans = []
        while cnt:
            row = list(cnt)
            ans.append(row)
            for v in row:
                cnt[v] -= 1
                if cnt[v] == 0:
                    del cnt[v]
        return ans


# V0-2
# IDEA : SORT SO EQUAL VALUES BECOME ADJACENT, THEN INDEX INSIDE EACH RUN
#
#   sorting puts all copies of a value in one contiguous run, so the k-th
#   element of a run IS the k-th copy of that value and belongs in row k.
#   the run counter resets whenever the value changes, which removes the hash
#   map entirely — the ordering carries the multiplicity information.
#
#   NOTE : rows are still created lazily, so the row count comes out at
#          exactly max(freq) with no empty rows.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def findMatrix(self, nums):
        arr = sorted(nums)
        ans = []
        k = 0
        for idx, v in enumerate(arr):
            k = k + 1 if idx and v == arr[idx - 1] else 0
            if k == len(ans):
                ans.append([])
            ans[k].append(v)
        return ans

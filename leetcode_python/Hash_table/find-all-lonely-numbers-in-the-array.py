"""

2150. Find All Lonely Numbers in the Array
Medium

You are given an integer array nums. A number x is lonely when it appears only once, and no adjacent number (i.e. x + 1 or x - 1) appears in the array.

Return all lonely numbers in nums. You may return the answer in any order.


Example 1:

Input: nums = [10,6,5,8]
Output: [10,8]
Explanation:
- 10 is a lonely number since it appears exactly once and 9 and 11 does not appear in nums.
- 8 is a lonely number since it appears exactly once and 7 and 9 does not appear in nums.
- 5 is not a lonely number since 6 appears in nums and vice versa.
Hence, the lonely numbers in nums are [10, 8].
Note that [8, 10] may also be returned.

Example 2:

Input: nums = [1,3,5,3]
Output: [1,5]
Explanation:
- 1 is a lonely number since it appears exactly once and 0 and 2 does not appear in nums.
- 5 is a lonely number since it appears exactly once and 4 and 6 does not appear in nums.
- 3 is not a lonely number since it appears twice.
Hence, the lonely numbers in nums are [1, 5].
Note that [5, 1] may also be returned.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^6

"""

# V0
# IDEA : ONE COUNTER ANSWERS BOTH HALVES OF THE CONDITION
#
#   x is lonely iff  cnt[x] == 1  and  neither x-1 nor x+1 is a key of cnt.
#   a Counter gives the occurrence count and the membership test at once.
#
#   NOTE : iterate over the counter's KEYS, not the array — that way each
#          candidate is tested once and duplicates cannot slip in.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def findLonely(self, nums):
        cnt = Counter(nums)
        return [x for x, c in cnt.items()
                if c == 1 and x - 1 not in cnt and x + 1 not in cnt]

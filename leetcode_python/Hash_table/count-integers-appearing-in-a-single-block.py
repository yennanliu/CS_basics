"""

4038. Count Integers Appearing in a Single Block
Easy

You are given an integer array nums.

An integer x is called special if all occurrences of x in nums
appear in a single contiguous block.

(in other words, the indices where x shows up are consecutive,
with no other value in between)

Return the number of distinct special integers in nums.


Example 1:

Input: nums = [1,1,2,2,3]

Output: 3

Explanation:

1 occupies indices [0,1], 2 occupies indices [2,3] and 3 occupies index [4].
Every value sits in one contiguous block, so all 3 distinct values are special.

Example 2:

Input: nums = [1,2,1]

Output: 1

Explanation:

1 shows up at indices 0 and 2, which are split by the 2 at index 1,
so 1 is NOT special. Only 2 is special.

Example 3:

Input: nums = [5]

Output: 1

Explanation:

A value that appears once is always a (single element) block.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : HASH MAP {val : [idx_1, idx_2, ....]}
"""
CORE IDEA:

    value -> 所有出現的 index，然後檢查這些 index 是否連續
"""
from collections import defaultdict

class Solution(object):
    def countSpecialIntegers(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge
        if len(nums) == 1:
            return 1
        
        cnt_map = defaultdict(list)

        for i in range(len(nums)):
            val = nums[i]
            cnt_map[val].append(i)

        cnt = 0

        for k in cnt_map.keys():
            indices = cnt_map[k]
            # NOTE !!
            # if val ONLY exists once,
            # it is still count (as a valid continuous index)
            if len(indices) == 1:
                cnt += 1
            else:
                if len(indices) > 1:
                    # NOTE !!!
                    # via below trick, we check if the index are continuous
                    if len(indices) == indices[-1] - indices[0] + 1:
                        cnt += 1


        return cnt


# V0-1
# IDEA : HASH MAP {val : [idx_1, idx_2, ....]}
#
#   collect every index a value lands on, then a value is special
#   ONLY if its indices are consecutive.
#
#   the cheap way to test "consecutive" is NOT to walk the list, but to
#   compare the span against the count:
#
#      last_idx - first_idx + 1 == number_of_occurrences
#
#   -> if any other value were sitting inside that span, the span would be
#      strictly wider than the number of occurrences
#
#   e.g. nums = [1,2,1] -> idx of 1 = [0,2] -> 2 - 0 + 1 = 3 != 2 -> NOT special
#
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def countSpecialIntegers(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge
        if not nums:
            return 0

        # map : {val : [idx_1, idx_2, ....]}
        my_map = defaultdict(list)

        for i in range(len(nums)):
            my_map[nums[i]].append(i)

        cnt = 0

        for k in my_map:
            indices = my_map[k]

            # NOTE !!! span == count  <=>  indices are consecutive
            if indices[-1] - indices[0] + 1 == len(indices):
                cnt += 1

        return cnt


# V0-2
# IDEA : ONE PASS, KEEP ONLY (first_idx, last_idx, count)
#
#   same "span == count" check as V0, but there is no need to KEEP every
#   index : first, last and the count are all the check ever reads
#
#   -> still O(n) space, but O(distinct) instead of O(n) list cells
#
# time = O(n), space = O(k), k = number of distinct values
class Solution2(object):
    def countSpecialIntegers(self, nums):
        # {val : [first_idx, last_idx, cnt]}
        info = {}

        for i, v in enumerate(nums):
            if v not in info:
                info[v] = [i, i, 1]
            else:
                info[v][1] = i
                info[v][2] += 1

        return sum(1 for first, last, cnt in info.values() if last - first + 1 == cnt)

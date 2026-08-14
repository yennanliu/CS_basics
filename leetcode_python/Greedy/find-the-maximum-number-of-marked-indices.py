"""

2576. Find the Maximum Number of Marked Indices
Medium

You are given a 0-indexed integer array nums.

Initially, all of the indices are unmarked. You are allowed to make this operation any number of times:

Pick two different unmarked indices i and j such that 2 * nums[i] <= nums[j], then mark i and j.

Return the maximum possible number of marked indices in nums using the above operation any number of times.


Example 1:

Input: nums = [3,5,2,4]
Output: 2
Explanation: In the first operation: pick i = 2 and j = 1, the operation is allowed because 2 * nums[2] <= nums[1]. Then mark index 2 and 1.
It can be shown that there's no other valid operation so the answer is 2.

Example 2:

Input: nums = [9,2,5,4]
Output: 4
Explanation: In the first operation: pick i = 3 and j = 0, the operation is allowed because 2 * nums[3] <= nums[0]. Then mark index 3 and 0.
In the second operation: pick i = 1 and j = 2, the operation is allowed because 2 * nums[1] <= nums[2]. Then mark index 1 and 2.
Since there is no other operation, the answer is 4.

Example 3:

Input: nums = [7,6,8]
Output: 0
Explanation: There is no valid operation to do, so the answer is 0.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : SORT + GREEDY TWO POINTERS
#
#   every operation consumes 2 indices, so at most n // 2 pairs can be formed.
#   if k pairs are achievable, the best way to build them is to pair the k
#   SMALLEST values with the k LARGEST values (small -> large), because a small
#   value is the easiest "i" and a large value is the easiest "j".
#
#   NOTE : that means the left partner always comes from nums[0 .. n//2 - 1] and
#          the right partner from nums[(n+1)//2 .. n-1]. So we walk a pointer i
#          over the left half and scan every element x of the right half; each
#          time nums[i] * 2 <= x we lock in a pair and advance i.
#
#   NOTE : the right half starts at (n+1)//2 (NOT n//2) — for odd n the middle
#          element must stay unpaired, otherwise we would over-count pairs.
#
# time = O(n * log n), space = O(1) beyond the sort
class Solution(object):
    def maxNumOfMarkedIndices(self, nums):
        nums.sort()
        n = len(nums)
        i = 0
        for j in range((n + 1) // 2, n):
            if nums[i] * 2 <= nums[j]:
                i += 1
        return i * 2

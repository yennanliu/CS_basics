"""

2459. Sort Array by Moving Items to Empty Space
Hard
(premium / locked problem)

You are given an integer array nums of size n containing each element from 0 to n - 1 (inclusive). Each of the elements from 1 to n - 1 represents an item, and the element 0 represents an empty space.

In one operation, you can move any item to the empty space. nums is considered to be sorted if the numbers of all the items are in ascending order and the empty space is either at the beginning or at the end of the array.

For example, if n = 4, nums is sorted if:

nums = [0,1,2,3] or
nums = [1,2,3,0]

...and considered to be unsorted otherwise.

Return the minimum number of operations needed to sort nums.


Example 1:

Input: nums = [4,2,0,3,1]
Output: 3
Explanation:
- Move item 2 to the empty space. Now, nums = [4,0,2,3,1].
- Move item 1 to the empty space. Now, nums = [4,1,2,3,0].
- Move item 4 to the empty space. Now, nums = [0,1,2,3,4].
It can be proven that 3 is the minimum number of operations needed.

Example 2:

Input: nums = [1,2,3,4,0]
Output: 0
Explanation: nums is already sorted so return 0.

Example 3:

Input: nums = [1,0,2,4,3]
Output: 2
Explanation:
- Move item 2 to the empty space. Now, nums = [1,2,0,4,3].
- Move item 3 to the empty space. Now, nums = [1,2,3,4,0].
It can be proven that 2 is the minimum number of operations needed.


Constraints:

n == nums.length
2 <= n <= 10^5
0 <= nums[i] < n
All the values of nums are unique.

"""

# V0
# IDEA : PERMUTATION CYCLES — THE HOLE RIDES ONE OF THEM FOR FREE
#
#   two targets are allowed (empty space first or last), so solve both and
#   take the smaller.
#
#   for a fixed target, "where should the value at index i end up?" defines a
#   permutation, which decomposes into cycles :
#     * a cycle of length 1 is already correct, cost 0
#     * the cycle CONTAINING the empty space costs L - 1 : the hole rotates
#       around it, each move placing one item directly home
#     * any other cycle of length L costs L + 1 : one extra move to park an
#       item in the hole to start the rotation, and one to bring it back
#
#   summing over the cycles gives the answer for that target.
#
# time = O(n), space = O(n)
class Solution(object):
    def sortArray(self, nums):
        n = len(nums)

        def cost(zero_last):
            # where value v belongs under this target arrangement
            def home(v):
                return (v - 1) % n if zero_last else v

            seen = [False] * n
            total = 0
            for i in range(n):
                if seen[i]:
                    continue
                length = 0
                has_hole = False
                j = i
                while not seen[j]:
                    seen[j] = True
                    if nums[j] == 0:
                        has_hole = True
                    length += 1
                    j = home(nums[j])
                if length > 1:
                    total += length - 1 if has_hole else length + 1
            return total

        return min(cost(False), cost(True))

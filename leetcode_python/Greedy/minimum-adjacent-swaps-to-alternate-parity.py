"""

3587. Minimum Adjacent Swaps to Alternate Parity
Medium

You are given an array nums of distinct integers.

In one operation, you can swap any two adjacent elements in the array.

An arrangement of the array is considered valid if the parity of adjacent elements alternates, meaning every pair of neighboring elements consists of one even and one odd number.

Return the minimum number of adjacent swaps required to transform nums into any valid arrangement.

If it is impossible to rearrange nums such that no two adjacent elements have the same parity, return -1.


Example 1:

Input: nums = [2,4,6,5,7]
Output: 3
Explanation:
Swapping 5 and 6, the array becomes [2,4,5,6,7]
Swapping 5 and 4, the array becomes [2,5,4,6,7]
Swapping 6 and 7, the array becomes [2,5,4,7,6]. The array is now a valid
arrangement. Thus, the answer is 3.

Example 2:

Input: nums = [2,4,5,7]
Output: 1
Explanation:
By swapping 4 and 5, the array becomes [2,5,4,7], which is a valid
arrangement. Thus, the answer is 1.

Example 3:

Input: nums = [1,2,3]
Output: 0
Explanation:
The array is already a valid arrangement. Thus, no operations are needed.

Example 4:

Input: nums = [4,5,6,8]
Output: -1
Explanation:
No valid arrangement is possible. Thus, the answer is -1.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
All elements in nums are distinct.

"""

# V0
# IDEA : ADJACENT SWAPS = SUM OF |CURRENT INDEX - TARGET INDEX| PER PARITY CLASS
#
#   swapping neighbours never reorders two numbers of the same parity
#   relative to each other in an optimal solution, so the k-th even number
#   stays the k-th even number. once the starting parity is fixed the target
#   slots are forced (0, 2, 4, ... for one class), and the minimum number of
#   adjacent swaps equals the sum of displacements of just one class — the
#   other class's displacements are the mirror image of the same swaps.
#
#   a valid arrangement exists only when the two counts differ by at most 1,
#   and the majority class must own the even-numbered slots.
#
# time = O(n), space = O(n)
class Solution(object):
    def minSwaps(self, nums):
        odd = []
        even = []
        for i, v in enumerate(nums):
            if v & 1:
                odd.append(i)
            else:
                even.append(i)
        if abs(len(odd) - len(even)) > 1:
            return -1

        def cost(pos):
            return sum(abs(p - 2 * t) for t, p in enumerate(pos))

        best = float('inf')
        if len(odd) >= len(even):
            best = min(best, cost(odd))
        if len(even) >= len(odd):
            best = min(best, cost(even))
        return best

"""

2293. Min Max Game
Medium

You are given a 0-indexed integer array nums whose length is a power of 2.

Apply the following algorithm on nums:

Let n be the length of nums. If n == 1, end the process. Otherwise, create a new 0-indexed integer array newNums of length n / 2.
For every even index i where 0 <= i < n / 2, assign the value of newNums[i] as min(nums[2 * i], nums[2 * i + 1]).
For every odd index i where 0 <= i < n / 2, assign the value of newNums[i] as max(nums[2 * i], nums[2 * i + 1]).
Replace the array nums with newNums.
Repeat the entire process starting from step 1.

Return the last number that remains in nums after applying the algorithm.


Example 1:

Input: nums = [1,3,5,2,4,8,2,2]
Output: 1
Explanation: The following arrays are the results of applying the algorithm repeatedly.
First: nums = [1,5,4,2]
Second: nums = [1,4]
Final: nums = [1]
1 is the last remaining number, so we return 1.

Example 2:

Input: nums = [3]
Output: 3
Explanation: 3 is already the last remaining number, so we return 3.


Constraints:

1 <= nums.length <= 1024
1 <= nums[i] <= 10^9
nums.length is a power of two.

"""

# V0
# IDEA : DIRECT SIMULATION OF THE HALVING ROUNDS
#
#   each round halves the array, so the total work is n + n/2 + n/4 + ... =
#   O(n) — simulating literally is already optimal.
#
#   the min/max alternation is keyed on the index in the NEW array : even
#   slots take the min of their pair, odd slots the max.
#
# time = O(n), space = O(n)
class Solution(object):
    def minMaxGame(self, nums):
        while len(nums) > 1:
            half = len(nums) // 2
            nxt = [0] * half
            for i in range(half):
                a, b = nums[2 * i], nums[2 * i + 1]
                nxt[i] = min(a, b) if i % 2 == 0 else max(a, b)
            nums = nxt
        return nums[0]

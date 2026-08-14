"""

1558. Minimum Numbers of Function Calls to Make Target Array
Medium

You are given an integer array nums. You have an integer array arr of the same length with all values set to 0 initially. You also have the following modify function:

You want to use the modify function to convert arr to nums using the minimum number of calls.

Return the minimum number of function calls to make nums from arr.

The test cases are generated so that the answer fits in a 32-bit signed integer.

Example 1:

Input: nums = [1,5]
Output: 5
Explanation: Increment by 1 (second element): [0, 0] to get [0, 1] (1 operation).
Double all the elements: [0, 1] -> [0, 2] -> [0, 4] (2 operations).
Increment by 1 (both elements)  [0, 4] -> [1, 4] -> [1, 5] (2 operations).
Total of operations: 1 + 2 + 2 = 5.

Example 2:

Input: nums = [2,2]
Output: 3
Explanation: Increment by 1 (both elements) [0, 0] -> [0, 1] -> [1, 1] (2 operations).
Double all the elements: [1, 1] -> [2, 2] (1 operation).
Total of operations: 2 + 1 = 3.

Example 3:

Input: nums = [4,2,5]
Output: 6
Explanation: (initial)[0,0,0] -> [1,0,0] -> [1,0,1] -> [2,0,2] -> [2,1,2] -> [4,2,4] -> [4,2,5](nums).

Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9

"""

# V0
# IDEA : BIT MANIPULATION (work backwards : halve everything, decrement odds)
#
#   run the process in reverse. an "+1 on one element" undoes a set LSB,
#   a "double everything" undoes one right shift of the WHOLE array.
#     - every set bit of every number costs exactly one +1 call
#     - the doublings needed = (longest bit length) - 1, shared by all
#
# time = O(n * log(max)), space = O(1)
class Solution(object):
    def minOperations(self, nums):
        adds = 0
        widest = 0
        for x in nums:
            bits = 0
            while x:
                adds += x & 1
                x >>= 1
                bits += 1
            if bits > widest:
                widest = bits
        return adds + (widest - 1 if widest else 0)

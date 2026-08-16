"""

3542. Minimum Operations to Convert All Elements to Zero
Medium

You are given an array nums of size n, consisting of non-negative integers. Your
task is to apply some (possibly zero) operations on the array so that all
elements become 0.

In one operation, you can select a subarray [i, j] (where 0 <= i <= j < n) and
set all occurrences of the minimum non-negative integer in that subarray to 0.

Return the minimum number of operations required to make all elements in the
array 0.

Example 1:

Input: nums = [0,2]

Output: 1

Explanation:

Select the subarray [1,1] (which is [2]), where the minimum non-negative integer
is 2. Setting all occurrences of 2 to 0 results in [0,0].

Thus, the minimum number of operations required is 1.

Example 2:

Input: nums = [3,1,2,1]

Output: 3

Explanation:

Select subarray [1,3] (which is [1,2,1]), where the minimum non-negative integer
is 1. Setting all occurrences of 1 to 0 results in [3,0,2,0].

Select subarray [2,2] (which is [2]), where the minimum non-negative integer is
2. Setting all occurrences of 2 to 0 results in [3,0,0,0].

Select subarray [0,0] (which is [3]), where the minimum non-negative integer is
3. Setting all occurrences of 3 to 0 results in [0,0,0,0].

Thus, the minimum number of operations required is 3.

Example 3:

Input: nums = [1,2,1,2,1,2]

Output: 4

Explanation:

Select subarray [0,5] (which is [1,2,1,2,1,2]), where the minimum non-negative
integer is 1. Setting all occurrences of 1 to 0 results in [0,2,0,2,0,2].

Select subarray [1,1] (which is [2]), where the minimum non-negative integer is
2. Setting all occurrences of 2 to 0 results in [0,0,0,2,0,2].

Select subarray [3,3] (which is [2]), where the minimum non-negative integer is
2. Setting all occurrences of 2 to 0 results in [0,0,0,0,0,2].

Select subarray [5,5] (which is [2]), where the minimum non-negative integer is
2. Setting all occurrences of 2 to 0 results in [0,0,0,0,0,0].

Thus, the minimum number of operations required is 4.

Constraints:

1 <= n == nums.length <= 10^5

0 <= nums[i] <= 10^5

"""

# V0
# IDEA : MONOTONIC STACK OVER "NESTED" VALUE LEVELS
#
#   an operation zeroes one value inside one contiguous window, so two equal
#   values can share an operation only when nothing strictly smaller separates
#   them — a smaller element inside the window would be the minimum instead.
#
#   that is exactly the structure a non-decreasing stack tracks.  push values as
#   they come; when a smaller value arrives, every strictly larger value on the
#   stack is closed off forever (it can never be merged with anything to the
#   right) so it costs one operation.  equal values sitting on top of the stack
#   are merged for free.  whatever remains on the stack at the end costs one
#   operation each.  zeroes are simply never pushed.
#
# time = O(n), space = O(n)
class Solution(object):
    def minOperations(self, nums):
        stack = []
        ops = 0
        for v in nums:
            while stack and stack[-1] > v:
                stack.pop()
                ops += 1
            if v > 0 and (not stack or stack[-1] != v):
                stack.append(v)
        return ops + len(stack)

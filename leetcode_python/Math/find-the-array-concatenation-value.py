"""

2562. Find the Array Concatenation Value
Easy

You are given a 0-indexed integer array nums.

The concatenation of two numbers is the number formed by concatenating their numerals.

- For example, the concatenation of 15, 49 is 1549.

The concatenation value of nums is initially equal to 0. Perform this operation until nums becomes empty:

- If nums has a size greater than one, add the value of the concatenation of the first and the last element to the concatenation value of nums, and remove those two elements from nums. For example, if the nums was [1, 2, 4, 5, 6], add 16 to the concatenation value.
- If only one element exists in nums, add its value to the concatenation value of nums, then remove it.

Return the concatenation value of nums.


Example 1:

Input: nums = [7,52,2,4]
Output: 596
Explanation: Before performing any operation, nums is [7,52,2,4] and concatenation value is 0.
 - In the first operation:
We pick the first element, 7, and the last element, 4.
Their concatenation is 74, and we add it to the concatenation value, so it becomes equal to 74.
Then we delete them from nums, so nums becomes equal to [52,2].
 - In the second operation:
We pick the first element, 52, and the last element, 2.
Their concatenation is 522, and we add it to the concatenation value, so it becomes equal to 596.
Then we delete them from the nums, so nums becomes empty.
Since the concatenation value is 596 so the answer is 596.

Example 2:

Input: nums = [5,14,13,8,12]
Output: 673
Explanation: Before performing any operation, nums is [5,14,13,8,12] and concatenation value is 0.
 - In the first operation:
We pick the first element, 5, and the last element, 12.
Their concatenation is 512, and we add it to the concatenation value, so it becomes equal to 512.
Then we delete them from the nums, so nums becomes equal to [14,13,8].
 - In the second operation:
We pick the first element, 14, and the last element, 8.
Their concatenation is 148, and we add it to the concatenation value, so it becomes equal to 660.
Then we delete them from the nums, so nums becomes equal to [13].
 - In the third operation:
nums has only one element, so we pick 13 and add it to the concatenation value, so it becomes equal to 673.
Then we delete it from nums, so nums become empty.
Since the concatenation value is 673 so the answer is 673.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 10^4

"""

# V0
# IDEA : TWO POINTERS + MATH (digit shift)
#
#   the "remove first and last" process just pairs index i with index j from
#   the two ends inward, so no actual list mutation is needed — walk two
#   pointers toward each other.
#
#   concat(a, b) = a * 10^(digits of b) + b, which avoids the str/int round
#   trip. digits of b is found by dividing b by 10 until it vanishes.
#
#   NOTE : when the length is odd the pointers meet (i == j) and that lone
#          middle element is added on its own — handled after the loop.
#
# time = O(n log M), space = O(1)   (M = max value in nums)
class Solution(object):
    def findTheArrayConcVal(self, nums):
        ans = 0
        i, j = 0, len(nums) - 1
        while i < j:
            shift, b = 1, nums[j]
            while b > 0:
                shift *= 10
                b //= 10
            ans += nums[i] * shift + nums[j]
            i += 1
            j -= 1
        if i == j:
            ans += nums[i]
        return ans

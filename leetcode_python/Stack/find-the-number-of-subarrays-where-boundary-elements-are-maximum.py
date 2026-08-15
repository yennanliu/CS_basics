"""

3113. Find the Number of Subarrays Where Boundary Elements Are Maximum
Hard

You are given an array of positive integers nums.

Return the number of subarrays of nums, where the first and the last elements of the subarray are equal to the largest element in the subarray.


Example 1:

Input: nums = [1,4,3,3,2]
Output: 6
Explanation:
There are 6 subarrays which have the first and the last elements equal to the largest element of the subarray:
subarray [1,4,3,3,2], with its largest element 1. The first element is 1 and the last element is also 1.
subarray [1,4,3,3,2], with its largest element 4. The first element is 4 and the last element is also 4.
subarray [1,4,3,3,2], with its largest element 3. The first element is 3 and the last element is also 3.
subarray [1,4,3,3,2], with its largest element 3. The first element is 3 and the last element is also 3.
subarray [1,4,3,3,2], with its largest element 2. The first element is 2 and the last element is also 2.
subarray [1,4,3,3,2], with its largest element 3. The first element is 3 and the last element is also 3.
Hence, we return 6.

Example 2:

Input: nums = [3,3,3]
Output: 6
Explanation:
There are 6 subarrays which have the first and the last elements equal to the largest element of the subarray:
subarray [3,3,3], with its largest element 3. The first element is 3 and the last element is also 3.
subarray [3,3,3], with its largest element 3. The first element is 3 and the last element is also 3.
subarray [3,3,3], with its largest element 3. The first element is 3 and the last element is also 3.
subarray [3,3,3], with its largest element 3. The first element is 3 and the last element is also 3.
subarray [3,3,3], with its largest element 3. The first element is 3 and the last element is also 3.
subarray [3,3,3], with its largest element 3. The first element is 3 and the last element is also 3.
Hence, we return 6.

Example 3:

Input: nums = [1]
Output: 1
Explanation:
There is a single subarray of nums which is [1], with its largest element 1. The first element is 1 and the last element is also 1.
Hence, we return 1.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : MONOTONIC STACK OF (VALUE, HOW MANY COPIES ARE STILL "VISIBLE")
#
#   a valid subarray is a pair of equal values i < j such that nothing
#   strictly larger sits between them. so for each element, count how many
#   earlier equal values are still unblocked.
#
#   a non-increasing stack keeps exactly the unblocked candidates : when a
#   new value arrives, every smaller value on the stack is now shadowed and
#   pops off. what remains on top is either the same value — whose stored
#   count says how many copies are visible, all of which pair with the new
#   element — or a larger one.
#
#   so each element contributes (visible copies of its value) + 1, the +1
#   being the length-1 subarray.
#
# time = O(n), space = O(n)
class Solution(object):
    def numberOfSubarrays(self, nums):
        stack = []                       # (value, count of visible copies)
        res = 0
        for x in nums:
            while stack and stack[-1][0] < x:
                stack.pop()
            if stack and stack[-1][0] == x:
                stack[-1][1] += 1
                res += stack[-1][1]      # pairs with each visible copy, plus itself
            else:
                stack.append([x, 1])
                res += 1
        return res

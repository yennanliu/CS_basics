"""

3265. Count Almost Equal Pairs I
Medium

You are given an array nums consisting of positive integers.

We call two integers x and y in this problem almost equal if both integers can become equal after performing the following operation at most once:

Choose either x or y and swap any two digits within the chosen number.

Return the number of indices i and j in nums where i < j such that nums[i] and nums[j] are almost equal.

Note that it is allowed for an integer to have leading zeros after performing an operation.


Example 1:

Input: nums = [3,12,30,17,21]
Output: 2
Explanation:
The almost equal pairs of elements are:
3 and 30. By swapping 3 and 0 in 30, you get 03 which is 3.
12 and 21. By swapping 1 and 2 in 12, you get 21.

Example 2:

Input: nums = [1,1,1,1,1]
Output: 10
Explanation:
Every two elements in the array are almost equal.

Example 3:

Input: nums = [123,231]
Output: 0
Explanation:
We cannot swap any two digits of 123 or 231 to reach the other.


Constraints:

2 <= nums.length <= 100
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : PAD TO A COMMON WIDTH, THEN TEST EVERY SINGLE SWAP
#
#   leading zeros are explicitly allowed after the swap, so two numbers are
#   comparable once both are written with the same number of digits — pad
#   the shorter with zeros on the left.
#
#   "at most one swap on ONE of them" is symmetric : if a swap inside x
#   reaches y then the same two positions swapped inside y reach x. so it is
#   enough to try every pair of positions in x, which is at most 21 tries for
#   7-digit values.
#
#   n <= 100 gives under 5000 pairs, so the direct check is fine here; the
#   sequel (LC 3267) allows two swaps at n = 5000 and needs hashing.
#
# time = O(n^2 * d^2), space = O(d)
class Solution(object):
    def countPairs(self, nums):
        strs = [str(v) for v in nums]
        width = max(len(t) for t in strs)
        strs = [t.rjust(width, '0') for t in strs]

        def almost(a, b):
            if a == b:
                return True
            diff = [i for i in range(width) if a[i] != b[i]]
            # one swap can repair exactly two mismatched positions
            return len(diff) == 2 and a[diff[0]] == b[diff[1]] and a[diff[1]] == b[diff[0]]

        n = len(nums)
        return sum(1
                   for i in range(n)
                   for j in range(i + 1, n)
                   if almost(strs[i], strs[j]))

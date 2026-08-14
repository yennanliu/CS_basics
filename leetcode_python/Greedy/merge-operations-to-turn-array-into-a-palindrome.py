"""

2422. Merge Operations to Turn Array Into a Palindrome
Medium
(premium / locked problem)

You are given an array nums consisting of positive integers.

You can perform the following operation on the array any number of times:

Choose any two adjacent elements and replace them with their sum.
For example, if nums = [1,2,3,1], you can apply one operation to make it [1,5,1].

Return the minimum number of operations needed to turn the array into a palindrome.


Example 1:

Input: nums = [4,3,2,1,2,3,1]
Output: 2
Explanation: We can turn the array into a palindrome in 2 operations as follows:
- Apply the operation on the fourth and fifth element of the array, nums becomes equal to [4,3,2,3,3,1].
- Apply the operation on the fifth and sixth element of the array, nums becomes equal to [4,3,2,3,4].
The array [4,3,2,3,4] is a palindrome.
It can be shown that 2 is the minimum number of operations needed.

Example 2:

Input: nums = [1,2,3,4]
Output: 3
Explanation: We do the operation 3 times in any position, we obtain the array [10] at the end which is a palindrome.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : TWO POINTERS, ALWAYS MERGE ON THE SMALLER SIDE
#
#   compare the outermost pair. if they match, they can stay as the
#   palindrome's outer layer and both pointers move inward for free.
#
#   if they differ, the SMALLER one can never be matched on its own — every
#   merge only grows a value, so the smaller end must absorb its neighbour.
#   that costs one operation and is forced, which makes the greedy optimal.
#
#   all values are positive, so a merged side strictly grows and the process
#   always terminates.
#
# time = O(n), space = O(n)
class Solution(object):
    def minimumOperations(self, nums):
        a = nums[:]
        i, j = 0, len(a) - 1
        res = 0
        while i < j:
            if a[i] == a[j]:
                i += 1
                j -= 1
            elif a[i] < a[j]:
                a[i + 1] += a[i]
                i += 1
                res += 1
            else:
                a[j - 1] += a[j]
                j -= 1
                res += 1
        return res

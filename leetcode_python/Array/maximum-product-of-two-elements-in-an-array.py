"""

1464. Maximum Product of Two Elements in an Array
Easy

Given the array of integers nums, you will choose two different indices i and j of that array. Return the maximum value of (nums[i]-1)*(nums[j]-1).


Example 1:

Input: nums = [3,4,5,2]
Output: 12
Explanation: If you choose the indices i=1 and j=2 (indexed from 0), you will get the maximum value, that is, (nums[1]-1)*(nums[2]-1) = (4-1)*(5-1) = 3*4 = 12.

Example 2:

Input: nums = [1,5,4,5]
Output: 16
Explanation: Choosing the indices i=1 and j=3 (indexed from 0), you will get the maximum value of (5-1)*(5-1) = 16.

Example 3:

Input: nums = [3,7]
Output: 12


Constraints:

2 <= nums.length <= 500
1 <= nums[i] <= 10^3

"""

# V0
# IDEA : TRACK THE TWO LARGEST VALUES (all values are positive)
#
#   nums[i] >= 1, so (nums[i]-1) is never negative and the product is
#   maximised by the two biggest entries -> no need to sort or to worry
#   about a "two most negative" case.
#   keep the largest (a) and second largest (b) in one pass.
#   NOTE : duplicates are fine, the two picks only need different indices.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxProduct(self, nums):
        a = b = 0                     # a = largest, b = second largest
        for x in nums:
            if x > a:
                a, b = x, a
            elif x > b:
                b = x
        return (a - 1) * (b - 1)


# V0-1
# IDEA : SORT, THEN TAKE THE TWO LARGEST
#
#   no case analysis at all : after sorting ascending the two biggest values
#   sit at the very end. slower than the single pass, but this is the shape to
#   use when the k-th largest is also wanted, or when negatives are possible
#   and both ends of the sorted array must be inspected.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def maxProduct(self, nums):
        s = sorted(nums)
        return (s[-1] - 1) * (s[-2] - 1)


# V0-2
# IDEA : COUNTING SORT OVER THE BOUNDED VALUE RANGE (1 <= nums[i] <= 1000)
#
#   the values are bounded, so tally them into buckets and walk the buckets
#   downwards, taking two units of count (a duplicated value can supply both,
#   since only the INDICES must differ).
#   the scan length does not depend on n, so this is the win when nums is huge
#   and the value range is tiny -- the usual counting-sort trade.
#
# time = O(n + M), M = 1001 buckets
# space = O(M)
class Solution(object):
    def maxProduct(self, nums):
        M = 1001
        cnt = [0] * M
        for x in nums:
            cnt[x] += 1
        picked = []
        for v in range(M - 1, 0, -1):
            while cnt[v] > 0 and len(picked) < 2:
                picked.append(v)
                cnt[v] -= 1
            if len(picked) == 2:
                break
        return (picked[0] - 1) * (picked[1] - 1)

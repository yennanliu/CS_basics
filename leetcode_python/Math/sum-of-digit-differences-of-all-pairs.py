"""

3153. Sum of Digit Differences of All Pairs
Medium

You are given an array nums consisting of positive integers where all integers have the same number of digits.

The digit difference between two integers is the count of different digits that are in the same position in the two integers.

Return the sum of the digit differences between all pairs of integers in nums.


Example 1:

Input: nums = [13,23,12]
Output: 4
Explanation:
We have the following:
- The digit difference between 13 and 23 is 1.
- The digit difference between 13 and 12 is 1.
- The digit difference between 23 and 12 is 2.
So the total sum of digit differences between all pairs of integers is 1 + 1 + 2 = 4.

Example 2:

Input: nums = [10,10,10,10]
Output: 0
Explanation:
All the integers in the array are the same. So the total sum of digit differences between all pairs of integers will be 0.


Constraints:

2 <= nums.length <= 10^5
1 <= nums[i] < 10^9
All integers in nums have the same number of digits.

"""

# V0
# IDEA : HANDLE EACH DIGIT POSITION SEPARATELY AND COUNT THE *AGREEING* PAIRS
#
#   positions are independent, so the answer is the sum over positions of
#   "how many pairs differ there". counting the pairs that AGREE is easier :
#   if a digit d occurs c times in this position it contributes C(c, 2)
#   agreeing pairs, so
#
#       differing = C(n, 2) - sum over d of C(count[d], 2)
#
#   peeling positions with // 10 and % 10 avoids building any strings.
#
# time = O(n * digits), space = O(1)
class Solution(object):
    def sumDigitDifferences(self, nums):
        n = len(nums)
        total_pairs = n * (n - 1) // 2
        res = 0
        vals = list(nums)
        while vals[0] > 0:
            cnt = [0] * 10
            for i in range(n):
                cnt[vals[i] % 10] += 1
                vals[i] //= 10
            same = sum(c * (c - 1) // 2 for c in cnt)
            res += total_pairs - same
        return res

"""

2535. Difference Between Element Sum and Digit Sum of an Array
Easy

You are given a positive integer array nums.

The element sum is the sum of all the elements in nums.
The digit sum is the sum of all the digits (not necessarily distinct) that appear in nums.

Return the absolute difference between the element sum and digit sum of nums.

Note that the absolute difference between two integers x and y is defined as |x - y|.


Example 1:

Input: nums = [1,15,6,3]
Output: 9
Explanation:
The element sum of nums is 1 + 15 + 6 + 3 = 25.
The digit sum of nums is 1 + 1 + 5 + 6 + 3 = 16.
The absolute difference between the element sum and digit sum is |25 - 16| = 9.

Example 2:

Input: nums = [1,2,3,4]
Output: 0
Explanation:
The element sum of nums is 1 + 2 + 3 + 4 = 10.
The digit sum of nums is 1 + 2 + 3 + 4 = 10.
The absolute difference between the element sum and digit sum is |10 - 10| = 0.


Constraints:

1 <= nums.length <= 2000
1 <= nums[i] <= 2000

"""

# V0
# IDEA : SIMULATION (accumulate both sums in one pass)
#
#   walk the array once, adding v to the element sum and peeling v's decimal
#   digits off with % 10 / // 10 into the digit sum.
#
#   NOTE : the element sum is ALWAYS >= the digit sum (a number is never
#          smaller than the sum of its own digits), so abs() is not needed --
#          but we keep it to mirror the statement.
#
# time = O(n * log10(M)), space = O(1)
class Solution(object):
    def differenceOfSum(self, nums):
        elem_sum = 0
        digit_sum = 0
        for v in nums:
            elem_sum += v
            while v:
                digit_sum += v % 10
                v //= 10
        return abs(elem_sum - digit_sum)


# V0-1
# IDEA : DECIMAL STRING SCAN (str() does the digit split, not % / //)
#
#   the digit sum of v is just the sum of the characters of its decimal
#   spelling, so one flat generator over str(v) replaces the divmod loop.
#   no arithmetic peeling at all - the base-10 decomposition is delegated to
#   int -> str conversion.
#
# time = O(n * log10(M)), space = O(log10(M)) for the temporary strings
class Solution(object):
    def differenceOfSum(self, nums):
        elem_sum = sum(nums)
        digit_sum = sum(int(ch) for v in nums for ch in str(v))
        return abs(elem_sum - digit_sum)


# V0-2
# IDEA : DP LOOKUP TABLE OF DIGIT SUMS (tabulation, digits reused)
#
#   digit_sum(v) = digit_sum(v // 10) + v % 10, so a bottom-up table over
#   0..max(nums) fills every digit sum in O(1) each. Then the answer is two
#   O(n) sums with NO per-element digit work.
#
#   this wins when the array is long relative to the value range (here
#   nums[i] <= 2000 while n can be 2000) and it is the shape you want if the
#   same digit sums get queried repeatedly.
#
# time = O(M + n), space = O(M)  where M = max(nums)
class Solution(object):
    def differenceOfSum(self, nums):
        top = max(nums)
        ds = [0] * (top + 1)
        for v in range(1, top + 1):
            ds[v] = ds[v // 10] + v % 10
        return abs(sum(nums) - sum(ds[v] for v in nums))

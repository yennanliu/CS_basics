"""

3334. Find the Maximum Factor Score of Array
Medium

You are given an integer array nums.

The factor score of an array is defined as the product of the LCM and GCD of all elements of that array.

Return the maximum factor score of nums after removing at most one element from it.

Note that both the LCM and GCD of a single number are the number itself, and the factor score of an empty array is 0.


Example 1:

Input: nums = [2,4,8,16]
Output: 64
Explanation:
On removing element 2, the GCD of the remaining elements is 4 while the LCM is 16, which gives a maximum factor score of 4 * 16 = 64.

Example 2:

Input: nums = [1,2,3,4,5]
Output: 60
Explanation:
The maximum factor score of 60 can be obtained without removing any elements.

Example 3:

Input: nums = [3]
Output: 9


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 30

"""

# V0
# IDEA : PREFIX AND SUFFIX GCD / LCM SO EACH REMOVAL IS AN O(1) COMBINE
#
#   removing element i leaves the elements before it and after it, so
#   precomputing running gcd and lcm from both ends lets each candidate be
#   scored by merging the two sides.
#
#   the "remove nothing" case is just the whole-array gcd times lcm, and a
#   single-element array keeps its own value for both.
#
# time = O(n log max), space = O(n)
class Solution(object):
    def maxScore(self, nums):

        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        def lcm(a, b):
            if a == 0:
                return b
            if b == 0:
                return a
            return a // gcd(a, b) * b

        n = len(nums)
        if n == 1:
            return nums[0] * nums[0]

        pre_g = [0] * (n + 1)
        pre_l = [0] * (n + 1)
        for i in range(n):
            pre_g[i + 1] = gcd(pre_g[i], nums[i])
            pre_l[i + 1] = lcm(pre_l[i], nums[i])

        suf_g = [0] * (n + 1)
        suf_l = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            suf_g[i] = gcd(suf_g[i + 1], nums[i])
            suf_l[i] = lcm(suf_l[i + 1], nums[i])

        best = pre_g[n] * pre_l[n]              # remove nothing
        for i in range(n):
            g = gcd(pre_g[i], suf_g[i + 1])
            l = lcm(pre_l[i], suf_l[i + 1])
            best = max(best, g * l)
        return best

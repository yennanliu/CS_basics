"""

2748. Number of Beautiful Pairs
Easy

You are given a 0-indexed integer array nums. A pair of indices i, j where 0 <= i < j < nums.length is called beautiful if the first digit of nums[i] and the last digit of nums[j] are coprime.

Return the total number of beautiful pairs in nums.

Two integers x and y are coprime if there is no integer greater than 1 that divides both of them. In other words, x and y are coprime if gcd(x, y) == 1, where gcd(x, y) is the greatest common divisor of x and y.


Example 1:

Input: nums = [2,5,1,4]
Output: 5
Explanation: There are 5 beautiful pairs in nums:
When i = 0 and j = 1: the first digit of nums[0] is 2, and the last digit of nums[1] is 5. We can confirm that 2 and 5 are coprime, since gcd(2,5) == 1.
When i = 0 and j = 2: the first digit of nums[0] is 2, and the last digit of nums[2] is 1. Indeed, gcd(2,1) == 1.
When i = 1 and j = 2: the first digit of nums[1] is 5, and the last digit of nums[2] is 1. Indeed, gcd(5,1) == 1.
When i = 1 and j = 3: the first digit of nums[1] is 5, and the last digit of nums[3] is 4. Indeed, gcd(5,4) == 1.
When i = 2 and j = 3: the first digit of nums[2] is 1, and the last digit of nums[3] is 4. Indeed, gcd(1,4) == 1.
Thus, we return 5.

Example 2:

Input: nums = [11,21,12]
Output: 2
Explanation: There are 2 beautiful pairs:
When i = 0 and j = 1: the first digit of nums[0] is 1, and the last digit of nums[1] is 1. Indeed, gcd(1,1) == 1.
When i = 0 and j = 2: the first digit of nums[0] is 1, and the last digit of nums[2] is 2. Indeed, gcd(1,2) == 1.
Thus, we return 2.


Constraints:

2 <= nums.length <= 100
1 <= nums[i] <= 9999
nums[i] % 10 != 0

"""

# V0
# IDEA : HASH TABLE (bucket counting on the 9 possible leading digits)
#
#  a "beautiful" pair only depends on 2 tiny values:
#     - the FIRST digit of the left element  (1..9)
#     - the LAST  digit of the right element (1..9, since nums[i] % 10 != 0)
#
#  so instead of the O(n^2) double loop we sweep once from left to right and
#  keep cnt[d] = how many already-seen elements have leading digit d.
#  For the current element (playing the role of j) we add up cnt[d] over all
#  d that are coprime with `nums[j] % 10`, then register our own leading digit.
#
#   NOTE : register the leading digit AFTER counting, otherwise the element
#          would pair with itself (i < j must hold strictly).
#   NOTE : the leading digit is `int(str(x)[0])` -- or repeatedly //= 10,
#          which avoids the string conversion.
#
# time = O(n * 10 * log(10)), space = O(10) = O(1)
class Solution(object):
    def countBeautifulPairs(self, nums):
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        cnt = [0] * 10
        res = 0
        for x in nums:
            last = x % 10
            for d in range(1, 10):
                if cnt[d] and gcd(d, last) == 1:
                    res += cnt[d]
            first = x
            while first > 9:
                first //= 10
            cnt[first] += 1
        return res

"""

2513. Minimize the Maximum of Two Arrays
Medium

We have two arrays arr1 and arr2 which are initially empty. You need to add positive integers to them such that they satisfy all the following conditions:

arr1 contains uniqueCnt1 distinct positive integers, each of which is not divisible by divisor1.
arr2 contains uniqueCnt2 distinct positive integers, each of which is not divisible by divisor2.
No integer is present in both arr1 and arr2.

Given divisor1, divisor2, uniqueCnt1, and uniqueCnt2, return the minimum possible maximum integer that can be present in either array.


Example 1:

Input: divisor1 = 2, divisor2 = 7, uniqueCnt1 = 1, uniqueCnt2 = 3
Output: 4
Explanation:
We can distribute the first 4 natural numbers into arr1 and arr2.
arr1 = [1] and arr2 = [2,3,4].
We can see that both arrays satisfy all the conditions.
Since the maximum value is 4, we return it.

Example 2:

Input: divisor1 = 3, divisor2 = 5, uniqueCnt1 = 2, uniqueCnt2 = 1
Output: 3
Explanation:
Here arr1 = [1,2], and arr2 = [3] satisfy all conditions.
Since the maximum value is 3, we return it.

Example 3:

Input: divisor1 = 2, divisor2 = 4, uniqueCnt1 = 8, uniqueCnt2 = 2
Output: 15
Explanation:
Here, the final possible arrays can be arr1 = [1,3,5,7,9,11,13,15], and arr2 = [2,6].
It can be shown that it is not possible to obtain a lower maximum satisfying all conditions.


Constraints:

2 <= divisor1, divisor2 <= 10^5
1 <= uniqueCnt1, uniqueCnt2 < 10^9
2 <= uniqueCnt1 + uniqueCnt2 <= 10^9

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER (+ inclusion-exclusion via LCM)
#
#   feasibility is monotone : if we can build both arrays inside [1..x], we can
#   also build them inside [1..x+1]. so binary search the smallest good x.
#
#   inside [1..x] :
#     - usable by arr1 (not divisible by divisor1) : x - x // divisor1
#     - usable by arr2 (not divisible by divisor2) : x - x // divisor2
#     - usable by EITHER (not divisible by lcm)    : x - x // lcm
#
#   Hall's-condition style check — x works iff all three hold:
#       x - x//divisor1 >= uniqueCnt1
#       x - x//divisor2 >= uniqueCnt2
#       x - x//lcm      >= uniqueCnt1 + uniqueCnt2
#
#   NOTE : the third test is the subtle one. numbers divisible by BOTH divisors
#          (i.e. by lcm) are usable by neither array, so the shared pool must
#          still cover the combined demand — checking the first two alone is
#          not enough (see example 3, divisors 2 and 4).
#
# time = O(log(MAX) + log(min(d1, d2))), space = O(1)
class Solution(object):
    def minimizeSet(self, divisor1, divisor2, uniqueCnt1, uniqueCnt2):
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        lcm = divisor1 // gcd(divisor1, divisor2) * divisor2

        def ok(x):
            if x - x // divisor1 < uniqueCnt1:
                return False
            if x - x // divisor2 < uniqueCnt2:
                return False
            return x - x // lcm >= uniqueCnt1 + uniqueCnt2

        left, right = 1, 10 ** 10
        while left < right:
            mid = (left + right) // 2
            if ok(mid):
                right = mid
            else:
                left = mid + 1
        return left

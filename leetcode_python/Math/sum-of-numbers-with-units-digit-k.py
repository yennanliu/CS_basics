"""

2310. Sum of Numbers With Units Digit K
Medium

Given two integers num and k, consider a set of positive integers with the following properties:

The units digit of each integer is k.
The sum of the integers is num.

Return the minimum possible size of such a set, or -1 if no such set exists.

Note:

The set can contain multiple instances of the same integer, and the sum of an empty set is considered 0.
The units digit of a number is the rightmost digit of the number.


Example 1:

Input: num = 58, k = 9
Output: 2
Explanation:
One valid set is [9,49], as the sum is 58 and each integer has a units digit of 9.
Another valid set is [19,39].
It can be shown that 2 is the minimum possible size of a valid set.

Example 2:

Input: num = 37, k = 2
Output: -1
Explanation: It is not possible to obtain a sum of 37 using only integers that have a units digit of 2.

Example 3:

Input: num = 0, k = 7
Output: 0
Explanation: The sum of an empty set is considered 0.


Constraints:

0 <= num <= 3000
0 <= k <= 9

"""

# V0
# IDEA : MATH / ENUMERATE THE SET SIZE (only the last digit constrains us)
#
#   if the set has i numbers, each ending in k, their sum ends in the same
#   digit as i * k, and the smallest achievable sum is i * k. so a size i
#   works iff
#       (i * k) % 10 == num % 10   and   i * k <= num
#   (the slack num - i*k is a multiple of 10 and can be spread over the
#    numbers 10 at a time without touching any units digit).
#
#   NOTE : num == 0 -> the empty set already works, answer 0.
#          i only needs to run 1..10 because i*k mod 10 has period <= 10.
#
# time = O(1), space = O(1)
class Solution(object):
    def minimumNumbers(self, num, k):
        if num == 0:
            return 0
        for i in range(1, 11):
            if i * k <= num and (i * k) % 10 == num % 10:
                return i
        return -1

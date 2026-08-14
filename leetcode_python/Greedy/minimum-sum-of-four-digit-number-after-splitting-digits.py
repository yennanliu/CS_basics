"""

2160. Minimum Sum of Four Digit Number After Splitting Digits
Easy

You are given a positive integer num consisting of exactly four digits. Split num into two new integers new1 and new2 by using the digits found in num. Leading zeros are allowed in new1 and new2, and all the digits found in num must be used.

For example, given num = 2932, you have the following digits: two 2's, one 9 and one 3. Some of the possible pairs [new1, new2] are [22, 93], [23, 92], [223, 9] and [2, 329].

Return the minimum possible sum of new1 and new2.


Example 1:

Input: num = 2932
Output: 52
Explanation: Some possible pairs [new1, new2] are [29, 23], [223, 9], etc.
The minimum sum can be obtained by the pair [29, 23]: 29 + 23 = 52.

Example 2:

Input: num = 4009
Output: 13
Explanation: Some possible pairs [new1, new2] are [0, 49], [490, 0], etc.
The minimum sum can be obtained by the pair [4, 9]: 4 + 9 = 13.


Constraints:

1000 <= num <= 9999

"""

# V0
# IDEA : SORT THE DIGITS AND DEAL THEM ALTERNATELY INTO TWO 2-DIGIT NUMBERS
#
#   splitting 4 digits into two 2-digit numbers is always at least as good as
#   a 1/3 split (a third digit would carry weight 100). so the sum is
#       10 * (a + b) + (c + d)
#   with {a, b} the two tens digits — minimised by putting the two SMALLEST
#   digits in the tens places.
#
#   sort ascending, then d[0], d[1] become the tens and d[2], d[3] the units.
#
#   NOTE : leading zeros are allowed, so no special case is needed.
#
# time = O(1), space = O(1)
class Solution(object):
    def minimumSum(self, num):
        d = sorted(str(num))
        return int(d[0] + d[2]) + int(d[1] + d[3])

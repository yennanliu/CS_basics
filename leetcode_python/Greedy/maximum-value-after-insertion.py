"""

1881. Maximum Value after Insertion
Medium

You are given a very large integer n, represented as a string, and an integer digit x. The digits in n and the digit x are in the inclusive range [1, 9], and n may represent a negative number.

You want to maximize n's numerical value by inserting x anywhere in the decimal representation of n. You cannot insert x to the left of the negative sign.

For example, if n = 73 and x = 6, it would be best to insert it between 7 and 3, making n = 763.

If n = -55 and x = 2, it would be best to insert it before the first 5, making n = -255.

Return a string representing the maximum value of n after the insertion.


Example 1:

Input: n = "99", x = 9
Output: "999"
Explanation: The result is the same regardless of where you insert 9.

Example 2:

Input: n = "-13", x = 2
Output: "-123"
Explanation: You can make n one of {-213, -123, -132}, and the largest of those three is -123.


Constraints:

1 <= n.length <= 10^5
1 <= x <= 9
The digits in n are in the range [1, 9].
n is a valid representation of an integer.
In the case of a negative n, it will begin with '-'.

"""

# V0
# IDEA : GREEDY (insert at the FIRST position that improves the digit)
#
#   inserting x always lengthens the number by one digit, so the magnitude
#   grows; only the digit ORDER decides which placement wins.
#
#   positive n : we want the biggest magnitude -> insert x just before the
#     first digit that is STRICTLY SMALLER than x (skip while d >= x).
#     inserting earlier than that would push a smaller digit into a higher
#     place value; later would waste x's advantage.
#
#   negative n : we want the SMALLEST magnitude -> insert x just before the
#     first digit STRICTLY GREATER than x (skip while d <= x), and never
#     before the '-' sign.
#
#   NOTE : if no such position exists, x is appended at the very end.
#
# time = O(len(n)), space = O(len(n)) for the result string
class Solution(object):
    def maxValue(self, n, x):
        neg = n[0] == '-'
        i = 1 if neg else 0

        while i < len(n):
            d = int(n[i])
            if neg:
                if d > x:
                    break
            else:
                if d < x:
                    break
            i += 1

        return n[:i] + str(x) + n[i:]

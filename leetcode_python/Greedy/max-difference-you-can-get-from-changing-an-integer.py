"""

1432. Max Difference You Can Get From Changing an Integer
Medium

You are given an integer num. You will apply the following steps to num two
separate times:

- Pick a digit x (0 <= x <= 9).
- Pick another digit y (0 <= y <= 9). Note y can be equal to x.
- Replace all the occurrences of x in the decimal representation of num by y.

Let a and b be the two results from applying the operation to num
independently.

Return the max difference between a and b.

Note that neither a nor b may have any leading zeros, and must not be 0.


Example 1:

Input: num = 555
Output: 888
Explanation: The first time pick x = 5 and y = 9 and store the new integer in a.
The second time pick x = 5 and y = 1 and store the new integer in b.
We have now a = 999 and b = 111 and max difference = 888

Example 2:

Input: num = 9
Output: 8
Explanation: The first time pick x = 9 and y = 9 and store the new integer in a.
The second time pick x = 9 and y = 1 and store the new integer in b.
We have now a = 9 and b = 1 and max difference = 8


Constraints:

1 <= num <= 10^8

"""

# V0
# IDEA : GREEDY
#
#  MAX (a):
#     - scan digits left -> right, take the FIRST digit that is not '9'
#       and turn every occurrence of it into '9'
#     - (leading zero is impossible when we only create '9')
#
#  MIN (b):
#     - if the leading digit is not '1' -> turn every occurrence of it into '1'
#       (cannot use '0': that would create a leading zero)
#     - else (leading digit IS '1') -> scan from index 1 for the first digit
#       that is neither '0' nor the leading digit, turn it into '0'
#
# time = O(d)
# space = O(d)
# d = number of digits
class Solution(object):
    def maxDiff(self, num):
        s = str(num)

        # ---- build a (max) ----
        a = s
        for c in s:
            if c != '9':
                a = s.replace(c, '9')
                break

        # ---- build b (min) ----
        b = s
        if s[0] != '1':
            b = s.replace(s[0], '1')
        else:
            for c in s[1:]:
                # NOTE !!! skip '0' (no-op) and skip the leading digit
                #          (turning it to '0' -> leading zero)
                if c != '0' and c != s[0]:
                    b = s.replace(c, '0')
                    break

        return int(a) - int(b)

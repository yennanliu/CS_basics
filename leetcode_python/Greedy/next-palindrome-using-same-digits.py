"""

1842. Next Palindrome Using Same Digits
Hard

You are given a numeric string num, representing a very large palindrome.

Return the smallest palindrome larger than num that can be created by rearranging its digits. If no such palindrome exists, return an empty string "".

A palindrome is a number that reads the same backward as forward.


Example 1:

Input: num = "1221"
Output: "2112"
Explanation: The next palindrome larger than "1221" is "2112".

Example 2:

Input: num = "32123"
Output: ""
Explanation: No palindromes larger than "32123" can be made by rearranging its digits.

Example 3:

Input: num = "45544554"
Output: "54455445"
Explanation: The next palindrome larger than "45544554" is "54455445".


Constraints:

1 <= num.length <= 10^5
num is a palindrome.

"""

# V0
# IDEA : NEXT PERMUTATION OF THE FIRST HALF ONLY, THEN MIRROR
#
#   a palindrome is completely determined by its first half (plus the middle
#   character when the length is odd), and the middle character can never
#   change -- swapping it away would break the digit multiset symmetry.
#
#   so "next palindrome" == "next permutation of the first half", mirrored:
#     1) find the largest i with h[i] < h[i+1]  (i < 0 -> half is the largest
#        arrangement -> no answer -> "")
#     2) swap h[i] with the rightmost h[j] > h[i]
#     3) reverse the suffix h[i+1:] so it is the smallest possible tail
#     4) copy the half back over the mirrored positions
#
#   NOTE : odd lengths need no special case -- the half stops before the middle
#          and step 4 never touches it.
#
# time = O(n), space = O(n)
class Solution(object):
    def nextPalindrome(self, num):
        s = list(num)
        n = len(s)
        h = n // 2

        # 1) rightmost ascent inside the first half
        i = h - 2
        while i >= 0 and s[i] >= s[i + 1]:
            i -= 1
        if i < 0:
            return ""

        # 2) rightmost element bigger than s[i]
        j = h - 1
        while s[j] <= s[i]:
            j -= 1
        s[i], s[j] = s[j], s[i]

        # 3) smallest tail
        s[i + 1:h] = reversed(s[i + 1:h])

        # 4) mirror
        for k in range(h):
            s[n - 1 - k] = s[k]
        return "".join(s)

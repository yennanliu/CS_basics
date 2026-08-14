"""

1903. Largest Odd Number in String
Easy

You are given a string num, representing a large integer. Return the largest-valued odd integer (as a string) that is a non-empty substring of num, or an empty string "" if no odd integer exists.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: num = "52"
Output: "5"
Explanation: The only non-empty substrings are "5", "2", and "52". "5" is the only odd number.

Example 2:

Input: num = "4206"
Output: ""
Explanation: There are no odd numbers in "4206".

Example 3:

Input: num = "35427"
Output: "35427"
Explanation: "35427" is already an odd number.


Constraints:

1 <= num.length <= 10^5
num only consists of digits and does not contain any leading zeros.

"""

# V0
# IDEA : GREEDY (keep the whole prefix up to the LAST odd digit)
#
#   a number is odd iff its LAST digit is odd, so the candidate substrings
#   are exactly num[0 .. i] for every odd digit num[i].
#   among those, longer is always bigger (num has no leading zeros), and
#   for equal length the prefix is fixed - so the best answer is simply
#   the prefix ending at the RIGHTMOST odd digit.
#
#   scan from the right, return on the first odd digit found.
#
# time = O(n), space = O(1) extra (the slice is the returned answer)
class Solution(object):
    def largestOddNumber(self, num):
        for i in range(len(num) - 1, -1, -1):
            if int(num[i]) % 2 == 1:
                return num[:i + 1]
        return ""

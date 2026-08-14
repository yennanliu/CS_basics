"""

2264. Largest 3-Same-Digit Number in String
Easy

You are given a string num representing a large integer. An integer is good if it meets the following conditions:

It is a substring of num with length 3.
It consists of only one unique digit.

Return the maximum good integer as a string or an empty string "" if no such integer exists.

Note:

A substring is a contiguous sequence of characters within a string.
There may be leading zeroes in num or a good integer.


Example 1:

Input: num = "6777133339"
Output: "777"
Explanation: There are two distinct good integers: "777" and "333".
"777" is the largest, so we return "777".

Example 2:

Input: num = "2300019"
Output: "000"
Explanation: "000" is the only good integer.

Example 3:

Input: num = "42352338"
Output: ""
Explanation: No substring of length 3 consists of only one unique digit. Therefore, there are no good integers.


Constraints:

3 <= num.length <= 1000
num only consists of digits.

"""

# V0
# IDEA : ENUMERATE THE 10 CANDIDATES FROM BIG TO SMALL
#
#   a "good" integer can only be one of "999", "888", ... "000".
#   they are already in decreasing numeric order, so probe them in that order
#   and return the first one that appears as a substring.
#
#   NOTE : leading zeros are allowed, so "000" is a legal answer.
#
# time = O(10 * n), space = O(1)
class Solution(object):
    def largestGoodInteger(self, num):
        for d in range(9, -1, -1):
            cand = str(d) * 3
            if cand in num:
                return cand
        return ""

"""

1796. Second Largest Digit in a String
Easy

Given an alphanumeric string s, return the second largest numerical digit that appears in s, or -1 if it does not exist.

An alphanumeric string is a string consisting of lowercase English letters and digits.

Example 1:

Input: s = "dfa12321afd"
Output: 2
Explanation: The digits that appear in s are [1, 2, 3]. The second largest digit is 2.

Example 2:

Input: s = "abc1111"
Output: -1
Explanation: The digits that appear in s are [1]. There is no second largest digit.

Constraints:

1 <= s.length <= 500
s consists of only lowercase English letters and digits.

"""

# V0
# IDEA : TRACK THE TWO LARGEST DISTINCT DIGITS IN ONE PASS
#
#   keep `first` = largest digit seen, `second` = largest strictly below it.
#   on a new digit v :
#     - v > first  -> second = first, first = v
#     - second < v < first -> second = v
#   duplicates of `first` are skipped by the strict comparisons, which is what
#   "second largest digit" means here.
#
# time = O(n), space = O(1)
class Solution(object):
    def secondHighest(self, s):
        first, second = -1, -1
        for c in s:
            if not c.isdigit():
                continue
            v = int(c)
            if v > first:
                first, second = v, first
            elif second < v < first:
                second = v
        return second

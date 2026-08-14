"""

2259. Remove Digit From Number to Maximize Result
Easy

You are given a string number representing a positive integer and a character digit.

Return the resulting string after removing exactly one occurrence of digit from number such that the value of the resulting string in decimal form is maximized. The test cases are generated such that digit occurs at least once in number.


Example 1:

Input: number = "123", digit = "3"
Output: "12"
Explanation: There is only one '3' in "123". After removing '3', the result is "12".

Example 2:

Input: number = "1231", digit = "1"
Output: "231"
Explanation: We can remove the first '1' to get "231" or remove the second '1' to get "123".
Since 231 > 123, we return "231".

Example 3:

Input: number = "551", digit = "5"
Output: "51"
Explanation: We can remove either the first or second '5' from "551".
Both result in the string "51".


Constraints:

2 <= number.length <= 100
number consists of digits from '1' to '9'.
digit is a digit from '1' to '9'.
digit occurs at least once in number.

"""

# V0
# IDEA : GREEDY (all candidates have the same length -> compare left to right)
#
#   removing any single occurrence of `digit` keeps the length at n-1, so the
#   bigger result is simply the lexicographically bigger string.
#
#   scan the occurrences left to right:
#     - if number[i] == digit and number[i+1] > digit, deleting index i pulls a
#       BIGGER digit into position i -> that is strictly best, stop right there.
#     - otherwise every occurrence is followed by a <= digit, so deleting the
#       LAST occurrence is optimal (it disturbs the smallest suffix).
#
#   NOTE : the "first occurrence followed by something larger" rule must be
#          checked in order - the earliest such index wins.
#
# time = O(n), space = O(n)
class Solution(object):
    def removeDigit(self, number, digit):
        n = len(number)
        last = -1
        for i in range(n):
            if number[i] != digit:
                continue
            last = i
            if i + 1 < n and number[i + 1] > digit:
                break
        return number[:last] + number[last + 1:]

"""

1118. Number of Days in a Month
Easy

Given a year year and a month month, return the number of days of that month.


Example 1:

Input: year = 1992, month = 7
Output: 31

Example 2:

Input: year = 2000, month = 2
Output: 29

Example 3:

Input: year = 1900, month = 2
Output: 28


Constraints:

1583 <= year <= 2100
1 <= month <= 12

"""

# V0
# IDEA : MATH (leap year rule + month length lookup table)
#
#   a year is a leap year when it is divisible by 4 but NOT by 100,
#   or when it is divisible by 400.
#     -> 2000 is a leap year (400 | 2000)
#     -> 1900 is NOT      (100 | 1900, 400 does not divide it)
#
#   every month except February has a fixed length, so keep a 1-indexed
#   table and only patch index 2.
#   NOTE : the table is padded with a leading 0 so that days[month]
#          can be indexed directly with the 1-based month.
#
# time = O(1), space = O(1)
class Solution(object):
    def numberOfDays(self, year, month):
        leap = (year % 4 == 0 and year % 100 != 0) or (year % 400 == 0)
        days = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
        if leap:
            days[2] = 29
        return days[month]

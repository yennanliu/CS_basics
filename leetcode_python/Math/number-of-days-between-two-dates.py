"""

1360. Number of Days Between Two Dates
Easy

Write a program to count the number of days between two dates.

The two dates are given as strings, their format is YYYY-MM-DD as shown in the examples.


Example 1:

Input: date1 = "2019-06-29", date2 = "2019-06-30"
Output: 1

Example 2:

Input: date1 = "2020-01-15", date2 = "2019-12-31"
Output: 15


Constraints:

The given dates are valid dates between the years 1971 and 2100.

"""

# V0
# IDEA : MATH - convert each date to a "days since 1971-01-01" ordinal,
#        then take abs difference
#
#  - leap year : divisible by 4, except centuries not divisible by 400
#  - Feb has 28 + is_leap days
#
# time = O(y + m)
# space = O(1)
# y = years since 1971, m = month index
class Solution(object):
    def daysBetweenDates(self, date1, date2):

        def isLeapYear(year):
            return year % 4 == 0 and (year % 100 != 0 or year % 400 == 0)

        def daysInMonth(year, month):
            days = [31, 28 + int(isLeapYear(year)), 31, 30, 31, 30,
                    31, 31, 30, 31, 30, 31]
            return days[month - 1]

        def calcDays(date):
            year, month, day = map(int, date.split("-"))
            days = 0
            for y in range(1971, year):
                days += 365 + int(isLeapYear(y))
            for m in range(1, month):
                days += daysInMonth(year, m)
            return days + day

        return abs(calcDays(date1) - calcDays(date2))


# V1
# IDEA : PYTHON datetime (library one-liner)
# time = O(1)
# space = O(1)
from datetime import datetime


class Solution2(object):
    def daysBetweenDates(self, date1, date2):
        d1 = datetime.strptime(date1, "%Y-%m-%d")
        d2 = datetime.strptime(date2, "%Y-%m-%d")
        return abs((d1 - d2).days)

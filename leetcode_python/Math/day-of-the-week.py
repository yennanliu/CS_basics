"""

1185. Day of the Week
Easy

Given a date, return the corresponding day of the week for that date.

The input is given as three integers representing the day, month and year respectively.

Return the answer as one of the following values {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}.

Note: January 1, 1971 was a Friday.


Example 1:

Input: day = 31, month = 8, year = 2019
Output: "Saturday"

Example 2:

Input: day = 18, month = 7, year = 1999
Output: "Sunday"

Example 3:

Input: day = 15, month = 8, year = 1993
Output: "Sunday"


Constraints:

The given dates are valid dates between the years 1971 and 2100.

"""

# V0
# IDEA : ZELLER-STYLE DAY COUNTING (days elapsed since a known anchor)
#
#   anchor : 1971-01-01 was a Friday.
#   count the days between the anchor and the query date, then take mod 7 :
#     - whole years  : 365 + 1 for every leap year passed
#     - whole months : cumulative month lengths of the query year
#                      (+1 if the query year is a leap year and month > 2)
#     - days         : day - 1
#
#   NOTE : leap rule = divisible by 4 and not by 100, or divisible by 400.
#          the +1 for February is only added once we are PAST February.
#
# time = O(year), space = O(1)
class Solution(object):
    def dayOfTheWeek(self, day, month, year):
        def is_leap(y):
            return (y % 4 == 0 and y % 100 != 0) or (y % 400 == 0)

        month_days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

        # days elapsed since 1971-01-01
        total = 0
        for y in range(1971, year):
            total += 366 if is_leap(y) else 365
        for m in range(month - 1):
            total += month_days[m]
        if month > 2 and is_leap(year):
            total += 1
        total += day - 1

        # 1971-01-01 was a Friday
        names = ["Friday", "Saturday", "Sunday", "Monday",
                 "Tuesday", "Wednesday", "Thursday"]
        return names[total % 7]

"""

1154. Day of the Year
Easy

Given a string date representing a Gregorian calendar date formatted as YYYY-MM-DD,
return the day number of the year.


Example 1:

Input: date = "2019-01-09"
Output: 9
Explanation: Given date is the 9th day of the year in 2019.

Example 2:

Input: date = "2019-02-10"
Output: 41


Constraints:

date.length == 10
date[4] == date[7] == '-', and all other date[i]'s are digits
date represents a calendar date between Jan 1st, 1900 and Dec 31st, 2019.

"""

# V0
# IDEA : MATH (sum days of all previous months + day)
#        leap year : divisible by 400, OR divisible by 4 but not by 100
# time = O(1)
# space = O(1)
class Solution(object):
    def dayOfYear(self, date):
        y, m, d = int(date[:4]), int(date[5:7]), int(date[8:])
        days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
        if y % 400 == 0 or (y % 4 == 0 and y % 100 != 0):
            days[1] = 29
        return sum(days[: m - 1]) + d

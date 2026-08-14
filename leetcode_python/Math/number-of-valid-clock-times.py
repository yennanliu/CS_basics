"""

2437. Number of Valid Clock Times
Easy

You are given a string of length 5 called time, representing the current time on a digital clock in the format "hh:mm". The earliest possible time is "00:00" and the latest possible time is "23:59".

In the string time, the digits represented by the ? symbol are unknown, and must be replaced with a digit from 0 to 9.

Return an integer answer, the number of valid clock times that can be created by replacing every ? with a digit from 0 to 9.


Example 1:

Input: time = "?5:00"
Output: 2
Explanation: We can replace the ? with either a 0 or 1, producing "05:00" or "15:00". Note that we cannot replace it with a 2, since the time "25:00" is invalid. In total, we have two choices.

Example 2:

Input: time = "0?:0?"
Output: 100
Explanation: Each ? can be replaced by any digit from 0 to 9, so we have 100 total choices.

Example 3:

Input: time = "??:??"
Output: 1440
Explanation: There are 24 possible choices for the hours, and 60 possible choices for the minutes. In total, we have 24 * 60 = 1440 choices.


Constraints:

time is a valid string of length 5 in the format "hh:mm".
"00" <= hh <= "23"
"00" <= mm <= "59"
Some of the digits might be replaced with '?' and need to be replaced with digits from 0 to 9.

"""

# V0
# IDEA : ENUMERATION (hours and minutes are independent -> multiply the counts)
#
#   the hour field ("hh") and the minute field ("mm") never constrain each
#   other, so count how many of 00..23 match the hh pattern, how many of
#   00..59 match the mm pattern, and multiply.
#   a 2-char pattern p matches value v iff, for each position, p is '?'
#   or p equals the corresponding digit of v.
#
# time = O(24 + 60) = O(1), space = O(1)
class Solution(object):
    def countTime(self, time):
        def count(pat, limit):
            c = 0
            for v in range(limit):
                d0, d1 = v // 10, v % 10
                if (pat[0] == '?' or int(pat[0]) == d0) and \
                   (pat[1] == '?' or int(pat[1]) == d1):
                    c += 1
            return c

        return count(time[:2], 24) * count(time[3:], 60)

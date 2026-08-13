"""

1344. Angle Between Hands of a Clock
Medium

Given two numbers, hour and minutes, return the smaller angle (in degrees)
formed between the hour and the minute hand.

Answers within 10^-5 of the actual value will be accepted as correct.


Example 1:

Input: hour = 12, minutes = 30
Output: 165

Example 2:

Input: hour = 3, minutes = 30
Output: 75

Example 3:

Input: hour = 3, minutes = 15
Output: 7.5


Constraints:

1 <= hour <= 12
0 <= minutes <= 59

"""

# V0
# IDEA: MATH
#
#  - minute hand: 360 / 60 = 6 degrees per minute
#  - hour hand:   360 / 12 = 30 degrees per hour,
#                 PLUS it drifts 30 / 60 = 0.5 degree per minute
#  - hour = 12 behaves like hour = 0 (12 * 30 = 360 == 0 mod 360),
#    the `min(diff, 360 - diff)` at the end handles it automatically
#
# time = O(1)
# space = O(1)
class Solution(object):
    def angleClock(self, hour, minutes):
        h_deg = 30.0 * (hour % 12) + 0.5 * minutes
        m_deg = 6.0 * minutes

        diff = abs(h_deg - m_deg)
        return min(diff, 360.0 - diff)

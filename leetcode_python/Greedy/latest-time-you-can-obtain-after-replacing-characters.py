"""

3114. Latest Time You Can Obtain After Replacing Characters
Easy

You are given a string s representing a 12-hour format time where some of the digits (possibly none) are replaced with a "?".

12-hour times are formatted as "HH:MM", where HH is between 00 and 11, and MM is between 00 and 59. The earliest 12-hour time is 00:00, and the latest is 11:59.

You have to replace all the "?" characters in s with digits such that the time we obtain by the resulting string is a valid 12-hour format time and is the latest possible.

Return the resulting string.


Example 1:

Input: s = "1?:?4"
Output: "11:54"
Explanation: The latest 12-hour format time we can achieve by replacing "?" characters is "11:54".

Example 2:

Input: s = "0?:5?"
Output: "09:59"
Explanation: The latest 12-hour format time we can achieve by replacing "?" characters is "09:59".


Constraints:

s.length == 5
s[2] is equal to the character ":".
All characters except s[2] are digits or "?" characters.
The input is generated such that there is at least one time between "00:00" and "11:59" that you can obtain after replacing the "?" characters.

"""

# V0
# IDEA : ONLY 720 VALID TIMES EXIST — TRY THEM FROM THE LATEST DOWN
#
#   hours run 00..11 and minutes 00..59, so the whole search space is 720
#   strings. walking them from "11:59" backwards and returning the first one
#   compatible with the mask is trivially correct, with no per-digit case
#   analysis to get wrong (the tens and units digits of the hour interact,
#   which is where hand-rolled greedy versions usually slip).
#
#   compatible means : every non-'?' character of s matches.
#
# time = O(1)  (720 candidates x 5 characters), space = O(1)
class Solution(object):
    def findLatestTime(self, s):
        for h in range(11, -1, -1):
            for m in range(59, -1, -1):
                cand = "%02d:%02d" % (h, m)
                if all(a == '?' or a == b for a, b in zip(s, cand)):
                    return cand
        return ""

"""

1604. Alert Using Same Key-Card Three or More Times in a One Hour Period
Medium

LeetCode company workers use key-cards to unlock office doors. Each time a worker uses their key-card, the security system saves the worker's name and the time when it was used. The system emits an alert if any worker uses the key-card three or more times in a one-hour period.

You are given a list of strings keyName and keyTime where [keyName[i], keyTime[i]] corresponds to a person's name and the time when their key-card was used in a single day.

Access times are given in the 24-hour time format "HH:MM", such as "23:51" and "09:49".

Return a list of unique worker names who received an alert for frequent keycard use. Sort the names in ascending order alphabetically.

Notice that "10:00" - "11:00" is considered to be within a one-hour period, while "22:51" - "23:52" is not considered to be within a one-hour period.


Example 1:

Input: keyName = ["daniel","daniel","daniel","luis","luis","luis","luis"], keyTime = ["10:00","10:40","11:00","09:00","11:00","13:00","15:00"]
Output: ["daniel"]
Explanation: "daniel" used the keycard 3 times in a one-hour period ("10:00","10:40", "11:00").

Example 2:

Input: keyName = ["alice","alice","alice","bob","bob","bob","bob"], keyTime = ["12:01","12:00","18:00","21:00","21:20","21:30","23:00"]
Output: ["bob"]
Explanation: "bob" used the keycard 3 times in a one-hour period ("21:00","21:20", "21:30").


Constraints:

1 <= keyName.length, keyTime.length <= 10^5
keyName.length == keyTime.length
keyTime[i] is in the format "HH:MM".
[keyName[i], keyTime[i]] is unique.
1 <= keyName[i].length <= 10
keyName[i] contains only lowercase English letters.

"""

# V0
# IDEA : GROUP BY NAME + SORT + FIXED-WIDTH WINDOW OF 3
#
#   convert "HH:MM" to minutes so times compare as plain ints, bucket them
#   per worker, and sort each bucket.
#
#   key observation : if ANY one-hour window holds >= 3 swipes, then some
#   window of exactly 3 CONSECUTIVE sorted swipes fits in an hour. So a
#   single scan checking ts[i+2] - ts[i] <= 60 is enough -- no sliding
#   window bookkeeping needed.
#
#   NOTE : the window is inclusive ("10:00" to "11:00" counts), so the
#          test is <= 60, not < 60.
#
# time = O(n log n), space = O(n)
from collections import defaultdict
class Solution(object):
    def alertNames(self, keyName, keyTime):
        times = defaultdict(list)
        for i in range(len(keyName)):
            t = keyTime[i]
            times[keyName[i]].append(int(t[:2]) * 60 + int(t[3:]))

        res = []
        for name in times:
            ts = sorted(times[name])
            for i in range(len(ts) - 2):
                if ts[i + 2] - ts[i] <= 60:
                    res.append(name)
                    break
        return sorted(res)

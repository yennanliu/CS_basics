"""

2446. Determine if Two Events Have Conflict
Easy

You are given two arrays of strings that represent two inclusive events that happened on the same day, event1 and event2, where:

event1 = [startTime1, endTime1] and
event2 = [startTime2, endTime2].

Event times are valid 24 hours format in the form of HH:MM.

A conflict happens when two events have some non-empty intersection (i.e., some moment is common to both events).

Return true if there is a conflict between two events. Otherwise, return false.


Example 1:

Input: event1 = ["01:15","02:00"], event2 = ["02:00","03:00"]
Output: true
Explanation: The two events intersect at time 2:00.

Example 2:

Input: event1 = ["01:00","02:00"], event2 = ["01:20","03:00"]
Output: true
Explanation: The two events intersect starting from 01:20 to 02:00.

Example 3:

Input: event1 = ["10:00","11:00"], event2 = ["14:00","15:00"]
Output: false
Explanation: The two events do not intersect.


Constraints:

event1.length == event2.length == 2
event1[i].length == event2[i].length == 2
startTime1 <= endTime1
startTime2 <= endTime2
All the event times follow the HH:MM format.

"""

# V0
# IDEA : INTERVAL OVERLAP ON THE RAW STRINGS (HH:MM is zero-padded -> lexicographic order == chronological order)
#
#   two closed intervals [s1, e1] and [s2, e2] intersect iff
#     s1 <= e2 AND s2 <= e1
#   i.e. NOT (one starts after the other ends).
#   NOTE : because HH and MM are always 2 digits, plain string comparison
#          already orders the times correctly - no parsing to minutes needed.
#
# time = O(1), space = O(1)
class Solution(object):
    def haveConflict(self, event1, event2):
        return event1[0] <= event2[1] and event2[0] <= event1[1]


# V0-1
# IDEA : PARSE TO MINUTES, THEN COMPARE INTEGERS
#
#   map "HH:MM" to minutes since midnight and run the same closed-interval
#   overlap test on plain integers.
#   NOTE : this is the version that survives a format change -- the string
#          comparison in V0 only works because HH and MM are zero padded, so
#          it would silently break on "9:30" vs "10:00".
#
# time = O(1), space = O(1)
class Solution(object):
    def haveConflict(self, event1, event2):
        def to_min(hhmm):
            h, m = hhmm.split(":")
            return int(h) * 60 + int(m)

        s1, e1 = to_min(event1[0]), to_min(event1[1])
        s2, e2 = to_min(event2[0]), to_min(event2[1])
        return s1 <= e2 and s2 <= e1


# V0-2
# IDEA : MATERIALISE THE MINUTES AND INTERSECT THE TWO SETS
#
#   a day holds only 1440 minutes, so we can literally build the set of minutes
#   each event occupies (inclusive on both ends) and ask whether the two sets
#   share anything. this is the definition of "conflict" transcribed as-is,
#   which makes it the brute-force oracle the two O(1) formulas can be tested
#   against.
#
# time = O(1440) = O(1), space = O(1440) = O(1)
class Solution(object):
    def haveConflict(self, event1, event2):
        def slots(ev):
            sh, sm = ev[0].split(":")
            eh, em = ev[1].split(":")
            start = int(sh) * 60 + int(sm)
            end = int(eh) * 60 + int(em)
            return set(range(start, end + 1))

        return len(slots(event1) & slots(event2)) > 0

"""

3386. Button with Longest Push Time
Easy

You are given a 2D array events which represents a sequence of events where a child performs a series of button presses.

Each events[i] = [index_i, time_i] indicates that the button at index index_i was pressed at time time_i.

The array is sorted in increasing order of time.
The time taken to press a button is the difference in time between consecutive button presses. The time for the first button is simply the time at which it was pressed.

Return the index of the button that took the longest time to push. If multiple buttons have the same longest time, return the button with the smallest index.


Example 1:

Input: events = [[1,2],[2,5],[3,9],[1,15]]
Output: 1
Explanation:

Button with index 1 is pressed at time 2.
Button with index 2 is pressed at time 5, so it took 5 - 2 = 3 units of time.
Button with index 3 is pressed at time 9, so it took 9 - 5 = 4 units of time.
Button with index 1 is pressed again at time 15, so it took 15 - 9 = 6 units of time.

Example 2:

Input: events = [[10,5],[1,7]]
Output: 10
Explanation:

Button with index 10 is pressed at time 5, so it took 5 units of time.
Button with index 1 is pressed at time 7, so it took 7 - 5 = 2 units of time.


Constraints:

1 <= events.length <= 1000
events[i] == [index_i, time_i]
1 <= index_i, time_i <= 10^5
The input is generated such that events is sorted in increasing order of time_i.

"""

# V0
# IDEA : ONE PASS ON THE GAPS BETWEEN CONSECUTIVE PRESSES
#
#   the cost of a press is time - (previous time), with the first press
#   costing its own timestamp — i.e. treat "previous time" as 0 to start.
#
#   keep the best (duration, index) pair, and on a tie prefer the smaller
#   index. note the durations are per-press, not accumulated per button :
#   pressing the same button twice does not add its two costs together.
#
# time = O(n), space = O(1)
class Solution(object):
    def buttonWithLongestTime(self, events):
        best_idx = events[0][0]
        best_dur = events[0][1]

        prev = events[0][1]
        for i in range(1, len(events)):
            idx, t = events[i]
            dur = t - prev
            prev = t
            if dur > best_dur or (dur == best_dur and idx < best_idx):
                best_dur = dur
                best_idx = idx
        return best_idx


# V0-1
# IDEA : SORT THE (DURATION, INDEX) PAIRS
#
#   materialise one pair per press -- (-duration, index) -- and sort it.
#   lexicographic order then puts the longest duration first and, among equal
#   durations, the smallest index first, so the answer is simply the head of
#   the sorted list; no explicit tie-break branch is needed.
#
# time  = O(n log n)
# space = O(n)
class Solution(object):
    def buttonWithLongestTime(self, events):
        pairs = []
        prev = 0
        for idx, t in events:
            pairs.append((-(t - prev), idx))
            prev = t
        pairs.sort()
        return pairs[0][1]


# V0-2
# IDEA : BUCKET BY BUTTON INDEX (counting-style sweep, no pair comparisons)
#
#   button indices are bounded (<= 10^5), so keep one slot per index holding
#   the longest single press seen for that button, then sweep the slots in
#   ascending index order.  scanning ascending makes the tie-break implicit :
#   the first index reaching the global best is the smallest one.
#
#   this is the shape you want if the same events had to be re-queried per
#   button, since it keeps the whole per-button profile, not just the winner.
#
# time  = O(n + M), M = largest button index
# space = O(M)
class Solution(object):
    def buttonWithLongestTime(self, events):
        M = max(idx for idx, _ in events)
        best = [-1] * (M + 1)
        prev = 0
        for idx, t in events:
            dur = t - prev
            prev = t
            if dur > best[idx]:
                best[idx] = dur
        top = max(best)
        for i in range(M + 1):
            if best[i] == top:
                return i

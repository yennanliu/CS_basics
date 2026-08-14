"""

630. Course Schedule III
Hard

There are n different online courses numbered from 1 to n.
You are given an array courses where courses[i] = [duration_i, lastDay_i] indicate that
the ith course should be taken continuously for duration_i days and must be finished
before or on lastDay_i.

You will start on the 1st day and you cannot take two or more courses simultaneously.

Return the maximum number of courses that you can take.

Example 1:

Input: courses = [[100,200],[200,1300],[1000,1250],[2000,3200]]
Output: 3
Explanation:
There are totally 4 courses, but you can take 3 courses at most:
First, take the 1st course, it costs 100 days so you will finish it on the 100th day,
and ready to take the next course on the 101st day.
Second, take the 3rd course, it costs 1000 days so you will finish it on the 1100th day,
and ready to take the next course on the 1101st day.
Third, take the 2nd course, it costs 200 days so you will finish it on the 1300th day.
The 4th course cannot be taken now, since you will finish it on the 3300th day,
which exceeds the closed date.

Example 2:

Input: courses = [[1,2]]
Output: 1

Example 3:

Input: courses = [[3,2],[4,3]]
Output: 0

Constraints:

1 <= courses.length <= 10^4
1 <= duration_i, lastDay_i <= 10^4

"""

# V0
# IDEA : GREEDY + MAX HEAP (exchange argument)
#
#   Sort courses by deadline ascending, then take them one by one.
#   Keep a running total of the time spent on the courses taken so far.
#   If adding the current course pushes the total past its deadline, drop the
#   single longest course taken so far (it may be the current one).
#   Dropping the longest keeps the course COUNT the same while freeing the most
#   time, so it can never hurt a later course -> the count stays optimal.
#
#   python's heapq is a MIN heap, so push -duration to simulate a max heap.
#
# time = O(n * log(n))
# space = O(n)
import heapq
class Solution(object):
    def scheduleCourse(self, courses):
        # take the tightest deadlines first
        courses.sort(key=lambda c: c[1])

        pq = []       # max heap (negated durations) of the courses currently taken
        total = 0     # sum of durations currently taken

        for duration, last_day in courses:
            heapq.heappush(pq, -duration)
            total += duration
            if total > last_day:
                # over the deadline -> give up the longest course so far
                total += heapq.heappop(pq)   # popped value is negative

        return len(pq)

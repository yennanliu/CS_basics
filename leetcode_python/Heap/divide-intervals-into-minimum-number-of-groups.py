# https://leetcode.com/problems/divide-intervals-into-minimum-number-of-groups/description/

"""

2406. Divide Intervals Into Minimum Number of Groups
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are given a 2D integer array intervals where intervals[i] = [lefti, righti] represents the inclusive interval [lefti, righti].

You have to divide the intervals into one or more groups such that each interval is in exactly one group, and no two intervals that are in the same group intersect each other.

Return the minimum number of groups you need to make.

Two intervals intersect if there is at least one common number between them. For example, the intervals [1, 5] and [5, 8] intersect.

 

Example 1:

Input: intervals = [[5,10],[6,8],[1,5],[2,3],[1,10]]
Output: 3
Explanation: We can divide the intervals into the following groups:
- Group 1: [1, 5], [6, 8].
- Group 2: [2, 3], [5, 10].
- Group 3: [1, 10].
It can be proven that it is not possible to divide the intervals into fewer than 3 groups.
Example 2:

Input: intervals = [[1,3],[5,6],[8,10],[11,13]]
Output: 1
Explanation: None of the intervals overlap, so we can put all of them in one group.
 

Constraints:

1 <= intervals.length <= 105
intervals[i].length == 2
1 <= lefti <= righti <= 106
 

"""


# V0
class Solution(object):
    def minGroups(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: int
        """
        pass



# V0-1
# IDEA: PQ (gpt)
import heapq

class Solution(object):
    def minGroups(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: int
        """
        intervals.sort()

        pq = []      # stores end times
        ans = 0

        for s, e in intervals:
            # Reuse a group if the previous interval ends before s.
            # Since intervals are inclusive, end < start.
            if pq and pq[0] < s:
                heapq.heappop(pq)

            heapq.heappush(pq, e)
            ans = max(ans, len(pq))

        return ans


# V0-2
# IDEA: PQ (gpt)
import heapq

class Solution(object):
    def minGroups(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: int
        """
        # 1. Sort intervals by start time
        intervals.sort(key=lambda x: x[0])
        
        pq = [] # Min-heap to track the end times of active groups
        
        for s, e in intervals:
            # 2. If the heap is not empty AND the current interval starts strictly after
            # the earliest ending group, we can reuse that group!
            if pq and s > pq[0]:
                heapq.heappop(pq) # Remove the old end time
                
            # 3. Add the current interval's end time.
            # (If we popped above, this replaces it. If we didn't, this creates a new group)
            heapq.heappush(pq, e)
            
        # 4. The number of elements in the heap is the number of groups we had to create
        return len(pq)



# V1


# V2-1
# IDEA: Sorting or Priority Queue
# https://leetcode.com/problems/divide-intervals-into-minimum-number-of-groups/editorial/
class Solution:
    def minGroups(self, intervals: List[List[int]]) -> int:
        # Convert the intervals to two events
        # start as (start, 1) and end as (end + 1, -1)
        events = []

        for interval in intervals:
            events.append((interval[0], 1))  # Start event
            events.append((interval[1] + 1, -1))  # End event (interval[1] + 1)

        # Sort the events first by time, and then by type (1 for start, -1 for end).
        events.sort(key=lambda x: (x[0], x[1]))

        concurrent_intervals = 0
        max_concurrent_intervals = 0

        # Sweep through the events
        for event in events:
            concurrent_intervals += event[1]  # Track currently active intervals
            max_concurrent_intervals = max(
                max_concurrent_intervals, concurrent_intervals
            )  # Update max

        return max_concurrent_intervals



# V2-2
# IDEA: Line Sweep Algorithm With Ordered Container
# https://leetcode.com/problems/divide-intervals-into-minimum-number-of-groups/editorial/
class Solution:
    def minGroups(self, intervals: List[List[int]]) -> int:
        # Use a dictionary to store the points and their counts
        point_to_count = defaultdict(int)

        # Mark the starting and ending points in the dictionary
        for interval in intervals:
            point_to_count[interval[0]] += 1  # Start of an interval
            point_to_count[
                interval[1] + 1
            ] -= 1  # End of an interval (interval[1] + 1)

        concurrent_intervals = 0
        max_concurrent_intervals = 0

        # Iterate over the sorted keys of the dictionary
        for point in sorted(point_to_count.keys()):
            concurrent_intervals += point_to_count[
                point
            ]  # Update currently active intervals
            max_concurrent_intervals = max(
                max_concurrent_intervals, concurrent_intervals
            )  # Update max intervals

        return max_concurrent_intervals

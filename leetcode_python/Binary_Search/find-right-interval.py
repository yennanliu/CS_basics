"""

436. Find Right Interval
Medium
Topics
premium lock icon
Companies
You are given an array of intervals, where intervals[i] = [starti, endi] and each starti is unique.

The right interval for an interval i is an interval j such that startj >= endi and startj is minimized. Note that i may equal j.

Return an array of right interval indices for each interval i. If no right interval exists for interval i, then put -1 at index i.

 

Example 1:

Input: intervals = [[1,2]]
Output: [-1]
Explanation: There is only one interval in the collection, so it outputs -1.
Example 2:

Input: intervals = [[3,4],[2,3],[1,2]]
Output: [-1,0,1]
Explanation: There is no right interval for [3,4].
The right interval for [2,3] is [3,4] since start0 = 3 is the smallest start that is >= end1 = 3.
The right interval for [1,2] is [2,3] since start1 = 2 is the smallest start that is >= end2 = 2.
Example 3:

Input: intervals = [[1,4],[2,3],[3,4]]
Output: [-1,2,-1]
Explanation: There is no right interval for [1,4] and [3,4].
The right interval for [2,3] is [3,4] since start2 = 3 is the smallest start that is >= end1 = 3.
 

Constraints:

1 <= intervals.length <= 2 * 104
intervals[i].length == 2
-106 <= starti <= endi <= 106
The start point of each interval is unique.
 


"""

# V0
# IDEA: BINRARY SEARCH (gpt)
class Solution(object):
    def findRightInterval(self, intervals):
        n = len(intervals)

        # (start, original_index)
        starts = []
        for i in range(n):
            starts.append((intervals[i][0], i))

        starts.sort()

        res = [-1] * n

        for i in range(n):
            target = intervals[i][1]

            # Binary search for first start >= target
            left, right = 0, n - 1
            ans = -1

            while left <= right:
                mid = (left + right) // 2

                if starts[mid][0] >= target:
                    ans = starts[mid][1]
                    right = mid - 1
                else:
                    left = mid + 1

            res[i] = ans

        return res



# V0-1
# IDEA: BINRARY SEARCH (GEMINI)
class Solution(object):
    def findRightInterval(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: List[int]
        """
        n = len(intervals)
        res = [-1] * n
        
        # Step 1: Store intervals with their original index: [start, end, original_idx]
        indexed_intervals = []
        for i in range(n):
            indexed_intervals.append([intervals[i][0], intervals[i][1], i])
            
        # Step 2: Sort based on the start time (index 0)
        indexed_intervals.sort(key=lambda x: x[0])
        
        # Step 3: Iterate through each original interval and binary search for the right interval
        for i in range(n):
            target_end = intervals[i][1]
            
            # Manual Binary Search (Lower Bound Search)
            left = 0
            right = n - 1
            best_idx = -1
            
            while left <= right:
                mid = left + (right - left) // 2
                
                # If the current mid interval's start is valid (>= target_end)
                if indexed_intervals[mid][0] >= target_end:
                    best_idx = indexed_intervals[mid][2] # Record its original index
                    right = mid - 1                     # Try to find an even smaller valid start
                else:
                    left = mid + 1                      # Too small, move right
                    
            res[i] = best_idx
            
        return res


# V0
# IDEA: SORT + HASHMAP (TLE) (gpt)
class Solution(object):
    def findRightInterval(self, intervals):
        n = len(intervals)

        res = [-1] * n

        # Make a copy
        # NOTE !!! how we make copy in py
        intervals_cache = intervals[:]

        # Sort by start
        intervals.sort(key=lambda x: x[0])

        # sorted index -> original index
        idx_map = {}

        for i in range(n):
            for j in range(n):
                if intervals[i][0] == intervals_cache[j][0]:
                    idx_map[i] = j
                    break

        for i in range(n):
            for j in range(i + 1, n):
                if intervals[j][0] >= intervals[i][1]:
                    res[idx_map[i]] = idx_map[j]
                    break

        return res



# V0-1
# IDEA: binary search (gemini)
import bisect

class Solution(object):
    def findRightInterval(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: List[int]
        """
        n = len(intervals)
        res = [-1] * n
        
        # Step 1: Create a sorted list of lists containing [start_val, original_idx]
        # This preserves the original index mapping securely without a messy dictionary lookup
        starts = sorted([[interval[0], i] for i, interval in enumerate(intervals)], key=lambda x: x[0])
        
        # Step 2: Extract just the sorted start values to use with Python's bisect tool
        just_starts = [x[0] for x in starts]
        
        # Step 3: Iterate over the original intervals and binary search for their right interval
        for i, interval in enumerate(intervals):
            end_val = interval[1]
            
            # Find the smallest start value that is >= the current interval's end value
            idx = bisect.bisect_left(just_starts, end_val)
            
            # If a valid index is found inside the bounds of the list
            if idx < n:
                # Map the sorted index back to its original index position
                res[i] = starts[idx][1]
                
        return res


# V1 
# http://bookshadow.com/weblog/2016/10/30/leetcode-find-right-interval/
# Definition for an interval.
# class Interval(object):
#     def __init__(self, s=0, e=0):
#         self.start = s
#         self.end = e
class Solution(object):
    def findRightInterval(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: List[int]
        """
        invs = sorted((x.start, i) for i, x in enumerate(intervals))
        ans = []
        for x in intervals:
            idx = bisect.bisect_right( invs, (x.end,) )
            ans.append(invs[idx][1] if idx < len(intervals) else -1)
        return ans
        
# V2 
# Time:  O(nlogn)
# Space: O(n)
import bisect
class Solution(object):
    def findRightInterval(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: List[int]
        """
        sorted_intervals = sorted((interval.start, i) for i, interval in enumerate(intervals))
        result = []
        for interval in intervals:
            idx = bisect.bisect_left(sorted_intervals, (interval.end,))
            result.append(sorted_intervals[idx][1] if idx < len(sorted_intervals) else -1)
        return result

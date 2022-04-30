"""

LC 759

We are given a list schedule of employees, which represents the working time for each employee.

Each employee has a list of non-overlapping Intervals, and these intervals are in sorted order.

Return the list of finite intervals representing common, positive-length free time for all employees, also in sorted order.

Example 1:
Input: schedule = [[[1,2],[5,6]],[[1,3]],[[4,10]]]
Output: [[3,4]]
Explanation:
There are a total of three employees, and all common
free time intervals would be [-inf, 1], [3, 4], [10, inf].
We discard any intervals that contain inf as they aren't finite.

Example 2:

Input: schedule = [[[1,3],[6,7]],[[2,4]],[[2,5],[9,12]]]
Output: [[5,6],[7,9]]
(Even though we are representing Intervals in the form [x, y], the objects inside are Intervals, not lists or arrays. For example, schedule[0][0].start = 1, schedule[0][0].end = 2, and schedule[0][0][0] is not defined.)

Also, we wouldn't include intervals like [5, 5] in our answer, as they have zero length.

Note:

schedule and schedule[i] are lists with lengths in range [1, 50].
0 <= schedule[i].start < schedule[i].end <= 10^8.

"""

# V0

# V1
# IDEA : heapq
# https://leetcode.com/problems/employee-free-time/discuss/1805842/Python-Solution
class Solution:
    def employeeFreeTime(self, schedule: '[[Interval]]') -> '[Interval]':
        pq = []
        for i, s in enumerate(schedule):
            heapq.heappush(pq, (s[0].start, i, 0))
        prev = pq[0][0]
        res = []
        while pq:
            s, i, j = heapq.heappop(pq)
            if s > prev:
                res.append(Interval(prev, s))
            prev = max(prev, schedule[i][j].end)
            if j + 1 < len(schedule[i]):
                heapq.heappush(pq, (schedule[i][j + 1].start, i, j + 1))
        return res

# V1'
# IDEA : SORT by start
# https://leetcode.com/problems/employee-free-time/discuss/408522/Python-sort-by-start
class Solution:
    def employeeFreeTime(self, schedule: 'list<list<Interval>>') -> 'list<Interval>':
        
        # sort all intervals by start time
        intervals = []
        for timeSlots in schedule:
            for timeSlot in timeSlots:
                intervals.append(timeSlot)
        
        intervals.sort(key= lambda x: x.start)
        
        # Maintain left and right pointer pointing to the interval
        left = right = 0
        commonFree = []
        
        # iterate through intervals with left pointer
        # if left.start > right.end, append the common free time to the ans
        # else, assign right to the lastest-ended intervals we heve visited so far.
        while left < len(intervals) - 1:
            left += 1
            if intervals[left].start > intervals[right].end:
                commonFree.append(Interval(intervals[right].end,intervals[left].start))
                right = left
            else:
                if intervals[left].end > intervals[right].end:
                    right = left
        return commonFree

# V1''
# https://leetcode.com/problems/employee-free-time/discuss/877358/Python-O(N-log-K)-heap-solution
# IDEA : 
# I merge the intervals while keeping the heap size less than or equal to K. It is only less than K when I've already popped out all the elements from a certain employee. My solution treats each employees intervals as a queue. pop(0) in python is O(n) so if you wanted you could just convert all of the intervals to deque so your pop(0) is O(1) and it functions as an actual queue but I just pretended pop(0) was O(1) for demonstration purposes.
class Solution:
    def employeeFreeTime(self, schedule: '[[Interval]]') -> '[Interval]':
        k = len(schedule)
        intervals = []
        heap = []
        for i in range(k):
            val = schedule[i].pop(0)
            heappush(heap, ([val.start, val.end], i))
            
        elem = [heap[0][0][0], heap[0][0][1]] 
        while heap:
            val = heappop(heap)
            start, end, idx = val[0][0], val[0][1], val[1]
            if start > elem[1]:
                intervals.append(elem)
                elem = [start, end]
            else:
                elem = [min(elem[0], start), max(elem[1], end)]
                
            if schedule[idx]:
                item = schedule[idx].pop(0)
                heappush(heap, ([item.start, item.end], idx))
        intervals.append(elem)
        out = []
        for i in range(1, len(intervals)):
            out.append(Interval(intervals[i-1][1], intervals[i][0]))
        return out

# V1'''
# IDEA : SORT
# https://leetcode.com/problems/employee-free-time/discuss/1039353/pythonjava-solution
class Solution:
    def employeeFreeTime(self, schedule: '[[Interval]]') -> '[Interval]':
        listSchedule = []
        for i in schedule:
            for j in i:
                listSchedule.append([j.start, j.end])
        listSchedule.sort(key = lambda x: (x[0], x[1]))
        minStart = listSchedule[0][0]
        maxEnd = listSchedule[0][1]
        res = []        
        for curStart, curEnd in listSchedule:
            if curStart <= maxEnd:
                maxEnd = max(maxEnd, curEnd)
            else:
                res.append(Interval(maxEnd, curStart))
                minStart = curStart
                maxEnd = curEnd
        return res

# V1''''
# IDEA : bisect
# https://leetcode.com/problems/employee-free-time/discuss/113142/Python-with-bisect
from bisect import *
class Solution(object):
    def employeeFreeTime(self, avails):
        time=[-float('inf'),float('inf')]
        for p in avails:
            for itv in p:
                s=itv.start
                e=itv.end
                l=bisect_right(time,s)
                r=bisect_left(time,e)
                if l%2:
                    if r%2:
                        time=time[:l]+[s,e]+time[r:]
                    else:
                        time=time[:l]+[s]+time[r:]
                else:
                    if r%2:
                        time=time[:l]+[e]+time[r:]
                    else:
                        time=time[:l]+time[r:]
        ans=[]
        for i in range(3,len(time)-2,2):
            if time[i-1]<time[i]:
                ans.append(Interval(time[i-1],time[i]))
        return ans

# V1'''''
# https://zxi.mytechroad.com/blog/geometry/leetcode-759-employee-free-time/
# https://www.youtube.com/watch?v=4XiZ-mVxvbk
# C++
# // Author: Huahua
# // Running time: 81 ms
# class Solution {
# public:
#     vector<Interval> employeeFreeTime(vector<vector<Interval>>& schedule) {
#       vector<Interval> all;
#       for (const auto intervals : schedule)
#         all.insert(all.end(), intervals.begin(), intervals.end());
#       std::sort(all.begin(), all.end(), 
#                 [](const Interval& a, const Interval& b){
#                   return a.start < b.start;
#                 });
#       vector<Interval> ans;
#       int end = all.front().end;
#       for (const Interval& busy : all) {
#         if (busy.start > end) 
#           ans.emplace_back(end, busy.start);  
#         end = max(end, busy.end);
#       }
#       return ans;
#     }
# };

# V1''''''
# https://www.acwing.com/file_system/file/content/whole/index/content/2808852/
class Solution:
    def employeeFreeTime(self, schedule: '[[Interval]]') -> '[Interval]':
        #TC: O(n log n)
        #SC: O(n) n = len(schedule)

        #Combine the problem of LC56 and LC986 

        allInterval = []
        for schedule_per_em in schedule:
            for interval in schedule_per_em:
                allInterval.append(interval)

        allInterval.sort(key = lambda x: x.start)

        Start = allInterval[0].start
        End = allInterval[0].end

        res = []

        for i in range(1, len(allInterval)):
            if End >= allInterval[i].start:
                End = max(End, allInterval[i].end)
            else:
                Start = allInterval[i].start
                res.append(Interval(End, Start))
                End = allInterval[i].end

        return res

# V1'''''''
# https://github.com/xiaoningning/LeetCode-Python/blob/master/759%20Employee%20Free%20Time.py
# https://github.com/DataStudySquad/LeetCode-5/blob/master/759%20Employee%20Free%20Time.py
from typing import List
import heapq
S = 0
E = 1
class Solution:
    def employeeFreeTime(self, schedule: List[List[List[int]]]) -> List[List[int]]:
        """
        Method 1
        Looking at the head of each list through iterator
        Merge interval of heads, need to sort, then use heap
        After merge, find the open interval
        No need to merge, find the max end time, and compare to the min start time
        Method 2
        Better algorithm to find the open interval
        [s, e], we can think of this as two events: balance++ when time = s, and
        balance-- when time = e. We want to know the regions where balance == 0.
        Similar to meeting rooms II
        """
        cur_max_end = min(
            itv[E]
            for itvs in schedule
            for itv in itvs
        )
        q = []
        for i, itvs in enumerate(schedule):
            # head
            j = 0
            itv = itvs[j]
            heapq.heappush(q, (itv[S], i, j))

        ret = []
        while q:
            _, i, j = heapq.heappop(q)
            itv = schedule[i][j]
            if cur_max_end < itv[S]:
                ret.append([cur_max_end, itv[S]])

            cur_max_end = max(cur_max_end, itv[E])

            # next
            j += 1
            if j < len(schedule[i]):
                itv = schedule[i][j]
                heapq.heappush(q, (itv[S], i, j))

        return ret

    def employeeFreeTime(self, schedule: List[List[List[int]]]) -> List[List[int]]:
        """
        Method 2
        """
        # flatten the nested list
        lst = []
        for itvs in schedule:
            for itv in itvs:
                lst.append([itv[S], S])
                lst.append([itv[E], E])

        lst.sort()
        count = 0
        prev = None
        ret = []
        for t, flag in lst:
            if count == 0 and prev:
                ret.append([prev, t])

            if flag == S:
                count += 1
            else:
                prev = t
                count -= 1

        return ret

    def employeeFreeTime_error(self, schedule: List[List[List[int]]]) -> List[List[int]]:
        """
        Cannot store iterator in the heap to compare
        use index instead
        """
        schedules = list(map(iter, schedule))
        cur_max_end = min(
            itv[E]
            for emp in schedule
            for itv in emp
        )
        q = []
        for emp_iter in schedules:
            itv = next(emp_iter, None)
            if itv:
                heapq.heappush(q, (itv[S], itv, emp_iter))

        ret = []
        while q:
            _, itv, emp_iter = heapq.heappop(q)
            if cur_max_end < itv[S]:
                ret.append([cur_max_end, itv[S]])
            cur_max_end = max(cur_max_end, itv[E])
            itv = next(emp_iter, None)
            if itv:
                heapq.heappush(q, (itv[S], itv, emp_iter))

        return ret

# V2
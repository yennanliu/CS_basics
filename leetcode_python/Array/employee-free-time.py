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

# V1
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

# V1
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
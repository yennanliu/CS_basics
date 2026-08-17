"""

1353. Maximum Number of Events That Can Be Attended
Medium

You are given an array of events where events[i] = [startDayi, endDayi]. Every event i starts at startDayi and ends at endDayi.

You can attend an event i at any day d where startTimei <= d <= endTimei. You can only attend one event at any time d.

Return the maximum number of events you can attend.

 

Example 1:


Input: events = [[1,2],[2,3],[3,4]]
Output: 3
Explanation: You can attend all the three events.
One way to attend them all is as shown.
Attend the first event on day 1.
Attend the second event on day 2.
Attend the third event on day 3.
Example 2:

Input: events= [[1,2],[2,3],[3,4],[1,2]]
Output: 4
 

Constraints:

1 <= events.length <= 105
events[i].length == 2
1 <= startDayi <= endDayi <= 1053

"""

"""
NOTE !!!

LC 1353 VS LC 253

-> there are totoally DIFFERENT LC,
   they look similar, but are asking different things.



The Line Sweep algorithm from **LC 253 (Meeting Rooms II)** 

`fails` for **LC 1353** because the two problems ask completely different 

questions about intervals.

---

### 1. Different Meaning of $[start, end]$

| Feature | LC 253: Meeting Rooms II | LC 1353: Max Events Attended |
| --- | --- | --- |
| **Interval Meaning** | **Continuous Occupancy:** A meeting $[1, 3]$ occupies Day 1, Day 2, AND Day 3. | **Flexibility Window:** An event $[1, 3]$ takes **only 1 day**; you pick *any one* day from $\{1, 2, 3\}$. |
| **Goal** | Find maximum parallel overlaps at any single instant (peak concurrency). | Assign distinct days to maximum number of events (matching / scheduling). |
| **Capacity** | Unlimited room capacity needed at peak hours. | Strict limit of **1 event per day**. |

---

### 2. Concrete Counterexamples

#### Case A: Over-Counting (Line Sweep is Too Optimistic)

* **Events:** `[[1, 1], [1, 1], [1, 1]]`
* **Line Sweep Result:** `3` (because 3 intervals overlap at Day 1).
* **Actual Answer:** `1`
* **Why Line Sweep Fails:** All 3 events require Day 1, but you can only attend **1 event per day**. There are no other days available to spread them out.

#### Case B: Ignoring Day Capacity Limits

* **Events:** `[[1, 3], [1, 3], [1, 3], [1, 3]]`
* **Line Sweep Result:** `4` (max overlap is 4).
* **Actual Answer:** `3`
* **Why Line Sweep Fails:** There are 4 events, but the union of their time windows only offers **3 distinct days** (Day 1, Day 2, Day 3). You can at most attend 3 events.

---

### Summary

Line Sweep measures **"How many intervals cover point $X$?"**

LC 1353 requires asking **"Which event should I commit to today so I don't waste future days?"** This element of choice across days makes LC 1353 a **Greedy + Priority Queue** problem, not a concurrency count problem.


"""

# V0
class Solution(object):
    def maxEvents(self, events):
        """
        :type events: List[List[int]]
        :rtype: int
        """
        pass

# V0-1
# IDEA: sweep line (scan line) + PQ (gpt)
"""
Core idea:


1. Sort events by `start` day.

2. Iterate through the days.

3. Add all events that start today into a min-heap 
   (ordered by end day).

4. Remove events that have already expired.

5. Attend the event that ends the earliest.

"""
import heapq

class Solution(object):
    def maxEvents(self, events):

        # 1. Sort events by their start day
        #events.sort(key=lambda x: x[0])
        events.sort()


        """
        NOTE !!!


        pq: Min-heap to store the `end days` of available events

        e.g.

        [end_d_1, end_d_2, ....]

        """
        pq = []          # min-heap of end days
        i = 0
        day = 0
        ans = 0
        n = len(events)

        while i < n or pq:

            if not pq:
                day = events[i][0]

            while i < n and events[i][0] == day:
                heapq.heappush(pq, events[i][1])
                i += 1

            while pq and pq[0] < day:
                heapq.heappop(pq)

            if pq:
                heapq.heappop(pq)
                ans += 1
                day += 1

        return ans


# V0-2
# IDEA: sweep line (scan line) + PQ (gemini)
import heapq

class Solution(object):
    def maxEvents(self, events):
        """
        :type events: List[List[int]]
        :rtype: int
        """
        # 1. Sort events by their start day
        events.sort(key=lambda x: x[0])
        

        """
        NOTE !!!


        pq: Min-heap to store the `end days` of available events

        e.g.

        [end_d_1, end_d_2, ....]

        """
        pq = []  # Min-heap to store the end days of available events
        max_events = 0
        day = 0
        i = 0
        n = len(events)
        
        # Keep processing as long as there are events left to add, or events in the heap
        while i < n or pq:
            # If the heap is empty, fast-forward the current day to the next event's start day
            if not pq:
                day = events[i][0]
            
            # Push all events that start on or before the current day into the heap
            while i < n and events[i][0] <= day:
                heapq.heappush(pq, events[i][1])
                i += 1
            
            # Remove all events from the heap that have already expired (end day < current day)
            while pq and pq[0] < day:
                heapq.heappop(pq)
            
            # If there are valid events left, attend the one that ends earliest
            if pq:
                heapq.heappop(pq)  # Attend it!
                max_events += 1
                day += 1           # Move to the next day
                
        return max_events


# V0
# IDEA : PRIORITY QUEUE
# NOTE !!!
# We just need to attend d where startTimei <= d <= endTimei, then we CAN attend the meeting
# startTimei <= d <= endTimei. You can only attend one event at any time d.
# time = O(D + n log n), D = day range (100000), n = len(events)
# space = O(n)
class Solution:
    def maxEvents(self, events: List[List[int]]) -> int:
        # algorithm: greedy+heap
        # step1: loop from min to max day
        # step2: each iteration put the candidates in the heap
        # step3: each iteration eliminate the ineligibility ones from the heap
        # step4: each iteration choose one event attend if it is possible
        # time = O(D + n log n), D = day range (100000), n = len(events)
        # space = O(n)
        events.sort(key = lambda x: -x[0])
        h = []
        ans = 0
        minDay = 1 #events[-1][0]
        maxDay = 100001 #max(x[1] for x in events) + 1
        for day in range(minDay, maxDay):
            # add all days that can start today
            while events and events[-1][0] <= day:
                heapq.heappush(h, events.pop()[1])
            
            # remove all days that cannot start
            while h and h[0]<day:
                heapq.heappop(h)
            
            # if can attend meeting
            if h:
                heapq.heappop(h)
                ans += 1            
        return ans

# V0'
# IDEA : PRIORITY QUEUE
# NOTE !!!
# We just need to attend d where startTimei <= d <= endTimei, then we CAN attend the meeting
# startTimei <= d <= endTimei. You can only attend one event at any time d.
# time = O(D + n log n), D = day range (100000), n = len(events)
# space = O(n)
class Solution:
    def maxEvents(self, events):
        events.sort(key = lambda x: (-x[0], -x[1]))
        endday = []
        ans = 0
        for day in range(1, 100001, 1):
            # check if events is not null and  events start day = day (events[-1][0] == day)
            # if above conditions are True, we insert "events.pop()[1]" to endday 
            while events and events[-1][0] == day:
                heapq.heappush(endday, events.pop()[1])
            # check if endday is not null, if first day in endday < day, then we pop its element
            while endday and endday[0] < day:
                heapq.heappop(endday)
            # if there is still remaining elements in endday -> means we CAN atten the meeting, so ans += 1 
            if endday:
                ans += 1
                heapq.heappop(endday)
        return  ans

# V1
# IDEA : PRIORITY QUEUE
# https://blog.csdn.net/qq_42791848/article/details/109575370
# time = O(D + n log n), D = day range (100000), n = len(events)
# space = O(n)
class Solution:
    def maxEvents(self, events):
        events.sort(key = lambda x: (-x[0], -x[1]))
        endday = []
        ans = 0
        for day in range(1, 100001, 1):
            while events and events[-1][0] == day:
                heapq.heappush(endday, events.pop()[1])
            while endday and endday[0] < day:
                heapq.heappop(endday)
            if endday:
                ans += 1
                heapq.heappop(endday)
        return  ans

# V1''
# IDEA : PRIORITY QUEUE
# https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/discuss/510263/JavaC%2B%2BPython-Priority-Queue
# IDEA :
# Sort events increased by start time.
# Priority queue pq keeps the current open events.
# Iterate from the day 1 to day 100000,
# Each day, we add new events starting on day d to the queue pq.
# Also we remove the events that are already closed.
# Then we greedily attend the event that ends soonest.
# If we can attend a meeting, we increment the result res.
#
# Complexity
# time = O(d + n log n), where d is the range of A[i][1]
# space = O(n)
class Solution(object):
    def maxEvents(self, A):
            # both of below work
            #A.sort(reverse=True)
            A.sort(key = lambda x : (-x[0], -x[1]))
            h = []
            res = d = 0
            while A or h:
                if not h:
                    d = A[-1][0]
                while A and A[-1][0] <= d:
                    heapq.heappush(h, A.pop()[1])
                heapq.heappop(h)
                res += 1
                d += 1
                while h and h[0] < d:
                    heapq.heappop(h)
            return res

# V1''''
# IDEA : HEAP
# https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/discuss/1799845/Python-Heap
# time = O(n log n + D), n = len(events), D = max end day
# space = O(n)
class Solution:
    def maxEvents(self, events: List[List[int]]) -> int:
        events.sort()
        heap = []
        n = max(events, key=lambda x: x[1])[1]
        
        cnt = 0
        i = 0
        for day in range(1, n+1):
            while i < len(events) and events[i][0] == day:
                heappush(heap, events[i][1])
                i += 1

            while heap and heap[0] < day:
                heappop(heap)

            if heap:
                curr = heappop(heap)
                cnt += 1

        return cnt

# V1'''''
# IDEA : HEAP + GREEDY
# https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/discuss/1414435/Python-Greedy%2BHeap
# time = O(D + n log n), D = day range, n = len(events)
# space = O(n)
class Solution:
    def maxEvents(self, events: List[List[int]]) -> int:
        # algorithm: greedy+heap
        # step1: loop from min to max day
        # step2: each iteration put the candidates in the heap
        # step3: each iteration eliminate the ineligibility ones from the heap
        # step4: each iteration choose one event attend if it is possible
        # time = O(D + n log n), D = day range, n = len(events)
        # space = O(n)
        events.sort(key = lambda x: -x[0])
        h = []
        att = 0
        minDay, maxDay = events[-1][0], max(events, key=lambda x:x[1])[1]+1
        for day in range(minDay, maxDay):
            # add all days that can start today
            while events and events[-1][0]<=day:
                heapq.heappush(h, events.pop()[1])
            
            # remove all days that cannot start
            while h and h[0]<day:
                heapq.heappop(h)
            
            # attend
            if h:
                heapq.heappop(h)
                att += 1            
        return att

# V1''''''
# IDEA : HEAP + index
# https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/discuss/954460/Python%3A-faster-than-79.04-of-Python
# time = O(D + n log n), D = day range, n = len(events)
# space = O(n)
import heapq
class Solution:
    def maxEvents(self, events: List[List[int]]) -> int:
        eventsIndex: Dict[int, List[List[int]] ] = {}
        for e in events: 
            if e[0] not in eventsIndex:
                eventsIndex[e[0]] = [e]
            else:
                eventsIndex[e[0]].append(e)

        firstDay = min(events)[0]
        lastDay = max(events, key=lambda x: x[1])[1]
        eventCounter = 0 
        candidates = [] # куча с приоритетом по дню завершения события 
        for d in range(firstDay, lastDay+1):
            if d in eventsIndex:
                for e in eventsIndex[d]:
                    heapq.heappush(candidates, e[1])
            if candidates:
                heapq.heappop(candidates)
                eventCounter = eventCounter + 1

            while candidates and (candidates[0] <= d):
                heapq.heappop(candidates)
                
        return eventCounter

# V1'''''
# https://www.youtube.com/watch?v=NjF9JGDGxg8
# https://zxi.mytechroad.com/blog/greedy/leetcode-1353-maximum-number-of-events-that-can-be-attended/
# C++
# class Solution {
# public:
#   int maxEvents(vector<vector<int>>& events) {
#     sort(begin(events), end(events), [](const auto& a, const auto& b){      
#       return a[1] < b[1];      
#     });
#     int ans = 0;
#     int seen[100001] = {0};
#     for (const auto& e : events) {
#       for (int i = e[0]; i <= e[1]; ++i) {
#         if (seen[i]) continue;
#         ++seen[i];
#         ++ans;
#         break;
#       }
#     }
#     return ans;
#   }
# };

# V2

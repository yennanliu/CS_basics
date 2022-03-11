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
1 <= startDayi <= endDayi <= 105

"""

# V0

# V1
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
# Time O(d + nlogn), where D is the range of A[i][1]
# Space O(N)
class Solution(object):
    def maxEvents(self, A):
            A.sort(reverse=True)
            h = []
            res = d = 0
            while A or h:
                if not h: d = A[-1][0]
                while A and A[-1][0] <= d:
                    heapq.heappush(h, A.pop()[1])
                heapq.heappop(h)
                res += 1
                d += 1
                while h and h[0] < d:
                    heapq.heappop(h)
            return res

# V1'
# IDEA : HEAP
# https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/discuss/1799845/Python-Heap
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

# V1
# IDEA : HEAP + GREEDY
# https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/discuss/1414435/Python-Greedy%2BHeap
class Solution:
    def maxEvents(self, events: List[List[int]]) -> int:
        # algorithm: greedy+heap
        # step1: loop from min to max day
        # step2: each iteration put the candidates in the heap
        # step3: each iteration eliminate the ineligibility ones from the heap
        # step4: each iteration choose one event attend if it is possible
        # time complexity: O(max(n1logn1, n2))
        # space complexity: O(n1)
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

# V1
# IDEA : HEAP + index
# https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/discuss/954460/Python%3A-faster-than-79.04-of-Python
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

# V2
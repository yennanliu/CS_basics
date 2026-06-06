"""

Given a char array representing tasks CPU need to do. 

It contains capital letters A to Z where different letters represent different tasks.

Tasks could be done without original order. Each task could be done in one interval.

For each interval, CPU could finish one task or just be idle.

However, there is a non-negative cooling interval n that means between two same tasks, 

there must be at least n intervals that CPU are doing different tasks or just be idle.

You need to return the least number of intervals the CPU will take to finish all the given tasks.

Example 1:

Input: tasks = ["A","A","A","B","B","B"], n = 2
Output: 8
Explanation: A -> B -> idle -> A -> B -> idle -> A -> B.

1
2
3
Note:

The number of tasks is in the range [1, 10000].
The integer n is in the range [0, 100].

"""


# V0
# IDEA: BIG PQ + QUEUE
import heapq
from collections import Counter, deque

class Solution(object):
    def leastInterval(self, tasks, n):
        if not tasks:
            return 0
        if n == 0:
            return len(tasks)
            
        # Step 1: Count task frequencies
        counts = Counter(tasks)
        
        # Step 2: Build a Max-Heap based on frequencies
        # We store negative counts because Python heapq is a min-heap by default
        max_heap = [-cnt for cnt in counts.values()]
        heapq.heapify(max_heap)
        
        # Step 3: Initialize the cooling queue and time tracker
        # Queue elements store tuples: (negative_remaining_count, available_time)
        # queue: [(negative_remaining_count, available_time)]
        cooling_queue = deque()
        time = 0
        
        # Keep cycling as long as we have tasks ready or tasks cooling down
        while max_heap or cooling_queue:
            time += 1
            
            if max_heap:
                # Extract the highest frequency task
                neg_cnt = heapq.heappop(max_heap)
                # Processing the task means its remaining count gets closer to 0
                remaining_cnt = neg_cnt + 1 
                
                # If there are still copies left, it must wait until (time + n)
                """
                NOTE !!!


                Why remaining_cnt < 0?
                
                -> 

                Remember that we are storing our 
                frequencies as `negative` numbers 
                to simulate a `Max-Heap` in Python.

                If a task has a count of -3 
                and we execute it once, 
                we add 1 to it: -3 + 1 = -2.

                Since -2 < 0, 
                -> it means there are still 2 copies 
                left of this task that need to 
                be scheduled in the future. 
                We must put it in the cooling queue.

                If a task has a count of -1 and 
                we execute it, -1 + 1 = 0. 

                Since 0 is not < 0, 
                it means this task is completely finished. 
                We don't add it to the cooling queue 
                because it never needs to run again!

                """
                if remaining_cnt < 0:
                    cooling_queue.append((remaining_cnt, time + n))
            
            # Check if the task at the front of the cooling queue is ready to be re-queued
            if cooling_queue and cooling_queue[0][1] == time:
                ready_task_cnt, _ = cooling_queue.popleft()
                heapq.heappush(max_heap, ready_task_cnt)
                
        return time


# V0-1
# IDEA: BIG PQ + QUEUE
from collections import Counter, deque
from heapq import heapify, heappop, heappush

class Solution(object):
    def leastInterval(self, tasks, n):

        freq = Counter(tasks)

        max_heap = [-cnt for cnt in freq.values()]
        heapify(max_heap)

        cooldown = deque()  # (available_time, remaining_count)

        time = 0

        while max_heap or cooldown:

            time += 1

            if max_heap:
                cnt = heappop(max_heap)

                cnt += 1

                if cnt != 0:
                    cooldown.append((time + n, cnt))

            if cooldown and cooldown[0][0] == time:
                _, cnt = cooldown.popleft()
                heappush(max_heap, cnt)

        return time


# V0
# pattern :
#    =============================================================================
#    -> task_time = (max_mission_count - 1) * (n + 1) + (number_of_max_mission)
#    =============================================================================
#   
#    -> Example 1) :
#    ->  AAAABBBBCCD, n=3
#    => THE EXPECTED tuned missions is like : ABXXABXXABXXAB
#    -> (4 - 1) * (3 + 1) + 2 = 14
#    -> 4 is the "how many missions the max mission has" (AAAA or BBBB)
#    -> 3 is n
#    -> 2 is "how many mission have max mission count" -> A and B. so it's 2
#    -> in sum,
#    -> (4 - 1) * (3 + 1) is for ABXXABXXABXX
#    -> and 2 is for AB
#
#   -> Example 2) :
#   -> AAABBB, n = 2
#   -> THE EXPECTED tuned missions is like : ABXABXAB
#   -> (3 - 1) * (2 + 1) + (2) = 8
class Solution(object):
    def leastInterval(self, tasks, n):
        count = collections.Counter(tasks)
        most = count.most_common()[0][1]
        num_most = len([i for i, v in count.items() if v == most])
        """
        example 1 : tasks = ["A","A","A","B","B","B"], n = 2
            -> we can split tasks as : A -> B -> idle -> A -> B -> idle -> A -> B
               -> 1) so there are 3-1 group. e.g. (A -> B -> idle), (A -> B -> idle)
                     and each group has (n+1) elements. e.g. (A -> B -> idle)
               -> 2) and the remain element is num_most. e.g. (A, B)
               -> 3) so total cnt = (3-1) * (2+1) + 2 = 8
    
        example 2 : tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
            -> we can split tasks as A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> idle -> idle -> A -> idle -> idle -> A
                -> 1) so there are 6-1 group. e.g. (A -> B -> C), (A -> D -> E), (A -> F -> G), (A -> idle -> idle), (A -> idle -> idle)
                      and each group has (n+1) elements. e.g. (A,B,C) .... (as above)
                -> 2) and the remain element is num_most. e.g. (A) 
                -> 3) so total cnt = (6-1)*(2+1) + 1 =  16
        """
        time = (most - 1) * (n + 1) + num_most
        return max(time, len(tasks)) # be aware of it 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81947087
# NOTE : However, there is a non-negative cooling interval n that means between two same tasks, there must be at least n intervals that CPU are doing different tasks or just be idle.
# IDEA : MISSION_TIME = ( # of most mission -1 ) * (n+1) + (# of how many missions with same mission count)
#        -> ANS = max(MISSION_TIME , tasks) (since every mission need to be run at least once)
class Solution(object):
    def leastInterval(self, tasks, n):
        """
        :type tasks: List[str]
        :type n: int
        :rtype: int
        """
        count = collections.Counter(tasks)
        most = count.most_common()[0][1]
        num_most = len([i for i, v in count.items() if v == most])
        time = (most - 1) * (n + 1) + num_most
        return max(time, len(tasks)) # be aware of it 


# V1'
# https://www.jiuzhang.com/solution/task-scheduler/#tag-highlight-lang-python
class Solution:
    """
    @param tasks: the given char array representing tasks CPU need to do
    @param n: the non-negative cooling interval
    @return: the least number of intervals the CPU will take to finish all the given tasks
    """
    def leastInterval(self, tasks, n):
        # write your code here
        d = collections.Counter(tasks)
        counts = list(d.values())
        longest = max(counts)
        ans = (longest - 1) * (n + 1) + counts.count(longest)
        return max(len(tasks), ans)

# V1
# IDEA : MAX HEAP + Dqeue (double end queue)
# -> maintain a heap for current max element
# -> and a queue for (count, and idleTime)
# -> and a time variable increase with every while loop
# -> once idleTime == time, pop element from queue, and add idle time and add it back to heap

# https://www.youtube.com/watch?v=s8p8ukTyA2I
# https://github.com/neetcode-gh/leetcode/blob/main/python/0621-task-scheduler.py
class Solution:
    def leastInterval(self, tasks: List[str], n: int) -> int:
        count = Counter(tasks)
        maxHeap = [-cnt for cnt in count.values()]
        heapq.heapify(maxHeap)

        time = 0
        q = deque()  # pairs of [-cnt, idleTime]
        while maxHeap or q:
            
            time += 1

            if not maxHeap:
                time = q[0][1]
            else:
                cnt = 1 + heapq.heappop(maxHeap)
                if cnt:
                    q.append([cnt, time + n])
            if q and q[0][1] == time:
                heapq.heappush(maxHeap, q.popleft()[0])
        return time

# V2 
# Time:  O(n)
# Space: O(26) = O(1)
from collections import Counter
class Solution(object):
    def leastInterval(self, tasks, n):
        """
        :type tasks: List[str]
        :type n: int
        :rtype: int
        """
        counter = Counter(tasks)
        _, max_count = counter.most_common(1)[0]

        result = (max_count-1) * (n+1)
        for count in counter.values():
            if count == max_count:
                result += 1
        return max(result, len(tasks))
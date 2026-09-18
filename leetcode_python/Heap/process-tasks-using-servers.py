"""

1882. Process Tasks Using Servers
Medium

You are given two 0-indexed integer arrays servers and tasks of lengths n and m
respectively. servers[i] is the weight of the i-th server, and tasks[j] is the time
needed to process the j-th task in seconds.

Tasks are assigned to the servers using a task queue. Initially, all servers are
free, and the queue is empty.

At second j, the j-th task is inserted into the queue (starting with the 0-th task
being inserted at second 0). As long as there are free servers and the queue is not
empty, the task in the front of the queue will be assigned to a free server with
the smallest weight, and in case of a tie, it is assigned to a free server with the
smallest index.

If there are no free servers and the queue is not empty, we wait until a server
becomes free and immediately assign the next task. If multiple servers become free
at the same time, then multiple tasks from the queue will be assigned in order of
insertion following the weight and index priorities above.

A server that is assigned task j at second t will be free again at second
t + tasks[j].

Build an array ans of length m, where ans[j] is the index of the server the j-th
task will be assigned to.

Return the array ans.


Example 1:

Input: servers = [3,3,2], tasks = [1,2,3,2,1,2]
Output: [2,2,0,2,1,2]
Explanation: Events in chronological order go as follows:
- At second 0, task 0 is added and processed using server 2 until second 1.
- At second 1, server 2 gets free. Task 1 is added and processed using server 2
  until second 3.
- At second 2, task 2 is added and processed using server 0 until second 5.
- At second 3, server 2 gets free. Task 3 is added and processed using server 2
  until second 5.
- At second 4, task 4 is added and processed using server 1 until second 5.
- At second 5, all servers get free. Task 5 is added and processed using server 2
  until second 7.

Example 2:

Input: servers = [5,1,4,3,2], tasks = [2,1,2,4,5,2,1]
Output: [1,4,1,4,1,3,2]


Constraints:

servers.length == n
tasks.length == m
1 <= n, m <= 2 * 10^5
1 <= servers[i], tasks[j] <= 2 * 10^5

"""

# V0
# IDEA : TWO HEAPS (free servers by weight, busy servers by free-time)
#
#   free = (weight, index)            -> the assignment rule, literally
#   busy = (free_at, weight, index)   -> who comes back, and when
#
#   task j arrives at second j, so before assigning it we return to `free`
#   every server whose free_at <= j. then:
#
#     - free is non-empty -> pop the smallest (weight, index)     [the rule]
#     - free is empty     -> we must WAIT, so pop the busy server
#                            that comes back soonest and start the
#                            task at THAT moment (free_at + tasks[j])
#
#   the tuple order (weight, index) gives the "smallest weight, then smallest
#   index" tie-break for free, and (free_at, weight, index) gives "earliest,
#   then the same rule" for busy -- no comparator needed.
#
#   NOTE !!! in the waiting branch the clock is the server's free_at, NOT j.
#            using j there makes the server come back too early and every later
#            assignment drifts.
#
# time = O((n + m) * log n), space = O(n)
import heapq


class Solution(object):
    def assignTasks(self, servers, tasks):
        """
        :type servers: List[int]
        :type tasks: List[int]
        :rtype: List[int]
        """
        # edge
        if not tasks:
            return []

        free = [(w, i) for i, w in enumerate(servers)]
        heapq.heapify(free)
        busy = []

        res = []
        for j, t in enumerate(tasks):
            # everything that finished at or before second j is available again
            while busy and busy[0][0] <= j:
                ft, w, i = heapq.heappop(busy)
                heapq.heappush(free, (w, i))

            if free:
                w, i = heapq.heappop(free)
                res.append(i)
                heapq.heappush(busy, (j + t, w, i))
            else:
                # nothing free -> wait for the first server to come back
                ft, w, i = heapq.heappop(busy)
                res.append(i)
                heapq.heappush(busy, (ft + t, w, i))

        return res

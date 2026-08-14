"""

1606. Find Servers That Handled Most Number of Requests
Hard

You have k servers numbered from 0 to k-1 that are being used to handle multiple requests simultaneously. Each server has infinite computational capacity but cannot handle more than one request at a time. The requests are assigned to servers according to a specific algorithm:

- The ith (0-indexed) request arrives.
- If all servers are busy, the request is dropped (not handled at all).
- If the (i % k)th server is available, assign the request to that server.
- Otherwise, assign the request to the next available server (wrapping around the list of servers and starting from 0 if necessary). For example, if the ith server is busy, try to assign the request to the (i+1)th server, then the (i+2)th server, and so on.

You are given a strictly increasing array arrival of positive integers, where arrival[i] represents the arrival time of the ith request, and another array load, where load[i] represents the load of the ith request (the time it takes to complete). Your goal is to find the busiest server(s). A server is considered busiest if it handled the most number of requests successfully among all the servers.

Return a list containing the IDs (0-indexed) of the busiest server(s). You may return the IDs in any order.


Example 1:

Input: k = 3, arrival = [1,2,3,4,5], load = [5,2,3,3,3]
Output: [1]
Explanation:
All of the servers start out available.
The first 3 requests are handled by the first 3 servers in order.
Request 3 comes in. Server 0 is busy, so it's assigned to the next available server, which is 1.
Request 4 comes in. It cannot be handled since all servers are busy, so it is dropped.
Servers 0 and 2 handled one request each, while server 1 handled two requests. Hence server 1 is the busiest server.

Example 2:

Input: k = 3, arrival = [1,2,3,4], load = [1,2,1,2]
Output: [0]
Explanation:
The first 3 requests are handled by first 3 servers.
Request 3 comes in. It is handled by server 0 since the server is available.
Server 0 handled two requests, while servers 1 and 2 handled one request each. Hence server 0 is the busiest server.

Example 3:

Input: k = 3, arrival = [1,2,3], load = [10,12,11]
Output: [0,1,2]
Explanation: Each server handles a single request, so they are all considered the busiest.


Constraints:

1 <= k <= 10^5
1 <= arrival.length, load.length <= 10^5
arrival.length == load.length
1 <= arrival[i], load[i] <= 10^9
arrival is strictly increasing.

"""

# V0
# IDEA : MIN-HEAP (busy servers by finish time) + BIT over free servers
#
#   two structures:
#     - busy : min-heap of (end_time, server) so we can release everything
#              that finished before the current arrival, each server popped
#              at most once per request -> amortised O(log k)
#     - a Binary Indexed Tree marking free servers with 1, which answers
#       "first free server at index >= p, wrapping to 0" in O(log k):
#         c = #free in [0, p-1]
#         if free_total > c  -> take the (c+1)-th free server
#         else               -> wrap, take the 1st free server
#       the (t)-th free server is found by BIT binary lifting.
#
#   NOTE : a linear scan from i % k is O(k) per request and TLEs at
#          k = arrival.length = 10^5.
#
# time = O((n + k) log k), space = O(k)
from heapq import heappush, heappop
class Solution(object):
    def busiestServers(self, k, arrival, load):
        tree = [0] * (k + 1)
        power = 1
        while power * 2 <= k:
            power *= 2

        def add(i, v):                      # i is 1-indexed
            while i <= k:
                tree[i] += v
                i += i & -i

        def pref(i):                        # sum of 1..i
            s = 0
            while i > 0:
                s += tree[i]
                i -= i & -i
            return s

        def kth(t):                         # smallest idx with pref(idx) >= t
            pos = 0
            bit = power
            while bit:
                if pos + bit <= k and tree[pos + bit] < t:
                    pos += bit
                    t -= tree[pos]
                bit >>= 1
            return pos + 1

        for s in range(1, k + 1):           # every server starts free
            add(s, 1)
        free = k

        busy = []                           # (end_time, server)
        cnt = [0] * k
        for i in range(len(arrival)):
            start = arrival[i]
            while busy and busy[0][0] <= start:
                _, s = heappop(busy)
                add(s + 1, 1)
                free += 1
            if free == 0:
                continue

            p = i % k
            c = pref(p)                     # free servers among ids 0..p-1
            rank = c + 1 if free > c else 1
            idx = kth(rank)
            server = idx - 1

            add(idx, -1)
            free -= 1
            cnt[server] += 1
            heappush(busy, (start + load[i], server))

        best = max(cnt)
        return [s for s in range(k) if cnt[s] == best]

"""

1847. Closest Room
Hard

There is a hotel with n rooms. The rooms are represented by a 2D integer array rooms where rooms[i] = [roomIdi, sizei] denotes that there is a room with room number roomIdi and size equal to sizei. Each roomIdi is guaranteed to be unique.

You are also given k queries in a 2D array queries where queries[j] = [preferredj, minSizej]. The answer to the jth query is the room number id of a room such that:

The room has a size of at least minSizej, and
abs(id - preferredj) is minimized, where abs(x) is the absolute value of x.

If there is a tie in the absolute difference, then use the room with the smallest such id. If there is no such room, the answer is -1.

Return an array answer of length k where answer[j] contains the answer to the jth query.


Example 1:

Input: rooms = [[2,2],[1,2],[3,2]], queries = [[3,1],[3,3],[5,2]]
Output: [3,-1,3]
Explanation: The answers to the queries are as follows:
Query = [3,1]: Room number 3 is the closest as abs(3 - 3) = 0, and its size of 2 is at least 1. The answer is 3.
Query = [3,3]: There are no rooms with a size of at least 3, so the answer is -1.
Query = [5,2]: Room number 3 is the closest as abs(3 - 5) = 2, and its size of 2 is at least 2. The answer is 3.

Example 2:

Input: rooms = [[1,4],[2,3],[3,5],[4,1],[5,2]], queries = [[2,3],[2,4],[2,5]]
Output: [2,1,3]
Explanation: The answers to the queries are as follows:
Query = [2,3]: Room number 2 is the closest as abs(2 - 2) = 0, and its size of 3 is at least 3. The answer is 2.
Query = [2,4]: Room numbers 1 and 3 both have sizes of at least 4. The answer is 1 since it is smaller.
Query = [2,5]: Room number 3 is the only room with a size of at least 5. The answer is 3.


Constraints:

n == rooms.length
1 <= n <= 10^5
k == queries.length
1 <= k <= 10^4
1 <= roomIdi, preferredj <= 10^7
1 <= sizei, minSizej <= 10^7

"""

# V0
# IDEA : OFFLINE SORT BY SIZE (descending) + FENWICK OVER SORTED ROOM IDS
#
#   the size filter is a moving threshold, so answer the queries OFFLINE:
#   sort queries by minSize descending and rooms by size descending. sweeping
#   the queries in that order only ever ACTIVATES rooms, never removes them.
#
#   over the active set we must find the id nearest to `preferred`. keep all
#   room ids in one sorted array and a Fenwick tree of 0/1 "is active" flags:
#     p = bisect_left(ids, preferred)
#     c = number of active ids strictly left of p
#     predecessor = c-th active id, successor = (c+1)-th active id
#   the k-th active id is a binary-lifting descent on the Fenwick tree.
#
#   NOTE : ties go to the SMALLER id, i.e. prefer the predecessor when the two
#          distances are equal -- hence `<=` in the comparison.
#
# time = O((n + k) log n), space = O(n)
import bisect
class Solution(object):
    def closestRoom(self, rooms, queries):
        ids = sorted(r[0] for r in rooms)
        n = len(ids)
        idx_of = {}
        for i, v in enumerate(ids):
            idx_of[v] = i + 1          # fenwick is 1-based

        tree = [0] * (n + 1)
        LOG = 1
        while LOG * 2 <= n:
            LOG *= 2

        def add(i):
            while i <= n:
                tree[i] += 1
                i += i & (-i)

        def count(i):
            # active ids among the first i positions
            s = 0
            while i > 0:
                s += tree[i]
                i -= i & (-i)
            return s

        def kth(k):
            # position (1-based) of the k-th active id
            pos, cur, pw = 0, 0, LOG
            while pw:
                nxt = pos + pw
                if nxt <= n and cur + tree[nxt] < k:
                    pos = nxt
                    cur += tree[nxt]
                pw //= 2
            return pos + 1

        order_rooms = sorted(range(len(rooms)), key=lambda i: -rooms[i][1])
        order_q = sorted(range(len(queries)), key=lambda j: -queries[j][1])

        res = [-1] * len(queries)
        i = 0
        active = 0
        for j in order_q:
            preferred, min_size = queries[j]
            while i < len(order_rooms) and rooms[order_rooms[i]][1] >= min_size:
                add(idx_of[rooms[order_rooms[i]][0]])
                active += 1
                i += 1
            if active == 0:
                continue

            p = bisect.bisect_left(ids, preferred)
            c = count(p)
            best = -1
            if c > 0:
                best = ids[kth(c) - 1]
            if c < active:
                succ = ids[kth(c + 1) - 1]
                if best == -1 or (succ - preferred) < (preferred - best):
                    best = succ
            res[j] = best
        return res

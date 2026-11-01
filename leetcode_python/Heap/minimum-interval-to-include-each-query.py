"""

1851. Minimum Interval to Include Each Query
Hard

You are given a 2D integer array intervals, where intervals[i] = [left_i, right_i]
describes the ith interval starting at left_i and ending at right_i (inclusive).
The size of an interval is defined as the number of integers it contains, or more
formally right_i - left_i + 1.

You are also given an integer array queries. The answer to the jth query is the
size of the smallest interval i such that left_i <= queries[j] <= right_i. If no
such interval exists, the answer is -1.

Return an array containing the answers to the queries.


Example 1:

Input: intervals = [[1,4],[2,4],[3,6],[4,4]], queries = [2,3,4,5]

Output: [3,3,1,4]

Explanation: The queries are processed as follows:
- Query = 2: The interval [2,4] is the smallest interval containing 2. The answer is 4 - 2 + 1 = 3.
- Query = 3: The interval [2,4] is the smallest interval containing 3. The answer is 4 - 2 + 1 = 3.
- Query = 4: The interval [4,4] is the smallest interval containing 4. The answer is 4 - 4 + 1 = 1.
- Query = 5: The interval [3,6] is the smallest interval containing 5. The answer is 6 - 3 + 1 = 4.

Example 2:

Input: intervals = [[2,3],[2,5],[1,8],[20,25]], queries = [2,19,5,22]

Output: [2,-1,4,6]

Explanation: The queries are processed as follows:
- Query = 2: The interval [2,3] is the smallest interval containing 2. The answer is 3 - 2 + 1 = 2.
- Query = 19: None of the intervals contain 19. The answer is -1.
- Query = 5: The interval [2,5] is the smallest interval containing 5. The answer is 5 - 2 + 1 = 4.
- Query = 22: The interval [20,25] is the smallest interval containing 22. The answer is 25 - 20 + 1 = 6.


Constraints:

1 <= intervals.length <= 10^5
1 <= queries.length <= 10^5
intervals[i].length == 2
1 <= left_i <= right_i <= 10^7
1 <= queries[j] <= 10^7

"""

# V0
# IDEA : SORT INTERVALS + SORT QUERIES (OFFLINE) + MIN HEAP OF (SIZE, RIGHT)
#
#   answer the queries in ascending order, not input order (offline), and sweep
#   the intervals sorted by left alongside them:
#
#   1) push every interval with left <= q onto a min heap keyed by size.
#      because queries only grow, an interval pushed for an earlier query has
#      left <= every later query too -- so it never has to be pushed twice.
#   2) pop from the top while its right < q. such an interval has ENDED: it
#      cannot contain q, and since queries only grow it cannot contain any
#      later query either, so throwing it away is safe.
#   3) whatever is now on top has left <= q <= right and the smallest size
#      -> that is the answer for q (or -1 if the heap is empty).
#
#   only the TOP is cleaned lazily: an expired interval buried under a smaller
#   live one is harmless, because it is never the one we read.
#
#   e.g. intervals = [[1,4],[2,4],[3,6],[4,4]], queries sorted = [2,3,4,5]
#     q=2: push [1,4](4),[2,4](3)                 top size 3 -> 3
#     q=3: push [3,6](4)                          top size 3 -> 3
#     q=4: push [4,4](1)                          top size 1 -> 1
#     q=5: pop [4,4],[2,4],[1,4] (all end < 5)    top [3,6] -> 4
#
#   (brute force -- scan every interval per query -- is O(n * q) = 10^10, TLE)
#
# time = O(n log n + q log q), space = O(n + q)
import heapq
from bisect import bisect_left


class Solution(object):
    def minInterval(self, intervals, queries):
        """
        :type intervals: List[List[int]]
        :type queries: List[int]
        :rtype: List[int]
        """
        # do not mutate the caller's list -- sort a copy, by left end
        sorted_intervals = sorted(intervals, key=lambda x: x[0])

        # query indices in ascending order of query value, so each answer
        # can be written back to its original position
        query_order = sorted(range(len(queries)), key=lambda j: queries[j])

        answers = [-1] * len(queries)
        min_heap = []   # (size, right) of every interval that has started
        next_interval = 0

        for j in query_order:
            q = queries[j]

            # 1) every interval starting at or before q becomes a candidate
            while next_interval < len(sorted_intervals) and sorted_intervals[next_interval][0] <= q:
                left, right = sorted_intervals[next_interval]
                size = right - left + 1
                heapq.heappush(min_heap, (size, right))
                next_interval += 1

            # 2) NOTE !!! drop intervals that ended before q -- they cannot
            #             cover q, nor any later (larger) query
            while min_heap and min_heap[0][1] < q:
                heapq.heappop(min_heap)

            # 3) the top now starts <= q, ends >= q, and is the smallest
            if min_heap:
                answers[j] = min_heap[0][0]

        return answers


# V1
# IDEA : SORT INTERVALS BY SIZE + UNION-FIND "NEXT UNANSWERED QUERY" POINTER
#
#   flip the loop: instead of finding the best interval for each query, let
#   each interval, smallest first, claim every query it covers. the FIRST
#   interval to claim a query is the smallest one covering it, so a query is
#   answered exactly once and then must be skipped forever.
#
#   the queries (deduplicated, sorted) sit in an array. parent[i] points at
#   the next still-unanswered index at or after i; answering index i sets
#   parent[i] = i + 1. find() follows those pointers (with path compression),
#   so an interval jumps straight over every query already answered instead
#   of re-scanning them -- each query is visited O(1) amortised times.
#
#   a different idea from V0, same bound. V0 is the one to say in an
#   interview; this one is worth knowing as the "paint an array, skip what is
#   painted" union-find trick (a "next free slot" pointer).
#
# time = O(n log n + q log q + (n + q) * alpha), space = O(q)


class Solution2(object):
    def minInterval(self, intervals, queries):
        """
        :type intervals: List[List[int]]
        :type queries: List[int]
        :rtype: List[int]
        """
        # distinct query values, ascending -- equal queries share one answer
        unique_queries = sorted(set(queries))
        m = len(unique_queries)

        # parent[i] = some index >= i on the way to the next unanswered one;
        # index m is a sentinel meaning "no unanswered query left"
        parent = list(range(m + 1))
        best_size = [-1] * m

        def find(i):
            """Return the first unanswered index >= i, compressing the path."""
            root = i
            while parent[root] != root:
                root = parent[root]
            # point every node on the path straight at the root
            while parent[i] != root:
                following = parent[i]
                parent[i] = root
                i = following
            return root

        # smallest interval first, so the first claim on a query is the answer
        by_size = sorted(intervals, key=lambda x: x[1] - x[0])

        for left, right in by_size:
            size = right - left + 1
            # first query value >= left, then skip any already answered
            idx = find(bisect_left(unique_queries, left))
            while idx < m and unique_queries[idx] <= right:
                best_size[idx] = size
                # answered: link it to its right neighbour so it is skipped
                parent[idx] = idx + 1
                idx = find(idx + 1)

        # map each distinct value back to its answer, then to input order
        size_of_value = {}
        for i in range(m):
            size_of_value[unique_queries[i]] = best_size[i]

        answers = []
        for q in queries:
            answers.append(size_of_value[q])
        return answers

"""

2113. Elements in Array After Removing and Replacing Elements
Medium
(premium / locked problem)

You are given a 0-indexed integer array nums. Initially on minute 0, the array is unchanged. Every minute, the leftmost element in nums is removed until no elements remain. Then, every minute, one element is appended to the end of nums, in the order they were removed in, until the original array is restored. This process repeats indefinitely.

For example, the array [0,1,2] would change as follows: [0,1,2] -> [1,2] -> [2] -> [] -> [0] -> [0,1] -> [0,1,2] -> [1,2] -> [2] -> [] -> [0] -> [0,1] -> [0,1,2] -> ...

You are given a 2D integer array queries of size n where queries[j] = [time_j, index_j]. The answer to the jth query is:

nums[index_j] if index_j < nums.length at minute time_j
-1 if index_j >= nums.length at minute time_j

Return an integer array ans of size n where ans[j] is the answer to the jth query.


Example 1:

Input: nums = [0,1,2], queries = [[0,2],[2,0],[3,2],[5,0]]
Output: [2,2,-1,0]
Explanation:
Minute 0: [0,1,2] - All elements are in the nums.
Minute 1: [1,2]   - The leftmost element, 0, is removed.
Minute 2: [2]     - The leftmost element, 1, is removed.
Minute 3: []      - The leftmost element, 2, is removed.
Minute 4: [0]     - 0 is added to the end of nums.
Minute 5: [0,1]   - 1 is added to the end of nums.

At minute 0, nums[2] is 2.
At minute 2, nums[0] is 2.
At minute 3, nums[2] does not exist.
At minute 5, nums[0] is 0.

Example 2:

Input: nums = [2], queries = [[0,0],[1,0],[2,0]]
Output: [2,-1,2]
Explanation:
Minute 0: [2] - All elements are in the nums.
Minute 1: []  - The leftmost element, 2, is removed.
Minute 2: [2] - 2 is added to the end of nums.


Constraints:

1 <= nums.length <= 100
0 <= nums[i] <= 100
n == queries.length
1 <= n <= 10^5
queries[j].length == 2
0 <= time_j <= 10^5
0 <= index_j < nums.length

"""

# V0
# IDEA : THE STATE IS PERIODIC WITH PERIOD 2n — ANSWER EACH QUERY IN O(1)
#
#   the whole cycle takes 2n minutes : n minutes shrinking to empty, then n
#   minutes growing back. so reduce  t = time % (2n)  and read off the state:
#
#     t < n  : the array is nums[t:]     (length n - t)
#              -> nums[t + index] when index < n - t
#     t >= n : let t2 = t - n; the array is nums[:t2]  (length t2)
#              -> nums[index]     when index < t2
#
#   anything past the current length answers -1.
#
# time = O(n + q), space = O(q)
class Solution(object):
    def elementInNums(self, nums, queries):
        n = len(nums)
        res = []
        for t, index in queries:
            t %= 2 * n
            if t < n:
                res.append(nums[t + index] if index < n - t else -1)
            else:
                res.append(nums[index] if index < t - n else -1)
        return res


# V0-1
# IDEA : SIMULATE ONE FULL CYCLE AND SNAPSHOT EVERY STATE, THEN LOOK UP
#
#   rather than deriving the index formula, just run the process for the 2n
#   minutes of one period with a deque -- popleft during the shrinking half,
#   append the removed values back (in removal order) during the growing half --
#   and store a copy of the array at each minute.
#
#   every query is then `states[time % (2n)]`, an O(1) lookup plus a length test.
#   n <= 100, so the 2n snapshots cost at most ~10^4 cells.
#
# time = O(n^2 + q), space = O(n^2 + q)
from collections import deque

class Solution(object):
    def elementInNums(self, nums, queries):
        n = len(nums)
        cur = deque(nums)
        removed = deque()
        states = []
        for _ in range(n):                 # minutes 0 .. n-1 : shrinking
            states.append(list(cur))
            removed.append(cur.popleft())
        for _ in range(n):                 # minutes n .. 2n-1 : growing back
            states.append(list(cur))
            cur.append(removed.popleft())

        res = []
        for t, index in queries:
            state = states[t % (2 * n)]
            res.append(state[index] if index < len(state) else -1)
        return res


# V0-2
# IDEA : OFFLINE SWEEP -- BUCKET THE QUERIES BY time % 2n, SIMULATE ONCE
#
#   the queries are answered out of order : first bucket each query index under
#   `time % (2n)`, then walk the single cycle once and, at each minute, answer
#   only the queries parked on that minute. Nothing but the current array is
#   ever kept, so no 2n snapshots are stored (unlike V0-1) -- the classic
#   offline-query trade of "answer in a convenient order" for memory.
#
#   the deque is materialised into a list only on minutes that actually carry
#   queries, which is what keeps the per-answer cost O(1).
#
# time = O(n^2 + q), space = O(n + q)
from collections import defaultdict, deque

class Solution(object):
    def elementInNums(self, nums, queries):
        n = len(nums)
        cycle = 2 * n
        buckets = defaultdict(list)
        for qi, (t, _) in enumerate(queries):
            buckets[t % cycle].append(qi)

        res = [-1] * len(queries)
        cur = deque(nums)
        removed = deque()
        for minute in range(cycle):
            todo = buckets.get(minute)
            if todo:
                snap = list(cur)
                for qi in todo:
                    index = queries[qi][1]
                    if index < len(snap):
                        res[qi] = snap[index]
            if minute < n:
                removed.append(cur.popleft())
            else:
                cur.append(removed.popleft())
        return res

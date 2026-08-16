"""

3636. Threshold Majority Queries
Hard

You are given an integer array nums of length n and an array queries, where
queries[i] = [li, ri, thresholdi].

Return an array of integers ans where ans[i] is equal to the element in the
subarray nums[li...ri] that appears at least thresholdi times, selecting the
element with the highest frequency (choosing the smallest in case of a tie),
or -1 if no such element exists.


Example 1:

Input: nums = [1,1,2,2,1,1], queries = [[0,5,4],[0,3,3],[2,3,2]]
Output: [1,-1,2]
Explanation:

Query       Sub-array          Threshold  Frequency table    Answer
[0, 5, 4]   [1,1,2,2,1,1]      4          1 -> 4, 2 -> 2     1
[0, 3, 3]   [1,1,2,2]          3          1 -> 2, 2 -> 2     -1
[2, 3, 2]   [2,2]              2          2 -> 2             2

Example 2:

Input: nums = [3,2,3,2,3,2,3], queries = [[0,6,4],[1,5,2],[2,4,1],[3,3,1]]
Output: [3,2,3,2]
Explanation:

Query       Sub-array          Threshold  Frequency table    Answer
[0, 6, 4]   [3,2,3,2,3,2,3]    4          3 -> 4, 2 -> 3     3
[1, 5, 2]   [2,3,2,3,2]        2          2 -> 3, 3 -> 2     2
[2, 4, 1]   [3,2,3]            1          3 -> 2, 2 -> 1     3
[3, 3, 1]   [2]                1          2 -> 1             2


Constraints:

1 <= nums.length == n <= 10^4
1 <= nums[i] <= 10^9
1 <= queries.length <= 5 * 10^4
queries[i] = [li, ri, thresholdi]
0 <= li <= ri < n
1 <= thresholdi <= ri - li + 1

"""

# V0
# IDEA : RANGE MODE BY BLOCK PRECOMPUTATION + A CANDIDATE SCAN OF THE EDGES
#
#   strip away the threshold and this is a range MODE query: find the most
#   frequent value in nums[l..r], smallest on ties, then compare its count
#   with the threshold. mode is not decomposable the way sum or min is --
#   you cannot merge two halves' modes -- so the usual segment tree is out.
#
#   the standard escape is block decomposition plus the following fact:
#   split [l, r] into a run of WHOLE blocks in the middle and two ragged
#   edges. any value whose count in [l, r] beats the whole-block mode must
#   occur somewhere in the edges, because a value living only inside the
#   middle already had its count measured there. so the candidate set is
#   just the O(block) values sitting on the edges, plus the precomputed
#   middle mode itself.
#
#   the middle mode for every pair of blocks is precomputed by fixing a
#   start block and sweeping right with a frequency table, which keeps the
#   running (max count, smallest such value) updated in O(1) per element.
#   for a candidate value, its exact count in [l, r] is two binary searches
#   in that value's sorted occurrence list.
#
#   short ranges (fewer than two whole blocks between the edges) are just
#   counted directly -- they are O(block) long anyway.
#
# time = O(n^2 / B + q * B log n), space = O(n^2 / B^2 + n)
class Solution(object):
    def subarrayMajority(self, nums, queries):
        from bisect import bisect_left, bisect_right

        n = len(nums)
        order = sorted(set(nums))
        rank = {v: i for i, v in enumerate(order)}
        a = [rank[v] for v in nums]           # ids keep the value ordering
        m = len(order)

        pos = [[] for _ in range(m)]
        for i, v in enumerate(a):
            pos[v].append(i)

        B = 24
        nb = (n + B - 1) // B
        # mode of the block range [s .. e], as (count, id)
        mode_cnt = [[0] * nb for _ in range(nb)]
        mode_val = [[0] * nb for _ in range(nb)]
        for s in range(nb):
            cnt = [0] * m
            best = 0
            bval = 0
            j = s * B
            row_c, row_v = mode_cnt[s], mode_val[s]
            for e in range(s, nb):
                stop = (e + 1) * B
                if stop > n:
                    stop = n
                while j < stop:
                    v = a[j]
                    c = cnt[v] + 1
                    cnt[v] = c
                    if c > best:
                        best = c
                        bval = v
                    elif c == best and v < bval:
                        bval = v
                    j += 1
                row_c[e] = best
                row_v[e] = bval

        ans = []
        for l, r, threshold in queries:
            bl = l // B
            br = r // B
            if br - bl <= 1:
                cnt = {}
                best = 0
                bval = 0
                for i in range(l, r + 1):
                    v = a[i]
                    c = cnt.get(v, 0) + 1
                    cnt[v] = c
                    if c > best:
                        best = c
                        bval = v
                    elif c == best and v < bval:
                        bval = v
            else:
                best = mode_cnt[bl + 1][br - 1]
                bval = mode_val[bl + 1][br - 1]
                seen = set()
                edges = list(range(l, (bl + 1) * B))
                edges += range(br * B, r + 1)
                for i in edges:
                    v = a[i]
                    if v in seen:
                        continue
                    seen.add(v)
                    p = pos[v]
                    c = bisect_right(p, r) - bisect_left(p, l)
                    if c > best or (c == best and v < bval):
                        best = c
                        bval = v
            ans.append(order[bval] if best >= threshold else -1)
        return ans

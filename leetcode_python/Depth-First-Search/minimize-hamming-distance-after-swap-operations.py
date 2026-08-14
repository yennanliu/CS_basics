"""

1722. Minimize Hamming Distance After Swap Operations
Medium

You are given two integer arrays, source and target, both of length n. You are also given an array allowedSwaps where each allowedSwaps[i] = [ai, bi] indicates that you are allowed to swap the elements at index ai and index bi (0-indexed) of array source. Note that you can swap elements at a specific pair of indices multiple times and in any order.

The Hamming distance of two arrays of the same length, source and target, is the number of positions where the elements are different. Formally, it is the number of indices i for 0 <= i <= n-1 where source[i] != target[i] (0-indexed).

Return the minimum Hamming distance of source and target after performing any amount of swap operations on array source.


Example 1:

Input: source = [1,2,3,4], target = [2,1,4,5], allowedSwaps = [[0,1],[2,3]]
Output: 1
Explanation: source can be transformed the following way:
- Swap indices 0 and 1: source = [2,1,3,4]
- Swap indices 2 and 3: source = [2,1,4,3]
The Hamming distance of source and target is 1 as they differ in 1 position: index 3.

Example 2:

Input: source = [1,2,3,4], target = [1,3,2,4], allowedSwaps = []
Output: 2
Explanation: There are no allowed swaps.
The Hamming distance of source and target is 2 as they differ in 2 positions: index 1 and index 2.

Example 3:

Input: source = [5,1,2,4,3], target = [1,5,4,2,3], allowedSwaps = [[0,4],[4,2],[1,3],[1,4]]
Output: 0


Constraints:

n == source.length == target.length
1 <= n <= 10^5
1 <= source[i], target[i] <= 10^5
0 <= allowedSwaps.length <= 10^5
allowedSwaps[i].length == 2
0 <= ai, bi <= n - 1
ai != bi


"""

# V0
# IDEA : UNION FIND + PER-COMPONENT MULTISET
#
#   swaps are transitive : if we may swap (a,b) and (b,c) we can realise ANY
#   permutation of the indices {a,b,c}. so a connected component of the
#   "allowed swap" graph is a bag whose values can be arranged freely.
#
#   -> union the indices, then for each component keep a Counter of the
#      source values it holds.
#
#   scan target left to right: at index i with component root r,
#     - if cnt[r][target[i]] > 0 : that value is available in the bag,
#       consume one, this position can be matched for free.
#     - otherwise the position must stay wrong -> distance += 1.
#
#   greedy is safe : matching value v at position i never blocks another
#   position, since every position that wants v is in the same bag and the
#   supply of v is what it is.
#
# time = O(n * alpha(n)), space = O(n)
from collections import defaultdict, Counter
class Solution(object):
    def minimumHammingDistance(self, source, target, allowedSwaps):
        n = len(source)
        p = list(range(n))

        def find(x):
            while p[x] != x:
                p[x] = p[p[x]]
                x = p[x]
            return x

        for a, b in allowedSwaps:
            ra, rb = find(a), find(b)
            if ra != rb:
                p[ra] = rb

        cnt = defaultdict(Counter)
        for i in range(n):
            cnt[find(i)][source[i]] += 1

        res = 0
        for i in range(n):
            bag = cnt[find(i)]
            if bag[target[i]] > 0:
                bag[target[i]] -= 1
            else:
                res += 1

        return res

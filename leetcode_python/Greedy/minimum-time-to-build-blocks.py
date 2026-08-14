"""

1199. Minimum Time to Build Blocks
Hard

You are given a list of blocks, where blocks[i] = t means that the i-th block
needs t units of time to be built. A block can only be built by exactly one
worker.

A worker can either split into two workers (number of workers increases by one)
or build a block then go home. Both decisions cost some time.

The time cost of spliting one worker into two workers is given as an integer
split. Note that if two workers split at the same time, they split in parallel
so the cost would be split.

Output the minimum time needed to build all blocks.

Initially, there is only one worker.


Example 1:

Input: blocks = [1], split = 1
Output: 1
Explanation: We use 1 worker to build 1 block in 1 time unit.

Example 2:

Input: blocks = [1,2], split = 5
Output: 7
Explanation: We split the worker into 2 workers in 5 time units then assign each
of them to a block so the cost is 5 + max(1, 2) = 7.

Example 3:

Input: blocks = [1,2,3], split = 1
Output: 4
Explanation: Split 1 worker into 2, then assign the first worker to the last
block and split the second worker into 2.
Then, use the two unassigned workers to build the first two blocks.
The cost is 1 + max(3, 1 + max(1, 2)) = 4.


Constraints:

1 <= blocks.length <= 1000
1 <= blocks[i] <= 10^5
1 <= split <= 100

"""

# V0
# IDEA: GREEDY + MIN HEAP (reverse Huffman-style merging)
"""

 -> think BACKWARDS: instead of splitting workers, MERGE blocks.

    merging blocks i and j means "one worker split, then handled both",
    and the merged block costs:

        split + max(blocks[i], blocks[j])

 -> the deepest (most split) positions in the tree should hold the
    CHEAPEST blocks, so each merge should greedily combine
    the two SMALLEST remaining blocks.

 -> keep merging until 1 block is left; that value is the answer.
"""
# time = O(n log n)
# space = O(n)
import heapq
class Solution(object):
    def minBuildTime(self, blocks, split):
        # copy, so the caller's list is not mutated
        h = list(blocks)
        heapq.heapify(h)

        while len(h) > 1:
            """
            NOTE !!!

            -> the first (smallest) popped block is "absorbed":
               it is built in parallel with the second one,
               so only max(a, b) == b matters
            """
            heapq.heappop(h)          # a (smallest, absorbed)
            b = heapq.heappop(h)      # b (2nd smallest, the max of the pair)
            heapq.heappush(h, b + split)

        return h[0]

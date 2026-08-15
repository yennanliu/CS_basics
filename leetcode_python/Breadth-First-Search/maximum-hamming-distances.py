"""

3141. Maximum Hamming Distances
Hard
🔒 (premium)

You are given an array nums and an integer m. The Hamming distance between two integers is the number of positions at which the corresponding bits are different.

Return an array answer of the same length as nums, where answer[i] is the maximum Hamming distance between nums[i] and any element in nums, where every integer is treated as an m-bit number.


Example 1:

Input: nums = [9,12,9,11], m = 4
Output: [2,3,2,3]
Explanation:
The binary representations are 9 = 1001, 12 = 1100, 11 = 1011.
- For nums[0] = 9, the farthest element is 12 at a distance of 2.
- For nums[1] = 12, the farthest element is 11 at a distance of 3.
- For nums[2] = 9, the farthest element is 12 at a distance of 2.
- For nums[3] = 11, the farthest element is 12 at a distance of 3.

Example 2:

Input: nums = [3,4,6,10], m = 4
Output: [3,3,2,3]
Explanation:
The binary representations are 3 = 0011, 4 = 0100, 6 = 0110, 10 = 1010.
- For nums[0] = 3, the farthest element is 4 at a distance of 3.
- For nums[1] = 4, the farthest element is 3 at a distance of 3.
- For nums[2] = 6, the farthest elements are 9 and 10 at a distance of 2.
- For nums[3] = 10, the farthest element is 4 at a distance of 3.


Constraints:

1 <= m <= 17
1 <= nums.length <= 2^m
0 <= nums[i] < 2^m

"""

# V0
# IDEA : FLIP THE QUESTION — FARTHEST FROM x IS NEAREST TO ~x
#
#   hamming(x, y) = m - hamming(~x, y) over m bits, so maximising the
#   distance from x is the same as MINIMISING the distance from x's
#   complement to some element of nums.
#
#   "nearest element" over the hypercube is a multi-source BFS : seed the
#   queue with every value in nums at distance 0, and each step flips a
#   single bit. dist[v] then holds how many bits separate v from the closest
#   input value.
#
#   the answer for nums[i] is m - dist[~nums[i]].
#
#   the graph has 2^m nodes and m edges each — 17 * 131072 ≈ 2.2 million
#   steps at the limit, which is why BFS beats comparing all pairs.
#
# time = O(m * 2^m), space = O(2^m)
from collections import deque


class Solution(object):
    def maxHammingDistances(self, nums, m):
        size = 1 << m
        mask = size - 1
        dist = [-1] * size

        q = deque()
        for v in nums:
            if dist[v] == -1:
                dist[v] = 0
                q.append(v)

        while q:
            u = q.popleft()
            for b in range(m):
                v = u ^ (1 << b)
                if dist[v] == -1:
                    dist[v] = dist[u] + 1
                    q.append(v)

        return [m - dist[x ^ mask] for x in nums]

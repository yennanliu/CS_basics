"""

2379. Minimum Recolors to Get K Consecutive Black Blocks
Easy

You are given a 0-indexed string blocks of length n, where blocks[i] is either 'W' or 'B', representing the color of the ith block. The characters 'W' and 'B' denote the colors white and black, respectively.

You are also given an integer k, which is the desired number of consecutive black blocks.

In one operation, you can recolor a white block such that it becomes a black block.

Return the minimum number of operations needed such that there is at least one occurrence of k consecutive black blocks.


Example 1:

Input: blocks = "WBBWWBBWBW", k = 7
Output: 3
Explanation:
One way to achieve 7 consecutive black blocks is to recolor the 0th, 3rd, and 4th blocks
so that blocks = "BBBBBBBWBW".
It can be shown that there is no way to achieve 7 consecutive black blocks in less than 3 operations.
Therefore, we return 3.

Example 2:

Input: blocks = "WBWBBBW", k = 2
Output: 0
Explanation:
No changes need to be made, since 2 consecutive black blocks already exist.
Therefore, we return 0.


Constraints:

n == blocks.length
1 <= n <= 100
blocks[i] is either 'W' or 'B'.
1 <= k <= n

"""

# V0
# IDEA : FIXED-WIDTH SLIDING WINDOW — MINIMISE THE WHITES INSIDE
#
#   any k consecutive blocks can be made all-black by recolouring exactly the
#   whites inside that window, so the answer is
#       min over windows of width k of (number of 'W' in the window)
#
#   slide the window keeping a running white count : the block leaving on the
#   left is subtracted and the one entering on the right is added.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumRecolors(self, blocks, k):
        whites = sum(1 for i in range(k) if blocks[i] == 'W')
        res = whites
        for i in range(k, len(blocks)):
            if blocks[i - k] == 'W':
                whites -= 1
            if blocks[i] == 'W':
                whites += 1
            res = min(res, whites)
        return res

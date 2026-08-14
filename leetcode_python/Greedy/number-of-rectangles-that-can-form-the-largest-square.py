"""

1725. Number Of Rectangles That Can Form The Largest Square
Easy

You are given an array rectangles where rectangles[i] = [li, wi] represents the ith rectangle of length li and width wi.

You can cut the ith rectangle to form a square with a side length of k if both k <= li and k <= wi. For example, if you have a rectangle [4,6], you can cut it to get a square with a side length of at most 4.

Let maxLen be the side length of the largest square you can obtain from any of the given rectangles.

Return the number of rectangles that can make a square with a side length of maxLen.


Example 1:

Input: rectangles = [[5,8],[3,9],[5,12],[16,5]]
Output: 3
Explanation: The largest squares you can get from each rectangle are of lengths [5,3,5,5].
The largest possible square is of length 5, and you can get it out of 3 rectangles.

Example 2:

Input: rectangles = [[2,3],[3,7],[4,3],[3,7]]
Output: 3


Constraints:

1 <= rectangles.length <= 1000
rectangles[i].length == 2
1 <= li, wi <= 10^9
li != wi

"""

# V0
# IDEA : RUNNING MAX + COUNT (single pass, no sorting)
#
#   the biggest square cuttable from [l, w] has side min(l, w).
#   so we need : max of min(l, w), and how many rectangles hit that max.
#
#   keep a running pair (best, cnt):
#     x > best  -> a new record : best = x, cnt = 1
#     x == best -> another rectangle ties it : cnt += 1
#     x < best  -> ignore
#
# time = O(n), space = O(1)
class Solution(object):
    def countGoodRectangles(self, rectangles):
        best = 0
        cnt = 0
        for l, w in rectangles:
            x = min(l, w)
            if x > best:
                best = x
                cnt = 1
            elif x == best:
                cnt += 1
        return cnt

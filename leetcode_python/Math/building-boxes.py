"""

1739. Building Boxes
Hard

You have a cubic storeroom where the width, length, and height of the room are all equal to n units. You are asked to place n boxes in this room where each box is a cube of unit side length. There are however some rules to placing the boxes:

You can place the boxes anywhere on the floor.
If box x is placed on top of the box y, then each side of the four vertical sides of the box y must either be adjacent to another box or to a wall.

Given an integer n, return the minimum possible number of boxes touching the floor.


Example 1:

Input: n = 3
Output: 3
Explanation: The figure above is for the placement of the three boxes.
These boxes are placed in the corner of the room, where the corner is on the left side.

Example 2:

Input: n = 4
Output: 3
Explanation: The figure above is for the placement of the four boxes.
These boxes are placed in the corner of the room, where the corner is on the left side.

Example 3:

Input: n = 10
Output: 6
Explanation: The figure above is for the placement of the ten boxes.
These boxes are placed in the corner of the room, where the corner is on the back side.


Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA : GREEDY STAIRCASE + MATH (build a corner pyramid, then extend it)
#
#   the densest legal stack is a staircase pyramid tucked into a corner.
#   with k full layers, the FLOOR layer is a triangle of side k, so
#
#     floor boxes for k layers = T(k) = k(k+1)/2
#     TOTAL boxes for k layers = T(1) + T(2) + ... + T(k)   (tetrahedral)
#
#   phase 1 : grow k while the whole pyramid still fits inside n. this
#   costs O(sqrt(n)) iterations because the totals grow like k^3.
#
#   phase 2 : the leftovers go onto the floor one diagonal at a time. adding
#   the i-th extra floor box lets us stack i more boxes total (that column
#   plus everything it now supports), so consume the remainder in chunks of
#   1, 2, 3, ... while counting one extra floor box each time.
#
#   NOTE : both loops run O(sqrt(n)) times, so n up to 10^9 is fine.
#
# time = O(sqrt(n)), space = O(1)
class Solution(object):
    def minimumBoxes(self, n):
        # phase 1 : as many complete pyramid layers as fit
        used = 0
        k = 1
        while used + k * (k + 1) // 2 <= n:
            used += k * (k + 1) // 2
            k += 1
        k -= 1

        res = k * (k + 1) // 2      # floor footprint of the full pyramid

        # phase 2 : add floor boxes one at a time for the remainder
        i = 1
        while used < n:
            res += 1
            used += i
            i += 1

        return res

"""

1580. Put Boxes Into the Warehouse II
Medium

You are given two arrays of positive integers, boxes and warehouse, representing the heights of some boxes of unit width and the heights of n rooms in a warehouse respectively. The warehouse's rooms are labeled from 0 to n - 1 from left to right where warehouse[i] (0-indexed) is the height of the ith room.

Boxes are put into the warehouse by the following rules:

Boxes cannot be stacked.
You can rearrange the insertion order of the boxes.
Boxes can be pushed into the warehouse from either side (left or right)
If the height of some room in the warehouse is less than the height of a box, then that box and all other boxes behind it will be stopped before that room.

Return the maximum number of boxes you can put into the warehouse.

Example 1:

Input: boxes = [1,2,2,3,4], warehouse = [3,4,1,2]
Output: 4
Explanation:

We can store the boxes in the following order:
1- Put the yellow box in room 2 from either the left or right side.
2- Put the orange box in room 3 from the right side.
3- Put the green box in room 1 from the left side.
4- Put the red box in room 0 from the left side.
Notice that there are other valid ways to put 4 boxes such as swapping the red and green boxes or the red and orange boxes.

Example 2:

Input: boxes = [3,5,5,2], warehouse = [2,1,3,4,5]
Output: 3
Explanation:

It is not possible to put the two boxes of height 5 in the warehouse since there's only 1 room of height >= 5.
Other valid solutions are to put the green box in room 2 or to put the orange box first in room 2 before putting the green and red boxes.

Constraints:

n == warehouse.length
1 <= boxes.length, warehouse.length <= 10^5
1 <= boxes[i], warehouse[i] <= 10^9

"""

# V0
# IDEA : PREFIX/SUFFIX MIN + GREEDY (a room is reachable from the better side)
#
#   pushing from the left, room i can only take a box of height
#     <= min(warehouse[0..i]) ; from the right <= min(warehouse[i..n-1]).
#   since we may choose the side, the real capacity of room i is
#     cap[i] = max(prefix_min[i], suffix_min[i])
#   then sort caps and boxes ascending and greedily give the smallest
#   unplaced box to the smallest capacity that can hold it.
#
# time = O(n log n + b log b), space = O(n)
class Solution(object):
    def maxBoxesInWarehouse(self, boxes, warehouse):
        n = len(warehouse)
        pre = [0] * n
        suf = [0] * n
        cur = warehouse[0]
        for i in range(n):
            if warehouse[i] < cur:
                cur = warehouse[i]
            pre[i] = cur
        cur = warehouse[n - 1]
        for i in range(n - 1, -1, -1):
            if warehouse[i] < cur:
                cur = warehouse[i]
            suf[i] = cur

        cap = sorted(max(pre[i], suf[i]) for i in range(n))
        boxes.sort()
        i = 0
        for h in cap:
            if i < len(boxes) and boxes[i] <= h:
                i += 1
        return i

"""

3074. Apple Redistribution into Boxes
Easy

You are given an array apple of size n and an array capacity of size m.

There are n packs where the ith pack contains apple[i] apples. There are m boxes as well, and the ith box has a capacity of capacity[i] apples.

Return the minimum number of boxes you need to select to redistribute these n packs of apples into boxes.

Note that, apples from the same pack can be distributed into different boxes.


Example 1:

Input: apple = [1,3,2], capacity = [4,3,1,5,2]
Output: 2
Explanation: We will use boxes with capacities 4 and 5.
It is possible to distribute the apples as the total capacity is greater than or equal to the total number of apples.

Example 2:

Input: apple = [5,5,5], capacity = [2,4,2,7]
Output: 4
Explanation: We will need to use all the boxes.


Constraints:

1 <= n == apple.length <= 50
1 <= m == capacity.length <= 50
1 <= apple[i], capacity[i] <= 50
The input is generated such that it's possible to redistribute packs of apples into boxes.

"""

# V0
# IDEA : PACKS CAN BE SPLIT, SO ONLY THE TOTALS MATTER — TAKE THE BIGGEST BOXES
#
#   because apples from one pack may be spread across boxes, there is no
#   packing constraint at all : any set of boxes whose capacities sum to at
#   least the total number of apples works.
#
#   so sort the capacities descending and keep taking until the running total
#   covers the apples — the fewest boxes possible.
#
# time = O(m log m), space = O(m)
class Solution(object):
    def minimumBoxes(self, apple, capacity):
        need = sum(apple)
        res = 0
        for c in sorted(capacity, reverse=True):
            if need <= 0:
                break
            need -= c
            res += 1
        return res

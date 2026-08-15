"""

2753. Count Houses in a Circular Street II
Hard

You are given an object street of class Street that represents a circular street and a positive integer k which represents a maximum bound for the number of houses in that street (in other words, the number of houses is less than or equal to k). Houses' doors could be open or closed initially (at least one is open).

Initially, you are standing in front of a door to a house on this street. Your task is to count the number of houses in the street.

The class Street contains the following functions which may help you:

void closeDoor(): Close the door of the house you are in front of.
boolean isDoorOpen(): Returns true if the door of the current house is open and false otherwise.
void moveRight(): Move to the right house.

Note that by circular street, we mean if you number the houses from 1 to n, then the right house of house_i is house_(i+1) for i < n, and the right house of house_n is house_1.

Return ans which represents the number of houses on this street.


Example 1:

Input: street = [1,1,1,1], k = 10
Output: 4
Explanation: There are 4 houses, and all their doors are open.
The number of houses is less than k, which is 10.

Example 2:

Input: street = [1,0,1,1,0], k = 5
Output: 5
Explanation: There are 5 houses, and the doors of the 1st, 3rd, and 4th house (moving in the right direction) are open, and the rest are closed.
The number of houses is equal to k, which is 5.


Constraints:

n == number of houses
1 <= n <= k <= 10^5
street is circular by definition provided in the statement.
The input is generated such that at least one of the doors is open.

"""

# V0
# IDEA : INTERACTIVE / BRAIN TEASER -- use one open door as an anchor
#
#  we cannot see n, we can only feel the street through 3 calls. The trick is
#  to leave ONE open door untouched and use it as a marker of "one full lap".
#
#  step 1 : walk right until we stand on an open door -- that is our anchor.
#           (guaranteed to exist, and reached within n <= k steps)
#  step 2 : from there take up to k more steps to the right. Every time we
#           land on an open door we record the step counter and CLOSE that
#           door.
#
#  why the LAST recorded counter is n:
#     - during steps 1..n we sweep the whole street once and close every open
#       door we meet; at step exactly n we are back on the anchor, which is
#       still open -> the counter n gets recorded.
#     - after that every door on the street is closed, so no further step can
#       overwrite the answer.
#   -> the last recorded value is exactly n.
#
#   NOTE : do NOT close the anchor before starting -- it is the only thing
#          that tells us a lap is complete.
#   NOTE : k >= n is promised, so the loop is long enough to reach step n.
#
# time = O(k), space = O(1)
#
# Definition for a street.
# class Street(object):
#     def closeDoor(self):
#         pass
#     def isDoorOpen(self):
#         pass
#     def moveRight(self):
#         pass
class Solution(object):
    def houseCount(self, street, k):
        # step 1 : park on an open door (the anchor)
        while not street.isDoorOpen():
            street.moveRight()
        # step 2 : one guarded lap, closing every open door we pass
        res = 0
        for i in range(1, k + 1):
            street.moveRight()
            if street.isDoorOpen():
                res = i
                street.closeDoor()
        return res

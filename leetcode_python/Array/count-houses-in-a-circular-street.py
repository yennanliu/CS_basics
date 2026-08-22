"""

2728. Count Houses in a Circular Street
Easy

You are given an object street of class Street that represents a circular street and a positive integer k which represents a maximum bound for the number of houses in that street (in other words, the number of houses is less than or equal to k). Houses' doors could be open or closed initially.

Initially, you are standing in front of a door to a house on this street. Your task is to count the number of houses in the street.

The class Street contains the following functions which may help you:

void openDoor(): Open the door of the house you are in front of.
void closeDoor(): Close the door of the house you are in front of.
boolean isDoorOpen(): Returns true if the door of the current house is open and false otherwise.
void moveRight(): Move to the right house.
void moveLeft(): Move to the left house.

Return ans which represents the number of houses on this street.


Example 1:

Input: street = [0,0,0,0], k = 10
Output: 4
Explanation: There are 4 houses, and all their doors are closed.
The number of houses is less than k, which is 10.

Example 2:

Input: street = [1,0,1,1,0], k = 5
Output: 5
Explanation: There are 5 houses, and the doors of the 1st, 3rd, and 4th house (moving in the right direction) are open, and the rest are closed.
The number of houses is equal to k, which is 5.


Constraints:

n == number of houses
1 <= n <= k <= 10^3

"""

# V0
# IDEA : INTERACTIVE - NORMALIZE THE STREET, THEN CLOSE-AND-COUNT ONE LAP
#
#   the initial door states are arbitrary, so we cannot use them as markers.
#   phase 1 : walk k steps (k >= n, so we cover every house at least once)
#             opening every door -> the whole street is now known to be OPEN.
#   phase 2 : from wherever we stand, keep closing the current door and
#             stepping on, counting each house. The moment we meet a door that
#             is ALREADY closed we have come back to the very first house we
#             closed, i.e. we walked exactly one full lap -> that count is n.
#
#   NOTE : phase 1 must move in the SAME direction as phase 2 (here: left),
#          but any consistent direction works since the street is circular.
#   NOTE : the door must be closed BEFORE moving on, otherwise the loop guard
#          never sees the marker again and it spins forever.
#
# time = O(k), space = O(1)
class Solution(object):
    def houseCount(self, street, k):
        # phase 1 : open every door
        for _ in range(k):
            street.openDoor()
            street.moveLeft()
        # phase 2 : close doors until we hit the first one we closed
        res = 0
        while street.isDoorOpen():
            street.closeDoor()
            street.moveLeft()
            res += 1
        return res


# V0-1
# IDEA : INTERACTIVE - NORMALIZE EVERY DOOR OPEN, LEAVE ONE CLOSED MARKER,
#        MEASURE THE RETURN DISTANCE
#
#   phase 1 : k >= n steps in one direction opening every door -> the whole
#             street is OPEN and, crucially, we are back where we started
#             only after a whole number of laps, so "here" is a well defined
#             house.
#   phase 2 : close THIS door and nothing else. now exactly one door in the
#             street is closed, so walking right and counting steps until the
#             next closed door is met can only stop when we are standing on
#             that same house again -> the step count is exactly n.
#
#   difference from V0 : V0 mutates as it walks and detects the boundary
#   between the already-processed and not-yet-processed part of the lap. Here a
#   single unique marker is planted first and the walk is read-only, which is
#   what makes the counting loop trivially obviously terminating.
#
# time = O(k)
# space = O(1)
class Solution(object):
    def houseCount(self, street, k):
        # phase 1 : make the whole street open (k >= n, so every house is hit)
        for _ in range(k):
            street.openDoor()
            street.moveRight()
        # phase 2 : plant the single closed marker, then walk back to it
        street.closeDoor()
        res = 0
        while True:
            street.moveRight()
            res += 1
            if not street.isDoorOpen():
                return res


# V0-2
# IDEA : INTERACTIVE - DIFFERENTIAL PROBING ("POKE A DOOR, SEE IT MOVE")
#
#   no normalization pass at all: identify the starting house by CAUSALITY.
#   for a candidate distance d, look at the house d steps to the right twice --
#   once with the start door forced OPEN and once with it forced CLOSED. If the
#   observed state flipped with it, the house d steps away IS the start house,
#   i.e. d is a multiple of n; the smallest such d is n itself.
#   for d < n the probed house is a different house, untouched by our poking,
#   so both readings agree and the candidate is rejected -- no matter what its
#   initial door state happened to be.
#
#   this is the brute force of the family (every candidate is walked to and
#   back, so ~4 * d moves per candidate) but it needs neither a normalized
#   street nor any assumption about the initial pattern.
#
# time = O(k^2)
# space = O(1)
class Solution(object):
    def houseCount(self, street, k):
        def probe(d):
            for _ in range(d):
                street.moveRight()
            seen = street.isDoorOpen()
            for _ in range(d):
                street.moveLeft()
            return seen

        for d in range(1, k + 1):
            street.openDoor()
            with_open = probe(d)
            street.closeDoor()
            with_closed = probe(d)
            if with_open and not with_closed:
                return d
        return k

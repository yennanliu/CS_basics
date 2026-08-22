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


# V0-1
# IDEA : OBSERVE THE PERIOD (KMP), THEN LIFT IT TO n BY PROBING MULTIPLES
#
#   observation alone can never give n : [1,0,1,0] and [1,0] look identical
#   from inside the street. What observation DOES give is the smallest period
#   p of the door pattern, and p necessarily divides n -- so n = p * t and only
#   the small factor t is still unknown.
#
#   step 1 : read 2k door states while walking right (no mutation). The window
#            is n-periodic and at least twice a lap long, so by Fine & Wilf its
#            smallest window period is exactly the street's smallest period p.
#            KMP's failure function gives it as L - border(L) in O(k).
#   step 2 : park on an open door a and CLOSE it -- the single asymmetry we are
#            allowed to introduce.
#   step 3 : hop in strides of p. Since the pattern is p-periodic, every house
#            a + j*p was open, and the only one that is now closed is a itself.
#            So the first stride that lands on a closed door is j = t, and
#            n = t * p.
#
#   cost note : this pays ~3k interactive calls where V0 needs ~2k; what it
#   buys is that the answer is derived (period x factor) instead of being read
#   off a single carefully guarded lap.
#
# time = O(k)
# space = O(k)   (the 2k observed door states)
class Solution(object):
    def houseCount(self, street, k):
        # step 1 : read a window of 2k states, then its smallest period via KMP
        L = 2 * k
        arr = []
        for _ in range(L):
            arr.append(street.isDoorOpen())
            street.moveRight()

        fail = [0] * L
        j = 0
        for i in range(1, L):
            while j and arr[i] != arr[j]:
                j = fail[j - 1]
            if arr[i] == arr[j]:
                j += 1
            fail[i] = j
        p = L - fail[L - 1]

        # step 2 : plant the asymmetry on an open door
        while not street.isDoorOpen():
            street.moveRight()
        street.closeDoor()

        # step 3 : stride by p until we land back on that door
        for t in range(1, k // p + 1):
            for _ in range(p):
                street.moveRight()
            if not street.isDoorOpen():
                return t * p
        return k


# V0-2
# IDEA : SMALLEST PERIOD BY BRUTE FORCE, THEN LIFT IT BY COUNTING OPEN DOORS
#
#   same first insight as V0-1 (n is a multiple of the observable period p),
#   but both halves are done the naive way:
#
#   step 1 : test every candidate q = 1, 2, ... against the whole 2k-state
#            window until one of them survives -> p, in O(k^2) instead of KMP's
#            O(k).
#   step 2 : the lift uses a global count rather than local probing. Each of the
#            t = n / p blocks is an identical copy, so if one block holds c open
#            doors the street holds exactly m = t * c of them. c is read off the
#            recorded window; m is obtained by walking k steps and closing every
#            open door we meet, which counts each open house exactly once (the
#            second visit finds it closed). Hence n = p * m / c.
#
#   this needs no anchor and no notion of "one full lap" at all -- only a ratio.
#
# time = O(k^2)
# space = O(k)
class Solution(object):
    def houseCount(self, street, k):
        # step 1 : window + brute force smallest period
        L = 2 * k
        arr = []
        for _ in range(L):
            arr.append(street.isDoorOpen())
            street.moveRight()

        p = next(q for q in range(1, k + 1)
                 if all(arr[i] == arr[i + q] for i in range(L - q)))

        # step 2 : c = open doors per period block, m = open doors in total
        c = sum(1 for x in arr[:p] if x)
        m = 0
        for _ in range(k):
            if street.isDoorOpen():
                m += 1
                street.closeDoor()
            street.moveRight()
        return p * m // c

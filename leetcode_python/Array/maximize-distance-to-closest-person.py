"""

849. Maximize Distance to Closest Person
Medium

You are given an array representing a row of seats where seats[i] = 1 represents a person sitting in the i^th seat, and seats[i] = 0 represents that the i^th seat is empty (0-indexed).

There is at least one empty seat, and at least one person sitting.

Alex wants to sit in the seat such that the distance between him and the closest person to him is maximized.

Return that maximum distance to the closest person.

Example 1:

https://assets.leetcode.com/uploads/2020/09/10/distance.jpg

Input: seats = [1,0,0,0,1,0,1]
Output: 2
Explanation:
If Alex sits in the second open seat (i.e. seats[2]), then the closest person has distance 2.
If Alex sits in any other open seat, the closest person has distance 1.
Thus, the maximum distance to the closest person is 2.

Example 2:

Input: seats = [1,0,0,0]
Output: 3
Explanation:
If Alex sits in the last seat (i.e. seats[3]), the closest person is 3 seats away.
This is the maximum distance possible, so the answer is 3.

Example 3:

Input: seats = [0,1]
Output: 1

Constraints:

2 <= seats.length <= 2 * 10^4
seats[i] is 0 or 1.
At least one seat is empty.
At least one seat is occupied.

"""

# V0
# IDEA : ONE PASS OVER THE GAPS BETWEEN OCCUPIED SEATS
#
#   Every empty seat sits in one of three kinds of gap:
#     - leading gap  (before the first person): sit at seat 0  -> dist = first
#     - trailing gap (after the last person)  : sit at seat n-1 -> dist = n-1-last
#     - middle gap   (between two people p, q): sit in the middle -> dist = (q-p)//2
#   The answer is the max over all gaps, so one scan that remembers the
#   previous occupied seat is enough.
#
#   e.g. seats = [1,0,0,0,1,0,1]
#        people at 0,4,6 -> middle gaps (4-0)//2 = 2, (6-4)//2 = 1
#        no leading / trailing gap -> answer 2
#
# time = O(n), space = O(1)
class Solution(object):
    def maxDistToClosest(self, seats):
        """
        :type seats: List[int]
        :rtype: int
        """
        n = len(seats)
        prev_person = -1  # index of the last occupied seat seen, -1 = none yet
        best = 0

        for i in range(n):
            if seats[i] != 1:
                continue

            if prev_person == -1:
                # leading gap: Alex sits at seat 0, i seats away from this person
                best = i
            else:
                # middle gap: the best seat is halfway between the two people
                best = max(best, (i - prev_person) // 2)

            prev_person = i

        # NOTE !!! the trailing gap is only measured after the loop:
        #          Alex sits at the last seat, n-1-prev_person away
        best = max(best, n - 1 - prev_person)

        return best


# V1
# IDEA : TWO PASSES — DISTANCE TO THE NEAREST PERSON ON EACH SIDE
#
#   left[i]  = distance from seat i to the closest person at or before i
#   right[i] = distance from seat i to the closest person at or after i
#   The closest person to an empty seat i is min(left[i], right[i]);
#   the answer is the max of that over all empty seats.
#   Easier to derive live than V0, at the cost of O(n) extra space.
#
# time = O(n), space = O(n)
class Solution2(object):
    def maxDistToClosest(self, seats):
        """
        :type seats: List[int]
        :rtype: int
        """
        n = len(seats)
        NO_PERSON = float('inf')

        # left-to-right: distance to the nearest person on the left
        left = [NO_PERSON] * n
        for i in range(n):
            if seats[i] == 1:
                left[i] = 0
            elif i > 0 and left[i - 1] != NO_PERSON:
                left[i] = left[i - 1] + 1

        # right-to-left: distance to the nearest person on the right
        right = [NO_PERSON] * n
        for i in range(n - 1, -1, -1):
            if seats[i] == 1:
                right[i] = 0
            elif i < n - 1 and right[i + 1] != NO_PERSON:
                right[i] = right[i + 1] + 1

        best = 0
        for i in range(n):
            if seats[i] == 0:
                closest = min(left[i], right[i])
                best = max(best, closest)

        return best

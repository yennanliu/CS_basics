"""

2212. Maximum Points in an Archery Competition
Medium

Alice and Bob are opponents in an archery competition. The competition has set the following rules:

Alice first shoots numArrows arrows and then Bob shoots numArrows arrows.
The points are then calculated as follows:
The target has integer scoring sections ranging from 0 to 11 inclusive.
For each section of the target with score k (in between 0 to 11), say Alice and Bob have shot ak and bk arrows on that section respectively. If ak >= bk, then Alice takes k points. If ak < bk, then Bob takes k points.
However, if ak == bk == 0, then nobody takes k points.

For example, if Alice and Bob both shot 2 arrows on the section with score 11, then Alice takes 11 points. On the other hand, if Alice shot 0 arrows on the section with score 11 and Bob shot 2 arrows on that same section, then Bob takes 11 points.

You are given the integer numArrows and an integer array aliceArrows of size 12, which represents the number of arrows Alice shot on each scoring section from 0 to 11. Now, Bob wants to maximize the total number of points he can obtain.

Return the array bobArrows which represents the number of arrows Bob shot on each scoring section from 0 to 11. The sum of the values in bobArrows should equal numArrows.

If there are multiple ways for Bob to earn the maximum total points, return any one of them.


Example 1:

Input: numArrows = 9, aliceArrows = [1,1,0,1,0,0,2,1,0,1,2,0]
Output: [0,0,0,0,1,1,0,0,1,2,3,1]
Explanation: The table above shows how the competition is scored.
Bob earns a total point of 4 + 5 + 8 + 9 + 10 + 11 = 47.

Example 2:

Input: numArrows = 3, aliceArrows = [0,0,1,0,0,0,0,0,0,0,0,2]
Output: [0,0,0,0,0,0,0,0,1,1,1,0]
Explanation: The table above shows how the competition is scored.
Bob earns a total point of 8 + 9 + 10 = 27.


Constraints:

1 <= numArrows <= 10^5
aliceArrows.length == bobArrows.length == 12
0 <= aliceArrows[i], numArrows[i] <= numArrows
sum(aliceArrows[i]) == numArrows

"""

# V0
# IDEA : ONLY 12 SECTIONS -> ENUMERATE ALL 2^12 SUBSETS BOB COULD WIN
#
#   to win section k Bob must shoot strictly more arrows than Alice, and the
#   cheapest way is exactly aliceArrows[k] + 1. so a candidate answer is just
#   a SUBSET of sections Bob claims; its cost and its score are both fixed.
#
#   4096 subsets is nothing, so try them all, keep the ones affordable within
#   numArrows, and track the best score.
#
#   NOTE : any arrows left over must still be shot — dump them into section 0
#          (worth nothing), keeping the sum exactly numArrows.
#
# time = O(2^12 * 12), space = O(1)
class Solution(object):
    def maximumBobPoints(self, numArrows, aliceArrows):
        best_score = -1
        best = None
        for mask in range(1 << 12):
            cost = 0
            score = 0
            for k in range(12):
                if mask >> k & 1:
                    cost += aliceArrows[k] + 1
                    score += k
            if cost > numArrows or score <= best_score:
                continue
            best_score = score
            bob = [0] * 12
            for k in range(12):
                if mask >> k & 1:
                    bob[k] = aliceArrows[k] + 1
            bob[0] += numArrows - cost      # spend the leftovers harmlessly
            best = bob
        return best

"""

1503. Last Moment Before All Ants Fall Out of a Plank
Medium

We have a wooden plank of the length n units. Some ants are walking on the plank,
each ant moves with a speed of 1 unit per second. Some of the ants move to the left,
the other move to the right.

When two ants moving in two different directions meet at some point,
they change their directions and continue moving again.
Assume changing directions does not take any additional time.

When an ant reaches one end of the plank at a time t, it falls out of the plank immediately.

Given an integer n and two integer arrays left and right,
the positions of the ants moving to the left and the right,
return the moment when the last ant(s) fall out of the plank.


Example 1:

Input: n = 4, left = [4,3], right = [0,1]
Output: 4
Explanation:
-The ant at index 0 is named A and going to the right.
-The ant at index 1 is named B and going to the right.
-The ant at index 3 is named C and going to the left.
-The ant at index 4 is named D and going to the left.
The last moment when an ant was on the plank is t = 4 seconds.
After that, it falls immediately out of the plank.

Example 2:

Input: n = 7, left = [], right = [0,1,2,3,4,5,6,7]
Output: 7
Explanation: All ants are going to the right, the ant at index 0 needs 7 seconds to fall.

Example 3:

Input: n = 7, left = [0,1,2,3,4,5,6,7], right = []
Output: 7
Explanation: All ants are going to the left, the ant at index 7 needs 7 seconds to fall.


Constraints:

1 <= n <= 10^4
0 <= left.length <= n + 1
0 <= left[i] <= n
0 <= right.length <= n + 1
0 <= right[i] <= n
1 <= left.length + right.length <= n + 1
All values of left and right are unique, and each value can appear only in one of the two arrays.

"""

# V0
# IDEA : BRAIN TEASER
#
#   two ants bouncing off each other is INDISTINGUISHABLE from
#   the two ants simply passing through each other (they are identical,
#   only the multiset of positions matters).
#
#   -> so every ant just walks straight to its own end,
#      and the answer is the largest single walking distance:
#        * an ant going left  at x needs x       seconds
#        * an ant going right at x needs (n - x) seconds
#
#   NOTE : left / right can be empty, so start the max at 0
# time = O(n)
# space = O(1)
class Solution(object):
    def getLastMoment(self, n, left, right):
        res = 0
        for x in left:
            res = max(res, x)
        for x in right:
            res = max(res, n - x)
        return res

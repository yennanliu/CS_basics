"""

3572. Maximize Y-Sum by Picking a Triplet of Distinct X-Values
Medium

You are given two integer arrays x and y, each of length n. You must choose three distinct indices i, j, and k such that:

x[i] != x[j]
x[j] != x[k]
x[k] != x[i]

Your goal is to maximize the value of y[i] + y[j] + y[k] under these conditions. Return the maximum possible sum that can be obtained by choosing such a triplet of indices.

If no such triplet exists, return -1.


Example 1:

Input: x = [1,2,1,3,2], y = [5,3,4,6,2]
Output: 14
Explanation:
Choose i = 0 (x[i] = 1, y[i] = 5), j = 1 (x[j] = 2, y[j] = 3), k = 3 (x[k] = 3, y[k] = 6).
All three values chosen from x are distinct. 5 + 3 + 6 = 14 is the maximum we can obtain. Hence, the output is 14.

Example 2:

Input: x = [1,2,1,2], y = [4,5,6,7]
Output: -1
Explanation:
There are only two distinct values in x. Hence, the output is -1.


Constraints:

n == x.length == y.length
3 <= n <= 10^5
1 <= x[i], y[i] <= 10^6

"""

# V0
# IDEA : KEEP ONLY THE BEST Y PER DISTINCT X, THEN TAKE THE TOP THREE
#
#   the constraint only forbids reusing an x value, so for a given x value
#   there is never a reason to pick anything but its largest y — swapping in
#   the largest y keeps the triplet legal and can only raise the sum.
#
#   after that collapse the problem is "sum of the three largest numbers",
#   and it is impossible exactly when fewer than three distinct x values exist.
#
# time = O(n), space = O(n)
class Solution(object):
    def maxSumDistinctTriplet(self, x, y):
        best = {}
        for xi, yi in zip(x, y):
            if xi not in best or best[xi] < yi:
                best[xi] = yi
        if len(best) < 3:
            return -1
        top = sorted(best.values(), reverse=True)[:3]
        return sum(top)

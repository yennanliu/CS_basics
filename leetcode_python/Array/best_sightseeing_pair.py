"""

1014. Best Sightseeing Pair
Medium

Given an array A of positive integers, A[i] represents the value of the i-th sightseeing spot, and two sightseeing spots i and j have distance j - i between them.

The score of a pair (i < j) of sightseeing spots is (A[i] + A[j] + i - j) : the sum of the values of the sightseeing spots, minus the distance between them.

Return the maximum score of a pair of sightseeing spots.

Example 1:

Input: [8,1,5,2,6]
Output: 11
Explanation: i = 0, j = 2, A[i] + A[j] + i - j = 8 + 5 + 0 - 2 = 11
 
Note:

2 <= A.length <= 50000
1 <= A[i] <= 1000

"""

# V0
class Solution(object):
    def maxScoreSightseeingPair(self, values):
        """
        :type values: List[int]
        :rtype: int
        """
        pass


# V0-1


# V0-2

# V0-3
# IDEA : ONE PASS, KEEP THE BEST "LEFT HALF" SEEN SO FAR (CLAUDE)
#
#   score(i, j) = (values[i] + i) + (values[j] - j)     for i < j
#
#   the two halves are independent : for a fixed j, the best partner is
#   just the max of (values[i] + i) over every i < j. so sweep j left to
#   right, carrying that max — Kadane-style "best so far".
#
#   e.g. [8,1,5,2,6] : at j = 2, best_left = 8 + 0 = 8 -> 8 + (5 - 2) = 11
#
# time = O(n), space = O(1)
class Solution(object):
    def maxScoreSightseeingPair(self, values):
        """
        :type values: List[int]
        :rtype: int
        """
        # best (values[i] + i) over every i to the left of the current j
        best_left = values[0] + 0
        best_score = float('-inf')

        for j in range(1, len(values)):
            # NOTE !!! score with j FIRST, then let j become a left candidate,
            #          so a spot never pairs with itself
            score = best_left + values[j] - j
            best_score = max(best_score, score)
            best_left = max(best_left, values[j] + j)

        return best_score


# V1
# IDEA : DP — PRECOMPUTE THE BEST "RIGHT HALF" FOR EVERY i (CLAUDE)
#
#   best_right[i] = max of (values[j] - j) over j > i
#   answer        = max over i of  values[i] + i + best_right[i]
#
#   the same split as V0, written as an explicit suffix-max table — easier
#   to explain first, then compress to V0's single variable.
#
# time = O(n), space = O(n)
class Solution2(object):
    def maxScoreSightseeingPair(self, values):
        """
        :type values: List[int]
        :rtype: int
        """
        n = len(values)
        NEG_INF = float('-inf')

        # best_right[i] = max(values[j] - j) for j in (i, n)
        best_right = [NEG_INF] * n
        for i in range(n - 2, -1, -1):
            candidate = values[i + 1] - (i + 1)
            best_right[i] = max(best_right[i + 1], candidate)

        best_score = NEG_INF
        for i in range(n - 1):
            best_score = max(best_score, values[i] + i + best_right[i])

        return best_score

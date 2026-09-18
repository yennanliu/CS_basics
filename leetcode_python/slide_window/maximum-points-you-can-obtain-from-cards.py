"""

1423. Maximum Points You Can Obtain from Cards
Medium

There are several cards arranged in a row, and each card has an associated number
of points. The points are given in the integer array cardPoints.

In one step, you can take one card from the beginning or from the end of the row.
You have to take exactly k cards.

Your score is the sum of the points of the cards you have taken.

Given the integer array cardPoints and the integer k, return the maximum score you
can obtain.


Example 1:

Input: cardPoints = [1,2,3,4,5,6,1], k = 3
Output: 12
Explanation: After the first step, your score will always be 1. However, choosing
the rightmost card first will maximize your total score. The optimal strategy is to
take the three cards on the right, giving a final score of 1 + 6 + 5 = 12.

Example 2:

Input: cardPoints = [2,2,2], k = 2
Output: 4
Explanation: Regardless of which two cards you take, your score will always be 4.

Example 3:

Input: cardPoints = [9,7,7,9,7,7,9], k = 7
Output: 55
Explanation: You have to take all the cards. Your score is the sum of points of all
cards.


Constraints:

1 <= cardPoints.length <= 10^5
1 <= cardPoints[i] <= 10^4
1 <= k <= cardPoints.length

"""

# V0
# IDEA : SLIDING WINDOW ON THE COMPLEMENT (what you LEAVE is contiguous)
#
#   the cards you take are a prefix plus a suffix -- awkward to enumerate.
#   but the cards you leave behind are exactly one contiguous block of size
#   n - k, and taking the most is the same as leaving the least:
#
#     answer = sum(all) - min window sum of length (n - k)
#
#   e.g. [1,2,3,4,5,6,1], k = 3 -> leave a block of 4
#        min block = 1+2+3+4 = 10, total = 22 -> 22 - 10 = 12
#
#   NOTE !!! k == n leaves a window of length 0 -> answer is the whole sum, and
#            the loop below must not run at all for it.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxScore(self, cardPoints, k):
        """
        :type cardPoints: List[int]
        :type k: int
        :rtype: int
        """
        # edge
        if not cardPoints or k <= 0:
            return 0

        n = len(cardPoints)
        total = sum(cardPoints)

        m = n - k
        if m <= 0:
            return total

        cur = sum(cardPoints[:m])
        mn = cur
        for i in range(m, n):
            cur += cardPoints[i] - cardPoints[i - m]
            if cur < mn:
                mn = cur

        return total - mn

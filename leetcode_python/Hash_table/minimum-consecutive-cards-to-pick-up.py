"""

2260. Minimum Consecutive Cards to Pick Up
Medium

You are given an integer array cards where cards[i] represents the value of the ith card. A pair of cards are matching if the cards have the same value.

Return the minimum number of consecutive cards you have to pick up to have a pair of matching cards among the picked cards. If it is impossible to have matching cards, return -1.


Example 1:

Input: cards = [3,4,2,3,4,7]
Output: 4
Explanation: We can pick up the cards [3,4,2,3] which contain a matching pair of cards with value 3. Note that picking up the cards [4,2,3,4] is also optimal.

Example 2:

Input: cards = [1,0,5,3]
Output: -1
Explanation: There is no way to pick up a set of consecutive cards that contain a pair of matching cards.


Constraints:

1 <= cards.length <= 10^5
0 <= cards[i] <= 10^6

"""

# V0
# IDEA : HASH TABLE (closest previous index of the same value)
#
#   any window that contains a matching pair contains two EQUAL values;
#   the shortest such window is bounded by two ADJACENT equal values.
#   so keep last[v] = most recent index of v and, on each new index i,
#   the candidate answer is i - last[cards[i]] + 1.
#
#   NOTE : we only ever need the most recent occurrence - an older one can
#          only give a longer window.
#
# time = O(n), space = O(n)
class Solution(object):
    def minimumCardPickup(self, cards):
        last = {}
        res = -1
        for i, v in enumerate(cards):
            if v in last:
                span = i - last[v] + 1
                if res == -1 or span < res:
                    res = span
            last[v] = i
        return res

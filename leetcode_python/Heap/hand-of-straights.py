"""

846. Hand of Straights
Medium

Alice has some number of cards and she wants to rearrange the cards into groups so that each group is of size groupSize, and consists of groupSize consecutive cards.

Given an integer array hand where hand[i] is the value written on the ith card and an integer groupSize, return true if she can rearrange the cards, or false otherwise.

 

Example 1:

Input: hand = [1,2,3,6,2,3,4,7,8], groupSize = 3
Output: true
Explanation: Alice's hand can be rearranged as [1,2,3],[2,3,4],[6,7,8]
Example 2:

Input: hand = [1,2,3,4,5], groupSize = 4
Output: false
Explanation: Alice's hand can not be rearranged into groups of 4.

 

Constraints:

1 <= hand.length <= 104
0 <= hand[i] <= 109
1 <= groupSize <= hand.length
 

Note: This question is the same as 1296: https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/

"""

# V0 

# V1 
# http://bookshadow.com/weblog/2018/06/03/leetcode-hand-of-straights/
# IDEA: 
# hand = [1, 2, 3, 6, 2, 3, 4, 7, 8]
# handDict = collections.Counter(hand)
# then handDict  = Counter({1: 1, 2: 2, 3: 2, 4: 1, 6: 1, 7: 1, 8: 1})
# so we can for loop visit above counter Dict and do op on it 
import collections
class Solution(object):
    def isNStraightHand(self, hand, W):
        """
        :type hand: List[int]
        :type W: int
        :rtype: bool
        """
        handDict = collections.Counter(hand)
        while handDict:
            mink = min(handDict.keys())
            for y in range(mink, mink + W):
                if not handDict[y]: return False
                handDict[y] -= 1
                if not handDict[y]: del handDict[y] # delete this dict key since there no element under this key (e.g. if handDict = Counter({1: 0, 2: 2, 3: 2, 4: 1, 6: 1, 7: 1, 8: 1}), then del handDict[1])
        return True

# V2 
# Time:  O(nlogn)
# Space: O(n)
from collections import Counter
from heapq import heapify, heappop
class Solution(object):
    def isNStraightHand(self, hand, W):
        """
        :type hand: List[int]
        :type W: int
        :rtype: bool
        """
        if len(hand) % W:
            return False

        counts = Counter(hand)
        min_heap = list(hand)
        heapify(min_heap)
        for _ in range(len(min_heap)//W):
            while counts[min_heap[0]] == 0:
                heappop(min_heap)
            start = heappop(min_heap)
            for _ in range(W):
                counts[start] -= 1
                if counts[start] < 0:
                    return False
                start += 1
        return True
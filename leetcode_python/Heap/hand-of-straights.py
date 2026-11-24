"""

846. Hand of Straights
Medium

Alice has some number of cards and she wants to rearrange the cards into groups
so that each group is of size groupSize, and consists of groupSize consecutive
cards.

Given an integer array hand where hand[i] is the value written on the ith card
and an integer groupSize, return true if she can rearrange the cards, or false
otherwise.

Example 1:

Input: hand = [1,2,3,6,2,3,4,7,8], groupSize = 3

Output: true

Explanation: Alice's hand can be rearranged as [1,2,3],[2,3,4],[6,7,8]

Example 2:

Input: hand = [1,2,3,4,5], groupSize = 4

Output: false

Explanation: Alice's hand can not be rearranged into groups of 4.

Constraints:

1 <= hand.length <= 10^4

0 <= hand[i] <= 10^9

1 <= groupSize <= hand.length

Note: This question is the same as 1296:
https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/

"""

# V0
# IDEA : GREEDY + HASHMAP (COUNTER) + SORTED DISTINCT VALUES
#
#   The SMALLEST card left can only be the START of a group: nothing smaller
#   is left to precede it. So every copy of it must open its own group
#   [card, card + 1, ..., card + groupSize - 1], and each of those values must
#   have at least that many copies left. Greedily consume and move on.
#
#   e.g. hand = [1,2,3,6,2,3,4,7,8], k = 3
#        count = {1:1, 2:2, 3:2, 4:1, 6:1, 7:1, 8:1}
#        1 -> open 1 group [1,2,3]  -> {2:1, 3:1, 4:1, 6:1, 7:1, 8:1}
#        2 -> open 1 group [2,3,4]  -> {6:1, 7:1, 8:1}
#        6 -> open 1 group [6,7,8]  -> {}             -> True
#
#   Processing a whole value at once (need = its count) means each distinct
#   value is visited once, instead of once per card.
#
# time = O(n + d log d), d = distinct values (the sort), <= O(n log n)
# space = O(n)
from collections import Counter


class Solution(object):
    def isNStraightHand(self, hand, groupSize):
        """
        :type hand: List[int]
        :type groupSize: int
        :rtype: bool
        """
        # the cards must split evenly into groups
        if len(hand) % groupSize != 0:
            return False

        card_count = Counter(hand)

        # visit distinct values smallest first
        for card in sorted(card_count):
            need = card_count[card]
            # already used up as the middle / end of earlier groups
            if need == 0:
                continue

            # every remaining copy of `card` starts a group,
            # so each value in the run must cover `need` copies
            for offset in range(groupSize):
                next_card = card + offset
                # NOTE !!! Counter returns 0 for a missing key, so a gap
                #          in the run (e.g. no 5 in 3,4,_,6) fails here
                if card_count[next_card] < need:
                    return False
                card_count[next_card] -= need

        return True


# V1
# IDEA : GREEDY + MIN-HEAP OF DISTINCT VALUES
#
#   Same greedy as V0, one group at a time: the heap's top is always the
#   smallest value still holding cards, so it starts the next group.
#   Extra check: a value can only run out when it IS the heap top. If a value
#   in the middle of a group hits 0 while a smaller value still has cards,
#   that smaller value can never finish a group of its own -> False.
#
#   e.g. hand = [1,1,2,3], k = 2 -> group [1,2] empties 2 while 1 is still
#        on top with 1 card left -> 1 would need a 2 that is gone -> False
#
# time = O(n log d) : n cards consumed, each heap pop is O(log d)
# space = O(d)
import heapq
from collections import Counter


class Solution2(object):
    def isNStraightHand(self, hand, groupSize):
        """
        :type hand: List[int]
        :type groupSize: int
        :rtype: bool
        """
        if len(hand) % groupSize != 0:
            return False

        card_count = Counter(hand)

        # one heap entry per distinct value, popped once it runs out
        min_heap = list(card_count.keys())
        heapq.heapify(min_heap)

        while min_heap:
            start = min_heap[0]

            # build one group [start, start + groupSize - 1]
            for card in range(start, start + groupSize):
                if card_count[card] == 0:
                    return False
                card_count[card] -= 1

                if card_count[card] == 0:
                    # NOTE !!! only the smallest value may run out;
                    #          anything else strands a smaller card
                    if card != min_heap[0]:
                        return False
                    heapq.heappop(min_heap)

        return True


# V2
# IDEA : HASHMAP, FIND EACH STREAK'S START (NO SORT)
#
#   Instead of sorting, for any card walk LEFT while card - 1 still has copies:
#   that reaches the smallest value of its consecutive streak, which (as in V0)
#   must start groups. Then consume groups from that start up to the card.
#   Each value's count only goes down, so the walks are amortised O(n).
#
# NOTE : O(n) but harder to explain live — give V0 in an interview,
#        mention this as the follow-up.
#
# time = O(n) amortised
# space = O(n)
from collections import Counter


class Solution3(object):
    def isNStraightHand(self, hand, groupSize):
        """
        :type hand: List[int]
        :type groupSize: int
        :rtype: bool
        """
        if len(hand) % groupSize != 0:
            return False

        card_count = Counter(hand)

        for card in hand:
            # walk left to the start of this card's streak
            start = card
            while card_count[start - 1] > 0:
                start -= 1

            # from the streak start up to `card`, every value with cards
            # left must open groups, smallest first
            while start <= card:
                while card_count[start] > 0:
                    for value in range(start, start + groupSize):
                        if card_count[value] == 0:
                            return False
                        card_count[value] -= 1
                start += 1

        return True

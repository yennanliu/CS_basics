"""

2347. Best Poker Hand
Easy

You are given an integer array ranks and a character array suits. You have 5 cards where the ith card has a rank of ranks[i] and a suit of suits[i].

The following are the types of poker hands you can make from best to worst:

"Flush": Five cards of the same suit.
"Three of a Kind": Three cards of the same rank.
"Pair": Two cards of the same rank.
"High Card": Any single card.

Return a string representing the best type of poker hand you can make with the given cards.

Note that the return values are case-sensitive.


Example 1:

Input: ranks = [13,2,3,1,9], suits = ["a","a","a","a","a"]
Output: "Flush"
Explanation: The hand with all the cards consists of 5 cards with the same suit, so we have a "Flush".

Example 2:

Input: ranks = [4,4,2,4,4], suits = ["d","a","a","b","c"]
Output: "Three of a Kind"
Explanation: The hand with the first, second, and fourth card consists of 3 cards with the same rank, so we have a "Three of a Kind".
Note that we could also make a "Pair" hand but "Three of a Kind" is a better hand.
Also note that other cards could be used to make the "Three of a Kind" hand.

Example 3:

Input: ranks = [10,10,2,12,9], suits = ["a","b","c","a","d"]
Output: "Pair"
Explanation: The hand with the first and second card consists of 2 cards with the same rank, so we have a "Pair".
Note that we cannot make a "Flush" or a "Three of a Kind".


Constraints:

ranks.length == suits.length == 5
1 <= ranks[i] <= 13
suits[i] is either 'a', 'b', 'c', or 'd'.
All the given cards are unique.

"""

# V0
# IDEA : TEST THE FOUR HANDS IN DESCENDING QUALITY AND RETURN THE FIRST HIT
#
#   with exactly 5 cards :
#     Flush           -> all five suits identical, i.e. one distinct suit
#     Three of a Kind -> some rank appears at least 3 times
#     Pair            -> some rank appears at least 2 times
#     High Card       -> otherwise
#
#   checking in this order is what makes "best hand" fall out — example 2 has
#   both a triple and a pair, and the triple wins.
#
# time = O(1), space = O(1)
from collections import Counter


class Solution(object):
    def bestHand(self, ranks, suits):
        if len(set(suits)) == 1:
            return "Flush"
        top = max(Counter(ranks).values())
        if top >= 3:
            return "Three of a Kind"
        if top >= 2:
            return "Pair"
        return "High Card"

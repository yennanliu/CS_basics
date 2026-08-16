"""

3664. Two-Letter Card Game
Medium

You are given a deck of cards represented by a string array cards, and each
card displays two lowercase letters.

You are also given a letter x. You play a game with the following rules:

Start with 0 points.
On each turn, you must find two compatible cards from the deck that both
contain the letter x in any position.
Remove the pair of cards and earn 1 point.
The game ends when you can no longer find a pair of compatible cards.

Return the maximum number of points you can gain with optimal play.

Two cards are compatible if the strings differ in exactly 1 position.


Example 1:

Input: cards = ["aa","ab","ba","ac"], x = "a"
Output: 2
Explanation:
On the first turn, select and remove cards "ab" and "ac", which are
compatible because they differ at only index 1.
On the second turn, select and remove cards "aa" and "ba", which are
compatible because they differ at only index 0.
Because there are no more compatible pairs, the total score is 2.

Example 2:

Input: cards = ["aa","ab","ba"], x = "a"
Output: 1
Explanation:
On the first turn, select and remove cards "aa" and "ba".
Because there are no more compatible pairs, the total score is 1.

Example 3:

Input: cards = ["aa","ab","ba","ac"], x = "b"
Output: 0
Explanation:
The only cards that contain the character 'b' are "ab" and "ba". However,
they differ in both indices, so they are not compatible. Thus, the output is
0.


Constraints:

2 <= cards.length <= 10^5
cards[i].length == 2
Each cards[i] is composed of only lowercase English letters between 'a' and
'j'.
x is a lowercase English letter between 'a' and 'j'.

"""

# V0
# IDEA : MAXIMUM MATCHING IN TWO COMPLETE MULTIPARTITE GRAPHS SHARING "xx"
#
#   only cards containing x matter, and they come in three shapes: "xc"
#   (x leading), "cx" (x trailing) and "xx". work out which pairs differ in
#   exactly one position:
#     "xc" vs "xd" -> compatible iff c != d
#     "cx" vs "dx" -> compatible iff c != d
#     "xc" vs "dx" -> NEVER (both positions differ)
#     "xx" vs anything of the other two shapes -> always
#     "xx" vs "xx" -> never (identical, zero positions differ)
#   so grouping the "xc" cards by c and the "cx" cards by c, each side is a
#   COMPLETE MULTIPARTITE graph, and for such a graph the maximum matching
#   has the closed form min(N // 2, N - largest_part): either everything
#   pairs up, or one oversized part swamps the rest and only N - L pairs
#   exist.
#
#   the "xx" cards are the only bridge. every edge lies wholly on one side,
#   so any matching splits the "xx" cards into those consumed on the left
#   side and those on the right -- and since they form their own part
#   (mutually incompatible) on whichever side they join, one scan over the
#   split point t settles it. giving all of them out is never worse, since
#   adding a vertex cannot shrink a maximum matching.
#
# time = O(n + z), space = O(1)
class Solution(object):
    def score(self, cards, x):
        first = [0] * 26          # cards "x c", keyed by c
        second = [0] * 26         # cards "c x", keyed by c
        both = 0                  # cards "xx"
        xi = ord(x)
        for c in cards:
            a, b = c[0], c[1]
            if a == x and b == x:
                both += 1
            elif a == x:
                first[ord(b) - 97] += 1
            elif b == x:
                second[ord(a) - 97] += 1

        sa, ma = sum(first), max(first)
        sb, mb = sum(second), max(second)

        def match(total, biggest):
            # maximum matching of a complete multipartite graph
            return min(total // 2, total - biggest)

        best = 0
        for t in range(both + 1):
            u = both - t
            got = (match(sa + t, ma if ma > t else t)
                   + match(sb + u, mb if mb > u else u))
            if got > best:
                best = got
        return best

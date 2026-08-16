"""

3403. Find the Lexicographically Largest String From the Box I
Medium

You are given a string word, and an integer numFriends.

Alice is organizing a game for her numFriends friends. There are multiple rounds
in the game, where in each round:

word is split into numFriends non-empty strings, such that no previous round has
had the exact same split.
All the split words are put into a box.

Find the lexicographically largest string from the box after all the rounds are
finished.

Example 1:

Input: word = "dbca", numFriends = 2

Output: "dbc"

Explanation:

All possible splits are:

"d" and "bca".
"db" and "ca".
"dbc" and "a".

Example 2:

Input: word = "gggg", numFriends = 4

Output: "g"

Explanation:

The only possible split is: "g", "g", "g", and "g".

Constraints:

1 <= word.length <= 5 * 10^3
word consists only of lowercase English letters.
1 <= numFriends <= word.length

"""

# V0
# IDEA : EVERY SUBSTRING OF THE CAPPED LENGTH IS REACHABLE
#
#   over all the rounds, *every* way of cutting word into numFriends non-empty
#   pieces is used exactly once, so the box eventually holds every piece of
#   every split.  a piece word[i..j] can appear iff the rest of the string can
#   still be cut into numFriends - 1 non-empty parts, which is possible exactly
#   when the piece is no longer than n - (numFriends - 1).
#
#   so the box is precisely the set of substrings of length <= cap, and the
#   answer is the largest of them.  a longer candidate always beats its own
#   prefixes, so only the full-length window at each start index matters — n
#   candidates, and the biggest one wins.
#
#   numFriends == 1 is the degenerate case where the only "split" is the whole
#   word.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def answerString(self, word, numFriends):
        if numFriends == 1:
            return word
        n = len(word)
        cap = n - numFriends + 1
        best = ""
        for i in range(n):
            cand = word[i:i + cap]
            if cand > best:
                best = cand
        return best

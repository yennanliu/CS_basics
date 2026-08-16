"""

3406. Find the Lexicographically Largest String From the Box II
Hard

You are given a string word, and an integer numFriends.

Alice is organizing a game for her numFriends friends. There are multiple rounds
in the game, where in each round:

word is split into numFriends non-empty strings, such that no previous round has
had the exact same split.
All the split words are put into a box.

Find the lexicographically largest string from the box after all the rounds are
finished.

A string a is lexicographically smaller than a string b if in the first position
where a and b differ, string a has a letter that appears earlier in the alphabet
than the corresponding letter in b.
If the first min(a.length, b.length) characters do not differ, then the shorter
string is the lexicographically smaller one.

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

1 <= word.length <= 2 * 10^5
word consists only of lowercase English letters.
1 <= numFriends <= word.length

"""

# V0
# IDEA : MAXIMUM SUFFIX (DUVAL-STYLE TWO POINTER), THEN TRUNCATE
#
#   as in the easy version the box holds exactly the substrings of length at
#   most cap = n - numFriends + 1, so we want the largest such substring.  the
#   quadratic scan is too slow here, so use this fact:
#
#     let i be the start of the lexicographically largest *suffix* of word.
#     then word[i : i+cap] is the largest substring of length <= cap.
#
#   proof sketch: if some word[j : j+cap] were strictly bigger, then either they
#   first differ inside the window — which would already make word[j:] beat
#   word[i:] — or word[i:] is a proper prefix of it, which again makes word[j:]
#   the bigger suffix.  both contradict the maximality of i.
#
#   the maximal suffix itself comes from the classic two-candidate scan: keep a
#   champion start i and a challenger j, compare them character by character
#   with an offset k.  on a mismatch the loser and everything it "covers" can be
#   discarded outright, which is what makes the whole thing linear.
#
# time = O(n), space = O(n) for the returned slice
class Solution(object):
    def answerString(self, word, numFriends):
        if numFriends == 1:
            return word
        n = len(word)
        cap = n - numFriends + 1
        i, j, k = 0, 1, 0
        while j + k < n:
            a, b = word[i + k], word[j + k]
            if a == b:
                k += 1
            elif a > b:
                j += k + 1
                k = 0
            else:
                i = max(i + k + 1, j)
                j = i + 1
                k = 0
        return word[i:i + cap]

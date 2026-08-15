"""

3302. Find the Lexicographically Smallest Valid Sequence
Medium

You are given two strings word1 and word2.

A string x is called almost equal to y if you can change at most one character in x to make it identical to y.

A sequence of indices seq is called valid if:

The indices are sorted in ascending order.
Concatenating the characters at these indices in word1 in the same order results in a string that is almost equal to word2.

Return an array of size word2.length representing the lexicographically smallest valid sequence of indices. If no such sequence of indices exists, return an empty array.

Note that the answer must represent the lexicographically smallest array, not the corresponding string formed by those indices.


Example 1:

Input: word1 = "vbcca", word2 = "abc"
Output: [0,1,2]
Explanation:
The lexicographically smallest valid sequence of indices is [0, 1, 2]:
Change word1[0] to 'a'.
word1[1] is already 'b'.
word1[2] is already 'c'.

Example 2:

Input: word1 = "bacdc", word2 = "abc"
Output: [1,2,4]
Explanation:
The lexicographically smallest valid sequence of indices is [1, 2, 4]:
word1[1] is already 'a'.
Change word1[2] to 'b'.
word1[4] is already 'c'.

Example 3:

Input: word1 = "aaaaaa", word2 = "aaabc"
Output: []
Explanation:
There is no valid sequence of indices.

Example 4:

Input: word1 = "abc", word2 = "ab"
Output: [0,1]


Constraints:

1 <= word2.length < word1.length <= 3 * 10^5
word1 and word2 consist only of lowercase English letters.

"""

# V0
# IDEA : PRECOMPUTE HOW MUCH OF word2 EACH SUFFIX CAN STILL MATCH, THEN BE GREEDY
#
#   lexicographically smallest means taking the earliest index at every step,
#   so the only question is whether a choice leaves the REST completable.
#
#   suffix[i] = how many characters of word2, counted from its end, can be
#   matched by word1[i:] with no changes at all. one backward pass builds it.
#
#   then sweep forward with a budget of one mismatch :
#       if word1[i] == word2[j]           -> take it, no cost
#       else if the mismatch is unused and the remaining word1[i+1:] can
#            still cover word2[j+1:] exactly (suffix[i+1] >= len(word2)-j-1)
#                                          -> spend the mismatch here
#   spending the change as EARLY as possible is what makes the index sequence
#   smallest, and the suffix table is exactly the guard that keeps it valid.
#
# time = O(n + m), space = O(n)
class Solution(object):
    def validSequence(self, word1, word2):
        n, m = len(word1), len(word2)

        # suffix[i] = length of word2's suffix matchable by word1[i:] exactly
        suffix = [0] * (n + 1)
        j = m - 1
        for i in range(n - 1, -1, -1):
            suffix[i] = suffix[i + 1]
            if j >= 0 and word1[i] == word2[j]:
                suffix[i] += 1
                j -= 1

        res = []
        used = False
        j = 0
        for i in range(n):
            if j == m:
                break
            if word1[i] == word2[j]:
                res.append(i)
                j += 1
            elif not used and suffix[i + 1] >= m - j - 1:
                used = True
                res.append(i)
                j += 1
        return res if j == m else []

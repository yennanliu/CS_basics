"""

425. Word Squares
Hard

Given an array of unique strings words, return all the word squares you can build
from words. The same word from words can be used multiple times. You can return
the answer in any order.

A sequence of strings forms a valid word square if the kth row and column read the
same string, where 0 <= k < max(numRows, numColumns).

For example, the word sequence ["ball","area","lead","lady"] forms a word square
because each word reads the same both horizontally and vertically.


Example 1:

Input: words = ["area","lead","wall","lady","ball"]
Output: [["ball","area","lead","lady"],["wall","area","lead","lady"]]
Explanation:
The output consists of two word squares. The order of output does not matter
(just the order of words in each word square matters).

Example 2:

Input: words = ["abat","baba","atan","atal"]
Output: [["baba","abat","baba","atal"],["baba","abat","baba","atan"]]


Constraints:

1 <= words.length <= 1000
1 <= words[i].length <= 4
All words[i] have the same length.
words[i] consists of only lowercase English letters.
All words[i] are unique.

"""

# V0
# IDEA : PREFIX MAP + BACKTRACKING (row k is FORCED to match column k)
#
#   once rows 0..k-1 are placed, row k is not free : reading down column k of
#   the rows already placed spells the prefix row k must start with.
#
#     ["ball", "area"]  ->  row 2 must start with  b[2] a[2] = "le"
#
#   so the search never guesses -- it looks the prefix up and only tries words
#   that actually have it. a dict prefix -> [words] built once up front turns
#   that lookup into O(1).
#
#   NOTE !!! the prefix map is keyed on EVERY prefix length including '' (which
#            maps to all words), so the empty square picks its first row from the
#            same lookup as every other row -- no special case for k == 0.
#
# time = O(m * 26^L * L) worst case, m = len(words), L = word length
# space = O(m * L)
from collections import defaultdict


class Solution(object):
    def wordSquares(self, words):
        """
        :type words: List[str]
        :rtype: List[List[str]]
        """
        # edge
        if not words:
            return []

        L = len(words[0])

        prefix = defaultdict(list)
        for w in words:
            for i in range(L + 1):
                prefix[w[:i]].append(w)

        res = []

        def bt(sq):
            k = len(sq)
            if k == L:
                res.append(sq[:])
                return
            # column k of the rows placed so far == the prefix row k needs
            need = ''.join(row[k] for row in sq)
            for cand in prefix[need]:
                sq.append(cand)
                bt(sq)
                sq.pop()

        bt([])
        return res

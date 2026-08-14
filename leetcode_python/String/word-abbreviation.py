"""

527. Word Abbreviation
Hard

Given an array of distinct strings words, return the minimal possible abbreviations for every word.

The following are the rules for a string abbreviation:

1. The initial abbreviation for each word is: the first character, then the number of characters
   in between, followed by the last character.
2. If more than one word shares the same abbreviation, then perform the following operation:
   - Increase the prefix (characters in the first part) of each of their abbreviations by 1.
     - For example, say you start with the words ["abcdef","abndef"] both initially abbreviated
       as "a4f". Then, a sequence of operations would be
       ["a4f","a4f"] -> ["ab3f","ab3f"] -> ["abc2f","abn2f"].
   - This operation is repeated until every abbreviation is unique.
3. At the end, if an abbreviation did not make a word shorter, then keep it as the original word.

Example 1:

Input: words = ["like","god","internal","me","internet","interval","intension","face","intrusion"]
Output: ["l2e","god","internal","me","i6t","interval","inte4n","f2e","intr4n"]

Example 2:

Input: words = ["aa","aaa"]
Output: ["aa","aaa"]


Constraints:

1 <= words.length <= 400
2 <= words[i].length <= 400
words[i] consists of lowercase English letters.
All the strings of words are unique.

"""

# V0
# IDEA : GROUP + LONGEST COMMON PREFIX
#
#   two words can only collide when they have the SAME length, SAME first char and SAME last char
#   -> group by (len, first, last)
#   -> inside a group, sort the words: the longest common prefix of a word with ANY other word
#      of the group is reached with one of its 2 sorted neighbors
#   -> a word needs prefix length (lcp + 1) to break the tie with that neighbor
#   -> finally, keep the original word if the abbreviation is not shorter
#
# time = O(n * log(n) * L)  # n = len(words), L = max word length (sorting compares strings)
# space = O(n * L)
from collections import defaultdict
class Solution(object):
    def wordsAbbreviation(self, words):

        def abbrev(word, prefix_len):
            # word[:prefix_len] + (# of skipped chars) + last char
            cand = word[:prefix_len] + str(len(word) - prefix_len - 1) + word[-1]
            # rule 3 : only keep the abbreviation when it is actually shorter
            return cand if len(cand) < len(word) else word

        n = len(words)
        res = [None] * n

        groups = defaultdict(list)
        for i, w in enumerate(words):
            groups[(len(w), w[0], w[-1])].append(i)

        for idxs in groups.values():
            # sort by the word itself -> words sharing long prefixes become neighbors
            idxs.sort(key=lambda i: words[i])

            need = [1] * len(idxs)
            for a in range(len(idxs) - 1):
                w1, w2 = words[idxs[a]], words[idxs[a + 1]]
                lcp = 0
                while lcp < len(w1) and w1[lcp] == w2[lcp]:
                    lcp += 1
                # both words must keep 1 char MORE than the common part
                need[a] = max(need[a], lcp + 1)
                need[a + 1] = max(need[a + 1], lcp + 1)

            for a, i in enumerate(idxs):
                res[i] = abbrev(words[i], need[a])

        return res

"""

1768. Merge Strings Alternately
Easy

You are given two strings word1 and word2. Merge the strings by adding letters
in alternating order, starting with word1. If a string is longer than the
other, append the additional letters onto the end of the merged string.

Return the merged string.

Example 1:

Input: word1 = "abc", word2 = "pqr"
Output: "apbqcr"
Explanation: The merged string will be merged as so:
word1:  a   b   c
word2:    p   q   r
merged: a p b q c r

Example 2:

Input: word1 = "ab", word2 = "pqrs"
Output: "apbqrs"
Explanation: Notice that as word2 is longer, "rs" is appended to the end.
word1:  a   b
word2:    p   q   r   s
merged: a p b q   r   s

Example 3:

Input: word1 = "abcd", word2 = "pq"
Output: "apbqcd"
Explanation: Notice that as word1 is longer, "cd" is appended to the end.
word1:  a   b   c   d
word2:    p   q
merged: a p b q c   d

Constraints:

1 <= word1.length, word2.length <= 100
word1 and word2 consist of lowercase English letters.

"""

# V0
# IDEA : TWO POINTERS — TAKE ONE CHAR FROM EACH WHILE BOTH HAVE ONE, THEN THE TAIL
#
#   the pointers i (word1) and j (word2) always satisfy i == j inside the
#   main loop, because each round consumes exactly one char from each word.
#   once the shorter word runs out, alternation is impossible, so whatever is
#   left of the longer word is appended unchanged (at most one tail is non-empty).
#
#   e.g. "ab", "pqrs" -> a p b q | tail of word2 = "rs" -> "apbqrs"
#
# time = O(m + n), space = O(m + n)  (the output itself)
class Solution(object):
    def mergeAlternately(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: str
        """
        # collect chars in a list: repeated string += can be O(n^2)
        merged = []
        i = 0
        j = 0

        # alternate while BOTH words still have characters
        while i < len(word1) and j < len(word2):
            merged.append(word1[i])
            merged.append(word2[j])
            i += 1
            j += 1

        # NOTE !!! only one of these tails can be non-empty
        merged.append(word1[i:])
        merged.append(word2[j:])

        return "".join(merged)


# V1
# IDEA : SINGLE INDEX OVER THE LONGER LENGTH — APPEND word1[k] / word2[k] IF IT EXISTS
#
#   walk k from 0 to max(m, n) - 1; at each k, word1's char (if any) comes
#   before word2's char (if any). past the end of the shorter word, its check
#   simply fails, so the longer word's tail falls out of the same loop.
#
# time = O(m + n), space = O(m + n)
class Solution2(object):
    def mergeAlternately(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: str
        """
        merged = []
        longest = max(len(word1), len(word2))

        for k in range(longest):
            # word1 goes first at every position
            if k < len(word1):
                merged.append(word1[k])
            if k < len(word2):
                merged.append(word2[k])

        return "".join(merged)

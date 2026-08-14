"""

2047. Number of Valid Words in a Sentence
Easy

A sentence consists of lowercase letters ('a' to 'z'), digits ('0' to '9'), hyphens ('-'), punctuation marks ('!', '.', and ','), and spaces (' ') only. Each sentence can be broken down into one or more tokens separated by one or more spaces ' '.

A token is a valid word if all three of the following are true:

It only contains lowercase letters, hyphens, and/or punctuation (no digits).
There is at most one hyphen '-'. If present, it must be surrounded by lowercase characters ("a-b" is valid, but "-ab" and "ab-" are not valid).
There is at most one punctuation mark. If present, it must be at the end of the token ("ab,", "cd!", and "." are valid, but "a!b" and "c.," are not valid).

Examples of valid words include "a-b.", "afad", "ba-c", "a!", and "!".

Given a string sentence, return the number of valid words in sentence.


Example 1:

Input: sentence = "cat and  dog"
Output: 3
Explanation: The valid words in the sentence are "cat", "and", and "dog".

Example 2:

Input: sentence = "!this  1-s b8d!"
Output: 0
Explanation: There are no valid words in the sentence.
"!this" is invalid because it starts with a punctuation mark.
"1-s" and "b8d" are invalid because they contain digits.

Example 3:

Input: sentence = "alice and  bob are playing stone-game10"
Output: 5
Explanation: The valid words in the sentence are "alice", "and", "bob", "are", and "playing".
"stone-game10" is invalid because it contains digits.


Constraints:

1 <= sentence.length <= 1000
sentence only contains lowercase English letters, digits, ' ', '-', '!', '.', and ','.
sentence can be empty.
There are at most 1000 tokens in the sentence.

"""

# V0
# IDEA : TOKENIZE, THEN CHECK THE THREE RULES DIRECTLY
#
#   for each whitespace-separated token :
#     1. no digits at all
#     2. at most one '-', and it must have a lowercase letter on BOTH sides
#     3. at most one punctuation ('!', '.', ','), and if present it must be
#        the LAST character
#
#   NOTE : sentence.split() already collapses the runs of multiple spaces
#          that the statement warns about.
#
# time = O(n), space = O(n)
class Solution(object):
    def countValidWords(self, sentence):
        PUNCT = '!.,'

        def valid(tok):
            hyphens = 0
            for i, c in enumerate(tok):
                if c.isdigit():
                    return False
                if c == '-':
                    hyphens += 1
                    if hyphens > 1:
                        return False
                    if i == 0 or i == len(tok) - 1 \
                            or not tok[i - 1].islower() or not tok[i + 1].islower():
                        return False
                elif c in PUNCT:
                    if i != len(tok) - 1:
                        return False
            return True

        return sum(1 for tok in sentence.split() if valid(tok))

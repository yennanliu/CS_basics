"""

1832. Check if the Sentence Is Pangram
Easy

A pangram is a sentence where every letter of the English alphabet appears at least once.

Given a string sentence containing only lowercase English letters, return true if sentence is a pangram, or false otherwise.


Example 1:

Input: sentence = "thequickbrownfoxjumpsoverthelazydog"
Output: true
Explanation: sentence contains at least one of every letter of the English alphabet.

Example 2:

Input: sentence = "leetcode"
Output: false


Constraints:

1 <= sentence.length <= 1000
sentence consists of lowercase English letters.

"""

# V0
# IDEA : BITMASK OF SEEN LETTERS (26 bits, one per letter)
#
#   set bit (c - 'a') for every character; the sentence is a pangram iff all
#   26 bits are set, i.e. mask == (1 << 26) - 1.
#
#   NOTE : a set of chars works just as well (len(set(sentence)) == 26) since
#          the input is guaranteed lowercase-only, but the mask is O(1) space
#          and is the version that ports to any language.
#
# time = O(n), space = O(1)
class Solution(object):
    def checkIfPangram(self, sentence):
        mask = 0
        for c in sentence:
            mask |= 1 << (ord(c) - ord('a'))
        return mask == (1 << 26) - 1

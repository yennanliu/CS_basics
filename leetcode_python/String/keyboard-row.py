"""

500. Keyboard Row
Easy

Given an array of strings words, return the words that can be typed using letters of the alphabet
on only one row of American keyboard like the image below.

In the American keyboard:

- the first row consists of the characters "qwertyuiop",
- the second row consists of the characters "asdfghjkl", and
- the third row consists of the characters "zxcvbnm".

Example 1:

Input: words = ["Hello","Alaska","Dad","Peace"]
Output: ["Alaska","Dad"]
Explanation: Both "a" and "A" are in the 2nd row of the American keyboard due to case insensitivity.

Example 2:

Input: words = ["omk"]
Output: []

Example 3:

Input: words = ["adsdf","sfd"]
Output: ["adsdf","sfd"]


Constraints:

1 <= words.length <= 20
1 <= words[i].length <= 100
words[i] consists of English letters (both lowercase and uppercase).

"""

# V0
# IDEA : HASH TABLE (map every letter -> its keyboard row id)
#        -> a word is valid if all of its letters map to the same row id
# time = O(L)  # L = total number of characters over all words
# space = O(1)  # the letter -> row map has a fixed size (26)
class Solution(object):
    def findWords(self, words):
        rows = ["qwertyuiop", "asdfghjkl", "zxcvbnm"]

        row_of = {}
        for idx, row in enumerate(rows):
            for ch in row:
                row_of[ch] = idx

        res = []
        for w in words:
            # case insensitive
            ids = {row_of[ch] for ch in w.lower()}
            if len(ids) == 1:
                res.append(w)

        return res

"""

2129. Capitalize the Title
Easy

You are given a string title consisting of one or more words separated by a single space, where each word consists of English letters. Capitalize the string by changing the capitalization of each word such that:

If the length of the word is 1 or 2 letters, change all letters to lowercase.
Otherwise, change the first letter to uppercase and the remaining letters to lowercase.

Return the capitalized title.


Example 1:

Input: title = "capiTalIze tHe titLe"
Output: "Capitalize The Title"
Explanation:
Since all the words have a length of at least 3, the first letter of each word is uppercase, and the remaining letters are lowercase.

Example 2:

Input: title = "First leTTeR of EACH Word"
Output: "First Letter of Each Word"
Explanation:
The word "of" has length 2, so it is all lowercase.
The remaining words have a length of at least 3, so the first letter of each remaining word is uppercase, and the remaining letters are lowercase.

Example 3:

Input: title = "i lOve leetcode"
Output: "i Love Leetcode"
Explanation:
The word "i" has length 1, so it is lowercase.
The word "lOve" has length 4, so it is capitalized.
The word "leetcode" has length 8, so it is capitalized.


Constraints:

1 <= title.length <= 100
title consists of words separated by a single space without any leading or trailing spaces.
Each word consists of uppercase and lowercase English letters and is non-empty.

"""

# V0
# IDEA : SPLIT, APPLY THE LENGTH RULE PER WORD, JOIN BACK
#
#   words of length <= 2 -> lower(); everything else -> capitalize(), which
#   in python already means "first letter upper, rest lower" — exactly the
#   rule stated.
#
#   NOTE : the input has single spaces and no leading/trailing space, so
#          split()/join() round-trips faithfully.
#
# time = O(n), space = O(n)
class Solution(object):
    def capitalizeTitle(self, title):
        return ' '.join(w.lower() if len(w) <= 2 else w.capitalize()
                        for w in title.split(' '))

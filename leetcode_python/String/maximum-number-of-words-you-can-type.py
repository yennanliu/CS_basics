"""

1935. Maximum Number of Words You Can Type
Easy

There is a malfunctioning keyboard where some letter keys do not work. All other keys on the keyboard work properly.

Given a string text of words separated by a single space (no leading or trailing spaces) and a string brokenLetters of all distinct letter keys that are broken, return the number of words in text you can fully type using this keyboard.


Example 1:

Input: text = "hello world", brokenLetters = "ad"
Output: 1
Explanation: We cannot type "world" because the 'd' key is broken.

Example 2:

Input: text = "leet code", brokenLetters = "lt"
Output: 1
Explanation: We cannot type "leet" because the 'l' and 't' keys are broken.

Example 3:

Input: text = "leet code", brokenLetters = "e"
Output: 0
Explanation: We cannot type either word because the 'e' key is broken.


Constraints:

1 <= text.length <= 10^4
0 <= brokenLetters.length <= 26
text consists of words separated by a single space without any leading or trailing spaces.
Each word only consists of lowercase English letters.
brokenLetters consists of distinct lowercase English letters.

"""

# V0
# IDEA : HASH SET OF BROKEN KEYS + SET DISJOINTNESS PER WORD
#
#   put the broken letters in a set, split the text on spaces, and keep a word
#   iff none of its characters is broken.
#
#   NOTE : set(word).isdisjoint(broken) short-circuits and is O(len(word)).
#
# time = O(n), space = O(26)
class Solution(object):
    def canBeTypedWords(self, text, brokenLetters):
        broken = set(brokenLetters)
        res = 0
        for w in text.split():
            if broken.isdisjoint(w):
                res += 1
        return res

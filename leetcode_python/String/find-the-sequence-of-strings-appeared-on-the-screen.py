"""

3324. Find the Sequence of Strings Appeared on the Screen
Medium

You are given a string target.

Alice is going to type target on her computer using a special keyboard that has only two keys:

Key 1 appends the character "a" to the string on the screen.
Key 2 changes the last character of the string on the screen to its next character in the English alphabet. For example, "c" changes to "d" and "z" changes to "a".

Note that initially there is an empty string "" on the screen, so she can only press key 1.

Return a list of all strings that appear on the screen as Alice types target, in the order they appear, using the minimum key presses.


Example 1:

Input: target = "abc"
Output: ["a","aa","ab","aba","abb","abc"]
Explanation:
The sequence of key presses done by Alice are:
Press key 1, and the string on the screen becomes "a".
Press key 1, and the string on the screen becomes "aa".
Press key 2, and the string on the screen becomes "ab".
Press key 1, and the string on the screen becomes "aba".
Press key 2, and the string on the screen becomes "abb".
Press key 2, and the string on the screen becomes "abc".

Example 2:

Input: target = "he"
Output: ["a","b","c","d","e","f","g","h","ha","hb","hc","hd","he"]


Constraints:

1 <= target.length <= 400
target consists only of lowercase English letters.

"""

# V0
# IDEA : BUILD LEFT TO RIGHT — EACH CHARACTER COSTS ONE APPEND PLUS SOME BUMPS
#
#   the minimum-press route is forced : once a prefix is finished it is never
#   revisited, so for each target character Alice presses key 1 (giving 'a')
#   and then key 2 until the letter matches.
#
#   every press changes the screen, so the answer records the prefix built so
#   far followed by each intermediate letter from 'a' up to target[i].
#
# time = O(n^2) for the strings produced, space = O(n^2)
class Solution(object):
    def stringSequence(self, target):
        res = []
        prefix = []
        for ch in target:
            for c in range(ord('a'), ord(ch) + 1):
                res.append(''.join(prefix) + chr(c))
            prefix.append(ch)
        return res

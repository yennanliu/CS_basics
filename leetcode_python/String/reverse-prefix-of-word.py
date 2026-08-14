"""

2000. Reverse Prefix of Word
Easy

Given a 0-indexed string word and a character ch, reverse the segment of word that starts at index 0 and ends at the index of the first occurrence of ch (inclusive). If the character ch does not exist in word, do nothing.

For example, if word = "abcdefd" and ch = "d", then you should reverse the segment that starts at 0 and ends at 3 (inclusive). The resulting string will be "dcbaefd".

Return the resulting string.


Example 1:

Input: word = "abcdefd", ch = "d"
Output: "dcbaefd"
Explanation: The first occurrence of "d" is at index 3.
Reverse the part of word from 0 to 3 (inclusive), the resulting string is "dcbaefd".

Example 2:

Input: word = "xyxzxe", ch = "z"
Output: "zxyxxe"
Explanation: The first and only occurrence of "z" is at index 3.
Reverse the part of word from 0 to 3 (inclusive), the resulting string is "zxyxxe".

Example 3:

Input: word = "abcd", ch = "z"
Output: "abcd"
Explanation: "z" does not exist in word.
You should not do any reverse operation, the resulting string is "abcd".


Constraints:

1 <= word.length <= 250
word consists of lowercase English letters.
ch is a lowercase English letter.

"""

# V0
# IDEA : FIND THE FIRST OCCURRENCE, THEN SLICE-REVERSE
#
#   i = word.find(ch)
#     - i == -1 -> ch is absent, return word untouched
#     - else    -> word[i::-1] is word[0..i] reversed, then glue word[i+1:]
#
#   NOTE : word[i::-1] walks backwards from i down to index 0 inclusive,
#          which is exactly the prefix we must flip.
#
# time = O(n), space = O(n)
class Solution(object):
    def reversePrefix(self, word, ch):
        i = word.find(ch)
        if i == -1:
            return word
        return word[i::-1] + word[i + 1:]

"""

3582. Generate Tag for Video Caption
Easy

You are given a string caption representing the caption for a video.

The following actions must be performed in order to generate a valid tag for the video:

Combine all words in the string into a single camelCase string prefixed with '#'. A camelCase string is one where the first letter of all words except the first one is capitalized. All the characters in the first word must be in lower case.
Remove all characters that are not an English letter, except the leading '#'.
Truncate the result to a maximum of 100 characters.

Return the tag after performing the actions on caption.


Example 1:

Input: caption = "Leetcode daily streak achieved"
Output: "#leetcodeDailyStreakAchieved"
Explanation:
The first word is "leetcode" in lowercase, and the remaining words are capitalized: "Daily", "Streak" and "Achieved".

Example 2:

Input: caption = "can I Go There"
Output: "#canIGoThere"
Explanation:
The first word is "can" in lowercase, and the remaining words are capitalized: "I", "Go" and "There".

Example 3:

Input: caption = "hhhh...hhhh"   (a single word of 101 'h' characters)
Output: "#hhhh...hhhh"           ('#' followed by 99 'h' characters)
Explanation:
Since the first word has length 101, we need to truncate the last two
letters from the word.


Constraints:

1 <= caption.length <= 150
caption consists only of English letters and ' '.

"""

# V0
# IDEA : SPLIT ON WHITESPACE, THEN RE-CASE EACH WORD BY ITS POSITION
#
#   str.split() with no argument already collapses runs of spaces and drops
#   leading/trailing ones, so an empty word never reaches the join step.
#   the first word is fully lowercased; every later word is
#   "upper first char + lowercased rest".
#
#   the final slice enforces the 100 character cap *including* the '#'.
#
# time = O(n), space = O(n)
class Solution(object):
    def generateTag(self, caption):
        words = caption.split()
        if not words:
            return "#"
        out = ["#", words[0].lower()]
        for w in words[1:]:
            out.append(w[0].upper() + w[1:].lower())
        return "".join(out)[:100]

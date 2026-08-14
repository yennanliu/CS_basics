"""

2207. Maximize Number of Subsequences in a String
Medium

You are given a 0-indexed string text and another 0-indexed string pattern of length 2, both of which consist of only lowercase English letters.

You can add either pattern[0] or pattern[1] anywhere in text exactly once. Note that the character can be added even at the beginning or at the end of text.

Return the maximum number of times pattern can occur as a subsequence of the modified text.

A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.


Example 1:

Input: text = "abdcdbc", pattern = "ac"
Output: 4
Explanation:
If we add pattern[0] = 'a' in between text[1] and text[2], we get "abadcdbc". Now, the number of times "ac" occurs as a subsequence is 4.
Some other strings which have 4 subsequences "ac" after adding a character to text are "aabdcdbc" and "abdacdbc".
However, strings such as "abdcadbc", "abdccdbc", and "abdcdbcc", although obtainable, have only 3 subsequences "ac" and thus are suboptimal.
It can be shown that it is not possible to get more than 4 subsequences "ac" by adding only one character.

Example 2:

Input: text = "aabb", pattern = "ab"
Output: 6
Explanation:
Some of the strings which can be obtained from text and have 6 subsequences "ab" are "aaabb", "aaabb", and "aabbb".


Constraints:

1 <= text.length <= 10^5
pattern.length == 2
text and pattern consist only of lowercase English letters.

"""

# V0
# IDEA : COUNT PAIRS IN ONE SWEEP, THEN CHOOSE THE BETTER SINGLE INSERTION
#
#   pattern is 2 characters, say (a, b). scanning left to right and keeping
#   `count_a` = how many a's seen so far, every b encountered contributes
#   `count_a` new subsequences. that gives the base count.
#
#   the extra character is best placed at an END :
#     inserting `a` at the very FRONT pairs it with every b in the text
#         -> gains count_b
#     inserting `b` at the very END pairs it with every a
#         -> gains count_a
#   take the larger gain.
#
#   NOTE : when a == b the same sweep still works — count_a is incremented
#          before... careful, the order matters, so the code counts the pair
#          contribution FIRST and then increments, matching "strictly before".
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumSubsequenceCount(self, text, pattern):
        a, b = pattern[0], pattern[1]
        count_a = count_b = 0
        res = 0
        for c in text:
            if c == b:
                res += count_a
                count_b += 1
            if c == a:
                count_a += 1
        return res + max(count_a, count_b)

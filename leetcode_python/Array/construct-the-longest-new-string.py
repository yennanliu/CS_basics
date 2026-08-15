"""

2745. Construct the Longest New String
Medium

You are given three integers x, y, and z.

You have x strings equal to "AA", y strings equal to "BB", and z strings equal to "AB". You want to choose some (possibly all or none) of these strings and concatenate them in some order to form a new string. This new string must not contain "AAA" or "BBB" as a substring.

Return the maximum possible length of the new string.

A substring is a contiguous non-empty sequence of characters within a string.


Example 1:

Input: x = 2, y = 5, z = 1
Output: 12
Explanation: We can concatenate the strings "BB", "AA", "BB", "AA", "BB", and "AB" in that order. Then, our new string is "BBAABBAABBAB".
That string has length 12, and we can show that it is impossible to construct a string of longer length.

Example 2:

Input: x = 3, y = 2, z = 2
Output: 14
Explanation: We can concatenate the strings "AB", "AB", "AA", "BB", "AA", "BB", and "AA" in that order. Then, our new string is "ABABAABBAABBAA".
That string has length 14, and we can show that it is impossible to construct a string of longer length.


Constraints:

1 <= x, y, z <= 50

"""

# V0
# IDEA : GREEDY / CASE ANALYSIS ON THE "AA" vs "BB" ALTERNATION
#
#   Think of each block by the letter it starts and ends with:
#       "AA" -> A..A, "BB" -> B..B, "AB" -> A..B
#   Concatenating two blocks that touch with the same letter on both sides
#   makes 3+ of that letter in a row ("AAA" / "BBB"). So in the final string
#   the "AA" and "BB" blocks MUST strictly alternate: AA BB AA BB ...
#
#   Consequences :
#     - if x == y  : use all of them, "AABBAABB..." works.
#     - if x != y  : the alternation caps the bigger pile at min(x, y) + 1
#                    (start and end with the majority letter), so we place
#                    min(x, y) of each plus one extra of the majority.
#
#   Every "AB" block is A..B, so a run of "AB"s ("ABABAB...") is always safe,
#   and it can be prefixed to a chain that STARTS with A (because the run
#   ends in B) — that is, all z of them are always usable, for free.
#
#   NOTE : the whole layout ends up as  (AB)*z + [AA BB AA ...]  when the
#          chain starts with "AA", or  [BB AA ...] + (AB)*z  when it starts
#          with "BB". Either way no "AB" is ever wasted.
#
#   NOTE : each block contributes 2 characters, hence the final "* 2".
#
# time = O(1), space = O(1)
class Solution(object):
    def longestString(self, x, y, z):
        if x == y:
            blocks = x + y + z
        else:
            blocks = 2 * min(x, y) + 1 + z
        return blocks * 2

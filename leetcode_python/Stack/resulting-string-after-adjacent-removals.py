"""

3561. Resulting String After Adjacent Removals
Medium

You are given a string s consisting of lowercase English letters.

You must repeatedly perform the following operation while the string s has at
least two consecutive characters:

Remove the leftmost pair of adjacent characters in the string that are
consecutive in the alphabet, in either order (e.g., 'a' and 'b', or 'b' and
'a').

Shift the remaining characters to the left to fill the gap.

Return the resulting string after no more operations can be performed.

Note: Consider the alphabet as circular, thus 'a' and 'z' are consecutive.

Example 1:

Input: s = "abc"

Output: "c"

Explanation:

Remove "ab" from the string, leaving "c" as the remaining string.

No further operations are possible. Thus, the resulting string after all
possible removals is "c".

Example 2:

Input: s = "adcb"

Output: ""

Explanation:

Remove "dc" from the string, leaving "ab" as the remaining string.

Remove "ab" from the string, leaving "" as the remaining string.

No further operations are possible. Thus, the resulting string after all
possible removals is "".

Example 3:

Input: s = "zadb"

Output: "db"

Explanation:

Remove "za" from the string, leaving "db" as the remaining string.

No further operations are possible. Thus, the resulting string after all
possible removals is "db".

Constraints:

1 <= s.length <= 10^5

s consists only of lowercase English letters.

"""

# V0
# IDEA : STACK — REPLAY THE REQUIRED LEFTMOST REMOVALS IN ONE PASS
#
#   the reduction is NOT order independent, so the "leftmost first" rule has to
#   be honoured rather than argued away: "abc" reduces to "c" if "ab" goes
#   first, but to "a" if "bc" does.  the statement pins the order down, and the
#   answer is whatever that specific order produces.
#
#   a left-to-right stack pass reproduces exactly that order.  the stack always
#   holds the fully reduced form of the prefix read so far, so nothing inside it
#   can still cancel and its top is the only character a new one could meet.
#   when the next character is alphabet-adjacent (mod 26) to that top, that pair
#   is the leftmost removable pair at this point, so cancelling it is forced,
#   not a choice; popping it exposes an older character that may in turn cancel
#   with a later one.
#
# time = O(n), space = O(n)
class Solution(object):
    def resultingString(self, s):
        stack = []
        for ch in s:
            if stack:
                d = abs(ord(stack[-1]) - ord(ch))
                if d == 1 or d == 25:
                    stack.pop()
                    continue
            stack.append(ch)
        return "".join(stack)

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
# IDEA : STACK — THE FINAL STRING DOES NOT DEPEND ON THE REMOVAL ORDER
#
#   removals are local: cancelling a pair only ever brings its two outer
#   neighbours together.  that makes the reduction confluent, so the "leftmost
#   first" rule in the statement does not change the result, and a single
#   left-to-right stack pass suffices.
#
#   push each character; if it is alphabet-adjacent (mod 26) to the current top,
#   the two annihilate and the top is popped, exposing an older character that
#   may in turn cancel with a later one.
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

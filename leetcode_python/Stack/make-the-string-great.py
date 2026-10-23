"""

1544. Make The String Great
Easy

Given a string s of lower and upper case English letters.

A good string is a string which doesn't have two adjacent characters s[i] and
s[i + 1] where:

0 <= i <= s.length - 2
s[i] is a lower-case letter and s[i + 1] is the same letter but in upper-case
or vice-versa.

To make the string good, you can choose two adjacent characters that make the
string bad and remove them. You can keep doing this until the string becomes
good.

Return the string after making it good. The answer is guaranteed to be unique
under the given constraints.

Notice that an empty string is also good.


Example 1:

Input: s = "leEeetcode"
Output: "leetcode"
Explanation: In the first step, either you choose i = 1 or i = 2, both will
result "leEeetcode" to be reduced to "leetcode".

Example 2:

Input: s = "abBAcC"
Output: ""
Explanation: We have many possible scenarios, and all lead to the same answer.
For example:
"abBAcC" --> "aAcC" --> "cC" --> ""
"abBAcC" --> "abBA" --> "aA" --> ""

Example 3:

Input: s = "s"
Output: "s"


Constraints:

1 <= s.length <= 100
s contains only lower and upper case English letters.

"""

# V0
# IDEA : STACK — CANCEL THE NEW CHAR AGAINST THE TOP (same shape as LC 1047)
#
#   the stack always holds a GOOD string: no adjacent bad pair inside it.
#   a new char can only form a bad pair with the char right before it, i.e.
#   the stack top. if it does, pop the top (both vanish) — and the char that
#   is now on top is exposed to the NEXT char, which is how removals cascade:
#
#   e.g. "abBA" -> push a, push b, B cancels b, A cancels a -> ""
#
#   (simulating "scan, remove one pair, rescan" also works, but is O(n^2))
#
# time = O(n), space = O(n)
class Solution(object):
    def makeGood(self, s):
        """
        :type s: str
        :rtype: str
        """
        stack = []
        for ch in s:
            # NOTE !!! a bad pair is the SAME letter in DIFFERENT case:
            #          "aa" is fine, "aA" / "Aa" is bad
            if stack and stack[-1] != ch and stack[-1].lower() == ch.lower():
                stack.pop()
            else:
                stack.append(ch)
        return ''.join(stack)

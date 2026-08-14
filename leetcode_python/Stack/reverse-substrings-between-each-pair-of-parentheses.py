"""

1190. Reverse Substrings Between Each Pair of Parentheses
Medium

You are given a string s that consists of lower case English letters and brackets.

Reverse the strings in each pair of matching parentheses, starting from the
innermost one.

Your result should not contain any brackets.


Example 1:

Input: s = "(abcd)"
Output: "dcba"

Example 2:

Input: s = "(u(love)i)"
Output: "iloveu"
Explanation: The substring "love" is reversed first, then the whole string.

Example 3:

Input: s = "(ed(et(oc))el)"
Output: "leetcode"
Explanation: First, we reverse the substring "oc", then "etco", and finally,
the whole string.


Constraints:

1 <= s.length <= 2000
s only contains lower case English characters and parentheses.
It is guaranteed that all parentheses are balanced.

"""

# V0
# IDEA: STACK of BUFFERS (simulation)
# on '(' -> push a new buffer
# on ')' -> pop buffer, reverse it, append to the buffer below
# time = O(n^2)
# space = O(n)
class Solution(object):
    def reverseParentheses(self, s):
        # stack[0] is the "outermost" buffer
        stack = [[]]

        for ch in s:
            if ch == '(':
                stack.append([])
            elif ch == ')':
                """
                NOTE !!!

                -> pop the innermost buffer, REVERSE it,
                   then merge it back into its parent buffer
                """
                top = stack.pop()
                top.reverse()
                stack[-1].extend(top)
            else:
                stack[-1].append(ch)

        return "".join(stack[0])


# V1
# IDEA: "WORMHOLE" - precompute bracket pairs, then walk with a flipping direction
# hitting a bracket teleports to its partner and flips the walking direction,
# so every char is visited exactly once
# time = O(n)
# space = O(n)
class Solution(object):
    def reverseParentheses(self, s):
        n = len(s)

        # pair[i] = index of the matching bracket of s[i]
        pair = [0] * n
        stack = []
        for i, ch in enumerate(s):
            if ch == '(':
                stack.append(i)
            elif ch == ')':
                j = stack.pop()
                pair[i] = j
                pair[j] = i

        res = []
        i, d = 0, 1
        while i < n:
            if s[i] == '(' or s[i] == ')':
                i = pair[i]
                d = -d
            else:
                res.append(s[i])
            i += d

        return "".join(res)

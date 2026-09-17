"""

301. Remove Invalid Parentheses
Hard

Given a string s that contains parentheses and letters, remove the minimum number
of invalid parentheses to make the input string valid.

Return a list of unique strings that are valid with the minimum number of removals.
You may return the answer in any order.


Example 1:

Input: s = "()())()"
Output: ["(())()","()()()"]

Example 2:

Input: s = "(a)())()"
Output: ["(a())()","(a)()()"]

Example 3:

Input: s = ")("
Output: [""]


Constraints:

1 <= s.length <= 25
s consists of lowercase English letters and parentheses '(' and ')'.
There will be at most 20 parentheses in s.

"""

# V0
# IDEA : BFS LEVEL BY LEVEL (the first level that has a valid string IS the answer)
#
#   "minimum number of removals" is a shortest-path question, so BFS answers it
#   without ever having to reason about WHICH parenthesis to drop : level k holds
#   every string reachable by deleting exactly k characters.
#
#   the moment a level contains at least one valid string we return that whole
#   level and stop -- anything deeper would delete more than the minimum.
#
#     ")("  ->  level 0 : ")("        invalid
#              level 1 : ")", "("     invalid
#              level 2 : ""           valid -> [""]
#
#   NOTE !!! the level is a set, not a list -- deleting either '(' of "((" gives
#            the same string, and without dedupe the frontier blows up (and the
#            answer would contain duplicates).
#
# time = O(2^n * n), n = len(s), space = O(2^n * n)
class Solution(object):
    def removeInvalidParentheses(self, s):
        """
        :type s: str
        :rtype: List[str]
        """

        def valid(t):
            cnt = 0
            for c in t:
                if c == '(':
                    cnt += 1
                elif c == ')':
                    cnt -= 1
                    # a ')' with nothing open can never be repaired later
                    if cnt < 0:
                        return False
            return cnt == 0

        level = {s}
        while True:
            found = [t for t in level if valid(t)]
            if found:
                return found

            nxt = set()
            for t in level:
                for i, c in enumerate(t):
                    # only removing a parenthesis can change validity
                    if c in '()':
                        nxt.add(t[:i] + t[i + 1:])
            level = nxt

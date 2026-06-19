# https://leetcode.com/problems/remove-duplicate-letters/description/

"""

316. Remove Duplicate Letters
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given a string s, remove duplicate letters so that every letter appears once and only once. You must make sure your result is the smallest in lexicographical order among all possible results.

 

Example 1:

Input: s = "bcabc"
Output: "abc"
Example 2:

Input: s = "cbacdcbc"
Output: "acdb"
 

Constraints:

1 <= s.length <= 104
s consists of lowercase English letters.
 

Note: This question is the same as 1081: https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/

"""

# V0
# IDEA: MONO STACK (GPT)
class Solution(object):
    def removeDuplicateLetters(self, s):
        """
        :type s: str
        :rtype: str
        """

        if not s:
            return ""

        # last occurrence index of each character
        last_idx = {}

        for i in range(len(s)):
            last_idx[s[i]] = i

        # monotonic stack
        res = []

        """
        NOTE !!!

        use `seen` to avoid duplicated append
        """
        # characters already in stack
        seen = set()

        for i in range(len(s)):
            ch = s[i]

            # skip duplicates
            if ch in seen:
                continue

            # pop larger chars if they appear later again
            while (res and
                   ch < res[-1] and
                   last_idx[res[-1]] > i):
                removed = res.pop()
                seen.remove(removed)

            res.append(ch)
            seen.add(ch)

        return "".join(res)


# V1


# V2
# IDEA: STACK
# https://leetcode.com/problems/remove-duplicate-letters/solutions/1859515/python-on-beats-98-stack-detailed-explan-11pb/
class Solution:
    def removeDuplicateLetters(self, s):
        
        last_occ = {}
        stack = []
        visited = set()

        for i in range(len(s)):
            last_occ[s[i]] = i

        for i in range(len(s)):

            if s[i] not in visited:
                while (stack and stack[-1] > s[i] and last_occ[stack[-1]] > i):
                    visited.remove(stack.pop())

                stack.append(s[i])
                visited.add(s[i])

        return ''.join(stack)


# V3


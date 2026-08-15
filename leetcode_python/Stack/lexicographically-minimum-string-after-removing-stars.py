"""

3170. Lexicographically Minimum String After Removing Stars
Medium

You are given a string s. It may contain any number of '*' characters. Your task is to remove all '*' characters.

While there is a '*', do the following operation:

Delete the leftmost '*' and the smallest non-'*' character to its left. If there are several smallest characters, you can delete any of them.

Return the lexicographically smallest resulting string after removing all '*' characters.


Example 1:

Input: s = "aaba*"
Output: "aab"
Explanation:
We should delete one of the 'b' characters. Since the string is lexicographically smallest after deleting the 'b' at index 3, the answer is "aab".

Example 2:

Input: s = "abc"
Output: "abc"
Explanation:
There is no '*' in the string.


Constraints:

1 <= s.length <= 10^5
s consists only of lowercase English letters and '*'.
The input is generated such that it is possible to delete all '*' characters.

"""

# V0
# IDEA : ONE STACK OF POSITIONS PER LETTER — DELETE THE *LATEST* SMALLEST
#
#   each '*' must remove the smallest surviving letter to its left, and when
#   several copies of that letter exist the choice is ours. removing the
#   RIGHTMOST copy is best : it leaves the earlier copies in place, and a
#   smaller letter earlier in the string is exactly what lexicographic order
#   rewards.
#
#   so keep 26 stacks of indices, one per letter. a '*' pops from the lowest
#   non-empty stack, which yields that letter's latest position in O(1).
#   marking deletions and joining what survives finishes the job.
#
# time = O(26 * n) worst case, space = O(n)
class Solution(object):
    def clearStars(self, s):
        stacks = [[] for _ in range(26)]
        removed = [False] * len(s)

        for i, ch in enumerate(s):
            if ch == '*':
                removed[i] = True
                for c in range(26):
                    if stacks[c]:
                        removed[stacks[c].pop()] = True
                        break
            else:
                stacks[ord(ch) - 97].append(i)

        return ''.join(ch for i, ch in enumerate(s) if not removed[i])

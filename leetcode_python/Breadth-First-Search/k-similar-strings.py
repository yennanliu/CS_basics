"""

854. K-Similar Strings
Hard

Strings s1 and s2 are k-similar (for some non-negative integer k) if we can swap
the positions of two letters in s1 exactly k times so that the resulting string
equals s2.

Given two anagrams s1 and s2, return the smallest k for which s1 and s2 are k-similar.


Example 1:

Input: s1 = "ab", s2 = "ba"
Output: 1
Explanation: The two string are 1-similar because we can use one swap to change
s1 to s2: "ab" --> "ba".

Example 2:

Input: s1 = "abc", s2 = "bca"
Output: 2
Explanation: The two strings are 2-similar because we can use two swaps to change
s1 to s2: "abc" --> "bac" --> "bca".


Constraints:

1 <= s1.length <= 20
s2.length == s1.length
s1 and s2 contain only lowercase letters from the set {'a','b','c','d','e','f'}.
s2 is an anagram of s1.

"""

# V0
# IDEA : BFS with GREEDY PRUNING
#
#   BFS over strings, one swap = one edge, so the first time we reach s2 the
#   level is the minimum number of swaps.
#
#   The naive branching factor (all C(n,2) swaps) explodes. Two prunings make
#   it tractable and stay optimal:
#
#     1) Always fix the LEFTMOST mismatching index i.
#        Some swap must eventually place s2[i] at position i, and doing it now
#        never costs more, so we only branch on swaps touching i.
#
#     2) Only swap in a j with cur[j] == s2[i] AND cur[j] != s2[j].
#        Moving a character that is already in its correct place would just
#        have to be undone later.
#
# time  = O(n * n! / ...) in theory, but pruning keeps it small for n <= 20
# space = O(number of visited states)
from collections import deque
class Solution(object):
    def kSimilarity(self, s1, s2):
        if s1 == s2:
            return 0

        n = len(s1)
        queue = deque([s1])
        visited = {s1}
        steps = 0

        while queue:
            steps += 1
            for _ in range(len(queue)):
                cur = queue.popleft()

                # leftmost position that is still wrong
                i = 0
                while cur[i] == s2[i]:
                    i += 1

                for j in range(i + 1, n):
                    # bring the right character to i,
                    # and don't disturb a character already in place
                    if cur[j] == s2[i] and cur[j] != s2[j]:
                        nxt = list(cur)
                        nxt[i], nxt[j] = nxt[j], nxt[i]
                        nxt = "".join(nxt)
                        if nxt == s2:
                            return steps
                        if nxt not in visited:
                            visited.add(nxt)
                            queue.append(nxt)

        return -1

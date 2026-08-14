"""

936. Stamping The Sequence
Hard

You are given two strings stamp and target. Initially, there is a string s of length
target.length with all s[i] == '?'.

In one turn, you can place stamp over s and replace every letter in the s with the
corresponding letter from stamp.

For example, if stamp = "abc" and target = "abcba", then s is "?????" initially.
In one turn you can:
    place stamp at index 0 of s to obtain "abc??",
    place stamp at index 1 of s to obtain "?abc?", or
    place stamp at index 2 of s to obtain "??abc".
Note that stamp must be fully contained in the boundaries of s in order to stamp
(i.e., you cannot place stamp at index 3 of s).

We want to convert s to target using at most 10 * target.length turns.

Return an array of the index of the left-most letter being stamped at each turn.
If we cannot obtain target from s within 10 * target.length turns, return an empty array.


Example 1:

Input: stamp = "abc", target = "ababc"
Output: [0,2]
Explanation: Initially s = "?????".
- Place stamp at index 0 to get "abc??".
- Place stamp at index 2 to get "ababc".
[1,0,2] would also be accepted as an answer, as well as some other answers.

Example 2:

Input: stamp = "abca", target = "aabcaca"
Output: [3,0,1]
Explanation: Initially s = "???????".
- Place stamp at index 3 to get "???abca".
- Place stamp at index 0 to get "abcabca".
- Place stamp at index 1 to get "aabcaca".


Constraints:

1 <= stamp.length <= target.length <= 1000
stamp and target consist of lowercase English letters.

"""

# V0
# IDEA : WORK BACKWARDS (greedy un-stamping) + TOPOLOGICAL SORT
"""
 Stamping forward is hard because later stamps overwrite earlier ones.
 So run it in REVERSE: start from `target` and peel stamps off, turning the
 covered positions into wildcards. The reversed order of the peels is the answer.

 Model each of the (n - m + 1) windows as a node:

    indeg[i] = how many positions inside window i still MISMATCH the stamp.
               indeg[i] == 0  ->  window i matches exactly, we can un-stamp it now.
    g[p]     = list of windows that are blocked by position p (mismatch at p).

 When we un-stamp window i, every position it covers becomes a wildcard '?'.
 A wildcard matches anything, so each window that was blocked by that position
 loses one mismatch -> decrement its indeg, and enqueue it when it hits 0.

 If every position ends up covered, reverse the peel order and return it.
"""
# time = O(n * (n - m + 1))
# space = O(n * (n - m + 1))
from collections import deque
class Solution(object):
    def movesToStamp(self, stamp, target):
        m, n = len(stamp), len(target)

        indeg = [m] * (n - m + 1)   # mismatches remaining per window
        g = [[] for _ in range(n)]  # position -> windows blocked by it
        q = deque()

        for i in range(n - m + 1):
            for j, c in enumerate(stamp):
                if target[i + j] == c:
                    indeg[i] -= 1
                    if indeg[i] == 0:
                        q.append(i)
                else:
                    g[i + j].append(i)

        res = []
        covered = [False] * n

        while q:
            i = q.popleft()
            res.append(i)
            for j in range(m):
                p = i + j
                if covered[p]:
                    continue
                # position p becomes a wildcard -> it no longer blocks anyone
                covered[p] = True
                for w in g[p]:
                    indeg[w] -= 1
                    if indeg[w] == 0:
                        q.append(w)

        # we peeled stamps off, so the stamping order is the reverse
        return res[::-1] if all(covered) else []
